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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.IndexBulkDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.ObjStoreBulkDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.ReferenceBulkDeleteSql;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkDeleteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BulkDeleteHandler {
	private static Logger logger = LoggerFactory.getLogger(BulkDeleteHandler.class);
	
	BulkDeleteContext objStoreDelete;
	BulkDeleteContext objRefDeleteByOid;
	Map<IndexTableKey, BulkDeleteContext> objIndexDelete;
	
	BulkDeleteHandler(BulkUpdateState state) throws SQLException {
		
		objStoreDelete = ObjStoreBulkDeleteSql.deleteByOid(state.eh, state.con, state.rdb);
		
		//参照
		if (state.eh.getReferencePropertyList(true, state.entityContext).size() > 0) {
			objRefDeleteByOid = ReferenceBulkDeleteSql.deleteByOidVersion(state.eh, state.con, state.rdb);
		}
		
		//外部Index
		List<PrimitivePropertyHandler> indexes = new ArrayList<>();
		for (PrimitivePropertyHandler pph: state.eh.getIndexedPropertyList(state.entityContext)) {
			GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) pph.getStoreSpecProperty();
			if (colDef.isExternalIndex()) {
				indexes.add(pph);
			}
		}
		for (PrimitivePropertyHandler pph: indexes) {
			GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) pph.getStoreSpecProperty();
			if (objIndexDelete == null) {
				objIndexDelete = new HashMap<>();
			}
			IndexTableKey ikey = new IndexTableKey(pph.getMetaData().getIndexType(), colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
			BulkDeleteContext bdc = objIndexDelete.get(ikey);
			if (bdc == null) {
				bdc = IndexBulkDeleteSql.deleteByOidVersion(colDef.asList().get(0), state.con, state.rdb);
				objIndexDelete.put(ikey, bdc);
			}
		}
	}
	
	public void addValue(BulkUpdateState state, Entity entity) throws SQLException {
		String oid = entity.getOid();
		Long ver = entity.getVersion();

		ObjStoreBulkDeleteSql.addValueForDeleteByOid(objStoreDelete, state.tenantId, state.eh, oid, ver);
		if (objStoreDelete.getCurrentSize() >= state.rdb.getBatchSize()) {
			objStoreDelete.execute();
		}
		
		//参照
		if (objRefDeleteByOid != null) {
			ReferenceBulkDeleteSql.addValueForDeleteByOidVersion(objRefDeleteByOid, state.tenantId, state.eh, oid, ver);
			if (objRefDeleteByOid.getCurrentSize() >= state.rdb.getBatchSize()) {
				objRefDeleteByOid.execute();
			}
		}
		
		//外部Index
		if (objIndexDelete != null) {
			for (Map.Entry<IndexTableKey, BulkDeleteContext> ent: objIndexDelete.entrySet()) {
				IndexBulkDeleteSql.addValueForDeleteByOidVersion(ent.getValue(), state.tenantId, state.eh, ent.getKey().indexType, oid, ver);
				if (ent.getValue().getCurrentSize() >= state.rdb.getBatchSize()) {
					ent.getValue().execute();
				}
			}
		}
	}

	
	public void flushAll() throws SQLException {
		if (objStoreDelete != null) {
			if (objStoreDelete.getCurrentSize() > 0) {
				objStoreDelete.execute();
			}
		}
		if (objRefDeleteByOid != null) {
			if (objRefDeleteByOid.getCurrentSize() > 0) {
				objRefDeleteByOid.execute();
			}
		}
		if (objIndexDelete != null) {
			for (Map.Entry<IndexTableKey, BulkDeleteContext> ent: objIndexDelete.entrySet()) {
				if (ent.getValue().getCurrentSize() > 0) {
					ent.getValue().execute();
				}
			}
		}
	}

	public void close() {
		if (objStoreDelete != null) {
			try {
				objStoreDelete.close();
			} catch (SQLException e) {
				logger.error("fail to BulkDeleteHandler close. maybe resource leak.", e);
			}
		}
		if (objRefDeleteByOid != null) {
			try {
				objRefDeleteByOid.close();
			} catch (SQLException e) {
				logger.error("fail to BulkDeleteHandler close. maybe resource leak.", e);
			}
		}
		if (objIndexDelete != null) {
			for (Map.Entry<IndexTableKey, BulkDeleteContext> ent: objIndexDelete.entrySet()) {
				try {
					ent.getValue().close();
				} catch (SQLException e) {
					logger.error("fail to BulkDeleteHandler close. maybe resource leak.", e);
				}
			}
		}
	}

}
