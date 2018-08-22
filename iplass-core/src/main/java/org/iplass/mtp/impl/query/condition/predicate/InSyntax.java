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

package org.iplass.mtp.impl.query.condition.predicate;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.SubQuerySyntax;
import org.iplass.mtp.impl.query.value.RowValueListSyntax;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;


public class InSyntax implements Syntax<In>, QueryConstants {
	
	private PolynomialSyntax polynomial;
	private SubQuerySyntax subQuerySyntax;
	private RowValueListSyntax rowValueList;

	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
		subQuerySyntax = context.getSyntax(SubQuerySyntax.class);
		rowValueList = context.getSyntax(RowValueListSyntax.class);
	}

	public In parse(ParseContext str) throws ParseException {
		
		if (!str.equalsNextToken(IN, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("in expected.", this, str));
		}
		
		str.consumeChars(IN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		In in = new In();
		
		int currentPos = str.getCurrentIndex();
		if (!str.startsWith(LEFT_PAREN)) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(LEFT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//subquery
		if (str.equalsNextToken(SELECT, ParseContext.TOKEN_DELIMITERS)) {
			str.setCurrentIndex(currentPos);
			SubQuery subQuery = subQuerySyntax.parse(str);
			in.setSubQuery(subQuery);
			return in;
		}

		//Value Expressionだが、inでしか使わないのでここでパース
		//row value list
		if (str.startsWith(LEFT_PAREN)) {
			currentPos = str.getCurrentIndex();
			try {
				List<ValueExpression> values = new ArrayList<ValueExpression>();
				boolean isFirst = true;
				while (isFirst || (!isFirst && str.startsWith(COMMA))) {
					if (isFirst) {
						isFirst = false;
					} else {
						str.consumeChars(COMMA.length());
						str.consumeChars(ParseContext.WHITE_SPACES);
					}
					values.add(rowValueList.parse(str));
				}
				
				if (!str.startsWith(RIGHT_PAREN)) {
					throw new ParseException(new EvalError(") expected.", this, str));
				}
				str.consumeChars(RIGHT_PAREN.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
				in.setValue(values);
				
				return in;
			} catch (ParseException pe) {
				str.setCurrentIndex(currentPos);
				//continue parse as simple value expression list...
			}
		}
		
		//value expression list
		List<ValueExpression> values = new ArrayList<ValueExpression>();
		boolean isFirst = true;
		while (isFirst || (!isFirst && str.startsWith(COMMA))) {
			if (isFirst) {
				isFirst = false;
			} else {
				str.consumeChars(COMMA.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
			}
			values.add(polynomial.parse(str));
		}
		
		if (!str.startsWith(RIGHT_PAREN)) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(RIGHT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		in.setValue(values);
		
		return in;
	}

}
