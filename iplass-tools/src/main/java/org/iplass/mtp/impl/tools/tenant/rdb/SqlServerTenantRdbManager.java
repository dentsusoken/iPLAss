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

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.tenant.PartitionCreateParameter;
import org.iplass.mtp.impl.tools.tenant.PartitionInfo;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.transaction.Transaction;

public class SqlServerTenantRdbManager extends DefaultTenantRdbManager {

	private static final String FILEGROUP_NAME = "FG_MTDB";
	private static final String PARTITION_FUNCTION_NAME = "PF_MTDB";
	private static final String PARTITION_SCHEME_NAME = "PS_MTDB";

	private static final String FMT_FILE = "%s_%d";
	private static final String FMT_FILE_NAME = FMT_FILE + ".ndf";
	private static final String FMT_FILEGROUP_NAME = FILEGROUP_NAME + "_%d";

	private static final String SQL_TENANT_EXISTS = "select 1 where exists (select * from t_tenant where url = ?)";

	private static final String SQL_GET_DB_NAME = "SELECT DB_NAME()";
	private static final String SQL_GET_DB_FILE_PATH_TEMPLATE = "SELECT LEFT(PHYSICAL_NAME, LEN(PHYSICAL_NAME) - CHARINDEX('\\', REVERSE(PHYSICAL_NAME) + '\\'))"
			+ "FROM (SELECT PHYSICAL_NAME FROM SYS.DATABASE_FILES WHERE NAME = '%s') AS PHYSICAL_NAME";
	private static final String SQL_ADD_FILEGROUP = "ALTER DATABASE %s ADD FILEGROUP %s";
	private static final String SQL_ADD_FILEGROUP_FILE = "ALTER DATABASE %s ADD FILE ("
			+ "NAME = %s, "
			+ "FILENAME = '%s', "
			+ "SIZE = 5MB, "
			+ "FILEGROWTH = 1MB) TO FILEGROUP %s";
	private static final String SQL_MOD_PARTITION_FUNCTION = "ALTER PARTITION FUNCTION " + PARTITION_FUNCTION_NAME + "() SPLIT RANGE (%d)";
	private static final String SQL_MOD_PARTITION_SCHEME = "ALTER PARTITION SCHEME " + PARTITION_SCHEME_NAME + " NEXT USED %s";
	private static final String SQL_EXIST_PARTITION_FUNCTION = "SELECT 'X' FROM SYS.PARTITION_FUNCTIONS WHERE NAME='"+ PARTITION_FUNCTION_NAME + "'";

	private static final String SQLSERVER_EXIST_TABLE_SQL = "select count(*) from sys.tables where lower(name) = lower(?)";
	private static final String SQLSERVER_EXIST_SYNONYM_SQL = "select count(*) from sys.synonyms where lower(name) = lower(?)";

	private RdbAdapter adapter;
	/** ファイルパスを取得するSQL。DBのOSによって区切り文字が変わる為、OSによって変更する。 */
	private String sqlGetDbFilePath;

	public SqlServerTenantRdbManager(RdbAdapter adapter, TenantRdbManagerParameter parameter) {
		super(adapter, parameter);
		this.adapter = adapter;
		// Windows であれば "\", それ以外は "/"
		String pathSeparator = isDbServerOSWindows(adapter) ? "\\\\" : "/";
		// \\ を pathSeparator に置換する
		this.sqlGetDbFilePath = SQL_GET_DB_FILE_PATH_TEMPLATE.replaceAll("\\\\", pathSeparator);
	}

	@Override
	public boolean existsURL(final String url) {

		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {

			@Override
			public Boolean logic() throws SQLException {
				//検索SQL実行
				PreparedStatement statement = getPreparedStatement(SQL_TENANT_EXISTS);
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
	public boolean isSupportPartition() {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {
				return getPreparedStatement(SQL_EXIST_PARTITION_FUNCTION).executeQuery().next();
			}
		};
		return exec.execute(adapter, true).booleanValue();
	}

	@Override
	public List<PartitionInfo> getPartitionInfo() {
		// TODO
		return null;
	}

	@Override
	public boolean createPartition(PartitionCreateParameter param, LogHandler logHandler) {

		if (param.getTenantId() <= 1) {
			// テナントIDが'1'のパーティションはテーブル作成時(DDL実行時)に作成済みのため
			return true;
		}

		if (!isSupportPartition()) {
			return true;
		}

		return createPartitionInternal(param, logHandler);
	}

	private boolean createPartitionInternal(final PartitionCreateParameter param, final LogHandler logHandler) {
		try {
			return Transaction.required(transaction -> {
					transaction.commit();	// 後続のパーティション構成の変更でデッドロックが発生するのを防ぐため

					doCreatePartition(param, logHandler);

					logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "createdPartitionMsg", param.getTenantId()));

					return Boolean.TRUE;
			}).booleanValue();
		} catch (Throwable e) {
			logHandler.error(getCommonResourceMessage(param.getLoggerLanguage(), "errorMsg", e.getMessage()));
			return false;
		}
	}

