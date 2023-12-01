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

package org.iplass.mtp.impl.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.builtin.NullKey;
import org.iplass.mtp.impl.cache.store.builtin.TransactionLocalCacheStoreFactory.TransactionLocalCacheStore;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read-Throughなキャッシュを簡単に実現するためのキャッシュコントローラ。
 *
 * @author K.Higuchi
 *
 * @param <K> keyの型
 * @param <V> valueの型
 */
public class CacheController<K, V> {

	private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

	private final LoadingAdapter<K,V> adapter;
	private final CacheStore store;
	private final boolean hasVersion;
	private final int indexCount;
	private final boolean useNegativeCacheOnSharedCache;
	private final boolean isStrictUpdate;

	public CacheController(CacheStore store, boolean hasVersion, int indexCount, LoadingAdapter<K,V> adapter, boolean useNegativeCacheOnSharedCache) {
		this(store, hasVersion, indexCount, adapter, useNegativeCacheOnSharedCache, false);
	}

	public CacheController(CacheStore store, boolean hasVersion, int indexCount, LoadingAdapter<K,V> adapter, boolean useNegativeCacheOnSharedCache, boolean isStrictUpdate) {
		this.adapter = adapter;
		this.hasVersion = hasVersion;
		this.indexCount = indexCount;
		this.store = store;
		this.useNegativeCacheOnSharedCache = useNegativeCacheOnSharedCache;
		this.isStrictUpdate = isStrictUpdate;
	}

	/**
	 * 指定のkeyのキャッシュの値を取得。
	 * キャッシュにない場合、LoadingAdapterを利用して
	 * バックエンドのデータストアよりデータをロードしてキャッシュする。
	 *
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V get(K key) {


//		if (store instanceof TransactionLocalCacheStore) {
//			if (((TransactionLocalCacheStore) store).isNegativeCachedOnLocalStore(key)) {
//				return null;
//			}
//		}

		CacheEntry entry = store.get(key);
		if (entry != null) {
			return (V) entry.getValue();
		}

		if (isStrictUpdate) {
			synchronized (this) {
				entry = store.get(key);
				if (entry != null) {
					return (V) entry.getValue();
				}
				return loadByKey(key);
			}
		} else {
			return loadByKey(key);
		}
	}

	private V loadByKey(K key) {
		V value = adapter.load(key);
		if (value != null) {
			putToCache(value, true, null, 0);
		} else {
			//negativeCacheする場合は、nullをキャッシュ
			if (useNegativeCacheOnSharedCache) {
				CacheEntry ce;
				if (hasVersion) {
					ce = new CacheEntry(key, null, Long.MIN_VALUE, (Object[]) null);
				} else {
					ce = new CacheEntry(key, null);
				}
				store.put(ce, true);
			}
		}
		return value;
	}

	private CacheEntry getInternal(K key) {
		CacheEntry entry = store.get(key);
		if (entry != null) {
			return entry;
		}

		V value = adapter.load(key);
		CacheEntry ret = null;
		if (value != null) {
			ret = putToCache(value, true, null, 0);
		} else {
			//negativeCacheする場合は、nullをキャッシュ
			if (useNegativeCacheOnSharedCache) {
				CacheEntry ce;
				if (hasVersion) {
					ce = new CacheEntry(key, null, Long.MIN_VALUE, (Object[]) null);
				} else {
					ce = new CacheEntry(key, null);
				}
				store.put(ce, true);
				ret = ce;
			}
		}
		return ret;

	}

	/**
	 * 指定のindexTypeのindexValにてキャッシュの値を取得。
	 * キャッシュにない場合、LoadingAdapterを利用して
	 * バックエンドのデータストアよりデータをロードしてキャッシュする。
	 *
	 * @param indexType
	 * @param indexVal
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V getByIndex(int indexType, Object indexVal) {

		CacheEntry entry = store.getByIndex(indexType, indexVal);

		if (entry != null) {
			return (V) entry.getValue();
		}

		if (isStrictUpdate) {
			synchronized (this) {
				entry = store.getByIndex(indexType, indexVal);
				if (entry != null) {
					return (V) entry.getValue();
				}
				return loadByIndex(indexType, indexVal);
			}
		} else {
			return loadByIndex(indexType, indexVal);
		}
	}

	@SuppressWarnings("unchecked")
	private V loadByIndex(int indexType, Object indexVal) {
		V value = null;
		List<V> list = adapter.loadByIndex(indexType, indexVal);
		if (list != null && list.size() > 0) {
			for (V v: list) {
				putToCache(v, true, null, 0);
			}
			CacheEntry entry = store.getByIndex(indexType, indexVal);
			if (entry != null) {
				value = (V) entry.getValue();
			}
		} else {
			//negativeCacheする場合は、nullをキャッシュ
			if (useNegativeCacheOnSharedCache) {
				CacheEntry ce;
				Object[] indexValues = new Object[store.getFactory().getIndexCount()];
				indexValues[indexType] = indexVal;
				if (hasVersion) {
					ce = new CacheEntry(new NullKey(), null, Long.MIN_VALUE, indexValues);
				} else {
					ce = new CacheEntry(new NullKey(), null, indexValues);
				}
				store.put(ce, true);
			}

		}
		return value;
	}

	private CacheEntry putToCache(V value, boolean clean, V oldVale, long oldVersion) {
		Object[] indexValue;
		if (indexCount > 0) {
			indexValue = new Object[indexCount];
			for (int i = 0; i < indexCount; i++) {
				indexValue[i] = adapter.getIndexVal(i, value);
			}
		} else {
			indexValue = null;
		}

		CacheEntry ce;
		if (hasVersion) {
			ce = new CacheEntry(adapter.getKey(value), value, adapter.getVersion(value), indexValue);
		} else {
			if (oldVale == null) {
				ce = new CacheEntry(adapter.getKey(value), value, indexValue);
			} else {
				ce = new CacheEntry(adapter.getKey(value), value, oldVersion + 1, indexValue);
			}
		}
		store.put(ce, clean);
		return ce;
	}

	/**
	 * このCacheControllerが管理するCacheStoreを取得。
	 *
	 * @return
	 */
	public CacheStore getStore() {
		return store;
	}

