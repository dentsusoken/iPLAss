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
import java.sql.Timestamp;

import org.iplass.mtp.impl.fulltextsearch.AbstractFulltextSearchService.RestoreDto;
import org.iplass.mtp.impl.fulltextsearch.sql.DeleteLogTable.Status;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;


public class DeleteLogSearchSql extends QuerySqlHandler {

	public String toGetDeleteIndexDataSql(int tenantId, String defId, Timestamp baseDate, RdbAdapter rdb) {
		return new StringBuilder().append("SELECT ")
				.append(DeleteLogTable.TENANT_ID)
				.append("," + DeleteLogTable.OBJ_DEF_ID)
				.append("," + DeleteLogTable.OBJ_ID)
				.append("," + DeleteLogTable.OBJ_VER)
				.append("," + DeleteLogTable.STATUS)
				.append(" FROM " + DeleteLogTable.TABLE_NAME)
				.append(" WHERE ")
				.append(DeleteLogTable.TENANT_ID + " = " + tenantId)
				.append(" AND " + DeleteLogTable.OBJ_DEF_ID + " = '")
				.append(rdb.sanitize(defId) + "'")
				.append(" AND " + DeleteLogTable.CRE_DATE + " < ")
				.append(rdb.toTimeStampExpression(baseDate))
				.append(" ORDER BY ")
				.append(DeleteLogTable.CRE_DATE)
				.toString();
	}

	public RestoreDto toFulltextSearchRestoreDto(ResultSet rs) throws SQLException {

		RestoreDto dto = new RestoreDto();

		dto.setTenantId(rs.getInt(1));
		dto.setObjDefId(rs.getString(2));
		dto.setObjId(rs.getString(3));
		dto.setObjVer(rs.getLong(4));
		dto.setStatus(Status.codeOf(rs.getString(5)));

		String id = dto.getTenantId() + "_" + dto.getObjDefId() + "_" + dto.getObjId() + "_" + dto.getObjVer();
		dto.setId(id);

		return dto;
	}

}
