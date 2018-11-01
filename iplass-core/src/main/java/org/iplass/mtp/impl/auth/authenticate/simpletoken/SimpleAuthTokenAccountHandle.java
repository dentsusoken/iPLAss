/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.simpletoken;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenCredential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;

public class SimpleAuthTokenAccountHandle implements AccountHandle {
	private static final long serialVersionUID = 731216514006775874L;

	public static final String AUTHED_BY_SIMPLE_AUTH_TOKEN ="authedBySimpleAuthToken";
	public static final String SIMPLE_AUTH_TOKEN_SERIES ="simpleAuthTokenSeries";
	
	private String unmodifiableUniqueKey;
	private Map<String, Object> attributeMap;
	private int authenticationProviderIndex;

	public SimpleAuthTokenAccountHandle() {
	}
	
	public SimpleAuthTokenAccountHandle(String unmodifiableUniqueKey, String series, String policyName) {
		this.unmodifiableUniqueKey = unmodifiableUniqueKey;
		getAttributeMap().put(SimpleAuthTokenAccountHandle.AUTHED_BY_SIMPLE_AUTH_TOKEN, Boolean.TRUE);
		getAttributeMap().put(SimpleAuthTokenAccountHandle.SIMPLE_AUTH_TOKEN_SERIES, series);
		getAttributeMap().put(User.ACCOUNT_POLICY, policyName);
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
		SimpleAuthTokenCredential c = new SimpleAuthTokenCredential();
		c.setId(unmodifiableUniqueKey);
		return c;
	}

	@Override
	public String getUnmodifiableUniqueKey() {
		return unmodifiableUniqueKey;
	}

	@Override
	public Map<String, Object> getAttributeMap() {
		if(attributeMap == null){
			attributeMap = new HashMap<String, Object>();
		}
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
