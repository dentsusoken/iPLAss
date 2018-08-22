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

package org.iplass.mtp.impl.tools.auth.builtin.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.function.Predicate;

import org.apache.commons.lang3.time.DateUtils;
import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAccount;
import org.iplass.mtp.impl.rdb.adapter.QuerySqlHandler;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.tools.auth.builtin.cond.SearchOperator;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition.SpecificType;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.util.DateUtil;

public class AccountMaintenanceSelectSQL extends QuerySqlHandler {

	private static final String ACCOUNT_COUNT_SQL = "SELECT COUNT(ACCOUNT_ID) FROM T_ACCOUNT ";

	private static final String ACCOUNT_SEARCH_SQL = "SELECT " +
													"TENANT_ID, ACCOUNT_ID, POL_NAME, OID, LAST_LOGIN_ON, LOGIN_ERR_CNT, LOGIN_ERR_DATE, LAST_PASSWORD_CHANGE, CRE_USER, CRE_DATE, UP_USER, UP_DATE " +
													"FROM T_ACCOUNT ";

	private static final String ACCOUNT_ORDERBY_SQL = " ORDER BY ACCOUNT_ID ASC";

	public String createAccountCountSQL(RdbAdapter rdb, Tenant tenant, UserSpecificCondition cond, AuthenticationPolicyDefinition authPolicy) {
		StringBuilder sb = new StringBuilder();
		sb.append(ACCOUNT_COUNT_SQL).append(createAccountSearchWhereSQL(rdb, tenant, cond, authPolicy));
		return sb.toString();
	}

	public void setAccountCountParameter(RdbAdapter rdb, PreparedStatement ps, Tenant tenant, UserSpecificCondition cond, AuthenticationPolicyDefinition authPolicy) throws SQLException {
		setAccountSearchParameter(rdb, ps, tenant, cond, authPolicy);
	}

