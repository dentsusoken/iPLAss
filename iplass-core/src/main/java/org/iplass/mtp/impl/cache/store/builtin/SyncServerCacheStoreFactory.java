/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.keyresolver.CacheKeyResolver;
import org.iplass.mtp.impl.cluster.ClusterEventListener;
import org.iplass.mtp.impl.cluster.ClusterService;
import org.iplass.mtp.impl.cluster.Message;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 信頼性の低い、サーバ間のキャッシュを同期するCacheStoreFactory。
 * 実ストアのtimeToLiveの設定で一定間隔でキャッシュがリフレッシュされる前提。
 * 他のサーバがキャッシュの更新処理中にダウンした場合などは、
 * 最悪、そのtimeToLiveの間、キャッシュと実データの間で不整合が発生する可能性あり。
 *
 * @author K.Higuchi
 *
 */
public class SyncServerCacheStoreFactory extends AbstractBuiltinCacheStoreFactory {

	//TODO とりあえず、、、通信回数が多すぎる。。。同一サーバへのメッセージをまとめられないか？

	private static Logger logger = LoggerFactory.getLogger(SyncServerCacheStoreFactory.class);

	private static final String CLUSTER_EVENT_NAME_MARK_DIRTY = "mtp.cache.md/";
	private static final String CLUSTER_EVENT_NAME_REMOVE_ALL = "mtp.cache.ra/";
	private static final String CLUSTER_EVENT_NAME_REMOVE_BY_INDEX = "mtp.cache.ri/";
	private static final String CLUSTER_MESSAGE_CACHE_KEY = "key";
	private static final String CLUSTER_MESSAGE_INDEX_PREFIX = "i";

	private static final String CLUSTER_MESSAGE_INDEX_KEY = "ikey";
	private static final String CLUSTER_MESSAGE_INDEX_VALUE = "ival";

	private CacheStoreFactory store;
	private CacheKeyResolver cacheKeyResolver;
	private List<CacheKeyResolver> cacheIndexResolver;
	private SyncServerCacheEventListener listener;

	public SyncServerCacheEventListener getListener() {
		return listener;
	}

	public void setListener(SyncServerCacheEventListener listener) {
		this.listener = listener;
	}

	@Override
	public void setIndexCount(int indexCount) {
		super.setIndexCount(indexCount);
		if (store != null) {
			store.setIndexCount(indexCount);
		}
	}

	public CacheKeyResolver getCacheKeyResolver() {
		return cacheKeyResolver;
	}

	public void setCacheKeyResolver(CacheKeyResolver cacheKeyResolver) {
		this.cacheKeyResolver = cacheKeyResolver;
	}

	public List<CacheKeyResolver> getCacheIndexResolver() {
		return cacheIndexResolver;
	}

	public void setCacheIndexResolver(List<CacheKeyResolver> cacheIndexResolver) {
		this.cacheIndexResolver = cacheIndexResolver;
	}

	public CacheStoreFactory getStore() {
		return store;
	}

	public void setStore(CacheStoreFactory store) {
		store.setIndexCount(getIndexCount());
		this.store = store;
	}

	@Override
	public CacheStore createCacheStore(String namespace) {
		CacheStore wrapped = store.createCacheStore(namespace);
		return new SyncServerCacheStore(namespace, wrapped);
	}

	@Override
	public boolean canUseForLocalCache() {
		return false;
	}

	@Override
	public boolean supportsIndex() {
		return store.supportsIndex();
	}

	public class SyncServerCacheStore implements CacheStore {

		private final String namespace;
		private final CacheStore wrapped;
		private final String eventNameMarkDirty;
		private final String eventNameClearAll;
		private final String eventNameRemoveByIndex;
		private final ClusterEventListener cel;

