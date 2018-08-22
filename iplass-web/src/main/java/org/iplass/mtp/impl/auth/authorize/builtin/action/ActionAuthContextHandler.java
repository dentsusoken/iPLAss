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
package org.iplass.mtp.impl.auth.authorize.builtin.action;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.auth.authorize.AuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthorizationContextCacheLogic;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthorizationContextHandler;
import org.iplass.mtp.impl.auth.authorize.builtin.BuiltinAuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.web.actionmapping.ActionMappingService;
import org.iplass.mtp.web.actionmapping.permission.ActionPermission;

public class ActionAuthContextHandler extends AuthorizationContextHandler {
	private static final Class<?>[] PERM_TYPE = {ActionPermission.class};
	private static final AllPermissionActionAuthContext DEFAULT = new AllPermissionActionAuthContext(false);

	public ActionAuthContextHandler() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Permission>[] permissionType() {
		return (Class<? extends Permission>[]) PERM_TYPE;
	}

	@Override
	protected String cacheNamespace() {
		return "mtp.auth.builtin.action";
	}
	
	@Override
	protected String contextName(Permission permission) {
		return ((ActionPermission) permission).getActionName();
	}

	@Override
	protected String toMetaDataPath(String contextName) {
		return ActionMappingService.ACTION_MAPPING_META_PATH + contextName;
	}

	@Override
	protected AuthorizationContextCacheLogic newAuthorizeContextCacheLogic(TenantAuthorizeContext tac) {
		return new ActionAuthContextCacheLogic(tac);
	}
	
	

	@Override
	public boolean useSharedPermission(Permission permission) {
		if (((ActionPermission) permission).isExternalResource()) {
			return false;
		}
		return super.useSharedPermission(permission);
	}

	@Override
	public BuiltinAuthorizationContext get(String contextName, TenantAuthorizeContext tac) {
		// rootの場合、空文字でくる、、、
		if (contextName.length() == 0) {
			contextName = "/";
		}
		
		BuiltinAuthorizationContext actionContext = super.get(contextName, tac);
		if (actionContext == null) {
			//～/*指定の定義がないか検索
			String currentActionName = contextName;
			while (currentActionName.contains("/") && actionContext == null) {
				currentActionName = currentActionName.substring(0, currentActionName.lastIndexOf("/"));
				actionContext = super.get(currentActionName + "/*", tac);
			}

			if (actionContext == null) {
				//*指定がないか
				actionContext = super.get("*", tac);
			}
		}
		return actionContext;
	}

	@Override
	protected AuthorizationContext defaultAuthorizationContext(String contextName, TenantAuthorizeContext tac) {
		return DEFAULT;
	}

}
