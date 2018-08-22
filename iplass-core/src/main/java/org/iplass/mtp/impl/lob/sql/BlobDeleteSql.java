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

public class BlobDeleteSql extends UpdateSqlHandler {
	
	public String toSql(int tenantId, long lobId, RdbAdapter rdb) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM OBJ_BLOB");
		sb.append(" WHERE TENANT_ID=").append(tenantId);
		sb.append(" AND LOB_ID=").append(lobId);
		
		return sb.toString();
	}
	
	public String toSql(int tenantId, String defId, String propId,
			String[] oid, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM OBJ_BLOB");
		sb.append(" WHERE TENANT_ID=").append(tenantId);
		sb.append(" AND OBJ_DEF_ID='").append(rdb.sanitize(defId)).append("'");
		if (propId != null) {
			sb.append(" AND PROP_DEF_ID=").append(rdb.sanitize(propId)).append("'");
		}
		sb.append(" AND OBJ_ID IN(");
		for (int i = 0; i < oid.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("'").append(rdb.sanitize(oid[i])).append("'");
		}
		sb.append(")");
		return sb.toString();
	}

	public String toSqlForCleanTemporary(RdbAdapter rdb, int day) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM OBJ_BLOB");
		sb.append(" WHERE LOB_STAT='T'");
		sb.append(" AND UP_DATE<").append(rdb.addDate(rdb.systimestamp(), day));
		return sb.toString();
	}
}
