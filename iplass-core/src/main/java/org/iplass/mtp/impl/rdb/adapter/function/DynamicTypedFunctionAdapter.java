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

package org.iplass.mtp.impl.rdb.adapter.function;

import java.math.BigDecimal;
import java.util.List;

import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.Function;

public class DynamicTypedFunctionAdapter extends BaseFunctionAdapter {
	
	private int[] typeArgIndex;
	
	public DynamicTypedFunctionAdapter(String functionName, int[] typeArgIndex) {
		super(functionName);
		this.typeArgIndex = typeArgIndex;
	}
	public DynamicTypedFunctionAdapter(String functionName, String sqlFunctionName, int[] typeArgIndex) {
		super(functionName, sqlFunctionName);
		this.typeArgIndex = typeArgIndex;
	}
	
	@Override
	public Class<?> getType(Function function, ArgumentTypeResolver typeResolver) {
		//引数から動的に判断
		List<ValueExpression> args = function.getArguments();
		if (args == null) {
			return null;
		}
		
		Class<?> type = typeResolver.resolveType(getIfNotNull(args, typeArgIndex[0]));
		for (int i = 1; i < typeArgIndex.length; i++) {
			if (type == null) {
				break;
			}
			type = whichType(typeResolver.resolveType(getIfNotNull(args, typeArgIndex[i])), type);
		}
		return type;
	}
	
	private ValueExpression getIfNotNull(List<ValueExpression> args, int index) {
		if (args.size() > index) {
			return args.get(index);
		} else {
			return null;
		}
	}
	
	private Class<?> whichType(Class<?> type1, Class<?> type2) {
		
		//優先順位
		//Null > Double > Decimal > Long > String(必要?)
		if (type1 == null || type2 == null) {
			return null;
		}
		if (type1 == Double.class || type2 == Double.class) {
			return Double.class;
		}
		if (type1 == BigDecimal.class || type2 == BigDecimal.class) {
			return BigDecimal.class;
		}
		if (type1 == Long.class || type2 == Long.class) {
			return Long.class;
		}
		if (type1 == String.class || type2 == String.class) {
			return String.class;
		} else {
			return null;
		}
	}
	
}
