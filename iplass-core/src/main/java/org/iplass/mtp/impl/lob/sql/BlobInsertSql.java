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

package org.iplass.mtp.impl.lob.sql;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class BlobInsertSql extends UpdateSqlHandler {

	public String toSql(int tenantId, long lobId, String name, String type,
			boolean isTemporary, String sessionId, String defId, String propId,
			String oid, Long version, Long lobDataId, RdbAdapter rdb) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO " + ObjBlobTable.TABLE_NAME + "("
				 + ObjBlobTable.TENANT_ID + ",");
		sql.append(ObjBlobTable.LOB_ID + ",");
		sql.append(ObjBlobTable.LOB_NAME
				+ "," + ObjBlobTable.LOB_TYPE
				+ "," + ObjBlobTable.LOB_STAT
				+ "," + ObjBlobTable.LOB_DATA_ID
				+ "," + ObjBlobTable.CRE_DATE
				+ "," + ObjBlobTable.UP_DATE
				+ "," + ObjBlobTable.SESS_ID
				+ "," + ObjBlobTable.OBJ_DEF_ID
				+ "," + ObjBlobTable.PROP_DEF_ID
				+ "," + ObjBlobTable.OBJ_ID
				+ "," + ObjBlobTable.OBJ_VER
				+ ") VALUES(");
		sql.append(tenantId).append(",");
		sql.append(lobId).append(",");
		sql.append("'").append(rdb.sanitize(name)).append("',");
		sql.append("'").append(rdb.sanitize(type)).append("',");
		if (!isTemporary) {
			sql.append("'V',");
		} else {
			sql.append("'T',");
		}
		if (lobDataId == null) {
			sql.append("null,");
		} else {
			sql.append(lobDataId).append(",");
		}
		sql.append(rdb.systimestamp()).append(",");
		sql.append(rdb.systimestamp()).append(",");
		if (isTemporary) {
			sql.append("'").append(rdb.sanitize(sessionId)).append("',null,null,null,null");
		} else {
			sql.append("null,");
			sql.append("'").append(rdb.sanitize(defId)).append("',");
			sql.append("'").append(rdb.sanitize(propId)).append("',");
			sql.append("'").append(rdb.sanitize(oid)).append("',");
			if (version != null) {
				sql.append(version);
			} else {
				sql.append("null");
			}
		}
		sql.append(")");
		
		return sql.toString();
	}
	

}
