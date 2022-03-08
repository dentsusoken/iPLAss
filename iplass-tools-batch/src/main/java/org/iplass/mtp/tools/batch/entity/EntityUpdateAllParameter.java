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

import org.iplass.mtp.impl.tools.entity.EntityUpdateAllCondition;

public class EntityUpdateAllParameter {

	/** テナントURL */
	public static final String PROP_TENANT_URL = "tenantUrl";
	/** テナントID */
	public static final String PROP_TENANT_ID = "tenantId";

	// テナントID
	private int tenantId;

	// テナント名
	private String tenantName;

	//EntityExport用条件
	private EntityUpdateAllCondition entityUpdateAllCondition;

	public EntityUpdateAllParameter(int tenantId, String tenantName) {
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

	public EntityUpdateAllCondition getEntityUpdateAllCondition() {
		return entityUpdateAllCondition;
	}

	public void setEntityUpdateAllCondition(EntityUpdateAllCondition entityUpdateAllCondition) {
		this.entityUpdateAllCondition = entityUpdateAllCondition;
	}

}
