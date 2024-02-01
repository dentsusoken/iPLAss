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
package org.iplass.mtp.impl.auth.oauth.token.opaque;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import org.iplass.mtp.auth.oauth.AccessTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.auth.oauth.token.OAuthAccessTokenStore;
import org.iplass.mtp.impl.auth.oauth.token.RefreshToken;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * OpaqueなAccessTokenを発行するビルトインのOAuthAccessTokenStore。
 * 
 * @author K.Higuchi
 *
 */
public class OpaqueOAuthAccessTokenStore implements OAuthAccessTokenStore, ServiceInitListener<OAuthAuthorizationService> {
	
	private String authTokenType = AccessTokenHandler.TYPE_OAUTH_DEFAULT;
	private String seriesHashSalt = "iPLAss#OAT2018";
	private String seriesHashAlgorithm = "SHA-256";
	private TokenCreationStrategy tokenCreationStrategy;
	
	private AccessTokenHandler accessTokenHandler;
	
	public String getAuthTokenType() {
		return authTokenType;
	}

	public void setAuthTokenType(String authTokenType) {
		this.authTokenType = authTokenType;
	}

	public TokenCreationStrategy getTokenCreationStrategy() {
		return tokenCreationStrategy;
	}

	public void setTokenCreationStrategy(TokenCreationStrategy tokenCreationStrategy) {
		this.tokenCreationStrategy = tokenCreationStrategy;
	}

	public String getSeriesHashSalt() {
		return seriesHashSalt;
	}

	public void setSeriesHashSalt(String seriesHashSalt) {
		this.seriesHashSalt = seriesHashSalt;
	}

	public String getSeriesHashAlgorithm() {
		return seriesHashAlgorithm;
	}

	public void setSeriesHashAlgorithm(String seriesHashAlgorithm) {
		this.seriesHashAlgorithm = seriesHashAlgorithm;
	}
	
