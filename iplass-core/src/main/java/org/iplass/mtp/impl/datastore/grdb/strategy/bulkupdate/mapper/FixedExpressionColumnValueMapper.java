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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.mapper;

import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.iplass.mtp.impl.rdb.adapter.bulk.FixedExpressionColumnValue;
import org.iplass.mtp.impl.rdb.adapter.bulk.FixedValueExpression;

public class FixedExpressionColumnValueMapper implements ColumnValueMapper {
	
	private String colName;
	private String valueExpression;
	
	public FixedExpressionColumnValueMapper(String colName, String valueExpression) {
		this.colName = colName;
		this.valueExpression = valueExpression;
	}

	@Override
	public void columns(List<ColumnValue> columnValues, RdbAdapter rdb) {
		columnValues.add(new FixedExpressionColumnValue(colName, valueExpression));
	}

	@Override
	public void values(List<Object> values, Entity target, RdbAdapter rdb) {
		values.add(FixedValueExpression.VALUE);
	}

}
