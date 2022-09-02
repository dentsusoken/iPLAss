/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.oidc;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oidc.AutoUserProvisioningHandler;
import org.iplass.mtp.auth.oidc.definition.ClientAuthenticationType;
import org.iplass.mtp.auth.oidc.definition.OpenIdConnectDefinition;
import org.iplass.mtp.auth.oidc.definition.ResponseMode;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.oidc.jwks.Jwks;
import org.iplass.mtp.impl.auth.authenticate.oidc.jwks.LocalJwks;
import org.iplass.mtp.impl.auth.authenticate.oidc.jwks.RemoteJwks;
import org.iplass.mtp.impl.auth.oauth.jwt.InvalidJwtException;
import org.iplass.mtp.impl.auth.oauth.jwt.Jwt;
import org.iplass.mtp.impl.auth.oauth.jwt.JwtProcessor;
import org.iplass.mtp.impl.auth.oauth.util.IdTokenConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthEndpointConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthUtil;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.definition.DefinableMetaData;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.metadata.BaseMetaDataRuntime;
import org.iplass.mtp.impl.metadata.BaseRootMetaData;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataRuntime;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptEngine;
import org.iplass.mtp.impl.script.ScriptRuntimeException;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.util.random.SecureRandomGenerator;
import org.iplass.mtp.impl.util.random.SecureRandomService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.template.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

public class MetaOpenIdConnect extends BaseRootMetaData implements DefinableMetaData<OpenIdConnectDefinition> {
	private static final long serialVersionUID = -4429152263057997180L;

	private static class RandomHolder {
		static final SecureRandomGenerator randomForState = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator("stateTokenGenerator");
		static final SecureRandomGenerator randomForNonce = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator("nonceGenerator");
		static final SecureRandomGenerator randomForCodeVerifier = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator("codeVerifierGenerator");
	}
	private static Logger logger = LoggerFactory.getLogger(MetaOpenIdConnect.class);

	private String issuer;
	private String authorizationEndpoint;
	private String tokenEndpoint;
	private String userInfoEndpoint;
	private String jwksEndpoint;
	private String jwksContents;
	private String clientId;
	private List<String> scopes;//defaultでopenidは入れる（入ってなくても自動で）
	private ClientAuthenticationType clientAuthenticationType;
	private boolean useNonce = true;
	private boolean enablePKCE = true;//methodはS256固定
	private boolean issParameterSupported = true;
	private boolean validateSign;
	private ResponseMode responseMode = ResponseMode.FORM_POST;
	private String subjectNameClaim ="preferred_username";
	private String autoUserProvisioningHandler;
	private boolean enableTransientUser;
	
	private String backUrlAfterAuth;
	private String backUrlAfterConnect;
	
	//TODO 単なるOAuthClient（OpenIdConnectをサポートしない）としても利用可能に
	//private boolean disableOpenIdConnectFuture;
	
	public String getBackUrlAfterAuth() {
		return backUrlAfterAuth;
	}

	public void setBackUrlAfterAuth(String backUrlAfterAuth) {
		this.backUrlAfterAuth = backUrlAfterAuth;
	}

	public String getBackUrlAfterConnect() {
		return backUrlAfterConnect;
	}

	public void setBackUrlAfterConnect(String backUrlAfterConnect) {
		this.backUrlAfterConnect = backUrlAfterConnect;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}

	public void setAuthorizationEndpoint(String authorizationEndpoint) {
		this.authorizationEndpoint = authorizationEndpoint;
	}

	public String getTokenEndpoint() {
		return tokenEndpoint;
	}

	public void setTokenEndpoint(String tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}

	public String getUserInfoEndpoint() {
		return userInfoEndpoint;
	}

	public void setUserInfoEndpoint(String userInfoEndpoint) {
		this.userInfoEndpoint = userInfoEndpoint;
	}

	public String getJwksEndpoint() {
		return jwksEndpoint;
	}

	public void setJwksEndpoint(String jwksEndpoint) {
		this.jwksEndpoint = jwksEndpoint;
	}

	public String getJwksContents() {
		return jwksContents;
	}

	public void setJwksContents(String jwksContents) {
		this.jwksContents = jwksContents;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public List<String> getScopes() {
		return scopes;
	}

	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}

	public ClientAuthenticationType getClientAuthenticationType() {
		return clientAuthenticationType;
	}

