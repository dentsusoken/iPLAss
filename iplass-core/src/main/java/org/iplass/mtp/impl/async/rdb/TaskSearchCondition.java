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

package org.iplass.mtp.impl.async.rdb;

import java.sql.Timestamp;

import org.iplass.mtp.async.TaskStatus;

public class TaskSearchCondition {
	private Integer tenantId;
	private Integer queueId;
	private Long taskId;
	private TaskStatus status;
	private boolean withHistory;
	
	private String groupingKey;
	private Integer retryCount;//以上をひっかける
	private Boolean returnResult;
	
	private Timestamp updateDate;//以前をひっかける
	
	private Integer limit;
	private Integer offset;
	
	public boolean hasCond() {
		if (tenantId != null
				|| queueId != null
				|| taskId != null
				|| status != null
				|| groupingKey != null
				|| retryCount != null
				|| returnResult != null
				|| updateDate != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public Timestamp getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getGroupingKey() {
		return groupingKey;
	}

	public void setGroupingKey(String groupingKey) {
		this.groupingKey = groupingKey;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Boolean getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(Boolean returnResult) {
		this.returnResult = returnResult;
	}

	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getTenantId() {
		return tenantId;
	}
	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}
	public Integer getQueueId() {
		return queueId;
	}
	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public boolean isWithHistory() {
		return withHistory;
	}
	public void setWithHistory(boolean withHistory) {
		this.withHistory = withHistory;
	}
}
