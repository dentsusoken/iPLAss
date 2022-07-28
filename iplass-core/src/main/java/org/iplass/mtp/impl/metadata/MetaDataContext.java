/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.iplass.mtp.impl.cache.CacheController;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.LoadingAdapter;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntry.State;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MetaDataContext {
	private static final Logger logger = LoggerFactory.getLogger(MetaDataContext.class);

	//TODO EntityのメタデータキャッシュはL1Cacheが良くヒットするが、画面系のメタデータはL1Cacheがほとんど使われない。キャッシュのコンテキストを分けるか？

	//FIXME メタデータのキャッシュの正確なクリア処理の実装

	public static final String METADATA_CACHE_NAMESPACE = "mtp.metadata.metaData";
//	public static final String METADATA_LIST_CACHE_NAMESPACE = "mtp.metadata.metaDataList";
	public static final String METADATA_DEF_LIST_CACHE_NAMESPACE = "mtp.metadata.metaDataDefList";


	/**
	 * MetaDataContext取得用ユーティリティメソッド。
	 */
	public static MetaDataContext getContext() {
		return ExecuteContext.getCurrentContext().getTenantContext().getMetaDataContext();
	}

	private final int tenantId;
	private final MetaDataRepository repository;
	private final TenantContextService tcService;

	private final CacheController<String, MetaDataEntry> cache;
//	private final CacheController<String, MetaDataListCacheEntry> listCache;
	private final CacheController<String, MetaDataDefinitionCacheEntry> defListCache;

	/** MetaDataの変更通知Listner */
	private final CopyOnWriteArrayList<MetaDataContextListener> listenerList;

	public MetaDataContext(int tenantId) {
		this.tenantId = tenantId;
		repository = ServiceRegistry.getRegistry().getService(MetaDataRepository.class);
		tcService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		
		//tenant local -> share とでキャッシュが分かれている。localで毎回DBに検索行かないために、ネガティブキャッシュする。
		//TODO ネガティブキャッシュの有効期間を短くする
		cache = new CacheController<String, MetaDataEntry>(cs.getCache(METADATA_CACHE_NAMESPACE + "/" + tenantId), true, 1, new MetaDataCacheLogic(), true, true);
//		listCache = new CacheController<String, MetaDataListCacheEntry>(cs.getCache(METADATA_LIST_CACHE_NAMESPACE + "/" + tenantId), false, 0, new MetaDataListCacheLogic(), false);
		defListCache = new CacheController<String, MetaDataDefinitionCacheEntry>(cs.getCache(METADATA_DEF_LIST_CACHE_NAMESPACE + "/" + tenantId), false, 0, new MetaDataDefinitionCacheLogic(), false, true);
		listenerList = new CopyOnWriteArrayList<MetaDataContextListener>();
	}

	/**
	 * <p>メタデータの変更通知先リスナーを追加します。</p>
	 *
	 * @param listener メタデータ変更通知先リスナー
	 */
	public void addMetaDataContextListener(MetaDataContextListener listener) {
		listenerList.add(listener);
	}

	/**
	 * <p>メタデータの変更通知先リスナーを削除します。</p>
	 *
	 * @param listener メタデータ変更通知先リスナー
	 */
	public void removeMetaDataContextListener(MetaDataContextListener listener) {
		if (listenerList.contains(listener)) {
			listenerList.remove(listener);
		}
	}

	/**
	 * <p>パスに一致する {@link MetaDataRuntime} を取得します。</p>
	 *
	 * @param type 取得する {@link MetaDataRuntime} の型
	 * @param path パス
	 * @return {@link MetaDataRuntime}
	 */
	@SuppressWarnings("unchecked")
	public <H extends MetaDataRuntime> H getMetaDataHandler(Class<H> type, String path) {
		MetaDataEntry ent = getMetaDataEntry(path);
		if (ent != null) {
			return (H) ent.getRuntime();
		} else {
			return null;
		}
	}

	/**
	 * <p>IDに一致する {@link MetaDataRuntime} を取得します。</p>
	 *
	 * @param type 取得する {@link MetaDataRuntime} の型
	 * @param id   ID
	 * @return {@link MetaDataRuntime}
	 */
	@SuppressWarnings("unchecked")
	public <H extends MetaDataRuntime> H getMetaDataHandlerById(Class<H> type, String id) {
		MetaDataEntry ent = getMetaDataEntryById(id);
		if (ent != null) {
			return (H) ent.getRuntime();
		} else {
			return null;
		}
	}

	/**
	 * <p>IDに一致する {@link MetaDataRuntime} を取得します。
	 * バージョンを指定した場合、キャッシュを参照しません。</p>
	 *
	 * @param type    取得する {@link MetaDataRuntime} の型
	 * @param id      ID
	 * @param version バージョン
	 * @return {@link MetaDataRuntime}
	 */
	@SuppressWarnings("unchecked")
	public <H extends MetaDataRuntime> H getMetaDataHandlerById(Class<H> type, String id, int version) {
		MetaDataEntry ent = getMetaDataEntryById(id, version);
		if (ent != null) {
			return (H) ent.getRuntime();
		} else {
			return null;
		}
	}

	/**
	 * <p>パスに一致する {@link MetaDataEntry} を取得します。</p>
	 *
	 * @param path パス
	 * @return {@link MetaDataEntry}
	 */
	public MetaDataEntry getMetaDataEntry(String path) {
		MetaDataEntry entry = cache.getByIndex(0, path);
		//shared check
		if (entry == null && tcService.getSharedTenantId() != tenantId) {
			MetaDataContext sharedContext = tcService.getSharedTenantContext().getMetaDataContext();
			entry = sharedContext.getMetaDataEntry(path);
			if (entry != null && entry.isSharable()) {
				entry = entry.copy();
				entry.setRepositryType(RepositoryType.SHARED);
			} else {
				entry = null;
			}
		}
		return entry;
	}

	/**
	 * <p>IDに一致する {@link MetaDataEntry} を取得します。</p>
	 *
	 * @param id ID
	 * @return {@link MetaDataEntry}
	 */
	public MetaDataEntry getMetaDataEntryById(String id) {
		MetaDataEntry entry = cache.get(id);
		//shared check
		if (entry == null && tcService.getSharedTenantId() != tenantId) {
			MetaDataContext sharedContext = tcService.getSharedTenantContext().getMetaDataContext();
			entry = sharedContext.getMetaDataEntryById(id);
			if (entry != null && entry.isSharable()) {
				entry = entry.copy();
				entry.setRepositryType(RepositoryType.SHARED);
			} else {
				entry = null;
			}
		}
		return entry;
	}

	/**
	 * <p>IDに一致する {@link MetaDataEntry} を取得します。
	 * バージョンを指定した場合、キャッシュを参照しません。</p>
	 *
	 * @param id      ID
	 * @param version バージョン
	 * @return {@link MetaDataEntry}
	 */
	public MetaDataEntry getMetaDataEntryById(String id, int version) {
		//バージョンを直接指定された場合はキャッシュを利用しない
		MetaDataEntry entry = repository.loadById(tenantId, id, version, false);
		if (entry != null) {
			entry.initRuntime();
		}

		//shared check
		if (entry == null && tcService.getSharedTenantId() != tenantId) {
			MetaDataContext sharedContext = tcService.getSharedTenantContext().getMetaDataContext();
			entry = sharedContext.getMetaDataEntryById(id, version);
			if (entry != null && entry.isSharable()) {
				entry = entry.copy();
				entry.setRepositryType(RepositoryType.SHARED);
			} else {
				entry = null;
			}
		}

		return entry;
	}

	/**
	 * <p>メタデータを登録します。</p>
	 *
	 * <p>
	 * メタデータをそれぞれ単独で登録する場合に利用することを想定しています。
	 * 一括でメタデータを更新する場合は、{@link #storeRepository(String, RootMetaData, MetaDataConfig)}
	 * を利用してください。</p>
	 *
	 * @param path パス
	 * @param metaData メタデータ
	 */
	public void store(String path, RootMetaData metaData) {
		doStore(path, metaData, null, true, false);
	}

	/**
	 * <p>メタデータをリポジトリに登録します。</p>
	 * <p>
	 * リポジトリに対してメタデータを登録後、メタデータをリロードする場合は、
	 * doAutoReloadにtrueを指定してください。トランザクションがコミットされたタイミングで
	 * リロードされます。<br/>
	 * doAutoReloadがfalseの場合のリロード制御は、メタデータの一括登録後に行うなど、
	 * 呼び出しもとで制御する必要があります。
	 * </p>
	 *
	 * @param path パス
	 * @param metaData メタデータ
	 * @param config Config設定
	 * @param doAutoReload 登録後にメタデータをロードするか
	 */
	public void store(String path, RootMetaData metaData, MetaDataConfig config, boolean doAutoReload) {
		doStore(path, metaData, config, doAutoReload, true);
	}

	/**
	 * <p>メタデータをリポジトリに登録します。
	 *
	 * <p>
	 * リポジトリに対してメタデータを登録後、メタデータをリロードする場合は、
	 * doAutoReloadにtrueを指定してください。トランザクションがコミットされたタイミングで
	 * リロードされます。<br/>
	 * doAutoReloadがfalseの場合のリロード制御は、メタデータの一括登録後に行うなど、
	 * 呼び出しもとで制御する必要があります。
	 * </p>
	 *
	 * @param path パス
	 * @param metaData メタデータ
	 * @param version バージョン
	 * @param config Config設定
	 * @param doAutoReload 登録後にメタデータをロードするか
	 * @param doReloadAfterCommit リロードをコミットタイミングまで待つか
	 */
	private void doStore(final String path, final RootMetaData metaData, final MetaDataConfig config, final boolean doAutoReload, final boolean doReloadAfterCommit) {

		Transaction.required(transaction -> {

				transaction.addTransactionListener(new TransactionListener() {
					@Override
					public void afterCommit(Transaction t) {
						//Commit完了後にリロード
						if (doAutoReload && doReloadAfterCommit) {
							doAfterStoreProccess(path);
						}
						//listenerはdoAutoReloadフラグによらず必ず呼び出す
						if (listenerList.size() > 0) {
							for (MetaDataContextListener l: listenerList) {
								l.created(path);
							}
						}
					}
				});

				MetaDataConfig storeConfig = config;
				if (storeConfig == null) {
					storeConfig = new MetaDataConfig(false, false, false, false);
				}

				MetaDataEntry current = repository.load(tenantId, path, true);
				if (current != null) {
					throw new MetaDataDuplicatePathException(tenantId + "'s " + path + " MetaData is currently exsits");
				}
				MetaDataEntry toStore = new MetaDataEntry(path, metaData, State.VALID, 0,
						storeConfig.isOverwritable(), storeConfig.isSharable(), storeConfig.isDataSharable(), storeConfig.isPermissionSharable());
				repository.store(tenantId, toStore);

				if (doAutoReload && !doReloadAfterCommit) {
					//登録完了後にリロード
					doAfterStoreProccess(path);
				}

				return null;
		});
	}

	private void doAfterStoreProccess(String path) {
		MetaDataEntry reload = repository.load(tenantId, path, false);
		reload.initRuntime();

		cache.notifyCreate(reload);
		updateNodeCache(path);
	}

	/**
	 * <p>メタデータを更新します。</p>
	 * <p>
	 * メタデータをそれぞれ単独で更新する場合に利用することを想定しています。
	 * 一括でメタデータを更新する場合は、{@link #update(String, RootMetaData, MetaDataConfig, boolean)}
	 * を利用してください。</p>
	 *
	 * @param path     パス
	 * @param metaData メタデータ
	 */
	public void update(String path, RootMetaData metaData) {
		doUpdate(path, metaData, null, true, false);
	}

	/**
	 * <p>メタデータをリポジトリに更新します。</p>
	 * <p>
	 * リポジトリに対してメタデータを更新後、メタデータをリロードする場合は、
	 * doAutoReloadにtrueを指定してください。トランザクションがコミットされたタイミングで
	 * リロードされます。<br/>
	 * doAutoReloadがfalseの場合のリロード制御は、メタデータの一括更新後に行うなど、
	 * 呼び出しもとで制御する必要があります。
	 * </p>
	 *
	 * @param path パス
	 * @param metaData メタデータ
	 * @param config Config設定
	 * @param doAutoReload 登録後にメタデータをロードするか
	 */
	public void update(final String path, final RootMetaData metaData, final MetaDataConfig config, final boolean doAutoReload) {
		doUpdate(path, metaData, config, doAutoReload, true);
	}

	/**
	 * <p>メタデータをリポジトリに更新します。</p>
	 * <p>
	 * リポジトリに対してメタデータを更新後、メタデータをリロードする場合は、
	 * doAutoReloadにtrueを指定してください。トランザクションがコミットされたタイミングで
	 * リロードされます。<br/>
	 * doAutoReloadがfalseの場合のリロード制御は、メタデータの一括更新後に行うなど、
	 * 呼び出しもとで制御する必要があります。
	 * </p>
	 *
	 * @param path パス
	 * @param metaData メタデータ
	 * @param config Config設定
	 * @param doAutoReload 登録後にメタデータをロードするか
	 * @param doReloadAfterCommit リロードをコミットタイミングまで待つか
	 */
	private void doUpdate(final String path, final RootMetaData metaData, final MetaDataConfig config, final boolean doAutoReload, final boolean doReloadAfterCommit) {
		//idから既存を取得して、パスの変更があるかどうかチェック
		//まだ同一トランザクション中などで反映されていない可能性があるので、リポジトリから直接取得する
		final MetaDataEntry current = getMetaDataEntryByIdDirect(metaData.getId());

		if (current == null) {
			throw new MetaDataRuntimeException(tenantId + "'s " + path+ "(" + metaData.getId() + ") MetaData is currently no exsits.");
		}

		//更新前のpath（変更ある場合）
		final String pathBefore = path.equals(current.getPath()) ? null: current.getPath();

		if (current.getRepositryType() == RepositoryType.SHARED) {
			if (!current.isOverwritable()) {
				throw new MetaDataRuntimeException(path + "(" + metaData.getId() + ") MetaData not allowed overwrite.");
			}
		}

		Transaction.required(transaction -> {

				transaction.addTransactionListener(new TransactionListener() {
					@Override
					public void afterCommit(Transaction t) {
						//Commit完了後にリロード
						if (doAutoReload && doReloadAfterCommit) {
							doAfterUpdateProccess(path, current, pathBefore);
						}
						//listenerはdoAutoReloadフラグによらず必ず呼び出す
						if (listenerList.size() > 0) {
							for (MetaDataContextListener l: listenerList) {
								l.updated(path, pathBefore);
							}
						}
					}
				});

				MetaDataEntry toStore = current.copy();
				toStore.setMetaData(metaData);
				toStore.setPath(path);
				repository.update(tenantId, toStore);

				if (config != null) {
					
					if (current.getRepositryType() == RepositoryType.SHARED) {
						toStore.setRepositryType(RepositoryType.SHARED_OVERWRITE);
					}

					updateConfigRepository(toStore, config, false);	//ここではリロードする必要なし
				}

				if (doAutoReload && !doReloadAfterCommit) {
					//更新完了後にリロード
					doAfterUpdateProccess(path, current, pathBefore);
				}

		});

	}

	private MetaDataEntry getMetaDataEntryByIdDirect(String id) {
		boolean isShared = tcService.getSharedTenantId() == tenantId;
		MetaDataEntry entry = repository.loadById(tenantId, id, isShared);
		checkShared(entry);
		
		//shared check
		if (entry == null && tcService.getSharedTenantId() != tenantId) {
			MetaDataContext sharedContext = tcService.getSharedTenantContext().getMetaDataContext();
			entry = sharedContext.getMetaDataEntryById(id);
			if (entry != null && entry.isSharable()) {
				entry = entry.copy();
				entry.setRepositryType(RepositoryType.SHARED);
			} else {
				entry = null;
			}
		}
		return entry;
	}
	
	/**
	 * 指定のIDのメタデータをキャッシュクリアしリロードします。
	 * 
	 * @param id
	 */
	public void reloadById(String id) {
		MetaDataEntry reload = repository.loadById(tenantId, id, false);
		if (reload != null) {
			reload.initRuntime();
			cache.notifyUpdate(reload);
			updateNodeCache(reload.getPath());
		}
	}

	private void doAfterUpdateProccess(String path, MetaDataEntry current, String pathBefore) {
		MetaDataEntry reload = repository.load(tenantId, path, false);
		if (current.getRepositryType() == RepositoryType.SHARED) {
			reload.setRepositryType(RepositoryType.SHARED_OVERWRITE);
		} else {
			//shared check
			if (tcService.getSharedTenantId() != tenantId) {
				MetaDataContext sharedContext = tcService.getSharedTenantContext().getMetaDataContext();
				MetaDataEntry entry = sharedContext.getMetaDataEntryById(current.getMetaData().getId());
				if (entry != null) {
					reload.setRepositryType(RepositoryType.SHARED_OVERWRITE);
				}
			}
		}
		reload.initRuntime();

		cache.notifyUpdate(reload);
		updateNodeCache(path);
		if (pathBefore != null) {
			updateNodeCache(pathBefore);
		}
	}

	/**
	 * <p>メタデータを削除します。</p>
	 * <p>
	 * メタデータをそれぞれ単独で削除する場合に利用することを想定しています。
	 * 一括でメタデータを更新する場合は、{@link #remove(String, boolean)}
	 * を利用してください。</p>
	 *
	 * @param path     パス
	 */
	public void remove(String path) {
		doRemove(path, true, false);
	}

	/**
	 * <p>メタデータをリポジトリから削除します。</p>
	 * <p>
	 * リポジトリに対してメタデータを削除後、メタデータをリロードする場合は、
	 * doAutoReloadにtrueを指定してください。トランザクションがコミットされたタイミングで
	 * リロードされます。<br/>
	 * doAutoReloadがfalseの場合のリロード制御は、メタデータの一括更新後に行うなど、
	 * 呼び出しもとで制御する必要があります。
	 * </p>
	 *
	 * @param path パス
	 * @param doAutoReload 登録後にメタデータをロードするか
	 */
	public void remove(final String path, final boolean doAutoReload) {
		doRemove(path, doAutoReload, true);
	}

	/**
	 * <p>メタデータをリポジトリから削除します。</p>
	 * <p>
	 * リポジトリに対してメタデータを削除後、メタデータをリロードする場合は、
	 * doAutoReloadにtrueを指定してください。トランザクションがコミットされたタイミングで
	 * リロードされます。<br/>
	 * doAutoReloadがfalseの場合のリロード制御は、メタデータの一括更新後に行うなど、
	 * 呼び出しもとで制御する必要があります。
	 * </p>
	 *
	 * @param path パス
	 * @param doAutoReload 登録後にメタデータをロードするか
	 */
	private void doRemove(final String path, final boolean doAutoReload, final boolean doReloadAfterCommit) {

		//まだ同一トランザクション中などで反映されていない可能性があるので、リポジトリから直接取得する
		final MetaDataEntry current = repository.load(tenantId, path, true);

		Transaction.required(transaction -> {

				transaction.addTransactionListener(new TransactionListener() {
					@Override
					public void afterCommit(Transaction t) {
						//Commit完了後にリロード
						if (doAutoReload && doReloadAfterCommit) {
							doAfterRemoveProccess(path, current);
						}
						//listenerはdoAutoReloadフラグによらず必ず呼び出す
						if (listenerList.size() > 0) {
							for (MetaDataContextListener l: listenerList) {
								l.removed(path);
							}
						}
					}
				});

				if (current != null && current.getRepositryType() != RepositoryType.SHARED) {
					repository.remove(tenantId, path);
				}

				if (doAutoReload && !doReloadAfterCommit) {
					//更新完了後にリロード
					doAfterRemoveProccess(path, current);
				}

		});
	}

	private void doAfterRemoveProccess(String path, MetaDataEntry current) {
		try {
			if (current != null) {
				cache.notifyDelete(current);
			}
		} catch (Exception e) {
			logger.error("can not load MetaData cause Exception... So, Do force delete Operation... Clear all Cache...", e);
			cache.clearAll();
		}

		updateNodeCache(path);
	}

	/**
	 * <p>メタデータのConfig定義を更新します。</p>
	 * <p>
	 * メタデータをそれぞれ単独で更新する場合に利用することを想定しています。
	 * 一括でメタデータを更新する場合は、{@link #updateConfigRepository(String, MetaDataConfig, boolean)}
	 * を利用してください。</p>
	 *
	 * @param path   パス
	 * @param config Config定義
	 */
	public void updateConfig(String path, MetaDataConfig config) {
		MetaDataEntry meta = getMetaDataEntry(path);
		if (meta == null) {
			throw new MetaDataRuntimeException(tenantId + "'s " + path + " MetaData is currently no exsits.");
		}
		updateConfigRepository(meta, config, true);
	}

	/**
	 * <p>メタデータのConfig定義をリポジトリに更新します。</p>
	 * <p>
	 * リポジトリに対してメタデータを更新後、メタデータをリロードする場合は、
	 * doAutoReloadにtrueを指定してください。トランザクションがコミットされたタイミングで
	 * リロードされます。<br/>
	 * doAutoReloadがfalseの場合のリロード制御は、メタデータの一括更新後に行うなど、
	 * 呼び出しもとで制御する必要があります。
	 * </p>
	 *
	 * @param path パス
	 * @param config Config設定
	 * @param doAutoReload 登録後にメタデータをロードするか
	 */
	private void updateConfigRepository(final MetaDataEntry meta, final MetaDataConfig config, final boolean doAutoReload) {

		
		if (meta.getRepositryType() == RepositoryType.SHARED) {
			throw new MetaDataRuntimeException(meta.getPath() + "(" + meta.getMetaData().getId() + ") MetaData not allowed change config.");
		}

		Transaction.required(transaction -> {

				if (doAutoReload) {
					//Commit完了後にリロード
					transaction.addTransactionListener(new TransactionListener() {
						@Override
						public void afterCommit(Transaction t) {
							MetaDataEntry reload = repository.loadById(tenantId, meta.getMetaData().getId(), false);
							reload.setRepositryType(meta.getRepositryType());//local or shared_overwrite
							reload.initRuntime();

							cache.notifyUpdate(reload);
							updateNodeCache(meta.getPath());

							if (listenerList.size() > 0) {
								for (MetaDataContextListener l: listenerList) {
									l.updated(meta.getPath(), null);
								}
							}
						}
					});
				}

				//すでにオーバーライト済みのメタデータがないかどうかチェック
				//TODO 微妙なタイミングでオーバーライトされてしまい、不整合が発生する可能性あるが、チェックしないよりましってことで、、、
				if (tcService.getSharedTenantId() == tenantId) {
					//sharable true -> false
					//および、sharable==true && overwritable==true から、overwritable=falseにする際、
					//すでにオーバーライト済みのメタデータが存在する場合、更新できない。
					if (meta.isSharable() && !config.isSharable()
							|| meta.isSharable() && meta.isOverwritable() && !config.isOverwritable()) {
						if (hasOverwriteMetaData(meta)) {
							throw new MetaDataRuntimeException(meta.getPath() + " MetaData is already shared and overwrote.so can not change config to no share or no overwrite.");
						}
					}

					//overwritable=trueで、dataSharable=true、permissionSharable=trueはできない
					if (config.isSharable() && config.isOverwritable()) {
						if (config.isDataSharable() || config.isPermissionSharable()) {
							throw new MetaDataRuntimeException(meta.getPath() + " MetaData can not set to overwrite and dataShare/permissionShare at the same time.");
						}
					}

					//TenantLocal RDBにデータが存在しないため登録する。
					MetaDataEntry localMeta = repository.loadById(tenantId, meta.getMetaData().getId(), false);
					if (localMeta == null) {
						repository.store(tenantId, localMeta);
					}
				}

				repository.updateConfigById(tenantId, meta.getMetaData().getId(), config);

		});

	}
	
	private boolean hasOverwriteMetaData(MetaDataEntry meta) {
		List<Integer> list = repository.getTenantIdsOf(meta.getMetaData().getId());
		if (list.size() == 0) {
			return false;
		}
		if (list.contains(tcService.getSharedTenantId()) && list.size() == 1) {
			return false;
		}
		return true;
	}

	/**
	 * <p>メタデータの状態を検証します。</p>
	 *
	 * <p>エラーがある場合は {@link MetaDataIllegalStateException} をスローします。</p>
	 *
	 * @param path パス
	 * @throws MetaDataIllegalStateException
	 */
	public void checkState(String path) throws MetaDataIllegalStateException {
		MetaDataRuntime metaData = getMetaDataHandler(MetaDataRuntime.class, path);
		if (metaData == null) {
			throw new MetaDataIllegalStateException("path:" + path + " MetaDataRuntime not found");
		}
		metaData.checkState();
	}

	/**
	 * <p>指定されたパスに属するメタデータ定義のPathを取得します。</p>
	 *
	 * <p>より詳細な一覧を取得する場合は、{@link #definitionList(String)} を利用してください。</p>
	 *
	 * @param prefixPath パス(前方一致)
	 * @return メタデータのPathリスト
	 */
	public List<String> pathList(final String prefixPath) {
		List<MetaDataEntryInfo> infoList = definitionList(prefixPath);
		if (infoList == null) {
			return null;
		}

		ArrayList<String> pathList = new ArrayList<String>(infoList.size());
		for (MetaDataEntryInfo info: infoList) {
			pathList.add(info.getPath());
		}
		return pathList;
	}

	/**
	 * <p>指定されたパスに属するメタデータ定義の基本情報を取得します。</p>
	 *
	 * @param prefixPath パス(前方一致)
	 * @return メタデータの基本情報 {@link MetaDataEntryInfo} リスト
	 */
	public List<MetaDataEntryInfo> definitionList(final String prefixPath) {
		String path = prefixPath;
		if (!path.endsWith("/")) {
			path = path + "/";
		}

		List<MetaDataEntryInfo> list = null;
		//shared check
		if (tcService.getSharedTenantId() != tenantId) {
			HashMap<String, MetaDataEntryInfo> map = new HashMap<String, MetaDataEntryInfo>();
			List<MetaDataEntryInfo> sharedList = tcService.getSharedTenantContext().getMetaDataContext().definitionList(path);
			if (sharedList != null) {
				for (MetaDataEntryInfo info: sharedList) {
					if (info.isSharable()) {
						info = info.copy();
						info.setRepositryType(RepositoryType.SHARED);
						map.put(info.getPath(), info);
					}
				}
			}
			MetaDataDefinitionCacheEntry nodeEntry = defListCache.get(path);
			if (nodeEntry != null) {
				Map<String, MetaDataEntryInfo> localMap = nodeEntry.map;
				if (localMap != null) {
					for (MetaDataEntryInfo info: localMap.values()) {
						if (map.containsKey(info.getPath())) {
							info = info.copy();
							info.setRepositryType(RepositoryType.SHARED_OVERWRITE);
						}
						map.put(info.getPath(), info);
					}
				}
			}
			list = new ArrayList<MetaDataEntryInfo>(map.values());
		} else {
			MetaDataDefinitionCacheEntry nodeEntry = defListCache.get(path);
			if (nodeEntry != null) {
				Map<String, MetaDataEntryInfo> localMap = nodeEntry.map;
				if (localMap != null) {
					list = new ArrayList<MetaDataEntryInfo>(localMap.values());
				}
			}
		}

		//ソート
		Collections.sort(list, new Comparator<MetaDataEntryInfo>() {
			@Override
			public int compare(MetaDataEntryInfo o1, MetaDataEntryInfo o2) {
				return o1.getPath().toLowerCase().compareTo(o2.getPath().toLowerCase());
			}
		});

		return list;
	}
	
	public List<MetaDataEntryInfo> invalidDefinitionList(final String prefixPath) {
		String path = prefixPath;
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		boolean isShared = tcService.getSharedTenantId() == tenantId;
		MetaDataContext sharedContext = null;
		if (!isShared) {
			sharedContext = tcService.getSharedTenantContext().getMetaDataContext();
		}
		
		List<MetaDataEntryInfo> localList = repository.definitionList(tenantId, path, isShared, true);
		List<MetaDataEntryInfo> invalidList = new LinkedList<>();
		for (MetaDataEntryInfo e: localList) {
			if (e.getState() == State.INVALID) {
				if (!isShared) {
					MetaDataEntry entry = sharedContext.getMetaDataEntryById(e.getId());
					if (entry == null || !entry.isSharable()) {
						invalidList.add(e);
					}
				} else {
					invalidList.add(e);
				}
			}
		}
		
		return invalidList;
	}

	public boolean exists(String prefixPath, String subPath) {
		if (!prefixPath.endsWith("/")) {
			prefixPath = prefixPath + "/";
		}
		
		//shared check
		if (tcService.getSharedTenantId() != tenantId) {
			boolean shareExists = tcService.getSharedTenantContext().getMetaDataContext().exists(prefixPath, subPath);
			if (shareExists) {
				return true;
			}
		}
		
		MetaDataDefinitionCacheEntry nodeEntry = defListCache.get(prefixPath);
		if (nodeEntry != null) {
			String fullPath = prefixPath + subPath;
			return nodeEntry.map.containsKey(fullPath);
		}
		
		return false;
	}


	/**
	 * <p>対象メタデータ定義をオーバーライトしているテナントのIDを取得します。</p>
	 *
	 * <p>シェアテナント以外で対象メタデータ定義を定義しているテナントIDです。</p>
	 *
	 * @param metaDataId ID
	 * @return オーバーライトテナントIDのリスト
	 */
	public List<Integer> getOverwriteTenantIdList(String metaDataId) {
		List<Integer> res = repository.getTenantIdsOf(metaDataId);
		res.removeIf(tid -> tid.equals(tcService.getSharedTenantId()));
		return res;
	}

	/**
	 * <p>キャッシュを無効にします。</p>
	 */
	public void invalidate() {
		cache.invalidateCacheStore();
		defListCache.invalidateCacheStore();
	}

	/**
	 * <p>キャッシュを全てクリアします。</p>
	 */
	public void clearAllCache() {
		cache.clearAll();
		defListCache.clearAll();
	}

	/**
	 * <p>キャッシュ情報を出力します。</p>
	 */
	public void traceCache() {
		cache.trace();
		//defListCache.trace();
	}

	//TODO これはEntityContextというかEntity定義の更新処理から実行されているだけ。
	public void refreshTransactionLocalCache(String id) {
		String beforePath = null;
		MetaDataEntry before = getMetaDataEntryById(id);
		if (before != null) {
			beforePath = before.getPath();
		}
		cache.refreshTransactionLocalStore(id);

		String afterPath = null;
		MetaDataEntry after = getMetaDataEntryById(id);
		if (after != null) {
			afterPath = after.getPath();
		}

		if (beforePath != null && !beforePath.equals(afterPath)) {
			updateNodeCache(beforePath);
		}
		if (afterPath != null) {
			updateNodeCache(afterPath);
		}
	}

	/**
	 * <p>基本情報リストのキャッシュを更新します。</p>
	 *
	 * @param prefixPath パス
	 */
	private void updateNodeCache(String prefixPath) {
		String listPath = prefixPath;
		if (!listPath.endsWith("/")) {
			listPath = listPath + "/";
		}
		boolean isShared = tcService.getSharedTenantId() == tenantId;
		while(listPath.length() != 0) {
			listPath = listPath.substring(0, listPath.lastIndexOf('/'));
			String pathWithSlash = listPath + "/";
			List<MetaDataEntryInfo> list = repository.definitionList(tenantId, pathWithSlash, isShared);
			if (list == null || list.size() == 0) {
				list = Collections.emptyList();
			}
			defListCache.notifyUpdate(new MetaDataDefinitionCacheEntry(pathWithSlash, list));
		}
	}

	/**
	 * <p>メタデータの個別詳細定義 {@link MetaDataRuntime} 取得用キャッシュロジック</p>
	 */
	private class MetaDataCacheLogic implements LoadingAdapter<String, MetaDataEntry> {

		@Override
		public Object getIndexVal(int index, MetaDataEntry val) {
			return val.getPath();
		}

		@Override
		public String getKey(MetaDataEntry val) {
			return val.getMetaData().getId();
		}

		@Override
		public MetaDataEntry load(String key) {
			boolean isShared = tcService.getSharedTenantId() == tenantId;
			final MetaDataEntry ent = repository.loadById(tenantId, key, isShared);
			checkShared(ent);
			if (ent != null) {
				if (ExecuteContext.getCurrentContext().getClientTenantId() != tenantId) {
					//sharedの初期化はsharedのテナントとして初期化したいので
					ExecuteContext.executeAs(tcService.getTenantContext(tenantId), new Executable<Void>() {
						@Override
						public Void execute() {
							ent.initRuntime();
							return null;
						}
					});
				} else {
					ent.initRuntime();
				}
				return ent;
			} else {
				return null;
			}
		}

		@Override
		public List<MetaDataEntry> loadByIndex(int index, Object indexVal) {
			boolean isShared = tcService.getSharedTenantId() == tenantId;
			final MetaDataEntry ent = repository.load(tenantId, (String) indexVal, isShared);
			checkShared(ent);
			if (ent != null) {
				if (ExecuteContext.getCurrentContext().getClientTenantId() != tenantId) {
					//sharedの初期化はsharedのテナントとして初期化したいので
					ExecuteContext.executeAs(tcService.getTenantContext(tenantId), new Executable<Void>() {
						@Override
						public Void execute() {
							ent.initRuntime();
							return null;
						}
					});
				} else {
					ent.initRuntime();
				}
				return Arrays.asList(ent);
			} else {
				return null;
			}
		}

		@Override
		public long getVersion(MetaDataEntry value) {
			return value.getVersion();
		}

	}

	private void checkShared(MetaDataEntry entry) {
		if (entry != null) {
			if(tcService.getSharedTenantId() != tenantId) {

				MetaDataContext sharedContext = tcService.getSharedTenantContext().getMetaDataContext();
				MetaDataEntry sharedEntry = sharedContext.getMetaDataEntry(entry.getPath());

				if (sharedEntry != null && sharedEntry.isSharable()) {
					entry.setRepositryType(RepositoryType.SHARED_OVERWRITE);
				}
			} else {
				entry.setRepositryType(RepositoryType.TENANT_LOCAL);
			}
		}
	}

	private class MetaDataDefinitionCacheLogic implements LoadingAdapter<String, MetaDataDefinitionCacheEntry> {

		@Override
		public Object getIndexVal(int index, MetaDataDefinitionCacheEntry val) {
			return null;
		}

		@Override
		public String getKey(MetaDataDefinitionCacheEntry val) {
			return val.repositoryPath;
		}

		@Override
		public MetaDataDefinitionCacheEntry load(String key) {
			boolean isShared = tcService.getSharedTenantId() == tenantId;
			List<MetaDataEntryInfo> list = repository.definitionList(tenantId, key, isShared);
			if (list != null) {
				return new MetaDataDefinitionCacheEntry(key, list);
			} else {
				return null;
			}
		}

		@Override
		public List<MetaDataDefinitionCacheEntry> loadByIndex(int index, Object indexVal) {
			return null;
		}

		@Override
		public long getVersion(MetaDataDefinitionCacheEntry value) {
			return 0;
		}
	}

	public static class MetaDataDefinitionCacheEntry {
		private String repositoryPath;
		private Map<String, MetaDataEntryInfo> map;
		public MetaDataDefinitionCacheEntry() {
		}
		public MetaDataDefinitionCacheEntry(String repositoryPath, List<MetaDataEntryInfo> list) {
			this.repositoryPath = repositoryPath;
			map = new HashMap<>();
			for (MetaDataEntryInfo e: list) {
				map.put(e.getPath(), e);
			}
		}
		@Override
		public String toString() {
			return "MetaDataDefinitionCacheEntry(path=" + repositoryPath
					+ ",map=" + map + ")";
		}
	}

}
