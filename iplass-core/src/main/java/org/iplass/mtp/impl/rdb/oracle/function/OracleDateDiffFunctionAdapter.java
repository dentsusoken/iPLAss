/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.DateTimeUnit;
import org.iplass.mtp.impl.rdb.oracle.OracleRdbAdapter;

public class OracleDateDiffFunctionAdapter implements FunctionAdapter<Function>, DateTimeUnit {
	
//	/*HOUR*/
//	select TRUNC((cast(to_timestamp('2012-03-01 12:12:12') as DATE)-cast('2011-03-01' as DATE))*24) from dual;
//	/*DAY*/
//	select TRUNC(cast(to_timestamp('2012-03-01 12:12:12') as DATE)-cast('2011-03-01' as DATE)) from dual;
//	/*MONTH*/
//	select TRUNC(MONTHS_BETWEEN(to_date('2012-03-05'), to_date('2011-03-01'))) from dual;
//	/*YEAR*/
//	select TRUNC(MONTHS_BETWEEN(to_date('2012-04-01'), to_date('2011-03-01'))/12) from dual;
	
	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		if (function.getArguments() == null || function.getArguments().size() != 3) {
			throw new QueryException(function.getName() + " must have 3 arguments.");
		}
		
		String unit = ((String) ((Literal) function.getArguments().get(0)).getValue()).toUpperCase();
		
		context.append("TRUNC(");
		
		switch (unit) {
		case YEAR:
			//MONTHS_BETWEEN / 12
			context.append(((OracleRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.appendArgument(function.getArguments().get(2));
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(")/12");
			break;
		case MONTH:
			//MONTHS_BETWEEN
			context.append(((OracleRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.appendArgument(function.getArguments().get(2));
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(")");
			break;
		case DAY:
			//Date - Date
			context.append("CAST((");
			context.appendArgument(function.getArguments().get(2));
			context.append(") AS DATE)-CAST((");
			context.appendArgument(function.getArguments().get(1));
			context.append(") AS DATE)");
			break;
		case HOUR:
			//(Date - Date) * 24
			context.append("(CAST((");
			context.appendArgument(function.getArguments().get(2));
			context.append(") AS DATE)-CAST((");
			context.appendArgument(function.getArguments().get(1));
			context.append(") AS DATE))*24");
			break;
		case MINUTE:
			//(Date - Date) * 1440
			context.append("(CAST((");
			context.appendArgument(function.getArguments().get(2));
			context.append(") AS DATE)-CAST((");
			context.appendArgument(function.getArguments().get(1));
			context.append(") AS DATE))*1440");
			break;
		case SECOND:
			//(Date - Date) * 86400
			context.append("(CAST((");
			context.appendArgument(function.getArguments().get(2));
			context.append(") AS DATE)-CAST((");
			context.appendArgument(function.getArguments().get(1));
			context.append(") AS DATE))*86400");
			break;
		default:
			throw new QueryException("unknown interval unit:" + unit);
		}
		
		context.append(")");
	}

	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		if (args == null || args.size() != 3) {
			throw new QueryException(getFunctionName() + " must have 3 arguments.");
		}
		
		String unit = args.get(0).toString().toUpperCase();
		
		context.append("TRUNC(");
		
		switch (unit) {
		case YEAR:
			//MONTHS_BETWEEN / 12
			context.append(((OracleRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(args.get(2));
			context.append(",");
			context.append(args.get(1));
			context.append(")/12");
			break;
		case MONTH:
			//MONTHS_BETWEEN
			context.append(((OracleRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(args.get(2));
			context.append(",");
			context.append(args.get(1));
			context.append(")");
			break;
		case DAY:
			//Date - Date
			context.append("CAST((");
			context.append(args.get(2));
			context.append(") AS DATE)-CAST((");
			context.append(args.get(1));
			context.append(") AS DATE)");
			break;
		case HOUR:
			//(Date - Date) * 24
			context.append("(CAST((");
			context.append(args.get(2));
			context.append(") AS DATE)-CAST((");
			context.append(args.get(1));
			context.append(") AS DATE))*24");
			break;
		case MINUTE:
			//(Date - Date) * 1440
			context.append("(CAST((");
			context.append(args.get(2));
			context.append(") AS DATE)-CAST((");
			context.append(args.get(1));
			context.append(") AS DATE))*1440");
			break;
		case SECOND:
			//(Date - Date) * 86400
			context.append("(CAST((");
			context.append(args.get(2));
			context.append(") AS DATE)-CAST((");
			context.append(args.get(1));
			context.append(") AS DATE))*86400");
			break;
		default:
			throw new QueryException("unknown interval unit:" + unit);
		}
		
		context.append(")");
	}
	
	@Override
	public String getFunctionName() {
		return "DATE_DIFF";
	}

	@Override
	public Class<?> getType(Function function, ArgumentTypeResolver typeResolver) {
		return Long.class;
	}


}
