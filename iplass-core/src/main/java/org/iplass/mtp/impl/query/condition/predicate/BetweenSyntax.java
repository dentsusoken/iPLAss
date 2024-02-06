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

package org.iplass.mtp.impl.query.condition.predicate;

import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class BetweenSyntax implements Syntax<Between>, QueryConstants {
	
	private PolynomialSyntax polynomial;

	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

	public Between parse(ParseContext str) throws ParseException {
		
		if (!str.equalsNextToken(BETWEEN, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("between expected.", this, str));
		}
			
		Between b = new Between();
		str.consumeChars(BETWEEN.length());
		if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("space expected.", this, str));
		}
		
		b.setFrom(polynomial.parse(str));
		
		if (!str.equalsNextToken(AND, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("and expected.", this, str));
		}
		str.consumeChars(AND.length());
		if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("space expected.", this, str));
		}
		
		b.setTo(polynomial.parse(str));
		
		return b;
	}

}
