/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class EqualsSyntax implements Syntax<Equals>, QueryConstants {

	private PolynomialSyntax polynomial;

	public static char[] EQUALS_DELIMITER = { '=' };

	public Equals parse(ParseContext str) throws ParseException {

		//Equals
		Equals equals = new Equals();
		String propertyName = str.nextToken(EQUALS_DELIMITER);
		equals.setPropertyName(propertyName);
		if (!str.startsWith(EQUALS)) {
			throw new ParseException(new EvalError("= expected.", this, str));
		}
		str.consumeChars(EQUALS.length());
		if(str.peekChar() != COMMA_CHAR && !str.isEnd()) {
			ValueExpression value = polynomial.parse(str);
			equals.setValue(value);
		}
		return equals;
	}

	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

}
