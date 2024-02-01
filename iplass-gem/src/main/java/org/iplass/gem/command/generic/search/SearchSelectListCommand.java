/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
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
	name=SearchSelectListCommand.WEBAPI_NAME,
	displayName="選択対象検索",
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	results={"data"},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/search/SearchSelectListCommand", displayName="選択対象")
public final class SearchSelectListCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/search/searchSelectList";

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
			WrappedSearchCommand wrapped = new WrappedSearchCommand(command);

			String ret = wrapped.execute(request);
			if (Constants.CMD_EXEC_SUCCESS.equals(ret)) {
				SearchResult<Entity> result = (SearchResult<Entity>) request.getAttribute("result");
				if (result != null) {
					List<String> data = new ArrayList<>();
					result.getList().stream().forEach(e -> data.add(e.getOid() + "_" + e.getVersion()));
					request.setAttribute("data", data);
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

	//検索項目をoidとversionのみにする＆検索条件を検索処理と同じにするため、ラッパークラスで制御
	private class WrappedSearchCommand extends SearchCommandBase {

		private SearchCommandBase command;

		public WrappedSearchCommand(SearchCommandBase command) {
			this.command = command;
		}

		@Override
		protected Class<? extends SearchContext> getContextClass() {
			return command.getContextClass();
		}

		@Override
		public SearchContext getContext(RequestContext request) {
			SearchContext context = command.getContext(request);
			return new SearchSelectListContext((SearchContextBase) context);
		}
	}
}
