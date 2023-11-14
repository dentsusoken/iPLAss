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

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ApplicationException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMapping(name=ViewUpdatePasswordCommand.ACTION_VIEW_UPDATE_PASSWORD,
		clientCacheType=ClientCacheType.CACHE,
		needTrustedAuthenticate=true,
		result=@Result(type=Type.TEMPLATE, value=Constants.TEMPLATE_UPDATE_PASSWORD)
)
@CommandClass(name="gem/auth/ViewUpdatePasswordCommand", displayName="パスワード更新画面表示")
@Template(
		name=Constants.TEMPLATE_UPDATE_PASSWORD,
		path=Constants.CMD_RSLT_JSP_UPDATE_PASSWORD,
		layoutActionName=Constants.LAYOUT_NORMAL_ACTION
)
public final class ViewUpdatePasswordCommand implements Command, AuthCommandConstants {

	private static Logger logger = LoggerFactory.getLogger(UpdatePasswordCommand.class);

	public static final String ACTION_VIEW_UPDATE_PASSWORD = "gem/auth/password";

	private TopViewDefinitionManager tvdm = ManagerLocator.getInstance().getManager(TopViewDefinitionManager.class);

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);
		String viewName = request.getParam(Constants.VIEW_NAME);

		UserMaintenanceParts parts = getUserMaintenanceParts();
		if (parts != null) {
			//Entity定義名チェック
			if (defName == null || !User.DEFINITION_NAME.equals(defName)) {
				if (logger.isDebugEnabled()) {
					logger.debug("invalid defName: " + defName);
				}
				throw new ApplicationException(resourceString("command.auth.ViewUpdatePasswordCommand.invalidParam"));
			}

			//View名チェック
			String settingViewName = parts.getViewName() != null ? parts.getViewName() : "";
			if (viewName != null && !settingViewName.equals(viewName)) {
				if (logger.isDebugEnabled()) {
					logger.debug("invalid viewName: " + viewName + ", expected: " + settingViewName);
				}
				throw new ApplicationException(resourceString("command.auth.ViewUpdatePasswordCommand.invalidParam"));
			}
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

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
