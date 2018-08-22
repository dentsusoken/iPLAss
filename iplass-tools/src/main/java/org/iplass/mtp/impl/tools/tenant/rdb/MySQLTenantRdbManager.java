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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

public class MySQLTenantRdbManager extends DefaultTenantRdbManager {

	private static Logger logger = LoggerFactory.getLogger(MySQLTenantRdbManager.class);

	private static final String MYSQL_EXIST_TABLE_SQL = "select count(*) from information_schema.tables where table_name = ?";
	private static final String MYSQL_EXIST_TABLE_SQL_WITH_SCHEMA = "select count(*) from information_schema.tables where table_schema = ? and table_name = ?";

	private static final String MYSQL_MAX_PARTITION_SQL = "select partition_name from information_schema.partitions where table_name = ? order by length(partition_name) desc , partition_name desc";
	private static final String MYSQL_MAX_PARTITION_SQL_WITH_SCHEMA = "select partition_name from information_schema.partitions where table_schema = ? and table_name = ? order by length(partition_name) desc , partition_name desc";

	private static final String MYSQL_EXIST_PARTITION_SQL = "select count(*) from information_schema.partitions where table_name = ? and partition_name = ?";
	private static final String MYSQL_EXIST_PARTITION_SQL_WITH_SCHEMA = "select count(*) from information_schema.partitions where table_schema = ? and table_name = ? and partition_name = ?";

	private RdbAdapter adapter;

	public MySQLTenantRdbManager(RdbAdapter adapter) {
		super(adapter);
		this.adapter = adapter;
	}

	@Override
	public boolean isSupportPartition() {
		return true;
	}

	@Override
	public List<PartitionInfo> getPartitionInfo() {

		return Transaction.required(t -> {

				List<PartitionInfo> ret = new ArrayList<PartitionInfo>();

				for (String tableName : getTableList()) {
					if (isStorageSpaceTable(tableName)) {
						//StorageSpaceの場合、Postfix分Loop
						for (String postfix : getStorageSpacePostfix(true)) {
							String storageSpaceTableName = tableName + postfix;
							//除外対象テーブルチェック
							if (!isPartitionTargetTable(storageSpaceTableName)) {
								continue;
							}

							//テーブル存在チェック
							if (!isExistsTable(storageSpaceTableName, true)) {
								continue;
							}

							//作られているPartitionの最大値を取得
							PartitionInfo info = getTablePartitionInfo(storageSpaceTableName);
							ret.add(info);
						}
					} else {
						//除外対象テーブルチェック
						if (!isPartitionTargetTable(tableName)) {
							continue;
						}

						//テーブル存在チェック
						if (!isExistsTable(tableName, false)) {
							continue;
						}

						//作られているPartitionの最大値を取得
						PartitionInfo info = getTablePartitionInfo(tableName);
						ret.add(info);
					}
				}

				return ret;
		});

	}

	/**
	 * テーブルに対して作成されているPartition情報(最大のテナントID)を返します。
	 */
	private PartitionInfo getTablePartitionInfo(final String tableName) {

		SqlExecuter<PartitionInfo> exec = new SqlExecuter<PartitionInfo>() {
			@Override
			public PartitionInfo logic() throws SQLException {

				//Partitionの存在チェック
				PreparedStatement ps = null;
				String databaseName = adapter.getConnection().getCatalog();
				if (StringUtil.isEmpty(databaseName)) {
					ps = getPreparedStatement(MYSQL_MAX_PARTITION_SQL);
					ps.setString(1, tableName);
				} else {
					ps = getPreparedStatement(MYSQL_MAX_PARTITION_SQL_WITH_SCHEMA);
					ps.setString(1, databaseName);
					ps.setString(2, tableName);
				}

				ResultSet rs = ps.executeQuery();
				int maxTenantId = -1;
				Set<String> partitionNames = new TreeSet<>();
				try {
					if(rs.next()) {
						//先頭のpartition_nameがテナントの最大
						String max_partition_name = rs.getString(1);
						partitionNames.add(max_partition_name);

						//partition_nameからテナントIDを取得
						try {
							maxTenantId = Integer.parseInt(max_partition_name.substring((tableName + "_").length()));
						} catch (NumberFormatException e) {
							logger.warn("cant parse the partition name:" + max_partition_name);
						}
					}
					while(rs.next()) {
						partitionNames.add(rs.getString(1));
					}

				} finally {
					rs.close();
				}

				PartitionInfo info = new PartitionInfo(tableName, partitionNames, maxTenantId);

				return info;
			}
		};
		return exec.execute(adapter, true);
	}

