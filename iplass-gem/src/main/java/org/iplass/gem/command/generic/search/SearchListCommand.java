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

package org.iplass.gem.command.generic.search;

import java.io.IOException;

import javax.servlet.ServletException;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.CreateSearchResultUtil;
import org.iplass.gem.command.common.SearchResultData;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.template.Template;
import org.iplass.mtp.command.annotation.webapi.RestJson;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.view.filter.EntityFilterManager;
import org.iplass.mtp.view.generic.EntityViewManager;
import org.iplass.mtp.webapi.definition.MethodType;
import org.iplass.mtp.webapi.definition.RequestType;

@WebApi(
	name=SearchListCommand.WEBAPI_NAME,
	displayName="Entity一覧検索(パーツ)",
	accepts=RequestType.REST_JSON,
	methods=MethodType.POST,
	restJson=@RestJson(parameterName="param"),
	results={SearchListCommand.RESULT_PARAM_COUNT, SearchListCommand.RESULT_PARAM_HTML_DATA},
	checkXRequestedWithHeader=true
)
@Template(name="gem/generic/search/list", displayName="検索結果パーツ", path="/jsp/gem/generic/search/list.jsp")
@CommandClass(name="gem/generic/search/SearchListCommand", displayName="Entity一覧検索(パーツ)")
public final class SearchListCommand extends SearchListPartsCommandBase {

	public static final String WEBAPI_NAME = "gem/generic/search/list";

	public static final String RESULT_PARAM_COUNT = "count";
	public static final String RESULT_PARAM_HTML_DATA = "htmlData";

	private EntityDefinitionManager edm;
	private EntityViewManager evm;
	private EntityFilterManager efm;

	public SearchListCommand() {
		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		evm = ManagerLocator.getInstance().getManager(EntityViewManager.class);
		efm = ManagerLocator.getInstance().getManager(EntityFilterManager.class);
	}

	@Override
	public String execute(RequestContext request) {

		SearchListContext context = new SearchListContext();
		context.setRequest(request);
		context.setEntityDefinition(edm.get(context.getDefName()));
		context.setEntityView(evm.get(context.getDefName()));
		context.setFilter(efm.get(context.getDefName()));

		Query query = toQuery(context);

		int count = count(context, query.copy());
		request.setAttribute(RESULT_PARAM_COUNT, count);

		query.setOrderBy(context.getOrderBy());
		query.setLimit(context.getLimit());

		SearchResult<Entity> result = search(context, query);

		try {
			SearchResultData resultData = CreateSearchResultUtil.getResultData(
					result.getList(), context.getEntityDefinition(),
					context.getResultSection(), context.getViewName());

			context.fireCreateSearchResultEvent(resultData);

			request.setAttribute(RESULT_PARAM_HTML_DATA, resultData.toResponse());
		} catch (IOException e) {
			throw new SystemException(e);
		} catch (ServletException e) {
			throw new SystemException(e);
		}
		return Constants.CMD_EXEC_SUCCESS;
	}

}
