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
package org.iplass.mtp.impl.infinispan.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.infinispan.Cache;
import org.infinispan.distribution.LocalizedCacheTopology;
import org.infinispan.manager.ClusterExecutor;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;
import org.iplass.mtp.impl.infinispan.InfinispanService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Infinispan タスク実行機能
 *
 * <p>
 * Infinispan メンバーノードへタスク実行を行うユーティリティクラス。
 * 本クラスに定義されている公開のメソッドは Future を含む情報を返却するが、結果の返却タイミングは全てのノードで処理の完了後となる（実行した時点で同期する）。
 *
 * 本来であれば各非同期処理毎に結果を戻すべきだが、infinispan の非同期処理で値を返却する為には、各ノードの実行結果を本機能で収集する必要がある。
 * 収集を行うために、機能内でノードの実行を待機する。
 * </p>
 *
 * @see org.infinispan.manager.ClusterExecutor#submitConsumer(org.infinispan.util.function.SerializableFunction, org.infinispan.util.function.TriConsumer)
 * @author SEKIGUCHI Naoya
 */
public class InfinispanTaskExecutor {
	/** ロガー */
	private static final Logger LOG = LoggerFactory.getLogger(InfinispanTaskExecutor.class);
	/** 要求ID用ハイフンパターン */
	private static final Pattern HYPHEN_PATTERN = Pattern.compile("-");
	/** 要求ID用ハイフン置換文字列 */
	private static final String HYPHEN_REPLACED = "";
	/** 処理を実行しない場合の空リクエストID */
	private static final String EMPTY_REQUEST_ID = "";

	/**
	 * クラスタ要求パターン
	 */
	private enum RequestPattern {
		/** 全メンバーノード */
		ALL {
			@Override
			ClusterExecutor getExecuter(EmbeddedCacheManager cacheManager) {
				return cacheManager.executor().allNodeSubmission();
			}
		},
		/** 自ノード以外のメンバーノード */
		REMOTE {
			@Override
			ClusterExecutor getExecuter(EmbeddedCacheManager cacheManager) {
				var myself = cacheManager.getAddress();
				return cacheManager.executor().filterTargets(t -> t != myself);
			}
		};

		/**
		 * ClusterExecutor を取得する
		 * @param cacheManager キャッシュマネージャー
		 * @return ClusterExecutor
		 */
		abstract ClusterExecutor getExecuter(EmbeddedCacheManager cacheManager);
	}

	/**
	 * プライベートコンストラクタ
	 */
	private InfinispanTaskExecutor() {
	}

	/**
	 * 全メンバーノードに対してタスクを実行する
	 * @param <T> 実行結果データ型
	 * @param task タスク
	 * @return タスク実行状態
	 */
	public static <T> InfinispanTaskState<T> submitAll(InfinispanSerializableTask<T> task) {
		return doSubmitOnce(RequestPattern.ALL, task);
	}

	/**
	 * 自ノード以外のメンバーノードに対してタスクを実行する
	 *
	 * <p>
	 * メンバーが自ノードしか存在しない場合は、タスクを実行せず終了する。
	 * </p>
	 *
	 * @param <T> 実行結果データ型
	 * @param task タスク
	 * @return タスク実行状態
	 */
	public static <T> InfinispanTaskState<T> submitRemote(InfinispanSerializableTask<T> task) {
		var members = getCacheManager().getMembers();
		if (1 == members.size() && members.contains(getCacheManager().getAddress())) {
			// メンバーノードが自身１つのみかつ、それが自分自身の場合、リクエストを実行せず終了する。
			LOG.warn("Nothing is done because only a self-node exists. task = {}", task.getClass().getSimpleName());
			return new InfinispanTaskState<T>(EMPTY_REQUEST_ID, Collections.emptyList());
		}

		return doSubmitOnce(RequestPattern.REMOTE, task);
	}

	/**
	 * {@link InfinispanTaskExecutor#submitByCacheKeyOwner(TaskFactory, String, List)} で利用する。
	 *
	 * @param <T> 生成するタスクの返却データ型
	 * @param <K> キャッシュキーデータ型
	 */
	@FunctionalInterface
	public static interface TaskFactory<T, K> extends Function<List<K>, InfinispanSerializableTask<T>> {
	}

