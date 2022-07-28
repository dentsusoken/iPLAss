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
package org.iplass.mtp.impl.auth.oauth.command;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.NeedTrustedAuthenticationException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthApplicationException;
import org.iplass.mtp.impl.auth.oauth.OAuthClientService;
import org.iplass.mtp.impl.auth.oauth.OAuthRuntimeException;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationCode;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationRequest;
import org.iplass.mtp.impl.auth.oauth.util.OAuthConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthEndpointConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.definition.HttpMethodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name="oauth/authorize",
	clientCacheType=ClientCacheType.NO_CACHE,
	allowMethod={HttpMethodType.GET, HttpMethodType.POST},
	publicAction=true,
	result={
		@Result(status=AuthorizeCommand.STAT_SUCCESS_REDIRECT, type=Type.REDIRECT, allowExternalLocation=true, value=WebRequestConstants.REDIRECT_PATH),
		@Result(status=AuthorizeCommand.STAT_SUCCESS_POST, type=Type.TEMPLATE, value=AuthorizeCommand.TMPL_POST),
		@Result(status=AuthorizeCommand.STAT_NEED_CONSENT, type=Type.DYNAMIC, value=AuthorizeCommand.REQUEST_TMPL_NAME),
		@Result(status=AuthorizeCommand.STAT_ERROR_REDIRECT, type=Type.REDIRECT, allowExternalLocation=true, value=WebRequestConstants.REDIRECT_PATH),
		@Result(status=AuthorizeCommand.STAT_ERROR_POST, type=Type.TEMPLATE, value=AuthorizeCommand.TMPL_POST),
	}
)
@Template(name=AuthorizeCommand.TMPL_POST, displayName="OAuth Post Response Mode", path="/jsp/oauth/OAuthPost.jsp", contentType="text/html; charset=utf-8")
@Template(name=AuthorizeCommand.TMPL_CONSENT, displayName="Default OAuth Consent View", path="/jsp/oauth/Consent.jsp", contentType="text/html; charset=utf-8")
@CommandClass(name="mtp/oauth/AuthorizeCommand", displayName="OAuth2.0 Authorization Endpoint")
public class AuthorizeCommand implements Command, OAuthEndpointConstants {

	static final String STAT_SUCCESS_REDIRECT = "SUCCESS_REDIRECT";
	static final String STAT_SUCCESS_POST = "SUCCESS_POST";
	static final String STAT_ERROR_REDIRECT = "ERROR_REDIRECT";
	static final String STAT_ERROR_POST = "ERROR_POST";
	
	static final String STAT_NEED_CONSENT = "NEED_CONSENT";
	
	static final String REQUEST_TMPL_NAME = "templateName";
	static final String REQUEST_AUTHORIZATION_CODE = "authorizationCode";
	static final String REQUEST_AUTHORIZATION_REQUEST = "authorizationRequest";
	static final String REQUEST_ERROR = "error";

	public static final String SESSION_AUTHORIZATION_REQUEST = "org.iplass.mtp.oauth.authorizationRequest";

	public static final String TMPL_POST = "oauth/OAuthPost";
	public static final String TMPL_CONSENT = "oauth/Consent";
	
	private static Logger logger = LoggerFactory.getLogger(AuthorizeCommand.class);
	
	private OAuthClientService clientService = ServiceRegistry.getRegistry().getService(OAuthClientService.class);
	
	//TODO https://tools.ietf.org/html/rfc8252
	//Authorization servers can protect against these fake agents by requiring an authentication factor only available to genuine external user agents.
	//ユーザエージェントでチェックするしかないか？

	/**
	 * Authorization Code Grant/Flow (with PKCE) のみサポート
	 * 
	 */
	@Override
	public String execute(RequestContext request) {
		String clientId = request.getParam(PARAM_CLIENT_ID);
		OAuthClientRuntime clientRuntime = clientService.getRuntimeByName(clientId);
		if (clientRuntime == null) {
			throw new OAuthRuntimeException("invalid client_id:" + clientId);
		}
		String redirectUri = StringUtil.stripToNull(request.getParam(PARAM_REDIRECT_URI));
		String validRedirectUri = clientRuntime.selectValidRedirectUri(redirectUri);
		if (validRedirectUri == null) {
			throw new OAuthRuntimeException("invalid redirect_uri:" + redirectUri);
		}
		
		OAuthAuthorizationRuntime authRuntime = clientRuntime.getAuthorizationServer();
		AuthorizationRequest authReq = new AuthorizationRequest(authRuntime.getMetaData().getName(), clientRuntime.getMetaData().getName(), validRedirectUri);
		authReq.setState(StringUtil.stripToNull(request.getParam(PARAM_STATE)));
		authReq.setCodeChallenge(StringUtil.stripToNull(request.getParam(PARAM_CODE_CHALLENGE)));
		authReq.setCodeChallengeMethod(StringUtil.stripToNull(request.getParam(PARAM_CODE_CHALLENGE_METHOD)));
		authReq.addResponseTypes(StringUtil.split(request.getParam(PARAM_RESPONSE_TYPE), ' '));
		authReq.addScopes(StringUtil.split(request.getParam(PARAM_SCOPE), ' '));
		authReq.setResponseMode(StringUtil.stripToNull(request.getParam(PARAM_RESPONSE_MODE)));
		
		authReq.setNonce(StringUtil.stripToNull(request.getParam(PARAM_NONCE)));
		authReq.addPrompts(StringUtil.split(request.getParam(PARAM_PROMPT), ' '));
		authReq.setMaxAge(request.getParamAsLong(PARAM_MAX_AGE));

		try {
			authRuntime.checkValidAuthorizationRequest(authReq);
			
			//check user auth context
			AuthContext ac = AuthContext.getCurrentContext();
			if (ac.isAuthenticated() && !authReq.hasPrompt(OAuthConstants.PROMPT_LOGIN)) {
				//check max_age
				if (authReq.getMaxAge() != null) {
					if (ac.getAuthTime() + TimeUnit.SECONDS.toMillis(authReq.getMaxAge()) < System.currentTimeMillis()) {
						if (authReq.hasPrompt(OAuthConstants.PROMPT_NONE)) {
							throw new OAuthApplicationException(OAuthConstants.ERROR_LOGIN_REQUIRED, "Login required.");
						} else {
							throw new NeedTrustedAuthenticationException();
						}
					}
				}
			} else {
				if (authReq.hasPrompt(OAuthConstants.PROMPT_NONE)) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_LOGIN_REQUIRED, "Login required.");
				} else {
					throw new NeedTrustedAuthenticationException();
				}
			}

