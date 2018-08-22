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

package org.iplass.mtp.impl.query.condition.predicate;

import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;

public class IsNullSyntax implements Syntax<IsNull>, QueryConstants {

	public void init(SyntaxContext context) {
	}

	public IsNull parse(ParseContext str) throws ParseException {
		if (!str.equalsNextToken(IS, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("is null expected.", this, str));
		}
		str.consumeChars(IS.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (!str.equalsNextToken(NULL, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("null expected.", this, str));
		}
		str.consumeChars(NULL.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		return new IsNull();
	}

}
