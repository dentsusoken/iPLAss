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

import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.util.function.SerializableFunction;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.infinispan.InfinispanConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * infinispan メンバーノード実行用管理タスク
 *
 * <p>
 * infinispan への要求は本クラスに変換して実行する。
 * </p>
 *
 * <p>
 * タスク実行中に上位へ例外をスローすると処理を infinispan で継続することができなくなってしまう。
 * そのため、正常終了の場合も、異常終了の場合も結果を返却する。
 * </p>
 *
 * <p>
 * 本クラスで保持している infinispanRequestId は Infinispan を利用した非同期処理の要求単位で発行される一意なIDである。
 * 実行時に MDC キー "infinispanRequestId" として設定している。
 * </p>
 *
 * @param <T> 処理結果データ型
 * @author SEKIGUCHI Naoya
 */
class InfinispanManagedTask<T> implements SerializableFunction<EmbeddedCacheManager, InfinispanManagedTaskResult<T>> {
	/** serialVersionUID */
	private static final long serialVersionUID = 1829338323561254174L;

	/** ロガー */
	private static transient Logger LOG = LoggerFactory.getLogger(InfinispanManagedTask.class);

	/** 実タスク */
	private InfinispanSerializableTask<T> task;
	/** 要求ノード */
	private String requestNode;
	/** Infinispan要求ID */
	private String infinispanRequestId;
	/** MDC用トレースID */
	private String traceId;
	/** タスク名 */
	private String taskName;

	/**
	 * コンストラクタ
	 * @param task タスク
	 * @param requestNode 要求ノード
	 * @param infinispanRequestId Infinispan要求ID
	 * @param traceId タスク要求時に設定されているトレースID。タスク要求先に転送し、同一値を設定する。
	 */
	public InfinispanManagedTask(InfinispanSerializableTask<T> task, String requestNode, String infinispanRequestId, String traceId) {
		this.task = task;
		this.requestNode = requestNode;
		this.infinispanRequestId = infinispanRequestId;
		this.traceId = traceId;

		this.taskName = task.getTaskName();
	}

	@Override
	public InfinispanManagedTaskResult<T> apply(EmbeddedCacheManager t) {
		// コンテキストを設定し、処理を実行する
		return executeAs(() -> {
			String executionNode = InfinispanUtil.getExecutionNode();

			LOG.debug("{} executes the request {}({}) from {}.", executionNode, taskName, infinispanRequestId, requestNode);
			try {
				T result = task.call();
				LOG.debug("{} completed request {}({}) from {}.", executionNode, taskName, infinispanRequestId, requestNode);
				return InfinispanManagedTaskResult.create(result);

			} catch (Throwable e) {
				// NOTE ここで例外をリスローすると非同期処理の終了が通知されなくなる。そのため、Error, Runtime系を含めて例外をリスローせずログ出力し終了する。
				LOG.error("{} failed to request {}({}) from {}.", executionNode, taskName, infinispanRequestId, requestNode, e);
				return InfinispanManagedTaskResult.create(e);
			}
		});
	}

	/**
	 * Infinispan 要求IDを取得する
	 *
	 * <p>
	 * infinispan へ処理を要求する際に、処理を識別するための一意なID
	 * </p>
	 *
	 * @return 要求ID
	 */
	public String getInfinispanRequestId() {
		return infinispanRequestId;
	}

	/**
	 * 処理を要求したノード名を取得する
	 * @return 処理を要求したノード名
	 */
	public String getRequestNode() {
		return requestNode;
	}

	/**
	 * 処理名を取得する
	 *
	 * @return 処理名
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * ExecuteContext を設定し処理を実行する
	 *
	 * <p>
	 * ExecuteContext は現在コンテキストを利用する。
	 * コンテキストに対してトレースIDを設定する。
	 * </p>
	 *
	 * @param <R> 実行結果データ型
	 * @param executable 実行する処理
	 * @return 実行結果
	 */
	private <R> R executeAs(Executable<R> executable) {
		var context = ExecuteContext.getCurrentContext();
		var tenantContext = context.getTenantContext();

		return ExecuteContext.executeAs(tenantContext, () -> {
			var currentContext = ExecuteContext.getCurrentContext();
			// 元のトレースIDを退避
			var prevTraceId = MDC.get(ExecuteContext.MDC_TRACE_ID);
			// トレースIDを設定
			currentContext.mdcPut(ExecuteContext.MDC_TRACE_ID, traceId);
			// リクエストIDを設定
			currentContext.mdcPut(InfinispanConstants.LogMDC.REQUEST_ID, infinispanRequestId);

			try {
				return executable.execute();

			} finally {
				// トレースIDを復元
				currentContext.mdcPut(ExecuteContext.MDC_TRACE_ID, prevTraceId);
				currentContext.mdcPut(InfinispanConstants.LogMDC.REQUEST_ID, null);
			}

		});
	}
}
