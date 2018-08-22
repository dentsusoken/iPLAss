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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.iplass.mtp.Manager;

/**
 * 非同期実行を管理するManager。
 * 非同期処理は、ローカルスレッド実行指定されない限り、一旦永続Storeに保存される。
 * そのため、ローカルスレッド実行でないtaskはSerializbleを実装する必要がある。
 * また、taskが実行結果を返す場合はその実行結果もSerializbleを実装する必要がある。
 *
 * @author K.Higuchi
 *
 */
public interface AsyncTaskManager extends Manager {

	/**
	 * ローカルスレッドでタスクを非同期実行する。<br>
	 * ローカルスレッド実行では以下の制約がある。<br>
	 * <ul>
	 * <li>トランザクション管理対象外：呼び出し元がロールバックしても、非同期処理が継続</li>
	 * <li>{@link #getResult(long, String)}による問い合わせ不可</li>
	 * <li>リトライ、タイムアウトなどの制御なし</li>
	 * </ul>
	 *
	 * @param task 非同期実行する処理
	 * @return 実行結果を表すFuture
	 */
	public <V> Future<V> executeOnThread(Callable<V> task);

	/**
	 * デフォルトの実行オプション設定にて指定のtaskを非同期実行する。
	 * デフォルトの設定は{@link AsyncTaskOption}を参照のこと。
	 *
	 * @param task 非同期実行する処理
	 * @return 実行結果を表すAsyncTaskFuture
	 */
	public <V> AsyncTaskFuture<V> execute(Callable<V> task);

	/**
	 * 指定のtask, optionで表現される非同期タスクを実行する。<br>
	 * ローカルスレッド実行指定（{@link AsyncTaskManager#executeOnThread(Callable)}
	 *  or optionのキュー名に{@link AsyncTaskManager#LOCAL_THREAD_QUEUE_NAME}指定)されない限り、
	 *  taskおよび、型VはSerializableを実装する必要がある。<br>
	 * また、ロカールスレッド実行ではない場合、
	 * このメソッドの結果として返される、{@link AsyncTaskFuture}のインスタンスの扱いは注意が必要。
	 * ロカールスレッド実行に比較して、キューを介したタスクの処理には比較的処理時間がかかるため、
	 * また、{@link StartMode.AFTER_COMMIT}の場合、
	 * 呼び出すスレッドのトランザクションが完了するまで非同期タスクの実行が開始されないため、
	 * {@link AsyncTaskFuture#get()}の呼び出しはタイムアウトする可能性が高い。
	 * 別のタイミングで、{@link #getResult(long, String)}を呼び出し、結果を取得すること。
	 * @param option 非同期タスク実行オプション
	 * @param task 非同期実行する処理
	 *
	 * @return 実行結果を表すAsyncTaskFuture
	 */
	public <V> AsyncTaskFuture<V> execute(AsyncTaskOption option, Callable<V> task);

	/**
	 * 指定のtaskId,queueNameで特定される非同期タスクの実行結果を問い合わせる。
	 *
	 * @param taskId
	 * @param queueName キュー名、nullの場合はデフォルトキュー指定されたとみなす
	 * @return 実行結果を表すAsyncTaskFuture
	 */
	public <V> AsyncTaskFuture<V> getResult(long taskId, String queueName);

	/**
	 * AsyncTaskInfoの詳細を取得する。
	 * task、およびresultもロードする。
	 *
	 * @param taskId
	 * @param queueName キュー名、nullの場合はデフォルトキュー指定されたとみなす
	 * @return
	 */
	public AsyncTaskInfo loadAsyncTaskInfo(long taskId, String queueName);

	/**
	 * 永続Storeに保存されている（ローカルスレッド実行でない）非同期タスクの情報を取得する。
	 * このメソッドを利用して返却されるAsyncTaskInfoには、taskおよび、resultは含まれない（メモリ負荷を考慮し）。
	 * これらを取得する場合は、別途{@link #loadAsyncTaskInfo(long, String)}を利用する。
	 *
	 * @param cond
	 * @return
	 */
	public List<AsyncTaskInfo> searchAsyncTaskInfo(AsyncTaskInfoSearchCondtion cond);

	/**
	 * AsyncTaskInfoを強制削除する。
	 *
	 * @param taskId
	 * @param queueName キュー名、nullの場合はデフォルトキュー指定されたとみなす
	 */
	public void forceDelete(long taskId, String queueName);

}
