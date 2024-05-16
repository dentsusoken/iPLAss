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
 * テストユーティリティ設定インターフェース
 *
 * <p>
 * テストユーティリティで利用する設定情報を管理する。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public interface MTPTestConfig {
	/**
	 * 設定ファイル名を取得する
	 * @return 設定ファイル名
	 */
	String getConfigFileName();

	/**
	 * テナント名を取得する
	 * @return テナント名
	 */
	String getTenantName();

	/**
	 * ログインユーザーIDを取得する
	 * @return ユーザーID
	 */
	String getUserId();

	/**
	 * ログインパスワードを取得する
	 * @return ログインパスワード
	 */
	String getPassword();

	/**
	 * トランザクションをロールバックするか
	 * @return トランザクションをロールバックするか（ロールバックする場合 true）
	 */
	Boolean isRollbackTransaction();

	/**
	 *  &#64;NoAuth が付与されているか
	 *
	 * <p>
	 * &#64;NoAuth は クラス、メソッドのいずれかに設定される。
	 * </p>
	 *
	 * @return &#64;NoAuth が付与されているか（&#64;NoAuth が付与されている場合 true）
	 */
	Boolean isNoAuth();

	/**
	 * テストが groovy であるか
	 * @return テストが groovy であるか（groovy テストの場合 true）
	 */
	Boolean isGroovy();
}
