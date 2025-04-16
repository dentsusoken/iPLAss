/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.builtin;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.auth.login.CredentialUpdateException;
import org.iplass.mtp.impl.auth.authenticate.AuthenticationProvider;
import org.iplass.mtp.impl.auth.authenticate.builtin.sql.AccountControlSQL;
import org.iplass.mtp.impl.auth.authenticate.builtin.sql.AccountSelectSQL;
import org.iplass.mtp.impl.auth.authenticate.builtin.sql.PasswordHistorySelectSQL;
import org.iplass.mtp.impl.auth.authenticate.builtin.sql.PasswordHistoryUpdateSQL;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;
import org.iplass.mtp.spi.Config;

public class RdbAccountStore implements AccountStore {

	//TODO sqlの整理
	//TODO Preparedの利用箇所の整理（ステートメントキャッシュ前提としても、利用頻度少ないのは無駄）
	private AccountSelectSQL accountSelect;
	private AccountControlSQL accountControl;
	private PasswordHistorySelectSQL passSelect;
	private PasswordHistoryUpdateSQL passUpdate;

	private RdbAdapter rdb;

	@Override
	public void inited(AuthenticationProvider provider, Config config) {
		rdb = config.getDependentService(RdbAdapterService.class).getRdbAdapter();
		accountSelect = rdb.getQuerySqlCreator(AccountSelectSQL.class);
		accountControl = rdb.getUpdateSqlCreator(AccountControlSQL.class);
		passSelect = rdb.getQuerySqlCreator(PasswordHistorySelectSQL.class);
		passUpdate = rdb.getUpdateSqlCreator(PasswordHistoryUpdateSQL.class);
	}

	@Override
	public void updateAccountLoginStatus(final BuiltinAccount account) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(accountControl.createLoginStatUpdateSQL());
				accountControl.setLoginStatUpdateParameter(rdb, ps, account);
				int cnt = ps.executeUpdate();
				if (cnt != 1) {
					throw new RuntimeException("fail to update login status:" + account.getAccountId());
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void updatePassword(final Password pass, final String updateUser) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(accountControl.createUpdatePasswordSQL(rdb));
				Date date = null;
				if (pass.getUpdateDate() != null) {
					date = new Date(pass.getUpdateDate().getTime());
				}
				accountControl.setUpdatePasswordParameter(rdb, ps, pass.getTenantId(), pass.getUid(), pass.getConvertedPassword(), pass.getSalt(), date, updateUser);
				int cnt = ps.executeUpdate();
				if (cnt != 1) {
					throw new CredentialUpdateException(resourceString("impl.auth.authenticate.updateCredential.updateErr"));
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void registAccount(final BuiltinAccount account, final String registId) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(accountControl.createRegistSQL(rdb));
				accountControl.setRegistParameter(rdb, ps, account, registId);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void updateAccount(final BuiltinAccount account, final String updateUser) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(accountControl.createUpdateSQL(rdb));
				accountControl.setUpdateParameter(rdb, ps, account, updateUser);
				int cnt = ps.executeUpdate();
				if (cnt == 0) {
					throw new RuntimeException("Update Fail. Maybe concurrent modification occured:accountId=" + account.getAccountId());
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void removeAccount(final int tenantId, final String accountId) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(accountControl
						.createDeleteSQL(rdb));
				accountControl.setDeleteParameter(rdb, ps, tenantId, accountId);
				int cnt = ps.executeUpdate();
				if (cnt == 0) {
					throw new RuntimeException("Delete Fail. Maybe concurrent modification occured:accountId=" + accountId);
				}
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public BuiltinAccount getAccount(final int tenantId, final String accountId) {
		SqlExecuter<BuiltinAccount> executer = new SqlExecuter<BuiltinAccount>() {
			@Override
			public BuiltinAccount logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(accountSelect
						.createAccountSQL());
				accountSelect.setAccountParameter(rdb, ps, tenantId, accountId);
				try (ResultSet rs = ps.executeQuery()) {
					return accountSelect.getAccount(rs, rdb);
				}
			}
		};
		return executer.execute(rdb, true);
	}

	@Override
	public BuiltinAccount getAccountFromOid(final int tenantId, final String oid) {
		SqlExecuter<BuiltinAccount> executer = new SqlExecuter<BuiltinAccount>() {
			@Override
			public BuiltinAccount logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(accountSelect
						.createAccountFromOidSQL());
				accountSelect.setAccountFromOidParameter(rdb, ps, tenantId, oid);
				try (ResultSet rs = ps.executeQuery()) {
					return accountSelect.getAccount(rs, rdb);
				}
			}
		};
		return executer.execute(rdb, true);
	}

	@Override
	public void addPasswordHistory(final Password hi) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(passUpdate.createInsertSQL(rdb));
				passUpdate.setInsertParameter(rdb, ps, hi);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public List<Password> getPasswordHistory(final int tenantId, final String accountId) {
		SqlExecuter<List<Password>> executer = new SqlExecuter<List<Password>>() {
			@Override
			public List<Password> logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(passSelect.createSelectSQL());
				passSelect.setSelectParameter(rdb, ps, tenantId, accountId);
				List<Password> res = new ArrayList<>();
				try (ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						res.add(passSelect.toPassword(rs, rdb));
					}
				}
				return res;
			}
		};
		return executer.execute(rdb, true);
	}

	@Override
	public void deletePasswordHistory(final int tenantId, final String accountId) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(passUpdate.createDeleteSQL(rdb));
				passUpdate.setDeleteParameter(rdb, ps, tenantId, accountId);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void deletePasswordHistory(final int tenantId, final String accountId,
			final Timestamp dateBefore) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(passUpdate.createDeleteByDateSQL(rdb));
				passUpdate.setDeleteByDateParameter(rdb, ps, tenantId, accountId, dateBefore);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
	public void updatePasswordHistoryAccountId(final int tenantId,
			final String oldAccountId, final String newAccountId) {
		SqlExecuter<Void> executer = new SqlExecuter<Void>() {
			@Override
			public Void logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(passUpdate.createUpdateAccountIdSQL(rdb));
				passUpdate.setUpdateAccountIdSQL(rdb, ps, tenantId, oldAccountId, newAccountId);
				ps.executeUpdate();
				return null;
			}
		};
		executer.execute(rdb, true);
	}

	@Override
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

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
