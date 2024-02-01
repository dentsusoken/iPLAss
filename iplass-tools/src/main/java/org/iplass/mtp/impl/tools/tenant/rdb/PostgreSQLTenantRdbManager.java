/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.tenant.PartitionCreateParameter;
import org.iplass.mtp.impl.tools.tenant.PartitionInfo;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgreSQLTenantRdbManager extends DefaultTenantRdbManager {

	private static final Logger logger = LoggerFactory.getLogger(PostgreSQLTenantRdbManager.class);

	private static final String SQL_EXIST_TENANT = "select 1 where exists (select * from t_tenant where url = ?)";
	private static final String SQL_EXIST_TABLE = "select count(*) from information_schema.tables where table_schema = ? and lower(table_name) = lower(?)";

	private static final String SQL_LAST_TENANT_ID = "select last_value from seq_t_tenant_id where is_called = true";
	private static final String SQL_IS_PARTITIONED_TABLE = "select 1 where exists (select oid from pg_class where relname = ? and relkind = 'p')";
	private static final String SQL_SELECT_PARTITION_TABLE = "select partition_name from (select substring(table_name from ?) as partition_name from information_schema.tables where table_schema = ? and table_name like ?) as p group by partition_name order by length(partition_name) desc, partition_name desc";
	private static final String SQL_COUNT_SUB_PARTITION = "select count(*) from information_schema.tables where table_schema = ? and table_name like ?";

	private static final String SQL_CREATE_PARTITION_TABLE = "create table \"%s_%d\" (like \"%s\" including defaults including constraints)";
	private static final String SQL_CREATE_SUB_PARTITION_TABLE = "create table \"%s_%d_%d\" partition of \"%s_%d\" for values with (modulus %d, remainder %d)";
	private static final String SQL_WITH_SUB_PARTITION = " partition by hash (\"obj_def_id\")";
	private static final String SQL_DROP_PARTITION_TABLE = "drop table if exists \"%s_%d\" cascade";

	private static final String SQL_ADD_CHECK_CONSTRAINT = "alter table \"%s_%d\" add constraint \"%s_%d_check\" check (tenant_id >= %d and tenant_id < %d)";
	private static final String SQL_DROP_CHECK_CONSTRAINT = "alter table \"%s_%d\" drop constraint \"%s_%d_check\"";

	private static final String SQL_ATTACH_PARTITION_TABLE = "alter table \"%s\" attach partition \"%s_%d\" for values from (%d) to (%d)";
	private static final String SQL_DETACH_PARTITION_TABLE = "alter table \"%s\" detach partition \"%s_%d\"";

	private RdbAdapter adapter;

	public PostgreSQLTenantRdbManager(RdbAdapter adapter, TenantRdbManagerParameter parameter) {
		super(adapter, parameter);
		this.adapter = adapter;
	}

	@Override
	public boolean existsURL(String url) {
		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {
				//検索SQL実行
				PreparedStatement statement = getPreparedStatement(SQL_EXIST_TENANT);
				String key = !url.startsWith("/") ? "/" + url : url;
				statement.setString(1, key);
				try (ResultSet rs = statement.executeQuery()) {
					return rs.next();
				}
			}
		};
		return exec.execute(adapter, true);
	}

	@Override
	public boolean isSupportPartition() {
		// iPLAssでのパーティションサポートはPostgreSQL11以上
		// パーティションの作成・削除時に対象のテーブルがパーティションテーブルであるかを判定(isPartitionedTable)
		return true;
	}

	@Override
	public List<PartitionInfo> getPartitionInfo() {
		return Transaction.required(t -> {
			List<PartitionInfo> partitionList = new ArrayList<>();

			Stream.of(getTableList()).filter(table -> isPartitionTargetTable(table)).forEach(tableName -> {
				if (isStorageSpaceTable(tableName)) {
					getStorageSpacePostfix(true).stream().filter(postfix -> isExistsTable(toStorageSpaceTableName(tableName, postfix), true)).forEach(postfix -> {
						if (isPartitionedTable(toStorageSpaceTableName(tableName, postfix))) {
							partitionList.add(getTablePartitionInfo(tableName, postfix));
						}
					});
				} else {
					if (isExistsTable(tableName, false)) {
						if (isPartitionedTable(tableName)) {
							partitionList.add(getTablePartitionInfo(tableName, null));
						}
					}
				}
			});

			return partitionList;
		});
	}

	private String toStorageSpaceTableName(final String tableName, final String postfix) {
		return postfix != null ? tableName + postfix.toLowerCase() : tableName;
	}

	/**
	 * テーブルに対して作成されているPartition情報(最大のテナントID)を返します。
	 */
	private PartitionInfo getTablePartitionInfo(final String table, final String postfix) {
		SqlExecuter<PartitionInfo> exec = new SqlExecuter<PartitionInfo>() {

			@Override
			public PartitionInfo logic() throws SQLException {
				String tableName = toStorageSpaceTableName(table, postfix);

				PreparedStatement ps = getPreparedStatement(SQL_SELECT_PARTITION_TABLE);
				ps.setString(1, tableName + "\\_[0-9]+");
				ps.setString(2, adapter.getConnection().getSchema());
				ps.setString(3, tableName + "\\_%");

				int maxTenantId = -1;
				Set<String> partitionNames = new TreeSet<>();

				try (ResultSet rs = ps.executeQuery()) {
					boolean isHead = true;
					while (rs.next()) {
						String partitionName = rs.getString(1);
						if (StringUtil.isNotEmpty(partitionName)) {
							if (isHead) {
								// 先頭のテナントID(最大テナントID)取得
								try {
									maxTenantId = Integer.parseInt(partitionName.substring((tableName + "_").length()));
								} catch (NumberFormatException e) {
									logger.warn("cant parse the partition name:" + partitionName);
								}
								isHead = false;
							}
							partitionNames.add(partitionName);
						}
					}
				}

				return new PartitionInfo(tableName, postfix, partitionNames, maxTenantId);
			}
		};
		return exec.execute(adapter, true);
	}

	@Override
	public boolean createPartition(PartitionCreateParameter param, LogHandler logHandler) {
		return Transaction.requiresNew(t -> {
			doCreatePartition(param, logHandler);
			return true;
		});
	}

	private void doCreatePartition(PartitionCreateParameter param, final LogHandler logHandler) {
		// 有効なテナント情報を取得
		List<TenantInfo> validTenantList = getValidTenantInfoList();
		getPartitionInfo().forEach(info -> {
			createTablePartition(param, info, validTenantList, logHandler);
		});
	}

	private void createTablePartition(final PartitionCreateParameter param, final PartitionInfo partitionInfo, final List<TenantInfo> storeTenantList, final LogHandler logHandler) {
		if (partitionInfo.getMaxTenantId() >= param.getTenantId()) {
			// 既にPartitionは存在しているのでなにもしない
			// もし実際には存在しない場合でも途中に作ることはしない
			logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "skipPartitionMsg", partitionInfo.getMaxTenantId(), partitionInfo.getTableName()));
			return;
		}

		// 最終テナントIDを取得
		int lastTenantId = getLastTenantId();

		// Partitionの最大テナントIDを取得
		int maxPartitionId = partitionInfo.getMaxTenantId() > 0 ? partitionInfo.getMaxTenantId() : 0;

		// 実行するテナントIDを取得
		List<Integer> addTenantIdList = new ArrayList<Integer>();

		// 存在するテナントについては、テナントID分だけ作成(削除でDropされている分などは作成しない)
		// またこの処理によりカスタムStorageSpaceなどが途中で作成された場合もテナント分作成される
		if (maxPartitionId < lastTenantId) {
			storeTenantList.stream().filter(tenant -> tenant.getId() > maxPartitionId).forEach(tenant -> {
				addTenantIdList.add(tenant.getId());
				logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "createExistTenantPartitionMsg", tenant.getId()));
			});
		}

		// 最終のテナントIDから指定されたID分は1つずつ作成する
		for (int tenantId = lastTenantId + (param.isOnlyPartitionCreate() ? 1 : 0); tenantId <= param.getTenantId(); tenantId++) {
			// 存在しない場合のみ
			if (!partitionInfo.exists(tenantId)) {
				if (maxPartitionId >= tenantId) {
					// 削除時にパーティションを削除せずに残っている場合など、途中には作れないのでスキップ
					// PostgreSQLではテナントIDの採番をシーケンスで行うためパーティションの最大テナントIDが最終テナントIDを追い越すことはないためあり得ない
					logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "skipPartitionMsg", partitionInfo.getMaxTenantId(), partitionInfo.getTableName()));
					continue;
				}
				addTenantIdList.add(tenantId);
			}
		}

		addTenantIdList.forEach(tenantId -> {
			executeTableCreatePartition(param, partitionInfo, tenantId, logHandler);
		});
	}

	private void executeTableCreatePartition(final PartitionCreateParameter param, final PartitionInfo partitionInfo, final int tenantId, final LogHandler logHandler) {
		List<String> sqlList = new ArrayList<>();

		String tableName = partitionInfo.getTableName();

		int tmpSubPartitionSize = param.getSubPartitionSize();
		if (tenantId < param.getTenantId() && StringUtil.isNotBlank(partitionInfo.getPostfix())) {
			// 既存テナントの場合のサブパーティション数は対象テナントの標準テーブルと同じとする
			tmpSubPartitionSize = getSubPartitionSize(tableName.substring(0, tableName.length() - partitionInfo.getPostfix().length()), tenantId);
		}
		final int subPartitionSize = tmpSubPartitionSize;

		if (isSubPartitionTargetTable(tableName) && subPartitionSize > 0) {
			sqlList.add(toSqlCreatePartitionTableWithSubPartition(tableName, tenantId));
			IntStream.rangeClosed(0, subPartitionSize - 1).forEach(subNo -> {
				sqlList.add(toSqlCreateSubPartitionTable(tableName, tenantId, subPartitionSize, subNo));
			});
		} else {
			sqlList.add(toSqlCreatePartitionTable(tableName, tenantId));
		}
		sqlList.add(toSqlAddCheckConstraint(tableName, tenantId));
		sqlList.add(toSqlAttachPartitionTable(tableName, tenantId));
		sqlList.add(toSqlDropCheckConstraint(tableName, tenantId));

		// Partitionの作成を別Transactionで実行
		Transaction.requiresNew(t -> {
			SqlExecuter<Void> exec = new SqlExecuter<Void>() {
				@Override
				public Void logic() throws SQLException {
					Statement stmt = getStatement();

					for (String sql : sqlList) {
						stmt.addBatch(sql);
					}
					stmt.executeBatch();

					logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "createdPartitionMsg", tableName + "_" + tenantId));

					return null;
				}
			};
			exec.execute(adapter, true);
			return null;
		});
	}

	@Override
	public boolean dropPartition(PartitionDeleteParameter param, LogHandler logHandler) {
		boolean isSuccess = Transaction.requiresNew(t -> {
			doDropPartition(param, logHandler);
			return true;
		});
		return isSuccess;
	}

	private void doDropPartition(PartitionDeleteParameter param, LogHandler logHandler) {
		Stream.of(getTableList()).filter(table -> isPartitionTargetTable(table)).forEach(tableName -> {
			if (isStorageSpaceTable(tableName)) {
				// StorageSpaceの場合、Postfix分Loop
				getStorageSpacePostfix(true).stream().filter(postfix -> isPartitionedTable(toStorageSpaceTableName(tableName, postfix))).forEach(postfix -> {
					executeTableDropPartition(param, toStorageSpaceTableName(tableName, postfix), logHandler);
				});
			} else {
				if (isPartitionedTable(tableName)) {
					executeTableDropPartition(param, tableName, logHandler);
				}
			}
		});
	}

	private void executeTableDropPartition(final PartitionDeleteParameter param, final String tableName, final LogHandler logHandler) {
		if (!isExistsPartitionTable(tableName, param.getTenantId())) {
			return;
		}

		List<String> sqlList = new ArrayList<>();

		sqlList.add(toSqlDetachPartitionTable(tableName, param.getTenantId()));
		sqlList.add(toSqlDropPartitionTable(tableName, param.getTenantId()));

		// Partitionの削除を別Transactionで実行
		Transaction.requiresNew(t -> {
			SqlExecuter<Void> exec = new SqlExecuter<Void>() {
				@Override
				public Void logic() throws SQLException {
					Statement stmt = getStatement();

					for (String sql : sqlList) {
						stmt.addBatch(sql);
					}
					stmt.executeBatch();

					logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "droppedPartitionMsg", tableName + "_" + param.getTenantId()));

					return null;
				}
			};

			try {
				exec.execute(adapter, true);
			} catch (Exception e) {
				logHandler.warn("partition cant dropped ...:" + tableName + "_" + param.getTenantId(), e);
			}

			return null;
		});
	}

	@Override
	protected boolean isExistsTable(String tableName) {
		SqlExecuter<Boolean> exec = createCheckExistSqlExecuter(SQL_EXIST_TABLE, tableName);
		return exec.execute(adapter, true);
	}

	private boolean isExistsPartitionTable(String tableName, int tenantId) {
		return isExistsTable(tableName + "_" + tenantId);
	}

	private String toSqlCreatePartitionTable(String tableName, int tenantId) {
		return String.format(SQL_CREATE_PARTITION_TABLE, tableName, tenantId, tableName);
	}

	private String toSqlCreatePartitionTableWithSubPartition(String tableName, int tenantId) {
		return toSqlCreatePartitionTable(tableName, tenantId) + SQL_WITH_SUB_PARTITION;
	}

	private String toSqlCreateSubPartitionTable(String tableName, int tenantId, int subPartitionSize, int subNo) {
		return String.format(SQL_CREATE_SUB_PARTITION_TABLE, tableName, tenantId, subNo, tableName, tenantId, subPartitionSize, subNo);
	}

	private String toSqlAddCheckConstraint(String tableName, int tenantId) {
		return String.format(SQL_ADD_CHECK_CONSTRAINT, tableName, tenantId, tableName, tenantId, tenantId, tenantId + 1);
	}

	private String toSqlDropCheckConstraint(String tableName, int tenantId) {
		return String.format(SQL_DROP_CHECK_CONSTRAINT, tableName, tenantId, tableName, tenantId);
	}

	private String toSqlAttachPartitionTable(String tableName, int tenantId) {
		return String.format(SQL_ATTACH_PARTITION_TABLE, tableName, tableName, tenantId, tenantId, tenantId + 1);
	}

	private String toSqlDetachPartitionTable(String tableName, int tenantId) {
		return String.format(SQL_DETACH_PARTITION_TABLE, tableName, tableName, tenantId);
	}

	private String toSqlDropPartitionTable(String tableName, int tenantId) {
		return String.format(SQL_DROP_PARTITION_TABLE, tableName, tenantId);
	}

	private SqlExecuter<Boolean> createCheckExistSqlExecuter(String sql, String tableName) {
		return new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(sql);
				ps.setString(1, adapter.getConnection().getSchema());
				ps.setString(2, tableName);

				int count = 0;
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						count = rs.getInt(1);
					}
				}
				return count > 0;
			}
		};
	}

	private int getLastTenantId() {
		return new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				Statement stmt = getStatement();
				try (ResultSet rs = stmt.executeQuery(SQL_LAST_TENANT_ID)) {
					return rs.next() ? Integer.valueOf(rs.getInt(1)) : Integer.valueOf(0);
				}
			}
		}.execute(adapter, true).intValue();
	}

	private boolean isPartitionedTable(String tableName) {
		return new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(SQL_IS_PARTITIONED_TABLE);
				ps.setString(1, tableName);
				try (ResultSet rs = ps.executeQuery()) {
					return Boolean.valueOf(rs.next());
				}
			}
		}.execute(adapter, true).booleanValue();
	}

	private int getSubPartitionSize(String tableName, int tenantId) {
		return new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				PreparedStatement ps = getPreparedStatement(SQL_COUNT_SUB_PARTITION);
				ps.setString(1, adapter.getConnection().getSchema());
				ps.setString(2, tableName + "\\_" + tenantId + "\\_%");
				try (ResultSet rs = ps.executeQuery()) {
					rs.next();
					return Integer.valueOf(rs.getInt(1));
				}
			}
		}.execute(adapter, true).intValue();
	}

	private String getPartitionResourceMessage(String lang, String suffix, Object... args) {
		return ToolsResourceBundleUtil.resourceString(lang, "tenant.partition." + suffix, args);
	}

	@Override
	protected SqlExecuter<Integer> getTenantRecordDeleteExecuter(int tenantId, String tableName, String deletionUnitColumns,
			int deleteRows) {

		if (StringUtil.isNotEmpty(deletionUnitColumns)) {
			// ユニークカラムの指定がある場合のSQLExecuter
			return new SqlExecuter<Integer>() {
				@Override
				public Integer logic() throws SQLException {
					String sql = "delete from " + tableName + " where (" + deletionUnitColumns + ") in (select " + deletionUnitColumns
							+ " from " + tableName + " where tenant_id = ? limit ?)";
					PreparedStatement ps = getPreparedStatement(sql);
					ps.setInt(1, tenantId);
					ps.setInt(2, deleteRows);

					return ps.executeUpdate();
				}
			};
		}

		// ユニークカラムの指定が無い場合の SQLExecuter
		return new SqlExecuter<Integer>() {
			@Override
			public Integer logic() throws SQLException {
				String sql = "delete from " + tableName + " where tenant_id = ?";
				PreparedStatement ps = getPreparedStatement(sql);
				ps.setInt(1, tenantId);

				return ps.executeUpdate();
			}
		};
	}

}
