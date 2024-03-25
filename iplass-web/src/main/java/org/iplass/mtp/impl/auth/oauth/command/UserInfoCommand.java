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
package org.iplass.mtp.impl.auth.oauth.command;

import java.util.Map;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;
import org.iplass.mtp.impl.auth.oauth.AccessTokenAccountHandle;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthClientService;
import org.iplass.mtp.impl.auth.oauth.OAuthRuntimeException;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.StateType;

@WebApi(name="oauth/userinfo",
	accepts=RequestType.REST_FORM,
	methods= {MethodType.GET, MethodType.POST},
	checkXRequestedWithHeader=false,
	state=StateType.STATELESS,
	responseType="application/json",
	supportBearerToken=true,
	oauthScopes= {"openid"}
)
@CommandClass(name="mtp/oauth/UserInfoCommand", displayName="OpenIDConnect1.0 UserInfo Endpoint")
public class UserInfoCommand implements Command {
	static final String STAT_SUCCESS = "SUCCESS";

	private OAuthClientService clientService = ServiceRegistry.getRegistry().getService(OAuthClientService.class);
	
	@Override
	public String execute(RequestContext request) {
		
		AuthContextHolder ach = AuthContextHolder.getAuthContext();
		AccountHandle ah = ach.getUserContext().getAccount();
		if (!(ah instanceof AccessTokenAccountHandle)) {
			//resolve token manually?
			throw new OAuthRuntimeException("not authed by AccessToken:" + ah);
		}

		AccessToken token = ((AccessTokenAccountHandle) ah).getAccessToken();
		
		OAuthClientRuntime client = clientService.getRuntimeByName(token.getClientId());
		if (client == null) {
			throw new OAuthRuntimeException("invalid client_id:" + token.getClientId());
		}

		OAuthAuthorizationRuntime authServer = client.getAuthorizationServer();
		Map<String, Object> userInfo = AuthContext.doPrivileged(() -> authServer.userInfo(token, client));
		
		ResponseBuilder res = Response.ok().type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8")).entity(userInfo);
		request.setAttribute(WebApiRequestConstants.DEFAULT_RESULT, res);
		
		return STAT_SUCCESS;
	}

}
