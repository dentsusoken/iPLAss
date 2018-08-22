/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.fulltextsearch.sql;

import java.sql.Timestamp;

import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogTable.Status;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class DeleteLogInsertSql extends UpdateSqlHandler {

	public String toSql(int tenantId, String defId, String objId, long objVer, Status status, Timestamp sysdate, RdbAdapter rdb) {

		String now = rdb.toTimeStampExpression(sysdate);

		return new StringBuilder().append("INSERT INTO ")
				.append(DeleteLogTable.TABLE_NAME + "(")
				.append(DeleteLogTable.TENANT_ID)
				.append("," + DeleteLogTable.OBJ_DEF_ID)
				.append("," + DeleteLogTable.OBJ_ID)
				.append("," + DeleteLogTable.OBJ_VER)
				.append("," + DeleteLogTable.STATUS)
				.append("," + DeleteLogTable.CRE_DATE)
				.append("," + DeleteLogTable.UP_DATE)
				.append(") VALUES (")
				.append(tenantId)
				.append(",'" + rdb.sanitize(defId) + "'")
				.append(",'" + rdb.sanitize(objId) + "'")
				.append("," + objVer)
				.append(",'" + status.code() + "'")
				.append("," + now)
				.append("," + now)
				.append(")")
				.toString();
	}

}
