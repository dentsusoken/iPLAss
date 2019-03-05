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
package org.iplass.mtp.impl.auth.oauth;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.auth.oauth.definition.ClientPolicyDefinition;
import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.impl.auth.oauth.MetaConsentType.ConsentTypeRuntime;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

public class MetaClientPolicy implements MetaData {
	private static final long serialVersionUID = 3667544863207206056L;

	//TODO ClientType別にClientの認証方法をカスタム可能に（たとえば、public clientの場合、client_id以外にユーザエージェントもチェックしたいとか）
	//tokenEndpointAuthMethod

	private ClientType clientType;
	
	private long accessTokenLifetimeSeconds;
	private boolean supportRefreshToken;
	private long refreshTokenLifetimeSeconds;
	private MetaConsentType consentType;
	private List<String> scopes;
	private boolean supportOpenIDConnect;

	public boolean isSupportOpenIDConnect() {
		return supportOpenIDConnect;
	}

	public void setSupportOpenIDConnect(boolean supportOpenIDConnect) {
		this.supportOpenIDConnect = supportOpenIDConnect;
	}

	public List<String> getScopes() {
		return scopes;
	}

	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}

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

	public MetaConsentType getConsentType() {
		return consentType;
	}

	public void setConsentType(MetaConsentType consentType) {
		this.consentType = consentType;
	}

	@Override
	public MetaClientPolicy copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	public ClientPolicyRuntime createRuntime(MetaOAuthAuthorization metaOauth) {
		return new ClientPolicyRuntime(metaOauth);
	}
	
	public class ClientPolicyRuntime {
		
		private ConsentTypeRuntime consentTypeRuntime;
		private List<String> scopeList;
		
		private ClientPolicyRuntime(MetaOAuthAuthorization metaOauth) {
			if (consentType != null) {
				consentTypeRuntime = consentType.createRuntime(metaOauth.getId(), clientType);
			}
			
			scopeList = new ArrayList<>();
			for (MetaScope ms: metaOauth.getScopes()) {
				if (getScopes() == null
						|| getScopes().contains(ms.getName())) {
					scopeList.add(ms.getName());
				}
			}
			
			//add standard scopes
			if (supportRefreshToken) {
				if (!scopeList.contains(OAuthConstants.SCOPE_OFFLINE_ACCESS)) {
					scopeList.add(OAuthConstants.SCOPE_OFFLINE_ACCESS);
				}
			}
			if (supportOpenIDConnect) {
				if (!scopeList.contains(OAuthConstants.SCOPE_OPENID)) {
					scopeList.add(OAuthConstants.SCOPE_OPENID);
				}
			}
			
		}
		
		public MetaClientPolicy getMetaData() {
			return MetaClientPolicy.this;
		}
		
		public ConsentTypeRuntime consentType() {
			return consentTypeRuntime;
		}
		
		public List<String> scopeList() {
			return scopeList;
		}
		
		public boolean isRequireRefreshToken(List<String> grantedScopes) {
			if (supportRefreshToken) {
				if (grantedScopes != null && grantedScopes.contains(OAuthConstants.SCOPE_OFFLINE_ACCESS)) {
					return true;
				}
			}
			
			return false;
		}
		
		public boolean isRequireIdToken(List<String> grantedScopes) {
			if (supportOpenIDConnect) {
				if (grantedScopes != null && grantedScopes.contains(OAuthConstants.SCOPE_OPENID)) {
					return true;
				}
			}
			
			return false;
		}
	}

	public void applyConfig(ClientPolicyDefinition def) {
		clientType = def.getClientType();
		accessTokenLifetimeSeconds = def.getAccessTokenLifetimeSeconds();
		supportRefreshToken = def.isSupportRefreshToken();
		refreshTokenLifetimeSeconds = def.getRefreshTokenLifetimeSeconds();
		if (def.getConsentType() != null) {
			consentType = MetaConsentType.createInstance(def.getConsentType());
			consentType.applyConfig(def.getConsentType());
		} else {
			consentType = null;
		}
		if (def.getScopes() != null) {
			scopes = new ArrayList<>(def.getScopes());
		} else {
			scopes = null;
		}
		supportOpenIDConnect = def.isSupportOpenIDConnect();
	}
	
	public ClientPolicyDefinition currentConfig() {
		ClientPolicyDefinition def = new ClientPolicyDefinition();
		def.setClientType(clientType);
		def.setAccessTokenLifetimeSeconds(accessTokenLifetimeSeconds);
		def.setSupportRefreshToken(supportRefreshToken);
		def.setRefreshTokenLifetimeSeconds(refreshTokenLifetimeSeconds);
		if (consentType != null) {
			def.setConsentType(consentType.currentConfig());
		}
		if (scopes != null) {
			def.setScopes(new ArrayList<>(scopes));
		}
		def.setSupportOpenIDConnect(supportOpenIDConnect);
		return def;
	}

}
