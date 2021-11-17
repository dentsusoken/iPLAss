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

package org.iplass.mtp.impl.rdb.common.function;

import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.AggregateFunctionAdapter;

public class CountFunctionAdapter extends AggregateFunctionAdapter<Count> {
	
	public CountFunctionAdapter() {
		super("COUNT", Long.class);
	}
	
	@Override
	public void toSQL(FunctionContext context, Count function, RdbAdapter rdb) {
		if (function.getValue() == null) {
			context.append("COUNT(*)");
		} else {
			context.append("COUNT(");
			if (function.isDistinct()) {
				context.append("DISTINCT ");
			}
			context.appendArgument(function.getValue());
			context.append(")");
		}
	}

	/**
	 * argsがnull or args.length==0: count(*)
	 * args.length==1: count(args[0])
	 * args.lenght==2: count(args[0] args[1])
	 */
	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args == null || args.size() == 0) {
			context.append("COUNT(*)");
		} else if (args.size() == 1) {
			context.append("COUNT(");
			context.append(args.get(0));
			context.append(")");
		} else if (args.size() == 2) {
			context.append("COUNT(");
			context.append(args.get(0));
			context.append(" ");
			context.append(args.get(1));
			context.append(")");
		} else {
			throw new QueryException("COUNT argment mismatch.");
		}
	}

}
