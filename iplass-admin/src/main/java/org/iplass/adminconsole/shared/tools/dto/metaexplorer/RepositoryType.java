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

package org.iplass.adminconsole.shared.tools.dto.metaexplorer;

public enum RepositoryType {

	ALL("All", "All Meta"),
	SHARED("Shared", "Shared Meta"),
	LOCAL("Local", "Local Meta");

	private String typeName;
	private String displayName;
	private RepositoryType(String typeName, String displayName) {
		this.typeName = typeName;
		this.displayName = displayName;
	}

	public String typeName() {
		return typeName;
	}
	public String displayName() {
		return displayName;
	}

	public static RepositoryType valueOfTypeName(String typeName) {
		for (RepositoryType type : values()) {
			if (type.typeName.equals(typeName)) {
				return type;
			}
		}
		throw new IllegalArgumentException("typeName:" + typeName + " is not defined.");
	}
}
