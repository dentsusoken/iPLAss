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

import java.util.List;
import java.util.TreeSet;
import java.util.function.Supplier;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialExpiredException;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.auth.login.LoginFailedException;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.auth.authorize.builtin.group.GroupContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;

public class AuthManagerImpl implements AuthManager {

	private AuthService authService = ServiceRegistry.getRegistry().getService(AuthService.class);

	@Override
	public AuthContext getContext() {
		return new AuthContextImpl();
	}

	@Override
	public <T> T doPrivileged(Supplier<T> action) {
		return authService.doSecuredAction(
				AuthContextHolder.getAuthContext().privilegedAuthContextHolder(), action);
	}

	@Override
	public boolean canUpdateCredential() {
		return authService.getAccountManagementModule().canUpdateCredential();
	}

	@Override
	public boolean canUpdateCredential(String policyName) {
		return authService.getAccountManagementModule(policyName).canUpdateCredential();
	}

	@Override
	public boolean canResetCredential() {
		return authService.getAccountManagementModule().canResetCredential();
	}

	@Override
	public boolean canResetCredential(String policyName) {
		return authService.getAccountManagementModule(policyName).canResetCredential();
	}

	@Override
	public void resetCredential(Credential credential) {
		authService.resetCredential(credential);
	}

	@Override
	public void resetCredential(Credential credential, String policyName) {
		authService.resetCredential(credential, policyName);
	}

	@Override
	public String[] getGroupOids(GroupOidListType type, String... groupCode) {

		TenantAuthorizeContext tenantAuthContext = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class);

		TreeSet<String> list = new TreeSet<String>();
		for (String code: groupCode) {
			GroupContext gc = tenantAuthContext.getGroupContext(code);
			if (gc != null) {
				switch (type) {
				case ONLY_SPECIFY:
					list.add(gc.getOid());
					break;
				case WITH_CHILDREN:
					List<GroupContext> gclc = gc.getAllNestedChildGroup();
					for (GroupContext gclcgc: gclc) {
						list.add(gclcgc.getOid());
					}
					break;
				case WITH_PARENTS:
					List<GroupContext> gclp = gc.getGroupPath();
					for (GroupContext gclpgc: gclp) {
						list.add(gclpgc.getOid());
					}
					break;
				default:
					break;
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}

	@Override
	public void login(Credential credential) throws LoginFailedException, CredentialExpiredException {
		authService.login(credential);
	}

	@Override
	public void logout() {
		authService.logout();
	}

	@Override
	public void reAuth(Credential credential) throws LoginFailedException,
			CredentialExpiredException {
		authService.reAuth(credential);
	}

	@Override
	public void updateCredential(Credential oldCredential,
			Credential newCredential) {
		authService.updateCredential(oldCredential, newCredential);
	}

	@Override
	public void updateCredential(Credential oldCredential,
			Credential newCredential, String policyName)
			throws CredentialUpdateException {
		authService.updateCredential(oldCredential, newCredential, policyName);
	}

	@Override
	public boolean canResetLockoutStatus() {
		return authService.getAccountManagementModule().canResetLockoutStatus();
	}

	@Override
	public boolean canResetLockoutStatus(String policyName) {
		return authService.getAccountManagementModule(policyName).canResetLockoutStatus();
	}

	@Override
	public void resetLockoutStatus(String accountId) {
		authService.getAccountManagementModule().resetLockoutStatus(accountId);
	}

	@Override
	public void resetLockoutStatus(String accountId, String policyName) {
		authService.getAccountManagementModule(policyName).resetLockoutStatus(accountId);
	}

}
