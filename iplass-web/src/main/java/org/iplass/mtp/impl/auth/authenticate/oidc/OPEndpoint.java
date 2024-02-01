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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.iplass.mtp.auth.oidc.definition.ClientAuthenticationType;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthEndpointConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;

public class OPEndpoint {
	private static Logger logger = LoggerFactory.getLogger(OPEndpoint.class);
	
	private String tokenEndpointUrl;
	private String userInfoEndpointUrl;
	private OpenIdConnectService opService;
	
	public OPEndpoint(String tokenEndpointUrl, String userInfoEndpointUrl, OpenIdConnectService opService) {
		this.tokenEndpointUrl = tokenEndpointUrl;
		this.userInfoEndpointUrl = userInfoEndpointUrl;
		this.opService = opService;
	}
	
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
			HttpResponse res = client.execute(post);
			try {
				HttpEntity entity = res.getEntity();
				content = EntityUtils.toString(entity);
			} finally {
				post.releaseConnection();
			}
			return opService.getObjectMapper().readValue(content, new TypeReference<Map<String, Object>>() {});
		} catch (ParseException | IOException e) {
			throw new OIDCRuntimeException(e);
		}
	}
	
	public Map<String, Object> userInfo(String tokenType, String accessToken) {
		//get user profile
		try {
			HttpGet get = new HttpGet(userInfoEndpointUrl);
			get.setHeader("Authorization", tokenType+ " " + accessToken);
			
			HttpResponse res = opService.getHttpClient().execute(get);
			try {
				HttpEntity entity = res.getEntity();
				StatusLine sl = res.getStatusLine();
				String content = EntityUtils.toString(entity);
				
				if (sl.getStatusCode() != HttpStatus.SC_OK) {
					logger.warn("can't get user's profile caluse http status=" + sl.getStatusCode() + " " + sl.getReasonPhrase() + " content=" + content);
					return null;
				}
				
				Map<String, Object> ret = opService.getObjectMapper().readValue(content, new TypeReference<Map<String, Object>>() {});
				return ret;
			} finally {
				get.releaseConnection();
			}
		} catch (ParseException | IOException e) {
			throw new OIDCRuntimeException(e);
		}
	}
	
}