	/**
	 * 現時点でキャッシュに格納されているデータのみが対象。Read-Throughはしない。
	 *
	 * @param indexType
	 * @param indexVal
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<V> getListByIndex(int indexType, Object indexVal) {
		List<CacheEntry> ceList = store.getListByIndex(indexType, indexVal);
		//複数件のキャッシュのindexの整合性を確認するのは難しいので、、、
//		if (ceList.size() == 0) {
//			List<V> list = adapter.loadByIndex(indexType, indexVal);
//			for (V v: list) {
//				putToCache(v);
//			}
//			ceList = store.getListByIndex(indexType, indexVal);
//		}
		if (ceList.size() > 0) {
			ArrayList<V> ret = new ArrayList<V>();
			for (CacheEntry ce: ceList) {
				ret.add((V) ce.getValue());
			}
			return ret;
		} else {
			return Collections.emptyList();
		}
	}

	public List<V> removeByIndex(int indexType, Object indexVal) {
		if (isStrictUpdate) {
			synchronized (this) {
				return removeByIndexInternal(indexType, indexVal);
			}
		} else {
			return removeByIndexInternal(indexType, indexVal);
		}
	}

	@SuppressWarnings("unchecked")
	private List<V> removeByIndexInternal(int indexType, Object indexVal) {
		List<CacheEntry> ceList = store.removeByIndex(indexType, indexVal);
		if (ceList != null && ceList.size() > 0) {
			ArrayList<V> ret = new ArrayList<V>();
			for (CacheEntry ce: ceList) {
				ret.add((V) ce.getValue());
			}
			return ret;
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * 指定の値が作成されたことをCacheControllerに通知する。
	 *
	 * @param value
	 */
	public void notifyCreate(V value) {
		//TODO putIfAbsentじゃなくてよいか？
		if (isStrictUpdate) {
			synchronized (this) {
				putToCache(value, false, null, 0);
			}
		} else {
			putToCache(value, false, null, 0);
		}
	}

