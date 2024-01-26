/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.query.value.aggregate;

import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.value.aggregate.WithinGroup;
import org.iplass.mtp.entity.query.value.aggregate.WithinGroupSortSpec;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.OrderBySyntax;
import org.iplass.mtp.impl.query.QueryConstants;

public class WithinGroupSyntax implements Syntax<WithinGroup>, QueryConstants {

	private OrderBySyntax orderBy;

	public WithinGroup parse(ParseContext str) throws ParseException {
		
		//WITHIN
		if (!str.equalsNextToken(WITHIN, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("WITHIN GROUP expected.", this, str));
		}
		str.consumeChars(WITHIN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//GROUP
		if (!str.equalsNextToken(GROUP, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("GROUP expected.", this, str));
		}
		str.consumeChars(GROUP.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (!str.startsWith(LEFT_PAREN)) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(LEFT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//ORDER BY
		OrderBy ob = orderBy.parse(str);
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (!str.startsWith(RIGHT_PAREN)) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(RIGHT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		WithinGroup withinGroup = new WithinGroup();
		for (SortSpec ss: ob.getSortSpecList()) {
			WithinGroupSortSpec wgss = new WithinGroupSortSpec(ss.getSortKey(), ss.getType(), ss.getNullOrderingSpec());
			withinGroup.add(wgss);
		}
		
		return withinGroup;
	}

	public void init(SyntaxContext context) {
		orderBy = context.getSyntax(OrderBySyntax.class);
	}

}
