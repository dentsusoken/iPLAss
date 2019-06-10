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

package org.iplass.mtp.tools.batch.entity;

import java.io.File;
import java.util.List;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionSummary;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tools.entity.EntityToolService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.util.StringUtil;

public class EntityJavaMappingClassCreator extends MtpCuiBase {

	private static final String ROOT_ENTITY = "/entity/Entity";

	private static TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private static EntityToolService entityToolService = ServiceRegistry.getRegistry().getService(EntityToolService.class);

	private EntityDefinitionManager edm = ManagerLocator.getInstance().getManager(EntityDefinitionManager.class);

	/** 実行モード */
	private ExecMode execMode = ExecMode.WIZARD;

	/** テナントID */
	private int tenantId = -1;

	/** 階層下の全てのEntityを対象とするか */
	private boolean isRecursive = true;

	/** 強制上書きとするか */
	private boolean isForce = false;

	/** 出力ディレクトリ */
	private String outDir = ".";

	/** Entityパス */
	private String entityPath;

	/** ベースパッケージ */
	private String basePackage;

	/**
	 * コンストラクタ
	 *
	 * args[0]・・・execMode["Wizard" or "Silent"]
	 * args[1]・・・tenantId
	 * args[2]・・・recursive["recursive" or other]
	 * args[3]・・・force["force" or other]
	 * args[4]・・・outDir[Current is empty or "."]
	 * args[5]・・・entityPath[Root is empty or "/"]
	 * args[6]・・・basePackage
	 **/
	public EntityJavaMappingClassCreator(String... args) {
		if (args != null) {
			if (args.length > 0) {
				execMode = ExecMode.valueOf(args[0]);
			}
			if (args.length > 1) {
				setTenantId(Integer.parseInt(args[1]));
			}
			if (args.length > 2) {
				setRecursive(args[2]);
			}
			if (args.length > 3) {
				setForce(args[3]);
			}
			if (args.length > 4) {
				setOutDir(args[4]);
			}
			if (args.length > 5) {
				setEntityPath(args[5]);
			}
			if (args.length > 6) {
				setBasePackage(args[6]);
			}
		}
	}

	/**
	 * メイン処理
	 *
	 * @param args 引数
	 */
	public static void main(String... args) {
		try {
			new EntityJavaMappingClassCreator(args).execute();
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
	 * Entityパスを設定します。
	 *
	 * @param entityPath Entityパス
	 */
	public void setEntityPath(String entityPath) {
		this.entityPath = entityPath;
	}

	/**
	 * 出力ディレクトリを設定します。
	 *
	 * @param outDir 出力ディレクトリ
	 */
	public void setOutDir(String outDir) {
		this.outDir = outDir;
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
	 * 階層下の全てのEntityを対象とするかを設定します。
	 *
	 * @param recursive 階層下の全てのEntityを対象とする場合は<code>true</code>を設定
	 */
	public void setRecursive(boolean isRecursive) {
		this.isRecursive = isRecursive;
	}

	/**
	 * 強制上書きとするかを設定します。
	 *
	 * @param force 強制上書きとする場合は"force"を設定
	 */
	public void setForce(String force) {
		this.isForce = "force".equals(StringUtil.lowerCase(force));
	}

	/**
	 * 強制上書きとするかを設定します。
	 *
	 * @param isForce 強制上書きとする場合は<code>true</code>を設定
	 */
	public void setForce(boolean isForce) {
		this.isForce = isForce;
	}

	/**
	 * ベースパッケージを設定します。
	 *
	 * @param basePackage ベースパッケージ
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

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
			String tenantId = readConsole(rs("EntityJavaMappingClassCreator.Wizard.tenantIdMsg"));
			if (StringUtil.isNotBlank(tenantId)) {
				try {
					setTenantId(Integer.parseInt(tenantId));
					validTenantId = true;
				} catch (NumberFormatException e) {
					logWarn(rs("EntityJavaMappingClassCreator.Wizard.invalidTenantIdMsg", tenantId));
				}
			}
		} while(validTenantId == false);

		// EntityPath
		String entityPath = readConsole(rs("EntityJavaMappingClassCreator.Wizard.entityPathMsg"));
		if (StringUtil.isNotBlank(entityPath)) {
			setEntityPath(entityPath);
		}

		// BasePackage
		String basePackage = readConsole(rs("EntityJavaMappingClassCreator.Wizard.basePackageMsg"));
		if (StringUtil.isNotBlank(basePackage)) {
			setBasePackage(basePackage);
		}

		// OutDir
		String outDir = readConsole(rs("EntityJavaMappingClassCreator.Wizard.outDirMsg"));
		if (StringUtil.isNotBlank(outDir)) {
			setOutDir(outDir);
		}

		// Recursive
		setRecursive(readConsoleBoolean(rs("EntityJavaMappingClassCreator.Wizard.recursiveMsg"), isRecursive));

		// Force
		setForce(readConsoleBoolean(rs("EntityJavaMappingClassCreator.Wizard.forceMsg"), isForce));

		// EntityJavaMappingClassファイル作成処理実行
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
				logError(rs("EntityJavaMappingClassCreator.notFoundTenant", tenantId));
				return isSuccess();
			}

			ExecuteContext.initContext(new ExecuteContext(tCtx));

			EntityDefinition ed = edm.get(entityPath);
			if (ed != null) {
				// Entity直接指定
				String entityName = ed.getName();
				if (StringUtil.isBlank(basePackage)
						&& ed.getMapping() != null && StringUtil.isNotBlank(ed.getMapping().getMappingModelClass())) {
					entityName = ed.getMapping().getMappingModelClass();
				}
				File file = new File(generateJavaClassFileName(entityName));
				if (!isForce && file.exists()) {
					// 上書き確認
					if (!readConsoleBoolean(rs("EntityJavaMappingClassCreator.confirmOverwrite", file.getPath()), false)) {
						setSuccess(true);
						return isSuccess();
					}
				}

				entityToolService.createJavaMappingClass(file, ed, basePackage);
			} else {
				// Entity階層パス指定
				List<DefinitionSummary> list = edm.definitionSummaryList(entityPath, isRecursive);
				list.forEach(ds -> {
					if (ROOT_ENTITY.equals(ds.getPath())) {
						return;
					}

					String entityName = ds.getName();
					EntityDefinition edx = edm.get(ds.getName());
					if (StringUtil.isBlank(basePackage)
							&& edx.getMapping() != null && StringUtil.isNotBlank(edx.getMapping().getMappingModelClass())) {
						entityName = edx.getMapping().getMappingModelClass();
					}
					File file = new File(generateJavaClassFileName(entityName));
					if (!isForce && file.exists()) {
						// 上書き確認
						if (!readConsoleBoolean(rs("EntityJavaMappingClassCreator.confirmOverwrite", file.getPath()), false)) {
							return;
						}
					}

					entityToolService.createJavaMappingClass(file, edx, basePackage);
				});
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

	private String generateJavaClassFileName(String entityName) {
		StringBuilder sb = new StringBuilder();

		sb.append(outDir).append('/');
		if (StringUtil.isNotBlank(basePackage)) {
			sb.append(basePackage.replace('.', '/')).append('/');
		}
		sb.append(entityName.replace('.', '/')).append(".java");

		return sb.toString();
	}

}
