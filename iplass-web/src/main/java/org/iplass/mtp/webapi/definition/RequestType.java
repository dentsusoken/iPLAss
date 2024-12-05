/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.webapi.definition;

/**
 * WebApiリクエストの受け入れ可能な形式を表す定義です。
 * WebApiを呼び出す際の、HTTPリクエストヘッダーのContent-Typeヘッダーに対応します。
 *
 * @author K.Higuchi
 *
 */
public enum RequestType {

	/**
	 * フォーム形式でのリクエストを表します。
	 * リクエストのパラメータは通常のHTTPパラメータで受け渡されます。
	 * WebApiリクエスト時のContent-Typeに"application/x-www-form-urlencoded","multipart/form-data"が指定された場合、
	 * また、GET/DELETE時にContent-Type未指定の場合、リクエストはこの形式とみなされて処理されます。
	 */
	REST_FORM,

	/**
	 * JSON形式でのリクエストを表します。
	 * リクエストのパラメータはJSON形式で受け渡されます。
	 * WebApiリクエスト時のContent-Typeに"application/json"が指定された場合リクエストはこの形式とみなされて処理されます。
	 */
	REST_JSON,

	/**
	 * XML形式でのリクエストを表します。
	 * リクエストのパラメータはXML形式で受け渡されます。
	 * WebApiリクエスト時のContent-Typeに"application/xml"が指定された場合リクエストはこの形式とみなされて処理されます。
	 */
	REST_XML,

	/**
	 * 上記以外のリクエストを表します。
	 * リクエストのパラメータは InputStream 形式で受け渡されます。
	 */
	REST_OTHERS;

	/**
	 * 全てのリクエスト形式
	 */
	public static RequestType[] ACCEPT_ALL = values();
}
