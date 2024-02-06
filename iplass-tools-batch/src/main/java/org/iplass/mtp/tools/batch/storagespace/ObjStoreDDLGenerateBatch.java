/*
 * Copyright 2015 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.storagespace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.datastore.StoreService;
import org.iplass.mtp.impl.datastore.grdb.GRdbDataStore;
import org.iplass.mtp.impl.datastore.grdb.RawColIndexType;
import org.iplass.mtp.impl.datastore.grdb.RawColType;
import org.iplass.mtp.impl.datastore.grdb.StorageSpaceMap;
import org.iplass.mtp.impl.script.GroovyScriptEngine;
import org.iplass.mtp.impl.script.ScriptService;
import org.iplass.mtp.impl.script.template.GroovyTemplate;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;
import org.iplass.mtp.impl.script.template.GroovyTemplateCompiler;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.tools.batch.ExecMode;
import org.iplass.mtp.tools.batch.MtpCuiBase;
import org.iplass.mtp.tools.batch.MtpBatchResourceDisposer;
import org.iplass.mtp.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom StorageSpace用のDDL生成バッチ
 */
public class ObjStoreDDLGenerateBatch extends MtpCuiBase {

	private static Logger logger = LoggerFactory.getLogger(ObjStoreDDLGenerateBatch.class);

	/** Templateファイル */
	private static String[] templates = {
		"obj_store_rb.gtl",
		"obj_store.gtl",
		"obj_ref_rb.gtl",
		"obj_ref.gtl",
		"obj_index_date.gtl",
		"obj_index_dbl.gtl",
		"obj_index_num.gtl",
		"obj_index_str.gtl",
		"obj_index_ts.gtl",
		"obj_unique_date.gtl",
		"obj_unique_dbl.gtl",
		"obj_unique_num.gtl",
		"obj_unique_str.gtl",
		"obj_unique_ts.gtl"
	};

	/** 圧縮形式 */
	private static String[] compressedFormats = {
		"zlib",
		"lz4",
		"none"
	};

	/** <p>実行モード</p> */
	private ExecMode execMode = ExecMode.WIZARD;

	/** 実行パラメータ */
	private ObjStoreDDLParameter parameter;

	private GRdbDataStore store = (GRdbDataStore)ServiceRegistry.getRegistry().getService(StoreService.class).getDataStore();

	/** カラム名小文字指定 */
	private boolean columnNameLowerCase = false;

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tmplRootPath
	 * args[2]・・・outputRootPath
	 * args[3]・・・storageSpaceName
	 * args[4]・・・partition
	 * args[5]・・・compressedFormat
	 **/
	public static void main(String[] args) throws Exception {

		ObjStoreDDLGenerateBatch instance = null;
		try {
			instance = new ObjStoreDDLGenerateBatch(args);
			instance.execute();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			MtpBatchResourceDisposer.disposeResource();
		}
	}

