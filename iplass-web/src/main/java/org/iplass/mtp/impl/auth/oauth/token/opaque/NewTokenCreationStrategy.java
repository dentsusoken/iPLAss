/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.transaction.Transaction;

/**
 * 生成リクエストの都度新しいtokenを生成するStrategy。
 * 同一Clientの、同一リソースオーナーの複数端末からリクエストされたら、
 * 有効なトークンはいずれか一方のものとなるが、
 * AuthTokenHandlerでtokenをhashして保存するのは可能。
 * 
 * @author K.Higuchi
 *
 */
public class NewTokenCreationStrategy implements TokenCreationStrategy {

	@Override
	public AuthToken create(OAuthClientRuntime client, AccessTokenHandler handler, String userUniqueId, AccessTokenInfo accessTokenInfo) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String series = handler.newSeriesString(userUniqueId, null, accessTokenInfo);
		
		AuthToken at = Transaction.requiresNew(t -> {
				//delete/insert
				AuthToken storeToken = handler.authTokenStore().getBySeries(tenantId, handler.getType(), series);
				AccessTokenInfo mergedAccessTokenInfo = accessTokenInfo;
				if (storeToken != null) {
					AccessTokenMement atm = (AccessTokenMement) storeToken.getDetails();
					
					//念のためハッシュ衝突をチェック
					if (!atm.getResouceOwnerId().equals(userUniqueId)
							|| !client.getMetaData().getId().equals(atm.getClientMetaDataId())) {
						throw new SystemException("AccessToken's series hash may have collision: client=" + atm.getClientMetaDataId() + " ,series=" + series);
					}
					
					HashSet<String> mergedScopes = new HashSet<>();
					if (atm.getGrantedScopes() != null) {
						mergedScopes.addAll(atm.getGrantedScopes());
					}
					mergedScopes.addAll(accessTokenInfo.getGrantedScopes());
					
					mergedAccessTokenInfo = new AccessTokenInfo();
					mergedAccessTokenInfo.setClientName(accessTokenInfo.getClientName());
					mergedAccessTokenInfo.setGrantedScopes(new ArrayList<>(mergedScopes));
					
					handler.authTokenStore().deleteBySeries(tenantId, handler.getType(), series);
				}
				
				AuthToken newToken = handler.newAuthToken(userUniqueId, null, mergedAccessTokenInfo);
				handler.authTokenStore().create(newToken);
				return newToken;
			});
		
		return at;
	}

	@Override
	public AuthToken create(OAuthClientRuntime client, AccessTokenHandler handler, OpaqueRefreshToken refreshToken) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String series = refreshToken.getSeries();
		
		AuthToken at = Transaction.requiresNew(t -> {
				AuthToken storeToken = handler.authTokenStore().getBySeries(tenantId, handler.getType(), series);
				if (storeToken == null) {
					//ユーザー操作によるToken削除の可能性などあり
					return null;
				}
				AccessTokenMement atm = (AccessTokenMement) storeToken.getDetails();
				
				//念のためハッシュ衝突をチェック(clientのみ)
				if (!client.getMetaData().getId().equals(atm.getClientMetaDataId())) {
					throw new SystemException("AccessToken's series hash may have collision: client=" + atm.getClientMetaDataId() + " ,series=" + series);
				}
				
				//update token and expires
				AccessTokenInfo newAccessTokenInfo = new AccessTokenInfo();
				newAccessTokenInfo.setClientName(client.getMetaData().getName());
				newAccessTokenInfo.setGrantedScopes(new ArrayList<>(atm.getGrantedScopes()));
				AuthToken newToken = handler.newAuthToken(storeToken.getOwnerId(), null, newAccessTokenInfo);
				handler.authTokenStore().update(newToken, storeToken);
				
				return newToken;
			});
		
		return at;
	}

}
