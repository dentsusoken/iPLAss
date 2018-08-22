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

package org.iplass.mtp.impl.fulltextsearch.sql;

import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;


public class CrawlLogDeleteSql extends UpdateSqlHandler {

	public String toSqlForCleanup(int tenantId, EntityHandler eh, RdbAdapter rdb) {
		return deleteByDefId(tenantId, eh.getMetaData().getId(), rdb);
	}

	public String deleteByDefId(int tenantId, String defId, RdbAdapter rdb) {

		return new StringBuilder().append("DELETE FROM ")
				.append(CrawlLogTable.TABLE_NAME)
				.append(" WHERE " + CrawlLogTable.TENANT_ID + "=")
				.append(tenantId)
				.append(" AND " + CrawlLogTable.OBJ_DEF_ID + "='")
				.append(rdb.sanitize(defId)).append("'")
				.toString();
	}

	public String deleteAll(int tenantId, RdbAdapter rdb) {

		return new StringBuilder().append("DELETE FROM ")
				.append(CrawlLogTable.TABLE_NAME)
				.append(" WHERE " + CrawlLogTable.TENANT_ID + "=")
				.append(tenantId)
				.toString();
	}
}
