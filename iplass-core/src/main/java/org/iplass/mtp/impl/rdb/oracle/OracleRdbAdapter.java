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

package org.iplass.mtp.impl.rdb.oracle;

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
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.iplass.mtp.entity.query.GroupBy.RollType;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.value.aggregate.Aggregate;
import org.iplass.mtp.entity.query.value.aggregate.Median;
import org.iplass.mtp.entity.query.value.aggregate.Mode;
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
import org.iplass.mtp.impl.rdb.adapter.function.DynamicTypedFunctionAdapter;
import org.iplass.mtp.impl.rdb.adapter.function.StaticTypedFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentDateFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentDateTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.CurrentTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.ExtractDateFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.LocalTimeFunctionAdapter;
import org.iplass.mtp.impl.rdb.common.function.RoundTruncFunctionAdapter;
import org.iplass.mtp.impl.rdb.oracle.function.OracleDateAddFunctionAdapter;
import org.iplass.mtp.impl.rdb.oracle.function.OracleDateDiffFunctionAdapter;
import org.iplass.mtp.spi.ServiceRegistry;

public class OracleRdbAdapter extends RdbAdapter {

	private static final String[] optimizerHintBracket = {"/*+", "*/"};
	
	private int lockTimeout = 0;
	private String timestampFunction = "CURRENT_TIMESTAMP(3)";
	private String addMonthsFunction = "ADD_MONTHS";
	private String monthsBetweenFunction = "MONTHS_BETWEEN";
	private boolean isUseSubQueryForIndexJoin = true;

	private String optimizerHint = "FIRST_ROWS(100)";
	private boolean enableInPartitioning = false;

	//oracle 11gR2patch3以上は、全角％、＿はワイルドカードではなくなった。11gR2patch2以前利用の場合は、これをtrueにセット
	private boolean escapeFullwidthWildcard;

	private boolean enableBindHint = true;
	private boolean alwaysBind = true;
	private int batchSize = 100;
	private int thresholdCountOfUsePrepareStatement = 50;
	private int maxFetchSize = 100;
	private int defaultQueryTimeout;
	private int defaultFetchSize;
	
	//Oracle 12cから利用可能なFETCH FIRST句を使うか否か
	private boolean useFetchFirstClause;
	
	private static final String DATE_MIN = "-47120101000000000";
	private static final String DATE_MAX = "99991231235959999";
	long dateMin;
	long dateMax;

	public OracleRdbAdapter() {
		addFunction(new StaticTypedFunctionAdapter("CHAR_LENGTH", "LENGTH", Long.class));
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
		addFunction(new RoundTruncFunctionAdapter("TRUNCATE", "TRUNC"));
		addFunction(new StaticTypedFunctionAdapter("UPPER", String.class));
		addFunction(new StaticTypedFunctionAdapter("LOWER", String.class));
		addFunction(new ExtractDateFunctionAdapter("SECOND"));
		addFunction(new ExtractDateFunctionAdapter("MINUTE"));
		addFunction(new ExtractDateFunctionAdapter("HOUR"));
		addFunction(new ExtractDateFunctionAdapter("DAY"));
		addFunction(new ExtractDateFunctionAdapter("MONTH"));
		addFunction(new ExtractDateFunctionAdapter("YEAR"));
		addFunction(new OracleDateAddFunctionAdapter());//DATE_ADD
		addFunction(new OracleDateDiffFunctionAdapter());//DATE_DIFFD
		addFunction(new CurrentDateFunctionAdapter());
		addFunction(new CurrentTimeFunctionAdapter());
		addFunction(new CurrentDateTimeFunctionAdapter());
		addFunction(new LocalTimeFunctionAdapter());

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

		//TODO
		//LOCALTIME(timestamp)

	}

	public boolean isUseFetchFirstClause() {
		return useFetchFirstClause;
	}

