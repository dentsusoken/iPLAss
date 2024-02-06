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

package org.iplass.mtp.impl.async.rdb.sql;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.impl.async.rdb.Task;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class TaskQueueInsertSql extends UpdateSqlHandler {

	public String toInsertSql(RdbAdapter rdb) {
		return "INSERT INTO " + TaskQueueTable.TABLE_NAME
				+ "("
				+ TaskQueueTable.TENANT_ID + ","
				+ TaskQueueTable.Q_ID + ","
				+ TaskQueueTable.TASK_ID + ","
				+ TaskQueueTable.VISIBLE_TIME + ","
				+ TaskQueueTable.STATUS + ","
				+ TaskQueueTable.GROUPING_KEY + ","
				+ TaskQueueTable.VIRTUAL_WORKER_ID + ","
				+ TaskQueueTable.EXP_MODE + ","
				+ TaskQueueTable.RESULT_FLG + ","
				+ TaskQueueTable.VERSION + ","
				+ TaskQueueTable.UPDATE + ","
				+ TaskQueueTable.SERVER_ID + ","
				+ TaskQueueTable.RETRY_COUNT + ","
				+ TaskQueueTable.CALLABLE
				+")"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?," + rdb.systimestamp() + ",?,0,?)";
	}

	public void setStoreParameter(Task task, RdbAdapter rdb, PreparedStatement ps)
			throws SQLException {
		int num = 1;
		ps.setInt(num++, task.getTenantId());
		ps.setInt(num++, task.getQueueId());
		ps.setLong(num++, task.getTaskId());
		ps.setLong(num++, task.getVisibleTime());
		ps.setString(num++, toStatusStr(task.getStatus()));
		ps.setString(num++, task.getGroupingKey());
		ps.setInt(num++, task.getVirtualWorkerId());
		ps.setString(num++, toExpModeStr(task.getExceptionHandlingMode()));
		ps.setString(num++, toFlagStr(task.isReturnResult()));
		ps.setLong(num++, task.getVersion());
		ps.setString(num++, task.getServerId());
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos))) {
			oos.writeObject(task.getCallable());
			oos.flush();
		} catch (IOException e) {
			throw new SQLException(e);
		}
		ps.setBinaryStream(num++, new ByteArrayInputStream(bos.toByteArray()));
	}

	static String toFlagStr(boolean flag) {
		if (flag) {
			return "1";
		} else {
			return "0";
		}
	}

	static String toExpModeStr(ExceptionHandlingMode exceptionHandlingMode) {
		if (exceptionHandlingMode == null) {
			return null;
		}
		switch (exceptionHandlingMode) {
		case ABORT:
			return "A";
		case ABORT_LOG_FATAL:
			return "F";
		case RESTART:
			return "R";
		default:
			return null;
		}
	}

	static String toStatusStr(TaskStatus status) {
		if (status == null) {
			return null;
		}
		switch (status) {
		case ABORTED:
			return "A";
		case COMPLETED:
			return "C";
		case EXECUTING:
			return "E";
		case SUBMITTED:
			return "S";
		case UNKNOWN:
			return "U";
		case RETURNED:
			return "R";
		default:
			return null;
		}
	}

	public String toCopySql(RdbAdapter rdb) {
		return "INSERT INTO " + TaskQueueTable.TABLE_NAME_HISTORY
				+ "("
				+ TaskQueueTable.TENANT_ID + ","
				+ TaskQueueTable.Q_ID + ","
				+ TaskQueueTable.TASK_ID + ","
				+ TaskQueueTable.VISIBLE_TIME + ","
				+ TaskQueueTable.STATUS + ","
				+ TaskQueueTable.GROUPING_KEY + ","
				+ TaskQueueTable.VIRTUAL_WORKER_ID + ","
				+ TaskQueueTable.EXP_MODE + ","
				+ TaskQueueTable.RESULT_FLG + ","
				+ TaskQueueTable.VERSION + ","
				+ TaskQueueTable.UPDATE + ","
				+ TaskQueueTable.SERVER_ID + ","
				+ TaskQueueTable.RETRY_COUNT + ","
				+ TaskQueueTable.CALLABLE +","
				+ TaskQueueTable.RESULT
				+")"
				+ " SELECT "
				+ TaskQueueTable.TENANT_ID + ","
				+ TaskQueueTable.Q_ID + ","
				+ TaskQueueTable.TASK_ID + ","
				+ TaskQueueTable.VISIBLE_TIME + ","
				+ "?,"
				+ TaskQueueTable.GROUPING_KEY + ","
				+ TaskQueueTable.VIRTUAL_WORKER_ID + ","
				+ TaskQueueTable.EXP_MODE + ","
				+ TaskQueueTable.RESULT_FLG + ","
				+ TaskQueueTable.VERSION + "+1,"
				+ rdb.systimestamp() + ","
				+ TaskQueueTable.SERVER_ID + ","
				+ TaskQueueTable.RETRY_COUNT + ","
				+ TaskQueueTable.CALLABLE +","
				+ TaskQueueTable.RESULT
				+ " FROM " + TaskQueueTable.TABLE_NAME
				+ " WHERE "
				+ TaskQueueTable.TENANT_ID + "=?"
				+ " AND " + TaskQueueTable.Q_ID + "=?"
				+ " AND " + TaskQueueTable.TASK_ID + "=?"
				+ " AND " + TaskQueueTable.VERSION + "=?";
	}

	public void setStoreParameterForCopy(Task task, TaskStatus toStatus,
			RdbAdapter rdb, PreparedStatement ps) throws SQLException {
		int num = 1;
		ps.setString(num++, toStatusStr(toStatus));
		
		ps.setInt(num++, task.getTenantId());
		ps.setInt(num++, task.getQueueId());
		ps.setLong(num++, task.getTaskId());
		ps.setLong(num++, task.getVersion());
		
	}
	
}
