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
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.iplass.mtp.impl.rdb.adapter.bulk.DynamicColumnValue;


public class ObjStoreBulkDeleteSql {
	private static final Long LONG_ZERO = Long.valueOf(0);

	public static BulkDeleteContext deleteByOid(EntityHandler eh, Connection con, RdbAdapter rdb) throws SQLException {
		BulkDeleteContext bdc = rdb.createBulkDeleteContext();
		List<ColumnValue> keys = new ArrayList<>(4);
		keys.add(new DynamicColumnValue(ObjStoreTable.TENANT_ID, rdb));
		keys.add(new DynamicColumnValue(ObjStoreTable.OBJ_DEF_ID, rdb));
		keys.add(new DynamicColumnValue(ObjStoreTable.OBJ_ID, rdb));
		keys.add(new DynamicColumnValue(ObjStoreTable.OBJ_VER, rdb));
		bdc.setContext(((GRdbEntityStoreRuntime) eh.getEntityStoreRuntime()).OBJ_STORE(), keys, null, con);

		return bdc;
	}
	
	public static void addValueForDeleteByOid(BulkDeleteContext bdc, int tenantId, EntityHandler eh, String objId, Long version) throws SQLException {
		List<Object> values = new ArrayList<>(4);
		values.add(tenantId);
		values.add(eh.getMetaData().getId());
		values.add(objId);
		if (version == null) {
			version = LONG_ZERO;
		}
		values.add(version);
		bdc.add(values);
	}
}
