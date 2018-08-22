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

import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.From;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;

public class FromSyntax implements Syntax<From>, QueryConstants {
	
	private AsOfSyntax asOf;
	
	public void init(SyntaxContext context) {
		asOf = context.getSyntax(AsOfSyntax.class);
	}

	public From parse(ParseContext str) throws ParseException {
		
		if (!str.equalsNextToken(FROM, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("from expected.", this, str));
		}
		str.consumeChars(FROM.length());
		if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("space expected.", this, str));
		}
		
		String entityName = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		if (entityName == null) {
			throw new ParseException(new EvalError("entityName expected.", this, str));
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		From f = new From(entityName);
		
		//AS OF
		if (str.equalsNextToken(AS, ParseContext.TOKEN_DELIMITERS)) {
			AsOf ao = asOf.parse(str);
			str.consumeChars(ParseContext.WHITE_SPACES);
			f.setAsOf(ao);
		}

		return f;
		
	}

}
