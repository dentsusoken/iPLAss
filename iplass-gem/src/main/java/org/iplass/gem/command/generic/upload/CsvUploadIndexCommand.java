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

package org.iplass.gem.command.generic.upload;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.detail.DetailCommandBase;
import org.iplass.gem.command.generic.detail.DetailCommandContext;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.definition.EntityDefinition;

/**
 * CSVアップロード画面表示用コマンド
 * @author lis3wg
 */
@ActionMapping(
		name=CsvUploadIndexCommand.ACTION_NAME,
		displayName="CSVアップロード表示",
		paramMapping={
			@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
			@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
			@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2"),
		},
		result=@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_CSV_UPLOAD,
						templateName="gem/generic/upload/csvUpload",
						layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
	)
@CommandClass(name="gem/generic/upload/CsvUploadIndexCommand", displayName="CSVアップロード表示")
public final class CsvUploadIndexCommand extends DetailCommandBase {

	public static final String ACTION_NAME = "gem/generic/upload/index";

	@Override
	public String execute(RequestContext request) {
		DetailCommandContext context = getContext(request);
		EntityDefinition ed = context.getEntityDefinition();

		request.setAttribute("entityDefinition", ed);
		request.setAttribute("detailFormView", context.getView());

		request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());

		request.setAttribute("requiredProperties", CsvUploadUtil.getRequiredProperties(ed));

		return Constants.CMD_EXEC_SUCCESS;
	}
}
