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

package org.iplass.mtp.impl.query.value.expr;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.expr.Polynomial;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;


public class PolynomialSyntax implements Syntax<ValueExpression>, QueryConstants {
	
	private static final int TYPE_PLUS = 1;
	private static final int TYPE_MINUS = 2;
	private static final int TYPE_OTHER = 3;
	
	private TermSyntax term;

	private int checkOp(ParseContext str) {
		if (str.startsWith(PLUS)) {
			return TYPE_PLUS;
		}
		if (str.startsWith(MINUS)) {
			return TYPE_MINUS;
		}
		return TYPE_OTHER;
	}

	public ValueExpression parse(ParseContext str) throws ParseException {
		
		//first expression
		ValueExpression firstExp = term.parse(str);
		
		//2項目以降の処理
		List<ValueExpression> addExp = null;
		List<ValueExpression> subExp = null;
		int type = TYPE_OTHER;
		while ((type = checkOp(str)) != TYPE_OTHER) {
			str.consumeChars(1);
			str.consumeChars(ParseContext.WHITE_SPACES);
			ValueExpression exp = term.parse(str);
			switch (type) {
			case TYPE_PLUS:
				if (addExp == null) {
					addExp = new ArrayList<ValueExpression>();
					addExp.add(firstExp);
				}
				addExp.add(exp);
				break;
			case TYPE_MINUS:
				if (subExp == null) {
					subExp = new ArrayList<ValueExpression>();
				}
				subExp.add(exp);
				break;
			default:
				break;
			}
		}
		
		if (addExp == null && subExp == null) {
			return firstExp;
		}
		
		if (addExp == null) {
			addExp = new ArrayList<ValueExpression>();
			addExp.add(firstExp);
		}
		
		Polynomial pol = new Polynomial();
		pol.setAddValues(addExp);
		pol.setSubValues(subExp);
		
		return pol;
		
	}

	public void init(SyntaxContext context) {
		term = context.getSyntax(TermSyntax.class);
	}

//	public void appendSb(StringBuilder sb, ValueExpression node) {
//		if (node instanceof Polynomial) {
//			Polynomial polNode = (Polynomial) node;
//			
//			if (polNode.getAddValues() != null) {
//				for (int i = 0; i < polNode.getAddValues().size(); i++) {
//					if (i != 0) {
//						sb.append(PLUS);
//					}
//					term.appendSb(sb, polNode.getAddValues().get(i));
//				}
//			}
//			if (polNode.getSubValues() != null) {
//				for (int i = 0; i < polNode.getSubValues().size(); i++) {
//					sb.append(MINUS);
//					term.appendSb(sb, polNode.getAddValues().get(i));
//				}
//			}
//		} else {
//			term.appendSb(sb, node);
//		}
//	}

}
