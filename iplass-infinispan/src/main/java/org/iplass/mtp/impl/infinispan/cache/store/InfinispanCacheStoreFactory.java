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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.util.function.SerializableBiFunction;
import org.infinispan.util.function.SerializableFunction;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.TimeToLiveCalculator;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.infinispan.InfinispanService;
import org.iplass.mtp.impl.infinispan.cache.store.InfinispanIndexedCacheStore.IndexEntry;
import org.iplass.mtp.impl.infinispan.cache.store.InfinispanIndexedCacheStore.IndexKey;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanCacheStoreFactory extends CacheStoreFactory implements ServiceInitListener<CacheService> {
	private static Logger logger = LoggerFactory.getLogger(InfinispanCacheStoreFactory.class);

	private static final String INDEX_STORE_POSTFIX = "._index";
	// NOTE infinispan 10.1 で infinispan.xml の '/infinispan/cache-container@default-cache' 属性がきかなくなっている為、機能としてデフォルト設定を利用する形に変更。
	/** cache の infinispan 設定名。infinispan.xml の '/infinispan/cache-container' 内参照 */
	private static final String DEFAULT_CACHE_CONFIGURATION_NAME = "___defaultcache";
	// TODO 別にする可能性があるので、定義名を分けておく。
	/** index cache の infinispan 設定名。 */
	private static final String DEFAULT_INDEX_CACHE_CONFIGURATION_NAME = DEFAULT_CACHE_CONFIGURATION_NAME;

	private EmbeddedCacheManager cm;
	private boolean createOnStartup = false;
	private CacheStore sharedInstance;
	private String cacheConfigrationName = DEFAULT_CACHE_CONFIGURATION_NAME;
	private TimeToLiveCalculator timeToLiveCalculator;

	private int indexRemoveRetryCount = 10;
	private long indexRemoveRetryInterval = 100;// ms
	private String indexCacheConfigrationName = DEFAULT_INDEX_CACHE_CONFIGURATION_NAME;

	private ExecutorService forCacheHandlerExecutorService;

	// TODO カスタムコンフィグの方法。namespacepatternの場合は必要となるかも

	@Override
	public void inited(CacheService service, Config config) {
		InfinispanService is = config.getDependentService(InfinispanService.class);
		cm = is.getCacheManager();

		if (createOnStartup) {
			if (getNamespace() == null) {
				throw new ServiceConfigrationException("if createOnStartup, specific namespace is required.");
			}
			if (logger.isDebugEnabled()) {
				logger.debug("create on startup InfinispanCacheStore:namespace=" + getNamespace());
			}
			sharedInstance = createCacheStoreInternal(getNamespace());
		}
	}

	public TimeToLiveCalculator getTimeToLiveCalculator() {
		return timeToLiveCalculator;
	}

	public void setTimeToLiveCalculator(TimeToLiveCalculator timeToLiveCalculator) {
		this.timeToLiveCalculator = timeToLiveCalculator;
	}

	public int getIndexRemoveRetryCount() {
		return indexRemoveRetryCount;
	}

	public void setIndexRemoveRetryCount(int indexRemoveRetryCount) {
		this.indexRemoveRetryCount = indexRemoveRetryCount;
	}

	public long getIndexRemoveRetryInterval() {
		return indexRemoveRetryInterval;
	}

	public void setIndexRemoveRetryInterval(long indexRemoveRetryInterval) {
		this.indexRemoveRetryInterval = indexRemoveRetryInterval;
	}

	public String getIndexCacheConfigrationName() {
		return indexCacheConfigrationName;
	}

	public void setIndexCacheConfigrationName(String indexCacheConfigrationName) {
		this.indexCacheConfigrationName = indexCacheConfigrationName;
	}

	public String getCacheConfigrationName() {
		return cacheConfigrationName;
	}

	public void setCacheConfigrationName(String cacheConfigrationName) {
		this.cacheConfigrationName = cacheConfigrationName;
	}

	public boolean isCreateOnStartup() {
		return createOnStartup;
	}

	public void setCreateOnStartup(boolean createOnStartup) {
		this.createOnStartup = createOnStartup;
	}

	@Override
	public void destroyed() {
		if (cm != null) {
			cm.stop();
			cm = null;
		}
		synchronized (this) {
			if (forCacheHandlerExecutorService != null) {
				forCacheHandlerExecutorService.shutdown();
				forCacheHandlerExecutorService = null;
			}
		}
	}

	@Override
	public CacheStore createCacheStore(String namespace) {
		if (sharedInstance == null) {
			return createCacheStoreInternal(namespace);
		} else {
			return sharedInstance;
		}
	}


	private CacheStore createCacheStoreInternal(String namespace) {
		if (getIndexCount() == 0) {
			return new InfinispanCacheStore(this, namespace, cacheConfigrationName);
		} else {
			InfinispanCacheStore realStore = new InfinispanCacheStore(this, namespace, cacheConfigrationName);
			if (indexCacheConfigrationName != null) {
				Configuration cc = cm.getCacheConfiguration(indexCacheConfigrationName);
				cm.defineConfiguration(namespace + INDEX_STORE_POSTFIX, cc);
			}
			Cache<IndexKey, IndexEntry> indexStore = cm.getCache(namespace + INDEX_STORE_POSTFIX);

			switch (realStore.cache.getCacheConfiguration().clustering().cacheMode()) {
			case LOCAL:
				return new InfinispanIndexedCacheStore(realStore, indexStore, getIndexCount(), indexRemoveRetryCount, indexRemoveRetryInterval * 1000 * 1000, false);
			case REPL_ASYNC:
			case REPL_SYNC:
				return new InfinispanIndexedCacheStore(realStore, indexStore, getIndexCount(), indexRemoveRetryCount, indexRemoveRetryInterval * 1000 * 1000, false);
			case INVALIDATION_ASYNC:
			case INVALIDATION_SYNC:
				return new InfinispanIndexedCacheStore(realStore, indexStore, getIndexCount(), indexRemoveRetryCount, indexRemoveRetryInterval * 1000 * 1000, true);
			case DIST_ASYNC:
			case DIST_SYNC:
				return new InfinispanIndexedDistributedCacheStore(realStore, indexStore, getIndexCount(), indexRemoveRetryCount, indexRemoveRetryInterval * 1000 * 1000);
			default:
				throw new IllegalStateException("no support of " + realStore.cache.getCacheConfiguration().clustering().cacheMode() + " mode of infinispan config");
			}
		}
	}

	@Override
	public boolean canUseForLocalCache() {
		return false;
	}

	@Override
	public boolean supportsIndex() {
		return true;
	}

	@Override
	public CacheHandler createCacheHandler(CacheStore store) {
		synchronized (this) {
			if (forCacheHandlerExecutorService == null) {
				forCacheHandlerExecutorService = Executors.newCachedThreadPool();
			}
		}
		return new InfinispanCacheHandler(((InfinispanCacheStore) store).cache, forCacheHandlerExecutorService);
	}

	public static class InfinispanCacheStore implements CacheStore {
		private final InfinispanCacheStoreFactory factory;
		private final Cache<Object, CacheEntry> cache;
		private final TimeToLiveCalculator timeToLiveCalculator;

		private List<CacheEventListener> listeners = new CopyOnWriteArrayList<CacheEventListener>();

		public InfinispanCacheStore(InfinispanCacheStoreFactory factory, String namespace, String cacheConfigName) {
			this.factory = factory;
			if (cacheConfigName != null) {
				Configuration cc = factory.cm.getCacheConfiguration(cacheConfigName);
				factory.cm.defineConfiguration(namespace, cc);
			}
			cache = factory.cm.getCache(namespace);
			cache.addListener(new InfinispanCacheListener(listeners));
			timeToLiveCalculator = factory.getTimeToLiveCalculator();
		}

		@Override
		public String getNamespace() {
			return cache.getName();
		}

		@Override
		public CacheStoreFactory getFactory() {
			return factory;
		}

		@Override
		public CacheEntry put(CacheEntry entry, boolean clean) {
			if (timeToLiveCalculator != null) {
				timeToLiveCalculator.set(entry);
			}

			if (clean) {
				if (entry.getTimeToLive() == null) {
					cache.putForExternalRead(entry.getKey(), entry);
				} else {
					cache.putForExternalRead(entry.getKey(), entry, entry.getTimeToLive(), TimeUnit.MILLISECONDS);
				}
				return null;
			} else {
				if (entry.getTimeToLive() == null) {
					return cache.put(entry.getKey(), entry);
				} else {
					return cache.put(entry.getKey(), entry, entry.getTimeToLive(), TimeUnit.MILLISECONDS);
				}
			}

		}

		@Override
		public CacheEntry putIfAbsent(CacheEntry entry) {
			if (timeToLiveCalculator != null) {
				timeToLiveCalculator.set(entry);
			}

			if (entry.getTimeToLive() == null) {
				return cache.putIfAbsent(entry.getKey(), entry);
			} else {
				return cache.putIfAbsent(entry.getKey(), entry, entry.getTimeToLive(), TimeUnit.MILLISECONDS);
			}
		}

		@Override
		public CacheEntry computeIfAbsent(Object key, Function<Object, CacheEntry> mappingFunction) {
			//ttlはmappingFunction実行しないと決められないので、有効期限チェックはgetのタイミングとする
			return cache.computeIfAbsent(key, new MappingFunctionWithTTL(mappingFunction, timeToLiveCalculator));
		}

		static class MappingFunctionWithTTL implements SerializableFunction<Object, CacheEntry> {
			private static final long serialVersionUID = 6304426240300516685L;

			Function<Object, CacheEntry> mappingFunction;
			TimeToLiveCalculator timeToLiveCalculator;

			MappingFunctionWithTTL(Function<Object, CacheEntry> mappingFunction, TimeToLiveCalculator timeToLiveCalculator) {
				this.mappingFunction = mappingFunction;
				this.timeToLiveCalculator = timeToLiveCalculator;
			}

			@Override
			public CacheEntry apply(Object k) {
				CacheEntry e = mappingFunction.apply(k);
				if (e != null && timeToLiveCalculator != null) {
					timeToLiveCalculator.set(e);
				}
				return e;
			}

		}

		@Override
		public CacheEntry compute(Object key, BiFunction<Object, CacheEntry, CacheEntry> remappingFunction) {
			//ttlはremappingFunction実行しないと決められないので、有効期限チェックはgetのタイミングとする
			return cache.compute(key, new RemappingFunctionWithTTL(remappingFunction, timeToLiveCalculator));
		}

		static class RemappingFunctionWithTTL implements SerializableBiFunction<Object, CacheEntry, CacheEntry> {
			private static final long serialVersionUID = -5435097223745759694L;

			BiFunction<Object, CacheEntry, CacheEntry> remappingFunction;
			TimeToLiveCalculator timeToLiveCalculator;

			RemappingFunctionWithTTL(BiFunction<Object, CacheEntry, CacheEntry> remappingFunction, TimeToLiveCalculator timeToLiveCalculator) {
				this.remappingFunction = remappingFunction;
				this.timeToLiveCalculator = timeToLiveCalculator;
			}

			@Override
			public CacheEntry apply(Object k, CacheEntry v) {
				CacheEntry e = remappingFunction.apply(k, v);
				if (e != null && e != v && timeToLiveCalculator != null) {
					timeToLiveCalculator.set(e);
				}
				return e;
			}

		}

		static boolean isStillAliveOrNull(CacheEntry e) {
			if (e == null) {
				return true;
			}
			if (e.getTimeToLive() == null) {
				return true;
			}
			return System.currentTimeMillis() < e.getExpirationTime();
		}

		@Override
		public CacheEntry get(Object key) {
			CacheEntry e = cache.get(key);
			if (!isStillAliveOrNull(e)) {
				remove(e);
				return null;
			}
			return e;
		}

		@Override
		public CacheEntry remove(Object key) {
			return cache.remove(key);
		}

		@Override
		public boolean remove(CacheEntry entry) {
			return cache.remove(entry.getKey(), entry);
		}

		@Override
		public CacheEntry replace(CacheEntry entry) {
			if (timeToLiveCalculator != null) {
				timeToLiveCalculator.set(entry);
			}
			if (entry.getTimeToLive() == null) {
				return cache.replace(entry.getKey(), entry);
			} else {
				return cache.replace(entry.getKey(), entry, entry.getTimeToLive(), TimeUnit.MILLISECONDS);
			}
		}

		@Override
		public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
			if (!oldEntry.getKey().equals(newEntry.getKey())) {
				throw new IllegalArgumentException("oldEntry key not equals newEntryKey");
			}
			if (timeToLiveCalculator != null) {
				timeToLiveCalculator.set(newEntry);
			}
			if (newEntry.getTimeToLive() == null) {
				return cache.replace(oldEntry.getKey(), oldEntry, newEntry);
			} else {
				return cache.replace(oldEntry.getKey(), oldEntry, newEntry, newEntry.getTimeToLive(), TimeUnit.MILLISECONDS);
			}
		}

		@Override
		public void removeAll() {
			cache.clear();
		}

		@Override
		public List<Object> keySet() {
			return new ArrayList<>(cache.keySet());
		}

		@Override
		public CacheEntry getByIndex(int indexKey, Object indexValue) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void addCacheEventListenner(CacheEventListener listener) {
			listeners.add(listener);
		}

		@Override
		public void removeCacheEventListenner(CacheEventListener listener) {
			listeners.remove(listener);
		}

		@Override
		public List<CacheEventListener> getListeners() {
			return listeners;
		}

		@Override
		public int getSize() {
			return cache.size();
		}

		@Override
		public String trace() {
			return cache.toString();
		}

		@Override
		public void destroy() {
			cache.stop();
		}

		protected Cache<Object, CacheEntry> getCache() {
			return cache;
		}
	}

	@Override
	public CacheStoreFactory getLowerLevel() {
		return null;
	}

}
