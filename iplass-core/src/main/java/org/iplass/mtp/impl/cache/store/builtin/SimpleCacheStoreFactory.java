/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.cache.store.builtin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;


public class SimpleCacheStoreFactory extends AbstractBuiltinCacheStoreFactory {
	private static Logger logger = LoggerFactory.getLogger(SimpleCacheStoreFactory.class);

	private static long MIN_POLLING_INTERVAL = TimeUnit.MINUTES.toMillis(1);

	private int initialCapacity = 16;
	private float loadFactor = 0.75f;
	private int concurrencyLevel = 16;
	private long timeToLive = -1;
	private int size = -1;
	private boolean multiThreaded = true;
	private long evictionInterval = -1;
	private boolean fineGrainedLock;
	private List<FineGrainedLockIndexConfig> indexConfig;
	
	public List<FineGrainedLockIndexConfig> getIndexConfig() {
		return indexConfig;
	}

	public void setIndexConfig(List<FineGrainedLockIndexConfig> indexConfig) {
		this.indexConfig = indexConfig;
	}

	public boolean isFineGrainedLock() {
		return fineGrainedLock;
	}

	public void setFineGrainedLock(boolean fineGrainedLock) {
		this.fineGrainedLock = fineGrainedLock;
	}

	public long getEvictionInterval() {
		return evictionInterval;
	}

	public void setEvictionInterval(long evictionInterval) {
		this.evictionInterval = evictionInterval;
	}

	public boolean isMultiThreaded() {
		return multiThreaded;
	}

	public void setMultiThreaded(boolean multiThreaded) {
		this.multiThreaded = multiThreaded;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public int getInitialCapacity() {
		return initialCapacity;
	}

	public void setInitialCapacity(int initialCapacity) {
		this.initialCapacity = initialCapacity;
	}

	public float getLoadFactor() {
		return loadFactor;
	}

	public void setLoadFactor(float loadFactor) {
		this.loadFactor = loadFactor;
	}

	public int getConcurrencyLevel() {
		return concurrencyLevel;
	}

	public void setConcurrencyLevel(int concurrencyLevel) {
		this.concurrencyLevel = concurrencyLevel;
	}

	private long pollingInterval() {
		if (evictionInterval > 0) {
			return evictionInterval;
		} else if (timeToLive > 0) {
			long ei = timeToLive / 2;
			if (ei < MIN_POLLING_INTERVAL) {
				return MIN_POLLING_INTERVAL;
			}
			return ei;
		}

		return -1;
	}


	@Override
	public CacheStore createCacheStore(String namespace) {
		SimpleCacheStoreBase ret;
		if (isMultiThreaded()) {
			if (getIndexCount() == 0) {
				if (logger.isTraceEnabled()) {
					logger.trace("create ConcurrentHashMapCacheStore:namespace=" + namespace);
				}
				ret = new ConcurrentHashMapCacheStore(namespace, this, initialCapacity, loadFactor, concurrencyLevel, timeToLive, size);
			} else {
				if (fineGrainedLock) {
					if (logger.isTraceEnabled()) {
						logger.trace("create FineGrainedLockIndexedConcurrentHashMapCacheStore:namespace=" + namespace);
					}
					ret = new FineGrainedLockIndexedConcurrentHashMapCacheStore(namespace, this, initialCapacity, timeToLive, size, getIndexCount(), indexConfig);
					
				} else {
					if (logger.isTraceEnabled()) {
						logger.trace("create IndexedConcurrentHashMapCacheStore:namespace=" + namespace);
					}
					ret = new IndexedConcurrentHashMapCacheStore(namespace, this, initialCapacity, loadFactor, concurrencyLevel, timeToLive, size, getIndexCount());
				}
			}
		} else {
			if (size > 0) {
				if (logger.isTraceEnabled()) {
					logger.trace("create MapBaseCacheStore(LRUMap):namespace=" + namespace);
				}
				LRUMap lruMap = new LRUMap(getInitialCapacity(), getLoadFactor(), size);
				MapBaseCacheStore store = new MapBaseCacheStore(namespace, lruMap, getIndexCount(), timeToLive, this);
				lruMap.setStore(store);
				ret = store;
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("create MapBaseCacheStore(HashMap):namespace=" + namespace);
				}
				ret = new MapBaseCacheStore(namespace, new HashMap<Object, CacheEntry>(initialCapacity, loadFactor), getIndexCount(), timeToLive, this);
			}
		}

		if (timeToLive > 0) {
			CacheEntryCleaner.getInstance().register(ret, pollingInterval());
		}
		return ret;
	}

