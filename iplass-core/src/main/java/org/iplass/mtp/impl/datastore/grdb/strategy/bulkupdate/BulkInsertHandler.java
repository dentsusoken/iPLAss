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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.mapper.ColumnValueMapper;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.IndexBulkInsertSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.ObjStoreBulkInsertSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.ReferenceBulkInsertSql;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BulkInsertHandler {
	private static Logger logger = LoggerFactory.getLogger(BulkInsertHandler.class);

	BulkInsertContext[] objStoreInsert;
	List<ColumnValueMapper>[] objStoreInsertMapper;
	int[] colSize;

	BulkInsertContext objRefInsert;
	List<ReferencePropertyHandler> refs;

	Map<IndexTableKey, BulkInsertContext> objIndexInsert;
	List<PrimitivePropertyHandler> indexes;

	Statement searchExtIndex;

	@SuppressWarnings("unchecked")
	BulkInsertHandler(BulkUpdateState state) throws SQLException {
		GRdbEntityStoreRuntime store = (GRdbEntityStoreRuntime) state.eh.getEntityStoreRuntime();
		//本体
		String tableName = store.OBJ_STORE();
		objStoreInsertMapper = new List[store.getCurrentMaxPage() + 1];
		objStoreInsert = new BulkInsertContext[objStoreInsertMapper.length];
		colSize = new int[objStoreInsertMapper.length];
		for (int i = 0; i <= store.getCurrentMaxPage(); i++) {
			if (i == 0) {
				objStoreInsertMapper[i] = ObjStoreBulkInsertSql.insertMain(state.tenantId, state.eh, state.rdb, state.entityContext);
			} else {
				objStoreInsertMapper[i] = ObjStoreBulkInsertSql.insertSubPage(state.tenantId, state.eh, i, state.rdb, state.entityContext);
			}

			BulkInsertContext ret = state.rdb.createBulkInsertContext();
			List<ColumnValue> cv = new ArrayList<>();
			for (ColumnValueMapper m: objStoreInsertMapper[i]) {
				m.columns(cv, state.rdb);
			}
			ret.setContext(tableName, cv, state.con);
			objStoreInsert[i] = ret;
			colSize[i] = cv.size();
		}

		//参照
		refs = state.eh.getReferencePropertyList(true, state.entityContext);
		if (refs.size() > 0) {
			objRefInsert = ReferenceBulkInsertSql.insert(state.eh, state.con, state.rdb);
		}

		//外部Index
		indexes = new ArrayList<>();
		for (PrimitivePropertyHandler pph: state.eh.getIndexedPropertyList(state.entityContext)) {
			GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) pph.getStoreSpecProperty();
			if (colDef.isExternalIndex()) {
				indexes.add(pph);
			}
		}
		for (PrimitivePropertyHandler pph: indexes) {
			GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) pph.getStoreSpecProperty();
			if (objIndexInsert == null) {
				objIndexInsert = new HashMap<>();
			}
			IndexTableKey ikey = new IndexTableKey(pph.getMetaData().getIndexType(), colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
			BulkInsertContext bic = objIndexInsert.get(ikey);
			if (bic == null) {
				bic = IndexBulkInsertSql.insert(colDef.asList().get(0), state.con, state.rdb);
				objIndexInsert.put(ikey, bic);
			}
		}
	}

	void addValue(BulkUpdateState state, Entity e) throws SQLException {
		for (int i = 0; i < objStoreInsert.length; i++) {
			ArrayList<Object> values = new ArrayList<>(colSize[i]);
			for (ColumnValueMapper cm: objStoreInsertMapper[i]) {
				cm.values(values, e, state.rdb);
			}
			objStoreInsert[i].add(values);
			if (objStoreInsert[i].getCurrentSize() >= state.rdb.getBatchSize()) {
				objStoreInsert[i].execute();
			}
		}

		String oid = e.getOid();
		Long ver = e.getVersion();

		//参照
		if (objRefInsert != null) {
			for (ReferencePropertyHandler rh: refs) {
				String targetObjDefId = rh.getReferenceEntityHandler(state.entityContext).getMetaData().getId();
				Object val = e.getValue(rh.getName());
				if (val != null) {
					if (val instanceof Entity[]) {
						for (Entity target: (Entity[]) val) {
							ReferenceBulkInsertSql.addValueForInsert(objRefInsert, state.tenantId, state.eh, rh.getId(), oid, ver, targetObjDefId, target.getOid(), target.getVersion());
							if (objRefInsert.getCurrentSize() >= state.rdb.getBatchSize()) {
								objRefInsert.execute();
							}
						}
					} else {
						Entity target = (Entity) val;
						ReferenceBulkInsertSql.addValueForInsert(objRefInsert, state.tenantId, state.eh, rh.getId(), oid, ver, targetObjDefId, target.getOid(), target.getVersion());
						if (objRefInsert.getCurrentSize() >= state.rdb.getBatchSize()) {
							objRefInsert.execute();
						}
					}
				}
			}
		}

		//外部Index
		if (objIndexInsert != null) {
			for (PrimitivePropertyHandler pph: indexes) {
				GRdbPropertyStoreHandler colDef = ((GRdbPropertyStoreRuntime) pph.getStoreSpecProperty()).asList().get(0);
				IndexTableKey ikey = new IndexTableKey(pph.getMetaData().getIndexType(), colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
				BulkInsertContext bic = objIndexInsert.get(ikey);

				Object val = e.getValue(pph.getName());
				boolean needAdd = true;
				if (ikey.indexType != IndexType.NON_UNIQUE) {
					//valueがnullでもinsrt（nullもUNIQUE項目として判断するため）
					//バージョン管理上で、ユニークIndexの変更はできない
					//すでに登録されている場合はinsertしない
					if (state.eh.isVersioned()) {
						if (searchExtIndex == null) {
							searchExtIndex = state.con.createStatement();
						}
						try (ResultSet rs = searchExtIndex.executeQuery(IndexBulkInsertSql.searchByOid(state.tenantId, colDef, oid, state.rdb))) {
							if (rs.next()) {
								needAdd = false;
							}
						}
					}
					if (ikey.indexType == IndexType.UNIQUE_WITHOUT_NULL && val == null) {
						needAdd = false;
					}
				}

				if (needAdd) {
					IndexBulkInsertSql.addValueForInsert(bic, state.tenantId, colDef, oid, ver, val);
				}

				if (bic.getCurrentSize() >= state.rdb.getBatchSize()) {
					bic.execute();
				}
			}
		}
	}


	public void flushAll() throws SQLException {
		for (BulkInsertContext bic: objStoreInsert) {
			if (bic.getCurrentSize() > 0) {
				bic.execute();
			}
		}
		if (objRefInsert != null) {
			if (objRefInsert.getCurrentSize() > 0) {
				objRefInsert.execute();
			}
		}
		if (objIndexInsert != null) {
			for (Map.Entry<IndexTableKey, BulkInsertContext> e: objIndexInsert.entrySet()) {
				if (e.getValue().getCurrentSize() > 0) {
					e.getValue().execute();
				}
			}
		}
	}

	public void close() {
		for (BulkInsertContext bic: objStoreInsert) {
			try {
				bic.close();
			} catch (SQLException e) {
				logger.error("fail to BulkInsertHandler close. maybe resource leak.", e);
			}
		}
		if (objRefInsert != null) {
			try {
				objRefInsert.close();
			} catch (SQLException e) {
				logger.error("fail to BulkInsertHandler close. maybe resource leak.", e);
			}
		}
		if (objIndexInsert != null) {
			for (Map.Entry<IndexTableKey, BulkInsertContext> ent: objIndexInsert.entrySet()) {
				try {
					ent.getValue().close();
				} catch (SQLException e) {
					logger.error("fail to BulkInsertHandler close. maybe resource leak.", e);
				}
			}
		}
		if (searchExtIndex != null) {
			try {
				searchExtIndex.close();
			} catch (SQLException e) {
				logger.error("fail to BulkInsertHandler close. maybe resource leak.", e);
			}
		}
	}

}
