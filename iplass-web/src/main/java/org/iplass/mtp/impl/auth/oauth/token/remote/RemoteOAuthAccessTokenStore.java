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
package org.iplass.mtp.impl.auth.oauth.token.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
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

	/**
	 * @return Introspection EndpointのClient（Resource Server）認証方式
	 */
	public ClientAuthenticationMethod getAuthenticationMethod() {
		return authenticationMethod;
	}

	/**
	 * @param authenticationMethod Introspection EndpointのClient（Resource Server）認証方式
	 */
	public void setAuthenticationMethod(ClientAuthenticationMethod authenticationMethod) {
		this.authenticationMethod = authenticationMethod;
	}

	/**
	 * @return HttpClientConfig
	 */
	public HttpClientConfig getHttpClientConfig() {
		return httpClientConfig;
	}

	/**
	 * @param httpClientConfig HttpClientConfig
	 */
	public void setHttpClientConfig(HttpClientConfig httpClientConfig) {
		this.httpClientConfig = httpClientConfig;
	}

	/**
	 * @return リトライ処理する際の 指数バックオフ
	 */
	public ExponentialBackoff getExponentialBackoff() {
		return exponentialBackoff;
	}

	/**
	 * @param exponentialBackoff リトライ処理する際の 指数バックオフ
	 */
	public void setExponentialBackoff(ExponentialBackoff exponentialBackoff) {
		this.exponentialBackoff = exponentialBackoff;
	}

	/**
	 * @return テナントの検証方式
	 */
	public TenantValidationType getTenantValidationType() {
		return tenantValidationType;
	}

	/**
	 * @param tenantValidationType テナントの検証方式
	 */
	public void setTenantValidationType(TenantValidationType tenantValidationType) {
		this.tenantValidationType = tenantValidationType;
	}

	/**
	 * @return Introspection EndpointのURL
	 */
	public String getIntrospectionEndpointUrl() {
		return introspectionEndpointUrl;
	}

	/**
	 * @param introspectionEndpointUrl Introspection EndpointのURL
	 */
	public void setIntrospectionEndpointUrl(String introspectionEndpointUrl) {
		this.introspectionEndpointUrl = introspectionEndpointUrl;
	}

	/**
	 * @return Authorization Serverから発行されるResource ServerのID
	 */
	public String getResourceServerId() {
		return resourceServerId;
	}

	/**
	 * @param resourceServerId Authorization Serverから発行されるResource ServerのID
	 */
	public void setResourceServerId(String resourceServerId) {
		this.resourceServerId = resourceServerId;
	}

	/**
	 * @return Authorization Serverから発行されるResource ServerのSecret
	 */
	public String getResourceServerSecret() {
		return resourceServerSecret;
	}

	/**
	 * @param resourceServerSecret Authorization Serverから発行されるResource ServerのSecret
	 */
	public void setResourceServerSecret(String resourceServerSecret) {
		this.resourceServerSecret = resourceServerSecret;
	}

	/**
	 * AccessTokenに紐づくユーザー情報の取得方法の制御
	 *
	 * <p>
	 * trueの場合<br>
	 * レスポンスの sub の値をoidとみなし、Resource Server上からUserエンティティを検索します。Userが存在しない場合、許可しません。
	 * </p>
	 *
	 * <p>
	 * falseの場合<br>
	 * レスポンスからユーザー情報を取得します。
	 * レスポンスに resource_owner でUserエンティティのJSON表現が格納されていた場合、それを利用します。
	 * resource_owner に格納されていなかった場合、 sub の値をoid、accountIdとし、 username の値をnameとしてユーザー情報とします。
	 * </p>
	 *
	 * @return AccessTokenに紐づくユーザー情報の取得方法の制御
	 */
	public boolean isReloadUser() {
		return reloadUser;
	}

	/**
	 * @param reloadUser AccessTokenに紐づくユーザー情報の取得方法の制御
	 */
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

		post.setEntity(new UrlEncodedFormEntity(params));

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
			config.addServiceInitListener(httpClientConfig);
		}

		httpInvoker = new SimpleHttpInvoker(httpClientConfig, exponentialBackoff);
	}

	@Override
	public void destroyed() {
		httpInvoker = null;
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
