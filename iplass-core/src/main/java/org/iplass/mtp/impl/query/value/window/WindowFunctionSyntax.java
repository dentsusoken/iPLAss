/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.query.value.window;

import org.iplass.mtp.entity.query.value.aggregate.Aggregate;
import org.iplass.mtp.entity.query.value.primary.PrimaryValue;
import org.iplass.mtp.entity.query.value.window.CumeDist;
import org.iplass.mtp.entity.query.value.window.DenseRank;
import org.iplass.mtp.entity.query.value.window.PartitionBy;
import org.iplass.mtp.entity.query.value.window.PercentRank;
import org.iplass.mtp.entity.query.value.window.Rank;
import org.iplass.mtp.entity.query.value.window.RowNumber;
import org.iplass.mtp.entity.query.value.window.WindowAggregate;
import org.iplass.mtp.entity.query.value.window.WindowFunction;
import org.iplass.mtp.entity.query.value.window.WindowOrderBy;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.aggregate.AggregateSyntax;

public class WindowFunctionSyntax implements Syntax<PrimaryValue>, QueryConstants {
	
	private AggregateSyntax aggregate;
	private PartitionBySyntax partitionBy;
	private WindowOrderBySyntax orderBy;
	
	public void init(SyntaxContext context) {
		aggregate = context.getSyntax(AggregateSyntax.class);
		partitionBy = context.getSyntax(PartitionBySyntax.class);
		orderBy = context.getSyntax(WindowOrderBySyntax.class);
	}

	public PrimaryValue parse(ParseContext str) throws ParseException {
		//this Syntax return WindowFunction or Aggregate
		WindowFunction wf = null;
		Aggregate ag = null;
		
		int currentIndex = str.getCurrentIndex();
		
		//aggregate window function
		try {
			ag = aggregate.parse(str);
		} catch (ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		
		//rank window function
		String token = null;
		if (ag == null) {
			token = str.nextToken(ParseContext.TOKEN_DELIMITERS);
			if (token == null) {
				throw new ParseException(new EvalError("window aggregate function expected.", this, str));
			}
			
			token = token.toUpperCase();
			switch (token) {
			case RANK:
				wf = new Rank();
				break;
			case DENSE_RANK:
				wf = new DenseRank();
				break;
			case PERCENT_RANK:
				wf = new PercentRank();
				break;
			case CUME_DIST:
				wf = new CumeDist();
				break;
			case ROW_NUMBER:
				wf = new RowNumber();
				break;
			default:
				str.setCurrentIndex(currentIndex);
				throw new ParseException(new EvalError("window aggregate function expected.", this, str));
			}
			
			str.consumeChars(ParseContext.WHITE_SPACES);
			
			if (!str.startsWith(LEFT_PAREN)) {
				throw new ParseException(new EvalError("( expected.", this, str));
			}
			str.consumeChars(LEFT_PAREN.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
			
			if (!str.startsWith(RIGHT_PAREN)) {
				throw new ParseException(new EvalError(") expected.", this, str));
			}
			str.consumeChars(RIGHT_PAREN.length());
		}
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (!str.equalsNextToken(OVER, ParseContext.TOKEN_DELIMITERS)) {
			if (ag != null) {
				//return simple aggregation
				return ag;
			}
			throw new ParseException(new EvalError("OVER expected.", this, str));
		}
		str.consumeChars(OVER.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		if (!str.startsWith(LEFT_PAREN)) {
			throw new ParseException(new EvalError("( expected.", this, str));
		}
		str.consumeChars(LEFT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		
		//ag is window aggregate
		if (ag != null) {
			wf = new WindowAggregate(ag);
		}
		
		//partition by
		currentIndex = str.getCurrentIndex();
		try {
			PartitionBy part = partitionBy.parse(str);
			wf.setPartitionBy(part);
			str.consumeChars(ParseContext.WHITE_SPACES);
		} catch(ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		
		//order by
		currentIndex = str.getCurrentIndex();
		try {
			WindowOrderBy order = orderBy.parse(str);
			wf.setOrderBy(order);
			str.consumeChars(ParseContext.WHITE_SPACES);
		} catch(ParseException e) {
			str.setCurrentIndex(currentIndex);
		}
		
		if (!str.startsWith(RIGHT_PAREN)) {
			throw new ParseException(new EvalError(") expected.", this, str));
		}
		str.consumeChars(RIGHT_PAREN.length());
		str.consumeChars(ParseContext.WHITE_SPACES);

		return wf;
	}
}
