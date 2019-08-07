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

package org.iplass.gem.command.generic.detail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.iplass.mtp.view.generic.DetailFormView;

/**
 * Entity追加用コマンド
 * @author lis3wg
 */
@ActionMappings({
	@ActionMapping(name=InsertCommand.INSERT_ACTION_NAME,
		displayName="追加",
		paramMapping={
			@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
			@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
			@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2"),
		},
		result={
			@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
					value=Constants.CMD_RSLT_JSP_VIEW,
					templateName="gem/generic/detail/view",
					layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
			@Result(status=Constants.CMD_EXEC_ERROR, type=Type.TEMPLATE, value=Constants.TEMPLATE_EDIT),
			@Result(status=Constants.CMD_EXEC_ERROR_TOKEN, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
					layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
			@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
					layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
		},
		tokenCheck=@TokenCheck
	),
	@ActionMapping(name=InsertCommand.REF_INSERT_ACTION_NAME,
		displayName="参照追加",
		paramMapping={
			@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
			@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
			@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2"),
		},
		result={
			@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
					value=Constants.CMD_RSLT_JSP_COMPLETED,
					templateName="gem/generic/detail/ref/completed"),
			@Result(status=Constants.CMD_EXEC_ERROR, type=Type.TEMPLATE, value=Constants.TEMPLATE_REF_EDIT),
			@Result(status=Constants.CMD_EXEC_ERROR_TOKEN, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
					layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
			@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.TEMPLATE, value=Constants.TEMPLATE_COMMON_ERROR,
					layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
		},
		tokenCheck=@TokenCheck
	)
})
@CommandClass(name="gem/generic/detail/InsertCommand", displayName="追加")
public final class InsertCommand extends DetailCommandBase {

	public static final String INSERT_ACTION_NAME = "gem/generic/detail/insert";

	public static final String REF_INSERT_ACTION_NAME = "gem/generic/detail/ref/insert";

	/**
	 * コンストラクタ
	 */
	public InsertCommand() {
		super();
	}

	@Override
	public String execute(RequestContext request) {
		final DetailCommandContext context = getContext(request);

		DetailFormView view = context.getView();
		if (view == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.detail.InsertCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		final Entity model = context.createEntity();
		final DetailFormViewData data = new DetailFormViewData();
		data.setEntityDefinition(context.getEntityDefinition());
		data.setView(context.getView());
		EditResult ret = null;
		if (context.hasErrors()) {
			data.setEntity(model);
			ret = new EditResult();
			ret.setResultType(ResultType.ERROR);
			ret.setErrors(context.getErrors().toArray(new ValidateError[context.getErrors().size()]));
			ret.setMessage(resourceString("command.generic.detail.InsertCommand.inputErr"));
		} else {
			ret = insertEntity(context, model);
			if (ret.getResultType() == ResultType.SUCCESS) {
				//entityのoidをセット。transactionコミット前にoidを取得したい場合があるので。
				request.setAttribute(Constants.OID, model.getOid());

				Transaction transaction = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
				transaction.addTransactionListener(new TransactionListener() {
					@Override
					public void afterCommit(Transaction t) {
						//被参照をテーブルで追加した場合、コミット前だとロードで取得できない
						data.setEntity(loadViewEntity(context, model.getOid(), 0l, model.getDefinitionName(), context.getReferencePropertyName()));

						//UserPropertyEditor用のマップ作製
						if (data.getEntity() != null) {
							//更新成功時
							setUserInfoMap(context, data.getEntity(), false);
						}
					}
				});
			} else {
				data.setEntity(model);
			}
		}

		String retKey = Constants.CMD_EXEC_SUCCESS;
		if (ret.getResultType() == ResultType.ERROR) {
			//失敗時は新規に戻す
			data.setExecType(Constants.EXEC_TYPE_INSERT);
			retKey = Constants.CMD_EXEC_ERROR;
			List<ValidateError> tmpList = new ArrayList<ValidateError>();
			if (ret.getErrors() != null) {
				tmpList.addAll(Arrays.asList(ret.getErrors()));
			}
			ValidateError[] errors = tmpList.toArray(new ValidateError[tmpList.size()]);
			request.setAttribute(Constants.ERROR_PROP, errors);
			request.setAttribute(Constants.MESSAGE, ret.getMessage());
		} else {
			data.setExecType(Constants.EXEC_TYPE_UPDATE);
		}

		//UserPropertyEditor用のマップ作製
		if (data.getEntity() != null) {
			//更新失敗時
			setUserInfoMap(context, data.getEntity(), true);
		}

		request.setAttribute(Constants.DATA, data);
		request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());

		return retKey;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
