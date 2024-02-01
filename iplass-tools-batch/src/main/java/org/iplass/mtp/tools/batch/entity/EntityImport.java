/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
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

import static org.iplass.mtp.tools.batch.entity.EntityImportParameter.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.EntityDefinitionManager;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.internal.InternalCredential;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportResult;
import org.iplass.mtp.impl.tools.entityport.EntityPortingService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Entity Import Batch
 */
public class EntityImport extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(EntityImport.class);

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

	/** インポートファイル名(引数) */
	private String importFileName;

	/** ユーザーID(InsertするEntityにAuditプロパティの値を指定する場合、必須) */
	private String userId;

	/** パスワード */
	private String password;

	/** 設定ファイルプロパティ */
	private Properties properties;

	/** 実行テナント */
	private Tenant tenant;

	/** 実行Entity */
	private EntityDefinition ed;

	/** 実行CSVファイル */
	private File importFile;
	
	/** BinaryデータをImportするか */
	private boolean isImportBinaryData;

	private TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);
	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private EntityPortingService eps = ServiceRegistry.getRegistry().getService(EntityPortingService.class);
	private AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
	private EntityDefinitionManager edm = ManagerLocator.manager(EntityDefinitionManager.class);

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・entity name
	 * args[3]・・・import file
	 * args[4]・・・import binary data
	 **/
	public static void main(String[] args) {

		EntityImport instance = null;
		try {
			instance = new EntityImport(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・entity name
	 * args[3]・・・import file
	 * args[4]・・・import binary data
	 **/
	public EntityImport(String... args) {

		if (args != null) {
			if (args.length > 0 && args[0] != null) {
				execMode = ExecMode.valueOf(args[0].toUpperCase());
			}
			if (args.length > 1 && args[1] != null) {
				tenantId = Integer.parseInt(args[1]);
				//-1の場合は、未指定
				if (tenantId == -1) {
					tenantId = null;
				}
			}
			if (args.length > 2 && args[2] != null) {
				//emptyの場合は、未指定
				if (!"empty".equals(args[2].toLowerCase())) {
					entityName = args[2];
				}
			}
			if (args.length > 3 && args[3] != null) {
				//emptyの場合は、未指定
				if (!"empty".equals(args[3].toLowerCase())) {
					importFileName = args[3];
				}
			}
			if (args.length > 4) {
				isImportBinaryData =  Boolean.parseBoolean(args[4]);;
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
			logInfo("■Start Import Wizard");
			logInfo("");

			//Wizardの実行
			return wizard();
		case SILENT :
			logInfo("■Start Import Silent");
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

	public EntityImport execMode(ExecMode execMode) {
		this.execMode = execMode;
		return this;
	}

	public EntityImport tenantId(Integer tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public EntityImport entity(String entityName) {
		this.entityName = entityName;
		return this;
	}

	public EntityImport file(String importFile) {
		this.importFileName = importFile;
		return this;
	}
	
	public EntityImport importBinaryData(boolean isImportBinaryData) {
		this.isImportBinaryData = isImportBinaryData;
		return this;
	}

	/**
	 * Importします。
	 *
	 * @param param Import情報
	 * @return
	 */
	public boolean importEntity(final EntityImportParameter param) {

		setSuccess(false);

		boolean isSuccess = Transaction.required(new Function<Transaction, Boolean>() {

			@Override
			public Boolean apply(Transaction t) {

				final List<String> messageSummary = new ArrayList<>();

				boolean ret = executeTask(param, new EntityDataImportTask(param, messageSummary));

				logInfo("-----------------------------------------------------------");
				logInfo("■Execute Result Summary");
				for(String message : messageSummary) {
					logInfo(message);
				}
				logInfo("-----------------------------------------------------------");
				logInfo("");

				logInfo(rs("EntityImport.completedImportEntityLog", param.getImportFilePath()));

				return ret;
			}
		});

		setSuccess(isSuccess);

		return isSuccess();
	}

	/**
	 * PackageImport情報を出力します。
	 */
	public void logArguments(EntityImportParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\tentity name :" + param.getEntityName());
		logInfo("\timport file :" + param.getImportFilePath());
		logInfo("\timport binary data :" + param.isImportBinaryData());
		logInfo("\timport file locale :" + param.getLocale());
		logInfo("\timport file timezone :" + param.getTimezone());

		EntityDataImportCondition condition = param.getEntityImportCondition();
		logInfo("\tentity data truncate :" + condition.isTruncate());
		logInfo("\tentity data force update :" + condition.isFourceUpdate());
		logInfo("\tentity data error skip :" + condition.isErrorSkip());
		logInfo("\tentity ignore not exists property :" + condition.isIgnoreNotExistsProperty());
		logInfo("\tentity execute listner :" + condition.isNotifyListeners());
		logInfo("\tentity update disupdatable property :" + condition.isUpdateDisupdatableProperty());
		logInfo("\tentity insert audit property specification :" + condition.isInsertEnableAuditPropertySpecification());
		logInfo("\tentity execute validation :" + condition.isWithValidation());
		logInfo("\tentity commit limit :" + condition.getCommitLimit());
		logInfo("\tentity oid prefix :" + condition.getPrefixOid());

		if (condition.isInsertEnableAuditPropertySpecification()) {
			logInfo("\tentity import execute user id :" + userId);
		}

		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	/**
	 * タスクを実行します。
	 */
	private <T> T executeTask(EntityImportParameter param, Supplier<T> task) {

		TenantContext tc  = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			if (StringUtil.isEmpty(userId)) {
				return task.get();
			} else {
				//ログインして実行
				try {
					as.login(StringUtil.isNotEmpty(password) ? new IdPasswordCredential(userId, password) : new InternalCredential(userId));
					return as.doSecuredAction(AuthContextHolder.getAuthContext(), () -> {
						return task.get();
					});
				} finally {
					as.logout();
				}
			}
		});
	}

	/**
	 * Entityデータをインポートします。
	 */
	private class EntityDataImportTask implements Supplier<Boolean> {

		private final EntityImportParameter param;
		private final List<String> messageSummary;

		public EntityDataImportTask(final EntityImportParameter param, final List<String> messageSummary) {
			this.param = param;
			this.messageSummary = messageSummary;
		}

		@Override
		public Boolean get() {

			//work用
			String logMessage = null;

			//Entityデータの登録
			logInfo(rs("EntityImport.startImportEntityLog"));

			String entityPath =  EntityService.ENTITY_META_PATH + param.getEntityName().replace(".", "/");
			MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(entityPath);
			if (entry == null) {
				throw new SystemException(rs("EntityImport.notExistsEntityMsg", param.getEntityName()));
			}
			logInfo(rs("EntityImport.startImportEntityDataLog", entityPath));

			EntityDataImportResult entityResult = null;
			
			String importBinaryDataDir = param.isImportBinaryData() ? param.getImportFile().getParent() : null;
			try (FileInputStream fis = new FileInputStream(param.getImportFile())) {
				entityResult = eps.importEntityData(param.getImportFilePath(), fis, entry, param.getEntityImportCondition(), null, importBinaryDataDir);
			} catch (IOException e) {
				throw new SystemException(e);
			}

			if (entityResult.isError()) {
				if (entityResult.getMessages() != null) {
					for (String message : entityResult.getMessages()) {
						logError(message);
					}
					logInfo("");
				}

				logMessage = rs("EntityImport.errorImportEntityDataLog", entityPath);
				logError(logMessage);
				messageSummary.add("[ERROR]" + logMessage);

				//エラースキップしない場合は、ここで終了
				if (!param.getEntityImportCondition().isErrorSkip()) {
					logError(rs("Common.errorMsg", ""));
					return false;
				}
				logInfo(rs("EntityImport.continueLog"));
			} else {
				if (entityResult.getMessages() != null) {
					for (String message : entityResult.getMessages()) {
						logInfo(message);
					}
					logInfo("");
				}

				//途中段階で一度メッセージ出力
				logMessage = rs("EntityImport.completedImportEntityDataLog", entityPath,
						entityResult.getInsertCount(), entityResult.getUpdateCount(), entityResult.getErrorCount());
				logInfo(logMessage);
				logInfo("");
				messageSummary.add(logMessage);
			}

			logMessage = rs("EntityImport.completedImportEntityLog");
			logInfo(logMessage);
			logInfo("");
			messageSummary.add(logMessage);

			return true;
		}
	}

	/**
	 * Wizard形式でImport用のパラメータを生成して、Import処理を実行します。
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

		EntityImportParameter param = new EntityImportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			//Entity名
			boolean validEntity = false;
			do {
				String inputEntityName = entityName;
				entityName = null;
				if (StringUtil.isEmpty(inputEntityName)) {
					inputEntityName = readConsole(rs("EntityImport.Wizard.inputEntityNameMsg"));
				}
				if (StringUtil.isNotBlank(inputEntityName)) {
					param.setEntityName(inputEntityName);

					//存在チェック
					EntityDefinition ed = edm.get(inputEntityName);
					if (ed == null) {
						logWarn(rs("EntityImport.notExistsEntityMsg", inputEntityName));
						continue;
					}
					validEntity = true;
				} else {
					logWarn(rs("EntityImport.Wizard.requiredEntityNameMsg"));
				}
			} while(validEntity == false);

			//Importファイル
			boolean validFile = false;
			do {
				String inputFileName = importFileName;
				importFileName = null;
				if (StringUtil.isEmpty(inputFileName)) {
					inputFileName = readConsole(rs("EntityImport.Wizard.inputImportFileMsg"));
				}
				if (StringUtil.isNotBlank(inputFileName)) {
					param.setImportFilePath(inputFileName);

					//存在チェック
					File file = new File(inputFileName);
					if (!file.exists()) {
						logWarn(rs("EntityImport.notExistsImportFileMsg", inputFileName));
						continue;
					}
					param.setImportFile(file);

					validFile = true;
				} else {
					logWarn(rs("EntityImport.Wizard.requiredImportFilePathMsg"));
				}

			} while(validFile == false);
			
			///BinaryデータをImportするか
			boolean isImportBinaryData = readConsoleBoolean(rs("EntityImport.Wizard.confirmImportBinaryDataMsg"), param.isImportBinaryData());
			param.setImportBinaryData(isImportBinaryData);

			//オプションをコンフィグから取得
			EntityDataImportCondition condition = loadConfigCondition();
			if (condition == null) {
				condition = new EntityDataImportCondition();
			}

			//Truncate
			boolean isTruncate = readConsoleBoolean(rs("PackageImport.Wizard.confirmTrancateDataMsg"), condition.isTruncate());
			condition.setTruncate(isTruncate);

			//bulkUpdate
			boolean isBulkUpdate = readConsoleBoolean(rs("PackageImport.Wizard.confirmBulkUpdateMsg"), condition.isBulkUpdate());
			condition.setBulkUpdate(isBulkUpdate);

			//ForceUpdate
			if (isBulkUpdate) {
				condition.setFourceUpdate(false);
			} else {
				boolean isForceUpdate = readConsoleBoolean(rs("PackageImport.Wizard.confirmForceUpdateMsg"), condition.isFourceUpdate());
				condition.setFourceUpdate(isForceUpdate);
			}

			//Errorスキップ
			if (isBulkUpdate) {
				condition.setErrorSkip(false);
			} else {
				boolean isErrorSkip = readConsoleBoolean(rs("PackageImport.Wizard.confirmSkipErrorDataMsg"), condition.isErrorSkip());
				condition.setErrorSkip(isErrorSkip);
			}

			//存在しないプロパティは無視
			boolean isIgnoreNotExistsProperty = readConsoleBoolean(rs("PackageImport.Wizard.confirmIgnoreNotExistsPropertyMsg"), condition.isIgnoreNotExistsProperty());
			condition.setIgnoreNotExistsProperty(isIgnoreNotExistsProperty);

			//Listner実行
			if (isBulkUpdate) {
				condition.setNotifyListeners(false);
			} else {
				boolean isNotifyListner = readConsoleBoolean(rs("PackageImport.Wizard.confirmNotifyListenerMsg"), condition.isNotifyListeners());
				condition.setNotifyListeners(isNotifyListner);
			}

			//更新不可項目の更新
			boolean isUpdateDisupdatableProperty = readConsoleBoolean(rs("PackageImport.Wizard.confirmUpdateDisupdatablePropertyMsg"), condition.isUpdateDisupdatableProperty());
			condition.setUpdateDisupdatableProperty(isUpdateDisupdatableProperty);

			//追加時に監査プロパティを指定した値で登録
			boolean isInsertEnableAuditPropertySpecification = readConsoleBoolean(rs("PackageImport.Wizard.confirmInsertEnableAuditPropertySpecification"), condition.isInsertEnableAuditPropertySpecification());
			condition.setInsertEnableAuditPropertySpecification(isInsertEnableAuditPropertySpecification);

			if (condition.isInsertEnableAuditPropertySpecification()) {
				//ユーザー情報の入力
				do {
					String executeUserId = readConsole(rs("PackageImport.Wizard.confirmExecuteUserId"));
					if (StringUtil.isNotEmpty(executeUserId)) {
						userId = executeUserId;

						String executeUserPW = readConsole(rs("PackageImport.Wizard.confirmExecuteUserPW"));
						if (StringUtil.isNotEmpty(executeUserPW)) {
							password = executeUserPW;
						}
					}
				} while(userId == null);
			}

			//Validationの実行
			if (isBulkUpdate || isUpdateDisupdatableProperty) {
				condition.setWithValidation(false);
			} else {
				boolean isWithValidation = readConsoleBoolean(rs("PackageImport.Wizard.confirmWithValidationMsg"), condition.isWithValidation());
				condition.setWithValidation(isWithValidation);
			}

			//Commit単位
			int commitLimit = readConsoleInteger(rs("PackageImport.Wizard.inputCommitUnitMsg"), 100);
			condition.setCommitLimit(commitLimit);

			//OID Prefix
			boolean validOidPrefix = false;
			do {
				String oidPrefix = readConsole(rs("PackageImport.Wizard.inputOIDPrefixMsg") + "(" + condition.getPrefixOid() + ")");
				if (StringUtil.isEmpty(oidPrefix)) {
					validOidPrefix = true;
				} else {
					//英数字チェック
					if (oidPrefix.matches("[0-9a-zA-Z]+")) {
						//OK
						condition.setPrefixOid(oidPrefix);
						validOidPrefix = true;
					} else {
						logWarn(rs("PackageImport.warnOIDPrefixMsg"));
					}
				}
			} while(validOidPrefix == false);

			//Locale
			String locale = readConsole(rs("PackageImport.Wizard.inputLocaleMsg")
					+ "(" + (condition.getLocale() != null ? condition.getLocale() : "") + ")");
			if (StringUtil.isNotBlank(locale)) {
				condition.setLocale(locale);
			}
			//TimeZone
			String timezone = readConsole(rs("PackageImport.Wizard.inputTimezoneMsg")
					+ "(" + (condition.getTimezone() != null ? condition.getTimezone() : "") + ")");
			if (StringUtil.isNotBlank(timezone)) {
				condition.setTimezone(timezone);
			}

			//条件をセット
			param.setEntityImportCondition(condition);

			boolean validExecute = false;
			do {
				//実行情報出力
				logArguments(param);

				boolean isExecute = readConsoleBoolean(rs("EntityImport.Wizard.confirmImportEntityMsg"), false);
				if (isExecute) {
					validExecute = true;
				} else {
					//defaultがfalseなので念のため再度確認
					isExecute = readConsoleBoolean(rs("EntityImport.Wizard.confirmRetryMsg"), true);

					if (isExecute) {
						//再度実行
						return wizard();
					}
				}
			} while(validExecute == false);

			//Consoleを削除してLogに切り替え
			switchLog(false, true);
	
			//Import処理実行
			return executeTask(param, (paramA) -> {
				return importEntity(paramA);
			});
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
				try (InputStream is = EntityImport.class.getResourceAsStream(configFileName)) {
					if (is == null) {
						logWarn(rs("EntityImport.notExistsConfigFileMsg", configFileName));
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
			String propTenantId = prop.getProperty(PROP_TENANT_ID);
			if (StringUtil.isNotEmpty(propTenantId)) {
				tenantId = Integer.parseInt(propTenantId);
			}
			if (tenantId == null) {
				String propTenantUrl = prop.getProperty(PROP_TENANT_URL);
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
			String propEntityName = prop.getProperty(PROP_ENTITY_NAME);
			if (StringUtil.isNotEmpty(propEntityName)) {
				entityName = propEntityName;
			}
		}

		//ファイル名
		if (importFileName == null) {
			String propImportFileName = prop.getProperty(PROP_IMPORT_FILE);
			if (StringUtil.isNotEmpty(propImportFileName)) {
				importFileName = propImportFileName;
			}
		}
		
		//BinaryデータをImportするか
		String propImportBinaryData = prop.getProperty(EntityImportParameter.PROP_IMPORT_BINARY_DATA);
		if (StringUtil.isNotEmpty(propImportBinaryData)) {
			isImportBinaryData = Boolean.valueOf(propImportBinaryData);
		}
	}

	/**
	 * コンフィグファイルからImport条件を取得します。
	 *
	 * @return Import条件(コンフィグファイルが無い場合はnull)
	 */
	private EntityDataImportCondition loadConfigCondition() {

		//プロパティの取得
		Properties prop = loadConfig();
		if (prop == null) {
			return null;
		}

		EntityDataImportCondition condition = new EntityDataImportCondition();

		//Truncate
		String truncate = prop.getProperty(PROP_ENTITY_TRUNCATE);
		if (StringUtil.isNotEmpty(truncate)) {
			condition.setTruncate(Boolean.valueOf(truncate));
		}

		//bulkUpdate
		String bulkUpdate = prop.getProperty(PROP_ENTITY_BULK_UPDATE);
		if (StringUtil.isNotEmpty(bulkUpdate)) {
			condition.setBulkUpdate(Boolean.valueOf(bulkUpdate));
		}

		//ForceUpdate
		if (condition.isBulkUpdate()) {
			condition.setFourceUpdate(false);
		} else {
			String forceUpdate = prop.getProperty(PROP_ENTITY_FORCE_UPDATE);
			if (StringUtil.isNotEmpty(forceUpdate)) {
				condition.setFourceUpdate(Boolean.valueOf(forceUpdate));
			}
		}

		//Errorスキップ
		if (condition.isBulkUpdate()) {
			condition.setErrorSkip(false);
		} else {
			String errorSkip = prop.getProperty(PROP_ENTITY_ERROR_SKIP);
			if (StringUtil.isNotEmpty(errorSkip)) {
				condition.setErrorSkip(Boolean.valueOf(errorSkip));
			}
		}

		//存在しないプロパティは無視
		String ignoreInvalidProperty = prop.getProperty(PROP_ENTITY_IGNORE_INVALID_PROPERTY);
		if (StringUtil.isNotEmpty(ignoreInvalidProperty)) {
			condition.setIgnoreNotExistsProperty(Boolean.valueOf(ignoreInvalidProperty));
		}

		//Listner実行
		if (condition.isBulkUpdate()) {
			condition.setNotifyListeners(false);
		} else {
			String notifyListener = prop.getProperty(PROP_ENTITY_NOTIFY_LISTENER);
			if (StringUtil.isNotEmpty(notifyListener)) {
				condition.setNotifyListeners(Boolean.valueOf(notifyListener));
			}
		}

		//更新不可項目の更新
		String updateDisupdatableProperty = prop.getProperty(PROP_ENTITY_UPDATE_DISUPDATABLE);
		if (StringUtil.isNotEmpty(updateDisupdatableProperty)) {
			condition.setUpdateDisupdatableProperty(Boolean.valueOf(updateDisupdatableProperty));
		}

		//InsertするEntityにAuditPropertyの値を指定
		String insertEnableAuditPropertySpecification = prop.getProperty(PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION);
		if (StringUtil.isNotEmpty(insertEnableAuditPropertySpecification)) {
			condition.setInsertEnableAuditPropertySpecification(Boolean.valueOf(insertEnableAuditPropertySpecification));
		}
		if (condition.isInsertEnableAuditPropertySpecification()) {
			//実行ユーザーID、PWを取得
			String execUserId = prop.getProperty(PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION_EXEC_USER_ID);
			if (StringUtil.isNotEmpty(execUserId)) {
				userId = execUserId;
			}
			String execUserPW = prop.getProperty(PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION_EXEC_USER_PW);
			if (StringUtil.isNotEmpty(execUserPW)) {
				password = execUserPW;
			}
		}

		//Validationの実行
		if (condition.isBulkUpdate() || condition.isUpdateDisupdatableProperty()) {
			condition.setWithValidation(false);
		} else {
			String withValidation = prop.getProperty(PROP_ENTITY_WITH_VALIDATION);
			if (StringUtil.isNotEmpty(withValidation)) {
				condition.setWithValidation(Boolean.valueOf(withValidation));
			}
		}

		//Commit単位
		String commitLimit = prop.getProperty(PROP_ENTITY_COMMIT_LIMIT);
		if (StringUtil.isNotEmpty(commitLimit)) {
			condition.setCommitLimit(Integer.valueOf(commitLimit));
		}

		//OID Prefix
		String oidPrefix = prop.getProperty(PROP_ENTITY_PREFIX_OID);
		if (StringUtil.isNotEmpty(oidPrefix)) {
			//英数字チェック
			if (oidPrefix.matches("[0-9a-zA-Z]+")) {
				//OK
				condition.setPrefixOid(oidPrefix);
			} else {
				logError(rs("PackageImport.warnOIDPrefixMsg"));
				return null;
			}
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
						logError(rs("EntityImport.notExistsEntityMsg", entityName));
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
		if (StringUtil.isNotEmpty(importFileName)) {
			File importFile = new File(importFileName);
			if (!importFile.exists()) {
				logError(rs("EntityImport.notExistsImportFileMsg", importFileName));
				return false;
			}
			this.importFile = importFile;
		}
		return true;
	}


	/**
	 * バッチ引数またはコンフィグファイル形式でImport用のパラメータを生成して、Import処理を実行します。
	 *
	 * @return false:エラー
	 */
	private boolean silent() {

		if (tenant == null) {
			logError(rs("Common.requiredMsg", PROP_TENANT_ID + " or " + PROP_TENANT_URL));
			return false;
		}
		logInfo("target tenant:[" + tenant.getId() + "]" + tenant.getName());

		EntityImportParameter param = new EntityImportParameter(tenant.getId(), tenant.getName());

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
			if (importFile == null) {
				logError(rs("Common.requiredMsg", "Import file"));
				return false;
			}
			param.setImportFilePath(importFile.getPath());
			param.setImportFile(importFile);
			
			param.setImportBinaryData(isImportBinaryData);

			//オプションをコンフィグから取得
			EntityDataImportCondition condition = loadConfigCondition();
			if (condition == null) {
				condition = new EntityDataImportCondition();
			}
			param.setEntityImportCondition(condition);

			//実行情報出力
			logArguments(param);

			//Import処理実行
			return executeTask(param, (paramA) -> {
				return importEntity(paramA);
			});
		});

	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
