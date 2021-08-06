/*
 * Copyright 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.pack;

import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_BULK_UPDATE;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_COMMIT_LIMIT;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_ERROR_SKIP;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_FORCE_UPDATE;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_IGNORE_INVALID_PROPERTY;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION_EXEC_USER_ID;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION_EXEC_USER_PW;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_NOTIFY_LISTENER;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_PREFIX_OID;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_TRUNCATE;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_UPDATE_DISUPDATABLE;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_ENTITY_WITH_VALIDATION;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_IMPORT_FILE;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_TENANT_ID;
import static org.iplass.mtp.tools.batch.pack.PackageImportParameter.PROP_TENANT_URL;

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

import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authenticate.internal.InternalCredential;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportCondition;
import org.iplass.mtp.impl.tools.entityport.EntityDataImportResult;
import org.iplass.mtp.impl.tools.metaport.MetaDataImportResult;
import org.iplass.mtp.impl.tools.pack.PackageEntity;
import org.iplass.mtp.impl.tools.pack.PackageInfo;
import org.iplass.mtp.impl.tools.pack.PackageRuntimeException;
import org.iplass.mtp.impl.tools.pack.PackageService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.StringUtil;


/**
 * Package Import Batch
 */
public class PackageImport extends MtpCuiBase {

	/** Silentモード 設定ファイル名 */
	public static final String KEY_CONFIG_FILE = "pack.config";

	//実行モード
	private ExecMode execMode = ExecMode.WIZARD;

	//テナントID(引数)
	private Integer tenantId;

	//インポートファイル(引数)
	private String importFile;

	/** ユーザID(InsertするEntityにAuditプロパティの値を指定する場合、必須) */
	private String userId;

	/** パスワード */
	private String password;

