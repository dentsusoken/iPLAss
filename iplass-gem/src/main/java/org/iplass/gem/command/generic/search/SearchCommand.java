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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.iplass.gem.command.Constants;
import org.iplass.gem.command.CreateSearchResultUtil;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.CommandInvoker;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SearchResult;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@WebApi(
	name=SearchCommand.WEBAPI_NAME,
	displayName="汎用検索",
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	results={"count", "message", "htmlData"},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/search/SearchCommand", displayName="汎用検索")
public final class SearchCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/search/search";

	@SuppressWarnings("unchecked")
	@Override
	public String execute(RequestContext request) {
		String searchType = request.getParam(Constants.SEARCH_TYPE);

		SearchCommandBase command = null;
		CommandInvoker ci = ManagerLocator.getInstance().getManager(CommandInvoker.class);
		if (Constants.SEARCH_TYPE_NORMAL.equals(searchType)) {
			command = (SearchCommandBase) ci.getCommandInstance(NormalSearchCommand.CMD_NAME);
		} else if (Constants.SEARCH_TYPE_DETAIL.equals(searchType)) {
			command = (SearchCommandBase) ci.getCommandInstance(DetailSearchCommand.CMD_NAME);
		} else if (Constants.SEARCH_TYPE_FIXED.equals(searchType)) {
			command = (SearchCommandBase) ci.getCommandInstance(FixedSearchCommand.CMD_NAME);
		}

		if (command != null) {

			String ret = command.execute(request);
			if (Constants.CMD_EXEC_SUCCESS.equals(ret)) {
				try {
					SearchResult<Entity> result = (SearchResult<Entity>) request.getAttribute("result");
					if (result != null) {
						SearchContext sc = command.getContext(request);

						List<Map<String, String>> retList = CreateSearchResultUtil.getHtmlData(result.getList(), sc.getEntityDefinition(), ((SearchContextBase) sc).getResultSection());
						request.setAttribute("htmlData", retList);
					} else {
						request.setAttribute("htmlData", new ArrayList<Map<String, String>>());
					}
				} catch (IOException e) {
					throw new SystemException(e);
				} catch (ServletException e) {
					throw new SystemException(e);
				}
			} else if (Constants.CMD_EXEC_ERROR_PARAMETER.equals(ret)) {
				return Constants.CMD_EXEC_ERROR_PARAMETER;
			} else if (Constants.CMD_EXEC_ERROR_VALIDATE.equals(ret)) {
				return Constants.CMD_EXEC_ERROR_VALIDATE;
			} else if (Constants.CMD_EXEC_ERROR_SEARCH.equals(ret)) {
				return Constants.CMD_EXEC_ERROR_SEARCH;
			} else {
				String message = (String) request.getAttribute(Constants.MESSAGE);
				throw new ApplicationException(message);
			}
		} else {
			throw new IllegalArgumentException("invalid search type. value=" + searchType);
		}

		return Constants.CMD_EXEC_SUCCESS;
	}
}
