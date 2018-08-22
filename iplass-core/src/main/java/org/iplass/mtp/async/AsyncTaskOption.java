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

/**
 * 非同期タスクの実行オプションを表すクラスです。
 * 実行時の設定（キュー名、実行モードなど）を指定可能です。
 * 
 * @author K.Higuchi
 *
 */
public class AsyncTaskOption {
	
	/**
	 * デフォルトキューを指し示すキュー名です。値は"default"です。
	 */
	public static final String DEFAULT_QUEUE_NAME ="default";
	
	/**
	 * ローカルスレッドで実行する際のキュー名です。値は"localThread"です。
	 */
	public static final String LOCAL_THREAD_QUEUE_NAME ="localThread";
	
	private String queue;
	private String groupingKey;
	private StartMode startMode = StartMode.AFTER_COMMIT;
	private ExceptionHandlingMode exceptionHandlingMode = ExceptionHandlingMode.RESTART;
	private boolean returnResult = false;
	
	private long executionTime;
	
	/**
	 * デフォルトの実行設定、デフォルトのキューで実行するAsyncTaskOptionを構築します。
	 * 実際のオプションの値は、
	 * queue=AsyncTaskOption.DEFAULT_QUEUE_NAME,
	 * groupingKey=null,
	 * startMode=StartMode.AFTER_COMMIT,
	 * exceptionHandlingMode=ExceptionHandlingMode.RESTART,
	 * returnResult=false,
	 * executionTime=0
	 * で初期化されます。
	 */
	public AsyncTaskOption() {
	}
	
	/**
	 * returnResult以外をデフォルト設定、デフォルトのキューで実行するAsyncTaskOptionを構築します。
	 * 実際のオプションの値は、
	 * queue=AsyncTaskOption.DEFAULT_QUEUE_NAME,
	 * groupingKey=null,
	 * startMode=StartMode.AFTER_COMMIT,
	 * exceptionHandlingMode=ExceptionHandlingMode.RESTART,
	 * executionTime=0
	 * で初期化されます。
	 * 
	 * @param returnResult この非同期タスクが結果値を返す場合はtrue
	 */
	public AsyncTaskOption(boolean returnResult) {
		this.returnResult = returnResult;
	}
	
	/**
	 * AsyncTaskOptionのコンストラクタです。
	 * 
	 * @param queue キュー名
	 * @param groupingKey グループキー
	 * @param startMode 非同期処理開始モード
	 * @param exceptionHandlingMode 例外処理モード
	 * @param returnResult 非同期処理が結果を返す場合true
	 * @param executionTime 実行開始時間。現在時間以下の場合は、即座に開始。
	 */
	public AsyncTaskOption(String queue, String groupingKey,
			StartMode startMode,
			ExceptionHandlingMode exceptionHandlingMode, boolean returnResult, long executionTime) {
		this.queue = queue;
		this.groupingKey = groupingKey;
		this.startMode = startMode;
		this.exceptionHandlingMode = exceptionHandlingMode;
		this.returnResult = returnResult;
		this.executionTime = executionTime;
	}
	
	/**
	 * キュー名を指定します。
	 * 
	 * @param queue
	 * @return
	 */
	public AsyncTaskOption queue(String queue) {
		this.queue = queue;
		return this;
	}
	
	/**
	 * 一連のタスクをグループ化するgroupingKeyを指定します。
	 * groupingKeyを設定すると、そのグループのタスクは必ず同一のタスク実行Workerで実行されるようになります。
	 * 「厳密な実行順」を設定されたキューを利用し、このgroupingKeyを指定した場合、
	 * そのgroupingKeyにおいてタスクの実行順序を保障することが可能となります。
	 * 
	 * @param groupingKey
	 * @return
	 */
	public AsyncTaskOption groupingKey(String groupingKey) {
		this.groupingKey = groupingKey;
		return this;
	}
	
	/**
	 * 非同期タスクの開始方法を指定します。
	 * 
	 * @param startMode
	 * @return
	 */
	public AsyncTaskOption startMode(StartMode startMode) {
		this.startMode = startMode;
		return this;
	}
	
	/**
	 * 例外発生時の処理方法を指定します。
	 * 
	 * @param exceptionHandlingMode
	 * @return
	 */
	public AsyncTaskOption exceptionHandlingMode(ExceptionHandlingMode exceptionHandlingMode) {
		this.exceptionHandlingMode = exceptionHandlingMode;
		return this;
	}
	
	/**
	 * 非同期タスクが処理結果を返却することを指定します。
	 * 処理結果を返す場合は、{@link AsyncTaskFuture#get()}で結果の取得が可能です。
	 * 
	 * @return
	 */
	public AsyncTaskOption returnResult() {
		this.returnResult = true;
		return this;
	}
	
	/**
	 * 非同期処理の開始時間を指定します。
	 * 現在時間より前が指定された場合はタスクが登録完了後、即座に開始されます。
	 * 
	 * @param executionTime
	 * @return
	 */
	public AsyncTaskOption executionTime(long executionTime) {
		this.executionTime = executionTime;
		return this;
	}
	
	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
	public ExceptionHandlingMode getExceptionHandlingMode() {
		return exceptionHandlingMode;
	}
	public void setExceptionHandlingMode(ExceptionHandlingMode exceptionHandlingMode) {
		this.exceptionHandlingMode = exceptionHandlingMode;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getGroupingKey() {
		return groupingKey;
	}
	public void setGroupingKey(String groupingKey) {
		this.groupingKey = groupingKey;
	}
	public StartMode getStartMode() {
		return startMode;
	}
	public void setStartMode(StartMode startMode) {
		this.startMode = startMode;
	}
	public boolean isReturnResult() {
		return returnResult;
	}
	public void setReturnResult(boolean returnResult) {
		this.returnResult = returnResult;
	}
}
