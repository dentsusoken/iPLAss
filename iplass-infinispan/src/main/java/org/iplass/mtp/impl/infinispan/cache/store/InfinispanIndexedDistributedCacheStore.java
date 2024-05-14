/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.infinispan.cache.store;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.infinispan.Cache;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.builtin.TransactionLocalCacheStoreFactory.TransactionLocalCacheStore;
import org.iplass.mtp.impl.infinispan.InfinispanService;
import org.iplass.mtp.impl.infinispan.task.InfinispanSerializableTask;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskExecutor;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskState;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanIndexedDistributedCacheStore extends InfinispanIndexedCacheStore {

	private static Logger logger = LoggerFactory.getLogger(InfinispanIndexedDistributedCacheStore.class);

	public InfinispanIndexedDistributedCacheStore(CacheStore wrapped, Cache<IndexKey, IndexEntry> indexStore, int indexSize, int indexRemoveRetryCount,
			long indexRemoveRetryIntervalNanos) {
		super(wrapped, indexStore, indexSize, indexRemoveRetryCount, indexRemoveRetryIntervalNanos, false);
	}

	@Override
	public void removeAll() {

		InfinispanTaskState<Void> state = InfinispanTaskExecutor.submitAll(new RemoveAllTask(wrapped.getNamespace()));

		state.getFuture().forEach(f -> {
			try {
				f.get();
			} catch (InterruptedException e) {
				fatalLogger.error("cant removeAll cause remote execution interrupted. maybe cache index is inconsistent state... namespace={}",
						wrapped.getNamespace(), e);
			} catch (ExecutionException e) {
				fatalLogger.error("cant removeAll cause remote execution interrupted. maybe cache index is inconsistent state... namespace={}",
						wrapped.getNamespace(), e.getCause());
			}
		});
	}

	@Override
	protected void mainteIndex(CacheEntry oldEntry, CacheEntry newEntry) {
		if (logger.isTraceEnabled()) {
			logger.trace("mainteIndex:old=" + oldEntry + ", new=" + newEntry);
		}

		CacheEntryRef oldRef = null;
		CacheEntryRef newRef = null;
		List<IndexKey> removeIndexList = null;
		List<IndexKey> addIndexList = null;

		for (int i = 0; i < indexSize; i++) {
			if (oldEntry != null) {
				if (removeIndexList == null) {
					removeIndexList = new ArrayList<>();
				}
				oldRef = new CacheEntryRef(oldEntry.getKey(), oldEntry.getVersion());
				Object iVal = oldEntry.getIndexValue(i);
				if (iVal instanceof Object[]) {
					for (Object iv: (Object[]) iVal) {
						removeIndexList.add(new IndexKey(i, iv));
					}
				} else {
					removeIndexList.add(new IndexKey(i, iVal));
				}
			}
			if (newEntry != null) {
				if (addIndexList == null) {
					addIndexList = new ArrayList<>();
				}
				newRef = new CacheEntryRef(newEntry.getKey(), newEntry.getVersion());
				Object iVal = newEntry.getIndexValue(i);
				if (iVal instanceof Object[]) {
					for (Object iv: (Object[]) iVal) {
						addIndexList.add(new IndexKey(i, iv));
					}
				} else {
					addIndexList.add(new IndexKey(i, iVal));
				}
			}
		}

		if (oldRef != null || newRef != null) {
			Set<IndexKey> keySet = new HashSet<>();
			if (removeIndexList != null) {
				keySet.addAll(removeIndexList);
			}
			if (addIndexList != null) {
				keySet.addAll(addIndexList);
			}

			final var finalOldRef = oldRef;
			final var finalNewRef = newRef;
			final var finalRemoveIndexList = removeIndexList;
			final var finalAddIndexList = addIndexList;

			InfinispanTaskState<Void> state = InfinispanTaskExecutor.submitByCacheKeyOwner(
					k -> new MainteIndexTask(finalOldRef, finalNewRef, finalRemoveIndexList, finalAddIndexList, indexRemoveTryCount,
							indexRemoveRetryIntervalNanos, indexStore.getName(), new HashSet<>(k)),
					indexStore.getName(), new ArrayList<>(keySet));

			state.getFuture().forEach(f -> {
				try {
					f.get();
				} catch (InterruptedException e) {
					fatalLogger.error("cant mainte index cause remote execution interrupted. maybe cache index is inconsistent state... oldEntry={}, newEntry={}",
							oldEntry, newEntry, e);
				} catch (ExecutionException e) {
					fatalLogger.error("cant mainte index cause remote execution interrupted. maybe cache index is inconsistent state... oldEntry={}, newEntry={}",
							oldEntry, newEntry, e.getCause());
				}

			});
		}
	}

	public static class RemoveAllTask implements InfinispanSerializableTask<Void> {
		private static final long serialVersionUID = -7335308264912546719L;

		private final String namespace;

		public RemoveAllTask(String namespace) {
			this.namespace = namespace;
		}

		@Override
		public Void callByNode() {
			LoggerFactory.getLogger(RemoveAllTask.class).debug("RemoveAllTask");
			CacheStore store = ServiceRegistry.getRegistry().getService(CacheService.class).getCache(namespace, false);
			if (store != null) {
				InfinispanIndexedDistributedCacheStore infiniStore = null;
				if (store instanceof InfinispanIndexedDistributedCacheStore) {
					infiniStore = (InfinispanIndexedDistributedCacheStore) store;
				} else if (store instanceof TransactionLocalCacheStore) {
					infiniStore = (InfinispanIndexedDistributedCacheStore) ((TransactionLocalCacheStore) store).getBackendCacheStore();
				} else {
					throw new SystemException("infinispan cache configuration invalid.Only TransactionLocalCache can wrapped InfinispanCacheStore");
				}

				if (logger.isTraceEnabled()) {
					logger.trace("removeAll in node:namespace=" + namespace + ", keySet=" + infiniStore.keySet());
				}
				infiniStore.removeAllLocal();

			}

			return null;
		}
	}

	public static class MainteIndexTask implements InfinispanSerializableTask<Void> {

		/** serialVersionUID */
		private static final long serialVersionUID = -8758094860575558310L;

		private CacheEntryRef oldRef;
		private CacheEntryRef newRef;

		private List<IndexKey> removeIndexList;
		private List<IndexKey> addIndexList;

		private int indexRemoveTryCount;
		private long indexRemoveRetryIntervalNanos;

		/** キャッシュ名 */
		private String cacheName;
		/** 入力キー */
		private Set<IndexKey> inputKeys;

		public MainteIndexTask(CacheEntryRef oldRef, CacheEntryRef newRef, List<IndexKey> removeIndexList, List<IndexKey> addIndexList,
				int indexRemoveTryCount, long indexRemoveRetryIntervalNanos, String cacheName, Set<IndexKey> inputKeys) {
			this.oldRef = oldRef;
			this.newRef = newRef;
			this.removeIndexList = removeIndexList;
			this.addIndexList = addIndexList;
			this.indexRemoveTryCount = indexRemoveTryCount;
			this.indexRemoveRetryIntervalNanos = indexRemoveRetryIntervalNanos;

			this.cacheName = cacheName;
			this.inputKeys = inputKeys;
		}


		@Override
		public Void callByNode() {
			Cache<IndexKey, IndexEntry> cache = ServiceRegistry.getRegistry().getService(InfinispanService.class).getCacheManager().getCache(cacheName);
			if (logger.isTraceEnabled()) {
				logger.trace("mainteIndexTask:old={}, new={}, keys={}", oldRef, newRef, inputKeys);
			}
			if (oldRef != null) {
				for (IndexKey k: removeIndexList) {
					if (inputKeys.contains(k)) {
						removeFromIndex(cache, k, oldRef, indexRemoveTryCount, indexRemoveRetryIntervalNanos);
					}
				}
			}
			if (newRef != null) {
				for (IndexKey k: addIndexList) {
					if (inputKeys.contains(k)) {
						addToIndex(cache, k, newRef);
					}
				}
			}
			return null;
		}
	}

}
