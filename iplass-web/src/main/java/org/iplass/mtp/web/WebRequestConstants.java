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

package org.iplass.mtp.web;

import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.web.actionmapping.RequestInfo;

/**
 * Webからの呼び出しの際、RequestContextで取得可能なattributeの名前の定数。
 *
 * @author K.Higuchi
 *
 */
public interface WebRequestConstants {

	/** Commandの実行結果ステータス。Template内でのみ取得可能。 */
	public static final String COMMAND_RESULT = "commandResult";

	/** Action名。Command内、Template内で取得可能。 */
	public static final String ACTION_NAME = "actionName";

	/** Action呼び出しにて実行されたCommandのインスタンス。Template内でのみ取得可能。 */
	public static final String EXECUTED_COMMAND = "executedCommand";

	/** Command実行時にExceptionが発生した場合、エラー画面Templateで当該のキー名でExceptionを取得可能。 */
	public static final String EXCEPTION = "mtp.web.exception";

	/** ResponseHeaderのインスタンス。Command内でレスポンスにCokkie、Headerをセットしたい場合利用。 */
	public static final String RESPONSE_HEADER = "responseHeader";

	/** RedirectPathを取得する為のキー */
	public static final String REDIRECT_PATH = "redirectPath";


	/** HTTPヘッダ（リクエストの）が格納されているMapのインスタンス。*/
	public static final String HTTP_HEADER = "header";

	/** attribute経由でパラメータMapを取得する際のattribute名。取得できるインスタンスは、RequestContext#getParamMap()と同じ。 */
	public static final String PARAM = "param";

	/** このリクエストが呼び出された際のHttpServletReqeustを取得する際のattribute名。取得されるインスタンスは、implements {@link HttpServletRequest}, {@link RequestInfo}の形。情報参照系のメソッドのみ利用可能。利用可能なメソッドは、{@link RequestInfo}に定義されているメソッド。 */
	public static final String SERVLET_REQUEST = "servletRequest";

	/** このリクエストがAction経由の呼び出しか否かを判断するフラグ（Boolean）を取得する際のattribute名 */
	public static final String ACTION = "action";

	/** テナントまでのパス文字列を取得する際のattribute名 */
	public static final String TENANT_CONTEXT_PATH = "tenantContextPath";

	/** アクション名以降のサブパス文字列を取得する際のattribute名 */
	public static final String SUB_PATH = "subPath";

}
