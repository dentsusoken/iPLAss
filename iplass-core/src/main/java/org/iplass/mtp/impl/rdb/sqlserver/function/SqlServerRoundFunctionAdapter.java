/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.common.function.RoundTruncFunctionAdapter;

public class SqlServerRoundFunctionAdapter extends RoundTruncFunctionAdapter {
	private static final String SQL_FUNCTION_NAME = "ROUND";

	public SqlServerRoundFunctionAdapter(String field) {
		super(field, SQL_FUNCTION_NAME);
	}

	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		context.append(SQL_FUNCTION_NAME);
		context.append("(");
		if (function.getArguments() != null) {
			for (int i = 0; i < function.getArguments().size(); i++) {
				if (i != 0) {
					context.append(",");
				}
				context.appendArgument(function.getArguments().get(i));
			}
		}
		if (function.getArguments().size() == 1) {
			context.append(",0");
		}
		context.append(")");
	}

	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args, RdbAdapter rdb) {
		context.append(SQL_FUNCTION_NAME);
		context.append("(");
		if (args != null) {
			for (int i = 0; i < args.size(); i++) {
				if (i != 0) {
					context.append(",");
				}
				context.append(args.get(i));
			}
		}
		if (args.size() == 1) {
			context.append(",0");
		}
		context.append(")");
	}

}
