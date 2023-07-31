/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.impl.cache.store.event.CacheEventListener;

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
