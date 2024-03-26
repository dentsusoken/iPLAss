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

package org.iplass.gem.command.generic.upload;

import java.util.Map;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.gem.command.generic.detail.DetailCommandBase;
import org.iplass.gem.command.generic.detail.DetailCommandContext;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.utilityclass.definition.UtilityClassDefinitionManager;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormCsvUploadInterrupter;
import org.iplass.mtp.view.generic.SearchFormView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		result=@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.TEMPLATE, value=Constants.TEMPLATE_CSV_UPLOAD)
)
@CommandClass(name="gem/generic/upload/CsvUploadIndexCommand", displayName="CSVアップロード表示")
@Template(
		name=Constants.TEMPLATE_CSV_UPLOAD,
		path=Constants.CMD_RSLT_JSP_CSV_UPLOAD,
		layoutActionName=Constants.LAYOUT_NORMAL_ACTION
)
public final class CsvUploadIndexCommand extends DetailCommandBase {

	private static Logger logger = LoggerFactory.getLogger(CsvUploadIndexCommand.class);

	public static final String ACTION_NAME = "gem/generic/upload/index";

	@Override
	public String execute(RequestContext request) {
		DetailCommandContext context = getContext(request);
		EntityDefinition ed = context.getEntityDefinition();

		EntityView view = context.getEntityView();
		SearchFormView searchFormView = FormViewUtil.getSearchFormView(ed, view, context.getViewName());

		request.setAttribute(Constants.ENTITY_DEFINITION, ed);
		request.setAttribute("detailFormView", context.getView());
		request.setAttribute("searchFormView", searchFormView);
		request.setAttribute(Constants.SEARCH_COND, context.getSearchCond());
		request.setAttribute("requiredProperties", CsvUploadUtil.getRequiredProperties(ed));
		request.setAttribute("customColumnNameMap", getCustomColumnNameMap(context, searchFormView));

		return Constants.CMD_EXEC_SUCCESS;
	}

	private Map<String, String> getCustomColumnNameMap(DetailCommandContext context, SearchFormView searchFormView) {

		if (searchFormView != null) {
			if (StringUtil.isNotEmpty(searchFormView.getCondSection().getCsvUploadInterrupterName())) {
				UtilityClassDefinitionManager ucdm = ManagerLocator.getInstance().getManager(UtilityClassDefinitionManager.class);
				SearchFormCsvUploadInterrupter interrupter = null;
				try {
					interrupter = ucdm.createInstanceAs(SearchFormCsvUploadInterrupter.class, searchFormView.getCondSection().getCsvUploadInterrupterName());
					return interrupter.columnNameMap(context.getEntityDefinition());
				} catch (ClassNotFoundException e) {
					logger.error(searchFormView.getCondSection().getCsvUploadInterrupterName() + " can not instantiate.", e);
					throw new ApplicationException(GemResourceBundleUtil.resourceString("command.generic.upload.CsvUploadIndexCommand.internalErr"));
				}

			}
		}

		return null;
	}

}
