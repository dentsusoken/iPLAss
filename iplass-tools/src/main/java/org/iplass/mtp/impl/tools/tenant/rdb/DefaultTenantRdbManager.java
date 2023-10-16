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

	/** TenantRdbManager パラメータ */
	private TenantRdbManagerParameter tenantRdbManagerParameter;

	public DefaultTenantRdbManager(RdbAdapter adapter, TenantRdbManagerParameter tenantRdbManagerParameter) {
		this.adapter = adapter;
		this.tenantRdbManagerParameter = tenantRdbManagerParameter;
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
		tenant.setYukoDateFrom(rs.getDate(6, adapter.javaCalendar()));
		tenant.setYukoDateTo(rs.getDate(7, adapter.javaCalendar()));
		tenant.setCreateUser(rs.getString(8));
		tenant.setCreateDate(rs.getTimestamp(9, adapter.rdbCalendar()));
		tenant.setUpdateUser(rs.getString(10));
		tenant.setUpdateDate(rs.getTimestamp(11, adapter.rdbCalendar()));

		return tenant;
	}

	@Override
	public boolean deleteTenant(final TenantDeleteParameter param, final boolean deleteAccount, final LogHandler logHandler) {
		// その他の削除（各テーブル削除時に、個別トランザクションを開始し処理を行う）
		deleteTenantTables(param, logHandler);

		boolean isSuccess = Transaction.requiresNew(t -> {

			//アカウントの削除
			if (deleteAccount) {
				deleteAccountTable(param, logHandler);
			}

			// テナントの削除
			deleteTenantTable(param, logHandler);

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
			String deletionUnitColumns = getDeletionUnitColumns(tableName);
			if (isStorageSpaceTable(tableName)) {
				//StorageSpaceの場合、Postfix分Loop
				for (String postfix : getStorageSpacePostfix(false)) {
					String storageSpaceTableName = tableName + postfix;
					if (isExistsTable(storageSpaceTableName, true)) {
						int row = deleteTenantRecords(param, storageSpaceTableName, deletionUnitColumns, logHandler);
						logHandler.info(getDeleteResourceMessage(param.getLoggerLanguage(), "deletedTableMsg", storageSpaceTableName, row));
					}
				}
			} else {
				if (isExistsTable(tableName, false)) {
					int row = deleteTenantRecords(param, tableName, deletionUnitColumns, logHandler);
					logHandler.info(getDeleteResourceMessage(param.getLoggerLanguage(), "deletedTableMsg", tableName, row));
				}
			}
		}
	}

	/**
	 * テナントレコードを削除する
	 *
	 * <p>
	 * 削除単位カラムは PostgreSQL タイプの削除時のみ利用する変数。
	 * </p>
	 *
	 * @see #getTenantRecordDeleteExecuter(int, String, String, int)
	 * @param param テナント削除パラメーター
	 * @param tableName 物理テーブル名
	 * @param deletionUnitColumns 物理テーブルの削除単位カラム (値例： column1, column2, .... )
	 * @param logHandler ログハンドラ
	 * @return レコード削除件数
	 */
	private int deleteTenantRecords(TenantDeleteParameter param, String tableName, String deletionUnitColumns,
			LogHandler logHandler) {
		int actualDeletedRows = 0;
		int deleteRows = getTenantRdbManagerParameter().getDeleteRows();
		boolean isContinue = true;

		SqlExecuter<Integer> tenantRecordDeleteExecuter = getTenantRecordDeleteExecuter(param.getTenantId(), tableName,
				deletionUnitColumns, deleteRows);

		do {
			// トランザクション開始し、削除実行
			Integer actual = Transaction.<Integer> requiresNew(t -> {
				return tenantRecordDeleteExecuter.execute(adapter, true);
			});

			// 削除件数の加算
			actualDeletedRows += actual;
			// 削除の継続判断（削除予定行よりも実際削除行が多いもしくは同一であれば継続。PKが無いテーブルもあるため、想定よりも多く削除する可能性があると考えられる）
			isContinue = deleteRows <= actual;

			// 処理継続時のみログ出力（最終的な削除ログは、呼び出し元で出力）
			if (isContinue) {
				logHandler.info(getDeleteResourceMessage(param.getLoggerLanguage(), "deletingTableMsg", tableName, actualDeletedRows));
			}
		} while (isContinue);

		return actualDeletedRows;
	}

	/**
	 * テナントレコードを削除するSQLExecuterインスタンスを取得する
	 *
	 * <p>
	 * 大量データ場合に一度に大量削除を行わないように deleteRows で指定された件数ずつ削除を行う。
	 * 件数削除が実施できない場合（limit 句が利用できず、deletionUnitColumns の指定が無い場合。実質、PostgreSQL のみ）はテーブルのテナントID単位で削除を行う。
	 * </p>
	 *
	 * <p>
	 * 削除時の件数指定方法（PostgreSQL のみ DELETE 時に件数指定ができない）
	 * <ul>
	 * <li>MySQL: <code>delete from ${TABLE_NAME} where tenant_id = ${TENANT_ID} limit ${DELETE_NUMBER}</code></li>
	 * <li>Oracle: <code>delete from ${TABLE_NAME} where tenant_id = ${TENANT_ID} and rownum &lt;= ${DELETE_NUMBER}</code></li>
	 * <li>PostgreSQL: <code>delete from ${TABLE_NAME} where ( ${DELETION_UNIT_COLS} ) in ( select ${DELETION_UNIT_COLS} from ${TABLE_NAME} where tenant_id = ${TENANT_ID} limit ${DELETE_NUMBER} )</code></li>
	 * <li>SQL Server: <code>delete top(${DELETE_NUMBER}) from ${TABLE_NAME} where tenant_id = ${TENANT_ID}</code></li>
	 * </ul>
	 * </p>
	 *
	 * @param tenantId 削除対象テナントID
	 * @param tableName 削除対象テーブル名
	 * @param deletionUnitColumns 削除対象テーブルの削除単位カラム。削除単位カラム空文字が設定される場合もある（旧バージョン互換テーブル）。{@link #getDeletionUnitColumns(String)} で取得する。
	 * @param deleteRows 削除行数
	 * @return SQLExecuterインスタンス
	 */
	abstract protected SqlExecuter<Integer> getTenantRecordDeleteExecuter(int tenantId, String tableName, String deletionUnitColumns,
			int deleteRows);

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

	/**
	 * TenantRdbManager パラメータを取得する
	 * @return TenantRdbManager パラメータ
	 */
	protected TenantRdbManagerParameter getTenantRdbManagerParameter() {
		return this.tenantRdbManagerParameter;
	}

	/**
	 * テーブル削除時の削除単位カラムを取得する
	 *
	 * <p>
	 * 削除単位カラムはユニークキーに近しいカラムを指定する。テーブルによってはカラムの指定が無い場合（空文字）もあり得る。
	 * </p>
	 *
	 * @param tableName テーブル名
	 * @return 削除単位カラム
	 */
	private String getDeletionUnitColumns(String tableName) {
		return TenantRdbConstants.TABLE_LIST_DELETION_UNIT_COLS.get(tableName);
	}

	private String getDeleteResourceMessage(String lang, String suffix, Object... args) {
		return ToolsResourceBundleUtil.resourceString(lang, "tenant.delete." + suffix, args);
	}

}
