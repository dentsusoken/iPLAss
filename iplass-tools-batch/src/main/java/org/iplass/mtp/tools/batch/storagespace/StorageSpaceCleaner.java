/*
 * Copyright 2018 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.storagespace;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.tools.storagespace.StorageSpaceService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StorageSpaceクリーンアップバッチ
 */
public class StorageSpaceCleaner extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(StorageSpaceCleaner.class);

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

	/**
	 * コンストラクタ
	 *
	 * args[0]・・・execMode["Wizard" or "Silent"]
	 * args[1]・・・tenantId
	 * args[2]・・・entityName
	 * args[3]・・・storageSpaceName
	 **/
	public StorageSpaceCleaner(String... args) {
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
		}
	}

	/**
	 * メイン処理
	 *
	 * @param args 引数
	 */
	public static void main(String[] args) {
		try {
			new StorageSpaceCleaner(args).execute();
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
			String tenantId = readConsole(rs("StorageSpaceCleaner.Wizard.tenantIdMsg"));
			if (StringUtil.isNotBlank(tenantId)) {
				try {
					setTenantId(Integer.parseInt(tenantId));
					validTenantId = true;
				} catch (NumberFormatException e) {
					logWarn(rs("StorageSpaceCleaner.Wizard.invalidTenantIdMsg", tenantId));
				}
			}
		} while(validTenantId == false);

		// EntityName(DefinitionName)
		boolean validEntityName = false;
		do {
			String entityName = readConsole(rs("StorageSpaceCleaner.Wizard.entityNameMsg"));
			if (StringUtil.isNotBlank(entityName)) {
				setEntityName(entityName);
				validEntityName = true;
			}
		} while(validEntityName == false);

		// StorageSpaceName
		boolean validStorageSpaceName = false;
		do {
			String storageSpaceName = readConsole(rs("StorageSpaceCleaner.Wizard.storageSpaceNameMsg"));
			if (StringUtil.isNotBlank(storageSpaceName)) {
				setStorageSpaceName(storageSpaceName);
				validStorageSpaceName = true;
			}
		} while(validStorageSpaceName == false);

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		// StorageSpacecクリーンアップ処理実行
		return executeTask(null, (param) -> {
			return proceed();
		});
	}

	private boolean proceed() {
		setSuccess(false);

		// テナント存在チェック
		TenantContext tc = tenantContextService.getTenantContext(tenantId);
		if (tc == null) {
			logError(rs("StorageSpaceCleaner.notFoundTenant", tenantId));
			return isSuccess();
		}

		return ExecuteContext.executeAs(tc, () -> {

			EntityHandler entityHandler = MetaDataContext.getContext().getMetaDataHandler(EntityHandler.class, EntityService.ENTITY_META_PATH + entityName.replace(".", "/"));
			if (entityHandler == null) {
				logError(rs("StorageSpaceCleaner.notFoundEntity", entityName));
				return isSuccess();
			}

			try {
				storageSpaceService.cleanup(tenantId, storageSpaceName, entityHandler.getMetaData());
			} catch (Exception e) {
				logError(rs("StorageSpaceCleaner.failedCleanup"), e);
				return isSuccess();
			}

			setSuccess(true);

			return isSuccess();
		});
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
