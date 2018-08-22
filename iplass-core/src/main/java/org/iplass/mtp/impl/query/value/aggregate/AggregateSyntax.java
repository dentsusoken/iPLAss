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

package org.iplass.mtp.impl.query.value.aggregate;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.aggregate.Aggregate;
import org.iplass.mtp.entity.query.value.aggregate.Avg;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.aggregate.Median;
import org.iplass.mtp.entity.query.value.aggregate.Min;
import org.iplass.mtp.entity.query.value.aggregate.Mode;
import org.iplass.mtp.entity.query.value.aggregate.StdDevPop;
import org.iplass.mtp.entity.query.value.aggregate.StdDevSamp;
import org.iplass.mtp.entity.query.value.aggregate.Sum;
import org.iplass.mtp.entity.query.value.aggregate.VarPop;
import org.iplass.mtp.entity.query.value.aggregate.VarSamp;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class AggregateSyntax implements Syntax<Aggregate>, QueryConstants {
	
	private PolynomialSyntax polynomial;
	
	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

	public Aggregate parse(ParseContext str) throws ParseException {
		
		Aggregate ag = null;
		
		int currentIndex = str.getCurrentIndex();
		boolean isCount = false;
		String token = str.nextToken(ParseContext.TOKEN_DELIMITERS);
		if (token == null) {
			throw new ParseException(new EvalError("aggregate function expected.", this, str));
		}
		
		token = token.toUpperCase();
		switch (token) {
		case COUNT:
			ag = new Count();
			isCount = true;
			break;
		case SUM:
			ag = new Sum();
			break;
		case MAX:
			ag = new Max();
			break;
		case MIN:
			ag = new Min();
			break;
		case AVG:
			ag = new Avg();
			break;
		case STDDEV_POP:
			ag = new StdDevPop();
			break;
		case STDDEV_SAMP:
			ag = new StdDevSamp();
			break;
		case VAR_POP:
			ag = new VarPop();
			break;
		case VAR_SAMP:
			ag = new VarSamp();
			break;
		case MODE:
			ag = new Mode();
			break;
		case MEDIAN:
			ag = new Median();
			break;
		default:
			str.setCurrentIndex(currentIndex);
			throw new ParseException(new EvalError("aggregate function expected.", this, str));
		}
		
		str.consumeChars(ParseContext.WHITE_SPACES);

		if (!str.startsWith(LEFT_PAREN)) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(LEFT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (!isCount) {
			ValueExpression nestedValue = polynomial.parse(str);
			ag.setValue(nestedValue);
		} else {
			//countの場合はちょっと特殊
			if (!str.startsWith(RIGHT_PAREN)) {
				if (str.equalsNextToken(DISTINCT, ParseContext.WHITE_SPACES)) {
					((Count) ag).setDistinct(true);
					str.consumeChars(DISTINCT.length());
					str.consumeChars(ParseContext.WHITE_SPACES);
				}
				ValueExpression nestedValue = polynomial.parse(str);
				ag.setValue(nestedValue);
			}
		}
		
		if (!str.startsWith(RIGHT_PAREN)) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(RIGHT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		return ag;
	}
}
