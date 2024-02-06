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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.mapper;

import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.iplass.mtp.impl.rdb.adapter.bulk.DynamicColumnValue;

public class DirectColumnValueMapper implements ColumnValueMapper {
	
	private String colName;
	private String propName;
	private Object defaultValue;

	public DirectColumnValueMapper(String colName, String propName, Object defaultValue) {
		this.colName = colName;
		this.propName = propName;
		this.defaultValue = defaultValue;
	}

	@Override
	public void columns(List<ColumnValue> columnValues, RdbAdapter rdb) {
		columnValues.add(new DynamicColumnValue(colName, rdb));
	}

	@Override
	public void values(List<Object> values, Entity target, RdbAdapter rdb) {
		Object val = target.getValue(propName);
		if (val == null) {
			val = defaultValue;
		}
		values.add(val);
	}

}
