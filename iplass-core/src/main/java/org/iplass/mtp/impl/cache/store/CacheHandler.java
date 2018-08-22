/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.cache.store;

import java.util.List;
import java.util.concurrent.Future;

public interface CacheHandler {

	/**
	 * 引数のtaskの処理を並列実行する。
	 * 各taskの処理は別トランザクションとして実行される。
	 *
	 * @param task 並列実行する処理を実装したCacheHandlerTask
	 * @param inputKeys 処理対象のCacheを指し示すKeyのリスト。未指定の場合は全件対象。
	 * @return 並列実行された処理結果のリスト
	 */
	public <K, V, R> List<? extends Future<R>> executeParallel(CacheHandlerTask<K, V, R> task, @SuppressWarnings("unchecked") K... inputKeys);

}
