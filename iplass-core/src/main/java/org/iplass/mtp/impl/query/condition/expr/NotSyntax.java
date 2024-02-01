/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.query.condition.expr;

import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.Not;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;

public class NotSyntax implements Syntax<Condition>, QueryConstants {
	
	private ParenSyntax bracket;

	public void init(SyntaxContext context) {
		bracket = context.getSyntax(ParenSyntax.class);
	}

	public Condition parse(ParseContext str) throws ParseException {
		
		if (str.equalsNextToken(NOT, ParseContext.TOKEN_DELIMITERS)) {
			
			str.consumeChars(NOT.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
			
			return new Not(bracket.parse(str));
		} else {
			return bracket.parse(str);
		}
	}

}
