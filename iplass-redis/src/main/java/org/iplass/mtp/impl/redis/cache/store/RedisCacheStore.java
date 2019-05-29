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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

public class RedisCacheStore extends RedisCacheStoreBase {

	private static final Logger logger = LoggerFactory.getLogger(RedisCacheStore.class);

	private StatefulRedisConnection<String, Object> redisConn;
	private RedisCommands<String, Object> redisCmds;

	private StatefulRedisPubSubConnection<String, String> psConn;
	private RedisPubSubCommands<String, String> psCmds;

	private NamespaceSerializedObjectCodec codec;

	public RedisCacheStore(RedisCacheStoreFactory factory, String namespace, long timeToLive, boolean isSave) {
		super(factory, namespace, timeToLive, isSave);

		codec = new NamespaceSerializedObjectCodec(namespace);

		redisConn = factory.getClient().connect(codec);
		redisCmds = redisConn.sync();
		psConn = factory.getClient().connectPubSub();
		psConn.addListener(new RedisPubSubListener<String, String>() {
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
				String prefix = codec.getNamespaceHandler().getPrefix();
				if (message.startsWith(prefix)) {
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Occuer expired event. channel:%s, message:%s", channel, message));
					}
					Object key = decodeBase64(message.substring(prefix.length()));
					notifyRemoved(new CacheEntry(key, null));
				}
			}
		});
		psCmds = psConn.sync();
		psCmds.subscribe("__keyevent@0__:expired");		// Expiredイベントのみ受信、DB番号は0固定
	}

	private boolean isExist(RedisCommands<String, Object> readCmds, String... keys) {
		return readCmds.exists(keys).compareTo(Long.valueOf(0L)) > 0;
	}

	CacheEntry put(RedisCommands<String, Object> readCmds, CacheEntry entry, boolean doExec) {
		String strKey = encodeBase64(entry.getKey());
		watch(strKey);
		CacheEntry previous = (CacheEntry) readCmds.get(strKey);
		multi();
		if (getTimeToLive() > 0) {
			redisCmds.setex(strKey, getTimeToLive(), entry);
		} else {
			redisCmds.set(strKey, entry);
		}
		if (doExec) { exec(); }
		return previous;
	}

	@Override
	public CacheEntry put(CacheEntry entry, boolean clean) {
		CacheEntry previous = put(redisCmds, entry, true);
		if (previous == null) {
			notifyPut(entry);
		} else {
			notifyUpdated(previous, entry);
		}
		return previous;
	}

	CacheEntry putIfAbsent(RedisCommands<String, Object> readCmds, CacheEntry entry, boolean doExec) {
		String strKey = encodeBase64(entry.getKey());
		watch(strKey);
		CacheEntry previous = (CacheEntry) readCmds.get(strKey);
		if (previous == null) {
			multi();
			if (getTimeToLive() > 0) {
				redisCmds.setex(strKey, getTimeToLive(), entry);
			} else {
				redisCmds.set(strKey, entry);
			}
			if (doExec) { exec(); }
		} else {
			unwatch();
		}
		return previous;
	}

	@Override
	public CacheEntry putIfAbsent(CacheEntry entry) {
		CacheEntry previous = putIfAbsent(redisCmds, entry, true);
		if (previous == null) {
			notifyPut(entry);
		}
		return previous;
	}

	@Override
	public CacheEntry get(Object key) {
		return key != null ? (CacheEntry) redisCmds.get(encodeBase64(key)) : null;
	}

	private CacheEntry remove(RedisCommands<String, Object> readCmds, String key, boolean doExec) {
		watch(key);
		if (isExist(readCmds, key)) {
			CacheEntry previous = (CacheEntry) readCmds.get(key);
			multi();
			redisCmds.del(key);
			if (doExec) { exec(); }
			return previous;
		}
		unwatch();
		return null;
	}

	CacheEntry remove(RedisCommands<String, Object> readCmds, Object key, boolean doExec) {
		if (key == null) { return null; }
		CacheEntry previous = remove(readCmds, encodeBase64(key), doExec);
		return previous;
	}

	@Override
	public CacheEntry remove(Object key) {
		CacheEntry previous = remove(redisCmds, key, true);
		if (previous != null) {
			notifyRemoved(previous);
		}
		return previous;
	}

	CacheEntry remove(RedisCommands<String, Object> readCmds, CacheEntry entry, boolean doExec) {
		String strKey = encodeBase64(entry.getKey());
		watch(strKey);
		if (isExist(readCmds, strKey)) {
			CacheEntry previous = (CacheEntry) readCmds.get(strKey);
			if (previous != null && previous.equals(entry)) {
				multi();
				redisCmds.del(strKey);
				if (doExec) { exec(); }
				return previous;
			}
		}
		unwatch();
		return null;
	}

	@Override
	public boolean remove(CacheEntry entry) {
		CacheEntry previous = remove(redisCmds, entry, true);
		if (previous != null && previous.equals(entry)) {
			notifyRemoved(previous);
			return true;
		}
		return false;
	}

	CacheEntry replace(RedisCommands<String, Object> readCmds, CacheEntry entry, boolean doExec) {
		String strKey = encodeBase64(entry.getKey());
		watch(strKey);
		if (isExist(readCmds, strKey)) {
			CacheEntry preEntry = (CacheEntry) readCmds.get(strKey);
			multi();
			if (getTimeToLive() > 0) {
				redisCmds.setex(strKey, getTimeToLive(), entry);
			} else {
				redisCmds.set(strKey, entry);
			}
			if (doExec) { exec(); }
			return preEntry;
		}
		unwatch();
		return null;
	}

	@Override
	public CacheEntry replace(CacheEntry entry) {
		CacheEntry previous = replace(redisCmds, entry, true);
		if (previous != null) {
			notifyUpdated(previous, entry);
		}
		return previous;
	}

	boolean replace(RedisCommands<String, Object> readCmds, CacheEntry oldEntry, CacheEntry newEntry, boolean doExec) {
		if (!oldEntry.getKey().equals(newEntry.getKey())) {
			throw new IllegalArgumentException("oldEntry key not equals newEntry key");
		}
		String strKey = encodeBase64(newEntry.getKey());
		watch(strKey);
		if (isExist(readCmds, strKey) && oldEntry.equals(readCmds.get(strKey))) {
			return replace(readCmds, newEntry, doExec) != null;
		}
		unwatch();
		return false;
	}

	@Override
	public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
		boolean previous = replace(redisCmds, oldEntry, newEntry, true);
		if (previous) {
			notifyUpdated(oldEntry, newEntry);
		}
		return previous;
	}

	@Override
	public void removeAll() {
		if (hasListener()) {
			keySet().forEach(key -> remove(key));
		} else {
			List<String> keys = redisCmds.keys("*");
			if (keys != null && !keys.isEmpty()) {
				redisCmds.del(keys.toArray(new String[keys.size()]));
			}
		}
	}

	@Override
	public List<Object> keySet() {
		List<String> keys = redisCmds.keys("*");
		if (keys != null && !keys.isEmpty()) {
			List<Object> keyList = new ArrayList<Object>();
			keys.forEach(key -> keyList.add(decodeBase64(key)));
			return keyList;
		}
		return Collections.emptyList();
	}

	@Override
	public CacheEntry getByIndex(int indexKey, Object indexValue) {
		return null;
	}

	@Override
	public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
		return null;
	}

	@Override
	public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
		return null;
	}

	@Override
	public String trace() {
		return redisCmds.info();
	}

	@Override
	public void destroy() {
		if (psCmds != null && psCmds.isOpen()) {
			psCmds.unsubscribe("__keyevent@0__:expired");
			psCmds.shutdown(false);
			psCmds = null;
		}
		if (psConn != null && psConn.isOpen()) {
			psConn.close();
			psConn = null;
		}
		if (redisCmds != null && redisCmds.isOpen()) {
			redisCmds.shutdown(isSave());
			redisCmds = null;
		}
		if (redisConn != null && redisConn.isOpen()) {
			redisConn.close();
			redisConn = null;
		}
	}

	NamespaceSerializedObjectCodec getCodec() {
		return codec;
	}

	void addPubSubListener(RedisPubSubListener<String, String> listener) {
		psConn.addListener(listener);
	}

	String multi() {
		return redisCmds.multi();
	}

	TransactionResult exec() {
		return redisCmds.exec();
	}

	String discard() {
		return redisCmds.discard();
	}

	String watch(String... keys) {
		return redisCmds.watch(keys);
	}

	String unwatch() {
		return redisCmds.unwatch();
	}

	Long lrem(String key, long count, Object value) {
		return redisCmds.lrem(key, count, value);
	}

	Long rpush(String key, Object... values) {
		return redisCmds.rpush(key, values);
	}

}
