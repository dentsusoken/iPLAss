/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.tools.batch.partition;

import java.util.List;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.tools.tenant.PartitionCreateParameter;
import org.iplass.mtp.impl.tools.tenant.PartitionInfo;
import org.iplass.mtp.impl.tools.tenant.TenantToolService;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.gui.partition.MySQLPartitionManagerApp;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLPartitionBatch extends MtpCuiBase implements PartitionBatch {

	private static Logger logger = LoggerFactory.getLogger(MySQLPartitionBatch.class);

	/** 実行モード */
	public enum MySQLPartitionBatchExecMode {GUI, CREATE};

	//実行モード
	private MySQLPartitionBatchExecMode execMode = MySQLPartitionBatchExecMode.GUI;

	private TenantToolService toolService = ServiceRegistry.getRegistry().getService(TenantToolService.class);

	/**
	 * args[0]・・・execMode
	 **/
	public static void main(String[] args) {

		MySQLPartitionBatch instance = null;
		try {
			instance = new MySQLPartitionBatch(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (instance.getExecMode() == MySQLPartitionBatchExecMode.GUI) {
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
	public MySQLPartitionBatch(String... args) {

		if (args != null) {
			if (args.length > 0) {
				setExecMode(MySQLPartitionBatchExecMode.valueOf(args[0]));
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

			MySQLPartitionManagerApp.main(new String[]{getLanguage()});
			return true;
		case CREATE :
			logInfo("■Start Create Wizard");
			logInfo("");

			//Wizardの実行
			return startCreateWizard();
		default :
			logError("unsupport execute mode : " + getExecMode());
			return false;
		}

	}

	public MySQLPartitionBatchExecMode getExecMode() {
		return execMode;
	}

	public void setExecMode(MySQLPartitionBatchExecMode execMode) {
		this.execMode = execMode;
	}

	/**
	 * <p>パーティション情報を返します。</p>
	 *
	 * @return パーティション情報
	 */
	@Override
	public List<PartitionInfo> getPartitionInfo() {

		return toolService.getPartitionInfo();
	}

	/**
	 * <p>パーティションを作成します。</p>
	 *
	 * @param param 作成条件
	 * @return 実行結果
	 */
	@Override
	public boolean createPartition(final PartitionCreateParameter param) {

		param.setLoggerLanguage(getLanguage());

		setSuccess(false);

		boolean isSuccess = toolService.createPartition(param, new LogHandler() {

			@Override
			public void info(String message) {
				MySQLPartitionBatch.this.logInfo(message);
			}

			@Override
			public void info(String message, Throwable e) {
				MySQLPartitionBatch.this.logInfo(message, e);
			}

			@Override
			public void warn(String message) {
				MySQLPartitionBatch.this.logWarn(message);
			}

			@Override
			public void warn(String message, Throwable e) {
				MySQLPartitionBatch.this.logWarn(message, e);
			}

			@Override
			public void error(String message) {
				MySQLPartitionBatch.this.logError(message);
			}

			@Override
			public void error(String message, Throwable e) {
				MySQLPartitionBatch.this.logError(message, e);
			}
		});

		setSuccess(isSuccess);

		return isSuccess();
	}

	private boolean startCreateWizard() {

		//MaxテナントID
		String strMaxTenantId = readConsole(rs("MySQLPartitionManager.Create.inputMaxTenantIdMsg"));

		if (StringUtil.isEmpty(strMaxTenantId)) {
			logWarn(rs("MySQLPartitionManager.Create.requiredMaxTenantIdMsg"));
			return startCreateWizard();
		}
		if (strMaxTenantId.equalsIgnoreCase("-show")) {
			//一覧を出力
			showAllPartitionList();
			return startCreateWizard();
		}
		if (strMaxTenantId.equalsIgnoreCase("-env")) {
			//環境情報を出力
			logEnvironment();
			return startCreateWizard();
		}

		//数値チェック
		PartitionCreateParameter param = new PartitionCreateParameter();
		try {
			int maxTenantId = Integer.parseInt(strMaxTenantId);
			param.setTenantId(maxTenantId);
		} catch (Exception e) {
			logWarn(rs("MySQLPartitionManager.Create.warnMaxTenantIdMsg"));
			return startCreateWizard();
		}

		//実行情報出力
		logArguments(param);

		boolean isExecute = readConsoleBoolean(rs("MySQLPartitionManager.Create.confirmCreatePartitionMsg"), false);
		if (!isExecute) {
			//再度実行
			return startCreateWizard();
		}

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//作成処理実行
		return executeTask(param, (paramA) -> {
			return createPartition(paramA);
		});
	}

	/**
	 * パーティションの一覧を出力します。
	 */
	private void showAllPartitionList() {
		try {

			List<PartitionInfo> partitionList = getPartitionInfo();
			logInfo("-----------------------------------------------------------");
			logInfo("■Partition List(Max Tenant ID)");
			for (PartitionInfo partition : partitionList) {
				logInfo("[" + partition.getTableName() + "] " + partition.getMaxTenantId());
			}
			logInfo("-----------------------------------------------------------");
		} catch (Exception e) {
			throw new SystemException(e);
		}
	}

	private void logArguments(PartitionCreateParameter param) {
		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\tmax tenant id :" + param.getTenantId());
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
