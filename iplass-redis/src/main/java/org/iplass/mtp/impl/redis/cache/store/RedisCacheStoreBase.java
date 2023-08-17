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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheCreateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheRemoveEvent;
import org.iplass.mtp.impl.cache.store.event.CacheUpdateEvent;
import org.iplass.mtp.impl.redis.RedisRuntimeException;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import io.lettuce.core.support.ConnectionPoolSupport;

public abstract class RedisCacheStoreBase implements CacheStore {

	protected final StatefulRedisPubSubConnection<String, String> pubSubConnection;
	protected RedisPubSubCommands<String, String> pubSubCommands;

	private final RedisCacheStoreFactory factory;
	private final String namespace;
	protected final long timeToLive;

	protected final NamespaceSerializedObjectCodec codec;
	protected final GenericObjectPool<StatefulRedisConnection<Object, Object>> pool;

	private final List<CacheEventListener> listeners;

	public RedisCacheStoreBase(RedisCacheStoreFactory factory, String namespace, long timeToLive,
			RedisCacheStorePoolConfig redisCacheStorePoolConfig) {
		this.pubSubConnection = factory.getClient().connectPubSub();
		this.factory = factory;
		this.namespace = namespace;
		this.timeToLive = timeToLive;

		this.codec = new NamespaceSerializedObjectCodec(namespace);

		GenericObjectPoolConfig<StatefulRedisConnection<Object, Object>> poolConfig = new GenericObjectPoolConfig<>();
		redisCacheStorePoolConfig.apply(poolConfig);

		this.pool = ConnectionPoolSupport.createGenericObjectPool(() -> factory.getClient().connect(codec), poolConfig);

		this.listeners = new CopyOnWriteArrayList<CacheEventListener>();
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
	public List<CacheEventListener> getListeners() {
		return listeners;
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
	public int getSize() {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			List<Object> keys = commands.keys("*");
			return keys == null ? 0 : keys.size();
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public String trace() {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			return commands.info();
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public void destroy() {
		pool.close();
		factory.getClient().shutdown();
	}

	protected void notifyRemoved(CacheEntry entry) {
		if (hasListener()) {
			CacheRemoveEvent e = new CacheRemoveEvent(entry);
			listeners.forEach(listener -> listener.removed(e));
		}
	}

	protected void notifyPut(CacheEntry entry) {
		if (hasListener()) {
			CacheCreateEvent e = new CacheCreateEvent(entry);
			listeners.forEach(listener -> listener.created(e));
		}
	}

	protected void notifyUpdated(CacheEntry preEntry, CacheEntry entry) {
		if (hasListener()) {
			CacheUpdateEvent e = new CacheUpdateEvent(preEntry, entry);
			listeners.forEach(listener -> listener.updated(e));
		}
	}

	protected void notifyInvalidated(CacheEntry entry) {
		if (hasListener()) {
			CacheInvalidateEvent e = new CacheInvalidateEvent(entry);
			listeners.forEach(listener -> listener.invalidated(e));
		}
	}

	protected boolean hasListener() {
		return listeners != null ? listeners.size() > 0 : false;
	}

}
