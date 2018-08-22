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
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class ObjStoreBulkInsertSql {
	
	public static List<ColumnValueMapper> insertSubPage(int tenantId, EntityHandler eh,
			int pageNo, RdbAdapter rdb, EntityContext context) {
		List<ColumnValueMapper> ret = new ArrayList<>();
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.TENANT_ID, Integer.toString(tenantId)));
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.OBJ_DEF_ID, rdb.toSqlExp(eh.getMetaData().getId())));
		ret.add(new DirectColumnValueMapper(ObjStoreTable.OBJ_ID, Entity.OID, null));
		ret.add(new DirectColumnValueMapper(ObjStoreTable.OBJ_VER, Entity.VERSION, Long.valueOf(0)));
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.PG_NO, Integer.toString(pageNo)));
		List<PropertyHandler> propList = eh.getDeclaredPropertyList();
		
		handlePropList(ret, tenantId, propList, eh, pageNo, rdb);
		
		return ret;
	}
	
	private static void handlePropList(List<ColumnValueMapper> mappers, int tenantId, List<PropertyHandler> propList, EntityHandler eh, int pageNo, RdbAdapter rdbAdaptor) {
		for (PropertyHandler p: propList) {
			if (p instanceof PrimitivePropertyHandler
					&& !((PrimitivePropertyHandler) p).getMetaData().getType().isVirtual()) {
				GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) p.getStoreSpecProperty();
				if (!colDef.isMulti()) {
					GRdbPropertyStoreHandler scol = (GRdbPropertyStoreHandler) colDef;
					if (scol.getMetaData().getPageNo() == pageNo
							|| scol.getIndexColName() != null && scol.getMetaData().getIndexPageNo() == pageNo) {
						mappers.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) p, pageNo));
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
						mappers.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) p, pageNo));
					}
				}
			}
		}
	}
	
	public static List<ColumnValueMapper> insertMain(int tenantId, EntityHandler eh,
			RdbAdapter rdb, EntityContext context) {
		List<ColumnValueMapper> ret = new ArrayList<>();
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.TENANT_ID, Integer.toString(tenantId)));
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.OBJ_DEF_ID, rdb.toSqlExp(eh.getMetaData().getId())));
		ret.add(new DirectColumnValueMapper(ObjStoreTable.OBJ_ID, Entity.OID, null));
		ret.add(new DirectColumnValueMapper(ObjStoreTable.OBJ_VER, Entity.VERSION, Long.valueOf(0)));
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.PG_NO, "0"));
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.OBJ_DEF_VER, Integer.toString(((MetaGRdbEntityStore) eh.getMetaData().getEntityStoreDefinition()).getVersion())));
		ret.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) eh.getProperty(Entity.STATE, context), 0));
		ret.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) eh.getProperty(Entity.NAME, context), 0));
		ret.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) eh.getProperty(Entity.DESCRIPTION, context), 0));
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.CRE_DATE, rdb.systimestamp()));
		ret.add(new FixedExpressionColumnValueMapper(ObjStoreTable.UP_DATE, rdb.systimestamp()));
		ret.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) eh.getProperty(Entity.START_DATE, context), 0));
		ret.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) eh.getProperty(Entity.END_DATE, context), 0));
		ret.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) eh.getProperty(Entity.LOCKED_BY, context), 0));
		ret.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) eh.getProperty(Entity.CREATE_BY, context), 0));
		ret.add(new PropertyColumnValueMapper(tenantId, eh, (PrimitivePropertyHandler) eh.getProperty(Entity.UPDATE_BY, context), 0));

		List<PropertyHandler> propList = eh.getDeclaredPropertyList();
		handlePropList(ret, tenantId, propList, eh, 0, rdb);
		
		return ret;
	}

}
