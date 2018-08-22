/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.entity.query.AsOf.AsOfSpec;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class AsOfSyntax implements Syntax<AsOf>, QueryConstants {
	
	private PolynomialSyntax polynomial;
	
	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}
	
	public AsOf parse(ParseContext str) throws ParseException {
		//AS OF
		AsOfSpec asOfSpec = null;
		ValueExpression asOf = null;
		if (!str.equalsNextToken(AS, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("AS expected.", this, str));
		}
		str.consumeChars(AS.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.equalsNextToken(OF, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("OF expected.", this, str));
		}
		str.consumeChars(OF.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (str.equalsNextToken(NOW, ParseContext.TOKEN_DELIMITERS)) {
			str.consumeChars(NOW.length());
			asOfSpec = AsOfSpec.NOW;
		} else if (str.equalsNextToken(UPDATE, ParseContext.TOKEN_DELIMITERS)) {
			str.consumeChars(UPDATE.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
			if (!str.equalsNextToken(TIME, ParseContext.TOKEN_DELIMITERS)) {
				throw new ParseException(new EvalError("time expected.", this, str));
			}
			str.consumeChars(TIME.length());
			asOfSpec = AsOfSpec.UPDATE_TIME;
		} else {
			asOf = polynomial.parse(str);
			asOfSpec = AsOfSpec.SPEC_VALUE;
		}
		
		AsOf ao = new AsOf(asOfSpec);
		if (asOf != null) {
			ao.setValue(asOf);
		}
		
		return ao;
	}

}
