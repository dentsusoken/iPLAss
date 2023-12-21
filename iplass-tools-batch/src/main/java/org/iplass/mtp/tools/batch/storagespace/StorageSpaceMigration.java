/*
 * Copyright 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.storagespace;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.entity.definition.stores.SchemalessRdbStore;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.datastore.DataStore;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.grdb.GRdbDataStore;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.tools.storagespace.StorageSpaceService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.storagespace.tableallocators.LocationSpecificationTableAllocator;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StorageSpace移行バッチ
 */
public class StorageSpaceMigration extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(StorageSpaceMigration.class);

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static StorageSpaceService storageSpaceService = ServiceRegistry.getRegistry().getService(StorageSpaceService.class);

	/** 疑似パーティション位置を自動で決定する為のコンソール入力文字 */
	private static final String CONSOLE_INPUT_PSEUDO_PARTITION_LOCATION_AUTO = "auto";

	/** 実行モード */
	private ExecMode execMode = ExecMode.WIZARD;

	/** テナントID */
	private int tenantId = -1;

	/** Entity名 */
	private String entityName;

	/** StorageSpace名 */
	private String storageSpaceName;

	/** クリーンアップ指示 */
	private boolean withCleanup = true;

	/** 疑似パーティション位置（マイナス値の場合は自動決定する） */
	private int pseudoPartitionLocation = -1;

	/**
	 * コンストラクタ
	 *
	 * <p>
	 * <ul>
	 * <li>args[0]・・・execMode["Wizard" or "Silent"]</li>
	 * <li>args[1]・・・tenantId</li>
	 * <li>args[2]・・・entityName</li>
	 * <li>args[3]・・・storageSpaceName</li>
	 * <li>args[4]・・・withCleanup</li>
	 * <li>args[5]・・・pseudoPartitionLocation</li>
	 * </ul>
	 * </p>
	 *
	 * <p>
	 * pseudoPartitionLocation は後から追加したパラメータとなっている。互換性維持の為、最後に追加している。
	 * </p>
	 **/
	public StorageSpaceMigration(String... args) {
		if (args != null) {
			if (args.length > 0) {
				execMode = ExecMode.valueOf(args[0]);
			}
			if (args.length > 1) {
				setTenantId(Integer.parseInt(args[1]));
			}
			if (args.length > 2) {
				setEntityName(args[2]);
			}
			if (args.length > 3) {
				setStorageSpaceName(args[3]);
			}
			if (args.length > 4) {
				setWithCleanup(Boolean.valueOf(args[4]));
			}
			if (args.length > 5) {
				setPseudoPartitionLocation(Integer.valueOf(args[5]));
			}
		}
	}

	/**
	 * メイン処理
	 *
	 * @param args 引数
	 */
	public static void main(String[] args) {
		try {
			new StorageSpaceMigration(args).execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	/**
	 * 実行モードを設定します。
	 *
	 * @param execMode 実行モード
	 */
	public void setExecMode(ExecMode execMode) {
		this.execMode = execMode;
	}

	/**
	 * テナントIDを設定します。
	 *
	 * @param tenantId テナントID
	 */
	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * Entity名を設定します。
	 *
	 * @param entityName Entity名
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * StorageSpace名を設定します。
	 *
	 * @param storageSpaceName StorageSpace名
	 */
	public void setStorageSpaceName(String storageSpaceName) {
		this.storageSpaceName = storageSpaceName;
	}

	/**
	 * クリーンアップを行うかを設定します。
	 *
	 * @param withCleanup クリーンアップ指示
	 */
	public void setWithCleanup(boolean withCleanup) {
		this.withCleanup = withCleanup;
	}

	/**
	 * 疑似パーティション位置を設定します。
	 *
	 * @param pseudoPartitionLocation 疑似パーティション位置
	 */
	public void setPseudoPartitionLocation(int pseudoPartitionLocation) {
		this.pseudoPartitionLocation = pseudoPartitionLocation;
	}

	/**
	 * モードに合わせて実行します。
	 *
	 * @return 実行結果
	 */
	public boolean execute() throws Exception {
		clearLog();

		//Console出力
		switchLog(true, false);

		// 環境情報出力
		logEnvironment();

		switch (execMode) {
		case WIZARD :
			logInfo("■Start Wizard");
			logInfo("");

			// Wizardの実行
			return startWizard();
		case SILENT :
			logInfo("■Start Silent");
			logInfo("");

			//Silentの場合はConsole出力を外す
			switchLog(false, true);

			return executeTask(null, (param) -> {
				return proceed();
			});
		default :
			logError("unsupport execute mode : " + execMode);
			return false;
		}
	}

	private boolean startWizard() {
		// TenantId
		boolean validTenantId = false;
		do {
			String tenantId = readConsole(rs("StorageSpaceMigration.Wizard.tenantIdMsg"));
			if (StringUtil.isNotBlank(tenantId)) {
				try {
					setTenantId(Integer.parseInt(tenantId));
					validTenantId = true;
				} catch (NumberFormatException e) {
					logWarn(rs("StorageSpaceMigration.Wizard.invalidTenantIdMsg", tenantId));
				}
			}
		} while(validTenantId == false);

		// EntityName(DefinitionName)
		boolean validEntityName = false;
		do {
			String entityName = readConsole(rs("StorageSpaceMigration.Wizard.entityNameMsg"));
			if (StringUtil.isNotBlank(entityName)) {
				setEntityName(entityName);
				validEntityName = true;
			}
		} while(validEntityName == false);

		// StorageSpaceName
		boolean validStorageSpaceName = false;
		do {
			String storageSpaceName = readConsole(rs("StorageSpaceMigration.Wizard.storageSpaceNameMsg"));
			if (StringUtil.isNotBlank(storageSpaceName)) {
				setStorageSpaceName(storageSpaceName);
				validStorageSpaceName = true;
			}
		} while(validStorageSpaceName == false);

		// 対象のストレージスペースに疑似パーティションが定義されているか確認
		int tableCount = getStorageSpaceMap(storageSpaceName).getTableCount();
		if (1 < tableCount) {
			// 疑似パーティションが定義されている場合、パーティション位置を指定
			boolean validPseudoPartitionLocation = false;
			do {
				String readPseudoPartitionLocation = readConsole(
						rs("StorageSpaceMigration.Wizard.pseudoPartitionLocationMsg", tableCount - 1, CONSOLE_INPUT_PSEUDO_PARTITION_LOCATION_AUTO));
				if (StringUtil.isNotBlank(readPseudoPartitionLocation)) {
					if (StringUtil.equalsIgnoreCase(CONSOLE_INPUT_PSEUDO_PARTITION_LOCATION_AUTO, readPseudoPartitionLocation)) {
						// auto が入力された場合は、デフォルト値のまま進める
						validPseudoPartitionLocation = true;
					} else {
						try {
							int pseudoPartitionLocation = Integer.parseInt(readPseudoPartitionLocation);
							if (0 > pseudoPartitionLocation || pseudoPartitionLocation >= tableCount) {
								// 想定外の設定値の場合は再入力する
								throw new IllegalArgumentException();
							}
							setPseudoPartitionLocation(pseudoPartitionLocation);
							validPseudoPartitionLocation = true;
						} catch (IllegalArgumentException e) {
							logWarn(rs("StorageSpaceMigration.Wizard.invalidPseudoPartitionLocationMsg", CONSOLE_INPUT_PSEUDO_PARTITION_LOCATION_AUTO));
						}
					}
				}
			} while (validPseudoPartitionLocation == false);
		}

		// WithCleanup
		setWithCleanup(readConsoleBoolean(rs("StorageSpaceMigration.Wizard.confirmCleanupMsg"), withCleanup));

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		// StorageSpacec移行処理実行
		return executeTask(null, (param) -> {
			return proceed();
		});
	}

	private boolean proceed() {
		setSuccess(false);

		// テナント存在チェック
		TenantContext tc = tenantContextService.getTenantContext(tenantId);
		if (tc == null) {
			logError(rs("StorageSpaceMigration.notFoundTenant", tenantId));
			return isSuccess();
		}

		return ExecuteContext.executeAs(tc, () -> {

			EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
			EntityDefinition ed = edm.get(entityName);

			if (ed == null) {
				logError(rs("StorageSpaceMigration.notFoundEntity", entityName));
				return isSuccess();
			}

			if (0 <= pseudoPartitionLocation) {
				// 疑似パーティション位置を指定されている場合、指定されたパーティションインデックスを指定する
				StorageSpaceMap storageSpaceMap = getStorageSpaceMap(storageSpaceName);

				// パーティション位置のチェック
				int tableCount = 0 < storageSpaceMap.getTableCount() ? storageSpaceMap.getTableCount() : 1;
				if (tableCount <= pseudoPartitionLocation) {
					// 最大値チェックは SILENT パターンでは必要
					// 最大パーティション位置以上の場合は、エラー終了。
					// storageSpaceMap.getTableCount() = 2 の場合 ⇒ OBJ_STORE__SPNAME, OBJ_STORE__SPNAME__1 が定義される。
					// pseudoPartitionLocation として指定可能な範囲は、 0 ～ 1となる。2 は指定不可能。
					throw new IllegalArgumentException(rs("StorageSpaceMigration.overPseudoPartitionPosition", tableCount - 1, pseudoPartitionLocation));
				}
				storageSpaceMap.setTableAllocator(new LocationSpecificationTableAllocator(pseudoPartitionLocation));

				// ストレージスペースが同一でも、強制敵にテーブル名接尾辞を再生成する
				// このフラグ設定は、ストレージスペース移行機能（本機能）だけで設定することを想定し用意している
				StoreService storeService = ServiceRegistry.getRegistry().getService(StoreService.class);
				DataStore dataStore = storeService.getDataStore();
				((GRdbDataStore) dataStore).setForceRegenerateTableNamePostfix(true);
			}

			String currentStorageSpaceName = ((SchemalessRdbStore) ed.getStoreDefinition()).getStorageSpace();
			String currentTableNamePostfix = storageSpaceService.getTableNamePostfix(entityName);

			try {
				// StorageSpace移行
				storageSpaceService.migrate(storageSpaceName, ed);
			} catch (Exception e) {
				logError(rs("StorageSpaceMigration.failedMigrate"), e);
				return isSuccess();
			}

			if (withCleanup) {
				try {
					MetaEntity metaEntity = MetaDataContext.getContext().getMetaDataHandler(
							EntityHandler.class, EntityService.ENTITY_META_PATH + entityName.replace(".", "/")).getMetaData();

					// 移行元StorageSpaceクリーンアップ
					storageSpaceService.cleanup(tenantId, currentStorageSpaceName, metaEntity, currentTableNamePostfix);
				} catch (Exception e) {
					logError(rs("StorageSpaceMigration.failedCleanup"), e);
					return isSuccess();
				}
			}

			setSuccess(true);

			return isSuccess();
		});
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}

	/**
	 * StoreService から StorageSpaceMap 定義を取得する
	 * @param storageSpaceName 取得対象のストレージスペース名
	 * @return StorageSpaceMap
	 */
	private StorageSpaceMap getStorageSpaceMap(String storageSpaceName) {
		StoreService storeService = ServiceRegistry.getRegistry().getService(StoreService.class);
		DataStore dataStore = storeService.getDataStore();
		if (null != dataStore && dataStore instanceof GRdbDataStore) {
			StorageSpaceMap storageSpaceMap = ((GRdbDataStore) dataStore).getStorageSpaceMap().get(storageSpaceName);
			// ストレージスペース存在チェック
			if (null == storageSpaceMap) {
				// ストレージスペースマップが存在しない場合、エラー終了
				throw new IllegalArgumentException(rs("StorageSpaceMigration.notFoundStorageSpace", storageSpaceName));
			}

			return storageSpaceMap;
		}

		throw new RuntimeException(
				"DataStore is null or the class is of an unexpected type. type = " + (null == dataStore ? "null" : dataStore.getClass().getName()));
	}
}