	/**
	 * 指定の値が更新されたことをCacheControllerに通知する。
	 *
	 * @param value
	 */
	public void notifyUpdate(V value) {
		if (isStrictUpdate) {
			synchronized (this) {
				notifyUpdateInternal(value);
			}
		} else {
			notifyUpdateInternal(value);
		}
	}

	@SuppressWarnings("unchecked")
	private void notifyUpdateInternal(V value) {
		CacheEntry oldValue = getInternal(adapter.getKey(value));
		//TODO replaceじゃなくてよいか？
		if (oldValue == null) {
			putToCache(value, false, null, 0);
		} else {
			putToCache(value, false, (V) oldValue.getValue(), oldValue.getVersion());
		}
	}

	/**
	 * 指定のkeyの値が削除されたことをCacheControllerに通知する。
	 *
	 * @param value
	 */
	public void notifyDeleteByKey(K key) {
		if (isStrictUpdate) {
			synchronized (this) {
				store.remove(key);
			}
		} else {
			store.remove(key);
		}
	}

	/**
	 * 指定のkeyの値を無効にするよう通知する。
	 *
	 * @param value
	 */
	public void notifyInvalidByKey(K key) {
		if (isStrictUpdate) {
			synchronized (this) {
				store.remove(key);
			}
		} else {
			store.remove(key);
		}
	}

	/**
	 * 指定の値が削除されたことをCacheControllerに通知する。
	 *
	 * @param value
	 */
	public void notifyDelete(V value) {
		//TODO remove(CacheEntry, CacheEntry)じゃなくてよいか？
		if (isStrictUpdate) {
			synchronized (this) {
				store.remove(adapter.getKey(value));
			}
		} else {
			store.remove(adapter.getKey(value));
		}
	}

	/**
	 * 指定の値を無効にするよう通知する。
	 *
	 * @param value
	 */
	public void notifyInvalid(V value) {
		if (isStrictUpdate) {
			synchronized (this) {
				store.remove(adapter.getKey(value));
			}
		} else {
			store.remove(adapter.getKey(value));
		}
	}

	/**
	 * キャッシュを全てクリアする。
	 *
	 */
	public void clearAll() {
		if (isStrictUpdate) {
			synchronized (this) {
				store.removeAll();
			}
		} else {
			store.removeAll();
		}
	}

	public void refreshTransactionLocalStore(K key) {
		if (store instanceof TransactionLocalCacheStore) {
			((TransactionLocalCacheStore) store).reloadFromBackendStore(key);
		}
	}
	
	public void maintenance(Consumer<CacheController<K, V>> maintenanceFunction) {
		if (isStrictUpdate) {
			synchronized (this) {
				maintenanceFunction.accept(this);
			}
		} else {
			maintenanceFunction.accept(this);
		}
	}

	public void invalidateCacheStore() {
		ServiceRegistry.getRegistry().getService(CacheService.class).invalidate(store.getNamespace());
	}

	//TODO voidに
	public String trace() {
		if (logger.isTraceEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("CacheController Info");
			builder.append("\nCacheController:" + this);
			builder.append("\n\tCacheStore:" + store);
			builder.append("\n\tLoadingAdapter:" + adapter);
			builder.append("\n\thasVersion:" + hasVersion);
			builder.append("\n\tindexCount:" + indexCount);
			builder.append("\n\tuseNegativeCacheOnSharedCache:" + useNegativeCacheOnSharedCache);

			builder.append("\n" + store.trace());

			logger.trace(builder.toString());
			return builder.toString();
		} else {
			return "CacheController Info(no details)";
		}
	}

}
