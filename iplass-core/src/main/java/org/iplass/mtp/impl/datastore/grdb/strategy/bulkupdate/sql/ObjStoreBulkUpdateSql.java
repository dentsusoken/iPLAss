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

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbMultiplePropertyStore.GRdbMultiplePropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.mapper.ColumnValueMapper;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.mapper.DirectColumnValueMapper;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.mapper.FixedExpressionColumnValueMapper;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.mapper.PropertyColumnValueMapper;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.properties.extend.VirtualType;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class ObjStoreBulkUpdateSql {
	
	public static class ObjStoreBulkUpdateSqlResult {
		public List<ColumnValueMapper> keys;
		public List<ColumnValueMapper> values;
		public String additionalConditionExpression;
	}
	
	public static boolean canUpdateProperty(PropertyHandler pHandler) {
		if (pHandler instanceof ReferencePropertyHandler) {
			return false;
		}
		PrimitivePropertyHandler pph = (PrimitivePropertyHandler) pHandler;
		if (pph.getMetaData().getType() instanceof VirtualType) {
			return false;
		}
		String propName = pHandler.getName();
		if (propName.equals(Entity.OID)
				|| propName.equals(Entity.UPDATE_DATE)//一律更新
				|| propName.equals(Entity.VERSION)
				|| propName.equals(Entity.CREATE_BY)
				|| propName.equals(Entity.CREATE_DATE)
				) {
			return false;
		}
		return true;
	}
	
	private static boolean handlePropAndVal(List<ColumnValueMapper> values, int tenantId, EntityHandler eh, int pageNo, List<String> updateProperties, RdbAdapter rdbAdaptor, EntityContext context) {
		boolean ret = false;
		for (PropertyHandler p: eh.getPropertyList(context)) {
			if (canUpdateProperty(p) && contains(updateProperties, p.getName())) {
				GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) p.getStoreSpecProperty();
				if (!colDef.isMulti()) {
					GRdbPropertyStoreHandler scol = (GRdbPropertyStoreHandler) colDef;
					if (scol.getMetaData().getPageNo() == pageNo
							|| scol.getIndexColName() != null && scol.getMetaData().getIndexPageNo() == pageNo) {
						values.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) p, pageNo));
						ret = true;
					}
				} else {
					GRdbMultiplePropertyStoreHandler mcol = (GRdbMultiplePropertyStoreHandler) colDef;
					boolean isAdd = false;
					for (MetaGRdbPropertyStore metaCol: mcol.getMetaData().getStore()) {
						if (metaCol.getPageNo() == pageNo) {
							isAdd = true;
							break;
						}
					}
					if (isAdd) {
						values.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) p, pageNo));
						ret = true;
					}
				}
			}
		}
		return ret;
	}
	
	private static boolean contains(List<String> updateProperties, String name) {
		if (updateProperties == null) {
			return true;
		}
		return updateProperties.contains(name);
	}

	private static void keys(List<ColumnValueMapper> keys, int tenantId, EntityHandler eh, int pageNo, RdbAdapter rdb) {
		keys.add(new FixedExpressionColumnValueMapper(ObjStoreTable.TENANT_ID, Integer.toString(tenantId)));
		keys.add(new FixedExpressionColumnValueMapper(ObjStoreTable.OBJ_DEF_ID, rdb.toSqlExp(eh.getMetaData().getId())));
		keys.add(new DirectColumnValueMapper(ObjStoreTable.OBJ_ID, Entity.OID, null));
		keys.add(new DirectColumnValueMapper(ObjStoreTable.OBJ_VER, Entity.VERSION, Long.valueOf(0)));
		keys.add(new FixedExpressionColumnValueMapper(ObjStoreTable.PG_NO, Integer.toString(pageNo)));
	}
	
	//単一Entity更新,page=0
	public static ObjStoreBulkUpdateSqlResult updateMain(int tenantId, EntityHandler eh, List<String> updateProperties,
			RdbAdapter rdb, EntityContext context) {
		ObjStoreBulkUpdateSqlResult res = new ObjStoreBulkUpdateSqlResult();
		//keys
		res.keys = new ArrayList<>(5);
		keys(res.keys, tenantId, eh, 0, rdb);
		
		//values
		res.values = new ArrayList<>();
		handlePropAndVal(res.values, tenantId, eh, 0, updateProperties, rdb, context);
		res.values.add(new FixedExpressionColumnValueMapper(ObjStoreTable.UP_DATE, rdb.systimestamp()));
		
		//defVersion check
		res.additionalConditionExpression = ObjStoreTable.OBJ_DEF_VER + "<=" + ((MetaGRdbEntityStore) eh.getEntityStoreRuntime().getMetaData()).getVersion();

		return res;
	}
	
	//単一Entity更新,page1以降
	public static ObjStoreBulkUpdateSqlResult updateSub(int tenantId, EntityHandler eh,
			int pageNo, List<String> updateProperties, RdbAdapter rdb, EntityContext context) {
		ObjStoreBulkUpdateSqlResult res = new ObjStoreBulkUpdateSqlResult();
		//keys
		res.keys = new ArrayList<>(5);
		keys(res.keys, tenantId, eh, pageNo, rdb);
		
		//values
		res.values = new ArrayList<>();
		boolean isAdd = handlePropAndVal(res.values, tenantId, eh, pageNo, updateProperties, rdb, context);
		if (isAdd) {
			return res;
		} else {
			return null;
		}
	}

}
