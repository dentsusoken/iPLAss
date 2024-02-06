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

package org.iplass.mtp.impl.lob.sql;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class LobStoreSearchSql extends QuerySqlHandler {

	public String toSql(int tenantId, long lobId, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ LobStoreTable.TENANT_ID
				+ "," + LobStoreTable.LOB_DATA_ID
				+ "," + LobStoreTable.REF_COUNT
				+ "," + LobStoreTable.B_DATA
				+ " FROM "
				+ LobStoreTable.TABLE_NAME
				+ " WHERE "
				+ LobStoreTable.TENANT_ID
				+ "=");
		sb.append(tenantId);
		sb.append(" AND " + LobStoreTable.LOB_DATA_ID + "=");
		sb.append(lobId);
		return sb.toString();
	}

	public Blob getBlob(ResultSet rs, RdbAdapter rdb) throws SQLException {
		if (rs.next()) {
			return rs.getBlob(LobStoreTable.B_DATA);
		} else {
			return null;
			//TODO 例外？
			//throw new EntityRuntimeException("no resultset...");
		}
	}

	public InputStream getBinaryStream(ResultSet rs, RdbAdapter rdb) throws SQLException {
		if (rs.next()) {
			return rs.getBinaryStream(LobStoreTable.B_DATA);
		} else {
			return null;
			//TODO 例外？
			//throw new EntityRuntimeException("no resultset...");
		}
	}

	public String toSqlForClean(int tenantId, int day, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ LobStoreTable.TENANT_ID
				+ "," + LobStoreTable.LOB_DATA_ID
				//+ "," + LobStoreTable.REF_COUNT
				//+ "," + LobStoreTable.B_DATA
				+ " FROM "
				+ LobStoreTable.TABLE_NAME
				+ " WHERE "
				+ LobStoreTable.TENANT_ID
				+ "=");
		sb.append(tenantId);
		sb.append(" AND " + LobStoreTable.REF_COUNT + " < 1");
		if (day < 0) {
			sb.append(" AND " + LobStoreTable.CRE_DATE + " < ").append(rdb.addDate(rdb.systimestamp(), day));
		}
		return sb.toString();
	}

	public String toSqlForSizeUpdate(int tenantId, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT "
				+ LobStoreTable.TENANT_ID
				+ "," + LobStoreTable.LOB_DATA_ID
				//+ "," + LobStoreTable.REF_COUNT
				//+ "," + LobStoreTable.B_DATA
				+ " FROM "
				+ LobStoreTable.TABLE_NAME
				+ " WHERE "
				+ LobStoreTable.TENANT_ID
				+ "=");
		sb.append(tenantId);
		sb.append(" AND " + LobStoreTable.LOB_SIZE + " is null ");

		return sb.toString();
	}
}
