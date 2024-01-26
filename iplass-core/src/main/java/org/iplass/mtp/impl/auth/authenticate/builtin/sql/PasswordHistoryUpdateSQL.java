/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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
import java.sql.SQLException;
import java.sql.Timestamp;

import org.iplass.mtp.impl.auth.authenticate.builtin.Password;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class PasswordHistoryUpdateSQL extends UpdateSqlHandler {
	public String createInsertSQL(RdbAdapter rdb) {
		return "INSERT INTO T_PASS_HI(TENANT_ID,ACCOUNT_ID,PASSWORD,SALT,UP_DATE) " +
				"VALUES (?,?,?,?,?)";
 	}
	public void setInsertParameter(RdbAdapter rdb, PreparedStatement ps, Password pass) throws SQLException {
		int num = 1;
		// TENANT_ID
		ps.setInt(num++, pass.getTenantId());
		// ACCOUNT_ID
		ps.setString(num++, pass.getUid());
		// PASSWORD
		ps.setString(num++, pass.getConvertedPassword());
		// SALT
		ps.setString(num++, pass.getSalt());
		//UP_DATE
		ps.setTimestamp(num++, pass.getUpdateDate(), rdb.rdbCalendar());
	}

	public String createDeleteSQL(RdbAdapter rdb) {
		return "DELETE FROM T_PASS_HI WHERE TENANT_ID=? AND ACCOUNT_ID=?";
	}
	public void setDeleteParameter(RdbAdapter rdb, PreparedStatement ps,
			int tenantId, String accountId) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, accountId);
	}

	public String createDeleteByDateSQL(RdbAdapter rdb) {
		return "DELETE FROM T_PASS_HI WHERE TENANT_ID=? AND ACCOUNT_ID=? AND UP_DATE<=?";
	}
	public void setDeleteByDateParameter(RdbAdapter rdb, PreparedStatement ps,
			int tenantId, String accountId, Timestamp date) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, accountId);
		ps.setTimestamp(3, date, rdb.rdbCalendar());
	}
	public String createUpdateAccountIdSQL(RdbAdapter rdb) {
		return "UPDATE T_PASS_HI SET ACCOUNT_ID=? WHERE TENANT_ID=? AND ACCOUNT_ID=?";
	}
	public void setUpdateAccountIdSQL(RdbAdapter rdb, PreparedStatement ps,
			int tenantId, String oldAccountId, String newAccountId) throws SQLException {
		ps.setString(1, newAccountId);
		ps.setInt(2, tenantId);
		ps.setString(3, oldAccountId);
	}
}
