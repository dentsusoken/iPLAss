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

package org.iplass.mtp.impl.rdb.sqlserver;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.iplass.mtp.entity.query.GroupBy.RollType;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.value.aggregate.Aggregate;
import org.iplass.mtp.entity.query.value.aggregate.StdDevPop;
import org.iplass.mtp.entity.query.value.aggregate.StdDevSamp;
import org.iplass.mtp.entity.query.value.aggregate.VarPop;
import org.iplass.mtp.entity.query.value.aggregate.VarSamp;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.rdb.adapter.HintPlace;
import org.iplass.mtp.impl.rdb.adapter.MultiInsertContext;
import org.iplass.mtp.impl.rdb.adapter.MultiTableUpdateMethod;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UnsupportedDataTypeException;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkUpdateContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.OrOperatorBulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.PreparedBulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.PreparedBulkUpdateContext;
import org.iplass.mtp.impl.rdb.adapter.function.DynamicTypedFunctionAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.StaticTypedFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentDateFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentDateTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.LocalTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.function.SqlServerDateAddFunctionAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.function.SqlServerDateDiffFunctionAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.function.SqlServerExtractDateFunctionAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.function.SqlServerInstrFunctionAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.function.SqlServerModFunctionAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.function.SqlServerRoundFunctionAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.function.SqlServerSubstrFunctionAdapter;
import org.iplass.mtp.impl.rdb.sqlserver.function.SqlServerTruncateFunctionAdapter;
import org.iplass.mtp.spi.ServiceRegistry;

public class SqlServerRdbAdapter extends RdbAdapter {

	private static final String[] optimizerHintBracket = {"OPTION (", ")"};

	private int lockTimeout = 0;
	private String timestampFunction = "GETDATE()";
	private String addMonthsFunction = "DATEADD";
	private String monthsBetweenFunction = "DATEDIFF";
	private boolean isUseSubQueryForIndexJoin = true;

	private String optimizerHint = "FAST 100";

	private boolean enableBindHint = false;
	private boolean alwaysBind = false;
	private int batchSize = 100;
	private int thresholdCountOfUsePrepareStatement = 50;
	private int maxFetchSize = 100;
	private int defaultQueryTimeout;
	private int defaultFetchSize;
	private Map<String, String> timeZoneMap;

	private String viewSubQueryAlias = "vsq";
	private int maxViewNameLength = 128;

	private static final String DATE_MIN = "17530101000000000";
	private static final String DATE_MAX = "99991231235959999";
	long dateMin;
	long dateMax;

	public SqlServerRdbAdapter() {
		addFunction(new StaticTypedFunctionAdapter("CHAR_LENGTH", "LEN", Long.class));
		addFunction(new SqlServerInstrFunctionAdapter("INSTR"));
		addFunction(new StaticTypedFunctionAdapter("CONCAT", String.class));//2012以降ならあるはず
		addFunction(new SqlServerSubstrFunctionAdapter("SUBSTR"));
		addFunction(new StaticTypedFunctionAdapter("REPLACE", "REPLACE", String.class));
		addFunction(new SqlServerModFunctionAdapter("MOD"));//dividend % divisor 作成してみた。
		addFunction(new StaticTypedFunctionAdapter("SQRT", Double.class));
		addFunction(new DynamicTypedFunctionAdapter("POWER", new int[]{0,1}));
		addFunction(new DynamicTypedFunctionAdapter("ABS", new int[]{0}));
		addFunction(new StaticTypedFunctionAdapter("CEIL", "CEILING", Long.class));
		addFunction(new StaticTypedFunctionAdapter("FLOOR", Long.class));
		addFunction(new SqlServerRoundFunctionAdapter("ROUND"));
//		addFunction(new RoundTruncFunctionAdapter("TRUNCATE", "ROUND"));	// 引数を2個目を0以外で渡す必要がある。
//		addFunction(new RoundTruncFunctionAdapter("TRUNCATE", "FLOOR"));	// 数値のみ。Oracleみたいに日付を指定することは出来ない。2個めの引数を指定してエラーとなるためダメ。
		addFunction(new SqlServerTruncateFunctionAdapter("TRUNCATE"));
		addFunction(new StaticTypedFunctionAdapter("UPPER", String.class));
		addFunction(new StaticTypedFunctionAdapter("LOWER", String.class));
		addFunction(new SqlServerExtractDateFunctionAdapter("SECOND"));
		addFunction(new SqlServerExtractDateFunctionAdapter("MINUTE"));
		addFunction(new SqlServerExtractDateFunctionAdapter("HOUR"));
		addFunction(new SqlServerExtractDateFunctionAdapter("DAY"));
		addFunction(new SqlServerExtractDateFunctionAdapter("MONTH"));
		addFunction(new SqlServerExtractDateFunctionAdapter("YEAR"));
		addFunction(new SqlServerDateAddFunctionAdapter());//DATE_ADD
		addFunction(new SqlServerDateDiffFunctionAdapter());//DATE_DIFFD
		addFunction(new CurrentDateFunctionAdapter());
		addFunction(new CurrentTimeFunctionAdapter());
		addFunction(new CurrentDateTimeFunctionAdapter());
		addFunction(new LocalTimeFunctionAdapter());

		I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sdf.setTimeZone(i18n.getTimezone());

		try {
			dateMin = sdf.parse(DATE_MIN).getTime();
			dateMax = sdf.parse(DATE_MAX).getTime();
		} catch (ParseException e) {
			throw new UnsupportedDataTypeException(e);
		}
	}

