/*
 * Copyright 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadataporting;

import static org.iplass.mtp.tools.batch.metadataporting.MetaDataExportParameter.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.metaport.MetaDataWriteCallback;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tenant.Tenant;
import org.iplass.mtp.tools.ToolsBatchResourceBundleUtil;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.pack.PackageExport;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.CollectionUtil;
import org.iplass.mtp.util.StringUtil;

public class MetaDataExport extends MtpCuiBase {

	/** Silentモード 設定ファイル名 */
	public static final String KEY_CONFIG_FILE = "meta.config";

	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private MetaDataPortingService mdps = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);
	private TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);

	//実行モード
	private ExecMode execMode = ExecMode.WIZARD;

	//テナントID(引数)
	private Integer tenantId;

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・language
	 **/
	public static void main(String[] args) {

		MetaDataExport instance = null;
		try {
			instance = new MetaDataExport(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			ExecuteContext.finContext();
		}
	}

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 * args[2]・・・language
	 **/
	public MetaDataExport(String... args) {

		if (args != null) {
			if (args.length > 0 && args[0] != null) {
				execMode = ExecMode.valueOf(args[0].toUpperCase());
			}
			if (args.length > 1 && args[1] != null) {
				tenantId = Integer.parseInt(args[1]);
				if (tenantId == -1) {
					tenantId = null;
				}
			}
			if (args.length > 2 && args[2] != null) {
				//systemの場合は、JVMのデフォルトを利用
				if (!"system".equals(args[2].toLowerCase())) {
					setLanguage(args[2]);
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

		try {
			switch (execMode) {
			case WIZARD :
				LogListner consoleLogListner = getConsoleLogListner();
				addLogListner(consoleLogListner);

				//環境情報出力
				logEnvironment();

				logInfo("■Start Export Wizard");
				logInfo("");

				//Wizardの実行
				return wizard();
			case SILENT :
				LogListner loggingLogListner = getLoggingLogListner();
				addLogListner(loggingLogListner);

				//環境情報出力
				logEnvironment();

				logInfo("■Start Export Silent");
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

	public MetaDataExport execMode(ExecMode execMode) {
		this.execMode = execMode;
		return this;
	}

	public MetaDataExport tenantId(Integer tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Exportします。
	 *
	 * @param param Export情報
	 * @return
	 */
	public boolean exportMeta(final MetaDataExportParameter param) {

		setSuccess(false);

		try {
			boolean isSuccess = Transaction.required(t -> {

				TenantContext tc = tcs.getTenantContext(param.getTenantId());
				return ExecuteContext.executeAs(tc, () -> {
					ExecuteContext.getCurrentContext().setLanguage(getLanguage());

					//外部から直接呼び出された場合を考慮し、Pathを取得
					if (param.getExportMetaDataPathList() == null) {
						param.setExportMetaDataPathList(getMetaDataPathList(param));
					}

					if (CollectionUtil.isNotEmpty(param.getExportMetaDataPathList())) {
						logDebug("metadata path count : " + param.getExportMetaDataPathList().size());

						logInfo(rs("MetaDataExport.startExportMetaData"));

						//MetaDataをtempに出力
						File metadataFile = new File(param.getExportDir(), param.getFileName() + ".xml");
						try (PrintWriter writer = new PrintWriter(metadataFile, "UTF-8")){

							mdps.write(writer, param.getExportMetaDataPathList(), new MetaDataWriteCallback() {

								@Override
								public void onWrited(String path, String version) {
									logInfo(rs("MetaDataExport.outputMetaData", path));
								}

								@Override
								public boolean onWarning(String path, String message, String version) {
									logWarn(rs("MetaDataExport.warningOutputMetaData", path));
									logWarn(message);
									return true;
								}

								@Override
								public void onStarted() {
								}

								@Override
								public void onFinished() {
								}

								@Override
								public boolean onErrored(String path, String message, String version) {
									logError(rs("MetaDataExport.errorOutputMetaData", path));
									logError(message);
									return false;
								}
							});
						} catch (FileNotFoundException e) {
							throw new SystemException(e);
						} catch (UnsupportedEncodingException e) {
							throw new SystemException(e);
						}

						logInfo(rs("MetaDataExport.completedExportMetaData"));

					} else {
						logWarn(rs("MetaDataExport.nonTargetMetaData"));
					}

					return true;
				});

			});

			setSuccess(isSuccess);

		} catch (Throwable e) {
			logError(getCommonResourceMessage("errorMsg", e.getMessage()));
		}

		return isSuccess();
	}

	/**
	 * MetaDataExport情報を出力します。
	 */
	public void logArguments(final MetaDataExportParameter param) {
		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\texport dir :" + param.getExportDirName());
		logInfo("\tfile name :" + param.getFileName());
		String metaTarget = null;
		if (param.isExportAllMetaData()) {
			metaTarget = "ALL";
			if (param.isExportTenantMetaData()) {
				metaTarget += "(include Tenant)";
			} else {
				metaTarget += "(exclude Tenant)";
			}
		} else {
			metaTarget = param.getExportMetaDataPathStr();
		}
		metaTarget += "(" + param.getExportMetaDataPathList().size() + ")";

		logInfo("\tmetadata target :" + metaTarget);
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}

	/**
	 * Export対象のメタデータパスを設定します。
	 * @param param
	 */
	private List<String> getMetaDataPathList(final MetaDataExportParameter param) {

		List<String> paths = new ArrayList<>();
		if (param.isExportAllMetaData()) {
			List<MetaDataEntryInfo> allMeta = MetaDataContext.getContext().definitionList("/");
			for (MetaDataEntryInfo info : allMeta) {
				if (param.isExportLocalMetaDataOnly()) {
					if (info.getRepositryType() != RepositoryType.TENANT_LOCAL) {
						continue;
					}
				}
				if (!param.isExportTenantMetaData()) {
					if (mdps.isTenantMeta(info.getPath())) {
						continue;
					}
				}
				paths.add(info.getPath());
			}
		} else {
			//個別指定

			Set<String> directPathSet = new HashSet<>();	//重複を避けるためSetに保持

			String[] pathStrArray = param.getExportMetaDataPathStr().split(",");
			for (String pathStr : pathStrArray) {
				//,,などの阻止
				if (StringUtil.isEmpty(pathStr)) {
					continue;
				}

				if (pathStr.endsWith("*")) {
					//アスタリスク指定
					List<MetaDataEntryInfo> allMeta = MetaDataContext.getContext().definitionList(pathStr.substring(0, pathStr.length() - 1));
					for (MetaDataEntryInfo info : allMeta) {
						if (param.isExportLocalMetaDataOnly()) {
							if (info.getRepositryType() != RepositoryType.TENANT_LOCAL) {
								continue;
							}
						}
						directPathSet.add(info.getPath());
					}
				} else {
					//直接指定
					MetaDataEntry entry = MetaDataContext.getContext().getMetaDataEntry(pathStr);
					if (entry != null) {
						if (param.isExportLocalMetaDataOnly()) {
							if (entry.getRepositryType() != RepositoryType.TENANT_LOCAL) {
								logWarn(rs("MetaDataExport.excludeNotLocalMetaLog", pathStr));
								continue;
							}
						}
						directPathSet.add(entry.getPath());
					} else {
						logWarn(rs("MetaDataExport.notFoundMetaLog", pathStr));
						continue;
					}
				}
			}
			paths.addAll(directPathSet);
		}

		//ソートして返す
		return paths.stream().sorted().collect(Collectors.toList());
	}

	/**
	 * Export対象のメタデータパスを出力します。
	 * @param param
	 */
	private void showMetaDataPathList(final MetaDataExportParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■MetaData List");
		for (String path : param.getExportMetaDataPathList()) {
			logInfo(path);
		}
		logInfo("-----------------------------------------------------------");
	}

	/**
	 * Wizard形式でExport用のパラメータを生成して、Export処理を実行します。
	 *
	 * @return 実行結果
	 */
	private boolean wizard() {

		Tenant tenant = null;
		if (tenantId != null) {
			//引数でテナントIDが指定されている場合
			tenant = ts.getTenant(tenantId);
			if (tenant == null) {
				logWarn(getCommonResourceMessage("notExistsTenantIdMsg", tenantId));
				tenantId = null;
				return wizard();
			}
		} else {
			//テナントURL
			String tenantUrl = readConsole(getCommonResourceMessage("inputTenantUrlMsg"));

			if (StringUtil.isEmpty(tenantUrl)) {
				logWarn(getCommonResourceMessage("requiredTenantUrlMsg"));
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
				logWarn(getCommonResourceMessage("notExistsTenantMsg", tenantUrl));
				return wizard();
			}
		}

		MetaDataExportParameter param = new MetaDataExportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()-> {
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			//出力先ディレクトリ
			boolean validFile = false;
			do {
				String exportDirName = readConsole(rs("MetaDataExport.Wizard.inputDirMsg") + "(" + param.getExportDirName() + ")");
				if (StringUtil.isNotBlank(exportDirName)) {
					param.setExportDirName(exportDirName);
				}
				File dir = new File(param.getExportDirName());
				if (checkDir(dir)) {
					param.setExportDir(dir);
					validFile = true;
				}
			} while(validFile == false);

			//ファイル名
			String fileName = readConsole(rs("MetaDataExport.Wizard.inputFileNameMsg") + "(" + param.getFileName() + ")");
			if (StringUtil.isNotBlank(fileName)) {
				param.setFileName(fileName);
			}

			boolean validTarget = false;
			do {
				boolean isExportLocalOnly = readConsoleBoolean(rs("MetaDataExport.Wizard.confirmTargetLocalMetaMsg"), param.isExportLocalMetaDataOnly());
				param.setExportLocalMetaDataOnly(isExportLocalOnly);

				boolean isExportAllMeta = readConsoleBoolean(rs("MetaDataExport.Wizard.confirmExportAllMetaMsg"), param.isExportAllMetaData());
				param.setExportAllMetaData(isExportAllMeta);
				if (isExportAllMeta) {
					//全メタデータ出力

					//テナントを含めるかを確認
					boolean isExportTenantMetaData = readConsoleBoolean(rs("MetaDataExport.Wizard.confirmIncludeTenantMetaMsg"), param.isExportTenantMetaData());
					param.setExportTenantMetaData(isExportTenantMetaData);

					validTarget = true;
				} else {
					//個別指定
					String exportMetaDataPathStr = readConsole(rs("MetaDataExport.Wizard.inputMetaPathMsg"));
					if (StringUtil.isEmpty(exportMetaDataPathStr)) {
						//未指定なのでContinue
						logWarn(rs("MetaDataExport.Wizard.requiredMetaPathMsg"));
						logInfo("");
					} else {
						param.setExportMetaDataPathStr(exportMetaDataPathStr);
						validTarget = true;
					}
				}

				if (validTarget) {
					//Pathの取得
					param.setExportMetaDataPathList(getMetaDataPathList(param));

					boolean isShow = readConsoleBoolean(rs("MetaDataExport.Wizard.confirmShowMetaListMsg", param.getExportMetaDataPathList().size()), false);
					if (isShow) {
						showMetaDataPathList(param);
					}
					boolean isContinue = readConsoleBoolean(getCommonResourceMessage("continueMsg"), true);
					if (!isContinue) {
						validTarget = false;
					}
				}

			} while(validTarget == false);

			boolean validExecute = false;
			do {
				//実行情報出力
				logArguments(param);

				boolean isExecute = readConsoleBoolean(rs("MetaDataExport.Wizard.confirmExecuteMsg"), false);
				if (isExecute) {
					validExecute = true;
				} else {
					//defaultがfalseなので念のため再度確認
					isExecute = readConsoleBoolean(rs("MetaDataExport.Wizard.confirmRetryMsg"), true);

					if (isExecute) {
						//再度実行
						return wizard();
					}
				}
			} while(validExecute == false);

			//ConsoleのLogListnerを一度削除してLog出力に切り替え
			LogListner consoleLogListner = getConsoleLogListner();
			removeLogListner(consoleLogListner);
			LogListner loggingListner = getLoggingLogListner();
			addLogListner(loggingListner);

			//Export処理実行
			try {
				exportMeta(param);

				return isSuccess();

			} finally {
				removeLogListner(loggingListner);
				addLogListner(consoleLogListner);
			}
		});
	}

	/**
	 * Propertyファイル形式でExport用のパラメータを生成して、Export処理を実行します。
	 *
	 * @return 実行結果
	 */
	private boolean silent() {

		//プロパティファイルの取得
		String configFileName = System.getProperty(KEY_CONFIG_FILE);
		if (StringUtil.isEmpty(configFileName)) {
			logError(rs("MetaDataExport.Silent.requiredConfigFileMsg", KEY_CONFIG_FILE));
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
						logError(rs("MetaDataExport.Silent.notExistsConfigFileMsg", configFileName));
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

		//テナントの取得
		Tenant tenant = null;
		if (tenantId != null) {
			//引数でテナントIDが指定されている場合
			tenant = ts.getTenant(tenantId);
			if (tenant == null) {
				logError(getCommonResourceMessage("notExistsTenantIdMsg", tenantId));
				return false;
			}
		} else {
			//プロパティから取得
			//ID
			String propTenantId = prop.getProperty(PROP_TENANT_ID);
			if (StringUtil.isNotEmpty(propTenantId)) {
				tenant = ts.getTenant(Integer.parseInt(propTenantId));
				if (tenant == null) {
					logError(getCommonResourceMessage("notExistsTenantIdMsg", propTenantId));
					return false;
				}
			}
			if (tenant == null) {
				//URL
				String propTenantUrl = prop.getProperty(PROP_TENANT_URL);
				if (StringUtil.isNotEmpty(propTenantUrl)) {
					if (!propTenantUrl.startsWith("/")) {
						propTenantUrl = "/" + propTenantUrl;
					}
					tenant = ts.getTenant(propTenantUrl);
					if (tenant == null) {
						logError(getCommonResourceMessage("notExistsTenantMsg", propTenantUrl));
						return false;
					}
				}
			}
			if (tenant == null) {
				logError(getCommonResourceMessage("requiredMsg", PROP_TENANT_ID + " or " + PROP_TENANT_URL));
				return false;
			}
		}
		logInfo("target tenant:[" + tenant.getId() + "]" + tenant.getName());

		MetaDataExportParameter param = new MetaDataExportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()-> {
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			String exportDirName = prop.getProperty(PROP_EXPORT_DIR);
			if (StringUtil.isNotEmpty(exportDirName)) {
				param.setExportDirName(exportDirName);
			}
			File exportDir = new File(param.getExportDirName());
			if (!checkDir(exportDir)) {
				return false;
			}
			param.setExportDir(exportDir);

			String fileName = prop.getProperty(PROP_FILE_NAME);
			if (StringUtil.isNotEmpty(fileName)) {
				param.setFileName(fileName);
			}

			String localOnly = prop.getProperty(PROP_META_LOCAL_ONLY);
			if (StringUtil.isNotEmpty(localOnly)) {
				param.setExportLocalMetaDataOnly(Boolean.valueOf(localOnly));
			}

			String source = prop.getProperty(PROP_META_SOURCE);
			if (StringUtil.isEmpty(source)) {
				//全対象
				param.setExportAllMetaData(true);

				String excludeTenant = prop.getProperty(PROP_META_EXCLUDE_TENANT);
				if (StringUtil.isNotEmpty(excludeTenant)) {
					param.setExportTenantMetaData(!Boolean.valueOf(excludeTenant));
				}

			} else {
				param.setExportAllMetaData(false);
				param.setExportMetaDataPathStr(source);
			}
			param.setExportMetaDataPathList(getMetaDataPathList(param));

			//実行情報出力
			logArguments(param);

			//Export処理実行
			return exportMeta(param);
		});

	}

	private boolean checkDir(File dir) {

		if (!dir.exists()) {
			dir.mkdir();
			logInfo(rs("MetaDataExport.createdDirMsg", dir.getPath()));
		}
		if (!dir.isDirectory()) {
			logError(rs("MetaDataExport.notDirMsg", dir.getPath()));
			return false;
		}
		return true;
	}

	private String rs(String key, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(getLanguage(), key, args);
	}
}
