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
package org.iplass.gem.command.fulltext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.gem.command.Constants;
import org.iplass.mtp.command.Command;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.command.annotation.action.ActionMapping;
import org.iplass.mtp.command.annotation.action.ActionMappings;
import org.iplass.mtp.command.annotation.action.Result;
import org.iplass.mtp.command.annotation.action.Result.Type;
import org.iplass.mtp.util.StringUtil;

@ActionMappings({
	@ActionMapping(
			name=FullTextSearchViewCommand.SEARCH_VIEW_ACTION_NAME,
			displayName="全文検索画面表示",
			result={
				@Result(status=Constants.CMD_EXEC_SUCCESS, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_FULLTEXT_SEARCH,
						templateName="gem/fulltext/search",
						layoutActionName=Constants.LAYOUT_NORMAL_ACTION),
				@Result(status=Constants.CMD_EXEC_ERROR_VIEW, type=Type.JSP,
						value=Constants.CMD_RSLT_JSP_ERROR,
						templateName="gem/generic/common/error",
						layoutActionName=Constants.LAYOUT_NORMAL_ACTION)
			}
	)
})
@CommandClass(name="gem/fulltext/FullTextSearchViewCommand", displayName="全文検索画面表示")
public final class FullTextSearchViewCommand implements Command {

	public static final String SEARCH_VIEW_ACTION_NAME = "gem/fulltext/searchview";

	@Override
	public String execute(RequestContext request) {

		String searchCond = request.getParam(Constants.SEARCH_COND);

		String fulltextKey = null;
		List<String> defNames = null;

		//searchCondが指定されていた場合はsearchCondを優先(詳細画面から戻ってきた場合)
		if (StringUtil.isNotEmpty(searchCond)) {
			fulltextKey = getSeachCondKeyword(searchCond);
			defNames = getSeachCondDefNames(searchCond);
		} else {
			fulltextKey = request.getParam("fulltextKey");
			if (StringUtil.isEmpty(fulltextKey)) {
				fulltextKey = "";
			}

			defNames = new ArrayList<>();
			String defName = request.getParam("searchDefName");
			if (StringUtil.isNotEmpty(defName)) {
				defNames.add(defName);
			}
		}

		request.setAttribute("fulltextKey", fulltextKey);
		request.setAttribute("searchDefName", defNames);

		return Constants.CMD_EXEC_SUCCESS;
	}

	private String getSeachCondKeyword(String src) {
		if (StringUtil.isBlank(src) || !src.contains("fulltextKey=")) {
			return "";
		}

		String tmp = src.substring(src.indexOf("fulltextKey="));
		tmp = tmp.substring("fulltextKey=".length());
		return tmp;
	}

	private List<String> getSeachCondDefNames(String src) {
		if (StringUtil.isBlank(src) || !src.contains("searchDefName=")) {
			return Collections.emptyList();
		}

		List<String> defNames = new ArrayList<>();

		//DefNameには&は含まれていないので&で分解
		String[] params = src.split("&");
		for (String param : params) {
			if (param.startsWith("searchDefName=")) {
				defNames.add(param.substring("searchDefName=".length()));
			}
		}
		return defNames;
	}
}

