/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import java.math.BigDecimal;
import java.util.List;

import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.rdb.adapter.function.BaseFunctionAdapter;

public class RoundTruncFunctionAdapter extends BaseFunctionAdapter {
	
	public RoundTruncFunctionAdapter(String funcName, String sqlFuncName) {
		super(funcName, sqlFuncName);
	}

	@Override
	public Class<?> getType(Function function, ArgumentTypeResolver typeResolver) {
		
		List<ValueExpression> args = function.getArguments();
		if (args == null) {
			return null;
		}
		if (args.size() == 1) {
			return Long.class;
//			return typeResolver.resolveType(args.get(0));
		}
		ValueExpression arg2 = (ValueExpression) args.get(1);
		if (arg2 instanceof Literal) {
			Object lv = ((Literal) arg2).getValue();
			if (lv instanceof Number) {
				long l = ((Number) lv).longValue();
				if (l <= 0) {
					return Long.class;
//					return typeResolver.resolveType(args.get(0));
				} else {
					return BigDecimal.class;
				}
			} 
		}
		
		throw new QueryException(function.getName() + " Function's second arg must Literal(Long) value.");
	}
	
}
