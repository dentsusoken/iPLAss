/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.metadata.rdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class UpdateConfigSQL extends UpdateSqlHandler {

	private static final String UPDATE_CONFIG_SQL;

	static {
		UPDATE_CONFIG_SQL = "UPDATE " + ObjMetaTable.TABLE_NAME
				+ " SET " + ObjMetaTable.SHARABLE + "=?"
				+ " ," + ObjMetaTable.OVERWRITABLE + "=?"
				+ " ," + ObjMetaTable.UP_USER + "=?"
				+ " ," + ObjMetaTable.UP_DATE + "=systimestamp"
				+ " WHERE " + ObjMetaTable.TENANT_ID + "=?"
				+ " AND " + ObjMetaTable.OBJ_DEF_ID + "=?"
				+ " AND " + ObjMetaTable.STATUS + "= 'V'";
	}

	public String createUpdateSQL(RdbAdapter rdb) {
		return UPDATE_CONFIG_SQL.replaceAll("systimestamp", rdb.systimestamp());
	}

	public void setUpdateParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String id, boolean share, boolean dataShare, boolean permissionShare, boolean overwrite) throws SQLException {
		int num = 1;
		String clientId = ExecuteContext.getCurrentContext().getClientId();
		String shareStr = toCharShare(share, dataShare, permissionShare);
		String overwriteStr = toCharOverwrite(overwrite);
		ps.setString(num++, shareStr);
		ps.setString(num++, overwriteStr);
		ps.setString(num++, clientId);
		ps.setInt(num++, tenantId);
		ps.setString(num++, id);
	}

	static String toCharOverwrite(boolean flag) {
		if (flag) {
			return "1";
		} else {
			return "0";
		}
	}

	static boolean toBooleanOverwrite(String flagStr) {
		if ("1".equals(flagStr)) {
			return true;
		} else {
			return false;
		}
	}

	//以下のイメージ
	//SHARABLE（bit表現）	OVERWRITABLE
	//PDM
	//eae
	//rtt
	//maa
	//
	//000 	0			NG
	//001 	1			OK
	//011 	3			NG
	//101 	5			NG
	//111 	7			NG

	static String toCharShare(boolean sharable, boolean dataSharable, boolean permissionSharable) {
		int val = 0;
		if (sharable) {
			val += 1;
		}
		if (dataSharable) {
			val += 2;
		}
		if (permissionSharable) {
			val += 4;
		}

		return Integer.toString(val);
	}

	/**
	 *
	 * [0] = sharable
	 * [1] = dataSharable
	 * [2] = permissionSharable
	 *
	 * @param flagStr
	 * @return
	 */
	static boolean[] toBooleanShare(String flagStr) {
		if (flagStr == null || flagStr.trim().length() == 0) {
			return new boolean[]{false, false, false};
		}
		int val = Integer.parseInt(flagStr);
		boolean[] ret = new boolean[3];
		if ((val & 1) == 1) {
			ret[0] = true;
		} else {
			ret[0] = false;
		}
		if ((val & 2) == 2) {
			ret[1] = true;
		} else {
			ret[1] = false;
		}
		if ((val & 4) == 4) {
			ret[2] = true;
		} else {
			ret[2] = false;
		}
		return ret;
	}

}
