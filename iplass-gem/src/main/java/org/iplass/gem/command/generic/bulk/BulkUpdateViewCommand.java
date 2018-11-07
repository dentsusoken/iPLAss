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
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_BULK_EDIT,
						templateName="gem/generic/bulk/bulkEdit",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_NODATA, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
			}
		)
})
@CommandClass(name = "gem/generic/detail/BulkDetailViewCommand", displayName = "一括詳細表示")
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
		String searchCond = context.getSearchCond();

		// 各種定義取得
		SearchFormView view = context.getView();
		if (view == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.detail.DetailViewCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		BulkUpdateFormViewData data = new BulkUpdateFormViewData(context);
		data.setExecType(Constants.EXEC_TYPE_UPDATE);

		StringBuilder builder = new StringBuilder();
		for (String oid : oids) {
			Integer targetRow = context.getRow(oid);
			Long targetVersion = context.getVersion(oid);
			if (oid != null && oid.length() > 0) {
				Entity entity = loadViewEntity(context, oid, targetVersion, context.getDefinitionName(), (List<String>) null);
				if (entity == null) {
					builder.append(resourceString("command.generic.detail.DetailViewCommand.noPermission", targetRow));
					break;
				}
				data.setEntity(targetRow, entity);
			}
		}
		if (builder.length() > 0) {
			request.setAttribute(Constants.MESSAGE, builder.toString());
			return Constants.CMD_EXEC_ERROR_NODATA;
		}

		request.setAttribute(Constants.DATA, data);
		request.setAttribute(Constants.SEARCH_COND, searchCond);
		return Constants.CMD_EXEC_SUCCESS;
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
