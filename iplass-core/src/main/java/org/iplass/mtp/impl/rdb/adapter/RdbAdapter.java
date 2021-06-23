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

package org.iplass.mtp.impl.rdb.adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.query.GroupBy.RollType;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.entity.query.value.aggregate.Aggregate;
import org.iplass.mtp.entity.query.value.aggregate.Avg;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.aggregate.Min;
import org.iplass.mtp.entity.query.value.aggregate.StdDevPop;
import org.iplass.mtp.entity.query.value.aggregate.StdDevSamp;
import org.iplass.mtp.entity.query.value.aggregate.Sum;
import org.iplass.mtp.entity.query.value.aggregate.VarPop;
import org.iplass.mtp.entity.query.value.aggregate.VarSamp;
import org.iplass.mtp.entity.query.value.window.CumeDist;
import org.iplass.mtp.entity.query.value.window.DenseRank;
import org.iplass.mtp.entity.query.value.window.PercentRank;
import org.iplass.mtp.entity.query.value.window.Rank;
import org.iplass.mtp.entity.query.value.window.RowNumber;
import org.iplass.mtp.entity.query.value.window.WindowRankFunction;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.properties.basic.BooleanType;
import org.iplass.mtp.impl.properties.basic.DateTimeType;
import org.iplass.mtp.impl.properties.basic.DateType;
import org.iplass.mtp.impl.properties.basic.DecimalType;
import org.iplass.mtp.impl.properties.basic.FloatType;
import org.iplass.mtp.impl.properties.basic.IntegerType;
import org.iplass.mtp.impl.properties.basic.StringType;
import org.iplass.mtp.impl.properties.basic.TimeType;
import org.iplass.mtp.impl.properties.extend.WrapperType;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkUpdateContext;
import org.iplass.mtp.impl.rdb.adapter.function.FunctionAdapter;
import org.iplass.mtp.impl.rdb.connection.ConnectionFactory;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.util.StringUtil;


public abstract class RdbAdapter {

	private String rdbTimeZone;
	private TimeZone rdbTimeZoneInstance;

	private HashMap<String, FunctionAdapter> functionMap = new HashMap<String, FunctionAdapter>();

	protected void addFunction(FunctionAdapter functionAdapter) {
		//LowerCase,UpperCase両方登録しとく
		functionMap.put(functionAdapter.getFunctionName().toLowerCase(), functionAdapter);
		functionMap.put(functionAdapter.getFunctionName().toUpperCase(), functionAdapter);
	}

	public <T extends UpdateSqlHandler> T getUpdateSqlCreator(Class<T> sqlCreatorClass) {
		//TODO 正式実装。プレフィックス指定してClass.forNameしてみて存在したら、そっちを返却

		try {
			return sqlCreatorClass.newInstance();
		} catch (InstantiationException e) {
			throw new EntityRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new EntityRuntimeException(e);
		}
	}

	public <T extends QuerySqlHandler> T getQuerySqlCreator(Class<T> sqlCreatorClass) {
		//TODO 正式実装。プレフィックス指定してClass.forNameしてみて存在したら、そっちを返却

		try {
			return sqlCreatorClass.newInstance();
		} catch (InstantiationException e) {
			throw new EntityRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new EntityRuntimeException(e);
		}
	}

	public String[] castExp(int sqlType, Integer lengthOrPrecision, Integer scale) {
		String rdbType = getDataTypeOf(sqlType, lengthOrPrecision, scale);
		return new String[]{"CAST(", " AS " + rdbType + ")"};
	}

	public CharSequence cast(int fromSqlType, int toSqlType, CharSequence valExpr, Integer lengthOrPrecision, Integer scale) {
		String[] castExp = castExp(toSqlType, lengthOrPrecision, scale);
		if (castExp == null) {
			return valExpr;
		} else {
			return castExp[0] + valExpr + castExp[1];
		}
	}

	public abstract boolean isSupportOptimizerHint();
	public abstract String getOptimizerHint();
	public abstract HintPlace getOptimizerHintPlace();
	public abstract String[] getOptimizerHintBracket();

