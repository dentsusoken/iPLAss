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

package org.iplass.mtp.impl.rdb.mysql;

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
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.iplass.mtp.entity.query.GroupBy.RollType;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.NativeHint;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.aggregate.Avg;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.entity.query.value.aggregate.Listagg;
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.aggregate.Min;
import org.iplass.mtp.entity.query.value.aggregate.StdDevPop;
import org.iplass.mtp.entity.query.value.aggregate.StdDevSamp;
import org.iplass.mtp.entity.query.value.aggregate.Sum;
import org.iplass.mtp.entity.query.value.aggregate.VarPop;
import org.iplass.mtp.entity.query.value.aggregate.VarSamp;
import org.iplass.mtp.impl.datastore.grdb.sql.table.ObjStoreTable;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.rdb.adapter.HintPlace;
import org.iplass.mtp.impl.rdb.adapter.MultiInsertContext;
import org.iplass.mtp.impl.rdb.adapter.MultiTableUpdateMethod;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UnsupportedDataTypeException;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkUpdateContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.InOperatorBulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.PreparedBulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.PreparedBulkUpdateContext;
import org.iplass.mtp.impl.rdb.adapter.function.AggregateFunctionAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.DynamicTypedFunctionAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.StaticTypedFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CountFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentDateFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentDateTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.ExtractDateFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.LocalTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.RoundTruncFunctionAdapter;
import org.iplass.mtp.impl.rdb.connection.ConnectionException;
import org.iplass.mtp.impl.rdb.connection.ConnectionFactory;
import org.iplass.mtp.impl.rdb.mysql.function.MySqlListaggFunctionAdapter;
import org.iplass.mtp.impl.rdb.mysql.function.MysqlDateAddFunctionAdapter;
import org.iplass.mtp.impl.rdb.mysql.function.MysqlDateDiffFunctionAdapter;
import org.iplass.mtp.impl.rdb.mysql.function.MysqlTruncateFunctionAdapter;
import org.iplass.mtp.spi.ServiceRegistry;

public class MysqlRdbAdaptor extends RdbAdapter {
//	private static final long DATE_MIN = -30609824400000L;//1000-01-01 00:00:00.000
//	private static final long DATE_MAX = 253402268399999L;//9999-12-31 23:59:59.999

	private static final String DATE_MIN = "10000101000000000";
	private static final String DATE_MAX = "99991231235959999";
	private static final String[] optimizerHintBracket = {"/*+", "*/"};

	private boolean optimizeCountQuery;
	private boolean useFractionalSecondsOnTimestamp = true;
	private boolean supportOptimizerHint = false;
	private boolean supportWindowFunction = false;
	private boolean needMultiTableTrick;
	private String multiTableTrickClauseForUpdate = ", (SELECT 1) dummy";

	private boolean localTemporaryTableManageOutsideTransaction = false;
	private boolean localTemporaryTableCreatedByDataSource = false;

	//変換前、タイムゾーン
	private String timestampMethod = "NOW(3)";
	private boolean enableBindHint;
	private int batchSize = 100;
	private int thresholdCountOfUsePrepareStatement = -1;
	private int maxFetchSize = 100;
	private int defaultQueryTimeout;
	private int defaultFetchSize;

	private String viewSubQueryAlias = "vsq";
	private int maxViewNameLength = 64;

