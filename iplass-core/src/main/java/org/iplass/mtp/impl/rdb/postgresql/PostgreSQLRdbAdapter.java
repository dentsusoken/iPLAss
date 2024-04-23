/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.rdb.postgresql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.iplass.mtp.entity.query.GroupBy.RollType;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
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
import org.iplass.mtp.impl.entity.property.PropertyService;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.i18n.I18nService;
import org.iplass.mtp.impl.properties.basic.TimeType;
import org.iplass.mtp.impl.rdb.adapter.BaseRdbTypeAdapter;
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
import org.iplass.mtp.impl.rdb.common.function.ListaggFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.LocalTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.connection.ConnectionFactory;
import org.iplass.mtp.impl.rdb.postgresql.function.PostgreSQLDateAddFunctionAdapter;
import org.iplass.mtp.impl.rdb.postgresql.function.PostgreSQLDateDiffFunctionAdapter;
import org.iplass.mtp.impl.rdb.postgresql.function.PostgreSQLListaggFunctionAdapter;
import org.iplass.mtp.impl.rdb.postgresql.function.PostgreSQLRoundTruncFunctionAdapter;
import org.iplass.mtp.impl.rdb.postgresql.function.PostgreSQLSubstrFunctionAdapter;
import org.iplass.mtp.spi.ServiceRegistry;

public class PostgreSQLRdbAdapter extends RdbAdapter {
	//postgresは非現実的な日付まで指定できるため、Oracleとあわせる。
//	private static final long DATE_MIN = -210866835600000L;//-4712-01-01 00:00:00.000
//	private static final long DATE_MAX = 253402268399999L;//9999-12-31 23:59:59.999

	//FIXME AWS RedShiftでサポートされる関数が微妙に異なる。。特に日付関数系。どちらかというとOracleに近い

	private static PostgreSQLTimeRdbTypeAdapter postgreSQLTimeRdbTypeAdapter =
			new PostgreSQLTimeRdbTypeAdapter(ServiceRegistry.getRegistry().getService(PropertyService.class).getPropertyType(java.sql.Time.class));

	private static final String[] optimizerHintBracket = {"/*+", "*/"};
	private static final String DATE_MIN = "-47120101000000000";
	private static final String DATE_MAX = "99991231235959999";
	long dateMin;
	long dateMax;

	//require　pg_hint_plan
	private boolean supportOptimizerHint = false;

	private String timestampFunction = "CURRENT_TIMESTAMP(3)";

	private boolean escapeBackslash = false;
	private boolean enableBindHint;
	private int batchSize = 100;
	private int maxFetchSize = 100;
	private int defaultQueryTimeout;
	private int defaultFetchSize = 10;//set default to 10
	private int lockTimeout = 0;

	private String viewSubQueryAlias = "vsq";
	private int maxViewNameLength = 63;
	private boolean useStandardListaggFunction;

	public PostgreSQLRdbAdapter() {
		addFunction(new StaticTypedFunctionAdapter("CHAR_LENGTH", Long.class));
		addFunction(new StaticTypedFunctionAdapter("INSTR", Long.class));
		addFunction(new StaticTypedFunctionAdapter("CONCAT", String.class));
		addFunction(new PostgreSQLSubstrFunctionAdapter("SUBSTR"));
		addFunction(new StaticTypedFunctionAdapter("REPLACE", String.class));
		addFunction(new DynamicTypedFunctionAdapter("MOD", new int[]{0,1}));
		addFunction(new StaticTypedFunctionAdapter("SQRT", Double.class));
		addFunction(new DynamicTypedFunctionAdapter("POWER", new int[]{0,1}));
		addFunction(new DynamicTypedFunctionAdapter("ABS", new int[]{0}));
		addFunction(new StaticTypedFunctionAdapter("CEIL", Long.class));
		addFunction(new StaticTypedFunctionAdapter("FLOOR", Long.class));
		addFunction(new PostgreSQLRoundTruncFunctionAdapter("ROUND", "ROUND"));
		addFunction(new PostgreSQLRoundTruncFunctionAdapter("TRUNCATE", "TRUNC"));
		addFunction(new StaticTypedFunctionAdapter("UPPER", String.class));
		addFunction(new StaticTypedFunctionAdapter("LOWER", String.class));
		addFunction(new ExtractDateFunctionAdapter("SECOND"));
		addFunction(new ExtractDateFunctionAdapter("MINUTE"));
		addFunction(new ExtractDateFunctionAdapter("HOUR"));
		addFunction(new ExtractDateFunctionAdapter("DAY"));
		addFunction(new ExtractDateFunctionAdapter("MONTH"));
		addFunction(new ExtractDateFunctionAdapter("YEAR"));
		addFunction(new PostgreSQLDateAddFunctionAdapter());//DATE_ADD
		addFunction(new PostgreSQLDateDiffFunctionAdapter());//DATE_DIFF
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
		if (useStandardListaggFunction) {
			addAggregateFunction(Listagg.class, new ListaggFunctionAdapter());
		} else {
			addAggregateFunction(Listagg.class, new PostgreSQLListaggFunctionAdapter());
		}

		DateFormatSymbols symbols = new DateFormatSymbols();
		symbols.setEras(new String[] { "-", "" });

		I18nService i18n = ServiceRegistry.getRegistry().getService(I18nService.class);
		SimpleDateFormat sdfMin = new SimpleDateFormat("GyyyyMMddHHmmssSSS", symbols);
		sdfMin.setTimeZone(i18n.getTimezone());
		SimpleDateFormat sdfMax = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sdfMax.setTimeZone(i18n.getTimezone());

		try {
			dateMin = sdfMin.parse(DATE_MIN).getTime();
			dateMax = sdfMax.parse(DATE_MAX).getTime();
		} catch (ParseException e) {
			throw new UnsupportedDataTypeException(e);
		}
	}

