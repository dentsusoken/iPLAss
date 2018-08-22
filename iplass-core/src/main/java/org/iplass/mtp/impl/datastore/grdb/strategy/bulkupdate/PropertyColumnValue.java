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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.bulk.DynamicColumnValue;

public class PropertyColumnValue extends DynamicColumnValue {
	private BaseRdbTypeAdapter type;
	private RdbAdapter rdb;
	private boolean asNative;
	private Object defaultValue;

	public PropertyColumnValue(String colName, BaseRdbTypeAdapter type, boolean asNative, Object defaultValue, RdbAdapter rdb) {
		super(colName, rdb);
		this.type = type;
		this.asNative = asNative;
		this.defaultValue = defaultValue;
		this.rdb = rdb;
	}
	
	@Override
	public void bindExpression(StringBuilder sb) {
		if (asNative) {
			super.bindExpression(sb);
		} else {
			type.appendToTypedColForPrepare(sb, rdb);
		}
	}

	@Override
	public void valueExpression(Object value, StringBuilder sb) {
		if (asNative) {
			type.appendToSqlAsRealType(value == null ? defaultValue: value, sb, rdb);
		} else {
			type.appendToTypedCol(sb, rdb, 
					() -> type.appendToSqlAsRealType(value == null ? defaultValue: value, sb, rdb));
		}
	}
	
	@Override
	public void setParameter(Object value, int index, PreparedStatement ps) throws SQLException {
		type.setParameter(index, value == null ? defaultValue: value, ps);
	}


}
