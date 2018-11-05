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

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.command.interceptor.CommandInvocation;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;
import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.impl.webapi.MetaWebApi.WebApiRuntime;
import org.iplass.mtp.impl.webapi.MetaWebApiTokenCheck;
import org.iplass.mtp.impl.webapi.WebApiInvocationImpl;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.TokenValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TokenInterceptor implements CommandInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

	@Override
	public String intercept(CommandInvocation invocation) {
		
		if (invocation instanceof WebApiInvocationImpl) {
			WebApiRuntime runtime = ((WebApiInvocationImpl) invocation).getWebApiRuntime();
			RequestContext context = invocation.getRequest();
			MetaWebApiTokenCheck tokenCheck = runtime.getMetaData().getTokenCheck();
			
			if (tokenCheck != null) {
				logger.trace("execute validate token...");
				
				boolean isValid = false;
				String token = context.getParam(TokenStore.TOKEN_PARAM_NAME);
				if (token == null) {
					//from header
					HttpServletRequest request = (HttpServletRequest) context.getAttribute(WebRequestConstants.SERVLET_REQUEST);
					token = request.getHeader(TokenStore.TOKEN_HEADER_NAME);
				}
				if (token == null) {
					tokenError(tokenCheck);
				}
				
				TokenStore tokenStore = TokenStore.getTokenStore(context.getSession(false));
				if (tokenStore == null) {
					tokenError(tokenCheck);
				}
				
				try {
					if (tokenCheck.isUseFixedToken()) {
						isValid = tokenStore.isValidFixed(token);
					} else {
						isValid = tokenStore.isValid(token, tokenCheck.isConsume());
					}
					if (!isValid) {
						tokenError(tokenCheck);
					}
					
					return invocation.proceedCommand();
					
				} catch (RuntimeException e) {
					//例外時に、消費したTokenを元に戻す
					if (isValid && !tokenCheck.isUseFixedToken() && tokenCheck.isExceptionRollback() && tokenCheck.isConsume()) {
						tokenStore.pushBack(token);
					}
					throw e;
				}
			}
		}
		
		//no token check
		return invocation.proceedCommand();
	}
	
	private void tokenError(MetaWebApiTokenCheck tokenCheck) {
		throw new TokenValidationException(resourceString("impl.web.interceptors.TokenInterceptor.invalidErr"));
	}
	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}
}
