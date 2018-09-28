/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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


import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.tools.tenant.TenantCreateParameter;
import org.iplass.mtp.impl.tools.tenant.TenantDeleteParameter;
import org.iplass.mtp.impl.tools.tenant.TenantInfo;
import org.iplass.mtp.impl.tools.tenant.TenantToolService;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.gui.tenant.TenantManagerApp;
import org.iplass.mtp.util.StringUtil;


/**
 * テナント情報を管理するクラス。
 */
public class TenantBatch extends MtpCuiBase {

	/** 実行モード */
	public enum TenantBatchExecMode {GUI, CREATE, DELETE, SHOW};

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

		//Console出力用のログリスナーを生成
		LogListner consoleLogListner = getConsoleLogListner();
		addLogListner(consoleLogListner);

		//環境情報出力
		logEnvironment();

		switch (getExecMode()) {
		case GUI :
			logInfo("■Start App");
			logInfo("");

			//Guiの場合はConsole出力を外す
			removeLogListner(consoleLogListner);

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
	 * テナントを作成します。（TenantManagerApp2からの実行もこちらを呼び出す）
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

		} catch (Throwable e) {
			logError(rs("Common.errorMsg", e.getMessage()), e);
		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
			logInfo("");

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
		if (getConfigSetting().isMySQL()) {
			logInfo("\tmysql use subpartition :" + param.isMySqlUseSubPartition());
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
		do {
			adminPW = readConsolePassword(rs("TenantBatch.Create.Wizard.inputAdminPWMsg"));
			if (StringUtil.isEmpty(adminPW)) {
				logWarn(rs("TenantBatch.Create.Wizard.requiredAdminPWMsg"));
				adminPW = null;
			}
		} while(adminPW == null);

		TenantCreateParameter param = new TenantCreateParameter(tenantName, adminUserId, adminPW);
		param.setTenantUrl(tenantUrl);
		param.setUseLanguages(getLanguage());

		//デフォルトスキップチェック
		boolean isDefault = readConsoleBoolean(rs("TenantBatch.Create.Wizard.confirmDefaultMsg"), false);

		if (!isDefault) {
			String tenantDisplayName = readConsole(rs("TenantBatch.Create.Wizard.inputTenantDispNameMsg") + "(" + param.getTenantDisplayName() + ")");
			if (StringUtil.isNotBlank(tenantDisplayName)) {
				param.setTenantDisplayName(tenantDisplayName);
			}
			String topURL = readConsole(rs("TenantBatch.Create.Wizard.inputTopUrlMsg") + "(" + param.getTopUrl() + ")");
			if (StringUtil.isNotBlank(topURL)) {
				param.setTopUrl(topURL);
			}
			String lang = readConsole(rs("TenantBatch.Create.Wizard.useMultiLangMsg"));
			if (StringUtil.isNotBlank(lang)) {
				param.setUseLanguages(lang);
			}

			boolean isBlank = readConsoleBoolean(rs("TenantBatch.Create.Wizard.createBlankTenantMsg"), false);
			param.setCreateBlankTenant(isBlank);

			if (getConfigSetting().isMySQL()) {
				boolean isSubPartition = readConsoleBoolean(rs("TenantBatch.Create.Wizard.useSubPartitionMsg"), false);
				param.setMySqlUseSubPartition(isSubPartition);
			}
		}

		//実行情報出力
		logArguments(param);

		boolean isExecute = readConsoleBoolean(rs("TenantBatch.Create.Wizard.confirmCreateTenantMsg"), false);
		if (!isExecute) {
			//再度実行
			return startCreateWizard();
		}

		//ConsoleのLogListnerを一度削除してLog出力に切り替え
		LogListner consoleLogListner = getConsoleLogListner();
		removeLogListner(consoleLogListner);
		LogListner loggingListner = getLoggingLogListner();
		addLogListner(loggingListner);

		//作成処理実行
		boolean ret = executeCreate(param);

		//LogListnerを一度削除
		removeLogListner(loggingListner);

		return ret;
	}

	/**
	 * テナントを削除します。（TenantManagerApp2からの実行もこちらを呼び出す）
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

		} catch (Throwable e) {
			logError(rs("Common.errorMsg", e.getMessage()));
		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
			logInfo("");

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
		if (getConfigSetting().isMySQL()) {
			logInfo("\tmysql drop partition :" + param.isMySqlDropPartition());
		}
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

		TenantDeleteParameter param = new TenantDeleteParameter();
		param.setTenantId(tenant.getId());
		param.setTenantName(tenant.getName());

		if (getConfigSetting().isMySQL()) {
			boolean isPartitionDelete = readConsoleBoolean(rs("TenantBatch.Delete.Wizard.confirmDropPartitionMsg"), true);
			param.setMySqlDropPartition(isPartitionDelete);
		}

		//実行情報出力
		logArguments(param);

		boolean isExecute = readConsoleBoolean(rs("TenantBatch.Delete.Wizard.confirmRemoveTenantMsg"), false);
		if (!isExecute) {
			//再度実行
			return startDeleteWizard();
		}

		//ConsoleのLogListnerを一度削除してLog出力に切り替え
		LogListner consoleLogListner = getConsoleLogListner();
		removeLogListner(consoleLogListner);
		LogListner loggingListner = getLoggingLogListner();
		addLogListner(loggingListner);

		//削除処理実行
		boolean ret = executeDelete(param);

		//LogListnerを一度削除
		removeLogListner(loggingListner);

		return ret;
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
}
