/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.template.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.report.ReportingEngine;
import org.iplass.mtp.impl.report.ReportingEngineService;
import org.iplass.mtp.impl.report.ReportingOutputModel;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.template.MetaTemplate;
import org.iplass.mtp.impl.web.template.TemplateRuntimeException;
import org.iplass.mtp.impl.web.template.report.MetaReportType.ReportTypeRuntime;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.template.definition.TemplateDefinition;
import org.iplass.mtp.web.template.report.definition.JasperReportType;
import org.iplass.mtp.web.template.report.definition.JxlsReportType;
import org.iplass.mtp.web.template.report.definition.LocalizedReportDefinition;
import org.iplass.mtp.web.template.report.definition.PoiReportType;
import org.iplass.mtp.web.template.report.definition.ReportTemplateDefinition;
import org.iplass.mtp.web.template.report.definition.ReportType;

public class MetaReportTemplate extends MetaTemplate {

	private static final long serialVersionUID = -8548055742699078566L;

	private String fileName;

	private byte[] binary;

	private MetaReportType reportType;

	private List<MetaLocalizedReport> localizedReportList = new ArrayList<MetaLocalizedReport>();

	public String getFileName() {
	    return fileName;
	}

	public void setFileName(String fileName) {
	    this.fileName = fileName;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	public MetaReportType getReportType() {
		return reportType;
	}

	public void setReportType(MetaReportType reportType) {
		this.reportType = reportType;
	}

	public List<MetaLocalizedReport> getLocalizedReportList() {
		return localizedReportList;
	}

	public void setLocalizedReportList(List<MetaLocalizedReport> localizedReportList) {
		this.localizedReportList = localizedReportList;
	}

	@Override
	public void applyConfig(TemplateDefinition definition) {
		fillFrom(definition);
		ReportTemplateDefinition def = (ReportTemplateDefinition) definition;

		if (def.getReportType() != null) {
			ReportType reportType = def.getReportType();
			if (reportType instanceof JasperReportType) {
				MetaJasperReportType jrt = new MetaJasperReportType();
				jrt.applyConfig(reportType);
				this.reportType = jrt;
			} else if(reportType instanceof PoiReportType) {
				MetaPoiReportType prt = new MetaPoiReportType();
				prt.applyConfig(reportType);
				this.reportType = prt;
			} else if(reportType instanceof JxlsReportType) {
				MetaJxlsReportType jxrt = new MetaJxlsReportType();
				jxrt.applyConfig(reportType);
				this.reportType = jxrt;
			}
		} else {
			this.reportType = null;
		}

		fileName = def.getFileName();
		binary = def.getBinary();

		if (def.getLocalizedReportList() != null) {
			localizedReportList = new ArrayList<MetaLocalizedReport>();
			for (LocalizedReportDefinition ed: def.getLocalizedReportList()) {

				MetaLocalizedReport mlr = new MetaLocalizedReport();
				mlr.setLocaleName(ed.getLocaleName());
				mlr.setFileName(ed.getFileName());
				mlr.setBinary(ed.getBinary());

				if (ed.getReportType() != null) {
					ReportType reportType = ed.getReportType();
					if (reportType instanceof JasperReportType) {
						MetaJasperReportType jrt = new MetaJasperReportType();
						jrt.applyConfig(reportType);
						mlr.setReportType(jrt);
					} else if(reportType instanceof PoiReportType) {
						MetaPoiReportType prt = new MetaPoiReportType();
						prt.applyConfig(reportType);
						mlr.setReportType(prt);
					} else if (reportType instanceof JxlsReportType) {
						MetaJxlsReportType jxrt = new MetaJxlsReportType();
						jxrt.applyConfig(reportType);
						mlr.setReportType(jxrt);
					}
				}

				localizedReportList.add(mlr);
			}
		} else {
			localizedReportList = null;
		}

	}

	@Override
	public TemplateDefinition currentConfig() {
		ReportTemplateDefinition definition = new ReportTemplateDefinition();
		fillTo(definition);
		if(reportType != null){
			definition.setReportType(reportType.currentConfig());
		}
		definition.setFileName(fileName);
		definition.setBinary(binary);

		if (localizedReportList != null) {
			for (MetaLocalizedReport mlr: localizedReportList) {
				definition.addLocalizedReport(mlr.currentConfig());
			}
		}

		return definition;
	}

	@Override
	public TemplateRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new ReportTemplateRuntime();
	}

