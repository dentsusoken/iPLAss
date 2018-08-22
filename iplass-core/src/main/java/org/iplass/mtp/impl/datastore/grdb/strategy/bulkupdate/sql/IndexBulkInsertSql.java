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

package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjIndexTable;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.PropertyColumnValue;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.iplass.mtp.impl.rdb.adapter.bulk.DynamicColumnValue;

public class IndexBulkInsertSql {
	private static final Long LONG_ZERO = Long.valueOf(0);

	public static String indexTableName(EntityHandler eh, GRdbPropertyStoreHandler colDef) {
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
		if (type == IndexType.NON_UNIQUE) {
			return ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
		} else {
			return ((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
		}
	}
	
	public static BulkInsertContext insert(final GRdbPropertyStoreHandler colDef, Connection con, final RdbAdapter rdb) throws SQLException {
		
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
		String indexTableName = indexTableName(eh, colDef);
		
		BulkInsertContext bic = rdb.createBulkInsertContext();
		List<ColumnValue> columnValue = new ArrayList<>();
		columnValue.add(new DynamicColumnValue(ObjIndexTable.TENANT_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_DEF_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjIndexTable.COL_NAME, rdb));
		columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_ID, rdb));
		if (type == IndexType.NON_UNIQUE) {
			columnValue.add(new DynamicColumnValue(ObjIndexTable.OBJ_VER, rdb));
		}
		columnValue.add(new PropertyColumnValue(ObjIndexTable.VAL, colDef.getSingleColumnRdbTypeAdapter(), false, null, rdb));
		bic.setContext(indexTableName, columnValue, con);
		
		return bic;
	}
	
	public static void addValueForInsert(BulkInsertContext bic, int tenantId, final GRdbPropertyStoreHandler colDef, String oid, Long version, final Object value) throws SQLException {
		
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
		
		List<Object> values;
		if (type == IndexType.NON_UNIQUE) {
			values = new ArrayList<>(6);
		} else {
			values = new ArrayList<>(5);
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
		values.add(value);
		bic.add(values);
	}
	
	
	public static String searchByOid(int tenantId, final GRdbPropertyStoreHandler colDef, String oid, RdbAdapter rdbAdaptor) {
		EntityHandler eh = colDef.getPropertyRuntime().getParent();
		String objDefId = eh.getMetaData().getId();
		IndexType type = colDef.getPropertyRuntime().getMetaData().getIndexType();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT " + ObjIndexTable.VAL + " FROM ");
//		sb.append("SELECT 1 FROM ");
		if (type == IndexType.NON_UNIQUE) {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_INDEX(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex()));
		} else {
			sb.append(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_UNIQUE(colDef.getSingleColumnRdbTypeAdapter().getColOfIndex()));
		}
		sb.append(" WHERE " + ObjIndexTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + ObjIndexTable.OBJ_DEF_ID + "='").append(rdbAdaptor.sanitize(objDefId)).append("'");
		sb.append(" AND " + ObjIndexTable.OBJ_ID + "='").append(rdbAdaptor.sanitize(oid)).append("'");
		sb.append(" AND " + ObjIndexTable.COL_NAME + "='").append(rdbAdaptor.sanitize(colDef.getExternalIndexColName())).append("'");
		
		return sb.toString();
	}

	
}
