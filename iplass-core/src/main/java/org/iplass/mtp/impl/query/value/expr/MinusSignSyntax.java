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

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.expr.MinusSign;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.primary.LiteralSyntax;
import org.iplass.mtp.impl.query.value.primary.PrimaryValueSyntax;

public class MinusSignSyntax implements Syntax<ValueExpression>, QueryConstants {
	
	private PrimaryValueSyntax primaryValue;
	private LiteralSyntax literalValue;

	public void init(SyntaxContext context) {
		primaryValue = context.getSyntax(PrimaryValueSyntax.class);
		literalValue = context.getSyntax(LiteralSyntax.class);
	}

	public ValueExpression parse(ParseContext str) throws ParseException {
		if (str.startsWith(MINUS)) {
			
			//short cut check for Literal value
			int currentIndex = str.getCurrentIndex();
			try {
				return literalValue.parse(str);
			} catch (ParseException e) {
				str.setCurrentIndex(currentIndex);
			}
			
			str.consumeChars(1);
			str.consumeChars(ParseContext.WHITE_SPACES);
			
			return new MinusSign(primaryValue.parse(str));
		} else {
			return primaryValue.parse(str);
		}
	}


}
