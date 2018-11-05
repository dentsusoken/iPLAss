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
package org.iplass.mtp.impl.auth.authenticate.token.web;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenCredential;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginHandler;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginInstruction;
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
	
	private boolean rejectAmbiguousRequest;
	
	public boolean isRejectAmbiguousRequest() {
		return rejectAmbiguousRequest;
	}

	public void setRejectAmbiguousRequest(boolean rejectAmbiguousRequest) {
		this.rejectAmbiguousRequest = rejectAmbiguousRequest;
	}

	private boolean isForm(HttpServletRequest sr, RestRequestContext rrc) {
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
		if (!(req instanceof RestRequestContext)) {
			return AutoLoginInstruction.THROUGH;
		}
		
		if (isLogined) {
			String token = tokenFromRequest(req);
			if (token == null) {
				return AutoLoginInstruction.THROUGH;
			}
			
			String tokenFromSess = null;
			Session s = sessionService.getSession(false);
			if (s != null) {
				tokenFromSess = (String) s.getAttribute(SESSION_ATTRIBUTE_BEARER_TOKEN);
			}
			
			if (!token.equals(tokenFromSess)) {
				if (rejectAmbiguousRequest) {
					//セッション上のトークンと、リクエストトークンが等しくないならエラー400
					throw new AuthorizationRequiredException(BearerTokenAutoLoginHandler.AUTH_SCHEME_BEARER, null, AuthorizationRequiredException.CODE_INVALID_REQUEST, "another login session is avaliable");
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("login session is avaliable, but another bearer token is specified. current session:" + tokenFromSess + ", request header:" + token);
					} else {
						logger.warn("login session is avaliable, but another bearer token is specified.");
					}
				}
			}
			return AutoLoginInstruction.THROUGH;
		} else {
			String token = tokenFromRequest(req);
			if (token == null) {
				return AutoLoginInstruction.THROUGH;
			}
			
			//TODO Credential type to configurable
			SimpleAuthTokenCredential cre = new SimpleAuthTokenCredential(token);
			
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
