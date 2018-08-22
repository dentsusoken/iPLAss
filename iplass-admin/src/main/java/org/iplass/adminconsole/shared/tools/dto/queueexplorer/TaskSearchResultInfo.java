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

package org.iplass.adminconsole.shared.tools.dto.queueexplorer;

import java.io.Serializable;

import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.async.TaskStatus;

public class TaskSearchResultInfo implements Serializable {

	private static final long serialVersionUID = 7437400838746991732L;

	private String queue;
	private long taskId;
	private String groupingKey;
	private TaskStatus status;
	private int retryCount;
	private ExceptionHandlingMode exceptionHandlingMode;
//	private boolean returnResult;
	private String taskInfo;
	private String resultInfo;

	public TaskSearchResultInfo() {
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getGroupingKey() {
		return groupingKey;
	}

	public void setGroupingKey(String groupingKey) {
		this.groupingKey = groupingKey;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public ExceptionHandlingMode getExceptionHandlingMode() {
		return exceptionHandlingMode;
	}

	public void setExceptionHandlingMode(ExceptionHandlingMode exceptionHandlingMode) {
		this.exceptionHandlingMode = exceptionHandlingMode;
	}

//	public boolean isReturnResult() {
//		return returnResult;
//	}
//
//	public void setReturnResult(boolean returnResult) {
//		this.returnResult = returnResult;
//	}

	public String getTaskInfo() {
		return taskInfo;
	}

	public void setTaskInfo(String taskInfo) {
		this.taskInfo = taskInfo;
	}

	public String getResultInfo() {
		return resultInfo;
	}

	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}

}
