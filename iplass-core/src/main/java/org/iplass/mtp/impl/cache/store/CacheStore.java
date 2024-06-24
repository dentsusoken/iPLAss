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

package org.iplass.mtp.impl.cache.store;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * ローレベルのキャッシュ機能を実装するキャッシュストアのインタフェース。
 *
 * @author K.Higuchi
 *
 */
public interface CacheStore {
	
	public String getNamespace();

	public CacheStoreFactory getFactory();
	
	public int getSize();
	
	/**
	 * 自動リロードするキャッシュエントリをputする。
	 * まだkeyに紐づいたエントリがない場合にreloadFunctionによって取得されたエントリをputする。
	 * 既にkeyに紐づいたエントリがある場合はそのエントリを返却する。
	 * 
	 * reloadFunctionが返却するキャッシュエントリのtimeToLiveの時間経過後reloadFunctionを利用してCacheEntryを再取得する。
	 * リロードは非同期で実行され、リロード完了するまでの間は古いCacheEntryを返す。
	 * 
	 * デフォルト実装では、JVMローカルでのみリロードされる。
	 * リロード対象のキャッシュエントリが他の更新系メソッド（put, remove, replace）により更新された場合、リロードはキャンセルされる。
	 * 
	 * @param key キャッシュエントリのキー
	 * @param reloadFunction キャッシュエントリが存在しない場合、リロード時に呼び出される関数。初回のロード時はapplyの引数CacheEntryはnullが渡される。また、リロード時はreloadFunctionは非同期にユーザ未特定で特権状態で実行される。
	 * @return 初回ロード、もしくはリロードされたキャッシュエントリ
	 */
	public default CacheEntry computeIfAbsentWithAutoReload(Object key, BiFunction<Object, CacheEntry, CacheEntry> reloadFunction) {
		CacheEntry[] newEntry = new CacheEntry[1];
		Long[] ttl = new Long[1];
		CacheEntry computed = computeIfAbsent(key, k -> {
			
			newEntry[0] = reloadFunction.apply(k, null);
			
			ttl[0] = newEntry[0].getTimeToLive();
			if (ttl[0] == null || ttl[0].longValue() <= 0L) {
				throw new IllegalArgumentException("computeIfAbsentWithAutoReload() must specify TTL on CacheEntry.");
			}
			
			//entryのttlを無期限に設定し、ttlによりinvalidateされないように
			newEntry[0].setTimeToLive(-1L);
			return newEntry[0];
		});
		
		if (computed == newEntry[0]) {
			//自身がputした場合のみリロードをスケジュールする
			CacheService cacheService = ServiceRegistry.getRegistry().getService(CacheService.class);
			cacheService.schedule(ttl[0],
					new AutoReloader(ExecuteContext.getCurrentContext().getClientTenantId(),
							getNamespace(), computed, ttl[0], reloadFunction));
		}
		
		return computed;
	}

	/**
	 * ConcurrentMapが提供するcomputeIfAbsentと同等の機能性を提供するメソッド。
	 * デフォルト実装ではmappingFunctionが複数回呼び出される可能性はある。
	 * 厳密にmappingFunctionが1度しか呼び出されないか否かは各CacheStore実装クラスによる。
	 * 
	 * @param key
	 * @param mappingFunction
	 * @return
	 */
	public default CacheEntry computeIfAbsent(Object key, Function<Object, CacheEntry> mappingFunction) {
		CacheEntry v, newValue;
		return ((v = get(key)) == null &&
				(newValue = mappingFunction.apply(key)) != null &&
				(v = putIfAbsent(newValue)) == null) ? newValue : v;
	}

	/**
	 * ConcurrentMapが提供するcomputeと同等の機能性を提供するメソッド。
	 * デフォルト実装ではremappingFunctionが複数回呼び出される可能性はある。
	 * 厳密にremappingFunctionが1度しか呼び出されないか否かは各CacheStore実装クラスによる。
	 * 
	 * @param key
	 * @param remappingFunction
	 * @return
	 */
	public default CacheEntry compute(Object key, BiFunction<Object, CacheEntry, CacheEntry> remappingFunction) {
		CacheEntry oldValue = get(key);
		for(;;) {
			CacheEntry newValue = remappingFunction.apply(key, oldValue);
			if (newValue == null) {
				//remove
				if (oldValue != null) {
					if (remove(oldValue)) {
						//success
						return null;
					} else {
						//retry
						oldValue = get(key);
					}
				} else {
					return null;
				}
			} else {
				//put
				if (oldValue != null) {
					if (replace(oldValue, newValue)) {
						//success
						return newValue;
					} else {
						//retry
						oldValue = get(key);
					}
				} else {
					oldValue = putIfAbsent(newValue);
					if (oldValue == null) {
						//success
						return newValue;
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param entry
	 * @param clean putするentryが更新されたものでない場合（他のNodeで読み込まれていても問題ない場合）ture
	 */
	public CacheEntry put(CacheEntry entry, boolean clean);

	public CacheEntry putIfAbsent(CacheEntry entry);

	public CacheEntry get(Object key);

	public CacheEntry remove(Object key);

	public boolean remove(CacheEntry entry);

	public CacheEntry replace(CacheEntry entry);

	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry);

	public void removeAll();

	public List<Object> keySet();

	//Indexを複数許す場合、先頭を返す
	public CacheEntry getByIndex(int indexKey, Object indexValue);

	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue);

	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue);

	public void addCacheEventListenner(CacheEventListener listener);

	public void removeCacheEventListenner(CacheEventListener listener);
	
	public List<CacheEventListener> getListeners();

	public String trace();
	
	public void destroy();
	
}
