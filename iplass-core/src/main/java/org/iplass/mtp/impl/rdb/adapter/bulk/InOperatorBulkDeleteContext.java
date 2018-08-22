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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InOperatorBulkDeleteContext implements BulkDeleteContext {
	
	private Statement stmt;
	private String baseSql;
	private List<ColumnValue> keyColumnValue;
	private List<List<Object>> keys;

	@Override
	public void setContext(String tableName, List<ColumnValue> keyColumnValue,
			String additionalConditionExpression, Connection con)
			throws SQLException {
		this.keyColumnValue = keyColumnValue;
		
		stmt = con.createStatement();
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ").append(tableName).append(" WHERE ");
		if (additionalConditionExpression != null) {
			sb.append("(").append(additionalConditionExpression).append(") AND ");
		}
		if (keyColumnValue.size() == 1) {
			sb.append(keyColumnValue.get(0).colName()).append(" IN ");
		} else {
			sb.append("(");
			for (int i = 0; i < keyColumnValue.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(keyColumnValue.get(i).colName());
			}
			sb.append(") IN ");
		}
		
		baseSql = sb.toString();
		
		keys = new ArrayList<>();
	}

	@Override
	public void add(List<Object> key) throws SQLException {
		if (keyColumnValue.size() != key.size()) {
			throw new IllegalArgumentException("key length not equals key col length");
		}
		keys.add(key);
	}

	@Override
	public void execute() throws SQLException {
		if (keys.size() > 0) {
			StringBuilder sb = new StringBuilder(baseSql);
			sb.append("(");
			for (int i = 0; i < keys.size(); i++) {
				if (i != 0) {
					sb.append(",");
				}
				List<Object> k = keys.get(i);
				if (k.size() == 1) {
					ColumnValue cv = keyColumnValue.get(0);
					if (cv instanceof FixedExpressionColumnValue) {
						sb.append(((FixedExpressionColumnValue) cv).fixedValueExpression());
					} else {
						((DynamicColumnValue) cv).valueExpression(k.get(0), sb);
					}
				} else {
					sb.append("(");
					for (int j = 0; j < k.size(); j++) {
						if (j != 0) {
							sb.append(",");
						}
						ColumnValue cv = keyColumnValue.get(j);
						if (cv instanceof FixedExpressionColumnValue) {
							sb.append(((FixedExpressionColumnValue) cv).fixedValueExpression());
						} else {
							((DynamicColumnValue) cv).valueExpression(k.get(j), sb);
						}
					}
					sb.append(")");
				}
			}
			sb.append(")");
			
			stmt.executeUpdate(sb.toString());
			keys.clear();
		}
	}

	@Override
	public void close() throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
	}

	@Override
	public int getCurrentSize() {
		if (keys == null) {
			return 0;
		} else {
			return keys.size();
		}
	}

}
