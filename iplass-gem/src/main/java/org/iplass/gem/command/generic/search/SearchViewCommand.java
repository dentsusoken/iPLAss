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

package org.iplass.gem.command.generic.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.ParamMapping;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.filter.EntityFilterManager;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.view.generic.FormViewUtil;
import org.iplass.mtp.view.generic.SearchFormView;
import org.iplass.mtp.view.generic.editor.NestProperty;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor;
import org.iplass.mtp.view.generic.element.property.PropertyItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ActionMappings({
	@ActionMapping(
			name=SearchViewCommand.SEARCH_ACTION_NAME,
			displayName="検索画面表示",
			paramMapping={
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
				@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2")
			},
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_SEARCH,
						templateName="gem/generic/search/search",
						layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
			}
	),
	@ActionMapping(
			name=SearchViewCommand.SELECT_ACTION_NAME,
			displayName="選択画面表示",
			paramMapping={
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${0}", condition="subPath.length==1"),
				@ParamMapping(name=Constants.VIEW_NAME, mapFrom="${0}", condition="subPath.length==2"),
				@ParamMapping(name=Constants.DEF_NAME, mapFrom="${1}", condition="subPath.length==2")
			},
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_REF_SEARCH,
						templateName="gem/generic/search/select",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_POPOUT_ACTION)
			}
	)
})
@CommandClass(name="gem/generic/search/SearchviewCommand", displayName="検索画面表示")
public final class SearchViewCommand implements Command {
	private static Logger logger = LoggerFactory.getLogger(SearchViewCommand.class);

	public static final String SEARCH_ACTION_NAME = "gem/generic/search/view";
	public static final String SELECT_ACTION_NAME = "gem/generic/search/ref/view";

	@Override
	public String execute(RequestContext request) {
		String defName = request.getParam(Constants.DEF_NAME);

		EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		EntityViewManager evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		EntityFilterManager efm = ManagerLocator.getInstance().getManager(EntityFilterManager.class);

		EntityDefinition entityDefinition = edm.get(defName);
		EntityView entityView = evm.get(defName);
		EntityFilter entityFilter = efm.get(defName);

		String viewName = request.getParam(Constants.VIEW_NAME);
		SearchFormView view = FormViewUtil.getSearchFormView(entityDefinition, entityView, viewName);

		if (view == null) {
			request.setAttribute(Constants.MESSAGE, resourceString("command.generic.search.SearchViewCommand.viewErr"));
			return Constants.CMD_EXEC_ERROR_VIEW;
		}

		SearchFormViewData data = new SearchFormViewData();
		data.setEntityDefinition(entityDefinition);
		data.setView(view);
		if (entityFilter != null) {
			data.setFilters(entityFilter.getItems());
		}

		//デフォルト検索条件の生成(searchCondのあるなしにかかわらず作成。リセット用)

		//画面定義の検索条件の項目名でリクエストパラメータがあればsearchCondにする
		//親とネストの条件が同時に指定される可能性があるため、EntityではなくMapに格納
		//Entity defaultSearchCond = new GenericEntity(defName);
		Map<String, Object> defaultSearchCond = new HashMap<String, Object>();
		applyCommonParam(request, defaultSearchCond);
		List<PropertyItem> properties = view.getCondSection().getElements().stream()
				.filter(e -> e instanceof PropertyItem).map(e -> (PropertyItem) e).collect(Collectors.toList());
		applyNormalSearchCond(request, defaultSearchCond, properties);
		applyDetailSearchCond(request, defaultSearchCond, view.getCondSection().getConditionDispCount());
		applyFixedSearchCond(request, defaultSearchCond);

		//デフォルトプロパティ条件を反映(GroovyScriptでのカスタマイズ)
		defaultSearchCond = evm.applyDefaultPropertyCondition(defName, viewName, defaultSearchCond);
		request.setAttribute(Constants.DEFAULT_SEARCH_COND, defaultSearchCond);

		request.setAttribute(Constants.DATA, data);
		return Constants.CMD_EXEC_SUCCESS;
	}

