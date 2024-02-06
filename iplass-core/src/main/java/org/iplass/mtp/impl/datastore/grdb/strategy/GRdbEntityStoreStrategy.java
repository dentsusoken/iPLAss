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

package org.iplass.mtp.impl.datastore.grdb.strategy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateCondition.UpdateValue;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.definition.IndexType;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.impl.counter.CounterService;
import org.iplass.mtp.impl.datastore.EQLAdditionalWarnLogInfo;
import org.iplass.mtp.impl.datastore.grdb.GRdbDataStore;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbPropertyStore.GRdbPropertyStoreHandler;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.datastore.grdb.sql.IndexDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.sql.IndexInsertSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreInsertSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreLockSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreSearchSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ObjStoreUpdateSql;
import org.iplass.mtp.impl.datastore.grdb.sql.RecycleBinSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ReferenceDeleteSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ReferenceInsertSql;
import org.iplass.mtp.impl.datastore.grdb.sql.ToSqlResult;
import org.iplass.mtp.impl.datastore.grdb.sql.ToSqlResult.BindValue;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjIndexTable;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.BulkContextBaseBulkUpdateStrategy;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.BulkUpdateStrategy;
import org.iplass.mtp.impl.datastore.grdb.strategy.bulkupdate.EachRecordBulkUpdateStrategy;
import org.iplass.mtp.impl.datastore.strategy.EntityStoreStrategy;
import org.iplass.mtp.impl.datastore.strategy.RecycleBinIterator;
import org.iplass.mtp.impl.datastore.strategy.SearchResultIterator;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.MetaSchemalessRdbStoreMapping;
import org.iplass.mtp.impl.entity.QueryOption;
import org.iplass.mtp.impl.entity.property.MetaReferenceProperty;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter.Null;
import org.iplass.mtp.impl.rdb.adapter.MultiInsertContext;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.connection.PreparedStatementWrapper;
import org.iplass.mtp.impl.rdb.connection.StatementWrapper;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GRdbEntityStoreStrategy implements EntityStoreStrategy {
	private static Logger log = LoggerFactory.getLogger(GRdbEntityStoreStrategy.class);

	private static final String BULK_STRATEGY = "mtp.grdb.BulkUpdateStrategy";
	private static final String TREAT_NON_UNIQUE_EXT_INDEX = "mtp.grdb.treatNonUniqueExternalIndexTable";

	private static final String COUNTER_SERVICE_INC_KEY = "oid";
	private static final String COUNTER_SERVICE_INC_KEY_RB = "rbid";

	private GRdbDataStore dataStore;
	private RdbAdapter rdb;
	private CounterService counterService;

	private BulkUpdateStrategy buStrategy;
	private boolean treatNonUniqueExternalIndexTable;

	private ObjStoreInsertSql insertSql;
	private ObjStoreUpdateSql updateSql;
	private ObjStoreDeleteSql delSql;
	private ObjStoreSearchSql searchSql;
	private ObjStoreLockSql lockSql;
	private IndexDeleteSql indexDelSql;
	private ReferenceDeleteSql refDelSql;
	private ReferenceInsertSql referenceInsertSql;
	private IndexInsertSql indexInsertSql;
	private RecycleBinSql recycleBinSql;

	public GRdbEntityStoreStrategy(GRdbDataStore dataStore, RdbAdapter rdb, CounterService counterService) {
		this.dataStore = dataStore;
		this.rdb = rdb;
		this.counterService = counterService;
		buStrategy = createBulkUpdateStrategy();
		insertSql = rdb.getUpdateSqlCreator(ObjStoreInsertSql.class);
		updateSql = rdb.getUpdateSqlCreator(ObjStoreUpdateSql.class);
		delSql = rdb.getUpdateSqlCreator(ObjStoreDeleteSql.class);
		indexDelSql = rdb.getUpdateSqlCreator(IndexDeleteSql.class);
		refDelSql = rdb.getUpdateSqlCreator(ReferenceDeleteSql.class);
		referenceInsertSql = rdb.getUpdateSqlCreator(ReferenceInsertSql.class);
		indexInsertSql = rdb.getUpdateSqlCreator(IndexInsertSql.class);
		searchSql = rdb.getQuerySqlCreator(ObjStoreSearchSql.class);
		lockSql = rdb.getQuerySqlCreator(ObjStoreLockSql.class);
		recycleBinSql = rdb.getUpdateSqlCreator(RecycleBinSql.class);

		treatNonUniqueExternalIndexTable = Boolean.getBoolean(TREAT_NON_UNIQUE_EXT_INDEX);

	}

	private BulkUpdateStrategy createBulkUpdateStrategy() {
		String propBulkStrategy = System.getProperty(BULK_STRATEGY);
		if (propBulkStrategy != null
				&& EachRecordBulkUpdateStrategy.class.getName().equals(propBulkStrategy)) {
			return new EachRecordBulkUpdateStrategy(this, rdb);
		}

		return new BulkContextBaseBulkUpdateStrategy(this, rdb);
	}

	@Override
	public int count(final EntityContext context, final Query query) {
		//sqlを、インラインビューにして、select count(*) form (～)として実行
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {

			@Override
			public Integer logic() throws SQLException {

				EntityHandler handler = context.getHandlerByName(query.getFrom().getEntityName());
				ToSqlResult sql = searchSql.count(handler, context, query, rdb.isEnableBindHint(), rdb);
				if (sql.bindVariables == null || sql.bindVariables.size() == 0) {
					//検索SQL実行
					Statement stmt = getStatement();
					stmt.setFetchSize(1);
					QueryOption qo = QueryOption.getQueryOption(query);
					if (qo != null && qo.getQueryTimeout() != 0) {
						qo.setQueryTimeout(qo.getQueryTimeout());
					}
					if (stmt instanceof StatementWrapper) {
						((StatementWrapper) stmt).setAdditionalWarnLogInfo(new EQLAdditionalWarnLogInfo(query, true, handler, context));
					}
					try (ResultSet rs = stmt.executeQuery(sql.sql)) {
						if (stmt instanceof StatementWrapper) {
							((StatementWrapper) stmt).setAdditionalWarnLogInfo(null);
						}
						rs.next();
						return rs.getInt(1);
					}
				} else {
					PreparedStatement stmt = getPreparedStatement(sql.sql);
					if (stmt instanceof PreparedStatementWrapper) {
						((PreparedStatementWrapper) stmt).setAdditionalWarnLogInfo(new EQLAdditionalWarnLogInfo(query, true, handler, context));
					}
					int index = 1;
					for (BindValue val: sql.bindVariables) {
						val.type.setParameter(index, val.value, stmt, rdb);
						index++;
					}
					stmt.setFetchSize(1);
					QueryOption qo = QueryOption.getQueryOption(query);
					if (qo != null && qo.getQueryTimeout() != 0) {
						qo.setQueryTimeout(qo.getQueryTimeout());
					}
					try (ResultSet rs = stmt.executeQuery()) {
						if (stmt instanceof PreparedStatementWrapper) {
							((PreparedStatementWrapper) stmt).setAdditionalWarnLogInfo(null);
						}
						rs.next();
						return rs.getInt(1);
					}
				}
			}
		};

		return exec.execute(rdb, true);
	}

	@Override
	public void delete(final EntityContext context, final Entity model, final EntityHandler handler,
			final DeleteOption option) {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {
				//pageNoが複数ある場合を考慮。まずpageNo=0を削除。成功（=Lockが取れた）したら、pageNo=1以降を一括で削除
				int tenantId = context.getTenantId(handler);
				String mainSql = delSql.deleteMainPageByOid(tenantId, handler, model.getOid(), model.getVersion(), option.isCheckTimestamp(), model.getUpdateDate(), rdb);

				int count = getStatement().executeUpdate(mainSql);
				if (count < 1) {
					throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
				}

				//pageNo > 0あるなら削除
				if (((GRdbEntityStoreRuntime) handler.getEntityStoreRuntime()).getCurrentMaxPage() > 0) {
					getStatement().addBatch(delSql.deleteByOid(tenantId, handler, model.getOid(), model.getVersion(), rdb));
				}

				//indexの削除
				//メタデータ判断してindexを使っていた場合のみ削除
				List<String> delTalbeIndex = new ArrayList<>();
				List<String> delTalbeUnique = new ArrayList<>();
				boolean hasReference = false;
				for (PropertyHandler ph: handler.getDeclaredPropertyList()) {
					if (ph instanceof ReferencePropertyHandler &&
							((MetaReferenceProperty) ph.getMetaData()).getMappedByPropertyMetaDataId() == null) {
						hasReference = true;
					}
					if (ph.getStoreSpecProperty() instanceof GRdbPropertyStoreRuntime) {
						GRdbPropertyStoreRuntime psh = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
						if (psh.getPropertyRuntime().isIndexed() && psh.isExternalIndex()) {
							if (IndexType.NON_UNIQUE == psh.getPropertyRuntime().getMetaData().getIndexType()) {
								if (!delTalbeIndex.contains(psh.getSingleColumnRdbTypeAdapter().getColOfIndex())) {
									delTalbeIndex.add(psh.getSingleColumnRdbTypeAdapter().getColOfIndex());
								}
							} else {
								if (!delTalbeUnique.contains(psh.getSingleColumnRdbTypeAdapter().getColOfIndex())) {
									delTalbeUnique.add(psh.getSingleColumnRdbTypeAdapter().getColOfIndex());
								}
							}
						}
					}
				}
				for (String table: delTalbeIndex) {
					getStatement().addBatch(indexDelSql.toSqlDelByOid(tenantId, handler, table, IndexType.NON_UNIQUE, model.getOid(), model.getVersion(), rdb));
				}
				for (String table: delTalbeUnique) {
					getStatement().addBatch(indexDelSql.toSqlDelByOid(tenantId, handler, table, IndexType.UNIQUE, model.getOid(), model.getVersion(), rdb));
				}

				//メタデータ判断してオブジェクト参照プロパティを持っているときのみ削除
				if (hasReference) {
					getStatement().addBatch(refDelSql.toSql(tenantId, handler, model.getOid(), model.getVersion(), rdb));
				}

				getStatement().executeBatch();

				return null;
			}
		};

		exec.execute(rdb, true);
	}

	@Override
	public String insert(final EntityContext context,
			final EntityHandler handler, final Entity model) {
		SqlExecuter<String> exec = new SqlExecuter<String>() {

			@Override
			public String logic() throws SQLException {
				int tenantId = context.getTenantId(handler);

				//oidの取得
				String oid = model.getOid();

				//InsertSql発行
				MultiInsertContext ctx = rdb.createMultiInsertContext(getStatement());

				String updateSql = insertSql.insertMain(tenantId, handler, model, rdb, context);
				ctx.addInsertSql(updateSql);

				GRdbEntityStoreRuntime store = (GRdbEntityStoreRuntime) handler.getEntityStoreRuntime();
				if (store.getCurrentMaxPage() > 0) {
					for (int i = 1; i <= store.getCurrentMaxPage(); i++) {
						ctx.addInsertSql(insertSql.insertSubPage(tenantId, handler, model, i, rdb, context));
					}
				}

				//外部インデックス追加SQLの発行
				insertIndex(tenantId, oid, handler, model, ctx, getStatement());

				//オブジェクト参照追加SQLの発行
				List<InsertReferenceTarget> insRefs = targetReference(handler, model, context, null);
				if (insRefs.size() > 0) {
					if (isInsertReferenceByPreparedBatch(insRefs)) {
						ctx.execute();
						try (PreparedStatement ps = getPreparedStatement(referenceInsertSql.prepareInsert(handler))) {
							ps.clearBatch();
							insertReferences(tenantId, oid, model.getVersion(), handler, insRefs, null, ps, context);
						}
					} else {
						insertReferences(tenantId, oid, model.getVersion(), handler, insRefs, ctx, null, context);
						ctx.execute();
					}
				} else {
					ctx.execute();
				}

				return oid;
			}
		};

		return exec.execute(rdb, true);
	}

	private boolean insertIndex(int tenantId, String oid,
			EntityHandler dataModelHandler, Entity model, MultiInsertContext ctx, Statement stmt) throws SQLException {
		boolean result = false;
		List<PropertyHandler> props = dataModelHandler.getDeclaredPropertyList();
		for (PropertyHandler storeP: props) {
			if (storeP instanceof PrimitivePropertyHandler
					&& !((PrimitivePropertyHandler) storeP).getMetaData().getType().isVirtual()) {
				//TODO 現状、multi=1のみ対応
				if (storeP.getMetaData().getMultiplicity() == 1) {
					IndexType indexType = storeP.getMetaData().getIndexType();
					if (indexType == IndexType.NON_UNIQUE || indexType == IndexType.UNIQUE || indexType == IndexType.UNIQUE_WITHOUT_NULL) {
						GRdbPropertyStoreHandler storePropHandler = (GRdbPropertyStoreHandler) storeP.getStoreSpecProperty();
						if (storePropHandler.isExternalIndex()) {
							Object val = model.getValue(storeP.getName());
							boolean needAdd = true;
							if (indexType != IndexType.NON_UNIQUE) {
								//valueがnullでもinsrt（nullもUNIQUE項目として判断するため）
								//バージョン管理上で、ユニークIndexの変更はできない
								//すでに登録されている場合はinsertしない
								if (dataModelHandler.isVersioned()) {
									try (ResultSet rs = stmt.executeQuery(indexInsertSql.searchByOid(tenantId, storePropHandler, oid, rdb))) {
										if (rs.next()) {
											needAdd = false;
										}
									}
								}
								if (indexType == IndexType.UNIQUE_WITHOUT_NULL && val == null) {
									needAdd = false;
								}
							}

							if (needAdd) {
								ctx.addInsertSql(indexInsertSql.insert(
										tenantId,
										storePropHandler,
										oid,
										model.getVersion(),
										val,
										rdb));
								result = true;
							}
						}
					}
				}
			}
		}
		return result;
	}

	private final static class InsertReferenceTarget {
		ReferencePropertyHandler rh;
		EntityHandler refEh;
		Object value;
		private InsertReferenceTarget(ReferencePropertyHandler rh,
				EntityHandler refEh, Object value) {
			this.rh = rh;
			this.refEh = refEh;
			this.value = value;
		}

	}

	private List<InsertReferenceTarget> targetReference(EntityHandler dataModelHandler, Entity model, EntityContext ectx, List<String> updatePropList) {
		List<InsertReferenceTarget> ret = null;
		if (updatePropList == null) {
			List<PropertyHandler> props = dataModelHandler.getDeclaredPropertyList();
			for (PropertyHandler p: props) {
				ret = addTargetRefrerence(ret, p, model, ectx);
			}
		} else {
			for (String propName: updatePropList) {
				PropertyHandler p = dataModelHandler.getDeclaredProperty(propName);
				ret = addTargetRefrerence(ret, p, model, ectx);
			}
		}

		if (ret == null) {
			return Collections.emptyList();
		} else {
			return ret;
		}
	}

	private List<InsertReferenceTarget> addTargetRefrerence(List<InsertReferenceTarget> list, PropertyHandler ph, Entity model, EntityContext ectx) {
		if (ph instanceof ReferencePropertyHandler) {
			ReferencePropertyHandler rh = (ReferencePropertyHandler) ph;
			if (rh.getMetaData().getMappedByPropertyMetaDataId() == null) {
				EntityHandler refEh = rh.getReferenceEntityHandler(ectx);
				if (list == null) {
					list = new LinkedList<>();
				}
				list.add(new InsertReferenceTarget(rh, refEh, model.getValue(rh.getName())));
			}
		}
		return list;
	}

	private boolean isInsertReferenceByPreparedBatch(List<InsertReferenceTarget> list) {
		if (rdb.getThresholdCountOfUsePrepareStatement() <= 0) {
			return false;
		}

		int count = 0;
		for (InsertReferenceTarget t: list) {
			if (t.value != null) {
				if (t.rh.getMetaData().getMultiplicity() != 1) {
					if (t.value instanceof Object[]) {
						count += ((Object[]) t.value).length;
					} else {
						count++;
					}
				} else {
					count++;
				}
			}
		}

		return count >= rdb.getThresholdCountOfUsePrepareStatement();
	}

	private void insRef(int[] counter, int tenantId, String oid, Long version, Entity ref, EntityHandler eh, ReferencePropertyHandler rh, EntityHandler refEntityType, MultiInsertContext ctx, PreparedStatement ps) throws SQLException {
		String refOid = ref.getOid();
		Long refVer = ref.getVersion();
		if (refOid == null || refOid.length() == 0) {
			throw new EntityRuntimeException("cant insert reference:" + rh.getName() + ", cause reference entity oid is null or empty String...");
		}
		counter[0]++;
		if (ctx != null) {
			ctx.addInsertSql(referenceInsertSql.insert(tenantId, eh,
					rh.getId(), oid, version, refEntityType.getMetaData().getId(), refOid, refVer, rdb));
		} else {
			ps.clearParameters();
			referenceInsertSql.setPrepareInsertParameter(ps, tenantId, eh, rh.getId(), oid, version, refEntityType.getMetaData().getId(), refOid, refVer, rdb);
			ps.addBatch();
			if (counter[0] % rdb.getBatchSize() == 0) {
				ps.executeBatch();
			}
		}
	}

	private void insertReferences(int tenantId, String oid, Long version,
			EntityHandler eh, List<InsertReferenceTarget> list, MultiInsertContext ctx, PreparedStatement ps, EntityContext ectx) throws SQLException {
		int[] counter = {0};
		for (InsertReferenceTarget t: list) {
			if (t.value != null) {
				if (t.rh.getMetaData().getMultiplicity() != 1) {
					if (t.value instanceof Entity[]) {
						for (Entity ref: (Entity[]) t.value) {
							insRef(counter, tenantId, oid, version, ref, eh, t.rh, t.refEh, ctx, ps);
						}
					} else {
						insRef(counter, tenantId, oid, version, (Entity) t.value, eh, t.rh, t.refEh, ctx, ps);
					}
				} else {
					insRef(counter, tenantId, oid, version, (Entity) t.value, eh, t.rh, t.refEh, ctx, ps);
				}
			}
		}

		if (ps != null) {
			//残りを実行
			if (counter[0] %  rdb.getBatchSize() != 0) {
				ps.executeBatch();
			}
		}
	}

	@Override
	public boolean lock(final EntityContext context, final EntityHandler handler, final String oid) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {
				String sql = lockSql.lockByOid(context.getTenantId(handler), handler, context, oid, rdb);
				//検索SQL実行
				try(ResultSet rs = getStatement().executeQuery(sql)) {
					return rs.next();
				}
			}
		};

		return exec.execute(rdb, true);
	}

	@Override
	public SearchResultIterator search(final EntityContext context,
			final Query query, final EntityHandler eh) {

		SqlExecuter<SearchResultIterator> exec = new SqlExecuter<SearchResultIterator>() {

			@Override
			public SearchResultIterator logic() throws SQLException {

				log.trace("query execute");

				QueryOption option = QueryOption.getQueryOption(query);

				EntityHandler handler = null;
				if (eh.getMetaData().getName().equals(query.getFrom().getEntityName())) {
					handler = eh;
				} else {
					handler = context.getHandlerByName(query.getFrom().getEntityName());
				}
				ToSqlResult sql = searchSql.query(handler, context, query, false, rdb.isEnableBindHint(), dataStore.getStringTypeLengthOnQuery(), rdb);

				log.trace("sql created");

				ResultSet rs = null;
				if (sql.bindVariables == null || sql.bindVariables.size() == 0) {
					//検索SQL実行
					Statement stmt = getStatement();
					if (stmt instanceof StatementWrapper) {
						((StatementWrapper) stmt).setAdditionalWarnLogInfo(new EQLAdditionalWarnLogInfo(query, false, handler, context));
					}
					if (option != null && option.getFetchSize() != 0) {
						if (rdb.getMaxFetchSize() < option.getFetchSize()) {
							stmt.setFetchSize(rdb.getMaxFetchSize());
						} else {
							stmt.setFetchSize(option.getFetchSize());
						}
					}
					if (option != null && option.getQueryTimeout() != 0) {
						stmt.setQueryTimeout(option.getQueryTimeout());
					}
					rs = getStatement().executeQuery(sql.sql);
					if (stmt instanceof StatementWrapper) {
						((StatementWrapper) stmt).setAdditionalWarnLogInfo(null);
					}
				} else {
					PreparedStatement stmt = getPreparedStatement(sql.sql);
					if (stmt instanceof PreparedStatementWrapper) {
						((PreparedStatementWrapper) stmt).setAdditionalWarnLogInfo(new EQLAdditionalWarnLogInfo(query, false, handler, context));
					}

					int index = 1;
					for (BindValue val: sql.bindVariables) {
						val.type.setParameter(index, val.value, stmt, rdb);
						index++;
					}
					if (option != null && option.getFetchSize() != 0) {
						if (rdb.getMaxFetchSize() < option.getFetchSize()) {
							stmt.setFetchSize(rdb.getMaxFetchSize());
						} else {
							stmt.setFetchSize(option.getFetchSize());
						}
					}
					if (option != null && option.getQueryTimeout() != 0) {
						stmt.setQueryTimeout(option.getQueryTimeout());
					}
					rs = stmt.executeQuery();
					if (stmt instanceof PreparedStatementWrapper) {
						((PreparedStatementWrapper) stmt).setAdditionalWarnLogInfo(null);
					}
				}

				log.trace("executed sql");

				return new GRdbSearchResultIterator(rs, handler, context, query, rdb);
			}
		};

		return exec.execute(rdb, false);
	}

	@Override
	public void update(final EntityContext context,
			final EntityHandler handler, final Entity model, final UpdateOption option) {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {
				int tenantId = context.getTenantId(handler);

				//実データの更新
				String sql = updateSql.updateMain(tenantId, handler, model, option.isCheckTimestamp(), option.getUpdateProperties(), rdb, context);
				int count = getStatement().executeUpdate(sql);

				if (count < 1) {
					throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
				}

				MultiInsertContext ctx = rdb.createMultiInsertContext(getStatement());

				GRdbEntityStoreRuntime store = (GRdbEntityStoreRuntime) handler.getEntityStoreRuntime();
				if (store.getCurrentMaxPage() > 0) {
					for (int i = 1; i <= store.getCurrentMaxPage(); i++) {
						String us = updateSql.updateSub(tenantId, handler, model, option.getUpdateProperties(), i, rdb, context);
						if (us != null) {
							ctx.addUpdateSql(us);
						}
					}
				}

				//外部インデックスの更新
				//TODO delete->insertでなく、UPDATEが速いと思うが、スキーマ変更時のデータ不整合発生可能性が完全に払拭されるまでは、delete->insertが無難か？
				updateIndex(tenantId, handler, model, ctx, option.getUpdateProperties());

				//参照の更新
				//TODO delete->insertでなく、差分の更新が速いか？
				String oid = model.getOid();
				Long version = model.getVersion();
				List<InsertReferenceTarget> updRefs = targetReference(handler, model, context, option.getUpdateProperties());
				if (updRefs.size() > 0) {
					for (InsertReferenceTarget t: updRefs) {
						ctx.addUpdateSql(refDelSql.deleteByOidAndVersion(tenantId, handler, t.rh.getId(), oid, version, rdb));
					}

					if (isInsertReferenceByPreparedBatch(updRefs)) {
						if (ctx.isSqlAdded()) {
							ctx.execute();
						}
						try (PreparedStatement ps = getPreparedStatement(referenceInsertSql.prepareInsert(handler))) {
							ps.clearBatch();
							insertReferences(tenantId, oid, model.getVersion(), handler, updRefs, null, ps, context);
						}
					} else {
						insertReferences(tenantId, oid, model.getVersion(), handler, updRefs, ctx, null, context);
						if (ctx.isSqlAdded()) {
							ctx.execute();
						}
					}
				} else {
					if (ctx.isSqlAdded()) {
						ctx.execute();
					}
				}

				return null;
			}
		};

		exec.execute(rdb, true);
	}

	private void updateIndex(int tenantId, EntityHandler dataModelHandler, Entity model, MultiInsertContext ctx, List<String> updatePropList) throws SQLException {
		for (String propName: updatePropList) {
			PropertyHandler storeP = dataModelHandler.getDeclaredProperty(propName);

			if (storeP != null && storeP.getStoreSpecProperty() instanceof GRdbPropertyStoreRuntime) {
				GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) storeP.getStoreSpecProperty();
				if (col.isExternalIndex()) {
					IndexType indexType = storeP.getMetaData().getIndexType();
					if (indexType == IndexType.NON_UNIQUE
							|| indexType == IndexType.UNIQUE
							|| indexType == IndexType.UNIQUE_WITHOUT_NULL) {

						//multiは未対応

						ctx.addUpdateSql(indexDelSql.deleteByOidAndVersion(tenantId, (GRdbPropertyStoreHandler) col, model.getOid(), model.getVersion(), rdb));
						//UNIQUE_WITHOUT_NULLもしくはNON_UNIQUE、かつ、valueがnullの場合はinsertしない
						Object val = model.getValue(storeP.getName());
						if (indexType == IndexType.UNIQUE || val != null) {
							ctx.addInsertSql(indexInsertSql.insert(tenantId, (GRdbPropertyStoreHandler) col, model.getOid(), model.getVersion(), val, rdb));
						}
					}
				}
			}
		}
	}

	@Override
	public int deleteAll(final DeleteCondition cond, final EntityContext entityContext, final EntityHandler handler, final String clientId) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {

				int tenantId = entityContext.getTenantId(handler);

				boolean hasIndexOrRef = false;
				for (PropertyHandler ph: handler.getDeclaredPropertyList()) {
					if (ph instanceof ReferencePropertyHandler &&
							((MetaReferenceProperty) ph.getMetaData()).getMappedByPropertyMetaDataId() == null) {
						hasIndexOrRef = true;
						break;
					} else if (ph.isIndexed()
							&& ph.getStoreSpecProperty() instanceof GRdbPropertyStoreHandler
							&& ((GRdbPropertyStoreHandler) ph.getStoreSpecProperty()).isExternalIndex()) {
						hasIndexOrRef = true;
						break;
					}
				}

				if (!hasIndexOrRef && !cond.isLockStrictly()) {
					//A.削除対象のEntity定義に外部INDEXがない、かつ、参照がない場合
					//　1.削除条件文をダイレクトに適用して、OBJ_STOREを直接削除
					int ret = getStatement().executeUpdate(delSql.deleteByCondition(tenantId, handler, cond, rdb, entityContext));
					int maxPageNo = ((GRdbEntityStoreRuntime) handler.getEntityStoreRuntime()).getCurrentMaxPage();
					if (maxPageNo == 0) {
						return ret;
					} else {
						return ret / (maxPageNo + 1);
					}

				} else {
					//B.それ以外
					//TemporaryTableを作成してそれを結合して、それぞれのテーブル削除
					try {
						//前の処理のデータが残っている可能性がある(同一トランザクション内で複数回のdeleteAll呼び出し、エラー発生時など)ため
						getStatement().addBatch(rdb.deleteTemporaryTable(ObjStoreTable.TABLE_NAME_TMP));

						if (!rdb.isSupportGlobalTemporaryTable()) {
							String createTempTable = rdb.createLocalTemporaryTable(ObjStoreTable.TABLE_NAME_TMP, ObjStoreTable.TABLE_NAME, new String[]{ObjStoreTable.OBJ_ID, ObjStoreTable.OBJ_VER});
							getStatement().addBatch(createTempTable);
						}

						//　1.OBJ_DATAの削除対象のレコードのoidを削除WORKテーブルにINSERT...SELECT
						//　　削除WORKテーブルは一時表として作成
						//　　（この際の検索条件は、INDEXテーブルの利用可）
						getStatement().addBatch(insertSql.copyToTemporaryTable(handler, cond.getWhere(), rdb, entityContext));

						int[] count = getStatement().executeBatch();
						if (count[count.length - 1] == 0) {
							//更新対象なし
							return 0;
						}

						if (cond.isLockStrictly()) {
							String sql = lockSql.lockByTempTable(tenantId, handler, entityContext, rdb);
							//検索SQL実行
							try(ResultSet rs = getStatement().executeQuery(sql)) {
//FIXME nextの呼び出しが必要か確認！！！
//								rs.next();
							}
						}

						//　2.OBJ_INDEXからインデックス用レコード削除
						//　　（この際の検索条件は、削除WORKテーブルを元に）
						Set<String> delTalbeIndex = new HashSet<>();
						Set<String> delTalbeUnique = new HashSet<>();
						boolean hasReference = false;
						for (PropertyHandler ph: handler.getDeclaredPropertyList()) {
							if (ph instanceof ReferencePropertyHandler &&
									((MetaReferenceProperty) ph.getMetaData()).getMappedByPropertyMetaDataId() == null) {
								hasReference = true;
							}
							if (ph.getStoreSpecProperty() instanceof GRdbPropertyStoreHandler) {
								GRdbPropertyStoreHandler psh = (GRdbPropertyStoreHandler) ph.getStoreSpecProperty();
								if (psh.isExternalIndex()) {
									if (IndexType.NON_UNIQUE == psh.getPropertyRuntime().getMetaData().getIndexType()) {
										delTalbeIndex.add(psh.getSingleColumnRdbTypeAdapter().getColOfIndex());
									} else {
										delTalbeUnique.add(psh.getSingleColumnRdbTypeAdapter().getColOfIndex());
									}
								}
							}
						}
						for (String table: delTalbeIndex) {
							getStatement().addBatch(indexDelSql.deleteByTempTable(tenantId, handler, table, IndexType.NON_UNIQUE, rdb));
						}
						for (String table: delTalbeUnique) {
							getStatement().addBatch(indexDelSql.deleteByTempTable(tenantId, handler, table, IndexType.UNIQUE, rdb));
						}

						//　3.OBJ_REFから参照関連レコード削除
						//　（この際の検索条件は、削除WORKテーブルを元に）
						//メタデータ判断してオブジェクト参照プロパティを持っているときのみ削除（or 安全性を考えて毎回削除か検討）
						if (hasReference) {
							getStatement().addBatch(refDelSql.deleteByTempTable(tenantId, handler, rdb, entityContext));
						}

						//　4.OBJ_DATAからレコード削除
						//　（この際の検索条件は、削除WORKテーブルを駆動表にoidでjoin）
						getStatement().addBatch(delSql.deleteByTempTable(tenantId, handler, rdb, entityContext));

						//件数チェック
						int[] resCounts = getStatement().executeBatch();
						int ret = resCounts[resCounts.length - 1];
						int maxPageNo = ((GRdbEntityStoreRuntime) handler.getEntityStoreRuntime()).getCurrentMaxPage();
						if (maxPageNo != 0) {
							if (ret % (maxPageNo + 1) != 0) {
								throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
							}
							ret = ret / (maxPageNo + 1);
						}

						if (count[count.length - 1] != ret) {
							throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
						}

						return ret;
					} finally {
						//　5.削除WORKテーブルからINSERTしたレコード削除
						if (!rdb.isSupportAutoClearTemporaryTableWhenCommit()) {
							//temporary tableの中身をクリア
							try {
								getStatement().executeUpdate(rdb.deleteTemporaryTable(ObjStoreTable.TABLE_NAME_TMP));
							} catch (Exception e) {
								LoggerFactory.getLogger(GRdbEntityStoreStrategy.class).warn("temporary table:" + ObjStoreTable.TABLE_NAME_TMP + " drop fail. may be resource leak...");
							}
						}
					}
				}
			}
		};

		return exec.execute(rdb, true).intValue();
	}

	@Override
	public int updateAll(final UpdateCondition cond, final EntityContext context, final EntityHandler handler, final String clientId) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {

				int tenantId = context.getTenantId(handler);

				boolean hasExIndex = false;
				for (UpdateValue uv: cond.getValues()) {
					GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) handler.getProperty(uv.getEntityField(), context).getStoreSpecProperty();
					if (col.isExternalIndex()) {
						hasExIndex = true;
						break;
					}
				}

				int maxPage = ((GRdbEntityStoreRuntime) handler.getEntityStoreRuntime()).getCurrentMaxPage();
				if (!hasExIndex && !cond.isLockStrictly() && maxPage == 0) {
					//A.更新対象のEntity定義に外部INDEXがない、かつmaxPage=0の場合
					//  更新条件文をダイレクトに適用して、OBJ_STOREを直接更新
					return getStatement().executeUpdate(updateSql.updateAllOnlyMainByCondition(tenantId, handler, cond, clientId, rdb, context));
				} else {
					//B.それ以外
					//TemporaryTableを作成してそれを結合して、それぞれのテーブル更新
					try {
						//前の処理のデータが残っている可能性がある(同一トランザクション内で複数回のdeleteAll/updateAll呼び出し、エラー発生時など)ため
						getStatement().addBatch(rdb.deleteTemporaryTable(ObjStoreTable.TABLE_NAME_TMP));

						if (!rdb.isSupportGlobalTemporaryTable()) {
							String createTempTable = rdb.createLocalTemporaryTable(ObjStoreTable.TABLE_NAME_TMP, ObjStoreTable.TABLE_NAME, new String[]{ObjStoreTable.OBJ_ID, ObjStoreTable.OBJ_VER});
							getStatement().addBatch(createTempTable);
						}

						//　1.OBJ_DATAの更新対象のレコードのoidをWORKテーブルにINSERT...SELECT
						//　　WORKテーブルは一時表として作成
						getStatement().addBatch(insertSql.copyToTemporaryTable(handler, cond.getWhere(), rdb, context));

						int[] count = getStatement().executeBatch();
						if (count[count.length - 1] == 0) {
							//更新対象なし
							return 0;
						}

						if (cond.isLockStrictly()) {
							String sql = lockSql.lockByTempTable(tenantId, handler, context, rdb);
							//検索SQL実行
							try(ResultSet rs = getStatement().executeQuery(sql)) {
//FIXME nextの呼び出しが必要か確認！！！
//								rs.next();
							}
						}

						//　2.OBJ_STORE（メイン）更新、テンポラリテーブルと結合して
						int updatePageCount = 0;
						MultiInsertContext ctx = rdb.createMultiInsertContext(getStatement());

						ctx.addUpdateSql(updateSql.updateAllMainByTempTable(tenantId, handler, cond.getValues(), clientId, rdb, context));

						//　3.OBJ_STORE（サブ）更新、テンポラリテーブルと結合して
						for (int i = 1; i <= maxPage; i++) {
							String us = updateSql.updateAllSubByTempTable(tenantId, handler, cond.getValues(), clientId, i, rdb, context);
							if (us != null) {
								ctx.addUpdateSql(us);
								updatePageCount++;
							}
						}

						//　4.IDEX更新（delete/insert）、テンポラリテーブルと結合して
						updateIndexAll(tenantId, handler, ctx, cond.getValues());

						int[] updateCounts = ctx.execute();

						// 5.件数チェック：workテーブルインサート件数と、mainの削除件数。違うってことはdef_verが違ったってこと
						for (int i = 0; i <= updatePageCount; i++) {
							if (count[count.length - 1] != updateCounts[i]) {
								throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
							}
						}

						return count[count.length - 1];

					} finally {
						//　5.削除WORKテーブルからINSERTしたレコード削除
						if (!rdb.isSupportAutoClearTemporaryTableWhenCommit()) {
							//temporary tableの中身をクリア
							try {
								getStatement().executeUpdate(rdb.deleteTemporaryTable(ObjStoreTable.TABLE_NAME_TMP));
							} catch (Exception e) {
								LoggerFactory.getLogger(GRdbEntityStoreStrategy.class).warn("temporary table:" + ObjStoreTable.TABLE_NAME_TMP + " drop fail. may be resource leak...");
							}
						}
					}
				}
			}
		};

		return exec.execute(rdb, true).intValue();

	}

	private void updateIndexAll(int tenantId, EntityHandler dataModelHandler, MultiInsertContext ctx, List<UpdateValue> updatePropList) throws SQLException {
		//TODO delete->insert。これでよいか。。。
		for (UpdateValue upadteValue: updatePropList) {
			String propName = upadteValue.getEntityField();
			PropertyHandler storeP = dataModelHandler.getDeclaredProperty(propName);

			if (storeP != null && storeP.getStoreSpecProperty() instanceof GRdbPropertyStoreRuntime) {
				GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) storeP.getStoreSpecProperty();
				if (col.isExternalIndex()) {
					IndexType indexType = storeP.getMetaData().getIndexType();
					if (indexType == IndexType.NON_UNIQUE
							|| indexType == IndexType.UNIQUE
							|| indexType == IndexType.UNIQUE_WITHOUT_NULL) {

						//multiは未対応

						ctx.addUpdateSql(indexDelSql.deleteByTempTable(tenantId, dataModelHandler, col.getSingleColumnRdbTypeAdapter().getColOfIndex(), indexType, rdb));
						//マルチテーブルインサートはINSERT～SLECT文では使用できないので、addUpdateを使用。。。
						ctx.addUpdateSql(indexInsertSql.insertByTempTable(tenantId, (GRdbPropertyStoreHandler) col, rdb));
					}
				}
			}
		}
	}

	@Override
	public Long copyToRecycleBin(final EntityContext context, final EntityHandler handler,
			final String oid, final String userId) {
		//rbid採番
		final Long rbid = newRbId(context, handler);

		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {
				int tenantId = context.getTenantId(handler);

				//OBJ_STORE copySql
				StorageSpaceMap ssMap = dataStore.getStorageSpaceMapOrDefault((MetaSchemalessRdbStoreMapping) handler.getMetaData().getStoreMapping());
				String copySql = recycleBinSql.copyDataToRB(tenantId, handler, rbid, oid, userId, rdb, ssMap);
				getStatement().executeUpdate(copySql);

				//OBJ_REF copySql
				String refCopySql = recycleBinSql.copyRefToRB(tenantId, rbid, handler, oid, rdb);
				getStatement().executeUpdate(refCopySql);

				return null;
			}
		};
		exec.execute(rdb, true);
		return rbid;
	}

	private Long newRbId(final EntityContext context, final EntityHandler handler) {
		return counterService.increment(context.getTenantId(handler), COUNTER_SERVICE_INC_KEY_RB, 1);
	}

	@Override
	public void copyFromRecycleBin(final EntityContext context,
			final EntityHandler handler, final Long rbid, final String userId) {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {
				int tenantId = context.getTenantId(handler);

				//lock and oid load
				String sql = recycleBinSql.lockData(tenantId, handler, rbid, null, rdb);

				String oid = null;
				try (ResultSet rs = getStatement().executeQuery(sql)) {
					if (rs.next()) {
						oid = rs.getString(ObjStoreTable.OBJ_ID);
					} else {
						throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
					}
				}

				MultiInsertContext ctx = rdb.createMultiInsertContext(getStatement());

				//OBJ_DATA restoreSql
				StorageSpaceMap ssMap = dataStore.getStorageSpaceMapOrDefault((MetaSchemalessRdbStoreMapping) handler.getMetaData().getStoreMapping());
				String copySql = recycleBinSql.copyDataFromRB(tenantId, handler, rbid, rdb, context, ssMap);
				ctx.addUpdateSql(copySql);

				//OBJ_REF restoreSql
				String refCopySql = recycleBinSql.copyRefFromRB(tenantId, rbid, handler, rdb);
				ctx.addUpdateSql(refCopySql);

				//index rebuild
				insertIndexForRestore(tenantId, handler, oid, ctx);

				ctx.execute();
				return null;
			}
		};

		exec.execute(rdb, true);

	}

	private void insertIndexForRestore(int tenantId, EntityHandler dataModelHandler, String oid, MultiInsertContext ctx) throws SQLException {
		List<PropertyHandler> props = dataModelHandler.getDeclaredPropertyList();
		for (PropertyHandler storeP: props) {

			if (storeP != null && storeP.getStoreSpecProperty() instanceof GRdbPropertyStoreRuntime) {
				GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) storeP.getStoreSpecProperty();
				if (col.isExternalIndex()) {
					IndexType indexType = storeP.getMetaData().getIndexType();
					if (indexType == IndexType.NON_UNIQUE
							|| indexType == IndexType.UNIQUE
							|| indexType == IndexType.UNIQUE_WITHOUT_NULL) {

						//マルチテーブルインサートはINSERT～SLECT文では使用できないので、addUpdateを使用。。。
						ctx.addUpdateSql(indexInsertSql.insertByOid(tenantId, (GRdbPropertyStoreHandler) col, oid, rdb));
					}
				}
			}
		}
	}

	@Override
	public void deleteFromRecycleBin(final EntityContext context,
			final EntityHandler handler, final Long rbid, final String userId) {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {

			@Override
			public Void logic() throws SQLException {
				int tenantId = context.getTenantId(handler);

				//lock
				String sql = recycleBinSql.lockData(
						tenantId, handler, rbid, null, rdb);
				try (ResultSet rs = getStatement().executeQuery(sql)) {
					if (!rs.next()) {
						throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
					}
				}

				String delSql = recycleBinSql.deleteDataRB(tenantId, handler, rbid, null, rdb);
				getStatement().addBatch(delSql);

				String refDelSql = recycleBinSql.deleteRefRB(tenantId, handler, rbid, null, rdb);
				getStatement().addBatch(refDelSql);

				getStatement().executeBatch();
				return null;
			}
		};

		exec.execute(rdb, true);
	}

	@Override
	public RecycleBinIterator getRecycleBin(final EntityContext context,
			final EntityHandler handler, final Long rbid) {
		SqlExecuter<RecycleBinIterator> exec = new SqlExecuter<RecycleBinIterator>() {

			@Override
			public RecycleBinIterator logic() throws SQLException {
				String sql = recycleBinSql.searchRB(context.getTenantId(handler), handler, rbid, rdb);
				ResultSet rs = getStatement().executeQuery(sql);
				return new GRdbRecycleBinIterator(rs, rdb);
			}
		};

		return exec.execute(rdb, false);
	}

	@Override
	public int countRecycleBin(EntityContext context, EntityHandler handler, Timestamp ts) {
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				String sql = recycleBinSql.searchRB(context.getTenantId(handler), handler, null, ts, rdb);
				sql = recycleBinSql.count(sql);
				ResultSet rs = getStatement().executeQuery(sql);
				try {
					rs.next();
					return rs.getInt(1);
				} finally {
					rs.close();
				}
			}
		};

		return exec.execute(rdb, false);
	}

	@Override
	public String newOid(final EntityContext context, final EntityHandler handler) {
		return Long.toString(counterService.increment(context.getTenantId(handler), COUNTER_SERVICE_INC_KEY, 1));
	}

	@Override
	public void clean(final EntityContext context, final EntityHandler handler) {
		log.info("metaEntity restored. id = " + handler.getMetaData().getId() + ". execute cleanup entity data.");

		int tenantId = context.getLocalTenantId();
		String tableNamePostfix = ((MetaGRdbEntityStore) handler.getMetaData().getEntityStoreDefinition()).getTableNamePostfix();
		purgeDividedTable(tenantId, handler.getMetaData().getId(), tableNamePostfix, rdb);
	}

	//FIXME cleanと統合
	@Override
	public void purgeById(final EntityContext context, final String defId) {
		final int tenantId = context.getLocalTenantId();

		log.debug("start purge meta entity data. tenant id = " + tenantId + ",defId = " + defId + ".");

		for (StorageSpaceMap storage: dataStore.getStorageSpaceMap().values()) {
			for (String tableNamePostfix: storage.allTableNamePostfix()) {
				purgeDividedTable(tenantId, defId, tableNamePostfix, rdb);
			}
		}

		log.debug("end purge meta entity data. tenant id = " + tenantId + ",defId = " + defId + ".");
	}

	private void purgeDividedTable(int tenantId, String defId, String tableNamePostfix, RdbAdapter rdb) {
		//OBJ_STORE
		doBatch(tenantId, defId, delSql.deleteAllDataByDefId(tenantId, defId, tableNamePostfix, rdb));

		//Index
		Set<String> extIndexTable = new HashSet<>();
		for (BaseRdbTypeAdapter adaptor : BaseRdbTypeAdapter.values()) {
			if (adaptor instanceof Null) {
				continue;
			}
			extIndexTable.add(adaptor.getColOfIndex());
		}
		for (String n: extIndexTable) {
			//Index用テーブルは、MTP、USERのStorageSpaceに対してデフォルトで作成していないのでExceptionが発生しても処理を続ける
			try {
				doBatch(tenantId, defId, indexDelSql.deleteAll(tenantId, defId, tableNamePostfix, n, IndexType.UNIQUE, rdb));
			} catch (EntityRuntimeException e) {
				log.warn("Unable to delete unique index data, but continue purge processing. indexType = " + n + ", tableNamePostfix = " + tableNamePostfix);
			}
			try {
				doBatch(tenantId, defId, indexDelSql.deleteAll(tenantId, defId, tableNamePostfix, n, IndexType.NON_UNIQUE, rdb));
			} catch (EntityRuntimeException e) {
				log.warn("Unable to delete none unique index data, but continue purge processing. indexType = " + n + ", tableNamePostfix = " + tableNamePostfix);
			}
		}

		//OBJ_REF
		doBatch(tenantId, defId, refDelSql.deleteAll(tenantId, defId, tableNamePostfix, rdb));
		doBatch(tenantId, defId, refDelSql.deleteAllByTargetDefId(tenantId, defId, tableNamePostfix, rdb));

		//RB
		//全件削除
		doBatch(tenantId, defId, recycleBinSql.deleteDataRB(tenantId, defId, tableNamePostfix, rdb));
		doBatch(tenantId, defId, recycleBinSql.deleteRefRB(tenantId, defId, tableNamePostfix, rdb));
		doBatch(tenantId, defId, recycleBinSql.deleteRefRBByTargetDefId(tenantId, defId, tableNamePostfix, rdb));
	}

	private int doBatch(int tenantId, String defId, final String sql) {
		//大量件数を考慮し、バッチ単位でトランザクションを分ける
		int count = Transaction.requiresNew(t -> {
			SqlExecuter<Integer> purgeExec = new SqlExecuter<Integer>() {
				@Override
				public Integer logic() throws SQLException {
					return getStatement().executeUpdate(sql);
				}
			};
			return purgeExec.execute(rdb, true);
		});

		log.debug("execute sql. tenant id = " + tenantId + ", defId = " + defId + ", sql = " + sql + ", count = " + count + ".");

		return count;
	}

	@Override
	public void defragData(EntityContext context, EntityHandler handler) {

		Transaction.requiresNew(t -> {
			SqlExecuter<Void> exec = new SqlExecuter<Void>() {
				@Override
				public Void logic() throws SQLException {
					//reference
					List<String> refPropertyIds = new ArrayList<>();
					for (PropertyHandler property : handler.getDeclaredPropertyList()) {
						if (property instanceof ReferencePropertyHandler
								&& ((MetaReferenceProperty) property.getMetaData()).getMappedByPropertyMetaDataId() == null) {
							refPropertyIds.add(property.getId());
						}
					}
					getStatement().executeUpdate(refDelSql.deleteForDefrag(context.getLocalTenantId(), handler, refPropertyIds, rdb));
					getStatement().executeUpdate(recycleBinSql.deleteForDefragRB(context.getLocalTenantId(), handler, refPropertyIds, rdb));

					//external index
					Map<String, List<String>> extIndexTable = new HashMap<>();
					Map<String, List<String>> extUniqueIndexTable = new HashMap<>();
					for (BaseRdbTypeAdapter adaptor : BaseRdbTypeAdapter.values()) {
						if (adaptor instanceof Null) {
							continue;
						}
						if (!extIndexTable.containsKey(adaptor.getColOfIndex())) {
							extIndexTable.put(adaptor.getColOfIndex(), new ArrayList<>());
						}
						if (!extUniqueIndexTable.containsKey(adaptor.getColOfIndex())) {
							extUniqueIndexTable.put(adaptor.getColOfIndex(), new ArrayList<>());
						}
					}
					for (PrimitivePropertyHandler property: handler.getIndexedPropertyList(context)) {
						GRdbPropertyStoreRuntime col = (GRdbPropertyStoreRuntime) property.getStoreSpecProperty();
						if (col.isExternalIndex()) {
							List<String> list;
							if (property.getMetaData().getIndexType() == IndexType.NON_UNIQUE) {
								list = extIndexTable.get(col.getSingleColumnRdbTypeAdapter().getColOfIndex());
							} else {
								list = extUniqueIndexTable.get(col.getSingleColumnRdbTypeAdapter().getColOfIndex());
							}
							col.asList().get(0).getExternalIndexColName();
							list.add(col.asList().get(0).getExternalIndexColName());
						}
					}

					//Tableの存在確認用
					Set<String> existTables = new HashSet<>();
					try (ResultSet rs = rdb.getTableNames(ObjIndexTable.TABLE_INDEX_PREFIX_NAME + "%", getConnection())) {
						while (rs.next()) {
							existTables.add(rs.getString("TABLE_NAME").toUpperCase());
						}
					}
					try (ResultSet rs = rdb.getTableNames(ObjIndexTable.TABLE_UNIQUE_PREFIX_NAME + "%", getConnection())) {
						while (rs.next()) {
							existTables.add(rs.getString("TABLE_NAME").toUpperCase());
						}
					}

					if (treatNonUniqueExternalIndexTable) {
						for (Map.Entry<String, List<String>> e: extIndexTable.entrySet()) {
							String tableName = ((GRdbEntityStoreRuntime) handler.getEntityStoreRuntime()).OBJ_INDEX(e.getKey());
							if (existTables.contains(tableName)) {
								getStatement().executeUpdate(indexDelSql.deleteForDefrag(context.getLocalTenantId(), handler, e.getKey(), IndexType.NON_UNIQUE, e.getValue(), rdb));
							}
						}
					}
					for (Map.Entry<String, List<String>> e: extUniqueIndexTable.entrySet()) {
						String tableName = ((GRdbEntityStoreRuntime) handler.getEntityStoreRuntime()).OBJ_UNIQUE(e.getKey());
						if (existTables.contains(tableName)) {
							getStatement().executeUpdate(indexDelSql.deleteForDefrag(context.getLocalTenantId(), handler, e.getKey(), IndexType.UNIQUE, e.getValue(), rdb));
						}
					}

					return null;
				}
			};
			exec.execute(rdb, true);
		});
	}

	@Override
	public void bulkUpdate(BulkUpdatable bulkUpdatable,
			EntityContext entityContext, EntityHandler entityHandler,
			String clientId) {
		buStrategy.bulkUpdate(bulkUpdatable, entityContext, entityHandler, clientId);
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
