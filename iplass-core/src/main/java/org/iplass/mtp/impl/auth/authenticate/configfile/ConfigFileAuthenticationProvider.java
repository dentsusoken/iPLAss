/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.configfile;

import java.util.List;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.authenticate.AccountManagementModule;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProviderBase;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;

public class ConfigFileAuthenticationProvider extends AuthenticationProviderBase {

	private List<AccountConfig> accounts;
	private List<Integer> tenantIds;
	
	public List<AccountConfig> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountConfig> accounts) {
		this.accounts = accounts;
	}

	public List<Integer> getTenantIds() {
		return tenantIds;
	}

	public void setTenantIds(List<Integer> tenantIds) {
		this.tenantIds = tenantIds;
	}

	@Override
	public void inited(AuthService service, Config config) {
		if (getUserEntityResolver() == null) {
			ConfigFileUserEntityResolver uer = new ConfigFileUserEntityResolver();
			uer.inited(service, this);
			setUserEntityResolver(uer);
		}
		
		super.inited(service, config);
		
	}

	@Override
	public AccountHandle login(Credential credential) {
		if (!(credential instanceof IdPasswordCredential)) {
			return null;
		}
		
		if (tenantIds != null && tenantIds.size() > 0) {
			int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
			boolean ok = false;
			for (Integer tid: tenantIds) {
				if (tid.intValue() == tenantId) {
					ok = true;
					break;
				}
			}
			if (!ok) {
				return null;
			}
		}
		
		IdPasswordCredential idPass = (IdPasswordCredential) credential;
		if (accounts != null) {
			for (AccountConfig ac: accounts) {
				if (ac.getId().equals(idPass.getId())
						&& ac.getPassword().equals(idPass.getPassword())) {
					return new ConfigFileAccountHandle(ac);
				}
			}
		}
		
		return null;
	}

	@Override
	public void logout(AccountHandle user) {
	}

	@Override
	public AccountManagementModule getAccountManagementModule() {
		return AuthenticationProviderBase.NO_UPDATABLE_AMM;
	}

	@Override
	public Class<? extends Credential> getCredentialType() {
		return IdPasswordCredential.class;
	}

	@Override
	protected Class<? extends AccountHandle> getAccountHandleClassForTrust() {
		return ConfigFileAccountHandle.class;
	}
	
	@Override
	public boolean isSelectableOnAuthPolicy() {
		return false;
	}

}
