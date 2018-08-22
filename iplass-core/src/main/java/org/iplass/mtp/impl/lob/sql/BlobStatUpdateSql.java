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

public class BlobStatUpdateSql extends UpdateSqlHandler {
	
	public String toSql(int tenantId, long lobId, String defId, String propId,
			String oid, Long version, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE " + ObjBlobTable.TABLE_NAME + " SET ");
		sb.append(ObjBlobTable.LOB_STAT + "='V'," + ObjBlobTable.SESS_ID + "=null,");
		sb.append(ObjBlobTable.UP_DATE + "=").append(rdb.systimestamp()).append(",");
		sb.append(ObjBlobTable.OBJ_DEF_ID + "='").append(rdb.sanitize(defId)).append("',");
		sb.append(ObjBlobTable.PROP_DEF_ID + "='").append(rdb.sanitize(propId)).append("',");
		sb.append(ObjBlobTable.OBJ_ID + "='").append(rdb.sanitize(oid)).append("',");
		sb.append(ObjBlobTable.OBJ_VER + "=").append(version);
		sb.append(" WHERE " + ObjBlobTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjBlobTable.LOB_ID + "=").append(lobId);
		return sb.toString();
	}
	
	public String toInfoUpdateSql(int tenantId, long lobId, String name, String type, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE " + ObjBlobTable.TABLE_NAME + " SET ");
		sb.append(ObjBlobTable.UP_DATE + "=").append(rdb.systimestamp()).append(",");
		sb.append(ObjBlobTable.LOB_NAME + "='").append(rdb.sanitize(name)).append("',");
		sb.append(ObjBlobTable.LOB_TYPE + "='").append(rdb.sanitize(type)).append("'");
		sb.append(" WHERE " + ObjBlobTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjBlobTable.LOB_ID + "=").append(lobId);
		return sb.toString();
	}
	

}
