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

package org.iplass.mtp.impl.rdb.mysql.function;

import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.DateTimeUnit;

public class MysqlDateDiffFunctionAdapter implements FunctionAdapter<Function>, DateTimeUnit {
	
//	/*HOUR*/
//	select timestampdiff(HOUR, '2011-03-01', '2012-03-01 01:01:01');
//
//	/*DAY*/
//	select timestampdiff(DAY, '2011-03-01', '2012-03-01');
//
//	/*MONTH*/
//	select timestampdiff(MONTH,'2011-03-01','2012-03-01');
//
//	select timestampdiff(MONTH,'2012-02-29','2012-03-29');
//
//	/*YEAR*/
//	select timestampdiff(YEAR,'2011-03-01','2012-03-01');
	
	public MysqlDateDiffFunctionAdapter() {
	}
	
	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		if (function.getArguments() == null || function.getArguments().size() != 3) {
			throw new QueryException(function.getName() + " must have 3 arguments.");
		}
		
		String unit = ((String) ((Literal) function.getArguments().get(0)).getValue()).toUpperCase();
		
		if (unit.equals(MONTH)
				|| unit.equals(YEAR)
				|| unit.equals(DAY)
				|| unit.equals(HOUR)
				|| unit.equals(MINUTE)
				|| unit.equals(SECOND)) {
			//date_add
			context.append("TIMESTAMPDIFF(");
			context.append(unit);
			context.append(",");
			context.appendArgument(function.getArguments().get(1));
			context.append(",");
			context.appendArgument(function.getArguments().get(2));
			context.append(") ");
		} else {
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
		
		if (unit.equals(MONTH)
				|| unit.equals(YEAR)
				|| unit.equals(DAY)
				|| unit.equals(HOUR)
				|| unit.equals(MINUTE)
				|| unit.equals(SECOND)) {
			//date_add
			context.append("TIMESTAMPDIFF(");
			context.append(unit);
			context.append(",");
			context.append(args.get(1));
			context.append(",");
			context.append(args.get(2));
			context.append(") ");
		} else {
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
