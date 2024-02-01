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

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class ObjStoreLockSql extends QuerySqlHandler {

	private static final String TMP_TABLE_ALIAS = "tt";

	public String lockByOid(int tenantId, EntityHandler eh,
			EntityContext context,
			String oid, RdbAdapter rdbAdaptor) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT " + ObjStoreTable.OBJ_ID
				+ " FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
		sb.append(rdbAdaptor.sanitize(eh.getMetaData().getId()));
		sb.append("' AND " + ObjStoreTable.OBJ_ID + "='");
		sb.append(rdbAdaptor.sanitize(oid));
		sb.append("' AND " + ObjStoreTable.PG_NO + "=0");
		sb.append(" ORDER BY " + ObjStoreTable.OBJ_VER);
		return rdbAdaptor.createRowLockSql(sb.toString());
	}

	public String lockByTempTable(int tenantId, EntityHandler eh, EntityContext context, RdbAdapter rdbAdaptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER);
		sb.append(" FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=");
		sb.append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='");
		sb.append(rdbAdaptor.sanitize(eh.getMetaData().getId()));
		if (rdbAdaptor.isSupportRowValueConstructor()) {
			sb.append("' AND (" + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + ") IN");
			sb.append("(SELECT " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER + " FROM " + rdbAdaptor.getTemplaryTablePrefix() + ObjStoreTable.TABLE_NAME_TMP + ")");
		} else {
			String objStoreTable = ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE();
			sb.append("' AND EXISTS (");
			sb.append("SELECT 1 FROM ").append(rdbAdaptor.getTemplaryTablePrefix()).append(ObjStoreTable.TABLE_NAME_TMP ).append(" ").append(TMP_TABLE_ALIAS);
			sb.append(" WHERE ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_ID ).append("=").append(objStoreTable).append(".").append(ObjStoreTable.OBJ_ID);
			sb.append(" AND ").append(TMP_TABLE_ALIAS).append(".").append(ObjStoreTable.OBJ_VER ).append("=").append(objStoreTable).append(".").append(ObjStoreTable.OBJ_VER);
			sb.append(")");
		}
		sb.append(" AND " + ObjStoreTable.PG_NO + "=0");
		sb.append(" ORDER BY " + ObjStoreTable.OBJ_ID + "," + ObjStoreTable.OBJ_VER);
		return rdbAdaptor.createRowLockSql(sb.toString());
	}


}
