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

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.webapi.WebApi;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;

@WebApi(
	name=CountCommand.WEBAPI_NAME,
	displayName="全件数取得",
	accepts=RequestType.REST_FORM,
	methods=MethodType.POST,
	results={"count", "message"},
	checkXRequestedWithHeader=true
)
@CommandClass(name="gem/generic/search/CountCommand", displayName="全件数取得")
public final class CountCommand implements Command {

	public static final String WEBAPI_NAME = "gem/generic/search/count";

	@Override
	public String execute(RequestContext request) {
		String searchType = request.getParam(Constants.SEARCH_TYPE);

		SearchCommandBase command = null;
		if (Constants.SEARCH_TYPE_NORMAL.equals(searchType)) {
			command = new NormalSearchCommand();
		} else if (Constants.SEARCH_TYPE_DETAIL.equals(searchType)) {
			command = new DetailSearchCommand();
		} else if (Constants.SEARCH_TYPE_FIXED.equals(searchType)) {
			command = new FixedSearchCommand();
		}

		if (command != null) {
			command.execute(request);
		}

		return Constants.CMD_EXEC_SUCCESS;
	}
}
