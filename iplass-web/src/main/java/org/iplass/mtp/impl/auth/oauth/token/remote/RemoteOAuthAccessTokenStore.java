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
package org.iplass.mtp.impl.auth.oauth.token.remote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.authenticate.builtin.web.BasicAuthUtil;
import org.iplass.mtp.impl.auth.oauth.AccessTokenAccountHandle;
import org.iplass.mtp.impl.auth.oauth.ClientAuthenticationMethod;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.OAuthRuntimeException;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.auth.oauth.token.OAuthAccessTokenStore;
import org.iplass.mtp.impl.auth.oauth.token.RefreshToken;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.http.ExponentialBackoff;
import org.iplass.mtp.impl.http.HttpClientConfig;
import org.iplass.mtp.impl.http.SimpleHttpInvoker;
import org.iplass.mtp.impl.http.SimpleHttpInvoker.Response;
import org.iplass.mtp.impl.webapi.jackson.WebApiObjectMapperService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * バックエンドでOAuth 2.0 Token Introspection (RFC7662)を呼び出すTokenStore。
 * AuthorizationServerとは別インスタンスのResourceServerでの利用想定。
 * 読み取り専用。
 * 
 * @author K.Higuchi
 *
 */
public class RemoteOAuthAccessTokenStore implements OAuthAccessTokenStore, ServiceInitListener<OAuthAuthorizationService> {
	
	private static Logger logger = LoggerFactory.getLogger(RemoteOAuthAccessTokenStore.class);
	
	private static final String TENANT_NAME_VARIABLE = "${tenantName}";
	
	private TenantValidationType tenantValidationType = TenantValidationType.NAME;
	private String introspectionEndpointUrl;
	private ClientAuthenticationMethod authenticationMethod = ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
	private String resourceServerId;
	private String resourceServerSecret;
	private HttpClientConfig httpClientConfig;
	private ExponentialBackoff exponentialBackoff;
	private boolean reloadUser;
	
	private WebApiObjectMapperService objectMapperService;
	private SimpleHttpInvoker httpInvoker;
	
	public ClientAuthenticationMethod getAuthenticationMethod() {
		return authenticationMethod;
	}

	public void setAuthenticationMethod(ClientAuthenticationMethod authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}

	public HttpClientConfig getHttpClientConfig() {
		return httpClientConfig;
	}

	public void setHttpClientConfig(HttpClientConfig httpClientConfig) {
		this.httpClientConfig = httpClientConfig;
	}

	public ExponentialBackoff getExponentialBackoff() {
		return exponentialBackoff;
	}

	public void setExponentialBackoff(ExponentialBackoff exponentialBackoff) {
		this.exponentialBackoff = exponentialBackoff;
	}

	public TenantValidationType getTenantValidationType() {
		return tenantValidationType;
	}

	public void setTenantValidationType(TenantValidationType tenantValidationType) {
		this.tenantValidationType = tenantValidationType;
	}

	public String getIntrospectionEndpointUrl() {
		return introspectionEndpointUrl;
	}

	public void setIntrospectionEndpointUrl(String introspectionEndpointUrl) {
		this.introspectionEndpointUrl = introspectionEndpointUrl;
	}

	public String getResourceServerId() {
		return resourceServerId;
	}

	public void setResourceServerId(String resourceServerId) {
		this.resourceServerId = resourceServerId;
	}

	public String getResourceServerSecret() {
		return resourceServerSecret;
	}

	public void setResourceServerSecret(String resourceServerSecret) {
		this.resourceServerSecret = resourceServerSecret;
	}

	public boolean isReloadUser() {
		return reloadUser;
	}

	public void setReloadUser(boolean reloadUser) {
		this.reloadUser = reloadUser;
	}

	@Override
	public AccessToken getAccessToken(String tokenString) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		String endpoint = introspectionEndpointUrl.replace(TENANT_NAME_VARIABLE, ec.getCurrentTenant().getName());
		HttpPost post = new HttpPost(endpoint);
		List<NameValuePair> params = new ArrayList<>();
		
