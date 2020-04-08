/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.token.sql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthTokenSelectSQL extends QuerySqlHandler {
	
	private static Logger logger = LoggerFactory.getLogger(AuthTokenSelectSQL.class);
	
	public String createSelectSQL() {
		return "SELECT " +
				"TENANT_ID,T_TYPE,U_KEY,SERIES,TOKEN,POL_NAME,S_DATE,T_INFO " +
				"FROM T_ATOKEN " +
				"WHERE TENANT_ID=? AND T_TYPE=? AND SERIES=?";
	}
	public void setSelectParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String type, String series) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, type);
		ps.setString(3, series);
	}
	public AuthToken toAuthToken(ResultSet rs, boolean saveDetailAsJson, ObjectMapper om, RdbAdapter rdb) throws SQLException {
		AuthToken at = new AuthToken(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getTimestamp(7, rdb.rdbCalendar()), null);
		byte[] tiByte = rs.getBytes(8);
		if (tiByte != null && tiByte.length > 0) {
			try {
				if (saveDetailAsJson) {
					at.setDetails(om.readValue(tiByte, DetailWrapper.class).getDetails());
				} else {
					ByteArrayInputStream byteIn = new ByteArrayInputStream(tiByte);
					ObjectInputStream in = new ObjectInputStream(byteIn);
					at.setDetails((Serializable) in.readObject());
				}
			} catch (IOException | ClassNotFoundException e) {
				logger.error("Can't parse tokenInfo value so ignore tokenInfo:tenantId=" + at.getTenantId() + ", type=" + at.getType() + ", series=" + at.getSeries());
			}
		}
		return at;
	}
	
	public String createSelectByUserUniqueKeySQL() {
		return "SELECT " +
				"TENANT_ID,T_TYPE,U_KEY,SERIES,TOKEN,POL_NAME,S_DATE,T_INFO " +
				"FROM T_ATOKEN " +
				"WHERE TENANT_ID=? AND U_KEY=? AND T_TYPE=?";
	}
	public void setSelectByUserUniqueKeyParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId, String type, String userUniqueKey) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, userUniqueKey);
		ps.setString(3, type);
	}

}
