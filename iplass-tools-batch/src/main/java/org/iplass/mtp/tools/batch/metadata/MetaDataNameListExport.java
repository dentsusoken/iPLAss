/*
 * Copyright 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadata;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.tools.metaport.MetaDataNameListCsvWriter;
import org.iplass.mtp.impl.tools.metaport.MetaDataPortingService;
import org.iplass.mtp.impl.tools.metaport.XMLEntryInfo;
import org.iplass.mtp.impl.tools.pack.PackageService;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.ToolsBatchResourceBundleUtil;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;


/**
 * Export MetaDataXML Name List Batch
 */
public class MetaDataNameListExport extends MtpCuiBase {

	/** リソースファイルの接頭語 */
	private static final String RES_WIZARD_PRE = "ExportMetaDataNameList.Wizard.";


	//実行モード
	private ExecMode execMode = ExecMode.WIZARD;

	private MetaDataPortingService metaService = ServiceRegistry.getRegistry().getService(MetaDataPortingService.class);
	private PackageService packageService = ServiceRegistry.getRegistry().getService(PackageService.class);

	/**
	 * args[0]・・・execMode
	 **/
	public static void main(String[] args) {

		MetaDataNameListExport instance = null;
		try {
			instance = new MetaDataNameListExport(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * args[0]・・・execMode
	 **/
	public MetaDataNameListExport(String... args) {

		if (args != null) {
			if (args.length > 0) {
				setExecMode(ExecMode.valueOf(args[0]));
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

		//Console出力用のログリスナーを生成
		LogListner consoleLogListner = getConsoleLogListner();
		addLogListner(consoleLogListner);

		//環境情報出力
		logEnvironment();

		switch (getExecMode()) {
		case WIZARD :
			logInfo("■Start Export Wizard");
			logInfo("");

			//Wizardの実行
			return startExportWizard();
		case SILENT :
			//TODO Silent版
			logInfo("■Start Export Silent");
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
	public boolean executeExport(final MetaDataNameListExportParameter param) {

		setSuccess(false);

		try {
			boolean isSuccess = Transaction.requiresNew(t -> {

					//対象ファイル
					File importFile = new File(param.getMetaDataFilePath());

					//出力ファイル
					File outFile = new File(param.getExportDir(), param.getExportFileName() + ".csv");

					//テナントはダミー(テナントに依存しないので)
					TenantContext tContext = new TenantContext(0, "dummy", "/", true);
					ExecuteContext eContext = new ExecuteContext(tContext);
					eContext.setLanguage(getLanguage());	//ログがTenantのLocaleになるのでバッチで指定された言語に設定
					ExecuteContext.initContext(eContext);

					InputStream metaXML = null;
					MetaDataNameListCsvWriter wrappedWriter = null;
					try {
						//メタデータ定義のXMLファイルを取得
						if ("zip".equals(getFileExtension(importFile))) {
							//Package ZIP
							metaXML = packageService.getMetaDataInputStream(importFile);
						} else {
							//MetaData XMLを直接指定した場合
							metaXML = new FileInputStream(importFile);
						}

						//XMLを解析してMetaDataEntryを取得
						XMLEntryInfo entryInfo = metaService.getXMLMetaDataEntryInfo(metaXML);

						//ソート
						List<MetaDataEntry> entries = new ArrayList<MetaDataEntry>(entryInfo.getPathEntryMap().values());
						Collections.sort(entries, new Comparator<MetaDataEntry>() {
							@Override
							public int compare(MetaDataEntry o1, MetaDataEntry o2) {
								return o1.getPath().toLowerCase().compareTo(o2.getPath().toLowerCase());
							}
						});

						//出力
						wrappedWriter = new MetaDataNameListCsvWriter(new FileOutputStream(outFile));

						//ヘッダ出力
						wrappedWriter.writeHeader();

						for (MetaDataEntry entry : entries) {
							wrappedWriter.writeEntry(entry);
						}

					} catch (IOException e) {
						throw new RuntimeException(e);
					} finally {
						try {
							if (metaXML != null) {
								metaXML.close();
							}
						} catch (IOException e) {
				            throw new RuntimeException(e);
						} finally {
							try {
								if (wrappedWriter != null) {
									wrappedWriter.close();
								}
							} catch (IOException e) {
					            throw new RuntimeException(e);
							}
						}
					}

					return true;
			});

			setSuccess(isSuccess);

		} catch (Throwable e) {
			logError(getCommonResourceMessage("errorMsg", e.getMessage()));
			e.printStackTrace();
		} finally {
			logInfo("");
			logInfo("■Execute Result :" + (isSuccess() ? "SUCCESS" : "FAILED"));
			logInfo("");

			ExecuteContext.initContext(null);
		}

		return isSuccess();
	}

	/**
	 * Import用のパラメータを生成して、Import処理を実行します。
	 *
	 * @return
	 */
	private boolean startExportWizard() {

		MetaDataNameListExportParameter param = new MetaDataNameListExportParameter();

		//Importファイル
		boolean validFile = false;
		do {
			String importFileName = readConsole(getImportWizardResourceMessage("inputMetaDataFileMsg"));
			if (StringUtil.isNotBlank(importFileName)) {
				param.setMetaDataFilePath(importFileName);

				//存在チェック
				File importFile = new File(param.getMetaDataFilePath());
				if (!importFile.exists()) {
					logWarn(getImportWizardResourceMessage("notExistsMetaDataFileMsg"));
					continue;
				}
				if (importFile.isDirectory()) {
					logWarn(getImportWizardResourceMessage("notFileMsg", param.getMetaDataFilePath()));
					continue;
				}

				validFile = true;

			} else {
				logWarn(getImportWizardResourceMessage("requiredMetaDataFilePathMsg"));
			}

		} while(validFile == false);

		//出力先ディレクトリ
		boolean validExportDir = false;
		do {
			String exportDirName = readConsole(getImportWizardResourceMessage("inputDirMsg") + "(" + param.getExportDirName() + ")");
			if (StringUtil.isNotBlank(exportDirName)) {
				param.setExportDirName(exportDirName);
			}

			//チェック
			File exportDir = new File(param.getExportDirName());
			if (!exportDir.exists()) {
				exportDir.mkdir();
				logInfo(getImportWizardResourceMessage("createdInputDirMsg", param.getExportDirName()));
			}
			if (!exportDir.isDirectory()) {
				logWarn(getImportWizardResourceMessage("notDirMsg", param.getExportDirName()));
			} else {
				param.setExportDir(exportDir);
				validExportDir = true;
			}
		} while(validExportDir == false);

		//出力ファイル名
		String exportFileName = readConsole(getImportWizardResourceMessage("inputFileNameMsg") + "(" + param.getExportFileName() + ")");
		if (StringUtil.isNotBlank(exportFileName)) {
			param.setExportFileName(exportFileName);
		}

		//ConsoleのLogListnerを一度削除してLog出力に切り替え
		LogListner consoleLogListner = getConsoleLogListner();
		removeLogListner(consoleLogListner);
		LogListner loggingListner = getLoggingLogListner();
		addLogListner(loggingListner);

		//Import処理実行
		boolean ret = executeExport(param);

		//LogListnerを一度削除
		removeLogListner(loggingListner);

		return ret;
	}

	private String getFileExtension(File file) {
		String fileName = file.getName();
		if(fileName.lastIndexOf(".") > 0) {
			return fileName.substring(fileName.lastIndexOf(".")+1);
		} else {
			return "";
		}
	}

	private String getImportWizardResourceMessage(String suffix, Object... args) {
		return ToolsBatchResourceBundleUtil.resourceString(getLanguage(), RES_WIZARD_PRE + suffix, args);
	}

}
