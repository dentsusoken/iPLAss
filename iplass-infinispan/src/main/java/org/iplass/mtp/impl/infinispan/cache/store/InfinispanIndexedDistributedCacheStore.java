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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.infinispan.Cache;
import org.infinispan.distexec.DefaultExecutorService;
import org.infinispan.distexec.DistributedCallable;
import org.infinispan.distexec.DistributedExecutorService;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.builtin.TransactionLocalCacheStoreFactory.TransactionLocalCacheStore;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanIndexedDistributedCacheStore extends
		InfinispanIndexedCacheStore {

	private static Logger logger = LoggerFactory.getLogger(InfinispanIndexedDistributedCacheStore.class);

	private final DistributedExecutorService distExec;

	public InfinispanIndexedDistributedCacheStore(CacheStore wrapped,
			Cache<IndexKey, IndexEntry> indexStore, int indexSize,
			int indexRemoveRetryCount, long indexRemoveRetryIntervalNanos) {
		super(wrapped, indexStore, indexSize, indexRemoveRetryCount,
				indexRemoveRetryIntervalNanos, false);
		distExec = new DefaultExecutorService(indexStore, Executors.newCachedThreadPool());
	}

	@Override
	public void removeAll() {

		List<CompletableFuture<Void>> ret = distExec.submitEverywhere(new RemoveAllTask(wrapped.getNamespace()));
		for (Future<Void> f: ret) {
			try {
				f.get();
			} catch (InterruptedException e) {
				fatalLogger.error("cant removeAll cause remote execution interrupted. maybe cache index is inconsistent state... namespace=" + wrapped.getNamespace(), e);
			} catch (ExecutionException e) {
				fatalLogger.error("cant removeAll cause remote execution interrupted. maybe cache index is inconsistent state... namespace=" + wrapped.getNamespace(), e.getCause());
			}
		}
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
			MainteIndexTask task = new MainteIndexTask(oldRef, newRef, removeIndexList, addIndexList, indexRemoveTryCount, indexRemoveRetryIntervalNanos);
			Set<IndexKey> keySet = new HashSet<>();
			if (removeIndexList != null) {
				keySet.addAll(removeIndexList);
			}
			if (addIndexList != null) {
				keySet.addAll(addIndexList);
			}
			List<CompletableFuture<Void>> ret = distExec.submitEverywhere(task, keySet.toArray(new IndexKey[keySet.size()]));
			for (Future<Void> f: ret) {
				try {
					f.get();
				} catch (InterruptedException e) {
					fatalLogger.error("cant mainte index cause remote execution interrupted. maybe cache index is inconsistent state... oldEntry=" + oldEntry + ", newEntry=" + newEntry, e);
				} catch (ExecutionException e) {
					fatalLogger.error("cant mainte index cause remote execution exceptioned. maybe cache index is inconsistent state... oldEntry=" + oldEntry + ", newEntry=" + newEntry, e.getCause());
				}
			}
		}
	}

	public static class RemoveAllTask implements DistributedCallable<Object, CacheEntry, Void>, Serializable {
		private static final long serialVersionUID = -7335308264912546719L;

		private final String namespace;

		public RemoveAllTask(String namespace) {
			super();
			this.namespace = namespace;
		}

		@Override
		public Void call() throws Exception {
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

		@Override
		public void setEnvironment(Cache<Object, CacheEntry> cache,
				Set<Object> inputKeys) {
		}

	}

	public static class MainteIndexTask implements DistributedCallable<IndexKey, IndexEntry, Void>, Serializable {
		private static final long serialVersionUID = 663386283917160542L;

		private int indexRemoveTryCount;
		private long indexRemoveRetryIntervalNanos;

		private CacheEntryRef oldRef;
		private CacheEntryRef newRef;

		private List<IndexKey> removeIndexList;
		private List<IndexKey> addIndexList;

		private transient Set<IndexKey> inputKeys;
		private transient Cache<IndexKey, IndexEntry> cache;

		public MainteIndexTask(CacheEntryRef oldRef, CacheEntryRef newRef,
				List<IndexKey> removeIndexList, List<IndexKey> addIndexList, int indexRemoveTryCount, long indexRemoveRetryIntervalNanos) {
			this.oldRef = oldRef;
			this.newRef = newRef;
			this.removeIndexList = removeIndexList;
			this.addIndexList = addIndexList;
			this.indexRemoveTryCount = indexRemoveTryCount;
			this.indexRemoveRetryIntervalNanos = indexRemoveRetryIntervalNanos;
		}

		@Override
		public Void call() throws Exception {
			if (logger.isTraceEnabled()) {
				logger.trace("mainteIndexTask:old=" + oldRef + ", new=" + newRef + ", keys=" + inputKeys);
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

		@Override
		public void setEnvironment(Cache<IndexKey, IndexEntry> cache,
				Set<IndexKey> inputKeys) {
			this.cache = cache;
			this.inputKeys = inputKeys;
		}

	}

}
