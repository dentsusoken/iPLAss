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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.pubsub.RedisPubSubListener;

public class IndexedRedisCacheStore extends RedisCacheStoreBase {

	private static final Logger logger = LoggerFactory.getLogger(IndexedRedisCacheStore.class);

	private final int indexSize;

	public IndexedRedisCacheStore(RedisCacheStoreFactory factory, String namespace, long timeToLive, int indexSize,
			boolean isSave) {
		super(factory, namespace, timeToLive, isSave);

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
					Object key = codec.decodeKey(ByteBuffer.wrap(codec.getCharset().encode(message).array()));
					removeAllFromIndex(key);
				}
			}
		});
	}

	@Override
	public CacheEntry put(CacheEntry entry, boolean clean) {
		/*
		 * CacheEntry previous = wrapped.put(redisIndexCmds, entry, false); if (previous
		 * != null) { removeFromIndex(previous); } addToIndex(entry); wrapped.exec(); if
		 * (previous == null) { wrapped.notifyPut(entry); } else {
		 * wrapped.notifyUpdated(previous, entry); } return previous;
		 */
		return null;
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		/*
		 * CacheEntry previous = wrapped.putIfAbsent(redisIndexCmds, entry, false); if
		 * (previous == null) { addToIndex(entry); wrapped.exec();
		 * wrapped.notifyPut(entry); } return previous;
		 */
		return null;
	}

	@Override
	public CacheEntry get(Object key) {
		return key != null ? (CacheEntry) commands.get(key) : null;
	}

	@Override
	public CacheEntry remove(Object key) {
		/*
		 * CacheEntry previous = wrapped.remove(redisIndexCmds, key, false); if
		 * (previous != null) { removeFromIndex(previous); wrapped.exec();
		 * wrapped.notifyRemoved(previous); } return previous;
		 */
		return null;
	}

	@Override
	public boolean remove(CacheEntry entry) {
		/*
		 * CacheEntry previous = wrapped.remove(redisIndexCmds, entry, false); if
		 * (previous != null) { removeFromIndex(entry); wrapped.exec();
		 * wrapped.notifyRemoved(previous); return true; } return false;
		 */
		return false;
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		/*
		 * CacheEntry previous = wrapped.replace(redisIndexCmds, entry, false); if
		 * (previous != null) { removeFromIndex(previous); addToIndex(entry);
		 * wrapped.exec(); wrapped.notifyUpdated(previous, entry); } return previous;
		 */
		return null;
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		/*
		 * boolean previous = wrapped.replace(redisIndexCmds, oldEntry, newEntry,
		 * false); if (previous) { removeFromIndex(oldEntry); addToIndex(newEntry);
		 * wrapped.exec(); wrapped.notifyUpdated(oldEntry, newEntry); } return previous;
		 */
		return false;
	}

	@Override
	public void removeAll() {
		keySet().forEach(key -> remove(key));
	}

	@Override
	public List<Object> keySet() {
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
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		List<Object> keyList = commands.lrange(new IndexKey(indexKey, indexValue), 0L, -1L);
		for (Object key : keyList) {
			if (!isExpired(key)) {
				return get(key);
			}
			removeFromIndex(indexKey, indexValue, key);
		}
		return null;
	}

	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		List<Object> keyList = commands.lrange(new IndexKey(indexKey, indexValue), 0L, -1L);
		if (keyList != null && !keyList.isEmpty()) {
			List<CacheEntry> entryList = new ArrayList<CacheEntry>();
			keyList.forEach(key -> {
				if (!isExpired(key)) {
					entryList.add(get(key));
				} else {
					removeFromIndex(indexKey, indexValue, key);
				}
			});
			return entryList;
		}
		return Collections.emptyList();
	}

	@Override
	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
		List<CacheEntry> entryList = getListByIndex(indexKey, indexValue);
		entryList.forEach(entry -> remove(entry.getKey()));
		return entryList;
	}

	private void pushToIndex(int index, Object indexValue, Object key) {
		commands.rpush(new IndexKey(index, indexValue), key);
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
		commands.lrem(new IndexKey(indexKey, indexValue), 0L, key);
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
		List<Object> iKeyList = commands.keys("*");
		commands.multi();
		iKeyList.forEach(iKey -> commands.lrem(iKey, 0, key));
		commands.exec();
	}

	private boolean isExpired(Object key) {
		return commands.ttl(key).longValue() == -2L;
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
