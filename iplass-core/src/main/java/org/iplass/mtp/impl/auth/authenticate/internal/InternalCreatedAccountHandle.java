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

package org.iplass.mtp.impl.auth.authenticate.internal;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;

public class InternalCreatedAccountHandle implements AccountHandle {
	private static final long serialVersionUID = -6723035594874988582L;
	
	private String accountId;
	private int authenticationProviderIndex;
	private Map<String, Object> attributeMap;
	
	public InternalCreatedAccountHandle(String accountId) {
		this.accountId = accountId;
		attributeMap = new HashMap<>();
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
		return new InternalCredential(accountId);
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
