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

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.controlflow.Else;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class ElseSyntax implements Syntax<Else>, QueryConstants {

	private PolynomialSyntax polynomial;
	
	@Override
	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

	@Override
	public Else parse(ParseContext str) throws ParseException {
		//ELSE
		if (!str.equalsNextToken(ELSE, ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("ELSE expected.", this, str));
		}
		str.consumeChars(ELSE.length());
		
		Else elseClause = new Else();
		
		if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("space expected.", this, str));
		}
		
		ValueExpression result = polynomial.parse(str);
		elseClause.setResult(result);

		return elseClause;
	}

}
