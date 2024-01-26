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

package org.iplass.mtp.impl.cache.store.builtin;

import java.util.LinkedHashMap;
import java.util.Map;

import org.iplass.mtp.impl.cache.store.CacheEntry;

public class LRUMap extends LinkedHashMap<Object, CacheEntry> {
	private static final long serialVersionUID = 5231893587904969880L;
	
	MapBaseCacheStore store;
	private int size;
	
	public LRUMap(int initialCapacity, float loadFactor, int size) {
		super(initialCapacity, loadFactor, true);
		this.size = size;
	}
	
	void setStore(MapBaseCacheStore store) {
		this.store = store;
	}

	@Override
    protected boolean removeEldestEntry(Map.Entry<Object, CacheEntry> eldest) {
    	
    	boolean isRemove = size() > size;
    	if (isRemove) {
    		//TODO 実際の削除前に、呼び出ししてしまっているがよいか、、
    		store.removeFromIndex(eldest.getValue(), false);
    		store.notifyRemoved(eldest.getValue());
    	}
        return isRemove;
    }
	
}

