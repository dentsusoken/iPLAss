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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthTokenUpdateSQL extends UpdateSqlHandler {
	
	public String createInsertSQL() {
		return "INSERT INTO T_ATOKEN(TENANT_ID,T_TYPE,U_KEY,SERIES,TOKEN,POL_NAME,S_DATE,T_INFO) " +
				"VALUES (?,?,?,?,?,?,?,?)";
 	}
	public void setInsertParameter(RdbAdapter rdb, PreparedStatement ps, AuthToken token, ObjectMapper om) throws SQLException, JsonProcessingException {
		// TENANT_ID
		ps.setInt(1, token.getTenantId());
		// T_TYPE
		ps.setString(2, token.getType());
		// U_KEY
		ps.setString(3, token.getOwnerId());
		// SERIES
		ps.setString(4, token.getSeries());
		// TOKEN
		ps.setString(5, token.getToken());
		// POL_NAME
		ps.setString(6, token.getPolicyName());
		//S_DATE
		ps.setTimestamp(7, token.getStartDate());
		// T_INFO
		if (token.getDetails() == null) {
			ps.setBytes(8, null);
		} else {
			ps.setBytes(8, om.writeValueAsBytes(new DetailWrapper(token.getDetails())));
		}
	}

	public String createDeleteSQL() {
		return "DELETE FROM T_ATOKEN WHERE TENANT_ID=? AND T_TYPE=? AND U_KEY=?";
	}
	public void setDeleteParameter(RdbAdapter rdb, PreparedStatement ps,
			int tenantId, String type, String uniqueKey) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, type);
		ps.setString(3, uniqueKey);
	}

	public String createDeleteBySeriesSQL() {
		return "DELETE FROM T_ATOKEN WHERE TENANT_ID=? AND T_TYPE=? AND SERIES=?";
	}
	public void setDeleteBySeriesParameter(RdbAdapter rdb, PreparedStatement ps,
			int tenantId, String type, String series) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, type);
		ps.setString(3, series);
	}

	public String createDeleteByDateSQL() {
		return "DELETE FROM T_ATOKEN WHERE TENANT_ID=? AND T_TYPE=? S_DATE<=?";
	}
	public void setDeleteByDateParameter(RdbAdapter rdb, PreparedStatement ps,
			int tenantId, String type, Timestamp date) throws SQLException {
		ps.setInt(1, tenantId);
		ps.setString(2, type);
		ps.setTimestamp(3, date);
	}

	public String createUpdateStrictSQL() {
		return "UPDATE T_ATOKEN SET TOKEN=?,S_DATE=?,T_INFO=? WHERE TENANT_ID=? AND T_TYPE=? AND SERIES=? AND TOKEN=?";
	}
	public void setUpdateStrictParameter(RdbAdapter rdb, PreparedStatement ps, AuthToken newToken, AuthToken currentToken, ObjectMapper om) throws SQLException, JsonProcessingException {
		ps.setString(1, newToken.getToken());
		ps.setTimestamp(2, newToken.getStartDate());
		if (newToken.getDetails() == null) {
			ps.setBytes(3, null);
		} else {
			ps.setBytes(3, om.writeValueAsBytes(new DetailWrapper(newToken.getDetails())));
		}
		ps.setInt(4, newToken.getTenantId());
		ps.setString(5, newToken.getType());
		ps.setString(6, newToken.getSeries());
		ps.setString(7, currentToken.getToken());
	}
}
