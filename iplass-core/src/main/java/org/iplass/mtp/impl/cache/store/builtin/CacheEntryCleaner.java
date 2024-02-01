/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEntryCleaner {
	private static Logger logger = LoggerFactory.getLogger(CacheEntryCleaner.class);
	private static CacheEntryCleaner instance = new CacheEntryCleaner();
	
	private ScheduledExecutorService executer;
	
	public static CacheEntryCleaner getInstance() {
		return instance;
	}
	
	public static void shutdown() {
		if (!instance.executer.isShutdown()) {
			instance.executer.shutdown();
			try {
				instance.executer.awaitTermination(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				throw new RuntimeException("exception when shutdown CacheEntryCleaner", e);
			}
		}
	}
	
	public CacheEntryCleaner() {
		SecurityManager s = System.getSecurityManager();
		final ThreadGroup group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		executer = Executors.newSingleThreadScheduledExecutor(r -> {
				Thread t = new Thread(group, r, "CacheEntryCleaner", 0);
				t.setDaemon(true);
				t.setPriority(Thread.MIN_PRIORITY);
				t.setUncaughtExceptionHandler((thread, e) -> {
					UncaughtExceptionHandler uh = Thread.getDefaultUncaughtExceptionHandler();
					if (uh != null) {
						uh.uncaughtException(thread, e);
					} else {
						logger.error("Exception in CacheEntryCleaner " + e, e);
					}
				});
				return t;
		});
	}
	
	public void register(SimpleCacheStoreBase store, long interval) {
		Worker worker = new Worker(store);
		ScheduledFuture<?> future = executer.scheduleWithFixedDelay(worker, interval, interval, TimeUnit.MILLISECONDS);
		worker.future = future;
	}
	
	private static class Worker implements Runnable {
		
		private WeakReference<SimpleCacheStoreBase> ref;
		private volatile ScheduledFuture<?> future;
		
		private Worker(SimpleCacheStoreBase store) {
			this.ref = new WeakReference<>(store);
		}

		@Override
		public void run() {
			SimpleCacheStoreBase cs = ref.get();
			if (cs == null) {
				future.cancel(true);
			} else {
				cs.removeInvalidEntry();
			}
		}
		
	}

}
