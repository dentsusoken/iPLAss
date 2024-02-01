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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.infinispan.Cache;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheCreateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheRemoveEvent;
import org.iplass.mtp.impl.cache.store.event.CacheUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanIndexedCacheStore implements CacheStore {
	
	//TODO index更新に失敗した場合のリカバリ方法（バッチで別プロセスで修復か。。。）
	
	private static Logger logger = LoggerFactory.getLogger(InfinispanIndexedCacheStore.class);
	protected static Logger fatalLogger = LoggerFactory.getLogger("mtp.fatal.cache");
	
	protected final CacheStore wrapped;
	protected final Cache<IndexKey, IndexEntry> indexStore;
	protected final int indexSize;
	protected final int indexRemoveTryCount;
	protected final long indexRemoveRetryIntervalNanos;
	protected final boolean invalidationMode;
	
	public InfinispanIndexedCacheStore(CacheStore wrapped, Cache<IndexKey, IndexEntry> indexStore, int indexSize, int indexRemoveRetryCount, long indexRemoveRetryIntervalNanos, boolean invalidationMode) {
		this.wrapped = wrapped;
		this.indexStore = indexStore;
		this.indexSize = indexSize;
		this.indexRemoveTryCount = indexRemoveRetryCount + 1;
		this.indexRemoveRetryIntervalNanos = indexRemoveRetryIntervalNanos;
		this.invalidationMode = invalidationMode;
		if (invalidationMode) {
			wrapped.addCacheEventListenner(new CacheEventListener() {
				@Override
				public void updated(CacheUpdateEvent event) {
				}
				@Override
				public void removed(CacheRemoveEvent event) {
				}
				@Override
				public void created(CacheCreateEvent event) {
				}
				@Override
				public void invalidated(CacheInvalidateEvent event) {
					mainteIndex(event.getEntry(), null);
				}
			});
		}
		
	}
	
	@Override
	public String getNamespace() {
		return wrapped.getNamespace();
	}

	@Override
	public CacheStoreFactory getFactory() {
		return wrapped.getFactory();
	}

	@Override
	public CacheEntry put(CacheEntry entry, boolean clean) {
		//Indexの整合性保つため常にfalse
		CacheEntry previous = wrapped.put(entry, false);
		mainteIndex(previous, entry);
		return previous;
	}
	
	protected void mainteIndex(CacheEntry oldEntry, CacheEntry newEntry) {
		if (logger.isTraceEnabled()) {
			logger.trace("mainteIndex:old=" + oldEntry + ", new=" + newEntry);
		}
		
		for (int i = 0; i < indexSize; i++) {
			if (oldEntry != null) {
				CacheEntryRef oldRef = new CacheEntryRef(oldEntry.getKey(), oldEntry.getVersion());
				Object iVal = oldEntry.getIndexValue(i);
				if (iVal instanceof Object[]) {
					for (Object iv: (Object[]) iVal) {
						removeFromIndex(indexStore, new IndexKey(i, iv), oldRef, indexRemoveTryCount, indexRemoveRetryIntervalNanos);
					}
				} else {
					removeFromIndex(indexStore, new IndexKey(i, iVal), oldRef, indexRemoveTryCount, indexRemoveRetryIntervalNanos);
				}
			}
			if (newEntry != null) {
				CacheEntryRef newRef = new CacheEntryRef(newEntry.getKey(), newEntry.getVersion());
				Object iVal = newEntry.getIndexValue(i);
				if (iVal instanceof Object[]) {
					for (Object iv: (Object[]) iVal) {
						addToIndex(indexStore, new IndexKey(i, iv), newRef);
					}
				} else {
					addToIndex(indexStore, new IndexKey(i, iVal), newRef);
				}
			}
		}
	}
	
	
	protected static void addToIndex(Cache<IndexKey, IndexEntry> indexStore, IndexKey iKey, CacheEntryRef ref) {
		if (logger.isTraceEnabled()) {
			logger.trace("addToIndex:" + iKey + ", " + ref);
		}
		//CAS
		for (;;) {
			IndexEntry entry = indexStore.get(iKey);
			if (entry == null) {
				IndexEntry newEntry = new IndexEntry(ref);
				if (indexStore.putIfAbsent(iKey, newEntry) == null) {
					return;
				}
			} else {
				IndexEntry newEntry = entry.add(ref);
				if (indexStore.replace(iKey, entry, newEntry)) {
					return;
				}
			}
		}
		
	}
	
	protected static void removeFromIndex(Cache<IndexKey, IndexEntry> indexStore, IndexKey iKey, CacheEntryRef ref, int indexRemoveTryCount, long indexRemoveRetryIntervalNanos) {
		if (logger.isTraceEnabled()) {
			logger.trace("removeFromIndex:" + iKey + ", " + ref);
		}
		//CAS（リトライ回数付）
		for (int retry = 0; retry < indexRemoveTryCount; retry++) {
			IndexEntry entry = indexStore.get(iKey);
			if (entry == null) {
				//まだ、見えていない？？
				LockSupport.parkNanos(indexRemoveRetryIntervalNanos);
			} else {
				IndexEntry newEntry = entry.remove(ref);
				if (entry == newEntry) {
					//INDEXから削除できなかった。存在しない。まだ見えてない？？
					LockSupport.parkNanos(indexRemoveRetryIntervalNanos);
				} else {
					if (newEntry == null) {
						if (indexStore.remove(iKey, entry)) {
							return;
						}
					} else {
						if (indexStore.replace(iKey, entry, newEntry)) {
							return;
						}
					}
				}
			}
		}
		
		fatalLogger.error("cant remove entry from index cause retry counts over. maybe cache index is inconsistent state... IndexKey=" + iKey + ", CacheEntryRef=" + ref);
	}
	

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		CacheEntry previous = wrapped.putIfAbsent(entry);
		if (previous == null) {
			mainteIndex(null, entry);
		}
		return previous;
	}

	@Override
	public CacheEntry computeIfAbsent(Object key, Function<Object, CacheEntry> mappingFunction) {
		return wrapped.computeIfAbsent(key, k -> {
			CacheEntry computed = mappingFunction.apply(k);
			if (computed != null) {
				mainteIndex(null, computed);
			}
			return computed;
		});
	}

	@Override
	public CacheEntry compute(Object key, BiFunction<Object, CacheEntry, CacheEntry> remappingFunction) {
		return wrapped.compute(key, (k, v) -> {
			CacheEntry computed = remappingFunction.apply(k, v);
			mainteIndex(v, computed);
			return computed;
		});
	}

	@Override
	public CacheEntry get(Object key) {
		return wrapped.get(key);
	}

	@Override
	public CacheEntry remove(Object key) {
		CacheEntry removed = wrapped.remove(key);
		if (removed != null) {
			mainteIndex(removed, null);
		}
		return removed;
	}

	@Override
	public boolean remove(CacheEntry entry) {
		boolean removed = wrapped.remove(entry);
		if (removed) {
			mainteIndex(entry, null);
		}
		
		return removed;
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		CacheEntry previous = wrapped.replace(entry);
		if (previous != null) {
			mainteIndex(previous, entry);
		}
		return previous;
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		boolean replaced = wrapped.replace(oldEntry, newEntry);
		if (replaced) {
			mainteIndex(oldEntry, newEntry);
		}
		return replaced;
	}

	@Override
	public void removeAll() {
		removeAllLocal();
	}
	
	
	protected void removeAllLocal() {
		//ロックできないので、、
		List<Object> keySet = keySet();
		for (Object k: keySet) {
			remove(k);
		}
	}

	@Override
	public List<Object> keySet() {
		return wrapped.keySet();
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		IndexKey key = new IndexKey(indexKey, indexValue);
		IndexEntry indexEntry = indexStore.get(key);
		if (indexEntry == null) {
			return null;
		} else {
			for (int i = 0; i < indexEntry.getRefs().length; i++) {
				CacheEntryRef entryRef = indexEntry.getRefs()[i];
				CacheEntry ce = get(entryRef.getCacheEntryKey());
				if (ce != null && ce.getVersion() == entryRef.getCacheEntryVersion()) {
					return ce;
				}
				//不正な状態で残ってしまったentryRefもしくは、index更新中のEntry
				//TODO warnで出したいけど、大量にログがでてしまう。。。
				logger.debug("maybe memoryleak... cache index:(" + key + ", " + entryRef + ") has no cacheEntry, so research next entry cache:namespace=" + getNamespace());
				//TODO 本当はメモリリーク防ぐために消したいけど、消してよいかの判断がつかない（更新中の場合もある。。）
				//FIXME 別途バッチで、Indexの整合性をチェック＆修正するプロセスを走らす
			}
			return null;
		}
	}
	
	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		IndexKey key = new IndexKey(indexKey, indexValue);
		IndexEntry indexEntry = indexStore.get(key);
		if (indexEntry == null) {
			return Collections.emptyList();
		} else {
			ArrayList<CacheEntry> ret = new ArrayList<>();
			HashSet<Object> keySet = new HashSet<>();
			for (CacheEntryRef r: indexEntry.getRefs()) {
				CacheEntry ce = get(r.getCacheEntryKey());
				if (ce != null && ce.getVersion() == r.getCacheEntryVersion()) {
					//Index更新中の場合、同一キーで複数エントリ(別バージョンの)がある可能性あり
					if (!keySet.contains(r.getCacheEntryKey())) {
						keySet.add(r.getCacheEntryKey());
						ret.add(ce);
					}
				} else {
					//不正な状態で残ってしまったentryRefもしくは、index更新中のEntry
					//TODO warnで出したいけど、大量にログがでてしまう。。。
					logger.debug("cache index:(" + key + ", " + r + ") has no cacheEntry, so research next entry cache:namespace=" + getNamespace());
					//TODO 本当はメモリリーク防ぐために消したいけど、消してよいかの判断がつかない（更新中の場合もある。。）
					//FIXME 別途バッチで、Indexの整合性をチェック＆修正するプロセスを走らす
				}
			}
			return ret;
		}
	}

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
	public void addCacheEventListenner(CacheEventListener listener) {
		wrapped.addCacheEventListenner(listener);
	}

	@Override
	public void removeCacheEventListenner(CacheEventListener listener) {
		wrapped.removeCacheEventListenner(listener);
	}

	@Override
	public List<CacheEventListener> getListeners() {
		return wrapped.getListeners();
	}
	
	@Override
	public int getSize() {
		return wrapped.getSize();
	}

	@Override
	public String trace() {
		return "dataStore:" + wrapped.trace() + ", indexStore:" + indexStore.toString();
	}

	@Override
	public void destroy() {
		wrapped.destroy();
		indexStore.stop();
	}
	
	protected static final class IndexKey implements Serializable {
		private static final long serialVersionUID = -5273768024839363116L;
		
		private final int index;
		private final Object value;
		
		public IndexKey(int index, Object value) {
			this.index = index;
			this.value = value;
		}
		
		public int getIndex() {
			return index;
		}
		public Object getValue() {
			return value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + index;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			IndexKey other = (IndexKey) obj;
			if (index != other.index)
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "IndexKey [index=" + index + ", value=" + value + "]";
		}
		
	}
	
	protected static final class IndexEntry implements Serializable {
		private static final long serialVersionUID = -5028653348986984019L;
		
		private final long modCount;
		private final CacheEntryRef[] refs;
		
		public IndexEntry(CacheEntryRef ref) {
			this.modCount = 0;
			this.refs = new CacheEntryRef[]{ref};
		}
		
		public IndexEntry(long modCount, CacheEntryRef[] refs) {
			this.modCount = modCount;
			this.refs = refs;
		}
		
		public long getModCount() {
			return modCount;
		}

		public CacheEntryRef[] getRefs() {
			return refs;
		}

		public IndexEntry add(CacheEntryRef ref) {
			
			CacheEntryRef[] newRefs = new CacheEntryRef[refs.length + 1];
			System.arraycopy(refs, 0, newRefs, 0, refs.length);
			newRefs[refs.length] = ref;
			return new IndexEntry(modCount + 1, newRefs);
		}
		
		public IndexEntry remove(CacheEntryRef ref) {
			int index = -1;
			for (int i = 0; i < refs.length; i++) {
				if (ref.equals(refs[i])) {
					index = i;
					break;
				}
			}
			if (index > -1) {
				if (refs.length == 1) {
					return null;
				} else {
					CacheEntryRef[] newRefs = new CacheEntryRef[refs.length - 1];
					System.arraycopy(refs, 0, newRefs, 0, index);
					System.arraycopy(refs, index + 1, newRefs, index, refs.length - index - 1);
					return new IndexEntry(modCount + 1, newRefs);
				}
			} else {
				return this;
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (modCount ^ (modCount >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			IndexEntry other = (IndexEntry) obj;
			if (modCount != other.modCount)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "IndexEntry [modCount=" + modCount + ", refs="
					+ Arrays.toString(refs) + "]";
		}
		
	}
	
	protected static final class CacheEntryRef implements Serializable {
		private static final long serialVersionUID = -7243931537378415392L;
		
		private final Object cacheEntryKey;
		private final long cacheEntryVersion;
		
		public CacheEntryRef(Object cacheEntryKey, long cacheEntryVersion) {
			this.cacheEntryKey = cacheEntryKey;
			this.cacheEntryVersion = cacheEntryVersion;
		}

		public Object getCacheEntryKey() {
			return cacheEntryKey;
		}

		public long getCacheEntryVersion() {
			return cacheEntryVersion;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((cacheEntryKey == null) ? 0 : cacheEntryKey.hashCode());
			result = prime * result
					+ (int) (cacheEntryVersion ^ (cacheEntryVersion >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheEntryRef other = (CacheEntryRef) obj;
			if (cacheEntryKey == null) {
				if (other.cacheEntryKey != null)
					return false;
			} else if (!cacheEntryKey.equals(other.cacheEntryKey))
				return false;
			if (cacheEntryVersion != other.cacheEntryVersion)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "CacheEntryRef [cacheEntryKey=" + cacheEntryKey
					+ ", cacheEntryVersion=" + cacheEntryVersion + "]";
		}
		
	}

}
