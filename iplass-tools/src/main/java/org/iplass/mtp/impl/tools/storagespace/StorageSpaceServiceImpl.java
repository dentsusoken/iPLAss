/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.storagespace;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.EntityDefinitionModifyResult;
import org.iplass.mtp.entity.definition.stores.SchemalessRdbStore;
import org.iplass.mtp.impl.datastore.RdbDataStore;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.grdb.GRdbDataStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore;
import org.iplass.mtp.impl.datastore.grdb.MetaGRdbEntityStore.GRdbEntityStoreRuntime;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.rdb.SqlExecuter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapter;
import org.iplass.mtp.impl.rdb.adapter.RdbAdapterService;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.entityport.EntityDataExportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportResult;
import org.iplass.mtp.impl.tools.entityport.EntityPortingService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;

public class StorageSpaceServiceImpl implements StorageSpaceService {

	private static final String TBL_OBJ_STORE = "obj_store";
	private static final String TBL_OBJ_STORE_RB = "obj_store_rb";

	private static final String COL_TENANT_ID = "tenant_id";
	private static final String COL_OBJ_DEF_ID = "obj_def_id";
	private static final String COL_OBJ_ID = "obj_id";

	private static final String[] CLEANUP_TABLES = {
			"obj_index_date",
			"obj_index_dbl",
			"obj_index_num",
			"obj_index_str",
			"obj_index_ts",
			"obj_ref",
			"obj_ref_rb",
			TBL_OBJ_STORE,
			TBL_OBJ_STORE_RB,
			"obj_unique_date",
			"obj_unique_dbl",
			"obj_unique_num",
			"obj_unique_str",
			"obj_unique_ts"
	};

	private static final String[] INDEX_TABLES = {
			"obj_index_date",
			"obj_index_dbl",
			"obj_index_num",
			"obj_index_str",
			"obj_index_ts"
	};

	private static final String[] UNIQUE_TABLES = {
			"obj_unique_date",
			"obj_unique_dbl",
			"obj_unique_num",
			"obj_unique_str",
			"obj_unique_ts"
	};

	private StoreService storeService;
	private RdbAdapterService rdbAdapterService;
	private EntityPortingService entityPortingService;

	private EntityDefinitionManager edm;

	/** 移行Commit単位(件数) */
	private int migrateCommitLimit = -1;

	/** クリーンアップCommit単位(件数) */
	private int cleanupCommitLimit = -1;

	@Override
	public void init(Config config) {
		storeService =  config.getDependentService(StoreService.class);
		rdbAdapterService =  config.getDependentService(RdbAdapterService.class);
		entityPortingService = config.getDependentService(EntityPortingService.class);

		if (config.getValue("migrateCommitLimit") != null) {
			migrateCommitLimit = Integer.parseInt(config.getValue("migrateCommitLimit"));
		}

		if (config.getValue("cleanupCommitLimit") != null) {
			cleanupCommitLimit = Integer.parseInt(config.getValue("cleanupCommitLimit"));
		}

		edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
	}

	@Override
	public void destroy() {
	}

	@Override
	public void migrate(String storageSpaceName, EntityDefinition entityDefinition) {
		// StorageSpace定義存在チェック
		boolean existStorageSpace = false;
		for (String storageSpace : ((RdbDataStore) storeService.getDataStore()).getStorageSpaceList()) {
			if (storageSpace.equals(storageSpaceName)) {
				existStorageSpace = true;
				break;
			}
		}
		if (!existStorageSpace) {
			throw new IllegalArgumentException(getRS("noStorageSpace", storageSpaceName));
		}

		// MetaDataEntry取得
		MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(EntityService.ENTITY_META_PATH + entityDefinition.getName().replace(".", "/"));

		File tempFile = null;
		try {
			tempFile = Files.createTempFile("mtp", ".csv").toFile();

			// EntityCSVデータエクスポート
			exportCSV(tempFile, entry);

			// StorageSpace変更
			String beforeTableNamePostfix = getTableNamePostfix(entityDefinition.getName());
			// changeStorageSpace メソッドを超えた場合、エンティティ定義に更新が入っている
			if (!changeStorageSpace(edm.get(entityDefinition.getName()), storageSpaceName)) {
				throw new StorageSpaceRuntimeException(getRS("failedChangeStorageSpace"));
			}
			String afterTableNamePostfix = getTableNamePostfix(entityDefinition.getName());

			// tableNamePostfix 同一チェック
			if (StringUtil.equals(beforeTableNamePostfix, afterTableNamePostfix)) {
				// 変更前後でテーブルスペース位置が同じ場合、処理を終了する
				throw new IllegalArgumentException(getRS("sameTableNamePostfix", afterTableNamePostfix));
			}

			// EntityCSVデータインポート
			if (!importCSV(tempFile, entityDefinition.getName(), entry)) {
				throw new StorageSpaceRuntimeException(getRS("failedImportEntityCSVData"));
			}
		} catch (IOException e) {
			throw new StorageSpaceRuntimeException(getRS("unexpectedError"), e);
		} finally {
			if (tempFile != null && tempFile.exists()) {
				tempFile.delete();
			}
		}
	}