			//check user has Available Role
			if (!authRuntime.hasAvailableRole()) {
				throw new OAuthApplicationException(OAuthConstants.ERROR_ACCESS_DENIED, "User can't access this resource.");
			}
			
			if (authReq.hasPrompt(OAuthConstants.PROMPT_CONSENT) || authRuntime.isNeedConsent(request, authReq)) {
				if (authReq.getPrompt() != null && authReq.getPrompt().contains(OAuthConstants.PROMPT_NONE)) {
					throw new OAuthApplicationException(OAuthConstants.ERROR_CONSENT_REQUIRED, "Consent required.");
				} else {
					return needConsent(request, authReq, authRuntime);
				}
			} else {
				//remove offline_access scope
				authReq.getScopes().remove(OAuthConstants.SCOPE_OFFLINE_ACCESS);
				
				AuthorizationCode code = authRuntime.generateCode(authReq);
				return success(request, code);
			}
		} catch (NeedTrustedAuthenticationException e) {
			//redirect to Login screen
			throw e;
		} catch (OAuthApplicationException e) {
			if (logger.isDebugEnabled()) {
				logger.debug(e.getMessage(), e);
			}
			return error(request, e.getCode(), e.getDescription(), authReq);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return error(request, OAuthConstants.ERROR_SERVER_ERROR, "See server log for details.", authReq);
		}
	}
	
	private String encode(String str) {
		return OAuthUtil.encodeRfc3986(str);
	}
	
	private String createRedirectUri(AuthorizationRequest authReq, Consumer<StringBuilder> appender) {
		StringBuilder redirectUri = new StringBuilder();
		redirectUri.append(authReq.getRedirectUri());
		if (authReq.getResponseMode() == null || authReq.getResponseMode().equals(OAuthConstants.RESPONSE_MODE_QUERY)) {
			if (authReq.getRedirectUri().contains("?")) {
				redirectUri.append('&');
			} else {
				redirectUri.append('?');
			}
		} else if (authReq.getResponseMode().equals(OAuthConstants.RESPONSE_MODE_FRAGMENT)) {
			redirectUri.append('#');
		}
		appender.accept(redirectUri);
		
		return redirectUri.toString();
	}
	
	String success(RequestContext request, AuthorizationCode code) {
		
		if (OAuthConstants.RESPONSE_MODE_FORM_POST.equals(code.getRequest().getResponseMode())) {
			request.setAttribute(REQUEST_AUTHORIZATION_REQUEST, code.getRequest());
			request.setAttribute(REQUEST_AUTHORIZATION_CODE, code);
			return STAT_SUCCESS_POST;
		} else {
			String redirectUri = createRedirectUri(code.getRequest(), sb -> {
				sb.append(PARAM_CODE).append('=').append(encode(code.getCodeValue()));
				if (code.getRequest().getState() != null) {
					sb.append('&').append(PARAM_STATE).append('=').append(encode(code.getRequest().getState()));
				}
			});
			
			request.setAttribute(WebRequestConstants.REDIRECT_PATH, redirectUri.toString());
			return STAT_SUCCESS_REDIRECT;
		}
	}
	
	String error(RequestContext request, String errorCode, String errorDescription, AuthorizationRequest authReq) {
		
		if (OAuthConstants.RESPONSE_MODE_FORM_POST.equals(authReq.getResponseMode())) {
			request.setAttribute(REQUEST_AUTHORIZATION_REQUEST, authReq);
			request.setAttribute(REQUEST_ERROR, new OAuthApplicationException(errorCode, errorDescription));
			return STAT_ERROR_POST;
		} else {
			String redirectUri = createRedirectUri(authReq, sb -> {
				sb.append(PARAM_ERROR).append('=').append(encode(errorCode));
				if (errorDescription != null) {
					sb.append('&').append(PARAM_ERROR_DESCRIPTION).append('=').append(encode(errorDescription));
				}
				if (authReq.getState() != null) {
					sb.append('&').append(PARAM_STATE).append('=').append(encode(authReq.getState()));
				}
			});
			
			request.setAttribute(WebRequestConstants.REDIRECT_PATH, redirectUri.toString());
			return STAT_ERROR_REDIRECT;
		}
	}
	
	String needConsent(RequestContext request, AuthorizationRequest authReq, OAuthAuthorizationRuntime authRuntime) {
		request.getSession().setAttribute(SESSION_AUTHORIZATION_REQUEST, authReq);
		request.setAttribute(REQUEST_TMPL_NAME, authRuntime.consentTemplateName());
		
		return STAT_NEED_CONSENT;
	}
}
