/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.generic.ResultType;
import org.iplass.mtp.ApplicationException;
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
import org.iplass.mtp.view.generic.BulkFormView;
import org.iplass.mtp.view.generic.BulkOperationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMappings({
	@ActionMapping(name=MultiBulkUpdateListCommand.BULK_UPDATE_ACTION_NAME,
			displayName="更新",
			paramMapping={
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
				@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2")
			},
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_BULK_MULTI_EDIT),
				@Result(status=Constants.CMD_EXEC_ERROR, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_BULK_MULTI_EDIT),
				@Result(status=Constants.CMD_EXEC_ERROR_TOKEN, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_COMMON_ERROR,
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE,
						value=Constants.TEMPLATE_COMMON_ERROR,
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
			},
			tokenCheck=@TokenCheck
	)
})
@CommandClass(name = "gem/generic/bulk/MultiBulkUpdateListCommand", displayName = "一括更新")
public class MultiBulkUpdateListCommand extends MultiBulkCommandBase {

	private static Logger logger = LoggerFactory.getLogger(MultiBulkUpdateListCommand.class);

	public static final String BULK_UPDATE_ACTION_NAME = "gem/generic/bulk/update";

	@Override
	protected Logger getLogger() {
		return logger;
	}

	/**
	 * コンストラクタ
	 */
	public MultiBulkUpdateListCommand() {
		super();
	}

	@Override
	public String execute(RequestContext request) {
		final MultiBulkCommandContext context = getContext(request);
		final boolean isSearchCondUpdate = isSearchCondUpdate(request);
		// 必要なパラメータ取得
		BulkFormView view = context.getView();

		if (view == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.bulk.BulkUpdateViewCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		if (context.getProperty().size() == 0) {
			// 一括更新するプロパティ定義が一件もない場合、プロパティの一括更新が無効になっています。
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.bulk.BulkUpdateViewCommand.canNotUpdateProp"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		EditResult ret = new EditResult();
		MultiBulkUpdateFormViewData data = new MultiBulkUpdateFormViewData(context);
		data.setView(context.getView());


		try {
			List<Entity> entities = context.getEntities();
			//一括更新する前の処理を呼び出します。
			List<ValidateError> errors = new ArrayList<ValidateError>();
			if (!isSearchCondUpdate) {
				BulkOperationContext bulkContext = context.getBulkUpdateInterrupterHandler().beforeOperation(entities);
				errors.addAll(bulkContext.getErrors());
				entities = bulkContext.getEntities();
			}
	
			if (!errors.isEmpty()) {
				ret.setResultType(ResultType.ERROR);
				ret.setErrors(errors.toArray(new ValidateError[errors.size()]));
				ret.setMessage(resourceString("command.generic.bulk.BulkUpdateListCommand.inputErr"));
			} else if (entities.size() > 0) {
				for (Entity entity : entities) {
					String oid = entity.getOid();
					Long version = entity.getVersion();
					Entity model = context.createEntity(oid, version);
					Integer row = context.getRow(oid, version);
					if (context.hasErrors()) {
						if (ret.getResultType() == null) {
							ret.setResultType(ResultType.ERROR);
							ret.setErrors(context.getErrors().toArray(new ValidateError[context.getErrors().size()]));
							ret.setMessage(resourceString("command.generic.bulk.BulkUpdateListCommand.inputErr"));
						}
						data.setEntity(row, model);
					} else {
						// 更新
						if (ret.getResultType() == null || ret.getResultType() == ResultType.SUCCESS) ret = updateEntity(context, model);
						if (ret.getResultType() == ResultType.SUCCESS) {
							Transaction transaction = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
							transaction.addTransactionListener(new TransactionListener() {
								@Override
								public void afterCommit(Transaction t) {
									// 検索条件で更新ではなければ、特定のバージョン指定でロード
									if (!isSearchCondUpdate) {
										data.setEntity(row, loadViewEntity(context, oid, version, context.getDefinitionName(), (List<String>) null));
									} else {
										data.setEntity(row, model);
									}
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
	
			//更新した後の処理を呼び出します。
			if (!isSearchCondUpdate) {
				context.getBulkUpdateInterrupterHandler().afterOperation(entities);
			}
		} catch (ApplicationException e) {
			if (getLogger().isDebugEnabled()) {
				getLogger().debug(e.getMessage(), e);
			}

			ret.setResultType(ResultType.ERROR);
			ret.setMessage(e.getMessage());
		}

		String retKey = Constants.CMD_EXEC_SUCCESS;
		if (ret.getResultType() == ResultType.SUCCESS) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.bulk.BulkUpdateListCommand.successMsg"));
		} else if (ret.getResultType() == ResultType.ERROR) {
			retKey = Constants.CMD_EXEC_ERROR;
			List<ValidateError> tmpList = new ArrayList<ValidateError>();
			if (ret.getErrors() != null) {
				tmpList.addAll(Arrays.asList(ret.getErrors()));
			}
			ValidateError[] _error = tmpList.toArray(new ValidateError[tmpList.size()]);
			request.setAttribute(Constants.ERROR_PROP, _error);
			request.setAttribute(Constants.MESSAGE, ret.getMessage());
		}

		request.setAttribute(Constants.DATA, data);
		request.setAttribute(Constants.ENTITY_DATA, context.createEntity());
		request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());
		request.setAttribute(Constants.BULK_UPDATE_SELECT_ALL_PAGE, context.getSelectAllPage());
		request.setAttribute(Constants.BULK_UPDATE_SELECT_TYPE, context.getSelectAllType());

		return retKey;
	}

	/**
	 * 検索条件で更新されたかどうか
	 * @return 検索条件で更新されるかどうか
	 */
	public boolean isSearchCondUpdate(RequestContext request) {
		return request.getAttribute(Constants.OID) != null
				&& request.getAttribute(Constants.VERSION) != null
				&& request.getAttribute(Constants.TIMESTAMP) != null;
	}
}
