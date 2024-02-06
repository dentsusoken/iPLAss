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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.util.ObjectUtil;

public class ConfigFileAccountHandle implements AccountHandle {
	private static final long serialVersionUID = 4360033341629716946L;

	private String accountId;
	private int authenticationProviderIndex;
	private Map<String, Object> attributeMap;
	
	ConfigFileAccountHandle(AccountConfig accountConfig) {
		accountId = accountConfig.getId();
		attributeMap = new HashMap<>();
		if (accountConfig.getAttributeMap() != null) {
			for (Map.Entry<String, Object> e: accountConfig.getAttributeMap().entrySet()) {
				Serializable val = ObjectUtil.deepCopy((Serializable) e.getValue());
				attributeMap.put(e.getKey(), val);
			}
		}
		if (accountConfig.isAdmin()) {
			attributeMap.put(User.ADMIN_FLG, true);
		}
	}

	@Override
	public boolean isAccountLocked() {
		return false;
	}

	@Override
	public boolean isExpired() {
		return false;
	}

	@Override
	public boolean isInitialLogin() {
		return false;
	}

	@Override
	public Credential getCredential() {
		return new IdPasswordCredential(accountId, null);
	}

	@Override
	public String getUnmodifiableUniqueKey() {
		return accountId;
	}

	@Override
	public Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	@Override
	public void setAuthenticationProviderIndex(int authenticationProviderIndex) {
		this.authenticationProviderIndex = authenticationProviderIndex;
	}

	@Override
	public int getAuthenticationProviderIndex() {
		return authenticationProviderIndex;
	}

}
