/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.entityport.EntityDataExportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityPortingService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Entity Export Batch
 */
public class EntityExport extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(EntityExport.class);

	/** 設定ファイル名指定KEY */
	public static final String KEY_CONFIG_FILE = "entity.config";

	/** 実行モード */
	private ExecMode execMode = ExecMode.WIZARD;

	/** テナントID(引数) */
	private Integer tenantId;

	/** テナントUrl(引数) */
	private String tenantUrl;

	/** Entity名(引数) */
	private String entityName;

	/** 設定ファイルプロパティ */
	private Properties properties;

	/** 実行テナント */
	private Tenant tenant;

	/** 実行Entity */
	private EntityDefinition ed;
	
	/** 出力ファイル */
	private File exportFile;

	/** 出力ファイル名 */
	private String exportFileName;

	private TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);
	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private EntityPortingService eps = ServiceRegistry.getRegistry().getService(EntityPortingService.class);
	private EntityDefinitionManager edm = ManagerLocator.manager(EntityDefinitionManager.class);

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・entity name
	 * args[3]・・・export file
	 **/
	public static void main(String[] args) {

		EntityExport instance = null;
		try {
			instance = new EntityExport(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・entity name
	 * args[3]・・・export file
	 **/
	public EntityExport(String... args) {

		if (args != null) {
			if (args.length > 0 && StringUtil.isNotBlank(args[0])) {
				execMode = ExecMode.valueOf(args[0].toUpperCase());
			}
			if (args.length > 1 && StringUtil.isNotBlank(args[1])) {
				tenantId = Integer.parseInt(args[1]);
				//-1の場合は、未指定
				if (tenantId == -1) {
					tenantId = null;
				}
			}
			if (args.length > 2 && StringUtil.isNotBlank(args[2])) {
				entityName = args[2];
			}
			if (args.length > 3 && StringUtil.isNotBlank(args[3])) {
				exportFileName = args[3];
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

		//コンフィグファイル読み込み
		loadConfigBaseInfo();

		//引数検証
		if (!validateArguments()) {
			logInfo("");
			logError("■Execute Result : FAILED");
			return false;
		}

		switch (execMode) {
		case WIZARD :
			logInfo("■Start Export Wizard");
			logInfo("");

			//Wizardの実行
			return wizard();
		case SILENT :
			logInfo("■Start Export Silent");
			logInfo("");

			//Silentの場合はConsole出力を外す
			switchLog(false, true);

			//Silentの実行
			return silent();
		default :
			logError("unsupport execute mode : " + execMode);
			return false;
		}
	}

	public EntityExport execMode(ExecMode execMode) {
		this.execMode = execMode;
		return this;
	}

	public EntityExport tenantId(Integer tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public EntityExport entity(String entityName) {
		this.entityName = entityName;
		return this;
	}

	public EntityExport file(String exportFile) {
		this.exportFileName = exportFile;
		return this;
	}

	/**
	 * Exportします。
	 *
	 * @param param Export情報
	 * @return 実行結果
	 */
	public boolean exportEntity(final EntityExportParameter param) {

		setSuccess(false);

		boolean isSuccess = Transaction.required(new Function<Transaction, Boolean>() {

			@Override
			public Boolean apply(Transaction t) {

				return executeTask(param, new EntityDataExportTask(param));
			}
		});

		setSuccess(isSuccess);

		return isSuccess();
	}

	/**
	 * Export情報を出力します。
	 */
	public void logArguments(EntityExportParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\tentity name :" + param.getEntityName());
		logInfo("\texport file :" + param.getExportFileName());

		EntityDataExportCondition condition = param.getEntityExportCondition();
		logInfo("\twhere clause :" + condition.getWhereClause());
		logInfo("\torder by clause :" + condition.getOrderByClause());
		logInfo("\tsearch all version :" + condition.isVersioned());

		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	/**
	 * タスクを実行します。
	 */
	private <T> T executeTask(EntityExportParameter param, Supplier<T> task) {

		TenantContext tc  = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			return task.get();
		});
	}

	/**
	 * Entityデータをエクスポートします。
	 */
	private class EntityDataExportTask implements Supplier<Boolean> {

		private final EntityExportParameter param;

		public EntityDataExportTask(final EntityExportParameter param) {
			this.param = param;
		}

		@Override
		public Boolean get() {
			String entityPath =  EntityService.ENTITY_META_PATH + param.getEntityName().replace(".", "/");
			MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(entityPath);
			if (entry == null) {
				throw new SystemException(rs("EntityExport.notExistsEntityMsg", param.getEntityName()));
			}

			try (FileOutputStream fos = new FileOutputStream(param.getExportFile())) {
				eps.write(fos, entry, param.getEntityExportCondition());
				return true;
			} catch (IOException e) {
				throw new SystemException(e);
			}
		}
	}

	/**
	 * Wizard形式でExport用のパラメータを生成して、Export処理を実行します。
	 *
	 * @return 実行結果
	 */
	private boolean wizard() {

		if (tenant == null) {
			//テナントが未確定の場合
			String tenantUrl = readConsole(rs("Common.inputTenantUrlMsg"));

			if (StringUtil.isEmpty(tenantUrl)) {
				logWarn(rs("Common.requiredTenantUrlMsg"));
				return wizard();
			}
			if (tenantUrl.equalsIgnoreCase("-show")) {
				//一覧を出力
				showValidTenantList();
				return wizard();
			}
			if (tenantUrl.equalsIgnoreCase("-env")) {
				//環境情報を出力
				logEnvironment();
				return wizard();
			}

			//URL存在チェック
			String url = tenantUrl.startsWith("/") ? tenantUrl : "/" + tenantUrl;
			tenant = ts.getTenant(url);
			if (tenant == null) {
				logWarn(rs("Common.notExistsTenantMsg", tenantUrl));
				return wizard();
			}
		}

		EntityExportParameter param = new EntityExportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			//Entity名
			boolean validEntity = false;
			do {
				String inputEntityName = entityName;
				entityName = null;
				if (StringUtil.isEmpty(inputEntityName)) {
					inputEntityName = readConsole(rs("EntityExport.Wizard.inputEntityNameMsg"));
				}
				if (StringUtil.isNotBlank(inputEntityName)) {
					param.setEntityName(inputEntityName);

					//存在チェック
					EntityDefinition ed = edm.get(inputEntityName);
					if (ed == null) {
						logWarn(rs("EntityExport.notExistsEntityMsg", inputEntityName));
						continue;
					}
					validEntity = true;
				} else {
					logWarn(rs("EntityExport.Wizard.requiredEntityNameMsg"));
				}
			} while(validEntity == false);

			//Exportファイル
			boolean validExportFile = false;
			do {
				if (StringUtil.isEmpty(exportFileName)) {
					exportFileName = readConsole(rs("EntityExport.Wizard.inputExportFileMsg"));
				}
				if (StringUtil.isNotBlank(exportFileName)) {
					param.setExportFile(new File(exportFileName));
					validExportFile = true;
				} else {
					logWarn(rs("EntityExport.Wizard.requiredExportFileMsg"));
				}
			} while(validExportFile == false);

			//オプションをコンフィグから取得
			EntityDataExportCondition condition = loadConfigCondition();
			if (condition == null) {
				condition = new EntityDataExportCondition();
			}
			
			String whereClause = readConsole(rs("EntityExport.Wizard.inputWhereClauseMsg"));
			if (StringUtil.isNotBlank(whereClause)) {
				condition.setWhereClause(whereClause);
			}
			
			String orderByClause = readConsole(rs("EntityExport.Wizard.inputOrderByClauseMsg"));
			if (StringUtil.isNotBlank(orderByClause)) {
				condition.setOrderByClause(orderByClause);
			}
			
			boolean isSearchAllVersion = readConsoleBoolean(rs("EntityExport.Wizard.confirmSearchAllVersionMsg"), condition.isVersioned());
			condition.setVersioned(isSearchAllVersion);
			
			//条件をセット
			param.setEntityExportCondition(condition);
			
			return null;
		});

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//Export処理実行
		return executeTask(param, (paramA) -> {
			return exportEntity(paramA);
		});
	}

	/**
	 * コンフィグファイルをロードします。
	 *
	 * @return プロパティ情報
	 */
	private Properties loadConfig() {

		if (properties != null) {
			return properties;
		}

		//プロパティファイルの取得
		String configFileName = System.getProperty(KEY_CONFIG_FILE);
		if (StringUtil.isEmpty(configFileName)) {
			return null;
		}

		//プロパティの取得
		Properties prop = new Properties();
		try {
			Path path = Paths.get(configFileName);
			if (Files.exists(path)) {
				logDebug("load config file from file path:" + configFileName);
				try (InputStream is = new FileInputStream(path.toFile());
						InputStreamReader reader = new InputStreamReader(is, "UTF-8");) {
						prop.load(reader);
					}
			} else {
				logDebug("load config file from classpath:" + configFileName);
				try (InputStream is = EntityExport.class.getResourceAsStream(configFileName)) {
					if (is == null) {
						logWarn(rs("EntityExport.notExistsConfigFileMsg", configFileName));
						return null;
					}
					try (InputStreamReader reader = new InputStreamReader(is, "UTF-8")) {
						prop.load(reader);
					}
				}
			}
		} catch (IOException e) {
			throw new SystemException(e);
		}
		properties = prop;
		return properties;
	}

	/**
	 * コンフィグファイルから基本情報を取得します。
	 */
	private void loadConfigBaseInfo() {

		//プロパティの取得
		Properties prop = loadConfig();
		if (prop == null) {
			return;
		}

		//テナント
		if (tenantId == null) {
			//引数でテナントが指定されていない場合、プロパティから取得
			String propTenantId = prop.getProperty(EntityExportParameter.PROP_TENANT_ID);
			if (StringUtil.isNotEmpty(propTenantId)) {
				tenantId = Integer.parseInt(propTenantId);
			}
			if (tenantId == null) {
				String propTenantUrl = prop.getProperty(EntityExportParameter.PROP_TENANT_URL);
				if (StringUtil.isNotEmpty(propTenantUrl)) {
					if (!propTenantUrl.startsWith("/")) {
						propTenantUrl = "/" + propTenantUrl;
					}
					tenantUrl = propTenantUrl;
				}
			}
		}

		//Entity名
		if (entityName == null) {
			String propEntityName = prop.getProperty(EntityExportParameter.PROP_ENTITY_NAME);
			if (StringUtil.isNotEmpty(propEntityName)) {
				entityName = propEntityName;
			}
		}

		//ファイル名
		if (exportFileName == null) {
			String propExportFileName = prop.getProperty(EntityExportParameter.PROP_EXPORT_FILE);
			if (StringUtil.isNotEmpty(propExportFileName)) {
				exportFileName = propExportFileName;
			}
		}
	}

	/**
	 * コンフィグファイルからExport条件を取得します。
	 *
	 * @return Export条件(コンフィグファイルが無い場合はnull)
	 */
	private EntityDataExportCondition loadConfigCondition() {

		//プロパティの取得
		Properties prop = loadConfig();
		if (prop == null) {
			return null;
		}

		EntityDataExportCondition condition = new EntityDataExportCondition();

		String propWhereClause = prop.getProperty(EntityExportParameter.PROP_ENTITY_WHERE_CLAUSE);
		if (StringUtil.isNotEmpty(propWhereClause)) {
			condition.setWhereClause(propWhereClause);
		}

		String propOrderClause = prop.getProperty(EntityExportParameter.PROP_ENTITY_ORDER_BY_CLAUSE);
		if (StringUtil.isNotEmpty(propOrderClause)) {
			condition.setOrderByClause(propOrderClause);
		}

		String propSearchAllVersion = prop.getProperty(EntityExportParameter.PROP_ENTITY_VERSIONED);
		if (StringUtil.isNotEmpty(propSearchAllVersion)) {
			condition.setVersioned(Boolean.valueOf(propSearchAllVersion));
		}

		return condition;
	}

	/**
	 * 引数の検証
	 *
	 * @return false:エラー
	 */
	private boolean validateArguments() {

		//テナント
		if (tenantId != null) {
			Tenant tenant = ts.getTenant(tenantId);
			if (tenant == null) {
				logError(rs("Common.notExistsTenantIdMsg", tenantId));
				return false;
			}
			this.tenant = tenant;
		} else if (tenantUrl != null) {
			Tenant tenant = ts.getTenant(tenantUrl);
			if (tenant == null) {
				logError(rs("Common.notExistsTenantUrlMsg", tenantUrl));
				return false;
			}
			this.tenant = tenant;
		}

		//Entity名
		if (StringUtil.isNotEmpty(entityName)) {
			if (tenant != null) {
				//テナントが確定している場合のみ存在チェック
				TenantContext tc = tcs.getTenantContext(tenant.getId());
				boolean entityValid = ExecuteContext.executeAs(tc, ()->{
					ExecuteContext.getCurrentContext().setLanguage(getLanguage());

					EntityDefinition ed = edm.get(entityName);
					if (ed == null) {
						logError(rs("EntityExport.notExistsEntityMsg", entityName));
						return false;
					}
					this.ed = ed;
					return true;
				});
				if (!entityValid) {
					return false;
				}
			}
		}

		//ファイル
		if (StringUtil.isNotEmpty(exportFileName)) {
			File exportFile = new File(exportFileName);
			this.exportFile = exportFile;
		}
		return true;
	}


	/**
	 * バッチ引数またはコンフィグファイル形式でExport用のパラメータを生成して、Export処理を実行します。
	 *
	 * @return false:エラー
	 */
	private boolean silent() {

		if (tenant == null) {
			logError(rs("Common.requiredMsg", EntityExportParameter.PROP_TENANT_ID + " or " + EntityExportParameter.PROP_TENANT_URL));
			return false;
		}
		logInfo("target tenant:[" + tenant.getId() + "]" + tenant.getName());

		EntityExportParameter param = new EntityExportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			//Entity
			if (ed == null) {
				logError(rs("Common.requiredMsg", "Entity name"));
				return false;
			}
			param.setEntityName(ed.getName());

			//File
			if (exportFileName == null) {
				logError(rs("Common.requiredMsg", "Export file"));
				return false;
			}
			param.setExportFileName(exportFileName);
			param.setExportFile(exportFile);

			//オプションをコンフィグから取得
			EntityDataExportCondition condition = loadConfigCondition();
			if (condition == null) {
				condition = new EntityDataExportCondition();
			}
			param.setEntityExportCondition(condition);

			//実行情報出力
			logArguments(param);

			//Export処理実行
			return executeTask(param, (paramA) -> {
				return exportEntity(paramA);
			});
		});

	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