	public int getAccountCountResultData(ResultSet rs) throws SQLException {
		if(rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}


	public String createAccountSearchSQL(RdbAdapter rdb, Tenant tenant, UserSpecificCondition cond, AuthenticationPolicyDefinition authPolicy) {
		StringBuilder sb = new StringBuilder();
		sb.append(ACCOUNT_SEARCH_SQL).append(createAccountSearchWhereSQL(rdb, tenant, cond, authPolicy)).append(ACCOUNT_ORDERBY_SQL);
		return sb.toString();
	}

	public void setAccountSearchParameter(RdbAdapter rdb, PreparedStatement ps, Tenant tenant, UserSpecificCondition cond, AuthenticationPolicyDefinition authPolicy) throws SQLException {
		int index = 1;
		ps.setInt(index++, tenant.getId());

		if (SpecificType.LOCKED == cond.getType()) {
			int lockCount = authPolicy.getAccountLockoutPolicy().getLockoutFailureCount();
			int duration = authPolicy.getAccountLockoutPolicy().getLockoutDuration();
			int faulureInterval = authPolicy.getAccountLockoutPolicy().getLockoutFailureExpirationInterval();
			ps.setString(index++, authPolicy.getName());
			ps.setInt(index++, lockCount);
			if (duration != 0) {
				ps.setTimestamp(index++, new Timestamp(DateUtils.addMinutes(InternalDateUtil.getNow(), duration * -1).getTime()));
			}
			if (faulureInterval != 0) {
				ps.setTimestamp(index++, new Timestamp(DateUtils.addMinutes(InternalDateUtil.getNow(), faulureInterval * -1).getTime()));
			}
		} else if (SpecificType.EXPIRED_PASSWORD == cond.getType()) {
			ps.setString(index++, authPolicy.getName());
			//SQL側で設定
		} else if (SpecificType.LAST_LOGIN == cond.getType()) {
			if (cond.getLastLoginFromDate() != null) {
				ps.setTimestamp(index++, new Timestamp(adjustTimes(cond.getLastLoginFromDate(), 0, 0, 0, 0).getTime()));
			}
			if (cond.getLastLoginToDate() != null) {
				ps.setTimestamp(index++, new Timestamp(adjustTimes(cond.getLastLoginToDate(), 23, 59, 59, 999).getTime()));
			}
		}
	}

	public void getAccountSearchResultData(ResultSet rs, Predicate<BuiltinAccount> callback) throws SQLException {
		while(rs.next()) {
			BuiltinAccount account = getAccount(rs);
			if (!callback.test(account)) {
				break;
			}
		}
	}

	private String createAccountSearchWhereSQL(RdbAdapter rdb, Tenant tenant, UserSpecificCondition cond, AuthenticationPolicyDefinition authPolicy) {
		StringBuilder sb = new StringBuilder();
		sb.append("WHERE TENANT_ID = ? ");

		if (SpecificType.LOCKED == cond.getType()) {
			int duration = authPolicy.getAccountLockoutPolicy().getLockoutDuration();
			int faulureInterval = authPolicy.getAccountLockoutPolicy().getLockoutFailureExpirationInterval();
			sb.append(" AND POL_NAME = ? ");
			sb.append(" AND LOGIN_ERR_CNT >= ? ");
			if (duration != 0) {
				sb.append(" AND LOGIN_ERR_DATE >= ? ");
			}
			if (faulureInterval != 0) {
				sb.append(" AND LOGIN_ERR_DATE >= ? ");
			}
		} else if (SpecificType.EXPIRED_PASSWORD == cond.getType()) {

			int maxAge = authPolicy.getPasswordPolicy().getMaximumPasswordAge();
			int condDays = maxAge - cond.getPasswordRemainDays();
			sb.append(" AND POL_NAME = ? ");
			sb.append(" AND LAST_PASSWORD_CHANGE IS NOT NULL ");
			sb.append(" AND LAST_PASSWORD_CHANGE != " + rdb.toDateExpression(InternalDateUtil.getYukoDateTo()));
			if (SearchOperator.EQUAL == cond.getPasswordRemainDaysOparator()) {
				sb.append(" AND (" + rdb.cast(Types.TIMESTAMP, Types.DATE, rdb.addDate("LAST_PASSWORD_CHANGE", condDays), null, null) + ")");
				sb.append(" = ");
				sb.append(rdb.toDateExpression(InternalDateUtil.getNowForSqlDate()));
			} else if (SearchOperator.LESSEQUAL == cond.getPasswordRemainDaysOparator()) {
				sb.append(" AND (" + rdb.cast(Types.TIMESTAMP, Types.DATE, rdb.addDate("LAST_PASSWORD_CHANGE", condDays), null, null) + ")");
				sb.append(" <= ");
				sb.append(rdb.toDateExpression(InternalDateUtil.getNowForSqlDate()));
			}
		} else if (SpecificType.LAST_LOGIN == cond.getType()) {
			if (cond.getLastLoginFromDate() != null) {
				sb.append(" AND LAST_LOGIN_ON >= ?");
			}
			if (cond.getLastLoginToDate() != null) {
				sb.append(" AND LAST_LOGIN_ON <= ?");
			}

		}

		return sb.toString();
	}

	private java.sql.Date adjustTimes(java.sql.Date date, int hour, int minute, int second, int millisecond) {
		Calendar cal = DateUtil.getCalendar(true);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, millisecond);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	private BuiltinAccount getAccount(ResultSet rs) throws SQLException {
		BuiltinAccount ret = new BuiltinAccount();
		int index = 1;
		ret.setTenantId(rs.getInt(index++));
		ret.setAccountId(rs.getString(index++));
		ret.setPolicyName(rs.getString(index++));
		ret.setOid(rs.getString(index++));
		ret.setLastLoginOn(rs.getTimestamp(index++));
		ret.setLoginErrorCnt(rs.getInt(index++));
		ret.setLoginErrorDate(rs.getTimestamp(index++));
		ret.setLastPasswordChange(rs.getDate(index++));
		ret.setCreateUser(rs.getString(index++));
		ret.setCreateDate(rs.getTimestamp(index++));
		ret.setUpdateUser(rs.getString(index++));
		ret.setUpdateDate(rs.getTimestamp(index++));
		return ret;
	}

}
