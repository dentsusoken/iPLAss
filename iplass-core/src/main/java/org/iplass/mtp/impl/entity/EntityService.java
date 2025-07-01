/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.entity;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.TypedDefinitionManager;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.VersionControlType;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.datastore.DataStore;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.strategy.ApplyMetaDataStrategy;
import org.iplass.mtp.impl.datastore.strategy.EntityStoreStrategy;
import org.iplass.mtp.impl.definition.AbstractTypedMetaDataService;
import org.iplass.mtp.impl.definition.DefinitionMetaDataTypeMap;
import org.iplass.mtp.impl.definition.DefinitionNameChecker;
import org.iplass.mtp.impl.entity.property.MetaProperty;
import org.iplass.mtp.impl.entity.versioning.NonVersionController;
import org.iplass.mtp.impl.entity.versioning.NumberbaseVersionController;
import org.iplass.mtp.impl.entity.versioning.SimpleTimebaseVersionController;
import org.iplass.mtp.impl.entity.versioning.StatebaseVersionController;
import org.iplass.mtp.impl.entity.versioning.TimebaseVersionController;
import org.iplass.mtp.impl.entity.versioning.VersionController;
import org.iplass.mtp.impl.metadata.MetaDataConfig;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.metadata.MetaDataRepository;
import org.iplass.mtp.impl.metadata.MetaDataRuntimeException;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EntityService extends AbstractTypedMetaDataService<MetaEntity, EntityHandler> implements Service {

	public static final String ENTITY_META_PATH = "/entity/";
	public static final String ENTITY_NAME = "Entity";

	private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
	private static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal");

	public static class TypeMap extends DefinitionMetaDataTypeMap<EntityDefinition, MetaEntity> {
		public TypeMap() {
			super(getFixedPath(), MetaEntity.class, EntityDefinition.class);
		}
		@Override
		public TypedDefinitionManager<EntityDefinition> typedDefinitionManager() {
			return ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
		}
		@Override
		public String toPath(String defName) {
			return pathPrefix + defName.replace('.', '/');
		}
		@Override
		public String toDefName(String path) {
			return path.substring(pathPrefix.length()).replace("/", ".");
		}

		@Override
		protected DefinitionNameChecker createDefinitionNameChecker() {
			return new EntityDefinitionNameChecker();
		}
	}

	private AsyncTaskService asyncTaskService;
	private StoreService storeService;//TODO DataModel単位で異なるStoreServiceを使用することを可能とする

	private List<AdditionalStoreMaintainer> additionalStoreMaintainer;

	private EntityInterceptor[] interceptors;

	private int limitOfReferences = 1000;
	private int purgeTargetDate;
	private Pattern oidValidationPattern;

	private final EnumMap<VersionControlType, VersionController> versionControllers;
	private ExtendPropertyAdapterFactory extendPropertyAdapterFactory;

	public EntityService () {
		versionControllers = new EnumMap<VersionControlType, VersionController>(VersionControlType.class);
		versionControllers.put(VersionControlType.NONE, new NonVersionController());
		versionControllers.put(VersionControlType.VERSIONED, new NumberbaseVersionController());
		versionControllers.put(VersionControlType.TIMEBASE, new TimebaseVersionController());
		versionControllers.put(VersionControlType.SIMPLE_TIMEBASE, new SimpleTimebaseVersionController());
		versionControllers.put(VersionControlType.STATEBASE, new StatebaseVersionController());
	}

	public static String getFixedPath() {
		return ENTITY_META_PATH;
	}

	public int getLimitOfReferences() {
		return limitOfReferences;
	}

	public int getPurgeTargetDate() {
		return purgeTargetDate;
	}

	public ExtendPropertyAdapterFactory getExtendPropertyAdapterFactory() {
		return extendPropertyAdapterFactory;
	}

	public VersionController getVersionController(EntityHandler eh) {
		if (eh.getMetaData().getVersionControlType() == null) {
			return versionControllers.get(VersionControlType.NONE);
		}
		return versionControllers.get(eh.getMetaData().getVersionControlType());
	}


	public Future<String> createDataModelSchema(final EntityDefinition definition) {
		EntityContext context = EntityContext.getCurrentContext();

		MetaEntity metaData = new MetaEntity();
		metaData.applyConfig(definition, context, new KeyGenerator());

		return createDataModelSchema(metaData, null);
	}

	public Future<String> createDataModelSchema(final MetaEntity newMeta, final MetaDataConfig config) {

		//別スレッドで実行（長時間かかる処理の可能性）
		Callable<String> task = new Callable<String>() {
			public String call() throws Exception {

				//非同期なので、Transactionは新規で作成する
				return Transaction.requiresNew(t -> {
					EntityContext context = EntityContext.getCurrentContext();
					return doCreate(newMeta, config, context, true);
				});
			}
		};

		return asyncTaskService.execute(task);

	}

	public String createDataModelSchema(final MetaEntity newMeta, final MetaDataConfig config, final boolean doAutoReload) {
		EntityContext eContext = EntityContext.getCurrentContext();

		return doCreate(newMeta, config, eContext, doAutoReload);
	}

	private String doCreate(final MetaEntity newMeta, final MetaDataConfig config, final EntityContext context, boolean doAutoReload) {
		// メタデータ定義名チェック
		// Entityはメタデータ登録時にcreateMetaDataメソッドが呼ばれないので個別にチェックする
		this.checkDefinitionName(newMeta);

		//TODO 同名のメタデータ存在チェック、設定値の妥当性の検証など

		DataStore ds = storeService.getDataStore();
		ApplyMetaDataStrategy configDataModel = ds.getApplyMetaDataStrategy();

		//直接メタデータを指定された場合にnullの可能性があるのでチェック
		if (newMeta.getId() == null) {
			newMeta.setId((new KeyGenerator()).generateId());
		}

		String path = convertPath(ENTITY_META_PATH + newMeta.getName());
		
		int curerntMaxVersion = -1;
		List<MetaDataEntryInfo> infoList = ServiceRegistry.getRegistry().getService(MetaDataRepository.class)
				.getHistoryById(ExecuteContext.getCurrentContext().getClientTenantId(), newMeta.getId());
		if (infoList != null) {
			for (MetaDataEntryInfo mi: infoList) {
				if (mi.getState() == State.VALID) {
					//有効なデータがある場合はエラー
					throw new MetaDataRuntimeException("Registered metadata already exists. id=" + newMeta.getId() + ", path=" + path);
				}
				if (curerntMaxVersion < mi.getVersion()) {
					curerntMaxVersion = mi.getVersion();
				}
			}
		}

		MetaEntity prevMeta = null;
		if (curerntMaxVersion >= 0) {
			//既に登録済の同一MetaDataが存在するので、登録済のMetaDataを取得
			MetaDataEntry curEntry = MetaDataContext.getContext().getMetaDataEntryById(newMeta.getId(), curerntMaxVersion);
			if (curEntry != null && curEntry.getMetaData() instanceof MetaEntity) {
				prevMeta = (MetaEntity) curEntry.getMetaData();
			}
		}

		if (prevMeta == null) {
			configDataModel.create(newMeta, context/*, curerntMaxVersion + 1*/);
			//MetaData登録
			MetaDataContext.getContext().store(path, newMeta, /*curerntMaxVersion + 1,*/ config, doAutoReload);
			logger.info("created " + newMeta.getName() + "(" + newMeta.getId() + ")");
		} else {
			//復活処理
			boolean prepare = configDataModel.prepare(newMeta, prevMeta, context);

			if (!prepare) {
				throw new EntityRuntimeException("can not prepare for re-create Entity:" + newMeta.getName() + "(tenant=" + ExecuteContext.getCurrentContext().getClientTenantId() + ")");
			}

			boolean res = false;

			try {
				res = updateData(configDataModel, ExecuteContext.getCurrentContext().getClientTenantId(), newMeta, prevMeta, context, config, curerntMaxVersion);
				if (res) {
					//MetaData登録
					MetaDataContext.getContext().store(path, newMeta, /*curerntMaxVersion + 1,*/ config, doAutoReload);
					logger.info("re-created " + newMeta.getName() + "(" + newMeta.getId() + ")");
				} else {
					throw new EntityRuntimeException("can not re-create Entity Definition:" + newMeta.getName() + "(tenant=" + ExecuteContext.getCurrentContext().getClientTenantId() + ")");
				}

			} finally {
				try {
					configDataModel.finish(res, newMeta, prevMeta, context);
				} catch (RuntimeException e) {
					fatalLogger.error("Entity metadata update process could not be completed correctly.:" + e.toString(), e);
				}
			}

		}

		return newMeta.getId();
	}


	private boolean doPrepare(final ApplyMetaDataStrategy st, int tenantId, final MetaEntity newOne, final MetaEntity previous, final EntityContext context) {

		return Transaction.requiresNew(t -> {
			boolean prepare = st.prepare(newOne, previous, context);
			return prepare;
		});
	}

	private boolean updateData(final ApplyMetaDataStrategy st, final int tenantId, final MetaEntity newOne, final MetaEntity previous, final EntityContext context, final MetaDataConfig config, final Integer previousVersion) {
		//SharedTenantのデータの場合は、複数のテナントを一括で更新する必要あり
		int[] targetTenantIds = null;
		TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
		MetaDataEntry ent;
		if (previousVersion == null) {
			ent = MetaDataContext.getContext().getMetaDataEntryById(previous.getId());
		} else {
			ent = MetaDataContext.getContext().getMetaDataEntryById(previous.getId(), previousVersion);
		}
		if (tcService.getSharedTenantId() == tenantId
				&& ent.isSharable()
				&& !ent.isDataSharable()) {
			List<Integer> allTenantIds = tcService.getAllTenantIdList();
			List<Integer> overwritedTenantIds = MetaDataContext.getContext().getOverwriteTenantIdList(previous.getId());
			for (Iterator<Integer> it = allTenantIds.iterator(); it.hasNext();) {
				Integer id = it.next();
				if (overwritedTenantIds.contains(id)) {
					logger.info(id + "'s MetaData:" + ent.getPath() + " (" + ent.getMetaData().getId() + ") is overwrote. so skip data patch.");
					it.remove();
				}
			}
			targetTenantIds = new int[allTenantIds.size()];
			for (int i = 0; i < allTenantIds.size(); i++) {
				targetTenantIds[i] = allTenantIds.get(i);
			}
		} else {
			targetTenantIds = new int[]{tenantId};
		}

		return st.modify(newOne, previous, context, targetTenantIds);
	}

	private boolean doModify(final ApplyMetaDataStrategy st, final int tenantId, final MetaEntity newOne, final MetaEntity previous, final EntityContext context, final MetaDataConfig config, final Integer previousVersion) {
		return Transaction.requiresNew(transaction -> {

				boolean res = updateData(st, tenantId, newOne, previous, context, config, previousVersion);

				if (!res) {
					transaction.setRollbackOnly();
					return false;
				}

//					//AutoNumberカラムの場合、対応するカウンターを作成
//					//FIXME でも、local->sharedのタイミングでも作らなければ。。。もしくはInsert時になかったら作るか。。。同時に来た場合は2つめがエラーとなる可能性が高い。別Tranで作って、リトライか。。
//
//					//TODO 作成したAutoNumberカウンターの削除は現状、未対応
//					for (MetaProperty p: newOne.getDeclaredPropertyList()) {
//						if (p instanceof MetaPrimitiveProperty) {
//							PropertyType type = ((MetaPrimitiveProperty) p).getType();
//							if (type instanceof AutoNumberType) {
//								boolean needCounterReset = false;
//								MetaProperty previousProperty = previous.getDeclaredProperty(p.getName());
//								if (!(previousProperty instanceof MetaPrimitiveProperty)) {
//									needCounterReset = true;
//								} else {
//									PropertyType prevType = ((MetaPrimitiveProperty) previousProperty).getType();
//									if (prevType instanceof AutoNumberType) {
//										AutoNumberType autoNum = (AutoNumberType) type;
//										AutoNumberType prevAutoNum = (AutoNumberType) prevType;
//										if (autoNum.getStartsWith() != prevAutoNum.getStartsWith()) {
//											needCounterReset = true;
//										}
//									} else {
//										needCounterReset = true;
//									}
//								}
//								if (needCounterReset) {
//									AutoNumberType autoNum = (AutoNumberType) type;
//									CounterService counter = ServiceRegistry.getRegistry().getService(AutoNumberType.AUTO_NUMBER_TYPE_COUNTER_NAME);
//									String incUnitKey = AutoNumberType.createIncrementUnitKey(newOne.getId(), p.getId());
//									for (int i = 0; i < targetTenantIds.length; i++) {
//										counter.resetCounter(targetTenantIds[i], incUnitKey, autoNum.getStartsWith() - 1);
//									}
//								}
//							}
//						}
//					}

//					newOne.createRuntime(new MetaDataConfig(ent.isSharable(), ent.isOverwritable(), ent.isDataSharable()));
				TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);
				tc.getMetaDataContext().update(convertPath(ENTITY_META_PATH + newOne.getName()), newOne);

				if (config != null) {
					tc.getMetaDataContext().updateConfig(convertPath(ENTITY_META_PATH + newOne.getName()), config);
				}

				return true;
		});
	}

	private boolean doRemove(final ApplyMetaDataStrategy st, final int tenantId, final MetaEntity previous, final EntityContext context) {
		return Transaction.requiresNew(t -> {
			TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);
			tc.getMetaDataContext().remove(convertPath(ENTITY_META_PATH + previous.getName()));//, previous);
			return true;
		});
	}

	private boolean doDefragMeta(final ApplyMetaDataStrategy st, final int tenantId, final MetaEntity target, final EntityContext context) {
		return Transaction.requiresNew(transaction -> {

				MetaEntity before = target.copy();

				//SharedTenantのデータの場合は、複数のテナントを一括で更新する必要あり
				int[] targetTenantIds = null;
				TenantContextService tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
				MetaDataEntry ent = MetaDataContext.getContext().getMetaDataEntryById(target.getId());
				//カスタムせずに、SharedTenantと同一のメタデータを利用している場合
				if (tcService.getSharedTenantId() == tenantId
						&& ent.isSharable()
						&& !ent.isDataSharable()) {
					List<Integer> allTenantIds = tcService.getAllTenantIdList();
					List<Integer> overwritedTenantIds = MetaDataContext.getContext().getOverwriteTenantIdList(target.getId());
					for (Iterator<Integer> it = allTenantIds.iterator(); it.hasNext();) {
						Integer id = it.next();
						if (overwritedTenantIds.contains(id)) {
							logger.info(id + "'s MetaData:" + ent.getPath() + " (" + ent.getMetaData().getId() + ") is overwrote. so skip defrag data.");
							it.remove();
						}
					}
					targetTenantIds = new int[allTenantIds.size()];
					for (int i = 0; i < allTenantIds.size(); i++) {
						targetTenantIds[i] = allTenantIds.get(i);
					}
				} else {
					targetTenantIds = new int[]{tenantId};
				}

				boolean res = st.defrag(target, context, targetTenantIds);

				if (!res) {
					transaction.setRollbackOnly();
					return false;
				}

				//TODO 作成したAutoNumberカウンターの削除は現状、未対応

				if (!before.equals(target)) {
					TenantContext tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);
					tc.getMetaDataContext().update(convertPath(ENTITY_META_PATH + target.getName()), target);
				}

				return true;
		});
	}


	private void doFinish(final boolean modifyResult, final ApplyMetaDataStrategy st, final int tenantId, final MetaEntity newOne, final MetaEntity previous, final EntityContext context) {
		try {
			Transaction.requiresNew(t -> {
				st.finish(modifyResult, newOne, previous, context);
			});
		} catch (RuntimeException e) {
			fatalLogger.error("Entity metadata update process could not be completed correctly. cause:" + e.toString(), e);
		}
	}

	public Future<String> updateDataModelSchema(final EntityDefinition definition) {
		return updateDataModelSchema(definition, null);
	}

	public Future<String> updateDataModelSchema(final EntityDefinition definition, Map<String, String> renamePropertyMap) {

		EntityContext context = EntityContext.getCurrentContext();
		EntityHandler currentHandler = context.getHandlerByName(definition.getName());

		if (currentHandler == null) {
			throw new MetaDataRuntimeException(definition.getName() + " not found.");
		}

		MetaEntity newMeta = currentHandler.getMetaData().copy();

		//プロパティ名の変更を反映
		if (renamePropertyMap != null) {
			for (Entry<String, String> entry : renamePropertyMap.entrySet()) {
				MetaProperty p = newMeta.getDeclaredProperty(entry.getKey());
				if (p == null) {
					throw new MetaDataRuntimeException(definition.getName() + "'s " + entry.getKey() + " not found.");
				}
				p.setName(entry.getValue());
			}
		}

		newMeta.applyConfig(definition, context, new KeyGenerator());

		return updateDataModelSchema(newMeta, null);
	}

	public Future<String> updateDataModelSchema(final MetaEntity newMeta, final MetaDataConfig config) {
		// メタデータ定義名チェック
		// Entityはメタデータ更新時にupdateMetaDataメソッドが呼ばれないので個別にチェックする
		// 更新時はEntityRuntimeExceptionスローしないとAdminConsole側でエラーハンドリングができない
		try {
			this.checkDefinitionName(newMeta);
		} catch (MetaDataRuntimeException e) {
			throw new EntityRuntimeException(e.getMessage(), e);
		}

		//非同期でスキーマ変更する。
		//TODO 同名のメタデータ存在チェック、設定値の妥当性の検証など

		//TODO キャッシュ管理

		//TODO 例外時のログ出力など
//		final ExecuteContext currentContext = ExecuteContext.getCurrentContext();

		Callable<String> task = new Callable<String>() {
			public String call() throws Exception {
//				try {
//					ExecuteContext.setContext(currentContext);

					ExecuteContext currentContext = ExecuteContext.getCurrentContext();

					//TODO EntityContextはトランザクションに紐付くものなのに、下記処理ではトランザクションをまたいで同一のEntityContextを使用してしまっている
					EntityContext context = EntityContext.getCurrentContext();
					//idから既存を取得(パスの変更がある場合を考慮)
					EntityHandler currentHandler = context.getHandlerById(newMeta.getId());

					DataStore currentDs = storeService.getDataStore();

					ApplyMetaDataStrategy reConfigDataModel = currentDs.getApplyMetaDataStrategy();

					//DataStoreの実装が異なる場合(別環境、別DataStore実装からImport)は、StoreMappingは、既存からコピーする
					if (newMeta.getEntityStoreDefinition() != null &&
							!newMeta.getEntityStoreDefinition().getClass().equals(currentDs.getEntityStoreType())) {
						if (currentHandler.getMetaData().getStoreMapping() != null) {
							newMeta.setStoreMapping((MetaStoreMapping) currentHandler.getMetaData().getStoreMapping().copy());
						}
					}

					logger.info("update " + currentHandler.getMetaData().getName() + "(" + currentHandler.getMetaData().getId() + ") process start");
					boolean prepare = doPrepare(reConfigDataModel, currentContext.getClientTenantId(), newMeta, currentHandler.getMetaData(), context);

					if (!prepare) {
						throw new EntityRuntimeException("can not prepare for update Entity:" + currentHandler.getMetaData().getName() + "(tenant=" + currentContext.getClientTenantId() + ")");
					}

					boolean result = false;
					try {
						result = doModify(reConfigDataModel, currentContext.getClientTenantId(), newMeta, currentHandler.getMetaData(), context, config, null);
						if (result) {
							return newMeta.getId();
						} else {
							throw new EntityRuntimeException("can not modify Entity Definition:" + currentHandler.getMetaData().getName() + "(tenant=" + currentContext.getClientTenantId() + ")");
						}
					} finally {
						doFinish(result, reConfigDataModel, currentContext.getClientTenantId(), newMeta, currentHandler.getMetaData(), context);
						if (result) {
							logger.info("update " + currentHandler.getMetaData().getName() + "(" + currentHandler.getMetaData().getId() + ") process finish");
						} else {
							logger.info("update " + currentHandler.getMetaData().getName() + "(" + currentHandler.getMetaData().getId() + ") process fail");
						}
					}
//				} finally {
//					ExecuteContext.setContext(null);
//				}
			}
		};

		return asyncTaskService.execute(task);
	}



	public Future<String> removeDataModelSchema(final EntityDefinition definition) {
		EntityContext context = EntityContext.getCurrentContext();

		MetaEntity metaData = new MetaEntity();
		metaData.applyConfig(definition, context, new KeyGenerator());

		return removeDataModelSchema(metaData);
	}

	public Future<String> removeDataModelSchema(final MetaEntity curMeta) {
		// FIXME 定義を削除した場合のDataの処理はどうするか？

//		final ExecuteContext currentContext = ExecuteContext.getCurrentContext();
		Callable<String> task = new Callable<String>() {
			public String call() throws Exception {
//				try {
//					ExecuteContext.setContext(currentContext);
					ExecuteContext currentContext = ExecuteContext.getCurrentContext();

					EntityContext context = EntityContext.getCurrentContext();
					EntityHandler currentHandler = context.getHandlerByName(curMeta.getName());

					ApplyMetaDataStrategy reConfigDataModel = currentHandler.getDataStore().getApplyMetaDataStrategy();
					MetaEntity metaEntity = currentHandler.getMetaData();

					logger.info("remove " + currentHandler.getMetaData().getName() + "(" + currentHandler.getMetaData().getId() + ") process start");
					boolean prepare = doPrepare(reConfigDataModel, currentContext.getClientTenantId(), metaEntity, metaEntity, context);
					if (!prepare) {
						throw new EntityRuntimeException("can not prepare for remove Entity:" + currentHandler.getMetaData().getName() + "(tenant=" + currentContext.getClientTenantId() + ")");
					}
					boolean result = false;
					MetaEntity newOne = null;
					try {
						MetaDataEntry ent = MetaDataContext.getContext().getMetaDataEntryById(metaEntity.getId());
						if (ent.getRepositryType() == RepositoryType.SHARED_OVERWRITE) {
							//上書きされているので、SHAREDの初期状態で再上書き
							MetaDataContext shredContext = ServiceRegistry.getRegistry().getService(TenantContextService.class).getSharedTenantContext().getMetaDataContext();
							MetaDataEntry shared = shredContext.getMetaDataEntryById(metaEntity.getId());
							if (shared != null) {
								result = doModify(reConfigDataModel, currentContext.getClientTenantId(), (MetaEntity) shared.getMetaData(), metaEntity, context, null, null);
								newOne = (MetaEntity) shared.getMetaData();
							} else {
								//path一致、id不一致の場合
								//実削除
								result = doRemove(reConfigDataModel, currentContext.getClientTenantId(), metaEntity, context);
								newOne = metaEntity;
							}
						} else {
							//実削除
							result = doRemove(reConfigDataModel, currentContext.getClientTenantId(), metaEntity, context);
							newOne = metaEntity;
						}

						if (!result) {
							throw new EntityRuntimeException("can not delete Entity definition:" + currentHandler.getMetaData().getName() + "(tenant=" + currentContext.getClientTenantId() + ")");
						}

						return metaEntity.getId();
					} finally {
						doFinish(result, reConfigDataModel, currentContext.getClientTenantId(), newOne, metaEntity, context);
						if (result) {
							logger.info("remove " + currentHandler.getMetaData().getName() + "(" + currentHandler.getMetaData().getId() + ") process finish");
						} else {
							logger.info("remove " + currentHandler.getMetaData().getName() + "(" + currentHandler.getMetaData().getId() + ") process fail");
						}
					}
//				} finally {
//					ExecuteContext.setContext(null);
//				}
			}
		};

		return asyncTaskService.execute(task);

	}

	public void renameProperty(String defName, String from, String to) {
		EntityHandler eh = getRuntimeByName(defName);
		if (eh == null) {
			throw new MetaDataRuntimeException(defName + " not found.");
		}

		MetaEntity meta = eh.getMetaData().copy();
		MetaProperty p = meta.getDeclaredProperty(from);
		if (p == null) {
			throw new MetaDataRuntimeException(defName + "'s " + from + " not found.");
		}

		if (meta.getDeclaredProperty(to) != null) {
			throw new MetaDataRuntimeException(defName + "'s " + to + " already declared.");
		}

		p.setName(to);

		MetaDataContext.getContext().update(convertPath(ENTITY_META_PATH + defName), meta);
	}

	@Override
	public List<String> nameList() {
		List<String> pathList = MetaDataContext.getContext().pathList(getFixedPath());
		//pathList.replaceAll(path -> path.substring(getFixedPath().length()).replace("/", "."));
		pathList.replaceAll(new UnaryOperator<String>() {
			@Override
			public String apply(String path) {
				return path.substring(getFixedPath().length()).replace("/", ".");
			}
		});
		//Entityの除外
		//pathList.removeIf(name -> ENTITY_NAME.equals(name));
		pathList.removeIf(new Predicate<String>() {
			@Override
			public boolean test(String name) {
				return ENTITY_NAME.equals(name);
			}
		});
		return pathList;
	}

	@Override
	public List<MetaDataEntryInfo> list() {
		return list("");
	}

	@Override
	public List<MetaDataEntryInfo> list(String path) {
		List<MetaDataEntryInfo> list = MetaDataContext.getContext().definitionList(convertPath(getFixedPath() + path));
		if (list != null) {
			//Entityの除外
			String entityPath = getFixedPath() + ENTITY_NAME;
			//list.removeIf(definition -> entityPath.equals(definition.getPath()));
			list.removeIf(new Predicate<MetaDataEntryInfo>() {
				@Override
				public boolean test(MetaDataEntryInfo definition) {
					return entityPath.equals(definition.getPath());
				}
			});
		}

		return list;
	}

	public EntityHandler getRuntimeByName(String name) {
		return MetaDataContext.getContext().getMetaDataHandler(EntityHandler.class, convertPath(ENTITY_META_PATH + name));
	}

	public EntityHandler getRuntimeById(String id) {
		return MetaDataContext.getContext().getMetaDataHandlerById(EntityHandler.class, id);
	}

	public EntityHandler getHandlerById(String id, int version) {
		return MetaDataContext.getContext().getMetaDataHandlerById(EntityHandler.class, id, version);
	}

	/**
	 * <p>
	 * 指定されたEntityデータを全て物理削除します。
	 * </p>
	 * <p>
	 * 無効化されたEntity定義のデータなどを含め、指定されたIDに紐づくEntityデータを全て物理削除します。
	 * </p>
	 *
	 * @param id Entity定義ID
	 */
	public void purgeById(final String id) {

		Transaction.required(t -> {
			final ExecuteContext context = ExecuteContext.getCurrentContext();
			EntityContext eContext = EntityContext.getCurrentContext();

			DataStore dataStore = storeService.getDataStore();
			EntityStoreStrategy strategy = dataStore.getEntityStoreStrategy();
			
			logger.info("purge by id:" + id + ") process start");
			strategy.purgeById(eContext, id);
			if (additionalStoreMaintainer != null) {
				for (AdditionalStoreMaintainer asm: additionalStoreMaintainer) {
					asm.clean(context.getClientTenantId(), id);
				}
			}
			logger.info("purge by id:" + id + ") process finish");
		});

	}

	/**
	 * <p>
	 * 未使用化されたProperty Column領域のデータを物理削除します。
	 * </p>
	 * <p>
	 * EntityのProperty定義を変更することで未使用化されたデータ領域のデータを物理削除します。
	 * </p>
	 *
	 * @param name Entity定義名
	 */
	public void defragByName(final String name) {

		//FIXME 非同期化

		ExecuteContext context = ExecuteContext.getCurrentContext();
		EntityContext eContext = EntityContext.getCurrentContext();

		EntityHandler eh = getRuntimeByName(name);
		MetaEntity meta = eh.getMetaData();
		DataStore ds = storeService.getDataStore();
		EntityStoreStrategy dataStrategy = ds.getEntityStoreStrategy();
		ApplyMetaDataStrategy metaStrategy = ds.getApplyMetaDataStrategy();

		boolean result = false;
		if (eh.isUseSharedMetaData()) {
			//MetaDataのdefragはスキップ
			result = true;
		} else {
			//MetaDataのdefrag
			logger.info("defrag " + name + "(" + meta.getId() + ") process start");
			boolean prepare = doPrepare(metaStrategy, context.getClientTenantId(), meta, meta, eContext);
			if (!prepare) {
				throw new EntityRuntimeException("can not prepare for defrag Entity:" + meta.getName() + "(tenant=" + context.getClientTenantId() + ")");
			}
			MetaEntity target = meta.copy();
			try {
				result = doDefragMeta(metaStrategy, context.getClientTenantId(), target, eContext);
			} catch (RuntimeException e) {
				throw new EntityRuntimeException("can not defrag Entity:" + meta.getName() + "(tenant=" + context.getClientTenantId() + ")", e);
			} finally {
				doFinish(result, metaStrategy, context.getClientTenantId(), target, meta, eContext);
			}
		}

		if (result) {
			//reload Meta
			eh = getRuntimeByName(name);

			//defrag data
			dataStrategy.defragData(eContext, eh);

			if (additionalStoreMaintainer != null) {
				for (AdditionalStoreMaintainer asm: additionalStoreMaintainer) {
					asm.defrag(context.getClientTenantId(), eh);
				}
			}
			logger.info("defrag " + name + "(" + meta.getId() + ") process finish");
		} else {
			logger.info("defrag " + name + "(" + meta.getId() + ") process fail");
		}

	}

	/**
	 * <p>スキーマがロックされているかを返します。</p>
	 *
	 * @param name Entity定義名
	 * @return true：ロック
	 */
	public boolean isLockedSchema(String name) {
		EntityContext context = EntityContext.getCurrentContext();
		EntityHandler currentHandler = context.getHandlerByName(name);
		if (currentHandler != null) {
			DataStore currentDs = storeService.getDataStore();
			ApplyMetaDataStrategy configDataModel = currentDs.getApplyMetaDataStrategy();

			return configDataModel.isLocked(currentHandler.getMetaData(), context);
		}
		return false;
	}

	public void destroy() {
	}

	public EntityInterceptor[] getInterceptors() {
		return interceptors;
	}

	public void init(Config config) {
		asyncTaskService = config.getDependentService(AsyncTaskService.class);
		storeService = config.getDependentService(StoreService.class);

		//TODO リスナー登録
		//repository.addEventListener(listener);

		List<?> interceptorList = config.getBeans("interceptor");
		if (interceptorList != null) {
			interceptors = interceptorList.toArray(new EntityInterceptor[interceptorList.size()]);
		}

		if (config.getValue("limitOfReferences") != null) {
			limitOfReferences = Integer.parseInt(config.getValue("limitOfReferences"));
		}

		if (config.getValue("purgeTargetDate") != null) {
			purgeTargetDate = Integer.parseInt(config.getValue("purgeTargetDate"));
		}

		additionalStoreMaintainer = config.getValues("additionalStoreMaintainer", AdditionalStoreMaintainer.class);

		extendPropertyAdapterFactory = config.getValue("extendPropertyAdapterFactory", ExtendPropertyAdapterFactory.class);
		if (extendPropertyAdapterFactory == null) {
			extendPropertyAdapterFactory = new ExtendPropertyAdapterFactory();
		}
		
		if (config.getValue("oidValidationPattern") != null) {
			oidValidationPattern = Pattern.compile(config.getValue("oidValidationPattern"));
		}
	}
	
	public Pattern getOidValidationPattern() {
		return oidValidationPattern;
	}
	
	public void checkValidOidPattern(String oid) {
		if (oidValidationPattern != null) {
			if (!oidValidationPattern.matcher(oid).matches()) {
				throw new EntityRuntimeException("OidProperty value must match pattern: " + oidValidationPattern.pattern());
			}
		}
	}

	/**
	 * <p>Pathを有効な値に変換します。</p>
	 *
	 * Entityのnameは「.」区切りなので、nameをそのまま渡された場合などを考慮して変換
	 *
	 * @param path パス
	 * @return 有効なパス
	 */
	private String convertPath(String path) {
		return path.replace(".","/");
	}

	@Override
	public void createMetaData(MetaEntity meta) {
		// TODO 一先ず元の処理を呼び出す
		createDataModelSchema(meta, null);
	}

	@Override
	public void updateMetaData(MetaEntity meta) {
		// TODO 一先ず元の処理を呼び出す
		updateDataModelSchema(meta, null);
	}

	@Override
	public void removeMetaData(String definitionName) {
		// TODO 一先ず元の処理を呼び出す
		EntityHandler eh = getRuntimeByName(definitionName);
		if (eh != null) {
			removeDataModelSchema(eh.getMetaData());
		}
	}

	@Override
	public Class<MetaEntity> getMetaDataType() {
		return MetaEntity.class;
	}

	@Override
	public Class<EntityHandler> getRuntimeType() {
		return EntityHandler.class;
	}
}
