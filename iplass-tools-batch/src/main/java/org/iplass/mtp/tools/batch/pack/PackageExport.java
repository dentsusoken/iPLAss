/*
 * Copyright 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.pack;

import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_ENTITY_EXCLUDE_USER;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_ENTITY_EXPORT;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_ENTITY_SOURCE;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_EXPORT_DIR;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_META_EXCLUDE_TENANT;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_META_EXPORT;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_META_LOCAL_ONLY;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_META_SOURCE;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_PACKAGE_NAME;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_SAVE_PACKAGE;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_TENANT_ID;
import static org.iplass.mtp.tools.batch.pack.PackageExportParameter.PROP_TENANT_URL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

import org.apache.commons.io.IOUtils;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantContextService;
import org.iplass.mtp.impl.entity.EntityService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;
import org.iplass.mtp.impl.tenant.TenantService;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.pack.PackageCreateCondition;
import org.iplass.mtp.impl.tools.pack.PackageCreateResult;
import org.iplass.mtp.impl.tools.pack.PackageEntity;
import org.iplass.mtp.impl.tools.pack.PackageService;
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
 * Package Export Batch
 */
public class PackageExport extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(PackageExport.class);

	/** Silentモード 設定ファイル名 */
	public static final String KEY_CONFIG_FILE = "pack.config";

	//実行モード
	private ExecMode execMode = ExecMode.WIZARD;

	private TenantService ts = ServiceRegistry.getRegistry().getService(TenantService.class);
	private TenantContextService tcs = ServiceRegistry.getRegistry().getService(TenantContextService.class);
	private PackageService ps = ServiceRegistry.getRegistry().getService(PackageService.class);
	private MetaDataPortingService mdps = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);
	private EntityService ehs = ServiceRegistry.getRegistry().getService(EntityService.class);

	//テナントID(引数)
	private Integer tenantId;

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 **/
	public static void main(String[] args) {

		PackageExport instance = null;
		try {
			instance = new PackageExport(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			ExecuteContext.finContext();
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tenantId
	 **/
	public PackageExport(String... args) {

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

	public PackageExport execMode(ExecMode execMode) {
		this.execMode = execMode;
		return this;
	}

	public PackageExport tenantId(Integer tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	/**
	 * Exportします。
	 *
	 * @param param Export情報
	 * @return
	 */
	public boolean exportPack(final PackageExportParameter param) {

		setSuccess(false);

		boolean isSuccess = Transaction.required(t -> {

			TenantContext tc = tcs.getTenantContext(param.getTenantId());
			return ExecuteContext.executeAs(tc, () -> {
				ExecuteContext.getCurrentContext().setLanguage(getLanguage());

				//外部から直接呼び出された場合を考慮し、Pathを取得
				if (param.isExportMetaData() && param.getExportMetaDataPathList() == null) {
					param.setExportMetaDataPathList(getMetaDataPathList(param));
				}

				//外部から直接呼び出された場合を考慮し、Pathを取得
				if (param.isExportEntityData() && param.getExportEntityDataPathList() == null) {
					param.setExportEntityDataPathList(getEntityDataPathList(param));
				}

				//PackageCreateConditionの生成
				final PackageCreateCondition cond = new PackageCreateCondition();
				cond.setName(param.getPackageName());
				cond.setMetaDataPaths(param.getExportMetaDataPathList());
				cond.setEntityPaths(param.getExportEntityDataPathList());

				List<String> messageSummary = new ArrayList<>();

				PackageCreateResult result = null;
				String fileName = null;
				if (param.isSavePackage()) {
					//Package情報を登録(別トランザクションにしないとarchivePackage処理でエラーになる。別トランザクションなので)
					final String oid = Transaction.requiresNew(tt -> {
						return ps.storePackage(cond, PackageEntity.TYPE_OFFLINE);
					});

					String infoMsg = rs("PackageExport.createdPackageInfoLog", oid );
					logInfo(infoMsg);
					messageSummary.add(infoMsg);

					//Package作成処理
					logInfo(rs("PackageExport.startExportPackageLog"));
					result = Transaction.requiresNew(tt -> {
						return ps.archivePackage(oid);
					});

					if (!result.isError()) {
						//zip作成処理(別トランザクションにしないとArchiveが取得できない。Package作成が別トランザクションなので)
						if (param.isSavePackage()) {
							fileName = Transaction.requiresNew(tt -> {
								return createExportFile(param, oid);
							});
						}
					}

				} else {
					//Package直接作成
					logInfo(rs("PackageExport.startExportPackageLog"));

					File file = new File(param.getExportDir(), param.getPackageName() + ".zip");
					try (OutputStream os = new FileOutputStream(file)) {
						result = new PackageCreateResult();
						final PackageCreateResult resultWork = result;
						Transaction.requiresNew(tt -> {
							ps.write(os, cond, resultWork, null);
						});
						fileName = file.getAbsolutePath();
					} catch (IOException e) {
			            throw new SystemException(e);
					}
				}

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
						messageSummary.add(message);
					}
					logInfo("");
				}

				logInfo("-----------------------------------------------------------");
				logInfo("■Execute Result Summary");
				for(String message : messageSummary) {
					logInfo(message);
				}
				logInfo("-----------------------------------------------------------");
				logInfo("");

				logInfo(rs("PackageExport.completedExportPackageLog", fileName));

				return true;
			});

		});

		setSuccess(isSuccess);

		return isSuccess();
	}

	/**
	 * PackageExport情報を出力します。
	 */
	public void logArguments(final PackageExportParameter param) {
		logInfo("-----------------------------------------------------------");
		logInfo("■Execute Argument");
		logInfo("\ttenant name :" + param.getTenantName());
		logInfo("\texport dir :" + param.getExportDirName());
		logInfo("\tpackage name :" + param.getPackageName());
		logInfo("\tsave package :" + param.isSavePackage());
		logInfo("\texport metadata :" + param.isExportMetaData());
		if (param.isExportMetaData()) {
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
		}
		logInfo("\texport entity data :" + param.isExportEntityData());
		if (param.isExportEntityData()) {
			String entityTarget = null;
			if (param.isExportAllEntityData()) {
				entityTarget = "ALL";
				if (param.isExportUserEntityData()) {
					entityTarget += "(include User)";
				} else {
					entityTarget += "(exclude User)";
				}
			} else {
				entityTarget = param.getExportEntityDataPathStr();
			}
			entityTarget += "(" + param.getExportEntityDataPathList().size() + ")";

			logInfo("\tentity target :" + entityTarget);
		}
		logInfo("-----------------------------------------------------------");
		logInfo("");
	}


	/**
	 * Export対象のメタデータパスを設定します。
	 * @param param
	 */
	private List<String> getMetaDataPathList(final PackageExportParameter param) {

		if (!param.isExportMetaData()) {
			return null;
		}

		List<String> paths = new ArrayList<>();
		if (param.isExportAllMetaData()) {
			List<MetaDataEntryInfo> allMeta = MetaDataContext.getContext().definitionList("/");
			for (MetaDataEntryInfo info : allMeta) {
				if (param.isExportLocalMetaDataOnly()) {
					if (info.getRepositryType() == RepositoryType.SHARED) {
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
							if (info.getRepositryType() == RepositoryType.SHARED) {
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
							if (entry.getRepositryType() == RepositoryType.SHARED) {
								logWarn(rs("PackageExport.excludeNotLocalMetaLog", pathStr));
								continue;
							}
						}
						directPathSet.add(entry.getPath());
					} else {
						logWarn(rs("PackageExport.notFoundMetaLog", pathStr));
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
	private void showMetaDataPathList(final PackageExportParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■MetaData List");
		for (String path : param.getExportMetaDataPathList()) {
			logInfo(path);
		}
		logInfo("-----------------------------------------------------------");
	}

	/**
	 * Export対象のEntityデータパスを設定します。
	 * @param param
	 */
	private List<String> getEntityDataPathList(final PackageExportParameter param) {

		if (!param.isExportEntityData()) {
			return null;
		}

		List<String> paths = new ArrayList<>();

		List<MetaDataEntryInfo> entityList = ehs.list();

		if (param.isExportAllEntityData()) {
			for (MetaDataEntryInfo info : entityList) {
				if (!param.isExportUserEntityData()) {
					if (info.getPath().equals(EntityService.getFixedPath() + User.DEFINITION_NAME)) {
						continue;
					}
				}
				paths.add(info.getPath());
			}
		} else {
			//個別指定

			Set<String> directPathSet = new HashSet<>();	//重複を避けるためSetに保持

			String[] pathStrArray = param.getExportEntityDataPathStr().split(",");
			for (String pathStr : pathStrArray) {
				//,,などの阻止
				if (StringUtil.isEmpty(pathStr)) {
					continue;
				}

				boolean isFound = false;
				if (pathStr.endsWith("*")) {
					//アスタリスク指定
					String prefixPath = null;
					if (pathStr.equals("*")) {
						prefixPath = "";
					} else {
						prefixPath = pathStr.substring(0, pathStr.length() - 1).replace(".", "/");
					}
					for (MetaDataEntryInfo info : entityList) {
						if (!info.getPath().startsWith(EntityService.getFixedPath() + prefixPath)) {
							continue;
						}
						isFound = true;
						directPathSet.add(info.getPath());
					}
				} else {
					//直接指定
					for (MetaDataEntryInfo info : entityList) {
						String entityPath = EntityService.getFixedPath() + pathStr.replace(".", "/");
						if (info.getPath().equals(entityPath)) {
							directPathSet.add(info.getPath());
							isFound = true;
							break;
						}
					}
				}
				if (!isFound) {
					logWarn(rs("PackageExport.notFoundEntityLog", pathStr));
					continue;
				}
			}
			paths.addAll(directPathSet);
		}

		final List<String> excludePaths = ps.getNonSupportEntityPathList();
		return paths.stream()
				.filter(path -> {
					return excludePaths == null || !excludePaths.contains(path);
				}).sorted().collect(Collectors.toList());
	}

	/**
	 * Export対象のEntityデータパスを出力します。
	 * @param param
	 */
	private void showEntityDataPathList(final PackageExportParameter param) {

		logInfo("-----------------------------------------------------------");
		logInfo("■EntityData List");
		for (String path : param.getExportEntityDataPathList()) {
			logInfo(path);
		}
		logInfo("-----------------------------------------------------------");
	}

	private String createExportFile(final PackageExportParameter param, final String oid) {

		//Entityの取得
		EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);
		Entity entity = em.load(oid, PackageEntity.ENTITY_DEFINITION_NAME);
		if (entity == null) {
			throw new SystemException(rs("PackageExport.notFoundPackageInfoLog", oid));
		}

		//Fileの取得
		BinaryReference binaryReference = entity.getValue(PackageEntity.ARCHIVE);
		if (binaryReference == null) {
			throw new SystemException(rs("PackageExport.notFoundPackageInfoLog", oid));
		}
		InputStream is = null;
		OutputStream os = null;
		String fileName = null;
		try {
			is = em.getInputStream(binaryReference);
			if (is == null) {
				throw new SystemException(rs("PackageExport.notFoundPackageInfoLog", oid));
			}

			File file = new File(param.getExportDir(), param.getPackageName() + ".zip");
			os = new FileOutputStream(file);
			fileName = file.getAbsolutePath();

			//出力
			IOUtils.copy(is, os);

		} catch (UnsupportedEncodingException e) {
            throw new SystemException(e);
		} catch (IOException e) {
            throw new SystemException(e);
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
	            throw new SystemException(e);
			} finally {
				try {
					if (os != null) {
						os.close();
						os = null;
					}
				} catch (IOException e) {
		            throw new SystemException(e);
				}
			}
		}
		return fileName;
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

		PackageExportParameter param = new PackageExportParameter(tenant.getId(), tenant.getName());

		TenantContext tc = tcs.getTenantContext(param.getTenantId());
		return ExecuteContext.executeAs(tc, ()-> {
			ExecuteContext.getCurrentContext().setLanguage(getLanguage());

			//出力先ディレクトリ
			boolean validFile = false;
			do {
				String exportDirName = readConsole(rs("PackageExport.Wizard.inputDirMsg") + "(" + param.getExportDirName() + ")");
				if (StringUtil.isNotBlank(exportDirName)) {
					param.setExportDirName(exportDirName);
				}
				File dir = new File(param.getExportDirName());
				if (checkDir(dir)) {
					param.setExportDir(dir);
					validFile = true;
				}
			} while(validFile == false);

			//Package名
			String packageName = readConsole(rs("PackageExport.Wizard.inputPackageNameMsg") + "(" + param.getPackageName() + ")");
			if (StringUtil.isNotBlank(packageName)) {
				param.setPackageName(packageName);
			}

			//Packageの保存
			boolean isSavePackage = readConsoleBoolean(rs("PackageExport.Wizard.confirmSavePackageMsg"), param.isSavePackage());
			param.setSavePackage(isSavePackage);

			boolean validTarget = false;
			do {
				//メタデータExport
				boolean validMetaData = false;
				do {
					boolean isExportMeta = readConsoleBoolean(rs("PackageExport.Wizard.confirmExportMetaMsg"), param.isExportMetaData());
					param.setExportMetaData(isExportMeta);
					if (isExportMeta) {

						boolean isExportLocalOnly = readConsoleBoolean(rs("PackageExport.Wizard.confirmTargetLocalMetaMsg"), param.isExportLocalMetaDataOnly());
						param.setExportLocalMetaDataOnly(isExportLocalOnly);

						boolean isExportAllMeta = readConsoleBoolean(rs("PackageExport.Wizard.confirmExportAllMetaMsg"), param.isExportAllMetaData());
						param.setExportAllMetaData(isExportAllMeta);
						if (isExportAllMeta) {
							//全メタデータ出力

							//テナントを含めるかを確認
							boolean isExportTenantMetaData = readConsoleBoolean(rs("PackageExport.Wizard.confirmIncludeTenantMetaMsg"), param.isExportTenantMetaData());
							param.setExportTenantMetaData(isExportTenantMetaData);

							validMetaData = true;
						} else {
							//個別指定
							String exportMetaDataPathStr = readConsole(rs("PackageExport.Wizard.inputMetaPathMsg"));
							if (StringUtil.isEmpty(exportMetaDataPathStr)) {
								//未指定なのでContinue
								logWarn(rs("PackageExport.Wizard.requiredMetaPathMsg"));
								logInfo("");
							} else {
								param.setExportMetaDataPathStr(exportMetaDataPathStr);
								validMetaData = true;
							}
						}

						if (validMetaData) {
							//Pathの取得
							param.setExportMetaDataPathList(getMetaDataPathList(param));
							boolean isShow = readConsoleBoolean(rs("PackageExport.Wizard.confirmShowMetaListMsg", param.getExportMetaDataPathList().size()), false);
							if (isShow) {
								showMetaDataPathList(param);
							}
							boolean isContinue = readConsoleBoolean(rs("Common.continueMsg"), true);
							if (!isContinue) {
								validMetaData = false;
							}
						}

					} else {
						//Exportしない
						validMetaData = true;
					}
				} while(validMetaData == false);


				//EntityデータExport
				boolean validEntityData = false;
				do {
					boolean isExportEntity = readConsoleBoolean(rs("PackageExport.Wizard.confirmExportEntityMsg"), param.isExportEntityData());
					param.setExportEntityData(isExportEntity);
					if (isExportEntity) {

						boolean isExportAllEntity = readConsoleBoolean(rs("PackageExport.Wizard.confirmExportAllEntityMsg"), param.isExportAllEntityData());
						param.setExportAllEntityData(isExportAllEntity);
						if (isExportAllEntity) {
							//全Entityデータ出力

							//Userを含めるかを確認
							boolean isExportUserEntityData = readConsoleBoolean(rs("PackageExport.Wizard.confirmIncludeUserEntityMsg"), param.isExportUserEntityData());
							param.setExportUserEntityData(isExportUserEntityData);

							validEntityData = true;
						} else {
							//個別指定
							String exportEntityPathStr = readConsole(rs("PackageExport.Wizard.inputEntityPathMsg"));
							if (StringUtil.isEmpty(exportEntityPathStr)) {
								//未指定なのでContinue
								logWarn(rs("PackageExport.Wizard.requiredEntityPathMsg"));
								logInfo("");
							} else {
								param.setExportEntityDataPathStr(exportEntityPathStr);
								validEntityData = true;
							}
						}

						if (validEntityData) {
							//Pathの取得
							param.setExportEntityDataPathList(getEntityDataPathList(param));
							boolean isShow = readConsoleBoolean(rs("PackageExport.Wizard.confirmShowEntityListMsg", param.getExportEntityDataPathList().size()), false);
							if (isShow) {
								showEntityDataPathList(param);
							}
							boolean isContinue = readConsoleBoolean(rs("Common.continueMsg"), true);
							if (!isContinue) {
								validEntityData = false;
							}
						}

					} else {
						//Exportしない
						validEntityData = true;
					}
				} while(validEntityData == false);

				//Export対象が含まれるかのチェック
				if (!param.isExportMetaData() && !param.isExportEntityData()) {
					//Export対象なし
					logWarn(rs("PackageExport.Wizard.targetEmptyMsg"));
					logInfo("");
				} else {
					validTarget = true;
				}

			} while(validTarget == false);

			boolean validExecute = false;
			do {
				//実行情報出力
				logArguments(param);

				boolean isExecute = readConsoleBoolean(rs("PackageExport.Wizard.confirmExportPackageMsg"), false);
				if (isExecute) {
					validExecute = true;
				} else {
					//defaultがfalseなので念のため再度確認
					isExecute = readConsoleBoolean(rs("PackageExport.Wizard.confirmRetryMsg"), true);

					if (isExecute) {
						//再度実行
						return wizard();
					}
				}
			} while(validExecute == false);

			//Consoleを削除してLogに切り替え
			switchLog(false, true);

			//Export処理実行
			return executeTask(param, (paramA) -> {
				return exportPack(paramA);
			});
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
			logError(rs("PackageExport.Silent.requiredConfigFileMsg", KEY_CONFIG_FILE));
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
						logError(rs("PackageExport.Silent.notExistsConfigFileMsg", configFileName));
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
				logError(rs("Common.notExistsTenantIdMsg", tenantId));
				return false;
			}
		} else {
			//プロパティから取得
			//ID
			String propTenantId = prop.getProperty(PROP_TENANT_ID);
			if (StringUtil.isNotEmpty(propTenantId)) {
				tenant = ts.getTenant(Integer.parseInt(propTenantId));
				if (tenant == null) {
					logError(rs("Common.notExistsTenantIdMsg", propTenantId));
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
						logError(rs("Common.notExistsTenantMsg", propTenantUrl));
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

		PackageExportParameter param = new PackageExportParameter(tenant.getId(), tenant.getName());

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

			String packageName = prop.getProperty(PROP_PACKAGE_NAME);
			if (StringUtil.isNotEmpty(packageName)) {
				param.setPackageName(packageName);
			}

			String exportMeta = prop.getProperty(PROP_META_EXPORT);
			if (StringUtil.isNotEmpty(exportMeta)) {
				param.setExportMetaData(Boolean.valueOf(exportMeta));
			}
			if (param.isExportMetaData()) {
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
			}

			String exportEntity = prop.getProperty(PROP_ENTITY_EXPORT);
			if (StringUtil.isNotEmpty(exportEntity)) {
				param.setExportEntityData(Boolean.valueOf(exportEntity));
			}
			if (param.isExportEntityData()) {
				String source = prop.getProperty(PROP_ENTITY_SOURCE);
				if (StringUtil.isEmpty(source)) {
					//全対象
					param.setExportAllEntityData(true);

					String excludeUser = prop.getProperty(PROP_ENTITY_EXCLUDE_USER);
					if (StringUtil.isNotEmpty(excludeUser)) {
						param.setExportUserEntityData(!Boolean.valueOf(excludeUser));
					}
				} else {
					param.setExportAllEntityData(false);
					param.setExportEntityDataPathStr(source);
				}
				param.setExportEntityDataPathList(getEntityDataPathList(param));
			}

			//Packageの保存
			String savePackage = prop.getProperty(PROP_SAVE_PACKAGE);
			if (StringUtil.isNotEmpty(savePackage)) {
				param.setSavePackage(Boolean.valueOf(savePackage));
			}

			//実行情報出力
			logArguments(param);

			//Export処理実行
			return executeTask(param, (paramA) -> {
				return exportPack(paramA);
			});
		});

	}

	private boolean checkDir(File dir) {

		if (!dir.exists()) {
			dir.mkdir();
			logInfo(rs("PackageExport.createdDirMsg", dir.getPath()));
		}
		if (!dir.isDirectory()) {
			logError(rs("PackageExport.notDirMsg", dir.getPath()));
			return false;
		}
		return true;
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
