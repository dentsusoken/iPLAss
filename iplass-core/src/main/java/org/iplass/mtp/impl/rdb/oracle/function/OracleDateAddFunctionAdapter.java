/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.oracle.function;

import java.sql.Timestamp;
import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.DateTimeUnit;
import org.iplass.mtp.impl.rdb.oracle.OracleRdbAdapter;

public class OracleDateAddFunctionAdapter implements FunctionAdapter, DateTimeUnit {
	
	//date + 1 * INTERVAL '1' DAY
	//date + 1 * INTERVAL '1' HOUR
	//date + 1 * INTERVAL '1' MINUTE
	//ADD_MONTHS(date, 1)
	//ADD_MONTHS(date, 1 * 12)
	
	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		if (function.getArguments() == null || function.getArguments().size() != 3) {
			throw new QueryException(function.getName() + " must have 3 arguments.");
		}
		
		String unit = ((String) ((Literal) function.getArguments().get(2)).getValue()).toUpperCase();
		
		switch (unit) {
		case YEAR:
			//ADD_MONTHS
			context.append(((OracleRdbAdapter) rdb).getAddMonthsFunction());
			context.append("(");
			context.appendArgument(function.getArguments().get(0));
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append("*12)");
			break;
		case MONTH:
			//ADD_MONTHS
			context.append(((OracleRdbAdapter) rdb).getAddMonthsFunction());
			context.append("(");
			context.appendArgument(function.getArguments().get(0));
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(")");
			break;
		case DAY:
		case HOUR:
		case MINUTE:
		case SECOND:
			//INTERVALでの計算
			context.append("((");
			context.appendArgument(function.getArguments().get(0));
			context.append(")+(");
			context.appendArgument(function.getArguments().get(1));
			context.append(")*INTERVAL '1' ");
			context.append(unit);
			context.append(")");
			break;
		default:
			throw new QueryException("unknown interval unit:" + unit);
		}
	}
	
	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args == null || args.size() != 3) {
			throw new QueryException(getFunctionName() + " must have 3 arguments.");
		}
		
		String unit = args.get(2).toString().toUpperCase();
		
		switch (unit) {
		case YEAR:
			//ADD_MONTHS
			context.append(((OracleRdbAdapter) rdb).getAddMonthsFunction());
			context.append("(");
			context.append(args.get(0));
			context.append(",");
			context.append(args.get(1));
			context.append("*12)");
			break;
		case MONTH:
			//ADD_MONTHS
			context.append(((OracleRdbAdapter) rdb).getAddMonthsFunction());
			context.append("(");
			context.append(args.get(0));
			context.append(",");
			context.append(args.get(1));
			context.append(")");
			break;
		case DAY:
		case HOUR:
		case MINUTE:
		case SECOND:
			//INTERVALでの計算
			context.append("((");
			context.append(args.get(0));
			context.append(")+(");
			context.append(args.get(1));
			context.append(")*INTERVAL '1' ");
			context.append(unit);
			context.append(")");
			break;
		default:
			throw new QueryException("unknown interval unit:" + unit);
		}
	}


	@Override
	public String getFunctionName() {
		return "DATE_ADD";
	}

	@Override
	public Class<?> getType(Function function, ArgumentTypeResolver typeResolver) {
		return Timestamp.class;
	}


}