	@Override
	public boolean createPartition(final PartitionCreateParameter param, final LogHandler logHandler) {

		boolean isSuccess = Transaction.requiresNew(t -> {

				doCreatePartition(param, logHandler);

				return true;
		});

		return isSuccess;
	}

	private void doCreatePartition(PartitionCreateParameter param, final LogHandler logHandler) {

		//Partition対象、Partition状態を取得
		List<PartitionInfo> partitionList = getPartitionInfo();

		//有効なテナント情報を取得
		List<TenantInfo> validTenantList = getValidTenantInfoList();

		for (PartitionInfo partitionInfo : partitionList) {
			createTablePartition(param, partitionInfo, validTenantList, logHandler);
		}
	}

	private void createTablePartition(final PartitionCreateParameter param, final PartitionInfo partitionInfo, final List<TenantInfo> storeTenantList, final LogHandler logHandler) {

		if (partitionInfo.getMaxTenantId() >= param.getTenantId()) {
			//既にPartitionは存在しているのでなにもしない
			//もし実際には存在しない場合でも途中に作ることはしない
			logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "skipPartitionMsg", partitionInfo.getMaxTenantId(), partitionInfo.getTableName()));
			return;
		}

		//存在する最大テナントIDを取得
		int maxTenantId = 0;
		if (!storeTenantList.isEmpty()) {
			maxTenantId = storeTenantList.get(storeTenantList.size() - 1).getId();
		}

		//Partitionの最大テナントIDを取得
		int maxPartitionId = partitionInfo.getMaxTenantId() > 0 ? partitionInfo.getMaxTenantId() : 0;

		//実行するテナントIDを取得
		List<Integer> addTenantIdList = new ArrayList<Integer>();

		//存在するテナントについては、テナントID分だけ作成(削除でDropされている分などは作成しない)
		//またこの処理によりカスタムStorageSpaceなどが途中で作成された場合もテナント分作成される
		if (maxPartitionId < maxTenantId) {
			for (TenantInfo tenant : storeTenantList) {
				if (tenant.getId() > maxPartitionId) {
					addTenantIdList.add(tenant.getId());
					logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "createExistTenantPartitionMsg", tenant.getId()));
				}
			}
		}
		//最大のテナントIDから指定されたID分は1つずつ作成する
		for (int tenantId = maxTenantId + 1; tenantId <= param.getTenantId(); tenantId++) {
			//存在しない場合のみ
			if (!partitionInfo.exists(tenantId)) {
				if (maxPartitionId >= tenantId) {
					//削除時にパーティションを削除せずに残っている場合など
					//途中には作れないのでスキップ
					logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "skipPartitionMsg", partitionInfo.getMaxTenantId(), partitionInfo.getTableName()));
					continue;
				}
				addTenantIdList.add(tenantId);
			}
		}

		for (int tenantId : addTenantIdList) {
			executeTableCreatePartition(param, partitionInfo.getTableName(), tenantId, logHandler);
		}
	}

	private void executeTableCreatePartition(final PartitionCreateParameter param, final String tableName, final int tenantId, final LogHandler logHandler) {

		String partitionName = tableName + "_" + tenantId;

		final StringBuilder sql = new StringBuilder("alter table " + tableName
				+ " add partition (partition " + partitionName
				+ " values less than (" + (tenantId + 1) + ") ");

		if(param.isMySqlUseSubPartition() && isSubPartitionTargetTable(tableName)){
			sql.append("(");
			//サブパーティション分作成
			for(int subPartCnt = 0 ; subPartCnt < getMaxSubPartitionCount() ; subPartCnt++){
				sql.append("subpartition "+ partitionName + "_" + subPartCnt );
				if(subPartCnt < getMaxSubPartitionCount() - 1){
					sql.append(",");
				}
			}
			sql.append(")");
		}
		sql.append(")");

		//Partitionの作成を別Transactionで実行
		Transaction.requiresNew(t -> {
				SqlExecuter<Void> exec = new SqlExecuter<Void>() {
					@Override
					public Void logic() throws SQLException {
						PreparedStatement ps = getPreparedStatement(sql.toString());

						ps.executeUpdate();

						logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "createdPartitionMsg", partitionName));

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

		for (String tableName : getTableList()) {
			if (isPartitionTargetTable(tableName)) {
				if (isStorageSpaceTable(tableName)) {
					//StorageSpaceの場合、Postfix分Loop
					for (String postfix : getStorageSpacePostfix(true)) {
						String storageSpaceTableName = tableName + postfix;
						executeTableDropPartition(param, logHandler, storageSpaceTableName);
					}
				} else {
					executeTableDropPartition(param, logHandler, tableName);
				}
			}
		}
	}

	private void executeTableDropPartition(final PartitionDeleteParameter param, final LogHandler logHandler, final String tableName) {

		String partitionName = tableName + "_" + param.getTenantId();

		if (existsTablePartition(param, logHandler, tableName)) {

			//PartitionのDrop
			Transaction.requiresNew(t -> {
					String sql = "alter table " + tableName + " drop partition " + partitionName;

					SqlExecuter<Void> exec = new SqlExecuter<Void>() {
						@Override
						public Void logic() throws SQLException {

							//PartitionのDrop
							PreparedStatement ps = getPreparedStatement(sql);

							ps.executeUpdate();

							logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "droppedPartitionMsg", partitionName));

							return null;
						}
					};
					try {
						exec.execute(adapter, true);
					} catch (Exception e) {
						logHandler.warn("partition cant dropped ...:" + sql, e);
					}
			});

		} else {
			logHandler.info(getPartitionResourceMessage(param.getLoggerLanguage(), "skipDropPartitionMsg", partitionName));
		}
	}

	private boolean existsTablePartition(final PartitionDeleteParameter param, final LogHandler logHandler, final String tableName) {

		SqlExecuter<Boolean> exec = new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {

				String partitionName = tableName + "_" + param.getTenantId();

				//Partitionの存在チェック
				PreparedStatement ps = null;
				String databaseName = adapter.getConnection().getCatalog();
				if (StringUtil.isEmpty(databaseName)) {
					ps = getPreparedStatement(MYSQL_EXIST_PARTITION_SQL);
					ps.setString(1, tableName);
					ps.setString(2, partitionName);
				} else {
					ps = getPreparedStatement(MYSQL_EXIST_PARTITION_SQL_WITH_SCHEMA);
					ps.setString(1, databaseName);
					ps.setString(2, tableName);
					ps.setString(3, partitionName);
				}

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
		return exec.execute(adapter, true);
	}

	@Override
	protected boolean isExistsTable(String tableName) {

		SqlExecuter<Boolean> exec = null;
		String databaseName = null;
		try {
			databaseName = adapter.getConnection().getCatalog();
		} catch (SQLException e) {
		}
		if (StringUtil.isEmpty(databaseName)) {
			exec = createCheckExistSqlExecuter(MYSQL_EXIST_TABLE_SQL, tableName, null);
		} else {
			//MySQLの場合、データベースが指定されていれば指定
			exec = createCheckExistSqlExecuter(MYSQL_EXIST_TABLE_SQL_WITH_SCHEMA, tableName, databaseName);
		}
		return exec.execute(adapter, true);
	}

	private SqlExecuter<Boolean> createCheckExistSqlExecuter(final String sql, final String tableName, final String databaseName) {
		return new SqlExecuter<Boolean>() {
			@Override
			public Boolean logic() throws SQLException {

				PreparedStatement ps = getPreparedStatement(sql);
				if (StringUtil.isEmpty(databaseName)) {
					ps.setString(1, tableName);
				} else {
					ps.setString(1, databaseName);
					ps.setString(2, tableName);
				}

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

}
