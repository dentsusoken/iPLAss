/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.iplass.adminconsole.shared.base.io.XsrfProtectedMultipartConstant;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * XsrfProtected Servlet（Multipart リクエスト用）
 *
 * <p>
 * {@link com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet#validateXsrfToken(com.google.gwt.user.client.rpc.RpcToken, java.lang.reflect.Method)} を利用しトークン検証を実施するサーブレット。
 * XsrfProtectedServiceServlet では GWT RPC が強制されるので、service メソッドをオーバーライドし Multipart リクエストのみ受け付け可能な形としている。
 * </p>
 *
 * <p>
 * 機能の責務
 * <ul>
 * <li>マルチパートパラメータのパース</li>
 * <li>XSRFトークンの検証</li>
 * <li>パースしたパラメータのリソース破棄</li>
 * </ul>
 * </p>
 *
 * @see org.iplass.adminconsole.client.base.io.upload.XsrfProtectedMultipartForm
 * @author SEKIGUCHI Naoya
 */
public abstract class XsrfProtectedMultipartServlet extends XsrfProtectedServiceServlet {
	/** serialVersionUID */
	private static final long serialVersionUID = 3352671393580437480L;
	/** POST */
	private static final String METHOD_POST = "POST";
	/** リクエスト許容サイズデフォルト値 */
	private static final long DEFAULT_MAX_SIZE = 2_097_152L; // 2MB = 1024 * 1024 * 2

	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(XsrfProtectedMultipartServlet.class);
	/** リクエスト許容サイズ */
	protected long maxSize = DEFAULT_MAX_SIZE;
	/** マルチパートリクエストパラメータ解析機能 */
	private MultipartRequestParameterParser parameterParser;
	/** デフォルト文字コード */
	private Charset defaultCharset = StandardCharsets.UTF_8;


	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		String maxSizeString = getInitParameter("maxSize");
		if (StringUtil.isNotBlank(maxSizeString)) {
			this.maxSize = Long.valueOf(maxSizeString);
		}

		parameterParser = createParameterParser();
	}

	/**
	 * デフォルトコンストラクタ
	 */
	public XsrfProtectedMultipartServlet() {
		super();
		perThreadRequest = new ThreadLocal<HttpServletRequest>();
		perThreadResponse = new ThreadLocal<HttpServletResponse>();
	}

	@Override
	protected final void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// リクエストの制約チェック
		// Method POST かつ
		boolean isPost = METHOD_POST.equals(req.getMethod());
		// マルチパートリクエスト かつ
		boolean isMultipart = JakartaServletFileUpload.isMultipartContent(req);
		// conetnt-length は許容範囲内
		boolean isContentLengthAcceptable = isContentLengthAcceptable(req.getContentLengthLong());

		if (isPost && isMultipart && isContentLengthAcceptable) {

			try {
				// XsrfProtectedServiceServlet#validateXsrfToken(RpcToken, Method) を利用するための準備
				perThreadRequest.set(req);
				perThreadResponse.set(resp);

				acceptRequest(req, resp);

			} finally {
				perThreadRequest.set(null);
				perThreadResponse.set(null);
			}

		} else {
			HttpSession session = req.getSession(false);
			String sessionId = session != null ? session.getId() : null;

			// 制約外リクエストの場合、404: Not Found を返却する
			int httpStatus = !isPost || !isMultipart ? HttpServletResponse.SC_NOT_FOUND
					// conetnt-length が許容範囲外の場合、413: Payload Too Large を返却する
					: HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE;

			// エラーログ出力
			logger.error("Error request. sessionId = {}, httpStatus = {}, uri = {}, content-length={}.",
					sessionId, httpStatus, req.getRequestURI(), req.getContentLengthLong());
			// http status 設定
			resp.sendError(httpStatus);
		}
	}

	/**
	 * リクエスト受け付け処理
	 *
	 * <p>
	 * 以下の処理を行う。
	 *
	 * <ul>
	 * <li>リクエストパラメータのパース</li>
	 * <li>XSRFトークン検証</li>
	 * <li>doMultipartPost メソッドの実行</li>
	 * <li>パースしたパラメータのリソース破棄</li>
	 * </p>
	 *
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレット例外
	 * @throws IOException 入出力例外
	 */
	private void acceptRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<MultipartRequestParameter> requestParameterList = parameterParser.parse(req);
		try {
			XsrfToken token = getXsrfToken(requestParameterList);

			// トークン検証
			validateXsrfToken(token, null);

			doMultipartPost(req, resp, requestParameterList);

		} finally {
			// パースパラメータの破棄
			requestParameterList.stream().forEach(p -> p.dispose());
		}
	}

	/**
	 * POST メソッドのマルチパートリクエスト処理
	 *
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @param itemList Multipartリクエストをパースしたパラメータリスト
	 * @throws ServletException サーブレット例外
	 * @throws IOException 入出力例外
	 */
	protected abstract void doMultipartPost(HttpServletRequest req, HttpServletResponse resp, List<MultipartRequestParameter> itemList)
			throws ServletException, IOException;

	/**
	 * リクエストの content-length が許容範囲であるか確認する。
	 *
	 * <p>
	 * AdminConsole で利用することを考慮すると contentLength が設定されないことはあり得ない。
	 * そのため、-1 等の無効な値の場合は許可しない。
	 * </p>
	 *
	 * @param contentLength リクエストの content-length;
	 * @return 判定結果（許容範囲内の場合 true）
	 */
	protected boolean isContentLengthAcceptable(long contentLength) {
		return 0 < contentLength && contentLength <= maxSize;
	}

	/**
	 * MultipartRequestParameterParser インスタンスを作成する。
	 * @return MultipartRequestParameterParser インスタンス
	 */
	protected MultipartRequestParameterParser createParameterParser() {
		return new CommonsFileuploadMultipartRequestParameterParser();
	}

	/**
	 * MultipartRequestParameterParser インスタンスを取得する。
	 * @return MultipartRequestParameterParser インスタンス
	 */
	protected MultipartRequestParameterParser getParameterParser() {
		return parameterParser;
	}

	/**
	 * デフォルト文字コードを取得する
	 *
	 * <p>
	 * トークン復号時に、ここで設定されている文字コードを利用する。
	 * </p>
	 *
	 * @return デフォルト文字コード
	 */
	protected Charset getDefaultCharset() {
		return defaultCharset;
	}

	/**
	 * デフォルト文字コードを設定する
	 * @param defaultCharset デフォルト文字コード
	 */
	protected void setDefaultCharset(Charset defaultCharset) {
		this.defaultCharset = defaultCharset;
	}

	/**
	 * リクエストパラメータから Xsrf トークンを取得する
	 * @param requestParameterList リクエストパラメータ
	 * @return Xsrf トークン
	 */
	private XsrfToken getXsrfToken(List<MultipartRequestParameter> requestParameterList) {
		Optional<MultipartRequestParameter> tokenItem = requestParameterList.stream()
				.filter(item -> XsrfProtectedMultipartConstant.RequestParameterName.XSRF_TOKEN_KEY.equals(item.getFieldName())).findFirst();
		return tokenItem.isPresent() ? new XsrfToken(tokenItem.get().getString(getDefaultCharset())) : null;
	}
}