	@Override
	public boolean canUseForLocalCache() {
		return true;
	}

	@Override
	public boolean supportsIndex() {
		return true;
	}

	static boolean isStillAliveOrNull(CacheEntry e, long timeToLive) {
		if (e == null) {
			return true;
		}
		if (timeToLive <= 0) {
			return true;
		}
		if (System.currentTimeMillis() - e.getCreationTime() > timeToLive) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Index利用しない場合のマルチスレッド対応されたCacheStore。
	 * 
	 */
	public static class ConcurrentHashMapCacheStore extends SimpleCacheStoreBase {

		private final Cache<Object, CacheEntry> cache;
		private long timeToLive;

		public ConcurrentHashMapCacheStore(String namespace, CacheStoreFactory factory, int initialCapacity,
                float loadFactor, int concurrencyLevel, long timeToLive, int size) {
			super(namespace, true, factory);
			this.timeToLive = timeToLive;
			if (size > 0) {
				cache = Caffeine.newBuilder()
						.maximumSize(size)
						.initialCapacity(initialCapacity)
						.removalListener((key, value, cause) -> {
							if (cause.wasEvicted()) {
								notifyRemoved((CacheEntry) value);
							}
						})
						.build();
			} else {
				cache = Caffeine.newBuilder().initialCapacity(initialCapacity).build();
			}
		}

		@Override
		protected void removeInvalidEntry() {
			List<Object> keys = keySet();
			if (keys != null) {
				for (Object k: keys) {
					//getの中で、チェックしてるので
					get(k);
				}
			}
		}

		@Override
		public CacheEntry get(Object key) {
			if (key == null) {
				return null;
			}
			CacheEntry e = cache.getIfPresent(key);
			if (!isStillAliveOrNull(e, timeToLive)) {
				remove(e);
				return null;
			}
			return e;
		}

		@Override
		public CacheEntry put(CacheEntry entry, boolean isClean) {
			CacheEntry previous = cache.asMap().put(entry.getKey(), entry);
			if (previous == null) {
				notifyPut(entry);
			} else {
				notifyUpdated(previous, entry);
			}
			return previous;
		}

		@Override
		public CacheEntry remove(Object key) {
			CacheEntry previous = cache.asMap().remove(key);
			if (previous != null) {
				notifyRemoved(previous);
			}
			return previous;
		}

		@Override
		public void removeAll() {
			if (hasListener()) {
				for (Object k: keySet()) {
					remove(k);
				}
			} else {
				cache.asMap().clear();
			}
		}

		@Override
		public CacheEntry putIfAbsent(CacheEntry entry) {
			CacheEntry putted = cache.asMap().putIfAbsent(entry.getKey(), entry);
			if (putted == null) {
				notifyPut(entry);
			}
			return putted;
		}

		/**
		 * mappingFunctionはkeyが紐づいていない際に、厳密に一度のみの呼び出しとなる。
		 * 
		 */
		@Override
		public CacheEntry computeIfAbsent(Object key, Function<Object, CacheEntry> mappingFunction) {
			boolean[] computed = new boolean[1];
			CacheEntry entry = cache.asMap().computeIfAbsent(key, k -> {
				computed[0] = true;
				return mappingFunction.apply(k);
			});
			
			if (computed[0] && entry != null) {
				notifyPut(entry);
			}
			return entry;
		}

		/**
		 * remappingFunctionは同一keyに対しての処理は他の競合スレッドはブロックされ、アトミックに処理される。
		 */
		@Override
		public CacheEntry compute(Object key, BiFunction<Object, CacheEntry, CacheEntry> remappingFunction) {
			CacheEntry[] old = new CacheEntry[1];
			CacheEntry entry = cache.asMap().compute(key, (k, v) -> {
				old[0] = v;
				return remappingFunction.apply(k, v);
			});
			
			if (entry != null) {
				//put
				if (old[0] == null) {
					notifyPut(entry);
				} else {
					notifyUpdated(old[0], entry);
				}
			} else {
				//remove
				if (old[0] != null) {
					notifyRemoved(entry);
				}
			}
			return entry;
		}

		@Override
		public boolean remove(CacheEntry entry) {
			boolean res = cache.asMap().remove(entry.getKey(), entry);
			if (res) {
				notifyRemoved(entry);
			}
			return res;
		}

		@Override
		public CacheEntry replace(CacheEntry entry) {
			CacheEntry previous = cache.asMap().replace(entry.getKey(), entry);
			if (previous != null) {
				notifyUpdated(previous, entry);
			}
			return previous;
		}

		@Override
		public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
			if (!oldEntry.getKey().equals(newEntry.getKey())) {
				throw new IllegalArgumentException("oldEntry key not equals newEntryKey");
			}
			boolean res = cache.asMap().replace(newEntry.getKey(), oldEntry, newEntry);
			if (res) {
				notifyUpdated(oldEntry, newEntry);
			}
			return res;
		}

		@Override
		public List<Object> keySet() {
			return new ArrayList<Object>(cache.asMap().keySet());
		}

		@Override
		public CacheEntry getByIndex(int indexKey, Object indexValue) {
			return null;
		}

		@Override
		public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
			return Collections.emptyList();
		}

//		@Override
//		public void invalidate(CacheEntry entry) {
//			CacheEntry previous = cache.remove(entry.getKey());
//			if (previous != null) {
//				notifyRemoved(previous);
//			}
//		}

