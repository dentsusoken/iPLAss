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

package org.iplass.mtp.impl.counter.sql;

import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.UpdateSqlHandler;

public class RdbTableCounterSql extends UpdateSqlHandler {

	public static final String TABLE_NAME = "COUNTER";
	public static final String TENANT_ID = "TENANT_ID";
	public static final String COUNTER_NAME = "CNT_NAME";
	public static final String INCREMENT_UNIT_KEY = "INC_UNIT_KEY";
	public static final String COUNTER_VAL = "CNT_VAL";


	public String createCounterSql(int tenantId, String counterName, String incrementUnitKey, long initValue, RdbAdapter rdb) {
		return "INSERT INTO " + TABLE_NAME + "(" + TENANT_ID + "," + COUNTER_NAME + "," + INCREMENT_UNIT_KEY + "," + COUNTER_VAL + ") "
			+ "VALUES(" + tenantId + ",'" + rdb.sanitize(counterName) + "','" + rdb.sanitize(incrementUnitKey) + "'," + initValue + ")";
	}

	public String incrementSql(int tenantId, String counterName, String incrementUnitKey, int increment, RdbAdapter rdb) {
		//TODO Preparedの方が高速か？？（ステートメントキャッシュ前提であればあるいは？）
		return "UPDATE " + TABLE_NAME + " SET " + COUNTER_VAL + "=" + COUNTER_VAL + "+" + increment +
				" WHERE " + TENANT_ID + "=" + tenantId + " AND " + COUNTER_NAME + "='" + rdb.sanitize(counterName) + "' AND " + INCREMENT_UNIT_KEY + "='" + rdb.sanitize(incrementUnitKey) + "'";
	}

	public String deleteCounterSql(int tenantId, String counterName, String incrementUnitKey, RdbAdapter rdb) {
		return "DELETE FROM " + TABLE_NAME +
				" WHERE " + TENANT_ID + "=" + tenantId + " AND " + COUNTER_NAME + "='" + rdb.sanitize(counterName) + "' AND " + INCREMENT_UNIT_KEY + "='" + rdb.sanitize(incrementUnitKey) + "'";
	}

	public String currentValueSql(int tenantId, String counterName, String incrementUnitKey, boolean withLock, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT " + COUNTER_VAL + " FROM " + TABLE_NAME +	" WHERE " + TENANT_ID + "=")
			.append(tenantId)
			.append(" AND " + COUNTER_NAME + "='")
			.append(rdb.sanitize(counterName)).append("' AND " + INCREMENT_UNIT_KEY + "='")
			.append(rdb.sanitize(incrementUnitKey)).append("'");

		return withLock ? rdb.createRowLockSql(sb.toString()) : sb.toString();
	}

	public String keySetSql(int tenantId, String counterName, String prefixIncrementUnitKey, RdbAdapter rdb) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT " + INCREMENT_UNIT_KEY + " FROM " + TABLE_NAME)
			.append(" WHERE " + TENANT_ID + "=" + tenantId)
			.append(" AND " + COUNTER_NAME + "='" + rdb.sanitize(counterName) + "'")
			.append(" AND " + INCREMENT_UNIT_KEY + " LIKE '"
					+ rdb.sanitize(rdb.sanitizeForLike(prefixIncrementUnitKey)) + "%' " + rdb.escape());

		return sb.toString();
	}


}
