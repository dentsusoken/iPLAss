/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth;

import java.util.List;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.auth.oauth.definition.OIDCClaimScopeDefinition;
import org.iplass.mtp.auth.oauth.definition.ScopeDefinition;
import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;

@XmlSeeAlso({MetaOIDCClaimScope.class})
public class MetaScope implements MetaData {
	private static final long serialVersionUID = -4509688467517539934L;

	private String name;
	private String displayName;
	private String description;
	
	private List<MetaLocalizedString> localizedDisplayNameList;
	private List<MetaLocalizedString> localizedDescriptionList;
	
	public MetaScope() {
	}
	
	public MetaScope(String name, String displayName, String description) {
		this.name = name;
		this.displayName = displayName;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MetaLocalizedString> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	public void setLocalizedDisplayNameList(List<MetaLocalizedString> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	public List<MetaLocalizedString> getLocalizedDescriptionList() {
		return localizedDescriptionList;
	}

	public void setLocalizedDescriptionList(List<MetaLocalizedString> localizedDescriptionList) {
		this.localizedDescriptionList = localizedDescriptionList;
	}

	@Override
	public MetaScope copy() {
		return ObjectUtil.deepCopy(this);
	}
	
	public static MetaScope createInstance(ScopeDefinition definition) {
		if (definition instanceof OIDCClaimScopeDefinition) {
			return new MetaOIDCClaimScope();
		} else if (definition instanceof ScopeDefinition) {
			return new MetaScope();
		}
		return null;
	}
	
	public void applyConfig(ScopeDefinition def) {
		name = def.getName();
		displayName = def.getDisplayName();
		description = def.getDescription();
		localizedDisplayNameList = I18nUtil.toMeta(def.getLocalizedDisplayNameList());
		localizedDescriptionList = I18nUtil.toMeta(def.getLocalizedDescriptionList());
	}
	public ScopeDefinition currentConfig() {
		ScopeDefinition def = new ScopeDefinition();
		fill(def);
		return def;
	}
	
	protected void fill(ScopeDefinition def) {
		def.setName(name);
		def.setDisplayName(displayName);
		def.setDescription(description);
		def.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));
		def.setLocalizedDescriptionList(I18nUtil.toDef(localizedDescriptionList));
	}

}
