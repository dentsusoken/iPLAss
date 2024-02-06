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
package org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate;

import java.sql.SQLException;
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
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.IndexBulkDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.IndexBulkInsertSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.ObjStoreBulkUpdateSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.ObjStoreBulkUpdateSql.ObjStoreBulkUpdateSqlResult;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.ReferenceBulkDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.sql.ReferenceBulkInsertSql;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkUpdateContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.ColumnValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BulkUpdateHandler {
	private static Logger logger = LoggerFactory.getLogger(BulkUpdateHandler.class);

	static class IndexUpdateContext {
		BulkDeleteContext objIndexDeleteByOidAndVersion;//objIndexInsertの前に実行される必要あり
		BulkInsertContext objIndexInsert;
	}

	BulkUpdateContext[] objStoreUpdate;
	ObjStoreBulkUpdateSqlResult[] objStoreUpdateMapper;
	int[] colSize;

	BulkDeleteContext objRefDeleteByOidPropId;//objRefInsertの前に実行される必要あり
	BulkInsertContext objRefInsert;
	List<ReferencePropertyHandler> refs;

	Map<IndexTableKey, IndexUpdateContext> objIndexUpdate;
	List<PrimitivePropertyHandler> indexes;

	BulkUpdateHandler(BulkUpdateState state) throws SQLException {
		GRdbEntityStoreRuntime store = (GRdbEntityStoreRuntime) state.eh.getEntityStoreRuntime();
		//本体
		String tableName = store.OBJ_STORE();
		objStoreUpdateMapper = new ObjStoreBulkUpdateSqlResult[store.getCurrentMaxPage() + 1];
		objStoreUpdate = new BulkUpdateContext[objStoreUpdateMapper.length];
		colSize = new int[objStoreUpdateMapper.length];
		for (int i = 0; i <= store.getCurrentMaxPage(); i++) {
			if (i == 0) {
				objStoreUpdateMapper[i] = ObjStoreBulkUpdateSql.updateMain(state.tenantId, state.eh, state.bulkUpdatable.getUpdateProperties(), state.rdb, state.entityContext);
			} else {
				objStoreUpdateMapper[i] = ObjStoreBulkUpdateSql.updateSub(state.tenantId, state.eh, i, state.bulkUpdatable.getUpdateProperties(), state.rdb, state.entityContext);
			}

			if (objStoreUpdateMapper[i] != null) {
				BulkUpdateContext ret = state.rdb.createBulkUpdateContext();
				List<ColumnValue> keyCv = new ArrayList<>();
				for (ColumnValueMapper m: objStoreUpdateMapper[i].keys) {
					m.columns(keyCv, state.rdb);
				}
				List<ColumnValue> valueCv = new ArrayList<>();
				for (ColumnValueMapper m: objStoreUpdateMapper[i].values) {
					m.columns(valueCv, state.rdb);
				}
				ret.setContext(tableName, keyCv, valueCv, objStoreUpdateMapper[i].additionalConditionExpression, state.con);
				objStoreUpdate[i] = ret;
				colSize[i] = valueCv.size();
			}
		}

		//参照の更新
		refs = state.eh.getReferencePropertyList(true, state.entityContext);
		if (state.bulkUpdatable.getUpdateProperties() != null) {
			ArrayList<ReferencePropertyHandler> filtered = new ArrayList<>();
			for (ReferencePropertyHandler rph: refs) {
				if (state.bulkUpdatable.getUpdateProperties().contains(rph.getName())) {
					filtered.add(rph);
				}
			}
			refs = filtered;
		}
		if (refs.size() > 0) {
			objRefDeleteByOidPropId = ReferenceBulkDeleteSql.deleteByOidVersionRefId(state.eh, state.con, state.rdb);
			objRefInsert = ReferenceBulkInsertSql.insert(state.eh, state.con, state.rdb);
		}

		//外部インデックスの更新
		//外部Index
		//version管理かつ、ユニークインデックスは更新しない
		indexes = new ArrayList<>();
		for (PrimitivePropertyHandler pph: state.eh.getIndexedPropertyList(state.entityContext)) {
			if (state.bulkUpdatable.getUpdateProperties() == null || state.bulkUpdatable.getUpdateProperties().contains(pph.getName())) {
				GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) pph.getStoreSpecProperty();
				if (colDef.isExternalIndex()) {
					boolean needAdd = true;
					if (state.eh.isVersioned()) {
						if (pph.getMetaData().getIndexType() == IndexType.UNIQUE || pph.getMetaData().getIndexType() == IndexType.UNIQUE_WITHOUT_NULL) {
							needAdd = false;
						}
					}
					if (needAdd) {
						indexes.add(pph);
					}
				}
			}
		}
		for (PrimitivePropertyHandler pph: indexes) {
			GRdbPropertyStoreRuntime colDef = (GRdbPropertyStoreRuntime) pph.getStoreSpecProperty();
			if (objIndexUpdate == null) {
				objIndexUpdate = new HashMap<>();
			}
			IndexTableKey ikey = new IndexTableKey(pph.getMetaData().getIndexType(), colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
			IndexUpdateContext iuc = objIndexUpdate.get(ikey);
			if (iuc == null) {
				GRdbPropertyStoreHandler col = colDef.asList().get(0);
				iuc = new IndexUpdateContext();
				iuc.objIndexDeleteByOidAndVersion = IndexBulkDeleteSql.deleteByOidVersionColName(col, state.con, state.rdb);
				iuc.objIndexInsert = IndexBulkInsertSql.insert(col, state.con, state.rdb);
				objIndexUpdate.put(ikey, iuc);
			}
		}
	}

	public void flushAll() throws SQLException {
		for (BulkUpdateContext buc: objStoreUpdate) {
			if (buc != null && buc.getCurrentSize() > 0) {
				buc.execute();
			}
		}
		if (objRefDeleteByOidPropId != null) {
			if (objRefDeleteByOidPropId.getCurrentSize() > 0) {
				objRefDeleteByOidPropId.execute();
			}
		}
		if (objRefInsert != null) {
			if (objRefInsert.getCurrentSize() > 0) {
				objRefInsert.execute();
			}
		}
		if (objIndexUpdate != null) {
			for (Map.Entry<IndexTableKey, IndexUpdateContext> e: objIndexUpdate.entrySet()) {
				if (e.getValue().objIndexDeleteByOidAndVersion.getCurrentSize() > 0) {
					e.getValue().objIndexDeleteByOidAndVersion.execute();
				}
				if (e.getValue().objIndexInsert.getCurrentSize() > 0) {
					e.getValue().objIndexInsert.execute();
				}
			}
		}
	}

	public void close() {
		for (BulkUpdateContext buc: objStoreUpdate) {
			try {
				if (buc != null) {
					buc.close();
				}
			} catch (SQLException e) {
				logger.error("fail to BulkUpdateHandler close. maybe resource leak.", e);
			}
		}
		if (objRefDeleteByOidPropId != null) {
			try {
				objRefDeleteByOidPropId.close();
			} catch (SQLException e) {
				logger.error("fail to BulkUpdateHandler close. maybe resource leak.", e);
			}
		}
		if (objRefInsert != null) {
			try {
				objRefInsert.close();
			} catch (SQLException e) {
				logger.error("fail to BulkUpdateHandler close. maybe resource leak.", e);
			}
		}
		if (objIndexUpdate != null) {
			for (Map.Entry<IndexTableKey, IndexUpdateContext> ent: objIndexUpdate.entrySet()) {
				try {
					ent.getValue().objIndexDeleteByOidAndVersion.close();
				} catch (SQLException e) {
					logger.error("fail to BulkUpdateHandler close. maybe resource leak.", e);
				}
				try {
					ent.getValue().objIndexInsert.close();
				} catch (SQLException e) {
					logger.error("fail to BulkUpdateHandler close. maybe resource leak.", e);
				}
			}
		}
	}

	public void addValue(BulkUpdateState state, Entity entity) throws SQLException {

		for (int i = 0; i < objStoreUpdate.length; i++) {
			if (objStoreUpdate[i] != null) {
				ArrayList<Object> key = new ArrayList<>(5);
				for (ColumnValueMapper cm: objStoreUpdateMapper[i].keys) {
					cm.values(key, entity, state.rdb);
				}
				ArrayList<Object> values = new ArrayList<>(colSize[i]);
				for (ColumnValueMapper cm: objStoreUpdateMapper[i].values) {
					cm.values(values, entity, state.rdb);
				}
				objStoreUpdate[i].add(key, values);
				if (objStoreUpdate[i].getCurrentSize() >= state.rdb.getBatchSize()) {
					objStoreUpdate[i].execute();
				}
			}
		}


		String oid = entity.getOid();
		Long ver = entity.getVersion();

		//参照
		if (objRefInsert != null) {
			for (ReferencePropertyHandler rh: refs) {
				String targetObjDefId = rh.getReferenceEntityHandler(state.entityContext).getMetaData().getId();
				ReferenceBulkDeleteSql.addValueForDeleteByOidVersionRefId(objRefDeleteByOidPropId, state.tenantId, state.eh, rh.getId(), oid, ver);
				if (objRefDeleteByOidPropId.getCurrentSize() >= state.rdb.getBatchSize()) {
					objRefDeleteByOidPropId.execute();
				}

				Object val = entity.getValue(rh.getName());
				if (val != null) {
					if (val instanceof Entity[]) {
						for (Entity target: (Entity[]) val) {
							ReferenceBulkInsertSql.addValueForInsert(objRefInsert, state.tenantId, state.eh, rh.getId(), oid, ver, targetObjDefId, target.getOid(), target.getVersion());
							if (objRefInsert.getCurrentSize() >= state.rdb.getBatchSize()) {
								//削除が優先する必要あり
								if (objRefDeleteByOidPropId.getCurrentSize() > 0) {
									objRefDeleteByOidPropId.execute();
								}
								objRefInsert.execute();
							}
						}
					} else {
						Entity target = (Entity) val;
						ReferenceBulkInsertSql.addValueForInsert(objRefInsert, state.tenantId, state.eh, rh.getId(), oid, ver, targetObjDefId, target.getOid(), target.getVersion());
						if (objRefInsert.getCurrentSize() >= state.rdb.getBatchSize()) {
							//削除が優先する必要あり
							if (objRefDeleteByOidPropId.getCurrentSize() > 0) {
								objRefDeleteByOidPropId.execute();
							}
							objRefInsert.execute();
						}
					}
				}
			}
		}

		//外部Index
		if (objIndexUpdate != null) {
			for (PrimitivePropertyHandler pph: indexes) {
				GRdbPropertyStoreHandler colDef = ((GRdbPropertyStoreRuntime) pph.getStoreSpecProperty()).asList().get(0);
				IndexTableKey ikey = new IndexTableKey(pph.getMetaData().getIndexType(), colDef.getSingleColumnRdbTypeAdapter().getColOfIndex());
				IndexUpdateContext iuc = objIndexUpdate.get(ikey);
				IndexBulkDeleteSql.addValueForDeleteByOidVersionColName(iuc.objIndexDeleteByOidAndVersion, state.tenantId, colDef, oid, ver);
				if (iuc.objIndexDeleteByOidAndVersion.getCurrentSize() >= state.rdb.getBatchSize()) {
					iuc.objIndexDeleteByOidAndVersion.execute();
				}

				Object val = entity.getValue(pph.getName());
				boolean needAdd = true;
				if (ikey.indexType == IndexType.UNIQUE_WITHOUT_NULL && val == null) {
					needAdd = false;
				}
				if (needAdd) {
					IndexBulkInsertSql.addValueForInsert(iuc.objIndexInsert, state.tenantId, colDef, oid, ver, val);
				}

				if (iuc.objIndexInsert.getCurrentSize() >= state.rdb.getBatchSize()) {
					//deleteが先に実行される必要あり
					if (iuc.objIndexDeleteByOidAndVersion.getCurrentSize() > 0) {
						iuc.objIndexDeleteByOidAndVersion.execute();
					}
					iuc.objIndexInsert.execute();
				}
			}
		}
	}

}
