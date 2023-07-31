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


import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;

public class NullCacheStore implements CacheStore {

	private String namespace;
	private CacheStoreFactory factory;

	public NullCacheStore(String namespace, CacheStoreFactory factory) {
		this.namespace = namespace;
		this.factory = factory;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public void addCacheEventListenner(CacheEventListener listener) {
	}

	@Override
	public void removeCacheEventListenner(CacheEventListener listener) {
	}

	@Override
	public List<Object> keySet() {
		return Collections.emptyList();
	}

	@Override
	public CacheEntry put(CacheEntry entry, boolean isClean) {
		return null;
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		return null;
	}

	@Override
	public CacheEntry computeIfAbsent(Object key, Function<Object, CacheEntry> mappingFunction) {
		return mappingFunction.apply(key);
	}

	@Override
	public CacheEntry compute(Object key, BiFunction<Object, CacheEntry, CacheEntry> remappingFunction) {
		return remappingFunction.apply(key, null);
	}

	@Override
	public CacheEntry get(Object key) {
		return null;
	}

	@Override
	public CacheEntry remove(Object key) {
		return null;
	}

	@Override
	public boolean remove(CacheEntry entry) {
		return false;
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		return null;
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		return false;
	}

	@Override
	public void removeAll() {
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		return null;
	}

	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		return Collections.emptyList();
	}

	@Override
	public CacheStoreFactory getFactory() {
		return factory;
	}

//	@Override
//	public void invalidate(CacheEntry entry) {
//	}

	@Override
	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
		return Collections.emptyList();
	}
	
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String trace() {
		StringBuilder builder = new StringBuilder();
		builder.append("-----------------------------------");
		builder.append("CacheStore Info");
		builder.append("\nCacheStore:" + this);
		builder.append("\n-----------------------------------");

		return builder.toString();
	}

	@Override
	public void destroy() {
		
	}

	@Override
	public List<CacheEventListener> getListeners() {
		return Collections.emptyList();
	}

}
