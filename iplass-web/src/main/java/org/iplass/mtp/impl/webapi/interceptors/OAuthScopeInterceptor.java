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
package org.iplass.mtp.impl.webapi.interceptors;

import org.iplass.mtp.command.interceptor.CommandInterceptor;
import org.iplass.mtp.command.interceptor.CommandInvocation;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.authenticate.token.web.AuthorizationRequiredException;
import org.iplass.mtp.impl.auth.authenticate.token.web.BearerTokenAutoLoginHandler;
import org.iplass.mtp.impl.auth.oauth.AccessTokenAccountHandle;
import org.iplass.mtp.impl.webapi.WebApiInvocationImpl;

/**
 * OAuthのscopeをチェックするCommandInterceptor。
 * 
 * @author K.Higuchi
 *
 */
public class OAuthScopeInterceptor implements CommandInterceptor {

	@Override
	public String intercept(CommandInvocation invocation) {
		WebApiInvocationImpl webApiInvocation = (WebApiInvocationImpl) invocation;
		AuthContextHolder authContext = AuthContextHolder.getAuthContext();
		if (authContext.getUserContext().getAccount() instanceof AccessTokenAccountHandle) {
			//check scope
			AccessTokenAccountHandle atah = (AccessTokenAccountHandle) authContext.getUserContext().getAccount();
			if (!webApiInvocation.getWebApiRuntime().isSufficientOAuthScope(atah.getAccessToken().getGrantedScopes())) {
				throw new AuthorizationRequiredException(BearerTokenAutoLoginHandler.AUTH_SCHEME_BEARER, null, AuthorizationRequiredException.CODE_INSUFFICIENT_SCOPE, null);
			}
		}
		
		//実行（後続処理へ）
		return invocation.proceedCommand();
		
	}
}
