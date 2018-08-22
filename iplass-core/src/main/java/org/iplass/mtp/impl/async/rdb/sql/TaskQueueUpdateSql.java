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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.impl.async.rdb.Task;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class TaskQueueUpdateSql extends UpdateSqlHandler {

	public String toUpdateSql(RdbAdapter rdb) {
		return "UPDATE " + TaskQueueTable.TABLE_NAME
				+ " SET "
				+ TaskQueueTable.VISIBLE_TIME + "=?,"
				+ TaskQueueTable.STATUS + "=?,"
				+ TaskQueueTable.RETRY_COUNT + "=?,"
				+ TaskQueueTable.VERSION + "=" + TaskQueueTable.VERSION + "+1,"
				+ TaskQueueTable.UPDATE + "=" + rdb.systimestamp() + ","
				+ TaskQueueTable.RESULT + "=?"
				+" WHERE "
				+ TaskQueueTable.TENANT_ID + "=?"
				+ " AND " + TaskQueueTable.Q_ID + "=?"
				+ " AND " + TaskQueueTable.TASK_ID + "=?"
				+ " AND " + TaskQueueTable.VERSION + "=?";
	}

	public void setStoreParameter(Task task, RdbAdapter rdb, PreparedStatement ps) throws SQLException {
		int num = 1;
		ps.setLong(num++, task.getVisibleTime());
		ps.setString(num++, TaskQueueInsertSql.toStatusStr(task.getStatus()));
		ps.setInt(num++, task.getRetryCount());
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(bos))) {
			oos.writeObject(task.getResult());
			oos.flush();
		} catch (IOException e) {
			throw new SQLException(e);
		}
		ps.setBinaryStream(num++, new ByteArrayInputStream(bos.toByteArray()));
		
		ps.setInt(num++, task.getTenantId());
		ps.setInt(num++, task.getQueueId());
		ps.setLong(num++, task.getTaskId());
		ps.setLong(num++, task.getVersion());
	}

}