		@Override
		public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
			List<CacheEntry> list = getListByIndex(indexKey, indexValue);
			if (list != null) {
				for (CacheEntry e: list) {
					remove(e.getKey());
				}
			}
			return list;
		}

		@Override
		protected void removeNullEntry(NullKey key) {
			cache.asMap().remove(key);
		}
		
		@Override
		public int getSize() {
			return (int) cache.estimatedSize();
		}

		@Override
		public String trace() {
			StringBuilder builder = new StringBuilder();
			builder.append("-----------------------------------");
			builder.append("\nCacheStore Info");
			builder.append("\nCacheStore:" + this);
			builder.append(super.baseTrace());
			ConcurrentMap<Object, CacheEntry> map = cache.asMap();
			builder.append("\n\tcache entry size:" + map.size());
			for (Entry<Object, CacheEntry> entry : map.entrySet()) {
				builder.append("\n\t\t" + entry.getKey() + "=" + entry.getValue());
			}
			builder.append("\n-----------------------------------");
			return builder.toString();
		}

		@Override
		public void destroy() {
			cache.asMap().clear();
			cache.invalidateAll();
			cache.cleanUp();
		}
	}


	/**
	 * キャッシュデータの更新頻度が低い場合に最適なIndex付きのConcurrentHashMapCacheStoreの実装。
	 * 
	 */
	public static class IndexedConcurrentHashMapCacheStore extends SimpleCacheStoreBase {
		
		private final ReentrantReadWriteLock indexLock = new ReentrantReadWriteLock();

		private final Cache<Object, CacheEntry> cache;
		private long timeToLive;
		private final HashMap<Object, Object>[] indexCache;

		public IndexedConcurrentHashMapCacheStore(String namespace, CacheStoreFactory factory, int initialCapacity,
                float loadFactor, int concurrencyLevel, long timeToLive, int size, int indexCount) {
			super(namespace, true, factory);
			this.timeToLive = timeToLive;

			if (size > 0) {
				cache = Caffeine.newBuilder()
						.maximumSize(size)
						.initialCapacity(initialCapacity)
						.removalListener((key, value, cause) -> {
							if (cause.wasEvicted()) {
								removeFromIndex((CacheEntry) value, false);
								notifyRemoved((CacheEntry) value);
							}
						})
						.build();
			} else {
				cache = Caffeine.newBuilder().initialCapacity(initialCapacity).build();
			}

			indexCache = newHashMapArray(indexCount);
			for (int i = 0; i < indexCache.length; i++) {
				indexCache[i] = new HashMap<Object, Object>();
			}
		}

		@SuppressWarnings("unchecked")
		private final HashMap<Object,Object>[] newHashMapArray(int size) {
			return new HashMap[size];
		}

		private void addToIndex(CacheEntry entry) {

//			ここら辺があやしい。。。

			for (int i = 0; i < indexCache.length; i++) {
				Object ikey = entry.getIndexValue(i);
				Object key = entry.getKey();
				if (ikey instanceof Object[]) {
					Object[] ikeyArray = (Object[]) ikey;
					for (int j = 0; j < ikeyArray.length; j++) {
						Object ival = margeVal(indexCache[i].get(ikeyArray[j]), key);
						indexCache[i].put(ikeyArray[j], ival);
					}
				} else {
					Object ival = margeVal(indexCache[i].get(ikey), key);
					indexCache[i].put(ikey, ival);
				}
			}
		}

		private void removeFromIndex(CacheEntry entry, boolean loose) {
			for (int i = 0; i < indexCache.length; i++) {
				Object ikey = entry.getIndexValue(i);
				if (ikey instanceof Object[]) {
					Object[] ikeyArray = (Object[]) ikey;
					for (int j = 0; j < ikeyArray.length; j++) {
						Object ival = subtractVal(indexCache[i].get(ikeyArray[j]), entry.getKey(), loose);
						indexCache[i].put(ikeyArray[j], ival);
					}
				} else {
					Object ival = subtractVal(indexCache[i].get(ikey), entry.getKey(), loose);
					indexCache[i].put(ikey, ival);
				}
			}
		}

		@Override
		protected void removeInvalidEntry() {
			List<Object> keys = keySet();
			if (keys != null) {
				for (Object k: keys) {
					//getの中で、チェックしてるので
					get(k);
				}
			}
		}
		@Override
		public CacheEntry get(Object key) {
			if (key == null) {
				return null;
			}
			CacheEntry e = cache.getIfPresent(key);
			if (!isStillAliveOrNull(e, timeToLive)) {
				if (logger.isDebugEnabled()) {
					logger.debug("invalidate " + e + ", cause timeToLive");
				}
				remove(e);
				return null;
			}
			return e;
		}

		@Override
		public CacheEntry put(CacheEntry entry, boolean isClean) {
			indexLock.writeLock().lock();
			try {
				CacheEntry previous = cache.asMap().put(entry.getKey(), entry);
				if (previous != null) {
					removeFromIndex(previous, false);
				}
				addToIndex(entry);
				if (previous == null) {
					notifyPut(entry);
				} else {
					notifyUpdated(previous, entry);
				}
				return previous;
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		@Override
		public CacheEntry remove(Object key) {
			indexLock.writeLock().lock();
			try {
				CacheEntry previous = cache.asMap().remove(key);
				if (previous != null) {
					removeFromIndex(previous, false);
					notifyRemoved(previous);
				}
				return previous;
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		@Override
		public void removeAll() {
			indexLock.writeLock().lock();
			try {
				if (hasListener()) {
					for (Object k: keySet()) {
						remove(k);
					}
				} else {
					cache.asMap().clear();
					for (int i = 0; i < indexCache.length; i++) {
						indexCache[i].clear();
					}
				}
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		@Override
		public CacheEntry putIfAbsent(CacheEntry entry) {
			indexLock.writeLock().lock();
			try {
				CacheEntry putted = cache.asMap().putIfAbsent(entry.getKey(), entry);
				if (putted == null) {
					addToIndex(entry);
					notifyPut(entry);
				}
				return putted;
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		/**
		 * mappingFunctionはkeyが紐づいていない際に、厳密に一度のみの呼び出しとなる。
		 * 
		 */
		@Override
		public CacheEntry computeIfAbsent(Object key, Function<Object, CacheEntry> mappingFunction) {
			indexLock.writeLock().lock();
			try {
				boolean[] computed = new boolean[1];
				CacheEntry entry = cache.asMap().computeIfAbsent(key, k -> {
					computed[0] = true;
					return mappingFunction.apply(k);
				});
				
				if (computed[0] && entry != null) {
					addToIndex(entry);
					notifyPut(entry);
				}
				return entry;
				
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		/**
		 * remappingFunctionは同一keyに対しての処理は他の競合スレッドはブロックされ、アトミックに処理される。
		 */
		@Override
		public CacheEntry compute(Object key, BiFunction<Object, CacheEntry, CacheEntry> remappingFunction) {
			indexLock.writeLock().lock();
			try {
				CacheEntry[] old = new CacheEntry[1];
				CacheEntry entry = cache.asMap().compute(key, (k, v) -> {
					old[0] = v;
					return remappingFunction.apply(k, v);
				});
				
				if (entry != null) {
					//put
					if (old[0] == null) {
						addToIndex(entry);
						notifyPut(entry);
					} else {
						//同一エントリの場合はindex更新しない
						if (old[0] != entry) {
							removeFromIndex(old[0], false);
							addToIndex(entry);
						}
						notifyUpdated(old[0], entry);
					}
				} else {
					//remove
					if (old[0] != null) {
						removeFromIndex(old[0], false);
						notifyRemoved(entry);
					}
				}
				return entry;
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		@Override
		public boolean remove(CacheEntry entry) {
			indexLock.writeLock().lock();
			try {
				boolean res = cache.asMap().remove(entry.getKey(), entry);
				if (res) {
					removeFromIndex(entry, false);
					notifyRemoved(entry);
				}
				return res;
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		@Override
		public CacheEntry replace(CacheEntry entry) {
			indexLock.writeLock().lock();
			try {
				CacheEntry previous = cache.asMap().replace(entry.getKey(), entry);
				if (previous != null) {
					removeFromIndex(previous, false);
					addToIndex(entry);
					notifyUpdated(previous, entry);
				}
				return previous;
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		@Override
		public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
			if (!oldEntry.getKey().equals(newEntry.getKey())) {
				throw new IllegalArgumentException("oldEntry key not equals newEntryKey");
			}
			indexLock.writeLock().lock();
			try {
				boolean res = cache.asMap().replace(newEntry.getKey(), oldEntry, newEntry);
				if (res) {
					removeFromIndex(oldEntry, false);
					addToIndex(newEntry);
					notifyUpdated(oldEntry, newEntry);
				}
				return res;
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		@Override
		public List<Object> keySet() {
			return new ArrayList<Object>(cache.asMap().keySet());
		}

		@Override
		public CacheEntry getByIndex(int indexKey, Object indexValue) {
			Object key = null;
			indexLock.readLock().lock();
			try {
				key = indexCache[indexKey].get(indexValue);
			} finally {
				indexLock.readLock().unlock();
			}
			if (key == null) {
				return null;
			}

			if (key instanceof Object[]) {
				return get(((Object[]) key)[0]);
			}
			return get(key);
		}

		@Override
		public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
			Object key = null;
			indexLock.readLock().lock();
			try {
				key = indexCache[indexKey].get(indexValue);
			} finally {
				indexLock.readLock().unlock();
			}
			if (key == null) {
				return Collections.emptyList();
			}
			if (key instanceof Object[]) {
				Object[] keyArray = (Object[]) key;
				ArrayList<CacheEntry> result = new ArrayList<CacheEntry>(keyArray.length);
				for (int i = 0; i < keyArray.length; i++) {
					CacheEntry entry = get(keyArray[i]);
					if (entry != null) {
						result.add(entry);
					}
				}
				return result;
			}
			CacheEntry entry = get(key);
			if (entry == null) {
				return Collections.emptyList();
			}
			return Arrays.asList(entry);
		}

//		@Override
//		public void invalidate(CacheEntry entry) {
//			indexLock.writeLock().lock();
//			try {
//				if (entry.getKey() == null) {
//					//Indexのネガティブキャッシュ
//					removeFromIndex(entry, true);
//				} else {
//					CacheEntry previous = cache.remove(entry.getKey());
//					if (previous != null) {
//						removeFromIndex(previous, false);
//						notifyRemoved(previous);
//					}
//				}
//			} finally {
//				indexLock.writeLock().unlock();
//			}
//
//		}

		@Override
		public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
			indexLock.writeLock().lock();
			try {
				List<CacheEntry> list = getListByIndex(indexKey, indexValue);
				if (list != null) {
					for (CacheEntry e: list) {
						remove(e.getKey());
					}
				}
				return list;
			} finally {
				indexLock.writeLock().unlock();
			}
		}

		@Override
		protected void removeNullEntry(NullKey key) {
			cache.asMap().remove(key);
		}
		
		@Override
		public int getSize() {
			return (int) cache.estimatedSize();
		}

		public String trace() {
			StringBuilder builder = new StringBuilder();
			builder.append("-----------------------------------");
			builder.append("\nCacheStore Info");
			builder.append("\nCacheStore:" + this);
			builder.append(super.baseTrace());
			ConcurrentMap<Object, CacheEntry> map = cache.asMap();
			builder.append("\n\tcache entry size:" + map.size());
			for (Entry<Object, CacheEntry> entry : map.entrySet()) {
				builder.append("\n\t\t" + entry.getKey() + "=" + entry.getValue());
			}
			builder.append("\n\tindexCache size:" + indexCache.length);
			for (int i = 0; i < indexCache.length; i++) {
				builder.append("\n\tindexCache[" + i + "] entry size:" + indexCache[i].size());
				for (Entry<Object, Object> entry : indexCache[i].entrySet()) {
					if (entry.getValue() instanceof Object[]) {
						builder.append("\n\t\t" + entry.getKey() + "=" + Arrays.toString((Object[])entry.getValue()));
					} else {
						builder.append("\n\t\t" + entry.getKey() + "=" + entry.getValue());
					}
				}
			}
			builder.append("\n-----------------------------------");
			return builder.toString();
		}

		@Override
		public void destroy() {
			indexLock.writeLock().lock();
			try {
				cache.asMap().clear();
				cache.invalidateAll();
				cache.cleanUp();
				for (int i = 0; i < indexCache.length; i++) {
					indexCache[i] = null;
				}
			} finally {
				indexLock.writeLock().unlock();
			}

		}
	}

	@Override
	public CacheHandler createCacheHandler(CacheStore store) {
		return new SimpleLocalCacheHandler(store, getConcurrencyLevelOfCacheHandler());
	}

	@Override
	public CacheStoreFactory getLowerLevel() {
		return null;
	}
}
