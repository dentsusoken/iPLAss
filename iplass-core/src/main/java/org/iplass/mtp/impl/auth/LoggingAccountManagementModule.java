/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.log.AuthLogger;
import org.iplass.mtp.impl.auth.log.AuthLoggerService;
import org.iplass.mtp.spi.ServiceRegistry;

public class LoggingAccountManagementModule implements AccountManagementModule {
	
	private AccountManagementModule amm;
	//TODO AccountNotificationListener経由に
	private AuthLogger authLogger = ServiceRegistry.getRegistry().getService(AuthLoggerService.class).getAuthLogger(null);
	

	public LoggingAccountManagementModule(AccountManagementModule amm) {
		this.amm = amm;
	}
	
	@Override
	public boolean canCreate() {
		return amm.canCreate();
	}

	@Override
	public boolean canUpdate() {
		return amm.canUpdate();
	}

	@Override
	public boolean canRemove() {
		return amm.canRemove();
	}
	
	@Override
	public boolean canRestore() {
		return amm.canRestore();
	}

	@Override
	public boolean canPurge() {
		return amm.canPurge();
	}

	@Override
	public boolean canUpdateCredential() {
		return amm.canUpdateCredential();
	}

	@Override
	public boolean canResetCredential() {
		return amm.canResetCredential();
	}

	@Override
	public void create(User user) {
		amm.create(user);
	}
	
	@Override
	public void afterCreate(User user) {
		amm.afterCreate(user);
	}

	@Override
	public void update(User user, List<String> updateProperties) {
		amm.update(user, updateProperties);
	}

	@Override
	public void afterUpdate(User user, String policyName, List<String> updateProperties) {
		amm.afterUpdate(user, policyName, updateProperties);
	}

	@Override
	public void remove(User user) {
		amm.remove(user);
	}
	
	@Override
	public void restore(User user) {
		amm.restore(user);
	}
	
	@Override
	public void purge(User user) {
		amm.purge(user);
	}

	@Override
	public void updateCredential(Credential oldCredential,
			Credential newCredential) throws CredentialUpdateException {
		try {
			amm.updateCredential(oldCredential, newCredential);
			authLogger.updatePasswordSuccess(oldCredential);
		} catch (CredentialUpdateException e) {
			authLogger.updatePasswordFail(oldCredential, e);
			throw e;
		}
	}

	@Override
	public void resetCredential(Credential credential)
			throws CredentialUpdateException {
		amm.resetCredential(credential);
		authLogger.resetPasswordSuccess(credential);
	}

	@Override
	public boolean canResetLockoutStatus() {
		return amm.canResetLockoutStatus();
	}

	@Override
	public void resetLockoutStatus(String accountId) {
		amm.resetLockoutStatus(accountId);
	}

}
