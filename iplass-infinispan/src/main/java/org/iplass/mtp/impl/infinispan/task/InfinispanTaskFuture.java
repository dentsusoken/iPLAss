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

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.remoting.transport.Address;

/**
 * タスク実行結果 Future クラス
 *
 * <p>
 * 作成時点では結果が格納されていないが、親 Future が完了すれば結果を返却可能な Future。
 * Infinispan の非同期タスクの実行結果は、ノード毎実行結果から取得する。
 * </p>
 *
 * @see org.iplass.mtp.impl.infinispan.task.InfinispanTaskExecutor
 * @param <T> 実行結果データ型
 * @author SEKIGUCHI Naoya
 */
public class InfinispanTaskFuture<T> implements Future<T> {
	/** 親 Future */
	private Future<Void> parent;
	/** 実行対象ノード */
	private Address node;
	/** ノード毎実行結果 */
	private Map<Address, InfinispanManagedTaskResult<T>> taskResultParNode;

	/**
	 * コンストラクタ
	 *
	 * @param parent 本処理実行時のFuture
	 * @param node 処理を実行したノード
	 * @param taskResultParNode ノード毎実行結果
	 */
	public InfinispanTaskFuture(Future<Void> parent, Address node, Map<Address, InfinispanManagedTaskResult<T>> taskResultParNode) {
		this.parent = parent;
		this.node = node;
		this.taskResultParNode = taskResultParNode;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return parent.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return parent.isCancelled();
	}

	@Override
	public boolean isDone() {
		return parent.isDone();
	}

	@Override
	public T get() throws InterruptedException, ExecutionException {
		parent.get();

		return getResult();
	}

	@Override
	public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		parent.get(timeout, unit);

		return getResult();
	}

	/**
	 * 本Futureの実行ノードを取得する
	 * @return 実行ノード
	 */
	public String getNode() {
		return node.toString();
	}

	/**
	 * 実行結果を取得する
	 *
	 * <p>
	 * 実行結果が正常であれば、実行結果を返却する。
	 * 例外が発生していたら、例外をスローする。
	 * </p>
	 *
	 * @return ノードで実行した結果
	 * @throws InterruptedException 割り込み例外
	 * @throws ExecutionException 実行例外
	 */
	private T getResult() throws InterruptedException, ExecutionException {
		// parent が完了していれば、taskResultParNode 変数には node の実行結果が格納されている
		InfinispanManagedTaskResult<T> taskResult = taskResultParNode.get(node);

		if (null == taskResult) {
			// 実行結果が存在しない場合は、何らかの原因で結果が取得できなかった
			throw new InfinispanTaskExecutionException("The execution result for node '" + node.toString() + "' does not exist.");
		}

		if (!taskResult.isSuccess()) {
			// 異常パターン
			throw new ExecutionException("An exception occurred during execution of node '" + node.toString() + "'.", taskResult.getCause());
		}

		// 正常パターン
		return taskResult.getResult();
	}
}
