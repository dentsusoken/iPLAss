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

package org.iplass.mtp.impl.rdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.EntityConcurrentUpdateException;
import org.iplass.mtp.entity.EntityDuplicateValueException;
import org.iplass.mtp.entity.EntityQueryTimeoutException;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.EntityValueCastException;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class SqlExecuter<R> {

	RdbAdapter rdb;
	Connection con;
	Statement stmt;
	List<PreparedStatement> pstmt;

	String connectionFactoryName;

	public Connection getConnection() throws SQLException {
		if (con == null) {
			con = rdb.getConnection(connectionFactoryName);
		}
		return con;
	}

	public PreparedStatement getPreparedStatement(String sql, String[] columnNames) throws SQLException {
		if (pstmt == null) {
			pstmt = new ArrayList<PreparedStatement>();
		}
		PreparedStatement ps = getConnection().prepareStatement(sql, columnNames);
		if (rdb.getDefaultQueryTimeout() != ps.getQueryTimeout()) {
			ps.setQueryTimeout(rdb.getDefaultQueryTimeout());
		}
		if (rdb.getDefaultFetchSize() >= 0 && rdb.getDefaultFetchSize() != ps.getFetchSize()) {
			ps.setFetchSize(rdb.getDefaultFetchSize());
		}
		pstmt.add(ps);

		return ps;
	}

	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		if (pstmt == null) {
			pstmt = new ArrayList<PreparedStatement>();
		}
		PreparedStatement ps = getConnection().prepareStatement(sql);
		if (rdb.getDefaultQueryTimeout() != ps.getQueryTimeout()) {
			ps.setQueryTimeout(rdb.getDefaultQueryTimeout());
		}
		if (rdb.getDefaultFetchSize() >= 0 && rdb.getDefaultFetchSize() != ps.getFetchSize()) {
			ps.setFetchSize(rdb.getDefaultFetchSize());
		}
		pstmt.add(ps);

		return ps;
	}

	public Statement getStatement() throws SQLException {
		if (stmt == null) {
			stmt = getConnection().createStatement();
			if (rdb.getDefaultQueryTimeout() != stmt.getQueryTimeout()) {
				stmt.setQueryTimeout(rdb.getDefaultQueryTimeout());
			}
			if (rdb.getDefaultFetchSize() >= 0 && rdb.getDefaultFetchSize() != stmt.getFetchSize()) {
				stmt.setFetchSize(rdb.getDefaultFetchSize());
			}
		}
		return stmt;
	}

	public R execute(RdbAdapter rdb, boolean withResourceClose) {
		return execute(null, rdb, withResourceClose);
	}

	public R execute(String connectionFactoryName, RdbAdapter rdb, boolean withResourceClose) {
		this.rdb = rdb;
		this.connectionFactoryName = connectionFactoryName;

		if (withResourceClose) {
			try {

				return logic();

			} catch (SQLException e) {
				//TODO このクラスが、entityパッケージを知っているのは微妙か。。。

				//timeout
				if (e instanceof SQLTimeoutException) {
					throw new EntityQueryTimeoutException(resourceString("impl.entity.queryTimeout"), e);
				}

				// データ重複
				if (rdb.isDuplicateValueException(e)) {
					throw new EntityDuplicateValueException(resourceString("impl.transaction.LocalTransaction.duplicate"), e);
				}
				// DeadLock
				EntityConcurrentUpdateException ecue;
				if (rdb.isDeadLock(e)) {
					ecue = new EntityConcurrentUpdateException(resourceString("spi.rdb.SqlExecuter.cantUpdate") ,e);
					ecue.setDeadLock(true);
					throw ecue;
				}
				// LockFailed
				if(rdb.isLockFailed(e)) {
					ecue = new EntityConcurrentUpdateException(resourceString("spi.rdb.SqlExecuter.cantUpdate"), e);
					ecue.setNowait(true);
					throw ecue;
				}
				// CastFailed
				if (rdb.isCastFailed(e)) {
					throw new EntityValueCastException(resourceString("spi.rdb.SqlExecuter.cantCast"), e);
				}
				throw new EntityRuntimeException(e);
			} finally {
				close();
			}
		} else {
			try {

				return logic();

			} catch (SQLException e) {
				close();
				//TODO このクラスが、entityパッケージを知っているのは微妙か。。。

				//timeout
				if (e instanceof SQLTimeoutException) {
					throw new EntityQueryTimeoutException(resourceString("impl.entity.queryTimeout"), e);
				}

				// データ重複
				if (rdb.isDuplicateValueException(e)) {
					throw new EntityDuplicateValueException(resourceString("impl.transaction.LocalTransaction.duplicate"), e);
				}
				// DeadLock
				EntityConcurrentUpdateException ecue;
				if (rdb.isDeadLock(e)) {
					ecue = new EntityConcurrentUpdateException(e);
					ecue.setDeadLock(true);
					throw ecue;
				}
				// LockFailed
				if(rdb.isLockFailed(e)) {
					ecue = new EntityConcurrentUpdateException(e);
					ecue.setNowait(true);
					throw ecue;
				}
				// CastFailed
				if (rdb.isCastFailed(e)) {
					throw new EntityValueCastException(resourceString("spi.rdb.SqlExecuter.cantCast"), e);
				}
				throw new EntityRuntimeException(e);
			}

		}
	}

	public void statementClose() {
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				logFailResourceCleaning(e);
			}
		}
	}

	public void close() {
		if (stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				logFailResourceCleaning(e);
			}
		}
		if (pstmt != null) {
			for (PreparedStatement ps: pstmt) {
				try {
					ps.close();
				} catch (SQLException e) {
					logFailResourceCleaning(e);
				}
			}
			pstmt = null;
		}
		if (con != null) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
				logFailResourceCleaning(e);
			}
		}
	}

	public abstract R logic() throws SQLException;

	private void logFailResourceCleaning(Exception e) {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.error("Fail to close DB connection Resource. Check whether resource is leak or not.", e);
	}

	public static <R> R execute(RdbAdapter rdb, SqlExecuter<R> exec) {
		return exec.execute(rdb, true);
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
