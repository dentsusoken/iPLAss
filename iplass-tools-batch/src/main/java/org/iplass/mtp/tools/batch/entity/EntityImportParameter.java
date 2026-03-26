/*
 * Copyright (C) 2021 DENTSU SOKEN INC. All Rights Reserved.
 *
 * Unless you have purchased a commercial license,
 * the following license terms apply:
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.iplass.mtp.tools.batch.entity;

import java.io.File;

import org.iplass.mtp.impl.tools.entityport.EntityDataImportCondition;

public class EntityImportParameter {

	/** テナントURL */
	public static final String PROP_TENANT_URL = "tenantUrl";
	/** テナントID */
	public static final String PROP_TENANT_ID = "tenantId";
	/** Entity名 */
	public static final String PROP_ENTITY_NAME = "entityName";
	/** インポートファイル */
	public static final String PROP_IMPORT_FILE = "importFile";
	/** BinaryデータをImportするか */
	public static final String PROP_IMPORT_BINARY_DATA = "importBinaryData";

	/** Silentモード Entityデータ Truncate */
	public static final String PROP_ENTITY_TRUNCATE = "entity.truncate";
	/** Silentモード Entityデータ bulkUpdate */
	public static final String PROP_ENTITY_BULK_UPDATE = "entity.bulkUpdate";
	/** Silentモード Entityデータ Listenerを実行 */
	public static final String PROP_ENTITY_NOTIFY_LISTENER = "entity.notifyListener";
	/** Silentモード Entityデータ Validationを実行(更新不可項目を対象にする場合はfalseに強制設定) */
	public static final String PROP_ENTITY_WITH_VALIDATION = "entity.withValidation";
	/** Silentモード Entityデータ 更新不可項目を更新対象 */
	public static final String PROP_ENTITY_UPDATE_DISUPDATABLE = "entity.updateDisupdatableProperty";
	/** Silentモード Entityデータ InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定 */
	public static final String PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION = "entity.insertEnableAuditPropertySpecification";
	/** Silentモード Entityデータ InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定時に実行するユーザーID */
	public static final String PROP_ENTITY_INSERT_AUDIT_PROPERTY_SPECIFICATION_EXEC_USER_ID = "entity.insertEnableAuditPropertySpecificationUserId";
	/** Silentモード Entityデータ InsertするEntityにcreateBy,createDate,updateBy,updateDateの値を指定時に実行するユーザーPW */
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

	//Entity名
	private String entityName;

	//Importファイルパス
	private String importFilePath;

	//Fileロケール名
	private String locale;

	//Fileタイムゾーン名
	private String timezone;

	//Importファイル(内部用)
	private File importFile;
	
	//BinaryデータをImportするか
	private boolean isImportBinaryData;


	//EntityImport用条件
	private EntityDataImportCondition entityImportCondition;

	public EntityImportParameter(int tenantId, String tenantName) {
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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getImportFilePath() {
		return importFilePath;
	}

	public void setImportFilePath(String importFilePath) {
		this.importFilePath = importFilePath;
	}
	
	public boolean isImportBinaryData() {
		return isImportBinaryData;
	}

	public void setImportBinaryData(boolean isImportBinaryData) {
		this.isImportBinaryData = isImportBinaryData;
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

	public File getImportFile() {
		return importFile;
	}

	public void setImportFile(File importFile) {
		this.importFile = importFile;
	}

	public EntityDataImportCondition getEntityImportCondition() {
		return entityImportCondition;
	}

	public void setEntityImportCondition(EntityDataImportCondition entityImportCondition) {
		this.entityImportCondition = entityImportCondition;
	}

}
