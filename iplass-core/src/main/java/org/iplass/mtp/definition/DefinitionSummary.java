/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.definition;

import java.io.Serializable;

/**
 * Definitionの概要を表すクラスです。
 * name、およびdisplayName（表示名）、descriptionなどを保持します。
 *
 * @author K.Higuchi
 *
 */
public class DefinitionSummary implements Serializable {
	private static final long serialVersionUID = 251841465062661676L;

	private String path;
	private String name;
	private String displayName;
	private String description;

	public DefinitionSummary() {
	}

	public DefinitionSummary(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}

	public DefinitionSummary(String path, String name, String displayName, String description) {
		this.path = path;
		this.name = name;
		this.displayName = displayName;
		this.description = description;
	}

	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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

}
