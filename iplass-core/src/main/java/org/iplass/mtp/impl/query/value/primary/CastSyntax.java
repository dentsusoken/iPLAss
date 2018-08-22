/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Cast;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class CastSyntax implements Syntax<Cast>, QueryConstants {
	
	private PolynomialSyntax polynomial;

	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

	public Cast parse(ParseContext str) throws ParseException {
		if (!str.equalsNextToken(CAST, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("CAST expected.", this, str));
		}
		str.consumeChars(CAST.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.startsWith(LEFT_PAREN)) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(LEFT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		Cast cast = new Cast();

		ValueExpression value = polynomial.parse(str);
		cast.setValue(value);
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (!str.equalsNextToken(AS, ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("AS expected.", this, str));
		}
		str.consumeChars(AS.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		String type = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		if (type == null) {
			throw new ParseException(new EvalError("type expected.", this, str));
		}
		type = type.toUpperCase();
		PropertyDefinitionType pdt = null;
		try {
			pdt = PropertyDefinitionType.valueOf(type);
		} catch (IllegalArgumentException e) {
			throw new ParseException(new EvalError("invalid type spec.", this, str));
		}
		cast.setType(pdt);
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		if (str.startsWith(LEFT_PAREN)) {
			str.consumeChars(1);
			while (!str.isEnd() && str.peekChar() != RIGHT_PAREN_CHAR) {
				str.consumeChars(ParseContext.WHITE_SPACES);
				String typeArgStr = str.nextToken(ParseContext.TOKEN_DELIMITERS);
				try {
					int typeArg = Integer.parseInt(typeArgStr.trim());
					if (cast.getTypeArguments() == null) {
						cast.setTypeArguments(new ArrayList<>());
					}
					cast.getTypeArguments().add(typeArg);
				} catch (NumberFormatException e) {
					throw new ParseException(new EvalError("invalid type arguments.", this, str));
				}
				str.consumeChars(ParseContext.WHITE_SPACES);
				if (str.peekChar() != ',') {
					if (str.peekChar() != RIGHT_PAREN_CHAR) {
						throw new ParseException(new EvalError(", or ) expected.", this, str));
					}
				} else {
					str.consumeChars(1);
				}
			}
			if (str.popChar() != RIGHT_PAREN_CHAR) {
				throw new ParseException(new EvalError(") expected.", this, str));
			}
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		
		if (!str.startsWith(RIGHT_PAREN)) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(RIGHT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		return cast;
	}

}