	private void doCreatePartition(final PartitionCreateParameter param, final LogHandler logHandler) {
		String dbName = getDBName();

		// ファイルグループの追加
		String fgn = addFileGroup(dbName, param.getTenantId());

		// ファイルグループへのファイルの追加
		addFileToFileGroup(dbName, param.getTenantId(), fgn);

		// パーティションスキーマの変更
		modifyPartitionScheme(param.getTenantId(), fgn);

		// パーティションファンクションの変更
		modifyPartitionFunction(param.getTenantId());
	}

	private String getDBName() {
		SqlExecuter<String> exec = new SqlExecuter<String>() {
			@Override
			public String logic() throws SQLException {
				ResultSet rs = getPreparedStatement(SQL_GET_DB_NAME).executeQuery();
				rs.next();
				return rs.getString(1);
			}
		};
		return exec.execute(adapter, true);
	}

	private String getDBFilePath(final String dbName) {
		SqlExecuter<String> exec = new SqlExecuter<String>() {
			@Override
			public String logic() throws SQLException {
				String sql = String.format(sqlGetDbFilePath, dbName);
				ResultSet rs = getPreparedStatement(sql).executeQuery();
				rs.next();
				return rs.getString(1);
			}
		};
		return exec.execute(adapter, true);
	}

	private String addFileGroup(final String dbName, final int tenantId) {
		String fgn = String.format(FMT_FILEGROUP_NAME, tenantId);
		String sql = String.format(SQL_ADD_FILEGROUP, dbName, fgn);

		executeAlter(sql);

		return fgn;
	}

	private void addFileToFileGroup(final String dbName, final int tenantId, final String fileGroupName) {
		String sql = String.format(SQL_ADD_FILEGROUP_FILE,
				dbName,
				String.format(FMT_FILE, dbName, tenantId),
				getDBFilePath(dbName) + File.separator + String.format(FMT_FILE_NAME, dbName, tenantId),
				fileGroupName);

		executeAlter(sql);
	}

	private void modifyPartitionScheme(final int tenantId, final String fileGroupName) {
		String sql = String.format(SQL_MOD_PARTITION_SCHEME, fileGroupName);
		executeAlter(sql);
	}

	private void modifyPartitionFunction(final int tenantId) {
		String sql = String.format(SQL_MOD_PARTITION_FUNCTION, tenantId);
		executeAlter(sql);
	}

	private void executeAlter(final String sql) {
		Transaction.requiresNew(t -> {
				SqlExecuter<Void> exec = new SqlExecuter<Void>() {
					@Override
					public Void logic() throws SQLException {
						getPreparedStatement(sql).execute();
						return null;
					}
				};
				exec.execute(adapter, true);
				return null;
		});
	}

	@Override
	public boolean dropPartition(PartitionDeleteParameter param, LogHandler logHandler) {
		//TODO SQLServerはDropできない？
		return true;
	}

	@Override
	protected boolean isExistsTable(String tableName) {

		SqlExecuter<Boolean> exec = createCheckExistSqlExecuter(SQLSERVER_EXIST_TABLE_SQL, tableName);
		if (exec.execute(adapter, true)) {
			return true;
		}
		// SQLServerの場合はシノニムもチェック
		exec = createCheckExistSqlExecuter(SQLSERVER_EXIST_SYNONYM_SQL, tableName);
		return exec.execute(adapter, true);
	}

	private SqlExecuter<Boolean> createCheckExistSqlExecuter(final String sql, final String tableName) {
		return new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {

				PreparedStatement ps = getPreparedStatement(sql);
				ps.setString(1, tableName);

				ResultSet rs = ps.executeQuery();
				int count = 0;
				try {
					if(rs.next()) {
						count = rs.getInt(1);
					}
				} finally {
					rs.close();
				}
				if (count <= 0) {
					return false;
				}
				return true;
			}
		};
	}

	private String getPartitionResourceMessage(String lang, String suffix, Object... args) {
		return ToolsResourceBundleUtil.resourceString(lang, "tenant.partition." + suffix, args);
	}

	private String getCommonResourceMessage(String lang, String subKey, Object... args) {
		return ToolsResourceBundleUtil.commonResourceString(lang, subKey, args);
	}

	@Override
	protected SqlExecuter<Integer> getTenantRecordDeleteExecuter(int tenantId, String tableName, String deletionUnitColumns,
			int deleteRows) {
		return new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				String sql = "delete top(?) from " + tableName + " where tenant_id = ?";
				PreparedStatement ps = getPreparedStatement(sql);
				ps.setInt(1, deleteRows);
				ps.setInt(2, tenantId);

				return ps.executeUpdate();
			}
		};
	}

	/**
	 * DBサーバのOSがWindowsであるか確認する
	 *
	 * @param adapter RdbAdapter
	 * @return DBサーバのOSがWindowsの場合 true
	 */
	protected boolean isDbServerOSWindows(RdbAdapter adapter) {
		SqlExecuter<String> executer = new SqlExecuter<String>() {
			@Override
			public String logic() throws SQLException {
				String sql = "select @@VERSION";
				Statement ps = getStatement();

				try (ResultSet rs = ps.executeQuery(sql)) {
					return rs.next() ? rs.getString(1) : "";
				}
			}
		};
		String version = executer.execute(adapter, true);
		// @@VERSION に WINDOWS という文字パターンが見つかれば、true を返却する
		return version.toUpperCase().indexOf("WINDOWS") > 0;
	}

}
