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
import org.iplass.gem.command.GemResourceBundleUtil;
import org.iplass.mtp.command.annotation.CommandClass;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.view.filter.expression.UnsupportedFilterOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CommandClass(name=DetailSearchCommand.CMD_NAME, displayName="詳細検索")
public final class DetailSearchCommand extends SearchCommandBase {

	public static final String CMD_NAME = "gem/generic/search/DetailSearchCommand";

	private static Logger log = LoggerFactory.getLogger(DetailSearchCommand.class);

	@Override
	protected Class<? extends SearchContext> getContextClass() {
		return DetailSearchContext.class;
	}

	@Override
	protected Query toQuery(SearchContext context) {
		try {
			return super.toQuery(context);
		} catch (UnsupportedFilterOperationException e) {
			if (log.isDebugEnabled()) {
				log.debug(e.getMessage(), e);
			}
			context.getRequest().setAttribute(Constants.MESSAGE, e.getMessage());
			return null;
		} catch (IllegalArgumentException e) {
			if (log.isDebugEnabled()) {
				log.debug(e.getMessage(), e);
			}
			context.getRequest().setAttribute(Constants.MESSAGE, resourceString("command.generic.search.SearchCommandBase.searchCondErr"));
			return null;
		}
	}

	private static String resourceString(String key, Object... arguments) {
		return GemResourceBundleUtil.resourceString(key, arguments);
	}
}
