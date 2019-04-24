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
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;

@ActionMappings({
	@ActionMapping(name=RevokeApplicationCommand.VIEW_ACTION_NAME,
			needTrustedAuthenticate=true,
			command={},
			result=@Result(type=Type.JSP,
					value=Constants.CMD_RSLT_JSP_APP_MAINTENANCE,
					templateName="gem/auth/application",
					layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
	),
	@ActionMapping(name=RevokeApplicationCommand.VIEW_ACTION_NAME,
		needTrustedAuthenticate=true,
		result=@Result(type=Type.JSP,
				value=Constants.CMD_RSLT_JSP_APP_MAINTENANCE,
				templateName="gem/auth/application",
				layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
	)
})
@CommandClass(name="gem/auth/RevokeApplicationCommand", displayName="アプリケーション削除")
public class RevokeApplicationCommand implements Command {

	public static final String VIEW_ACTION_NAME = "gem/auth/application";
	public static final String ACTION_NAME = "gem/auth/revokeapplication";

	@Override
	public String execute(RequestContext request) {

		return Constants.CMD_EXEC_SUCCESS;
	}

}
