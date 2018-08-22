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

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * キャッシュストアに対して並列タスクを実行する際の、
 * ロジック実装するインタフェース。
 * 
 * @author K.Higuchi
 *
 * @param <K> 処理対象のキャッシュのKey
 * @param <V> キャッシュのValue
 * @param <R> 処理結果
 */
public interface CacheHandlerTask<K, V, R> extends Callable<R>, Serializable {
	
	/**
	 * 処理対象がこのメソッドでセットされる。
	 * 
	 * @param cache キャッシュストア
	 * @param inputKeys このタスクの処理対象のKeyのセット
	 */
	public void setContext(CacheContext<K, V> cache, Set<K> inputKeys);

}
