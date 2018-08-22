/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.web.actionmapping;

import javax.servlet.http.Cookie;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.web.WebRequestConstants;

/**
 * HTTPレスポンスのヘッダーに値をセットしたい際に利用するインタフェース。
 * Webからの呼び出しの際、CommandもしくはGroovyTempalte（※JSPで定義されたTemplate内では実行不可。LayoutActionにてJSPのTemplateで定義されたActionを指定した場合も不可）にて、
 * {@link RequestContext}のattributeから取得可能（attribute名は、{@link WebRequestConstants}を参照のこと）。
 * もしくは、 {@link ActionUtil#getResponseHeader()}を利用し取得可能。
 * 
 * 提供するメソッドは、HttpServletResponseにて定義されているメソッドのうちヘッダー操作系のメソッド。
 * 
 * 
 * @author K.Higuchi
 *
 */
public interface ResponseHeader {
	
	public void addCookie(Cookie cookie);
	public boolean containsHeader(String name);
	
	//TODO sendErrorは非公開の方向か。例外をスローの形（ただ現状だとエラーコードは500限定）
//	public void sendError(int sc, String msg);
//	public void sendError(int sc);
	public void setDateHeader(String name, long date);
	public void addDateHeader(String name, long date);
	public void setHeader(String name, String value);
	public void addHeader(String name, String value);
	public void setIntHeader(String name, int value);
	public void addIntHeader(String name, int value);
	public void setStatus(int sc);
}
