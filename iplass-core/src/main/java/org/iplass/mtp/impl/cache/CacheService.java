/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.cache.store.CacheStoreFactory;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheService implements Service {
	private static Logger logger = LoggerFactory.getLogger(CacheService.class);

	private static final String CACHE_STORE_FACTORY_DEFAULT_NAME = "defaultFactory";

	private HashMap<String, CacheStoreFactory> namespaceMap;
	private ArrayList<NamespacePattern> namespacePattern;
	private CacheStoreFactory defaultFactory;

	private List<CacheStoreFactory> factories;

	private ConcurrentHashMap<Object, Object> cacheStore;

	/**
	 * 指定のnamespaceで、共有のCacheStoreを取得（作成）する。
	 *
	 * @param namespace
	 * @return
	 */
	public CacheStore getCache(String namespace) {
		return getCache(namespace, true);
	}

	/**
	 * 指定のnamespaceで、共有のCacheStoreを取得（作成）する。
	 *
	 * @param namespace
	 * @param isCreate
	 * @return
	 */
	public CacheStore getCache(String namespace, boolean isCreate) {
		CacheStore c = (CacheStore) cacheStore.get(namespace);
		if (c == null && isCreate) {
			c = create(namespace, false);
			cacheStore.putIfAbsent(namespace, c);
			c = (CacheStore) cacheStore.get(namespace);
		}
		return c;
	}

	/**
	 * 指定のnamespaceで、非共有のCacheStoreを作成する。
	 * namespaceは、CacheFactoryの選択に利用されるのみで、
	 * このメソッドで作成したCacheStoreは共有されない。
	 *
	 * @param namespace
	 * @return
	 */
	public CacheStore createLocalCache(String namespace) {
		CacheStore ret = create(namespace, true);
		return ret;
	}

	/**
	 * CacheHandlerのインスタンスを作成する。
	 *
	 * @param store
	 * @return
	 */
	public CacheHandler createCacheHandler(CacheStore store) {
		CacheStoreFactory f = store.getFactory();
		return f.createCacheHandler(store);
	}

	private CacheStore create(String namespace, boolean isLocalUse) {
		CacheStoreFactory f = namespaceMap.get(namespace);
		if (f != null) {
			if (isLocalUse) {
				if (!f.canUseForLocalCache()) {
					throw new ServiceConfigrationException(f.getClass() + " can not use for localCache. at CacheStore:" + namespace);
				}
			}
			logger.debug("create CacheStore:" + namespace + " by specific namespace");
			return f.createCacheStore(namespace);
		} else {
			for (NamespacePattern p: namespacePattern) {
				if (p.pattern.matcher(namespace).matches()) {
					if (isLocalUse) {
						if (!p.factory.canUseForLocalCache()) {
							throw new ServiceConfigrationException(p.factory.getClass() + " can not use for localCache. at CacheStore:" + namespace + " by namespacePattern:" + p.factory.getNamespacePattern() + " Factory");
						}
					}
					logger.debug("create CacheStore:" + namespace + " by namespacePattern:" + p.factory.getNamespacePattern() + " Factory");
					return p.factory.createCacheStore(namespace);
				}
			}

			//use default...
			if (defaultFactory != null) {
				if (isLocalUse) {
					if (!defaultFactory.canUseForLocalCache()) {
						throw new ServiceConfigrationException(defaultFactory.getClass() + " can not use for localCache. at CacheStore:" + namespace + " by defaultFactory");
					}
				}
				return defaultFactory.createCacheStore(namespace);
			}
		}
		throw new ServiceConfigrationException("no CacheStore configration matches for " + namespace);
	}


	/**
	 * 指定のnamespaceの共有のCacheStoreを無効化する。
	 *
	 * @param namespace
	 */
	public void invalidate(String namespace) {
		ConcurrentHashMap<Object, Object> refCacheStore = cacheStore;
		if (refCacheStore != null) {
			CacheStore c = (CacheStore) refCacheStore.get(namespace);
			if (c != null) {
				if (refCacheStore.remove(namespace, c)) {
					logger.debug("invaliadte CacheStore:" + namespace);
					c.destroy();
//					c.removeAll();
				}
			}
		}

	}

	public void destroy() {

		ConcurrentHashMap<Object, Object> refCacheStore = cacheStore;
		cacheStore = null;
		if (refCacheStore != null) {
			for (Map.Entry<Object, Object> e: refCacheStore.entrySet()) {
				CacheStore c = (CacheStore) e.getValue();
				if (c != null) {
					logger.debug("destroy CacheStore:" + e.getKey());
					c.destroy();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void init(Config config) {

		namespaceMap = new HashMap<String, CacheStoreFactory>();
		namespacePattern = new ArrayList<CacheService.NamespacePattern>();
		cacheStore = new ConcurrentHashMap<Object, Object>();
		factories = new ArrayList<>();

		Set<String> names = config.getNames();
		for (String n: names) {
			if (n.equals(CACHE_STORE_FACTORY_DEFAULT_NAME)) {
				defaultFactory = (CacheStoreFactory) config.getBean(n);
				factories.add(defaultFactory);
			} else {
				List<CacheStoreFactory> cacheStoreFactory = (List<CacheStoreFactory>) config.getBeans(n);

				if (cacheStoreFactory != null) {
					for (CacheStoreFactory f: cacheStoreFactory) {
						factories.add(f);

						if (f.getNamespace() != null) {
							namespaceMap.put(f.getNamespace(), f);
						} else {
							namespacePattern.add(new NamespacePattern(f));
						}
					}
				}
			}
		}
	}

	public List<CacheStoreFactory> getFactories() {
		return factories;
	}


	private class NamespacePattern {
		Pattern pattern;
		CacheStoreFactory factory;

		private NamespacePattern(CacheStoreFactory factory) {
			pattern = Pattern.compile(factory.getNamespacePattern());
			this.factory = factory;
		}
	}
}
