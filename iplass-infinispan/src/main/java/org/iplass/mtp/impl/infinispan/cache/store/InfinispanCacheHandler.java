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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.infinispan.Cache;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheHandler;
import org.iplass.mtp.impl.cache.store.CacheHandlerTask;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.infinispan.InfinispanService;
import org.iplass.mtp.impl.infinispan.task.InfinispanNamedTask;
import org.iplass.mtp.impl.infinispan.task.InfinispanSerializableTask;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskExecutor;
import org.iplass.mtp.impl.infinispan.task.InfinispanTaskState;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * InfinispanCacheHandler
 *
 * @author SEKIGUCHI Naoya
 */
public class InfinispanCacheHandler implements CacheHandler {

	private static Logger logger = LoggerFactory.getLogger(InfinispanCacheHandler.class);

	private String cacheName;

	public InfinispanCacheHandler(Cache<?, ?> c, ExecutorService es) {
		this.cacheName = c.getName();
	}

	@SafeVarargs
	@Override
	public final <K, V, R> List<Future<R>> executeParallel(CacheHandlerTask<K, V, R> task, K... inputKeys) {

		ExecuteContext ec = ExecuteContext.getCurrentContext();
		List<K> inputKeyList = inputKeys == null || inputKeys.length == 0 ? Collections.emptyList() : Arrays.asList(inputKeys);
		InfinispanTaskState<R> state = null;
		if (0 < inputKeyList.size()) {
			// inputKeys 指定あり
			state = InfinispanTaskExecutor.submitByCacheKeyOwner(
					p -> new CacheHandlerTaskInfinispanAdapter<K, V, R>(task, cacheName, new HashSet<>(p), ec.getClientTenantId(), ec.getCurrentTimestamp()),
					cacheName, inputKeyList);
		} else {
			// inputKeys 指定なし
			CacheHandlerTaskInfinispanAdapter<K, V, R> submitTask = new CacheHandlerTaskInfinispanAdapter<K, V, R>(task, cacheName, Collections.emptySet(),
					ec.getClientTenantId(), ec.getCurrentTimestamp());
			state = InfinispanTaskExecutor.submitAll(submitTask);
		}

		return state.getFuture();
	}


	/**
	 * CacheHandlerTask Infinispan Adapter
	 * @param <K> キャッシュキーデータ型
	 * @param <V> キャッシュ値データ型
	 * @param <R> 返却値データ型
	 */
	public static class CacheHandlerTaskInfinispanAdapter<K, V, R> implements InfinispanSerializableTask<R>, InfinispanNamedTask {
		/** serialVersionUID */
		private static final long serialVersionUID = -4277615798099119604L;
		/** パラレル実行タスク */
		private CacheHandlerTask<K, V, R> cht;
		/** キャッシュ名 */
		private String cacheName;
		/** キャッシュキー */
		private Set<K> inputKeys;

		//ExecuteContextの情報
		/** テナントID */
		private int tenantId;
		/** 実行時Timestamp */
		private Timestamp currentTimestamp;

		/**
		 * コンストラクタ
		 * @param cht パラレル実行タスク
		 * @param cacheName キャッシュ名
		 * @param inputKeys キャッシュキー
		 * @param tenantId テナントID
		 * @param currentTimestamp 実行時Timestamp
		 */
		public CacheHandlerTaskInfinispanAdapter(CacheHandlerTask<K, V, R> cht, String cacheName, Set<K> inputKeys, int tenantId,
				Timestamp currentTimestamp) {
			this.cht = cht;
			this.tenantId = tenantId;
			this.currentTimestamp = currentTimestamp;
			this.cacheName = cacheName;
			this.inputKeys = inputKeys;
		}

		@Override
		public R callByNode() {
			return ExecuteContext.executeAs(getTenantContext(), new Executable<R>() {
				@Override
				public R execute() {
					if (currentTimestamp != null) {
						// 実行時Timestamp 設定
						ExecuteContext.getCurrentContext().setCurrentTimestamp(currentTimestamp);
					}

					Cache<Object, CacheEntry> cache = ServiceRegistry.getRegistry().getService(InfinispanService.class).getCacheManager().getCache(cacheName);
					cht.setContext(new InfinispanCacheContext<K, V>(cache), inputKeys);

					long start = System.currentTimeMillis();

					try {
						return cht.call();

					} catch (Exception e) {
						throw new WrapException(e);

					} finally {
						if (logger.isDebugEnabled()) {
							long queryTime = System.currentTimeMillis() - start;
							logger.debug("execute task: " + cht + " time =" + queryTime + "ms.");
						}
					}
				}
			});
		}

		@Override
		public String getTaskName() {
			return cht.getClass().getSimpleName();
		}

		/**
		 * TenantContext を取得する
		 * @return TenantContext
		 */
		private TenantContext getTenantContext() {
			return ServiceRegistry.getRegistry().getService(TenantContextService.class).getTenantContext(tenantId);
		}
	}
}
