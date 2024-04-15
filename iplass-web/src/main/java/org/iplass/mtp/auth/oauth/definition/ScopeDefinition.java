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
package org.iplass.mtp.auth.oauth.definition;

import java.io.Serializable;
import java.util.List;

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.definition.LocalizedStringDefinition;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * スコープの定義です。
 * 
 * @author K.Higuchi
 *
 */
@XmlSeeAlso({
	OIDCClaimScopeDefinition.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public class ScopeDefinition implements Serializable {
	private static final long serialVersionUID = 5164831280206763413L;

	private String name;
	private String displayName;
	private String description;
	
	private List<LocalizedStringDefinition> localizedDisplayNameList;
	private List<LocalizedStringDefinition> localizedDescriptionList;

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
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}
	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}
	public List<LocalizedStringDefinition> getLocalizedDescriptionList() {
		return localizedDescriptionList;
	}
	public void setLocalizedDescriptionList(List<LocalizedStringDefinition> localizedDescriptionList) {
		this.localizedDescriptionList = localizedDescriptionList;
	}

}
