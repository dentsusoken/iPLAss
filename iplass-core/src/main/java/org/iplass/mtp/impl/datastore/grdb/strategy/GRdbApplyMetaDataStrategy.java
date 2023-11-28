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

package org.iplass.mtp.impl.datastore.grdb.strategy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.impl.datastore.grdb.GRdbDataStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreMaintenanceSql;
import org.iplass.mtp.impl.datastore.grdb.sql.SchemaControlHandleLockSql;
import org.iplass.mtp.impl.datastore.grdb.sql.SchemaControlInsertSql;
import org.iplass.mtp.impl.datastore.grdb.sql.SchemaControlSearchSql;
import org.iplass.mtp.impl.datastore.grdb.sql.SchemaControlUpdateVersionSql;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColContext.ColCopy;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.ColResolver;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.LockStatus;
import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.diff.UpdEntity;
import org.iplass.mtp.impl.datastore.strategy.ApplyMetaDataStrategy;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.entity.MetaSchemalessRdbStoreMapping;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class GRdbApplyMetaDataStrategy implements ApplyMetaDataStrategy {

	private GRdbDataStore dataStore;
	private RdbAdapter rdb;

	private SchemaControlInsertSql scInsSql;
	private SchemaControlHandleLockSql schlSql;
	private SchemaControlUpdateVersionSql scuvSql;
	private SchemaControlSearchSql scSearchSql;

	public GRdbApplyMetaDataStrategy(GRdbDataStore dataStore, RdbAdapter rdb) {
		this.dataStore = dataStore;
		this.rdb = rdb;
		scInsSql = rdb.getUpdateSqlCreator(SchemaControlInsertSql.class);
		schlSql = rdb.getUpdateSqlCreator(SchemaControlHandleLockSql.class);
		scuvSql = rdb.getUpdateSqlCreator(SchemaControlUpdateVersionSql.class);
		scSearchSql = rdb.getQuerySqlCreator(SchemaControlSearchSql.class);
	}

	@Override
	public void create(final MetaEntity newOne,
			final EntityContext context/*, final int version*/) {

		MetaSchemalessRdbStoreMapping mapping = (MetaSchemalessRdbStoreMapping) newOne.getStoreMapping();
		StorageSpaceMap ssMap = dataStore.getStorageSpaceMapOrDefault(mapping);

		ColResolver colResolver = new ColResolver(null, mapping, ssMap, rdb);

		MetaGRdbEntityStore ssDef = colResolver.getMetaStore();
		newOne.setEntityStoreDefinition(ssDef);

		ssDef.setVersion(0);
		ssDef.setTableNamePostfix(ssMap.generateTableNamePostfix(context.getLocalTenantId(), newOne.getId()));

		for (MetaProperty prop: newOne.getDeclaredPropertyList()) {
			if (prop instanceof MetaPrimitiveProperty) {
				colResolver.allocateCol((MetaPrimitiveProperty) prop, null, newOne.getVersionControlType());
			}
		}

		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {
				//同一IDのデータ存在チェック
				int currentVer = -1;
				ResultSet rs = getStatement().executeQuery(scSearchSql.toSql(context.getLocalTenantId(), newOne.getId(), false, rdb));
				try {
					currentVer = scSearchSql.getCurrentVersion(rs);
				} finally {
					rs.close();
				}

				int count = 0;
				if (currentVer >= 0) {
					//SchemaControlのOBJ_DEF_VER,CR_DATA_VER更新
					String sql = scuvSql.toSql(context.getLocalTenantId(), newOne.getId(), currentVer, 0, true, LockStatus.NO_LOCK, rdb);
					count = getStatement().executeUpdate(sql);
				} else {
					//SchemaControlに追加
//					String sql = scInsSql.toSql(context.getLocalTenantId(), newOne.getId(), rdb);
					String sql = scInsSql.toSql(context.getLocalTenantId(), newOne.getId(), 0, rdb);
					count = getStatement().executeUpdate(sql);
				}
				return count > 0;
			}

		};

		exec.execute(rdb, true);
	}

	@Override
	public boolean isLocked(final MetaEntity definition, final EntityContext context) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {

				boolean locked = false;
				ResultSet rs = getStatement().executeQuery(scSearchSql.toSql(context.getLocalTenantId(), definition.getId(), false, rdb));
				try {
					locked = scSearchSql.isLocked(rs);
				} finally {
					rs.close();
				}
				return locked;
			}

		};
		return exec.execute(rdb, true).booleanValue();
	}

	@Override
	public boolean prepare(final MetaEntity newOne,
			final MetaEntity previous, final EntityContext context) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {
				int currentVer = ((MetaGRdbEntityStore) previous.getEntityStoreDefinition()).getVersion();

				boolean needIns = false;
				ResultSet rs = getStatement().executeQuery(scSearchSql.toSql(context.getLocalTenantId(), previous.getId(), true, rdb));
				try {
					if (!rs.next()) {
						needIns = true;
					}
				} finally {
					rs.close();
				}
				if (needIns) {
					//共通Entityの初回カスタマイズの場合、スキーマ制御テーブルに存在しないので作成する。
					String insSql = scInsSql.toSql(context.getLocalTenantId(), previous.getId(), currentVer, rdb);
					getStatement().executeUpdate(insSql);
				}

				//SchemaControlのLOCK_STATUSをLOCK
				String sql = schlSql.toSql(context.getLocalTenantId(), previous.getId(), currentVer, LockStatus.NO_LOCK, LockStatus.LOCK, rdb);
				int count = getStatement().executeUpdate(sql);
				return count > 0;
			}

		};

		return exec.execute(rdb, true).booleanValue();
	}

	@Override
	public boolean modify(final MetaEntity newOne, final MetaEntity previous, final EntityContext context, final int[] targetTenantIds) {

		final UpdEntity updateDiff = new UpdEntity(previous, newOne, context,
				dataStore.getStorageSpaceMapOrDefault((MetaSchemalessRdbStoreMapping) previous.getStoreMapping()),
				dataStore.getStorageSpaceMapOrDefault((MetaSchemalessRdbStoreMapping) newOne.getStoreMapping()), rdb,
				dataStore.isForceRegenerateTableNamePostfix());
		updateDiff.modifyMetaData();

		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {
				for (int tenantId: targetTenantIds) {
					updateDiff.applyToData(getStatement(), rdb, tenantId);
				}

				int currentVer = ((MetaGRdbEntityStore) previous.getEntityStoreDefinition()).getVersion();
				int newVer = ((MetaGRdbEntityStore) newOne.getEntityStoreDefinition()).getVersion();

				//SchemaControlのOBJ_DEF_VER,CR_DATA_VER(データ変更が必要な場合のみ)更新
				String sql = scuvSql.toSql(context.getLocalTenantId(), newOne.getId(), currentVer, newVer, updateDiff.needDataModify(), LockStatus.LOCK, rdb);
				int count = getStatement().executeUpdate(sql);

				return count > 0;
			}
		};

		return exec.execute(rdb, true).booleanValue();
	}

	@Override
	public void patchData(MetaEntity newOne, MetaEntity previous,
			EntityContext context, final int targetTenantId) {
		final UpdEntity updateDiff = new UpdEntity(previous, newOne, context,
				dataStore.getStorageSpaceMapOrDefault((MetaSchemalessRdbStoreMapping) previous.getStoreMapping()),
				dataStore.getStorageSpaceMapOrDefault((MetaSchemalessRdbStoreMapping) newOne.getStoreMapping()), rdb);
		if (updateDiff.needDataModify()) {
			updateDiff.modifyMetaData();
			SqlExecuter<Void> exec = new SqlExecuter<Void>() {
				@Override
				public Void logic() throws SQLException {
					updateDiff.applyToData(getStatement(), rdb, targetTenantId);
					return null;
				}
			};
			exec.execute(rdb, true);
		}
	}

	@Override
	public void finish(final boolean modifyResult, final MetaEntity newOne,
			final MetaEntity previous, final EntityContext context) {

		MetaEntity toEnt;
		if (modifyResult) {
			toEnt = newOne;
		} else {
			toEnt = previous;
		}

		//SchemaControlのLOCK_STATUSをNO_LOCK
		int ver = ((MetaGRdbEntityStore) toEnt.getEntityStoreDefinition()).getVersion();

		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {
				String sql = schlSql.toSql(context.getLocalTenantId(), toEnt.getId(), ver, LockStatus.LOCK, LockStatus.NO_LOCK, rdb);
				int count = getStatement().executeUpdate(sql);
				return count > 0;
			}
		};

		Boolean res = exec.execute(rdb, true);
		if (!res.booleanValue()) {
			throw new EntityRuntimeException("fail unlock Entity:" + previous.getName() + "(tenant=" + context.getLocalTenantId() + ",id=" + toEnt.getId() + ",version=" + ver + ",modifyResult=" + modifyResult + ")");
		}
	}

	@Override
	public boolean defrag(MetaEntity target, EntityContext context, int[] targetTenantIds) {

		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {
				MetaSchemalessRdbStoreMapping mapping = (MetaSchemalessRdbStoreMapping) target.getStoreMapping();
				StorageSpaceMap ssMap = dataStore.getStorageSpaceMapOrDefault(mapping);
				int currentVer = ((MetaGRdbEntityStore) target.getEntityStoreDefinition()).getVersion();
				int currentPageNo = ((MetaGRdbEntityStore) target.getEntityStoreDefinition()).currentMaxPage();

				ColResolver colResolver = new ColResolver(target, (MetaSchemalessRdbStoreMapping) target.getStoreMapping(), ssMap, rdb);
				colResolver.shrink();

				//更新対象なし
				if (!colResolver.getColContext().hasColCopy()) {
					return true;
				}

				int newVer = currentVer + 1;
				colResolver.getMetaStore().setVersion(newVer);
				target.setEntityStoreDefinition(colResolver.getMetaStore());
				int newPageNo = colResolver.getMetaStore().currentMaxPage();

				for (int tenantId: targetTenantIds) {
					ObjStoreMaintenanceSql sc = rdb.getUpdateSqlCreator(ObjStoreMaintenanceSql.class);

					switch (rdb.getMultiTableUpdateMethod()) {
					case INLINE_VIEW:
						//TODO ORA-01792の発生を防ぐように実装が必要。。。
//						ccls = new List[newPageNo + 1];
//						for (int i = 0; i <= newPageNo; i++) {
//							ccls[i] = colResolver.getColContext().getColCopyList(i);
//						}
//						getStatement().executeUpdate(sc.updateColInlineView(tenantId, target.getId(), colResolver.getMetaStore().getVersion(), ccls, colResolver.getMetaStore().getTableNamePostfix(), rdb));
//						getStatement().executeUpdate(sc.updateColInlineViewRB(tenantId, target.getId(), colResolver.getMetaStore().getVersion(), ccls, colResolver.getMetaStore().getTableNamePostfix(), rdb));
//						break;
					case NO_SUPPORT:
						//pageNo=0から更新
						List<ColCopy> ccl = colResolver.getColContext().getColCopyList(0);
						getStatement().executeUpdate(sc.updateCol(tenantId, target.getId(), colResolver.getMetaStore().getVersion(), 0, ccl, colResolver.getMetaStore().getTableNamePostfix(), rdb));
						getStatement().executeUpdate(sc.updateColRB(tenantId, target.getId(), colResolver.getMetaStore().getVersion(), 0, ccl, colResolver.getMetaStore().getTableNamePostfix(), rdb));

						for (int i = 1; i <= newPageNo; i++) {
							//pageNo=1以降更新
							ccl = colResolver.getColContext().getColCopyList(i);
							if (ccl != null) {
								getStatement().executeUpdate(sc.updateCol(tenantId, target.getId(), colResolver.getMetaStore().getVersion(), i, ccl, colResolver.getMetaStore().getTableNamePostfix(), rdb));
								getStatement().executeUpdate(sc.updateColRB(tenantId, target.getId(), colResolver.getMetaStore().getVersion(), i, ccl, colResolver.getMetaStore().getTableNamePostfix(), rdb));
							}
						}
						break;
					case DIRECT_JOIN:
						@SuppressWarnings("unchecked")
						List<ColCopy>[] ccls = new List[newPageNo + 1];
						for (int i = 0; i <= newPageNo; i++) {
							ccls[i] = colResolver.getColContext().getColCopyList(i);
						}
						getStatement().executeUpdate(sc.updateColDirectJoin(tenantId, target.getId(), colResolver.getMetaStore().getVersion(), ccls, colResolver.getMetaStore().getTableNamePostfix(), rdb));
						getStatement().executeUpdate(sc.updateColDirectJoinRB(tenantId, target.getId(), colResolver.getMetaStore().getVersion(), ccls, colResolver.getMetaStore().getTableNamePostfix(), rdb));
						break;
					default:
						break;
					}

					//不要ページ削除
					if (newPageNo < currentPageNo) {
						getStatement().executeUpdate(sc.deletePage(tenantId, target.getId(), newPageNo + 1, currentPageNo, colResolver.getMetaStore().getTableNamePostfix(), rdb));
						getStatement().executeUpdate(sc.deletePageRB(tenantId, target.getId(), newPageNo + 1, currentPageNo, colResolver.getMetaStore().getTableNamePostfix(), rdb));
					}
				}

				//SchemaControlのOBJ_DEF_VER,CR_DATA_VER更新
				String sql = scuvSql.toSql(context.getLocalTenantId(), target.getId(), currentVer, newVer, true, LockStatus.LOCK, rdb);
				int count = getStatement().executeUpdate(sql);

				return count > 0;

			}
		};

		return exec.execute(rdb, true).booleanValue();
	}

}