	public void setUseFetchFirstClause(boolean useFetchFirstClause) {
		this.useFetchFirstClause = useFetchFirstClause;
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

	public boolean isEscapeFullwidthWildcard() {
		return escapeFullwidthWildcard;
	}

	public void setEscapeFullwidthWildcard(boolean escapeFullwidthWildcard) {
		this.escapeFullwidthWildcard = escapeFullwidthWildcard;
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
		//oracleでは、
		//-4712/01/01 ～ 9999/12/31の日付の範囲じゃないと扱えない

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
		return "TO_DATE('" + fmt.format(date) + "','YYYY-MM-DD')";
	}

	public String toTimeExpression(Time time) {
		checkDateRange(time);
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		return "TO_TIMESTAMP('1970-01-01 " + fmt.format(time) + "','YYYY-MM-DD HH24:MI:SS.FF')";
	}

	public String toTimeStampExpression(Timestamp date) {
		checkDateRange(date);
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		if (rdbTimeZone() != null) {
			fmt.setTimeZone(rdbTimeZone());
		}
		return "TO_TIMESTAMP('" + fmt.format(date) + "','YYYY-MM-DD HH24:MI:SS.FF')";
	}

	public String systimestamp() {
		if (rdbTimeZone() == null) {
			return timestampFunction;
		} else {
			return timestampFunction + " AT TIME ZONE '" + rdbTimeZone().getID() + "'";
		}
	}

	private static final String[] CAST_VARCHAR = {"CAST(", " AS VARCHAR2(4000))"};
	private static final String[] CAST_BIGINT = {"CAST(", " AS NUMBER)"};
	private static final String[] CAST_DECIMAL = {"CAST(", " AS NUMBER)"};
	private static final String[] CAST_DATE = {"TRUNC(CAST(", " AS DATE))"};
	private static final String[] CAST_DOUBLE = {"CAST(", " AS BINARY_DOUBLE)"};
	private static final String[] CAST_TIME = {"TO_TIMESTAMP(CONCAT('1970-01-01 ',TO_CHAR(CAST(", " AS TIMESTAMP),'HH24:MI:SS')),'YYYY-MM-DD HH24:MI:SS.FF')"};
	private static final String[] CAST_TIMESTAMP = {"CAST(", " AS TIMESTAMP)"};
	
	@Override
	public String[] castExp(int sqlType, Integer lengthOrPrecision, Integer scale) {
		switch (sqlType) {
		case Types.VARCHAR:
			if (lengthOrPrecision != null) {
				return new String[]{"CAST(", " AS VARCHAR2(" + lengthOrPrecision + " CHAR))"};
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
					return new String[]{"CAST(", " AS NUMBER(*," + scale + "))"};
				}
			} else {
				if (scale == null) {
					return new String[]{"CAST(", " AS NUMBER(" + lengthOrPrecision + ",0))"};
				} else {
					return new String[]{"CAST(",  " AS NUMBER(" + lengthOrPrecision + "," + scale + "))"};
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
		
		switch (toSqlType) {
		case Types.DATE:
			if (fromSqlType == Types.VARCHAR) {
				return "TO_DATE(" + valExpr + ",'YYYY-MM-DD')";
			}
			break;
		case Types.TIME:
			if (fromSqlType == Types.VARCHAR) {
				return "TO_TIMESTAMP(CONCAT('1970-01-01 '," + valExpr + "),'YYYY-MM-DD HH24:MI:SS.FF')";
			}
			break;
		case Types.TIMESTAMP:
			if (fromSqlType == Types.VARCHAR) {
				return "TO_TIMESTAMP(" + valExpr + ",'YYYY-MM-DD HH24:MI:SS.FF')";
			}
			break;
		case Types.VARCHAR:
			if (fromSqlType == Types.DATE) {
				return "TO_CHAR(" + valExpr + ",'YYYY-MM-DD')";
			}
			if (fromSqlType == Types.TIME) {
				return "TO_CHAR(" + valExpr + ",'HH24:MI:SS')";
			}
			if (fromSqlType == Types.TIMESTAMP) {
				return "TO_CHAR(" + valExpr + ",'YYYY-MM-DD HH24:MI:SS.FF')";
			}
			break;
		default:
			break;
		}
		
		return super.cast(fromSqlType, toSqlType, valExpr, lengthOrPrecision, scale);
	}

	public MultiInsertContext createMultiInsertContext(Statement stmt) {
		return new OracleMultiInsertContext(stmt);
	}

	//TODO サポートするデータ型の整理
	@Override
	protected String getDataTypeOf(int sqlType, Integer lengthOrPrecision, Integer scale) {
		switch (sqlType) {
		case Types.VARCHAR:
			if (lengthOrPrecision == null) {
				return "VARCHAR2(4000)";
			} else {
				return "VARCHAR2(" + lengthOrPrecision + " CHAR)";
			}
		case Types.BIGINT:
			return "NUMBER";
		case Types.DECIMAL:
			if (lengthOrPrecision == null) {
				if (scale == null) {
					return "NUMBER";
				} else {
					return "NUMBER(*," + scale + ")";
				}
			} else {
				if (scale == null) {
					return "NUMBER(" + lengthOrPrecision + ")";
				} else {
					return "NUMBER("+ lengthOrPrecision + "," + scale + ")";
				}
			}
		case Types.DATE:
			return "DATE";
		case Types.DOUBLE:
			return "BINARY_DOUBLE";
		case Types.TIME:
		case Types.TIMESTAMP:
			return "TIMESTAMP";
		default:
			return null;
		}
	}
	
	@Override
	public String dual() {
		return "FROM DUAL";
	}

	@Override
	public String rowLockExpression() {
		if (lockTimeout == 0) {
			return "FOR UPDATE NOWAIT";
		} else if (lockTimeout > 0) {
			return "FOR UPDATE WAIT " + lockTimeout;
		} else {
			return "FOR UPDATE";
		}
	}

	@Override
	public String toLimitSql(String selectSql, int limitCount, int offset, boolean asBind) {
		if (useFetchFirstClause) {
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
		} else {
			if (asBind) {
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT * FROM (SELECT A.*,ROWNUM RN FROM (");
				sb.append(selectSql);
				sb.append(") A) WHERE RN BETWEEN ? AND ? ORDER BY RN");
				return sb.toString();
			} else {
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT * FROM (SELECT A.*,ROWNUM RN FROM (");
				sb.append(selectSql);
				sb.append(") A) WHERE RN BETWEEN ");
				sb.append(offset + 1);
				sb.append(" AND ");
				sb.append(offset + limitCount);
				sb.append(" ORDER BY RN");

				return sb.toString();
			}
		}
	}
	
	@Override
	public Object[] toLimitSqlBindValue(int limitCount,
			int offset) {
		if (useFetchFirstClause) {
			return new Integer[]{
					Integer.valueOf(offset), Integer.valueOf(limitCount)};
		} else {
			return new Integer[]{
					Integer.valueOf(offset + 1), Integer.valueOf(offset + limitCount)};
		}
	}


	@Override
	public boolean isDuplicateValueException(SQLException e) {
		if (e.getErrorCode() == 1) {
			return true;
		} else {
			if (e instanceof BatchUpdateException) {
				//FIXME 現状、バッチ更新でPK違反かどうかをエラーコードで知ることができない。。。エラーメッセージにORA-00001が含まれているかどうかで判断するしかない。。。
				if (e.getMessage() != null
						&& e.getMessage().contains("ORA-00001")) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public boolean isDeadLock(SQLException e) {
		return e.getErrorCode() == 60;
	}

	@Override
	public boolean isLockFailed(SQLException e) {
		if (e.getErrorCode() == 54 //no wait
				|| e.getErrorCode() == 30006) { //wait 100
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isCastFailed(SQLException e) {
		if (e.getErrorCode() == 1722 //invalid number
				|| (e.getErrorCode() >= 1830 && e.getErrorCode() <= 1865)) { //invalid date
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String addDate(String dateExpression, int day) {
		//3桁以上の場合は精度を付加しないとエラーになるので追加
		String ret = dateExpression + "+INTERVAL '" + day + "' DAY";
		if (day >= 10000) {
			ret += "(5)";
		} else if (day >= 1000) {
			ret += "(4)";
		} else if (day >= 100) {
			ret += "(3)";
		}
		return ret;
	}

	@Override
	public String checkStatusQuery() {
		return "SELECT 1 FROM DUAL";
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
		return "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
	}

	@Override
	public String likePattern(String str) {
        if (str == null) {
        	return null;
        }

        boolean needSanitaizing = false;
        char current = 0;
        for (int i = 0; i < str.length(); i++) {
            current = str.charAt(i);

            switch (current) {
			case '％':
			case '＿':
				if (escapeFullwidthWildcard) {
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
            case '％':
            case '＿':
            	if (escapeFullwidthWildcard) {
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
		return "EMPTY_BLOB()";
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
					"CAST(FROM_TZ(",
					",SESSIONTIMEZONE) AT TIME ZONE '" + to + "' AS TIMESTAMP)"};
		} else {
			return new String[] {
					"CAST(FROM_TZ(",
					",'" + rdbTimeZone().getID() + "') AT TIME ZONE '" + to + "' AS TIMESTAMP)"};
		}
	}

	@Override
	public boolean isEnableInPartitioning() {
		return enableInPartitioning ;
	}

	public void setEnableInPartitioning(boolean enableInPartitioning) {
		this.enableInPartitioning = enableInPartitioning;
	}

	@Override
	public int getInPartitioningSize() {
		return 1000;
	}

	@Override
	public String deleteTemporaryTable(String tableName) {
		return "DELETE FROM " + tableName;
	}

	@Override
	public boolean isSupportGlobalTemporaryTable() {
		return true;
	}

	@Override
	public String createLocalTemporaryTable(String tableName,
			String baseTableName, String[] baseColumnName) {
		return null;
	}

	@Override
	public boolean isSupportAutoClearTemporaryTableWhenCommit() {
		return true;
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
	
	public String aggregateFunctionName(Aggregate agg) {
		if (agg instanceof Mode) {
			return "STATS_MODE";
		}
		if (agg instanceof Median) {
			return "MEDIAN";
		}
		return super.aggregateFunctionName(agg);
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
		//TODO select * でinlineview作ると、ORA-01792が発生しやすいため、難しい。。。
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
		return true;
	}

	@Override
	public boolean isSupportTableHint() {
		return false;
	}

	@Override
	public String[] getTableHintBracket() {
		return null;
	}

}
