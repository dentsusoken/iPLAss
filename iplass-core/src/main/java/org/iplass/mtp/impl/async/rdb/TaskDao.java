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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.async.TaskStatus;
import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.impl.async.rdb.sql.TaskQueueDeleteSql;
import org.iplass.mtp.impl.async.rdb.sql.TaskQueueInsertSql;
import org.iplass.mtp.impl.async.rdb.sql.TaskQueueSearchSql;
import org.iplass.mtp.impl.async.rdb.sql.TaskQueueUpdateSql;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;

public class TaskDao {
	private RdbAdapter rdb;
	private TaskQueueDeleteSql deleteSql;
	private TaskQueueInsertSql insertSql;
	private TaskQueueSearchSql searchSql;
	private TaskQueueUpdateSql updateSql;

	public TaskDao(RdbAdapter rdb) {
		this.rdb = rdb;
		deleteSql = rdb.getUpdateSqlCreator(TaskQueueDeleteSql.class);
		insertSql = rdb.getUpdateSqlCreator(TaskQueueInsertSql.class);
		searchSql = rdb.getQuerySqlCreator(TaskQueueSearchSql.class);
		updateSql = rdb.getUpdateSqlCreator(TaskQueueUpdateSql.class);
	}

	public Task load(final int tenantId, final int queueId, final long taskId, final boolean withBinary, final boolean withHistory, final boolean withLock) {
		SqlExecuter<Task> exec = new SqlExecuter<Task>() {
			@Override
			public Task logic() throws SQLException {
				String sql = searchSql.toLoadSql(
						tenantId, queueId, taskId, withBinary, withHistory, withLock, rdb);

				ResultSet rs = getStatement().executeQuery(sql);
				try {
					if (rs.next()) {
						Task task = searchSql.toTask(rdb, rs, withBinary);
						return task;
					}
				} finally {
					rs.close();
				}
				return null;
			}
		};

		return exec.execute(rdb, true);
	}

	public void insert(final Task task) {

		SqlExecuter<Void> exec = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				String sql = insertSql.toInsertSql(rdb);
				PreparedStatement ps = getPreparedStatement(sql);
				insertSql.setStoreParameter(task, rdb, ps);
				ps.executeUpdate();
				return null;
			}
		};

		exec.execute(rdb, true);
	}

	public List<Task> searchForPoll(final int queueId, final int[] workerIds,
			final long currentTimeMillis, final String serverId, final int maxRetryCount) {
		//no binary, no history, with limit
		SqlExecuter<List<Task>> exec = new SqlExecuter<List<Task>>() {
			@Override
			public List<Task> logic() throws SQLException {
				String sql = searchSql.toForPollSql(
						queueId, workerIds, currentTimeMillis, serverId, maxRetryCount, rdb);

				ArrayList<Task> list = new ArrayList<>();
				ResultSet rs = getStatement().executeQuery(sql);
				try {
					while (rs.next()) {
						list.add(searchSql.toTask(rdb, rs, false));
					}
				} finally {
					rs.close();
				}
				return list;
			}
		};

		return exec.execute(rdb, true);
	}

	public int countPreExecuting(final int tenantId, final int queueId,
			final String groupingKey, final long taskId, final String serverId) {
		//taskIdより前のtaskをカウント
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				String sql = searchSql.toCountPreExecutingSql(
						tenantId, queueId, groupingKey, taskId, serverId, rdb);

				ResultSet rs = getStatement().executeQuery(sql);
				try {
					rs.next();
					return rs.getInt(1);
				} finally {
					rs.close();
				}
			}
		};

		return exec.execute(rdb, true);
	}

	public void update(final Task t) {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				String sql = updateSql.toUpdateSql(rdb);
				PreparedStatement ps = getPreparedStatement(sql);
				updateSql.setStoreParameter(t, rdb, ps);
				int count = ps.executeUpdate();
				if (count < 1) {
					throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
				}
				return null;
			}
		};

		exec.execute(rdb, true);
	}

	public void moveToHistory(final Task task, final TaskStatus toStatus) {
		//copy
		//delete with version check
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				String copySql = insertSql.toCopySql(rdb);
				PreparedStatement ps = getPreparedStatement(copySql);
				insertSql.setStoreParameterForCopy(task, toStatus, rdb, ps);
				int count = ps.executeUpdate();
				if (count < 1) {
					throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
				}

				ps.close();

				String delSql = deleteSql.toDeleteSql(rdb);
				ps = getPreparedStatement(delSql);
				deleteSql.setDelParameter(task, rdb, ps);
				count = ps.executeUpdate();
				if (count < 1) {
					throw new EntityConcurrentUpdateException(resourceString("impl.datastore.schemalessrdb.strategy.SLREntityStoreStrategy.alreadyOperated"));
				}
				return null;
			}
		};

		exec.execute(rdb, true);
	}

	public List<Task> search(final TaskSearchCondition cond) {
		SqlExecuter<List<Task>> exec = new SqlExecuter<List<Task>>() {
			@Override
			public List<Task> logic() throws SQLException {
				String sql = searchSql.toSearchSql(cond, rdb);

				ArrayList<Task> list = new ArrayList<>();
				ResultSet rs = getStatement().executeQuery(sql);
				try {
					while (rs.next()) {
						list.add(searchSql.toTask(rdb, rs, false));
					}
				} finally {
					rs.close();
				}
				return list;
			}
		};

		return exec.execute(rdb, true);
	}

	public void deleteHistoryByDate(final Timestamp date, final boolean isDirectTenant) {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				String delSql = deleteSql.toDeleteByDateSql(rdb, isDirectTenant);
				PreparedStatement ps = getPreparedStatement(delSql);
				deleteSql.setDelParameterForDate(date, rdb, ps, isDirectTenant);
				ps.executeUpdate();
				return null;
			}
		};

		exec.execute(rdb, true);
	}

	public void delete(final Task task) {
		SqlExecuter<Void> exec = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				String delSql = deleteSql.toDeleteSql(rdb);
				PreparedStatement ps = getPreparedStatement(delSql);
				deleteSql.setDelParameter(task, rdb, ps);
				ps.executeUpdate();
				return null;
			}
		};

		exec.execute(rdb, true);
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
