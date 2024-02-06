/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.command.beanmapper.el;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

//参考：BeanELResolver#SoftConcurrentHashMap
class SoftConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {
	private static final long serialVersionUID = 1L;

	static private class SoftConcurrentHashMapValue<K, V> extends SoftReference<V> {
        final K key;
        SoftConcurrentHashMapValue(K key, V val, ReferenceQueue<V> q) {
			super(val, q);
			this.key = key;
		}
	}

	private ConcurrentHashMap<K, SoftConcurrentHashMapValue<K, V>> map;
	private ReferenceQueue<V> refQ;
	
	SoftConcurrentHashMap() {
		map = new ConcurrentHashMap<K, SoftConcurrentHashMapValue<K, V>>();
		refQ = new ReferenceQueue<V>();
	}

	SoftConcurrentHashMap(int initialCapacity) {
		map = new ConcurrentHashMap<K, SoftConcurrentHashMapValue<K, V>>(initialCapacity);
		refQ = new ReferenceQueue<V>();
	}
	
	@SuppressWarnings("unchecked")
	private void cleanup() {
		SoftConcurrentHashMapValue<K, V> ref = null;
		while ((ref = (SoftConcurrentHashMapValue<K, V>) refQ.poll()) != null) {
			map.remove(ref.key);
		}
	}

	@Override
	public V put(K key, V value) {
		cleanup();
		SoftConcurrentHashMapValue<K, V> prev = map.put(key, new SoftConcurrentHashMapValue<K, V>(key, value, refQ));
		return prev == null? null: prev.get();
	}

	@Override
	public V putIfAbsent(K key, V value) {
		cleanup();
		SoftConcurrentHashMapValue<K, V> prev = map.putIfAbsent(key, new SoftConcurrentHashMapValue<K, V>(key, value, refQ));
		return prev == null? null: prev.get();
	}

	@Override
	public V get(Object key) {
		cleanup();
		SoftConcurrentHashMapValue<K, V> BPRef = map.get(key);
		if (BPRef == null) {
			return null;
		}
		if (BPRef.get() == null) {
			map.remove(key);
			return null;
		}
		return BPRef.get();
	}

}
