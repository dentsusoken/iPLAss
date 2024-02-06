/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.connection;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.ClientInfoStatus;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplicationAwareConnection implements Connection {
	
	private static Logger logger = LoggerFactory.getLogger(ReplicationAwareConnection.class);

	private ReplicationAwareDataSourceConnectionFactory conFactory;
	private Function<Connection, Connection> afterGetPhysicalConnectionHandler;
	
	private Connection con;
	private boolean readOnly;
	private boolean autoCommit = true;
	private boolean close;

	ReplicationAwareConnection(ReplicationAwareDataSourceConnectionFactory conFactory, Function<Connection, Connection> afterGetPhysicalConnectionHandler) {
		this.conFactory = conFactory;
		this.afterGetPhysicalConnectionHandler = afterGetPhysicalConnectionHandler;
	}
	
	private Connection getCon() throws SQLException {
		if (close) {
			throw new SQLException("Connection is closed");
		}
		if (con == null) {
			if (readOnly) {
				con = conFactory.getReplicaConnectionInternal();
				if (logger.isDebugEnabled()) {
					logger.debug("Create Pysical Connection from replicaDataSource:" + this);
				}
			} else {
				con = conFactory.getConnectionInternal();
				if (logger.isDebugEnabled()) {
					logger.debug("Create Pysical Connection from dataSource:" + this);
				}
			}
			conFactory.initPhysicalConnection(con, afterGetPhysicalConnectionHandler);
			con.setAutoCommit(autoCommit);
			con.setReadOnly(readOnly);
		}
		
		return con;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return getCon().unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return getCon().isWrapperFor(iface);
	}

	public Statement createStatement() throws SQLException {
		return getCon().createStatement();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return getCon().prepareStatement(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return getCon().prepareCall(sql);
	}

	public String nativeSQL(String sql) throws SQLException {
		return getCon().nativeSQL(sql);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.autoCommit = autoCommit;
		if (con != null) {
			getCon().setAutoCommit(autoCommit);
		}
	}

	public boolean getAutoCommit() throws SQLException {
		if (con == null) {
			return autoCommit;
		} else {
			return getCon().getAutoCommit();
		}
	}

	public void commit() throws SQLException {
		if (con != null) {
			getCon().commit();
		}
	}

	public void rollback() throws SQLException {
		if (con != null) {
			getCon().rollback();
		}
	}

	public void close() throws SQLException {
		close = true;
		if (con != null) {
			con.close();
		}
	}

	public boolean isClosed() throws SQLException {
		if (con == null) {
			return close;
		} else {
			return con.isClosed();
		}
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return getCon().getMetaData();
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		if (con == null) {
			this.readOnly = readOnly;
		} else {
			getCon().setReadOnly(readOnly);
		}
	}

	public boolean isReadOnly() throws SQLException {
		if (con == null) {
			return readOnly;
		} else {
			return getCon().isReadOnly();
		}
	}

	public void setCatalog(String catalog) throws SQLException {
		getCon().setCatalog(catalog);
	}

	public String getCatalog() throws SQLException {
		return getCon().getCatalog();
	}

	public void setTransactionIsolation(int level) throws SQLException {
		getCon().setTransactionIsolation(level);
	}

	public int getTransactionIsolation() throws SQLException {
		return getCon().getTransactionIsolation();
	}

	public SQLWarning getWarnings() throws SQLException {
		if (con != null) {
			return getCon().getWarnings();
		} else {
			return null;
		}
	}

	public void clearWarnings() throws SQLException {
		if (con != null) {
			getCon().clearWarnings();
		}
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return getCon().createStatement(resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return getCon().prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return getCon().prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return getCon().getTypeMap();
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		getCon().setTypeMap(map);
	}

	public void setHoldability(int holdability) throws SQLException {
		getCon().setHoldability(holdability);
	}

	public int getHoldability() throws SQLException {
		return getCon().getHoldability();
	}

	public Savepoint setSavepoint() throws SQLException {
		return getCon().setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return getCon().setSavepoint(name);
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		getCon().rollback(savepoint);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		getCon().releaseSavepoint(savepoint);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return getCon().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return getCon().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		return getCon().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		return getCon().prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		return getCon().prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		return getCon().prepareStatement(sql, columnNames);
	}

	public Clob createClob() throws SQLException {
		return getCon().createClob();
	}

	public Blob createBlob() throws SQLException {
		return getCon().createBlob();
	}

	public NClob createNClob() throws SQLException {
		return getCon().createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return getCon().createSQLXML();
	}

	public boolean isValid(int timeout) throws SQLException {
		return getCon().isValid(timeout);
	}

	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		try {
			getCon().setClientInfo(name, value);
		} catch (SQLException e) {
			throw new SQLClientInfoException("Failed Create Connecton on setClientInfo()", Collections.singletonMap(name, ClientInfoStatus.REASON_UNKNOWN), e);
		}
	}

	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		try {
			getCon().setClientInfo(properties);
		} catch (SQLException e) {
			
			HashMap<String, ClientInfoStatus> ci = new HashMap<>();
			if (properties != null) {
				for (Object k: properties.keySet()) {
					ci.put((String) k, ClientInfoStatus.REASON_UNKNOWN);
				}
			}
			throw new SQLClientInfoException("Failed Create Connecton on setClientInfo()", ci, e);
		}
	}

	public String getClientInfo(String name) throws SQLException {
		return getCon().getClientInfo(name);
	}

	public Properties getClientInfo() throws SQLException {
		return getCon().getClientInfo();
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return getCon().createArrayOf(typeName, elements);
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return getCon().createStruct(typeName, attributes);
	}

	public void setSchema(String schema) throws SQLException {
		getCon().setSchema(schema);
	}

	public String getSchema() throws SQLException {
		return getCon().getSchema();
	}

	public void abort(Executor executor) throws SQLException {
		getCon().abort(executor);
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		getCon().setNetworkTimeout(executor, milliseconds);
	}

	public int getNetworkTimeout() throws SQLException {
		return getCon().getNetworkTimeout();
	}

	@Override
	public String toString() {
		return "ReplicationAwareConnection@" + Integer.toHexString(hashCode()) + " [readOnly=" + readOnly + ", wrapped=" + con + "]";
	}

}