		post.setHeader("Accept", "application/json");
		if (authenticationMethod == ClientAuthenticationMethod.CLIENT_SECRET_BASIC) {
			IdPasswordCredential idPass = new IdPasswordCredential(resourceServerId, resourceServerSecret);
			post.setHeader(BasicAuthUtil.HEADER_AUTHORIZATION, BasicAuthUtil.encodeValue(idPass));
		} else if (authenticationMethod == ClientAuthenticationMethod.CLIENT_SECRET_POST) {
			params.add(new BasicNameValuePair("client_id", resourceServerId));
			params.add(new BasicNameValuePair("client_secret", resourceServerSecret));
		}
		
		params.add(new BasicNameValuePair("token", tokenString));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(params));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
		Response res = httpInvoker.call(post, r -> {
			if (r.status == 200 || r.status == 400 || r.status == 401) {
				return true;
			} else {
				return false;
			}
		});
		
		if (res.exception != null) {
			if (res.exception instanceof RuntimeException) {
				throw (RuntimeException) res.exception;
			} else {
				throw new OAuthRuntimeException(res.exception);
			}
		}
		if (res.status != 200) {
			throw new OAuthRuntimeException("Introspection Endpoint return error:" + res.status + " " + res.content);
		}
		
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("response: " + res.content);
			}
			IntroResponse resJson = objectMapperService.getObjectMapper().readValue(res.content, IntroResponse.class);
			
			if (!resJson.active) {
				if (logger.isDebugEnabled()) {
					logger.debug("token is not active: " + resJson);
				}
				return null;
			}
			
			if (tenantValidationType == TenantValidationType.NAME) {
				if (!ec.getCurrentTenant().getName().equals(resJson.tenant_name)) {
					if (logger.isDebugEnabled()) {
						logger.debug("mismatch tenant_name: " + resJson);
					}
					return null;
				}
			} else if (tenantValidationType == TenantValidationType.ID) {
				if (resJson.tenant_id == null || resJson.tenant_id.intValue() != ec.getClientTenantId()) {
					if (logger.isDebugEnabled()) {
						logger.debug("mismatch tenant_id: " + resJson);
					}
					return null;
				}
			}
			
			RemoteAccessToken accessToken = new RemoteAccessToken(tokenString, resJson);
			
			if (reloadUser) {
				String oid = accessToken.getSub();
				
				AccessTokenAccountHandle dummy = new AccessTokenAccountHandle(oid, accessToken, null);
				User user = UserEntityResolverHolder.userEntityResolver.searchUser(dummy);
				if (user == null) {
					return null;
				}
				accessToken.setUser(user);
			}
			
			return accessToken;
			
		} catch (IOException e) {
			throw new OAuthRuntimeException(res.exception);
		}
	}
	
	@Override
	public void inited(OAuthAuthorizationService service, Config config) {
		objectMapperService = ServiceRegistry.getRegistry().getService(WebApiObjectMapperService.class);
		
		if (httpClientConfig == null) {
			httpClientConfig = new HttpClientConfig();
		}
		httpClientConfig.inited(service, config);

		httpInvoker = new SimpleHttpInvoker(httpClientConfig.getInstance(), exponentialBackoff);
	}

	@Override
	public void destroyed() {
	}

	@Override
	public AccessToken createAccessToken(OAuthClientRuntime client, String resourceOwnerId, List<String> scopes) {
		throw new UnsupportedOperationException("RemoteOAuthAccessTokenStore cannot support AccessToken creation");
	}

	@Override
	public AccessToken createAccessToken(OAuthClientRuntime client, RefreshToken refreshToken) {
		throw new UnsupportedOperationException("RemoteOAuthAccessTokenStore cannot support AccessToken creation");
	}

	@Override
	public AccessToken getAccessTokenByUserOid(OAuthClientRuntime client, String userOid) {
		throw new UnsupportedOperationException("RemoteOAuthAccessTokenStore cannot support getAccessTokenByUserOid");
	}

	@Override
	public RefreshToken getRefreshToken(String tokenString) {
		throw new UnsupportedOperationException("RemoteOAuthAccessTokenStore cannot support getRefreshToken");
	}

	@Override
	public void revokeToken(OAuthClientRuntime client, String tokenString, String tokenTypeHint) {
		throw new UnsupportedOperationException("RemoteOAuthAccessTokenStore cannot support revokeToken");
	}

	@Override
	public void revokeTokenByUserOid(String userOid) {
		throw new UnsupportedOperationException("RemoteOAuthAccessTokenStore cannot support revokeToken");
	}

}
