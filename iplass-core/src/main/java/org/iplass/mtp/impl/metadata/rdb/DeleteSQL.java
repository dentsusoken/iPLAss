/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata.rdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class DeleteSQL extends UpdateSqlHandler{

	private static final String PURGE_SQL;

	static {
		PURGE_SQL = "DELETE FROM " + ObjMetaTable.TABLE_NAME
				+ " WHERE " + ObjMetaTable.TENANT_ID + " = ? "
				+ " AND " + ObjMetaTable.OBJ_DEF_ID + " = ? ";
	}

	public String createPurgeByIdSQL() {
		return PURGE_SQL;
	}

	public void setPurgeByIdParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String id) throws SQLException {
		int num = 1;
		ps.setInt(num++, tenantId);
		ps.setString(num++, rdb.sanitize(id));
	}

}
