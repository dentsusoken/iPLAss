/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.function.BiFunction;
import java.util.function.Function;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.TimeToLiveCalculator;
import org.iplass.mtp.impl.redis.RedisRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.RedisNoScriptException;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;

public class RedisCacheStore extends RedisCacheStoreBase {

	private static final Logger logger = LoggerFactory.getLogger(RedisCacheStore.class);

	public RedisCacheStore(RedisCacheStoreFactory factory, String namespace,
			TimeToLiveCalculator timeToLiveCalculator, RedisCacheStorePoolConfig redisCacheStorePoolConfig) {
		super(factory, namespace, timeToLiveCalculator, redisCacheStorePoolConfig);

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
			throw new RedisRuntimeException("can not get CacheEntry. key:" + key, e);
		}
	}

	@Override
	public CacheEntry put(CacheEntry entry, boolean clean) {
		setTtl(entry);
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			String sha = commands.digest(RedisCacheStoreLuaScript.PUT);

			CacheEntry previous;
			try {
				previous = (CacheEntry) commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { entry.getKey() },
						getTtlSeconds(entry), entry);
			} catch (RedisNoScriptException e) {
				previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.PUT, ScriptOutputType.VALUE,
						new Object[] { entry.getKey() }, getTtlSeconds(entry), entry);
			}

			if (previous == null) {
				notifyPut(entry);
			} else {
				notifyUpdated(previous, entry);
			}
			return previous;
		} catch (Exception e) {
			throw new RedisRuntimeException("can not put CacheEntry. entry:" + entry, e);
		}
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		setTtl(entry);
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			String sha = commands.digest(RedisCacheStoreLuaScript.PUT_IF_ABSENT);

			CacheEntry previous;
			try {
				previous = (CacheEntry) commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { entry.getKey() },
						getTtlSeconds(entry), entry);
			} catch (RedisNoScriptException e) {
				previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.PUT_IF_ABSENT, ScriptOutputType.VALUE,
						new Object[] { entry.getKey() }, getTtlSeconds(entry), entry);
			}

			if (previous == null) {
				notifyPut(entry);
			}
			return previous;
		} catch (Exception e) {
			throw new RedisRuntimeException("can not putIfAbsent CacheEntry. entry:" + entry, e);
		}
	}

	@Override
	public CacheEntry compute(Object key, BiFunction<Object, CacheEntry, CacheEntry> remappingFunction) {
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();

				commands.watch(key);

				CacheEntry oldEntry = get(key);
				CacheEntry newEntry = remappingFunction.apply(key, oldEntry);
				if (newEntry == null) {
					if (oldEntry != null) {
						commands.multi();

						// remove
						String sha = commands.digest(RedisCacheStoreLuaScript.REMOVE_BY_ENTRY);
						try {
							commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { key }, oldEntry);
						} catch (RedisNoScriptException e) {
							commands.eval(RedisCacheStoreLuaScript.REMOVE_BY_ENTRY, ScriptOutputType.VALUE,
									new Object[] { key }, oldEntry);
						}

						TransactionResult result = commands.exec();
						if (result.wasDiscarded()) {
							continue;
						}

						notifyRemoved(oldEntry);
						return null;
					} else {
						commands.unwatch();
						return null;
					}
				} else {
					setTtl(newEntry);
					if (oldEntry != null) {
						commands.multi();

						// replace
						if (!oldEntry.getKey().equals(newEntry.getKey())) {
							throw new IllegalArgumentException("oldEntry key not equals newEntry key");
						}
						String sha = commands.digest(RedisCacheStoreLuaScript.REPLACE_NEW);

						try {
							commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { newEntry.getKey() },
									getTtlSeconds(newEntry), oldEntry, newEntry);
						} catch (RedisNoScriptException e) {
							commands.eval(RedisCacheStoreLuaScript.REPLACE_NEW, ScriptOutputType.VALUE,
									new Object[] { newEntry.getKey() }, getTtlSeconds(newEntry), oldEntry, newEntry);
						}

						TransactionResult result = commands.exec();
						if (result.wasDiscarded()) {
							continue;
						}

						notifyUpdated(oldEntry, newEntry);
						return newEntry;
					} else {
						commands.multi();

						// putIfAbsent
						String sha = commands.digest(RedisCacheStoreLuaScript.PUT_IF_ABSENT);
						try {
							commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { key }, getTtlSeconds(newEntry), newEntry);
						} catch (RedisNoScriptException e) {
							commands.eval(RedisCacheStoreLuaScript.PUT_IF_ABSENT, ScriptOutputType.VALUE,
									new Object[] { key }, getTtlSeconds(newEntry), newEntry);
						}

						TransactionResult result = commands.exec();
						if (result.wasDiscarded()) {
							continue;
						}

						notifyPut(newEntry);
						return newEntry;
					}
				}
			} catch (Exception e) {
				throw new RedisRuntimeException("can not compute CacheEntry. key:" + key, e);
			}
		}
		throw new SystemException("can not compute CacheEntry cause retry count over. key:" + key);
	}

	@Override
	public CacheEntry computeIfAbsent(Object key, Function<Object, CacheEntry> mappingFunction) {
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();

				commands.watch(key);

				CacheEntry oldEntry = get(key);
				if (oldEntry == null) {
					commands.multi();

					// putIfAbsent
					CacheEntry newEntry = mappingFunction.apply(key);
					if (newEntry != null) {
						setTtl(newEntry);
						String sha = commands.digest(RedisCacheStoreLuaScript.PUT_IF_ABSENT);
						try {
							commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { key }, getTtlSeconds(newEntry), newEntry);
						} catch (RedisNoScriptException e) {
							commands.eval(RedisCacheStoreLuaScript.PUT_IF_ABSENT, ScriptOutputType.VALUE,
									new Object[] { key }, getTtlSeconds(newEntry), newEntry);
						}

						TransactionResult result = commands.exec();
						if (result.wasDiscarded()) {
							continue;
						}

						notifyPut(newEntry);
						return newEntry;
					}
				} else {
					commands.unwatch();
					return oldEntry;
				}
			} catch (Exception e) {
				throw new RedisRuntimeException("can not computeIfAbsent CacheEntry. key:" + key, e);
			}
		}
		throw new SystemException("can not computeIfAbsent CacheEntry cause retry count over. key:" + key);
	}

	@Override
	public CacheEntry remove(Object key) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			String sha = commands.digest(RedisCacheStoreLuaScript.REMOVE_BY_KEY);

			CacheEntry previous;
			try {
				previous = (CacheEntry) commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { key });
			} catch (RedisNoScriptException e) {
				previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.REMOVE_BY_KEY, ScriptOutputType.VALUE,
						new Object[] { key });
			}

			if (previous != null) {
				notifyRemoved(previous);
			}
			return previous;
		} catch (Exception e) {
			throw new RedisRuntimeException("can not remove CacheEntry. key:" + key, e);
		}
	}

	@Override
	public boolean remove(CacheEntry entry) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			String sha = commands.digest(RedisCacheStoreLuaScript.REMOVE_BY_ENTRY);

			CacheEntry previous;
			try {
				previous = (CacheEntry) commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { entry.getKey() },
						entry);
			} catch (RedisNoScriptException e) {
				previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.REMOVE_BY_ENTRY, ScriptOutputType.VALUE,
						new Object[] { entry.getKey() }, entry);
			}

			if (previous != null) {
				notifyRemoved(previous);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new RedisRuntimeException("can not remove CacheEntry. entry:" + entry, e);
		}
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		setTtl(entry);
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			String sha = commands.digest(RedisCacheStoreLuaScript.REPLACE);

			CacheEntry previous;
			try {
				previous = (CacheEntry) commands.evalsha(sha, ScriptOutputType.VALUE, new Object[] { entry.getKey() },
						getTtlSeconds(entry), entry);
			} catch (RedisNoScriptException e) {
				previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.REPLACE, ScriptOutputType.VALUE,
						new Object[] { entry.getKey() }, getTtlSeconds(entry), entry);
			}

			if (previous != null) {
				notifyUpdated(previous, entry);
			}
			return previous;
		} catch (Exception e) {
			throw new RedisRuntimeException("can not replace CacheEntry. entry:" + entry, e);
		}
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		if (!oldEntry.getKey().equals(newEntry.getKey())) {
			throw new IllegalArgumentException("oldEntry key not equals newEntry key");
		}

		setTtl(newEntry);
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			String sha = commands.digest(RedisCacheStoreLuaScript.REPLACE_NEW);

			CacheEntry previous;
			try {
				previous = (CacheEntry) commands.evalsha(sha, ScriptOutputType.VALUE,
						new Object[] { newEntry.getKey() }, getTtlSeconds(newEntry), oldEntry, newEntry);
			} catch (RedisNoScriptException e) {
				previous = (CacheEntry) commands.eval(RedisCacheStoreLuaScript.REPLACE_NEW, ScriptOutputType.VALUE,
						new Object[] { newEntry.getKey() }, getTtlSeconds(newEntry), oldEntry, newEntry);
			}

			if (previous != null) {
				notifyUpdated(oldEntry, newEntry);
			}
			return previous != null;
		} catch (Exception e) {
			throw new RedisRuntimeException("can not replace CacheEntry. entry:" + newEntry, e);
		}
	}

	@Override
	public List<Object> keySet() {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			List<Object> keys = commands.keys("*");
			return keys;
		} catch (Exception e) {
			throw new RedisRuntimeException("can not get cache keySet", e);
		}
	}

	@Override
	public void removeAll() {
		if (hasListener()) {
			keySet().forEach(key -> remove(key));
		} else {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				String sha = commands.digest(RedisCacheStoreLuaScript.REMOVE_ALL);

				try {
					commands.evalsha(sha, ScriptOutputType.MULTI, new Object[] { "*" });
				} catch (RedisNoScriptException e) {
					commands.eval(RedisCacheStoreLuaScript.REMOVE_ALL, ScriptOutputType.MULTI, new Object[] { "*" });
				}
			} catch (Exception e) {
				throw new RedisRuntimeException("can not remove all CacheEntry", e);
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
