/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.util.TempFile;
import org.iplass.mtp.impl.web.template.report.MetaJxlsReportOutputLogic.JxlsReportOutputLogicRuntime;
import org.iplass.mtp.impl.web.template.report.MetaReportParamMap;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.report.definition.OutputFileType;
import org.jxls.common.Context;
import org.jxls.expression.ExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.CannotOpenWorkbookException;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JxlsReportingOutputModel implements ReportingOutputModel {

	private static Logger logger = LoggerFactory.getLogger(JxlsReportingOutputModel.class);

	private String passwordAttributeName;
	private POIFSFileSystem fs;
	private File tempPasswordFile;

	private JxlsReportOutputLogicRuntime logicRuntime;

	private byte[] binary;
	private String type;
	private MetaReportParamMap[] paramMap;

	private JxlsCompiledScriptCacheStore cacheStore;

	JxlsReportingOutputModel(byte[] binary, String type, String extension) throws Exception {
		this.binary = binary;
		this.type = type;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

	public JxlsReportOutputLogicRuntime getLogicRuntime() {
		return logicRuntime;
	}

	public void setLogicRuntime(JxlsReportOutputLogicRuntime logicRuntime) {
		this.logicRuntime = logicRuntime;
	}

	public byte[] getBinary() {
		return binary;
	}

	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public MetaReportParamMap[] getParamMap() {
		return paramMap;
	}

	public void setParamMap(MetaReportParamMap[] paramMap) {
		this.paramMap = paramMap;
	}

	public JxlsCompiledScriptCacheStore getCacheStore() {
		return cacheStore;
	}

	public void setCacheStore(JxlsCompiledScriptCacheStore cacheStore) {
		this.cacheStore = cacheStore;
	}

	public void write(Context context, OutputStream os, String password)
			throws IOException, InvalidFormatException, GeneralSecurityException {
		try (InputStream is = new ByteArrayInputStream(getBinary())) {

			JxlsHelper jxlsHelper = JxlsHelper.getInstance();
			ExpressionEvaluator evaluator = new JxlsGroovyEvaluator(cacheStore);

			OutputFileType outputType = OutputFileType.convertOutputFileType(getType());

			// パスワードなしの場合は、直接Responseに出力
			if (StringUtil.isEmpty(password)) {
				outputReport(getTransformer(jxlsHelper, is, os, evaluator, outputType), context, jxlsHelper);
			} else {
				if (OutputFileType.XLS_JXLS.equals(outputType)) {
					logger.warn(
							"XLS type does not support encryption. IF you want to encryption, change to XLSX type.");
					outputReport(getTransformer(jxlsHelper, is, os, evaluator, outputType), context, jxlsHelper);
				} else {
					// POIFSFileSystem経由でレスポンスに書きこみ
					getPOIFSFileSystem(context, is, jxlsHelper, password, evaluator, outputType).writeFilesystem(os);
				}
			}
		}
	}

	@Override
	public void close() throws IOException {
		try {
			if (fs != null) {
				fs.close();
				fs = null;
			}
		} finally {
			if (tempPasswordFile != null) {
				if (!tempPasswordFile.delete()) {
					logger.warn("Fail to delete temporary resource:" + tempPasswordFile.getPath());
				}
				tempPasswordFile = null;
			}
		}
	}

	private POIFSFileSystem getPOIFSFileSystem(Context context, InputStream is, JxlsHelper jxlsHelper, String password,
			ExpressionEvaluator evaluator, OutputFileType outputFileType)
			throws IOException, InvalidFormatException, GeneralSecurityException {
		if (fs != null) {
			return fs;
		}

		// 一時ファイルに内容を出力
		tempPasswordFile = TempFile.createTempFile("tmp", ".tmp");
		try (OutputStream fos = new FileOutputStream(tempPasswordFile)) {
			outputReport(getTransformer(jxlsHelper, is, fos, evaluator, outputFileType), context, jxlsHelper);
		}

		// POIFSFileSystemを生成
		fs = new POIFSFileSystem();

		// パスワード情報を作成
		EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
		Encryptor enc = info.getEncryptor();
		enc.confirmPassword(password);

		// OPCPackageを利用して一時ファイルにPOIFSFileSystem経由でパスワード設定
		try (OPCPackage opc = OPCPackage.open(tempPasswordFile, PackageAccess.READ_WRITE);
				OutputStream encos = enc.getDataStream(fs);) {
			opc.save(encos);
		}

		return fs;
	}

	private void outputReport(Transformer transformer, Context context, JxlsHelper jxlsHelper) throws IOException {
		if (getLogicRuntime() != null) {
			logicRuntime.outputReport(transformer, context);
		} else {
			// デフォルトの帳票出力処理
			jxlsHelper.processTemplate(context, transformer);
		}
	}

	private Transformer getTransformer(JxlsHelper jxlsHelper, InputStream is, OutputStream os,
			ExpressionEvaluator evaluator, OutputFileType outputFileType) {
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(is);
		} catch (Exception e) {
			throw new CannotOpenWorkbookException(e);
		}

		PoiTransformer transformer = outputFileType == OutputFileType.XLSX_SXSSF_JXLS
				? PoiTransformer.createSxssfTransformer(workbook)
				: PoiTransformer.createTransformer(workbook);
		transformer.setOutputStream(os);
		transformer.getTransformationConfig().setExpressionEvaluator(evaluator);

		return transformer;
	}
}