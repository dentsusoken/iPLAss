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

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.redis.RedisRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;

public class RedisCacheStore extends RedisCacheStoreBase {

	private static final Logger logger = LoggerFactory.getLogger(RedisCacheStore.class);

	public RedisCacheStore(RedisCacheStoreFactory factory, String namespace, long timeToLive,
			RedisCacheStorePoolConfig redisCacheStorePoolConfig) {
		super(factory, namespace, timeToLive, redisCacheStorePoolConfig);

		pubSubConnection.addListener(new RedisPubSubListener<String, String>() {
			@Override
			public void unsubscribed(String channel, long count) {
			}

			@Override
			public void subscribed(String channel, long count) {
			}

			@Override
			public void punsubscribed(String pattern, long count) {
			}

			@Override
			public void psubscribed(String pattern, long count) {
			}

			@Override
			public void message(String pattern, String channel, String message) {
			}

			@Override
			public void message(String channel, String message) {
				String prefix = codec.getPrefix();
				if (message.startsWith(prefix)) {
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Occur expired event. channel:%s, message:%s", channel, message));
					}
					Object key = codec.decodeKey(codec.getCharset().encode(message));
					notifyRemoved(new CacheEntry(key, null));
				}
			}
		});
		this.pubSubCommands = pubSubConnection.sync();
		pubSubCommands.subscribe("__keyevent@" + String.valueOf(factory.getServer().getDatabase()) + "__:expired"); // Expiredイベントのみ受信、DB番号は0固定
	}

	@Override
	public CacheEntry get(Object key) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			return key != null ? (CacheEntry) commands.get(key) : null;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public CacheEntry put(CacheEntry entry, boolean clean) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			CacheEntry previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.PUT, ScriptOutputType.VALUE,
					new Object[] { entry.getKey() }, timeToLive, entry);

			if (previous == null) {
				notifyPut(entry);
			} else {
				notifyUpdated(previous, entry);
			}
			return previous;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			CacheEntry previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.PUT_IF_ABSENT,
					ScriptOutputType.VALUE, new Object[] { entry.getKey() }, timeToLive, entry);

			if (previous == null) {
				notifyPut(entry);
			}
			return previous;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public CacheEntry remove(Object key) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			CacheEntry previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.REMOVE_BY_KEY,
					ScriptOutputType.VALUE, new Object[] { key });

			if (previous != null) {
				notifyRemoved(previous);
			}
			return previous;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public boolean remove(CacheEntry entry) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			CacheEntry previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.REMOVE_BY_ENTRY,
					ScriptOutputType.VALUE, new Object[] { entry.getKey() }, entry);

			if (previous != null) {
				notifyRemoved(previous);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			CacheEntry previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.REPLACE, ScriptOutputType.VALUE,
					new Object[] { entry.getKey() }, timeToLive, entry);

			if (previous != null) {
				notifyUpdated(previous, entry);
			}
			return previous;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		if (!oldEntry.getKey().equals(newEntry.getKey())) {
			throw new IllegalArgumentException("oldEntry key not equals newEntry key");
		}

		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			CacheEntry previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.REPLACE_NEW,
					ScriptOutputType.VALUE, new Object[] { newEntry.getKey() }, timeToLive, oldEntry, newEntry);

			if (previous != null) {
				notifyUpdated(oldEntry, newEntry);
			}
			return previous != null;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public List<Object> keySet() {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			List<Object> keys = commands.keys("*");
			return keys;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public void removeAll() {
		if (hasListener()) {
			keySet().forEach(key -> remove(key));
		} else {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				commands.eval(RedisCacheStoreLuaScript.REMOVE_ALL, ScriptOutputType.MULTI, new Object[] { "*" });
			} catch (Exception e) {
				throw new RedisRuntimeException(e);
			}
		}
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		// Noop
		return null;
	}

	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		// Noop
		return null;
	}

	@Override
	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
		// Noop
		return null;
	}

}
