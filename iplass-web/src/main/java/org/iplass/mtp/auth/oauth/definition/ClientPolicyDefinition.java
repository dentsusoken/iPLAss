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
package org.iplass.mtp.auth.oauth.definition;

import java.io.Serializable;
import java.util.List;

/**
 * ClientType別のポリシー定義です。
 * 
 * @author K.Higuchi
 *
 */
public class ClientPolicyDefinition implements Serializable {
	private static final long serialVersionUID = 4024837978999496199L;

	private ClientType clientType;
	private long accessTokenLifetimeSeconds;
	private boolean supportRefreshToken;
	private long refreshTokenLifetimeSeconds;
	private ConsentTypeDefinition consentType;
	private List<String> scopes;
	private boolean supportOpenIDConnect;
	
	public ClientType getClientType() {
		return clientType;
	}
	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
	public long getAccessTokenLifetimeSeconds() {
		return accessTokenLifetimeSeconds;
	}
	public void setAccessTokenLifetimeSeconds(long accessTokenLifetimeSeconds) {
		this.accessTokenLifetimeSeconds = accessTokenLifetimeSeconds;
	}
	public boolean isSupportRefreshToken() {
		return supportRefreshToken;
	}
	public void setSupportRefreshToken(boolean supportRefreshToken) {
		this.supportRefreshToken = supportRefreshToken;
	}
	public long getRefreshTokenLifetimeSeconds() {
		return refreshTokenLifetimeSeconds;
	}
	public void setRefreshTokenLifetimeSeconds(long refreshTokenLifetimeSeconds) {
		this.refreshTokenLifetimeSeconds = refreshTokenLifetimeSeconds;
	}
	public ConsentTypeDefinition getConsentType() {
		return consentType;
	}
	public void setConsentType(ConsentTypeDefinition consentType) {
		this.consentType = consentType;
	}
	public List<String> getScopes() {
		return scopes;
	}
	
	/**
	 * ClientType毎に利用可能なscopeを限定する場合設定します。
	 * 未設定の場合は、OAuthAuthorizationDefinitionに定義されるすべてのscopeが利用可能です。
	 * 
	 * @param scopes
	 */
	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}
	public boolean isSupportOpenIDConnect() {
		return supportOpenIDConnect;
	}
	public void setSupportOpenIDConnect(boolean supportOpenIDConnect) {
		this.supportOpenIDConnect = supportOpenIDConnect;
	}
}
