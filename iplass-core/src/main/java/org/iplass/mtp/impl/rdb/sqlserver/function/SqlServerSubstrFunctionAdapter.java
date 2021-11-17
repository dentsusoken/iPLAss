/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;

public class SqlServerSubstrFunctionAdapter implements FunctionAdapter<Function> {

	private String field;

	public SqlServerSubstrFunctionAdapter(String field) {
		this.field = field;
	}

	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		if (function.getArguments() == null || function.getArguments().size() < 2 || function.getArguments().size() > 3) {
			throw new QueryException(function.getName() + " must have two or three arguments.");
		}
		context.append("SUBSTRING(");
		context.appendArgument(function.getArguments().get(0));
		context.append(", ");
		context.appendArgument(function.getArguments().get(1));
		if (function.getArguments().size() == 3) {
			context.append(", ");
			context.appendArgument(function.getArguments().get(2));
		} else {
			context.append(", LEN(");
			context.appendArgument(function.getArguments().get(0));
			context.append(")");
		}
		context.append(")");
	}

	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args == null || args.size() < 2 || args.size() > 3) {
			throw new QueryException(getFunctionName() + " must have two or three arguments.");
		}
		context.append("SUBSTRING(");
		context.append(args.get(0));
		context.append(", ");
		context.append(args.get(1));
		if (args.size() == 3) {
			context.append(", ");
			context.append(args.get(2));
		} else {
			context.append(", LEN(");
			context.append(args.get(0));
			context.append(")");
		}
		context.append(")");
	}

	@Override
	public String getFunctionName() {
		return field;
	}

	@Override
	public Class<?> getType(Function function, ArgumentTypeResolver typeResolver) {
		return String.class;
	}

}