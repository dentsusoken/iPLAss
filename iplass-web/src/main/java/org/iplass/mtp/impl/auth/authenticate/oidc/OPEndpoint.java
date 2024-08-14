/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.iplass.mtp.auth.oidc.definition.ClientAuthenticationType;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthEndpointConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * OPEndpoint
 */
public class OPEndpoint {
	private static Logger logger = LoggerFactory.getLogger(OPEndpoint.class);

	private String tokenEndpointUrl;
	private String userInfoEndpointUrl;
	private OpenIdConnectService opService;

	/**
	 * コンストラクタ
	 * @param tokenEndpointUrl
	 * @param userInfoEndpointUrl
	 * @param opService
	 */
	public OPEndpoint(String tokenEndpointUrl, String userInfoEndpointUrl, OpenIdConnectService opService) {
		this.tokenEndpointUrl = tokenEndpointUrl;
		this.userInfoEndpointUrl = userInfoEndpointUrl;
		this.opService = opService;
	}

	/**
	 * token を取得する
	 *
	 * @param clientAuthenticationType
	 * @param clientId
	 * @param clientSecret
	 * @param code
	 * @param redirectUri
	 * @param codeVerifier
	 * @return token
	 */
	public Map<String, Object> token(ClientAuthenticationType clientAuthenticationType, String clientId, String clientSecret, String code, String redirectUri, String codeVerifier) {
		try {
			String content = null;
			HttpClient client = opService.getHttpClient();
			HttpPost post = new HttpPost(tokenEndpointUrl);
			List<NameValuePair> nvps = new ArrayList<>();

			if (clientAuthenticationType == ClientAuthenticationType.CLIENT_SECRET_BASIC) {
				post.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes("UTF-8")));
			} else {
				nvps.add(new BasicNameValuePair(OAuthEndpointConstants.PARAM_CLIENT_ID, clientId));
				nvps.add(new BasicNameValuePair(OAuthEndpointConstants.PARAM_CLIENT_SECRET, clientSecret));
			}
			nvps.add(new BasicNameValuePair(OAuthEndpointConstants.PARAM_CODE, code));
			nvps.add(new BasicNameValuePair(OAuthEndpointConstants.PARAM_REDIRECT_URI, redirectUri));
			nvps.add(new BasicNameValuePair(OAuthEndpointConstants.PARAM_GRANT_TYPE, OAuthConstants.GRANT_TYPE_AUTHORIZATION_CODE));
			if (codeVerifier != null) {
				nvps.add(new BasicNameValuePair(OAuthEndpointConstants.PARAM_CODE_VERIFIER, codeVerifier));
			}

			post.setEntity(new UrlEncodedFormEntity(nvps));
			content = client.execute(post, res -> {
				HttpEntity entity = res.getEntity();
				try {
					return EntityUtils.toString(entity);

				} finally {
					EntityUtils.consume(entity);
				}
			});

			return opService.getObjectMapper().readValue(content, new TypeReference<Map<String, Object>>() {});
		} catch (IOException e) {
			throw new OIDCRuntimeException(e);
		}
	}

	/**
	 * user profile を取得する
	 * @param tokenType
	 * @param accessToken
	 * @return userInfo
	 */
	public Map<String, Object> userInfo(String tokenType, String accessToken) {
		//get user profile
		try {
			HttpGet get = new HttpGet(userInfoEndpointUrl);
			get.setHeader("Authorization", tokenType+ " " + accessToken);

			return opService.getHttpClient().execute(get, res -> {
				HttpEntity entity = res.getEntity();

				try {
					String content = EntityUtils.toString(entity);

					return opService.getObjectMapper().readValue(content, new TypeReference<Map<String, Object>>() {
					});

				} finally {
					EntityUtils.consume(entity);
				}
			});

		} catch (IOException e) {
			throw new OIDCRuntimeException(e);
		}
	}

}
