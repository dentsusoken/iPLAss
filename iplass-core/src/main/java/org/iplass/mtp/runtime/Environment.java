/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.Manager;
import org.iplass.mtp.ManagerLocator;

/**
 * <% if (doclang == "ja") {%>
 * サーバ実行環境の情報を取得するためのインタフェースです。
 * <%} else {%>
 * An interface for server execution environment information.
 * <%}%>
 * 
 * @author K.Higuchi
 *
 */
public interface Environment extends Manager {
	
	/**
	 * <% if (doclang == "ja") {%>
	 * Environmentのインスタンスを取得します。
	 * <%} else {%>
	 * Get an instance of Environment.
	 * <%}%>
	 * 
	 * @return
	 */
	public static Environment getInstance() {
		return ManagerLocator.manager(Environment.class);
	}
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 現在実行中のサーバが指定の役割を保持しているか否かを返却します。
	 * serverRoleは、"mtp.server.myserverroles"システムプロパティでカンマ区切りにて指定可能です。
	 * システムプロパティに指定されたserverRoleのリストに引数のserverRoleが含まれている場合trueを返却します。
	 * システムプロパティが指定されていない場合、serverInRole()の返却値は常にtrueとなります。
	 * <%} else {%>
	 * This returns whether the currently running server has the specified role or not.
	 * serverRole can be specified with the "mtp.server.myserverroles" system property separated by commas.
	 * This returns true if the argument serverRole is included in the serverRole list specified in the system properties.
	 * If the system property is not specified, the return value of serverInRole() is always true.
	 * <%}%>
	 * 
	 * @param serverRole <%=doclang == 'ja' ? 'チェック対象のserverRole': 'ServerRole to check'%>
	 * @return
	 */
	public boolean serverInRole(String serverRole);
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 現在実行中のサーバのインスタンスを特定するIDを返却します。
	 * serverIdは、"mtp.server.myserverid"システムプロパティで指定することが可能です。
	 * "mtp.server.myserverid"システムプロパティが指定されていない場合、サーバのhost名がserverIdとなります。
	 * <%} else {%>
	 * Returns an ID that identifies the instance of the server that is currently running.
	 * serverId can be specified in the "mtp.server.myserverid" system property.
	 * If the "mtp.server.myserverid" system property is not specified, the server host name is serverId.
	 * <%}%>
	 * 
	 * @return
	 */
	public String getServerId();
	
	/**
	 * <% if (doclang == "ja") {%>
	 * 実行環境のiPLAssのバージョン情報を取得します。
	 * <%} else {%>
	 * Gets version information of iPLAss in the execution environment.
	 * <%}%>
	 * 
	 * @return
	 */
	public String getVersion();

}