	public void setClientAuthenticationType(ClientAuthenticationType clientAuthenticationType) {
		this.clientAuthenticationType = clientAuthenticationType;
	}

	public boolean isUseNonce() {
		return useNonce;
	}

	public void setUseNonce(boolean useNonce) {
		this.useNonce = useNonce;
	}

	public boolean isEnablePKCE() {
		return enablePKCE;
	}

	public void setEnablePKCE(boolean enablePKCE) {
		this.enablePKCE = enablePKCE;
	}

	public boolean isIssParameterSupported() {
		return issParameterSupported;
	}

	public void setIssParameterSupported(boolean issParameterSupported) {
		this.issParameterSupported = issParameterSupported;
	}

	public boolean isValidateSign() {
		return validateSign;
	}

	public void setValidateSign(boolean validateSign) {
		this.validateSign = validateSign;
	}

	public ResponseMode getResponseMode() {
		return responseMode;
	}

	public void setResponseMode(ResponseMode responseMode) {
		this.responseMode = responseMode;
	}

	public String getSubjectNameClaim() {
		return subjectNameClaim;
	}

	public void setSubjectNameClaim(String subjectNameClaim) {
		this.subjectNameClaim = subjectNameClaim;
	}

	public String getAutoUserProvisioningHandler() {
		return autoUserProvisioningHandler;
	}

	public void setAutoUserProvisioningHandler(String autoUserProvisioningHandler) {
		this.autoUserProvisioningHandler = autoUserProvisioningHandler;
	}

	public boolean isEnableTransientUser() {
		return enableTransientUser;
	}

	public void setEnableTransientUser(boolean enableTransientUser) {
		this.enableTransientUser = enableTransientUser;
	}

	@Override
	public MetaDataRuntime createRuntime(MetaDataConfig metaDataConfig) {
		return new OpenIdConnectRuntime();
	}

	@Override
	public MetaOpenIdConnect copy() {
		return  ObjectUtil.deepCopy(this);
	}

	@Override
	public void applyConfig(OpenIdConnectDefinition def) {
		name = def.getName();
		description = def.getDescription();
		displayName = def.getDisplayName();
		localizedDisplayNameList = I18nUtil.toMeta(def.getLocalizedDisplayNameList());
		issuer = def.getIssuer();
		authorizationEndpoint = def.getAuthorizationEndpoint();
		tokenEndpoint = def.getTokenEndpoint();
		userInfoEndpoint = def.getUserInfoEndpoint();
		jwksEndpoint = def.getJwksEndpoint();
		jwksContents = def.getJwksContents();
		clientId = def.getClientId();
		scopes = (def.getScopes() == null) ? null: new ArrayList<String>(def.getScopes());
		clientAuthenticationType = def.getClientAuthenticationType();
		useNonce = def.isUseNonce();
		enablePKCE = def.isEnablePKCE();
		issParameterSupported = def.isIssParameterSupported();
		validateSign = def.isValidateSign();
		responseMode = def.getResponseMode();
		subjectNameClaim = def.getSubjectNameClaim();
		autoUserProvisioningHandler = def.getAutoUserProvisioningHandler();
		enableTransientUser = def.isEnableTransientUser();
		backUrlAfterAuth = def.getBackUrlAfterAuth();
		backUrlAfterConnect = def.getBackUrlAfterConnect();
	}
	@Override
	public OpenIdConnectDefinition currentConfig() {
		OpenIdConnectDefinition def = new OpenIdConnectDefinition();
		def.setName(name);
		def.setDescription(description);
		def.setDisplayName(displayName);
		def.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
		def.setIssuer(issuer);
		def.setAuthorizationEndpoint(authorizationEndpoint);
		def.setTokenEndpoint(tokenEndpoint);
		def.setUserInfoEndpoint(userInfoEndpoint);
		def.setJwksEndpoint(jwksEndpoint);
		def.setJwksContents(jwksContents);
		def.setClientId(clientId);
		if (scopes != null) {
			def.setScopes(new ArrayList<String>(scopes));
		}
		def.setClientAuthenticationType(clientAuthenticationType);
		def.setUseNonce(useNonce);
		def.setEnablePKCE(enablePKCE);
		def.setIssParameterSupported(issParameterSupported);
		def.setValidateSign(validateSign);
		def.setResponseMode(responseMode);
		def.setSubjectNameClaim(subjectNameClaim);
		def.setAutoUserProvisioningHandler(autoUserProvisioningHandler);
		def.setEnableTransientUser(enableTransientUser);
		def.setBackUrlAfterAuth(backUrlAfterAuth);
		def.setBackUrlAfterConnect(backUrlAfterConnect);
		
		return def;
	}
	
