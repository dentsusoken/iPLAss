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

package org.iplass.mtp.impl.query;

import org.iplass.mtp.entity.query.Having;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.condition.expr.OrSyntax;

public class HavingSyntax implements Syntax<Having>, QueryConstants {
	
	private OrSyntax or;

	public void init(SyntaxContext context) {
		or = context.getSyntax(OrSyntax.class);
	}

	public Having parse(ParseContext str) throws ParseException {
		
		//HAVING
		if (!str.equalsNextToken(HAVING, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("HAVING expected.", this, str));
		}
		str.consumeChars(HAVING.length());
		
		Having having = new Having();
		
		if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("space expected.", this, str));
		}
		
		//Condition
		Condition cond = or.parse(str);
		having.setCondition(cond);
		return having;
	}

}
