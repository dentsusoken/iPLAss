/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class UpdateSQL extends UpdateSqlHandler{

	private static final String UPDATE_SQL;
	private static final String UPDATE_WITH_PATH_SQL;

	static {
		UPDATE_SQL = "UPDATE " + ObjMetaTable.TABLE_NAME
				+ " SET " + ObjMetaTable.STATUS + "='D'"
				+ " ," + ObjMetaTable.UP_USER + "=?"
				+ " ," + ObjMetaTable.UP_DATE + "=systimestamp"
				+ " WHERE " + ObjMetaTable.TENANT_ID + "=?"
				+ " AND " + ObjMetaTable.OBJ_DEF_ID + "=?"
				+ " AND " + ObjMetaTable.STATUS + "='V'";


		UPDATE_WITH_PATH_SQL = "UPDATE " + ObjMetaTable.TABLE_NAME
				+ " SET " + ObjMetaTable.STATUS + "='D'"
				+ " ," + ObjMetaTable.OBJ_DEF_PATH + "=?"
				+ " ," + ObjMetaTable.UP_USER + "=?"
				+ " ," + ObjMetaTable.UP_DATE + "=systimestamp"
				+ " WHERE " + ObjMetaTable.TENANT_ID + "=?"
				+ " AND " + ObjMetaTable.OBJ_DEF_ID + "=?";
	}

	public String createUpdateSQL(RdbAdapter rdb) {
		return UPDATE_SQL.replaceAll("systimestamp", rdb.systimestamp());
	}

	public void setUpdateParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String metaDataId) throws SQLException {
		String clientId = ExecuteContext.getCurrentContext().getClientId();
		ps.setString(1, clientId);
		ps.setInt(2, tenantId);
		ps.setString(3, metaDataId);
	}

	public String createUpdateWithPathSQL(RdbAdapter rdb) {
		return UPDATE_WITH_PATH_SQL.replaceAll("systimestamp", rdb.systimestamp());
	}

	public void setUpdateWithPathParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String metaDataId, String path) throws SQLException {
		String clientId = rdb.sanitize(ExecuteContext.getCurrentContext().getClientId());
		ps.setString(1, path);
		ps.setString(2, clientId);
		ps.setInt(3, tenantId);
		ps.setString(4, metaDataId);
	}


}
