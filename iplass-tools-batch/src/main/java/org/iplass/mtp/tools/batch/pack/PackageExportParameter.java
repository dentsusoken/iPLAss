/*
 * Copyright 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.pack;

import java.io.File;
import java.util.List;

import org.iplass.mtp.impl.util.InternalDateUtil;

public class PackageExportParameter {

	/** Silentモード テナントURL */
	public static final String PROP_TENANT_URL = "tenantUrl";
	/** Silentモード テナントID */
	public static final String PROP_TENANT_ID = "tenantId";
	/** Silentモード 出力先ディレクトリ */
	public static final String PROP_EXPORT_DIR = "exportDir";
	/** Silentモード パッケージ名 */
	public static final String PROP_PACKAGE_NAME = "packageName";
	/** Silentモード メタデータ 出力 */
	public static final String PROP_META_EXPORT = "meta.export";
	/** Silentモード メタデータ ローカルのみ */
	public static final String PROP_META_LOCAL_ONLY = "meta.localOnly";
	/** Silentモード メタデータ 対象Path、複数ある場合はカンマ区切り、未指定の場合は全て */
	public static final String PROP_META_SOURCE = "meta.source";
	/** Silentモード メタデータ 対象を指定しない場合にテナントメタデータを除外するか */
	public static final String PROP_META_EXCLUDE_TENANT = "meta.excludeTenant";
	/** Silentモード Entityデータ 出力 */
	public static final String PROP_ENTITY_EXPORT = "entity.export";
	/** Silentモード Entityデータ 対象Name、複数ある場合はカンマ区切り、未指定の場合は全て */
	public static final String PROP_ENTITY_SOURCE = "entity.source";
	/** Silentモード Entityデータ 対象を指定しない場合にUserデータを除外するか */
	public static final String PROP_ENTITY_EXCLUDE_USER = "meta.excludeUser";
	/** Silentモード Packageを保存するか */
	public static final String PROP_SAVE_PACKAGE = "savePackage";

	// テナントID
	private int tenantId;

	// テナント名
	private String tenantName;

	//出力先ディレクトリ名
	private String exportDirName = "./";

	//パッケージ名
	private String packageName;

	//メタデータの出力設定
	private boolean exportMetaData = false;

	//LocalメタデータのみExportする
	private boolean exportLocalMetaDataOnly = true;

	//全てのメタデータをExportする
	private boolean exportAllMetaData = true;

	//メタデータ全てExport時にTenantを含めるか
	private boolean exportTenantMetaData = false;

	//個別指定のExportメタデータパス(mtp.*,samples.Sample01,sample2.sub.*)
	private String exportMetaDataPathStr;

	//Entityデータの出力設定
	private boolean exportEntityData = false;

	//全てのEntityデータをExportする
	private boolean exportAllEntityData = true;

	//Entityデータ全てExport時にUserを含めるか
	private boolean exportUserEntityData = false;

	//個別指定のExportEntityパス(mtp.*,samples.Sample01,sample2.sub.*)
	private String exportEntityDataPathStr;

	//Packageを保存するか
	private boolean savePackage = true;

	//作成者
	private String registId = "program";

	//出力先ディレクトリ(内部用)
	private File exportDir;

	//出力メタデータパスのリスト(内部用)
	private List<String> exportMetaDataPathList = null;
	//出力Entityデータパスのリスト(内部用)
	private List<String> exportEntityDataPathList = null;

	public PackageExportParameter(int tenantId, String tenantName) {
		this.setTenantId(tenantId);
		this.setTenantName(tenantName);

		packageName = tenantId + "_" + tenantName + "_" + InternalDateUtil.formatYYYY_MM_DD(InternalDateUtil.getNow());
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

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public File getExportDir() {
		return exportDir;
	}

	public void setExportDir(File exportDir) {
		this.exportDir = exportDir;
	}

	public boolean isExportMetaData() {
		return exportMetaData;
	}

	public void setExportMetaData(boolean exportMetaData) {
		this.exportMetaData = exportMetaData;
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

	public boolean isExportEntityData() {
		return exportEntityData;
	}

	public void setExportEntityData(boolean exportEntityData) {
		this.exportEntityData = exportEntityData;
	}

	public boolean isExportAllEntityData() {
		return exportAllEntityData;
	}

	public void setExportAllEntityData(boolean exportAllEntityData) {
		this.exportAllEntityData = exportAllEntityData;
	}

	public boolean isExportUserEntityData() {
		return exportUserEntityData;
	}

	public void setExportUserEntityData(boolean exportUserEntityData) {
		this.exportUserEntityData = exportUserEntityData;
	}

	public String getExportEntityDataPathStr() {
		return exportEntityDataPathStr;
	}

	public void setExportEntityDataPathStr(String exportEntityDataPathStr) {
		this.exportEntityDataPathStr = exportEntityDataPathStr;
	}

	public boolean isSavePackage() {
		return savePackage;
	}

	public void setSavePackage(boolean savePackage) {
		this.savePackage = savePackage;
	}

	public String getRegistId() {
		return registId;
	}

	public void setRegistId(String registId) {
		this.registId = registId;
	}

	public List<String> getExportMetaDataPathList() {
		return exportMetaDataPathList;
	}

	public void setExportMetaDataPathList(List<String> exportMetaDataPathList) {
		this.exportMetaDataPathList = exportMetaDataPathList;
	}

	public List<String> getExportEntityDataPathList() {
		return exportEntityDataPathList;
	}

	public void setExportEntityDataPathList(List<String> exportEntityDataPathList) {
		this.exportEntityDataPathList = exportEntityDataPathList;
	}

}