	@Override
	public int getDefaultQueryTimeout() {
		return defaultQueryTimeout;
	}

	public void setDefaultQueryTimeout(int defaultQueryTimeout) {
		this.defaultQueryTimeout = defaultQueryTimeout;
	}

	@Override
	public int getDefaultFetchSize() {
		return defaultFetchSize;
	}

	public void setDefaultFetchSize(int defaultFetchSize) {
		this.defaultFetchSize = defaultFetchSize;
	}

	@Override
	public int getMaxFetchSize() {
		return maxFetchSize;
	}

	public void setMaxFetchSize(int maxFetchSize) {
		this.maxFetchSize = maxFetchSize;
	}

	@Override
	public HintPlace getOptimizerHintPlace() {
		return HintPlace.AFTER_SELECT;
	}

	@Override
	public String[] getOptimizerHintBracket() {
		return optimizerHintBracket;
	}

	@Override
	public String getOptimizerHint() {
		return optimizerHint;
	}

	public void setOptimizerHint(String optimizerHint) {
		this.optimizerHint = optimizerHint;
	}

	public String getAddMonthsFunction() {
		return addMonthsFunction;
	}

	public void setAddMonthsFunction(String addMonthsFunction) {
		this.addMonthsFunction = addMonthsFunction;
	}

	public String getMonthsBetweenFunction() {
		return monthsBetweenFunction;
	}

	public void setMonthsBetweenFunction(String monthsBetweenFunction) {
		this.monthsBetweenFunction = monthsBetweenFunction;
	}

	public String getTimestampFunction() {
		return timestampFunction;
	}

	public void setTimestampFunction(String timestampFunction) {
		this.timestampFunction = timestampFunction;
	}

	public void setLockTimeout(int lockTimeout) {
		this.lockTimeout = lockTimeout;
	}

	public int getLockTimeout() {
		return lockTimeout;
	}

	private void checkDateRange(java.util.Date date) {
		if (date != null) {
			long time = date.getTime();
			if (dateMin > time || dateMax < time) {
				throw new UnsupportedDataTypeException("out of range Date:" + date);
			}
		}
	}

	public String toDateExpression(Date date) {
		checkDateRange(date);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		return "CONVERT(DATE, '" + fmt.format(date) + "')";
	}

	public String toTimeExpression(Time time) {
		checkDateRange(time);
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		return "CONVERT(DATETIME2, '1970-01-01 " + fmt.format(time) + "')";
	}

	public String toTimeStampExpression(Timestamp date) {
		checkDateRange(date);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (rdbTimeZone() != null) {
			fmt.setTimeZone(rdbTimeZone());
		}
		return "CONVERT(DATETIME2, '" + fmt.format(date) + "')";
	}

