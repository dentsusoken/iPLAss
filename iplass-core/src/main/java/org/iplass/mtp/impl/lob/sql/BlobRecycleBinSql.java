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

package org.iplass.mtp.impl.lob.sql;

import java.sql.Timestamp;

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjRefTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class BlobRecycleBinSql extends UpdateSqlHandler {
	
	public String searchSql(RdbAdapter rdb, int tenantId, long rbid) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ ObjBlobTable.LOB_ID
				+ " FROM " + ObjBlobTable.TABLE_NAME_RB
				+ " WHERE " + ObjBlobTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjBlobTable.RB_ID + "=").append(rbid);
		return sb.toString();
	}
	
	public String insertSql(int tenantId, Long rbid, Long lobid, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + ObjBlobTable.TABLE_NAME_RB + "(");
		sb.append(ObjBlobTable.TENANT_ID + "," + ObjBlobTable.RB_ID + "," + ObjBlobTable.LOB_ID);
		sb.append(") VALUES(");
		sb.append(tenantId).append(",");
		sb.append(rbid).append(",");
		sb.append(lobid).append(")");
		
		return sb.toString();
	}
	
	public String deleteByRbIdSql(int tenantId, Long rbid, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(ObjBlobTable.TABLE_NAME_RB);
		sb.append(" WHERE " + ObjBlobTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjBlobTable.RB_ID + "=").append(rbid);
		return sb.toString();
	}
	
	public String deleteByTimestampSql(int tenantId, EntityHandler eh, Timestamp ts, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(ObjBlobTable.TABLE_NAME_RB);
		sb.append(" WHERE " + ObjBlobTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjRefTable.RB_ID + " in (SELECT " + ObjStoreTable.RB_ID + " FROM " + ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE_RB() + " WHERE ");
		sb.append(ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdb.sanitize(eh.getMetaData().getId())).append("'");
		if (ts != null) {
			sb.append(" AND " + ObjStoreTable.RB_DATE + "<").append(rdb.toTimeStampExpression(ts));
		}
		sb.append(")");
		return sb.toString();
	}
}
