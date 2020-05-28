/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.datastore.grdb.strategy;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.datastore.strategy.RecycleBinIterator;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GRdbRecycleBinIterator implements RecycleBinIterator {
	
	private ResultSet rs;
	private RdbAdapter rdb;

	public GRdbRecycleBinIterator(ResultSet rs, RdbAdapter rdb) {
		this.rs = rs;
		this.rdb = rdb;
	}

	@Override
	public boolean next() {
		try {
			return rs.next();
		} catch (Exception e) {
			throw new EntityRuntimeException(e);
		}
	}

	@Override
	public long getRbid() {
		try {
			return rs.getLong(ObjStoreTable.RB_ID);
		} catch (Exception e) {
			throw new EntityRuntimeException(e);
		}
	}

	@Override
	public String getOid() {
		try {
			return rs.getString(ObjStoreTable.OBJ_ID);
		} catch (Exception e) {
			throw new EntityRuntimeException(e);
		}
	}

	@Override
	public String getName() {
		try {
			return rs.getString(ObjStoreTable.OBJ_NAME);
		} catch (Exception e) {
			throw new EntityRuntimeException(e);
		}
	}

	@Override
	public Timestamp getRbDate() {
		try {
			return rs.getTimestamp(ObjStoreTable.RB_DATE, rdb.rdbCalendar());
		} catch (Exception e) {
			throw new EntityRuntimeException(e);
		}
	}

	@Override
	public String getRbUser() {
		try {
			return rs.getString(ObjStoreTable.RB_USER);
		} catch (Exception e) {
			throw new EntityRuntimeException(e);
		}
	}

	@Override
	public void close() {
		Statement stmt = null;
		Connection con = null;
		try {
			stmt = rs.getStatement();
			con = stmt.getConnection();
			rs.close();
		} catch (SQLException e) {
			Logger logger = LoggerFactory.getLogger(this.getClass());
			logger.error("Fail to close DB connection Resource. Check whether resource is leak or not.", e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger logger = LoggerFactory.getLogger(this.getClass());
					logger.error("Fail to close DB connection Resource. Check whether resource is leak or not.", e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					Logger logger = LoggerFactory.getLogger(this.getClass());
					logger.error("Fail to close DB connection Resource. Check whether resource is leak or not.", e);
				}
			}
		}
	}

}
