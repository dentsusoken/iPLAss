/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.util;

import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.WebFrontendService;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;

/**
 * RequestPath ユーティリティ
 * <p>
 * HttpServletRequest から Web API のルートパスやテナントのルートパスを取得するためのユーティリティクラスです。<br>
 * 引数に HttpServletRequest が指定されていないメソッドは、 {@link org.iplass.mtp.impl.web.WebRequestStack} から取得します。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public class RequestPathUtil {
	/** 標準HTTPポート */
	private static final int DEFAULT_HTTP_PORT = 80;
	/** 標準HTTPSポート*/
	private static final int DEFAULT_HTTPS_PORT = 443;
	/** HTTPスキーム */
	private static final String HTTP_SCHEME = "http";
	/** HTTPSスキーム */
	private static final String HTTPS_SCHEME = "https";

	/**
	 * プライベートコンストラクタ
	 */
	private RequestPathUtil() {
	}

	/**
	 * Web API パスを取得する
	 * <p>
	 * WebFrontendService に設定されている RestPath の値を取得します。
	 * </p>
	 * @param pathIndex RestPath のインデックス
	 * @return WebAPI パス文字列
	 */
	public static String getWebApiPath(int pathIndex) {
		return ServiceRegistry.getRegistry().getService(WebFrontendService.class).getRestPath().get(pathIndex);
	}

	/**
	 * Web API パスを取得する
	 * <p>
	 * WebFrontendService に設定されている RestPath の先頭の値を取得します。
	 * </p>
	 * @return WebAPI パス文字列
	 */
	public static String getWebApiPath() {
		return getWebApiPath(0);
	}

	/**
	 * テナントのルートパスを取得する
	 * <p>
	 * テナントルートパスは <value>https://hostname:443/context/tenant/</value> のような形です。
	 * </p>
	 * @param request HttpServletRequest
	 * @return テナントルートパス
	 */
	public static String getTenantRoot(HttpServletRequest request) {
		StringBuilder url = new StringBuilder();

		String scheme = request.getScheme();
		int port = request.getServerPort();
		String contextPath = request.getContextPath();
		String tenantUrl = ExecuteContext.getCurrentContext().getCurrentTenant().getUrl();

		if (port < 0) {
			port = scheme.equals(HTTP_SCHEME) ? DEFAULT_HTTP_PORT : DEFAULT_HTTPS_PORT;
		}

		// リバースプロキシを利用した場合でも正しくとれる
		url.append(scheme).append("://").append(request.getServerName());
		if ((scheme.equals(HTTP_SCHEME) && (port != DEFAULT_HTTP_PORT)) || (scheme.equals(HTTPS_SCHEME) && (port != DEFAULT_HTTPS_PORT))) {
			url.append(':').append(port);
		}

		if (StringUtil.isNotEmpty(contextPath)) {
			url.append(contextPath);
		}

		if (StringUtil.isNotEmpty(tenantUrl)) {
			url.append(tenantUrl);
		}

		return url.toString();
	}

	/**
	 * テナントルートパスを取得します。
	 * @see #getTenantRoot(HttpServletRequest)
	 * @return テナントルートパス
	 */
	public static String getTenantRoot() {
		return getTenantRoot(WebRequestStack.getCurrent().getRequest());
	}

	/**
	 * Web API ルートパスを取得する
	 * <p>
	 * WebAPI ルートパスは <value>https://hostname:443/context/tenant/api/</value> のような形式です。
	 * </p>
	 * @param request HttpServletRequest
	 * @param webApiPath WebAPI パス文字列
	 * @return WebAPIルートパス
	 */
	public static String getWebApiRoot(HttpServletRequest request, String webApiPath) {
		return getTenantRoot(request) + webApiPath;
	}

	/**
	 * Web API ルートパスを取得する
	 * @see #getWebApiRoot(HttpServletRequest, String)
	 * @param request HttpServletRequest
	 * @return WebAPIルートパス
	 */
	public static String getWebApiRoot(HttpServletRequest request) {
		return getWebApiRoot(request, getWebApiPath());
	}

	/**
	 * Web API ルートパスを取得する
	 * @see #getWebApiRoot(HttpServletRequest, String)
	 * @param webApiPath WebAPI パス文字列
	 * @return WebAPIルートパス
	 */
	public static String getWebApiRoot(String webApiPath) {
		return getWebApiRoot(WebRequestStack.getCurrent().getRequest(), webApiPath);
	}

	/**
	 * Web API ルートパスを取得する
	 * @see #getWebApiRoot(HttpServletRequest, String)
	 * @return WebAPIルートパス
	 */
	public static String getWebApiRoot() {
		return getWebApiRoot(WebRequestStack.getCurrent().getRequest());
	}
}