	@Override
	public AccessToken getAccessTokenByUserOid(OAuthClientRuntime client, String userOid) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		AuthToken authToken = accessTokenHandler.authTokenStore().getBySeries(tenantId, accessTokenHandler.getType(), toSeriesString(client, userOid));
		if (authToken == null) {
			return null;
		}
		AccessTokenMement mement = (AccessTokenMement) authToken.getDetails();
		return new OpaqueAccessToken(client, mement, authToken.getSeries(), null, authToken.getStartDate().getTime(), null);
	}

	@Override
	public AccessToken getAccessToken(String tokenString) {
		AuthToken authToken = new AuthToken(tokenString);
		String tokenPart = authToken.getToken();
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		authToken = accessTokenHandler.authTokenStore().getBySeries(tenantId, authToken.getType(), authToken.getSeries());
		if (authToken == null) {
			return null;
		}
		if (!accessTokenHandler.checkTokenValid(tokenPart, authToken)) {
			return null;
		}
		AccessTokenMement mement = (AccessTokenMement) authToken.getDetails();
		OAuthClientRuntime client = OAuthServiceHolder.client.getRuntimeById(mement.getClientMetaDataId());
		return new OpaqueAccessToken(client, mement, authToken.getSeries(), null, authToken.getStartDate().getTime(), null);
	}

	@Override
	public AccessToken createAccessToken(OAuthClientRuntime client, RefreshToken refreshToken) {
		AuthToken authToken = tokenCreationStrategy.create(client, accessTokenHandler, (OpaqueRefreshToken) refreshToken);
		if (authToken == null) {
			return null;
		}
		AccessTokenMement mement = (AccessTokenMement) authToken.getDetails();
		OpaqueAccessToken accessToken = new OpaqueAccessToken(client, mement, authToken.getSeries(), authToken.encodeToken(), authToken.getStartDate().getTime(), null);
		return accessToken;
	}

	@Override
	public AccessToken createAccessToken(OAuthClientRuntime client, String userOid, List<String> scopes) {
		AccessTokenInfo ati = new AccessTokenInfo();
		ati.setClientName(client.getMetaData().getName());
		ati.setGrantedScopes(scopes);
		ati.setType(accessTokenHandler.getType());
		AuthToken authToken = tokenCreationStrategy.create(client, accessTokenHandler, userOid, ati);
		AccessTokenMement mement = (AccessTokenMement) authToken.getDetails();
		OpaqueRefreshToken refreshToken = null;
		if (mement.getRefreshToken() != null) {
			refreshToken = new OpaqueRefreshToken(client, (RefreshTokenMement) mement.getRefreshToken().getDetails(), mement.getRefreshToken().getSeries(), mement.getRefreshToken().encodeToken());
		}
		
		OpaqueAccessToken accessToken = new OpaqueAccessToken(client, (AccessTokenMement) authToken.getDetails(), authToken.getSeries(), authToken.encodeToken(), authToken.getStartDate().getTime(), refreshToken);
		return accessToken;
	}

	@Override
	public void inited(OAuthAuthorizationService service, Config config) {
		AuthTokenService ats = ServiceRegistry.getRegistry().getService(AuthTokenService.class);
		accessTokenHandler = (AccessTokenHandler) ats.getHandler(authTokenType);
		try {
			MessageDigest.getInstance(seriesHashAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceConfigrationException("invalid messageDigestAlgorithm", e);
		}
	}

	@Override
	public void destroyed() {
	}

	//TODO 切り替え可能に
	String toSeriesString(OAuthClientRuntime client, String ownerId) {
		try {
			MessageDigest md = MessageDigest.getInstance(seriesHashAlgorithm);
			String msg = ownerId + "-" + client.getMetaData().getId() + "-" + ExecuteContext.getCurrentContext().getClientTenantId() + "-" + seriesHashSalt;
			byte[] bytes = md.digest(msg.getBytes("UTF-8"));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public RefreshToken getRefreshToken(String tokenString) {
		AuthToken authToken = new AuthToken(tokenString);
		String tokenPart = authToken.getToken();
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		authToken = accessTokenHandler.refreshTokenHandler().authTokenStore().getBySeries(tenantId, authToken.getType(), authToken.getSeries());
		if (authToken == null) {
			return null;
		}
		if (!accessTokenHandler.refreshTokenHandler().checkTokenValid(tokenPart, authToken)) {
			return null;
		}
		RefreshTokenMement mement = (RefreshTokenMement) authToken.getDetails();
		OAuthClientRuntime client = OAuthServiceHolder.client.getRuntimeById(mement.getClientMetaDataId());
		
		return new OpaqueRefreshToken(client, mement, authToken.getSeries(), null);
	}

	@Override
	public void revokeToken(OAuthClientRuntime client, String tokenString, String tokenTypeHint) {
		//tokenTypeHintは利用しない
		
		AuthToken authToken = new AuthToken(tokenString);
		String type = authToken.getType();
		if (type.equals(accessTokenHandler.getType())) {
			AccessToken at = getAccessToken(tokenString);
			if (!at.getClientId().equals(client.getMetaData().getName())) {
				return;
			}
		} else if (type.equals(accessTokenHandler.getRefreshTokenType())) {
			RefreshToken rt = getRefreshToken(tokenString);
			if (!rt.getClientId().equals(client.getMetaData().getName())) {
				return;
			}
		} else {
			//illegal type...
			return;
		}
		
		//accessTokenとrefreshTokenは一蓮托生
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		accessTokenHandler.authTokenStore().deleteBySeries(tenantId, authToken.getType(), authToken.getSeries());
	}

	@Override
	public void revokeTokenByUserOid(String userOid) {
		accessTokenHandler.authTokenStore().delete(ExecuteContext.getCurrentContext().getClientTenantId(), accessTokenHandler.getType(), userOid);
	}

}