	public boolean isUseStandardListaggFunction() {
		return useStandardListaggFunction;
	}

	public void setUseStandardListaggFunction(boolean useStandardListaggFunction) {
		this.useStandardListaggFunction = useStandardListaggFunction;
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

	public boolean isEscapeBackslash() {
		return escapeBackslash;
	}

	public void setEscapeBackslash(boolean escapeBackslash) {
		this.escapeBackslash = escapeBackslash;
	}

	public void setLockTimeout(int lockTimeout) {
		this.lockTimeout = lockTimeout;
	}

	public int getLockTimeout() {
		return lockTimeout;
	}

	@Override
	public Connection getConnection(String connectionFactoryName) throws SQLException {
		ConnectionFactory cf;
		if (connectionFactoryName == null) {
			cf = ServiceRegistry.getRegistry().getService(ConnectionFactory.class);
		} else {
			cf = ServiceRegistry.getRegistry().<ConnectionFactory>getService(connectionFactoryName);
		}
		return cf.getConnection();
	}

	@Override
	public String getOptimizerHint() {
		return null;
	}
	@Override
	public HintPlace getOptimizerHintPlace() {
		return HintPlace.HEAD_OF_SQL;
	}

	@Override
	public String[] getOptimizerHintBracket() {
		return optimizerHintBracket;
	}

	public String getTimestampFunction() {
		return timestampFunction;
	}

	public void setTimestampFunction(String timestampFunction) {
		this.timestampFunction = timestampFunction;
	}

	private void checkDateRange(java.util.Date date) {
		//postgresでは、非現実的な日付まで指定可能なため、以下とする。
		//-4712/01/01 ～ 9999/12/31

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
		return "CAST('" + fmt.format(date) + "' AS DATE )";
	}

	public String toTimeExpression(Time time) {
		checkDateRange(time);
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		return "CAST('1970-01-01 " + fmt.format(time) + "' AS TIMESTAMP(0))";
	}

	public String toTimeStampExpression(Timestamp date) {
		checkDateRange(date);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (rdbTimeZone() != null) {
			fmt.setTimeZone(rdbTimeZone());
		}
		return "CAST('" + fmt.format(date) + "' AS TIMESTAMP(3))";
	}

	public String systimestamp() {
		if (rdbTimeZone() == null) {
			return timestampFunction;
		} else {
			return timestampFunction + " AT TIME ZONE '" + rdbTimeZone().getID() + "'";
		}
	}

	public MultiInsertContext createMultiInsertContext(Statement stmt) {
		return new PostgreSQLMultiInsertContext(stmt);
	}

	@Override
	public String[] castExp(int sqlType, Integer lengthOrPrecision, Integer scale) {
		if (sqlType == Types.DECIMAL && scale != null && scale < 0) {
			if (lengthOrPrecision == null) {
				//負のスケールを設定できないため、取り得る最大桁数を設定し、ROUND関数で切り捨てる。
				return new String[]{"ROUND(CAST(",  " AS NUMERIC(65,0))," + scale + ")"};
			} else {
				//負のスケールを設定できないため、取り得る最大桁数を設定し、ROUND関数で切り捨てる。
				return new String[]{"ROUND(CAST(",  " AS NUMERIC(" + lengthOrPrecision + -scale + ",0))," + scale + ")"};
			}
		}

		return super.castExp(sqlType, lengthOrPrecision, scale);
	}

	//TODO サポートするデータ型の整理
	@Override
	protected String getDataTypeOf(int sqlType, Integer lengthOrPrecision, Integer scale) {
		switch (sqlType) {
		case Types.VARCHAR:
			if (lengthOrPrecision == null) {
				return "VARCHAR(4000)";
			} else {
				return "VARCHAR(" + lengthOrPrecision + ")";
			}
		case Types.BIGINT:
			return "BIGINT";
		case Types.DECIMAL:
			if (lengthOrPrecision == null) {
				if (scale == null) {
					return "NUMERIC(65,30)";
				} else {
					return "NUMERIC(" + 35 + scale + "," + scale + ")";
				}
			} else {
				if (scale == null) {
					return "NUMERIC(" + lengthOrPrecision + ",0)";
				} else {
					return "NUMERIC("+ lengthOrPrecision + "," + scale + ")";
				}
			}
		case Types.DATE:
			return "DATE";
		case Types.DOUBLE:
			return "NUMERIC(65,30)";
		case Types.TIME:
		case Types.TIMESTAMP:
			return "TIMESTAMP";
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
			return "FOR UPDATE NOWAIT";
		} else {
			// PostgreSQLは待機時間の指定なし
			return "FOR UPDATE";
		}
	}

	@Override
	public String toLimitSql(String selectSql, int limitCount, int offset, boolean asBind) {
		if (asBind) {
			StringBuilder sb = new StringBuilder();
			sb.append(selectSql);
			sb.append(" LIMIT ? OFFSET ?");
			return sb.toString();
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(selectSql);
			sb.append(" LIMIT " + limitCount + " OFFSET " + offset);

			return sb.toString();
		}
	}

	@Override
	public Object[] toLimitSqlBindValue(int limitCount,
			int offset) {
		return new Integer[]{Integer.valueOf(limitCount), Integer.valueOf(offset)};
	}

	@Override
	public boolean isDuplicateValueException(SQLException e) {
		return "23505".equals(e.getSQLState());
	}

	@Override
	public boolean isDeadLock(SQLException e) {
		return "40P01".equals(e.getSQLState());
	}

	@Override
	public boolean isLockFailed(SQLException e) {
		return "55P03".equals(e.getSQLState());
	}

	@Override
	public boolean isCastFailed(SQLException e) {
		// 22007 : 無効な日付時刻の書式
		// 22P02 : 無効なテキスト表現
		return "22007".equals(e.getSQLState()) || "22P02".equals(e.getSQLState());
	}

	@Override
	public String addDate(String dateExpression, int day) {
		String ret = dateExpression + "+ ' " + day + " DAYS'";
		return ret;
	}

	@Override
	public String checkStatusQuery() {
		return "SELECT 1";
	}

	@Override
	public String likePattern(String str) {
		return str;
	}

	@Override
	public String escape() {
		if (escapeBackslash) {
			return "ESCAPE '\\\\'";
		} else {
			return "ESCAPE '\\'";
		}
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
		switch (rollType) {
		case ROLLUP:
			return " ROLLUP(";
		case CUBE:
			return " CUBE(";
		default:
			return "";
		}
	}

	@Override
	public String rollUpEnd(RollType rollType) {
		if (rollType != null) {
			return ") ";
		} else {
			return "";
		}
	}

	@Override
	public String seqNextSelectSql(String sequenceName, int tenantId, String entityDefId) {
		return "SELECT NEXTVAL('" + sequenceName + "')";
	}

	//TODO sanitaize対象確認要

	@Override
	public String sanitize(String str) {
        if (str == null) {
        	return null;
        }

        boolean needSanitaizing = false;
        char current = 0;
        for (int i = 0; i < str.length(); i++) {
            current = str.charAt(i);
            switch (current) {
			case '\'':
				needSanitaizing = true;
				break;
			case '\\':
				if (escapeBackslash) {
					needSanitaizing = true;
				}
				break;
			default:
				break;
			}

            if (needSanitaizing) {
            	break;
            }
        }

        if (!needSanitaizing) {
            return str;
        }


        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
        	current = str.charAt(i);
            switch (current) {
            case '\'':
                buff.append('\'');
                break;
            case '\\':
            	if (escapeBackslash) {
            		buff.append('\\');
            	}
                break;
            default:
                break;
            }

            buff.append(current);
        }

        return buff.toString();
	}

