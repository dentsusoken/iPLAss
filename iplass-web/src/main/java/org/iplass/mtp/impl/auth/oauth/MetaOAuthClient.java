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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.auth.oauth.definition.GrantType;
import org.iplass.mtp.auth.oauth.definition.OAuthClientDefinition;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaOAuthClient extends BaseRootMetaData implements DefinableMetaData<OAuthClientDefinition> {
	private static final long serialVersionUID = -3395174355560173705L;

	private static Logger logger = LoggerFactory.getLogger("mtp.auth.oauth");
	
	private String authorizationServerId;
	
	private ClientType clientType;
	
	//clientId=nameとする
	//private String clientId;
	
	private List<String> redirectUris;
	private String sectorIdentifierUri;//subjectのID生成用に
	
	private List<GrantType> grantTypes;//authorization_code, refresh_tokenに限定
	//private List<String> responseTypes;//codeに限定
	//private List<String> scopes;//authServer（のClientポリシー）単位で設定
	
	private String clientUri;
	private String logoUri;
	private List<String> contacts;
	private String tosUri;
	private String policyUri;
	
	//以下OpenID Connect
	//private String applicationType;//web(default) or native
	//private initiateLoginUri;//openid connect
	//:
	//:
	//subject_type
	
	//private List<String> postLogoutRedirectUris;//TODO OpenID Connect Session Management 1.0
	
	public String getClientUri() {
		return clientUri;
	}
	public void setClientUri(String clientUri) {
		this.clientUri = clientUri;
	}
	public String getLogoUri() {
		return logoUri;
	}
	public void setLogoUri(String logoUri) {
		this.logoUri = logoUri;
	}
	public List<String> getContacts() {
		return contacts;
	}
	public void setContacts(List<String> contacts) {
		this.contacts = contacts;
	}
	public String getTosUri() {
		return tosUri;
	}
	public void setTosUri(String tosUri) {
		this.tosUri = tosUri;
	}
	public String getPolicyUri() {
		return policyUri;
	}
	public void setPolicyUri(String policyUri) {
		this.policyUri = policyUri;
	}
	public List<String> getRedirectUris() {
		return redirectUris;
	}
	public void setRedirectUris(List<String> redirectUris) {
		this.redirectUris = redirectUris;
	}
	public String getSectorIdentifierUri() {
		return sectorIdentifierUri;
	}
	public void setSectorIdentifierUri(String sectorIdentifierUri) {
		this.sectorIdentifierUri = sectorIdentifierUri;
	}
	public List<GrantType> getGrantTypes() {
		return grantTypes;
	}
	public void setGrantTypes(List<GrantType> grantTypes) {
		this.grantTypes = grantTypes;
	}
	public ClientType getClientType() {
		return clientType;
	}
	public void setClientType(ClientType clientType) {
		this.clientType = clientType;
	}
	public String getAuthorizationServerId() {
		return authorizationServerId;
	}
	public void setAuthorizationServerId(String authorizationServerId) {
		this.authorizationServerId = authorizationServerId;
	}

	@Override
	public OAuthClientRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new OAuthClientRuntime();
	}

	@Override
	public MetaOAuthClient copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(OAuthClientDefinition def) {
		name = def.getName();
		description = def.getDescription();
		displayName = def.getDisplayName();
		localizedDisplayNameList = I18nUtil.toMeta(def.getLocalizedDisplayNameList());
		
		if (def.getAuthorizationServer() != null) {
			OAuthAuthorizationService oauthService = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
			authorizationServerId = oauthService.getRuntimeByName(def.getAuthorizationServer()).getMetaData().getId();
		} else {
			authorizationServerId = null;
		}
		
		clientType = def.getClientType();
		if (def.getRedirectUris() != null) {
			redirectUris = new ArrayList<>(def.getRedirectUris());
		} else {
			redirectUris = null;
		}
		sectorIdentifierUri = def.getSectorIdentifierUri();
		if (def.getGrantTypes() != null) {
			grantTypes = new ArrayList<>(def.getGrantTypes());
		} else {
			grantTypes = null;
		}
		clientUri = def.getClientUri();
		logoUri = def.getLogoUri();
		if (def.getContacts() != null) {
			contacts = new ArrayList<>(def.getContacts());
		} else {
			contacts = null;
		}
		tosUri = def.getTosUri();
		policyUri = def.getPolicyUri();
	}

	@Override
	public OAuthClientDefinition currentConfig() {
		OAuthClientDefinition def = new OAuthClientDefinition();
		def.setName(name);
		def.setDescription(description);
		def.setDisplayName(displayName);
		def.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
		
		if (authorizationServerId != null) {
			OAuthAuthorizationRuntime oauthRuntime = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class).getRuntimeById(authorizationServerId);
			if (oauthRuntime != null) {
				def.setAuthorizationServer(oauthRuntime.getMetaData().getName());
			}
		}
		
		def.setClientType(clientType);
		if (redirectUris != null) {
			def.setRedirectUris(new ArrayList<>(redirectUris));
		}
		def.setSectorIdentifierUri(sectorIdentifierUri);
		if (grantTypes != null) {
			def.setGrantTypes(new ArrayList<>(grantTypes));
		}
		def.setClientUri(clientUri);
		def.setLogoUri(logoUri);
		if (contacts != null) {
			def.setContacts(new ArrayList<>(contacts));
		}
		def.setTosUri(tosUri);
		def.setPolicyUri(policyUri);
		
		return def;
	}
	
	public class OAuthClientRuntime extends BaseMetaDataRuntime {
		private OAuthAuthorizationService serverService = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
		private OAuthClientCredentialHandler ch = (OAuthClientCredentialHandler) ServiceRegistry.getRegistry().getService(AuthTokenService.class).getHandler(OAuthClientCredentialHandler.TYPE_CLIENT);
		private String sectorId;
		
		public OAuthClientRuntime() {
			try {
				if (redirectUris == null || redirectUris.size() == 0) {
					throw new IllegalStateException("redirectUris  must be specified");
				} else {
					for (String ru: redirectUris) {
						//check redirectUris valid
						try {
							new URI(ru);
						} catch (URISyntaxException e) {
							throw new IllegalStateException("redirectUris must valid uri:" + ru, e);
						}
					}
				}
				
				sectorId = genSectorId();
				
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}
		
		private String genSectorId() {
			
			String targetHost = null;
			if (sectorIdentifierUri != null) {
				try {
					targetHost = new URI(sectorIdentifierUri).getHost();
				} catch (URISyntaxException e) {
					throw new IllegalStateException("sectorIdentifierUri must valid uri", e);
				}
			} else if (redirectUris != null) {
				for (String ru: redirectUris) {
					try {
						String host = new URI(ru).getHost();
						if (targetHost == null) {
							targetHost = host;
						} else {
							if (!targetHost.equals(host)) {
								throw new IllegalStateException("if set multi-domain redirectUris, sectorIdentifierUri must specify");
							}
						}
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
				}
			}
			return targetHost;
		}

		@Override
		public MetaOAuthClient getMetaData() {
			return MetaOAuthClient.this;
		}
		
		public OAuthAuthorizationRuntime getAuthorizationServer() {
			return serverService.getRuntimeById(authorizationServerId);
		}
		
		public Credential generateCredential() {
			if (clientType == ClientType.PUBLIC) {
				throw new OAuthRuntimeException("Public client can not generate credential.");
			}
			
			return ch.generateCredential(getName());
		}
		
		public boolean validateCredential(Credential cre, boolean allowPublicClient) {
			if (clientType == ClientType.PUBLIC) {
				if (allowPublicClient) {
					if (!cre.getId().equals(getName())) {
						if (logger.isWarnEnabled()) {
							logger.warn(getName() + ",publicClientValidate,fail");
						}
						return false;
					}

					return true;
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn(getName() + ",publicClientValidate,fail");
					}
					return false;
				}
			}
			
			return ch.validateCredential(cre, getName());
		}
		
		public void deleteOldCredential() {
			ch.deleteOldCredential(getName());
		}

		public String selectValidRedirectUri(String redirectUri) {
			if (redirectUri == null) {
				return null;
			}
			
			if (redirectUris == null) {
				return null;
			}
			
			for (String ru: redirectUris) {
				if (ru.equals(redirectUri)) {
					return ru;
				}
			}
			
			return null;
		}
		
		public String sectorIdentifier() {
			if (sectorId == null) {
				throw new NullPointerException("sectorIdentifier is null");
			}
			return sectorId;
		}
	}

}
