/*
 * Copyright 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadata;

import java.io.File;


public class MetaDataNameListExportParameter {

	//メタデータファイルパス
	private String metadataFilePath;

	//出力先ディレクトリ名
	private String exportDirName = "./";

	//出力ファイル名
	private String exportFileName = "metadata_namelist";

	//出力先ディレクトリ(内部用)
	private File exportDir;

	public MetaDataNameListExportParameter() {
	}

	public String getMetaDataFilePath() {
		return metadataFilePath;
	}

	public void setMetaDataFilePath(String importFilePath) {
		this.metadataFilePath = importFilePath;
	}

	public String getExportDirName() {
		return exportDirName;
	}

	public void setExportDirName(String exportDirName) {
		this.exportDirName = exportDirName;
	}

	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public File getExportDir() {
		return exportDir;
	}

	public void setExportDir(File exportDir) {
		this.exportDir = exportDir;
	}
}
