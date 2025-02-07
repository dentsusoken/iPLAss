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

package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjIndexTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.iplass.mtp.impl.rdb.adapter.bulk.DynamicColumnValue;


public class IndexBulkDeleteSql {
	private static final Long LONG_ZERO = Long.valueOf(0);

	public static BulkDeleteContext deleteByOidVersion(GRdbPropertyStoreHandler colDef, Connection con, RdbAdapter rdb) throws SQLException {
		
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
		String indexTableName = IndexBulkInsertSql.indexTableName(eh, colDef);
		
		BulkDeleteContext bdc = rdb.createBulkDeleteContext();
		List<ColumnValue> columnValue = new ArrayList<>(4);
		columnValue.add(new DynamicColumnValue(ObjIndexTable.TENANT_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_DEF_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_ID, rdb));
		if (type == IndexType.NON_UNIQUE) {
			columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_VER, rdb));
		}
		bdc.setContext(indexTableName, columnValue, null, con);
		
		return bdc;
	}

	public static void addValueForDeleteByOidVersion(BulkDeleteContext bdc, int tenantId, EntityHandler eh, IndexType type, String oid, Long version) throws SQLException {
		List<Object> values;
		if (type == IndexType.NON_UNIQUE) {
			values = new ArrayList<>(4);
		} else {
			values = new ArrayList<>(3);
		}
		values.add(tenantId);
		values.add(eh.getMetaData().getId());
		values.add(oid);
		if (type == IndexType.NON_UNIQUE) {
			if (version == null) {
				version = LONG_ZERO;
			}
			values.add(version);
		}
		bdc.add(values);
	}


	public static BulkDeleteContext deleteByOidVersionColName(GRdbPropertyStoreHandler colDef, Connection con, RdbAdapter rdb) throws SQLException {
		
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
		String indexTableName = IndexBulkInsertSql.indexTableName(eh, colDef);
		
		BulkDeleteContext bdc = rdb.createBulkDeleteContext();
		List<ColumnValue> columnValue = new ArrayList<>(5);
		columnValue.add(new DynamicColumnValue(ObjIndexTable.TENANT_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_DEF_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjIndexTable.COL_NAME, rdb));
		columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_ID, rdb));
		if (type == IndexType.NON_UNIQUE) {
			columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_VER, rdb));
		}
		bdc.setContext(indexTableName, columnValue, null, con);
		
		return bdc;
	}

	public static void addValueForDeleteByOidVersionColName(BulkDeleteContext bdc, int tenantId, GRdbPropertyStoreHandler colDef, String oid, Long version) throws SQLException {
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
		
		List<Object> values;
		if (type == IndexType.NON_UNIQUE) {
			values = new ArrayList<>(5);
		} else {
			values = new ArrayList<>(4);
		}
		values.add(tenantId);
		values.add(eh.getMetaData().getId());
		values.add(colDef.getExternalIndexColName());
		values.add(oid);
		if (type == IndexType.NON_UNIQUE) {
			if (version == null) {
				version = LONG_ZERO;
			}
			values.add(version);
		}
		bdc.add(values);
	}

	public static String countByOidSql(int tenantId, EntityHandler eh, Set<String> oids, RdbAdapter rdbAdaptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT " + ObjStoreTable.OBJ_ID + ",COUNT(*) FROM ");
		sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE());
		sb.append(" WHERE " + ObjStoreTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjStoreTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(eh.getMetaData().getId())).append("'");
		sb.append(" AND " + ObjStoreTable.OBJ_ID + " IN (");
		boolean first = true;
		for (String oid : oids) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append("'").append(rdbAdaptor.sanitize(oid)).append("'");
		}
		sb.append(") AND " + ObjStoreTable.PG_NO + "=0 GROUP BY " + ObjStoreTable.OBJ_ID);

		return sb.toString();
	}
}
