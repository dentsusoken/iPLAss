/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.query.value.window;

import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.value.window.WindowOrderBy;
import org.iplass.mtp.entity.query.value.window.WindowSortSpec;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QueryConstants;

public class WindowOrderBySyntax implements Syntax<WindowOrderBy>, QueryConstants {
	
	private OrderBySyntax orderBy;

	public WindowOrderBy parse(ParseContext str) throws ParseException {
		
		OrderBy ob = orderBy.parse(str);
		WindowOrderBy wob = new WindowOrderBy();
		for (SortSpec ss: ob.getSortSpecList()) {
			WindowSortSpec wss = new WindowSortSpec(ss.getSortKey(), ss.getType(), ss.getNullOrderingSpec());
			wob.add(wss);
		}
		return wob;
	}


	public void init(SyntaxContext context) {
		orderBy = context.getSyntax(OrderBySyntax.class);
	}

}
