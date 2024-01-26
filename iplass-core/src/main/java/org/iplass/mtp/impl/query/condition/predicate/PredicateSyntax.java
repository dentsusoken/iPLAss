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

package org.iplass.mtp.impl.query.condition.predicate;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.ComparisonPredicate;
import org.iplass.mtp.entity.query.condition.predicate.Contains;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.condition.predicate.Predicate;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;


public class PredicateSyntax implements Syntax<Predicate>, QueryConstants {

	private ComparisonPredicateSyntax comparisonPredicate;
	private BetweenSyntax between;
	private InSyntax in;
	private IsNotNullSyntax isNotNull;
	private IsNullSyntax isNull;
	private LikeSyntax like;
//	private EntityFieldSyntax entityField;
//	private PrimaryValueSyntax primaryValue;
	private PolynomialSyntax primaryValue;
	private ContainsSyntax contains;


	public void init(SyntaxContext context) {
//		entityField = context.getSyntax(EntityFieldSyntax.class);
//		primaryValue = context.getSyntax(PrimaryValueSyntax.class);
		primaryValue = context.getSyntax(PolynomialSyntax.class);
		comparisonPredicate = context.getSyntax(ComparisonPredicateSyntax.class);
		between = context.getSyntax(BetweenSyntax.class);
		in = context.getSyntax(InSyntax.class);
		isNotNull = context.getSyntax(IsNotNullSyntax.class);
		isNull = context.getSyntax(IsNullSyntax.class);
		like = context.getSyntax(LikeSyntax.class);
		contains = context.getSyntax(ContainsSyntax.class);
	}

	public Predicate parse(ParseContext str) throws ParseException {

		//inの複数EntityField指定
		//TODO 別Syntax扱いがよい
		int index = str.getCurrentIndex();

		if (str.startsWith(LEFT_PAREN)) {
			str.consumeChars(LEFT_PAREN.length());
			str.consumeChars(ParseContext.WHITE_SPACES);

			try {
				boolean isFirst = true;
				List<ValueExpression> props = new ArrayList<ValueExpression>();
				while (isFirst || (!isFirst && str.startsWith(COMMA))) {
					if (isFirst) {
						isFirst = false;
					} else {
						str.consumeChars(COMMA.length());
						str.consumeChars(ParseContext.WHITE_SPACES);
					}
					
					props.add(primaryValue.parse(str));
				}
				
				if (!str.startsWith(RIGHT_PAREN) || props.size() < 2) {
					//inの複数EntityField指定ではなかった、、
					str.setCurrentIndex(index);
//					throw new ParseException(new EvalError(") expected.", this, str));
				} else {
					str.consumeChars(RIGHT_PAREN.length());
					str.consumeChars(ParseContext.WHITE_SPACES);
	
					In i = in.parse(str);
//					if (i.getSubQuery() == null) {
//						throw new ParseException(new EvalError("multi property in clause only alowed to subquery.", this, str));
//					}
					i.setPropertyList(props);
					return i;
				}
			} catch (ParseException e) {
				str.setCurrentIndex(index);
			}
		}

		if (str.equalsNextToken("contains", ParseContext.TOKEN_DELIMITERS)) {
			Contains con = contains.parse(str);
			return con;
		}


		ValueExpression ef = primaryValue.parse(str);

		if (str.isEnd()) {
			throw new ParseException(new EvalError("Predicate expected.", this, str));
		}
		index = str.getCurrentIndex();
		//TODO 総当り。。。効率化できないか？
		try {
			ComparisonPredicate cp = comparisonPredicate.parse(str);
			cp.setProperty(ef);
			return cp;
		} catch (ParseException e) {
			str.setCurrentIndex(index);
		}

		try {
			Like l = like.parse(str);
			l.setProperty(ef);
			return l;
		} catch (ParseException e) {
			str.setCurrentIndex(index);
		}

		try {
			Between b = between.parse(str);
			b.setProperty(ef);
			return b;
		} catch (ParseException e) {
			str.setCurrentIndex(index);
		}

		try {
			In i = in.parse(str);
			i.setProperty(ef);
			return i;
		} catch (ParseException e) {
			str.setCurrentIndex(index);
		}

		try {
			IsNotNull inn = isNotNull.parse(str);
			inn.setProperty(ef);
			return inn;
		} catch (ParseException e) {
			str.setCurrentIndex(index);
		}

		try {
			IsNull isn = isNull.parse(str);
			isn.setProperty(ef);
			return isn;
		} catch (ParseException e) {
			str.setCurrentIndex(index);
		}


		str.setCurrentIndex(index);
		throw new ParseException(new EvalError("Predicate expected.", this, str));
	}

}
