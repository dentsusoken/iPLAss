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

import java.io.Serializable;

/**
 * AsyncTaskInfoを検索する際の検索条件。
 *
 * @author K.Higuchi
 *
 */
public class AsyncTaskInfoSearchCondtion implements Serializable {

	private static final long serialVersionUID = -296441816095609625L;

	private String queue;
	private String groupingKey;
	private Long taskId;
	private TaskStatus status;
	private Integer retryCount;
	private Boolean returnResult;

	private boolean withHistory = false;

	private Integer limit;
	private Integer offset;

	public AsyncTaskInfoSearchCondtion() {
	}

	public AsyncTaskInfoSearchCondtion(String queue) {
		this.queue = queue;
	}

	public String getQueue() {
		return queue;
	}

	/**
	 * キュー名を設定。未指定の場合はデフォルトキューとみなす。
	 * @param queue
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getGroupingKey() {
		return groupingKey;
	}
	/**
	 * グループキーを指定。
	 * @param groupingKey
	 */
	public void setGroupingKey(String groupingKey) {
		this.groupingKey = groupingKey;
	}
	public Long getTaskId() {
		return taskId;
	}
	/**
	 * タスクIDを指定。
	 * @param taskId
	 */
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public TaskStatus getStatus() {
		return status;
	}
	/**
	 * ステータスを指定。
	 * @param status
	 */
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public Integer getRetryCount() {
		return retryCount;
	}
	/**
	 * エラー発生時のリトライ実行回数を指定。指定回数以上のデータを検索する。
	 * @param retryCount
	 */
	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	public Boolean getReturnResult() {
		return returnResult;
	}
	/**
	 * 非同期タスク実行結果を返すデータのみを検索対象にする場合はtrueを設定。
	 * @param returnResult
	 */
	public void setReturnResult(Boolean returnResult) {
		this.returnResult = returnResult;
	}
	public boolean isWithHistory() {
		return withHistory;
	}
	/**
	 * すでに実行完了（もしくはキャンセルされた）した過去のデータを検索対象にする場合はtrueを設定。
	 * @param withHistory
	 */
	public void setWithHistory(boolean withHistory) {
		this.withHistory = withHistory;
	}
	public Integer getLimit() {
		return limit;
	}
	/**
	 * 検索件数を指定。
	 *
	 * @param limit
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getOffset() {
		return offset;
	}
	/**
	 * 検索結果のオフセットを指定。limit指定がされている前提。
	 * @param offset
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

}