	@Override
	public void cleanup(int tenantId, String storageSpaceName, MetaEntity metaEntity) {
		cleanupInner(tenantId, storageSpaceName, metaEntity, (ssm) -> ssm.allTableNamePostfix());
	}

	@Override
	public void cleanup(int tenantId, String storageSpaceName, MetaEntity metaEntity, String tableNamePostfix) {
		cleanupInner(tenantId, storageSpaceName, metaEntity, (ssm) -> Arrays.asList(new String[] { tableNamePostfix }));
	}

	@Override
	public String getTableNamePostfix(String entityName) {
		EntityService entityService = ServiceRegistry.getRegistry().getService(EntityService.class);
		EntityHandler entityHandler = entityService.getRuntimeByName(entityName);
		GRdbEntityStoreRuntime entityStoreRuntime = (GRdbEntityStoreRuntime) entityHandler.getEntityStoreRuntime();

		MetaGRdbEntityStore metaEntityStore = (MetaGRdbEntityStore) entityStoreRuntime.getMetaData();
		return metaEntityStore.getTableNamePostfix();
	}

	/**
	 * ストレージスペースクリーン内部処理
	 * @param tenantId テナントID
	 * @param storageSpaceName ストレージスペース名
	 * @param metaEntity Entityメタデータ
	 * @param postfixListFn テーブル名接尾辞取得ファンクション
	 */
	private void cleanupInner(int tenantId, String storageSpaceName, MetaEntity metaEntity, Function<StorageSpaceMap, List<String>> postfixListFn) {
		// StorageSpace存在チェック
		StorageSpaceMap ssm = ((GRdbDataStore) storeService.getDataStore()).getStorageSpaceMapOrDefault(storageSpaceName);
		if (!ssm.getStorageSpaceName().equals(storageSpaceName)) {
			// 指定のStorageSpaceが存在しない
			throw new IllegalArgumentException(getRS("noStorageSpace", storageSpaceName));
		}
		List<String> postfixList = postfixListFn.apply(ssm);

		final String objDefId = metaEntity.getId();
		final List<String> objIdList = findObjId(tenantId, objDefId, postfixList, rdbAdapterService.getRdbAdapter());
		final List<String> cleanupTableList = makeCleanupTableList(postfixList, ssm.isUseExternalIndexedTable(), ssm.isUseExternalUniqueIndexedTable());

		// Entityデータ削除処理
		int index = 0;
		boolean exists = !objIdList.isEmpty();
		while (exists) {
			int toIndex = cleanupCommitLimit > 0 ? index + cleanupCommitLimit > objIdList.size() ? objIdList.size() : index + (cleanupCommitLimit) : objIdList.size();
			final List<String> objIdSubList = objIdList.subList(index, toIndex);

			Transaction.requiresNew(t -> {
				for (String table : cleanupTableList) {
					SqlExecuter<int[]> sqlExec = new SqlExecuter<int[]>() {
						@Override
						public int[] logic() throws SQLException {
							StringBuilder sbSql = new StringBuilder();
							sbSql.append("DELETE FROM ").append(table);
							sbSql.append(" WHERE ").append(COL_TENANT_ID).append("=?");
							sbSql.append(" AND ").append(COL_OBJ_DEF_ID).append("=?");
							sbSql.append(" AND ").append(COL_OBJ_ID).append("=?");

							PreparedStatement ps = getPreparedStatement(sbSql.toString());

							for (String objId : objIdSubList) {
								ps.setInt(1, tenantId);
								ps.setString(2, objDefId);
								ps.setString(3, objId);
								ps.addBatch();
							}

							return ps.executeBatch();
						}
					};
					sqlExec.execute(rdbAdapterService.getRdbAdapter(), true);
				}
				t.commit();
			});
			index = toIndex;
			exists = index < objIdList.size();
		}
	}

