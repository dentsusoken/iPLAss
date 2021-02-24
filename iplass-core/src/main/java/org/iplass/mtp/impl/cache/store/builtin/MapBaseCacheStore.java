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

package org.iplass.mtp.impl.cache.store.builtin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;

public final class MapBaseCacheStore extends SimpleCacheStoreBase {

	private Map<Object, CacheEntry> cache;
	private HashMap<Object, Object>[] indexCache;
	private int indexCount;
	private long timeToLive;

	@SuppressWarnings("unchecked")
	public MapBaseCacheStore(String namespace, Map<Object, CacheEntry> cache, int indexCount, long timeToLive, CacheStoreFactory factory) {
		super(namespace, false, factory);
		this.indexCount = indexCount;
		this.timeToLive = timeToLive;
		this.cache = cache;
		if (indexCount > 0) {
			indexCache = new HashMap[indexCount];
			for (int i = 0; i < indexCache.length; i++) {
				indexCache[i] = new HashMap<Object, Object>();
			}
		}
	}

	private boolean isStillAliveOrNull(CacheEntry e) {
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
		CacheEntry e = cache.get(key);
		if (!isStillAliveOrNull(e)) {
			remove(e);
			return null;
		}
		return e;
	}

	protected void addToIndex(CacheEntry entry) {
		for (int i = 0; i < indexCount; i++) {
			Object ikey = entry.getIndexValue(i);
			Object key = entry.getKey();
			if (ikey instanceof Object[]) {
				Object[] ikeyArray = (Object[]) ikey;
				for (int j = 0; j < ikeyArray.length; j++) {
					if (ikeyArray[j] != null) {
						Object ival = margeVal(indexCache[i].get(ikeyArray[j]), key);
						indexCache[i].put(ikeyArray[j], ival);
					}
				}
			} else {
				if (ikey != null) {
					Object ival = margeVal(indexCache[i].get(ikey), key);
					indexCache[i].put(ikey, ival);
				}
			}
		}
	}

	protected void removeFromIndex(CacheEntry entry, boolean loose) {
		for (int i = 0; i < indexCount; i++) {
			Object ikey = entry.getIndexValue(i);
			if (ikey instanceof Object[]) {
				Object[] ikeyArray = (Object[]) ikey;
				for (int j = 0; j < ikeyArray.length; j++) {
					if (ikeyArray[j] != null) {
						Object ival = subtractVal(indexCache[i].get(ikeyArray[j]), entry.getKey(), loose);
						indexCache[i].put(ikeyArray[j], ival);
					}
				}
			} else {
				if (ikey != null) {
					Object ival = subtractVal(indexCache[i].get(ikey), entry.getKey(), loose);
					indexCache[i].put(ikey, ival);
				}
			}
		}
	}

	@Override
	public CacheEntry put(CacheEntry entry, boolean isClean) {
		CacheEntry previous = cache.put(entry.getKey(), entry);
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
	}

	@Override
	public CacheEntry remove(Object key) {
		if (key == null) {
			return null;
		}
		CacheEntry previous = cache.remove(key);
		if (previous != null) {
			removeFromIndex(previous, false);
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
			cache.clear();
			for (int i = 0; i < indexCount; i++) {
				indexCache[i].clear();
			}
		}
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		if (entry.getKey() == null) {
			//Indexのネガティブキャッシュ
			addToIndex(entry);
			return null;
		} else {
			if (!cache.containsKey(entry.getKey())) {
				CacheEntry previous = cache.put(entry.getKey(), entry);
				addToIndex(entry);
				notifyPut(entry);
				return previous;//nullのはず
			} else {
				return cache.get(entry.getKey());
			}
		}
	}

	@Override
	public boolean remove(CacheEntry entry) {
		if (cache.containsKey(entry.getKey()) && cache.get(entry.getKey()).equals(entry)) {
			cache.remove(entry.getKey());
			removeFromIndex(entry, false);
			notifyRemoved(entry);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		if (cache.containsKey(entry.getKey())) {
			CacheEntry oldValue = cache.put(entry.getKey(), entry);
			removeFromIndex(oldValue, false);
			addToIndex(entry);
			notifyUpdated(oldValue, entry);
			return oldValue;
		} else {
			return null;
		}
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		if (!oldEntry.getKey().equals(newEntry.getKey())) {
			throw new IllegalArgumentException("oldEntry key not equals newEntryKey");
		}
		if (cache.containsKey(newEntry.getKey()) && cache.get(newEntry.getKey()).equals(oldEntry)) {
			CacheEntry previous = cache.put(newEntry.getKey(), newEntry);
			removeFromIndex(previous, false);
			addToIndex(newEntry);
			notifyUpdated(previous, newEntry);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<Object> keySet() {
		return new ArrayList<Object>(cache.keySet());
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		Object key = indexCache[indexKey].get(indexValue);
		if (key == null) {
			return null;
		}
		if (key instanceof Object[]) {
			Object firstKey = ((Object[]) key)[0];
			return get(firstKey);
		}
		return get(key);
	}

	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		Object key = indexCache[indexKey].get(indexValue);
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

//	@Override
//	public void invalidate(CacheEntry entry) {
//		if (entry.getKey() == null) {
//			//Indexのネガティブキャッシュ
//			removeFromIndex(entry, true);
//		} else {
//			CacheEntry previous = cache.remove(entry.getKey());
//			if (previous != null) {
//				removeFromIndex(previous, false);
//				notifyInvalidated(previous);
//			}
//		}
//	}
//
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
		cache.remove(key);
	}
	
	@Override
	public Integer getSize() {
		return cache.size();
	}

	@Override
	public String trace() {
		StringBuilder builder = new StringBuilder();
		builder.append("-----------------------------------");
		builder.append("\nCacheStore Info");
		builder.append("\nCacheStore:" + this);
		builder.append(super.baseTrace());
		builder.append("\n\tindexCount:" + indexCount);
		builder.append("\n\ttimeToLive:" + timeToLive);
		builder.append("\n\tcache entry size:" + cache.size());
		for (Entry<Object, CacheEntry> entry : cache.entrySet()) {
			builder.append("\n\t\t" + entry.getKey() + "=" + entry.getValue());
		}
		if (indexCache != null) {
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
		} else {
			builder.append("\n\tindexCache :" + null);
		}
		builder.append("\n-----------------------------------");
		return builder.toString();
	}
	
	@Override
	public void destroy() {
		cache.clear();
		if (indexCache != null) {
			for (int i = 0; i < indexCache.length; i++) {
				indexCache[i] = null;
			}
		}
		cache = null;
	}

}
