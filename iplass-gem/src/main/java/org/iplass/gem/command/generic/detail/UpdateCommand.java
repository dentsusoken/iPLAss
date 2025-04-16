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

package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.ResultType;
import org.iplass.gem.command.generic.detail.handler.ShowDetailViewEventHandler;
import org.iplass.gem.command.generic.detail.handler.ShowEditViewEventHandler;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.action.TokenCheck;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.ValidateError;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.generic.DetailFormView;

/**
 * Entity更新用コマンド
 * @author lis3wg
 */
@ActionMappings({
		@ActionMapping(
				name = UpdateCommand.UPDATE_ACTION_NAME,
				displayName = "更新",
				paramMapping = {
						@ParamMapping(name = Constants.DEF_NAME, mapFrom = "${0}", condition = "subPath.length==1"),
						@ParamMapping(name = Constants.VIEW_NAME, mapFrom = "${0}", condition = "subPath.length==2"),
						@ParamMapping(name = Constants.DEF_NAME, mapFrom = "${1}", condition = "subPath.length==2")
				},
				result = {
						@Result(status = Constants.CMD_EXEC_SUCCESS, type = Type.TEMPLATE, value = Constants.TEMPLATE_VIEW),
						@Result(status = Constants.CMD_EXEC_ERROR, type = Type.TEMPLATE, value = Constants.TEMPLATE_EDIT),
						@Result(
								status = Constants.CMD_EXEC_ERROR_TOKEN,
								type = Type.TEMPLATE,
								value = Constants.TEMPLATE_COMMON_ERROR,
								layoutActionName = Constants.LAYOUT_NORMAL_ACTION),
						@Result(
								status = Constants.CMD_EXEC_ERROR_VIEW,
								type = Type.TEMPLATE,
								value = Constants.TEMPLATE_COMMON_ERROR,
								layoutActionName = Constants.LAYOUT_NORMAL_ACTION),
						@Result(
								status = Constants.CMD_EXEC_ERROR_NODATA,
								type = Type.TEMPLATE,
								value = Constants.TEMPLATE_COMMON_ERROR,
								layoutActionName = Constants.LAYOUT_NORMAL_ACTION)
				},
				tokenCheck = @TokenCheck
		),
		@ActionMapping(
				name = UpdateCommand.REF_UPDATE_ACTION_NAME,
				displayName = "参照更新",
				paramMapping = {
						@ParamMapping(name = Constants.DEF_NAME, mapFrom = "${0}", condition = "subPath.length==1"),
						@ParamMapping(name = Constants.VIEW_NAME, mapFrom = "${0}", condition = "subPath.length==2"),
						@ParamMapping(name = Constants.DEF_NAME, mapFrom = "${1}", condition = "subPath.length==2")
				},
				result = {
						@Result(status = Constants.CMD_EXEC_SUCCESS, type = Type.TEMPLATE, value = Constants.TEMPLATE_COMPLETED),
						@Result(status = Constants.CMD_EXEC_ERROR, type = Type.TEMPLATE, value = Constants.TEMPLATE_REF_EDIT),
						@Result(
								status = Constants.CMD_EXEC_ERROR_TOKEN,
								type = Type.TEMPLATE,
								value = Constants.TEMPLATE_COMMON_ERROR,
								layoutActionName = Constants.LAYOUT_POPOUT_ACTION),
						@Result(
								status = Constants.CMD_EXEC_ERROR_VIEW,
								type = Type.TEMPLATE,
								value = Constants.TEMPLATE_COMMON_ERROR,
								layoutActionName = Constants.LAYOUT_POPOUT_ACTION),
						@Result(
								status = Constants.CMD_EXEC_ERROR_NODATA,
								type = Type.TEMPLATE,
								value = Constants.TEMPLATE_COMMON_ERROR,
								layoutActionName = Constants.LAYOUT_POPOUT_ACTION)
				},
				tokenCheck = @TokenCheck
		)
})
@CommandClass(name = "gem/generic/detail/UpdateCommand", displayName = "更新")
public final class UpdateCommand extends DetailCommandBase {

	public static final String UPDATE_ACTION_NAME = "gem/generic/detail/update";

	public static final String REF_UPDATE_ACTION_NAME = "gem/generic/detail/ref/update";

	/**
	 * コンストラクタ
	 */
	public UpdateCommand() {
		super();
	}

