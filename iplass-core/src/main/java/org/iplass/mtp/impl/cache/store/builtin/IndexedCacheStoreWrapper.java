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

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;

public class IndexedCacheStoreWrapper implements CacheStore {
	
	//TODO
	//dist:DistributeExec利用で、個別のNodeでバージョンチェック＆CAS。成功したらINDEX更新（同様にDistributeExec利用で）。CASするので、replace時にリモートだと通信オーバーヘッドあるので。
	//local:直接CAS＆indexもCAS（バージョンで比較？）
	//repl:直接CAS＆indexもCAS（バージョンで比較？）
	//inval:直接CAS＆indexもCAS（バージョンで比較？）
	
	
	
	
	private final CacheStore wrapped;
	private final CacheStore[] indexStore;
	
	private final ReentrantReadWriteLock[] locks;
	
	//ConcurentHashMapと同様のsegmenthash戦略
	private final int segmentMask;
	private final int segmentShift;
	
	public IndexedCacheStoreWrapper(CacheStore wrapped, CacheStore[] indexStore, int concurrencyLevel, boolean fair) {
		this.wrapped = wrapped;
		this.indexStore = indexStore;
		
		int sshift = 0;
		int ssize = 1;
		while (ssize < concurrencyLevel) {
			++sshift;
			ssize <<= 1;
		}
		segmentShift = 32 - sshift;
		segmentMask = ssize - 1;
		locks = new ReentrantReadWriteLock[ssize];
		for (int i = 0; i < locks.length; i++) {
			locks[i] = new ReentrantReadWriteLock(fair);
		}
		
	}
	
	private static int hash(int h) {
		// Spread bits to regularize both segment and index locations,
		// using variant of single-word Wang/Jenkins hash.
		h += (h <<  15) ^ 0xffffcd7d;
		h ^= (h >>> 10);
		h += (h <<   3);
		h ^= (h >>>  6);
		h += (h <<   2) + (h << 14);
		return h ^ (h >>> 16);
	}

	private final ReentrantReadWriteLock lockFor(int hash) {
		return locks[(hash >>> segmentShift) & segmentMask];
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheEntry get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheEntry remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(CacheEntry entry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Object> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCacheEventListenner(CacheEventListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeCacheEventListenner(CacheEventListener listener) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Integer getSize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String trace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<CacheEventListener> getListeners() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
