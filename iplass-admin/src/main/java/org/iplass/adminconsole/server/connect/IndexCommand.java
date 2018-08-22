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

package org.iplass.adminconsole.server.connect;

import org.iplass.adminconsole.server.base.i18n.AdminResourceBundleUtil;
import org.iplass.adminconsole.shared.base.dto.auth.UnauthorizedAccessException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;

/**
 * <p>Admin Console起動用のAction、Command、Template定義です。</p>
 *
 */
@ActionMapping(
		name="admin/index",
		displayName="Admin Console",
		result=@Result(type=Type.JSP, value="/jsp/admin/main.jsp", templateName="admin/main.jsp")
)
@CommandClass(
		name="admin/IndexCommand",
		displayName="Admin Console",
		readOnly=true
)
public final class IndexCommand implements Command {

	/**
	 * コンストラクタ
	 */
	public IndexCommand() {
	}

	@Override
	public String execute(RequestContext request) {

		UserContext userContext = AuthContextHolder.getAuthContext().getUserContext();

		if (userContext instanceof AnonymousUserContext) {
			throw new UnauthorizedAccessException(AdminResourceBundleUtil.resourceString("util.AuthUtil.notHavePermission"));
		}
		if (!userContext.getUser().isAdmin()) {
			throw new UnauthorizedAccessException(AdminResourceBundleUtil.resourceString("util.AuthUtil.notHavePermission"));
		}

		return null;
	}
}
