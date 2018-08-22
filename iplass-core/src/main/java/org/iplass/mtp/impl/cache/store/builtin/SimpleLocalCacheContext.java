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

import org.iplass.mtp.impl.cache.store.CacheContext;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;

public class SimpleLocalCacheContext<K, V> implements CacheContext<K, V> {

	private final CacheStore cs;

	public SimpleLocalCacheContext(CacheStore cs) {
		this.cs = cs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V getValue(K key) {
		CacheEntry ce = cs.get(key);
		if (ce != null) {
			return (V) ce.getValue();
		} else {
			return null;
		}
	}

}
