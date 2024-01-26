/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tenant.rdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;
import org.iplass.mtp.tenant.Tenant;

public class TenantControlSQL extends UpdateSqlHandler {
	/** IDは自動采番 */
	public static final String COLUMNS = "NAME, DESCRIPTION, " +
//			"HOST_NAME, " +
			"URL, " +
			"YUKO_DATE_FROM, YUKO_DATE_TO, " +
			"CRE_USER, CRE_DATE, UP_USER, UP_DATE";

	public String createRegistSQL(RdbAdapter rdb) {
		return "INSERT INTO T_TENANT (" + COLUMNS + ") " +
				"VALUES " +
				"(?, " + /* NAME */
				"?, " + /* DESCRIPTION */
//				"?, " + /* HOST_NAME */
				"?, " + /* URL */
				"?, " + /* YUKO_DATE_FROM */
				"?, " + /* YUKO_DATE_TO */
				"?, " + /* CRE_USER */
				rdb.systimestamp() + ", " + /* CRE_DATE */
				"?, " + /* UP_USER */
				rdb.systimestamp() + ")"; /* UP_DATE */
	}

	public void setRegistParameter(RdbAdapter rdb, PreparedStatement ps, Tenant tenant, String createUser) throws SQLException {
		setParameter(ps, tenant, createUser, true);
	}

	private int setParameter(PreparedStatement ps, Tenant tenant, String user, boolean isRegist) throws SQLException {
		int num = 1;
		// NAME
		ps.setString(num++, tenant.getName());
		// DESCRIPTION
		ps.setString(num++, tenant.getDescription());
		if(isRegist) {
//			// HOST_NAME
//			ps.setString(num++, tenant.getHostName());
			// URL
			ps.setString(num++, tenant.getUrl());
		}
		// YUKO_DATE_FROM
		ps.setDate(num++, tenant.getFrom());
		// YUKO_DATE_TO
		ps.setDate(num++, tenant.getTo());

		if(isRegist) {
			// CRE_USER
			ps.setString(num++, user);
//			// CRE_DATE
//			ps.setTimestamp(num++, ts);
		}
		// UP_USER
		ps.setString(num++, user);
//		// UP_DATE
//		ps.setTimestamp(num++, ts);
		return num;
	}

	public String createUpdateSQL(RdbAdapter rdb, boolean forceUpdate) {

		String sql = "UPDATE T_TENANT SET " +
				"NAME = ?, DESCRIPTION = ?, " +
				"YUKO_DATE_FROM = ?, YUKO_DATE_TO = ?, " +
				"UP_USER = ?, UP_DATE = " + rdb.systimestamp() +
				" WHERE ID = ?";

		if (!forceUpdate) {
			 sql = sql + " AND UP_DATE = ?";
		}

		return sql;
	}

	public void setUpdateParameter(RdbAdapter rdb, PreparedStatement ps, Tenant tenant, String updateUser, boolean forceUpdate) throws SQLException {
		int num = setParameter(ps, tenant, updateUser, false);
		// 条件の追加
		// TenantId
		ps.setInt(num++, tenant.getId());

		if (!forceUpdate) {
			// 更新日付
			ps.setTimestamp(num++, tenant.getUpdateDate(), rdb.rdbCalendar());
		}
	}
}
