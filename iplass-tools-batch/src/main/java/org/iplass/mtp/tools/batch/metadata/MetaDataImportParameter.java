/*
 * Copyright 2018 DENTSU SOKEN INC. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.metadata;

import java.io.File;
import java.util.List;

import org.iplass.mtp.tenant.Tenant;

public class MetaDataImportParameter {

	/** Silentモード テナントURL */
	public static final String PROP_TENANT_URL = "tenantUrl";
	/** Silentモード テナントID */
	public static final String PROP_TENANT_ID = "tenantId";

	/** Silentモード パッケージファイル */
	public static final String PROP_IMPORT_FILE = "importFile";

	/** Silentモード Entityメタデータのプロパティ整合性チェック時の確認メッセージを出すかどうか */
	public static final String PROP_MEAT_OUTPUT_CHECK_RESULT_CONFIRM = "meta.outputCheckResultConfirm";

	// テナントID
	private int tenantId;

	// テナント名
	private String tenantName;

	//Importファイルパス
	private String importFilePath;

	// Entityメタデータのプロパティ整合性チェック時の確認メッセージを出すかどうか
	private boolean outputCheckResultConfirm;

	//Importファイル(内部用)
	private File importFile;

	//対象メタデータ(内部用)
	private List<String> metaDataPaths;

	//Importテナント情報(内部用)
	private Tenant importTenant;

	//Importのテナントが警告だったか(内部用)
	private boolean warningTenant;

	public MetaDataImportParameter(int tenantId, String tenantName) {
		this.setTenantId(tenantId);
		this.setTenantName(tenantName);
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

	public String getImportFilePath() {
		return importFilePath;
	}

	public void setImportFilePath(String importFilePath) {
		this.importFilePath = importFilePath;
	}

	public boolean isOutputCheckResultConfirm() {
		return this.outputCheckResultConfirm;
	}

	public void setOutputCheckResultConfirm(boolean outputCheckResultConfirm) {
		this.outputCheckResultConfirm = outputCheckResultConfirm;
	}

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	public List<String> getMetaDataPaths() {
		return metaDataPaths;
	}

	public void setMetaDataPaths(List<String> metaDataPaths) {
		this.metaDataPaths = metaDataPaths;
	}

	public Tenant getImportTenant() {
		return importTenant;
	}

	public void setImportTenant(Tenant importTenant) {
		this.importTenant = importTenant;
	}

	public boolean isWarningTenant() {
		return warningTenant;
	}

	public void setWarningTenant(boolean warningTenant) {
		this.warningTenant = warningTenant;
	}

}
