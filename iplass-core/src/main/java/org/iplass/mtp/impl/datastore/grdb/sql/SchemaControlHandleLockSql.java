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


public class SchemaControlHandleLockSql extends UpdateSqlHandler {
	
	public String toSql(int tenantId, String defId, int ver, LockStatus fromLockStatus, LockStatus toLockStatus, RdbAdapter rdb) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE SCHEMA_CTRL SET LOCK_STATUS='");
		appendLockStatus(sb, toLockStatus);
		sb.append("' WHERE TENANT_ID=").append(tenantId);
		sb.append(" AND OBJ_DEF_ID='");
		sb.append(rdb.sanitize(defId));
		sb.append("' AND OBJ_DEF_VER=");
		sb.append(ver);
		sb.append(" AND LOCK_STATUS='");
		appendLockStatus(sb, fromLockStatus);
		sb.append("'");
		
		return sb.toString();
	}
	
	static void appendLockStatus(StringBuilder sb, LockStatus lockStatus) {
		if (lockStatus == LockStatus.NO_LOCK) {
			sb.append("0");
		} else if (lockStatus == LockStatus.LOCK) {
			sb.append("1");
		} else {
			throw new IllegalArgumentException("lockStatus is null");
		}
	}

}
