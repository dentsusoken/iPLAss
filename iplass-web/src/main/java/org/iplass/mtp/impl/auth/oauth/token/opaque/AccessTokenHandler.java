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
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.oauth.AccessTokenInfo;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenStore;
import org.iplass.mtp.impl.auth.oauth.AccessTokenAccountHandle;
import org.iplass.mtp.impl.auth.oauth.AccessTokenCredential;
import org.iplass.mtp.impl.auth.oauth.MetaClientPolicy.ClientPolicyRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthRuntimeException;
import org.iplass.mtp.impl.auth.oauth.token.opaque.RefreshTokenMement.RefreshTokenInfo;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;

/**
 * OAUthのアクセストークンのAuthTokenHandler。内部にRefreshTokenHandlerを内包する形。
 * <br>
 * 
 * アクセストークンのフォーマット：[type].[[userOid]-[clientMetaDataId]-[tenantId]-[salt]のSHA512].[token]
 * <br>
 * リフレッシュ時には、token部分が再生成。
 * 
 * @author K.Higuchi
 *
 */
public class AccessTokenHandler extends AuthTokenHandler {
	
	public static final String TYPE_OAUTH_DEFAULT = "OAT";
	public static final String TYPE_OAUTH_REFRESH_DEFAULT = "ORT";
	
	private class AccessTokenAuthTokenStore implements AuthTokenStore {

		@Override
		public AuthToken getBySeries(int tenantId, String type, String series) {
			return AccessTokenHandler.super.authTokenStore().getBySeries(tenantId, type, series);
		}

		@Override
		public List<AuthToken> getByOwner(int tenantId, String type, String ownerId) {
			return AccessTokenHandler.super.authTokenStore().getByOwner(tenantId, type, ownerId);
		}

		@Override
		public void create(AuthToken token) {
			AccessTokenHandler.super.authTokenStore().create(token);
			
			//create refresh token
			AccessTokenMement mement = (AccessTokenMement) token.getDetails();
			OAuthClientRuntime client = OAuthServiceHolder.client.getRuntimeById(mement.getClientMetaDataId());
			OAuthAuthorizationRuntime authorizationServer = client.getAuthorizationServer();
			if (authorizationServer.getClientPolicy(client.getMetaData().getClientType()).isRequireRefreshToken(mement.getGrantedScopes())) {
				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				refreshTokenHandler.authTokenStore().deleteBySeries(tenantId, refreshTokenHandler.getType(), token.getSeries());
				AuthToken refreshToken = refreshTokenHandler.newAuthToken(token.getOwnerId(), token.getPolicyName(), new RefreshTokenInfo(client.getMetaData().getName()));
				refreshTokenHandler.authTokenStore().create(refreshToken);
				mement.setRefreshToken(refreshToken);
			}
		}

		@Override
		public void update(AuthToken newToken, AuthToken currentToken) {
			AccessTokenHandler.super.authTokenStore().update(newToken, currentToken);
			
			//create refresh token if none
			AccessTokenMement newMement = (AccessTokenMement) newToken.getDetails();
			AccessTokenMement currentMement = (AccessTokenMement) currentToken.getDetails();
			if (newMement.getGrantedScopes() != null && newMement.getGrantedScopes().contains(OAuthConstants.SCOPE_OFFLINE_ACCESS)) {
				if (currentMement.getGrantedScopes() == null
						|| !currentMement.getGrantedScopes().contains(OAuthConstants.SCOPE_OFFLINE_ACCESS)) {
					OAuthClientRuntime client = OAuthServiceHolder.client.getRuntimeById(newMement.getClientMetaDataId());
					OAuthAuthorizationRuntime authorizationServer = client.getAuthorizationServer();
					if (authorizationServer.getClientPolicy(client.getMetaData().getClientType()).isRequireRefreshToken(newMement.getGrantedScopes())) {
						int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
						refreshTokenHandler.authTokenStore().deleteBySeries(tenantId, refreshTokenHandler.getType(), newToken.getSeries());
						AuthToken refreshToken = refreshTokenHandler.newAuthToken(newToken.getOwnerId(), newToken.getPolicyName(), new RefreshTokenInfo(client.getMetaData().getName()));
						refreshTokenHandler.authTokenStore().create(refreshToken);
						newMement.setRefreshToken(refreshToken);
					}
				}
			}
		}

		@Override
		public void delete(int tenantId, String type, String ownerId) {
			AccessTokenHandler.super.authTokenStore().delete(tenantId, type, ownerId);
			refreshTokenHandler.authTokenStore().delete(tenantId, refreshTokenHandler.getType(), ownerId);
		}

		@Override
		public void deleteBySeries(int tenantId, String type, String series) {
			AccessTokenHandler.super.authTokenStore().deleteBySeries(tenantId, type, series);
			refreshTokenHandler.authTokenStore().deleteBySeries(tenantId, refreshTokenHandler.getType(), series);
		}

