package org.iplass.mtp.tools.batch.storagespace;

import java.io.File;

public class ObjStoreDDLParameter {

	/**
	 * <p>Template(gtlファイル)格納パス</p>
	 *
	 * <p>oracle、mysqlそれぞれに存在する</p>
	 */
	private String templateRootPath = "";

	/** <p>Partitionを利用するか</p> */
	private boolean usePartition = true;

	/**
	 * <p>作成対象のstorageSpaceName</p>
	 *
	 * <p>
	 * service-configに定義されているもの。
	 * 複数同時に作成する場合はカンマで区切る。
	 * 未指定の場合、またはallとした場合は全てを対象にする
	 * </p>
	 * */
	private String[] storageSpaceName = {};

	/**
	 * <p>DDL出力パス</p>
	 *
	 * <p>未指定の場合は標準出力に出力します。</p>
	 */
	private String outputPath = "./../ddl";


	//Templateディレクトリ(内部用)
	private File templateRootDir;

	//出力ディレクトリ(内部用)
	private File outputDir;

	public String getTemplateRootPath() {
		return templateRootPath;
	}

	public void setTemplateRootPath(String templateRootPath) {
		this.templateRootPath = templateRootPath;
	}

	public boolean isUsePartition() {
		return usePartition;
	}

	public void setUsePartition(boolean usePartition) {
		this.usePartition = usePartition;
	}

	public String[] getStorageSpaceName() {
		return storageSpaceName;
	}

	public void setStorageSpaceName(String[] storageSpaceName) {
		this.storageSpaceName = storageSpaceName;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public File getTemplateRootDir() {
		return templateRootDir;
	}

	public void setTemplateRootDir(File templateRootDir) {
		this.templateRootDir = templateRootDir;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

}
