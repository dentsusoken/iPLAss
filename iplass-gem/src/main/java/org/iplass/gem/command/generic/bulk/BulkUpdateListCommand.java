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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.ResultType;
import org.iplass.mtp.ManagerLocator;
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
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.iplass.mtp.transaction.TransactionManager;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.element.property.PropertyColumn;

@ActionMappings({
	@ActionMapping(name=BulkUpdateListCommand.BULK_UPDATE_ACTION_NAME,
			displayName="更新",
			paramMapping={
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
				@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2")
			},
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_BULK_EDIT,
						templateName="gem/generic/bulk/bulkEdit",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_BULK_EDIT,
						templateName="gem/generic/bulk/bulkEdit",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_TOKEN, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
			},
			tokenCheck=@TokenCheck
	)
})
@CommandClass(name = "gem/generic/bulk/BulkUpdateListCommand", displayName = "一括更新")
public class BulkUpdateListCommand extends BulkCommandBase {

	public static final String BULK_UPDATE_ACTION_NAME = "gem/generic/bulk/bulkUpdate";

	/**
	 * コンストラクタ
	 */
	public BulkUpdateListCommand() {
		super();
	}

	@Override
	public String execute(RequestContext request) {
		final BulkCommandContext context = getContext(request);
		// 必要なパラメータ取得
		Set<String> oids = context.getOids();
		SearchFormView view = context.getView();

		if (view == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.bulk.BulkUpdateViewCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		if (context.getProperty().size() == 0) {
			// 一括更新するプロパティ定義が一件もない場合、プロパティの一括更新が無効になっています。
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.bulk.BulkUpdateViewCommand.canNotUpdateProp"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		EditResult ret = null;
		BulkUpdateFormViewData data = new BulkUpdateFormViewData(context);
		data.setUpdatedProperties(context.getUpdatedProps());
		data.setExecType(Constants.EXEC_TYPE_UPDATE);
		data.setView(context.getView());
		for (String oid : oids) {
			for (Long version : context.getVersions(oid)) {
				Entity model = context.createEntity(oid, version);
				Integer row = context.getRow(oid, version);
				if (context.hasErrors()) {
					if (ret == null) {
						ret = new EditResult();
						ret.setResultType(ResultType.ERROR);
						ret.setErrors(context.getErrors().toArray(new ValidateError[context.getErrors().size()]));
						ret.setMessage(resourceString("command.generic.bulk.BulkUpdateListCommand.inputErr"));
					}
					data.setEntity(row, model);
				} else {
					// 更新
					if (ret == null || ret.getResultType() == ResultType.SUCCESS) ret = updateEntity(context, model);
					if (ret.getResultType() == ResultType.SUCCESS) {
						Transaction transaction = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
						transaction.addTransactionListener(new TransactionListener() {
							@Override
							public void afterCommit(Transaction t) {
								// 特定のバージョン指定でロード
								data.setEntity(row, loadViewEntity(context, oid, version, context.getDefinitionName(), (List<String>) null));
							}

							@Override
							public void afterRollback(Transaction t) {
								data.setEntity(row, model);
							}
						});
					} else {
						data.setEntity(row, model);
					}
				}
			}
		}

		String retKey = Constants.CMD_EXEC_SUCCESS;
		if (ret.getResultType() == ResultType.SUCCESS) {
			//更新されたプロパティリストに登録
			List<PropertyColumn> updatedProps = context.getProperty();
			// 組み合わせで使うプロパティ
			if (updatedProps.size() > 1) {
				Map<String, Object> updatedPropsMap = new LinkedHashMap<>();
				updatedProps.stream().forEach(pc -> {
					updatedPropsMap.put(pc.getPropertyName(), context.getBulkUpdatePropertyValue(pc.getPropertyName()));
				});
				data.addUpdatedProperty(context.getBulkUpdatePropName(), updatedPropsMap);
			} else {
				String updatedPropName = context.getBulkUpdatePropName();
				Object updatedPropValue = context.getBulkUpdatePropertyValue(updatedPropName);
				data.addUpdatedProperty(updatedPropName, updatedPropValue);
			}
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.bulk.BulkUpdateListCommand.successMsg"));
		} else if (ret.getResultType() == ResultType.ERROR) {
			retKey = Constants.CMD_EXEC_ERROR;
			List<ValidateError> tmpList = new ArrayList<ValidateError>();
			if (ret.getErrors() != null) {
				tmpList.addAll(Arrays.asList(ret.getErrors()));
			}
			ValidateError[] errors = tmpList.toArray(new ValidateError[tmpList.size()]);
			request.setAttribute(Constants.ERROR_PROP, errors);
			// 一括更新に失敗した場合、更新に失敗したプロパティ名をセットする
			request.setAttribute(Constants.BULK_UPDATE_PROP_NM, context.getBulkUpdatePropName());
			request.setAttribute(Constants.MESSAGE, ret.getMessage());
		}

		request.setAttribute(Constants.DATA, data);
		request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());
		request.setAttribute(Constants.BULK_UPDATE_SELECT_TYPE, context.getSelectAllType());

		return retKey;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