	long dateMin;
	long dateMax;
	public MysqlRdbAdaptor() {
		addFunction(new StaticTypedFunctionAdapter("CHAR_LENGTH", Long.class));
		addFunction(new StaticTypedFunctionAdapter("INSTR", Long.class));
		addFunction(new StaticTypedFunctionAdapter("CONCAT", String.class));
		addFunction(new StaticTypedFunctionAdapter("SUBSTR", String.class));
		addFunction(new StaticTypedFunctionAdapter("REPLACE", String.class));
		addFunction(new DynamicTypedFunctionAdapter("MOD", new int[]{0,1}));
		addFunction(new StaticTypedFunctionAdapter("SQRT", Double.class));
		addFunction(new DynamicTypedFunctionAdapter("POWER", new int[]{0,1}));
		addFunction(new DynamicTypedFunctionAdapter("ABS", new int[]{0}));
		addFunction(new StaticTypedFunctionAdapter("CEIL", Long.class));
		addFunction(new StaticTypedFunctionAdapter("FLOOR", Long.class));
		addFunction(new RoundTruncFunctionAdapter("ROUND", "ROUND"));
		addFunction(new MysqlTruncateFunctionAdapter("TRUNCATE"));
		addFunction(new StaticTypedFunctionAdapter("UPPER", String.class));
		addFunction(new StaticTypedFunctionAdapter("LOWER", String.class));
		addFunction(new ExtractDateFunctionAdapter("SECOND"));
		addFunction(new ExtractDateFunctionAdapter("MINUTE"));
		addFunction(new ExtractDateFunctionAdapter("HOUR"));
		addFunction(new ExtractDateFunctionAdapter("DAY"));
		addFunction(new ExtractDateFunctionAdapter("MONTH"));
		addFunction(new ExtractDateFunctionAdapter("YEAR"));
		addFunction(new MysqlDateAddFunctionAdapter());//DATE_ADD
		addFunction(new MysqlDateDiffFunctionAdapter());//DATE_DIFF
		addFunction(new CurrentDateFunctionAdapter());
		addFunction(new CurrentTimeFunctionAdapter());
		addFunction(new CurrentDateTimeFunctionAdapter());
		addFunction(new LocalTimeFunctionAdapter());

		addAggregateFunction(Count.class, new CountFunctionAdapter());
		addAggregateFunction(Sum.class, new AggregateFunctionAdapter<Sum>("SUM", null));
		addAggregateFunction(Avg.class, new AggregateFunctionAdapter<Avg>("AVG", Double.class));
		addAggregateFunction(Max.class, new AggregateFunctionAdapter<Max>("MAX", null));
		addAggregateFunction(Min.class, new AggregateFunctionAdapter<Min>("MIN", null));
		addAggregateFunction(StdDevPop.class, new AggregateFunctionAdapter<StdDevPop>("STDDEV_POP", Double.class));
		addAggregateFunction(StdDevSamp.class, new AggregateFunctionAdapter<StdDevSamp>("STDDEV_SAMP", Double.class));
		addAggregateFunction(VarPop.class, new AggregateFunctionAdapter<VarPop>("VAR_POP", Double.class));
		addAggregateFunction(VarSamp.class, new AggregateFunctionAdapter<VarSamp>("VAR_SAMP", Double.class));
		addAggregateFunction(Listagg.class, new MySqlListaggFunctionAdapter());

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
	public Connection getConnection(String connectionFactoryName) throws SQLException {
		ConnectionFactory cf = null;
		if (connectionFactoryName == null) {
			cf =  ServiceRegistry.getRegistry().getService(ConnectionFactory.class);
		} else {
			cf = ServiceRegistry.getRegistry().<ConnectionFactory>getService(connectionFactoryName);
		}

		if (localTemporaryTableManageOutsideTransaction && !localTemporaryTableCreatedByDataSource) {
			return cf.getConnection(con -> {
				try {
					boolean isAutoCommit = con.getAutoCommit();

					if (!isAutoCommit) {
						con.setAutoCommit(true);
					}

					try (Statement stmt = con.createStatement()) {
						stmt.executeUpdate(createLocalTemporaryTableInternal(
							ObjStoreTable.TABLE_NAME_TMP, ObjStoreTable.TABLE_NAME, new String[]{ObjStoreTable.OBJ_ID, ObjStoreTable.OBJ_VER}));
					}

					con.setAutoCommit(isAutoCommit);

					return con;
				} catch (SQLException e) {
					throw new ConnectionException("Exeception occured in the handler that after get physical connection.", e);
				}
			});
		}

		return cf.getConnection();
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

	public boolean isUseFractionalSecondsOnTimestamp() {
		return useFractionalSecondsOnTimestamp;
	}

	public void setUseFractionalSecondsOnTimestamp(
			boolean useFractionalSecondsOnTimestamp) {
		this.useFractionalSecondsOnTimestamp = useFractionalSecondsOnTimestamp;
	}

	/**
	 * timestampMethodを取得します。
	 * @return timestampMethod
	 */
	public String getTimestampMethod() {
	    return timestampMethod;
	}

	/**
	 * timestampMethodを設定します。
	 * @param timestampMethod timestampMethod
	 */
	public void setTimestampMethod(String timestampMethod) {
	    this.timestampMethod = timestampMethod;
	}

	@Override
	public MultiInsertContext createMultiInsertContext(Statement stmt) {
		return new MysqlMultiInsertContext(stmt);
	}

	@Override
	public String dual() {
		return "FROM DUAL";
	}

	@Override
	protected String getDataTypeOf(int sqlType, Integer lengthOrPrecision, Integer scale) {
		switch (sqlType) {
		case Types.VARCHAR:
			return null;
		case Types.BIGINT:
			return "SIGNED";
		case Types.DECIMAL:
			if (lengthOrPrecision == null) {
				if (scale == null) {
					return "DECIMAL(65,30)";
				} else {
					return "DECIMAL(65," + scale + ")";
				}
			} else {
				if (scale == null) {
					return "DECIMAL(" + lengthOrPrecision + ",0)";
				} else {
					return "DECIMAL(" + lengthOrPrecision + "," + scale + ")";
				}
			}
		case Types.DATE:
			if (useFractionalSecondsOnTimestamp) {
				return "DATETIME(3)";
			} else {
				return "DATETIME";
			}
		case Types.DOUBLE:
			//MySQLでは、DOUBLE（浮動小数点）への明示的なキャストができないので、最大桁数の固定小数点で代用する
			return "DECIMAL(65,30)";
//			return null;
		case Types.TIME:
		case Types.TIMESTAMP:
			if (useFractionalSecondsOnTimestamp) {
				return "DATETIME(3)";
			} else {
				return "DATETIME";
			}
		default:
			return null;
		}
	}
	
	@Override
	public String rowLockExpression() {
		// MysqlはNoWaitは対応していない。For updateのタイムアウトによる検知のみ。
		return "FOR UPDATE";
	}

	@Override
	public String systimestamp() {
		//デフォルトはNOW()、メソッドが設定されている場合は、設定したメソッドが実行される。
		return this.timestampMethod;
	}

	private void checkDateRange(java.util.Date date) {
		//mysqlでは、
		//1000/01/01 ～ 9999/12/31の日付の範囲じゃないと扱えない

		if (date != null) {
			long time = date.getTime();
			if (dateMin > time || dateMax < time) {
				throw new UnsupportedDataTypeException("out of range Date:" + date);
			}
		}
	}
	@Override
	public String toDateExpression(Date date) {
		checkDateRange(date);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		return "STR_TO_DATE('" + fmt.format(date) + "','%Y-%m-%d')";
	}

	@Override
	public String toTimeExpression(Time time) {
		checkDateRange(time);
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		return "STR_TO_DATE('1970-01-01 " + fmt.format(time) + "','%Y-%m-%d %H:%i:%s')";
	}

	@Override
	public String toTimeStampExpression(Timestamp date) {
		checkDateRange(date);
		if (useFractionalSecondsOnTimestamp) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			if (rdbTimeZone() != null) {
				fmt.setTimeZone(rdbTimeZone());
			}
			return "STR_TO_DATE('" + fmt.format(date) + "','%Y-%m-%d %H:%i:%s.%f')";
		} else {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if (rdbTimeZone() != null) {
				fmt.setTimeZone(rdbTimeZone());
			}
			return "STR_TO_DATE('" + fmt.format(date) + "','%Y-%m-%d %H:%i:%s')";
		}
	}

	@Override
	public String toLimitSql(String selectSql, int limitCount, int offset, boolean asBind) {
		if (asBind) {
			StringBuilder sb = new StringBuilder();
			sb.append(selectSql);
			sb.append(" LIMIT ?,?");
			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(selectSql);
			sb.append(" LIMIT " + offset + "," + limitCount);

			return sb.toString();
		}
	}

	@Override
	public Object[] toLimitSqlBindValue(int limitCount,
			int offset) {
		return new Integer[]{Integer.valueOf(offset), Integer.valueOf(limitCount)};
	}

	@Override
	public boolean isDuplicateValueException(SQLException e) {
		if (e.getErrorCode() == 1022 || e.getErrorCode() == 1062) {
			return true;
		} else {
			if (e instanceof BatchUpdateException) {
				//FIXME 現状、バッチ更新でユニークキー違反かどうかをエラーコードで知ることができない。。。エラーメッセージにDuplicate entryが含まれているかどうかで判断するしかない。。。
				if (e.getMessage() != null
						&& (e.getMessage().contains("Duplicate entry")
								|| e.getMessage().contains("Can't write; duplicate key in table"))) {
					return true;
				}
			}

			return false;
		}
	}

	@Override
	public boolean isDeadLock(SQLException e) {
		return e.getErrorCode() == 1213;
	}

	@Override
	public boolean isLockFailed(SQLException e) {
		// MysqlはNoWaitは対応していない。For updateのタイムアウトによる検知のみ。
		return e.getErrorCode() == 1205;
	}

	@Override
	public boolean isCastFailed(SQLException e) {
		//mysqlは、なんだかんだでよろしく変換する。nullとかに
		return false;
//		if (e.getErrorCode() == 1722 //invalid number
//				|| (e.getErrorCode() >= 1830 && e.getErrorCode() <= 1865)) { //invalid date
//			return true;
//		} else {
//			return false;
//		}
	}


	@Override
	public String addDate(String dateExpression, int day) {
		return "DATE_ADD("+ dateExpression + ", INTERVAL '" + day + "' DAY )";
	}

	@Override
	public String checkStatusQuery() {
		return "SELECT 1 ";
	}

	@Override
	public String likePattern(String str) {
		return str;
	}

	@Override
	public String escape() {
		return "ESCAPE '\\\\'";
	}


	@Override
	public String sanitize(String str) {
		// \もエスケープ対象

		if (str == null) {
			return null;
		}
		boolean containsQuote = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\''
				|| str.charAt(i) == '\\') {
				containsQuote = true;
				break;
			}
		}

