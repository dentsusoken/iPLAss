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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.iplass.mtp.impl.async.rdb.Task;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class TaskQueueDeleteSql extends UpdateSqlHandler {

	public String toDeleteSql(RdbAdapter rdb) {
		return "DELETE FROM "
				+ TaskQueueTable.TABLE_NAME
				+ " WHERE "
				+ TaskQueueTable.TENANT_ID + "=?"
				+ " AND " + TaskQueueTable.Q_ID + "=?"
				+ " AND " + TaskQueueTable.TASK_ID + "=?"
				+ " AND " + TaskQueueTable.VERSION + "=?";
	}

	public void setDelParameter(Task task, RdbAdapter rdb,
			PreparedStatement ps) throws SQLException {
		int num = 1;
		ps.setInt(num++, task.getTenantId());
		ps.setInt(num++, task.getQueueId());
		ps.setLong(num++, task.getTaskId());
		ps.setLong(num++, task.getVersion());
	}

	public String toDeleteByDateSql(RdbAdapter rdb, boolean isDirectTenant) {
		String sql = "DELETE FROM "
				+ TaskQueueTable.TABLE_NAME_HISTORY
				+ " WHERE "
				+ TaskQueueTable.UPDATE + "<?";
		if (isDirectTenant) {
			sql = sql + " AND " + TaskQueueTable.TENANT_ID + "=?";
		}
		return sql;
	}

	public void setDelParameterForDate(Timestamp date,
			RdbAdapter rdb, PreparedStatement ps, boolean isDirectTenant) throws SQLException {

		ps.setTimestamp(1, date, rdb.rdbCalendar());

		if (isDirectTenant) {
			int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
			ps.setInt(2, tenantId);
		}
	}

}
