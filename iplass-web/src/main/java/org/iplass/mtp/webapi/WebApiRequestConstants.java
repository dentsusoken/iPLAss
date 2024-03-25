/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.webapi;

import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.RequestInfo;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

public interface WebApiRequestConstants {

	/** WebAPIのリクエスト種別を取得する際のattribute名です。{@link RequestType}のいずれかの値が取得されます。 */
	public static final String REQUEST_TYPE = "requestType";

	/** WebAPIのHTTPメソッドを取得する際のattribute名です。{@link MethodType}のいずれかの値が取得されます。 */
	public static final String HTTP_METHOD = "httpMethod";

	/** WebApi名を取得する際のattribute名です。 */
	public static final String API_NAME = "webApiName";
	/**
	 * WebApi呼び出しの実行結果として、クライアントへ返却するオブジェクトをrequestContextへ格納する際の名前で、
	 * 定義にて未設定の場合のデフォルトの名前です。
	 * 実際の値は、"result"です。
	 */
	public static final String DEFAULT_RESULT = "result";

	/** ResponseHeaderのインスタンス。Command内でレスポンスにCokkie、Headerをセットしたい場合利用。 */
	public static final String RESPONSE_HEADER = WebRequestConstants.RESPONSE_HEADER;
	
	/** HTTPヘッダ（リクエストの）が格納されているMapのインスタンス。*/
	public static final String HTTP_HEADER = WebRequestConstants.HTTP_HEADER;
	
	/** attribute経由でパラメータMapを取得する際のattribute名。取得できるインスタンスは、RequestContext#getParamMap()と同じ。 */
	public static final String PARAM = WebRequestConstants.PARAM;
	
	/** このリクエストが呼び出された際のHttpServletReqeustを取得する際のattribute名。取得されるインスタンスは、implements {@link HttpServletRequest}, {@link RequestInfo}の形。情報参照系のメソッドのみ利用可能。利用可能なメソッドは、{@link RequestInfo}に定義されているメソッド。 */
	public static final String SERVLET_REQUEST = WebRequestConstants.SERVLET_REQUEST;
	
	/** REST FORM, SOAP WSDLで利用するデフォルトのパラメータ名です。 */
	public static final String DEFAULT_PARAM_NAME = WebRequestConstants.PARAM;

	/** このリクエストがwebApi経由の呼び出しか否かを判断するフラグ（Boolean）を取得する際のattribute名 */
	public static final String WEB_API = "webApi";
	
	/** WebApi名以降のサブパス文字列を取得する際のattribute名 */
	public static final String SUB_PATH = WebRequestConstants.SUB_PATH;

}
