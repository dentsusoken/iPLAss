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

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class LobStoreInsertSql extends UpdateSqlHandler {
	
	public String toSql(int tenantId, long lobId, Long size, RdbAdapter rdb) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO " + LobStoreTable.TABLE_NAME + "("
				+ LobStoreTable.TENANT_ID
				+ "," + LobStoreTable.LOB_DATA_ID 
				+ "," + LobStoreTable.CRE_DATE
				+ "," + LobStoreTable.REF_COUNT
				+ "," + LobStoreTable.B_DATA);
		if (size != null) {
			sql.append("," + LobStoreTable.LOB_SIZE);
		}
		sql.append(") VALUES(");
		sql.append(tenantId).append(",");
		sql.append(lobId).append(",");
		sql.append(rdb.systimestamp()).append(",");
		sql.append("1,");
		sql.append("null");
		if (size != null) {
			sql.append(",").append(size.longValue());
		}
		sql.append(")");
		
		return sql.toString();
	}

}
