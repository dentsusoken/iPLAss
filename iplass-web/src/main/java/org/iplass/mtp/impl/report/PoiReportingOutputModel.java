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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.impl.web.template.report.MetaPoiReportOutputLogic.PoiReportOutputLogicRuntime;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoiReportingOutputModel implements ReportingOutputModel{

	private static Logger logger = LoggerFactory.getLogger(PoiReportingOutputModel.class);

	private static final String TYPE_SXSSF = "XLSX_SXSSF_POI";

	private Workbook book;

	private String passwordAttributeName;
	private POIFSFileSystem fs;
	private File tempPasswordFile;

	private PoiReportOutputLogicRuntime logicRuntime;

	PoiReportingOutputModel(byte[] binary, String type, String extension) throws Exception{
		try (ByteArrayInputStream bis = new ByteArrayInputStream(binary)){
			// ブック読み込み
			book = WorkbookFactory.create(bis);

			if (TYPE_SXSSF.equals(type)) {
				if (!(book instanceof XSSFWorkbook)) {
					throw new ApplicationException("SXSSF does not support other than XSSF");
				}
				book = new SXSSFWorkbook((XSSFWorkbook)book);
			}
		}
	}

	public Workbook getBook() {
	    return book;
	}

	public PoiReportOutputLogicRuntime getLogicRuntime() {
	    return logicRuntime;
	}

	public void setLogicRuntime(PoiReportOutputLogicRuntime logicRuntime) {
	    this.logicRuntime = logicRuntime;
	}

	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

	public void write(OutputStream os, String password) throws IOException, InvalidFormatException, GeneralSecurityException {

		if (StringUtil.isEmpty(password)) {
			//パスワードなしの場合は、直接Responseに出力
			book.write(os);
		} else {
			if (getBook() instanceof HSSFWorkbook) {
				//HSSFWorkbookはPassword設定サポート外
				logger.warn("HSSFWorkbook does not support encryption. IF you want to encryption, change to XSSFWorkbook type.");
				getBook().write(os);
			} else {
				//POIFSFileSystem経由でレスポンスに書きこみ
				getPOIFSFileSystem(password).writeFilesystem(os);
			}
		}
	}

	@Override
	public void close() throws IOException {
		try {
			if (book != null) {
				book.close();

				//SXSSFWorkbookの場合は、一時ファイルを削除
				if (book instanceof SXSSFWorkbook) {
					((SXSSFWorkbook)book).dispose();
				}

				book = null;
			}
		} finally {
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
	}

	private POIFSFileSystem getPOIFSFileSystem(String password) throws IOException, InvalidFormatException, GeneralSecurityException {
		if (fs != null) {
			return fs;
		}

		//一時ファイルにWorkbookの内容を出力
		tempPasswordFile = TempFile.createTempFile("tmp", ".tmp");
		try (FileOutputStream fos = new FileOutputStream(tempPasswordFile)){
			book.write(fos);
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
}
