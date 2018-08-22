/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.web.interceptors;

import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.command.interceptor.CommandInvocation;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;
import org.iplass.mtp.impl.web.actionmapping.MetaTokenCheck;
import org.iplass.mtp.impl.web.actionmapping.WebInvocationImpl;
import org.iplass.mtp.impl.web.token.TokenStore;
import org.iplass.mtp.web.actionmapping.TokenValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TokenInterceptor implements CommandInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

//	@Override
//	public void intercept(RequestInvocation invocation) {
//		final WebInvocationImpl webInvocation = (WebInvocationImpl) invocation;
//
//		final MetaTokenCheck tokenCheck = webInvocation.getAction().getMetaData().getTokenCheck();
//		if (tokenCheck != null) {
//			logger.trace("execute validate token...");
//
//			String token = invocation.getRequest().getParam(TokenStore.TOKEN_PARAM_NAME);
//			if (token == null) {
//				doError(tokenCheck);
//			}
//
//			SessionContext session = invocation.getRequest().getSession();
//			TokenStore tokenStore = TokenStore.getTokenStore(session);
//			if (tokenStore == null) {
//				doError(tokenCheck);
//			}
//			boolean isValid = false;
//			try {
//				if (tokenCheck.isUseFixedToken()) {
//					isValid = tokenStore.isValidFixed(token);
//				} else {
//					isValid = tokenStore.isValid(token, tokenCheck.isConsume());
//				}
//				if (!isValid) {
//					doError(tokenCheck);
//				}
//
//				invocation.proceedRequest();
//
//			} catch (RuntimeException e) {
//				//例外時に、消費したTokenを元に戻す
//				if (isValid && !tokenCheck.isUseFixedToken() && tokenCheck.isExceptionRollback() && tokenCheck.isConsume()) {
//					tokenStore.pushBack(token);
//				}
//				throw e;
//			}
//
//		} else {
//			invocation.proceedRequest();
//		}
//	}

	private void doError(MetaTokenCheck tokenCheck) {
		throw new TokenValidationException(resourceString("impl.web.interceptors.TokenInterceptor.invalidErr"));
	}

	@Override
	public String intercept(CommandInvocation invocation) {
		if (invocation instanceof WebInvocationImpl) {
			final WebInvocationImpl webInvocation = (WebInvocationImpl) invocation;

			final MetaTokenCheck tokenCheck = webInvocation.getAction().getMetaData().getTokenCheck();
			if (tokenCheck != null) {
				logger.trace("execute validate token...");

				String token = invocation.getRequest().getParam(TokenStore.TOKEN_PARAM_NAME);
				if (token == null) {
					doError(tokenCheck);
				}

				SessionContext session = invocation.getRequest().getSession();
				TokenStore tokenStore = TokenStore.getTokenStore(session);
				if (tokenStore == null) {
					doError(tokenCheck);
				}
				boolean isValid = false;
				try {
					if (tokenCheck.isUseFixedToken()) {
						isValid = tokenStore.isValidFixed(token);
					} else {
						isValid = tokenStore.isValid(token, tokenCheck.isConsume());
					}
					if (!isValid) {
						doError(tokenCheck);
					}

					return invocation.proceedCommand();

				} catch (RuntimeException e) {
					//例外時に、消費したTokenを元に戻す
					if (isValid && !tokenCheck.isUseFixedToken() && tokenCheck.isExceptionRollback() && tokenCheck.isConsume()) {
						tokenStore.pushBack(token);
					}
					throw e;
				}

			} else {
				return invocation.proceedCommand();
			}
		} else {
			return invocation.proceedCommand();
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}
}
