/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.web.WebRequestConstants;

/**
 * <% if (doclang == "ja") {%>
 * Web経由のリクエストの際の、HttpServletRequestから取得可能な情報を表す。
 * HttpServletRequestの一部メソッドを利用した場合、基盤の挙動を保証することが難しいため、
 * ラップするクラスを定義し、利用可能な情報のみ取得可能にしたもの。
 * 
 * Webからの呼び出しの際、CommandもしくはGroovyTemplateにて、
 * {@link RequestContext}のattributeから取得可能（attribute名は、{@link WebRequestConstants}を参照のこと）。
 * 
 * <%} else {%>
 * Represents available methods of HttpServletRequest.
 * When using some methods of HttpServletRequest,
 * it is difficult to guarantee iPLAss's behavior,
 * so the interface is defined and only the methods that can be used are defined.
 * 
 * When invoke from the web, in Command or GroovyTemplate,
 * It can be get from the attribute of {@link RequestContext}.
 * see {@link WebRequestConstants} for attribute name.
 * 
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public interface RequestInfo {

	/**
	 * <% if (doclang == "ja") {%>
	 * リクエストボディのInputStreamを取得します。
	 * リクエストのヘッダーでcontent-typeが指定されていない場合、ボディは空として扱います。
	 * 
	 * <%} else {%>
	 * Get InputStream of request body.
	 * If content-type is not specified in the request header,
	 * the body is treated as empty.
	 * 
	 * <%}%>
	 * 
	 * @see ServletRequest#getInputStream()
	 * @return
	 * @throws IOException
	 */
	public ServletInputStream getInputStream() throws IOException;

	/**
	 * <% if (doclang == "ja") {%>
	 * リクエストボディのReaderを取得します。
	 * リクエストのヘッダーでcontent-typeが指定されていない場合、ボディは空として扱います。
	 * 
	 * <%} else {%>
	 * Get Reader of request body.
	 * If content-type is not specified in the request header,
	 * the body is treated as empty.
	 * 
	 * <%}%>
	 * 
	 * @see ServletRequest#getReader()
	 * @return
	 * @throws IOException
	 */
	public BufferedReader getReader() throws IOException;

	/**
	 * @see ServletRequest#getCharacterEncoding()
	 * @return
	 */
	public String getCharacterEncoding();

	/**
	 * @see ServletRequest#getContentLength()
	 * @return
	 */
	public int getContentLength();

	/**
	 * @see ServletRequest#getContentType()
	 * @return
	 */
	public String getContentType();

	/**
	 * <% if (doclang == "ja") {%>
	 * 現在のリクエストに最適の言語をLocaleの形式で取得します。
	 * 
	 * <%} else {%>
	 * Get the appropriate locale of language for the current request.
	 * 
	 * <%}%>
	 * 
	 * @see ServletRequest#getLocale()
	 * @return
	 */
	public Locale getLocale();

	/**
	 * @see ServletRequest#getProtocol()
	 * @return
	 */
	public String getProtocol();

	/**
	 * @see ServletRequest#getRemoteAddr()
	 * @return
	 */
	public String getRemoteAddr();

	/**
	 * @see ServletRequest#getRemoteHost()
	 * @return
	 */
	public String getRemoteHost();

	/**
	 * @see ServletRequest#getRemotePort()
	 * @return
	 */
	public int getRemotePort();

	/**
	 * @see ServletRequest#getScheme()
	 * @return
	 */
	public String getScheme();

	/**
	 * @see ServletRequest#getServerName()
	 * @return
	 */
	public String getServerName();

	/**
	 * @see ServletRequest#getServerPort()
	 * @return
	 */
	public int getServerPort();

	/**
	 * @see ServletRequest#isSecure()
	 * @return
	 */
	public boolean isSecure();

	/**
	 * @see HttpServletRequest#getAuthType()
	 * @return
	 */
	public String getAuthType();

	
	/**
	 * @see HttpServletRequest#getContextPath()
	 * @return
	 */
	public String getContextPath();

	/**
	 * @see HttpServletRequest#getCookies()
	 * @return
	 */
	public Cookie[] getCookies();

	/**
	 * @see HttpServletRequest#getDateHeader(String)
	 * @param name
	 * @return
	 */
	public long getDateHeader(String name);

	/**
	 * @see HttpServletRequest#getHeader(String)
	 * @param name
	 * @return
	 */
	public String getHeader(String name);

	/**
	 * @see HttpServletRequest#getHeaderNames()
	 * @return
	 */
	public Enumeration<String> getHeaderNames();

	/**
	 * @see HttpServletRequest#getHeaders(String)
	 * @param name
	 * @return
	 */
	public Enumeration<String> getHeaders(String name);

	/**
	 * @see HttpServletRequest#getIntHeader(String)
	 * @param name
	 * @return
	 */
	public int getIntHeader(String name);

	/**
	 * @see HttpServletRequest#getMethod()
	 * @return
	 */
	public String getMethod();

	
	/**
	 * @see HttpServletRequest#getPathInfo()
	 * @return
	 */
	public String getPathInfo();

	/**
	 * @see HttpServletRequest#getQueryString()
	 * @return
	 */
	public String getQueryString();

	/**
	 * @see HttpServletRequest#getRemoteUser()
	 * @return
	 */
	public String getRemoteUser();

	/**
	 * @see HttpServletRequest#getRequestURI()
	 * @return
	 */
	public String getRequestURI();

	/**
	 * @see HttpServletRequest#getRequestURL()
	 * @return
	 */
	public StringBuffer getRequestURL();

	/**
	 * @see HttpServletRequest#getServletPath()
	 * @return
	 */
	public String getServletPath();
	
	/**
	 * @see HttpServletRequest#getUserPrincipal()
	 * @return
	 */
	public Principal getUserPrincipal();
	
	/**
	 * @see HttpServletRequest#isUserInRole(String)
	 * @param role
	 * @return
	 */
	public boolean isUserInRole(String role);
	
	/**
	 * @see HttpServletRequest#getLocales()
	 * @return
	 */
	public Enumeration<Locale> getLocales();

}