	public String systimestamp() {
		return timestampFunction;
	}

	//テーブル構成に併せる
	private static final String[] CAST_VARCHAR = {"CAST(", " AS NVARCHAR(4000))"};
	private static final String[] CAST_BIGINT = {"CAST(", " AS BIGINT)"};
	private static final String[] CAST_DECIMAL = {"CAST(", " AS NUMERIC(38))"};
	private static final String[] CAST_DATE = {"CAST(CAST(", " AS DATETIME2) AS DATE)"};
	private static final String[] CAST_DOUBLE = {"CAST(", " AS FLOAT)"};
	private static final String[] CAST_TIME = {"CONVERT(DATETIME2, CONCAT('1970-01-01 ',FORMAT(CAST(", " AS DATETIME2),'HH:mm:ss')), 20)"};
	private static final String[] CAST_TIMESTAMP = {"CAST(", " AS DATETIME2)"};

	@Override
	public String[] castExp(int sqlType, Integer lengthOrPrecision, Integer scale) {
		switch (sqlType) {
		case Types.VARCHAR:
			if (lengthOrPrecision != null) {
				return new String[]{"CAST(", " AS NVARCHAR(" + lengthOrPrecision + "))"};
			} else {
				return CAST_VARCHAR;
			}
		case Types.BIGINT:
			return CAST_BIGINT;
		case Types.DECIMAL:
			if (lengthOrPrecision == null) {
				if (scale == null) {
					return CAST_DECIMAL;
				} else {
					if (scale < 0) {
						//MySQLは、負のスケールを設定できないため、取り得る最大桁数を設定し、ROUND関数で切り捨てる。
						return new String[]{"ROUND(CAST(",  " AS NUMERIC(38))," + scale + ")"};
					} else {
						return new String[]{"CAST(", " AS NUMERIC(38," + scale + "))"};
					}
				}
			} else {
				if (scale == null) {
					return new String[]{"CAST(", " AS NUMERIC(" + lengthOrPrecision + ",0))"};
				} else {
					if (scale < 0) {
						//MySQLは、負のスケールを設定できないため、取り得る最大桁数を設定し、ROUND関数で切り捨てる。
						return new String[]{"ROUND(CAST(",  " AS NUMERIC(" + lengthOrPrecision + -scale + ",0))," + scale + ")"};
					} else {
						return new String[]{"CAST(",  " AS NUMERIC(" + lengthOrPrecision + "," + scale + "))"};
					}
				}
			}
		case Types.DATE:
			return CAST_DATE;
		case Types.DOUBLE:
			return CAST_DOUBLE;
		case Types.TIME:
			return CAST_TIME;
		case Types.TIMESTAMP:
			return CAST_TIMESTAMP;
		default:
			return null;
		}
	}

	@Override
	public CharSequence cast(int fromSqlType, int toSqlType, CharSequence valExpr, Integer lengthOrPrecision, Integer scale) {
		//-のscale指定ができないので、ROUNDで対応
		if (toSqlType == Types.DECIMAL && scale != null && scale < 0) {
			CharSequence castString = super.cast(fromSqlType, toSqlType, valExpr, lengthOrPrecision, 0);
			return "ROUND(" + castString + "," + scale + ")";
		}

		return super.cast(fromSqlType, toSqlType, valExpr, lengthOrPrecision, scale);
	}

	public MultiInsertContext createMultiInsertContext(Statement stmt) {
		return new SqlServerMultiInsertContext(stmt);
	}

	@Override
	public String getDataTypeOf(int sqlType, Integer lengthOrPrecision, Integer scale) {
		switch (sqlType) {
		case Types.VARCHAR:
			if (lengthOrPrecision == null) {
				return "NVARCHAR(4000)";
			} else {
				return "NVARCHAR(" + lengthOrPrecision + ")";
			}
		case Types.BIGINT:
			return "BIGINT";
		case Types.DECIMAL:
			if (lengthOrPrecision == null) {
				if (scale == null) {
					return "NUMERIC(38)";
				} else {
					return "NUMERIC(38," + scale + ")";
				}
			} else {
				if (scale == null) {
					return "NUMERIC(" + lengthOrPrecision + ",0)";
				} else {
					return "NUMERIC(" + lengthOrPrecision + "," + scale + ")";
				}
			}
		case Types.DATE:
			return "DATE";
		case Types.DOUBLE:
			return "FLOAT";
		case Types.TIME:
		case Types.TIMESTAMP:
			return "DATETIME2";
		default:
			return null;
		}
	}