	/**
	 * キャッシュキーのオーナーノードでタスクを実行する
	 *
	 * <p>
	 * タスクキーはノード毎に選別され、タスクが実行される。
	 * 複数ノードが存在した場合に、キャッシュキー均等に分散されるのではなく、キャッシュキーのオーナーとなるノードに実行を依頼する。
	 * </p>
	 *
	 * <p>
	 * 例： NodeA, NodeB, NodeC が存在し、cacheKeys に [1, 2, 3, 4, 5] と設定されていた場合、以下のようなイメージで実行される。
	 *
	 * <ul>
	 * <li>NodeA が処理するキャッシュキー： 1, 4, 5</li>
	 * <li>NodeB が処理するキャッシュキー： 2, 3</li>
	 * <li>NodeC はキャッシュキーのオーナーが存在しないので処理を実行しない</li>
	 * </ul>
	 * </p>
	 *
	 * @param <T> 実行結果データ型
	 * @param <K> キャッシュキーデータ型
	 * @param taskFactory タスク生成機能。引数には、ノード毎に選別したキャッシュキーが設定される。
	 * @param cacheName キャッシュ名
	 * @param cacheKeys キャッシュキーリスト
	 * @return タスク実行状態
	 */
	public static <T, K> InfinispanTaskState<T> submitByCacheKeyOwner(TaskFactory<T, K> taskFactory, String cacheName, List<K> cacheKeys) {
		EmbeddedCacheManager cacheManager = getCacheManager();

		Map<Address, List<K>> cacheKeysPerNode = screeningCacheKeys(cacheManager.getCache(cacheName), cacheKeys);

		String requestId = generateRequestId();
		String requestNode = InfinispanUtil.getExecutionNode();
		// 実行ノード毎のタスク実行結果。Infinispan の実行結果を格納する。
		final Map<Address, InfinispanManagedTaskResult<T>> taskResultParNode = new ConcurrentHashMap<>();
		// 実行ノード毎に呼び出し元へ返却用の Future を管理する。
		final Map<Address, Future<Void>> taskFuture = new ConcurrentHashMap<>();

		// 各ノードへ実行依頼をかける
		// parallelStream を利用し並列実行する
		cacheKeysPerNode.keySet().parallelStream().forEach(node -> {
			// 並列実行ポイント

			var executor = cacheManager.executor().filterTargets(t -> t == node);
			var keyList = cacheKeysPerNode.get(node);
			var task = taskFactory.apply(keyList);
			InfinispanManagedTask<T> managedTask = new InfinispanManagedTaskImpl<T>(task, requestId, requestNode);

			LOG.debug("Submit task {}({}) to {}, keyList={}.", managedTask.getTaskName(), requestId, node, keyList);
			taskFuture.put(node, submitInner(executor, managedTask, taskResultParNode));
		});

		// 実行ノード毎に呼び出し元へ返却用の Future を管理する。
		final Map<Address, Future<T>> resultFuture = new ConcurrentHashMap<>();

		// 実行結果を収集する
		taskFuture.keySet().parallelStream().forEach(node -> {
			// 並列実行ポイント

			var future = taskFuture.get(node);
			try {
				// 実行を待ち、結果を格納する
				future.get();
				var taskResult = taskResultParNode.get(node);
				resultFuture.put(node, InfinispanTaskFutureFactory.create(future, node, taskResult));

			} catch (InterruptedException | ExecutionException e) {
				// 例外発生時は呼び出し元へエスカレーション
				resultFuture.put(node, InfinispanTaskFutureFactory.create(future, node, e));
			}
		});

		return new InfinispanTaskState<T>(requestId, new ArrayList<>(resultFuture.values()));
	}


