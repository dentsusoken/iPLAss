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
import java.sql.SQLException;
import java.util.List;

public interface BulkDeleteContext {
	public void setContext(String tableName, List<ColumnValue> keyColumnValue, String additionalConditionExpression, Connection con) throws SQLException;
	public void add(List<Object> key) throws SQLException;
	public void execute() throws SQLException;
	public void close() throws SQLException;
	public int getCurrentSize();

}
