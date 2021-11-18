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

package org.iplass.mtp.impl.rdb.adapter.function;

import java.util.List;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.aggregate.Aggregate;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class AggregateFunctionAdapter<T extends Aggregate> implements FunctionAdapter<T> {
	
	private String functionName;
	private String sqlFunctionName;

	private Class<?> type;
	
	public AggregateFunctionAdapter(String functionName, Class<?> type) {
		this(functionName, functionName, type);
	}
	
	public AggregateFunctionAdapter(String functionName, String sqlFunctionName, Class<?> type) {
		this.functionName = functionName;
		this.sqlFunctionName = sqlFunctionName;
		this.type = type;
	}
	
	public String getSqlFunctionName() {
		return sqlFunctionName;
	}

	public void setSqlFunctionName(String sqlFunctionName) {
		this.sqlFunctionName = sqlFunctionName;
	}

	@Override
	public Class<?> getType(T function, ArgumentTypeResolver typeResolver) {
		if (type != null) {
			return type;
		}
		
		ValueExpression value = function.getValue();
		if (value != null) {
			return typeResolver.resolveType(value);
		} else {
			return null;
		}
	};
	
	@Override
	public void toSQL(FunctionContext context, T function, RdbAdapter rdb) {
		context.append(sqlFunctionName);
		context.append("(");
		context.appendArgument(function.getValue());
		context.append(")");
	}

	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args, RdbAdapter rdb) {
		context.append(sqlFunctionName);
		context.append("(");
		if (args != null) {
			for (int i = 0; i < args.size(); i++) {
				if (i != 0) {
					context.append(",");
				}
				context.append(args.get(i));
			}
		}
		context.append(")");
	}

	@Override
	public String getFunctionName() {
		return functionName;
	}

}
