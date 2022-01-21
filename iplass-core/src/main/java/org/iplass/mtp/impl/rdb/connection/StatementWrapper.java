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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.argument.StructuredArguments;
import net.logstash.logback.marker.Markers;

public class StatementWrapper implements Statement {

	private static Logger logger = LoggerFactory.getLogger(StatementWrapper.class);

	private Statement wrapped;
	private ConnectionWrapper con;
	protected int warnLogThreshold;
	protected boolean warnLogBefore;
	protected boolean countSqlExecution;
	
	protected AdditionalWarnLogInfo additionalWarnLogInfo;
	protected AtomicInteger sqlCount;

	StatementWrapper(Statement wrapped, ConnectionWrapper con, int warnLogThreshold, boolean warnLogBefore, boolean countSqlExecution) {
		this.wrapped = wrapped;
		this.con = con;
		this.warnLogThreshold = warnLogThreshold;
		this.warnLogBefore = warnLogBefore;
		this.countSqlExecution = countSqlExecution;
	}
	
	private <T> T withLog(String method, String sql, SQLExecution<T> s) throws SQLException {
		
		if (countSqlExecution) {
			if (sqlCount == null) {
				ExecuteContext ec = ExecuteContext.getCurrentContext();
				sqlCount = (AtomicInteger) ec.getAttribute(ConnectionFactory.SQL_COUNT_KEY);
			}
			if (sqlCount != null) {
				sqlCount.incrementAndGet();
			}
		}
		
		long start = System.currentTimeMillis();
		try {
			if (warnLogBefore && additionalWarnLogInfo != null && additionalWarnLogInfo.logBefore()) {
				logger.warn(Markers.append("warning_type", "alert"), withWarnLogFormat(method, sql, null), logParam(method, sql, null));
			}
			
			return s.run();
			
		} finally {
			long queryTime = System.currentTimeMillis() - start;
			if (warnLogThreshold > 0  && queryTime > warnLogThreshold) {
				if (logger.isWarnEnabled()) {
					logger.warn(Markers.append("warning_type", "time"), withWarnLogFormat(method, sql, queryTime), logParam(method, sql, queryTime));
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(Markers.append("warning_type", "time"), withWarnLogFormat(method, sql, queryTime), logParam(method, sql, queryTime));
				}
			}
		}
	}
	
	private Object[] logParam(String method, String sql, Long queryTime) {
		Object[] logParam;
		int base;
		if (queryTime == null) {
			base = 2;
		} else {
			base = 3;
		}
		if (additionalWarnLogInfo == null) {
			logParam = new Object[base];
		} else {
			logParam = new Object[base + additionalWarnLogInfo.parameterSize()];
		}
		
		int i = 0;
		logParam[i++] = StructuredArguments.value("method", method);
		if (queryTime != null) {
			logParam[i++] = StructuredArguments.value("execution_time", queryTime);
		}
		if (sql != null) {
			logParam[i++] = StructuredArguments.value("sql", sql);
		}
		if (additionalWarnLogInfo != null) {
			additionalWarnLogInfo.setParameter(base, logParam);
		}
		return logParam;
	}
	
	private String withWarnLogFormat(String method, String sql, Long queryTime) {
		String fmt;
		if (queryTime == null) {
			if (sql == null) {
				fmt = "{}";
			} else {
				fmt = "{} sql={}";
			}
		} else {
			if (sql == null) {
				fmt = "{} time= {} ms.";
			} else {
				fmt = "{} time= {} ms. sql={}";
			}
		}
		
		if (additionalWarnLogInfo == null) {
			return fmt;
		} else {
			String warnLogFmt = additionalWarnLogInfo.logFormat();
			StringBuilder sb = new StringBuilder(fmt.length() + 4 + warnLogFmt.length());
			sb.append(fmt).append(" -- ").append(warnLogFmt);
			return sb.toString();
		}
	}
	
	public AdditionalWarnLogInfo getAdditionalWarnLogInfo() {
		return additionalWarnLogInfo;
	}

	public void setAdditionalWarnLogInfo(AdditionalWarnLogInfo additionalWarnLogInfo) {
		this.additionalWarnLogInfo = additionalWarnLogInfo;
	}

	public final void addBatch(String sql) throws SQLException {
		logger.debug("addBatch: {}", sql);
		wrapped.addBatch(sql);
	}

	public final void cancel() throws SQLException {
		wrapped.cancel();
	}

	public final void clearBatch() throws SQLException {
		wrapped.clearBatch();
	}

	public final void clearWarnings() throws SQLException {
		wrapped.clearWarnings();
	}

