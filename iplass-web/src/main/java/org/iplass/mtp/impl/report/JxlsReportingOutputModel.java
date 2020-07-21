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
import org.apache.poi.util.TempFile;
import org.iplass.mtp.impl.web.template.report.MetaJxlsContextParamMap;
import org.iplass.mtp.impl.web.template.report.MetaJxlsReportOutputLogic.JxlsReportOutputLogicRuntime;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.report.definition.OutputFileType;
import org.jxls.common.Context;
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
	private MetaJxlsContextParamMap[] contextParamMap;
	
	//Constructor
	JxlsReportingOutputModel(byte[] binary, String type, String extension) throws Exception{
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
	
	public MetaJxlsContextParamMap[] getContextParamMap() {
		return contextParamMap;
	}

	public void setContextParamMap(MetaJxlsContextParamMap[] contextParamMap) {
		this.contextParamMap = contextParamMap;
	}

	public void write(Context context, OutputStream os, String password) throws IOException, InvalidFormatException, GeneralSecurityException {
		try(InputStream is = new ByteArrayInputStream(getBinary())) {
			
			OutputFileType outputType = OutputFileType.convertOutputFileType(getType());
			
			//パスワードなしの場合は、直接Responseに出力
			if (StringUtil.isEmpty(password)) {
				outputReport(is,os,context);
			} else {
				if (OutputFileType.XLS_JXLS.equals(outputType)) {
					logger.warn("XLS type does not support encryption. IF you want to encryption, change to XLSX type.");
					outputReport(is, os, context);
				} else {
					//POIFSFileSystem経由でレスポンスに書きこみ
					getPOIFSFileSystem(context, is, password).writeFilesystem(os);
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
	
	private POIFSFileSystem getPOIFSFileSystem(Context context, InputStream is, String password) throws IOException, InvalidFormatException, GeneralSecurityException {
		if (fs != null) {
			return fs;
		}

		//一時ファイルに内容を出力
		tempPasswordFile = TempFile.createTempFile("tmp", ".tmp");
		try (OutputStream fos = new FileOutputStream(tempPasswordFile)){
			outputReport(is, fos, context);
		}

		//POIFSFileSystemを生成
		fs = new POIFSFileSystem();

		//パスワード情報を作成
		EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
		Encryptor enc = info.getEncryptor();
		enc.confirmPassword(password);

		//OPCPackageを利用して一時ファイルにPOIFSFileSystem経由でパスワード設定
		try (OPCPackage opc = OPCPackage.open(tempPasswordFile, PackageAccess.READ_WRITE);
			OutputStream encos = enc.getDataStream(fs);
		) {
			opc.save(encos);
		}

		return fs;
	}
	
	private void outputReport(InputStream is, OutputStream os, Context context) throws IOException {
		if (getLogicRuntime() != null) {
			logicRuntime.outputReport(is, os, context);
		} else {
			JxlsHelper.getInstance().processTemplate(is, os, context);
		}
	}
}
