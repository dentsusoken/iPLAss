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
package org.iplass.mtp.impl.auth.oauth.token.remote;

import java.util.Arrays;
import java.util.List;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.auth.oauth.token.RefreshToken;

public class RemoteAccessToken extends AccessToken {
	
	private String tokenEncoded;
	private User user;
	private List<String> grantedScopes;
	private String clientId;
	private String sub;
	private long expires;
	private long expiresIn;
	private long issuedAt;
	private long notBefore;
	
	private String audience;
	private String issuer;
	private String username;
	
	RemoteAccessToken(String tokenEncoded, IntroResponse jsonMap) {
		this.tokenEncoded = tokenEncoded;
		if (jsonMap.scope != null) {
			this.grantedScopes = Arrays.asList(jsonMap.scope.split(" "));
		}
		this.clientId = jsonMap.client_id;
		this.sub = jsonMap.sub;
		if (jsonMap.exp != null) {
			this.expires = jsonMap.exp;
			expiresIn = this.expires - (System.currentTimeMillis() / 1000);
		} else {
			expiresIn = 1;
		}
		if (jsonMap.iat != null) {
			this.issuedAt = jsonMap.iat;
		}
		if (jsonMap.nbf != null) {
			this.notBefore = jsonMap.nbf;
		}
		this.audience = jsonMap.aud;
		this.issuer = jsonMap.iss;
		this.username = jsonMap.username;
		
		this.user = jsonMap.resource_owner;
		if (user == null) {
			user = new User();
			user.setOid(sub);
			user.setAccountId(sub);
			user.setName(username);
		}
	}
	
	public long getExpires() {
		return expires;
	}

	public String getAudience() {
		return audience;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getUsername() {
		return username;
	}

	public String getSub() {
		return sub;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public List<String> getGrantedScopes() {
		return grantedScopes;
	}
	
	@Override
	public String getTokenEncoded() {
		return tokenEncoded;
	}

	@Override
	public long getExpiresIn() {
		return expiresIn;
	}

	@Override
	public User getUser() {
		return user;
	}
	
	@Override
	public RefreshToken getRefreshToken() {
		return null;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public long getExpirationTime() {
		return expires;
	}

	@Override
	public long getIssuedAt() {
		return issuedAt;
	}

	@Override
	public long getNotBefore() {
		return notBefore;
	}

}
