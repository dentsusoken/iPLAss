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

/**
 * infinispan メンバーノード実行用管理タスクインターフェース
 *
 * <p>
 * infinispan を利用してメンバーノードへ処理を依頼する処理インターフェース。
 * </p>
 *
 * @param <T> 処理結果データ型
 * @author SEKIGUCHI Naoya
 */
interface InfinispanManagedTask<T> extends SerializableFunction<EmbeddedCacheManager, InfinispanManagedTaskResult<T>> {
	/**
	 * 要求IDを取得する
	 *
	 * <p>
	 * infinispan へ処理を要求する際に、処理を識別するための一意なID
	 * </p>
	 *
	 * @return 要求ID
	 */
	String getRequestId();

	/**
	 * 処理を要求したノード名を取得する
	 * @return 処理を要求したノード名
	 */
	String getRequestNode();

	/**
	 * 処理名を取得する
	 *
	 * @return 処理名
	 */
	String getTaskName();
}
