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

package org.iplass.mtp.impl.rdb.mysql.function;

import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.aggregate.Listagg;
import org.iplass.mtp.entity.query.value.aggregate.WithinGroupSortSpec;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.common.function.ListaggFunctionAdapter;

/**
 * GROUP_CONCAT([DISTINCT] <expression> ORDER BY <sortSpec> SEPARATOR <separator>)
 */
public class MySqlListaggFunctionAdapter extends ListaggFunctionAdapter {

	public MySqlListaggFunctionAdapter() {
		super();
		setSqlFunctionName("GROUP_CONCAT");
	}

	@Override
	public void toSQL(FunctionContext context, Listagg function, RdbAdapter rdb) {
		context.append("GROUP_CONCAT(");
		if (function.isDistinct()) {
			context.append("DISTINCT ");
		}
		context.appendArgument(function.getValue());
		
		if (function.getWithinGroup() != null) {
			context.append(" ORDER BY ");
			for (int i = 0; i < function.getWithinGroup().getSortSpecList().size(); i++) {
				if (i > 0) {
					context.append(",");
				}
				toOrderBySpecSQL(context, function.getWithinGroup().getSortSpecList().get(i));
			}
		}
		
		if (function.getSeparator() != null) {
			context.append(" SEPARATOR ");
			context.appendArgument(function.getSeparator());
		} else if (rdb.getListaggDefaultSeparator() != null) {
			context.append(" SEPARATOR '");
			context.append(rdb.getListaggDefaultSeparator());
			context.append("'");
		}
		context.append(")");
		
	}
	
	@Override
	protected void toOrderBySpecSQL(FunctionContext context, WithinGroupSortSpec ss) {
		if (ss.getNullOrderingSpec() != null) {
			context.appendArgument(ss.getSortKey());
			switch (ss.getNullOrderingSpec()) {
			case FIRST:
				context.append(" IS NULL DESC,");
				break;
			case LAST:
				context.append(" IS NULL ASC,");
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
	 * GROUP_CONCAT(args[0] args[1] ORDER BY args[3] SEPARATOR 'args[2]')
	 */
	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args.size() != 4) {
			throw new QueryException("LISTAGG argment mismatch.");
		}
		
		context.append("GROUP_CONCAT(");
		if (args.get(0) != null) {
			context.append(args.get(0));
			context.append(" ");
		}
		context.append(args.get(1));
		
		if (args.get(3) != null) {
			context.append(" ORDER BY ");
			context.append(args.get(3));
		}
		
		if (args.get(2) != null) {
			context.append(" SEPARATOR '");
			context.append(args.get(2));
			context.append("'");
		} else if (rdb.getListaggDefaultSeparator() != null) {
			context.append(" SEPARATOR '");
			context.append(rdb.getListaggDefaultSeparator());
			context.append("'");
		}
		context.append(")");

	}

}
