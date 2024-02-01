/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.iplass.mtp.impl.async.AsyncTaskService;
import org.iplass.mtp.impl.cache.store.CacheContext;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheHandlerTask;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.ServiceRegistry;

public class SimpleLocalCacheHandler implements CacheHandler {
	
	private int concurrencyLevel;
	
	private CacheStore cs;
	
	public SimpleLocalCacheHandler(CacheStore cs, int concurrencyLevel) {
		this.cs = cs;
		this.concurrencyLevel = concurrencyLevel;
	}

	@SafeVarargs
	@Override
	public final <K, V, R> List<Future<R>> executeParallel(
			CacheHandlerTask<K, V, R> handler, K... inputKeys) {
		
		ArrayList<Future<R>> ret = new ArrayList<Future<R>>();
		
		if (inputKeys == null || inputKeys.length == 0) {
			//key未指定の場合は、複数スレッドでの並列処理ができないので、単一スレッドで処理
			CacheContext<K, V> cc = new SimpleLocalCacheContext<K, V>(cs);
			handler.setContext(cc, Collections.<K>emptySet());
			try {
				R r = handler.call();
				ret.add(new SyncFuture<R>(r, null));
			} catch (Exception e) {
				ret.add(new SyncFuture<R>(null, e));
			}

		} else {
			//並列度により、複数スレッドで処理
			if (concurrencyLevel > 1) {
				AsyncTaskService atc = ServiceRegistry.getRegistry().getService(AsyncTaskService.class);
				for (int i = 0; i < concurrencyLevel; i++) {
					final CacheHandlerTask<K, V, R> copyTask = ObjectUtil.deepCopy(handler);
					final CacheContext<K, V> cc = new SimpleLocalCacheContext<K, V>(cs);
					final Set<K> keys = new HashSet<K>();
					for (int j = i; j < inputKeys.length; j+=concurrencyLevel) {
						keys.add(inputKeys[j]);
					}
					if (keys.size() > 0) {
						Future<R> r = atc.execute(new Callable<R>() {
							@Override
							public R call() throws Exception {
								copyTask.setContext(cc, keys);
								return copyTask.call();
							}
						});
						ret.add(r);
					}
				}
			} else {
				Set<K> allKeys = new HashSet<K>(Arrays.asList(inputKeys));
				CacheContext<K, V> cc = new SimpleLocalCacheContext<K, V>(cs);
				handler.setContext(cc, allKeys);
				try {
					R r = handler.call();
					ret.add(new SyncFuture<R>(r, null));
				} catch (Exception e) {
					ret.add(new SyncFuture<R>(null, e));
				}
			}
		}
		
		return ret;
	}
	
	private static class SyncFuture<R> implements Future<R> {
		
		private R result;
		private Exception e;
		
		private SyncFuture(R result, Exception e) {
			this.result = result;
			this.e = e;
		}
		

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return false;
		}

		@Override
		public R get() throws InterruptedException, ExecutionException {
			if (e != null) {
				throw new ExecutionException(e);
			}
			return result;
		}

		@Override
		public R get(long timeout, TimeUnit unit) throws InterruptedException,
				ExecutionException, TimeoutException {
			return get();
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean isDone() {
			return true;
		}
		
	}
	
}
