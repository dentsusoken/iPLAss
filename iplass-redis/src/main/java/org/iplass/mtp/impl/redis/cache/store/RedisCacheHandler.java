/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheHandlerTask;
import org.iplass.mtp.impl.cache.store.CacheStore;

public class RedisCacheHandler implements CacheHandler {

	private CacheStore cs;

	public RedisCacheHandler(CacheStore cs) {
		this.cs = cs;
	}

	@SafeVarargs
	@Override
	public final <K, V, R> List<? extends Future<R>> executeParallel(CacheHandlerTask<K, V, R> task, K... inputKeys) {
		task.setContext(new RedisCacheContext<K, V>(cs),
				inputKeys != null && inputKeys.length > 0 ? new HashSet<K>(Arrays.asList(inputKeys))
						: Collections.<K>emptySet());

		ArrayList<Future<R>> ret = new ArrayList<Future<R>>();

		try {
			R r = task.call();
			ret.add(new SyncFuture<R>(r, null));
		} catch (Exception e) {
			ret.add(new SyncFuture<R>(null, e));
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
		public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
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
