/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.postgresql.function;

import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.DateTimeUnit;

public class PostgreSQLDateDiffFunctionAdapter implements FunctionAdapter, DateTimeUnit {
	
//	/* HOUR */
//	select trunc(extract(EPOCH FROM to_timestamp('2012-03-01 01:01:01','yyyy-mm-dd hh:mi:ss')-to_timestamp('2011-03-01','yyyy-mm-dd'))/60/60);
//	/* DAY */
//	select trunc(extract(EPOCH FROM to_timestamp('2012-03-01 01:01:01','yyyy-mm-dd hh:mi:ss')-to_timestamp('2011-03-01','yyyy-mm-dd'))/60/60/24);
//	/* MONTH */
//	select extract(YEAR FROM age(to_date('2012-03-01','yyyy-mm-dd'),to_date('2011-03-01','yyyy-mm-dd')))*12
//	     + extract(MONTH FROM age(to_date('2012-03-01','yyyy-mm-dd'),to_date('2011-03-01','yyyy-mm-dd')));
//	/* YEAR */
//	select extract(YEAR FROM age(to_date('2012-03-01','yyyy-mm-dd'),to_date('2011-03-01','yyyy-mm-dd')));
	
	public PostgreSQLDateDiffFunctionAdapter() {
	}
	
	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		if (function.getArguments() == null || function.getArguments().size() != 3) {
			throw new QueryException(function.getName() + " must have 3 arguments.");
		}
		
		String unit = ((String) ((Literal) function.getArguments().get(0)).getValue()).toUpperCase();
		
		switch (unit) {
		case YEAR:
			//EXTRACT(YEAR FROM AGE(date2, date1));
			context.append("EXTRACT(YEAR FROM AGE(");
			context.appendArgument(function.getArguments().get(2));
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append("))");
			break;
		case MONTH:
			//(EXTRACT(YEAR FROM AGE(date2, date1))*12 + EXTRACT(MONTH FROM AGE(date2, date1)))
			context.append("(EXTRACT(YEAR FROM AGE(");
			context.appendArgument(function.getArguments().get(2));
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append("))*12+EXTRACT(MONTH FROM AGE(");
			context.appendArgument(function.getArguments().get(2));
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(")))");
			break;
		case DAY:
			//TRUNC(EXTRACT(EPOCH FROM CAST((date2) AS TIMESTAMP) - CAST((date1) AS TIMESTAMP)) / 86400)
			context.append("TRUNC(EXTRACT(EPOCH FROM CAST((");
			context.appendArgument(function.getArguments().get(2));
			context.append(") AS TIMESTAMP)-CAST((");
			context.appendArgument(function.getArguments().get(1));
			context.append(") AS TIMESTAMP))/86400)");
			break;
		case HOUR:
			//TRUNC(EXTRACT(EPOCH FROM CAST((date2) AS TIMESTAMP) - CAST((date1) AS TIMESTAMP)) / 1440)
			context.append("TRUNC(EXTRACT(EPOCH FROM CAST((");
			context.appendArgument(function.getArguments().get(2));
			context.append(") AS TIMESTAMP)-CAST((");
			context.appendArgument(function.getArguments().get(1));
			context.append(") AS TIMESTAMP))/3600)");
			break;
		case MINUTE:
			//TRUNC(EXTRACT(EPOCH FROM CAST((date2) AS TIMESTAMP) - CAST((date1) AS TIMESTAMP)) / 60)
			context.append("TRUNC(EXTRACT(EPOCH FROM CAST((");
			context.appendArgument(function.getArguments().get(2));
			context.append(") AS TIMESTAMP)-CAST((");
			context.appendArgument(function.getArguments().get(1));
			context.append(") AS TIMESTAMP))/60)");
			break;
		case SECOND:
			//EXTRACT(EPOCH FROM CAST((date2) AS TIMESTAMP) - CAST((date1) AS TIMESTAMP))
			context.append("EXTRACT(EPOCH FROM CAST((");
			context.appendArgument(function.getArguments().get(2));
			context.append(") AS TIMESTAMP)-CAST((");
			context.appendArgument(function.getArguments().get(1));
			context.append(") AS TIMESTAMP))");
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
		
		String unit = args.get(0).toString().toUpperCase();
		
		switch (unit) {
		case YEAR:
			//EXTRACT(YEAR FROM AGE(date2, date1));
			context.append("EXTRACT(YEAR FROM AGE(");
			context.append(args.get(2));
			context.append(",");
			context.append(args.get(1));
			context.append("))");
			break;
		case MONTH:
			//(EXTRACT(YEAR FROM AGE(date2, date1))*12 + EXTRACT(MONTH FROM AGE(date2, date1)))
			context.append("(EXTRACT(YEAR FROM AGE(");
			context.append(args.get(2));
			context.append(",");
			context.append(args.get(1));
			context.append("))*12+EXTRACT(MONTH FROM AGE(");
			context.append(args.get(2));
			context.append(",");
			context.append(args.get(1));
			context.append(")))");
			break;
		case DAY:
			//TRUNC(EXTRACT(EPOCH FROM CAST((date2) AS TIMESTAMP) - CAST((date1) AS TIMESTAMP)) / 86400)
			context.append("TRUNC(EXTRACT(EPOCH FROM CAST((");
			context.append(args.get(2));
			context.append(") AS TIMESTAMP)-CAST((");
			context.append(args.get(1));
			context.append(") AS TIMESTAMP))/86400)");
			break;
		case HOUR:
			//TRUNC(EXTRACT(EPOCH FROM CAST((date2) AS TIMESTAMP) - CAST((date1) AS TIMESTAMP)) / 1440)
			context.append("TRUNC(EXTRACT(EPOCH FROM CAST((");
			context.append(args.get(2));
			context.append(") AS TIMESTAMP)-CAST((");
			context.append(args.get(1));
			context.append(") AS TIMESTAMP))/3600)");
			break;
		case MINUTE:
			//TRUNC(EXTRACT(EPOCH FROM CAST((date2) AS TIMESTAMP) - CAST((date1) AS TIMESTAMP)) / 60)
			context.append("TRUNC(EXTRACT(EPOCH FROM CAST((");
			context.append(args.get(2));
			context.append(") AS TIMESTAMP)-CAST((");
			context.append(args.get(1));
			context.append(") AS TIMESTAMP))/60)");
			break;
		case SECOND:
			//EXTRACT(EPOCH FROM CAST((date2) AS TIMESTAMP) - CAST((date1) AS TIMESTAMP))
			context.append("EXTRACT(EPOCH FROM CAST((");
			context.append(args.get(2));
			context.append(") AS TIMESTAMP)-CAST((");
			context.append(args.get(1));
			context.append(") AS TIMESTAMP))");
			break;
		default:
			throw new QueryException("unknown interval unit:" + unit);
		}
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
