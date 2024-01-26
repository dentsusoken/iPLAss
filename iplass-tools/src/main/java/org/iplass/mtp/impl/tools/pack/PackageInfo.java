/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.pack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.tenant.Tenant;

public class PackageInfo implements Serializable {

	private static final long serialVersionUID = 8448254032674205351L;

	private String packageName;

	private List<String> metaDataPaths;

	private Tenant tenant;

	private boolean warningTenant;

	private List<String> entityPaths;

	private boolean hasLobData;

	/**
	 * @return packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName セットする packageName
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * @return metaDataPaths
	 */
	public List<String> getMetaDataPaths() {
		return metaDataPaths;
	}

	/**
	 * @param metaDataPaths セットする metaDataPaths
	 */
	public void setMetaDataPaths(List<String> metaDataPaths) {
		this.metaDataPaths = metaDataPaths;
	}

	/**
	 * @return tenant
	 */
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * @param tenant セットする tenant
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public boolean isWarningTenant() {
		return warningTenant;
	}

	public void setWarningTenant(boolean warningTenant) {
		this.warningTenant = warningTenant;
	}

	/**
	 * @return entityPaths
	 */
	public List<String> getEntityPaths() {
		return entityPaths;
	}

	/**
	 * @param entityPaths セットする entityPaths
	 */
	public void setEntityPaths(List<String> entityPaths) {
		this.entityPaths = entityPaths;
	}

	public void addEntityPaths(String entityPath) {
		if (entityPaths == null) {
			entityPaths = new ArrayList<>();
		}
		entityPaths.add(entityPath);
	}

	/**
	 * @return hasLobData
	 */
	public boolean isHasLobData() {
		return hasLobData;
	}

	/**
	 * @param hasLobData セットする hasLobData
	 */
	public void setHasLobData(boolean hasLobData) {
		this.hasLobData = hasLobData;
	}

}
