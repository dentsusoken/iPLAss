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

	/** Silentモード テナントURL */
	public static final String PROP_TENANT_URL = "tenantUrl";
	/** Silentモード テナントID */
	public static final String PROP_TENANT_ID = "tenantId";

	/** Silentモード パッケージファイル */
	public static final String PROP_IMPORT_FILE = "importFile";

	/** Silentモード Entityデータ Truncate */
	public static final String PROP_ENTITY_TRUNCATE = "entity.truncate";
	/** Silentモード Entityデータ bulkUpdate */
	public static final String PROP_ENTITY_BULK_UPDATE = "entity.bulkUpdate";
	/** Silentモード Entityデータ Listnerを実行 */
	public static final String PROP_ENTITY_NOTIFY_LISTENER = "entity.notifyListener";
	/** Silentモード Entityデータ Validationを実行(更新不可項目を対象にする場合はfalseに強制設定) */
	public static final String PROP_ENTITY_WITH_VALIDATION = "entity.withValidation";
	/** Silentモード Entityデータ 更新不可項目を更新対象 */
	public static final String PROP_ENTITY_UPDATE_DISUPDATABLE = "entity.updateDisupdatableProperty";
	/** Silentモード Entityデータ InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定 */
	public static final String PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION = "entity.insertEnableAuditPropertySpecification";
	/** Silentモード Entityデータ InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定時に実行するユーザID */
	public static final String PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION_EXEC_USER_ID = "entity.insertEnableAuditPropertySpecificationUserId";
	/** Silentモード Entityデータ InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定時に実行するユーザPW */
	public static final String PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION_EXEC_USER_PW = "entity.insertEnableAuditPropertySpecificationUserPW";
	/** Silentモード Entityデータ 強制更新 */
	public static final String PROP_ENTITY_FORCE_UPDATE = "entity.forceUpdate";
	/** Silentモード Entityデータ エラーデータはSkip */
	public static final String PROP_ENTITY_ERROR_SKIP = "entity.errorSkip";
	/** Silentモード Entityデータ 存在しないプロパティは無視 */
	public static final String PROP_ENTITY_IGNORE_INVALID_PROPERTY = "entity.ignoreInvalidProperty";
	/** Silentモード Entityデータ OIDに付与するPrefix */
	public static final String PROP_ENTITY_PREFIX_OID = "entity.prefixOid";
	/** Silentモード Entityデータ Commit単位(件数) */
	public static final String PROP_ENTITY_COMMIT_LIMIT = "entity.commitLimit";

	// テナントID
	private int tenantId;

	// テナント名
	private String tenantName;

	//Importファイルパス
	private String importFilePath;

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
