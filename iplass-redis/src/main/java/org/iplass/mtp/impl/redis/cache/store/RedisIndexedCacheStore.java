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

import org.iplass.mtp.MtpException;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;

public class RedisIndexedCacheStore extends RedisCacheStoreBase {

	private static final Logger logger = LoggerFactory.getLogger(RedisIndexedCacheStore.class);

	private final int indexSize;
	private final RedisCacheStore wrapped;

	private StatefulRedisConnection<String, Object> redisIndexConn;
	private RedisCommands<String, Object> redisIndexCmds;

	public RedisIndexedCacheStore(RedisCacheStoreFactory factory, String namespace, int indexSize, boolean isSave, RedisCacheStore wrapped) {
		super(factory, namespace, wrapped.getTimeToLive(), isSave);

		this.indexSize = indexSize;
		this.wrapped = wrapped;

		redisIndexConn = factory.getClient().connect(new NamespaceSerializedObjectCodec(namespace));
		redisIndexCmds = redisIndexConn.sync();

		wrapped.addPubSubListener(new RedisPubSubListener<String, String>() {
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
				String prefix = wrapped.getCodec().getNamespaceHandler().getPrefix();
				if (message.startsWith(prefix)) {
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Occur expired event. channel:%s, message:%s", channel, message));
					}
					Object key = decodeBase64(message.substring(prefix.length()));
					removeAllFromIndex(key);
				}
			}
		});
	}

	@Override
	public String getNamespace() {
		return wrapped.getNamespace();
	}

	@Override
	public CacheStoreFactory getFactory() {
		return wrapped.getFactory();
	}

	@Override
	public CacheEntry put(CacheEntry entry, boolean clean) {
		try {
			wrapped.multi();
			CacheEntry previous = wrapped.put(redisIndexCmds, entry, clean);
			if (previous != null) {
				removeFromIndex(previous);
			}
			addToIndex(entry);
			wrapped.exec();
			return previous;
		} catch (Exception e) {
			wrapped.discard();
			throw new MtpException(e);
		}
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		try {
			wrapped.multi();
			CacheEntry previous = wrapped.putIfAbsent(redisIndexCmds, entry);
			if (previous == null) {
				addToIndex(entry);
			}
			wrapped.exec();
			return previous;
		} catch (Exception e) {
			wrapped.discard();
			throw new MtpException(e);
		}
	}

	@Override
	public CacheEntry get(Object key) {
		return wrapped.get(key);
	}

	@Override
	public CacheEntry remove(Object key) {
		try {
			wrapped.multi();
			CacheEntry previous = wrapped.remove(redisIndexCmds, key);
			if (previous != null) {
				removeFromIndex(previous);
			}
			wrapped.exec();
			return previous;
		} catch (Exception e) {
			wrapped.discard();
			throw new MtpException(e);
		}
	}

	@Override
	public boolean remove(CacheEntry entry) {
		try {
			wrapped.multi();
			boolean removed = wrapped.remove(redisIndexCmds, entry);
			if (removed) {
				removeFromIndex(entry);
			}
			wrapped.exec();
			return removed;
		} catch (Exception e) {
			wrapped.discard();
			throw new MtpException(e);
		}
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		try {
			wrapped.multi();
			CacheEntry previous = wrapped.replace(redisIndexCmds, entry);
			if (previous != null) {
				removeFromIndex(previous);
				addToIndex(entry);
			}
			wrapped.exec();
			return previous;
		} catch (Exception e) {
			wrapped.discard();
			throw new MtpException(e);
		}
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		try {
			wrapped.multi();
			boolean replaced = wrapped.replace(redisIndexCmds, oldEntry, newEntry);
			if (replaced) {
				removeFromIndex(oldEntry);
				addToIndex(newEntry);
			}
			wrapped.exec();
			return replaced;
		} catch (Exception e) {
			wrapped.discard();
			throw new MtpException(e);
		}
	}

	@Override
	public void removeAll() {
		wrapped.removeAll();
	}

	@Override
	public List<Object> keySet() {
		List<String> keys = redisIndexCmds.keys("*");
		if (keys != null && !keys.isEmpty()) {
			List<Object> keyList = new ArrayList<Object>();
			for (String key : keys) {
				Object keyObj = decodeBase64(key);
				if (!(keyObj instanceof IndexKey)) {	// IndexのKeyは除く
					keyList.add(keyObj);
				}
			}
			return keyList;
		}
		return Collections.emptyList();
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		List<Object> keyList = redisIndexCmds.lrange(encodeBase64(new IndexKey(indexKey, indexValue)), 0L, -1L);
		for (Object key : keyList) {
			if (!isExpired(key)) {
				return wrapped.get(key);
			}
			removeFromIndex(indexKey, indexValue, key);
		}
		return null;
	}

	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		List<Object> keyList = redisIndexCmds.lrange(encodeBase64(new IndexKey(indexKey, indexValue)), 0L, -1L);
		if (keyList != null && !keyList.isEmpty()) {
			List<CacheEntry> entryList = new ArrayList<CacheEntry>();
			for (Object key : keyList) {
				if (!isExpired(key)) {
					entryList.add(wrapped.get(key));
				} else {
					removeFromIndex(indexKey, indexValue, key);
				}
			}
			return entryList;
		}
		return Collections.emptyList();
	}

	@Override
	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
		List<CacheEntry> entryList = getListByIndex(indexKey, indexValue);
		for (CacheEntry entry : entryList) {
			remove(entry.getKey());
		}
		return entryList;
	}

	@Override
	public void addCacheEventListenner(CacheEventListener listener) {
		wrapped.addCacheEventListenner(listener);
	}

	@Override
	public void removeCacheEventListenner(CacheEventListener listener) {
		wrapped.removeCacheEventListenner(listener);
	}

	@Override
	public List<CacheEventListener> getListeners() {
		return wrapped.getListeners();
	}

	@Override
	public String trace() {
		return wrapped.trace();
	}

	@Override
	public void destroy() {
		wrapped.destroy();

		if (redisIndexCmds != null && redisIndexCmds.isOpen()) {
			redisIndexCmds.shutdown(isSave());
			redisIndexCmds = null;
		}
		if (redisIndexConn != null && redisIndexConn.isOpen()) {
			redisIndexConn.close();
			redisIndexConn = null;
		}
	}

	private void pushToIndex(int index, Object indexValue, Object key) {
		wrapped.rpush(encodeBase64(new IndexKey(index, indexValue)),  key);
	}

	private void addToIndex(CacheEntry entry) {
		for (int i = 0; i < indexSize; i++) {
			Object iKey = entry.getIndexValue(i);
			if (iKey instanceof Object[]) {
				Object[] iKeyArray = (Object[]) iKey;
				for (int j = 0; j < iKeyArray.length; j++) {
					if (iKeyArray[j] != null) {
						pushToIndex(i, iKeyArray[j], entry.getKey());
					}
				}
			} else {
				if (iKey != null) {
					pushToIndex(i, iKey, entry.getKey());
				}
			}
		}
	}

	private void removeFromIndex(int indexKey, Object indexValue, Object key) {
		wrapped.lrem(encodeBase64(new IndexKey(indexKey, indexValue)), 0L, key);
	}

	private void removeFromIndex(CacheEntry entry) {
		for (int i = 0; i < indexSize; i++) {
			Object iKey = entry.getIndexValue(i);
			if (iKey instanceof Object[]) {
				Object[] iKeyArray = (Object[]) iKey;
				for (int j = 0; j < iKeyArray.length; j++) {
					if (iKeyArray[j] != null) {
						removeFromIndex(i, iKeyArray[j], entry.getKey());
					}
				}
			} else {
				if (iKey != null) {
					removeFromIndex(i, iKey, entry.getKey());
				}
			}
		}
	}

	private void removeAllFromIndex(Object key) {
		List<String> iKeyList = redisIndexCmds.keys("*");
		try {
			redisIndexCmds.multi();
			for (String iKey : iKeyList) {
				redisIndexCmds.lrem(iKey, 0, key);
			}
			redisIndexCmds.exec();
		} catch (Exception e) {
			redisIndexCmds.discard();
			throw new MtpException(e);
		}
	}

	private boolean isExpired(Object key) {
		return redisIndexCmds.ttl(encodeBase64(key)).longValue() == -2L;
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