	/**
	 * args[0]・・・execMode
	 * args[1]・・・tmplRootPath
	 * args[2]・・・outputRootPath
	 * args[3]・・・storageSpaceName
	 * args[4]・・・partition
	 * args[5]・・・compressedFormat
	 **/
	public ObjStoreDDLGenerateBatch(String... args) throws Exception {

		if (args != null) {

			if (args.length > 0) {
				execMode = ExecMode.valueOf(args[0]);
			}

			parameter = new ObjStoreDDLParameter();

			if (args.length > 1) {
				parameter.setTemplateRootPath(args[1]);
			}
			if (args.length > 2) {
				parameter.setOutputPath(args[2]);
			}
			if (args.length > 3) {
				if (!args[3].equalsIgnoreCase("all")) {
					String[] spaceNames = args[3].split(",");
					for (int i =0; i < spaceNames.length; i++) {
						spaceNames[i] = spaceNames[i].trim();
					}
					parameter.setStorageSpaceName(spaceNames);
				}
			}
			if (args.length > 4) {
				parameter.setUsePartition("TRUE".equalsIgnoreCase(args[4]));
			} else if (getConfigSetting().isSQLServer()) {
				// SQL Serverの場合、パーティションの利用はデフォルトではしない
				parameter.setUsePartition(false);
			} else if (getConfigSetting().isPostgreSQL()) {
				// PostgreSQLの場合、パーティションの利用はデフォルトではしない
				parameter.setUsePartition(false);
			}
			if (args.length > 5) {
				if (getConfigSetting().isMySQL()) {
					//指定された圧縮形式であればtrue、それ以外はfalse
					if (Arrays.asList(compressedFormats).contains(args[5])) {
						parameter.setUseCompression(true);
						parameter.setCompressedFormat(args[5]);
					} else {
						parameter.setUseCompression(false);
					}
				} else {
					// MySQL以外の場合、ページ圧縮は利用しない
					parameter.setUseCompression(false);
				}
			}
		}

		// PostgreSQLの場合はカラム名は小文字にする
		if (getConfigSetting().isPostgreSQL()) {
			columnNameLowerCase = true;
		}
	}

	/**
	 * モードに合わせて実行します。
	 *
	 * @return
	 */
	public boolean execute() throws Exception {

		clearLog();

		//Console出力
		switchLog(true, false);

		//環境情報出力
		logEnvironment();

		switch (execMode) {
		case WIZARD :
			logInfo("■Start Wizard");
			logInfo("");

			//Wizardの実行
			return startWizard();
		case SILENT :
			logInfo("■Start Silent");
			logInfo("");

			//Silentの場合はConsole出力を外す
			switchLog(false, true);

			//mainで引数としてObjStoreDDLParameterが生成されている前提
			return executeTask(null, (param) -> {
				return generate();
			});
		default :
			logError("unsupport execute mode : " + execMode);
			return false;
		}
	}

	private boolean generate() {

		GroovyScriptEngine gse = (GroovyScriptEngine) ServiceRegistry.getRegistry().getService(ScriptService.class).createScriptEngine();

		setSuccess(false);

		try {

			for (String tmplName: templates) {
				GroovyTemplate tmpl = getTemplate(tmplName, gse);

				try(Writer w = getWriter(tmplName.replace(".gtl", ".sql"))) {
					for (StorageSpaceMap e: store.getStorageSpaceMap().values()) {
						if (parameter.getStorageSpaceName() == null || contains(parameter.getStorageSpaceName(), e.getStorageSpaceName())) {
							List<String> allPostFix = e.allTableNamePostfix();
							List<Col> cols = toCol(e);
							for (String pf: allPostFix) {
								Map<String, Object> bindings = new HashMap<>();
								if (pf != null) {
									bindings.put("tableNamePostfix", "__" + pf);
								} else {
									bindings.put("tableNamePostfix", "");
								}

								bindings.put("columns", cols);
								bindings.put("partition", parameter.isUsePartition() && !e.isCustomPartition());
								bindings.put("compression", parameter.isUseCompression());
								bindings.put("compressedFormat", parameter.getCompressedFormat());
								GroovyTemplateBinding gtb = new GroovyTemplateBinding(w, bindings);

								tmpl.doTemplate(gtb);
								w.append(System.lineSeparator());
							}
						}
					}
					w.flush();
				}
			}

			setSuccess(true);

		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			ExecuteContext.initContext(null);
		}

		return isSuccess();
	}

	private GroovyTemplate getTemplate(String name, GroovyScriptEngine gse) throws Exception {
		try (FileInputStream is = new FileInputStream(new File(parameter.getTemplateRootPath(), name))) {
			String str = IOUtils.toString(is, "UTF-8");
			return GroovyTemplateCompiler.compile(str, name, gse);
		}
	}

