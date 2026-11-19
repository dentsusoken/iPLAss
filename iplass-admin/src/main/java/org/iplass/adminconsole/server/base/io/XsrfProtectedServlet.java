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

import org.iplass.adminconsole.shared.base.io.XsrfProtectedConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.server.rpc.jakarta.XsrfProtectedServiceServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * XsrfProtected Servlet
 *
 */
public abstract class XsrfProtectedServlet extends XsrfProtectedServiceServlet {
	/** serialVersionUID */
	private static final long serialVersionUID = 5183467290123847361L;
	/** POST */
	protected static final String METHOD_POST = "POST";

	/** ロガー */
	private static final Logger logger = LoggerFactory.getLogger(XsrfProtectedServlet.class);

	/**
	 * デフォルトコンストラクタ
	 */
	public XsrfProtectedServlet() {
		super();
		perThreadRequest = new ThreadLocal<HttpServletRequest>();
		perThreadResponse = new ThreadLocal<HttpServletResponse>();
	}

	@Override
	protected final void service(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		int rejectedStatus = checkRequest(req);
		if (rejectedStatus != 0) {
			rejectRequest(req, resp, rejectedStatus, null);
			return;
		}

		try {
			// XsrfProtectedServiceServlet#validateXsrfToken(RpcToken, Method) を利用するための準備
			perThreadRequest.set(req);
			perThreadResponse.set(resp);

			doProtectedService(req, resp);

		} finally {
			perThreadRequest.set(null);
			perThreadResponse.set(null);
		}
	}

	/**
	 * リクエストの制約をチェックする。
	 *
	 * @param req リクエスト
	 * @return 制約を満たす場合は 0、満たさない場合は応答するHTTPステータス
	 * @throws IOException 入出力例外
	 */
	protected int checkRequest(final HttpServletRequest req) throws IOException {
		return METHOD_POST.equals(req.getMethod()) ? 0 : HttpServletResponse.SC_METHOD_NOT_ALLOWED;
	}

	/**
	 * 制約を満たしたリクエストを処理する。
	 *
	 * @param req リクエスト
	 * @param resp レスポンス
	 * @throws ServletException サーブレット例外
	 * @throws IOException 入出力例外
	 */
	protected abstract void doProtectedService(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException;

	/**
	 * リクエストをエラーとして応答し、その内容をログに出力する。
	 *
	 * @param req リクエスト
	 * @param resp レスポンス
	 * @param httpStatus 応答するHTTPステータス
	 * @param message エラーメッセージ（指定しない場合は null）
	 * @throws IOException 入出力例外
	 */
	protected final void rejectRequest(final HttpServletRequest req, final HttpServletResponse resp,
			final int httpStatus, final String message) throws IOException {
		HttpSession session = req.getSession(false);
		String sessionId = session != null ? session.getId() : null;
		logger.error("Error request. sessionId = {}, httpStatus = {}, uri = {}, content-length={}.",
				sessionId, httpStatus, req.getRequestURI(), req.getContentLengthLong());

		if (message != null) {
			resp.sendError(httpStatus, message);
		} else {
			resp.sendError(httpStatus);
		}
	}

	/**
	 * リクエストパラメータから Xsrf トークンを取得する
	 *
	 * @param req リクエスト
	 * @return Xsrf トークン
	 */
	protected String extractXsrfToken(final HttpServletRequest req) {
		return req.getParameter(XsrfProtectedConstant.RequestParameterName.XSRF_TOKEN_KEY);
	}

	/**
	 * リクエストの Xsrf トークンを検証する。
	 *
	 * @param req リクエスト
	 * @return 検証結果
	 */
	protected final boolean isValidXsrfToken(final HttpServletRequest req) {
		try {
			super.validateXsrfToken(new XsrfToken(extractXsrfToken(req)), null);
			return true;
		} catch (RpcTokenException e) {
			logger.debug("Invalid xsrf token. {}", e.getMessage());
			return false;
		}
	}
}
