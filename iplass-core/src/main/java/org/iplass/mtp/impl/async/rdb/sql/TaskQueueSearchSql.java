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

package org.iplass.mtp.impl.async.rdb.sql;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.async.ExceptionHandlingMode;
import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.impl.async.rdb.CallableInput;
import org.iplass.mtp.impl.async.rdb.Task;
import org.iplass.mtp.impl.async.rdb.TaskSearchCondition;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.script.GroovyObjectInputStream;
import org.iplass.mtp.spi.ServiceRegistry;

public class TaskQueueSearchSql extends QuerySqlHandler {

	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);

	private void appendLoadSql(StringBuilder sb, int tenantId, int queueId, long taskId, boolean withBinary,
			String tableName, RdbAdapter rdb) {
		sb.append("SELECT "
				+ TaskQueueTable.TENANT_ID + ","
				+ TaskQueueTable.Q_ID + ","
				+ TaskQueueTable.TASK_ID + ","
				+ TaskQueueTable.VISIBLE_TIME + ","
				+ TaskQueueTable.STATUS + ","
				+ TaskQueueTable.GROUPING_KEY + ","
				+ TaskQueueTable.VIRTUAL_WORKER_ID + ","
				+ TaskQueueTable.VERSION + ","
				+ TaskQueueTable.UPDATE + ","
				+ TaskQueueTable.SERVER_ID + ","
				+ TaskQueueTable.RETRY_COUNT + ","
				+ TaskQueueTable.EXP_MODE + ","
				+ TaskQueueTable.RESULT_FLG);
		if (withBinary) {
			sb.append(","
					+ TaskQueueTable.CALLABLE + ","
					+ TaskQueueTable.RESULT);
		}
		sb.append(" FROM " + tableName
				+ " WHERE ");
		sb.append(TaskQueueTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + TaskQueueTable.Q_ID + "=").append(queueId);
		sb.append(" AND " + TaskQueueTable.TASK_ID + "=").append(taskId);
	}

	public String toLoadSql(int tenantId, int queueId, long taskId, boolean withBinary,
			boolean withHistory, boolean withLock, RdbAdapter rdb) {

		StringBuilder sb = new StringBuilder();
		appendLoadSql(sb, tenantId, queueId, taskId, withBinary, TaskQueueTable.TABLE_NAME, rdb);
		if (withHistory) {
			sb.append(" UNION ALL ");
			appendLoadSql(sb, tenantId, queueId, taskId, withBinary, TaskQueueTable.TABLE_NAME_HISTORY, rdb);
		}

		return withLock ? rdb.createRowLockSql(sb.toString()) : sb.toString();
	}

	public String toForPollSql(int queueId, int[] workerIds,
			long currentTimeMillis, String serverId, int maxRetryCount, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ TaskQueueTable.TENANT_ID + ","
				+ TaskQueueTable.Q_ID + ","
				+ TaskQueueTable.TASK_ID + ","
				+ TaskQueueTable.VISIBLE_TIME + ","
				+ TaskQueueTable.STATUS + ","
				+ TaskQueueTable.GROUPING_KEY + ","
				+ TaskQueueTable.VIRTUAL_WORKER_ID + ","
				+ TaskQueueTable.VERSION + ","
				+ TaskQueueTable.UPDATE + ","
				+ TaskQueueTable.SERVER_ID + ","
				+ TaskQueueTable.RETRY_COUNT + ","
				+ TaskQueueTable.EXP_MODE + ","
				+ TaskQueueTable.RESULT_FLG);
		sb.append(" FROM " + TaskQueueTable.TABLE_NAME
				+ " WHERE ");
		sb.append(TaskQueueTable.Q_ID + "=").append(queueId);
		sb.append(" AND " + TaskQueueTable.VISIBLE_TIME + "<=").append(currentTimeMillis);
		sb.append(" AND " + TaskQueueTable.STATUS + " IN('S','E')");
		sb.append(" AND " + TaskQueueTable.VIRTUAL_WORKER_ID + " IN(-1,");
		for (int i = 0; i < workerIds.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(workerIds[i]);
		}
		sb.append(")");
		if (serverId != null) {
			sb.append(" AND " + TaskQueueTable.SERVER_ID + "='").append(rdb.sanitize(serverId)).append("'");
		}
		sb.append(" AND " + TaskQueueTable.RETRY_COUNT + "<=" + maxRetryCount);
		sb.append(" ORDER BY " + TaskQueueTable.VISIBLE_TIME);
		return rdb.toLimitSql(sb.toString(), 100, 0);
	}

	private void appendSearchSql(StringBuilder sb, TaskSearchCondition cond, String tableName, RdbAdapter rdb) {

		sb.append("SELECT "
				+ TaskQueueTable.TENANT_ID + ","
				+ TaskQueueTable.Q_ID + ","
				+ TaskQueueTable.TASK_ID + ","
				+ TaskQueueTable.VISIBLE_TIME + ","
				+ TaskQueueTable.STATUS + ","
				+ TaskQueueTable.GROUPING_KEY + ","
				+ TaskQueueTable.VIRTUAL_WORKER_ID + ","
				+ TaskQueueTable.VERSION + ","
				+ TaskQueueTable.UPDATE + ","
				+ TaskQueueTable.SERVER_ID + ","
				+ TaskQueueTable.RETRY_COUNT + ","
				+ TaskQueueTable.EXP_MODE + ","
				+ TaskQueueTable.RESULT_FLG);
		sb.append(" FROM " + tableName);
		if (cond.hasCond()) {
			sb.append(" WHERE ");
			boolean needAnd = false;
			if (cond.getTenantId() != null) {
				sb.append(TaskQueueTable.TENANT_ID + "=").append(cond.getTenantId());
				needAnd = true;
			}
			if (cond.getQueueId() != null) {
				if (needAnd) {
					sb.append(" AND ");
				}
				sb.append(TaskQueueTable.Q_ID + "=").append(cond.getQueueId());
				needAnd = true;
			}
			if (cond.getTaskId() != null) {
				if (needAnd) {
					sb.append(" AND ");
				}
				sb.append(TaskQueueTable.TASK_ID + "=").append(cond.getTaskId());
				needAnd = true;
			}
			if (cond.getStatus() != null) {
				if (needAnd) {
					sb.append(" AND ");
				}
				sb.append(TaskQueueTable.STATUS + "='").append(TaskQueueInsertSql.toStatusStr(cond.getStatus())).append("'");
				needAnd = true;
			}
			if (cond.getGroupingKey() != null) {
				if (needAnd) {
					sb.append(" AND ");
				}
				sb.append(TaskQueueTable.GROUPING_KEY + "='").append(rdb.sanitize(cond.getGroupingKey())).append("'");
				needAnd = true;
			}
			if (cond.getRetryCount() != null) {
				if (needAnd) {
					sb.append(" AND ");
				}
				sb.append(TaskQueueTable.RETRY_COUNT + ">=").append(cond.getRetryCount());
				needAnd = true;
			}
			if (cond.getReturnResult() != null) {
				if (needAnd) {
					sb.append(" AND ");
				}
				sb.append(TaskQueueTable.RESULT_FLG + "='").append(TaskQueueInsertSql.toFlagStr(cond.getReturnResult().booleanValue())).append("'");
				needAnd = true;
			}
			if (cond.getUpdateDate() != null) {
				if (needAnd) {
					sb.append(" AND ");
				}
				sb.append(TaskQueueTable.UPDATE + "<=").append(rdb.toTimeStampExpression(cond.getUpdateDate()));
				needAnd = true;
			}
		}
	}

	public String toSearchSql(TaskSearchCondition cond, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		appendSearchSql(sb, cond, TaskQueueTable.TABLE_NAME, rdb);
		if (cond.isWithHistory()) {
			sb.append(" UNION ALL ");
			appendSearchSql(sb, cond, TaskQueueTable.TABLE_NAME_HISTORY, rdb);
		}

		if (cond.isWithHistory()) {
			sb.append(" ORDER BY " + TaskQueueTable.VISIBLE_TIME + " DESC");
		} else {
			sb.append(" ORDER BY " + TaskQueueTable.VISIBLE_TIME);
		}

		if (cond.getLimit() != null) {
			int offset = 0;
			if (cond.getOffset() != null) {
				offset = cond.getOffset();
			}
			return rdb.toLimitSql(sb.toString(), cond.getLimit(), offset);
		} else {
			return sb.toString();
		}
	}

	public String toCountPreExecutingSql(int tenantId, int queueId,
			String groupingKey, long taskId, String serverId, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNT(*)");
		sb.append(" FROM " + TaskQueueTable.TABLE_NAME
				+ " WHERE ");
		sb.append(TaskQueueTable.TENANT_ID + "=").append(tenantId);
		sb.append(" AND " + TaskQueueTable.Q_ID + "=").append(queueId);
		sb.append(" AND " + TaskQueueTable.GROUPING_KEY + "='").append(rdb.sanitize(groupingKey)).append("'");
		sb.append(" AND " + TaskQueueTable.TASK_ID + "<").append(taskId);
		sb.append(" AND " + TaskQueueTable.STATUS + " IN('S','E')");
		if (serverId != null) {
			sb.append(" AND " + TaskQueueTable.SERVER_ID + "='").append(rdb.sanitize(serverId)).append("'");
		}
		return sb.toString();
	}

	private TaskStatus toStatus(String taskName) {
		if (taskName == null || taskName.length() == 0) {
			return null;
		}
		if (taskName.equals("S"))  {
			return TaskStatus.SUBMITTED;
		}
		if (taskName.equals("E"))  {
			return TaskStatus.EXECUTING;
		}
		if (taskName.equals("A"))  {
			return TaskStatus.ABORTED;
		}
		if (taskName.equals("R"))  {
			return TaskStatus.RETURNED;
		}
		if (taskName.equals("C"))  {
			return TaskStatus.COMPLETED;
		}
		if (taskName.equals("U"))  {
			return TaskStatus.UNKNOWN;
		}

		return null;
	}

	private ExceptionHandlingMode toExpMode(String expMode) {
		if (expMode == null || expMode.length() == 0) {
			return null;
		}

		if (expMode.equals("A"))  {
			return ExceptionHandlingMode.ABORT;
		}
		if (expMode.equals("F"))  {
			return ExceptionHandlingMode.ABORT_LOG_FATAL;
		}
		if (expMode.equals("R"))  {
			return ExceptionHandlingMode.RESTART;
		}

		return null;
	}

	private boolean toFlag(String flg) {
		if (flg == null || flg.length() == 0) {
			return false;
		}
		if ("1".equals(flg)) {
			return true;
		}

		return false;
	}

	private Object deserial(final Blob lob, final TenantContext tc) {
		if (lob == null) {
			return null;
		}

		return ExecuteContext.executeAs(tc, new Executable<Object>() {
			@Override
			public Object execute() {
				try (InputStream is = lob.getBinaryStream();
						ObjectInputStream ois = new GroovyObjectInputStream(new BufferedInputStream(is))) {
					return ois.readObject();
				} catch (IOException | ClassNotFoundException | SQLException e) {
					throw new IllegalStateException(e);
				}
			}
		});
	}

	private Object deserial(final InputStream is, final TenantContext tc) {
		if (is == null) {
			return null;
		}

		try (InputStream tis = is) {
			return ExecuteContext.executeAs(tc, new Executable<Object>() {
				@Override
				public Object execute() {
					try (ObjectInputStream ois = new GroovyObjectInputStream(new BufferedInputStream(tis))) {
						return ois.readObject();
					} catch (IOException | ClassNotFoundException e) {
						throw new IllegalStateException(e);
					}
				}
			});
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public Task toTask(RdbAdapter rdb, ResultSet rs, boolean withBinary) throws SQLException {
		Task task = new Task();
		task.setTenantId(rs.getInt(TaskQueueTable.TENANT_ID));
		task.setQueueId(rs.getInt(TaskQueueTable.Q_ID));
		task.setTaskId(rs.getLong(TaskQueueTable.TASK_ID));
		task.setVisibleTime(rs.getLong(TaskQueueTable.VISIBLE_TIME));
		task.setStatus(toStatus(rs.getString(TaskQueueTable.STATUS)));
		task.setGroupingKey(rs.getString(TaskQueueTable.GROUPING_KEY));
		task.setVirtualWorkerId(rs.getInt(TaskQueueTable.VIRTUAL_WORKER_ID));
		task.setVersion(rs.getLong(TaskQueueTable.VERSION));
		task.setUpdateTime(rs.getTimestamp(TaskQueueTable.UPDATE, rdb.rdbCalendar()));
		task.setExceptionHandlingMode(toExpMode(rs.getString(TaskQueueTable.EXP_MODE)));
		task.setReturnResult(toFlag(rs.getString(TaskQueueTable.RESULT_FLG)));
		task.setServerId(rs.getString(TaskQueueTable.SERVER_ID));
		task.setRetryCount(rs.getInt(TaskQueueTable.RETRY_COUNT));

		if (withBinary) {
			TenantContext tc = tcs.getTenantContext(task.getTenantId());
			if (rdb.isSupportBlobType()) {
				task.setCallable((CallableInput<?>) deserial(rs.getBlob(TaskQueueTable.CALLABLE), tc));
				task.setResult(deserial(rs.getBlob(TaskQueueTable.RESULT), tc));
			} else {
				task.setCallable((CallableInput<?>) deserial(rs.getBinaryStream(TaskQueueTable.CALLABLE), tc));
				task.setResult(deserial(rs.getBinaryStream(TaskQueueTable.RESULT), tc));
			}
		}

		return task;
	}

}
