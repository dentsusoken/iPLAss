/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.query.value.window;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.window.PartitionBy;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.value.expr.PolynomialSyntax;

public class PartitionBySyntax implements Syntax<PartitionBy>, QueryConstants {
	
	private PolynomialSyntax polynomial;

	public PartitionBy parse(ParseContext str) throws ParseException {
		
		//PARTITION
		if (!str.equalsNextToken(PARTITION, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("PARTITION BY expected.", this, str));
		}
		str.consumeChars(PARTITION.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		//BY
		if (!str.equalsNextToken(BY, ParseContext.TOKEN_DELIMITERS)) {
			throw new ParseException(new EvalError("BY expected.", this, str));
		}
		str.consumeChars(BY.length());
		str.consumeChars(ParseContext.WHITE_SPACES);
		
		PartitionBy partitionBy = new PartitionBy();
		
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

			ValueExpression partitionField = polynomial.parse(str);
			partitionBy.add(partitionField);
		}
		
		return partitionBy;
	}


	public void init(SyntaxContext context) {
		polynomial = context.getSyntax(PolynomialSyntax.class);
	}

}
