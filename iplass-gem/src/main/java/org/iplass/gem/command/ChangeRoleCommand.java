/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command;

import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@WebApi(
		name=ChangeRoleCommand.WEBAPI_NAME,
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results=ChangeRoleCommand.CHECK_RESULT,
		checkXRequestedWithHeader=true
	)
@CommandClass(name="gem/menu/ChangeRoleCommand", displayName="ロール変更")
public final class ChangeRoleCommand implements Command {

	public static final String WEBAPI_NAME = "gem/menu/changeRole";
	public static final String CHECK_RESULT = "checkResult";

	@Override
	public String execute(RequestContext request) {
		String roleName = request.getParam(Constants.ROLE_NAME);

		//ユーザーが対象ロールに紐づくか確認
		AuthContext authContext = AuthContext.getCurrentContext();
		if (authContext.userInRole(roleName)) {
			request.getSession().setAttribute(Constants.ROLE_NAME, roleName);
			request.setAttribute(CHECK_RESULT, true);
		} else {
			if (MenuCommand.DEFAULT.equals(roleName) && AuthContext.getCurrentContext().getUser().isAdmin()) {
				//defaultの切り替えはAdmin以外はない
				request.getSession().setAttribute(Constants.ROLE_NAME, roleName);
				request.setAttribute(CHECK_RESULT, true);
			} else {
				request.setAttribute(CHECK_RESULT, false);
			}
		}
		return null;
	}

}
