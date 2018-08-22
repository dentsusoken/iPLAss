/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public interface FunctionAdapter {
	
	public String getFunctionName();

	public abstract Class<?> getType(Function function,
			ArgumentTypeResolver typeResolver);

	public abstract void toSQL(FunctionContext context, Function function,
			RdbAdapter rdb);

	public abstract void toSQL(StringBuilder context, List<CharSequence> args, RdbAdapter rdb);
	
	public interface FunctionContext {
		
		public void append(String str);
		public void appendArgument(ValueExpression arg);
		
	}
	
	public interface ArgumentTypeResolver {
		public Class<?> resolveType(ValueExpression value);
	}

}