	@Override
	public String execute(RequestContext request) {
		final DetailCommandContext context = getContext(request);

		final String oid = context.getOid();
		final Long version = context.getVersion();

		//View定義のステータスチェック
		evm.checkState(context.getDefinitionName());

		DetailFormView view = context.getView();
		if (view == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.detail.UpdateCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		if (StringUtil.isNotEmpty(oid)) {
			Entity current = null;
			if (view.isLoadDefinedReferenceProperty()) {
				current = loadBeforeUpdateEntity(context, oid, version, context.getDefinitionName(), context.getReferencePropertyName(),
						context.isLoadVersioned());
			} else {
				current = loadBeforeUpdateEntity(context, oid, version, context.getDefinitionName(), (List<String>) null, context.isLoadVersioned());
			}
			if (current == null
					|| !evm.hasEntityReferencePermissionDetailFormView(context.getDefinitionName(), context.getViewName(), current)) {
				request.setAttribute(Constants.MESSAGE, resourceString("command.generic.detail.DetailViewCommand.noPermission"));
				return Constants.CMD_EXEC_ERROR_NODATA;
			}
			context.setCurrentEntity(current);
		}

		Entity edited = context.createEntity();
		final DetailFormViewData data = new DetailFormViewData();
		data.setEntityDefinition(context.getEntityDefinition());
		data.setView(context.getView());
		EditResult ret = null;
		if (context.hasErrors()) {
			context.rollbackEntity(edited);
			data.setEntity(edited);
			ret = new EditResult();
			ret.setResultType(ResultType.ERROR);
			ret.setErrors(context.getErrors().toArray(new ValidateError[context.getErrors().size()]));
			ret.setMessage(resourceString("command.generic.detail.UpdateCommand.inputErr"));
		} else {
			// 更新
			ret = updateEntity(context, edited);
			final Long updatedVersion = edited.getVersion();
			if (ret.getResultType() == ResultType.SUCCESS && oid != null) {
				Transaction transaction = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
				transaction.addTransactionListener(new TransactionListener() {
					@Override
					public void afterCommit(Transaction t) {
						//被参照をテーブルで追加した場合、コミット前だとロードで取得できない
						if (context.isVersioned()) {
							if (context.isNewVersion()) {
								//新しいバージョンで登録時はそのデータを表示
								data.setEntity(loadViewEntity(context, oid, updatedVersion, context.getDefinitionName(), context.getReferencePropertyName(),
										context.isLoadVersioned()));
							} else {
								//特定バージョンの場合だけバージョン指定でロード
								Long version = context.getVersion();
								data.setEntity(
										loadViewEntity(context, oid, version, context.getDefinitionName(), context.getReferencePropertyName(), context.isLoadVersioned()));
							}
						} else {
							data.setEntity(
									loadViewEntity(context, oid, null, context.getDefinitionName(), context.getReferencePropertyName(), context.isLoadVersioned()));
						}

						//更新成功時
						if (data.getEntity() != null) {
							//UserPropertyEditor用のマップ作製
							setUserInfoMap(context, data.getEntity(), false);

							//Handler実行
							if (data.getEntity().getOid() != null) {
								if (context instanceof ShowDetailViewEventHandler) {
									((ShowDetailViewEventHandler) context).fireShowDetailViewEvent(data);
								}
							}
						}
					}
				});
			} else {
				context.rollbackEntity(edited);
				data.setEntity(edited);
			}
		}

		data.setExecType(Constants.EXEC_TYPE_UPDATE);

		String retKey = Constants.CMD_EXEC_SUCCESS;
		if (ret.getResultType() == ResultType.ERROR) {
			retKey = Constants.CMD_EXEC_ERROR;
			List<ValidateError> tmpList = new ArrayList<>();
			if (ret.getErrors() != null) {
				tmpList.addAll(Arrays.asList(ret.getErrors()));
			}
			ValidateError[] errors = tmpList.toArray(new ValidateError[tmpList.size()]);
			request.setAttribute(Constants.ERROR_PROP, errors);
			request.setAttribute(Constants.MESSAGE, ret.getMessage());
		}

		//権限チェック
		AuthContext auth = AuthContext.getCurrentContext();
		data.setCanCreate(auth.checkPermission(new EntityPermission(context.getDefinitionName(), EntityPermission.Action.CREATE)));
		data.setCanUpdate(auth.checkPermission(new EntityPermission(context.getDefinitionName(), EntityPermission.Action.UPDATE)));
		data.setCanDelete(auth.checkPermission(new EntityPermission(context.getDefinitionName(), EntityPermission.Action.DELETE)));

		//更新失敗時
		if (data.getEntity() != null) {
			//UserPropertyEditor用のマップ作製
			setUserInfoMap(context, data.getEntity(), true);

			//Handler実行
			if (retKey == Constants.CMD_EXEC_SUCCESS) {
				if (context instanceof ShowDetailViewEventHandler) {
					((ShowDetailViewEventHandler) context).fireShowDetailViewEvent(data);
				}
			} else if (retKey == Constants.CMD_EXEC_ERROR) {
				if (context instanceof ShowEditViewEventHandler) {
					((ShowEditViewEventHandler) context).fireShowEditViewEvent(data);
				}
			}
		}

		request.setAttribute(Constants.DATA, data);
		request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());
		request.setAttribute(Constants.VERSION_SPECIFIED, context.isVersionSpecified());

		return retKey;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
