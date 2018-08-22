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

package org.iplass.mtp.impl.query.value.primary;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.ArrayValue;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;


public class ArrayValueSyntax implements Syntax<ArrayValue>, QueryConstants {
	
	private PolynomialSyntax polynomial;

	@Override
	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

	@Override
	public ArrayValue parse(ParseContext str) throws ParseException {
		
		if (!str.equalsNextToken(ARRAY, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("array expected.", this, str));
		}
		
		ArrayValue array = new ArrayValue();
		str.consumeChars(ARRAY.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.startsWith(LEFT_BRACKET)) {
			throw new ParseException(new EvalError("[ expected.", this, str));
		}
		str.consumeChars(LEFT_BRACKET.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		List<ValueExpression> values = new ArrayList<ValueExpression>();
		boolean isFirst = true;
		while (!str.startsWith(RIGHT_BRACKET) && !str.isEnd()) {
			if (isFirst) {
				isFirst = false;
			} else {
				if (!str.startsWith(COMMA)) {
					throw new ParseException(new EvalError(", expected.", this, str));
				}
				str.consumeChars(COMMA.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
			}
			values.add(polynomial.parse(str));
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		
		if (!str.startsWith(RIGHT_BRACKET)) {
			throw new ParseException(new EvalError("] expected.", this, str));
		}
		str.consumeChars(RIGHT_BRACKET.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (values.size() != 0) {
			array.setValues(values);
		}
		
		return array;
	}

}
