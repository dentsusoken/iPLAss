/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.async;

import org.iplass.mtp.impl.async.AsyncTaskContextImpl;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.tenant.Tenant;

/**
 * 非同期実行コンテキスト。
 * 非同期実行タスク内で、非同期実行処理に関する情報（タスクID、キュー名）を取得する際に利用する。
 * 
 * @author K.Higuchi
 *
 */
public abstract class AsyncTaskContext {
	
	/**
	 * 現在のAsyncTaskContextのインスタンスを取得。
	 * これを呼び出した処理が、非同期処理で呼ばれたタスクではない場合は、nullが返却される。
	 * 
	 * @return
	 */
	public static AsyncTaskContext getCurrentContext() {
		return (AsyncTaskContext) ExecuteContext.getCurrentContext().getAttribute(AsyncTaskContextImpl.EXE_CONTEXT_ATTR_NAME);
	}
	
	/**
	 * 現在実行中の非同期タスクのタスクIDを取得。
	 * 
	 * @return
	 */
	public abstract long getTaskId();
	
	/**
	 * 現在実行中の非同期タスクのキュー名を取得。
	 * 
	 * @return
	 */
	public abstract String getQueueName();
	
	/**
	 * 現在実行中の非同期タスクのテナントを取得。
	 * @return
	 */
	public abstract Tenant getTenant();

}
