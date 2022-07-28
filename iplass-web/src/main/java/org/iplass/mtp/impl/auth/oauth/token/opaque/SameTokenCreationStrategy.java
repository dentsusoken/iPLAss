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
package org.iplass.mtp.impl.auth.oauth.token.opaque;

import java.util.ArrayList;
import java.util.HashSet;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.oauth.AccessTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.token.opaque.RefreshTokenMement.RefreshTokenInfo;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 以前に生成したもの（且つまだ有効期間内のもの）と同一のTokenを返却するStrategy。
 * AuthTokenHandlerでは、tokenのhash設定はできない。
 * 同一Clientの同一リソースオーナーの複数端末からTokenリクエストがあっても、
 * いずれのリクエストにも有効な（同一の）Tokenを返すことができる。
 * 
 * @author K.Higuchi
 *
 */
public class SameTokenCreationStrategy implements TokenCreationStrategy {
	
	private static Logger logger = LoggerFactory.getLogger(SameTokenCreationStrategy.class);
	
	private long retryIntervalMillis;
	private int retryCount;

	public long getRetryIntervalMillis() {
		return retryIntervalMillis;
	}

	public void setRetryIntervalMillis(long retryIntervalMillis) {
		this.retryIntervalMillis = retryIntervalMillis;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	@Override
	public AuthToken create(OAuthClientRuntime client, AccessTokenHandler handler, String userUniqueId, AccessTokenInfo accessTokenInfo) {
		//並列でコールされうることを考慮してリトライする
		
		if (handler.getHashSettings() != null && handler.getHashSettings().size() > 0) {
			throw new SystemException("SameTokenCreationStrategy is not support AuthToken hash setting.");
		}
		
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String series = handler.newSeriesString(userUniqueId, null, accessTokenInfo);
		
		for (int i = 0; i <= retryCount; i++) {
			AuthToken at = null;
			
			try {
				at = Transaction.requiresNew(t -> {
					AuthToken storeToken = handler.authTokenStore().getBySeries(tenantId, handler.getType(), series);
					if (storeToken == null) {
						//create new
						AuthToken newToken = handler.newAuthToken(userUniqueId, null, accessTokenInfo);
						handler.authTokenStore().create(newToken);
						return newToken;
					}
					
					//新規のscopeが含まれている場合は作り直し
					AccessTokenMement atm = (AccessTokenMement) storeToken.getDetails();
					
					//念のためハッシュ衝突をチェック
					if (!atm.getResouceOwnerId().equals(userUniqueId)
							|| !client.getMetaData().getId().equals(atm.getClientMetaDataId())) {
						throw new SystemException("AccessToken's series hash may have collision: client=" + atm.getClientMetaDataId() + " ,series=" + series);
					}
					
					if (atm.getGrantedScopes() == null || !atm.getGrantedScopes().containsAll(accessTokenInfo.getGrantedScopes())) {
						HashSet<String> mergedScopes = new HashSet<>();
						if (atm.getGrantedScopes() != null) {
							mergedScopes.addAll(atm.getGrantedScopes());
						}
						mergedScopes.addAll(accessTokenInfo.getGrantedScopes());
						AccessTokenInfo mergedAccessTokenInfo = new AccessTokenInfo();
						mergedAccessTokenInfo.setClientName(accessTokenInfo.getClientName());
						mergedAccessTokenInfo.setGrantedScopes(new ArrayList<>(mergedScopes));
						
						//delete/create new
						handler.authTokenStore().deleteBySeries(tenantId, handler.getType(), series);
						AuthToken newToken = handler.newAuthToken(userUniqueId, null, mergedAccessTokenInfo);
						handler.authTokenStore().create(newToken);
						return newToken;
					}
					
					
					//scopeが含まれている、且つ有効期限内であれば、同一のトークンを返却
					AuthToken retToken = null;
					if (atm.getExpires() > System.currentTimeMillis()) {
						retToken = storeToken;
					} else {
						//update token and expires
						AccessTokenInfo newAccessTokenInfo = new AccessTokenInfo();
						newAccessTokenInfo.setClientName(client.getMetaData().getName());
						newAccessTokenInfo.setGrantedScopes(new ArrayList<>(atm.getGrantedScopes()));
						AuthToken newToken = handler.newAuthToken(userUniqueId, null, newAccessTokenInfo);
						handler.authTokenStore().update(newToken, storeToken);
						retToken = newToken;
					}
					
					//check and create refreshToken
					AccessTokenMement retTokenMement = (AccessTokenMement) retToken.getDetails();
					boolean needRefreshToken = false;
					OAuthAuthorizationRuntime authorizationServer = null;
					if (accessTokenInfo.getGrantedScopes().contains(OAuthConstants.SCOPE_OFFLINE_ACCESS)) {
						authorizationServer = client.getAuthorizationServer();
						if (authorizationServer.getClientPolicy(client.getMetaData().getClientType()).isRequireRefreshToken(accessTokenInfo.getGrantedScopes())) {
							needRefreshToken = true;
						}
					}
					
					if (needRefreshToken && retTokenMement.getRefreshToken() == null) {
						retTokenMement.setRefreshToken(handler.refreshTokenHandler().authTokenStore().getBySeries(tenantId, handler.refreshTokenHandler().getType(), retToken.getSeries()));
					}
					
					if (needRefreshToken) {
						if (retTokenMement.getRefreshToken() == null
								|| ((RefreshTokenMement) retTokenMement.getRefreshToken().getDetails()).getExpires() <= System.currentTimeMillis()) {
							handler.refreshTokenHandler().authTokenStore().deleteBySeries(tenantId, handler.refreshTokenHandler().getType(), retToken.getSeries());
							AuthToken refreshToken = handler.refreshTokenHandler().newAuthToken(retToken.getOwnerId(), retToken.getPolicyName(), new RefreshTokenInfo(client.getMetaData().getName()));
							handler.refreshTokenHandler().authTokenStore().create(refreshToken);
							retTokenMement.setRefreshToken(refreshToken);
						}
					}

					return retToken;
				});
				
			} catch (RuntimeException e) {
				if (i == retryCount) {
					throw e;
				} else {
					logger.warn("AuthToken:" + series + " update failed, do re-try...", e);
				}
			}
			
			if (at != null) {
				return at;
			}
			
			try {
				Thread.sleep(retryIntervalMillis);
			} catch (InterruptedException e) {
				throw new SystemException("thread is interrupted.", e);
			}
		}
		throw new SystemException("Can not create AuthToken:" + series + ". retry count over");
	}

	@Override
	public AuthToken create(OAuthClientRuntime client, AccessTokenHandler handler, OpaqueRefreshToken refreshToken) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String series = refreshToken.getSeries();
		
		for (int i = 0; i <= retryCount; i++) {
			AuthToken at = null;
			
			try {
				at = Transaction.requiresNew(t -> {
					AuthToken storeToken = handler.authTokenStore().getBySeries(tenantId, handler.getType(), series);
					if (storeToken == null) {
						//ユーザ操作によるToken削除の可能性などあり
						return null;
					}
					AccessTokenMement atm = (AccessTokenMement) storeToken.getDetails();
					
					//念のためハッシュ衝突をチェック(clientのみ)
					if (!client.getMetaData().getId().equals(atm.getClientMetaDataId())) {
						throw new SystemException("AccessToken's series hash may have collision: client=" + atm.getClientMetaDataId() + " ,series=" + series);
					}
					
					//別リクエストが更新した
					if (atm.getExpires() > System.currentTimeMillis()) {
						return storeToken;
					} else {
						//update token and expires
						AccessTokenInfo newAccessTokenInfo = new AccessTokenInfo();
						newAccessTokenInfo.setClientName(client.getMetaData().getName());
						newAccessTokenInfo.setGrantedScopes(new ArrayList<>(atm.getGrantedScopes()));
						AuthToken newToken = handler.newAuthToken(storeToken.getOwnerId(), null, newAccessTokenInfo);
						handler.authTokenStore().update(newToken, storeToken);
						
						return newToken;
					}
				});
		
			} catch (RuntimeException e) {
				if (i == retryCount) {
					throw e;
				} else {
					logger.warn("AuthToken:" + series + " update failed, do re-try...", e);
				}
			}
			if (at != null) {
				return at;
			}
			
			try {
				Thread.sleep(retryIntervalMillis);
			} catch (InterruptedException e) {
				throw new SystemException("thread is interrupted.", e);
			}
		}
		throw new SystemException("Can not refresh AuthToken:" + series + ". retry count over");
	}

}
