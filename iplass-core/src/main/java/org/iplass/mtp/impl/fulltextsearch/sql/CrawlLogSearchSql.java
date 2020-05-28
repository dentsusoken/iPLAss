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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.impl.fulltextsearch.AbstractFulltextSeachService.CrawlTimestampDto;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class CrawlLogSearchSql extends QuerySqlHandler {

	public String toGetLastCrawlTimestampSql(int tenantId, String defId, int version, RdbAdapter rdb) {

		return new StringBuilder().append("SELECT ")
				.append(CrawlLogTable.UP_DATE)
				.append(" FROM " + CrawlLogTable.TABLE_NAME)
				.append(" WHERE ")
				.append(CrawlLogTable.TENANT_ID + " = " + tenantId)
				.append(" AND " + CrawlLogTable.OBJ_DEF_ID + " = '")
				.append(rdb.sanitize(defId) + "'")
				.append(" AND " + CrawlLogTable.OBJ_DEF_VER + " = ")
				.append(version)
				.toString();
	}

	public String toGetAllLastCrawlTimestampSql(int tenantId, RdbAdapter rdb) {

		return new StringBuilder().append("SELECT ")
				.append(CrawlLogTable.OBJ_DEF_ID)
				.append(", MAX(" + CrawlLogTable.OBJ_DEF_VER + ")")
				.append(", MAX(" + CrawlLogTable.UP_DATE + ")")
				.append(" FROM " + CrawlLogTable.TABLE_NAME)
				.append(" WHERE ")
				.append(CrawlLogTable.TENANT_ID + " = " + tenantId)
				.append(" GROUP BY ")
				.append(CrawlLogTable.OBJ_DEF_ID)
				.toString();
	}

	public CrawlTimestampDto toFulltextSearchCrawlTimestampDto(ResultSet rs, RdbAdapter rdb) throws SQLException {

		CrawlTimestampDto dto = new CrawlTimestampDto();
		dto.setObjDefId(rs.getString(1));
		dto.setObjDefVer(rs.getString(2));
		dto.setUpDate(rs.getTimestamp(3, rdb.rdbCalendar()));
		return dto;
	}

}
