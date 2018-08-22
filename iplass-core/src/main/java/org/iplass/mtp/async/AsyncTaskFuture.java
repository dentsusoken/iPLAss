/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 非同期タスクの結果を表すクラス。
 * 
 * @author K.Higuchi
 *
 * @param <V>
 */
public interface AsyncTaskFuture<V> extends Future<V> {
	
	/**
	 * 非同期タスクのタスクid。
	 * 
	 * @return
	 */
	public long getTaskId();
	
	/**
	 * 非同期タスクが格納（実行）されたキュー名。
	 * 
	 * @return
	 */
	public String getQueueName();
	
	/**
	 * 非同期タスクの現在の実行ステータス。
	 * 
	 * @return
	 */
	public TaskStatus getStatus();
	
	/**
	 * この非同期タスクをキャンセルする。<br>
	 * ローカルスレッド実行ではない場合は、
	 * 非同期タスクに対する指示もトランザクション管理される。
	 * もし、このcancelを呼び出す処理がロールバックされた場合、
	 * cancel呼び出し自体もロールバックされるので注意。
	 * もし、トランザクションが失敗してもcancel呼び出しを確定したい場合は、
	 * 別トランザクション（Transactionを利用し）でcancelを呼び出すようにする。
	 * 
	 * @param mayInterruptIfRunning ローカルスレッド実行ではない場合、この引数は意味を持たない
	 * 
	 * @see org.iplass.mtp.transaction.Transaction
	 * 
	 */
	public boolean cancel(boolean mayInterruptIfRunning);

	/**
	 * 非同期タスクの実行結果を取得する。
	 * ただし、非同期タスクが実行結果を返す場合に限る。
	 * 
	 */
	public V get() throws InterruptedException, ExecutionException;
	

}
