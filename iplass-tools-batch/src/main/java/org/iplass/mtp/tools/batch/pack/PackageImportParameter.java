/*
 * Copyright 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
 */

package org.iplass.mtp.tools.batch.pack;

import java.io.File;

import org.iplass.mtp.impl.tools.entityport.EntityDataImportCondition;
import org.iplass.mtp.impl.tools.pack.PackageInfo;
import org.iplass.mtp.impl.util.InternalDateUtil;
import org.iplass.mtp.tenant.Tenant;

public class PackageImportParameter {

	// テナントID
	private int tenantId;

	// テナント名
	private String tenantName;

	//Importファイルパス
	private String importFilePath;

	//Workディレクトリ名
	private String workDirName = "./";

	//Upload用Package名(固定)
	private String packageName;

	//Fileロケール名
	private String locale;

	//Fileタイムゾーン名
	private String timezone;

	//作成者
	private String registId = "program";

	//Importファイル(内部用)
	private File importFile;

	//Workディレクトリ(内部用)
	private File workDir;

	//ImportファイルPackage情報(内部用)
	private PackageInfo packInfo;

	//Importテナント情報(内部用)
	private Tenant importTenant;

	//EntityImport用条件
	private EntityDataImportCondition entityImportCondition;

	public PackageImportParameter(int tenantId, String tenantName) {
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

	public String getImportFilePath() {
		return importFilePath;
	}

	public void setImportFilePath(String importFilePath) {
		this.importFilePath = importFilePath;
	}

	public String getWorkDirName() {
		return workDirName;
	}

	public void setWorkDirName(String workDirName) {
		this.workDirName = workDirName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}


	public String getRegistId() {
		return registId;
	}

	public void setRegistId(String registId) {
		this.registId = registId;
	}

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	public File getWorkDir() {
		return workDir;
	}

	public void setWorkDir(File workDir) {
		this.workDir = workDir;
	}

	public PackageInfo getPackInfo() {
		return packInfo;
	}

	public void setPackInfo(PackageInfo packInfo) {
		this.packInfo = packInfo;
	}

	public EntityDataImportCondition getEntityImportCondition() {
		return entityImportCondition;
	}

	public void setEntityImportCondition(EntityDataImportCondition entityImportCondition) {
		this.entityImportCondition = entityImportCondition;
	}

	public Tenant getImportTenant() {
		return importTenant;
	}

	public void setImportTenant(Tenant importTenant) {
		this.importTenant = importTenant;
	}

}
