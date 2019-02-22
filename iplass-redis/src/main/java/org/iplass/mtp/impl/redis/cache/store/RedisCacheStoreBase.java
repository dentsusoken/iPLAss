/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.redis.cache.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.iplass.mtp.MtpException;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheCreateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheRemoveEvent;
import org.iplass.mtp.impl.cache.store.event.CacheUpdateEvent;

public abstract class RedisCacheStoreBase implements CacheStore {

	private final RedisCacheStoreFactory factory;
	private final String namespace;
	private final long timeToLive;
	private final boolean isSave;

	private List<CacheEventListener> listeners;

	public RedisCacheStoreBase(RedisCacheStoreFactory factory, String namespace, long timeToLive, boolean isSave) {
		this.factory = factory;
		this.namespace = namespace;
		this.timeToLive = timeToLive;
		this.isSave = isSave;

		listeners = new CopyOnWriteArrayList<CacheEventListener>();
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public CacheStoreFactory getFactory() {
		return factory;
	}

	@Override
	public void addCacheEventListenner(CacheEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeCacheEventListenner(CacheEventListener listener) {
		listeners.remove(listener);
	}

	@Override
	public List<CacheEventListener> getListeners() {
		return listeners;
	}

	protected long getTimeToLive() {
		return timeToLive;
	}

	protected boolean isSave() {
		return isSave;
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

	protected String encodeBase64(Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream bis = new ObjectOutputStream(baos)) {
			bis.writeObject(obj);
			bis.flush();
			return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (IOException e) {
			throw new MtpException(e);
		}
	}

	protected Object decodeBase64(String src) {
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(src));
		try (ObjectInputStream ois = new ObjectInputStream(bais)) {
			return ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new MtpException(e);
		}
	}

}