	public abstract boolean isSupportTableHint();
	public abstract String[] getTableHintBracket();

	protected abstract String getDataTypeOf(int sqlType, Integer lengthOrPrecision, Integer scale);

	public BaseRdbTypeAdapter getRdbTypeAdapter(Object value) {

		if (value == null) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Null.class);
		}
		if (value instanceof String) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Varchar.class);
		}
		if (value instanceof Long
				|| value instanceof Integer) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Integer.class);
		}
		if (value instanceof BigDecimal) {
			return new BaseRdbTypeAdapter.Decimal(new DecimalType(((BigDecimal) value).scale(), RoundingMode.HALF_EVEN));
		}
		if (value instanceof Date) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Date.class);
		}
		if (value instanceof Double
				|| value instanceof Float) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Float.class);
		}
		if (value instanceof Time) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Time.class);
		}
		if (value instanceof Timestamp) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.DateTime.class);
		}
		if (value instanceof Boolean) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Bool.class);
		}

		return null;
	}

	public BaseRdbTypeAdapter getRdbTypeAdapter(PropertyType propType) {

		if (propType instanceof WrapperType) {

			PropertyType wrapped = ((WrapperType) propType).actualType();
			RdbTypeAdapter actualAdapter = getRdbTypeAdapter(wrapped);

			//TODO Wrapperを返すでなく、actualのTypeをそのまま返せばよいのでは？
			return new WrapperRdbTypeAdapter(propType, (BaseRdbTypeAdapter) actualAdapter);
		}

		if (propType instanceof BooleanType) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Bool.class);
		}
		if (propType instanceof DateTimeType) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.DateTime.class);
		}
		if (propType instanceof DateType) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Date.class);
		}
		if (propType instanceof TimeType) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Time.class);
		}
		if (propType instanceof DecimalType) {
			return new BaseRdbTypeAdapter.Decimal(propType);
		}
		if (propType instanceof FloatType) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Float.class);
		}
		if (propType instanceof IntegerType) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Integer.class);
		}
		if (propType instanceof StringType) {
			return BaseRdbTypeAdapter.getInstance(BaseRdbTypeAdapter.Varchar.class);
		}

		return null;
	}

	public String toSqlExp(Object rdbValue) {//TODO 型情報が必要では？？
		if (rdbValue == null) {
			return null;
		}
		if (rdbValue instanceof String) {
			return "'" + sanitize((String) rdbValue) + "'";
		} else if (rdbValue instanceof Long
				|| rdbValue instanceof Integer
				|| rdbValue instanceof BigDecimal) {//TODO BigDecimalの対応をもう少し考える（精度をどう格納する？）
			return rdbValue.toString();
		} else if (rdbValue instanceof Date) {
			return toDateExpression((Date) rdbValue);
		} else if (rdbValue instanceof Double) {
			return rdbValue.toString();
		} else if (rdbValue instanceof Time) {
			return toTimeExpression((Time) rdbValue);
		} else if (rdbValue instanceof Timestamp) {
			return toTimeStampExpression((Timestamp) rdbValue);
		}

		throw new UnsupportedDataTypeException("Unsupported Data Type:" + rdbValue.getClass().getName());
	}

	public void setParameter(PreparedStatement ps, int index, Object rdbValue) throws SQLException {//TODO 型情報が必要では？？
		if (rdbValue == null) {
			ps.setNull(index, Types.NULL);
		} else if (rdbValue instanceof String) {
			ps.setString(index, (String) rdbValue);
		} else if (rdbValue instanceof Integer) {
			ps.setInt(index, (Integer) rdbValue);
		} else if (rdbValue instanceof Long) {
			ps.setLong(index, (Long) rdbValue);
		} else if (rdbValue instanceof BigDecimal) {//TODO BigDecimalの対応をもう少し考える（精度をどう格納する？）
			ps.setBigDecimal(index, (BigDecimal) rdbValue);
		} else if (rdbValue instanceof Date) {
			ps.setDate(index, (Date) rdbValue);
		} else if (rdbValue instanceof Double) {
			ps.setDouble(index, (Double) rdbValue);
		} else if (rdbValue instanceof Time) {
			ps.setTime(index, (Time) rdbValue);
		} else if (rdbValue instanceof Timestamp) {
			ps.setTimestamp(index, (Timestamp) rdbValue, rdbCalendar());
		} else {
			ps.setObject(index, rdbValue);
		}
	}

	public abstract String toDateExpression(Date date);

	public abstract String toTimeExpression(Time time);

	public abstract String toTimeStampExpression(Timestamp date);

	public String sanitize(String str) {
		if (str == null) {
			return null;
		}
		boolean containsQuote = false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\'') {
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
				}
				sb.append(c);
			}
			return sb.toString();
		} else {
			return str;
		}
	}

	public final FunctionAdapter resolveFunction(String name) {
		FunctionAdapter ret = functionMap.get(name);
		//ファンクション名をtoUpperCaseして再取得
		if (ret == null) {
			ret = functionMap.get(name.toUpperCase());
		}
		return ret;
	}

	public abstract String systimestamp();

	public abstract MultiInsertContext createMultiInsertContext(Statement stmt);

	public abstract BulkInsertContext createBulkInsertContext();
	public abstract BulkUpdateContext createBulkUpdateContext();
	public abstract BulkDeleteContext createBulkDeleteContext();

	public final Connection getConnection() throws SQLException {
		return getConnection(null);
	}
	public Connection getConnection(String connectionFactoryName) throws SQLException {
		if (connectionFactoryName == null) {
			return ServiceRegistry.getRegistry().getService(ConnectionFactory.class).getConnection();
		} else {
			return ServiceRegistry.getRegistry().<ConnectionFactory>getService(connectionFactoryName).getConnection();
		}
	}

	public abstract String seqNextSelectSql(String sequenceName, int tenantId, String entityDefId);

	//Indexテーブルを利用する際、EXISTSによる相関サブクエリの記述をするか、テンポラリにIndexテーブルを作って内部結合するか
	public abstract boolean isUseSubQueryForIndexJoin();

	public abstract String dual();

	public abstract String rowLockExpression();

	public String toLimitSql(String selectSql, int limitCount, int offset) {
		return toLimitSql(selectSql, limitCount, offset, false);
	}
	public abstract String toLimitSql(String selectSql, int limitCount, int offset, boolean asBind);
	public abstract Object[] toLimitSqlBindValue(int limitCount, int offset);

	public abstract boolean isDuplicateValueException(SQLException e);

	public abstract boolean isDeadLock(SQLException e);

	public abstract boolean isLockFailed(SQLException e);
	public abstract boolean isCastFailed(SQLException e);

	public abstract String addDate(String dateExpression, int day);

	public abstract String checkStatusQuery();

	/**
	 * SQLのlike文のパターン文字列に設定するエスケープ処理。
	 * '自体のエスケープはしない。
	 * @param str
	 * @return
	 */
	public final String sanitizeForLike(String str) {
		return likePattern(StringUtil.escapeEqlForLike(str));
	}

	/**
	 * EQLのLikeのパターン文字列で、
	 * %、_、\を\でエスケープされているEQLでの文字列を、RDBネイティブなエスケープ構文に変換する。
	 *
	 * @param str
	 * @return
	 */
	public abstract String likePattern(String str);

	/**
	 * Likeのエスケープ文字指定構文。
	 *
	 * @return
	 */
	public abstract String escape();

	public abstract String tableAlias(String selectSql);

	public abstract boolean isSupportGroupingExtention(RollType rollType);

	public abstract boolean isSupportGroupingExtention();
	public abstract boolean isSupportGroupingExtentionWithOrderBy();

	public abstract String rollUpStart(RollType rollType);

	public abstract String rollUpEnd(RollType rollType);

	public abstract void appendSortSpecExpression(StringBuilder sb, CharSequence sortValue, SortType sortType, NullOrderingSpec nullOrderingSpec);

	public String getRdbTimeZone() {
		return rdbTimeZone;
	}

	public void setRdbTimeZone(String rdbTimeZone) {
		this.rdbTimeZone = rdbTimeZone;
		if (rdbTimeZone == null) {
			rdbTimeZoneInstance = null;
		} else {
			rdbTimeZoneInstance = TimeZone.getTimeZone(rdbTimeZone);
		}

	}

	public TimeZone rdbTimeZone() {
		return rdbTimeZoneInstance;
	}

	public Calendar rdbCalendar() {
		TimeZone tz = rdbTimeZone();
		if (tz == null) {
			return null;
		}
		return Calendar.getInstance(tz);
	}

	public Calendar javaCalendar() {
		TimeZone tz = rdbTimeZone();
		if (tz == null) {
			return null;
		} else {
			return Calendar.getInstance();
		}
	}

	/**
	 * timezoneを変更したtimestampを取得する関数。
	 * 変換前のtimestampはデフォルトタイムゾーン（DBのタイムゾーンもしくはDBセッションのタイムゾーン）と同一である想定。
	 *
	 * @param to
	 * @return [0]がpre文字列、[1]がpost文字列
	 */
	public abstract String[] convertTZ(String to);

	public String upperFunctionName() {
		return "UPPER";
	}

	public abstract String initBlob();

	public abstract boolean isEnableInPartitioning();
	public abstract int getInPartitioningSize();

	/**
	 * global temporary tableをサポートしている場合はtrue
	 *
	 * @return
	 */
	public abstract boolean isSupportGlobalTemporaryTable();

	public abstract boolean isSupportAutoClearTemporaryTableWhenCommit();

	/**
	 * ローカルなTemporaryTableを作成する。
	 *
	 */
	public abstract String createLocalTemporaryTable(String tableName, String baseTableName, String[] baseColumnName);

	/**
	 * TemporaryTableを削除（global temporary tableの場合はデータを削除する）するSQLを生成。
	 *
	 * @param tableName
	 * @return
	 */
	public abstract String deleteTemporaryTable(String tableName);

	/**
	 * bindヒント句を有効化するか否か
	 *
	 * @return
	 */
	public abstract boolean isEnableBindHint();

	/**
	 * EQLをSQLに変換する際、常にバインド変数化するか否か。
	 *
	 * @return
	 */
	public abstract boolean isAlwaysBind();

	/**
	 * バッチ更新利用する際の推奨バッチサイズ。
	 * @return
	 */
	public abstract int getBatchSize();

	public abstract int getMaxFetchSize();

	public abstract int getDefaultQueryTimeout();

	public abstract int getDefaultFetchSize();

	/**
	 * 複数の更新（追加）を一括で処理する際の、PrepareStatementを利用するか否かを判断するための閾値。
	 *
	 * @return
	 */
	public abstract int getThresholdCountOfUsePrepareStatement();

	//FIXME 暫定の実装。ver3.1で正式対応
	private HashMap<Class<? extends Aggregate>, String> aggNameMap = createAggNameMap();
	private HashMap<Class<? extends Aggregate>, String> createAggNameMap() {
		HashMap<Class<? extends Aggregate>, String> map = new HashMap<>();
		map.put(Count.class, "COUNT");
		map.put(Sum.class, "SUM");
		map.put(Avg.class, "AVG");
		map.put(Max.class, "MAX");
		map.put(Min.class, "MIN");
		map.put(StdDevPop.class, "STDDEV_POP");
		map.put(StdDevSamp.class, "STDDEV_SAMP");
		map.put(VarPop.class, "STDDEV_SAMP");
		map.put(VarSamp.class, "VAR_SAMP");
		return map;
	}
	public String aggregateFunctionName(Aggregate agg) {
		return aggNameMap.get(agg.getClass());
	}

	public abstract boolean isSupportWindowFunction();

	//FIXME 暫定の実装。ver3.1で正式対応
	private HashMap<Class<? extends WindowRankFunction>, String> windowRankFuncNameMap = createWindowRankFuncNameMap();
	private HashMap<Class<? extends WindowRankFunction>, String> createWindowRankFuncNameMap() {
		HashMap<Class<? extends WindowRankFunction>, String> map = new HashMap<>();
		map.put(CumeDist.class, "CUME_DIST");
		map.put(DenseRank.class, "DENSE_RANK");
		map.put(PercentRank.class, "PERCENT_RANK");
		map.put(Rank.class, "RANK");
		map.put(RowNumber.class, "ROW_NUMBER");
		return map;
	}
	public String windowRankFunctionName(WindowRankFunction rankFunc) {
		return windowRankFuncNameMap.get(rankFunc.getClass());
	}

	public abstract MultiTableUpdateMethod getMultiTableUpdateMethod();

	public abstract ResultSet getTableNames(String tableNamePattern, Connection con) throws SQLException;

	public String getTemplaryTablePrefix() {
		return "";
	}

	/**
	 * 行ロックのSQLを作成します。
	 * <p>
	 * DMLの判別はしていません。SELECT文に対してのみ使用してください。
	 * </p>
	 *
	 * @param sql 行ロックのSQLを作成するSQL。
	 * @return 行ロックを行うSQL
	 */
	public String createRowLockSql(String sql) {
		return sql + " " + rowLockExpression();
	}

	/**
	 * 行値構成子(行値式)をサポートしているかを判別します。
	 *
	 * @return サポートしている場合は<code>true</code>を返します。
	 */
	public boolean isSupportRowValueConstructor() {
		return true;
	}

	/**
	 * UPDATE文の表名にエイリアスを使用する場合はFROM句が必要かを判別します。
	 *
	 * @return FROM句が必要な場合は<code>true</code>を返します。
	 */
	public boolean isNeedFromClauseTableAliasUpdateStatement() {
		return false;
	}

	/**
	 * BLOB型をサポートしているかを判別します。
	 *
	 * @return サポートしている場合は<code>true</code>を返します。
	 */
	public boolean isSupportBlobType() {
		return true;
	}

	/**
	 * LIMIT句のための省略時のOrderBy句を取得します。
	 *
	 * @return OrderBy句
	 */
	public String getDefaultOrderByForLimit() {
		return "";
	}

	/**
	 * ビューのサブクエリエイリアスを取得します。
	 * 
	 * @return エイリアス
	 */
	public String getViewSubQueryAlias() {
		return "";
	}

	/**
	 * ビュー名の最大長を取得します。
	 * 
	 * @return ビュー名の最大長。0未満の場合は無制限。
	 */
	public int getMaxViewNameLength() {
		return -1;
	}

	/**
	 * ビューカラムのSQLを作成します。
	 * 
	 * @param colNo カラム番号
	 * @param colName カラム名
	 * @return ビューカラムSQL
	 */
	public abstract String createViewColumnSql(int colNo, String colName);

	/**
	 * Binary型のビューカラムのSQLを作成します。
	 * 
	 * @param colNum カラム番号
	 * @param colName カラム名
	 * @param lobIdSuffix ロブIDカラム接尾辞
	 * @return ビューカラムSQL
	 */
	public abstract String createBinaryViewColumnSql(int colNo, String colName, String lobIdSuffix);

	/**
	 * LongText型のビューカラムのSQLを作成します。
	 * 
	 * @param colNum カラム番号
	 * @param colName カラム名
	 * @param lobIdSuffix LobIDカラム接尾辞
	 * @return ビューカラムSQL
	 */
	public abstract String createLongTextViewColumnSql(int colNo, String colName, String lobIdSuffix);

	/**
	 * 指定のSELECT文をビュー作成DDLにします。
	 * 
	 * @param viewName ビュー名
	 * @param selectSql SELECT文
	 * @param withDropView ビュー削除も含める
	 * @return ビュー作成DDL
	 */
	public abstract String toCreateViewDDL(String viewName, String selectSql, boolean withDropView);

	/**
	 * Multi-Table Trickを利用するか否かを指定。
	 * MySQL用の設定項目。
	 * 
	 * @return
	 */
	public boolean isNeedMultiTableTrick() {
		return false;
	}

	/**
	 * Multi-Table Trickで利用する句。
	 * MySQL用の設定項目。
	 * 
	 * @return
	 */
	public String getMultiTableTrickClauseForUpdate() {
		return null;
	}
}
