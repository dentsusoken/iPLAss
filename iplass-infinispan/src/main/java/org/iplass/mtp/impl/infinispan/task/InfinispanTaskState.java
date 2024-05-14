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

import java.util.List;
import java.util.concurrent.Future;

/**
 * Infinispan タスク実行状態
 *
 * <p>
 * {@link org.iplass.mtp.impl.infinispan.task.InfinispanTaskExecutor} によりタスクを実行した際の、タスク実行状態を保持する。
 * </p>
 *
 * @see org.iplass.mtp.impl.infinispan.task.InfinispanTaskExecutor
 * @param <T> 実行結果データ型
 */
public class InfinispanTaskState<T> {
	/** 要求ID */
	private String requestId;
	/** タスク実行時の Future インスタンスリスト */
	private List<Future<T>> futureList;

	/**
	 * コンストラクタ
	 * @param requestId 要求ID
	 * @param futureList タスク実行時の Future インスタンスリスト
	 */
	public InfinispanTaskState(String requestId, List<Future<T>> futureList) {
		this.requestId = requestId;
		this.futureList = futureList;
	}

	/**
	 * 要求IDを取得する
	 * @return 要求ID
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * 非同期処理の Future リストを取得する
	 *
	 * @return Future リスト
	 */
	public List<Future<T>> getFuture() {
		return futureList;
	}
}
