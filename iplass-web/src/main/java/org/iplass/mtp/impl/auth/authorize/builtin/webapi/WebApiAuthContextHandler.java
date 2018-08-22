/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authorize.builtin.webapi;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.auth.authorize.AuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthorizationContextCacheLogic;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthorizationContextHandler;
import org.iplass.mtp.impl.auth.authorize.builtin.BuiltinAuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.webapi.WebApiService;
import org.iplass.mtp.webapi.permission.WebApiPermission;

public class WebApiAuthContextHandler extends AuthorizationContextHandler {
	private static final Class<?>[] PERM_TYPE = {WebApiPermission.class};
	private static final AllPermissionWebApiAuthContext DEFAULT = new AllPermissionWebApiAuthContext(false);

	public WebApiAuthContextHandler() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Permission>[] permissionType() {
		return (Class<? extends Permission>[]) PERM_TYPE;
	}

	@Override
	protected String cacheNamespace() {
		return "mtp.auth.builtin.webapi";
	}
	
	@Override
	protected String contextName(Permission permission) {
		return ((WebApiPermission) permission).getWebApiName();
	}

	@Override
	protected String toMetaDataPath(String contextName) {
		return WebApiService.WEB_API_META_PATH + contextName;
	}

	@Override
	protected AuthorizationContextCacheLogic newAuthorizeContextCacheLogic(TenantAuthorizeContext tac) {
		return new WebApiAuthContextCacheLogic(tac);
	}

	@Override
	public BuiltinAuthorizationContext get(String contextName, TenantAuthorizeContext tac) {
		// rootの場合、空文字でくる、、、
		if (contextName.length() == 0) {
			contextName = "/";
		}
		
		BuiltinAuthorizationContext webapiContext = super.get(contextName, tac);
		if (webapiContext == null) {
			//～/*指定の定義がないか検索
			String currentWebapiName = contextName;
			while (currentWebapiName.contains("/") && webapiContext == null) {
				currentWebapiName = currentWebapiName.substring(0, currentWebapiName.lastIndexOf("/"));
				webapiContext = super.get(currentWebapiName + "/*", tac);
			}

			if (webapiContext == null) {
				//*指定がないか
				webapiContext = super.get("*", tac);
			}
		}
		return webapiContext;
	}

	@Override
	protected AuthorizationContext defaultAuthorizationContext(String contextName, TenantAuthorizeContext tac) {
		return DEFAULT;
	}

}
