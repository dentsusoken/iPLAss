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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.XsrfToken;
import com.google.gwt.user.server.rpc.XsrfProtectedServiceServlet;

/**
 * XSRF トークン検証を行うサーブレットの基底クラス。
 *
 * {@link #service(HttpServletRequest, HttpServletResponse)} は以下の順でリクエストを処理する。
 * 1. {@link #checkRequest(HttpServletRequest)} によるリクエスト制約チェック
 * 2. {@link #prepareRequest(HttpServletRequest)} による前準備
 * 3. {@link #extractXsrfToken(HttpServletRequest, Object)} によるトークン抽出と検証
 * 4. {@link #doProtectedService(HttpServletRequest, HttpServletResponse, Object)} による業務処理
 * 5. {@link #cleanupRequest(Object)} によるリソース破棄（finally で実行）
 *
 * @param <T> {@link #prepareRequest(HttpServletRequest)} が返し、後続処理へ渡すリクエストコンテキストの型
 */
public abstract class XsrfProtectedServlet<T> extends XsrfProtectedServiceServlet {
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

		T context = prepareRequest(req);
		try {
			// XsrfProtectedServiceServlet#validateXsrfToken(RpcToken, Method) を利用するための準備
			perThreadRequest.set(req);
			perThreadResponse.set(resp);

			XsrfToken token = extractXsrfToken(req, context);
			if (!isValidXsrfToken(token)) {
				rejectRequest(req, resp, HttpServletResponse.SC_FORBIDDEN, "invalid xsrf token");
				return;
			}

			doProtectedService(req, resp, context);

		} finally {
			perThreadRequest.set(null);
			perThreadResponse.set(null);
			cleanupRequest(context);
		}
	}

	/**
	 * リクエストの制約をチェックする。
	 *
	 * @param req リクエスト
	 * @return 制約を満たす場合は 0、満たさない場合は応答するHTTPステータス
	 */
	protected int checkRequest(final HttpServletRequest req) {
		return METHOD_POST.equals(req.getMethod()) ? 0 : HttpServletResponse.SC_METHOD_NOT_ALLOWED;
	}

	/**
	 * トークン検証および業務処理の前に必要なリクエスト準備を行う。
	 *
	 * @param req リクエスト
	 * @return 後続処理に渡すコンテキスト（不要な場合は null）
	 * @throws ServletException サーブレット例外
	 * @throws IOException 入出力例外
	 */
	protected T prepareRequest(final HttpServletRequest req) throws ServletException, IOException {
		return null;
	}

	/**
	 * リクエストまたはコンテキストから Xsrf トークンを取得する。
	 *
	 * @param req リクエスト
	 * @param context リクエスト処理コンテキスト（prepareRequest の戻り値）
	 * @return Xsrf トークン（存在しない場合は null）
	 */
	protected abstract XsrfToken extractXsrfToken(final HttpServletRequest req, final T context);

	/**
	 * 制約チェックおよび XSRF トークン検証を通過したリクエストを処理する。
	 *
	 * @param req リクエスト
	 * @param resp レスポンス
	 * @param context リクエスト処理コンテキスト（prepareRequest の戻り値）
	 * @throws ServletException サーブレット例外
	 * @throws IOException 入出力例外
	 */
	protected abstract void doProtectedService(final HttpServletRequest req, final HttpServletResponse resp,
			final T context) throws ServletException, IOException;

	/**
	 * リクエスト処理の前準備で確保したリソースを破棄する。
	 *
	 * @param context リクエスト処理コンテキスト（prepareRequest の戻り値）
	 */
	protected void cleanupRequest(final T context) {
		// デフォルトでは何もしない
	}

	/**
	 * リクエストをエラーとして応答し、その内容をログに出力する。
	 *
	 * @param req リクエスト
	 * @param resp レスポンス
	 * @param httpStatus 応答するHTTPステータス
	 * @param message エラーメッセージ（指定しない場合は null）
	 * @throws IOException 入出力例外
	 */
	private void rejectRequest(final HttpServletRequest req, final HttpServletResponse resp,
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
	 * Xsrf トークンを検証する。
	 *
	 * @param token Xsrf トークン
	 * @return 検証結果
	 */
	private boolean isValidXsrfToken(final XsrfToken token) {
		try {
			super.validateXsrfToken(token, null);
			return true;
		} catch (RpcTokenException e) {
			// 403 応答自体は rejectRequest で error 出力するため、例外詳細は debug とする
			logger.debug("Invalid xsrf token. {}", e.getMessage());
			return false;
		}
	}
}