	private Writer getWriter(String name) throws Exception {
		if (parameter.getOutputPath() != null) {
			File root = new File(parameter.getOutputPath());
			if (!root.exists()) {
				root.mkdirs();
			}
			File f = new File(parameter.getOutputPath(), name);
			if (f.exists()) {
				f.delete();
			}
			f.createNewFile();
			return new FileWriter(f);
		} else {
			return new PrintWriter(System.out) {
				@Override
				public void close() {
					//標準出力の場合は閉じない
					flush();
				}
			};
		}
	}

	private boolean contains(String[] list, String target) {
		for (int i = 0; i < list.length; i++) {
			if (list[i].equals(target)) {
				return true;
			}
		}
		return false;
	}

	private void setVector(Col[] cols, RawColType type, RawColIndexType indexType, double distance, int targetMaxCol) {
		for (int i = 1;  i <= targetMaxCol; i++) {
			cols[(int) Math.round(i * distance) - 1] = new Col(type, indexType, columnNameLowerCase ? type.getColNamePrefix(indexType).toLowerCase() : type.getColNamePrefix(indexType), i);
		}
	}

	private List<Col> toCol(StorageSpaceMap e) {
		List<Col> res = new ArrayList<>();
		int maxCol = e.maxColumns();
		RawColType[] types = RawColType.values();
		RawColIndexType[] indexTypes = new RawColIndexType[]{RawColIndexType.UNIQUE_INDEX, RawColIndexType.INDEX, RawColIndexType.NONE};
		//なるべく均等になるように配置
		Col[][][] matrix = new Col[types.length][indexTypes.length][maxCol];
		for (int i = 0; i < types.length; i++) {
			for (int j = 0; j < indexTypes.length; j++) {
				int targetMaxCol = types[i].getMaxCol(e, indexTypes[j]);

				double distance = 0;
				if (targetMaxCol != 0) {
					distance = maxCol/targetMaxCol;
				}
				setVector(matrix[i][j], types[i], indexTypes[j], distance, targetMaxCol);
			}
		}

		for (int j = 0; j < indexTypes.length; j++) {
			for (int i = 0; i < maxCol; i++) {
				for (int k = 0; k < types.length; k++) {
					if (matrix[k][j][i] != null) {
						res.add(matrix[k][j][i]);
					}
				}
			}
		}

		return res;
	}