	public class ReportTemplateRuntime extends TemplateRuntime {

		private static final String DOT = ".";
		private ReportingEngine reportEngine ;
		private ReportingEngineService service = ServiceRegistry.getRegistry().getService(ReportingEngineService.class);
		private ReportTypeRuntime reportTypeRuntime;

		private Map<String, ReportSet> reportSetMap = new HashMap<String, ReportSet>();

		private class ReportSet {

			private ReportingEngine reportEngine;
			private byte[] binary;
			private ReportTypeRuntime reportTypeRuntime;
			private String fileName;

		}

		public ReportTemplateRuntime() {
			if(binary == null){
				//初期登録時は、バイナリなしのため
				return ;
			}

			// ReportEngineの作成
			try {
				ReportingEngine _reportEngine;
				if (localizedReportList != null) {
					for (MetaLocalizedReport mlr : localizedReportList) {
						_reportEngine = service.createReportingEngine(mlr.getReportType().currentConfig().getClass().getName());
						if(_reportEngine == null){
							//ReportEngineがnullの場合、サポート外のメタデータを使用する場合
							throw new TemplateRuntimeException("Report tempalte is outside of support . templateName:" + getName());
						}

						ReportSet reportSet = new ReportSet();
						reportSet.reportEngine = _reportEngine;
						reportSet.binary = mlr.getBinary();
						reportSet.reportTypeRuntime = mlr.getReportType().createRuntime();
						reportSet.fileName = mlr.getFileName();

						reportSetMap.put(mlr.getLocaleName(), reportSet);
					}
				}

				reportEngine = service.createReportingEngine(reportType.currentConfig().getClass().getName());
				if(reportEngine == null){
					//ReportEngineがnullの場合、サポート外のメタデータを使用する場合
					throw new TemplateRuntimeException("Report tempalte is outside of support . templateName:" + getName());
				}
				// ReportTypeRuntimeの生成
				reportTypeRuntime = reportType.createRuntime();
			} catch (Exception e) {
				setIllegalStateException(new RuntimeException(e));
			}
		}


		public MetaReportTemplate getMetaData() {
			return MetaReportTemplate.this;
		}

		@Override
		public void handleContent(WebRequestStack requestContext)
				throws IOException, ServletException {
			checkState();

			try{

				ReportingEngine _reportEngine = reportEngine;
				byte[] _binary = binary;
				ReportTypeRuntime _reportTypeRuntime = reportTypeRuntime;
				String _fileName = fileName;
				MetaReportType _reportType = (MetaReportType) reportTypeRuntime.getMetaData();
						
				String lang = ExecuteContext.getCurrentContext().getLanguage();

				if (reportSetMap.get(lang) != null) {
					_reportEngine = reportSetMap.get(lang).reportEngine;
					_binary = reportSetMap.get(lang).binary;
					_reportTypeRuntime = reportSetMap.get(lang).reportTypeRuntime;
					_fileName = reportSetMap.get(lang).fileName;
				}
				
				//出力モデルの生成
				try (ReportingOutputModel createOutputModel
						= _reportEngine.createOutputModel(_binary, _reportType.getOutputFileType(), _fileName.substring(_fileName.indexOf(DOT)+1))) {

					_reportTypeRuntime.setParam(createOutputModel);

					//帳票出力処理
					_reportEngine.exportReport(requestContext, createOutputModel);
				}

			} catch (Exception e) {
				throw new TemplateRuntimeException("The report output went wrong. templateName:" + getName(), e);
			}

			if (requestContext.getPageContext() != null) {
				throw new TemplateRuntimeException("Report template can not include from jsp... templateName:" + getName());
			}
		}
	}


}
