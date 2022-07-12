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

public class EntityDeleteAllParameter {

	/** テナントID */
	public static final String PROP_TENANT_ID = "tenantId";
	/** Entity名 */
	public static final String PROP_ENTITY_NAME = "entityName";

	/** Where条件 */
	public static final String PROP_ENTITY_WHERE_CLAUSE = "entity.whereClause";

	// テナントID
	private int tenantId;

	// テナント名
	private String tenantName;
	
	//Entity名
	private String entityName;
	
	// Where条件
	private String whereClause;

	//Listnerを実行する
	private boolean notifyListeners = true;
	
	// Commit単位(件数)
	private Integer commitLimit = 100;

	public EntityDeleteAllParameter(int tenantId, String tenantName) {
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
	
	public String getWhereClause() {
		return whereClause;
	}

	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public boolean isNotifyListeners() {
		return notifyListeners;
	}
	public void setNotifyListeners(boolean notifyListeners) {
		this.notifyListeners = notifyListeners;
	}
	
	public Integer getCommitLimit() {
		return commitLimit;
	}
	public void setCommitLimit(Integer commitLimit) {
		this.commitLimit = commitLimit;
	}

}
