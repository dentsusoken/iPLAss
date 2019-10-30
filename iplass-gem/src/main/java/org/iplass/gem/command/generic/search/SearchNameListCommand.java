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

import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.view.filter.EntityFilterManager;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(
		name=SearchNameListCommand.WEBAPI_NAME,
		displayName="Entity一覧検索(ウィジェット)",
		accepts=RequestType.REST_JSON,
		methods=MethodType.POST,
		restJson=@RestJson(parameterName="param"),
		results={SearchNameListCommand.RESULT_PARAM_LIST, SearchNameListCommand.RESULT_PARAM_COUNT},
		checkXRequestedWithHeader=true
)
@Template(name="gem/generic/search/listWidget", displayName="検索結果ウィジェット", path="/jsp/gem/generic/search/listWidget.jsp")
@CommandClass(name="gem/generic/search/SearchNameListCommand", displayName="Entity一覧検索(ウィジェット)")
public final class SearchNameListCommand extends SearchListPartsCommandBase {

	public static final String WEBAPI_NAME = "gem/generic/search/nameList";

	public static final String RESULT_PARAM_LIST = "list";
	public static final String RESULT_PARAM_COUNT = "count";

	private EntityDefinitionManager edm;
	private EntityViewManager evm;
	private EntityFilterManager efm;

	public SearchNameListCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		efm = ManagerLocator.getInstance().getManager(EntityFilterManager.class);
	}

	@Override
	public String execute(RequestContext request) {

		SearchNameListContext context = new SearchNameListContext();
		context.setRequest(request);
		context.setEntityDefinition(edm.get(context.getDefName()));
		context.setEntityView(evm.get(context.getDefName()));
		context.setFilter(efm.get(context.getDefName()));

		Query query = toQuery(context);

		int count = count(context, query.copy());
		request.setAttribute(RESULT_PARAM_COUNT, count);

		query.setOrderBy(context.getOrderBy());
		query.setLimit(context.getLimit());

		List<Entity> entity = search(context, query).getList();
		request.setAttribute(RESULT_PARAM_LIST, entity);

		return Constants.CMD_EXEC_SUCCESS;
	}

}