	/**
	 * ノード毎に実行するキャッシュキーを選別する
	 *
	 * <p>
	 * キャッシュキー毎のプライマリノードは {@link org.infinispan.Cache} インスタンスに設定されている
	 * {@link org.infinispan.distribution.LocalizedCacheTopology} を利用して、書き込みオーナーの先頭をプライマリノードと判別する。
	 * </p>
	 *
	 * @param <K> キャッシュキーデータ型
	 * @param cache Infinispan キャッシュインスタンス
	 * @param cacheKeys キャッシュキー
	 * @return ノード毎に実行するパラメータマップ
	 */
	private static <K> Map<Address, List<K>> screeningCacheKeys(Cache<?, ?> cache, List<K> cacheKeys) {
		Map<Address, List<K>> result = new HashMap<Address, List<K>>();
		LocalizedCacheTopology cacheTopology = cache.getAdvancedCache().getDistributionManager().getCacheTopology();
		for (K param : cacheKeys) {
			var distributionInfo = cacheTopology.getDistribution(param);
			var primaryNode = distributionInfo.writeOwners().get(0);
			var distParams = result.get(primaryNode);
			if (null == distParams) {
				distParams = new ArrayList<>();
				result.put(primaryNode, distParams);
			}
			distParams.add(param);
		}

		return result;
	}

	/**
	 * 要求IDを生成する
	 *
	 * <p>
	 * 要求IDはUUIDよりハイフンを除き、大文字変換した値。
	 * </p>
	 *
	 * @return 要求ID
	 */
	private static String generateRequestId() {
		// TODO 確認
		String id = UUID.randomUUID().toString();
		return HYPHEN_PATTERN.matcher(id).replaceAll(HYPHEN_REPLACED).toUpperCase();
	}

	private static <T> InfinispanTaskState<T> doSubmitOnce(RequestPattern pattern, InfinispanSerializableTask<T> task) {
		String requestId = generateRequestId();
		String requestNode = InfinispanUtil.getExecutionNode();
		InfinispanManagedTask<T> managedTask = new InfinispanManagedTaskImpl<T>(task, requestId, requestNode);

		final Map<Address, InfinispanManagedTaskResult<T>> resultMap = new ConcurrentHashMap<>();

		ClusterExecutor executor2 = pattern.getExecuter(getCacheManager());
		LOG.debug("Submit task {}({}) to {}.", managedTask.getTaskName(), managedTask.getRequestId(), pattern);
		Future<Void> future = submitInner(executor2, managedTask, resultMap);

		List<Future<T>> resultFuture = new ArrayList<>();

		try {
			// 非同期処理待ち合わせ
			future.get();
			// 1つの Future を実行されたノード毎の Future に分解する
			resultMap.forEach((address, result) -> resultFuture.add(InfinispanTaskFutureFactory.create(future, address, result)));

		} catch (InterruptedException | ExecutionException e) {
			// 例外発生時は呼び出し元へエスカレーション
			resultFuture.add(InfinispanTaskFutureFactory.create(future, getCacheManager().getAddress(), e));
		}

		return new InfinispanTaskState<T>(requestId, resultFuture);
	}


	/**
	 * タスク要求する
	 * @param <T> 実行結果データ型
	 * @param executor ノードへ実行を要求する ClusterExecutor インスタンス
	 * @param managedTask 実行対象のタスク
	 * @param resultMap 実行結果を格納するための Map インスタンス
	 * @return タスク実行結果の Future
	 */
	private static <T> Future<Void> submitInner(ClusterExecutor executor, InfinispanManagedTask<T> managedTask,
			final Map<Address, InfinispanManagedTaskResult<T>> resultMap) {
		CompletableFuture<Void> future = executor.submitConsumer(managedTask, (address, result, cause) -> {
			if (null != cause) {
				// 想定外のエラー終了
				resultMap.put(address, InfinispanManagedTaskResult.create(cause));

			} else {
				// コマンド処理内で終了
				resultMap.put(address, result);
			}
		});

		return future;
	}

	/**
	 * キャッシュマネージャーを取得する
	 * @return キャッシュマネージャー
	 */
	private static EmbeddedCacheManager getCacheManager() {
		return ServiceRegistry.getRegistry().getService(InfinispanService.class).getCacheManager();
	}
}
