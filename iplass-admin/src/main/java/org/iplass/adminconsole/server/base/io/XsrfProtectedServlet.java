/*
 * Copyright (C) 2026 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.server.base.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.iplass.adminconsole.shared.base.io.XsrfProtectedMultipartConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * XsrfProtected Servlet（ダウンロード用）
 *
 */
public abstract class XsrfProtectedServlet extends XsrfProtectedServiceServlet {
	/** serialVersionUID */
	private static final long serialVersionUID = 5183467290123847361L;
	/** POST */
	private static final String METHOD_POST = "POST";

	/** ロガー */
	private Logger logger = LoggerFactory.getLogger(XsrfProtectedServlet.class);

	/**
	 * デフォルトコンストラクタ
	 */
	public XsrfProtectedServlet() {
		super();
		perThreadRequest = new ThreadLocal<HttpServletRequest>();
		perThreadResponse = new ThreadLocal<HttpServletResponse>();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected final void service(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		// 制約外リクエストの場合、405: Method Not Allowed を返却する
		if (!METHOD_POST.equals(req.getMethod())) {
			HttpSession session = req.getSession(false);
			String sessionId = session != null ? session.getId() : null;
			int httpStatus = HttpServletResponse.SC_METHOD_NOT_ALLOWED;

			// エラーログ出力
			logger.error("Error request. sessionId = {}, httpStatus = {}, uri = {}.",
					sessionId, httpStatus, req.getRequestURI());
			// http status 設定
			resp.sendError(httpStatus);
			return;
		}

		try {
			// XsrfProtectedServiceServlet#validateXsrfToken(RpcToken, Method) を利用するための準備
			perThreadRequest.set(req);
			perThreadResponse.set(resp);

			// リクエストの文字コード設定
			req.setCharacterEncoding(StandardCharsets.UTF_8.name());

			// トークン検証
			if (!validateXsrfToken(req)) {
				HttpSession session = req.getSession(false);
				String sessionId = session != null ? session.getId() : null;
				// トークン検証エラーの場合、403: Forbidden を返却する
				int httpStatus = HttpServletResponse.SC_FORBIDDEN;

				// エラーログ出力
				logger.error("Error request. sessionId = {}, httpStatus = {}, uri = {}.",
						sessionId, httpStatus, req.getRequestURI());
				// http status 設定
				resp.sendError(httpStatus, "invalid xsrf token");
				return;
			}

			doProtectedService(req, resp);

		} finally {
			perThreadRequest.set(null);
			perThreadResponse.set(null);
		}
	}

	/**
	 * POST メソッドのリクエスト処理
	 *
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 * @throws ServletException サーブレット例外
	 * @throws IOException 入出力例外
	 */
	protected abstract void doProtectedService(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException;

	/**
	 * リクエストパラメータから Xsrf トークンを取得する
	 * @param req HttpServletRequest
	 * @return Xsrf トークン
	 */
	protected String extractXsrfToken(final HttpServletRequest req) {
		return req.getParameter(XsrfProtectedMultipartConstant.RequestParameterName.XSRF_TOKEN_KEY);
	}

	/**
	 * リクエストの Xsrf トークンを検証する。
	 *
	 * @param req リクエスト
	 * @return 検証結果
	 */
	protected boolean validateXsrfToken(final HttpServletRequest req) {
		try {
			super.validateXsrfToken(new XsrfToken(extractXsrfToken(req)), null);
			return true;
		} catch (RpcTokenException e) {
			logger.debug("Invalid xsrf token. {}", e.getMessage());
			return false;
		}
	}
}
