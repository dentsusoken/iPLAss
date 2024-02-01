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
package org.iplass.mtp.impl.auth.oauth.token;

import java.util.List;

import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;

public interface OAuthAccessTokenStore {

	public AccessToken createAccessToken(OAuthClientRuntime client, String resourceOwnerId, List<String> scopes);
	public AccessToken createAccessToken(OAuthClientRuntime client, RefreshToken refreshToken);
	public AccessToken getAccessTokenByUserOid(OAuthClientRuntime client, String userOid);
	public AccessToken getAccessToken(String tokenString);
	public RefreshToken getRefreshToken(String tokenString);
	public void revokeToken(OAuthClientRuntime client, String tokenString, String tokenTypeHint);
	public void revokeTokenByUserOid(String userOid);

}
