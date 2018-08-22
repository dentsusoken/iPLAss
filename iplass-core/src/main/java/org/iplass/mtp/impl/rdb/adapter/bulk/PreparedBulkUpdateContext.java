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
package org.iplass.mtp.impl.rdb.adapter.bulk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreparedBulkUpdateContext implements BulkUpdateContext {
	
	private static final Logger logger = LoggerFactory.getLogger(PreparedBulkUpdateContext.class);
	
	private List<ColumnValue> keyColumnValue;
	private List<ColumnValue> updateColumnValue;
	private PreparedStatement ps;
	private int currentSize;
	
	public PreparedBulkUpdateContext() {
	}

	@Override
	public void setContext(String tableName, List<ColumnValue> keyColumnValue, List<ColumnValue> updateColumnValue, String additionalConditionExpression, Connection con) throws SQLException {
		this.keyColumnValue = keyColumnValue;
		this.updateColumnValue = updateColumnValue;
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET ");
		for (int i = 0; i < updateColumnValue.size(); i++) {
			if (i != 0) {
				sql.append(",");
			}
			ColumnValue cvm = updateColumnValue.get(i);
			sql.append(cvm.colName());
			sql.append("=");
			if (cvm instanceof FixedExpressionColumnValue) {
				sql.append(((FixedExpressionColumnValue) cvm).fixedValueExpression());
			} else if (cvm instanceof DynamicColumnValue) {
				((DynamicColumnValue) cvm).bindExpression(sql);
			} else {
				throw new IllegalArgumentException();
			}
		}
		sql.append(" WHERE ");
		for (int i = 0; i < keyColumnValue.size(); i++) {
			if (i != 0) {
				sql.append(" AND ");
			}
			ColumnValue cvm = keyColumnValue.get(i);
			sql.append(cvm.colName());
			sql.append("=");
			if (cvm instanceof FixedExpressionColumnValue) {
				sql.append(((FixedExpressionColumnValue) cvm).fixedValueExpression());
			} else if (cvm instanceof DynamicColumnValue) {
				((DynamicColumnValue) cvm).bindExpression(sql);
			} else {
				throw new IllegalArgumentException();
			}
		}
		if (additionalConditionExpression != null) {
			sql.append(" AND (").append(additionalConditionExpression).append(")");
		}
		ps = con.prepareStatement(sql.toString());
	}

	@Override
	public void add(List<Object> key, List<Object> values) throws SQLException {
		if (keyColumnValue.size() != key.size()) {
			throw new IllegalArgumentException("key length not equals key col length");
		}
		if (updateColumnValue.size() != values.size()) {
			throw new IllegalArgumentException("values length not equals update col length");
		}

		int index = 1;
		for (int i = 0; i < values.size(); i++) {
			ColumnValue cvm = updateColumnValue.get(i);
			if (cvm instanceof FixedExpressionColumnValue) {
				//skip
			} else {
				((DynamicColumnValue) cvm).setParameter(values.get(i), index, ps);
				index++;
			}
		}
		for (int i = 0; i < key.size(); i++) {
			ColumnValue cvm = keyColumnValue.get(i);
			if (cvm instanceof FixedExpressionColumnValue) {
				//skip
			} else {
				((DynamicColumnValue) cvm).setParameter(key.get(i), index, ps);
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
