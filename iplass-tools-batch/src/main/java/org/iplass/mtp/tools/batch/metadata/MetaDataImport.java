/*
 * Copyright 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadata;

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
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.tenant.MetaTenant;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.metaport.MetaDataImportResult;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.metaport.XMLEntryInfo;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.pack.PackageExport;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MetaDataImport extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(MetaDataImport.class);

	/** Silentモード 設定ファイル名 */
	public static final String KEY_CONFIG_FILE = "meta.config";

	//実行モード
	private ExecMode execMode = ExecMode.WIZARD;

	//テナントID(引数)
	private Integer tenantId;

	//インポートファイル(引数)
	private String importFile;

	private TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);
	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private MetaDataPortingService mdps = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・import file
	 **/
	public static void main(String[] args) {

		MetaDataImport instance = null;
		try {
			instance = new MetaDataImport(args);
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
	public MetaDataImport(String... args) {

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

		//Console出力
		switchLog(true, false);

		//環境情報出力
		logEnvironment();

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

	public MetaDataImport execMode(ExecMode execMode) {
		this.execMode = execMode;
		return this;
	}

	public MetaDataImport tenantId(Integer tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public MetaDataImport importFile(String importFile) {
		this.importFile = importFile;
		return this;
	}

	/**
	 * Importします。
	 *
	 * @param param Import情報
	 * @return
	 */
	public boolean importMeta(final MetaDataImportParameter param) {

		setSuccess(false);

		boolean isSuccess = Transaction.required(new Function<Transaction, Boolean>() {

			@Override
			public Boolean apply(Transaction t) {

				TenantContext tc  = tcs.getTenantContext(param.getTenantId());
				return ExecuteContext.executeAs(tc, ()->{
					ExecuteContext.getCurrentContext().setLanguage(getLanguage());

					logInfo(rs("MetaDataImport.startImportMetaLog"));

					try (InputStream is = new FileInputStream(param.getImportFile())) {
						//ファイルに含まれるMetaData情報を取得
						XMLEntryInfo entryInfo = mdps.getXMLMetaDataEntryInfo(is);

						//インポート処理の実行
						MetaDataImportResult result = mdps.importMetaData(param.getImportFile().getName(), entryInfo, param.getImportTenant());

						if (result.isError()) {
							if (result.getMessages() != null) {
								for (String message : result.getMessages()) {
									logError(message);
								}
								logInfo("");
							}

							logError(rs("Common.errorMsg", ""));
							return false;
						}

						if (result.getMessages() != null) {
							for (String message : result.getMessages()) {
								logInfo(message);
							}
						}

						logInfo(rs("MetaDataImport.completedImportMetaLog"));
					} catch (IOException e) {
						throw new SystemException("failed to read metadata configure. file=" + param.getImportFile().getName(), e);
					}

					return true;

				});
			}
		});

		setSuccess(isSuccess);

		return isSuccess();
	}

	/**
	 * 実行情報を出力します。
	 */
	public void logArguments(MetaDataImportParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\timport file :" + param.getImportFilePath());

		if (CollectionUtil.isNotEmpty(param.getMetaDataPaths())) {
			logInfo("\tmetadata count :" + param.getMetaDataPaths().size());
		} else {
			logInfo("\tmetadata count :" + "0");
		}

		logInfo("-----------------------------------------------------------");
		logInfo("");
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

		MetaDataImportParameter param = new MetaDataImportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		ExecuteContext.executeAs(tc, ()->{
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			//Importファイル
			boolean validFile = false;
			do {
				String importFileName = importFile;
				importFile = null;
				if (StringUtil.isEmpty(importFileName)) {
					importFileName = readConsole(rs("MetaDataImport.Wizard.inputImportFileMsg"));
				}
				if (StringUtil.isNotBlank(importFileName)) {
					param.setImportFilePath(importFileName);

					//存在チェック
					File file = new File(param.getImportFilePath());
					if (!file.exists()) {
						logWarn(rs("MetaDataImport.notExistsImportFileMsg"));
						continue;
					}

					//データチェック
					try {
						analyse(file, param);
					} catch (IOException e) {
						logWarn(rs("MetaDataImport.errorAnalysisFileMsg", e.getMessage()));
						continue;
					}

					//対象メタデータチェック
					int metaCount = (CollectionUtil.isNotEmpty(param.getMetaDataPaths()) ? param.getMetaDataPaths().size() : 0);
					if (metaCount == 0) {
						logInfo(rs("MetaDataImport.Wizard.notIncludeMetaMsg"));
					} else {
						//警告テナントの存在チェック
						if (param.isWarningTenant()) {
							//警告がある場合は、Packageに含まれるテナントは取り込まない
							logWarn(rs("MetaDataImport.includeWarnTenantMetaMsg"));
						}

						boolean isShow = readConsoleBoolean(rs("MetaDataImport.Wizard.confirmShowMetaListMsg", metaCount), false);
						if (isShow) {
							showMetaDataPathList(param.getMetaDataPaths());
						}
					}
					boolean isContinue = readConsoleBoolean(rs("Common.continueMsg"), true);
					if (!isContinue) {
						continue;
					}

					param.setImportFile(file);

					validFile = true;

				} else {
					logWarn(rs("MetaDataImport.Wizard.requiredImportFilePathMsg"));
				}
			} while(validFile == false);

			boolean validExecute = false;
			do {
				//実行情報出力
				logArguments(param);

				boolean isExecute = readConsoleBoolean(rs("MetaDataImport.Wizard.confirmExecuteMsg"), false);
				if (isExecute) {
					validExecute = true;
				} else {
					//defaultがfalseなので念のため再度確認
					isExecute = readConsoleBoolean(rs("MetaDataImport.Wizard.confirmRetryMsg"), true);

					if (isExecute) {
						//再度実行
						return wizard();
					}
				}
			} while(validExecute == false);

			return null;
		});

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//Import処理実行
		return executeTask(param, (paramA) -> {
			return importMeta(paramA);
		});
	}

	/**
	 * Propertyファイル形式でImport用のパラメータを生成して、Import処理を実行します。
	 *
	 * @return 実行結果
	 */
	private boolean silent() {

		//プロパティファイルの取得
		//インポートに対してはパラメータが少ない、引数で指定可能なので必須にしない
		String configFileName = System.getProperty(KEY_CONFIG_FILE);

		//プロパティの取得
		Properties prop = new Properties();
		if (StringUtil.isNotEmpty(configFileName)) {
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
							logError(rs("MetaDataImport.Silent.notExistsConfigFileMsg", configFileName));
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

		MetaDataImportParameter param = new MetaDataImportParameter(tenant.getId(), tenant.getName());

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
				logError(rs("MetaDataImport.notExistsImportFileMsg"));
				return false;
			}
			param.setImportFilePath(importFilePath);

			//データチェック
			try {
				analyse(file, param);
			} catch (IOException e) {
				logError(rs("MetaDataImport.errorAnalysisFileMsg", e.getMessage()));
				return false;
			}

			//警告テナントの存在チェック
			if (param.isWarningTenant()) {
				//警告がある場合は、Packageに含まれるテナントは取り込まない
				logWarn(rs("MetaDataImport.includeWarnTenantMetaMsg"));
			}
			param.setImportFile(file);

			//実行情報出力
			logArguments(param);

			//Import処理実行
			return executeTask(param, (paramA) -> {
				return importMeta(paramA);
			});
		});

	}

	/**
	 * インポートファイルを解析します。
	 *
	 * @param file インポートファイル
	 * @param param 解析結果をセットするパラメータ
	 * @throws IOException
	 */
	private void analyse(File file, MetaDataImportParameter param) throws IOException {

		//データチェック
		try (InputStream is = new FileInputStream(file)) {

			XMLEntryInfo entryInfo = mdps.getXMLMetaDataEntryInfo(is);
			List<String> pathList = entryInfo.getPathEntryMap().keySet().stream()
					.sorted((path1, path2) -> {
						//大文字・小文字区別しない
						return path1.compareToIgnoreCase(path2);
					})
					.collect(Collectors.toList());
			param.setMetaDataPaths(pathList);

			//テナントのチェック
			Optional<MetaTenant> importMetaTenant = entryInfo.getPathEntryMap().entrySet().stream()
					.filter(entry -> {
						return mdps.isTenantMeta(entry.getKey());
					})
					.map(entry -> {
						return (MetaTenant)entry.getValue().getMetaData();
					})
					.findFirst();

			if (importMetaTenant.isPresent()) {
				MetaTenant metaTenant = importMetaTenant.get();
				Tenant importTenant = new Tenant();
				metaTenant.applyToTenant(importTenant);

				importTenant.setId(-1); //IDはセットされない（不明なので未セット）
				importTenant.setName(metaTenant.getName()); //nameはapplyToTenantでセットされないのでセット(DB側優先)
				importTenant.setDescription(metaTenant.getDescription()); //descriptionはapplyToTenantでセットされないのでセット(DB側優先)

				Tenant currentTenant = ExecuteContext.getCurrentContext().getCurrentTenant();
				if (!currentTenant.getName().equals(metaTenant.getName())) {
					//名前が違う場合はWarning対象
					param.setWarningTenant(true);
					param.setImportTenant(null);
				} else {
					param.setWarningTenant(false);
					param.setImportTenant(importTenant);
				}
			}
		}
	}

	/**
	 * Import対象のメタデータパスを出力します。
	 * @param pathList
	 */
	private void showMetaDataPathList(List<String> pathList) {

		logInfo("-----------------------------------------------------------");
		logInfo("■MetaData List");
		for (String path : pathList) {
			logInfo(path);
		}
		logInfo("-----------------------------------------------------------");
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
