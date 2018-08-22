/*
 * Copyright 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.pack;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
import org.iplass.mtp.tools.ToolsBatchResourceBundleUtil;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.StringUtil;


/**
 * Package Import Batch
 */
public class PackageImport extends MtpCuiBase {

	//実行モード
	private ExecMode execMode = ExecMode.WIZARD;

	private TenantService tenantService = ServiceRegistry.getRegistry().getService(TenantService.class);
	private TenantContextService tenantContextService = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private PackageService packageService = ServiceRegistry.getRegistry().getService(PackageService.class);

	/**
	 * args[0]・・・execMode
	 * args[1]・・・language
	 **/
	public static void main(String[] args) {

		PackageImport instance = null;
		try {
			instance = new PackageImport(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			ExecuteContext.finContext();
		}
	}

	/**
	 * args[0]・・・execMode
	 * args[1]・・・language
	 **/
	public PackageImport(String... args) {

		if (args != null) {
			if (args.length > 0) {
				setExecMode(ExecMode.valueOf(args[0]));
			}
			if (args.length > 1) {
				//systemの場合は、JVMのデフォルトを利用
				if (!"system".equals(args[1])) {
					setLanguage(args[1]);
				}
			}
		}

		setupLanguage();
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
		case WIZARD :
			logInfo("■Start Import Wizard");
			logInfo("");

			//Wizardの実行
			return startImportWizard();
		case SILENT :
			//TODO Silent版
			logInfo("■Start Import Silent");
			logInfo("");

			return false;
		default :
			logError("unsupport execute mode : " + getExecMode());
			return false;
		}

	}

	public ExecMode getExecMode() {
		return execMode;
	}

	public void setExecMode(ExecMode execMode) {
		this.execMode = execMode;
	}

