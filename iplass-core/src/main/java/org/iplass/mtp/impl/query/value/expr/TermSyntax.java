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

package org.iplass.mtp.impl.query.value.expr;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.expr.Term;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;


public class TermSyntax implements Syntax<ValueExpression>, QueryConstants {
	
	private static final int TYPE_ASTER = 1;
	private static final int TYPE_SOLID = 2;
	private static final int TYPE_OTHER = 3;
	

	private MinusSignSyntax minusSign;
	
	public void init(SyntaxContext context) {
		minusSign = context.getSyntax(MinusSignSyntax.class);

	}

	private int checkOp(ParseContext str) {
		if (str.startsWith(ASTER)) {
			return TYPE_ASTER;
		}
		if (str.startsWith(SOLID)) {
			return TYPE_SOLID;
		}
		return TYPE_OTHER;
	}

	public ValueExpression parse(ParseContext str) throws ParseException {
		
		//first expression
		ValueExpression firstExp = minusSign.parse(str);
		
		//2項目以降の処理
		List<ValueExpression> mulExp = null;
		List<ValueExpression> divExp = null;
		int type = TYPE_OTHER;
		while ((type = checkOp(str)) != TYPE_OTHER) {
			str.consumeChars(1);
			str.consumeChars(ParseContext.WHITE_SPACES);
			ValueExpression exp = minusSign.parse(str);
			switch (type) {
			case TYPE_ASTER:
				if (mulExp == null) {
					mulExp = new ArrayList<ValueExpression>();
					mulExp.add(firstExp);
				}
				mulExp.add(exp);
				break;
			case TYPE_SOLID:
				if (divExp == null) {
					divExp = new ArrayList<ValueExpression>();
				}
				divExp.add(exp);
				break;
			default:
				break;
			}
		}
		
		if (mulExp == null && divExp == null) {
			return firstExp;
		}
		
		if (mulExp == null) {
			mulExp = new ArrayList<ValueExpression>();
			mulExp.add(firstExp);
		}
		
		Term term = new Term();
		term.setMulValues(mulExp);
		term.setDivValues(divExp);
		
		return term;
		
	}

//	public void appendSb(StringBuilder sb, ValueExpression node) {
//		if (node instanceof Term) {
//			Term termNode = (Term) node;
//			
//			if (termNode.getMulValues() != null) {
//				for (int i = 0; i < termNode.getMulValues().size(); i++) {
//					if (i != 0) {
//						sb.append(PLUS);
//					}
//					minusSign.appendSb(sb, termNode.getMulValues().get(i));
//				}
//			}
//			if (termNode.getDivValues() != null) {
//				for (int i = 0; i < termNode.getDivValues().size(); i++) {
//					sb.append(MINUS);
//					minusSign.appendSb(sb, termNode.getDivValues().get(i));
//				}
//			}
//		} else {
//			minusSign.appendSb(sb, node);
//		}
//	}
}
