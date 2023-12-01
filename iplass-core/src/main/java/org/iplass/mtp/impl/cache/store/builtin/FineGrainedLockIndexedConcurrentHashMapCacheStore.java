/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.TimeToLiveCalculator;
import org.iplass.mtp.impl.cache.store.builtin.FineGrainedLockIndex.IndexValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * CacheEntryの更新処理の並列性を向上するため、 細粒度にロックを制御しつつCache本体とIndexの更新の整合性をとるCacheStore。
 * Cache本体とIndexの更新処理はAtomicであるが、 IndexedConcurrentHashMapCacheStoreと異なり、
 * 一括更新操作系（removeAll、removeByIndex）は呼び出し時点でロックをとらず取得したリストに対しての操作になり、
 * 厳密にはシリアライズされず別スレッドで並列にCacheが更新されることはある。
 * 
 * 比較的高頻度のCacheEntryの更新があるような場合の利用を想定。
 * 
 */
public class FineGrainedLockIndexedConcurrentHashMapCacheStore extends SimpleCacheStoreBase {
	private static Logger logger = LoggerFactory.getLogger(FineGrainedLockIndexedConcurrentHashMapCacheStore.class);

	private final Cache<Object, CacheEntry> cache;
	private TimeToLiveCalculator timeToLiveCalculator;
	private final FineGrainedLockIndex[] indexCache;

	public FineGrainedLockIndexedConcurrentHashMapCacheStore(String namespace, CacheStoreFactory factory,
			int initialCapacity, TimeToLiveCalculator timeToLiveCalculator, int size, int indexCount, List<FineGrainedLockIndexConfig> indexConfig) {
		super(namespace, true, factory);
		this.timeToLiveCalculator = timeToLiveCalculator;

		if (size > 0) {
			cache = Caffeine.newBuilder().maximumSize(size).initialCapacity(initialCapacity)
					.removalListener((key, value, cause) -> {
						if (cause.wasEvicted()) {
							removeFromIndex((CacheEntry) value, false);
							notifyRemoved((CacheEntry) value);
						}
					}).build();
		} else {
			cache = Caffeine.newBuilder().initialCapacity(initialCapacity).build();
		}

		indexCache = new FineGrainedLockIndex[indexCount];
		for (int i = 0; i < indexCache.length; i++) {
			if (indexConfig != null && i < indexConfig.size()) {
				indexCache[i] = indexConfig.get(i).build();
			} else {
				indexCache[i] = new FineGrainedLockIndexConfig().build();
			}
		}
	}

	private void removeFromIndex(CacheEntry entry, boolean loose) {
		try (FineGrainedLockState fgls = new FineGrainedLockState(null, entry, indexCache)) {
			fgls.maintain();
		}
	}

