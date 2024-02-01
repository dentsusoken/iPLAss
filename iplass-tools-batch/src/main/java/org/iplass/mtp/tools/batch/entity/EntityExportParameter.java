/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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
	/** 出力先ディレクトリ */
	public static final String PROP_EXPORT_DIR = "exportDir";
	/** BinaryデータをExportするか */
	public static final String PROP_EXPORT_BINARY_DATA = "exportBinaryData";
	
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

	//出力先ディレクトリ名
	private String exportDirName = "./";

	//出力先ディレクトリ(内部用)
	private File exportDir;
	
	//BinaryデータをExportするか
	private boolean isExportBinaryData;

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

	public String getExportDirName() {
		return exportDirName;
	}

	public void setExportDirName(String exportDirName) {
		this.exportDirName = exportDirName;
	}

	public File getExportDir() {
		return exportDir;
	}

	public void setExportDir(File exportDir) {
		this.exportDir = exportDir;
	}

	public boolean isExportBinaryData() {
		return isExportBinaryData;
	}

	public void setExportBinaryData(boolean isExportBinaryData) {
		this.isExportBinaryData = isExportBinaryData;
	}

	public EntityDataExportCondition getEntityExportCondition() {
		return entityExportCondition;
	}

	public void setEntityExportCondition(EntityDataExportCondition entityExportCondition) {
		this.entityExportCondition = entityExportCondition;
	}

}
