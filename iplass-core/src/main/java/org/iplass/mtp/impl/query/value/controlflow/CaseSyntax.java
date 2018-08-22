/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.query.value.controlflow;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.value.controlflow.Case;
import org.iplass.mtp.entity.query.value.controlflow.When;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;

public class CaseSyntax implements Syntax<Case>, QueryConstants {
	
	private WhenSyntax when;
	private ElseSyntax elseClause;

	public void init(SyntaxContext context) {
		when = context.getSyntax(WhenSyntax.class);
		elseClause = context.getSyntax(ElseSyntax.class);
	}

	public Case parse(ParseContext str) throws ParseException {
		if (!str.equalsNextToken(CASE, ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("CASE expected.", this, str));
		}
		str.consumeChars(CASE.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		Case caseClause = new Case();
		
		List<When> whenList = null;
		while (str.equalsNextToken(WHEN, ParseContext.WHITE_SPACES)) {
			When w = when.parse(str);
			if (whenList == null) {
				whenList = new ArrayList<When>();
			}
			whenList.add(w);
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		if (whenList != null) {
			caseClause.setWhen(whenList);
		}
		if (str.equalsNextToken(ELSE, ParseContext.WHITE_SPACES)) {
			caseClause.setElseClause(elseClause.parse(str));
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		
		if (!str.equalsNextToken(END, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("END expected.", this, str));
		}
		str.consumeChars(END.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		return caseClause;
	}

}
