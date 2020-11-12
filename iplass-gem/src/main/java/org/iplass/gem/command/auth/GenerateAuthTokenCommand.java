/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.auth;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenCredential;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenInfo;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.auth.token.AuthTokenInfoList;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.impl.auth.authenticate.simpletoken.SimpleAuthTokenHandler;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;


@WebApi(
		name=GenerateAuthTokenCommand.WEBAPI_NAME,
		accepts=RequestType.REST_FORM,
		methods=MethodType.POST,
		tokenCheck=@WebApiTokenCheck(useFixedToken=true),
		results="token",
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/auth/GenerateAuthTokenCommand", displayName="認証トークン生成")
public class GenerateAuthTokenCommand  implements Command {
	
	public static final String WEBAPI_NAME = "gem/auth/generateAuthToken";
	//default
	private String tokenType = SimpleAuthTokenHandler.TYPE_SIMPLE_DEFAULT;
	
	@Override
	public String execute(RequestContext request) {
		
		AuthTokenInfoList infoList = AuthContext.getCurrentContext().getAuthTokenInfos();
		if (infoList == null) {
			return Constants.CMD_EXEC_ERROR;
		}
		
		String application = request.getParam("application");
		
		AuthTokenInfo tokenInfo = new SimpleAuthTokenInfo(tokenType, application);
		SimpleAuthTokenCredential credential = (SimpleAuthTokenCredential) infoList.generateNewToken(tokenInfo);
		
		request.setAttribute("token", credential.getToken());
		
		return Constants.CMD_EXEC_SUCCESS;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
}
