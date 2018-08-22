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

package org.iplass.mtp.impl.tools.tenant.rdb;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.grdb.GRdbDataStore;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.tenant.TenantDeleteParameter;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;

public abstract class DefaultTenantRdbManager implements TenantRdbManager {

	private static final String TENANT_EXISTS_SQL = "select 1 from dual where exists(select * from t_tenant where url = ?)";

	private static final String TENANT_SELECT_SQL = "select id, name, description, host_name, url," +
			"yuko_date_from, yuko_date_to, cre_user, cre_date, up_user, up_date " +
			"from t_tenant ";

	private static final String ALL_TENANT_SQL = TENANT_SELECT_SQL +
			"order by id ";

	private static final String VALID_TENANT_SQL = TENANT_SELECT_SQL +
			"where yuko_date_from <= ? and yuko_date_to >= ? " +
			"order by id ";

	private static final String GET_TENANT_SQL = TENANT_SELECT_SQL +
			"where url = ? " +
			"order by id ";

	private static final String DELETE_TENANT_SQL = "delete from t_tenant where id = ?";

	private static final String DELETE_ACCOUNT_SQL = "delete from t_account where tenant_id = ? ";

	private RdbAdapter adapter;

	public DefaultTenantRdbManager(RdbAdapter adapter) {
		this.adapter = adapter;
	}

	protected abstract boolean isExistsTable(String tableName);

