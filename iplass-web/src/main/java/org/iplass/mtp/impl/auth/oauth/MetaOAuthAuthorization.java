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
package org.iplass.mtp.impl.auth.oauth;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.oauth.definition.ClientPolicyDefinition;
import org.iplass.mtp.auth.oauth.definition.ClientType;
import org.iplass.mtp.auth.oauth.definition.GrantType;
import org.iplass.mtp.auth.oauth.definition.OAuthAuthorizationDefinition;
import org.iplass.mtp.auth.oauth.definition.ScopeDefinition;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.oauth.MetaClientPolicy.ClientPolicyRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOIDCClaimScope.OIDCClaimScopeRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaSubjectIdentifierType.SubjectIdentifierTypeRuntime;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationCode;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationRequest;
import org.iplass.mtp.impl.auth.oauth.idtoken.IdToken;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.auth.oauth.token.RefreshToken;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthUtil;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.template.TemplateUtil;

public class MetaOAuthAuthorization extends BaseRootMetaData implements DefinableMetaData<OAuthAuthorizationDefinition> {
	private static final long serialVersionUID = 3413613829144055452L;

	private static List<MetaScope> standardScopes = new ArrayList<>();
	static {
		standardScopes.add(new MetaScope(OAuthConstants.SCOPE_OFFLINE_ACCESS, "Offline Access", "Application requres offline access to your resources."));
		standardScopes.add(new MetaScope(OAuthConstants.SCOPE_OPENID, "OpenID", "Application requires your public identifier(OpenID)."));
	}
	
	private List<String> availableRoles;
	private List<MetaScope> scopes;
	private String consentTemplateName;
	private List<MetaClientPolicy> clientPolicies;
	private MetaSubjectIdentifierType subjectIdentifierType;
	private String issuerUri;
	
	public String getIssuerUri() {
		return issuerUri;
	}

	public void setIssuerUri(String issuerUri) {
		this.issuerUri = issuerUri;
	}

	public MetaSubjectIdentifierType getSubjectIdentifierType() {
		return subjectIdentifierType;
	}

	public void setSubjectIdentifierType(MetaSubjectIdentifierType subjectIdentifierType) {
		this.subjectIdentifierType = subjectIdentifierType;
	}

	public List<MetaClientPolicy> getClientPolicies() {
		return clientPolicies;
	}

	public void setClientPolicies(List<MetaClientPolicy> clientPolicies) {
		this.clientPolicies = clientPolicies;
	}

	public List<String> getAvailableRoles() {
		return availableRoles;
	}

	public void setAvailableRoles(List<String> availableRoles) {
		this.availableRoles = availableRoles;
	}

	public String getConsentTemplateName() {
		return consentTemplateName;
	}

	public void setConsentTemplateName(String consentTemplateName) {
		this.consentTemplateName = consentTemplateName;
	}

	public List<MetaScope> getScopes() {
		return scopes;
	}

	public void setScopes(List<MetaScope> scopes) {
		this.scopes = scopes;
	}

