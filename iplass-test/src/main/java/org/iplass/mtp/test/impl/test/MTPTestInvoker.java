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
package org.iplass.mtp.test.impl.test;

/**
 * テスト実行インターフェース
 *
 * <p>
 * テスト実行時の前後処理拡張を行う際の、テスト実行処理を定義するインターフェース。
 * </p>
 *
 * @param <T> 返却データ型
 * @author SEKIGUCHI Naoya
 */
public interface MTPTestInvoker<T> {
	/**
	 * テストを実行する
	 * @param config テスト設定
	 * @return テスト実行後返却値
	 * @throws Throwable 例外
	 */
	T invoke(MTPTestConfig config) throws Throwable;
}
