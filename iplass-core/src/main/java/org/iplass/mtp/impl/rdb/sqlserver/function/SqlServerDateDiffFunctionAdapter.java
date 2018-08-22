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
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.DateTimeUnit;
import org.iplass.mtp.impl.rdb.sqlserver.SqlServerRdbAdapter;

public class SqlServerDateDiffFunctionAdapter implements FunctionAdapter, DateTimeUnit {

//	/*HOUR*/
//	select DATEDIFF(SECOND, '2011-03-01', '2012-03-01 12:12:12')/60/60;
//	/*DAY*/
//	select DATEDIFF(SECOND, '2011-03-01', '2012-03-01 12:12:12')/60/60/24;
//	/*MONTH*/
//	select DATEDIFF(MONTH, '2011-03-01', '2012-03-05');
//	/*YEAR*/
//	select DATEDIFF(YEAR, '2011-03-01', '2012-04-01');

	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		if (function.getArguments() == null || function.getArguments().size() != 3) {
			throw new QueryException(function.getName() + " must have 3 arguments.");
		}

		String unit = ((String) ((Literal) function.getArguments().get(0)).getValue()).toUpperCase();

		context.append("(");

		switch (unit) {
		case YEAR:
			//YEAR
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(YEAR);
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(",");
			context.appendArgument(function.getArguments().get(2));
			context.append(")");
			break;
		case MONTH:
			//MONTHS_BETWEEN
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(MONTH);
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(",");
			context.appendArgument(function.getArguments().get(2));
			context.append(")");
			break;
		case DAY:
			//Date - Date
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(SECOND);
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(",");
			context.appendArgument(function.getArguments().get(2));
			context.append(")/60/60/24");
			break;
		case HOUR:
			//(Date - Date) * 24
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(SECOND);
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(",");
			context.appendArgument(function.getArguments().get(2));
			context.append(")/60/60");
			break;
		case MINUTE:
			//(Date - Date) * 1440
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(SECOND);
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(",");
			context.appendArgument(function.getArguments().get(2));
			context.append(")/60");
			break;
		case SECOND:
			//(Date - Date) * 86400
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(SECOND);
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(",");
			context.appendArgument(function.getArguments().get(2));
			context.append(")");
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

		context.append("(");

		switch (unit) {
		case YEAR:
			//YEAR
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(YEAR);
			context.append(",");
			context.append(args.get(1));
			context.append(",");
			context.append(args.get(2));
			context.append(")");
			break;
		case MONTH:
			//MONTHS_BETWEEN
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(MONTH);
			context.append(",");
			context.append(args.get(1));
			context.append(",");
			context.append(args.get(2));
			context.append(")");
			break;
		case DAY:
			//Date - Date
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(SECOND);
			context.append(",");
			context.append(args.get(1));
			context.append(",");
			context.append(args.get(2));
			context.append(")/60/60/24");
			break;
		case HOUR:
			//(Date - Date) * 24
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(SECOND);
			context.append(",");
			context.append(args.get(1));
			context.append(",");
			context.append(args.get(2));
			context.append(")/60/60");
			break;
		case MINUTE:
			//(Date - Date) * 1440
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(SECOND);
			context.append(",");
			context.append(args.get(1));
			context.append(",");
			context.append(args.get(2));
			context.append(")/60");
			break;
		case SECOND:
			//(Date - Date) * 86400
			context.append(((SqlServerRdbAdapter) rdb).getMonthsBetweenFunction());
			context.append("(");
			context.append(SECOND);
			context.append(",");
			context.append(args.get(1));
			context.append(",");
			context.append(args.get(2));
			context.append(")");
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
