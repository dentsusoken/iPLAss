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

package org.iplass.mtp.impl.query.value.subquery;

import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.value.subquery.ScalarSubQuery;
import org.iplass.mtp.impl.parser.EvalError;
import org.iplass.mtp.impl.parser.ParseContext;
import org.iplass.mtp.impl.parser.ParseException;
import org.iplass.mtp.impl.parser.Syntax;
import org.iplass.mtp.impl.parser.SyntaxContext;
import org.iplass.mtp.impl.query.QueryConstants;
import org.iplass.mtp.impl.query.SubQuerySyntax;

public class ScalarSubQuerySyntax implements Syntax<ScalarSubQuery>, QueryConstants {
	
	private SubQuerySyntax subQuery;

	public void init(SyntaxContext context) {
		subQuery = context.getSyntax(SubQuerySyntax.class);
	}

	public ScalarSubQuery parse(ParseContext str) throws ParseException {
		int index = str.getCurrentIndex();
		SubQuery sq = subQuery.parse(str);
		if (sq.getQuery().getSelect().getSelectValues().size() != 1) {
			str.setCurrentIndex(index);
			throw new ParseException(new EvalError("scalar sub query must return a single value.", this, str));
		}
		
		return new ScalarSubQuery(sq);
	}

}
