/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.QuerySyntax;
import org.iplass.mtp.impl.query.condition.expr.OrSyntax;

public class SubQuerySyntax implements Syntax<SubQuery>, QueryConstants {
	
	private QuerySyntax query;
	private OrSyntax or;

	public void init(SyntaxContext context) {
		query = context.getSyntax(QuerySyntax.class);
		or = context.getSyntax(OrSyntax.class);
	}

	public SubQuery parse(ParseContext str) throws ParseException {
		if (!str.startsWith(LEFT_PAREN)) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(1);
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		Query q = query.parse(str);
		
		SubQuery sq = new SubQuery(q);
		
		if (str.equalsNextToken(ON, ParseContext.TOKEN_DELIMITERS)) {
			str.consumeChars(ON.length());
			if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
				throw new ParseException(new EvalError("space expected.", this, str));
			}
			
			Condition on = or.parse(str);
			sq.setOn(on);
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (!str.startsWith(RIGHT_PAREN)) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(1);
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		return sq;
	}

}
