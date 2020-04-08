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
package org.iplass.mtp.impl.rdb.adapter;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.TimeZone;

import org.iplass.mtp.entity.query.GroupBy.RollType;
import org.iplass.mtp.entity.query.SortSpec.NullOrderingSpec;
import org.iplass.mtp.entity.query.SortSpec.SortType;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkDeleteContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkInsertContext;
import org.iplass.mtp.impl.rdb.adapter.bulk.BulkUpdateContext;

public class NoopRdbAdapter extends RdbAdapter {

	@Override
	public boolean isSupportOptimizerHint() {
		return false;
	}

	@Override
	public String getOptimizerHint() {
		return null;
	}

	@Override
	public HintPlace getOptimizerHintPlace() {
		return null;
	}

	@Override
	public String[] getOptimizerHintBracket() {
		return null;
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
	protected String getDataTypeOf(int sqlType, Integer lengthOrPrecision, Integer scale) {
		return null;
	}

	@Override
	public String toDateExpression(Date date) {
		return null;
	}

	@Override
	public String toTimeExpression(Time time) {
		return null;
	}

	@Override
	public String toTimeStampExpression(Timestamp date) {
		return null;
	}

	@Override
	public String systimestamp() {
		return null;
	}

	@Override
	public MultiInsertContext createMultiInsertContext(Statement stmt) {
		return null;
	}

	@Override
	public BulkInsertContext createBulkInsertContext() {
		return null;
	}

	@Override
	public BulkUpdateContext createBulkUpdateContext() {
		return null;
	}

	@Override
	public BulkDeleteContext createBulkDeleteContext() {
		return null;
	}

	@Override
	public String seqNextSelectSql(String sequenceName, int tenantId, String entityDefId) {
		return null;
	}

	@Override
	public boolean isUseSubQueryForIndexJoin() {
		return false;
	}

	@Override
	public String dual() {
		return null;
	}

	@Override
	public String rowLockExpression() {
		return null;
	}

	@Override
	public String toLimitSql(String selectSql, int limitCount, int offset, boolean asBind) {
		return null;
	}

	@Override
	public Object[] toLimitSqlBindValue(int limitCount, int offset) {
		return null;
	}

	@Override
	public boolean isDuplicateValueException(SQLException e) {
		return false;
	}

	@Override
	public boolean isDeadLock(SQLException e) {
		return false;
	}

	@Override
	public boolean isLockFailed(SQLException e) {
		return false;
	}

	@Override
	public boolean isCastFailed(SQLException e) {
		return false;
	}

	@Override
	public String addDate(String dateExpression, int day) {
		return null;
	}

	@Override
	public String checkStatusQuery() {
		return null;
	}

	@Override
	public String likePattern(String str) {
		return null;
	}

	@Override
	public String escape() {
		return null;
	}

	@Override
	public String tableAlias(String selectSql) {
		return null;
	}

	@Override
	public boolean isSupportGroupingExtention(RollType rollType) {
		return false;
	}

	@Override
	public boolean isSupportGroupingExtention() {
		return false;
	}

	@Override
	public boolean isSupportGroupingExtentionWithOrderBy() {
		return false;
	}

	@Override
	public String rollUpStart(RollType rollType) {
		return null;
	}

	@Override
	public String rollUpEnd(RollType rollType) {
		return null;
	}

	@Override
	public void appendSortSpecExpression(StringBuilder sb, CharSequence sortValue, SortType sortType,
			NullOrderingSpec nullOrderingSpec) {
	}

	@Override
	public String[] convertTZ(String to) {
		return null;
	}

	@Override
	public String initBlob() {
		return null;
	}

	@Override
	public boolean isEnableInPartitioning() {
		return false;
	}

	@Override
	public int getInPartitioningSize() {
		return 0;
	}

	@Override
	public boolean isSupportGlobalTemporaryTable() {
		return false;
	}

	@Override
	public boolean isSupportAutoClearTemporaryTableWhenCommit() {
		return false;
	}

	@Override
	public String createLocalTemporaryTable(String tableName, String baseTableName, String[] baseColumnName) {
		return null;
	}

	@Override
	public String deleteTemporaryTable(String tableName) {
		return null;
	}

	@Override
	public boolean isEnableBindHint() {
		return false;
	}

	@Override
	public boolean isAlwaysBind() {
		return false;
	}

	@Override
	public int getBatchSize() {
		return 0;
	}

	@Override
	public int getMaxFetchSize() {
		return 0;
	}

	@Override
	public int getDefaultQueryTimeout() {
		return 0;
	}

	@Override
	public int getDefaultFetchSize() {
		return 0;
	}

	@Override
	public int getThresholdCountOfUsePrepareStatement() {
		return 0;
	}

	@Override
	public boolean isSupportWindowFunction() {
		return false;
	}

	@Override
	public MultiTableUpdateMethod getMultiTableUpdateMethod() {
		return null;
	}

	@Override
	public ResultSet getTableNames(String tableNamePattern, Connection con) throws SQLException {
		return null;
	}

	@Override
	public TimeZone rdbTimeZone() {
		return null;
	}

}
