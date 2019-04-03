/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.definition.Definition;

/**
 * OAuth2におけるResourceServerの定義です。
 * 
 * @author K.Higuchi
 *
 */
public class OAuthResourceServerDefinition implements Definition {
	private static final long serialVersionUID = -6012202693824050765L;

	private String name;
	private String displayName;
	private String description;
	private List<CustomTokenIntrospectorDefinition> customTokenIntrospectors;
	
	public List<CustomTokenIntrospectorDefinition> getCustomTokenIntrospectors() {
		return customTokenIntrospectors;
	}

	public void setCustomTokenIntrospectors(List<CustomTokenIntrospectorDefinition> customTokenIntrospectors) {
		this.customTokenIntrospectors = customTokenIntrospectors;
	}
	
	@Override
	public String getName() {
		return name;
	}

	/**
	 * nameがResourceServerのclient_idとなります。
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