	@Override
	public boolean existsURL(final String url) {

		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {
				//検索SQL実行
				PreparedStatement statement = getPreparedStatement(TENANT_EXISTS_SQL);
				String key = !url.startsWith("/") ? "/" + url : url;
				statement.setString(1, key);
				ResultSet rs = statement.executeQuery();
				try {
					return rs.next();
				} finally {
					rs.close();
				}
			}
		};
		return exec.execute(adapter, true);
	}

	@Override
	public TenantInfo getTenantInfo(final String url) {
		SqlExecuter<TenantInfo> exec = new SqlExecuter<TenantInfo>() {

			@Override
			public TenantInfo logic() throws SQLException {
				// SQLの取得

				// 検索SQL実行
				PreparedStatement statement = getPreparedStatement(GET_TENANT_SQL);
				String key = !url.startsWith("/") ? "/" + url : url;
				statement.setString(1, key);
				ResultSet rs = statement.executeQuery();
				try {
					if (rs.next()) {
						TenantInfo tenant = convert(rs);
						return tenant;
					}
				} finally {
					rs.close();
				}

				return null;
			}
		};
		return exec.execute(adapter, true);
	}

	@Override
	public List<TenantInfo> getValidTenantInfoList() {
		return getTenantInfoList(true);
	}

	@Override
	public List<TenantInfo> getAllTenantInfoList() {
		return getTenantInfoList(false);
	}

	private List<TenantInfo> getTenantInfoList(final boolean onlyValid) {
		SqlExecuter<List<TenantInfo>> exec = new SqlExecuter<List<TenantInfo>>() {

			@Override
			public List<TenantInfo> logic() throws SQLException {
				// SQLの取得

				//検索SQL実行
				PreparedStatement statement = null;
				if (onlyValid) {
					Date now = InternalDateUtil.getNowForSqlDate();
					statement = getPreparedStatement(VALID_TENANT_SQL);
					statement.setDate(1, now);
					statement.setDate(2, now);
				} else {
					statement = getPreparedStatement(ALL_TENANT_SQL);
				}
				ResultSet rs = statement.executeQuery();
				List<TenantInfo> result = new ArrayList<TenantInfo>();
				try {
					while(rs.next()) {
						TenantInfo tenant = convert(rs);
						result.add(tenant);
					}
				} finally {
					rs.close();
				}

				return result;
			}
		};
		return exec.execute(adapter, true);
	}

	private TenantInfo convert(ResultSet rs) throws SQLException {

		TenantInfo tenant = new TenantInfo();
		tenant.setId(rs.getInt(1));
		tenant.setName(rs.getString(2));
		tenant.setDescription(rs.getString(3));
		tenant.setHostName(rs.getString(4));
		tenant.setUrl(rs.getString(5));
		tenant.setYukoDateFrom(rs.getDate(6));
		tenant.setYukoDateTo(rs.getDate(7));
		tenant.setCreateUser(rs.getString(8));
		tenant.setCreateDate(rs.getTimestamp(9));
		tenant.setUpdateUser(rs.getString(10));
		tenant.setUpdateDate(rs.getTimestamp(11));

		return tenant;
	}

	@Override
	public boolean deleteTenant(final TenantDeleteParameter param, final boolean deleteAccount, final LogHandler logHandler) {

		boolean isSuccess = Transaction.requiresNew(t -> {

				//テナントの削除
				deleteTenantTable(param, logHandler);

				//アカウントの削除
				if (deleteAccount) {
					deleteAccountTable(param, logHandler);
				}

				//その他の削除
				deleteTenantTables(param, logHandler);

				return true;
		});

		if (isSuccess) {
			//Partitionの削除
			if (isSupportPartition()) {
				PartitionDeleteParameter partitionParam = new PartitionDeleteParameter();
				partitionParam.setTenantId(param.getTenantId());
				partitionParam.setLoggerLanguage(param.getLoggerLanguage());
				isSuccess = dropPartition(partitionParam, logHandler);
			}
		}

		return isSuccess;
	}

	private void deleteTenantTable(final TenantDeleteParameter param, LogHandler logHandler) {

		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(DELETE_TENANT_SQL);
				ps.setInt(1, param.getTenantId());

				return ps.executeUpdate();
			}
		};
		int row = exec.execute(adapter, true);
		logHandler.info(getDeleteResourceMessage(param.getLoggerLanguage(), "deletedTableMsg", "t_tenant", row));
	}

	private void deleteAccountTable(final TenantDeleteParameter param, LogHandler logHandler) {

		//テナントIDを元にT_ACCOUNTデータを削除
		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {

				// 対象アカウントを削除
				PreparedStatement ps = getPreparedStatement(DELETE_ACCOUNT_SQL);
				ps.setInt(1, param.getTenantId());
				return ps.executeUpdate();
			}
		};
		int row = exec.execute(adapter, true);
		logHandler.info(getDeleteResourceMessage(param.getLoggerLanguage(), "deletedTableMsg", "t_account", row));
	}

	private void deleteTenantTables(TenantDeleteParameter param, LogHandler logHandler) {

		for (String tableName : getTableList()) {
			if (isStorageSpaceTable(tableName)) {
				//StorageSpaceの場合、Postfix分Loop
				for (String postfix : getStorageSpacePostfix(false)) {
					String storageSpaceTableName = tableName + postfix;
					if (isExistsTable(storageSpaceTableName, true)) {
						int row = deleteTenantTables(param.getTenantId(), storageSpaceTableName);
						logHandler.info(getDeleteResourceMessage(param.getLoggerLanguage(), "deletedTableMsg", storageSpaceTableName, row));
					}
				}
			} else {
				if (isExistsTable(tableName, false)) {
					int row = deleteTenantTables(param.getTenantId(), tableName);
					logHandler.info(getDeleteResourceMessage(param.getLoggerLanguage(), "deletedTableMsg", tableName, row));
				}
			}
		}
	}

	private int deleteTenantTables(int tenantId, String tableName) {

		SqlExecuter<Integer> exec = new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				String sql = "delete from " + tableName + " where tenant_id = ?";
				PreparedStatement ps = getPreparedStatement(sql);
				ps.setInt(1, tenantId);

				return ps.executeUpdate();
			}
		};
		return exec.execute(adapter, true);
	}

	protected String[] getTableList() {
		return TenantRdbConstants.TABLE_LIST;
	}

	/**
	 * 対象のテーブルがStorageSpace用のテーブルかを判定します。
	 *
	 * @param tableName テーブル名
	 * @return true：StorageSpace
	 */
	protected boolean isStorageSpaceTable(String tableName) {
		return TenantRdbConstants.STORAGE_SPACE_TABLE.contains(tableName);
	}

	protected boolean isPartitionTargetTable(String tableName) {
		//除外対象テーブルチェック
		return !TenantRdbConstants.EXCLUDE_PARTITION_TABLE.contains(tableName);
	}

	protected boolean isSubPartitionTargetTable(String tableName) {
		//除外対象テーブルチェック
		return !TenantRdbConstants.EXCLUDE_SUB_PARTITION_TABLE.contains(tableName);
	}

	protected int getMaxSubPartitionCount() {
		return TenantRdbConstants.MAX_SUBPARTITION;
	}

	/**
	 * <p>StorageSpaceに定義されているテーブル名のPostfixのリストを返します。</p>
	 *
	 * @param isPartition Partitionが対象か
	 * @return StorageSpaceのPostfixのリスト
	 */
	protected List<String> getStorageSpacePostfix(boolean isPartition) {
		GRdbDataStore store = (GRdbDataStore)ServiceRegistry.getRegistry().getService(StoreService.class).getDataStore();

		List<String> postfixList = new ArrayList<String>();
		for (StorageSpaceMap e: store.getStorageSpaceMap().values()) {
			if (isPartition) {
				//Partitionの場合はカスタムは除外
				if (e.isCustomPartition()) {
					continue;
				}
			}
			List<String> allPostFix = e.allTableNamePostfix();
			for (String pf: allPostFix) {
				if (pf != null) {
					postfixList.add("__" + pf);
				} else {
					postfixList.add("");
				}
			}
		}
		return postfixList;
	}

	protected boolean isExistsTable(final String tableName, final boolean isStorageSpace) {
		if (isStorageSpace) {
			//StorageSpaceの場合は、動的にテーブル名が変わるので無条件で存在チェックを行う
			return isExistsTable(tableName);
		} else {
			//存在チェック対象テーブルチェック
			if (TenantRdbConstants.CHECK_EXIST_TABLE.contains(tableName)) {
				return isExistsTable(tableName);
			}
		}

		//存在チェック対象以外は、true
		return true;
	}

	private String getDeleteResourceMessage(String lang, String suffix, Object... args) {
		return ToolsResourceBundleUtil.resourceString(lang, "tenant.delete." + suffix, args);
	}

}
