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

import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.StateType;

@WebApi(name="oauth/revoke",
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	checkXRequestedWithHeader=false,
	privilaged=true,
	state=StateType.STATELESS,
	responseType="application/json"
)
@CommandClass(name="mtp/oauth/RevokeCommand", displayName="OAuth2.0 Token Revocation Endpoint")
public class RevokeCommand implements Command {
	static final String PARAM_TOKEN = "token";
	static final String PARAM_TOKEN_TYPE_HINT = "token_type_hint";

	static final String STAT_SUCCESS = "SUCCESS";
	
	@Override
	public String execute(RequestContext request) {
		OAuthClientRuntime clientRuntime = CommandUtil.validateClient(request, true);
		String token = request.getParam(PARAM_TOKEN);
		String tokenTypeHint = StringUtil.stripToNull(request.getParam(PARAM_TOKEN_TYPE_HINT));
		clientRuntime.getAuthorizationServer().revoke(token, tokenTypeHint, clientRuntime);
		return STAT_SUCCESS;
	}
}
