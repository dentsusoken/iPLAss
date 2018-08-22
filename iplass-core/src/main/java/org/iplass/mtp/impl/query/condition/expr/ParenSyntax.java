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

package org.iplass.mtp.impl.query.condition.expr;

import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.Paren;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.condition.predicate.PredicateSyntax;

public class ParenSyntax implements Syntax<Condition>, QueryConstants {
	
	private OrSyntax or;
	private PredicateSyntax predicate;

	public void init(SyntaxContext context) {
		or = context.getSyntax(OrSyntax.class);
		predicate = context.getSyntax(PredicateSyntax.class);
	}

	public Condition parse(ParseContext str) throws ParseException {
		//TODO 実際は括弧が必要ない場合「((a=12))」とかにBracketValueをショートカットするロジックを入れるかどうか

		if (str.startsWith(LEFT_PAREN)) {
			
			int index = str.getCurrentIndex();

			try {
				str.consumeChars(LEFT_PAREN.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
				
				Condition nested = or.parse(str);
				
				if (!str.startsWith(RIGHT_PAREN)) {
					throw new ParseException(new EvalError(") expected.", this, str));
				}
				str.consumeChars(RIGHT_PAREN.length());
				str.consumeChars(ParseContext.WHITE_SPACES);
				
				return new Paren(nested);
			} catch (ParseException e) {
				str.setCurrentIndex(index);
				//in の( )の場合があるため
				return predicate.parse(str);
			}
		} else {
			return predicate.parse(str);
		}
	}

}
