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
import java.util.function.BiFunction;
import java.util.function.Function;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.TimeToLiveCalculator;
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

	private final int indexSize;

	public IndexedRedisCacheStore(RedisCacheStoreFactory factory, String namespace,
			TimeToLiveCalculator timeToLiveCalculator, int indexSize,
			RedisCacheStorePoolConfig redisCacheStorePoolConfig) {
		super(factory, namespace, timeToLiveCalculator, redisCacheStorePoolConfig);

		this.indexSize = indexSize;

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
					removeAllIndexByEntryKey(key);
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
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();

				commands.watch(entry.getKey());

				CacheEntry previous = get(entry.getKey());
				List<IndexKey> previousIndexKeys = null;
				if (previous != null) {
					previousIndexKeys = getIndexKeysFromEntry(previous);
					commands.watch(previousIndexKeys);
				}

				commands.multi();

				if (entry.getTimeToLive().longValue() > 0L) {
					commands.setex(entry.getKey(), getTtlSeconds(entry), entry);
				} else {
					commands.set(entry.getKey(), entry);
				}
				if (previous != null && previousIndexKeys != null) {
					removeIndexValues(previous, previousIndexKeys, commands);
				}
				addIndexValues(entry, commands);

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
				throw new RedisRuntimeException("can not put CacheEntry. entry:" + entry, e);
			}
		}
		throw new SystemException("can not put CacheEntry cause retry count over. entry:" + entry);
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		setTtl(entry);
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				commands.watch(entry.getKey());

				CacheEntry previous = get(entry.getKey());

				if (previous == null) {
					commands.multi();

					if (entry.getTimeToLive().longValue() >= 0L) {
						commands.setex(entry.getKey(), getTtlSeconds(entry), entry);
					} else {
						commands.set(entry.getKey(), entry);
					}
					addIndexValues(entry, commands);

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
				throw new RedisRuntimeException("can not putIfAbsent CacheEntry. entry:" + entry, e);
			}
		}
		throw new SystemException("can not putIfAbsent CacheEntry cause retry count over:" + entry);
	}

	@Override
	public CacheEntry compute(Object key, BiFunction<Object, CacheEntry, CacheEntry> remappingFunction) {
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();

				commands.watch(key);

				CacheEntry oldEntry = get(key);
				List<IndexKey> oldIndexKeys = null;
				if (oldEntry != null) {
					oldIndexKeys = getIndexKeysFromEntry(oldEntry);
					commands.watch(oldIndexKeys);
				}

				CacheEntry newEntry = remappingFunction.apply(key, oldEntry);
				if (newEntry == null) {
					if (oldEntry != null) {
						commands.multi();

						// remove
						commands.del(key);
						removeIndexValues(oldEntry, oldIndexKeys, commands);

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
						// replace
						if (!oldEntry.getKey().equals(newEntry.getKey())) {
							throw new IllegalArgumentException("oldEntry key not equals newEntry key");
						}

						commands.multi();

						if (newEntry.getTimeToLive().longValue() >= 0L) {
							commands.setex(newEntry.getKey(), getTtlSeconds(newEntry), newEntry);
						} else {
							commands.set(newEntry.getKey(), newEntry);
						}
						removeIndexValues(oldEntry, oldIndexKeys, commands);
						addIndexValues(newEntry, commands);

						TransactionResult result = commands.exec();
						if (result.wasDiscarded()) {
							continue;
						}

						notifyUpdated(oldEntry, newEntry);
						return newEntry;
					} else {
						commands.multi();

						// putIfAbsent
						if (newEntry.getTimeToLive().longValue() >= 0L) {
							commands.setex(newEntry.getKey(), getTtlSeconds(newEntry), newEntry);
						} else {
							commands.set(newEntry.getKey(), newEntry);
						}
						addIndexValues(newEntry, commands);

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
						if (newEntry.getTimeToLive().longValue() >= 0L) {
							commands.setex(newEntry.getKey(), getTtlSeconds(newEntry), newEntry);
						} else {
							commands.set(newEntry.getKey(), newEntry);
						}
						addIndexValues(newEntry, commands);

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
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				commands.watch(key);

				CacheEntry previous = get(key);

				if (previous != null) {
					List<IndexKey> previousIndexKeys = getIndexKeysFromEntry(previous);
					commands.watch(previousIndexKeys);

					commands.multi();

					commands.del(key);
					removeIndexValues(previous, previousIndexKeys, commands);

					TransactionResult result = commands.exec();
					if (result.wasDiscarded()) {
						continue;
					}

					notifyRemoved(previous);
					return previous;
				} else {
					commands.unwatch();
					return null;
				}
			} catch (Exception e) {
				throw new RedisRuntimeException("can not remove CacheEntry. key:" + key, e);
			}
		}
		throw new SystemException("can not remove CacheEntry cause retry count over. key:" + key);
	}

	@Override
	public boolean remove(CacheEntry entry) {
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				commands.watch(entry.getKey());

				CacheEntry previous = get(entry.getKey());

				if (previous != null && previous.equals(entry)) {
					List<IndexKey> previousIndexKeys = getIndexKeysFromEntry(previous);
					commands.watch(previousIndexKeys);

					commands.multi();

					commands.del(entry.getKey());
					removeIndexValues(previous, previousIndexKeys, commands);

					TransactionResult result = commands.exec();
					if (result.wasDiscarded()) {
						continue;
					}

					notifyRemoved(previous);
					return true;
				}
				return false;
			} catch (Exception e) {
				throw new RedisRuntimeException("can not remove CacheEntry. entry:" + entry, e);
			}
		}
		throw new SystemException("can not remove CacheEntry cause retry count over. key:" + entry.getKey());
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		setTtl(entry);
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				commands.watch(entry.getKey());

				CacheEntry previous = get(entry.getKey());

				if (previous != null) {
					List<IndexKey> previousIndexKeys = getIndexKeysFromEntry(previous);
					commands.watch(previousIndexKeys);

					commands.multi();

					if (entry.getTimeToLive().longValue() >= 0L) {
						commands.setex(entry.getKey(), getTtlSeconds(entry), entry);
					} else {
						commands.set(entry.getKey(), entry);
					}
					removeIndexValues(previous, previousIndexKeys, commands);
					addIndexValues(entry, commands);

					TransactionResult result = commands.exec();
					if (result.wasDiscarded()) {
						continue;
					}

					notifyUpdated(previous, entry);
					return previous;
				} else {
					commands.unwatch();
					return null;
				}
			} catch (Exception e) {
				throw new RedisRuntimeException("can not replace CacheEntry. key:" + entry.getKey(), e);
			}
		}
		throw new SystemException("can not replace CacheEntry. key:" + entry.getKey());
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		if (!oldEntry.getKey().equals(newEntry.getKey())) {
			throw new IllegalArgumentException("oldEntry key not equals newEntry key");
		}

		setTtl(newEntry);
		for (int count = 0; count <= retryCount; count++) {
			try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
				RedisCommands<Object, Object> commands = connection.sync();
				commands.watch(oldEntry.getKey());

				CacheEntry previous = get(oldEntry.getKey());

				if (previous != null && previous.equals(oldEntry)) {
					List<IndexKey> previousIndexKeys = getIndexKeysFromEntry(previous);
					commands.watch(previousIndexKeys);

					commands.multi();

					if (newEntry.getTimeToLive().longValue() >= 0L) {
						commands.setex(newEntry.getKey(), getTtlSeconds(newEntry), newEntry);
					} else {
						commands.set(newEntry.getKey(), newEntry);
					}
					removeIndexValues(oldEntry, previousIndexKeys, commands);
					addIndexValues(newEntry, commands);

					TransactionResult result = commands.exec();
					if (result.wasDiscarded()) {
						continue;
					}

					notifyUpdated(previous, newEntry);
					return true;
				} else {
					commands.unwatch();
					return false;
				}
			} catch (Exception e) {
				throw new RedisRuntimeException("can not replace CacheEntry. key:" + newEntry.getKey(), e);
			}
		}
		throw new SystemException("can not replace CacheEntry. key:" + newEntry.getKey());
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
			throw new RedisRuntimeException("can not get keySet", e);
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
					// 有効期限切れのCacheEntryに紐づくインデックスを削除する
					removeIndex(new IndexKey(index, indexValue), entryKey, commands);
				}
			}
			return null;
		} catch (Exception e) {
			throw new RedisRuntimeException(
					"can not getByIndex CacheEntry. index:" + index + ", indexValue:" + indexValue, e);
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
						// 有効期限切れのCacheEntryに紐づくインデックスを削除する
						removeIndex(new IndexKey(index, indexValue), entryKey, commands);
					}
				});
				return entryList;
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new RedisRuntimeException(
					"can not getListByIndex CacheEntry. index:" + index + ", indexValue:" + indexValue, e);
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

	private void addIndexValues(CacheEntry entry, RedisCommands<Object, Object> commands) {
		List<IndexKey> indexKeys = getIndexKeysFromEntry(entry);
		for (IndexKey indexKey : indexKeys) {
			commands.rpush(indexKey, entry.getKey());
		}
	}

	private void removeIndexValues(CacheEntry entry, List<IndexKey> indexKeys, RedisCommands<Object, Object> commands) {
		for (IndexKey indexKey : indexKeys) {
			removeIndex(indexKey, entry.getKey(), commands);
		}
	}

	private void removeIndex(IndexKey indexKey, Object entryKey, RedisCommands<Object, Object> commands) {
		commands.lrem(indexKey, 0L, entryKey);
	}

	// TODO 削除方法が非効率
	private void removeAllIndexByEntryKey(Object entryKey) {
		try (StatefulRedisConnection<Object, Object> connection = pool.borrowObject()) {
			RedisCommands<Object, Object> commands = connection.sync();
			List<Object> keyList = commands.keys("*");
			commands.multi();
			keyList.forEach(key -> {
				if (key instanceof IndexKey) {
					commands.lrem(key, 0, entryKey);
				}
			});
			commands.exec();
		} catch (Exception e) {
			throw new RedisRuntimeException("can not removeAllIndexByEntryKey. key:" + entryKey, e);
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
