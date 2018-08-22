/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.gem.command.Constants;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.CommandInvoker;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@WebApi(
	name=SearchValidateCommand.WEBAPI_NAME,
	displayName="汎用検索検証",
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	results={"message"},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/search/ValidateCommand", displayName="汎用検索検証")
public final class SearchValidateCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/search/validate";

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
			//検証モードで実行
			command.setValidateCondition(request, true);
			return command.execute(request);
		}

		throw new IllegalArgumentException("invalid search type. value=" + searchType);
	}
}
