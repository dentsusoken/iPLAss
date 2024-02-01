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

import org.iplass.mtp.entity.query.condition.predicate.ComparisonPredicate;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class ComparisonPredicateSyntax implements Syntax<ComparisonPredicate>, QueryConstants {
	
	private PolynomialSyntax polynomial;
	
	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

	public ComparisonPredicate parse(ParseContext str) throws ParseException {
		
		ComparisonPredicate cp = null;
		if (str.startsWith(EQUALS)) {
			cp = new Equals();
			str.consumeChars(EQUALS.length());
		} else if (str.startsWith(GREATER_EQUAL)) {
			cp = new GreaterEqual();
			str.consumeChars(GREATER_EQUAL.length());
		} else if (str.startsWith(GREATER)) {
			cp = new Greater();
			str.consumeChars(GREATER.length());
		} else if (str.startsWith(LESSER_EQUAL)) {
			cp = new LesserEqual();
			str.consumeChars(LESSER_EQUAL.length());
		} else if (str.startsWith(LESSER)) {
			cp = new Lesser();
			str.consumeChars(LESSER.length());
		} else if (str.startsWith(NOT_EQUALS)) {
			cp = new NotEquals();
			str.consumeChars(NOT_EQUALS.length());
		} else {
			throw new ParseException(new EvalError("operator expected.", this, str));
		}
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		ValueExpression value = polynomial.parse(str);
		cp.setValue(value);
		return cp;
	}
	
}
