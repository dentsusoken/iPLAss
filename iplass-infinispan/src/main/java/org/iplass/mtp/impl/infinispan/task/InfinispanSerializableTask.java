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

import org.infinispan.util.function.SerializableCallable;

/**
 * Infinispan へ処理を依頼するタスクインターフェース
 *
 * <p>
 * 各ノードで実行する処理を実装すること。
 * </p>
 *
 * @param <T> 処理結果データ型
 * @author SEKIGUCHI Naoya
 */
public interface InfinispanSerializableTask<T> extends SerializableCallable<T> {
	@Override
	default T call() throws Exception {
		return callByNode();
	}

	/**
	 * ノードで実行する処理
	 *
	 * @return 実行結果
	 * @throws Exception 実行例外
	 */
	T callByNode() throws Exception;
}
