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
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.tools.storagespace.StorageSpaceService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.ToolsBatchResourceBundleUtil;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.util.StringUtil;

/**
 * StorageSpace移行バッチ
 */
public class StorageSpaceMigration extends MtpCuiBase {

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static StorageSpaceService storageSpaceService = ServiceRegistry.getRegistry().getService(StorageSpaceService.class);

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

	/**
	 * コンストラクタ
	 *
	 * args[0]・・・execMode["Wizard" or "Silent"]
	 * args[1]・・・language
	 * args[2]・・・tenantId
	 * args[3]・・・entityName
	 * args[4]・・・storageSpaceName
	 * args[5]・・・withCleanup
	 **/
	public StorageSpaceMigration(String... args) {
		if (args != null) {
			if (args.length > 0) {
				execMode = ExecMode.valueOf(args[0]);
			}
			if (args.length > 1) {
				// "system"の場合は、JVMのデフォルトを利用
				if (!"system".equals(args[1])) {
					setLanguage(args[1]);
				}
			}
			if (args.length > 2) {
				setTenantId(Integer.parseInt(args[2]));
			}
			if (args.length > 3) {
				setEntityName(args[3]);
			}
			if (args.length > 4) {
				setStorageSpaceName(args[4]);
			}
			if (args.length > 5) {
				setWithCleanup(Boolean.valueOf(args[5]));
			}
		}
		setupLanguage();
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
	 * モードに合わせて実行します。
	 *
	 * @return 実行結果
	 */
	public boolean execute() throws Exception {
		clearLog();

		// Console出力用のログリスナーを追加
		addLogListner(getConsoleLogListner());

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

			return proceed();
		default :
			logError("unsupport execute mode : " + execMode);
			return false;
		}
	}

	private boolean startWizard() {
		// TenantId
		boolean validTenantId = false;
		do {
			String tenantId = readConsole(getWizardResourceMessage("tenantIdMsg"));
			if (StringUtil.isNotBlank(tenantId)) {
				try {
					setTenantId(Integer.parseInt(tenantId));
					validTenantId = true;
				} catch (NumberFormatException e) {
					logWarn(getWizardResourceMessage("invalidTenantIdMsg", tenantId));
				}
			}
		} while(validTenantId == false);

		// EntityName(DefinitionName)
		boolean validEntityName = false;
		do {
			String entityName = readConsole(getWizardResourceMessage("entityNameMsg"));
			if (StringUtil.isNotBlank(entityName)) {
				setEntityName(entityName);
				validEntityName = true;
			}
		} while(validEntityName == false);

		// StorageSpaceName
		boolean validStorageSpaceName = false;
		do {
			String storageSpaceName = readConsole(getWizardResourceMessage("storageSpaceNameMsg"));
			if (StringUtil.isNotBlank(storageSpaceName)) {
				setStorageSpaceName(storageSpaceName);
				validStorageSpaceName = true;
			}
		} while(validStorageSpaceName == false);

		// WithCleanup
		setWithCleanup(readConsoleBoolean(getWizardResourceMessage("confirmCleanupMsg"), withCleanup));

		// Console出力用のログリスナーを一度削除してログ出力に切り替え
		removeLogListner(getConsoleLogListner());
		addLogListner(getLoggingLogListner());

		// StorageSpacec移行処理実行
		boolean ret = proceed();

		// ログ出力用のログリスナーを削除
		removeLogListner(getLoggingLogListner());

		return ret;
	}

	private boolean proceed() {
		setSuccess(false);

		try {
			// テナント存在チェック
			TenantContext tCtx = tenantContextService.getTenantContext(tenantId);
			if (tCtx == null) {
				logError(getResourceMessage("notFoundTenant", tenantId));
				return isSuccess();
			}

			ExecuteContext.initContext(new ExecuteContext(tCtx));

			EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);
			EntityDefinition ed = edm.get(entityName);

			if (ed == null) {
				logError(getResourceMessage("notFoundEntity", entityName));
				return isSuccess();
			}

			String currentStorageSpaceName = ((SchemalessRdbStore) ed.getStoreDefinition()).getStorageSpace();

			try {
				// StorageSpace移行
				storageSpaceService.migrate(storageSpaceName, ed);
			} catch (Exception e) {
				logError(getResourceMessage("failedMigrate"), e);
				return isSuccess();
			}

			if (withCleanup) {
				try {
					MetaEntity metaEntity = MetaDataContext.getContext().getMetaDataHandler(
							EntityHandler.class, EntityService.ENTITY_META_PATH + entityName.replace(".", "/")).getMetaData();

					// 移行元StorageSpaceクリーンアップ
					storageSpaceService.cleanup(tenantId, currentStorageSpaceName, metaEntity);
				} catch (Exception e) {
					logError(getResourceMessage("failedCleanup"), e);
					return isSuccess();
				}
			}

			setSuccess(true);
		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
			logInfo("");

			ExecuteContext.initContext(null);
		}

		return isSuccess();
	}

	/** リソースファイルの接頭語 */
	private static final String RES_PRE = "StorageSpaceMigration.";
	private static final String RES_WIZARD_PRE = RES_PRE + "Wizard.";

	private String getResourceMessage(String suffix, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(getLanguage(), RES_PRE + suffix, args);
	}

	private String getWizardResourceMessage(String suffix, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(getLanguage(), RES_WIZARD_PRE + suffix, args);
	}

}
