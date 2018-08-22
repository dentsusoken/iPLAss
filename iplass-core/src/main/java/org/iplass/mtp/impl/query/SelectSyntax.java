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

package org.iplass.mtp.impl.query;

import org.iplass.mtp.entity.query.Select;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.hint.HintCommentSyntax;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class SelectSyntax implements Syntax<Select>, QueryConstants {
	
	private PolynomialSyntax polynomial;
	private HintCommentSyntax hintComment;

	public Select parse(ParseContext str) throws ParseException {
		
		//SELECT
		if (!str.equalsNextToken(SELECT, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("SELECT expected.", this, str));
		}
		str.consumeChars(SELECT.length());
		
		Select select = new Select();
		
		boolean hasSpace = str.consumeChars(ParseContext.WHITE_SPACES);
		
		// /*+ (Hint Comment)
		if (str.startsWith(LEFT_HINT_COMMENT)) {
			select.setHintComment(hintComment.parse(str));
			str.consumeChars(ParseContext.WHITE_SPACES);
		} else {
			if (!hasSpace) {
				throw new ParseException(new EvalError("space expected.", this, str));
			}
		}
		
		//DISTINCT
		if (str.equalsNextToken(DISTINCT, ParseContext.TOKEN_DELIMITERS)) {
			str.consumeChars(DISTINCT.length());
			select.setDistinct(true);
			
			if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
				throw new ParseException(new EvalError("space expected.", this, str));
			}
		}
		
		//ValueExpression
		boolean isFirst = true;
		while (true) {
			if (isFirst) {
				isFirst = false;
			} else if (!str.startsWith(COMMA)) {
				break;
			} else {
				str.consumeChars(COMMA.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
			}

			ValueExpression val = polynomial.parse(str);
			select.add(val);
		}
		
		return select;
	}


	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
		hintComment = context.getSyntax(HintCommentSyntax.class);
	}


//	public void appendSb(StringBuilder sb, Select node) {
//		
//		sb.append(SELECT).append(" ");
//		if (node.isDistinct()) {
//			sb.append(DISTINCT).append(" ");
//		}
//		if (node.getSelectValues() != null) {
//			for (int i = 0; i < node.getSelectValues().size(); i++) {
//				if (i != 0) {
//					sb.append(COMMA);
//				}
//				polynomial.appendSb(node.getSelectValues().get(i));
//			}
//		}
//	}


}
