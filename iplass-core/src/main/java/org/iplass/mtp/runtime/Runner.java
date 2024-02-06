/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.runtime;

import java.util.function.Supplier;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.command.RequestContext;

/**
 * <% if (doclang == "ja") {%>
 * EntryPointからiPLAss内でロジックを実行するためのインタフェースです。
 * <%} else {%>
 * An interface for executing logic from EntryPoint in iPLAss.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public interface Runner {
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 指定のテナントURLでロジックを実行するよう設定します。
	 * <%} else {%>
	 * Set to execute logic with the specified tenant URL.
	 * <%}%>
	 * 
	 * @param tenantUrl
	 * @return
	 */
	public Runner withTenant(String tenantUrl);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 指定のテナントIDでロジックを実行するよう設定します。
	 * <%} else {%>
	 * Set to execute logic with the specified tenant ID.
	 * <%}%>
	 * 
	 * @param tenantId
	 * @return
	 */
	public Runner withTenant(Integer tenantId);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 指定のcredentialでログインしてロジックを実行するよう設定します。
	 * <%} else {%>
	 * Set to execute the logic by logging in with the specified credentials.
	 * <%}%>
	 * 
	 * @param credential
	 * @return
	 */
	public Runner withAuth(Credential credential);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 指定の言語でロジックを実行するよう設定します。
	 * <%} else {%>
	 * Set to execute logic in the specified language.
	 * <%}%>
	 * 
	 * @param lang
	 * @return
	 */
	public Runner withLang(String lang);
	
	
	/**
	 * <% if (doclang == "ja") {%>
	 * ロジックを実行します。
	 * <%} else {%>
	 * Execute logic.
	 * <%}%>
	 * 
	 * @param r
	 */
	public void run(Runnable r);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * ロジックを実行します。
	 * <%} else {%>
	 * Execute logic.
	 * <%}%>
	 * 
	 * @param s
	 * @return
	 */
	public <T> T run(Supplier<T> s);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * CommandInvokerを経由して指定のcommandNameのCommandを実行します。
	 * <%} else {%>
	 * Execute the Command with the specified commandName via CommandInvoker.
	 * <%}%>
	 * 
	 * @param commandName
	 * @param request
	 * @return
	 */
	public String run(String commandName, RequestContext request);
	
}
