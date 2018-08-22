/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.dto.pack;

import java.io.Serializable;
import java.util.List;

import org.iplass.mtp.tenant.Tenant;

public class PackageEntryInfo implements Serializable {

	private static final long serialVersionUID = -3721271923335808120L;

	private List<String> metaDataPaths;

	private Tenant tenant;

	private boolean warningTenant;

	private List<String> entityPaths;

	private boolean hasLobData;

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
	 * @param warnTenant セットする tenant
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
