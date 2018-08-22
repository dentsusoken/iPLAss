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

import org.iplass.mtp.entity.query.Limit;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;

public class LimitSyntax implements Syntax<Limit>, QueryConstants {
	
	public Limit parse(ParseContext str) throws ParseException {
		
		//LIMIT
		if (!str.equalsNextToken(LIMIT, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("LIMIT expected.", this, str));
		}
		str.consumeChars(LIMIT.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		Limit limit = new Limit();
		
		boolean isBind = true;
		if (str.startsWith(LEFT_HINT_COMMENT)) {
			str.consumeChars(LEFT_HINT_COMMENT.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (str.equalsNextToken(HINT_NO_BIND, ParseContext.TOKEN_DELIMITERS)) {
				str.consumeChars(HINT_NO_BIND.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
				isBind = false;
			} else {
				throw new ParseException(new EvalError("only no_bind hint is valid.", this, str));
			}
			if (str.startsWith(RIGHT_HINT_COMMENT)) {
				str.consumeChars(RIGHT_HINT_COMMENT.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
			} else {
				throw new ParseException(new EvalError("hint clause not terminated.", this, str));
			}
		}
		
		//int of limit
		int currentIndex = str.getCurrentIndex();
		String limitVal = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		try {
			limit.setLimit(Integer.parseInt(limitVal));
		} catch (NumberFormatException e) {
			str.setCurrentIndex(currentIndex);
			throw new ParseException(new EvalError("limit count unspecified.", this, str));
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//OFFSET
		if (str.equalsNextToken(OFFSET, ParseContext.TOKEN_DELIMITERS)) {
			str.consumeChars(OFFSET.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
			
			//int of offset
			currentIndex = str.getCurrentIndex();
			String offsetVal = str.nextToken(ParseContext.TOKEN_DELIMITERS);
			try {
				limit.setOffset(Integer.parseInt(offsetVal));
			} catch (NumberFormatException e) {
				str.setCurrentIndex(currentIndex);
				throw new ParseException(new EvalError("offset index unspecified.", this, str));
			}
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		
		limit.setBindable(isBind);
		
		return limit;
	}

	public void init(SyntaxContext context) {
	}

}