	private void exportCSV(File file, final MetaDataEntry entry) throws IOException {
		EntityDataExportCondition condition = new EntityDataExportCondition();
		condition.setVersioned(true);

		// Export
		try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file))) {
			entityPortingService.write(os, entry, condition);
			os.flush();
		}
	}

	private boolean importCSV(File file, String entityName, final MetaDataEntry entry) throws IOException {
		EntityDataImportCondition condition = new EntityDataImportCondition();
		condition.setCommitLimit(migrateCommitLimit);
		condition.setIgnoreNotExistsProperty(false);
		condition.setUpdateDisupdatableProperty(true);
		condition.setUniqueKey("oid");

		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			EntityDataImportResult result = entityPortingService.importEntityData(entityName, is, entry, condition, null);
			return !result.isError();
		}
	}

	private boolean changeStorageSpace(EntityDefinition ed, String storageSpaceName) {
		SchemalessRdbStore sd = (SchemalessRdbStore)ed.getStoreDefinition();
		sd.setStorageSpace(storageSpaceName);
		EntityDefinitionModifyResult result = edm.update(ed);
		return result.isSuccess();
	}

	private List<String> findObjId(int tenantId, String objDefId, List<String> postfixList, RdbAdapter rdb) {
		SqlExecuter<List<String>> exec = new SqlExecuter<List<String>>() {
			@Override
			public List<String> logic() throws SQLException {
				String sanitizedObjDefId = rdb.sanitize(objDefId);
				StringBuilder sbSql = new StringBuilder();

				if (!postfixList.isEmpty()) {
					postfixList.forEach(postfix -> {
						if (sbSql.length() > 0) {
							sbSql.append(" UNION ");
						}
						sbSql.append("SELECT ").append(COL_OBJ_ID).append(" FROM ").append(TBL_OBJ_STORE);
						if (StringUtil.isNotBlank(postfix)) {
							sbSql.append(StorageSpaceMap.TABLE_NAME_SEPARATOR).append(postfix);
						}
						sbSql.append(" WHERE ").append(COL_TENANT_ID).append("=").append(tenantId);
						sbSql.append(" AND ").append(COL_OBJ_DEF_ID).append("='").append(sanitizedObjDefId).append("'");
						sbSql.append(" UNION ");
						sbSql.append("SELECT ").append(COL_OBJ_ID).append(" FROM ").append(TBL_OBJ_STORE_RB);
						if (StringUtil.isNotBlank(postfix)) {
							sbSql.append(StorageSpaceMap.TABLE_NAME_SEPARATOR).append(postfix);
						}
						sbSql.append(" WHERE ").append(COL_TENANT_ID).append("=").append(tenantId);
						sbSql.append(" AND ").append(COL_OBJ_DEF_ID).append("='").append(sanitizedObjDefId).append("'");
					});
				} else {
					sbSql.append("SELECT ").append(COL_OBJ_ID).append(" FROM ").append(TBL_OBJ_STORE);
					sbSql.append(" WHERE ").append(COL_TENANT_ID).append("=").append(tenantId);
					sbSql.append(" AND ").append(COL_OBJ_DEF_ID).append("='").append(sanitizedObjDefId).append("'");
					sbSql.append(" UNION ");
					sbSql.append("SELECT ").append(COL_OBJ_ID).append(" FROM ").append(TBL_OBJ_STORE_RB);
					sbSql.append(" WHERE ").append(COL_TENANT_ID).append("=").append(tenantId);
					sbSql.append(" AND ").append(COL_OBJ_DEF_ID).append("='").append(sanitizedObjDefId).append("'");
				}

				List<String> ret = new ArrayList<String>();

				try (ResultSet rs = getStatement().executeQuery(sbSql.toString())) {
					while (rs.next()) {
						ret.add(rs.getString(1));
					}
				}

				return ret;
			}
		};
		return exec.execute(rdbAdapterService.getRdbAdapter(), true);
	}

	private List<String> makeCleanupTableList(List<String> postfixList, boolean useExternalIndexedTable, boolean useExternalUniqueIndexedTable) {
		if (postfixList.isEmpty()) {
			return Arrays.asList(CLEANUP_TABLES);
		}

		List<String> list = new ArrayList<>();

		postfixList.forEach(postfix -> {
			for (String table : CLEANUP_TABLES) {
				if (StringUtil.isNotBlank(postfix)) {
					if(!useExternalIndexedTable && Arrays.asList(INDEX_TABLES).contains(table)) {
						//接尾語付きIndexテーブルを利用しない場合は削除対象のテーブルに含まない。
						continue;
					} else if(!useExternalUniqueIndexedTable && Arrays.asList(UNIQUE_TABLES).contains(table)) {
						//接尾語付きUnique Indexテーブルを利用しない場合は削除対象のテーブルに含まない。
						continue;
					}
					list.add(table + StorageSpaceMap.TABLE_NAME_SEPARATOR + postfix);
				} else {
					list.add(table);
				}
			}
		});

		return list;
	}

	private String getRS(String suffix, Object... arguments) {
		return ToolsResourceBundleUtil.resourceString("storagespace." + suffix, arguments);
	}

}
