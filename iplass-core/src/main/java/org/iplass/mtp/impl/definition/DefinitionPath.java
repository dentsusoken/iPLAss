/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.definition;

import org.iplass.mtp.definition.Definition;

public final class DefinitionPath {
	
	final Class<? extends Definition> type;
	final String path;
	public DefinitionPath(Class<? extends Definition> type, String path) {
		super();
		this.type = type;
		this.path = path;
	}
	public Class<? extends Definition> getType() {
		return type;
	}
	public String getPath() {
		return path;
	}

}
