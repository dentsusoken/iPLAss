/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMapping.ClientCacheType;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.view.top.TopViewDefinition;
import org.iplass.mtp.view.top.TopViewDefinitionManager;
import org.iplass.mtp.view.top.parts.TopViewParts;
import org.iplass.mtp.view.top.parts.UserMaintenanceParts;

/**
 * ユーザー情報変更画面表示
 * @author EDS Y.Yasuda
 *
 */
@ActionMapping(name=ViewUserMaintenanceCommand.ACTION_VIEW_UPDATE_PASSWORD,
		clientCacheType=ClientCacheType.CACHE,
		needTrustedAuthenticate=true,
		result=@Result(type=Type.TEMPLATE, value=Constants.TEMPLATE_UPDATE_PASSWORD)
)
@CommandClass(name="gem/auth/ViewUserMaintenanceCommand", displayName="ユーザー情報変更画面表示")
@Template(
		name=Constants.TEMPLATE_UPDATE_PASSWORD,
		path=Constants.CMD_RSLT_JSP_UPDATE_PASSWORD,
		layoutActionName=Constants.LAYOUT_NORMAL_ACTION
)
public final class ViewUserMaintenanceCommand implements Command, AuthCommandConstants {

	public static final String ACTION_VIEW_UPDATE_PASSWORD = "gem/auth/password";

	private TopViewDefinitionManager tvdm = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);

	@Override
	public String execute(RequestContext request) {
		UserMaintenanceParts parts = getUserMaintenanceParts();
		if (parts != null) {
			//Entity定義名設定
			request.setAttribute(Constants.NESTTABLE_DEF_NAME, User.DEFINITION_NAME);
			request.setAttribute(Constants.NESTTABLE_VIEW_NAME, parts.getViewName());
		}

		return null;
	}

	/**
	 * ユーザーメンテナンスパーツを取得
	 * @return
	 */
	private UserMaintenanceParts getUserMaintenanceParts() {
		TopViewDefinition topView = tvdm.getRequestTopView();
		if (topView != null) {
			for (TopViewParts parts : topView.getParts()) {
				if (parts instanceof UserMaintenanceParts) {
					return (UserMaintenanceParts) parts;
				}
			}
		}
		return null;
	}
}
