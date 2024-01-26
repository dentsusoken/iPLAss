/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.gem.command.generic.delete;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;

/**
 * ごみ箱表示コマンド
 * @author lis3wg
 */
@ActionMapping(
		name=TrashCommand.ACTION_NAME,
		displayName="ごみ箱表示",
		paramMapping={
			@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
			@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
			@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2"),
		},
		result=@Result(type=Result.Type.JSP,
				value=Constants.CMD_RSLT_JSP_PURGE,
				templateName="gem/generic/delete/purge",
				layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
	)
@CommandClass(name="gem/generic/delete/TrashCommand", displayName="ごみ箱表示")
public final class TrashCommand implements Command{

	public static final String ACTION_NAME = "gem/generic/delete/trash";

	@Override
	public String execute(RequestContext request) {
		return null;
	}

}
