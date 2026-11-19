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

package org.iplass.adminconsole.client.base.io.download;

import java.util.Map;

import org.iplass.adminconsole.shared.base.io.XsrfProtectedConstant;
import org.iplass.adminconsole.shared.base.rpc.AdminXsrfTokenHolder;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.XsrfToken;

/**
 * XSRFトークン付きPOSTでバイナリを取得し、表示用のblob URLを返すローダー。
 */
final class XsrfProtectedImageLoader {

	/**
	 * 取得結果の通知先。
	 */
	interface Callback {

		/**
		 * 取得に成功した場合に呼び出される。
		 *
		 * @param objectUrl 表示に使用するblob URL
		 */
		void onLoad(String objectUrl);

		/**
		 * 取得に失敗した場合に呼び出される。
		 *
		 * @param status HTTPステータス（取得できなかった場合は 0）
		 */
		void onError(int status);
	}

	private XsrfProtectedImageLoader() {
	}

	/**
	 * XSRFトークンを付与したPOSTでバイナリを取得し、blob URLを生成する。
	 *
	 * @param url ダウンロードサーブレットのURL
	 * @param params リクエストパラメータ
	 * @param callback 取得結果の通知先
	 */
	public static void load(String url, Map<String, String> params, Callback callback) {
		StringBuilder body = new StringBuilder();
		for (Map.Entry<String, String> param : params.entrySet()) {
			if (param.getValue() == null) {
				continue;
			}
			appendParameter(body, param.getKey(), param.getValue());
		}

		XsrfToken token = AdminXsrfTokenHolder.token();
		appendParameter(body, XsrfProtectedConstant.RequestParameterName.XSRF_TOKEN_KEY,
				token != null ? token.getToken() : "");

		request(url, body.toString(), callback);
	}

	/**
	 * 生成したblob URLを解放する。
	 *
	 * @param objectUrl 解放するblob URL
	 */
	public static void release(String objectUrl) {
		if (objectUrl != null && !objectUrl.isEmpty()) {
			revokeObjectUrl(objectUrl);
		}
	}

	private static void appendParameter(StringBuilder body, String name, String value) {
		if (body.length() > 0) {
			body.append('&');
		}
		body.append(URL.encodeQueryString(name));
		body.append('=');
		body.append(URL.encodeQueryString(value));
	}

	private static native void request(String url, String body, Callback callback) /*-{
		try {
			var xhr = new $wnd.XMLHttpRequest();
			xhr.open('POST', url, true);
			xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
			xhr.responseType = 'blob';
			xhr.onload = function() {
				if (xhr.status === 200) {
					var objectUrl = $wnd.URL.createObjectURL(xhr.response);
					callback.@org.iplass.adminconsole.client.base.io.download.XsrfProtectedImageLoader.Callback::onLoad(Ljava/lang/String;)(objectUrl);
				} else {
					callback.@org.iplass.adminconsole.client.base.io.download.XsrfProtectedImageLoader.Callback::onError(I)(xhr.status);
				}
			};
			xhr.onerror = function() {
				callback.@org.iplass.adminconsole.client.base.io.download.XsrfProtectedImageLoader.Callback::onError(I)(xhr.status || 0);
			};
			xhr.send(body);
		} catch (e) {
			callback.@org.iplass.adminconsole.client.base.io.download.XsrfProtectedImageLoader.Callback::onError(I)(0);
		}
	}-*/;

	private static native void revokeObjectUrl(String objectUrl) /*-{
		$wnd.URL.revokeObjectURL(objectUrl);
	}-*/;
}
