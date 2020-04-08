/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.impl.auth.authenticate.builtin.Password;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class PasswordHistorySelectSQL extends QuerySqlHandler {
	
	public String createSelectSQL() {
		return "SELECT " +
				"TENANT_ID,ACCOUNT_ID,PASSWORD,SALT,UP_DATE " +
				"FROM T_PASS_HI " +
				"WHERE TENANT_ID = ? AND ACCOUNT_ID = ? ORDER BY UP_DATE DESC";
	}
	public void setSelectParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String accountId) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, accountId);
	}
	public Password toPassword(ResultSet rs, RdbAdapter rdb) throws SQLException {
		return new Password(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getTimestamp(5, rdb.rdbCalendar()));
	}

}
