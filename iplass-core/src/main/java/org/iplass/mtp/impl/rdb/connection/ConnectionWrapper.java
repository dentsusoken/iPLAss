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

package org.iplass.mtp.impl.rdb.connection;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
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
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionWrapper implements Connection {

	private Connection wrapped;
	private int warnLogThreshold;
	private boolean warnLogBefore;
	
	ConnectionWrapper(Connection wrapped, int warnLogThreshold, boolean warnLogBefore) {
		this.wrapped = wrapped;
		this.warnLogThreshold = warnLogThreshold;
		this.warnLogBefore = warnLogBefore;
	}

	public Connection getWrapped() {
		return wrapped;
	}
	
	public void setWrapped(Connection wrapped) {
		this.wrapped = wrapped;
	}

	public void clearWarnings() throws SQLException {
		wrapped.clearWarnings();
	}

	public void close() throws SQLException {
		wrapped.close();
	}

	public void commit() throws SQLException {
		wrapped.commit();
	}

	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return wrapped.createArrayOf(typeName, elements);
	}

	public Blob createBlob() throws SQLException {
		return wrapped.createBlob();
	}

	public Clob createClob() throws SQLException {
		return wrapped.createClob();
	}

	public NClob createNClob() throws SQLException {
		return wrapped.createNClob();
	}

	public SQLXML createSQLXML() throws SQLException {
		return wrapped.createSQLXML();
	}

	public Statement createStatement() throws SQLException {
		return new StatementWrapper(wrapped.createStatement(), this, warnLogThreshold, warnLogBefore);
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return new StatementWrapper(wrapped.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability), this, warnLogThreshold, warnLogBefore);
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return new StatementWrapper(wrapped.createStatement(resultSetType, resultSetConcurrency), this, warnLogThreshold, warnLogBefore);
	}

	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return wrapped.createStruct(typeName, attributes);
	}

	public boolean getAutoCommit() throws SQLException {
		return wrapped.getAutoCommit();
	}

	public String getCatalog() throws SQLException {
		return wrapped.getCatalog();
	}

	public Properties getClientInfo() throws SQLException {
		return wrapped.getClientInfo();
	}

	public String getClientInfo(String name) throws SQLException {
		return wrapped.getClientInfo(name);
	}

	public int getHoldability() throws SQLException {
		return wrapped.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		return wrapped.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException {
		return wrapped.getTransactionIsolation();
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return wrapped.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException {
		return wrapped.getWarnings();
	}

	public boolean isClosed() throws SQLException {
		return wrapped.isClosed();
	}

	public boolean isReadOnly() throws SQLException {
		return wrapped.isReadOnly();
	}

	public boolean isValid(int timeout) throws SQLException {
		return wrapped.isValid(timeout);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return wrapped.isWrapperFor(iface);
	}

	public String nativeSQL(String sql) throws SQLException {
		return wrapped.nativeSQL(sql);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return new CallableStatementWrapper(wrapped.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability), this);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return new CallableStatementWrapper(wrapped.prepareCall(sql, resultSetType, resultSetConcurrency), this);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		return new CallableStatementWrapper(wrapped.prepareCall(sql), this);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return new PreparedStatementWrapper(wrapped.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability), this, sql, warnLogThreshold, warnLogBefore);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return new PreparedStatementWrapper(wrapped.prepareStatement(sql, resultSetType,
				resultSetConcurrency), this, sql, warnLogThreshold, warnLogBefore);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return new PreparedStatementWrapper(wrapped.prepareStatement(sql, autoGeneratedKeys), this, sql, warnLogThreshold, warnLogBefore);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return new PreparedStatementWrapper(wrapped.prepareStatement(sql, columnIndexes), this, sql, warnLogThreshold, warnLogBefore);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return new PreparedStatementWrapper(wrapped.prepareStatement(sql, columnNames), this, sql, warnLogThreshold, warnLogBefore);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new PreparedStatementWrapper(wrapped.prepareStatement(sql), this, sql, warnLogThreshold, warnLogBefore);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		wrapped.releaseSavepoint(savepoint);
	}

	public void rollback() throws SQLException {
		wrapped.rollback();
	}

	public void rollback(Savepoint savepoint) throws SQLException {
		wrapped.rollback(savepoint);
	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		wrapped.setAutoCommit(autoCommit);
	}

	public void setCatalog(String catalog) throws SQLException {
		wrapped.setCatalog(catalog);
	}

	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		wrapped.setClientInfo(properties);
	}

	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		wrapped.setClientInfo(name, value);
	}

	public void setHoldability(int holdability) throws SQLException {
		wrapped.setHoldability(holdability);
	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		wrapped.setReadOnly(readOnly);
	}

	public Savepoint setSavepoint() throws SQLException {
		return wrapped.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		return wrapped.setSavepoint(name);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		wrapped.setTransactionIsolation(level);
	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		wrapped.setTypeMap(map);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return wrapped.unwrap(iface);
	}

	//for JDBC 4.1
	public void setSchema(String schema) throws SQLException {
		wrapped.setSchema(schema);
	}

	public String getSchema() throws SQLException {
		return wrapped.getSchema();
	}

	public void abort(Executor executor) throws SQLException {
		wrapped.abort(executor);
	}

	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		wrapped.setNetworkTimeout(executor, milliseconds);
	}

	public int getNetworkTimeout() throws SQLException {
		return wrapped.getNetworkTimeout();
	}
	
	public int getWarnLogThreshold() {
		return warnLogThreshold;
	}
}
