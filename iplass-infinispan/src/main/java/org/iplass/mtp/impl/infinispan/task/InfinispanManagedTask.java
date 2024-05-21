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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	/** 要求ID */
	private String requestId;
	/** 要求ノード */
	private String requestNode;
	/** タスク名 */
	private String taskName;

	/**
	 * コンストラクタ
	 * @param task タスク
	 * @param requestId 要求ID
	 * @param requestNode 要求ノード
	 */
	public InfinispanManagedTask(InfinispanSerializableTask<T> task, String requestId, String requestNode) {
		this.task = task;
		this.requestId = requestId;
		this.requestNode = requestNode;
		this.taskName = task.getTaskName();
	}

	@Override
	public InfinispanManagedTaskResult<T> apply(EmbeddedCacheManager t) {
		String executionNode = InfinispanUtil.getExecutionNode();
		LOG.debug("{} executes the request {}({}) from {}.", executionNode, taskName, requestId, requestNode);
		try {
			T result = task.call();
			LOG.debug("{} completed request {}({}) from {}.", executionNode, taskName, requestId, requestNode);
			return InfinispanManagedTaskResult.create(result);

		} catch (Throwable e) {
			// NOTE ここで例外をリスローすると非同期処理の終了が通知されなくなる。そのため、Error, Runtime系を含めて例外をリスローせずログ出力し終了する。
			LOG.error("{} failed to request {}({}) from {}.", executionNode, taskName, requestId, requestNode, e);
			return InfinispanManagedTaskResult.create(e);
		}
	}

	/**
	 * 要求IDを取得する
	 *
	 * <p>
	 * infinispan へ処理を要求する際に、処理を識別するための一意なID
	 * </p>
	 *
	 * @return 要求ID
	 */
	public String getRequestId() {
		return requestId;
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

}
