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

import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjRefTable;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.iplass.mtp.impl.rdb.adapter.bulk.DynamicColumnValue;

public class ReferenceBulkInsertSql {
	
	private static final Long LONG_ZERO = Long.valueOf(0);

	public static BulkInsertContext insert(EntityHandler eh, Connection con, RdbAdapter rdb) throws SQLException {
		
		List<ColumnValue> columnValue = new ArrayList<>();
		columnValue.add(new DynamicColumnValue(ObjRefTable.TENANT_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjRefTable.OBJ_DEF_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjRefTable.REF_DEF_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjRefTable.OBJ_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjRefTable.OBJ_VER, rdb));
		columnValue.add(new DynamicColumnValue(ObjRefTable.TARGET_OBJ_DEF_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjRefTable.TARGET_OBJ_ID, rdb));
		columnValue.add(new DynamicColumnValue(ObjRefTable.TARGET_OBJ_VER, rdb));
		
		BulkInsertContext bic = rdb.createBulkInsertContext();
		bic.setContext(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_REF(), columnValue, con);
		return bic;
	}
	
	public static void addValueForInsert(BulkInsertContext bic, int tenantId, EntityHandler eh, String propId, String oid, Long version, String targetObjDefId, String targetObjId, Long targetObjVersion) throws SQLException {
		
		if (version == null) {
			version = LONG_ZERO;
		}
		if (targetObjVersion == null) {
			targetObjVersion = LONG_ZERO;
		}
		List<Object> values = new ArrayList<>(8);
		values.add(tenantId);
		values.add(eh.getMetaData().getId());
		values.add(propId);
		values.add(oid);
		values.add(version);
		values.add(targetObjDefId);
		values.add(targetObjId);
		values.add(targetObjVersion);
		
		bic.add(values);
	}

}