	@Override
	public String dual() {
		return "";
	}

	@Override
	public String rowLockExpression() {
		if (lockTimeout == 0) {
			return "WITH (UPDLOCK,NOWAIT)";
		} else {
			return "WITH (UPDLOCK)";
		}
	}

	@Override
	public String toLimitSql(String selectSql, int limitCount, int offset, boolean asBind) {
		if (asBind) {
			StringBuilder sb = new StringBuilder();
			sb.append(selectSql);
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(selectSql);
			sb.append(" OFFSET ");
			sb.append(offset);
			sb.append(" ROWS FETCH FIRST ");
			sb.append(limitCount);
			sb.append(" ROWS ONLY");
			return sb.toString();
		}
	}

	@Override
	public Object[] toLimitSqlBindValue(int limitCount,
			int offset) {
		return new Integer[]{
				Integer.valueOf(offset), Integer.valueOf(limitCount)};
	}

	@Override
	public boolean isDuplicateValueException(SQLException e) {
		if (e.getErrorCode() == 2627) {
			return true;
		} else {
			if (e instanceof BatchUpdateException) {
				// FIXME 判別方法が不明
				return true;
			}
			return false;
		}
	}

	@Override
	public boolean isDeadLock(SQLException e) {
		return e.getErrorCode() == 1205;
	}

	@Override
	public boolean isLockFailed(SQLException e) {
		if (e.getErrorCode() == 1222) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isCastFailed(SQLException e) {
		return e.getErrorCode() == 8114;
	}

	@Override
	public String addDate(String dateExpression, int day) {
		return "DATEADD(day," + day + "," + dateExpression + ")";
	}

	@Override
	public String checkStatusQuery() {
		return "SELECT 1";
	}

	@Override
	public String escape() {
		return "ESCAPE '\\'";
	}

	@Override
	public String tableAlias(String selectSql) {
		return selectSql;
	}

	@Override
	public boolean isSupportGroupingExtention(RollType rollType) {
		return true;
	}

	@Override
	public String rollUpStart(RollType rollType) {
		return "";
	}

	@Override
	public String rollUpEnd(RollType rollType) {
		switch (rollType) {
		case ROLLUP:
			return " WITH ROLLUP ";
		case CUBE:
			return " WITH CUBE ";
		default:
			return " ";
		}
	}

	@Override
	public String seqNextSelectSql(String sequenceName, int tenantId, String entityDefId) {
		return "SELECT NEXT VALUE FOR " + sequenceName;
	}

	@Override
	public String likePattern(String str) {
        return str;
	}

	@Override
	public String initBlob() {
		return "0x";
	}

	@Override
	public boolean isUseSubQueryForIndexJoin() {
		return isUseSubQueryForIndexJoin;
	}

	public void setUseSubQueryForIndexJoin(boolean isUseSubQueryForIndexJoin) {
		this.isUseSubQueryForIndexJoin = isUseSubQueryForIndexJoin;
	}

	@Override
	public void appendSortSpecExpression(StringBuilder sb, CharSequence sortValue, SortType sortType,
			NullOrderingSpec nullOrderingSpec) {
		if (nullOrderingSpec != null) {
			switch (nullOrderingSpec) {
			case FIRST:
				sb.append("CASE WHEN ");
				sb.append(sortValue);
				sb.append(" IS NULL THEN 0 ELSE 1 END ASC, ");
				break;
			case LAST:
				sb.append("CASE WHEN ");
				sb.append(sortValue);
				sb.append(" IS NULL THEN 0 ELSE 1 END DESC, ");
				break;
			default:
				break;
			}
		}
		sb.append(sortValue);	// FIXME サブクエリにOrder By指定するとエラー ← でもサブクエリかどうかなんてここでは判断できない
		if (sortType != null) {
			switch (sortType) {
			case ASC:
				sb.append(" ASC");
				break;
			case DESC:
				sb.append(" DESC");
				break;
			default:
				break;
			}
		}
	}

	@Override
	public String[] convertTZ(String to) {
		// 「AT TIME ZONE」SQL Server 2016からサポート

		if (rdbTimeZone() == null) {
			//CURRENT_TIMEZONE()は、SQL Serverでは未サポートなので、rdbTimeZone指定されている前提で
			String[] ret = {"", ""};
			return ret;
		} else {
			return new String[] {
					"",
					" AT TIME ZONE '" + maptz(rdbTimeZone().getID()) + "' AT TIME ZONE '" + maptz(to) + "'"
					};
		}
	}
	
	private String maptz(String timeZone) {
		if (timeZoneMap == null) {
			return timeZone;
		}
		String mapped = timeZoneMap.get(timeZone);
		if (mapped == null) {
			return timeZone;
		} else {
			return mapped;
		}
	}

	@Override
	public boolean isEnableInPartitioning() {
		return false;
	}

	@Override
	public int getInPartitioningSize() {
		return -1;
	}

	@Override
	public String deleteTemporaryTable(String tableName) {
		String tempTableName = getTemplaryTablePrefix() + tableName;
		return "IF OBJECT_ID('tempdb.dbo." + tempTableName + "', 'U') IS NOT NULL DROP TABLE " + tempTableName;
	}

	@Override
	public boolean isSupportGlobalTemporaryTable() {
		return false;
	}

	@Override
	public String createLocalTemporaryTable(String tableName,
			String baseTableName, String[] baseColumnName) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		for (int i = 0; i < baseColumnName.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(baseColumnName[i]);
		}
		sb.append(" INTO ");
		sb.append(getTemplaryTablePrefix());
		sb.append(tableName);
		sb.append(" FROM ");
		sb.append(baseTableName);
		sb.append(" WHERE 1=2");	// レコードを作らないため
		return sb.toString();
	}

