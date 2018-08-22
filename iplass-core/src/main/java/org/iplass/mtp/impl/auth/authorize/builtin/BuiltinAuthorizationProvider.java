/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authorize.builtin;

import java.util.HashMap;
import java.util.List;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authorize.AuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.AuthorizationProvider;
import org.iplass.mtp.impl.auth.authorize.builtin.role.RoleContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;

public class BuiltinAuthorizationProvider implements AuthorizationProvider {

	private TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	
	private boolean grantAllPermissionsToAdmin = true;
	
	private List<AuthorizationContextHandler> authorizationContextHandler;
	
	private HashMap<Class<? extends Permission>, AuthorizationContextHandler> authorizationContextHandlerMap;
	
	public <T extends Permission> AuthorizationContextHandler getAuthorizationContextHandler(Class<T> type) {
		return authorizationContextHandlerMap.get(type);
	}
	
	public List<AuthorizationContextHandler> getAuthorizationContextHandler() {
		return authorizationContextHandler;
	}

	public void setAuthorizationContextHandler(List<AuthorizationContextHandler> authorizationContextHandler) {
		this.authorizationContextHandler = authorizationContextHandler;
	}

	public boolean isGrantAllPermissionsToAdmin() {
		return grantAllPermissionsToAdmin;
	}

	public void setGrantAllPermissionsToAdmin(boolean grantAllPermissionsToAdmin) {
		this.grantAllPermissionsToAdmin = grantAllPermissionsToAdmin;
	}

	@Override
	public boolean userInRole(AuthContextHolder userAuthContext, int tenantId, String role) {
		TenantAuthorizeContext context = tcService.getTenantContext(tenantId).getResource(TenantAuthorizeContext.class);
		RoleContext roleContext = context.getRoleContext(role);
		if (roleContext == null) {
			return false;
		} else {
			return roleContext.userInRole(userAuthContext);
		}
	}

	@Override
	public boolean useSharedPermission(Permission permission) {
		AuthorizationContextHandler handler = getAuthorizationContextHandler(permission.getClass());
		if (handler == null) {
			throw new IllegalArgumentException("AuthorizationContextHandler of type:" + permission.getClass().getName() + " not defined.");
		}

		return handler.useSharedPermission(permission);
	}
	
	@Override
	public AuthorizationContext getAuthorizationContext(int tenantId, Permission permission) {
		AuthorizationContextHandler handler = getAuthorizationContextHandler(permission.getClass());
		if (handler == null) {
			throw new IllegalArgumentException("AuthorizationContextHandler of type:" + permission.getClass().getName() + " not defined.");
		}
		
		TenantAuthorizeContext context = tcService.getTenantContext(tenantId).getResource(TenantAuthorizeContext.class);
		return handler.getOrDefault(handler.contextName(permission), context);
	}

	@Override
	public void inited(AuthService s, Config config) {
		authorizationContextHandlerMap = new HashMap<>();
		for (AuthorizationContextHandler ach: authorizationContextHandler) {
			for (Class<? extends Permission> p: ach.permissionType()) {
				authorizationContextHandlerMap.put(p, ach);
			}
		}
	}

	@Override
	public void destroyed() {
		//再デプロイ時のメモリリーク対策
		tcService = null;
	}

}
