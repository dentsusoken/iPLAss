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

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAccount;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class AccountControlSQL  extends UpdateSqlHandler {
	public String createLoginStatUpdateSQL() {
		return "UPDATE T_ACCOUNT SET LOGIN_ERR_CNT=?,LOGIN_ERR_DATE=?,LAST_LOGIN_ON=? WHERE TENANT_ID = ? AND ACCOUNT_ID = ?";
	}

	public void setLoginStatUpdateParameter(RdbAdapter rdb, PreparedStatement ps, BuiltinAccount account) throws SQLException {
		ps.setInt(1, account.getLoginErrorCnt());
		ps.setTimestamp(2, account.getLoginErrorDate());
		ps.setTimestamp(3, account.getLastLoginOn());
		ps.setInt(4, account.getTenantId());
		ps.setString(5, account.getAccountId());
	}

	public String createRegistSQL(RdbAdapter rdb) {
		return "INSERT INTO T_ACCOUNT(TENANT_ID,ACCOUNT_ID,POL_NAME,PASSWORD,SALT,OID,LAST_LOGIN_ON,LOGIN_ERR_CNT,LOGIN_ERR_DATE,CRE_USER,CRE_DATE,UP_USER,UP_DATE,LAST_PASSWORD_CHANGE) " +
				"VALUES " +
				"(?," + /* TENANT_ID  */
				"?," + /* ACCOUNT_ID */
				"?," + /* POL_NAME */
				"?," + /* PASSWORD */
				"?," + /* SALT */
				"?," + /* OID */
				"?," + /* LAST_LOGIN_ON */
				"?," + /* LOGIN_ERR_CNT */
				"?," + /* LOGIN_ERR_DATE */
				"?," + /* CRE_USER */
				rdb.systimestamp() + "," + /* CRE_DATE */
				"?," + /* UP_USER */
				rdb.systimestamp() + ", " + /* UP_DATE */
				"?)"; /* LAST_PASSWORD_CHANGE */
 	}

	public void setRegistParameter(RdbAdapter rdb, PreparedStatement ps, BuiltinAccount account, String user) throws SQLException {
		int num = 1;
		// TENANT_ID
		ps.setInt(num++, account.getTenantId());
		// ACCOUNT_ID
		ps.setString(num++, account.getAccountId());
		// POL_NAME
		ps.setString(num++, account.getPolicyName());
		// PASSWORD
		ps.setString(num++, account.getPassword());
		// SALT
		ps.setString(num++, account.getSalt());
		// OID
		ps.setString(num++, account.getOid());
		// LAST_LOGIN_ON
		ps.setTimestamp(num++, account.getLastLoginOn());
		// LOGIN_ERR_CNT
		ps.setInt(num++, account.getLoginErrorCnt());
		//LOGIN_ERR_DATE
		ps.setTimestamp(num++, account.getLoginErrorDate());
		// CRE_USER
		ps.setString(num++, user);
		// UP_USER
		ps.setString(num++, user);
		// LAST_PASSWORD_CHANGE
		ps.setDate(num++, account.getLastPasswordChange());
	}

	public String createResetLoginErrorCntSQL(RdbAdapter rdb) {
		return "UPDATE T_ACCOUNT SET LOGIN_ERR_CNT = 0 WHERE TENANT_ID = ? AND ACCOUNT_ID = ?";
	}
	public void setResetLoginErrorCntParameter(RdbAdapter rdb, PreparedStatement ps, BuiltinAccount account) throws SQLException {
		int num = 1;
		ps.setInt(num++, account.getTenantId());
		ps.setString(num++, account.getAccountId());
	}

	public String createUpdateSQL(RdbAdapter rdb) {
		return "UPDATE T_ACCOUNT " +
				"SET " +
				"ACCOUNT_ID=?, " +
				"POL_NAME=?," +
				"LOGIN_ERR_CNT=?, " +
				"LOGIN_ERR_DATE=?," +
				"UP_USER=?, UP_DATE=" + rdb.systimestamp() +
				",LAST_PASSWORD_CHANGE=?" +
				" WHERE TENANT_ID=? AND OID=? AND UP_DATE=?";
	}

	public void setUpdateParameter(RdbAdapter rdb, PreparedStatement ps, BuiltinAccount account, String user) throws SQLException {
		int num = 1;
		ps.setString(num++, account.getAccountId());
		ps.setString(num++, account.getPolicyName());
		ps.setInt(num++, account.getLoginErrorCnt());
		ps.setTimestamp(num++, account.getLoginErrorDate());
		ps.setString(num++, user);
		ps.setDate(num++, account.getLastPasswordChange());
		ps.setInt(num++, account.getTenantId());
		ps.setString(num++, account.getOid());
		ps.setTimestamp(num++, account.getUpdateDate());
	}

	public String createUpdatePasswordSQL(RdbAdapter rdb) {
		return "UPDATE T_ACCOUNT SET PASSWORD=?,SALT=?,LOGIN_ERR_CNT=0,LOGIN_ERR_DATE=null,LAST_PASSWORD_CHANGE=?,UP_USER=?,UP_DATE=" + rdb.systimestamp()  +
				" WHERE TENANT_ID=? AND ACCOUNT_ID=?" ;
	}

	public void setUpdatePasswordParameter(RdbAdapter rdb, PreparedStatement ps,int tenantId, String accountId, String newPassword, String salt, Date lastPasswordChange, String user) throws SQLException {
		int num = 1;
		ps.setString(num++, newPassword);
		ps.setString(num++, salt);
		ps.setDate(num++, lastPasswordChange);
		ps.setString(num++, user);
		ps.setInt(num++, tenantId);
		ps.setString(num++, accountId);
	}

	public String createDeleteSQL(RdbAdapter rdb) {
		return "DELETE FROM T_ACCOUNT WHERE TENANT_ID=? AND ACCOUNT_ID=?";
	}

	public void setDeleteParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String accountId) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, accountId);
	}

}
