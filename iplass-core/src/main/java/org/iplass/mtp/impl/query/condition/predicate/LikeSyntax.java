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

import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.condition.predicate.Like.CaseType;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.primary.LiteralSyntax;

public class LikeSyntax implements Syntax<Like>, QueryConstants {
	
	private LiteralSyntax literalSyntax;

	public void init(SyntaxContext context) {
		literalSyntax = context.getSyntax(LiteralSyntax.class);
	}

	public Like parse(ParseContext str) throws ParseException {
		if (!str.equalsNextToken(LIKE, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("like expected.", this, str));
		}
		
		str.consumeChars(LIKE.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		Literal pattern = literalSyntax.parse(str);
		if (!(pattern.getValue() instanceof String)) {
			throw new ParseException(new EvalError("string expression expected.", this, str));
		}
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		CaseType ct = CaseType.CI;
		if (str.equalsNextToken(CI, ParseContext.TOKEN_DELIMITERS)) {
			ct = CaseType.CI;
			str.consumeChars(CI.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
		} else if (str.equalsNextToken(CS, ParseContext.TOKEN_DELIMITERS)) {
			ct = CaseType.CS;
			str.consumeChars(CS.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		
		Like like = new Like();
		like.setPatternAsLiteral(pattern);
		like.setCaseType(ct);
		return like;
	}

}
