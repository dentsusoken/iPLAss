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

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.oauth.MetaClientPolicy.ClientPolicyRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.token.opaque.RefreshTokenMement.RefreshTokenInfo;
import org.iplass.mtp.spi.Config;

class RefreshTokenHandler extends AuthTokenHandler {
	//internal use only
	
	@Override
	public void inited(AuthTokenService service, Config config) {
		super.inited(service, config);
	}
	
	@Override
	public AuthTokenInfo toAuthTokenInfo(AuthToken authToken) {
		RefreshTokenInfo info = new RefreshTokenInfo();
		info.setType(getType());
		info.setKey(authToken.getSeries());
		((RefreshTokenMement) authToken.getDetails()).fill(info);
		
		return info;
	}
	
	@Override
	public Credential toCredential(AuthToken newToken) {
		//never use
		return null;
	}

	@Override
	protected Serializable createDetails(String seriesString, String tokenString, String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		RefreshTokenMement mement = new RefreshTokenMement();
		RefreshTokenInfo info = (RefreshTokenInfo) tokenInfo;
		OAuthClientRuntime client = OAuthServiceHolder.client.getRuntimeByName(info.getClientName());
		OAuthAuthorizationRuntime server = client.getAuthorizationServer();
		ClientPolicyRuntime cp = server.getClientPolicy(client.getMetaData().getClientType());
		long expires = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cp.getMetaData().getRefreshTokenLifetimeSeconds());
		
		mement.save(info, expires, userUniqueId);
		return mement;
	}

	@Override
	public String newSeriesString(String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		RefreshTokenInfo ati = (RefreshTokenInfo) tokenInfo;
		OAuthClientRuntime clientRuntime = OAuthServiceHolder.client.getRuntimeByName(ati.getClientName());
		return ((OpaqueOAuthAccessTokenStore) OAuthServiceHolder.authorization.getAccessTokenStore()).toSeriesString(clientRuntime, userUniqueId);
	}
}
