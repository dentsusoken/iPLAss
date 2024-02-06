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

package org.iplass.mtp.impl.query;

import org.iplass.mtp.entity.query.SortSpec;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class SortSpecSyntax implements Syntax<SortSpec>, QueryConstants {
	
	private PolynomialSyntax polynomial;

	public SortSpec parse(ParseContext str) throws ParseException {
		
		//ValueExpression
		ValueExpression ve = polynomial.parse(str);
		SortSpec sortSpec = new SortSpec();
		sortSpec.setSortKey(ve);
		
		//ASC or DESC
//		boolean hasSpace = str.consumeChars(ParseContext.WHITE_SPACES);
		int current = str.getCurrentIndex();
		String token = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		if (ASC.equalsIgnoreCase(token)) {
			sortSpec.setType(SortType.ASC);
		} else if (DESC.equalsIgnoreCase(token)) {
			sortSpec.setType(SortType.DESC);
		} else {
			str.setCurrentIndex(current);
		}
		
		//NULLS FIRST/LAST
		current = str.getCurrentIndex();
		str.consumeChars(ParseContext.WHITE_SPACES);
		token = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		if (NULLS.equalsIgnoreCase(token)) {
			current = str.getCurrentIndex();
			if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
				str.setCurrentIndex(current);
				throw new ParseException(new EvalError("FIRST or LAST expected.", this, str));
			}
			
			token = str.nextToken(ParseContext.TOKEN_DELIMITERS);
			if (FIRST.equalsIgnoreCase(token)) {
				sortSpec.setNullOrderingSpec(NullOrderingSpec.FIRST);
			} else if (LAST.equalsIgnoreCase(token)) {
				sortSpec.setNullOrderingSpec(NullOrderingSpec.LAST);
			} else {
				str.setCurrentIndex(current);
				throw new ParseException(new EvalError("FIRST or LAST expected.", this, str));
			}
		} else {
			str.setCurrentIndex(current);
		}
		
		return sortSpec;
	}

	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

}