		if (containsQuote) {
			StringBuilder sb = new StringBuilder();
			char c = 0;
			for (int i = 0; i < str.length(); i++) {
				c = str.charAt(i);
				if (c == '\'') {
					sb.append('\'');
				} else if (c == '\\') {
					sb.append('\\');
				}
				sb.append(c);
			}
			return sb.toString();
		} else {
			return str;
		}
	}

	private static final String[] CAST_VARCHAR = {"CAST(", " AS CHAR)"};
	private static final String[] CAST_BIGINT = {"CAST(", " AS SIGNED)"};
	//MySQLは、負のスケールを設定できないため、取り得る最大桁数を設定し、ROUND関数で切り捨てる。
	private static final String[] CAST_DECIMAL = {"CAST(", " AS DECIMAL(65,30))"};
	private static final String[] CAST_DATE = {"CAST(", " AS DATE)"};
	//MySQLでは、DOUBLE（浮動小数点）への明示的なキャストができないので、最大桁数の固定小数点で代用する
	private static final String[] CAST_DOUBLE = {"CAST(", " AS DECIMAL(65,30))"};
	private static final String[] CAST_TIME = {"STR_TO_DATE(CONCAT('1970-01-01 ',TIME_FORMAT(CAST(", " AS TIME),'%H:%i:%s')),'%Y-%m-%d %H:%i:%s')"};
	private static final String[] CAST_TIME_WITH_FS = {"STR_TO_DATE(CONCAT('1970-01-01 ',TIME_FORMAT(CAST(", " AS TIME),'%H:%i:%s')),'%Y-%m-%d %H:%i:%s.%f')"};
	private static final String[] CAST_TIMESTAMP = {"CAST(", " AS DATETIME)"};
	private static final String[] CAST_TIMESTAMP_WITH_FS = {"CAST(", " AS DATETIME(3))"};

	@Override
	public String[] castExp(int sqlType, Integer lengthOrPrecision, Integer scale) {

		switch (sqlType) {
		case Types.VARCHAR:
			if (lengthOrPrecision != null) {
				return new String[]{"CAST(", " AS CHAR(" + lengthOrPrecision + "))"};
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
						return new String[]{"ROUND(CAST(",  " AS DECIMAL(65,0))," + scale + ")"};
					} else {
						return new String[]{"CAST(", " AS DECIMAL(65," + scale + "))"};
					}
				}
			} else {
				if (scale == null) {
					return new String[]{"CAST(", " AS DECIMAL(" + lengthOrPrecision + ",0))"};
				} else {
					if (scale < 0) {
						//MySQLは、負のスケールを設定できないため、取り得る最大桁数を設定し、ROUND関数で切り捨てる。
						return new String[]{"ROUND(CAST(",  " AS DECIMAL(" + lengthOrPrecision + -scale + ",0))," + scale + ")"};
					} else {
						return new String[]{"CAST(",  " AS DECIMAL(" + lengthOrPrecision + "," + scale + "))"};
					}
				}
			}
		case Types.DATE:
			return CAST_DATE;
		case Types.DOUBLE:
			return CAST_DOUBLE;
		case Types.TIME:
			if (useFractionalSecondsOnTimestamp) {
				return CAST_TIME_WITH_FS;
			} else {
				return CAST_TIME;
			}
		case Types.TIMESTAMP:
			if (useFractionalSecondsOnTimestamp) {
				return CAST_TIMESTAMP_WITH_FS;
			} else {
				return CAST_TIMESTAMP;
			}
		default:
			return null;
		}
	}

	@Override
	public CharSequence cast(int fromSqlType, int toSqlType, CharSequence valExpr, Integer lengthOrPrecision, Integer scale) {
		//MySQLでは、-のscale指定ができないので、ROUNDで対応
		if (toSqlType == Types.DECIMAL && scale != null && scale < 0) {
			CharSequence castString = super.cast(fromSqlType, toSqlType, valExpr, lengthOrPrecision, 0);
			return "ROUND(" + castString + "," + scale + ")";
		}
		
		return super.cast(fromSqlType, toSqlType, valExpr, lengthOrPrecision, scale);
	}

	@Override
	public String tableAlias( String selectSql ) {
		// MySQLの場合、UPDATE文でサブクエリ FROM 節と更新対象の両方に同じテーブルを使用することはできない。
		// そのため、テーブル別名をつける事で対応する。
		return "SELECT X.* FROM (" + selectSql + ") AS X";
	}

	@Override
	public boolean isSupportGroupingExtention(RollType rollType) {
		switch (rollType) {
		case ROLLUP:
			return true;
		case CUBE://TODO MYSQLにはCUBEがないので。。。
			return false;
		default:
			return false;
		}
	}

	@Override
	public String rollUpStart(RollType rollType) {
		return "";
	}

	@Override
	public String rollUpEnd(RollType rollType) {
		switch (rollType) {
		case ROLLUP:
		case CUBE://TODO MYSQLにはCUBEがないので。。。
			return " WITH ROLLUP ";
//			return " WITH CUBE ";
		default:
			return "";
		}
	}

	@Override
	public String seqNextSelectSql(String sequenceName, int tenantId, String entityDefId) {
		throw new UnsupportedOperationException("SEQUENCE not supported");
	}

	@Override
	public String initBlob() {
		return "''";
	}

	@Override
	public boolean isUseSubQueryForIndexJoin() {
		return false;
	}

	@Override
	public String getOptimizerHint() {
		return null;
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
	public boolean isSupportOptimizerHint() {
		return supportOptimizerHint;
	}

	public void setSupportOptimizerHint(boolean supportOptimizerHint) {
		this.supportOptimizerHint = supportOptimizerHint;
	}

	@Override
	public boolean isSupportTableHint() {
		return true;
	}

	@Override
	public String[] getTableHintBracket() {
		return null;
	}

	@Override
	public void appendSortSpecExpression(StringBuilder sb, CharSequence sortValue, SortType sortType,
			NullOrderingSpec nullOrderingSpec) {
		if (nullOrderingSpec != null) {
			sb.append(sortValue);
			switch (nullOrderingSpec) {
			case FIRST:
				sb.append(" IS NULL DESC,");
				break;
			case LAST:
				sb.append(" IS NULL ASC,");
				break;
			default:
				break;
			}
		}
		sb.append(sortValue);
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
		String[] ret = {
				"CONVERT_TZ(",
				",(SELECT @@session.time_zone),'" + to + "')"};
		return ret;
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
		if (localTemporaryTableManageOutsideTransaction || localTemporaryTableCreatedByDataSource) {
			return "TRUNCATE TABLE " + tableName;
		} else {
			return "DROP TEMPORARY TABLE IF EXISTS " + tableName;
		}
	}

	@Override
	public boolean isSupportGlobalTemporaryTable() {
		return false;
	}

	@Override
	public String createLocalTemporaryTable(String tableName,
			String baseTableName, String[] baseColumnName) {
		if (localTemporaryTableManageOutsideTransaction || localTemporaryTableCreatedByDataSource) {
			return "DELETE FROM " + tableName + " WHERE 1=2";	// 何もしないため。但しテーブルがある前提なのでなければエラーとする。
		} else {
			return createLocalTemporaryTableInternal(tableName, baseTableName, baseColumnName);
		}
	}

	private String createLocalTemporaryTableInternal(String tableName, String baseTableName, String[] baseColumnName) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TEMPORARY TABLE IF NOT EXISTS ");
		sb.append(tableName);
		sb.append(" ENGINE=MEMORY ");//TODO メモリで良いか？？
		sb.append("SELECT ");
		for (int i = 0; i < baseColumnName.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(baseColumnName[i]);
		}
		sb.append(" FROM ");
		sb.append(baseTableName);
		sb.append(" WHERE 1=2");//レコードを作らないため
		return sb.toString();
	}

	@Override
	public boolean isSupportAutoClearTemporaryTableWhenCommit() {
		return false;
	}

	@Override
	public boolean isSupportGroupingExtentionWithOrderBy() {
		return false;
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
		return false;
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
		return new InOperatorBulkDeleteContext();
	}

	@Override
	public MultiTableUpdateMethod getMultiTableUpdateMethod() {
		return MultiTableUpdateMethod.DIRECT_JOIN;
	}

	@Override
	public ResultSet getTableNames(String tableNamePattern, Connection con) throws SQLException {
		DatabaseMetaData dbMeta = con.getMetaData();
		return dbMeta.getTables(con.getCatalog(), null, tableNamePattern, null);
	}

	@Override
	public boolean isSupportWindowFunction() {
		return supportWindowFunction;
	}

	public void setSupportWindowFunction(boolean supportWindowFunction) {
		this.supportWindowFunction = supportWindowFunction;
	}

	/**
	 * ローカル一時テーブルをトランザクションの外で管理するかを指定します。
	 * <p>
	 * <div>
	 * 5.6.5から利用可能なGTIDを利用する場合<code>true</code>を指定する必要があります。<br/>
	 * 但し、{@link #setLocalTemporaryTableCreateAtDataSource}に<code>true</code>を指定した場合は指定する必要はありません。
	 * </div>
	 * </p>
	 *
	 * @param localTemporaryTableManageOutsideTransaction トランザクションの外で管理する場合は<code>true</code>を指定します。
	 */
	public void setLocalTemporaryTableManageOutsideTransaction(boolean localTemporaryTableManageOutsideTransaction) {
		this.localTemporaryTableManageOutsideTransaction = localTemporaryTableManageOutsideTransaction;
	}

	/**
	 * ローカル一時テーブルがデータソースで作成されるかを指定します。
	 *
	 * @param localTemporaryTableCreatedByDataSource データソースで作成される場合は<code>true</code>を指定します。
	 */
	public void setLocalTemporaryTableCreatedByDataSource(boolean localTemporaryTableCreatedByDataSource) {
		this.localTemporaryTableCreatedByDataSource = localTemporaryTableCreatedByDataSource;
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
		return String.format("c%d AS `%s`", colNo, colName);
	}

	@Override
	public String createBinaryViewColumnSql(int colNo, String colName, String lobIdSuffix) {
		return String.format("SUBSTR(SUBSTR(c%d, 1, INSTR(c%d, ',') - 1), 7) AS `%s%s`",
				colNo, colNo, colName, lobIdSuffix);
	}

	@Override
	public String createLongTextViewColumnSql(int colNo, String colName, String lobIdSuffix) {
		StringBuilder sb = new StringBuilder();

		// LobID
		sb.append(String.format("TRIM(SUBSTR(c%d, 3, 16)) AS `%s%s`", colNo, colName, lobIdSuffix)).append(",");
		// Text
		sb.append(String.format("SUBSTR(c%d, 22) AS `%s`", colNo, colName));

		return sb.toString();
	}

	@Override
	public String toCreateViewDDL(String viewName, String selectSql, boolean withDropView) {
		StringBuilder sb = new StringBuilder();

		String lf = System.lineSeparator();

		// ビュー削除DDL
		if (withDropView) {
			sb.append("DROP VIEW IF EXISTS `").append(viewName.toLowerCase()).append("`;");
			sb.append(lf).append(lf);
		}

		// ビュー作成DDL
		sb.append("CREATE VIEW `").append(viewName.toLowerCase()).append("` AS").append(lf);
		sb.append(selectSql).append(";").append(lf).append(lf);

		return sb.toString();
	}

	@Override
	public boolean isNeedMultiTableTrick() {
		return needMultiTableTrick;
	}

	public void setNeedMultiTableTrick(boolean needMultiTableTrick) {
		this.needMultiTableTrick = needMultiTableTrick;
	}

	@Override
	public String getMultiTableTrickClauseForUpdate() {
		return multiTableTrickClauseForUpdate;
	}

	public void setMultiTableTrickClauseForUpdate(String multiTableTrickClauseForUpdate) {
		this.multiTableTrickClauseForUpdate = multiTableTrickClauseForUpdate;
	}

	public boolean isOptimizeCountQuery() {
		return optimizeCountQuery;
	}

	public void setOptimizeCountQuery(boolean optimizeCountQuery) {
		this.optimizeCountQuery = optimizeCountQuery;
	}
	
	@Override
	public UnaryOperator<CharSequence> countQuery(Query q) {
		//導出テーブルのマージ最適化が行われた場合、クエリーブロックのヒント句が適切にマージされないので、
		//マージ最適化が発生すると想定される場合は、導出テーブル使わない形でEQLレベルで対処する。
		if (!isOptimizeCountQuery() || !isSupportOptimizerHint() || !hasNativeHint(q)) {
			return super.countQuery(q);
		}
		
		OptimizeCountQueryChecker checker = new OptimizeCountQueryChecker();
		q.accept(checker);
		if (checker.possible) {
			List<ValueExpression> sl = new ArrayList<>();
			sl.add(new Count());
			q.getSelect().setSelectValues(sl);
			return sql -> sql;
		} else {
			return super.countQuery(q);
		}
	}
	
	private boolean hasNativeHint(Query q) {
		if (q.getSelect().getHintComment() != null) {
			List<Hint> hl = q.getSelect().getHintComment().getHintList();
			if (hl != null) {
				for (Hint h: hl) {
					if (h instanceof NativeHint) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