	/**
	 * Importします。
	 *
	 * @param param Import情報
	 * @return
	 */
	public boolean executeImport(final PackageImportParameter param) {

		setSuccess(false);

		try {
			boolean isSuccess = Transaction.required(new Function<Transaction, Boolean>() {

				String oid = null;

				@Override
				public Boolean apply(Transaction t) {

					final List<String> messageSummary = new ArrayList<>();

					boolean ret = Transaction.requiresNew(tt->{

						TenantContext tc  = tenantContextService.getTenantContext(param.getTenantId());

						return ExecuteContext.executeAs(tc, ()->{
							ExecuteContext.getCurrentContext().setLanguage(getLanguage());

							String logMessage = null;	//work用

							//Fileのアップロード
							oid = packageService.uploadPackage(
									param.getPackageName(), null, param.getImportFile(), PackageEntity.TYPE_OFFLINE);

							logMessage = rs("PackageImport.createdPackageInfoLog", oid);
							logInfo(logMessage);
							logInfo("");

							messageSummary.add(logMessage);

							//メタデータの登録
							if (CollectionUtil.isNotEmpty(param.getPackInfo().getMetaDataPaths())) {
								logInfo(rs("PackageImport.startImportMetaLog"));

								MetaDataImportResult metaResult
									= packageService.importPackageMetaData(oid, param.getWorkDir(), param.getImportTenant());

								if (metaResult.isError()) {
									if (metaResult.getMessages() != null) {
										for (String message : metaResult.getMessages()) {
											logError(message);
										}
										logInfo("");
									}

									logError(getCommonResourceMessage("errorMsg", ""));
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
								logMessage = rs("PackageImport.completedImportMetaLog");
								logInfo(logMessage);
								logInfo("");

								messageSummary.add(logMessage);
							} else {
								//途中段階で一度メッセージ出力
								logMessage = rs("PackageImport.notIncludeMetaLog");
								logInfo(logMessage);
								logInfo("");

								messageSummary.add(logMessage);
							}
							return true;
						});
					});

					if (!ret) {
						return false;
					}

					//Entityデータのインポートはトランザクションを別にして、TenantContextを再取得して実行
					//インポートのメタデータにUtilityClassが含まれるとTenantContextがreloadされてメタデータが取得できなくなるため
					ret = Transaction.requiresNew(tt->{

						TenantContext reloadTc = tenantContextService.getTenantContext(param.getTenantId());
						return ExecuteContext.executeAs(reloadTc, ()->{
							ExecuteContext.getCurrentContext().setLanguage(getLanguage());

							String logMessage = null;	//work用

							//Entityデータの登録
							if (CollectionUtil.isNotEmpty(param.getPackInfo().getEntityPaths())) {
								logInfo(rs("PackageImport.startImportEntityLog"));

								for (String path : param.getPackInfo().getEntityPaths()) {
									//.csvを除く
									String entityPath =  EntityService.ENTITY_META_PATH + path.substring(0, path.length() - 4).replace(".", "/");
									logInfo(rs("PackageImport.startImportEntityDataLog", entityPath));


									EntityDataImportResult entityResult
											= packageService.importPackageEntityData(oid, entityPath, param.getEntityImportCondition(), param.getWorkDir());

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
											logError(getCommonResourceMessage("errorMsg", ""));
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
						});
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
			logError(getCommonResourceMessage("errorMsg", e.getMessage()));
			e.printStackTrace();
		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
			logInfo("");
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
		logInfo("\twork dir :" + param.getWorkDirName());

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
			logInfo("\tentity execute validation :" + condition.isWithValidation());
			logInfo("\tentity commit limit :" + condition.getCommitLimit());
			logInfo("\tentity oid prefix :" + condition.getPrefixOid());

		} else {
			logInfo("\tentity count :" + "0");
		}

		logInfo("-----------------------------------------------------------");
		logInfo("");
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
	 * Import用のパラメータを生成して、Import処理を実行します。
	 *
	 * @return
	 */
	private boolean startImportWizard() {

		//テナントURL
		String tenantUrl = readConsole(getCommonResourceMessage("inputTenantUrlMsg"));

		if (StringUtil.isEmpty(tenantUrl)) {
			logWarn(getCommonResourceMessage("requiredTenantUrlMsg"));
			return startImportWizard();
		}
		if (tenantUrl.equalsIgnoreCase("-show")) {
			//一覧を出力
			showValidTenantList();
			return startImportWizard();
		}
		if (tenantUrl.equalsIgnoreCase("-env")) {
			//環境情報を出力
			logEnvironment();
			return startImportWizard();
		}

		//URL存在チェック
		String key = tenantUrl.startsWith("/") ? tenantUrl : "/" + tenantUrl;
		Tenant tenant = tenantService.getTenant(key);
		if (tenant == null) {
			logWarn(getCommonResourceMessage("notExistsTenantMsg", key));
			return startImportWizard();
		}

		PackageImportParameter param = new PackageImportParameter(tenant.getId(), tenant.getName());

		//Workディレクトリ
		boolean validWork = false;
		do {
			String workDirName = readConsole(rs("PackageImport.Wizard.inputWorkMsg") + "(" + param.getWorkDirName() + ")");
			if (StringUtil.isNotBlank(workDirName)) {
				param.setWorkDirName(workDirName);
			}

			//チェック
			File workDir = new File(param.getWorkDirName());
			if (!workDir.exists()) {
				workDir.mkdir();
				logInfo(rs("PackageImport.Wizard.createdWorkDirMsg", param.getWorkDirName()));
			}
			if (!workDir.isDirectory()) {
				logError(rs("PackageImport.Wizard.notDirMsg", param.getWorkDirName()));
			} else {
				param.setWorkDir(workDir);
				validWork = true;
			}
		} while(validWork == false);

		//Importファイル
		PackageInfo packInfo = null;
		boolean validFile = false;
		do {
			String importFileName = readConsole(rs("PackageImport.Wizard.inputImportFileMsg"));
			if (StringUtil.isNotBlank(importFileName)) {
				param.setImportFilePath(importFileName);

				//存在チェック
				File importFile = new File(param.getImportFilePath());
				if (!importFile.exists()) {
					logWarn(rs("PackageImport.Wizard.notExistsImportFileMsg"));
					continue;
				}

				try {
					//データチェック
					packInfo = packageService.getPackageInfo(importFile);

					//対象メタデータチェック
					int metaCount = (CollectionUtil.isNotEmpty(packInfo.getMetaDataPaths()) ? packInfo.getMetaDataPaths().size() : 0);
					if (metaCount == 0) {
						logInfo(rs("PackageImport.Wizard.notIncludeMetaMsg"));
					} else {
						//警告テナントの存在チェック
						if (packInfo.isWarningTenant()) {
							//警告がある場合は、Packageに含まれるテナントは取り込まない
							logWarn(rs("PackageImport.Wizard.includeWarnTenantMetaMsg"));
						} else {
							//警告がない場合は、Packageに含まれるテナントをセット
							param.setImportTenant(packInfo.getTenant());
						}

						boolean isShow = readConsoleBoolean(rs("PackageImport.Wizard.confirmShowMetaListMsg", metaCount), false);
						if (isShow) {
							showMetaDataPathList(packInfo);
						}
					}
					boolean isContinue = readConsoleBoolean(getCommonResourceMessage("continueMsg"), true);
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
					isContinue = readConsoleBoolean(getCommonResourceMessage("continueMsg"), true);
					if (!isContinue) {
						continue;
					}

					param.setImportFile(importFile);
					param.setPackInfo(packInfo);

					validFile = true;

				} catch (PackageRuntimeException e) {
					logWarn(rs("PackageImport.Wizard.errorAnalysisFileMsg", e.getMessage()));
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

			//ForceUpdate
			boolean isForceUpdate = readConsoleBoolean(rs("PackageImport.Wizard.confirmForceUpdateMsg"), condition.isFourceUpdate());
			condition.setFourceUpdate(isForceUpdate);

			//Errorスキップ
			boolean isErrorSkip = readConsoleBoolean(rs("PackageImport.Wizard.confirmSkipErrorDataMsg"), condition.isErrorSkip());
			condition.setErrorSkip(isErrorSkip);

			//存在しないプロパティは無視
			boolean isIgnoreNotExistsProperty = readConsoleBoolean(rs("PackageImport.Wizard.confirmIgnoreNotExistsPropertyMsg"), condition.isIgnoreNotExistsProperty());
			condition.setIgnoreNotExistsProperty(isIgnoreNotExistsProperty);

			//Listner実行
			boolean isNotifyListner = readConsoleBoolean(rs("PackageImport.Wizard.confirmNotifyListenerMsg"), condition.isNotifyListeners());
			condition.setNotifyListeners(isNotifyListner);

			//更新不可項目の更新
			boolean isUpdateDisupdatableProperty = readConsoleBoolean(rs("PackageImport.Wizard.confirmUpdateDisupdatablePropertyMsg"), condition.isUpdateDisupdatableProperty());
			condition.setUpdateDisupdatableProperty(isUpdateDisupdatableProperty);

			if (isUpdateDisupdatableProperty) {
				condition.setWithValidation(false);
			} else {
				//Validationの実行
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
						logWarn(rs("PackageImport.Wizard.warnOIDPrefixMsg"));
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
					return startImportWizard();
				}
			}
		} while(validExecute == false);

		//ConsoleのLogListnerを一度削除してLog出力に切り替え
		LogListner consoleLogListner = getConsoleLogListner();
		removeLogListner(consoleLogListner);
		LogListner loggingListner = getLoggingLogListner();
		addLogListner(loggingListner);

		//Import処理実行
		boolean ret = executeImport(param);

		//LogListnerを一度削除
		removeLogListner(loggingListner);

		return ret;
	}

	private String rs(String key, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(getLanguage(), key, args);
	}

}
