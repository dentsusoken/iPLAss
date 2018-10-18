/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi.interceptors;

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.auth.NeedTrustedAuthenticationException;
import org.iplass.mtp.auth.NoPermissionException;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.command.interceptor.CommandInvocation;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginHandler;
import org.iplass.mtp.impl.auth.authenticate.AutoLoginInstruction;
import org.iplass.mtp.impl.auth.authenticate.token.web.AuthorizationRequiredException;
import org.iplass.mtp.impl.auth.authenticate.token.web.BearerTokenAutoLoginHandler;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;
import org.iplass.mtp.impl.web.i18n.LangSelector;
import org.iplass.mtp.impl.webapi.MetaWebApi;
import org.iplass.mtp.impl.webapi.MetaWebApi.WebApiRuntime;
import org.iplass.mtp.impl.webapi.WebApiInvocationImpl;
import org.iplass.mtp.impl.webapi.rest.RestRequestContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRuntimeException;
import org.iplass.mtp.webapi.permission.RequestContextWebApiParameter;
import org.iplass.mtp.webapi.permission.WebApiPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthInterceptor implements CommandInterceptor {

	private static Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	private LangSelector lang = new LangSelector();
	private AuthService authService = ServiceRegistry.getRegistry().getService(AuthService.class);

	private AuthContextHolder getAuthContextHolder(WebApiRuntime webapi) {
		if (webapi.getMetaData().isPrivilaged()) {
			if (logger.isDebugEnabled()) {
				logger.debug("do as privilaged webapi:" + webapi.getMetaData().getName());
			}
			return AuthContextHolder.getAuthContext().privilegedAuthContextHolder();
		} else {
			return AuthContextHolder.getAuthContext();
		}
	}
	
	private void processAutoLogin(WebApiInvocationImpl invocation, AuthService authService) {
		UserContext user = authService.getCurrentSessionUserContext();
		
		//process auto login...

		if (user != null && !(user instanceof AnonymousUserContext)) {
			AuthenticationProvider ap = authService.getAuthenticationProvider();
			AutoLoginHandler alh = ap.getAutoLoginHandler();
			if (alh != null) {
				//TODO BearerTokenAutoLoginHandlerを参照してしまう。。
				if (alh instanceof BearerTokenAutoLoginHandler) {
					if (!invocation.getWebApiRuntime().getMetaData().isSupportBearerToken()) {
						return;
					}
				}
				
				AutoLoginInstruction inst = alh.handle(invocation.getRequest(), true, user);
				switch (inst.getInstruction()) {
				case DO_AUTH:
					try {
						authService.login(inst.getCredential());
						alh.handleSuccess(inst, invocation.getRequest(), authService.getCurrentSessionUserContext());
					} catch (ApplicationException e) {
						Exception he = alh.handleException(inst, e, invocation.getRequest(), true, user);
						if (he != null) {
							//notify to client...
							if (he instanceof RuntimeException) {
								throw (RuntimeException) he;
							} else {
								throw new WebApiRuntimeException(he);
							}
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("auto login fail. cause:" + e);
							}
						}
					}
					break;

				case LOGOUT:
					authService.logout();
					break;

				case ERROR:
					authService.logout();
					if (logger.isDebugEnabled()) {
						logger.debug("auto login fail. AutoLoginHandler return ERROR.");
					}
					throw new ApplicationException(resourceString("auth.Login.noLogin"));

				case THROUGH:
				default:
					break;
				}
			}
		} else {
			for (AuthenticationProvider ap: authService.getAuthenticationProviders()) {
				AutoLoginHandler autoLoginHandler = ap.getAutoLoginHandler();
				if (autoLoginHandler != null) {
					//TODO BearerTokenAutoLoginHandlerを参照してしまう。。
					if (autoLoginHandler instanceof BearerTokenAutoLoginHandler) {
						if (!invocation.getWebApiRuntime().getMetaData().isSupportBearerToken()) {
							continue;
						}
					}
					
					AutoLoginInstruction inst = autoLoginHandler.handle(invocation.getRequest(), false, null);
					switch (inst.getInstruction()) {
					case DO_AUTH:
						try {
							authService.login(inst.getCredential());
							autoLoginHandler.handleSuccess(inst, invocation.getRequest(), authService.getCurrentSessionUserContext());
							return;
						} catch (ApplicationException e) {
							Exception he = autoLoginHandler.handleException(inst, e, invocation.getRequest(), false, null);
							if (he != null) {
								//notify to client...
								if (he instanceof RuntimeException) {
									throw (RuntimeException) he;
								} else {
									throw new WebApiRuntimeException(he);
								}
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug("auto login fail. cause:" + e);
								}
							}
						}
						//check next AuthenticationProvider
						break;

					case LOGOUT:
						authService.logout();
						return;

					case ERROR:
						authService.logout();
						if (logger.isDebugEnabled()) {
							logger.debug("auto login fail. AutoLoginHandler return ERROR.");
						}
						throw new ApplicationException(resourceString("auth.Login.noLogin"));

					case THROUGH:
					default:
						//check next AuthenticationProvider
						break;
					}
				}
			}
		}
	}
	
	@Override
	public String intercept(CommandInvocation invocation) {
		WebApiInvocationImpl webApiInvocation = (WebApiInvocationImpl) invocation;

		//AutoLogin processing
		processAutoLogin(webApiInvocation, authService);
		
		AuthContextHolder account = getAuthContextHolder(webApiInvocation.getWebApiRuntime());

		//当該権限にて処理実行
		return authService.doSecuredAction(account, () -> {
			//set Lang if user has Lang Setting
			lang.selectLangByUser(webApiInvocation.getRequest(), ExecuteContext.getCurrentContext());
			
			boolean isPermitted;
			if (webApiInvocation.getWebApiRuntime().getMetaData().isPublicWebApi()) {
				isPermitted = true;
				if (logger.isDebugEnabled()) {
					logger.debug("do as public webapi:" + webApiInvocation.getWebApiRuntime().getMetaData().getName());
				}
			} else {
				WebApiPermission permission = new WebApiPermission(webApiInvocation.getWebApiRuntime().getMetaData().getName(),
						new RequestContextWebApiParameter(webApiInvocation.getRequest(), additionalParam(webApiInvocation)));
				isPermitted = account.checkPermission(permission);
			}
			
			if (!isPermitted) {
				//TODO BearerTokenAutoLoginHandlerを参照してしまっているが、、、
				if (account.getUserContext() instanceof AnonymousUserContext) {
					if (webApiInvocation.getWebApiRuntime().getMetaData().isSupportBearerToken()) {
						throw new AuthorizationRequiredException(BearerTokenAutoLoginHandler.AUTH_SCHEME_BEARER, null, AuthorizationRequiredException.CODE_NONE, null);
					}
				}
				
				throw new NoPermissionException(resourceString("impl.webapi.WebAPIUtil.noPermission"));
			}
			
			//信頼された認証を必要とするか否か
			if (webApiInvocation.getWebApiRuntime().getMetaData().isNeedTrustedAuthenticate()) {
				if (!authService.checkCurrentSessionTrusted().isTrusted()) {
					//TODO to resource bundle
					throw new NeedTrustedAuthenticationException("need trusted authentication");
				}
			}

			//実行（後続処理へ）
			return invocation.proceedCommand();
		});
	}

	private Map<String, Object> additionalParam(WebApiInvocationImpl webApiInvocation) {
		MetaWebApi meta = webApiInvocation.getWebApiRuntime().getMetaData();
		RestRequestContext rc = (RestRequestContext) webApiInvocation.getRequest();
		String paramName = null;
		switch ( rc.requestType()) {
		case REST_JSON:
			paramName = meta.getRestJsonParameterName();
			break;
		case REST_XML:
			paramName = meta.getRestXmlParameterName();
			break;
		default:
			break;
		}
		if (paramName != null) {
			HashMap<String, Object> ret = new HashMap<>();
			ret.put(paramName, rc.getAttribute(paramName));
			return ret;
		} else {
			return null;
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}

}
