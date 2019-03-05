/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;

public class AccessTokenAccountHandle implements AccountHandle {
	private static final long serialVersionUID = 9109210238979237297L;

	public static final String AUTHED_BY_ACCESS_TOKEN ="authedByAccessToken";
	public static final String GRANTED_SCOPES ="oauthGrantedScopes";
	public static final String CLIENT_ID ="oauthClientId";
	
	private String unmodifiableUniqueKey;
	private Map<String, Object> attributeMap;
	private int authenticationProviderIndex;
	private AccessToken accessToken;

	public AccessTokenAccountHandle() {
	}
	
	public AccessTokenAccountHandle(String unmodifiableUniqueKey, AccessToken accessToken, String policyName) {
		this.unmodifiableUniqueKey = unmodifiableUniqueKey;
		this.accessToken = accessToken;
		getAttributeMap().put(AUTHED_BY_ACCESS_TOKEN, Boolean.TRUE);
		if (accessToken != null) {
			getAttributeMap().put(GRANTED_SCOPES, new ArrayList<>(accessToken.getGrantedScopes()));
			getAttributeMap().put(CLIENT_ID, accessToken.getClientId());
		}
		getAttributeMap().put(User.ACCOUNT_POLICY, policyName);
	}
	
	public AccessToken getAccessToken() {
		return accessToken;
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
		AccessTokenCredential c = new AccessTokenCredential();
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
