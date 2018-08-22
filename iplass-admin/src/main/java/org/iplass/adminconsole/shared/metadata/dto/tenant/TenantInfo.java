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

package org.iplass.adminconsole.shared.metadata.dto.tenant;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.gem.Skin;
import org.iplass.gem.Theme;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.tenant.Tenant;

public class TenantInfo implements Serializable{

	private static final long serialVersionUID = -6875426740802399867L;

	private Tenant tenant;

	private List<Skin> skins;

	private List<Theme> themes;

	private LinkedHashMap<String, String> enableLanguageMap;

	private DefinitionEntry tenantEntry;

	/**
	 * tenantを取得します。
	 * @return tenant
	 */
	public Tenant getTenant() {
	    return tenant;
	}

	/**
	 * tenantを設定します。
	 * @param tenant tenant
	 */
	public void setTenant(Tenant tenant) {
	    this.tenant = tenant;
	}

	/**
	 * skinsを取得します。
	 * @return skins
	 */
	public List<Skin> getSkins() {
		return skins;
	}

	/**
	 * skinsを設定します。
	 * @param skins skins
	 */
	public void setSkins(List<Skin> skins) {
		this.skins = skins;
	}

	/**
	 * themesを取得します。
	 * @return themes
	 */
	public List<Theme> getThemes() {
		return themes;
	}

	/**
	 * themesを設定します。
	 * @param themes themes
	 */
	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public LinkedHashMap<String, String> getEnableLanguageMap() {
		return enableLanguageMap;
	}

	public void setEnableLanguageMap(LinkedHashMap<String, String> enableLanguageMap) {
		this.enableLanguageMap = enableLanguageMap;
	}

	public DefinitionEntry getTenantEntry() {
		return tenantEntry;
	}

	public void setTenantEntry(DefinitionEntry tenantEntry) {
		this.tenantEntry = tenantEntry;
	}

}
