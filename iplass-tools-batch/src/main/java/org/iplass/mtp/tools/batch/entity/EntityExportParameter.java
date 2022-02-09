/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.impl.tools.entityport.EntityDataExportCondition;

public class EntityExportParameter {

	/** テナントURL */
	public static final String PROP_TENANT_URL = "tenantUrl";
	/** テナントID */
	public static final String PROP_TENANT_ID = "tenantId";
	/** Entity名 */
	public static final String PROP_ENTITY_NAME = "entityName";
	/** エクスポートファイル */
	public static final String PROP_EXPORT_FILE = "exportFile";
	
	/** Where条件 */
	public static final String PROP_ENTITY_WHERE_CLAUSE = "entity.whereClause";

	/** OrderBy条件 */
	public static final String PROP_ENTITY_ORDER_BY_CLAUSE = "entity.orderByClause";
	
	/** 全バージョン検索 */
	public static final String PROP_ENTITY_VERSIONED = "entity.versioned";

	// テナントID
	private int tenantId;

	// テナント名
	private String tenantName;

	//Entity名
	private String entityName;

	//Exportファイル名
	private String exportFileName;

	//Exportファイル(内部用)
	private File exportFile;

	//EntityExport用条件
	private EntityDataExportCondition entityExportCondition;

	public EntityExportParameter(int tenantId, String tenantName) {
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

	public String getExportFileName() {
		return exportFileName;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public File getExportFile() {
		return exportFile;
	}

	public void setExportFile(File exportFile) {
		this.exportFile = exportFile;
	}

	public EntityDataExportCondition getEntityExportCondition() {
		return entityExportCondition;
	}

	public void setEntityExportCondition(EntityDataExportCondition entityExportCondition) {
		this.entityExportCondition = entityExportCondition;
	}

}
