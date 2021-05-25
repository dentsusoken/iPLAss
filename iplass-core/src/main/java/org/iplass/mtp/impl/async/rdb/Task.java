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

package org.iplass.mtp.impl.async.rdb;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.concurrent.Callable;

import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.slf4j.MDC;

public class Task implements Serializable {
	private static final long serialVersionUID = 7838808634253106613L;
	
	private int tenantId;
	private int queueId;
	private long taskId;
	private String serverId;
	private long visibleTime;
	private TaskStatus status;
	private String groupingKey;
	private int virtualWorkerId;
	private long version;
	private Timestamp updateTime;
	private CallableInput<?> callable;
	private ExceptionHandlingMode exceptionHandlingMode;
	private boolean returnResult;
	private Object result;
	private int retryCount;
	
	public Task() {
	}
	
	public Task(Callable<?> callable, String groupingKey, ExceptionHandlingMode exceptionHandlingMode, boolean returnResult, long visibleTime) {
		this.callable = new CallableInput<>(callable, null, false ,MDC.get(ExecuteContext.MDC_TRACE_ID));
		this.groupingKey = groupingKey;
		this.exceptionHandlingMode = exceptionHandlingMode;
		this.returnResult = returnResult;
		this.visibleTime = visibleTime;
	}
	public Task(CallableInput<?> callable, String groupingKey, ExceptionHandlingMode exceptionHandlingMode, boolean returnResult, long visibleTime) {
		this.callable = callable;
		this.groupingKey = groupingKey;
		this.exceptionHandlingMode = exceptionHandlingMode;
		this.returnResult = returnResult;
		this.visibleTime = visibleTime;
	}
	
	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getTenantId() {
		return tenantId;
	}
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}
	public int getQueueId() {
		return queueId;
	}
	public void setQueueId(int queueId) {
		this.queueId = queueId;
	}
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public long getVisibleTime() {
		return visibleTime;
	}
	public void setVisibleTime(long visibleTime) {
		this.visibleTime = visibleTime;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public String getGroupingKey() {
		return groupingKey;
	}
	public void setGroupingKey(String groupingKey) {
		this.groupingKey = groupingKey;
	}
	public int getVirtualWorkerId() {
		return virtualWorkerId;
	}
	public void setVirtualWorkerId(int virtualWorkerId) {
		this.virtualWorkerId = virtualWorkerId;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public CallableInput<?> getCallable() {
		return callable;
	}
	public void setCallable(CallableInput<?> callable) {
		this.callable = callable;
	}
	public ExceptionHandlingMode getExceptionHandlingMode() {
		return exceptionHandlingMode;
	}
	public void setExceptionHandlingMode(ExceptionHandlingMode exceptionHandlingMode) {
		this.exceptionHandlingMode = exceptionHandlingMode;
	}
	public boolean isReturnResult() {
		return returnResult;
	}
	public void setReturnResult(boolean returnResult) {
		this.returnResult = returnResult;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Override
	public String toString() {
		return "Task [tenantId=" + tenantId + ", queueId=" + queueId
				+ ", taskId=" + taskId + ", serverId=" + serverId
				+ ", visibleTime=" + visibleTime + ", status=" + status
				+ ", groupingKey=" + groupingKey + ", virtualWorkerId="
				+ virtualWorkerId + ", version=" + version + ", updateTime="
				+ updateTime + ", callable=" + callable
				+ ", exceptionHandlingMode=" + exceptionHandlingMode
				+ ", returnResult=" + returnResult + ", result=" + result
				+ ", retryCount=" + retryCount + "]";
	}


}
