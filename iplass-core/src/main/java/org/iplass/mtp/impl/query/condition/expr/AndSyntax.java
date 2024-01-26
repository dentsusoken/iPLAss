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

package org.iplass.mtp.impl.query.condition.expr;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;


public class AndSyntax implements Syntax<Condition>, QueryConstants {
	
	private NotSyntax not;

	public void init(SyntaxContext context) {
		not = context.getSyntax(NotSyntax.class);
	}

	public Condition parse(ParseContext str) throws ParseException {
		//first condition
		Condition firstCond = not.parse(str);
		
		//2項目以降
		List<Condition> condList = null;
		while (str.equalsNextToken(AND, ParseContext.TOKEN_DELIMITERS)) {
			str.consumeChars(AND.length());
			str.consumeChars(ParseContext.WHITE_SPACES);
			Condition cond = not.parse(str);
			if (condList == null) {
				condList = new ArrayList<Condition>();
				condList.add(firstCond);
			}
			condList.add(cond);
		}
		
		if (condList == null) {
			return firstCond;
		}
		
		And and = new And();
		and.setConditions(condList);
		
		return and;
	}

}
