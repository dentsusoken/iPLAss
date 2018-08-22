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

package org.iplass.mtp.impl.tools.auth.builtin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;

import org.iplass.mtp.auth.policy.definition.AuthenticationPolicyDefinition;
import org.iplass.mtp.impl.auth.authenticate.builtin.BuiltinAccount;
import org.iplass.mtp.impl.auth.authenticate.builtin.sql.AccountControlSQL;
import org.iplass.mtp.impl.auth.authenticate.builtin.sql.AccountSelectSQL;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.tools.auth.builtin.cond.UserSpecificCondition;
import org.iplass.mtp.impl.tools.auth.builtin.sql.AccountMaintenanceSelectSQL;
import org.iplass.mtp.tenant.Tenant;

public class AccountToolDao {

	private RdbAdapter rdb;
	private AccountMaintenanceSelectSQL accountMaintenance;
	private AccountSelectSQL accountSelect;
	private AccountControlSQL accountControl;

	public AccountToolDao(RdbAdapter rdb) {
		this.rdb = rdb;
		accountMaintenance = rdb.getQuerySqlCreator(AccountMaintenanceSelectSQL.class);
		accountSelect = rdb.getQuerySqlCreator(AccountSelectSQL.class);
		accountControl = rdb.getUpdateSqlCreator(AccountControlSQL.class);
	}


	public int countAccount(final UserSpecificCondition cond, final AuthenticationPolicyDefinition authPolicy) {
		final Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		SqlExecuter<Integer> executer = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {

				String sql = accountMaintenance.createAccountCountSQL(rdb, tenant, cond, authPolicy);

				PreparedStatement ps = getPreparedStatement(sql);
				accountMaintenance.setAccountCountParameter(rdb, ps, tenant, cond, authPolicy);
				int ret = 0;
				try (ResultSet rs = ps.executeQuery()) {
					ret = accountMaintenance.getAccountCountResultData(rs);
				}
				return ret;
			}
		};
		return executer.execute(rdb, true);
	}

	public void searchAccount(final UserSpecificCondition cond, final AuthenticationPolicyDefinition authPolicy,
			final int limit, final int offset, final Predicate<BuiltinAccount> callback) {
		final Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {

				String sql = accountMaintenance.createAccountSearchSQL(rdb, tenant, cond, authPolicy);

				//Limit対応
				if (limit > 0) {
					int limitOffset = 0;
					if (offset > 0) {
						limitOffset = offset;
					}
					sql = rdb.toLimitSql(sql, limit, limitOffset);
				}

				PreparedStatement ps = getPreparedStatement(sql);
				accountMaintenance.setAccountSearchParameter(rdb, ps, tenant, cond, authPolicy);
				try (ResultSet rs = ps.executeQuery()) {
					accountMaintenance.getAccountSearchResultData(rs, callback);
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	public BuiltinAccount getAccount(final String accountId) {
		SqlExecuter<BuiltinAccount> executer = new SqlExecuter<BuiltinAccount>() {
			@Override
			public BuiltinAccount logic() throws SQLException {
				Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
				PreparedStatement ps = getPreparedStatement(accountSelect.createAccountSQL());
				accountSelect.setAccountParameter(rdb, ps, tenant.getId(), accountId);
				try (ResultSet rs = ps.executeQuery()) {
					return accountSelect.getAccount(rs);
				}
			}
		};
		return executer.execute(rdb, true);
	}

	public void resetLoginErrorCnt(BuiltinAccount account) {

		SqlExecuter<BuiltinAccount> executer = new SqlExecuter<BuiltinAccount>() {
			@Override
			public BuiltinAccount logic() throws SQLException {

				PreparedStatement ps = getPreparedStatement(accountControl.createResetLoginErrorCntSQL(rdb));
				accountControl.setResetLoginErrorCntParameter(rdb, ps, account);
				int cnt = ps.executeUpdate();
				if (cnt == 0) {
					throw new RuntimeException("Update Fail. Maybe concurrent modification occured:accountId=" + account.getAccountId());
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}
}
