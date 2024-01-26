/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb.sql;

import org.iplass.mtp.impl.datastore.grdb.strategy.metadata.LockStatus;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class SchemaControlUpdateVersionSql extends UpdateSqlHandler {
	
	public String toSql(int tenantId, String defId, int currentVer, int newVer, boolean isDataChange, LockStatus lockStatus, RdbAdapter rdb) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE SCHEMA_CTRL SET OBJ_DEF_VER=");
		sb.append(newVer);
		if (isDataChange) {
			sb.append(",CR_DATA_VER=");
			sb.append(newVer);
		}
		sb.append(" WHERE TENANT_ID=").append(tenantId);
		sb.append(" AND OBJ_DEF_ID='");
		sb.append(rdb.sanitize(defId));
		sb.append("' AND OBJ_DEF_VER=");
		sb.append(currentVer);
		sb.append(" AND LOCK_STATUS='");
		SchemaControlHandleLockSql.appendLockStatus(sb, lockStatus);
		sb.append("'");
		
		return sb.toString();
	}

}
