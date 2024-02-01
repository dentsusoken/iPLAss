/*
 * Copyright 2014 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Export MetaDataXML Name List Batch
 */
public class MetaDataNameListExport extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(MetaDataNameListExport.class);

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
			// リソース破棄
			MtpBatchResourceDisposer.disposeResource();
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

		//Console出力
		switchLog(true, false);

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

		boolean isSuccess = Transaction.requiresNew(t -> {

			//対象ファイル
			File importFile = new File(param.getMetaDataFilePath());

			//出力ファイル
			File outFile = new File(param.getExportDir(), param.getExportFileName() + ".csv");

			//テナントはダミー(テナントに依存しないので)
			TenantContext tc = new TenantContext(0, "dummy", "/", true);
			return ExecuteContext.executeAs(tc, () -> {
				ExecuteContext.getCurrentContext().setLanguage(getLanguage());

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
					List<MetaDataEntry> entries = new ArrayList<>(entryInfo.getPathEntryMap().values());
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
		});

		setSuccess(isSuccess);

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
			String importFileName = readConsole(rs("ExportMetaDataNameList.Wizard.inputMetaDataFileMsg"));
			if (StringUtil.isNotBlank(importFileName)) {
				param.setMetaDataFilePath(importFileName);

				//存在チェック
				File importFile = new File(param.getMetaDataFilePath());
				if (!importFile.exists()) {
					logWarn(rs("ExportMetaDataNameList.Wizard.notExistsMetaDataFileMsg"));
					continue;
				}
				if (importFile.isDirectory()) {
					logWarn(rs("ExportMetaDataNameList.Wizard.notFileMsg", param.getMetaDataFilePath()));
					continue;
				}

				validFile = true;

			} else {
				logWarn(rs("ExportMetaDataNameList.Wizard.requiredMetaDataFilePathMsg"));
			}

		} while(validFile == false);

		//出力先ディレクトリ
		boolean validExportDir = false;
		do {
			String exportDirName = readConsole(rs("ExportMetaDataNameList.Wizard.inputDirMsg") + "(" + param.getExportDirName() + ")");
			if (StringUtil.isNotBlank(exportDirName)) {
				param.setExportDirName(exportDirName);
			}

			//チェック
			File exportDir = new File(param.getExportDirName());
			if (!exportDir.exists()) {
				exportDir.mkdir();
				logInfo(rs("ExportMetaDataNameList.Wizard.createdInputDirMsg", param.getExportDirName()));
			}
			if (!exportDir.isDirectory()) {
				logWarn(rs("ExportMetaDataNameList.Wizard.notDirMsg", param.getExportDirName()));
			} else {
				param.setExportDir(exportDir);
				validExportDir = true;
			}
		} while(validExportDir == false);

		//出力ファイル名
		String exportFileName = readConsole(rs("ExportMetaDataNameList.Wizard.inputFileNameMsg") + "(" + param.getExportFileName() + ")");
		if (StringUtil.isNotBlank(exportFileName)) {
			param.setExportFileName(exportFileName);
		}

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//Import処理実行
		return executeTask(param, (paramA) -> {
			return executeExport(paramA);
		});
	}

	private String getFileExtension(File file) {
		String fileName = file.getName();
		if(fileName.lastIndexOf(".") > 0) {
			return fileName.substring(fileName.lastIndexOf(".")+1);
		} else {
			return "";
		}
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
