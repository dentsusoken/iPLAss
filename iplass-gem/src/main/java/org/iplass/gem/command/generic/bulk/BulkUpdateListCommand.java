package org.iplass.gem.command.generic.bulk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
@CommandClass(name = "gem/generic/detail/BulkUpdateListCommand", displayName = "一括更新")
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
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.detail.DetailViewCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}
		//　一括更新プロパティエディターが設定されているかをチェックする
		Optional<PropertyColumn> optional = view.getResultSection().getElements().stream()
				.filter(e -> e instanceof PropertyColumn)
				.map(e -> (PropertyColumn) e)
				.filter(e -> e.getBulkUpdateEditor() != null)
				.findFirst();
		if(!optional.isPresent()) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.detail.DetailViewCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		EditResult ret = null;
		BulkUpdateFormViewData data = new BulkUpdateFormViewData(context);
		data.setUpdatedProperties(context.getUpdatedProps());
		data.setExecType(Constants.EXEC_TYPE_UPDATE);
		data.setView(context.getView());
		for (String oid : oids) {
			Entity model = context.createEntity(oid);
			Integer row = context.getRow(oid);
			if (context.hasErrors()) {
				if (ret == null) {
					ret = new EditResult();
					ret.setResultType(ResultType.ERROR);
					ret.setErrors(context.getErrors().toArray(new ValidateError[context.getErrors().size()]));
					ret.setMessage(resourceString("command.generic.detail.UpdateCommand.inputErr"));
				}
				data.setEntity(row, model);
			} else {
				// 更新
				ret = updateEntity(context, model);
				if (ret.getResultType() == ResultType.SUCCESS) {
					Transaction transaction = ManagerLocator.getInstance().getManager(TransactionManager.class).currentTransaction();
					transaction.addTransactionListener(new TransactionListener() {
						@Override
						public void afterCommit(Transaction t) {
							// 被参照をテーブルで追加した場合、コミット前だとロードで取得できない
							if (context.isVersioned() && !context.isNewVersion()) {
								// 特定バージョンの場合だけバージョン指定でロード
								Long version = context.getVersion(oid);
								data.setEntity(row, loadViewEntity(context, oid, version, context.getDefinitionName(), context.getReferencePropertyName()));
							} else {
								data.setEntity(row, loadViewEntity(context, oid, null, context.getDefinitionName(), context.getReferencePropertyName()));
							}
						}
					});
				} else {
					data.setEntity(row, model);
				}
			}
		}

		String retKey = Constants.CMD_EXEC_SUCCESS;
		if (ret.getResultType() == ResultType.SUCCESS) {
			data.addUpdatedProperty(context.getBulkUpdatePropName(), context.getBulkUpdatePropDispValue());
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.detail.BulkUpdateCommand.successMsg"));
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
