/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.query;

import org.iplass.mtp.entity.query.OrderBy;
import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;

public class OrderBySyntax implements Syntax<OrderBy>, QueryConstants {
	
	private SortSpecSyntax sortSpec;

	public OrderBy parse(ParseContext str) throws ParseException {
		
		//ORDER
		if (!str.equalsNextToken(ORDER, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("ORDER BY expected.", this, str));
		}
		str.consumeChars(ORDER.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//BY
		if (!str.equalsNextToken(BY, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("BY expected.", this, str));
		}
		str.consumeChars(BY.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		OrderBy orderBy = new OrderBy();
		
		//SortSpec
		boolean isFirst = true;
		while (true) {
			if (isFirst) {
				isFirst = false;
			} else if (!str.startsWith(COMMA)) {
				break;
			} else {
				str.consumeChars(COMMA.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
			}

			SortSpec sortSpecNode = sortSpec.parse(str);
			orderBy.add(sortSpecNode);
		}
		
		return orderBy;
	}


	public void init(SyntaxContext context) {
		sortSpec = context.getSyntax(SortSpecSyntax.class);
	}

}
