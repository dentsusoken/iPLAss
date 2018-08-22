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

package org.iplass.mtp.impl.datastore.grdb.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjRefTable;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class ReferenceInsertSql extends UpdateSqlHandler {
	
	private static final Long LONG_ZERO = Long.valueOf(0);

	public String insert(int tenantId, EntityHandler eh, String propId, String oid, Long version, String targetObjDefId, String targetObjId, Long targetObjVersion, RdbAdapter rdb) {
		if (version == null) {
			version = LONG_ZERO;
		}
		if (targetObjVersion == null) {
			targetObjVersion = LONG_ZERO;
		}
		StringBuilder sb = new StringBuilder();
		
		sb.append("INSERT INTO ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
		sb.append("("
				+ ObjRefTable.TENANT_ID
				+ "," + ObjRefTable.OBJ_DEF_ID
				+ "," + ObjRefTable.REF_DEF_ID
				+ "," + ObjRefTable.OBJ_ID
				+ "," + ObjRefTable.OBJ_VER
				+ "," + ObjRefTable.TARGET_OBJ_DEF_ID
				+ "," + ObjRefTable.TARGET_OBJ_ID
				+ "," + ObjRefTable.TARGET_OBJ_VER
				+ ") VALUES(");
		sb.append(tenantId).append(",'");
		sb.append(rdb.sanitize(eh.getMetaData().getId())).append("','");
		sb.append(rdb.sanitize(propId)).append("','");
		sb.append(rdb.sanitize(oid)).append("',");
		sb.append(version).append(",'");
		sb.append(rdb.sanitize(targetObjDefId)).append("','");
		sb.append(rdb.sanitize(targetObjId)).append("',");
		sb.append(targetObjVersion);
		sb.append(")");
		
		return sb.toString();
	}
	
	public String prepareInsert(EntityHandler eh) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("INSERT INTO ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF());
		sb.append("("
				+ ObjRefTable.TENANT_ID
				+ "," + ObjRefTable.OBJ_DEF_ID
				+ "," + ObjRefTable.REF_DEF_ID
				+ "," + ObjRefTable.OBJ_ID
				+ "," + ObjRefTable.OBJ_VER
				+ "," + ObjRefTable.TARGET_OBJ_DEF_ID
				+ "," + ObjRefTable.TARGET_OBJ_ID
				+ "," + ObjRefTable.TARGET_OBJ_VER
				+ ") VALUES(?,?,?,?,?,?,?,?)");
		
		return sb.toString();
	}
	
	public void setPrepareInsertParameter(PreparedStatement stmt, int tenantId, EntityHandler eh, String propId, String oid, Long version, String targetObjDefId, String targetObjId, Long targetObjVersion, RdbAdapter rdb) throws SQLException {
		if (version == null) {
			version = LONG_ZERO;
		}
		if (targetObjVersion == null) {
			targetObjVersion = LONG_ZERO;
		}
		stmt.setInt(1, tenantId);
		stmt.setString(2, eh.getMetaData().getId());
		stmt.setString(3, propId);
		stmt.setString(4, oid);
		stmt.setLong(5, version);
		stmt.setString(6, targetObjDefId);
		stmt.setString(7, targetObjId);
		stmt.setLong(8, targetObjVersion);
	}

}