	private TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);
	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private PackageService ps = ServiceRegistry.getRegistry().getService(PackageService.class);
	private AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・import file
	 **/
	public static void main(String[] args) {

		PackageImport instance = null;
		try {
			instance = new PackageImport(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・import file
	 **/
	public PackageImport(String... args) {

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
					importFile = args[2];
				}
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

		try {
			switch (execMode) {
			case WIZARD :
				LogListner consoleLogListner = getConsoleLogListner();
				addLogListner(consoleLogListner);

				//環境情報出力
				logEnvironment();

				logInfo("■Start Import Wizard");
				logInfo("");

				//Wizardの実行
				return wizard();
			case SILENT :
				LogListner loggingLogListner = getLoggingLogListner();
				addLogListner(loggingLogListner);

				//環境情報出力
				logEnvironment();

				logInfo("■Start Import Silent");
				logInfo("");

				//Silentの実行
				return silent();
			default :
				logError("unsupport execute mode : " + execMode);
				return false;
			}

		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
			logInfo("");
		}
	}

	public PackageImport execMode(ExecMode execMode) {
		this.execMode = execMode;
		return this;
	}

	public PackageImport tenantId(Integer tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public PackageImport importFile(String importFile) {
		this.importFile = importFile;
		return this;
	}

	/**
	 * Importします。
	 *
	 * @param param Import情報
	 * @return
	 */
	public boolean importPack(final PackageImportParameter param) {

		setSuccess(false);

		try {
			boolean isSuccess = Transaction.required(new Function<Transaction, Boolean>() {

				String oid = null;

				@Override
				public Boolean apply(Transaction t) {

					final List<String> messageSummary = new ArrayList<>();

					boolean ret = Transaction.requiresNew(tt->{
						//Fileのアップロード
						oid = executeTask(param, new UploadTask(param, messageSummary));

						//メタデータの登録
						return executeTask(param, new MetaDataImportTask(param, messageSummary, oid));
					});

					if (!ret) {
						return false;
					}

					//Entityデータのインポートはトランザクションを別にして、TenantContextを再取得して実行
					//インポートのメタデータにUtilityClassが含まれるとTenantContextがreloadされてメタデータが取得できなくなるため
					ret = Transaction.requiresNew(tt->{
						//Entityデータの登録
						return executeTask(param, new EntityDataImportTask(param, messageSummary, oid));
					});

					logInfo("-----------------------------------------------------------");
					logInfo("■Execute Result Summary");
					for(String message : messageSummary) {
						logInfo(message);
					}
					logInfo("-----------------------------------------------------------");
					logInfo("");

					logInfo(rs("PackageImport.completedImportPackageLog", param.getImportFilePath()));

					return true;
				}
			});

			setSuccess(isSuccess);

		} catch (Throwable e) {
			logError(rs("Common.errorMsg", e.getMessage()));
		}

		return isSuccess();
	}

	/**
	 * PackageImport情報を出力します。
	 */
	public void logArguments(PackageImportParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\timport file :" + param.getImportFilePath());
		logInfo("\timport file locale :" + param.getLocale());
		logInfo("\timport file timezone :" + param.getTimezone());

		PackageInfo packInfo = param.getPackInfo();

		if (CollectionUtil.isNotEmpty(packInfo.getMetaDataPaths())) {
			logInfo("\tmetadata count :" + packInfo.getMetaDataPaths().size());
		} else {
			logInfo("\tmetadata count :" + "0");
		}

		if (CollectionUtil.isNotEmpty(packInfo.getEntityPaths())) {
			logInfo("\tentity count :" + packInfo.getEntityPaths().size());

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
		} else {
			logInfo("\tentity count :" + "0");
		}

		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	/**
	 * タスクを実行します。
	 */
	private <T> T executeTask(PackageImportParameter param, Supplier<T> task) {

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
	 * Packageをアップロード(登録)します。
	 */
	private class UploadTask implements Supplier<String> {

		private final PackageImportParameter param;
		private final List<String> messageSummary;

		public UploadTask(final PackageImportParameter param, final List<String> messageSummary) {
			this.param = param;
			this.messageSummary = messageSummary;
		}

		@Override
		public String get() {
			//Fileのアップロード
			String oid = ps.uploadPackage(
					param.getPackageName(), null, param.getImportFile(), PackageEntity.TYPE_OFFLINE);

			String logMessage = rs("PackageImport.createdPackageInfoLog", oid);
			logInfo(logMessage);
			logInfo("");

			messageSummary.add(logMessage);

			return oid;
		}
	}

	/**
	 * メタデータをインポートします。
	 */
	private class MetaDataImportTask implements Supplier<Boolean> {

		private final PackageImportParameter param;
		private final List<String> messageSummary;
		private final String oid;

		public MetaDataImportTask(final PackageImportParameter param, final List<String> messageSummary, String oid) {
			this.param = param;
			this.messageSummary = messageSummary;
			this.oid = oid;
		}

		@Override
		public Boolean get() {

			//メタデータの登録
			if (CollectionUtil.isNotEmpty(param.getPackInfo().getMetaDataPaths())) {
				logInfo(rs("PackageImport.startImportMetaLog"));

				MetaDataImportResult metaResult
					= ps.importPackageMetaData(oid, param.getImportTenant());

				if (metaResult.isError()) {
					if (metaResult.getMessages() != null) {
						for (String message : metaResult.getMessages()) {
							logError(message);
						}
						logInfo("");
					}

					logError(rs("Common.errorMsg", ""));
					return false;
				}

				if (metaResult.getMessages() != null) {
					for (String message : metaResult.getMessages()) {
						logInfo(message);
						messageSummary.add(message);
					}
					logInfo("");
				}

				//途中段階で一度メッセージ出力
				String logMessage = rs("PackageImport.completedImportMetaLog");
				logInfo(logMessage);
				logInfo("");

				messageSummary.add(logMessage);

			} else {
				//途中段階で一度メッセージ出力
				String logMessage = rs("PackageImport.notIncludeMetaLog");
				logInfo(logMessage);
				logInfo("");

				messageSummary.add(logMessage);
			}
			return true;
		}
	}

	/**
	 * Entityデータをインポートします。
	 */
	private class EntityDataImportTask implements Supplier<Boolean> {

		private final PackageImportParameter param;
		private final List<String> messageSummary;
		private final String oid;

		public EntityDataImportTask(final PackageImportParameter param, final List<String> messageSummary, String oid) {
			this.param = param;
			this.messageSummary = messageSummary;
			this.oid = oid;
		}

		@Override
		public Boolean get() {

			String logMessage = null;	//work用

			//Entityデータの登録
			if (CollectionUtil.isNotEmpty(param.getPackInfo().getEntityPaths())) {
				logInfo(rs("PackageImport.startImportEntityLog"));

				for (String path : param.getPackInfo().getEntityPaths()) {
					//.csvを除く
					String entityPath =  EntityService.ENTITY_META_PATH + path.substring(0, path.length() - 4).replace(".", "/");
					logInfo(rs("PackageImport.startImportEntityDataLog", entityPath));


					EntityDataImportResult entityResult
							= ps.importPackageEntityData(oid, entityPath, param.getEntityImportCondition());

					if (entityResult.isError()) {
						if (entityResult.getMessages() != null) {
							for (String message : entityResult.getMessages()) {
								logError(message);
							}
							logInfo("");
						}

						logMessage = rs("PackageImport.errorImportEntityDataLog", entityPath);
						logError(logMessage);
						messageSummary.add("[ERROR]" + logMessage);

						//エラースキップしない場合は、ここで終了
						if (!param.getEntityImportCondition().isErrorSkip()) {
							logError(rs("Common.errorMsg", ""));
							return false;
						}
						logInfo(rs("PackageImport.continueLog"));
					} else {
						if (entityResult.getMessages() != null) {
							for (String message : entityResult.getMessages()) {
								logInfo(message);
							}
							logInfo("");
						}

						//途中段階で一度メッセージ出力
						logMessage = rs("PackageImport.completedImportEntityDataLog", entityPath,
								entityResult.getInsertCount(), entityResult.getUpdateCount(), entityResult.getErrorCount());
						logInfo(logMessage);
						logInfo("");
						messageSummary.add(logMessage);
					}
				}

				logMessage = rs("PackageImport.completedImportEntityLog");
				logInfo(logMessage);
				logInfo("");
				messageSummary.add(logMessage);

			} else {
				logMessage = rs("PackageImport.notIncludeEntityLog");
				logInfo(logMessage);
				logInfo("");

				messageSummary.add(logMessage);
			}

			return true;
		}
	}

	/**
	 * Import対象のメタデータパスを出力します。
	 * @param param
	 */
	private void showMetaDataPathList(PackageInfo packInfo) {

		logInfo("-----------------------------------------------------------");
		logInfo("■MetaData List");
		for (String path : packInfo.getMetaDataPaths()) {
			logInfo(path);
		}
		logInfo("-----------------------------------------------------------");
	}

	/**
	 * Import対象のEntityデータパスを出力します。
	 * @param param
	 */
	private void showEntityDataPathList(PackageInfo packInfo) {

		logInfo("-----------------------------------------------------------");
		logInfo("■EntityData List");
		for (String path : packInfo.getEntityPaths()) {
			logInfo(path);
		}
		logInfo("-----------------------------------------------------------");
	}

	/**
	 * Wizard形式でImport用のパラメータを生成して、Import処理を実行します。
	 *
	 * @return 実行結果
	 */
	private boolean wizard() {

		Tenant tenant = null;
		if (tenantId != null) {
			//引数でテナントIDが指定されている場合
			tenant = ts.getTenant(tenantId);
			if (tenant == null) {
				logWarn(rs("Common.notExistsTenantIdMsg", tenantId));
				tenantId = null;
				return wizard();
			}
		} else {
			//テナントURL
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

		PackageImportParameter param = new PackageImportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			//Importファイル
			PackageInfo packInfo = null;
			boolean validFile = false;
			do {
				String importFileName = importFile;
				importFile = null;
				if (StringUtil.isEmpty(importFileName)) {
					importFileName = readConsole(rs("PackageImport.Wizard.inputImportFileMsg"));
				}
				if (StringUtil.isNotBlank(importFileName)) {
					param.setImportFilePath(importFileName);

					//存在チェック
					File file = new File(param.getImportFilePath());
					if (!file.exists()) {
						logWarn(rs("PackageImport.notExistsImportFileMsg"));
						continue;
					}

					try {
						//データチェック
						packInfo = ps.getPackageInfo(file);

						//対象メタデータチェック
						int metaCount = (CollectionUtil.isNotEmpty(packInfo.getMetaDataPaths()) ? packInfo.getMetaDataPaths().size() : 0);
						if (metaCount == 0) {
							logInfo(rs("PackageImport.Wizard.notIncludeMetaMsg"));
						} else {
							//警告テナントの存在チェック
							if (packInfo.isWarningTenant()) {
								//警告がある場合は、Packageに含まれるテナントは取り込まない
								logWarn(rs("PackageImport.includeWarnTenantMetaMsg"));
							} else {
								//警告がない場合は、Packageに含まれるテナントをセット
								param.setImportTenant(packInfo.getTenant());
							}

							boolean isShow = readConsoleBoolean(rs("PackageImport.Wizard.confirmShowMetaListMsg", metaCount), false);
							if (isShow) {
								showMetaDataPathList(packInfo);
							}
						}
						boolean isContinue = readConsoleBoolean(rs("Common.continueMsg"), true);
						if (!isContinue) {
							continue;
						}

						//対象Entityデータチェック
						int entityCount = (CollectionUtil.isNotEmpty(packInfo.getEntityPaths()) ? packInfo.getEntityPaths().size() : 0);
						if (entityCount == 0) {
							logInfo(rs("PackageImport.Wizard.notIncludeEntityMsg"));
						} else {
							boolean isShow = readConsoleBoolean(rs("PackageImport.Wizard.confirmShowEntityListMsg", entityCount), false);
							if (isShow) {
								showEntityDataPathList(packInfo);
							}
						}
						isContinue = readConsoleBoolean(rs("Common.continueMsg"), true);
						if (!isContinue) {
							continue;
						}

						param.setImportFile(file);
						param.setPackInfo(packInfo);

						validFile = true;

					} catch (PackageRuntimeException e) {
						logWarn(rs("PackageImport.errorAnalysisFileMsg", e.getMessage()));
					}

				} else {
					logWarn(rs("PackageImport.Wizard.requiredImportFilePathMsg"));
				}

			} while(validFile == false);

			//Entityが含まれる場合は、Import条件を作成
			if (CollectionUtil.isNotEmpty(packInfo.getEntityPaths())) {
				EntityDataImportCondition condition = new EntityDataImportCondition();

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
					//ユーザ情報の入力
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

			} else {
				param.setEntityImportCondition(null);
			}

			boolean validExecute = false;
			do {
				//実行情報出力
				logArguments(param);

				boolean isExecute = readConsoleBoolean(rs("PackageImport.Wizard.confirmImportPackageMsg"), false);
				if (isExecute) {
					validExecute = true;
				} else {
					//defaultがfalseなので念のため再度確認
					isExecute = readConsoleBoolean(rs("PackageImport.Wizard.confirmRetryMsg"), true);

					if (isExecute) {
						//再度実行
						return wizard();
					}
				}
			} while(validExecute == false);

			return null;
		});

		//ConsoleのLogListnerを一度削除してLog出力に切り替え
		LogListner consoleLogListner = getConsoleLogListner();
		removeLogListner(consoleLogListner);
		LogListner loggingListner = getLoggingLogListner();
		addLogListner(loggingListner);

		//Import処理実行
		try {
			importPack(param);

			return isSuccess();

		} finally {
			removeLogListner(loggingListner);
			addLogListner(consoleLogListner);
		}
	}

	/**
	 * Propertyファイル形式でImport用のパラメータを生成して、Import処理を実行します。
	 *
	 * @return 実行結果
	 */
	private boolean silent() {

		//プロパティファイルの取得
		String configFileName = System.getProperty(KEY_CONFIG_FILE);
		if (StringUtil.isEmpty(configFileName)) {
			logError(rs("PackageImport.Silent.requiredConfigFileMsg", KEY_CONFIG_FILE));
			return false;
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
				try (InputStream is = PackageExport.class.getResourceAsStream(configFileName)) {
					if (is == null) {
						logError(rs("PackageImport.Silent.notExistsConfigFileMsg", configFileName));
						return false;
					}
					try (InputStreamReader reader = new InputStreamReader(is, "UTF-8")) {
						prop.load(reader);
					}
				}
			}
		} catch (IOException e) {
			throw new SystemException(e);
		}

		Tenant tenant = null;
		if (tenantId != null) {
			//引数でテナントIDが指定されている場合
			tenant = ts.getTenant(tenantId);
			if (tenant == null) {
				logError(rs("Common.notExistsTenantIdMsg", tenantId));
				return false;
			}
		} else {
			//プロパティから取得
			String propTenantId = prop.getProperty(PROP_TENANT_ID);
			if (StringUtil.isNotEmpty(propTenantId)) {
				tenant = ts.getTenant(Integer.parseInt(propTenantId));
				if (tenant == null) {
					logError(rs("Common.notExistsTenantIdMsg", propTenantId));
					return false;
				}
			}
			if (tenant == null) {
				String propTenantUrl = prop.getProperty(PROP_TENANT_URL);
				if (StringUtil.isNotEmpty(propTenantUrl)) {
					if (!propTenantUrl.startsWith("/")) {
						propTenantUrl = "/" + propTenantUrl;
					}
					tenant = ts.getTenant(propTenantUrl);
					if (tenant == null) {
						logError(rs("Common.notExistsTenantUrlMsg", propTenantUrl));
						return false;
					}
				}
			}
			if (tenant == null) {
				logError(rs("Common.requiredMsg", PROP_TENANT_ID + " or " + PROP_TENANT_URL));
				return false;
			}
		}
		logInfo("target tenant:[" + tenant.getId() + "]" + tenant.getName());

		PackageImportParameter param = new PackageImportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			String importFilePath = importFile;
			if (StringUtil.isEmpty(importFilePath)) {
				importFilePath = prop.getProperty(PROP_IMPORT_FILE);
			}
			if (StringUtil.isEmpty(importFilePath)) {
				logError(rs("Common.requiredMsg", PROP_IMPORT_FILE));
				return false;
			}

			//存在チェック
			File file = new File(importFilePath);
			if (!file.exists()) {
				logError(rs("PackageImport.notExistsImportFileMsg"));
				return false;
			}
			param.setImportFilePath(importFilePath);

			//データチェック
			try {
				PackageInfo packInfo = ps.getPackageInfo(file);

				//警告テナントの存在チェック
				if (packInfo.isWarningTenant()) {
					//警告がある場合は、Packageに含まれるテナントは取り込まない
					logWarn(rs("PackageImport.includeWarnTenantMetaMsg"));
				} else {
					//警告がない場合は、Packageに含まれるテナントをセット
					param.setImportTenant(packInfo.getTenant());
				}

				param.setImportFile(file);
				param.setPackInfo(packInfo);

			} catch (PackageRuntimeException e) {
				logError(rs("PackageImport.errorAnalysisFileMsg", e.getMessage()));
				return false;
			}

			//Entityが含まれる場合は、Import条件を作成
			if (CollectionUtil.isNotEmpty(param.getPackInfo().getEntityPaths())) {

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
					//実行ユーザID、PWを取得
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
						return false;
					}
				}

				param.setEntityImportCondition(condition);
			}

			//実行情報出力
			logArguments(param);

			//Import処理実行
			return importPack(param);
		});

	}

}
