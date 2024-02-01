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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAccount;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;

public class AccountSelectSQL extends QuerySqlHandler {

	private static final String ACCOUNT_SELECT_SQL = "SELECT " +
													"TENANT_ID,ACCOUNT_ID,POL_NAME,PASSWORD,SALT,OID,LAST_LOGIN_ON,LOGIN_ERR_CNT,LOGIN_ERR_DATE,LAST_PASSWORD_CHANGE,CRE_USER,CRE_DATE,UP_USER,UP_DATE " +
													"FROM T_ACCOUNT ";

	public String createAccountSQL() {
		return ACCOUNT_SELECT_SQL + "WHERE TENANT_ID = ? AND ACCOUNT_ID = ? ";

	}
	public void setAccountParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String accountId) throws SQLException {
		int index = 1;
		ps.setInt(index++, tenantId);
		ps.setString(index++, accountId);
	}
	public String createAccountFromOidSQL() {
		return ACCOUNT_SELECT_SQL + "WHERE TENANT_ID = ? AND OID = ? ";

	}
	public void setAccountFromOidParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String oid) throws SQLException {
		int index = 1;
		ps.setInt(index++, tenantId);
		ps.setString(index++, oid);
	}

	public BuiltinAccount getAccount(ResultSet rs, RdbAdapter rdb) throws SQLException {
		if(!rs.next()) {
			return null;
		}
		BuiltinAccount ret = new BuiltinAccount();
		int index = 1;
		ret.setTenantId(rs.getInt(index++));
		ret.setAccountId(rs.getString(index++));
		ret.setPolicyName(rs.getString(index++));
		ret.setPassword(rs.getString(index++));
		ret.setSalt(rs.getString(index++));
		ret.setOid(rs.getString(index++));
		ret.setLastLoginOn(rs.getTimestamp(index++, rdb.rdbCalendar()));
		ret.setLoginErrorCnt(rs.getInt(index++));
		ret.setLoginErrorDate(rs.getTimestamp(index++, rdb.rdbCalendar()));
		ret.setLastPasswordChange(rs.getDate(index++, rdb.javaCalendar()));
		ret.setCreateUser(rs.getString(index++));
		ret.setCreateDate(rs.getTimestamp(index++, rdb.rdbCalendar()));
		ret.setUpdateUser(rs.getString(index++));
		ret.setUpdateDate(rs.getTimestamp(index++, rdb.rdbCalendar()));
		return ret;
	}

	public String createExistAccount() {
		return "SELECT COUNT(ID) FROM T_ACCOUNT WHERE TENANT_ID = ? AND ACCOUNT_ID = ? ";
	}

	public void setExistAccount(RdbAdapter rdb, PreparedStatement ps, int tenantId, String accountId) throws SQLException {
		int index = 1;
		ps.setInt(index++, tenantId);
		ps.setString(index++, accountId);
	}
}