	@Override
	public String initBlob() {
		return "null";
	}

	@Override
	public boolean isUseSubQueryForIndexJoin() {
		return false;
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

	@Override
	public void appendSortSpecExpression(StringBuilder sb, CharSequence sortValue, SortType sortType,
			NullOrderingSpec nullOrderingSpec) {
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
		if (nullOrderingSpec != null) {
			switch (nullOrderingSpec) {
			case FIRST:
				sb.append(" NULLS FIRST");
				break;
			case LAST:
				sb.append(" NULLS LAST");
				break;
			default:
				break;
			}
		}
	}

	@Override
	public String[] convertTZ(String to) {
		if (rdbTimeZone() == null) {
			return new String[] {
					"TIMEZONE('" + to + "',CAST(",
					" AS TIMESTAMPTZ(3)))"};
		} else {
			return new String[] {
					"TIMEZONE('" + to + "',TIMEZONE('" + rdbTimeZone().getID() + "',",
					"))"};
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
		return "DROP TABLE IF EXISTS " + tableName;
	}

	@Override
	public boolean isSupportGlobalTemporaryTable() {
		return false;
	}

	@Override
	public boolean isSupportAutoClearTemporaryTableWhenCommit() {
		return true;
	}

	@Override
	public String createLocalTemporaryTable(String tableName,
			String baseTableName, String[] baseColumnName) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TEMPORARY TABLE ");
		sb.append(tableName);
		sb.append(" ON COMMIT DROP AS SELECT ");
		for (int i = 0; i < baseColumnName.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(baseColumnName[i]);
		}
		sb.append(" FROM ");
		sb.append(baseTableName);
		sb.append(" WITH NO DATA");
		return sb.toString();
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
		return -1;
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
		return MultiTableUpdateMethod.NO_SUPPORT;
	}

	@Override
	public ResultSet getTableNames(String tableNamePattern, Connection con) throws SQLException {
		DatabaseMetaData dbMeta = con.getMetaData();
		return dbMeta.getTables(null, con.getSchema(), tableNamePattern, null);
	}

	@Override
	public boolean isSupportWindowFunction() {
		return true;
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
		return false;
	}

	@Override
	public String[] getTableHintBracket() {
		return null;
	}

	@Override
	public boolean isSupportBlobType() {
		return false;
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
		return String.format("c%d AS \"%s\"", colNo, colName.toLowerCase());
	}

	@Override
	public String createBinaryViewColumnSql(int colNo, String colName, String lobIdSuffix) {
		return String.format("SUBSTR(SUBSTR(c%d::TEXT, 1, STRPOS(c%d::TEXT, ',') - 1), 7) AS \"%s%s\"",
				colNo, colNo, colName.toLowerCase(), lobIdSuffix.toLowerCase());
	}

	@Override
	public String createLongTextViewColumnSql(int colNo, String colName, String lobIdSuffix) {
		StringBuilder sb = new StringBuilder();

		// LobID
		sb.append(String.format("TRIM(SUBSTR(c%d::TEXT, 3, 16)) AS \"%s%s\"",
				colNo, colName.toLowerCase(), lobIdSuffix.toLowerCase())).append(",");
		// Text
		sb.append(String.format("SUBSTR(c%d::TEXT, 22) AS \"%s\"", colNo, colName.toLowerCase()));

		return sb.toString();
	}

	@Override
	public String toCreateViewDDL(String viewName, String selectSql, boolean withDropView) {
		StringBuilder sb = new StringBuilder();

		String lf = System.lineSeparator();

		// ビュー削除DDL
		if (withDropView) {
			sb.append("DROP VIEW IF EXISTS \"").append(viewName.toLowerCase()).append("\" CASCADE;");
			sb.append(lf).append(lf);
		}

		// ビュー作成DDL
		sb.append("CREATE VIEW \"").append(viewName.toLowerCase()).append("\" AS").append(lf);
		sb.append(selectSql).append(";").append(lf).append(lf);

		return sb.toString();
	}

	@Override
	public BaseRdbTypeAdapter getRdbTypeAdapter(Object value) {
		if (value instanceof Time) {
			return postgreSQLTimeRdbTypeAdapter;
		} else {
			return super.getRdbTypeAdapter(value);
		}
	}

	@Override
	public BaseRdbTypeAdapter getRdbTypeAdapter(PropertyType propType) {
		if (propType instanceof TimeType) {
			return postgreSQLTimeRdbTypeAdapter;
		} else {
			return super.getRdbTypeAdapter(propType);
		}
	}

}
