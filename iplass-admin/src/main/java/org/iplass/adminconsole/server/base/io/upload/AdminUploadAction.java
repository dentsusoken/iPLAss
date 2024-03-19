/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.base.io.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.server.base.service.AdminConsoleService;
import org.iplass.adminconsole.shared.base.io.AdminUploadConstant;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adminファイルアップロード操作サーブレット
 *
 * <p>
 * ファイルアップロードのマルチパートリクエストを受け付けるサーブレット。
 * 本クラスを継承したクラスは、	{@link #executeAction(HttpServletRequest, List)} を実装し、JSON文字列を返却する。
 * </p>
 *
 * <p>
 * 本サーブレットのレスポンスは <code>application/json</code> で返却する。
 * 処理が正常終了した場合、正常時レスポンスを返却する。例外シーケンスに入った場合は、異常終了時レスポンスを返却する。
 * </p>
 *
 * <p>
 * 正常時レスポンスイメージ
 * <pre>
 * {
 *   "isSuccess": true,
 *   "data": アプリ定義JSON
 * }
 * <pre>
 * <p>
 *
 * <p>
 * 異常終了時レスポンスイメージ
 * <pre>
 * {
 *   "isSuccess": false,
 *   "data": { "errorMessage": "エラーメッセージ" }
 * }
 * </pre>
 * </p>
 *
 * <p>
 * NOTE: ライブラリ gwtupload を利用していたが、利用しない形式に変更
 * </p>
 *
 * @see org.iplass.adminconsole.shared.base.io.AdminUploadConstant
 * @author SEKIGUCHI Naoya
 */
public abstract class AdminUploadAction extends XsrfProtectedMultipartServlet {
	/** serialVersionUID */
	private static final long serialVersionUID = -5553465242497700877L;

	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(AdminUploadAction.class);
	/** contextTempDir */
	private File contextTempDir;
	/** マルチパートリクエストの最大パラメータ数 */
	private long maxParameterCount;
	/** ファイルアップロードを許容する最大サイズ */
	private long maxFileSize;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		contextTempDir = (File) config.getServletContext().getAttribute("javax.servlet.context.tempdir");

		WebFrontendService webFront = ServiceRegistry.getRegistry().getService(WebFrontendService.class);
		// マルチパートリクエストの最大パラメータ数を設定する。
		maxParameterCount = webFront.getMaxMultipartParameterCount();

		//サイズ制限についてパラメータで指定されていない場合、Serviceの設定値をセット
		AdminConsoleService acs = ServiceRegistry.getRegistry().getService(AdminConsoleService.class);

		// initParameter で設定されていない場合、AdminConsoleService の値を設定する
		String maxSizeString = getInitParameter("maxSize");
		maxSize = null == maxSizeString ? acs.getMaxUploadFileSize() : maxSize;

		String maxFileSizeString = getInitParameter("maxFileSize");
		maxFileSize = null == maxFileSizeString ? acs.getMaxUploadFileSize() : Long.valueOf(maxFileSizeString);

		// parameterParser の設定
		configureParameterParser(getParameterParser());
	}

	/**
	 * 個別のファイルアップロード処理
	 *
	 * @param request HttpServletRequest
	 * @param parameterItem リクエストパラメータ
	 * @return アプリ実行結果JSON文字列
	 * @throws UploadActionException アップロード操作例外
	 */
	abstract public String executeAction(final HttpServletRequest request, final List<MultipartRequestParameter> parameterItem) throws UploadActionException;

	@Override
	protected final void doMultipartPost(HttpServletRequest req, HttpServletResponse resp, List<MultipartRequestParameter> files) {
		HttpSession session = req.getSession(false);
		String sessionId = null != session ? session.getId() : null;
		String logPrefix = "ADMIN-UPLOAD (" + getClass().getSimpleName() + ", sessionId = " + sessionId + ")";
		try {
			logger.info("{} START.", logPrefix);
			// アップロードされたファイル情報のログ出力
			files.stream().filter(f -> !f.isFormField())
					.forEach(f -> logger.info("{} FILE INFO. fieldName = {},  file = {}, content-type = {}, size = {}.",
							logPrefix, f.getFieldName(), f.getName(), f.getContentType(), f.getSize()));
			if (logger.isDebugEnabled()) {
				// フォーム入力値パラメータ情報のログ出力
				files.stream().filter(f -> f.isFormField())
						.forEach(f -> logger.debug("{} FORM FIELD INFO. fieldName = {}, content-type = {}, size = {}.",
								logPrefix, f.getFieldName(), f.getContentType(), f.getSize()));
			}

			String resultJson = executeAction(req, files);

			writeResponse(resp, resultJson, HttpServletResponse.SC_OK);

			logger.info("{} FINISH.", logPrefix);

		} catch (UploadActionException | UploadRuntimeException e) {
			logger.error("{} ERROR.", logPrefix, e);
			// UploadActionException, UploadRuntimeException は例外メッセージを返却する。
			// json = {"errorMessage": "例外メッセージ"}
			String errorResultJson = "{" + jsonKeyString(AdminUploadConstant.ResponseKey.ERROR_MESSAGE, e.getMessage()) + "}";
			writeResponse(resp, errorResultJson, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		} catch (RuntimeException e) {
			logger.error("{} ERROR.", logPrefix, e);
			// Runtime 系はシステムエラーメッセージを返却する。
			// json = {"errorMessage": "システムエラーメッセージ"}
			String message = AdminResourceBundleUtil.resourceString("upload.AdminUploadAction.systemErr");
			String errorResultJson = "{" + jsonKeyString(AdminUploadConstant.ResponseKey.ERROR_MESSAGE, message) + "}";
			writeResponse(resp, errorResultJson, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * レスポンス書き込み
	 *
	 * @param resp HttpServletResponse
	 * @param resultJson 実行結果JSON文字列
	 * @param status HttpStatusコード
	 */
	private void writeResponse(HttpServletResponse resp, String resultJson, int status) {
		resp.setStatus(status);
		resp.addHeader("Cache-Control", "no-cache");
		resp.setContentType("application/json");
		resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

		boolean isSuccess = HttpServletResponse.SC_OK == status;
		// json = { "isSuccess": booleanValue, "response": resultJson }
		// isSuccess の booleanValue は 成功している場合：true, 失敗している場合：false
		String json = "{" + jsonKeyValue(AdminUploadConstant.ResponseKey.IS_SUCCESS, isSuccess) + ","
				+ jsonKeyValue(AdminUploadConstant.ResponseKey.DATA, resultJson) + "}";
		try (PrintWriter writer = resp.getWriter()) {
			writer.write(json);
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * JSON文字列（キー、文字列）を作成する
	 *
	 * @param k キー
	 * @param s 文字列
	 * @return JSON文字列
	 */
	private String jsonKeyString(String k, String s) {
		// "k": "s" を返却
		return "\"" + k + "\":\"" + s + "\"";
	}

	/**
	 * JSON文字列（キー、オブジェクト）を作成する
	 *
	 * @param k キー
	 * @param o オブジェクト
	 * @return JSON文字列
	 */
	private String jsonKeyValue(String k, Object o) {
		// "k": o を返却
		return "\"" + k + "\":" + o;
	}

	// TODO このメソッドはユーティリティ。
	protected final byte[] convertFileToByte(File file) {
		byte[] bytes = null;
		FileInputStream is = null;
		FileChannel channel = null;
		try {
			is = new FileInputStream(file);
			channel = is.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
			channel.read(buffer);
			buffer.clear();
			bytes = new byte[buffer.capacity()];
			buffer.get(bytes);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return bytes;
	}

	/**
	 * @return contextTempDir
	 */
	protected File getContextTempDir() {
		return contextTempDir;
	}

	/**
	 * パラメーターパーサーを設定する
	 * @param <T> MultipartRequestParameterParser派生クラス
	 * @param parameterParser インスタンス
	 * @see #createParameterParser()
	 */
	protected <T extends MultipartRequestParameterParser> void configureParameterParser(T parameterParser) {
		// スーパークラスで生成しているインスタンスの為、CommonsFileuploadMultipartRequestParameterParser にキャスト
		CommonsFileuploadMultipartRequestParameterParser parser = (CommonsFileuploadMultipartRequestParameterParser) parameterParser;

		parser.setSizeMax(maxSize);
		parser.setFileSizeMax(maxFileSize);
		parser.setFileCountMax(maxParameterCount);
	}
}
