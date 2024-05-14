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

/**
 * タスク名を設定するインターフェース
 *
 * <p>
 * Infinispan 管理タスクで依頼されたタスク名を決定する際、
 * タスクに本インターフェースが実装されていた場合は {@link #getTaskName()} からタスク名を取得する。
 * </p>
 *
 * <p>
 * 実装例： InfinispanSerializableTask が実装されているクラスを対象にする
 * <pre><code>
 * public class SomeTaskImpl implements InfinispanSerializableTask, InfinispanNamedTask {
 *   // :
 *   // :
 *
 *   &#64;Override
 *   public String getTaskName() {
 *     return "myTaskName";
 *   }
 * }
 * </code></pre>
 * </p>
 *
 * @see org.iplass.mtp.impl.infinispan.task.InfinispanManagedTaskImpl
 * @see org.iplass.mtp.impl.infinispan.task.InfinispanSerializableTask
 * @author SEKIGUCHI Naoya
 */
public interface InfinispanNamedTask {
	/**
	 * タスク名を取得する
	 * @return タスク名
	 */
	String getTaskName();
}
