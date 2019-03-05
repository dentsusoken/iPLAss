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
package org.iplass.mtp.impl.auth.oauth.token.opaque;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.auth.oauth.token.RefreshToken;

public class OpaqueAccessToken extends AccessToken {
	
	private String series;
	private String tokenEncoded;
	private long expires;
	private long expiresIn;
	private User user;
	private List<String> grantedScopes;
	private String clientId;
	private long creationTime;
	
	private RefreshToken refreshToken;
	
	public OpaqueAccessToken(OAuthClientRuntime client, AccessTokenMement mement, String series, String tokenEncoded, long creationTime, OpaqueRefreshToken refreshToken) {
		this.series = series;
		this.tokenEncoded = tokenEncoded;
		this.expires = mement.getExpires();
		expiresIn = (this.expires - System.currentTimeMillis()) / 1000;
		this.user = mement.getUser();
		this.refreshToken = refreshToken;
		this.clientId = client.getMetaData().getName();
		this.creationTime = creationTime;
		
		//check scopes is valid
		grantedScopes = new ArrayList<>();
		OAuthAuthorizationRuntime server = client.getAuthorizationServer();
		List<String> scopesByClientType = server.getClientPolicy(client.getMetaData().getClientType()).scopeList();
		for (String s: mement.getGrantedScopes()) {
			if (scopesByClientType.contains(s)) {
				grantedScopes.add(s);
			}
		}
	}
	
	public String getSeries() {
		return series;
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
		return refreshToken;
	}

	@Override
	public String getClientId() {
		return clientId;
	}

	@Override
	public Object getExpirationTime() {
		return TimeUnit.MILLISECONDS.toSeconds(expires);
	}

	@Override
	public Object getIssuedAt() {
		return TimeUnit.MILLISECONDS.toSeconds(creationTime);
	}

	@Override
	public Object getNotbefore() {
		return TimeUnit.MILLISECONDS.toSeconds(creationTime);
	}

}