	private void applyCommonParam(RequestContext request, Map<String, Object> defaultSearchCond) {
		String searchType = request.getParam(Constants.SEARCH_TYPE);
		if (StringUtil.isNotBlank(searchType)) {
			defaultSearchCond.put(Constants.SEARCH_TYPE, searchType);
		}
		String es = request.getParam(Constants.EXECUTE_SEARCH);
		if (StringUtil.isNotBlank(es) && Constants.EXECUTE_SEARCH_VALUE.equals(es)) {
			defaultSearchCond.put(Constants.EXECUTE_SEARCH, Constants.EXECUTE_SEARCH_VALUE);
		}
	}

	private void applyNormalSearchCond(RequestContext request, Map<String, Object> defaultSearchCond, List<PropertyItem> properties) {
		for (PropertyItem property : properties) {
			if (property.isBlank()) continue;

			String[] val = request.getParams(Constants.SEARCH_COND_PREFIX + property.getPropertyName());
			if (val != null && val.length > 0) {
				//一律文字列で保持、変換は各JSPで
				defaultSearchCond.put(property.getPropertyName(), val);
			}

			if (property.getEditor() instanceof ReferencePropertyEditor) {
				//ネストの項目があれば追加する
				ReferencePropertyEditor editor = (ReferencePropertyEditor) property.getEditor();
				if (!editor.getNestProperties().isEmpty()) {
					setNestCondition(request, defaultSearchCond, editor, property.getPropertyName());
				}
			}
		}
	}

	private void setNestCondition(RequestContext request, Map<String, Object> defaultSearchCond,
			ReferencePropertyEditor editor, String parent) {
		for (NestProperty nest : editor.getNestProperties()) {
			String name = parent + "." + nest.getPropertyName();
			String[] val = request.getParams(Constants.SEARCH_COND_PREFIX + name);
			if (val != null && val.length > 0) {
				defaultSearchCond.put(name, val);
			}
			if (nest.getEditor() instanceof ReferencePropertyEditor
					&& !((ReferencePropertyEditor) nest.getEditor()).getNestProperties().isEmpty()) {
				setNestCondition(request, defaultSearchCond, (ReferencePropertyEditor) nest.getEditor(), name);
			}
		}
	}

	private void applyDetailSearchCond(RequestContext request, Map<String, Object> defaultSearchCond, int condCount) {
		//件数が未指定の場合は設定ベースで
		Integer count = null;
		String dtlCondCount = request.getParam(Constants.DETAIL_COND_COUNT);
		if (StringUtil.isNotBlank(dtlCondCount)) {
			try {
				count = Integer.parseInt(dtlCondCount);
				defaultSearchCond.put(Constants.DETAIL_COND_COUNT, dtlCondCount);
			} catch (NumberFormatException e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
		}
		if (count == null) count = condCount;

		for (int i = 0; i < count; i++) {
			String nmKey = Constants.DETAIL_COND_PROP_NM + i;
			String nmValue = request.getParam(nmKey);
			if (StringUtil.isNotBlank(nmValue)) {
				defaultSearchCond.put(nmKey, nmValue);
			}

			String prdcKey = Constants.DETAIL_COND_PREDICATE + i;
			String prdcValue = request.getParam(prdcKey);
			if (StringUtil.isNotBlank(prdcValue)) {
				defaultSearchCond.put(prdcKey, prdcValue);
			}

			String vlKey = Constants.DETAIL_COND_VALUE + i;
			String vlValue = request.getParam(vlKey);
			if (StringUtil.isNotBlank(vlValue)) {
				defaultSearchCond.put(vlKey, vlValue);
			}
		}

		String expr = request.getParam(Constants.DETAIL_COND_EXPR);
		if (StringUtil.isNotBlank(expr)) {
			defaultSearchCond.put(Constants.DETAIL_COND_EXPR, expr);
		}
		String expression = request.getParam(Constants.DETAIL_COND_FILTER_EXPRESSION);
		if (StringUtil.isNotBlank(expression)) {
			defaultSearchCond.put(Constants.DETAIL_COND_FILTER_EXPRESSION, expression);
		}
	}

	private void applyFixedSearchCond(RequestContext request, Map<String, Object> defaultSearchCond) {
		String filterName = request.getParam(Constants.FILTER_NAME);
		if (StringUtil.isNotBlank(filterName)) {
			defaultSearchCond.put(Constants.FILTER_NAME, filterName);
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