	@Override
	public String getTemplaryTablePrefix() {
		return "#";
	}

	@Override
	public boolean isSupportAutoClearTemporaryTableWhenCommit() {
		// MYSQLと同じ
		return false;
	}

	@Override
	public boolean isSupportGroupingExtentionWithOrderBy() {
		return true;
	}

	@Override
	public boolean isSupportGroupingExtention() {
		return true;
	}

	@Override
	public boolean isEnableBindHint() {
		return enableBindHint;
	}

	public void setEnableBindHint(boolean enableBindHint) {
		this.enableBindHint = enableBindHint;
	}

	@Override
	public boolean isAlwaysBind() {
		return alwaysBind;
	}

	public void setAlwaysBind(boolean alwaysBind) {
		this.alwaysBind = alwaysBind;
	}

	@Override
	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	@Override
	public int getThresholdCountOfUsePrepareStatement() {
		return thresholdCountOfUsePrepareStatement;
	}

	@Override
	public String aggregateFunctionName(Aggregate agg) {
		if (agg instanceof StdDevPop) {
			return "STDEVP";
		}
		if (agg instanceof StdDevSamp) {
			return "STDEV";
		}
		if (agg instanceof VarPop) {
			return "VARP";
		}
		if (agg instanceof VarSamp) {
			return "VAR";
		}
		return super.aggregateFunctionName(agg);
	}

	public void setThresholdCountOfUsePrepareStatement(
			int thresholdCountOfUsePrepareStatement) {
		this.thresholdCountOfUsePrepareStatement = thresholdCountOfUsePrepareStatement;
	}

	@Override
	public BulkInsertContext createBulkInsertContext() {
		return new PreparedBulkInsertContext();
	}

	@Override
	public BulkUpdateContext createBulkUpdateContext() {
		return new PreparedBulkUpdateContext();
	}

	@Override
	public BulkDeleteContext createBulkDeleteContext() {
		return new OrOperatorBulkDeleteContext();
	}

	@Override
	public MultiTableUpdateMethod getMultiTableUpdateMethod() {
		return MultiTableUpdateMethod.NO_SUPPORT;
	}

