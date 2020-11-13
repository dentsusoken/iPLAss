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

package org.iplass.gem.command.auth;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.auth.login.rememberme.RememberMeTokenInfo;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenInfo;
import org.iplass.mtp.auth.oauth.AccessTokenInfo;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.auth.token.AuthTokenInfoList;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.command.annotation.webapi.WebApiTokenCheck;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@ActionMapping(name=RevokeApplicationCommand.VIEW_ACTION_NAME,
		needTrustedAuthenticate=true,
		command={},
		result=@Result(type=Type.JSP,
				value=Constants.CMD_RSLT_JSP_APP_MAINTENANCE,
				templateName="gem/auth/application",
				layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
	)
@WebApi(
		name=RevokeApplicationCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		restJson=@RestJson(parameterName="param"),
		methods=MethodType.POST,
		tokenCheck=@WebApiTokenCheck(useFixedToken=true),
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/auth/RevokeApplicationCommand", displayName="アプリケーション削除")
public class RevokeApplicationCommand implements Command {

	public static final String VIEW_ACTION_NAME = "gem/auth/application";
	public static final String WEBAPI_NAME = "gem/auth/revokeapplication";

	@Override
	public String execute(RequestContext request) {

		AuthTokenInfoList infoList = AuthContext.getCurrentContext().getAuthTokenInfos();
		if (infoList == null) {
			return Constants.CMD_EXEC_ERROR;
		}

		String target = request.getParam("target");

		if (target != null) {
			if (target.equals("one-token")) {
				String type = request.getParam("tokenType");
				String key = request.getParam("tokenKey");
				AuthTokenInfo info = infoList.get(type, key);
				if (info != null) {
					infoList.remove(type, key);
				}
			} else {
				for (AuthTokenInfo info : infoList.getList()) {
					if (info instanceof AccessTokenInfo) {
						if (target.equals("all-app")) {
							infoList.remove(info.getType(), info.getKey());
						}
					} else if (info instanceof RememberMeTokenInfo) {
						if (target.equals("all-rememberme")) {
							infoList.remove(info.getType(), info.getKey());
						}
					} else if (info instanceof SimpleAuthTokenInfo) {
						if (target.equals("all-sat")) {
							infoList.remove(info.getType(), info.getKey());
						}
					}
				}
			}
		}

		return Constants.CMD_EXEC_SUCCESS;
	}

}
