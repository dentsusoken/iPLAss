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

package org.iplass.mtp.impl.query.condition.predicate;

import org.iplass.mtp.entity.query.condition.predicate.Contains;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;

public class ContainsSyntax implements Syntax<Contains>, QueryConstants {

	private static char QUOTE_CHAR = '\'';

	public void init(SyntaxContext context) {
	}

	public Contains parse(ParseContext str) throws ParseException {

		if (!str.equalsNextToken(CONTAINS, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("contains expected.", this, str));
		}

		str.consumeChars(CONTAINS.length());

		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.startsWith(LEFT_PAREN)) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(LEFT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);

		String searchText = str.innerToken(QUOTE_CHAR, true);

		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.startsWith(RIGHT_PAREN)) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(RIGHT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);

		return new Contains(searchText);
	}

}
