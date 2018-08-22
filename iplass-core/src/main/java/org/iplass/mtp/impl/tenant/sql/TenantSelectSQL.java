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

package org.iplass.mtp.impl.tenant.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.tenant.Tenant;

/**
 * テナント検索用のSQL
 * @author 片野　博之
 *
 */
public class TenantSelectSQL extends QuerySqlHandler  {
	public static final String COLUMNS = "ID, " + TenantControlSQL.COLUMNS;
	public String createSQL() {
		return "SELECT " + COLUMNS + " FROM T_TENANT WHERE ID = ? AND YUKO_DATE_FROM <= ? AND YUKO_DATE_TO >= ?";
	}

	public void setParameter(RdbAdapter rdb, PreparedStatement ps, int tenantId) throws SQLException {
		Date now = InternalDateUtil.getNowForSqlDate();
		int index = 1;
		ps.setInt(index++, tenantId);
		ps.setDate(index++, now);
		ps.setDate(index++, now);
	}

	public String createSQL(String url) {
		if(url.length() == 0) {
			return "SELECT " + COLUMNS + " FROM T_TENANT WHERE URL IS NULL AND YUKO_DATE_FROM <= ? AND YUKO_DATE_TO >= ?";
		} else {
			return "SELECT " + COLUMNS + " FROM T_TENANT WHERE URL = ? AND YUKO_DATE_FROM <= ? AND YUKO_DATE_TO >= ?";
		}
	}

	public void setParameter(RdbAdapter rdb, PreparedStatement ps, String url) throws SQLException {
		Date now = InternalDateUtil.getNowForSqlDate();
		int index = 1;
		if(url.length() > 0) {
			ps.setString(index++, url);
		}
		ps.setDate(index++, now);
		ps.setDate(index++, now);
	}

	public Tenant createTenant(ResultSet rs) throws SQLException {
		if(!rs.next()) {
			return null;
		}
		Tenant tenant = new Tenant();
		tenant.setId(rs.getInt("ID"));
		tenant.setName(rs.getString("NAME"));
		tenant.setDescription(rs.getString("DESCRIPTION"));
		String url = rs.getString("URL");
		tenant.setUrl(url == null ? "": url);
		tenant.setFrom(rs.getDate("YUKO_DATE_FROM"));
		tenant.setTo(rs.getDate("YUKO_DATE_TO"));
		
		tenant.setCreateUser(rs.getString("CRE_USER"));
		tenant.setCreateDate(rs.getTimestamp("CRE_DATE"));
		tenant.setUpdateUser(rs.getString("UP_USER"));
		tenant.setUpdateDate(rs.getTimestamp("UP_DATE"));
		
		if(rs.next()) {
			throw new IllegalStateException("Tenant情報が複数あります");
		}
		return tenant;

	}
	
	public String createAllTenantIdListSQL() {
		return "SELECT ID FROM T_TENANT WHERE YUKO_DATE_FROM <= ? AND YUKO_DATE_TO >= ?";
	}
	
	public void setAllTenantIdListParameter(RdbAdapter rdb, PreparedStatement ps) throws SQLException {
		Date now = InternalDateUtil.getNowForSqlDate();
		int index = 1;
		ps.setDate(index++, now);
		ps.setDate(index++, now);
	}
	
	public List<Integer> getAllTenantIdList(ResultSet rs) throws SQLException {
		List<Integer> list = new ArrayList<Integer>();
		while (rs.next()) {
			list.add(rs.getInt("ID"));
		}
		return list;
	}
	
}
