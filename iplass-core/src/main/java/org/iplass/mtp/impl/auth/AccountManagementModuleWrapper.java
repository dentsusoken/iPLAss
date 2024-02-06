/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.LinkedList;
import java.util.List;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;

public class AccountManagementModuleWrapper implements AccountManagementModule {
	
	private List<AccountManagementModule> list;
	
	public AccountManagementModuleWrapper() {
	}
	
	public void add(AccountManagementModule module) {
		if (list == null) {
			list = new LinkedList<>();
		}
		list.add(module);
	}
	
	public AccountManagementModule stripOrThis() {
		if (list == null) {
			return null;
		}
		if (list.size() == 1) {
			return list.get(0);
		}
		return this;
	}
	
	@Override
	public boolean canCreate() {
		for (AccountManagementModule m: list) {
			if (m.canCreate()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canUpdate() {
		for (AccountManagementModule m: list) {
			if (m.canUpdate()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canRemove() {
		for (AccountManagementModule m: list) {
			if (m.canRemove()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canRestore() {
		for (AccountManagementModule m: list) {
			if (m.canRestore()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canPurge() {
		for (AccountManagementModule m: list) {
			if (m.canPurge()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canUpdateCredential() {
		for (AccountManagementModule m: list) {
			if (m.canUpdateCredential()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canResetCredential() {
		for (AccountManagementModule m: list) {
			if (m.canResetCredential()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canResetLockoutStatus() {
		for (AccountManagementModule m: list) {
			if (m.canResetLockoutStatus()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void create(User user) {
		for (AccountManagementModule m: list) {
			m.create(user);
		}
	}

	@Override
	public void afterCreate(User user) {
		for (AccountManagementModule m: list) {
			m.afterCreate(user);
		}
	}

	@Override
	public void update(User user, List<String> updateProperties) {
		for (AccountManagementModule m: list) {
			m.update(user, updateProperties);
		}
	}

	@Override
	public void afterUpdate(User user, String policyName, List<String> updateProperties) {
		for (AccountManagementModule m: list) {
			m.afterUpdate(user, policyName, updateProperties);
		}
	}

	@Override
	public void remove(User user) {
		for (AccountManagementModule m: list) {
			m.remove(user);
		}
	}

	@Override
	public void restore(User user) {
		for (AccountManagementModule m: list) {
			m.restore(user);
		}
	}

	@Override
	public void purge(User user) {
		for (AccountManagementModule m: list) {
			m.purge(user);
		}
	}

	@Override
	public void updateCredential(Credential oldCredential, Credential newCredential) throws CredentialUpdateException {
		for (AccountManagementModule m: list) {
			m.updateCredential(oldCredential, newCredential);
		}
	}

	@Override
	public void resetCredential(Credential credential) throws CredentialUpdateException {
		for (AccountManagementModule m: list) {
			m.resetCredential(credential);
		}
	}

	@Override
	public void resetLockoutStatus(String accountId) {
		for (AccountManagementModule m: list) {
			m.resetLockoutStatus(accountId);
		}
	}

}