	public final void close() throws SQLException {
		wrapped.close();
	}

	public final boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		return withLog("execute", sql, () -> wrapped.execute(sql, autoGeneratedKeys));
	}

	public final boolean execute(String sql, int[] columnIndexes) throws SQLException {
		return withLog("execute", sql, () -> wrapped.execute(sql, columnIndexes));
	}

	public final boolean execute(String sql, String[] columnNames)
			throws SQLException {
		return withLog("execute", sql, () -> wrapped.execute(sql, columnNames));
	}

	public final boolean execute(String sql) throws SQLException {
		return withLog("execute", sql, () -> wrapped.execute(sql));
	}

	public int[] executeBatch() throws SQLException {
		return withLog("batch", null, () -> wrapped.executeBatch());
	}

	public final ResultSet executeQuery(String sql) throws SQLException {
		return withLog("query", sql, () -> new ResultSetWrapper(wrapped.executeQuery(sql), this));
	}

	public final int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		return withLog("update", sql, () -> wrapped.executeUpdate(sql, autoGeneratedKeys));
	}

	public final int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		return withLog("update", sql, () -> wrapped.executeUpdate(sql, columnIndexes));
	}

	public final int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		return withLog("update", sql, () -> wrapped.executeUpdate(sql, columnNames));
	}

	public final int executeUpdate(String sql) throws SQLException {
		return withLog("update", sql, () -> wrapped.executeUpdate(sql));
	}

	public final Connection getConnection() throws SQLException {
		return con;
	}

	public final int getFetchDirection() throws SQLException {
		return wrapped.getFetchDirection();
	}

	public final int getFetchSize() throws SQLException {
		return wrapped.getFetchSize();
	}

	public final ResultSet getGeneratedKeys() throws SQLException {
		return new ResultSetWrapper(wrapped.getGeneratedKeys(), this);
	}

	public final int getMaxFieldSize() throws SQLException {
		return wrapped.getMaxFieldSize();
	}

	public final int getMaxRows() throws SQLException {
		return wrapped.getMaxRows();
	}

	public final boolean getMoreResults() throws SQLException {
		return wrapped.getMoreResults();
	}

	public final boolean getMoreResults(int current) throws SQLException {
		return wrapped.getMoreResults(current);
	}

	public final int getQueryTimeout() throws SQLException {
		return wrapped.getQueryTimeout();
	}

	public final ResultSet getResultSet() throws SQLException {
		return new ResultSetWrapper(wrapped.getResultSet(), this);
	}

	public final int getResultSetConcurrency() throws SQLException {
		return wrapped.getResultSetConcurrency();
	}

	public final int getResultSetHoldability() throws SQLException {
		return wrapped.getResultSetHoldability();
	}

	public final int getResultSetType() throws SQLException {
		return wrapped.getResultSetType();
	}

	public final int getUpdateCount() throws SQLException {
		return wrapped.getUpdateCount();
	}

	public final SQLWarning getWarnings() throws SQLException {
		return wrapped.getWarnings();
	}

	public final boolean isClosed() throws SQLException {
		return wrapped.isClosed();
	}

	public final boolean isPoolable() throws SQLException {
		return wrapped.isPoolable();
	}

	public final boolean isWrapperFor(Class<?> iface) throws SQLException {
		return wrapped.isWrapperFor(iface);
	}

	public final void setCursorName(String name) throws SQLException {
		wrapped.setCursorName(name);
	}

	public final void setEscapeProcessing(boolean enable) throws SQLException {
		wrapped.setEscapeProcessing(enable);
	}

	public final void setFetchDirection(int direction) throws SQLException {
		wrapped.setFetchDirection(direction);
	}

	public final void setFetchSize(int rows) throws SQLException {
		wrapped.setFetchSize(rows);
	}

	public final void setMaxFieldSize(int max) throws SQLException {
		wrapped.setMaxFieldSize(max);
	}

	public final void setMaxRows(int max) throws SQLException {
		wrapped.setMaxRows(max);
	}

	public final void setPoolable(boolean poolable) throws SQLException {
		wrapped.setPoolable(poolable);
	}

	public final void setQueryTimeout(int seconds) throws SQLException {
		wrapped.setQueryTimeout(seconds);
	}

	public final <T> T unwrap(Class<T> iface) throws SQLException {
		return wrapped.unwrap(iface);
	}

	//for JDBC 4.1
	public void closeOnCompletion() throws SQLException {
		wrapped.closeOnCompletion();
	}

	public boolean isCloseOnCompletion() throws SQLException {
		return wrapped.isCloseOnCompletion();
	}

}
