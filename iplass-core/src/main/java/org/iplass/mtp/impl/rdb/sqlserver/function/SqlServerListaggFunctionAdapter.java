/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.sqlserver.function;

import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.aggregate.Listagg;
import org.iplass.mtp.entity.query.value.aggregate.WithinGroupSortSpec;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.common.function.ListaggFunctionAdapter;

/**
 * STRING_AGG(<expression>, <separator>) WITHIN GROUP(ORDER BY <sortSpec>) <br>
 * SQLServerでの制約
 * <ul>
 * <li>2017以降</li>
 * <li>DISTINCTできない</li>
 * <li>within groupのorder byを異なるものにできない</li>
 * </ul>
 *
 */
public class SqlServerListaggFunctionAdapter extends ListaggFunctionAdapter {
	
	public SqlServerListaggFunctionAdapter() {
		super();
		setSqlFunctionName("STRING_AGG");
	}
	
	@Override
	public void toSQL(FunctionContext context, Listagg function, RdbAdapter rdb) {
		context.append("STRING_AGG(");
		
		//Distinct is not supported by sqlServer.
		
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
	
	@Override
	protected void toOrderBySpecSQL(FunctionContext context, WithinGroupSortSpec ss) {
		if (ss.getNullOrderingSpec() != null) {
			switch (ss.getNullOrderingSpec()) {
			case FIRST:
				context.append("CASE WHEN ");
				context.appendArgument(ss.getSortKey());
				context.append(" IS NULL THEN 0 ELSE 1 END ASC, ");
				break;
			case LAST:
				context.append("CASE WHEN ");
				context.appendArgument(ss.getSortKey());
				context.append(" IS NULL THEN 0 ELSE 1 END DESC, ");
				break;
			default:
				break;
			}
		}
		context.appendArgument(ss.getSortKey());
		if (ss.getType() != null) {
			switch (ss.getType()) {
			case ASC:
				context.append(" ASC");
				break;
			case DESC:
				context.append(" DESC");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * STRING_AGG(args[1], 'args[2]') within group(order by args[3])
	 */
	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args.size() != 4) {
			throw new QueryException("LISTAGG argment mismatch.");
		}
		
		context.append("STRING_AGG(");
		
		//Distinct is not supported by sqlServer.
		
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
