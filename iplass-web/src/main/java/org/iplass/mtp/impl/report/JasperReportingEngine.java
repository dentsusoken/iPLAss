/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.mtp.impl.report;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.template.report.MetaReportParamMap;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.definition.TemplateDefinitionManager;
import org.iplass.mtp.web.template.report.ReportOutputException;
import org.iplass.mtp.web.template.report.definition.JasperReportType;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;
import org.iplass.mtp.web.template.report.definition.OutputFileType;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;


public class JasperReportingEngine implements ReportingEngine{

	private static Logger logger = LoggerFactory.getLogger(JasperReportingEngine.class);

	private String[] supportFiles;

	private static final String SESSION_STR = "session";
	private static final String REQUEST_STR = "request";
	private static final String PREFIX_REQUEST = REQUEST_STR +".";
	private static final String PREFIX_SESSION = SESSION_STR +".";

	@Override
	public ReportingOutputModel createOutputModel(byte[] binary, String type, String extension) throws Exception{
		return new JasperReportingOutputModel(binary, type, extension);
	}

	@Override
	public boolean isSupport(String type) {
		boolean isSupport = false;
		for(String supportFile : this.supportFiles){
			if(supportFile.equals(type)){
				isSupport = true;
			}
		}
		return isSupport;
	}

	@Override
	public void exportReport( WebRequestStack context, ReportingOutputModel model ) throws Exception{

		RequestContext request = context.getRequestContext();

		JasperReportingOutputModel jasperModel = (JasperReportingOutputModel) model;

		OutputFileType outputType = OutputFileType.convertOutputFileType(jasperModel.getType());

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(REQUEST_STR, request);
		params.put(SESSION_STR, request.getSession());

		MetaReportParamMap[] paramMaps = jasperModel.getMaps();
		if (paramMaps != null) {
			for (MetaReportParamMap p: paramMaps) {

				//TODO listは廃止する。
				//TODO Command内でAttributeにDataSourceをセットするのと一緒。(色々なタイプが設定できるので、自由に)
				//TODO 合わせてStringもおかしいので改善(必要なのはreport(サブレポート)だけ)

				if ("report".equals(p.getParamType())) {

					TemplateDefinitionManager tdm = ManagerLocator.getInstance().getManager(TemplateDefinitionManager.class);
					ReportTemplateDefinition td = (ReportTemplateDefinition) tdm.get(p.getMapFrom());

					InputStream inputStream = new ByteArrayInputStream(td.getBinary());
					JasperReport temp = JasperCompileManager.compileReport(inputStream);
					params.put(p.getName(), temp);

					// 多言語処理
					String lang = ExecuteContext.getCurrentContext().getLanguage();
					for (LocalizedReportDefinition lrd : td.getLocalizedReportList()) {
						if (lang.equals(lrd.getLocaleName())) {
							InputStream localInputStream = new ByteArrayInputStream(lrd.getBinary());
							JasperReport localTemp = JasperCompileManager.compileReport(localInputStream);

							if (localTemp != null) {
								params.put(p.getName(), localTemp);
							}
						}
					}

				} else if ("list".equals(p.getParamType())) {
					params.put(p.getName(), new JRBeanCollectionDataSource((Collection<?>)getAttribute(request, p.getMapFrom())));
				} else {
					//通常のパラメータ
					params.put(p.getName(), getAttribute(request, p.getMapFrom()));
				}
			}
		}

		JRDataSource dataSource = null;
		if (StringUtil.isEmpty(jasperModel.getDataSourceAttributeName())) {
			dataSource = new JREmptyDataSource();
		} else {
			Object dsObject = getAttribute(request, jasperModel.getDataSourceAttributeName());
			if (dsObject == null) {
				dataSource = new JREmptyDataSource();
			} else {
				if (dsObject instanceof JRDataSource) {
					dataSource = (JRDataSource)dsObject;
				} else {
					throw new ReportOutputException("unsupported datasource type. class=" + dsObject.getClass().getName());
				}
			}
		}

		String ownerPassword = null;
		String userPassword = null;
		// 新しいowner/userパスワード属性名を優先
		if (StringUtil.isNotEmpty(jasperModel.getOwnerPasswordAttributeName())) {
			ownerPassword = (String)getAttribute(request, jasperModel.getOwnerPasswordAttributeName());
		}
		if (StringUtil.isNotEmpty(jasperModel.getUserPasswordAttributeName())) {
			userPassword = (String)getAttribute(request, jasperModel.getUserPasswordAttributeName());
		}
		// 後方互換性: 従来のpasswordAttributeNameが設定されている場合はowner/user両方に適用
		if (ownerPassword == null && userPassword == null && StringUtil.isNotEmpty(jasperModel.getPasswordAttributeName())) {
			String password = (String)getAttribute(request, jasperModel.getPasswordAttributeName());
			ownerPassword = password;
			userPassword = password;
		}

		// JasperPrintインスタンス生成
		List<JasperPrint> jrList = new ArrayList<JasperPrint>();
		// 複数指定すれば、1つのPDFファイル上に複数の帳票が出力されるが現状、１ファイルのみ。
		jrList.add(JasperFillManager.fillReport(jasperModel.getJrMain(), params, dataSource));

		//Input
		SimpleExporterInput input = SimpleExporterInput.getInstance(jrList);
		//Output
		SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput((OutputStream)context.getResponse().getOutputStream());

		//出力形式毎に処理実施
		JRAbstractExporter<?, ?, ?, ?> exporter = null;
		if(OutputFileType.PDF.equals(outputType)){
			// PDF形式で帳票の出力
			JRPdfExporter pdfExporter = new JRPdfExporter();
			pdfExporter.setExporterInput(input);
			pdfExporter.setExporterOutput(output);

			SimplePdfExporterConfiguration config = new SimplePdfExporterConfiguration();
			if (StringUtil.isNotEmpty(ownerPassword) || StringUtil.isNotEmpty(userPassword)) {
				if (StringUtil.isNotEmpty(ownerPassword)) {
					config.setOwnerPassword(ownerPassword);
				}
				if (StringUtil.isNotEmpty(userPassword)) {
					config.setUserPassword(userPassword);
				}
				config.setEncrypted(true);
			}
			pdfExporter.setConfiguration(config);

			exporter = pdfExporter;

		}else if(OutputFileType.XLS.equals(outputType)){
			// XLS形式で帳票の出力
			JRXlsExporter xlsExporter = new JRXlsExporter();
			xlsExporter.setExporterInput(input);
			xlsExporter.setExporterOutput(output);

			SimpleXlsReportConfiguration config = new SimpleXlsReportConfiguration();
			if (StringUtil.isNotEmpty(ownerPassword) || StringUtil.isNotEmpty(userPassword)) {
				logger.warn("XLS type does not support encryption. IF you want to encryption, change to XLSX type.");
			}
			config.setWhitePageBackground(false);
			xlsExporter.setConfiguration(config);

			exporter = xlsExporter;

		}else if(OutputFileType.XLSX.equals(outputType)){
			// XLSX形式で帳票の出力
			JRXlsxExporter xlsxExporter = new JRXlsxExporter ();
			xlsxExporter.setExporterInput(input);
			xlsxExporter.setExporterOutput(output);

			SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
			// XLSXはuserPasswordのみサポート、ownerPasswordは設定されていればuserPasswordとして使用
			if (StringUtil.isNotEmpty(userPassword)) {
				config.setPassword(userPassword);
			} else if (StringUtil.isNotEmpty(ownerPassword)) {
				config.setPassword(ownerPassword);
			}
			config.setWhitePageBackground(false);
			xlsxExporter.setConfiguration(config);

			exporter = xlsxExporter;
		}
		if (exporter != null) {
			exporter.exportReport();
		}
	}

	/**
	 * supportFilesを取得します。
	 * @return supportFiles
	 */
	public String[] getSupportFiles() {
	    return supportFiles;
	}

	/**
	 * supportFilesを設定します。
	 * @param supportFiles supportFiles
	 */
	public void setSupportFiles(String[] supportFiles) {
	    this.supportFiles = supportFiles;
	}

	@Override
	public ReportingType getReportingType() {
		ReportingType type = new ReportingType();
		type.setName(JasperReportType.class.getName());
		type.setDisplayName("JasperReports");
		return type;
	}

	private Object getAttribute(RequestContext request, String attributeName) {

		if(attributeName.startsWith(PREFIX_REQUEST)){
			String valueName = attributeName.substring(PREFIX_REQUEST.length());
			return request.getAttribute(valueName);
		}else if(attributeName.startsWith(PREFIX_SESSION)){
			String valueName = attributeName.substring(PREFIX_SESSION.length());
			return request.getSession().getAttribute(valueName);
		} else {
			//Prefix未指定の場合はリクエストから取得
			return request.getAttribute(attributeName);
		}
	}

}
