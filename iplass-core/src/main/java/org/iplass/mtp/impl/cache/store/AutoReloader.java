/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.cache.store;

import java.lang.ref.WeakReference;
import java.util.function.BiFunction;

import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * キャッシュエントリの自動リロード処理を行うクラス。
 * 現状の実装では、CacheEntiryはローカルのメモリ内で、格納時と同じインスタンスが保持されているることを前提としている。
 * 
 * @author K.Higuchi
 */
class AutoReloader implements Runnable {
	
	public static final String AUTO_RELOADER_USER_NAME = "CacheReloader";
	private static Logger logger = LoggerFactory.getLogger(AutoReloader.class);
	
	private int tenantId;
	private String namespace;
	private WeakReference<CacheEntry> entryRef;
	private long timeToLive;
	private BiFunction<Object, CacheEntry, CacheEntry> reloadFunction;

	AutoReloader(int tenantId, String namespace, CacheEntry entry, long timeToLive,
			BiFunction<Object, CacheEntry, CacheEntry> reloadFunction) {
		this.tenantId = tenantId;
		this.namespace = namespace;
		this.entryRef = new WeakReference<CacheEntry>(entry);
		this.timeToLive = timeToLive;
		this.reloadFunction = reloadFunction;
	}
	
	@Override
	public void run() {
		CacheEntry entry = entryRef.get();
		if (entry == null) {
			logger.debug("cache entry is already garbage collected, so skip reloading. namespace:{}", namespace);
			return;
		}
		
		CacheStore store = ServiceRegistry.getRegistry().getService(CacheService.class).getCache(namespace, false);
		if (store == null) {
			logger.debug("cache store is null, so skip reloading. namespace:{}", namespace);
			return;
		}
		
		CacheEntry currentEntry = store.get(entry.getKey());
		if (currentEntry != entry) {
			logger.debug("cache entry is already updated by another thread, so skip reloading. namespace:{}, key:{}", namespace, entry.getKey());
			return;
		}
		
		CacheEntry[] reloaded = new CacheEntry[1];
		Long[] ttl = new Long[1];
		CacheEntry newEntry = null;
		try {
			newEntry = store.compute(entry.getKey(), (k, e) -> {
				if (e == entry) {
					return ExecuteContext.executeAs(ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId),
							() -> {
								ExecuteContext ec = ExecuteContext.getCurrentContext();
								try {
									ec.setClientId(AUTO_RELOADER_USER_NAME);
									ec.mdcPut(AuthService.MDC_USER, AUTO_RELOADER_USER_NAME);
									
									logger.debug("reloading cache entry:{} in namespace:{} ",k, namespace);
									reloaded[0] = reloadFunction.apply(k, e);
									if (reloaded[0] != null) {
										ttl[0] = reloaded[0].getTimeToLive();
										if (ttl[0] == null || ttl[0].longValue() <= 0L) {
											throw new IllegalArgumentException("computeIfAbsentWithAutoReload() must specify TTL on CacheEntry.");
										}
										//entryのttlを無期限に設定し、ttlによりinvalidateされないように
										reloaded[0].setTimeToLive(-1L);
									}
									return reloaded[0];
								} finally {
									ec.mdcPut(AuthService.MDC_USER, null);
								}
							});
				} else {
					logger.debug("mybe another thread has updated the cache entry: {} in namespace: {}, so skip reloading", k, namespace);
					return e;
				}
			});
			
		} catch (RuntimeException e) {
			logger.error("failed to reload cache entry: " + entry.getKey() + " in namespace: " + namespace + ", so cannot update CacheEntry. Attempt to reload after next TTL.", e);
			CacheService cacheService = ServiceRegistry.getRegistry().getService(CacheService.class);
			cacheService.schedule(ttl[0], new AutoReloader(tenantId, namespace, newEntry, timeToLive, reloadFunction));
		}
		
		if (newEntry != null && newEntry == reloaded[0]) {
			//自身がputした場合のみリロードをスケジュールする
			CacheService cacheService = ServiceRegistry.getRegistry().getService(CacheService.class);
			cacheService.schedule(ttl[0], new AutoReloader(tenantId, namespace, newEntry, ttl[0], reloadFunction));
		} else {
			logger.debug("mybe another thread has updated the cache entry: {} in namespace: {}, so skip rescheduling", entry.getKey(), namespace);
		}
	}

}
