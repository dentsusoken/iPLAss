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

package org.iplass.mtp.impl.cache.store.builtin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheCreateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheRemoveEvent;
import org.iplass.mtp.impl.cache.store.event.CacheUpdateEvent;

public abstract class SimpleCacheStoreBase implements CacheStore {

	private String namespace;
	private List<CacheEventListener> listeners;
	private boolean multithreaded;
	private CacheStoreFactory factory;

	public SimpleCacheStoreBase(String namespace, boolean multithreaded, CacheStoreFactory factory) {
		this.namespace = namespace;
		this.multithreaded = multithreaded;
		this.factory = factory;
		if (multithreaded) {
			listeners = new CopyOnWriteArrayList<CacheEventListener>();
		}
	}

	@Override
	public CacheStoreFactory getFactory() {
		return factory;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	protected void notifyRemoved(CacheEntry entry) {
		if (listeners != null) {
			CacheRemoveEvent e = new CacheRemoveEvent(entry);
			for (CacheEventListener l: listeners) {
				l.removed(e);
			}
		}
	}

	protected void notifyPut(CacheEntry entry) {
		if (listeners != null) {
			CacheCreateEvent e = new CacheCreateEvent(entry);
			for (CacheEventListener l: listeners) {
				l.created(e);
			}
		}
	}

	protected void notifyUpdated(CacheEntry preEntry, CacheEntry entry) {
		if (listeners != null) {
			CacheUpdateEvent e = new CacheUpdateEvent(preEntry, entry);
			for (CacheEventListener l: listeners) {
				l.updated(e);
			}
		}
	}

	protected void notifyInvalidated(CacheEntry entry) {
		if (listeners != null) {
			CacheInvalidateEvent e = new CacheInvalidateEvent(entry);
			for (CacheEventListener l: listeners) {
				l.invalidated(e);
			}
		}
	}
	
	protected boolean hasListener() {
		if (listeners == null) {
			return false;
		}
		return listeners.size() > 0;
	}
	
//	protected void notifyExpired(CacheEntry entry) {
//		if (listeners != null) {
//			for (CacheEventListener l: listeners) {
//				l.notifyExpired(entry);
//			}
//		}
//	}
//
	@Override
	public void addCacheEventListenner(CacheEventListener listener) {
		if (!multithreaded) {
			if (listeners == null) {
				listeners = new ArrayList<CacheEventListener>();
			}
		}
		listeners.add(listener);
	}

	@Override
	public void removeCacheEventListenner(CacheEventListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}
	
	@Override
	public List<CacheEventListener> getListeners() {
		if (listeners == null) {
			return Collections.emptyList();
		} else {
			return listeners;
		}
	}

	protected abstract void removeInvalidEntry();
	protected abstract void removeNullEntry(NullKey key);
	
	//インデックスに関するユーティリティメソッド。
	Object margeVal(Object current, Object added) {
		if (added == null) {
			added = new NullKey();
		}

		if (current == null) {
			return added;
		}
		if (current instanceof NullKey) {
			removeNullEntry((NullKey) current);
			return added;
		}
		if (current instanceof Object[]) {
			Object[] newVal = new Object[((Object[]) current).length + 1];
			System.arraycopy(current, 0, newVal, 0, newVal.length - 1);
			newVal[newVal.length - 1] = added;
			return newVal;
		}
		return new Object[]{current, added};
	}

	Object subtractVal(Object current, Object removed, boolean loose) {
		if (current == null) {
			return null;
		}
//		if (current instanceof NullKey) {
//			return null;
//		}
		if (current instanceof Object[]) {
			Object[] ca = (Object[]) current;
			ArrayList<Object> ret = new ArrayList<Object>(ca.length);
			for (int i = 0; i < ca.length; i++) {
				if (!ca[i].equals(removed)) {
					ret.add(ca[i]);
				}
			}
			if (ret.size() == 0) {
				return null;
			}
			if (ret.size() == 1) {
				return ret.get(0);
			}
			return ret.toArray();
		}
		if (current.equals(removed)) {
			return null;
		}

		if (removed instanceof NullKey) {
			return current;
		}

		if (loose) {
			return current;
		}

		//ありえないはずなのだが、、、
		throw new IllegalStateException("illegal cache state... toRemove:" + removed + " current:" + current);
	}

	protected String baseTrace() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tnamespace:" + namespace);
		builder.append("\n\tmultithreaded:" + multithreaded);

		return builder.toString();

	}

}
