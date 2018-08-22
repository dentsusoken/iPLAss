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

package org.iplass.mtp.impl.query;

import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.Refer;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.condition.expr.OrSyntax;
import org.iplass.mtp.impl.query.value.primary.EntityFieldSyntax;

public class ReferSyntax implements Syntax<Refer>, QueryConstants {
	
	private OrSyntax or;
	private EntityFieldSyntax entityField;
	private AsOfSyntax asOf;
	
	public void init(SyntaxContext context) {
		or = context.getSyntax(OrSyntax.class);
		entityField = context.getSyntax(EntityFieldSyntax.class);
		asOf = context.getSyntax(AsOfSyntax.class);
	}
	
	public Refer parse(ParseContext str) throws ParseException {
		
		if (!str.equalsNextToken(REFER, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("refer expected.", this, str));
		}
		str.consumeChars(REFER.length());
		if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
			throw new ParseException(new EvalError("space expected.", this, str));
		}
		
		EntityField referenceName = entityField.parse(str);
		
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//AS OF
		AsOf ao = null;
		if (str.equalsNextToken(AS, ParseContext.TOKEN_DELIMITERS)) {
			ao = asOf.parse(str);
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		
		//ON
		Condition cond = null;
		if (str.equalsNextToken(ON, ParseContext.TOKEN_DELIMITERS)) {
			str.consumeChars(ON.length());
			if (!str.consumeChars(ParseContext.WHITE_SPACES)) {
				throw new ParseException(new EvalError("space expected.", this, str));
			}
			
			cond = or.parse(str);
			str.consumeChars(ParseContext.WHITE_SPACES);
		}
		
		if (cond == null && ao == null) {
			throw new ParseException(new EvalError("'AS OF' or 'ON' expected.", this, str));
		}

		Refer r = new Refer(referenceName, cond);
		if (ao != null) {
			r.setAsOf(ao);
		}
		
		return r;
	}

}
