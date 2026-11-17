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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * XSRFトークン検証付きのServlet基底クラス。
 *
 */
public abstract class XsrfProtectedServlet extends HttpServlet {

	private static final long serialVersionUID = 5183467290123847361L;

	/** XSRFトークンのリクエストパラメータ名 */
	protected static final String PARAM_XSRF_TOKEN = "xsrfToken";

	/** セッションCookie名のcontext-param名 */
	protected static final String PARAM_SESSION_COOKIE_NAME = "gwt.xsrf.session_cookie_name";

	/** セッションCookie名の初期値 */
	private static final String DEFAULT_SESSION_COOKIE_NAME = "JSESSIONID";

	/** セッションCookie名を保持するServletContext属性キー */
	private static final String CONTEXT_ATTR_SESSION_COOKIE_NAME =
			XsrfProtectedServlet.class.getName() + ".sessionCookieName";

	@Override
	public void init() throws ServletException {
		super.init();

		//セッションCookie名をServletContextに保持する
		String name = getServletConfig().getInitParameter(PARAM_SESSION_COOKIE_NAME);
		if (name == null || name.isEmpty()) {
			name = getServletContext().getInitParameter(PARAM_SESSION_COOKIE_NAME);
		}
		if (name == null || name.isEmpty()) {
			name = DEFAULT_SESSION_COOKIE_NAME;
		}
		getServletContext().setAttribute(CONTEXT_ATTR_SESSION_COOKIE_NAME, name);
	}

	@Override
	protected final void service(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding(StandardCharsets.UTF_8.name());

		//XSRFトークンの検証
		if (!validateXsrfToken(req)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, "invalid xsrf token");
			return;
		}

		doProtectedService(req, resp);
	}

	/**
	 * XSRFトークン検証済みのリクエストを処理する。
	 *
	 * @param req リクエスト
	 * @param resp レスポンス
	 * @throws ServletException Servlet例外
	 * @throws IOException IO例外
	 */
	protected abstract void doProtectedService(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException;

	/**
	 * リクエストからXSRFトークンを取り出す。
	 *
	 * @param req リクエスト
	 * @return XSRFトークン
	 */
	protected String extractXsrfToken(final HttpServletRequest req) {
		return req.getParameter(PARAM_XSRF_TOKEN);
	}

	/**
	 * XSRFトークンを検証する。
	 *
	 * @param req リクエスト
	 * @return 検証結果
	 */
	protected boolean validateXsrfToken(final HttpServletRequest req) {

		final String token = extractXsrfToken(req);
		if (token == null || token.isEmpty()) {
			return false;
		}

		final String sessionId = getSessionCookieValue(req);
		if (sessionId == null || sessionId.isEmpty()) {
			return false;
		}

		final String expected = md5Hex(sessionId);
		if (expected == null) {
			return false;
		}

		//大文字小文字の揺れを吸収して比較する
		final byte[] actualBytes = token.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8);
		final byte[] expectedBytes = expected.toLowerCase(Locale.ROOT).getBytes(StandardCharsets.UTF_8);
		return MessageDigest.isEqual(actualBytes, expectedBytes);
	}

	/**
	 * セッションCookieの値を取得する。
	 *
	 * @param req リクエスト
	 * @return セッションID
	 */
	private String getSessionCookieValue(final HttpServletRequest req) {

		Object attr = getServletContext().getAttribute(CONTEXT_ATTR_SESSION_COOKIE_NAME);
		final String cookieName = (attr instanceof String) ? (String) attr : DEFAULT_SESSION_COOKIE_NAME;

		final Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookieName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}

		//Cookieが取得できない場合はセッションIDにフォールバックする
		final HttpSession session = req.getSession(false);
		return (session == null) ? null : session.getId();
	}

	/**
	 * 文字列のMD5(16進小文字)を返す。
	 *
	 * @param value 対象文字列
	 * @return MD5の16進文字列表現
	 */
	private String md5Hex(final String value) {

		try {
			final MessageDigest digest = MessageDigest.getInstance("MD5");
			final byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));

			final StringBuilder sb = new StringBuilder(bytes.length * 2);
			for (byte b : bytes) {
				sb.append(Character.forDigit((b >> 4) & 0xF, 16));
				sb.append(Character.forDigit(b & 0xF, 16));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			//MD5は必ず存在するため通常到達しない
			getServletContext().log("failed to calculate md5.", e);
			return null;
		}
	}
}
