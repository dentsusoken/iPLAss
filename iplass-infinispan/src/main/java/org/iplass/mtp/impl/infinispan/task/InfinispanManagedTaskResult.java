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

import java.io.Serializable;

/**
 * infinispan メンバーノード実行用管理タスク実行結果
 *
 * <p>
 * result もしくは cause いずれかに値が設定される。
 * </p>
 *
 * @param <T> 処理結果データ型
 * @author SEKIGUCHI Naoya
 */
class InfinispanManagedTaskResult<T> implements Serializable {

	private static final long serialVersionUID = -3235712138004688981L;

	/** 実行結果が null のインスタンス */
	private static final InfinispanManagedTaskResult<?> NULL_RESULT_INSTANCE = new InfinispanManagedTaskResult<>();

	/** 実行結果 */
	private T result = null;
	/** 例外発生時の原因例外 */
	private Throwable cause = null;
	/** 処理が成功しているか */
	private boolean isSuccess = true;

	/**
	 * 実行結果無し用コンストラクタ
	 */
	private InfinispanManagedTaskResult() {
	}

	/**
	 * 実行結果用コンストラクタ
	 * @param result 実行結果
	 */
	private InfinispanManagedTaskResult(T result) {
		this.result = result;
	}

	/**
	 * 例外用コンストラクタ
	 *
	 * <p>
	 * 本コンストラクタを利用した場合のみ、isSuccess に false を設定する
	 * </p>
	 *
	 * @param cause 原因例外
	 */
	private InfinispanManagedTaskResult(Throwable cause) {
		this.isSuccess = false;
		this.cause = cause;
	}

	/**
	 * 実行結果が null となるインスタンス取得
	 * @param <R> 実行結果データ型
	 * @return null インスタンス
	 */
	@SuppressWarnings("unchecked")
	public static <R> InfinispanManagedTaskResult<R> nullValue() {
		return (InfinispanManagedTaskResult<R>) NULL_RESULT_INSTANCE;
	}

	/**
	 * 正常終了インスタンスを作成する
	 *
	 * <p>
	 * 実行結果が null の場合は null インスタンスを、それ以外は新規に作成する。
	 * </p>
	 *
	 * @param <R> 実行結果データ型
	 * @param result 実行結果
	 * @return 正常終了インスタンス
	 */
	public static <R> InfinispanManagedTaskResult<R> create(R result) {
		return result == null ? nullValue() : new InfinispanManagedTaskResult<R>(result);
	}

	/**
	 * 異常終了インスタンスを作成する
	 * @param <R> 実行結果データ型
	 * @param cause 原因例外
	 * @return 異常終了インスタンス
	 */
	public static <R> InfinispanManagedTaskResult<R> create(Throwable cause) {
		return new InfinispanManagedTaskResult<R>(cause);
	}

	/**
	 * 処理が成功しているか判定する
	 * @return 処理の結果（true の場合、正常終了）
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * 実行結果を取得する
	 * @return 実行結果
	 */
	public T getResult() {
		return result;
	}

	/**
	 * 原因例外を取得する
	 * @return 原因例外
	 */
	public Throwable getCause() {
		return cause;
	}
}
