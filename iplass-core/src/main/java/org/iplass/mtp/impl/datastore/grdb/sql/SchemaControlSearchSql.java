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

package org.iplass.mtp.impl.datastore.grdb.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class SchemaControlSearchSql extends QuerySqlHandler {

	public String toSql(int tenantId, String objDefId, boolean withLock, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT TENANT_ID,OBJ_DEF_ID,OBJ_DEF_VER,LOCK_STATUS,CR_DATA_VER" +
				" FROM SCHEMA_CTRL" +
				" WHERE TENANT_ID=");
		sb.append(tenantId);
		sb.append(" AND OBJ_DEF_ID='").append(rdb.sanitize(objDefId)).append("'");
		return withLock ? rdb.createRowLockSql(sb.toString()) : sb.toString();
	}

	public int getCurrentVersion(ResultSet rs) throws SQLException {
		while(rs.next()) {
			return rs.getInt(3);
		}
		return -1;
	}

	public boolean isLocked(ResultSet rs) throws SQLException {
		while(rs.next()) {
			return "1".equals(rs.getString(4));
		}
		return false;
	}

}