	@Override
	public OAuthAuthorizationRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new OAuthAuthorizationRuntime();
	}

	@Override
	public MetaOAuthAuthorization copy() {
		return ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(OAuthAuthorizationDefinition def) {
		name = def.getName();
		description = def.getDescription();
		displayName = def.getDisplayName();
		if (def.getAvailableRoles() != null) {
			availableRoles = new ArrayList<>(def.getAvailableRoles());
		} else {
			availableRoles = null;
		}
		if (def.getScopes() != null) {
			scopes = new ArrayList<>();
			for (ScopeDefinition sd: def.getScopes()) {
				MetaScope ms = MetaScope.createInstance(sd);
				ms.applyConfig(sd);
				scopes.add(ms);
			}
		} else {
			scopes = null;
		}
		consentTemplateName = def.getConsentTemplateName();
		if (def.getClientPolicies() != null) {
			clientPolicies = new ArrayList<>();
			for (ClientPolicyDefinition cpd: def.getClientPolicies()) {
				MetaClientPolicy mcp = new MetaClientPolicy();
				mcp.applyConfig(cpd);
				clientPolicies.add(mcp);
				
			}
		} else {
			clientPolicies = null;
		}
		if (def.getSubjectIdentifierType() != null) {
			subjectIdentifierType = MetaSubjectIdentifierType.createInstance(def.getSubjectIdentifierType());
			subjectIdentifierType.applyConfig(def.getSubjectIdentifierType());
		} else {
			subjectIdentifierType = null;
		}
		issuerUri = def.getIssuerUri();
	}

	@Override
	public OAuthAuthorizationDefinition currentConfig() {
		OAuthAuthorizationDefinition def = new OAuthAuthorizationDefinition();
		def.setName(name);
		def.setDescription(description);
		def.setDisplayName(displayName);
		
		if (availableRoles != null) {
			def.setAvailableRoles(new ArrayList<>(availableRoles));
		}
		if (scopes != null) {
			ArrayList<ScopeDefinition> list = new ArrayList<>();
			for (MetaScope ms: scopes) {
				list.add(ms.currentConfig());
			}
			def.setScopes(list);
		}
		def.setConsentTemplateName(consentTemplateName);
		if (clientPolicies != null) {
			ArrayList<ClientPolicyDefinition> list = new ArrayList<>();
			for (MetaClientPolicy mcp: clientPolicies) {
				list.add(mcp.currentConfig());
			}
			def.setClientPolicies(list);
		}
		if (subjectIdentifierType != null) {
			def.setSubjectIdentifierType(subjectIdentifierType.currentConfig());
		}
		def.setIssuerUri(issuerUri);
		
		return def;
	}
	
	public class OAuthAuthorizationRuntime extends BaseMetaDataRuntime {
		
		private OAuthAuthorizationService service = ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class);
		private OAuthClientService clientService = ServiceRegistry.getRegistry().getService(OAuthClientService.class);
		
		private EnumMap<ClientType, ClientPolicyRuntime> clientPolicyRuntimeMap;
		private SubjectIdentifierTypeRuntime subjectIdentifierTypeRuntime;
		private Map<String, MetaScope> scopeMap;
		private Map<String, OIDCClaimScopeRuntime> oidcClaimScopeMap;
		
		
		private OAuthAuthorizationRuntime() {
			try {
				boolean useOpenIdConnect = false;
				clientPolicyRuntimeMap = new EnumMap<>(ClientType.class);
				if (clientPolicies != null) {
					for (MetaClientPolicy cp: clientPolicies) {
						clientPolicyRuntimeMap.put(cp.getClientType(), cp.createRuntime(MetaOAuthAuthorization.this));
						useOpenIdConnect = useOpenIdConnect || cp.isSupportOpenIDConnect();
					}
				}
				if (subjectIdentifierType == null && useOpenIdConnect) {
					throw new NullPointerException("subjectIdentifierType must be specified for OpenID Connect");
				}
				if (subjectIdentifierType != null) {
					subjectIdentifierTypeRuntime = subjectIdentifierType.createRuntime();
				}
				scopeMap = new HashMap<>();
				oidcClaimScopeMap = new HashMap<>();
				if (scopes != null) {
					for (MetaScope ms: scopes) {
						scopeMap.put(ms.getName(), ms);
						if (ms instanceof MetaOIDCClaimScope) {
							oidcClaimScopeMap.put(ms.getName(), ((MetaOIDCClaimScope) ms).createRuntime(getName()));
						}
					}
				}
				for (MetaScope ms: standardScopes) {
					if (!scopeMap.containsKey(ms.getName())) {
						scopeMap.put(ms.getName(), ms);
					}
				}
				
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}
		
		@Override
		public MetaOAuthAuthorization getMetaData() {
			return MetaOAuthAuthorization.this;
		}
		
		public SubjectIdentifierTypeRuntime getSubjectIdentifierType() {
			return subjectIdentifierTypeRuntime;
		}

		public void checkValidAuthorizationRequest(AuthorizationRequest authorizationRequest) {
			if (authorizationRequest.getClientId() == null) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "client_id required.");
			}
			OAuthClientRuntime clientRuntime = clientService.getRuntimeByName(authorizationRequest.getClientId());
			if (clientRuntime == null) {
				throw new IllegalArgumentException("OAuthClient not found:" + authorizationRequest.getClientId());
			}
			if (!clientRuntime.getMetaData().getAuthorizationServerId().equals(getId())) {
				throw new IllegalArgumentException("OAuthClient is not registered to AuthServer:" + authorizationRequest.getClientId());
			}
			
			if (clientRuntime.getMetaData().getGrantTypes() == null
					|| !clientRuntime.getMetaData().getGrantTypes().contains(GrantType.AUTHORIZATION_CODE)) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_UNAUTHORIZED_CLIENT, "grant_type not allowed.");
			}
			if (authorizationRequest.getRedirectUri() == null) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "redirect_uri required.");
			}
			if (authorizationRequest.getResponseTypes() == null || authorizationRequest.getResponseTypes().size() == 0) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "response_type required.");
			}
			for (String rt: authorizationRequest.getResponseTypes()) {
				//currently only code is supported
				if (!OAuthConstants.RESPONSE_TYPE_CODE.equals(rt)) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_UNSUPPORTED_RESPONSE_TYPE, "invalid response_type.");
				}
			}
			if (authorizationRequest.getScopes() == null || authorizationRequest.getScopes().size() == 0) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "scope required.");
			}
			List<String> scopesByClientType = getClientPolicy(clientRuntime.getMetaData().getClientType()).scopeList();
			if (!scopesByClientType.containsAll(authorizationRequest.getScopes())) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_SCOPE, "invalid scope.");
			}
			if (service.isParamStateRequired()) {
				if (authorizationRequest.getState() == null) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "state required.");
				}
			}
			if (service.isParamNonceRequired()) {
				if (authorizationRequest.getNonce() == null) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "nonce required.");
				}
			}
			
			if (authorizationRequest.getResponseMode() != null) {
				if (!OAuthConstants.RESPONSE_MODE_FORM_POST.equals(authorizationRequest.getResponseMode())
						&& !OAuthConstants.RESPONSE_MODE_QUERY.equals(authorizationRequest.getResponseMode())
						&& !OAuthConstants.RESPONSE_MODE_FRAGMENT.equals(authorizationRequest.getResponseMode())) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "invalid response_mode.");
				}
			}
			
			if (authorizationRequest.getCodeChallenge() == null && authorizationRequest.getCodeChallengeMethod() == null) {
				if (service.isForcePKCE()) {
					if (clientRuntime.getMetaData().getClientType() == ClientType.PUBLIC) {
						throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "PKCE required for public clients.");
					}
				}
				
			} else {
				if (authorizationRequest.getCodeChallenge() == null) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "code_challenge required.");
				}
				if (service.isForceS256ForCodeChallengeMethod()) {
					if (!OAuthConstants.CODE_CHALLENGE_METHOD_S256.equals(authorizationRequest.getCodeChallengeMethod())) {
						throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "code_challenge_method only support S256.");
					}
				} else {
					if (authorizationRequest.getCodeChallengeMethod() != null) {
						if (!OAuthConstants.CODE_CHALLENGE_METHOD_S256.equals(authorizationRequest.getCodeChallengeMethod())
								|| !OAuthConstants.CODE_CHALLENGE_METHOD_PLAIN.equals(authorizationRequest.getCodeChallengeMethod())) {
							throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "invalid code_challenge_method.");
						}
					}
				}
			}
			
			if (authorizationRequest.getPrompt() != null && authorizationRequest.getPrompt().size() > 0) {
				if (authorizationRequest.getPrompt().contains(OAuthConstants.PROMPT_NONE)) {
					if (authorizationRequest.getPrompt().size() != 1) {
						throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "invalid prompt.");
					}
				} else {
					if (authorizationRequest.getPrompt().size() > 2) {
						//currently supports "login" and "consent"
						throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "invalid prompt.");
					}
					for (String p: authorizationRequest.getPrompt()) {
						if (!p.equals(OAuthConstants.PROMPT_CONSENT) && !p.equals(OAuthConstants.PROMPT_LOGIN)) {
							throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "invalid prompt.");
						}
					}
				}
			}
			
			if (authorizationRequest.getMaxAge() != null) {
				if (authorizationRequest.getMaxAge().longValue() < 0) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_REQUEST, "invalid max_age.");
				}
			}
			
		}
		
		public ClientPolicyRuntime getClientPolicy(ClientType clientType) {
			return clientPolicyRuntimeMap.get(clientType);
		}
		
		public List<MetaScope> getScopeByName(List<String> scopeNames) {
			ArrayList<MetaScope> ret = new ArrayList<>();
			for (String scopeName: scopeNames) {
				MetaScope ms = scopeMap.get(scopeName);
				if (ms != null) {
					ret.add(ms);
				}
			}
			
			return ret;
		}
		
		public MetaScope getScope(String scopeName) {
			return scopeMap.get(scopeName);
		}
		
		public OIDCClaimScopeRuntime getOIDCClaimScope(String scopeName) {
			return oidcClaimScopeMap.get(scopeName);
		}

		public boolean isNeedConsent(RequestContext request, AuthorizationRequest authReq) {
			OAuthClientRuntime client = clientService.getRuntimeByName(authReq.getClientId());
			ClientPolicyRuntime clientPolicy = clientPolicyRuntimeMap.get(client.getMetaData().getClientType());
			
			AccessToken current = service.getAccessTokenStore().getAccessTokenByUserOid(client, AuthContext.getCurrentContext().getUser().getOid());
			return clientPolicy.consentType().needConsent(request, authReq.getScopes(), current);
		}

		public AuthorizationCode generateCode(AuthorizationRequest authReq) {
			AuthContext authContext = AuthContext.getCurrentContext();
			authReq.setUser(authContext.getUser());
			authReq.setAuthTime(authContext.getAuthTime());
			
			return service.getAuthorizationCodeStore().newAuthorizationCode(authReq);
		}

		public String consentTemplateName() {
			//metadata or ServiceConfigのデフォルトから取得
			if (consentTemplateName != null) {
				return consentTemplateName;
			}
			return service.getDefaultConsentTemplateName();
		}
		
		public boolean hasAvailableRole() {
			
			if (availableRoles == null) {
				return false;
			}
			
			AuthContext ac = AuthContext.getCurrentContext();
			for (String r: availableRoles) {
				// *は全部OK
				if (OAuthAuthorizationDefinition.AVAILABLE_ROLE_ANY.equals(r)) {
					return true;
				}
				if (ac.userInRole(r)) {
					return true;
				}
			}
			
			return false;
		}
		
		public OAuthTokens exchangeCodeToToken(String codeStr, String redirectUri, String codeVerifier, OAuthClientRuntime client) {
			if (!getId().equals(client.getMetaData().getAuthorizationServerId())) {
				throw new OAuthRuntimeException("client's authServer is unmatch");
			}
			
			AuthorizationCode code = service.getAuthorizationCodeStore().getAndRemoveAuthorizationCode(codeStr);
			if (code == null
					|| !code.getRequest().getClientId().equals(client.getMetaData().getName())
					|| !code.getRequest().getRedirectUri().equals(redirectUri)
					|| code.getExpires() < System.currentTimeMillis()) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_GRANT, "invalid code/redirect_uri/client_id/code_verifier.");
			}
			
			//PKCE
			if (code.getRequest().getCodeChallenge() != null) {
				if (!code.getRequest().getCodeChallenge().equals(OAuthUtil.calcCodeChallenge(code.getRequest().getCodeChallengeMethod(), codeVerifier))) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_GRANT, "invalid code/redirect_uri/client_id/code_verifier.");
				}
			} else if (codeVerifier != null) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_GRANT, "invalid code/redirect_uri/client_id/code_verifier.");
			}
			
			AccessToken accessToken = service.getAccessTokenStore().createAccessToken(client, code.getRequest().getUser().getOid(), code.getRequest().getScopes());
			
			IdToken idToken = null;
			if (code.getRequest().getScopes().contains(OAuthConstants.SCOPE_OPENID)) {
				idToken = new IdToken(code, accessToken, this, client, service);
			}
			
			return new OAuthTokens(accessToken, idToken);
		}
		
		public AccessToken refreshToken(String refreshTokenStr, OAuthClientRuntime client) {
			if (!getId().equals(client.getMetaData().getAuthorizationServerId())) {
				throw new OAuthRuntimeException("client's authServer is unmatch");
			}
			
			if (client.getMetaData().getGrantTypes() == null
					|| !client.getMetaData().getGrantTypes().contains(GrantType.REFRESH_TOKEN)) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_UNAUTHORIZED_CLIENT, "grant_type not allowed.");
			}
			
			ClientPolicyRuntime clientPolicy = clientPolicyRuntimeMap.get(client.getMetaData().getClientType());
			if (!clientPolicy.getMetaData().isSupportRefreshToken()) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_UNAUTHORIZED_CLIENT, "grant_type not allowed.");
			}

			RefreshToken refreshToken = service.getAccessTokenStore().getRefreshToken(refreshTokenStr);
			if (refreshToken == null) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_GRANT, "invalid refresh_token.");
			}
			
			if (refreshToken.getExpiresIn() <= 0) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_GRANT, "invalid refresh_token.");
			}
			if (!refreshToken.getClientId().equals(client.getMetaData().getName())) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_GRANT, "invalid refresh_token.");
			}
			
			AccessToken accessToken = service.getAccessTokenStore().createAccessToken(client, refreshToken);
			if (accessToken == null) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_INVALID_GRANT, "invalid refresh_token.");
			}
			return accessToken;
		}

		public void revoke(String tokenStr, String tokenTypeHint, OAuthClientRuntime client) {
			if (!getId().equals(client.getMetaData().getAuthorizationServerId())) {
				throw new OAuthRuntimeException("client's authServer is unmatch");
			}
			
			service.getAccessTokenStore().revokeToken(client, tokenStr, tokenTypeHint);
		}

		public String issuerId(RequestContext request) {
			if (issuerUri != null) {
				return issuerUri;
			}
			
			HttpServletRequest httpReq = (HttpServletRequest) request.getAttribute(WebRequestConstants.SERVLET_REQUEST);
			StringBuilder sb = new StringBuilder();
			if (httpReq.isSecure()) {
				sb.append("https://");
			} else {
				//for develop only
				sb.append("http://");
			}
			sb.append(httpReq.getServerName());
			
			int port = httpReq.getServerPort();
			if ((httpReq.isSecure() && port != 443)
					|| (!httpReq.isSecure() && port != 80)) {
				sb.append(':').append(port);
			}
			sb.append(TemplateUtil.getTenantContextPath());
			sb.append("/oauth");
			if (!OAuthAuthorizationService.DEFAULT_NAME.equals(getName())) {
				sb.append("/");
				sb.append(getName());
			}
			
			return sb.toString();
		}

		public Map<String, Object> userInfo(AccessToken accessToken, OAuthClientRuntime client) {
			if (!getId().equals(client.getMetaData().getAuthorizationServerId())) {
				throw new OAuthRuntimeException("client's authServer is unmatch");
			}
			
			Map<String, Object> userInfoClaims = new HashMap<>();
			for (String s: accessToken.getGrantedScopes()) {
				OIDCClaimScopeRuntime csr = getOIDCClaimScope(s);
				if (csr != null) {
					csr.map(accessToken.getUser(), userInfoClaims);
				}
			}
			String sub = getSubjectIdentifierType().subjectId(accessToken.getUser(), client);
			userInfoClaims.put("sub", sub);
			return userInfoClaims;
		}
		
	}

}
