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

package org.iplass.mtp.impl.query.value.primary;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.controlflow.CaseSyntax;
import org.iplass.mtp.impl.query.value.subquery.ScalarSubQuerySyntax;
import org.iplass.mtp.impl.query.value.window.WindowFunctionSyntax;

public class PrimaryValueSyntax implements Syntax<ValueExpression>, QueryConstants {
	
	private ParenValueSyntax bracketValue;
	private EntityFieldSyntax entityField;
	private CastSyntax cast;
	private FunctionSyntax function;
	private LiteralSyntax literal;
	private ScalarSubQuerySyntax scalarSubQuery;
	private WindowFunctionSyntax windowFunction;
//	private AggregateSyntax aggregate;
	private ArrayValueSyntax arrayValue;
	private CaseSyntax caseClause;

	public void init(SyntaxContext context) {
		scalarSubQuery = context.getSyntax(ScalarSubQuerySyntax.class);
		bracketValue = context.getSyntax(ParenValueSyntax.class);
		windowFunction = context.getSyntax(WindowFunctionSyntax.class);
//		aggregate = context.getSyntax(AggregateSyntax.class);
		function = context.getSyntax(FunctionSyntax.class);
		cast = context.getSyntax(CastSyntax.class);
		entityField = context.getSyntax(EntityFieldSyntax.class);
		literal = context.getSyntax(LiteralSyntax.class);
		arrayValue = context.getSyntax(ArrayValueSyntax.class);
		caseClause = context.getSyntax(CaseSyntax.class);
	}

	public ValueExpression parse(ParseContext str) throws ParseException {
		
		//TODO 総当り方式では効率悪い。LL法を作って、先読みしながら、Syntaxを決定する
		
		// ( の場合は、BracketValueSyntaxか、ScalarSubQuerySyntax
		if (str.startsWith(LEFT_PAREN)) {
			int currentIndex = str.getCurrentIndex();
			
			try {
				return bracketValue.parse(str);
			} catch (ParseException e) {
				// ( はScalarSubQuerySyntaxとする
				str.setCurrentIndex(currentIndex);
				return scalarSubQuery.parse(str);
			}
		}
		
		//CASE の場合は、CASE文
		if (str.equalsNextToken(CASE, ParseContext.WHITE_SPACES)) {
			return caseClause.parse(str);
		}
		
		//総当り。。。
		if (str.isEnd()) {
			throw new ParseException(new EvalError("can't parse value.", this, str));
		}
		int currentIndex = str.getCurrentIndex();
		
		try {
			return arrayValue.parse(str);
		} catch (ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		
		try {
			return literal.parse(str);
		} catch (ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		
		try {
			return windowFunction.parse(str);
		} catch (ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		//including windowFunction parse process
//		try {
//			return aggregate.parse(str);
//		} catch (ParseException e) {
//			str.setCurrentIndex(currentIndex);
//		}
		
		try {
			return cast.parse(str);
		} catch (ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		
		try {
			return function.parse(str);
		} catch (ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		
		try {
			return entityField.parse(str);
		} catch (ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		
		
		throw new ParseException(new EvalError("can't parse value.", this, str));
	}

}