	@Override
	public ResultSet getTableNames(String tableNamePattern, Connection con) throws SQLException {
		DatabaseMetaData dbMeta = con.getMetaData();
		return dbMeta.getTables(null, dbMeta.getUserName().toUpperCase(), tableNamePattern, null);
	}

	@Override
	public boolean isSupportWindowFunction() {
		return true;
	}

	@Override
	public boolean isSupportOptimizerHint() {
		// FIXME サブクエリに付かないようにする対応が必要
		return false;
	}

	@Override
	public boolean isSupportTableHint() {
		return false;
	}

	@Override
	public String[] getTableHintBracket() {
		return null;
	}

	@Override
	public String createRowLockSql(String sql) {
		StringBuilder lockSql = new StringBuilder();

		if (lockTimeout != 0) {
			lockSql.append("SET LOCK_TIMEOUT " + lockTimeout + " ");
		}

		// FIXME Select句やFrom句にWhere句が存在するサブクエリがあったらダメ

		int idxWhere = sql.toUpperCase().indexOf("WHERE");
		if (idxWhere > 0) {
			// Where句あり
			lockSql.append(sql.substring(0, idxWhere - 1) + " " + rowLockExpression() + " " + sql.substring(idxWhere, sql.length()));
		} else if (idxWhere < 0){
			// Where句なし(エラーにした方がよい？)
			lockSql.append(sql + " " + rowLockExpression());
		} else {
			// Where句から始まるありえないSQLだが一応先頭に付与する
			lockSql.append(rowLockExpression() + " " + sql);
		}

		return lockSql.toString();
	}

	@Override
	public boolean isSupportRowValueConstructor() {
		return false;
	}

	@Override
	public boolean isNeedFromClauseTableAliasUpdateStatement() {
		return true;
	}

	@Override
	public String getDefaultOrderByForLimit() {
		return " ORDER BY (SELECT NULL) ";
	}
	
	public Map<String, String> getTimeZoneMap() {
		return timeZoneMap;
	}

	public void setTimeZoneMap(Map<String, String> timeZoneMap) {
		this.timeZoneMap = timeZoneMap;
	}

	@Override
	public String getViewSubQueryAlias() {
		return "AS " + viewSubQueryAlias;
	}

	public void setViewSubQueryAlias(String alias) {
		viewSubQueryAlias = alias;
	}

	@Override
	public int getMaxViewNameLength() {
		return maxViewNameLength;
	}

	public void setMaxViewNameLength(int length) {
		this.maxViewNameLength = length;
	}

	@Override
	public String createViewColumnSql(int colNo, String colName) {
		return String.format("c%d AS %s", colNo, colName);
	}

	@Override
	public String createBinaryViewColumnSql(int colNo, String colName, String lobIdSuffix) {
		return String.format("SUBSTRING(LEFT(c%d, CHARINDEX(',', c%d) - 1), 7, LEN(c%d)) AS %s%s",
				colNo, colNo, colNo, colName, lobIdSuffix);
	}

	@Override
	public String createLongTextViewColumnSql(int colNo, String colName, String lobIdSuffix) {
		StringBuilder sb = new StringBuilder();

		// LobID
		sb.append(String.format("TRIM(SUBSTRING(c%d, 3, 16)) AS %s%s", colNo, colName, lobIdSuffix)).append(",");
		// Text
		sb.append(String.format("RIGHT(c%d, LEN(c%d) - 21) AS %s", colNo, colNo, colName));

		return sb.toString();
	}

	@Override
	public String toCreateViewDDL(String viewName, String selectSql, boolean withDropView) {
		StringBuilder sb = new StringBuilder();

		String lf = System.lineSeparator();

		// ビュー削除DDL
		if (withDropView) {
			sb.append("DROP VIEW ").append(viewName).append(lf);
			sb.append("GO").append(lf).append(lf);
		}

		// ビュー作成DDL
		sb.append("CREATE VIEW ").append(viewName).append(" AS").append(lf);
		sb.append(selectSql).append(lf);
		sb.append("GO").append(lf).append(lf);

		return sb.toString();
	}
}