		@Override
		public void deleteByDate(int tenantId, String type, Timestamp ts) {
			AccessTokenHandler.super.authTokenStore().deleteByDate(tenantId, type, ts);
			refreshTokenHandler.authTokenStore().deleteByDate(tenantId, refreshTokenHandler.getType(), ts);
		}
		
	}
	
	private String refreshTokenStore;
	private String refreshTokenType;
	private String refreshTokenSecureRandomGeneratorName;
	
	private AccessTokenAuthTokenStore atatStore = new AccessTokenAuthTokenStore();
	private RefreshTokenHandler refreshTokenHandler;
	
	public RefreshTokenHandler refreshTokenHandler() {
		return refreshTokenHandler;
	}
	
	public String getRefreshTokenStore() {
		return refreshTokenStore;
	}

	public void setRefreshTokenStore(String refreshTokenStore) {
		this.refreshTokenStore = refreshTokenStore;
	}

	public String getRefreshTokenType() {
		return refreshTokenType;
	}

	public void setRefreshTokenType(String refreshTokenType) {
		this.refreshTokenType = refreshTokenType;
	}

	public String getRefreshTokenSecureRandomGeneratorName() {
		return refreshTokenSecureRandomGeneratorName;
	}

	public void setRefreshTokenSecureRandomGeneratorName(String refreshTokenSecureRandomGeneratorName) {
		this.refreshTokenSecureRandomGeneratorName = refreshTokenSecureRandomGeneratorName;
	}

	@Override
	public void inited(AuthTokenService service, Config config) {
		super.inited(service, config);
		if (getType() == null) {
			setType(TYPE_OAUTH_DEFAULT);
		}
		
		refreshTokenHandler = new RefreshTokenHandler();
		if (refreshTokenStore != null) {
			refreshTokenHandler.setStore(refreshTokenStore);
		}
		if (refreshTokenType != null) {
			refreshTokenHandler.setType(refreshTokenType);
		} else {
			refreshTokenHandler.setType(TYPE_OAUTH_REFRESH_DEFAULT);
		}
		if (refreshTokenSecureRandomGeneratorName != null) {
			refreshTokenHandler.setSecureRandomGeneratorName(refreshTokenSecureRandomGeneratorName);
		}
		refreshTokenHandler.setHashSettings(getHashSettings());
		refreshTokenHandler.inited(service, config);
	}
	
	public AuthTokenStore authTokenStore() {
		return atatStore;
	}
	
	@Override
	public AuthTokenInfo toAuthTokenInfo(AuthToken authToken) {
		AccessTokenInfo info = new AccessTokenInfo();
		info.setType(getType());
		info.setKey(authToken.getSeries());
		info.setStartDate(authToken.getStartDate());
		((AccessTokenMement) authToken.getDetails()).fill(info);
		
		return info;
	}
	
	@Override
	public Credential toCredential(AuthToken newToken) {
		AccessTokenCredential cre = new AccessTokenCredential(newToken.encodeToken());
		return cre;
	}

	@Override
	protected Serializable createDetails(String seriesString, String tokenString, String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		AccessTokenMement mement = new AccessTokenMement();
		AccessTokenInfo info = (AccessTokenInfo) tokenInfo;
		OAuthClientRuntime client = OAuthServiceHolder.client.getRuntimeByName(info.getClientName());
		OAuthAuthorizationRuntime server = client.getAuthorizationServer();
		ClientPolicyRuntime cp = server.getClientPolicy(client.getMetaData().getClientType());
		long expires = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(cp.getMetaData().getAccessTokenLifetimeSeconds());
		AccessTokenAccountHandle dummy = new AccessTokenAccountHandle(userUniqueId, null, null);
		User user = OAuthServiceHolder.userEntityResolver.searchUser(dummy);
		if (user == null) {
			throw new OAuthRuntimeException("can not search User:" + userUniqueId);
		}
		
		//キャッシュするのでこのタイミングでsubjectIdを付与（もしまだ付与されていなかったら）
		if (server.getSubjectIdentifierType() != null) {
			user = server.getSubjectIdentifierType().handleOnLoad(user);
		}
		
		mement.save(info, expires, userUniqueId, user);
		return mement;
	}

	@Override
	public String newSeriesString(String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		AccessTokenInfo ati = (AccessTokenInfo) tokenInfo;
		OAuthClientRuntime clientRuntime = OAuthServiceHolder.client.getRuntimeByName(ati.getClientName());
		return ((OpaqueOAuthAccessTokenStore) OAuthServiceHolder.authorization.getAccessTokenStore()).toSeriesString(clientRuntime, userUniqueId);
	}

}
