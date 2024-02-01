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

package org.iplass.adminconsole.shared.tools.dto.queueexplorer;

import java.io.Serializable;

import org.iplass.mtp.async.TaskStatus;

public class CancelResult implements Serializable {

	private static final long serialVersionUID = -7675341119971533230L;

	private long taskId;
	private boolean canceled;
	private TaskStatus beforeStatus;
	private TaskStatus resultStatus;

	public CancelResult() {
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setBeforeStatus(TaskStatus beforeStatus) {
		this.beforeStatus = beforeStatus;
	}

	public TaskStatus getBeforeStatus() {
		return beforeStatus;
	}

	public void setResultStatus(TaskStatus status) {
		this.resultStatus = status;
	}

	public TaskStatus getResultStatus() {
		return resultStatus;
	}

}
