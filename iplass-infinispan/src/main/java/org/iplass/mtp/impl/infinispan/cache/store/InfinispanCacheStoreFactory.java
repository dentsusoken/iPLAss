/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.infinispan.cache.store;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryCreated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryInvalidated;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryModified;
import org.infinispan.notifications.cachelistener.annotation.CacheEntryRemoved;
import org.infinispan.notifications.cachelistener.event.CacheEntryCreatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryInvalidatedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryModifiedEvent;
import org.infinispan.notifications.cachelistener.event.CacheEntryRemovedEvent;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.impl.cache.store.event.CacheCreateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheEventListener;
import org.iplass.mtp.impl.cache.store.event.CacheEventType;
import org.iplass.mtp.impl.cache.store.event.CacheInvalidateEvent;
import org.iplass.mtp.impl.cache.store.event.CacheRemoveEvent;
import org.iplass.mtp.impl.cache.store.event.CacheUpdateEvent;
import org.iplass.mtp.impl.infinispan.InfinispanService;
import org.iplass.mtp.impl.infinispan.cache.store.InfinispanIndexedCacheStore.IndexEntry;
import org.iplass.mtp.impl.infinispan.cache.store.InfinispanIndexedCacheStore.IndexKey;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanCacheStoreFactory extends CacheStoreFactory implements ServiceInitListener<CacheService> {
	private static Logger logger = LoggerFactory.getLogger(InfinispanCacheStoreFactory.class);

	private static final String INDEX_STORE_POSTFIX = "._index";

	private EmbeddedCacheManager cm;
	private boolean createOnStartup = false;
	private CacheStore sharedInstance;
	private String cacheConfigrationName;

	private int indexRemoveRetryCount = 10;
	private long indexRemoveRetryInterval = 100;// ms
	private String indexCacheConfigrationName;

	private ExecutorService forCacheHandlerExecutorService;

	// TODO カスタムコンフィグの方法。namespacepatternの場合は必要となるかも

	@Override
	public void inited(CacheService service, Config config) {
		InfinispanService is = config.getDependentService(InfinispanService.class);
		cm = is.getCacheManager();

		if (createOnStartup) {
			if (getNamespace() == null) {
				throw new ServiceConfigrationException("if createOnStartup, specific namespace is required.");
			}
			if (logger.isDebugEnabled()) {
				logger.debug("create on startup InfinispanCacheStore:namespace=" + getNamespace());
			}
			sharedInstance = createCacheStoreInternal(getNamespace());
		}
	}

	public int getIndexRemoveRetryCount() {
		return indexRemoveRetryCount;
	}

	public void setIndexRemoveRetryCount(int indexRemoveRetryCount) {
		this.indexRemoveRetryCount = indexRemoveRetryCount;
	}

	public long getIndexRemoveRetryInterval() {
		return indexRemoveRetryInterval;
	}

	public void setIndexRemoveRetryInterval(long indexRemoveRetryInterval) {
		this.indexRemoveRetryInterval = indexRemoveRetryInterval;
	}

	public String getIndexCacheConfigrationName() {
		return indexCacheConfigrationName;
	}

	public void setIndexCacheConfigrationName(String indexCacheConfigrationName) {
		this.indexCacheConfigrationName = indexCacheConfigrationName;
	}

	public String getCacheConfigrationName() {
		return cacheConfigrationName;
	}

	public void setCacheConfigrationName(String cacheConfigrationName) {
		this.cacheConfigrationName = cacheConfigrationName;
	}

	public boolean isCreateOnStartup() {
		return createOnStartup;
	}

	public void setCreateOnStartup(boolean createOnStartup) {
		this.createOnStartup = createOnStartup;
	}

	@Override
	public void destroyed() {
		if (cm != null) {
			cm.stop();
			cm = null;
		}
		synchronized (this) {
			if (forCacheHandlerExecutorService != null) {
				forCacheHandlerExecutorService.shutdown();
				forCacheHandlerExecutorService = null;
			}
		}
	}

	@Override
	public CacheStore createCacheStore(String namespace) {
		if (sharedInstance == null) {
			return createCacheStoreInternal(namespace);
		} else {
			return sharedInstance;
		}
	}


	private CacheStore createCacheStoreInternal(String namespace) {
		if (getIndexCount() == 0) {
			return new InfinispanCacheStore(this, namespace, cacheConfigrationName);
		} else {
			InfinispanCacheStore realStore = new InfinispanCacheStore(this, namespace, cacheConfigrationName);
			if (indexCacheConfigrationName != null) {
				Configuration cc = cm.getCacheConfiguration(indexCacheConfigrationName);
				cm.defineConfiguration(namespace + INDEX_STORE_POSTFIX, cc);
			}
			Cache<IndexKey, IndexEntry> indexStore = cm.getCache(namespace + INDEX_STORE_POSTFIX);

			switch (realStore.cache.getCacheConfiguration().clustering().cacheMode()) {
			case LOCAL:
				return new InfinispanIndexedCacheStore(realStore, indexStore, getIndexCount(), indexRemoveRetryCount, indexRemoveRetryInterval * 1000 * 1000, false);
			case REPL_ASYNC:
			case REPL_SYNC:
				return new InfinispanIndexedCacheStore(realStore, indexStore, getIndexCount(), indexRemoveRetryCount, indexRemoveRetryInterval * 1000 * 1000, false);
			case INVALIDATION_ASYNC:
			case INVALIDATION_SYNC:
				return new InfinispanIndexedCacheStore(realStore, indexStore, getIndexCount(), indexRemoveRetryCount, indexRemoveRetryInterval * 1000 * 1000, true);
			case DIST_ASYNC:
			case DIST_SYNC:
				return new InfinispanIndexedDistributedCacheStore(realStore, indexStore, getIndexCount(), indexRemoveRetryCount, indexRemoveRetryInterval * 1000 * 1000);
			default:
				throw new IllegalStateException("no support of " + realStore.cache.getCacheConfiguration().clustering().cacheMode() + " mode of infinispan config");
			}
		}
	}

	@Override
	public boolean canUseForLocalCache() {
		return false;
	}

	@Override
	public boolean supportsIndex() {
		return true;
	}

	@Override
	public CacheHandler createCacheHandler(CacheStore store) {
		synchronized (this) {
			if (forCacheHandlerExecutorService == null) {
				forCacheHandlerExecutorService = Executors.newCachedThreadPool();
			}
		}
		return new InfinispanCacheHandler(((InfinispanCacheStore) store).cache, forCacheHandlerExecutorService);
	}

	public static class InfinispanCacheStore implements CacheStore {
		private final InfinispanCacheStoreFactory factory;
		private final Cache<Object, CacheEntry> cache;

		private List<CacheEventListener> listeners = new CopyOnWriteArrayList<CacheEventListener>();

		public InfinispanCacheStore(InfinispanCacheStoreFactory factory, String namespace, String cacheConfigName) {
			this.factory = factory;
			if (cacheConfigName != null) {
				Configuration cc = factory.cm.getCacheConfiguration(cacheConfigName);
				factory.cm.defineConfiguration(namespace, cc);
			}
			cache = factory.cm.getCache(namespace);
			cache.addListener(new InfinispanCacheListener(listeners));
		}

		@Override
		public String getNamespace() {
			return cache.getName();
		}

		@Override
		public CacheStoreFactory getFactory() {
			return factory;
		}

		@Override
		public CacheEntry put(CacheEntry entry, boolean clean) {
			if (clean) {
				cache.putForExternalRead(entry.getKey(), entry);
				return null;
			} else {
				return cache.put(entry.getKey(), entry);
			}

		}

		@Override
		public CacheEntry putIfAbsent(CacheEntry entry) {
			return cache.putIfAbsent(entry.getKey(), entry);
		}

		@Override
		public CacheEntry get(Object key) {
			return cache.get(key);
		}

		@Override
		public CacheEntry remove(Object key) {
			return cache.remove(key);
		}

		@Override
		public boolean remove(CacheEntry entry) {
			return cache.remove(entry.getKey(), entry);
		}

		@Override
		public CacheEntry replace(CacheEntry entry) {
			return cache.replace(entry.getKey(), entry);
		}

		@Override
		public boolean replace(CacheEntry oldEntry, CacheEntry newEntry) {
			return cache.replace(oldEntry.getKey(), oldEntry, newEntry);
		}

		@Override
		public void removeAll() {
			cache.clear();
		}

		@Override
		public List<Object> keySet() {
			return new ArrayList<>(cache.keySet());
		}

		@Override
		public CacheEntry getByIndex(int indexKey, Object indexValue) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<CacheEntry> getListByIndex(int indexKey, Object indexValue) {
			throw new UnsupportedOperationException();
		}

		@Override
		public List<CacheEntry> removeByIndex(int indexKey, Object indexValue) {
			throw new UnsupportedOperationException();
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

		@Override
		public int getSize() {
			return cache.size();
		}

		@Override
		public String trace() {
			return cache.toString();
		}

		@Override
		public void destroy() {
			cache.stop();
		}

		protected Cache<Object, CacheEntry> getCache() {
			return cache;
		}

	}

	private static ThreadLocal<InfinispanCacheContextHolder> holder = new ThreadLocal<>();

	private static InfinispanCacheContextHolder newHolder() {
		InfinispanCacheContextHolder current = holder.get();
		InfinispanCacheContextHolder toRet = new InfinispanCacheContextHolder();
		if (current != null) {
			toRet.prevStack = current;
		}
		holder.set(toRet);
		return toRet;
	}

	private static InfinispanCacheContextHolder currentHolder() {
		return holder.get();
	}

	private static void endHolder() {
		InfinispanCacheContextHolder current = holder.get();
		if (current != null) {// nullの場合がある。infinispanのバグ？PassivateされてるやつがActivateされるとき、modifyでpreがいきなりfalseで来る
			if (current.prevStack != null) {
				holder.set(current.prevStack);
			} else {
				holder.set(null);
			}
		}
	}

	private static class InfinispanCacheContextHolder {
		CacheEventType type;
		CacheEntry preValue;
		InfinispanCacheContextHolder prevStack;
	}

	@Listener
	public static class InfinispanCacheListener {

		private List<CacheEventListener> listeners;

		public InfinispanCacheListener(List<CacheEventListener> listeners) {
			this.listeners = listeners;
		}

		@CacheEntryCreated
		public void created(CacheEntryCreatedEvent<Object, CacheEntry> event) {
			if (event.isPre()) {
				InfinispanCacheContextHolder h = newHolder();
				h.type = CacheEventType.CREATE;
			} else {
				try {
					if (listeners.size() > 0) {
						InfinispanCacheContextHolder h = currentHolder();
						switch (h.type) {
						case CREATE:
							CacheCreateEvent cce = new CacheCreateEvent(event.getValue());
							for (CacheEventListener l : listeners) {
								l.created(cce);
							}
							break;
						default:
							throw new IllegalStateException("preEvent afterEvent missmatch.preType:" + h.type + ", event:" + event);
						}
					}
				} finally {
					endHolder();
				}
			}
		}

		@CacheEntryModified
		public void modified(CacheEntryModifiedEvent<Object, CacheEntry> event) {

//			if (logger.isDebugEnabled()) {
//				logger.debug("modify event:" + event);
//			}

			if (event.isPre()) {
				InfinispanCacheContextHolder h = newHolder();
				if (event.getValue() == null) {
					h.type = CacheEventType.CREATE;
				} else {
					h.type = CacheEventType.UPDATE;
					h.preValue = event.getValue();
				}
			} else {
				try {
					if (listeners.size() > 0) {
						InfinispanCacheContextHolder h = currentHolder();
						switch (h.type) {
						case CREATE:
							CacheCreateEvent cce = new CacheCreateEvent(event.getValue());
							for (CacheEventListener l : listeners) {
								l.created(cce);
							}
							break;
						case UPDATE:
							CacheUpdateEvent cue = new CacheUpdateEvent(h.preValue, event.getValue());
							for (CacheEventListener l : listeners) {
								l.updated(cue);
							}
							break;
						default:
							throw new IllegalStateException("preEvent afterEvent missmatch.preType:" + h.type + ", event:" + event);
						}
					}
				} finally {
					endHolder();
				}
			}
		}

		@CacheEntryRemoved
		public void removed(CacheEntryRemovedEvent<Object, CacheEntry> event) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("remove event:" + event);
//			}

			if (event.isPre()) {
				InfinispanCacheContextHolder h = newHolder();
				h.type = CacheEventType.REMOVE;
				h.preValue = event.getValue();
			} else {
				try {
					if (listeners.size() > 0) {
						InfinispanCacheContextHolder h = currentHolder();
						switch (h.type) {
						case REMOVE:
							CacheRemoveEvent cre = new CacheRemoveEvent(h.preValue);
							for (CacheEventListener l : listeners) {
								l.removed(cre);
							}
							break;
						case INVALIDATE:
							// なぜかInvalidateのafterがremovedに、、、infinispanバグ？
							CacheInvalidateEvent cie = new CacheInvalidateEvent(h.preValue);
							for (CacheEventListener l : listeners) {
								l.invalidated(cie);
							}
							break;
						default:
							throw new IllegalStateException("preEvent afterEvent missmatch.preType:" + h.type + ", event:" + event);
						}
					}
				} finally {
					endHolder();
				}
			}
		}

		@CacheEntryInvalidated
		public void invalidated(CacheEntryInvalidatedEvent<Object, CacheEntry> event) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("invalidate event:" + event);
//			}

			if (event.isPre()) {
				InfinispanCacheContextHolder h = newHolder();
				h.type = CacheEventType.INVALIDATE;
				h.preValue = event.getValue();
			} else {
				// 呼ばれない？？？
				try {
					if (listeners.size() > 0) {
						InfinispanCacheContextHolder h = currentHolder();
						switch (h.type) {
						case INVALIDATE:
							CacheInvalidateEvent cie = new CacheInvalidateEvent(h.preValue);
							for (CacheEventListener l : listeners) {
								l.invalidated(cie);
							}
							break;
						default:
							throw new IllegalStateException("preEvent afterEvent missmatch.preType:" + h.type + ", event:" + event);
						}
					}
				} finally {
					endHolder();
				}
			}
		}

//		@CacheEntryRemoved
//		@CacheEntryActivated
//		@CacheEntryCreated
//		@CacheEntryLoaded
//		@CacheEntryPassivated
//		@CacheEntryVisited
//		public void otherEvented(CacheEntryEvent<Object, CacheEntry> event) {
//			if (logger.isDebugEnabled()) {
//				logger.debug("event:" + event);
//			}
//
//		}
	}

	@Override
	public CacheStoreFactory getLowerLevel() {
		return null;
	}

}