		SyncServerCacheStore(final String namespace, final CacheStore wrapped) {
			this.namespace = namespace;
			this.eventNameMarkDirty = CLUSTER_EVENT_NAME_MARK_DIRTY + namespace;
			this.eventNameClearAll = CLUSTER_EVENT_NAME_REMOVE_ALL + namespace;
			this.eventNameRemoveByIndex = CLUSTER_EVENT_NAME_REMOVE_BY_INDEX + namespace;
			this.wrapped = wrapped;
			this.cel = new ClusterEventListener() {
					@Override
					public void onMessage(Message msg) {
						if (msg.getEventName().startsWith(CLUSTER_EVENT_NAME_MARK_DIRTY)) {
							Object key = cacheKeyResolver.toCacheKey(msg.getParameter(CLUSTER_MESSAGE_CACHE_KEY));
							Object[] index = null;
							if (getIndexCount() > 0) {
								index = new Object[getIndexCount()];
								for (int i = 0; i < index.length; i++) {
									String iVal = msg.getParameter(CLUSTER_MESSAGE_INDEX_PREFIX + i);
									if (iVal != null) {
										index[i] = cacheIndexResolver.get(i).toCacheKey(iVal);
									}
								}
							}
							CacheEntry entry = new CacheEntry(key, null, 0, index);
							wrapped.remove(key);
							List<CacheEventListener> ls = wrapped.getListeners();
							if (ls.size() > 0) {
								CacheInvalidateEvent e = new CacheInvalidateEvent(entry);
								for (CacheEventListener l: wrapped.getListeners()) {
									l.invalidated(e);
								}
							}
							if (listener != null) {
								listener.markDirty(namespace, entry);
							}
							if (logger.isDebugEnabled()) {
								logger.debug("remove cache entry by cluster message.namespace=" + namespace + ", key=" + key);
							}
						} else if (msg.getEventName().startsWith(CLUSTER_EVENT_NAME_REMOVE_BY_INDEX)) {
							int indexKey = Integer.parseInt(msg.getParameter(CLUSTER_MESSAGE_INDEX_KEY));
							String iValStr = msg.getParameter(CLUSTER_MESSAGE_INDEX_VALUE);
							if (iValStr != null) {
								Object iVal = cacheIndexResolver.get(indexKey).toCacheKey(iValStr);
								wrapped.removeByIndex(indexKey, iVal);
								if (logger.isDebugEnabled()) {
									logger.debug("remove cache entry by cluster message.namespace=" + namespace + ", indexKey=" + indexKey + ", indexValue=" + iVal);
								}
							}
						} else {
							wrapped.removeAll();
							if (listener != null) {
								listener.removeAll(namespace);
							}
							if (logger.isDebugEnabled()) {
								logger.debug("remove all cache entry by cluster message.namespace=" + namespace);
							}
						}
					}
			};
			
			ServiceRegistry.getRegistry().getService(ClusterService.class).registerListener(
					new String[]{eventNameMarkDirty, eventNameClearAll, eventNameRemoveByIndex}, cel);
		}

		@Override
		public String getNamespace() {
			return namespace;
		}

		@Override
		public CacheEntry put(CacheEntry entry, boolean isClean) {
			CacheEntry previous = wrapped.put(entry, isClean);
			if (!isClean) {
				sendByKeyEvent(entry);
			}
			return previous;
		}

		@Override
		public CacheEntry putIfAbsent(CacheEntry entry) {
			CacheEntry e = wrapped.putIfAbsent(entry);
			if (e == null) {
				sendByKeyEvent(entry);
			}
			return e;
		}

		@Override
		public CacheEntry get(Object key) {
			return wrapped.get(key);
		}

		@Override
		public CacheEntry remove(Object key) {
			CacheEntry e = wrapped.remove(key);
			if (e != null) {
				sendByKeyEvent(e);
			} else {
				//無効化を他サーバへ通知
				CacheEntry forInvalidation = new CacheEntry(key, null);
				sendByKeyEvent(forInvalidation);
			}
			return e;
		}

		@Override
		public boolean remove(CacheEntry entry) {
			boolean isRemove = wrapped.remove(entry);
			if (isRemove) {
				sendByKeyEvent(entry);
			}
			return isRemove;
		}

		@Override
		public CacheEntry replace(CacheEntry entry) {
			CacheEntry e = wrapped.replace(entry);
			if (e != null) {
				sendByKeyEvent(entry);
			}
			return e;
		}

