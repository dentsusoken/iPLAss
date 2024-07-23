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
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.util.TempFile;
import org.iplass.mtp.impl.web.template.report.MetaJxlsReportOutputLogic.JxlsReportOutputLogicRuntime;
import org.iplass.mtp.impl.web.template.report.MetaReportParamMap;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.template.report.definition.OutputFileType;
import org.jxls.builder.JxlsStreaming;
import org.jxls.transform.poi.JxlsPoiTemplateFillerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jxls帳票出力モデル
 */
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

	/**
	 * パスワード属性名を取得する
	 * @return パスワード属性名
	 */
	public String getPasswordAttributeName() {
		return passwordAttributeName;
	}

	/**
	 * パスワード属性名を設定する
	 * @param passwordAttributeName パスワード属性名
	 */
	public void setPasswordAttributeName(String passwordAttributeName) {
		this.passwordAttributeName = passwordAttributeName;
	}

	/**
	 * 利用者カスタムの出力ロジックRuntimeを取得する
	 * @return 利用者カスタムの出力ロジックRuntime
	 */
	public JxlsReportOutputLogicRuntime getLogicRuntime() {
		return logicRuntime;
	}

	/**
	 * 利用者カスタムの出力ロジックRuntimeを設定する
	 * @param logicRuntime 利用者カスタムの出力ロジックRuntime
	 */
	public void setLogicRuntime(JxlsReportOutputLogicRuntime logicRuntime) {
		this.logicRuntime = logicRuntime;
	}

	/**
	 * 帳票テンプレートファイルバイナリを取得する
	 * @return 帳票テンプレートファイルバイナリ
	 */
	public byte[] getBinary() {
		return binary;
	}

	/**
	 * 帳票テンプレートファイルバイナリを設定する
	 * @param binary 帳票テンプレートバイナリ
	 */
	public void setBinary(byte[] binary) {
		this.binary = binary;
	}

	/**
	 * 帳票出力タイプを取得する
	 *
	 * <p>
	 * ここで設定されるタイプは {@link org.iplass.mtp.web.template.report.definition.OutputFileType} の末尾が JXLS の値となります。
	 * </p>
	 *
	 * @return 帳票出力タイプ
	 */
	public String getType() {
		return type;
	}

	/**
	 * 帳票出力タイプを設定する
	 * @param type 帳票出力タイプ
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * パラメータマッピング情報を取得する
	 * @return パラメータマッピング情報
	 */
	public MetaReportParamMap[] getParamMap() {
		return paramMap;
	}

	/**
	 * パラメータマッピング情報を設定する
	 * @param paramMap パラメータマッピング情報
	 */
	public void setParamMap(MetaReportParamMap[] paramMap) {
		this.paramMap = paramMap;
	}

	/**
	 * コンパイル済み groovy script キャッシュストアを取得する
	 * @return コンパイル済み groovy script キャッシュストア
	 */
	public JxlsCompiledScriptCacheStore getCacheStore() {
		return cacheStore;
	}

	/**
	 * コンパイル済み groovy script キャッシュストアを設定する
	 * @param cacheStore コンパイル済み groovy script キャッシュストア
	 */
	public void setCacheStore(JxlsCompiledScriptCacheStore cacheStore) {
		this.cacheStore = cacheStore;
	}

	/**
	 * レポートを書き込む
	 * @param reportData 帳票データ
	 * @param os 帳票出力先
	 * @param password 帳票に設定するパスワード
	 * @throws IOException 入出力例外
	 * @throws InvalidFormatException 帳票フォーマット不正
	 * @throws GeneralSecurityException 帳票書き込み時セキュリティ例外
	 */
	public void write(Map<String, Object> reportData, OutputStream os, String password)
			throws IOException, InvalidFormatException, GeneralSecurityException {
		try (InputStream templateInput = new ByteArrayInputStream(getBinary())) {
			OutputFileType outputType = OutputFileType.convertOutputFileType(getType());

			// ストリーミング対応フォーマット判定
			boolean isStreaming = outputType == OutputFileType.XLSX_SXSSF_JXLS;

			// NOTE Jxls テンプレート処理のエントリポイント
			var builder = JxlsPoiTemplateFillerBuilder.newInstance()
					// 式評価機能
					.withExpressionEvaluatorFactory(new JxlsGroovyEvaluatorFactory(cacheStore))
					// テンプレート
					.withTemplate(templateInput)
					// テンプレートのストリーミング対応
					.withStreaming(isStreaming ? JxlsStreaming.STREAMING_ON : JxlsStreaming.STREAMING_OFF);

			// パスワードなしの場合は、直接Responseに出力
			if (StringUtil.isEmpty(password)) {
				outputReport(reportData, builder, os);
			} else {
				if (OutputFileType.XLS_JXLS.equals(outputType)) {
					logger.warn(
							"XLS type does not support encryption. IF you want to encryption, change to XLSX type.");
					outputReport(reportData, builder, os);
				} else {
					// POIFSFileSystem経由でレスポンスに書きこみ。POIFSFileSystem は close メソッドでクローズする。
					getPOIFSFileSystem(reportData, password, builder).writeFilesystem(os);
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

	/**
	 * テンプレートファイルを Jxls テンプレート出力し、その結果を POIFSFileSystem で返却する
	 */
	private POIFSFileSystem getPOIFSFileSystem(Map<String, Object> reportData, String password, JxlsPoiTemplateFillerBuilder builder)
			throws IOException, InvalidFormatException, GeneralSecurityException {
		if (fs != null) {
			return fs;
		}

		// 一時ファイルに内容を出力
		tempPasswordFile = TempFile.createTempFile("tmp", ".tmp");
		try (OutputStream fos = new FileOutputStream(tempPasswordFile)) {
			outputReport(reportData, builder, fos);
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

	/**
	 * 帳票出力を実施する
	 *
	 * <p>
	 * {@link #logicRuntime} が設定されている場合、logicRuntime の機能で帳票出力を実施。
	 * 設定されていない場合は、そのまま出力を実施する。
	 * </p>
	 */
	private void outputReport(Map<String, Object> reportData, JxlsPoiTemplateFillerBuilder builder, OutputStream out) throws IOException {
		if (getLogicRuntime() != null) {
			// ユーザー定義の処理
			logicRuntime.outputReport(builder, reportData, out);
		} else {
			// デフォルトの帳票出力処理
			builder.build().fill(reportData, () -> out);
		}
	}
}