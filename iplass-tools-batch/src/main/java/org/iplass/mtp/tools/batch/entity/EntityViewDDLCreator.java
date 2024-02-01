/*
 * Copyright (C) 2020 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.tools.batch.entity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.tools.entity.EntityToolService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityViewDDLCreator extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(EntityViewDDLCreator.class);

	private static final String ROOT_ENTITY = EntityHandler.ROOT_ENTITY_ID;

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static EntityToolService entityToolService = ServiceRegistry.getRegistry().getService(EntityToolService.class);

	private EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

	/** 実行モード */
	private ExecMode execMode = ExecMode.WIZARD;

	/** テナントID(0未満は未指定) */
	private int tenantId = -1;

	/** 上書を許可するか */
	private boolean isOverwrite = false;

	/** 階層下の全ての階層のEntityを対象とするか */
	private boolean isRecursive = true;

	/** 出力ファイルパス */
	private String outFile = "./view.ddl";

	/** Entityパス('/'はルート) */
	private String entityPath = "/";

	/**
	 * コンストラクタ
	 *
	 * args[0]・・・execMode["Wizard" or "Silent"]
	 * args[1]・・・tenantId[Less then zero is wizard mode]
	 * args[2]・・・overwrite["overwrite" or other]
	 * args[3]・・・recursive["recursive" or other]
	 * args[4]・・・outFile[Default is "./view.ddl"]
	 * args[5]・・・entityPath[Empty or "/" is root]
	 **/
	public EntityViewDDLCreator(String... args) {
		if (args != null) {
			if (args.length > 0) {
				setExecMode(args[0]);
			}
			if (args.length > 1) {
				setTenantId(args[1]);
				if (this.tenantId < 0) {
					setExecMode(ExecMode.WIZARD);
				}
			}
			if (args.length > 2) {
				setOverwrite(args[2]);
			}
			if (args.length > 3) {
				setRecursive(args[3]);
			}
			if (args.length > 4) {
				setOutFile(args[4]);
			}
			if (args.length > 5) {
				setEntityPath(args[5]);
			}
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
	 * 実行モードを設定します。
	 *
	 * @param execMode 実行モード
	 */
	public void setExecMode(String execMode) {
		setExecMode(ExecMode.valueOf(execMode.toUpperCase()));
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
	 * テナントIDを設定します。
	 *
	 * @param tenantId テナントID
	 */
	public void setTenantId(String tenantId) {
		try {
			setTenantId(Integer.parseInt(tenantId));
		} catch (NumberFormatException e) {
			setTenantId(-1);
		}
	}

	/**
	 * Entityパスを設定します。
	 *
	 * @param entityPath Entityパス
	 */
	public void setEntityPath(String entityPath) {
		this.entityPath = entityPath;
	}

	/**
	 * 出力ファイルを設定します。
	 *
	 * @param outFile 出力ファイル
	 */
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}

	/**
	 * 上書を許可するかを設定します。
	 *
	 * @param isForce 上書を許可する場合は<code>true</code>を設定
	 */
	public void setOverwrite(boolean isOverwrite) {
		this.isOverwrite = isOverwrite;
	}

	/**
	 * 上書を許可するかを設定します。
	 *
	 * @param force 上書を許可する場合は"overwrite"を設定
	 */
	public void setOverwrite(String overwrite) {
		setOverwrite("overwrite".equals(StringUtil.lowerCase(overwrite)));
	}

	/**
	 * 階層下の全てのEntityを対象とするかを設定します。
	 *
	 * @param recursive 階層下の全てのEntityを対象とする場合は<code>true</code>を設定
	 */
	public void setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}

	/**
	 * 階層下の全てのEntityを対象とするかを設定します。
	 *
	 * @param recursive 階層下の全てのEntityを対象とする場合は"recursive"を設定
	 */
	public void setRecursive(String recursive) {
		setRecursive("recursive".equals(StringUtil.lowerCase(recursive)));
	}

	/**
	 * メイン処理
	 *
	 * args[0]・・・execMode["Wizard" or "Silent"]
	 * args[1]・・・tenantId[Less then zero is wizard mode]
	 * args[2]・・・overwrite["overwrite" or other]
	 * args[3]・・・recursive["recursive" or other]
	 * args[4]・・・outFile[Default is "./view.ddl"]
	 * args[5]・・・entityPath[Empty or "/" is root]
	 *
	 * @param args 引数
	 */
	public static void main(String... args) {
		try {
			new EntityViewDDLCreator(args).execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	/**
	 * 処理実行
	 *
	 * @return 処理結果
	 * @throws Exception 例外
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

			// Wizard実行
			return startWizard();
		case SILENT :
			logInfo("■Start Silent");
			logInfo("");

			//Silentの場合はConsole出力を外す
			switchLog(false, true);

			// Silent実行
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
			String tenantId = readConsole(rs("EntityViewDDLCreator.Wizard.tenantIdMsg"));
			if (StringUtil.isNotBlank(tenantId)) {
				try {
					this.tenantId = Integer.parseInt(tenantId);
					validTenantId = true;
				} catch (NumberFormatException e) {
					logWarn(rs("EntityViewDDLCreator.Wizard.invalidTenantIdMsg", tenantId));
				}
			}
		} while(!validTenantId);

		// EntityPath
		boolean validEntityPath = false;
		do {
			String entityPath = readConsole(rs("EntityViewDDLCreator.Wizard.entityPathMsg"));
			if (StringUtil.isNotBlank(entityPath)) {
				setEntityPath(entityPath);
				validEntityPath = true;
			}
		} while(!validEntityPath);

		// OutFilePath
		String outFile = readConsole(rs("EntityViewDDLCreator.Wizard.outFilePathMsg", this.outFile));
		if (StringUtil.isNotBlank(outFile)) {
			setOutFile(outFile);
		}

		// Overwrite
		setOverwrite(readConsoleBoolean(rs("EntityViewDDLCreator.Wizard.overwriteMsg"), isOverwrite));

		// Recursive
		setRecursive(readConsoleBoolean(rs("EntityViewDDLCreator.Wizard.recursiveMsg"), isRecursive));

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		// EntityViewDDLファイル作成処理実行
		return executeTask(null, (param) -> {
			return proceed();
		});
	}

	private boolean proceed() {
		setSuccess(false);

		// テナント存在チェック
		TenantContext tc = tenantContextService.getTenantContext(tenantId);
		if (tc == null) {
			logError(rs("EntityViewDDLCreator.notFoundTenant", tenantId));
			return isSuccess();
		}

		Path outFilePath = Paths.get(outFile);

		// 出力ファイル存在チェック
		if (!isOverwrite) {
			if (Files.exists(outFilePath)) {
				logError(rs("EntityViewDDLCreator.alreadyExistOutFilePath", outFile));
				return isSuccess();
			}
		}

		return ExecuteContext.executeAs(tc, () -> {
			EntityDefinition ed = edm.get(entityPath);
			if (ed != null) {
				// 直接指定
				entityToolService.createViewDDL(outFilePath, ed);
			} else {
				// パス指定
				List<DefinitionSummary> defSumList = edm.definitionSummaryList(entityPath, isRecursive);
				if (defSumList.isEmpty()) {
					logError(rs("EntityViewDDLCreator.notFoundEntityPath", entityPath));
					return isSuccess();
				}

				EntityDefinition[] eds = defSumList.stream().filter(
						ds -> !ROOT_ENTITY.equals(ds.getPath())).map(ds -> edm.get(ds.getName())).toArray(EntityDefinition[]::new);

				entityToolService.createViewDDL(outFilePath, eds);
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
