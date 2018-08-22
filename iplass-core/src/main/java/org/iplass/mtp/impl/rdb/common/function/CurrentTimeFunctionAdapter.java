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

package org.iplass.mtp.impl.rdb.common.function;

import java.sql.Time;
import java.util.List;

import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;

/**
 * テナントに設定されたTimezoneに従った現在のローカル時刻を返す関数。
 *
 * @author K.Higuchi
 *
 */
public class CurrentTimeFunctionAdapter implements FunctionAdapter {

	@Override
	public String getFunctionName() {
		return "CURRENT_TIME";
	}

	@Override
	public Class<?> getType(Function function, ArgumentTypeResolver typeResolver) {
		return Time.class;
	}

	@Override
	public void toSQL(FunctionContext context, Function function, RdbAdapter rdb) {
		context.append(rdb.toTimeExpression(ExecuteContext.getCurrentContext().getCurrentLocalTime()));
	}

	@Override
	public void toSQL(StringBuilder context, List<CharSequence> args,
			RdbAdapter rdb) {
		context.append(rdb.toTimeExpression(ExecuteContext.getCurrentContext().getCurrentLocalTime()));
	}

}
