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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.infinispan.remoting.transport.Address;

/**
 * タスク実行結果格納用の Future 生成機能
 *
 * @author SEKIGUCHI Naoya
 */
class InfinispanTaskFutureFactory {
	/**
	 * プライベートコンストラクタ
	 */
	private InfinispanTaskFutureFactory() {
	}

	/**
	 * Future インスタンスを生成する
	 *
	 * <p>
	 * タスク実行結果の正常・異常終了を判別しインスタンスを返却する。
	 * </p>
	 *
	 * @param <T> 実行結果データ型
	 * @param parent 本処理実行時のFuture
	 * @param address 処理を実行したノード
	 * @param result 実行結果
	 * @return 実行結果に応じたFutureインスタンス
	 */
	public static <T> InfinispanTaskFuture<T> create(Future<Void> parent, Address address, InfinispanManagedTaskResult<T> result) {
		if (result.isSuccess()) {
			return new SuccessFuture<T>(parent, address, result.getResult());
		}

		return new FailureFuture<T>(parent, address, result.getCause());
	}

	/**
	 * 実行失敗用の Future インスタンスを生成する
	 *
	 * @param <T> 実行結果データ型
	 * @param parent 本処理実行時のFuture
	 * @param address 処理を実行したノード
	 * @param cause 失敗原因例外
	 * @return 失敗 Future インスタンス
	 */
	public static <T> InfinispanTaskFuture<T> create(Future<Void> parent, Address address, Throwable cause) {
		return new FailureFuture<T>(parent, address, cause);
	}

	/**
	 * 抽象 Future クラス
	 * @param <T> 実行結果データ型
	 */
	static abstract class AbstractFuture<T> implements InfinispanTaskFuture<T> {
		/** 本処理実行時のFuture */
		private Future<Void> parent;
		/** 実行ノードアドレス */
		private String node;

		/**
		 * コンストラクタ
		 * @param parent 本処理実行時のFuture
		 * @param address 処理を実行したノード
		 */
		private AbstractFuture(Future<Void> parent, Address address) {
			this.parent = parent;
			this.node = address.toString();
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
		public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			return get();
		}

		@Override
		public String getNode() {
			return node;
		}
	}

	/**
	 * 正常終了 Future クラス
	 * @param <T> 実行結果データ型
	 */
	static class SuccessFuture<T> extends AbstractFuture<T> {
		/** 実行結果 */
		private T result;

		/**
		 * コンストラクタ
		 * @param parent 本処理実行時のFuture
		 * @param address 処理を実行したノード
		 * @param result 実行結果
		 */
		private SuccessFuture(Future<Void> parent, Address address, T result) {
			super(parent, address);
			this.result = result;
		}

		@Override
		public T get() throws InterruptedException, ExecutionException {
			return result;
		}
	}

	/**
	 * 異常終了 Future クラス
	 * @param <T> 実行結果データ型
	 */
	static class FailureFuture<T> extends AbstractFuture<T> {
		/** 原因例外 */
		private Throwable cause;

		/**
		 * コンストラクタ
		 * @param parent 本処理実行時のFuture
		 * @param address 処理を実行したノード
		 * @param cause 原因例外
		 */
		private FailureFuture(Future<Void> parent, Address address, Throwable cause) {
			super(parent, address);
			this.cause = cause;
		}

		@Override
		public T get() throws InterruptedException, ExecutionException {
			if (cause instanceof InterruptedException) {
				throw (InterruptedException) cause;
			} else if (cause instanceof ExecutionException) {
				throw (ExecutionException) cause;
			}

			throw new ExecutionException(cause);
		}
	}

}
