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
package org.iplass.mtp.impl.auth.authenticate.token.web;

import org.iplass.mtp.webapi.definition.MethodType;

/**
 * BearerToken提供インターフェース
 * <p>
 * {@link org.iplass.mtp.impl.auth.authenticate.token.web.BearerTokenAutoLoginHandler} で使用されるBearerTokenを提供するインターフェースです。<br>
 * 本インターフェースは、{@link org.iplass.mtp.command.RequestContext} の派生クラスで、BearerTokenを用いた認証を利用するクラスに実装されることを想定しています。
 * </p>
 * @author SEKIGUCHI Naoya
 */
public interface BearerTokenSupplier {
	/** Authorizationヘッダーのキー名 */
	static final String HEADER_AUTHORIZATION = BearerTokenAutoLoginHandler.HEADER_AUTHORIZATION;

	/**
	 * BearerTokenをサポートするかどうかを返します。
	 * @return BearerTokenをサポートする場合はtrue、そうでない場合はfalse
	 */
	boolean supportBearerToken();

	/**
	 * Authorization ヘッダーの値を提供します。
	 * <p>
	 * "Bearer {token}" の形式で提供されることが期待されます。
	 * </p>
	 * @return Authorization ヘッダーの値
	 */
	String getAuthorizationHeaderValue();

	/**
	 * HTTPリクエストのMethodTypeを返します
	 * <p>
	 * ヘッダーから認証情報を取得できない場合に、フォームからトークンの取得を試みる際の検証に使用します。<br>
	 * ヘッダーからトークンを取得できることが保証されていれば、必ずしも実装する必要はありません。
	 * </p>
	 * @return HTTPリクエストのMethodType
	 */
	default MethodType methodType() {
		return null;
	}

	/**
	 * HTTPリクエストのContent-Typeを返します。
	 * <p>
	 * ヘッダーから認証情報を取得できない場合に、フォームからトークンの取得を試みる際の検証に使用します。<br>
	 * ヘッダーからトークンを取得できることが保証されていれば、必ずしも実装する必要はありません。
	 * </p>
	 * @return HTTPリクエストのContent-Type
	 */
	default String getContentType() {
		return null;
	}
}
