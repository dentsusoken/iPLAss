/*
 * Copyright 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadata;

import java.io.File;
import java.util.List;

import org.iplass.mtp.impl.util.InternalDateUtil;

public class MetaDataExportParameter {

	/** Silentモード テナントURL */
	public static final String PROP_TENANT_URL = "tenantUrl";
	/** Silentモード テナントID */
	public static final String PROP_TENANT_ID = "tenantId";
	/** Silentモード 出力先ディレクトリ */
	public static final String PROP_EXPORT_DIR = "exportDir";
	/** Silentモード ファイル名 */
	public static final String PROP_FILE_NAME = "fileName";
	/** Silentモード メタデータ ローカルのみ */
	public static final String PROP_META_LOCAL_ONLY = "meta.localOnly";
	/** Silentモード メタデータ 対象Path、複数ある場合はカンマ区切り、未指定の場合は全て */
	public static final String PROP_META_SOURCE = "meta.source";
	/** Silentモード メタデータ 対象を指定しない場合にテナントメタデータを除外するか */
	public static final String PROP_META_EXCLUDE_TENANT = "meta.excludeTenant";

	// テナントID
	private int tenantId;

	// テナント名
	private String tenantName;

	//出力先ディレクトリ名
	private String exportDirName = "./";

	//ファイル名
	private String fileName;

	//LocalメタデータのみExportする
	private boolean exportLocalMetaDataOnly = true;

	//全てのメタデータをExportする
	private boolean exportAllMetaData = true;

	//メタデータ全てExport時にTenantを含めるか
	private boolean exportTenantMetaData = false;

	//個別指定のExportメタデータパス(mtp.*,samples.Sample01,sample2.sub.*)
	private String exportMetaDataPathStr;

	//出力先ディレクトリ(内部用)
	private File exportDir;

	//出力メタデータパスのリスト(内部用)
	private List<String> exportMetaDataPathList = null;

	public MetaDataExportParameter(int tenantId, String tenantName) {
		this.setTenantId(tenantId);
		this.setTenantName(tenantName);

		fileName = tenantId + "_" + tenantName + "_metadata_" + InternalDateUtil.formatYYYY_MM_DD(InternalDateUtil.getNow());
	}

	public int getTenantId() {
		return tenantId;
	}

	public void setTenantId(int tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getExportDirName() {
		return exportDirName;
	}

	public void setExportDirName(String exportDirName) {
		this.exportDirName = exportDirName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getExportDir() {
		return exportDir;
	}

	public void setExportDir(File exportDir) {
		this.exportDir = exportDir;
	}

	public boolean isExportLocalMetaDataOnly() {
		return exportLocalMetaDataOnly;
	}

	public void setExportLocalMetaDataOnly(boolean exportLocalMetaDataOnly) {
		this.exportLocalMetaDataOnly = exportLocalMetaDataOnly;
	}

	public boolean isExportAllMetaData() {
		return exportAllMetaData;
	}

	public void setExportAllMetaData(boolean exportAllMetaData) {
		this.exportAllMetaData = exportAllMetaData;
	}

	public boolean isExportTenantMetaData() {
		return exportTenantMetaData;
	}

	public void setExportTenantMetaData(boolean exportTenantMetaData) {
		this.exportTenantMetaData = exportTenantMetaData;
	}

	public String getExportMetaDataPathStr() {
		return exportMetaDataPathStr;
	}

	public void setExportMetaDataPathStr(String exportMetaDataPathStr) {
		this.exportMetaDataPathStr = exportMetaDataPathStr;
	}

	public List<String> getExportMetaDataPathList() {
		return exportMetaDataPathList;
	}

	public void setExportMetaDataPathList(List<String> exportMetaDataPathList) {
		this.exportMetaDataPathList = exportMetaDataPathList;
	}

}
