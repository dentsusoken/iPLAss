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
package org.iplass.mtp.impl.auth.authenticate.token.web;

import jakarta.servlet.http.HttpServletRequest;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenCredential;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginHandler;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginInstruction;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.session.Session;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.impl.webapi.rest.RestRequestContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.webapi.definition.MethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFC6750ベースのAutoLoginHandler。
 * 
 * @author K.Higuchi
 *
 */
public class BearerTokenAutoLoginHandler implements AutoLoginHandler {
	
	private static Logger logger = LoggerFactory.getLogger(BearerTokenAutoLoginHandler.class);
	
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String AUTH_SCHEME_BEARER = "Bearer";
	public static final String PARAM_ACCESS_TOKEN = "access_token";
	
	private static final String SESSION_ATTRIBUTE_BEARER_TOKEN ="mtp.auth.token.bearer.encodedToken";
	
	private SessionService sessionService = ServiceRegistry.getRegistry().getService(SessionService.class);
	private AuthTokenHandler authTokenHandler;
	
	private boolean rejectAmbiguousRequest;
	private boolean bearerTokenHeaderOnly;
	private String authTokenType;
	
	public boolean isBearerTokenHeaderOnly() {
		return bearerTokenHeaderOnly;
	}

	public void setBearerTokenHeaderOnly(boolean bearerTokenHeaderOnly) {
		this.bearerTokenHeaderOnly = bearerTokenHeaderOnly;
	}

	public String getAuthTokenType() {
		return authTokenType;
	}

	public void setAuthTokenType(String authTokenType) {
		this.authTokenType = authTokenType;
		authTokenHandler = ServiceRegistry.getRegistry().getService(AuthTokenService.class).getHandler(authTokenType);
	}

	public boolean isRejectAmbiguousRequest() {
		return rejectAmbiguousRequest;
	}

	public void setRejectAmbiguousRequest(boolean rejectAmbiguousRequest) {
		this.rejectAmbiguousRequest = rejectAmbiguousRequest;
	}
	
	public void setAuthTokenHandler(AuthTokenHandler authTokenHandler) {
		this.authTokenHandler = authTokenHandler;
	}

	private boolean isForm(HttpServletRequest sr, RestRequestContext rrc) {
		if (bearerTokenHeaderOnly) {
			return false;
		}
		if (!"application/x-www-form-urlencoded".equals(sr.getContentType())) {
			return false;
		}
		if (rrc.methodType() == MethodType.GET) {//reject DELETE?
			return false;
		}
		return true;
	}
	
	private String tokenFromRequest(RequestContext req) {
		String token = null;
		HttpServletRequest sr = (HttpServletRequest) req.getAttribute(WebRequestConstants.SERVLET_REQUEST);
		String authHeaderValue = sr.getHeader(HEADER_AUTHORIZATION);
		if (authHeaderValue != null && authHeaderValue.regionMatches(true, 0, AUTH_SCHEME_BEARER + " ", 0, AUTH_SCHEME_BEARER.length() + 1)) {
			token = authHeaderValue.substring(AUTH_SCHEME_BEARER.length() + 1).trim();
			logger.debug("handle bearer token from HTTP header");
		} else {
			if (isForm(sr, (RestRequestContext) req)) {
				token = req.getParam(PARAM_ACCESS_TOKEN);
				if (token != null) {
					logger.debug("handle bearer token from body(form parameter)");
				}
			}
		}
		if (token != null && token.length() > 0) {
			return token;
		} else {
			return null;
		}
	}
	
	@Override
	public AutoLoginInstruction handle(RequestContext req, boolean isLogined, UserContext user) {
		if (isLogined) {
			//WebAPIに限定
			if (!(req instanceof RestRequestContext)) {
				return AutoLoginInstruction.ERROR;
			}
			
			//BearerTokenをサポートしているWebAPIに限定
			RestRequestContext restReq = (RestRequestContext) req;
			if (!restReq.supportBearerToken()) {
				return AutoLoginInstruction.ERROR;
			}
			
			String tokenStr = tokenFromRequest(req);
			if (tokenStr == null) {
				return AutoLoginInstruction.THROUGH;
			}
			
			if (rejectAmbiguousRequest) {
				String tokenFromSess = null;
				Session s = sessionService.getSession(false);
				if (s != null) {
					tokenFromSess = (String) s.getAttribute(SESSION_ATTRIBUTE_BEARER_TOKEN);
				}
				if (!tokenStr.equals(tokenFromSess)) {
					//セッション上のトークンと、リクエストトークンが等しくないならエラー400
					throw new AuthorizationRequiredException(BearerTokenAutoLoginHandler.AUTH_SCHEME_BEARER, null, AuthorizationRequiredException.CODE_INVALID_REQUEST, "another login session is avaliable");
				}
			} else {
				AuthToken token = new AuthToken(tokenStr);
				logger.warn("login session is avaliable, but another bearer token is specified. currentUser:" + user.getAccount().getUnmodifiableUniqueKey() + ", token:" + token.getType() + "." + token.getSeries() + "...");
			}
			
			return AutoLoginInstruction.THROUGH;
		} else {
			if (!(req instanceof RestRequestContext)) {
				return AutoLoginInstruction.THROUGH;
			}
			
			//BearerTokenをサポートしているWebAPIに限定
			RestRequestContext restReq = (RestRequestContext) req;
			if (!restReq.supportBearerToken()) {
				return AutoLoginInstruction.THROUGH;
			}
			
			String tokenStr = tokenFromRequest(req);
			if (tokenStr == null) {
				return AutoLoginInstruction.THROUGH;
			}
			
			AuthToken token = new AuthToken(tokenStr);
			
			
			if (!authTokenHandler.getType().equals(token.getType())) {
				return AutoLoginInstruction.THROUGH;
			}
			
			Credential cre = authTokenHandler.toCredential(token);
			return new AutoLoginInstruction(cre);
		}
	}

	@Override
	public void handleSuccess(AutoLoginInstruction ali, RequestContext req, UserContext user) {
		if (!sessionService.isSessionStateless()) {
			//sessionにtokenを保存。
			Session s = sessionService.getSession(false);
			if (s != null) {
				s.setAttribute(SESSION_ATTRIBUTE_BEARER_TOKEN, ((SimpleAuthTokenCredential) ali.getCredential()).getToken());
			}
		}
	}

	@Override
	public Exception handleException(AutoLoginInstruction ali, ApplicationException e, RequestContext req, boolean isLogined,
			UserContext user) {
		throw new AuthorizationRequiredException(AUTH_SCHEME_BEARER, null, AuthorizationRequiredException.CODE_INVALID_TOKEN, "See server log for details");
	}

}
