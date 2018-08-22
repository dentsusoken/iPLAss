/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;

public class BuiltinAccountHandle implements AccountHandle {
	private static final long serialVersionUID = -5079682470668709171L;

//	private BuiltinAccount account;
	private String accountId;
	private String oid;
	private boolean isAccountLocked;
	private boolean isPasswordExpired;
	private boolean isInitialLogin;
	private Map<String, Object> attributeMap;
	private int authenticationProviderIndex;

	public BuiltinAccountHandle() {
	}

	public BuiltinAccountHandle(BuiltinAccount account, String policyName) {
		this.accountId = account.getAccountId();
		this.oid = account.getOid();
		getAttributeMap().put(AccountHandle.LAST_PASSWORD_CHANGE, account.getLastPasswordChange());
		getAttributeMap().put(User.ACCOUNT_POLICY, policyName);
		getAttributeMap().put(AccountHandle.LAST_LOGIN_ON, account.getLastLoginOn());
	}

	@Override
	public boolean isAccountLocked() {
		return isAccountLocked;
	}

	public void setAccountLocked(boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}

	@Override
	public boolean isExpired() {
		return isPasswordExpired;
	}

	public void setPasswordExpired(boolean isPasswordExpired) {
		this.isPasswordExpired = isPasswordExpired;
	}

	@Override
	public boolean isInitialLogin() {
		return isInitialLogin;
	}

	public void setInitialLogin(boolean isInitialLogin) {
		this.isInitialLogin = isInitialLogin;
	}

	@Override
	public Map<String, Object> getAttributeMap() {
		if(attributeMap == null){
			attributeMap = new HashMap<String, Object>();
		}
		return attributeMap;
	}

	@Override
	public Credential getCredential() {
		return new IdPasswordCredential(accountId, null);
	}

	@Override
	public void setAuthenticationProviderIndex(int authenticationProviderIndex) {
		this.authenticationProviderIndex = authenticationProviderIndex;
	}

	@Override
	public int getAuthenticationProviderIndex() {
		return authenticationProviderIndex;
	}

	@Override
	public String getUnmodifiableUniqueKey() {
		return oid;
	}

}