	@Override
	protected void removeInvalidEntry() {
		List<Object> keys = keySet();
		if (keys != null) {
			for (Object k : keys) {
				// getの中で、チェックしてるので
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
		if (!SimpleCacheStoreFactory.isStillAliveOrNull(e)) {
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
		timeToLiveCalculator.set(entry);
		
		if (entry.getKey() instanceof NullKey) {
			//NullKeyの場合は本体には格納しない
			try (FineGrainedLockState fgls = new FineGrainedLockState(entry, null, indexCache)) {
				fgls.maintain();
			}
			return null;
		} else {
			CacheEntry[] previous = new CacheEntry[1];
			CacheEntry putted = cache.asMap().compute(entry.getKey(), (k, v) -> {
				previous[0] = v;
				try (FineGrainedLockState fgls = new FineGrainedLockState(entry, v, indexCache)) {
					fgls.maintain();
				}
				return entry;
			});
			
			if (previous[0] == null) {
				notifyPut(entry);
			} else {
				notifyUpdated(previous[0], putted);
			}
			return previous[0];
		}
	}

	@Override
	public CacheEntry remove(Object key) {
		CacheEntry[] previous = new CacheEntry[1];
		cache.asMap().compute(key, (k, v) -> {
			previous[0] = v;
			if (v != null) {
				try (FineGrainedLockState fgls = new FineGrainedLockState(null, v, indexCache)) {
					fgls.maintain();
				}
			}
			return null;
		});
		
		if (previous[0] != null) {
			notifyRemoved(previous[0]);
		}
		return previous[0];
	}

	@Override
	public void removeAll() {
		for (Object k : keySet()) {
			remove(k);
		}
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		if (entry.getKey() instanceof NullKey) {
			throw new UnsupportedOperationException("PutIfAbsent with Null Entry(NullKey) is Unsupported.");
		}
		
		timeToLiveCalculator.set(entry);
		CacheEntry[] previous = new CacheEntry[1];
		cache.asMap().compute(entry.getKey(), (k, v) -> {
			previous[0] = v;
			
			if (v == null) {
				try (FineGrainedLockState fgls = new FineGrainedLockState(entry, v, indexCache)) {
					fgls.maintain();
				}
				return entry;
			} else {
				return v;
			}
		});
		
		if (previous[0] == null) {
			notifyPut(entry);
		}
		return previous[0];
	}

	/**
	 * mappingFunctionはkeyが紐づいていない際に、厳密に一度のみの呼び出しとなる。
	 * 
	 */
	@Override
	public CacheEntry computeIfAbsent(Object key, Function<Object, CacheEntry> mappingFunction) {
		if (key instanceof NullKey) {
			throw new UnsupportedOperationException("computeIfAbsent with Null Entry(NullKey) is Unsupported.");
		}
		
		boolean[] computed = new boolean[1];
		CacheEntry entry = cache.asMap().computeIfAbsent(key, k -> {
			computed[0] = true;
			CacheEntry v = mappingFunction.apply(k);
			if (v != null) {
				timeToLiveCalculator.set(v);
				
				try (FineGrainedLockState fgls = new FineGrainedLockState(v, null, indexCache)) {
					fgls.maintain();
				}
			}
			return v;
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
			CacheEntry newV = remappingFunction.apply(k, v);
			if (v != newV) {
				if (newV != null) {
					timeToLiveCalculator.set(newV);
				}
				try (FineGrainedLockState fgls = new FineGrainedLockState(newV, v, indexCache)) {
					fgls.maintain();
				}
			}
			return newV;
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
		CacheEntry[] previous = new CacheEntry[1];
		boolean[] res = new boolean[1];
		cache.asMap().computeIfPresent(entry.getKey(), (k, v) -> {
			previous[0] = v;
			if (entry.equals(v)) {
				try (FineGrainedLockState fgls = new FineGrainedLockState(null, v, indexCache)) {
					fgls.maintain();
				}
				res[0] = true;
				return null;
			} else {
				return v;
			}
		});
		
		if (res[0]) {
			notifyRemoved(previous[0]);
		}
		return res[0];
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		timeToLiveCalculator.set(entry);
		CacheEntry[] previous = new CacheEntry[1];
		cache.asMap().computeIfPresent(entry.getKey(), (k, v) -> {
			previous[0] = v;
			try (FineGrainedLockState fgls = new FineGrainedLockState(entry, v, indexCache)) {
				fgls.maintain();
			}
			return entry;
		});
		
		if (previous[0] != null) {
			notifyUpdated(previous[0], entry);
		}
		return previous[0];
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		if (!oldEntry.getKey().equals(newEntry.getKey())) {
			throw new IllegalArgumentException("oldEntry key not equals newEntryKey");
		}
		
		timeToLiveCalculator.set(newEntry);
		CacheEntry[] previous = new CacheEntry[1];
		boolean[] res = new boolean[1];
		cache.asMap().computeIfPresent(newEntry.getKey(), (k, v) -> {
			previous[0] = v;
			if (oldEntry.equals(v)) {
				try (FineGrainedLockState fgls = new FineGrainedLockState(newEntry, v, indexCache)) {
					fgls.maintain();
				}
				res[0] = true;
				return newEntry;
			} else {
				return v;
			}
		});
		
		if (res[0]) {
			notifyUpdated(previous[0], newEntry);
		}
		return res[0];
	}

	@Override
	public List<Object> keySet() {
		return new ArrayList<Object>(cache.asMap().keySet());
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		IndexValue iv = indexCache[indexKey].getIndexValue(indexValue, false);
		if (iv == null) {
			return null;
		}
		Object key = iv.firstRef();
		if (key == null) {
			return null;
		}
		if (key instanceof NullKey) {
			Object[] indexValues = new Object[indexCache.length];
			indexValues[indexKey] = indexValue;
			return new CacheEntry(key, null, Long.MIN_VALUE, indexValues);
		}
		
		return get(key);
	}

	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		IndexValue iv = indexCache[indexKey].getIndexValue(indexValue, false);
		if (iv == null) {
			return Collections.emptyList();
		}
		
		List<Object> keys = iv.refs();
		if (keys.size() == 0) {
			return Collections.emptyList();
		}
		
		if (keys.size() == 1 && keys.get(0) instanceof NullKey) {
			Object[] indexValues = new Object[indexCache.length];
			indexValues[indexKey] = indexValue;
			return Collections.singletonList(new CacheEntry(keys.get(0), null, Long.MIN_VALUE, indexValues));
		}
		
		ArrayList<CacheEntry> result = new ArrayList<CacheEntry>(keys.size());
		for (Object k: keys) {
			CacheEntry e = get(k);
			if (e != null) {
				result.add(e);
			}
		}
		return result;
	}

	@Override
	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
		IndexValue iv = indexCache[indexKey].getIndexValue(indexValue, false);
		if (iv == null) {
			return Collections.emptyList();
		}
		
		List<Object> keys = iv.refs();
		if (keys.size() == 0) {
			return Collections.emptyList();
		}
		
		ArrayList<CacheEntry> result = new ArrayList<CacheEntry>(keys.size());
		for (Object k: keys) {
			if (!(k instanceof NullKey)) {
				CacheEntry e = remove(k);
				if (e != null) {
					result.add(e);
				}
			}
		}
		return result;
	}

	@Override
	protected void removeNullEntry(NullKey key) {
		//この実装では本体にNullKeyは格納しない
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
		builder.append("\n-----------------------------------");
		return builder.toString();
	}

	@Override
	public void destroy() {
		cache.asMap().clear();
		cache.invalidateAll();
		cache.cleanUp();
		for (int i = 0; i < indexCache.length; i++) {
			indexCache[i].destroy();
		}
	}
}