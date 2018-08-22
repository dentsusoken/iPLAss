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

package org.iplass.mtp.impl.auth;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.token.AuthTokenInfoList;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;

public class AuthContextImpl extends AuthContext {

	AuthContextImpl() {
	}

	@Override
	public User getUser() {
		return (User) getAuthContextHolder().getUserCopy();
	}

	@Override
	public String getPolicyName() {
		//return getAuthContextHolder().getUserContext().getUser().getAccountPolicy();
		if (getAuthContextHolder().getPolicy() != null) {
			return getAuthContextHolder().getPolicy().getMetaData().getName();
		}
		return null;
	}

	private AuthContextHolder getAuthContextHolder() {
		return AuthContextHolder.getAuthContext();
	}

	@Override
	public boolean userInRole(String role) {
		if (role == null) {
			throw new NullPointerException("role is null");
		}
		return getAuthContextHolder().userInRole(role, ExecuteContext.getCurrentContext().getClientTenantId());
	}

	@Override
	public boolean checkPermission(Permission permission) {
		return getAuthContextHolder().checkPermission(permission);
	}

	@Override
	public Tenant getTenant() {
//		return ExecuteContext.getCurrentContext().getTenant().getTenant().copy();
		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		return ObjectUtil.deepCopy(tenant);
	}

	@Override
	public void refresh() {
		AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
		as.reloadUserEntity();
		AuthContextHolder.reflesh();
	}

	@Override
	public boolean isCurrentSessionTrusted() {
		return getAuthContextHolder().checkCurrentSessionTrusted().isTrusted();
	}

	@Override
	public Class<? extends Credential> getCredentialTypeForTrust() {
		return getAuthContextHolder().checkCurrentSessionTrusted().getCredentialTypeForTrust();
	}

	@Override
	public boolean isAuthenticated() {
		return !(getAuthContextHolder().getUserContext() instanceof AnonymousUserContext);
	}

	@Override
	public Object getAttribute(String name) {
		UserContext uc = getAuthContextHolder().getUserContext();
		if (uc == null) {
			return null;
		}
		return uc.getAttribute(name);
	}

	@Override
	public boolean isPrivileged() {
		return getAuthContextHolder().isPrivilegedExecution();
	}

	@Override
	public AuthTokenInfoList getAuthTokenInfos() {
		if (isAuthenticated()) {
			return getAuthContextHolder().getAuthTokenInfoList();
		} else {
			return null;
		}
	}

}
