/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.postgresql.function;

import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.aggregate.Listagg;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.common.function.ListaggFunctionAdapter;

/**
 * STRING_AGG([DISTINCT] <expression>, <separator> ORDER BY <sortSpec>)
 *
 */
public class PostgreSQLListaggFunctionAdapter extends ListaggFunctionAdapter {
	
	public PostgreSQLListaggFunctionAdapter() {
		super();
		setSqlFunctionName("STRING_AGG");
	}
	
	@Override
	public void toSQL(FunctionContext context, Listagg function, RdbAdapter rdb) {
		context.append("STRING_AGG(");
		if (function.isDistinct()) {
			context.append("DISTINCT ");
		}
		context.appendArgument(function.getValue());
		if (function.getSeparator() != null) {
			context.append(",");
			context.appendArgument(function.getSeparator());
		} else if (rdb.getListaggDefaultSeparator() != null) {
			context.append(",'");
			context.append(rdb.getListaggDefaultSeparator());
			context.append("'");
		}
		
		if (function.getWithinGroup() != null) {
			context.append(" ORDER BY ");
			for (int i = 0; i < function.getWithinGroup().getSortSpecList().size(); i++) {
				if (i > 0) {
					context.append(",");
				}
				toOrderBySpecSQL(context, function.getWithinGroup().getSortSpecList().get(i));
			}
		}
		
		context.append(")");
	}

	/**
	 * STRING_AGG(args[0] args[1], 'args[2]' order by args[3])
	 */
	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args.size() != 4) {
			throw new QueryException("LISTAGG argment mismatch.");
		}
		
		context.append("STRING_AGG(");
		if (args.get(0) != null) {
			context.append(args.get(0));
			context.append(" ");
		}
		context.append(args.get(1));
		if (args.get(2) != null) {
			context.append(",'");
			context.append(args.get(2));
			context.append("'");
		} else if (rdb.getListaggDefaultSeparator() != null) {
			context.append(",'");
			context.append(rdb.getListaggDefaultSeparator());
			context.append("'");
		}
		if (args.get(3) != null) {
			context.append(" ORDER BY ");
			context.append(args.get(3));
			context.append(")");
		}
	}

}
