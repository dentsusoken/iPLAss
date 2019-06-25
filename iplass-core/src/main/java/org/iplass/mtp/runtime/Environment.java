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
 * サーバ実行環境の情報を取得するためのインタフェースです。
 * 
 * @author K.Higuchi
 *
 */
public interface Environment extends Manager {
	
	/**
	 * Environmentのインスタンスを取得します。
	 * 
	 * @return
	 */
	public static Environment getInstance() {
		return ManagerLocator.manager(Environment.class);
	}
	
	/**
	 * 現在実行中のサーバが指定の役割を保持しているか否かを返却します。
	 * serverRoleは、"mtp.server.myserverroles"システムプロパティでカンマ区切りにて指定可能です。
	 * システムプロパティに指定されたserverRoleのリストに引数のserverRoleが含まれている場合trueを返却します。
	 * システムプロパティが指定されていない場合、serverInRole()の返却値は常にtrueとなります。
	 * 
	 * @param serverRole チェック対象のserverRole
	 * @return
	 */
	public boolean serverInRole(String serverRole);
	
	/**
	 * 現在実行中のサーバのインスタンスを特定するIDを返却します。
	 * serverIdは、"mtp.server.myserverid"システムプロパティで指定することが可能です。
	 * "mtp.server.myserverid"システムプロパティが指定されていない場合、サーバのhost名がserverIdとなります。
	 * 
	 * @return
	 */
	public String getServerId();
	
	/**
	 * 実行環境のiPLAssのバージョン情報を取得します。
	 * 
	 * @return
	 */
	public String getVersion();

}
