/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.query.value.controlflow;

import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.controlflow.When;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.condition.expr.OrSyntax;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class WhenSyntax implements Syntax<When>, QueryConstants {
	
	private OrSyntax or;
	private PolynomialSyntax polynomial;

	@Override
	public void init(SyntaxContext context) {
		or = context.getSyntax(OrSyntax.class);
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

	@Override
	public When parse(ParseContext str) throws ParseException {
		//WHEN
		if (!str.equalsNextToken(WHEN, ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("WHEN expected.", this, str));
		}
		str.consumeChars(WHEN.length());
		
		When when = new When();
		
		if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("space expected.", this, str));
		}
		
		//Condition
		Condition cond = or.parse(str);
		when.setCondition(cond);
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//THEN
		if (!str.equalsNextToken(THEN, ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("THEN expected.", this, str));
		}
		str.consumeChars(THEN.length());
		
		if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("space expected.", this, str));
		}
		
		ValueExpression result = polynomial.parse(str);
		when.setResult(result);

		return when;
	}

}
