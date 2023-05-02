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

package org.iplass.gem.command.generic.delete;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.ResultType;
import org.iplass.gem.command.generic.detail.DetailViewCommand;
import org.iplass.gem.command.generic.search.SearchViewCommand;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.action.TokenCheck;
import org.iplass.mtp.entity.DeleteTargetVersion;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.DetailFormView;

/**
 * Entity削除コマンド
 * @author lis3wg
 */
@ActionMapping(
	name=DeleteCommand.ACTION_NAME,
	displayName="削除",
	paramMapping={
		@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
		@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
		@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2")
	},
	result={
		@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.TEMPLATE, value=Constants.TEMPLATE_SEARCH),
		@Result(status=Constants.CMD_EXEC_ERROR, type=Type.TEMPLATE, value=Constants.TEMPLATE_EDIT),
		@Result(status=Constants.CMD_EXEC_ERROR_NODATA,type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
				layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
		@Result(status=Constants.CMD_EXEC_SUCCESS_BACK_PATH, type=Type.JSP,
				value=Constants.CMD_RSLT_JSP_BACK_PATH,
				templateName="gem/generic/backPath"),
	},
	tokenCheck=@TokenCheck
)
@CommandClass(name="gem/generic/delete/DeleteCommand", displayName="削除")
public final class DeleteCommand extends DeleteCommandBase {

	public static final String ACTION_NAME = "gem/generic/delete/delete";

	/** バージョン指定削除KEY */
	public static final String DELETE_SPECIFIC_VERSION = "deleteSpecificVersion";

	/** 詳細画面表示用コマンド */
	private DetailViewCommand detail;

	/** 検索用コマンド */
	private SearchViewCommand search;

	/**
	 * コンストラクタ
	 */
	public DeleteCommand() {
		detail = new DetailViewCommand();
		detail.setDetail(true);
		search = new SearchViewCommand();
	}

	@Override
	public String execute(RequestContext request) {

		DeleteCommandContext context = getContext(request);

		DetailFormView form = context.getDetailView();

		String defName = request.getParam(Constants.DEF_NAME);
		String oid = request.getParam(Constants.OID);
		String version = request.getParam(Constants.VERSION);
		String backPath = request.getParam(Constants.BACK_PATH);
		String searchCond = request.getParam(Constants.SEARCH_COND);
		String topViewListOffset = request.getParam(Constants.TOPVIEW_LIST_OFFSET);
		String deleteSpecificVersion = request.getParam(DELETE_SPECIFIC_VERSION);

		if (StringUtil.isNotEmpty(oid)) {
			Long targetVersion = null;
			DeleteTargetVersion deleteTargetVersion = DeleteTargetVersion.ALL;
			if (context.getEntityDefinition().getVersionControlType() != VersionControlType.NONE
					&& Boolean.valueOf(deleteSpecificVersion)) {
				//バージョン指定削除
				targetVersion = Long.parseLong(version);
				deleteTargetVersion = DeleteTargetVersion.SPECIFIC;
			}
			Entity entity = loadEntity(defName, oid, targetVersion);

			if (entity == null 
					|| !evm.hasEntityReferencePermissionDetailFormView(context.getDefinitionName(), context.getViewName(), entity)) {
				request.setAttribute(Constants.MESSAGE, GemResourceBundleUtil.resourceString("command.generic.detail.DetailViewCommand.noPermission"));
				return Constants.CMD_EXEC_ERROR_NODATA;
			}

			if (entity != null) {
				DeleteResult ret = deleteEntity(entity, form.isPurge(), deleteTargetVersion);
				if (ret.getResultType() == ResultType.ERROR) {
					//削除でエラーが出てたら終了
					request.setAttribute(Constants.MESSAGE, ret.getMessage());
					ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction().rollback();
					detail.execute(request);
					return Constants.CMD_EXEC_ERROR;
				}
			}
		}

		if (StringUtil.isEmpty(backPath)) {
			//削除後は一覧画面へ
			search.execute(request);
			return Constants.CMD_EXEC_SUCCESS;
		} else {
			request.setAttribute(Constants.DEF_NAME, defName);
			request.setAttribute(Constants.SEARCH_COND, searchCond);
			request.setAttribute(Constants.BACK_PATH, backPath);
			if (StringUtil.isNotEmpty(topViewListOffset)) {
				request.setAttribute(Constants.TOPVIEW_LIST_OFFSET, topViewListOffset);
			}
			return Constants.CMD_EXEC_SUCCESS_BACK_PATH;
		}
	}

}