	private boolean startWizard() throws Exception {

		ObjStoreDDLParameter param = new ObjStoreDDLParameter();

		//Templateディレクトリ
		boolean validTemplateFile = false;
		do {
			//デフォルトを02_tools/templateとして想定
			String defaultPath = "./../template/" + (getConfigSetting().isOracle() ? "oracle" : getConfigSetting().isSQLServer() ? "sqlserver" : getConfigSetting().isPostgreSQL() ? "postgresql" : "mysql");
			String templatePath = readConsole(rs("ObjStoreDDLGenerator.Wizard.templateDirMsg") + "(" + defaultPath + ")");
			if (StringUtil.isNotBlank(templatePath)) {
				param.setTemplateRootPath(templatePath);
			} else {
				param.setTemplateRootPath(defaultPath);
			}

			//チェック
			File templateDir = new File(param.getTemplateRootPath());
			if (!templateDir.exists()) {
				logWarn(rs("ObjStoreDDLGenerator.Wizard.notExistsDirMsg", templateDir.getAbsolutePath()));
			} else if (!templateDir.isDirectory()) {
				logWarn(rs("ObjStoreDDLGenerator.Wizard.notDirMsg", templateDir.getAbsolutePath()));
			} else {
				param.setTemplateRootDir(templateDir);
				validTemplateFile = true;
			}
		} while(validTemplateFile == false);

		//出力先ディレクトリ
		boolean validOutputFile = false;
		do {
			String outputPath = readConsole(rs("ObjStoreDDLGenerator.Wizard.outputDirMsg") + "(" + param.getOutputPath() + ")");
			if (StringUtil.isNotBlank(outputPath)) {
				param.setOutputPath(outputPath);
			}

			//チェック
			File outputDir = new File(param.getOutputPath());
			if (!outputDir.exists()) {
				outputDir.mkdir();
				logInfo(rs("ObjStoreDDLGenerator.Wizard.createdOutputDirMsg", param.getOutputPath()));
			}
			if (!outputDir.isDirectory()) {
				logWarn(rs("ObjStoreDDLGenerator.Wizard.notDirMsg", param.getOutputPath()));
			} else {
				param.setOutputDir(outputDir);
				validOutputFile = true;
			}
		} while(validOutputFile == false);

		//StorageSpace名
		boolean validStorageSpaceName = false;
		List<StorageSpaceMap> ssList = new ArrayList<>();
		do {
			String storageSpaceName = readConsole(rs("ObjStoreDDLGenerator.Wizard.storageSpaceNameMsg") + "(" + "" + ")");
			if (StringUtil.isNotBlank(storageSpaceName)) {
				param.setStorageSpaceName(storageSpaceName.split(","));

				//チェック
				if (param.getStorageSpaceName() != null) {
					boolean existsName = false;
					for (String target : param.getStorageSpaceName()) {
						existsName = false;
						for (StorageSpaceMap e: store.getStorageSpaceMap().values()) {
							if (target.equals(e.getStorageSpaceName())) {
								existsName = true;
								ssList.add(e);
								break;
							}
						}
						if (!existsName) {
							logWarn(rs("ObjStoreDDLGenerator.Wizard.notStorageSpaceMsg", target));
							break;
						}
					}
					validStorageSpaceName = existsName;
				}

			} else {
				param.setStorageSpaceName(null);
				validStorageSpaceName = true;
			}

		} while(validStorageSpaceName == false);

		//partition
		boolean checkPartition = false;
		if (ssList.isEmpty()) {
			//全部が対象の場合は確認する
			checkPartition = true;
		} else {
			for (StorageSpaceMap e : ssList) {
				if (!e.isCustomPartition()) {
					//標準のものが１つでもあれば確認する
					checkPartition = true;
					break;
				}
			}
		}
		if (checkPartition) {
			if (getConfigSetting().isPostgreSQL()) {
				// PostgreSQLの場合、パーティションの利用はデフォルトではしない
				param.setUsePartition(false);
			}
			if (getConfigSetting().isSQLServer()) {
				// SQL Serverの場合、パーティションの利用はデフォルトではしない
				param.setUsePartition(false);
			}
			boolean usePartition = readConsoleBoolean(rs("ObjStoreDDLGenerator.Wizard.confirmPartitionMsg"), param.isUsePartition());
			param.setUsePartition(usePartition);
		} else {
			//標準のものがないのでPartitionは利用しない
			param.setUsePartition(false);
		}

		//compression
		if (getConfigSetting().isMySQL()) {

			String compressedFormat = readConsole(rs("ObjStoreDDLGenerator.Wizard.confirmCompressionMsg"));

			if (StringUtil.isNotBlank(compressedFormat)) {
				param.setCompressedFormat(compressedFormat);
			}

			//指定された圧縮形式であればtrue、それ以外はfalse
			if (Arrays.asList(compressedFormats).contains(compressedFormat)) {
				param.setUseCompression(true);
			} else {
				param.setUseCompression(false);
			}

		} else {
			//MySQL以外の場合、ページ圧縮は利用しない
			param.setUseCompression(false);
		}

		this.parameter = param;

		//Consoleを削除してLogに切り替え
		switchLog(false, true);

		//Export処理実行
		return executeTask(null, (paramA) -> {
			return generate();
		});
	}

	public static class Col {
		String type;
		String indexType;
		String prefix;
		int no;

		public Col(RawColType type, RawColIndexType indexType, String prefix, int no) {
			this.type = type.toString();
			this.indexType = indexType.toString();
			this.prefix = prefix;
			this.no = no;
		}
	}

	@Override
	protected Logger loggingLogger() {
		return logger;
	}
}
