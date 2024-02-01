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
package org.iplass.mtp.impl.rdb.adapter.bulk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreparedBulkInsertContext implements BulkInsertContext {
	
	private static final Logger logger = LoggerFactory.getLogger(PreparedBulkInsertContext.class);
	
	private List<ColumnValue> columnValue;
	private PreparedStatement ps;
	private int currentSize;
	
	public PreparedBulkInsertContext() {
	}

	@Override
	public void setContext(String tableName, List<ColumnValue> columnValue, Connection con) throws SQLException {
		this.columnValue = columnValue;
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ");
		sql.append(tableName);
		sql.append("(");
		for (int i = 0; i < columnValue.size(); i++) {
			if (i != 0) {
				sql.append(",");
			}
			sql.append(columnValue.get(i).colName());
		}
		sql.append(") VALUES(");
		for (int i = 0; i < columnValue.size(); i++) {
			if (i != 0) {
				sql.append(",");
			}
			ColumnValue cvm = columnValue.get(i);
			if (cvm instanceof FixedExpressionColumnValue) {
				sql.append(((FixedExpressionColumnValue) cvm).fixedValueExpression());
			} else if (cvm instanceof DynamicColumnValue) {
				((DynamicColumnValue) cvm).bindExpression(sql);
			} else {
				throw new IllegalArgumentException();
			}
			
		}
		sql.append(")");
		ps = con.prepareStatement(sql.toString());
	}

	@Override
	public void add(List<Object> values) throws SQLException {
		if (columnValue.size() != values.size()) {
			throw new IllegalArgumentException("val length not equals col length");
		}
		int index = 1;
		for (int i = 0; i < values.size(); i++) {
			ColumnValue cvm = columnValue.get(i);
			if (cvm instanceof FixedExpressionColumnValue) {
				//skip
			} else {
				((DynamicColumnValue) cvm).setParameter(values.get(i), index, ps);
				index++;
			}
		}
		ps.addBatch();
		currentSize++;
	}

	@Override
	public void execute() throws SQLException {
		if (currentSize > 0) {
			ps.executeBatch();
			currentSize = 0;
		}
	}

	@Override
	public void close() throws SQLException {
		if (ps != null) {
			try {
				ps.clearBatch();
			} catch (SQLException e) {
				logger.error("cant clearBatch. caluse " + e, e);
			} finally {
				ps.close();
			}
		}
	}

	@Override
	public int getCurrentSize() {
		return currentSize;
	}

}
