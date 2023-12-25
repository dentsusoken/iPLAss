/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthManager;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.web.template.TemplateUtil;

@ActionMapping(name=LogoutCommand.ACTION_LOOUT,
		clientCacheType=ClientCacheType.NO_CACHE,
		privileged=true,
		result={@Result(status="SUCCESS", type=Type.REDIRECT, value=LogoutCommand.RESULT_REDIRECT_PATH)
})
@CommandClass(name="gem/auth/LogoutCommand", displayName="ログアウト処理")
public final class LogoutCommand implements Command {

	public static final String ACTION_LOOUT = "gem/auth/logout";
	public static final String RESULT_REDIRECT_PATH = "mtp.auth.LogoutCommand.redirectPath";

	private AuthManager am = ManagerLocator.getInstance().getManager(AuthManager.class);

	@Override
	public String execute(RequestContext request) {
		am.logout();
		request.setAttribute(RESULT_REDIRECT_PATH, TemplateUtil.getTenantContextPath() + "/gem/");
		return "SUCCESS";
	}

}
