/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.tools.batch.tenant;


import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.tools.tenant.TenantCreateParameter;
import org.iplass.mtp.impl.tools.tenant.TenantDeleteParameter;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.impl.tools.tenant.TenantToolService;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.impl.tools.tenant.rdb.TenantRdbConstants;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.gui.tenant.TenantManagerApp;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * テナント情報を管理するクラス。
 */
public class TenantBatch extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(TenantBatch.class);

	/** Silentモード 設定ファイル名キー */
	public static final String KEY_CONFIG_FILE = "tenant.config";

	/** 実行モード */
	public enum TenantBatchExecMode {GUI, CREATE, DELETE, SHOW, SILENT};

	//実行モード
	private TenantBatchExecMode execMode = TenantBatchExecMode.GUI;

	private TenantToolService toolService = ServiceRegistry.getRegistry().getService(TenantToolService.class);

	/**
	 * args[0]・・・execMode
	 **/
	public static void main(String[] args) {

		TenantBatch instance = null;
		try {
			instance = new TenantBatch(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (instance.getExecMode() == TenantBatchExecMode.GUI) {
				MtpBatchResourceDisposer.addShutdownHookForDisposeResource(() -> {
					// NOTE 個別の破棄処理が必要な場合は、ここに実装する。
				});
			} else {
				MtpBatchResourceDisposer.disposeResource();
			}
		}
	}

	/**
	 * args[0]・・・execMode
	 **/
	public TenantBatch(String... args) {

		if (args != null) {
			if (args.length > 0 && args[0] != null) {
				setExecMode(TenantBatchExecMode.valueOf(args[0].toUpperCase()));
			}
		}
	}

	/**
	 * モードに合わせて実行します。
	 *
	 * @return
	 */
	public boolean execute() {

		clearLog();

		//Console出力
		switchLog(true, false);

		//環境情報出力
		logEnvironment();

		switch (getExecMode()) {
		case GUI :
			logInfo("■Start App");
			logInfo("");

			//Guiの場合はConsole出力を外す
			switchLog(false, true);

			TenantManagerApp.main(new String[]{getLanguage()});
			return true;
		case CREATE :
			logInfo("■Start Create Wizard");
			logInfo("");

			//Wizardの実行
			return startCreateWizard();
		case DELETE :
			logInfo("■Start Delete Wizard");
			logInfo("");

			//Wizardの実行
			return startDeleteWizard();
		case SHOW :
			showAllTenantList();
			logInfo("");
			return true;
		case SILENT :
			logInfo("■Start Silent");
			logInfo("");

			//Silentの場合はConsole出力を外す
			switchLog(false, true);

			return startSilent();
		default :
			logError("unsupport execute mode : " + getExecMode());
			return false;
		}

	}

	public TenantBatchExecMode getExecMode() {
		return execMode;
	}

	public void setExecMode(TenantBatchExecMode execMode) {
		this.execMode = execMode;
	}

	/**
	 * テナントを作成します。（TenantManagerAppからの実行もこちらを呼び出す）
	 *
	 * @param info 作成情報
	 * @return
	 */
	public boolean executeCreate(final TenantCreateParameter param) {

		param.setLoggerLanguage(getLanguage());

		setSuccess(false);

		try {
			boolean isSuccess = toolService.create(param, new TenantBatchLogListener());
			setSuccess(isSuccess);
		} finally {
			ExecuteContext.initContext(null);
		}

		return isSuccess();
	}

	/**
	 * テナント作成情報を出力します。
	 */
	public void logArguments(TenantCreateParameter param) {
		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant url :" + param.getTenantUrl());
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\ttenant display name :" + param.getTenantDisplayName());
		logInfo("\tadmin accunt id :" + param.getAdminUserId());
		//logInfo("\tadmin accunt password :" + param.getAdminPassword());
		logInfo("\ttopUrl :" + param.getTopUrl());
		logInfo("\tuseLanguages :" + param.getUseLanguages());
		logInfo("\tcreateBlankTenant :" + param.isCreateBlankTenant());
		if (getConfigSetting().isPostgreSQL()) {
			logInfo("\tsubpartition size :" + param.getSubPartitionSize());
		}
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	/**
	 * テナント作成用のパラメータを生成して、作成処理を実行します。
	 *
	 * @return
	 */
	private boolean startCreateWizard() {

		//テナント名
		String tenantName = readConsole(rs("TenantBatch.Create.Wizard.inputTenantNameMsg"));

		if (StringUtil.isEmpty(tenantName)) {
			logWarn(rs("TenantBatch.Create.Wizard.requiredTenantNameMsg"));
			return startCreateWizard();
		}
		if (tenantName.equalsIgnoreCase("-show")) {
			//一覧を出力
			showAllTenantList();
			return startCreateWizard();
		}
		if (tenantName.equalsIgnoreCase("-env")) {
			//環境情報を出力
			logEnvironment();
			return startCreateWizard();
		}

		String tenantUrl = readConsole(rs("TenantBatch.Create.Wizard.inputTenantUrlMsg") + "(/" + tenantName + ")");
		if (StringUtil.isEmpty(tenantUrl)) {
			tenantUrl = "/" + tenantName;
		}

		//URL存在チェック
		if (toolService.existsURL(tenantUrl)) {
			logWarn(rs("TenantBatch.Create.Wizard.existsTenantMsg", tenantUrl));
			return startCreateWizard();
		}

		//Admin ID
		String adminUserId = null;
		do {
			adminUserId = readConsole(rs("TenantBatch.Create.Wizard.inputAdminIdMsg"));
			if (StringUtil.isEmpty(adminUserId)) {
				logWarn(rs("TenantBatch.Create.Wizard.requiredAdminIdMsg"));
				adminUserId = null;
			}
		} while(adminUserId == null);

		//Admin PW
		String adminPW = null;
		boolean invalidateAdminPW = true;
		do {
			do {
				adminPW = readConsolePassword(rs("TenantBatch.Create.Wizard.inputAdminPWMsg"));
				if (StringUtil.isEmpty(adminPW)) {
					logWarn(rs("TenantBatch.Create.Wizard.requiredAdminPWMsg"));
					adminPW = null;
				}
			} while(adminPW == null);

			//Confirm Admin PW
			String confirmAdminPW = null;
			do {
				confirmAdminPW = readConsolePassword(rs("TenantBatch.Create.Wizard.inputReTypeAdminPWMsg"));
				if (StringUtil.isEmpty(confirmAdminPW)) {
					logWarn(rs("TenantBatch.Create.Wizard.requiredAdminPWMsg"));
					confirmAdminPW = null;
				}
			} while(confirmAdminPW == null);

			if (!adminPW.equals(confirmAdminPW)) {
				logWarn(rs("TenantBatch.Create.Wizard.unmatchAdminPWMsg"));
				adminPW = null;
				confirmAdminPW = null;
			} else {
				invalidateAdminPW = false;
			}

		} while(invalidateAdminPW);

		TenantCreateParameter createParam = new TenantCreateParameter(tenantName, adminUserId, adminPW);
		createParam.setTenantUrl(tenantUrl);
		createParam.setUseLanguages(toolService.getDefaultEnableLanguages());

		//デフォルトスキップチェック
		boolean isDefault = readConsoleBoolean(rs("TenantBatch.Create.Wizard.confirmDefaultMsg"), false);

		if (!isDefault) {
			String tenantDisplayName = readConsole(rs("TenantBatch.Create.Wizard.inputTenantDispNameMsg"));
			if (StringUtil.isNotBlank(tenantDisplayName)) {
				createParam.setTenantDisplayName(tenantDisplayName);
			}
			String topURL = readConsole(rs("TenantBatch.Create.Wizard.inputTopUrlMsg"));
			if (StringUtil.isNotBlank(topURL)) {
				createParam.setTopUrl(topURL);
			}
			String lang = readConsole(rs("TenantBatch.Create.Wizard.useMultiLangMsg"));
			createParam.setUseLanguages(lang);

			boolean isBlank = readConsoleBoolean(rs("TenantBatch.Create.Wizard.createBlankTenantMsg"), false);
			createParam.setCreateBlankTenant(isBlank);

			if (getConfigSetting().isPostgreSQL()) {
				boolean invalidateSubPartitionSize = true;
				do {
					int subPartitionSize = readConsoleInteger(rs("TenantBatch.Create.Wizard.inputSubPartitionSizeMsg"), TenantRdbConstants.MAX_SUBPARTITION);
					if (subPartitionSize < TenantRdbConstants.MIN_SUBPARTITION) {
						logWarn(rs("TenantBatch.Create.Wizard.invalidValueSubPartitionSizeMsg", TenantRdbConstants.MIN_SUBPARTITION));
					} else {
						createParam.setSubPartitionSize(subPartitionSize);
						invalidateSubPartitionSize = false;
					}
				} while(invalidateSubPartitionSize);
			}
		}

		//実行情報出力
		logArguments(createParam);

		boolean isExecute = readConsoleBoolean(rs("TenantBatch.Create.Wizard.confirmCreateTenantMsg"), false);
		if (!isExecute) {
			//再度実行
			return startCreateWizard();
		}

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//作成処理実行
		return executeTask(createParam, (param) -> {
			return executeCreate(param);
		});
	}

	/**
	 * テナントを削除します。（TenantManagerAppからの実行もこちらを呼び出す）
	 *
	 * @param param 削除情報
	 * @return
	 */
	public boolean executeDelete(final TenantDeleteParameter param) {

		param.setLoggerLanguage(getLanguage());

		setSuccess(false);

		try {
			boolean isSuccess = toolService.remove(param, new TenantBatchLogListener());
			setSuccess(isSuccess);
		} finally {
			ExecuteContext.initContext(null);
		}

		return isSuccess();
	}

	/**
	 * テナント削除情報を出力します。
	 */
	public void logArguments(TenantDeleteParameter param) {
		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\ttenant id :" + param.getTenantId());
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	/**
	 * テナント削除用のパラメータを生成して、削除処理を実行します。
	 *
	 * @return
	 */
	private boolean startDeleteWizard() {

		//テナントURL
		String tenantUrl = readConsole(rs("Common.inputTenantUrlMsg"));

		if (StringUtil.isEmpty(tenantUrl) || tenantUrl.equalsIgnoreCase("-show")) {
			//一覧を出力
			showAllTenantList();
			return startDeleteWizard();
		}
		if (tenantUrl.equalsIgnoreCase("-env")) {
			//環境情報を出力
			logEnvironment();
			return startDeleteWizard();
		}

		//存在チェック
		TenantInfo tenant = toolService.getTenantInfo(tenantUrl);
		if (tenant == null) {
			logWarn(rs("Common.notExistsTenantMsg", tenantUrl));
			return startDeleteWizard();
		}

		TenantDeleteParameter deleteParam = new TenantDeleteParameter();
		deleteParam.setTenantId(tenant.getId());
		deleteParam.setTenantName(tenant.getName());

		//実行情報出力
		logArguments(deleteParam);

		boolean isExecute = readConsoleBoolean(rs("TenantBatch.Delete.Wizard.confirmRemoveTenantMsg"), false);
		if (!isExecute) {
			//再度実行
			return startDeleteWizard();
		}

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//削除処理実行
		return executeTask(deleteParam, (param) -> {
			return executeDelete(param);
		});
	}

	private boolean startCreateSilent(Properties prop) {
		// テナント名
		String tenantName = prop.getProperty("tenantName");
		if (StringUtil.isBlank(tenantName)) {
			logError(rs("TenantBatch.Silent.requiredTenantNameMsg"));
			return false;
		}

		// 管理者ID
		String adminUserId = prop.getProperty("adminUserId");
		if (StringUtil.isBlank(adminUserId)) {
			logError(rs("TenantBatch.Silent.requiredAdminUserIdMsg"));
			return false;
		}

		// 管理者パスワード
		String adminPassword = prop.getProperty("adminPassword");
		if (StringUtil.isBlank(adminPassword)) {
			logError(rs("TenantBatch.Silent.requiredAdminPasswordMsg"));
			return false;
		}

		TenantCreateParameter createParam = new TenantCreateParameter(tenantName, adminUserId, adminPassword);

		// テナントURL
		String tenantUrl = prop.getProperty("tenantUrl");
		if (StringUtil.isNotBlank(tenantUrl)) {
			createParam.setTenantUrl(tenantUrl);
		}

		// テナントURL存在チェック
		if (toolService.existsURL(createParam.getTenantUrl())) {
			logError(rs("TenantBatch.Silent.existsTenantMsg", createParam.getTenantUrl()));
			return false;
		}

		// テナント表示名
		String tenantDisplayName = prop.getProperty("tenantDisplayName");
		if (tenantDisplayName != null) {
			createParam.setTenantDisplayName(tenantDisplayName);
		}

		// TOP画面URL
		String topUrl = prop.getProperty("topUrl");
		if (topUrl != null) {
			createParam.setTopUrl(topUrl);
		}

		// 利用言語
		String useLanguages = prop.getProperty("useLanguages");
		if (useLanguages != null) {
			createParam.setUseLanguages(useLanguages);
		} else {
			createParam.setUseLanguages(toolService.getDefaultEnableLanguages());
		}

		// ブランクテナントの作成
		String createBlankTenant = prop.getProperty("createBlankTenant");
		if (createBlankTenant != null) {
			createParam.setCreateBlankTenant(Boolean.parseBoolean(createBlankTenant));
		}

		// サブパーティション数
		String strSubPartitionSize = prop.getProperty("subPartitionSize");
		if (strSubPartitionSize != null && StringUtil.isNotBlank(strSubPartitionSize)) {
			try {
				int subPartitionSize = Integer.parseInt(strSubPartitionSize);
				if (subPartitionSize < TenantRdbConstants.MIN_SUBPARTITION) {
					logError(rs("TenantBatch.Silent.invalidValueSubPartitionSizeMsg", TenantRdbConstants.MIN_SUBPARTITION));
					return false;
				}
				createParam.setSubPartitionSize(subPartitionSize);
			} catch (NumberFormatException e) {
				logError(rs("TenantBatch.Silent.invalidSubPartitionSizeMsg"));
				return false;
			}
		}

		// テナント作成者
		String registId = prop.getProperty("registId");
		if (registId != null) {
			createParam.setRegistId(registId);
		}

		// 実行情報出力
		logArguments(createParam);

		//作成処理実行
		boolean ret = executeTask(createParam, (param) -> {
			return executeCreate(param);
		});

		return ret;
	}

	private boolean startSilent() {
		// 設定ファイル名取得
		String configFileName = System.getProperty(KEY_CONFIG_FILE);
		if (StringUtil.isBlank(configFileName)) {
			logError(rs("TenantBatch.Silent.requiredConfigFileMsg", KEY_CONFIG_FILE));
			return false;
		}

		// 設定ファイルロード
		Properties prop = new Properties();
		try {
			Path path = Paths.get(configFileName);
			if (Files.exists(path)) {
				logDebug("load config file from file path:" + configFileName);
				try (Reader reader = Files.newBufferedReader(path)) {
					prop.load(reader);
				}
			} else {
				URL url = TenantBatch.class.getResource(configFileName);
				if (url != null) {
					logDebug("load config file from classpath:" + configFileName);
					try (Reader reader = Files.newBufferedReader(Paths.get(url.toURI()))) {
						prop.load(reader);
					}
				} else {
					logError(rs("TenantBatch.Silent.notExistsConfigFileMsg", configFileName));
					return false;
				}
			}
		} catch (IOException | URISyntaxException e) {
			throw new SystemException(e);
		}

		return startCreateSilent(prop);
	}

	private class TenantBatchLogListener implements LogHandler {

		@Override
		public void info(String message) {
			TenantBatch.this.logInfo(message);
		}

		@Override
		public void info(String message, Throwable e) {
			TenantBatch.this.logInfo(message, e);
		}

		@Override
		public void warn(String message) {
			TenantBatch.this.logWarn(message);
		}

		@Override
		public void warn(String message, Throwable e) {
			TenantBatch.this.logWarn(message, e);
		}

		@Override
		public void error(String message) {
			TenantBatch.this.logError(message);
		}

		@Override
		public void error(String message, Throwable e) {
			TenantBatch.this.logError(message, e);
		}
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
