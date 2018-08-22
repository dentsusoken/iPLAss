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
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class FunctionSyntax implements Syntax<Function>, QueryConstants {
	
	private PolynomialSyntax polynomial;

	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

	public Function parse(ParseContext str) throws ParseException {
		
		String funcName = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		if (funcName == null) {
			throw new ParseException(new EvalError("function name expected.", this, str));
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (!str.startsWith(LEFT_PAREN)) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(1);
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		Function func = new Function(funcName);
		
		boolean isFirst = true;
		List<ValueExpression> values = new ArrayList<ValueExpression>();
		while (!str.startsWith(RIGHT_PAREN) && !str.isEnd()) {
			if (isFirst) {
				isFirst = false;
			} else {
				if (!str.consumeChars(COMMA.length())) {
					throw new ParseException(new EvalError(", expected.", this, str));
				}
				str.consumeChars(ParseContext.WHITE_SPACES);
			}
			values.add(polynomial.parse(str));
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		
		if (!str.startsWith(RIGHT_PAREN)) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(RIGHT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (values.size() != 0) {
			func.setArguments(values);
		}
		
		return func;
	}

}
