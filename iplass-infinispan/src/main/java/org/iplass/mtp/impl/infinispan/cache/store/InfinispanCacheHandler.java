/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheHandlerTask;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.infinispan.InfinispanService;
import org.iplass.mtp.impl.infinispan.task.InfinispanSerializableTask;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskExecutor;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskState;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanCacheHandler implements CacheHandler {

	private static Logger logger = LoggerFactory.getLogger(InfinispanCacheHandler.class);

	//	private DistributedExecutorService ds;
	private String cacheName;

	public InfinispanCacheHandler(Cache<?, ?> c, ExecutorService es) {
		//		ds = new DefaultExecutorService(c, es);
		this.cacheName = c.getName();
	}

	// FIXME 利用箇所が不明
	@SafeVarargs
	@Override
	public final <K, V, R> List<CompletableFuture<R>> executeParallel(
			CacheHandlerTask<K, V, R> task, K... inputKeys) {

		EmbeddedCacheManager cacheManager = ServiceRegistry.getRegistry().getService(InfinispanService.class).getCacheManager();

		ExecuteContext ec = ExecuteContext.getCurrentContext();
		Set<K> inputKeySet = inputKeys == null || inputKeys.length == 0 ? Collections.emptySet() : new HashSet<>(Arrays.asList(inputKeys));
		Task<K, V, R> submitTask = new Task<K, V, R>(task, ec.getClientTenantId(), ec.getCurrentTimestamp(), cacheName, inputKeySet);
		InfinispanTaskState state = InfinispanTaskExecutor.submitAllNode(submitTask);

		List<CompletableFuture<R>> result = new ArrayList<>(1);
		// TODO CompletableFuture<Void> が強制される
		result.add((CompletableFuture<R>) state.getFuture());

		return result;
	}

	// FIXME 実装見直しが必要
	public static class Task<K, V, R> implements InfinispanSerializableTask {
		private static final long serialVersionUID = -4514954103680453534L;

		private String cacheName;

		private CacheHandlerTask<K, V, R> cht;

		//ExecuteContextの情報
		private int tenantId;
		private Timestamp currentTimestamp;

		// FIXME serializable 必須
		private Set<K> inputKeys;

		private transient TenantContext tc;

		public Task(CacheHandlerTask<K, V, R> cht, int tenantId, Timestamp currentTimestamp, String cacheName, Set<K> inputKeys) {
			this.cht = cht;
			this.tenantId = tenantId;
			this.currentTimestamp = currentTimestamp;
			this.cacheName = cacheName;
			this.inputKeys = inputKeys;
		}
		//		@Override
		public R call() throws Exception {

			try {
				return ExecuteContext.executeAs(getTenantContext(), new Executable<R>() {
					@Override
					public R execute() {
						if (currentTimestamp != null) {
							ExecuteContext.getCurrentContext().setCurrentTimestamp(currentTimestamp);
						}
						long start = 0;
						if (logger.isDebugEnabled()) {
							start = System.currentTimeMillis();
						}
						try {
							R r = cht.call();
							if (logger.isDebugEnabled()) {
								long queryTime = System.currentTimeMillis() - start;
								logger.debug("execute task: " + cht + " time =" + queryTime + "ms.");
							}
							return r;
						} catch (Exception e) {
							if (logger.isDebugEnabled()) {
								long queryTime = System.currentTimeMillis() - start;
								logger.debug("execute task: " + cht + " time =" + queryTime + "ms. error=" + e, e);
							} else {
								logger.error("execute task: " + cht + " error=" + e, e);
							}
							throw new WrapException(e);
						}
					}
				});

			} catch(WrapException e) {
				throw  e.getException();
			}

		}

		//		@Override
		public void setEnvironment(final Cache<Object, CacheEntry> cache,
				final Set<Object> inputKeys) {
			ExecuteContext.executeAs(getTenantContext(), new Executable<Void>() {
				@SuppressWarnings("unchecked")
				@Override
				public Void execute() {
					if (currentTimestamp != null) {
						ExecuteContext.getCurrentContext().setCurrentTimestamp(currentTimestamp);
					}
					cht.setContext(new InfinispanCacheContext<K, V>(cache), (Set<K>) inputKeys);
					return null;
				}
			});

		}

		private TenantContext getTenantContext() {
			if (tc == null) {
				tc = ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);
			}
			return tc;
		}

		@Override
		public void runNode() {
			ExecuteContext.executeAs(getTenantContext(), new Executable<Void>() {
				@Override
				public Void execute() {
					Cache<Object, CacheEntry> cache = ServiceRegistry.getRegistry().getService(InfinispanService.class).getCacheManager().getCache(cacheName);
					if (currentTimestamp != null) {
						ExecuteContext.getCurrentContext().setCurrentTimestamp(currentTimestamp);
					}
					// FIXME ここで指定しているCacheContext が serialize できずにエラーになってしまう。InfinispanCacheContext の内部はデータも含めて serializable でなければいけない。context の指定方法を見直す必要がある
					cht.setContext(new InfinispanCacheContext<K, V>(cache), inputKeys);

					return null;
				}
			});

			try {
				call();
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

	}

}
