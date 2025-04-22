/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.warmup;

/**
 * ウォームアップタスクインターフェース
 * <p>
 * WarmupService にタスクを設定することで、アプリケーション起動時にタスクを実行することができます。
 * 初期化処理が必要な場合は init メソッド、終了処理が必要な場合は destroy メソッドをオーバーライドしてください。
 * </p>
 *
 * @author SEKIGUCHI Naoya
 */
public interface WarmupTask {
	/**
	 * タスクの初期化処理を実行します
	 */
	default void init() {
	}

	/**
	 * タスクの終了処理を実行します
	 */
	default void destroy() {
	}

	/**
	 * タスクのウォームアップ処理を実行します。
	 * @param context ウォームアップコンテキスト
	 */
	void warmup(WarmupContext context);
}
