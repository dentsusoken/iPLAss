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

package org.iplass.mtp.impl.rdb.common.function;

import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.aggregate.Listagg;
import org.iplass.mtp.entity.query.value.aggregate.WithinGroup;
import org.iplass.mtp.entity.query.value.aggregate.WithinGroupSortSpec;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.AggregateFunctionAdapter;

/**
 * SQL2016ベースのAdapter
 * 
 * LISTAGG([DISTINCT] <expression>, <separator>) WITHIN GROUP(ORDER BY <sortSpec>)
 * 
 * @author K.Higuchi
 *
 */
public class ListaggFunctionAdapter extends AggregateFunctionAdapter<Listagg> {
	
	public ListaggFunctionAdapter() {
		super("LISTAGG", String.class);
	}
	
	@Override
	public void toSQL(FunctionContext context, Listagg function, RdbAdapter rdb) {
		context.append("LISTAGG(");
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
		context.append(")");
		
		toWithinGroupSQL(context, function.getWithinGroup());
	}
	
	protected void toWithinGroupSQL(FunctionContext context, WithinGroup wg) {
		if (wg != null) {
			context.append(" WITHIN GROUP(ORDER BY ");
			for (int i = 0; i < wg.getSortSpecList().size(); i++) {
				if (i > 0) {
					context.append(",");
				}
				toOrderBySpecSQL(context, wg.getSortSpecList().get(i));
			}
			context.append(")");
		}
	}
	
	protected void toOrderBySpecSQL(FunctionContext context, WithinGroupSortSpec ss) {
		context.appendArgument(ss.getSortKey());
		if (ss.getType() != null) {
			context.append(" ");
			context.append(ss.getType().toString());
		}
		if (ss.getNullOrderingSpec() != null) {
			context.append(" NULLS ");
			context.append(ss.getNullOrderingSpec().toString());
		}
	}

	/**
	 * listagg(args[0] args[1], 'args[2]') within group(order by args[3])
	 */
	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args.size() != 4) {
			throw new QueryException("LISTAGG argment mismatch.");
		}
		
		context.append("LISTAGG(");
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
		context.append(")");

		if (args.get(3) != null) {
			context.append(" WITHIN GROUP(ORDER BY ");
			context.append(args.get(3));
			context.append(")");
		}
	}

}
