/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.logstash.logback.argument.StructuredArguments;
import net.logstash.logback.marker.Markers;

public class PreparedStatementWrapper extends StatementWrapper implements
		PreparedStatement {

	private static Logger logger = LoggerFactory.getLogger(PreparedStatementWrapper.class);
	private PreparedStatement wrapped;
	@SuppressWarnings("unused")
	private ConnectionWrapper con;

	private String sql;
	private List<Object> values;
	private List<Integer> indexs;

	PreparedStatementWrapper(PreparedStatement wrapped,
			ConnectionWrapper con, String sql, int warnLogThreshold, boolean warnLogBefore, boolean countSqlExecution) {
		super(wrapped, con, warnLogThreshold, warnLogBefore, countSqlExecution);
		this.wrapped = wrapped;
		this.con = con;
		this.sql = sql;
		values = new ArrayList<Object>();
		indexs = new ArrayList<Integer>();
	}

	protected <T> T withLog(String method, String sql, boolean withParam, SQLExecution<T> s) throws SQLException {
		
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
				logger.warn(Markers.append("warning_type", "alert"), withWarnLogFormat(method, sql, null, withParam), logParam(method, sql, null, withParam));
			}
			
			return s.run();
			
		} finally {
			long queryTime = System.currentTimeMillis() - start;
			if (warnLogThreshold > 0  && queryTime > warnLogThreshold) {
				if (logger.isWarnEnabled()) {
					logger.warn(Markers.append("warning_type", "time"), withWarnLogFormat(method, sql, queryTime, withParam), logParam(method, sql, queryTime, withParam));
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug(Markers.append("warning_type", "time"), withWarnLogFormat(method, sql, queryTime, withParam), logParam(method, sql, queryTime, withParam));
				}
			}
		}
	}

	private Object[] logParam(String method, String sql, Long queryTime, boolean withParam) {
		Object[] logParam;
		int base;
		if (queryTime == null) {
			base = 2;
		} else {
			base = 3;
		}
		if (withParam) {
			base += 2;
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
		if (withParam) {
			logParam[i++] = StructuredArguments.value("sql_parameter_index", indexs);
			logParam[i++] = StructuredArguments.value("sql_parameter_values", values);
		}
		if (additionalWarnLogInfo != null) {
			additionalWarnLogInfo.setParameter(base, logParam);
		}
		return logParam;
	}
	
	private String withWarnLogFormat(String method, String sql, Long queryTime, boolean withParam) {
		String fmt;
		if (queryTime == null) {
			if (sql == null) {
				if (withParam) {
					fmt = "{} -- index= {} values= {}";
				} else {
					fmt = "{}";
				}
			} else {
				if (withParam) {
					fmt = "{} sql={} -- index= {} values= {}";
					
				} else {
					fmt = "{} sql={}";
				}
			}
		} else {
			if (sql == null) {
				if (withParam) {
					fmt = "{} time= {} ms. -- index= {} values= {}";
				} else {
					fmt = "{} time= {} ms.";
				}
			} else {
				if (withParam) {
					fmt = "{} time= {} ms. sql={} -- index= {} values= {}";
				} else {
					fmt = "{} time= {} ms. sql={}";
				}
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
	
	public void addBatch() throws SQLException {
		try {
			wrapped.addBatch();
			if (logger.isDebugEnabled()) {
				logger.debug(withWarnLogFormat("addBatch()", null, null, true), logParam("addBatch()", null, null, true));
			}
		} finally {
			values.clear();
			indexs.clear();
		}
	}

	public void clearParameters() throws SQLException {
		try {
			wrapped.clearParameters();
		} finally {
			values.clear();
			indexs.clear();
		}
	}

	public boolean execute() throws SQLException {
		try {
			return withLog("execute", sql, true, () -> wrapped.execute());
		} finally {
			values.clear();
			indexs.clear();
		}
	}


	public ResultSet executeQuery() throws SQLException {
		try {
			return withLog("query", sql, true, () -> new ResultSetWrapper(wrapped.executeQuery(), this));
		} finally {
			values.clear();
			indexs.clear();
		}
	}

	public int executeUpdate() throws SQLException {
		try {
			return withLog("update", sql, true, () -> wrapped.executeUpdate());
		} finally {
			values.clear();
			indexs.clear();
		}
	}
	
	public final int[] executeBatch() throws SQLException {
		return withLog("batch", sql, false, () -> wrapped.executeBatch());
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return wrapped.getMetaData();
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return wrapped.getParameterMetaData();
	}

	public void setArray(int parameterIndex, Array x) throws SQLException {
		wrapped.setArray(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		wrapped.setAsciiStream(parameterIndex, x, length);
		setValues(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		wrapped.setAsciiStream(parameterIndex, x, length);
		setValues(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		wrapped.setAsciiStream(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		wrapped.setBigDecimal(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		wrapped.setBinaryStream(parameterIndex, x, length);
		setValues(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		wrapped.setBinaryStream(parameterIndex, x, length);
		setValues(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		wrapped.setBinaryStream(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		wrapped.setBlob(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		wrapped.setBlob(parameterIndex, inputStream, length);
		setValues(parameterIndex, inputStream);
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		wrapped.setBlob(parameterIndex, inputStream);
		setValues(parameterIndex, inputStream);
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		wrapped.setBoolean(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		wrapped.setByte(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		wrapped.setBytes(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		wrapped.setCharacterStream(parameterIndex, reader, length);
		setValues(parameterIndex, reader);
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		wrapped.setCharacterStream(parameterIndex, reader, length);
		setValues(parameterIndex, reader);
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		wrapped.setCharacterStream(parameterIndex, reader);
		setValues(parameterIndex, reader);
	}

	public void setClob(int parameterIndex, Clob x) throws SQLException {
		wrapped.setClob(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		wrapped.setClob(parameterIndex, reader, length);
		setValues(parameterIndex, reader);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		wrapped.setClob(parameterIndex, reader);
		setValues(parameterIndex, reader);
	}

	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		//driverによってnullの場合の挙動が異なるので、ここで吸収。。
		if (cal == null) {
			wrapped.setDate(parameterIndex, x);
		} else {
			wrapped.setDate(parameterIndex, x, cal);
		}
		setValues(parameterIndex, x);
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		wrapped.setDate(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		wrapped.setDouble(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		wrapped.setFloat(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		wrapped.setInt(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		wrapped.setLong(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		wrapped.setNCharacterStream(parameterIndex, value, length);
		setValues(parameterIndex, value);
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		wrapped.setNCharacterStream(parameterIndex, value);
		setValues(parameterIndex, value);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		wrapped.setNClob(parameterIndex, value);
		setValues(parameterIndex, value);
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		wrapped.setNClob(parameterIndex, reader, length);
		setValues(parameterIndex, reader);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		wrapped.setNClob(parameterIndex, reader);
		setValues(parameterIndex, reader);
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		wrapped.setNString(parameterIndex, value);
		setValues(parameterIndex, value);
	}

	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		wrapped.setNull(parameterIndex, sqlType, typeName);
		setValues(parameterIndex, typeName + ":" + sqlType);
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		wrapped.setNull(parameterIndex, sqlType);
		setValues(parameterIndex, sqlType);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		wrapped.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
		setValues(parameterIndex, x);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		wrapped.setObject(parameterIndex, x, targetSqlType);
		setValues(parameterIndex, x);
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		wrapped.setObject(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setRef(int parameterIndex, Ref x) throws SQLException {
		wrapped.setRef(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		wrapped.setRowId(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		wrapped.setShort(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		wrapped.setSQLXML(parameterIndex, xmlObject);
		setValues(parameterIndex, xmlObject);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		wrapped.setString(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		//driverによってnullの場合の挙動が異なるので、ここで吸収。。
		if (cal == null) {
			wrapped.setTime(parameterIndex, x);
		} else {
			wrapped.setTime(parameterIndex, x, cal);
		}
		setValues(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		wrapped.setTime(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		//driverによってnullの場合の挙動が異なるので、ここで吸収。。
		if (cal == null) {
			wrapped.setTimestamp(parameterIndex, x);
		} else {
			wrapped.setTimestamp(parameterIndex, x, cal);
		}
		setValues(parameterIndex, x);
	}

	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		wrapped.setTimestamp(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	@SuppressWarnings("deprecation")
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		wrapped.setUnicodeStream(parameterIndex, x, length);
		setValues(parameterIndex, x);
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		wrapped.setURL(parameterIndex, x);
		setValues(parameterIndex, x);
	}

	private final void setValues(int index, Object value) {
		values.add(value);
		indexs.add(index);
	}
}
