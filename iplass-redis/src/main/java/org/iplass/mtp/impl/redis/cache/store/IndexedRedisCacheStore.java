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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.redis.RedisRuntimeException;
import org.iplass.mtp.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;

public class IndexedRedisCacheStore extends RedisCacheStoreBase {

	private static final Logger logger = LoggerFactory.getLogger(IndexedRedisCacheStore.class);

	private final int retryCount;
	private final int indexSize;

	public IndexedRedisCacheStore(RedisCacheStoreFactory factory, String namespace, long timeToLive, int indexSize,
			RedisCacheStorePoolConfig redisCacheStorePoolConfig) {
		super(factory, namespace, timeToLive, redisCacheStorePoolConfig);

		this.indexSize = indexSize;
		this.retryCount = factory.getRetryCount();

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
					removeAllIndex(key);
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
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				commands.watch(entry.getKey());

				List<IndexKey> indexKeys = getIndexKeysFromEntry(entry);
				commands.watch(indexKeys);

				CacheEntry previous = get(entry.getKey());

				commands.multi();

				if (timeToLive > 0) {
					commands.setex(entry.getKey(), timeToLive, entry);
				} else {
					commands.set(entry.getKey(), entry);
				}

				if (previous != null) {
					removeIndexValues(previous, commands);
				}
				addIndexValues(entry, indexKeys, commands);

				TransactionResult result = commands.exec();
				if (result.wasDiscarded()) {
					continue;
				}

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
		throw new SystemException("can not put CacheEntry cause retry count over:" + entry);
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				commands.watch(entry.getKey());

				List<IndexKey> indexKeys = getIndexKeysFromEntry(entry);
				commands.watch(indexKeys);

				CacheEntry previous = get(entry.getKey());

				if (previous == null) {
					commands.multi();

					if (timeToLive > 0) {
						commands.setex(entry.getKey(), timeToLive, entry);
					} else {
						commands.set(entry.getKey(), entry);
					}

					addIndexValues(entry, indexKeys, commands);

					TransactionResult result = commands.exec();
					if (result.wasDiscarded()) {
						continue;
					}

					notifyPut(entry);
					return null;
				} else {
					commands.unwatch();
					return previous;
				}
			} catch (Exception e) {
				throw new RedisRuntimeException(e);
			}
		}
		throw new SystemException("can not putIfAbsent CacheEntry cause retry count over:" + entry);
	}

	@Override
	public CacheEntry remove(Object key) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			commands.watch(key);

			CacheEntry previous = get(key);

			if (previous != null) {
				List<IndexKey> indexKeys = getIndexKeysFromEntry(previous);
				commands.watch(indexKeys);

				commands.multi();

				commands.del(key);
				removeIndexValues(previous, commands);

				TransactionResult result = commands.exec();
				if (result.wasDiscarded()) {
					throw new SystemException("can not remove CacheEntry. key:" + key);
				}

				notifyRemoved(previous);
				return previous;
			} else {
				commands.unwatch();
				return null;
			}
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public boolean remove(CacheEntry entry) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			commands.watch(entry.getKey());

			CacheEntry previous = get(entry.getKey());

			if (previous != null && previous.equals(entry)) {
				List<IndexKey> indexKeys = getIndexKeysFromEntry(previous);
				commands.watch(indexKeys);

				commands.multi();

				commands.del(entry.getKey());
				removeIndexValues(entry, commands);

				TransactionResult result = commands.exec();
				if (result.wasDiscarded()) {
					throw new SystemException("can not remove CacheEntry. key:" + entry.getKey());
				}

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
			commands.watch(entry.getKey());

			CacheEntry previous = get(entry.getKey());

			if (previous != null) {
				List<IndexKey> indexKeys = getIndexKeysFromEntry(previous);
				commands.watch(indexKeys);

				commands.multi();

				if (timeToLive > 0) {
					commands.setex(entry.getKey(), timeToLive, entry);
				} else {
					commands.set(entry.getKey(), entry);
				}

				removeIndexValues(entry, commands);
				addIndexValues(entry, indexKeys, commands);

				TransactionResult result = commands.exec();
				if (result.wasDiscarded()) {
					throw new SystemException("can not replace CacheEntry. key:" + entry.getKey());
				}

				notifyUpdated(previous, entry);
				return previous;
			} else {
				commands.unwatch();
				return null;
			}
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
			commands.watch(oldEntry.getKey());

			CacheEntry previous = get(oldEntry.getKey());

			if (previous != null && previous.equals(oldEntry)) {
				List<IndexKey> indexKeys = getIndexKeysFromEntry(previous);
				commands.watch(indexKeys);

				commands.multi();

				if (timeToLive > 0) {
					commands.setex(newEntry.getKey(), timeToLive, newEntry);
				} else {
					commands.set(newEntry.getKey(), newEntry);
				}

				removeIndexValues(newEntry, commands);
				addIndexValues(newEntry, indexKeys, commands);

				TransactionResult result = commands.exec();
				if (result.wasDiscarded()) {
					throw new SystemException("can not replace CacheEntry. key:" + newEntry.getKey());
				}

				notifyUpdated(previous, newEntry);
				return true;
			} else {
				commands.unwatch();
				return false;
			}
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public void removeAll() {
		keySet().forEach(key -> remove(key));
	}

	@Override
	public List<Object> keySet() {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			List<Object> keys = commands.keys("*");
			if (CollectionUtil.isNotEmpty(keys)) {
				List<Object> keyList = new ArrayList<Object>();
				keys.forEach(key -> {
					if (!(key instanceof IndexKey)) { // IndexのKeyは除く
						keyList.add(key);
					}
				});
				return keyList;
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public CacheEntry getByIndex(int index, Object indexValue) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();

			IndexKey indexKey = new IndexKey(index, indexValue);
			List<Object> entryKeyList = commands.lrange(indexKey, 0L, -1L);

			for (Object entryKey : entryKeyList) {
				CacheEntry entry = get(entryKey);
				if (entry != null) {
					return entry;
				} else {
					removeIndexValues(entry, commands);
				}
			}
			return null;
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public List<CacheEntry> getListByIndex(int index, Object indexValue) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			IndexKey indexKey = new IndexKey(index, indexValue);
			List<Object> entryKeyList = commands.lrange(indexKey, 0L, -1L);

			if (CollectionUtil.isNotEmpty(entryKeyList)) {
				List<CacheEntry> entryList = new ArrayList<CacheEntry>();
				entryKeyList.forEach(entryKey -> {
					CacheEntry entry = get(entryKey);
					if (entry != null) {
						entryList.add(entry);
					} else {
						removeIndexValues(entry, commands);
					}
				});
				return entryList;
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	@Override
	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
		List<CacheEntry> entryList = getListByIndex(indexKey, indexValue);
		entryList.forEach(entry -> remove(entry.getKey()));
		return entryList;
	}

	private List<IndexKey> getIndexKeysFromEntry(CacheEntry entry) {
		List<IndexKey> indexKeys = new ArrayList<>();

		for (int i = 0; i < indexSize; i++) {
			Object indexValue = entry.getIndexValue(i);
			if (indexValue instanceof Object[]) {
				Object[] indexValueArray = (Object[]) indexValue;
				for (int j = 0; j < indexValueArray.length; j++) {
					if (indexValueArray[j] != null) {
						indexKeys.add(new IndexKey(i, indexValueArray[j]));
					}
				}
			} else {
				if (indexValue != null) {
					indexKeys.add(new IndexKey(i, indexValue));
				}
			}
		}

		return indexKeys;
	}

	private void addIndexValues(CacheEntry entry, List<IndexKey> indexKeys, RedisCommands<Object, Object> commands) {
		for (IndexKey indexKey : indexKeys) {
			pushIndex(indexKey, entry.getKey(), commands);
		}
	}

	private void pushIndex(IndexKey indexKey, Object entryKey, RedisCommands<Object, Object> commands) {
		commands.rpush(indexKey, entryKey);
	}

	private void removeIndex(IndexKey indexKey, Object entryKey, RedisCommands<Object, Object> commands) {
		commands.lrem(indexKey, 0L, entryKey);
	}

	private void removeIndexValues(CacheEntry entry, RedisCommands<Object, Object> commands) {
		List<IndexKey> indexKeys = getIndexKeysFromEntry(entry);
		for (IndexKey indexKey : indexKeys) {
			removeIndex(indexKey, entry.getKey(), commands);
		}
	}

	private void removeAllIndex(Object key) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			List<Object> indexKeyList = commands.keys("*");
			commands.multi();
			indexKeyList.forEach(indexKey -> commands.lrem(indexKey, 0, key));
			commands.exec();
		} catch (Exception e) {
			throw new RedisRuntimeException(e);
		}
	}

	private static final class IndexKey implements Serializable {
		private static final long serialVersionUID = -2752341127081890863L;

		private int index;
		private Object value;

		public IndexKey(int index, Object value) {
			this.index = index;
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + index;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			IndexKey other = (IndexKey) obj;
			if (index != other.index)
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "IndexKey [index=" + index + ", value=" + value + "]";
		}
	}

}