		@Override
		public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
			boolean isReplace = wrapped.replace(oldEntry, newEntry);
			if (isReplace) {
				sendByKeyEvent(newEntry);
			}
			return isReplace;
		}

		@Override
		public void removeAll() {
			wrapped.removeAll();
			Message msg = new Message(eventNameClearAll);
			ServiceRegistry.getRegistry().getService(ClusterService.class).sendMessage(msg);
		}

		@Override
		public List<Object> keySet() {
			return wrapped.keySet();
		}

		@Override
		public CacheEntry getByIndex(int indexKey, Object indexValue) {
			return wrapped.getByIndex(indexKey, indexValue);
		}

		@Override
		public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
			return wrapped.getListByIndex(indexKey, indexValue);
		}

		@Override
		public void addCacheEventListenner(CacheEventListener listener) {
			wrapped.addCacheEventListenner(listener);
		}

		@Override
		public void removeCacheEventListenner(CacheEventListener listener) {
			wrapped.addCacheEventListenner(listener);
		}

		private void sendByKeyEvent(CacheEntry entry) {
			Message msg = new Message(eventNameMarkDirty);
			if (entry.getKey() != null) {
				msg.addParameter(CLUSTER_MESSAGE_CACHE_KEY, cacheKeyResolver.toString(entry.getKey()));
			}
			if (getIndexCount() > 0) {
				for (int i = 0; i < getIndexCount(); i++) {
					if (entry.getIndexValue(i) != null) {
						msg.addParameter(CLUSTER_MESSAGE_INDEX_PREFIX + i, cacheIndexResolver.get(i).toString(entry.getIndexValue(i)));
					}
				}
			}
			ServiceRegistry.getRegistry().getService(ClusterService.class).sendMessage(msg);
		}

		@Override
		public CacheStoreFactory getFactory() {
			return SyncServerCacheStoreFactory.this;
		}

//		@Override
//		public void invalidate(CacheEntry entry) {
//			wrapped.invalidate(entry);
//			sendByKeyEvent(entry);
//		}
//
		@Override
		public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
			List<CacheEntry> ret = wrapped.removeByIndex(indexKey, indexValue);
			Message msg = new Message(eventNameRemoveByIndex);
			msg.addParameter(CLUSTER_MESSAGE_INDEX_KEY, Integer.toString(indexKey));
			if (indexValue != null) {
				String iValStr = cacheIndexResolver.get(indexKey).toString(indexValue);
				msg.addParameter(CLUSTER_MESSAGE_INDEX_VALUE, iValStr);
			}
			ServiceRegistry.getRegistry().getService(ClusterService.class).sendMessage(msg);
			return ret;
		}

		@Override
		public String trace() {
			StringBuilder builder = new StringBuilder();
			builder.append("-----------------------------------");
			builder.append("\nCacheStore Info");
			builder.append("\nCacheStore:" + this);
			builder.append("\n\tnamespace:" + namespace);
			builder.append("\n\teventNameMarkDirty:" + eventNameMarkDirty);
			builder.append("\n\teventNameClearAll:" + eventNameClearAll);
			builder.append("\n\teventNameRemoveByIndex:" + eventNameRemoveByIndex);
			builder.append("\n\twrapped:" + wrapped);

			builder.append("\n" + wrapped.trace());
			builder.append("\n-----------------------------------");

			return builder.toString();
		}

		@Override
		public void destroy() {
			ServiceRegistry.getRegistry().getService(ClusterService.class).removeListener(
					new String[]{eventNameMarkDirty, eventNameClearAll, eventNameRemoveByIndex}, cel);
			wrapped.destroy();
		}

		@Override
		public List<CacheEventListener> getListeners() {
			return wrapped.getListeners();
		}
	}

	@Override
	public CacheHandler createCacheHandler(CacheStore store) {
		return new SimpleLocalCacheHandler(store, getConcurrencyLevelOfCacheHandler());
	}

	@Override
	public CacheStoreFactory getLowerLevel() {
		return store;
	}
}
