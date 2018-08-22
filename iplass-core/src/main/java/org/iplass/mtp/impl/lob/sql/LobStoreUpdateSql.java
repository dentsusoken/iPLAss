/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class LobStoreUpdateSql extends UpdateSqlHandler {

	public String toPrepareSqlForLobUpdate(boolean manageLobSizeOnRdb, RdbAdapter rdbAdaptor) {

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + LobStoreTable.TABLE_NAME)
		.append(" SET " + LobStoreTable.B_DATA + "=?");

		if (manageLobSizeOnRdb) {
			sql.append(" ," + LobStoreTable.LOB_SIZE + "=?");
		}
		sql.append(" WHERE " + LobStoreTable.TENANT_ID + "=? ")
		.append(" AND " + LobStoreTable.LOB_DATA_ID + "=?");

		return sql.toString();
	}

	public String toPrepareSqlForLobSizeUpdate(RdbAdapter rdbAdaptor) {

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + LobStoreTable.TABLE_NAME)
		.append(" SET " + LobStoreTable.LOB_SIZE + "=?")
		.append(" WHERE " + LobStoreTable.TENANT_ID + "=? ")
		.append(" AND " + LobStoreTable.LOB_DATA_ID + "=?");

		return sql.toString();
	}

	public String toPrepareSqlForRefCountUpdate(RdbAdapter rdbAdaptor) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + LobStoreTable.TABLE_NAME + " SET " + LobStoreTable.REF_COUNT + "=" + LobStoreTable.REF_COUNT + "+?");
		sql.append(" WHERE " + LobStoreTable.TENANT_ID + "=? AND ");
		sql.append(LobStoreTable.LOB_DATA_ID + "=?");

		return sql.toString();
	}

	public String toSqlForRefCountUpdateByList(RdbAdapter rdbAdaptor, int tenantId, List<String> lobDataIdList, int count) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE " + LobStoreTable.TABLE_NAME + " SET " + LobStoreTable.REF_COUNT + "=" + LobStoreTable.REF_COUNT + "+");
		sql.append(count);
		sql.append(" WHERE " + LobStoreTable.TENANT_ID + "=");
		sql.append(tenantId);
		sql.append(" AND ");
		sql.append(LobStoreTable.LOB_DATA_ID + " in ");
		for (int i = 0; i < lobDataIdList.size(); i++) {
			if (i != 0) {
				sql.append(",");
			}
			sql.append(lobDataIdList.get(i));
		}
		sql.append(")");

		return sql.toString();
	}

}
