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

import java.util.function.Supplier;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.command.RequestContext;

/**
 * EntryPointからiPLAss内でロジックを実行するためのインタフェースです。
 * 
 * @author K.Higuchi
 *
 */
public interface Runner {
	
	/**
	 * 指定のテナントURLでロジックを実行するよう設定します。
	 * 
	 * @param tenantUrl
	 * @return
	 */
	public Runner withTenant(String tenantUrl);
	
	/**
	 * 指定のテナントIDでロジックを実行するよう設定します。
	 * 
	 * @param tenantId
	 * @return
	 */
	public Runner withTenant(Integer tenantId);
	
	/**
	 * 指定のcredentialでログインしてロジックを実行するよう設定します。
	 * 
	 * @param credential
	 * @return
	 */
	public Runner withAuth(Credential credential);
	
	/**
	 * 指定の言語でロジックを実行するよう設定します。
	 * 
	 * @param lang
	 * @return
	 */
	public Runner withLang(String lang);
	
	
	/**
	 * ロジックを実行します。
	 * 
	 * @param r
	 */
	public void run(Runnable r);
	
	/**
	 * ロジックを実行します。
	 * 
	 * @param s
	 * @return
	 */
	public <T> T run(Supplier<T> s);
	
	/**
	 * CommandInvokerを経由して指定のcommandNameのCommandを実行します。
	 * 
	 * @param commandName
	 * @param request
	 * @return
	 */
	public String run(String commandName, RequestContext request);
	
}
