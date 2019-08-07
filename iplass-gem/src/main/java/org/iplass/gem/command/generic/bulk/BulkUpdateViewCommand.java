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

package org.iplass.gem.command.generic.bulk;

import java.util.List;
import java.util.Set;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.CommandConfig;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.view.generic.SearchFormView;

@ActionMappings({
	@ActionMapping(name=BulkUpdateViewCommand.BULK_EDIT_ACTION_NAME,
			displayName="一括詳細編集",
			paramMapping={
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
				@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2"),
			},
			command=@CommandConfig(commandClass=BulkUpdateViewCommand.class, value="cmd.detail=true;"),
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_BULK_EDIT),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_COMMON_ERROR,
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_NODATA, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_COMMON_ERROR,
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
			}
		)
})
@CommandClass(name = "gem/generic/bulk/BulkDetailViewCommand", displayName = "一括詳細表示")
@Template(
		name=Constants.TEMPLATE_BULK_EDIT,
		path=Constants.CMD_RSLT_JSP_BULK_EDIT,
		layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
public class BulkUpdateViewCommand extends BulkCommandBase {

	public static final String BULK_EDIT_ACTION_NAME = "gem/generic/bulk/bulkEdit";

	private boolean detail;

	public boolean isDetail() {
		return detail;
	}

	public void setDetail(boolean detail) {
		this.detail = detail;
	}

	/**
	 * コンストラクタ
	 */
	public BulkUpdateViewCommand() {
		super();
	}

	@Override
	public String execute(RequestContext request) {
		BulkCommandContext context = getContext(request);

		// 必要なパラメータ取得
		Set<String> oids = context.getOids();

		// 各種定義取得
		SearchFormView view = context.getView();
		if (view == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.bulk.BulkUpdateViewCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		BulkUpdateFormViewData data = new BulkUpdateFormViewData(context);
		data.setExecType(Constants.EXEC_TYPE_UPDATE);

		for (String oid : oids) {
			if (oid != null && oid.length() > 0) {
				for (Long targetVersion : context.getVersions(oid)) {
					Integer targetRow = context.getRow(oid, targetVersion);
					Entity entity = loadViewEntity(context, oid, targetVersion, context.getDefinitionName(), (List<String>) null);
					if (entity == null) {
						request.setAttribute(Constants.MESSAGE, resourceString("command.generic.bulk.BulkUpdateViewCommand.noPermission"));
						return Constants.CMD_EXEC_ERROR_NODATA;
					}
					data.setEntity(targetRow, entity);
				}
			}
		}

		request.setAttribute(Constants.DATA, data);
		request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());
		request.setAttribute(Constants.BULK_UPDATE_SELECT_ALL_PAGE, context.getSelectAllPage());
		return Constants.CMD_EXEC_SUCCESS;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