	public class OpenIdConnectRuntime extends BaseMetaDataRuntime {
		
		private AutoUserProvisioningHandler aup;
		private String scopeParamValue;
		private HashSet<String> scopeParamSet;
		private OpenIdConnectService opService;
		private EntityManager em;
		private String clientSecret;
		
		private Jwks jwks;
		private OPEndpoint opEndpoint;
		
		private ScriptEngine scriptEngine = ExecuteContext.getCurrentContext().getTenantContext().getScriptEngine();
		private GroovyTemplate backUrlAfterAuthTmpl;
		private GroovyTemplate backUrlAfterConnectTmpl;
		
		private OpenIdConnectRuntime() {
			if (autoUserProvisioningHandler != null) {
				try {
					aup = ManagerLocator.getInstance().getManager(UtilityClassDefinitionManager.class).createInstanceAs(AutoUserProvisioningHandler.class, autoUserProvisioningHandler);
					aup.init(currentConfig());
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
			
			scopeParamSet = new HashSet<>();
			StringBuilder sb = new StringBuilder();
			sb.append(OAuthConstants.SCOPE_OPENID);
			scopeParamSet.add(OAuthConstants.SCOPE_OPENID);
			if (scopes != null  && scopes.size() > 0) {
				for (String sc: scopes) {
					if (!OAuthConstants.SCOPE_OPENID.equals(sc)) {
						sb.append(' ').append(sc);
						scopeParamSet.add(sc);
					}
				}
			}
			scopeParamValue = sb.toString();
			opService = ServiceRegistry.getRegistry().getService(OpenIdConnectService.class);
			em = ManagerLocator.manager(EntityManager.class);
			clientSecret = opService.getClientSecret(getId());
//			if (clientSecret == null) {
//				setIllegalStateException(new IllegalStateException("Client Secret unspecified."));
//			}
			if (jwksContents != null && !jwksContents.isEmpty()) {
				jwks = new LocalJwks(jwksContents, opService);
			} else if (jwksEndpoint != null) {
				jwks = new RemoteJwks(jwksEndpoint, opService);
			} else {
//				setIllegalStateException(new IllegalStateException("jwks endpoint or contents must specified"));
			}
			opEndpoint = new OPEndpoint(tokenEndpoint, userInfoEndpoint, opService);
			
//			if (issuer == null) {
//				setIllegalStateException(new NullPointerException("issuer must specified"));
//			}
//			if (authorizationEndpoint == null) {
//				setIllegalStateException(new NullPointerException("authorizationEndpoint must specified"));
//			}
//			if (tokenEndpoint == null) {
//				setIllegalStateException(new NullPointerException("tokenEndpoint must specified"));
//			}
//			if (clientId == null) {
//				setIllegalStateException(new NullPointerException("clientId must specified"));
//			}
//			if (clientAuthenticationType == null) {
//				setIllegalStateException(new NullPointerException("clientAuthenticationType must specified"));
//			}
//			if (subjectNameClaim == null) {
//				setIllegalStateException(new NullPointerException("subjectNameClaim must specified"));
//			}
			
			try {
				if (backUrlAfterAuth != null) {
					backUrlAfterAuthTmpl = GroovyTemplateCompiler.compile(
							backUrlAfterAuth,
							"OpenIdConnect_backUrlAfterAuth_" + getName(), (GroovyScriptEngine) scriptEngine);
				}
				if (backUrlAfterConnect != null) {
					backUrlAfterConnectTmpl = GroovyTemplateCompiler.compile(
							backUrlAfterConnect,
							"OpenIdConnect_backUrlAfterConnect_" + getName(), (GroovyScriptEngine) scriptEngine);
				}
			} catch (RuntimeException e) {
				setIllegalStateException(e);
			}
		}
		
		//for test
		void setOPEndpoint(OPEndpoint opEndpoint) {
			this.opEndpoint = opEndpoint;
		}
		OPEndpoint getOPEndpoint() {
			return opEndpoint;
		}
		
		@Override
		public MetaOpenIdConnect getMetaData() {
			return MetaOpenIdConnect.this;
		}
		
		public AutoUserProvisioningHandler getAutoUserProvisioningHandler() {
			return aup;
		}
		
		public String backUrlAfterAuth(RequestContext req) {
			return doTmpl(backUrlAfterAuthTmpl, req);
		}
		
		private String doTmpl(GroovyTemplate tmpl, RequestContext req) {
			if (tmpl == null) {
				return null;
			}
			Map<String, Object> binding = new HashMap<String, Object>();
			binding.put("request", req);
			
			StringWriter sw = new StringWriter();
			try {
				tmpl.doTemplate(new GroovyTemplateBinding(sw, binding));
			} catch (IOException e) {
				throw new ScriptRuntimeException(e);
			}
			return sw.toString();
		}
		
		public String backUrlAfterConnect(RequestContext req) {
			return doTmpl(backUrlAfterConnectTmpl, req);
		}

		public String createRedirectUri(RequestContext req, String actionName) {
			HttpServletRequest httpReq = (HttpServletRequest) req.getAttribute(WebRequestConstants.SERVLET_REQUEST);
			StringBuilder sb = new StringBuilder();
			if (httpReq.isSecure()) {
				sb.append("https://");
			} else {
				sb.append("http://");
			}
			sb.append(httpReq.getServerName());

			int port = httpReq.getServerPort();
			if ((httpReq.isSecure() && port != 443)
					|| (!httpReq.isSecure() && port != 80)) {
				sb.append(':').append(port);
			}
			sb.append(TemplateUtil.getTenantContextPath());
			sb.append("/").append(actionName);
			if (!OpenIdConnectService.DEFAULT_NAME.equals(getName())) {
				sb.append("/").append(getName());
			}
			
			return sb.toString();
		}

		public OIDCState newOIDCState(String backUrlAfterAuth, String redirectUri) {
			checkState();
			
			OIDCState state = new OIDCState();
			state.setToken(RandomHolder.randomForState.secureRandomToken());
			state.setBackUrlAfterAuth(backUrlAfterAuth);
			state.setIssuer(issuer);
			state.setRedirectUri(redirectUri);
			if (useNonce) {
				state.setNonce(RandomHolder.randomForNonce.secureRandomToken());
			}
			if (enablePKCE) {
				state.setCodeVerifier(RandomHolder.randomForCodeVerifier.secureRandomToken());
			}
			
			return state;
		}

		private boolean validateState(OIDCState state, String stateToken, String iss, String redirectUri) {
			if (state == null || stateToken == null) {
				return false;
			}
			if (!state.getRedirectUri().equals(redirectUri)) {
				if (logger.isDebugEnabled()) {
					logger.debug("redirectUri unmatch:expected=" + state.getRedirectUri() + ", actual=" + redirectUri);
				}
				return false;
			}
			if (!stateToken.equals(state.getToken())) {
				//invalid state token
				if (logger.isDebugEnabled()) {
					logger.debug("state unmatch:expected=" + state.getToken() + ", actual=" + stateToken);
				}
				return false;
			}
			if (issParameterSupported) {
				if (!state.getIssuer().equals(iss)) {
					//invalid issuer
					if (logger.isDebugEnabled()) {
						logger.debug("issuer unmatch:expected=" + state.getIssuer() + ", actual=" + iss);
					}
					return false;
				}
			}
			return true;
		}
		
		@SuppressWarnings("unchecked")
		public OIDCValidateResult validate(OIDCCredential credential) {
			checkState();
			
			if (!validateState(credential.getState(), credential.getStateToken(), credential.getIss(), credential.getRedirectUri())) {
				return new OIDCValidateResult("invalid_state", "Invalid client state.", null, null);
			}
			
			Map<String, Object> ret = opEndpoint.token(clientAuthenticationType, clientId, clientSecret,
					credential.getCode(), credential.getRedirectUri(), credential.getState().getCodeVerifier());

			String idTokenJwt = null;
			String tokenType = null;
			String accessToken = null;
			Long expiresIn = null;
			String refreshToken = null;
			String error = null;
			String errorDescription = null;
			String errorUri = null;
			String scope = null;
			Jwt idToken = null;
			Set<String> scopeSet = null;

			error = (String) ret.get(OAuthEndpointConstants.PARAM_ERROR);
			if (error != null) {
				errorDescription = (String) ret.get(OAuthEndpointConstants.PARAM_ERROR_DESCRIPTION);
				errorUri = (String) ret.get(OAuthEndpointConstants.PARAM_ERROR_URI);
				return new OIDCValidateResult(error, errorDescription, errorUri, null);
			}
			
			idTokenJwt = (String) ret.get(OAuthEndpointConstants.PARAM_ID_TOKEN);
			tokenType = (String) ret.get(OAuthEndpointConstants.PARAM_TOKEN_TYPE);
			accessToken = (String) ret.get(OAuthEndpointConstants.PARAM_ACCESS_TOKEN);
			Number expiresInNum = (Number) ret.get(OAuthEndpointConstants.PARAM_EXPIRES_IN);
			if (expiresInNum != null) {
				expiresIn = expiresInNum.longValue();
			}
			refreshToken = (String) ret.get(OAuthEndpointConstants.PARAM_REFRESH_TOKEN);
			scope = (String) ret.get(OAuthEndpointConstants.PARAM_SCOPE);
			
			if (tokenType == null || accessToken == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("invalid token response:" + ret);
				}
				return new OIDCValidateResult("invalid_token_response", "Invalid Token Response.token_type and access_token required.", null, null);
				
			}
			
			//The OAuth 2.0 token_type response parameter value MUST be Bearer
			if (!OAuthConstants.TOKEN_TYPE_BEARER.equalsIgnoreCase(tokenType)) {
				if (logger.isDebugEnabled()) {
					logger.debug("received token type is unknown:" + tokenType);
				}
				return new OIDCValidateResult("unknown_token_type", "The token type is unknown.", null, null);
			}
			
			//validate scope exactly match.
			scopeSet = new HashSet<>();
			if (scope != null) {
				scopeSet.addAll(Arrays.asList(scope.split(" ")));
				if (!scopeParamSet.equals(scopeSet)) {
					return new OIDCValidateResult("scope_not_granted", "The requested scope is not granted.", null, null);
				}
			} else {
				scopeSet = (Set<String>) scopeParamSet.clone();
			}
			
			try {
				idToken = decodeIdToken(idTokenJwt);
				if (logger.isDebugEnabled()) {
					logger.debug("received id token:hader=" + idToken.getHeader() + ", payload=" + idToken.getClaims());
				}
				validateIdToken(idToken, accessToken, credential);
			} catch (RuntimeException e) {
				return new OIDCValidateResult("invalid_id_token", "Invalid IdToken.", null, e);
			}
				
			Map<String, Object> claims = new HashMap<String, Object>(idToken.getClaims());
			if (userInfoEndpoint != null) {
				Map<String, Object> userInfo = opEndpoint.userInfo(tokenType, accessToken);
				if (userInfo != null) {
					claims.putAll(userInfo);
				}
			}
			
			return new OIDCValidateResult((String) claims.get(IdTokenConstants.CLAIM_SUB), (String) claims.get(subjectNameClaim), claims, tokenType, accessToken, expiresIn, refreshToken, scopeSet);
		}
		
		private Jwt decodeIdToken(String idTokenJwt) {
			if (validateSign) {
				JwtProcessor jwtp = JwtProcessor.getInstance();
				return jwtp.decode(idTokenJwt, opService.getAllowedClockSkewMinutes(), kid -> jwks.get(kid));
			} else {
				//from OIDC Spec
				//3.1.3.7.  ID Token Validation
				//6 If the ID Token is received via direct communication between the Client and the Token Endpoint (which it is in this flow),
				//the TLS server validation MAY be used to validate the issuer in place of checking the token signature.
				int index1 = idTokenJwt.indexOf('.');
				int index2 = idTokenJwt.indexOf('.', index1 + 1);
				if (index1 == index2 || index2 != idTokenJwt.lastIndexOf('.')) {
					throw new InvalidJwtException("invalid JWT format");
				}
				Map<String, Object> header = null;
				Map<String, Object> claims = null;
				try {
					String headerPart = new String(Base64.getUrlDecoder().decode(idTokenJwt.substring(0, index1)), "UTF-8");
					String payload = new String(Base64.getUrlDecoder().decode(idTokenJwt.substring(index1 + 1, index2)), "UTF-8");
					header = opService.getObjectMapper().readValue(headerPart, new TypeReference<Map<String, Object>>() {});
					claims = opService.getObjectMapper().readValue(payload, new TypeReference<Map<String, Object>>() {});
					
					//有効期限チェック
					long now = System.currentTimeMillis();
					Number exp = (Number) claims.get(IdTokenConstants.CLAIM_EXP);
					if (exp != null) {
						if (now >= (TimeUnit.SECONDS.toMillis(exp.longValue()) + TimeUnit.MINUTES.toMillis(opService.getAllowedClockSkewMinutes()))) {
							throw new InvalidJwtException("JWT expired");
						}
					}
					
					return new Jwt(header, claims);
				} catch (UnsupportedEncodingException | JsonProcessingException e) {
					throw new InvalidJwtException(e);
				}
			}
		}
		
		@SuppressWarnings("unchecked")
		private void validateIdToken(Jwt idToken, String accessToken, OIDCCredential cre) {
			//iss
			String iss = (String) idToken.getClaims().get(IdTokenConstants.CLAIM_ISS);
			if (iss == null) {
				throw new InvalidJwtException("iss required");
			}
			if (!iss.equals(cre.getState().getIssuer())) {
				throw new InvalidJwtException("iss unmatch");
			}
			
			//sub
			String sub = (String) idToken.getClaims().get(IdTokenConstants.CLAIM_SUB);
			if (sub == null) {
				throw new InvalidJwtException("sub required");
			}
			
			//aud
			Object aud = idToken.getClaims().get(IdTokenConstants.CLAIM_AUD);
			if (aud == null) {
				throw new InvalidJwtException("aud required");
			}
			if (aud instanceof String) {
				if (!((String) aud).equals(clientId)) {
					throw new InvalidJwtException("aud unmatch");
				}
			} else {
				if (!((List<String>) aud).contains(clientId)) {
					throw new InvalidJwtException("aud unmatch");
				}
			}
			
			//exp
			//有効期限チェックは事前に実施済み想定（ここで記載したかったが、jwtライブラリ側でデフォルトで組み込まれてるので）
			Number exp = (Number) idToken.getClaims().get(IdTokenConstants.CLAIM_EXP);
			if (exp == null) {
				throw new InvalidJwtException("exp required");
			}
			
			//iat
			//The iat Claim can be used to reject tokens that were issued too far away from the current time, limiting the amount of time that nonces need to be stored to prevent attacks. The acceptable range is Client specific.
			Number iat = (Number) idToken.getClaims().get(IdTokenConstants.CLAIM_IAT);
			if (iat == null) {
				throw new InvalidJwtException("iat required");
			}
			if (cre.getState().getCreateTime() > TimeUnit.SECONDS.toMillis(iat.longValue()) + TimeUnit.MINUTES.toMillis(opService.getAllowedClockSkewMinutes())) {
				//authorization codeフロー開始時刻以前のIdTokenは拒否
				throw new InvalidJwtException("invalid iat");
			}
			
			//nonce
			if (cre.getState().getNonce() != null) {
				String nonce = (String) idToken.getClaims().get(IdTokenConstants.CLAIM_NONCE);
				if (nonce == null) {
					throw new InvalidJwtException("nonce required");
				}
				if (!nonce.equals(cre.getState().getNonce())) {
					throw new InvalidJwtException("invalid nonce");
				}
			}
			
			//azp
			//If the ID Token contains multiple audiences, the Client SHOULD verify that an azp Claim is present.
			//If an azp (authorized party) Claim is present, the Client SHOULD verify that its client_id is the Claim Value.
			String azp = (String) idToken.getClaims().get(IdTokenConstants.CLAIM_AZP);
			if (aud instanceof List) {
				if (azp == null) {
					throw new InvalidJwtException("azp required");
				}
			}
			if (azp != null) {
				if (!azp.equals(clientId)) {
					throw new InvalidJwtException("invalid azp");
				}
			}
			
			//at_hash
			String atHash = (String) idToken.getClaims().get(IdTokenConstants.CLAIM_AT_HASH);
			if (atHash != null) {
				if (!atHash.equals(OAuthUtil.atHash(accessToken, (String) idToken.getHeader().get(IdTokenConstants.HEAER_ALG)))) {
					throw new InvalidJwtException("invalid at_hash");
				}
			}
			
			//c_hash
			String cHash = (String) idToken.getClaims().get(IdTokenConstants.CLAIM_C_HASH);
			if (cHash != null) {
				if (!cHash.equals(OAuthUtil.cHash(cre.getCode(), (String) idToken.getHeader().get(IdTokenConstants.HEAER_ALG)))) {
					throw new InvalidJwtException("invalid c_hash");
				}
			}
		}

		public String authorizeUrl(OIDCState state) {
			
			StringBuilder url = new StringBuilder();
			url.append(authorizationEndpoint);
			if (authorizationEndpoint.indexOf('?') > -1) {
				if (authorizationEndpoint.charAt(authorizationEndpoint.length() - 1) != '?') {
					url.append("&");
				}
			} else {
				url.append("?");
			}
			
			url.append(OAuthEndpointConstants.PARAM_CLIENT_ID).append("=").append(OAuthUtil.encodeRfc3986(clientId));
			url.append("&");
			//TODO ハイブリッドフロー（code id_token）のサポート（issが利用できない場合のmix-up攻撃対策として）
			url.append(OAuthEndpointConstants.PARAM_RESPONSE_TYPE).append("=").append(OAuthConstants.RESPONSE_TYPE_CODE);
			url.append("&");
			url.append(OAuthEndpointConstants.PARAM_SCOPE).append("=").append(OAuthUtil.encodeRfc3986(scopeParamValue));
			url.append("&");
			url.append(OAuthEndpointConstants.PARAM_REDIRECT_URI).append("=").append(OAuthUtil.encodeRfc3986(state.getRedirectUri()));
			url.append("&");
			url.append(OAuthEndpointConstants.PARAM_STATE).append("=").append(OAuthUtil.encodeRfc3986(state.getToken()));
			if (responseMode != null) {
				url.append("&");
				String rmStr;
				if (responseMode == ResponseMode.FORM_POST) {
					rmStr = OAuthConstants.RESPONSE_MODE_FORM_POST;
				} else if (responseMode == ResponseMode.QUERY) {
					rmStr = OAuthConstants.RESPONSE_MODE_QUERY;
				} else {
					throw new IllegalArgumentException();
				}
				url.append(OAuthEndpointConstants.PARAM_RESPONSE_MODE).append("=").append(OAuthUtil.encodeRfc3986(rmStr));
			}
			if (useNonce) {
				url.append("&");
				url.append(OAuthEndpointConstants.PARAM_NONCE).append("=").append(OAuthUtil.encodeRfc3986(state.getNonce()));
			}
			if (enablePKCE) {
				url.append("&");
				url.append(OAuthEndpointConstants.PARAM_CODE_CHALLENGE_METHOD).append("=").append(OAuthConstants.CODE_CHALLENGE_METHOD_S256);
				url.append("&");
				url.append(OAuthEndpointConstants.PARAM_CODE_CHALLENGE).append("=").append(OAuthUtil.calcCodeChallenge(OAuthConstants.CODE_CHALLENGE_METHOD_S256, state.getCodeVerifier()));
			}
			
			return url.toString();
		}

		public void connect(String userOid, OIDCValidateResult vr) {
			GenericEntity e = new GenericEntity(OpenIdProviderAccountEntityEventListener.DEFINITION_NAME);
			e.setValue(OpenIdProviderAccountEntityEventListener.OIDC_DEFINITION_NAME, getName());
			e.setValue(OpenIdProviderAccountEntityEventListener.SUBJECT_ID, vr.getSubjectId());
			e.setValue(OpenIdProviderAccountEntityEventListener.SUBJECT_NAME, vr.getSubjectName());
			e.setValue(OpenIdProviderAccountEntityEventListener.USER, new User(userOid, null, false));
			
			AuthContext.doPrivileged(() -> em.insert(e));
		}

		public void disconnect(String userOid) {
			AuthContext.doPrivileged(() -> {
				Query q = new Query().select(Entity.OID).from(OpenIdProviderAccountEntityEventListener.DEFINITION_NAME)
						.where(new And(new Equals(OpenIdProviderAccountEntityEventListener.USER_OID, userOid),
								new Equals(OpenIdProviderAccountEntityEventListener.OIDC_DEFINITION_NAME, getName())));
				Entity e = em.searchEntity(q).getFirst();
				if (e != null) {
					em.delete(e, new DeleteOption());
				}
			});
		}

		public boolean isAllowedOnPolicy(AuthenticationPolicyRuntime userPolicy) {
			if (userPolicy.getMetaData().getOpenIdConnectDefinition() != null) {
				for (String oidcName: userPolicy.getMetaData().getOpenIdConnectDefinition()) {
					if (getMetaData().getName().equals(oidcName)) {
						return true;
					}
					int ai = oidcName.indexOf('*');
					if (ai >= 0) {
						//check wildcard
						String path = oidcName.substring(0, ai);
						if (getMetaData().getName().startsWith(path)) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}

}
