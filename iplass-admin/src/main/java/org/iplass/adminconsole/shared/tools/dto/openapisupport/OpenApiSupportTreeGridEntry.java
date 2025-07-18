/*
 * Copyright (C) 2025 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.adminconsole.shared.tools.dto.openapisupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class OpenApiSupportTreeGridEntry implements Serializable {
	private String name;

	private String path;

	private boolean isFolder;

	private List<OpenApiSupportTreeGridEntry> children = new ArrayList<>();
	private Map<String, OpenApiSupportTreeGridEntry> nameChildrenMap = new HashMap<>();

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path セットする path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return isFolder
	 */
	public boolean isFolder() {
		return isFolder;
	}

	/**
	 * @param isFolder セットする isFolder
	 */
	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public void addChild(OpenApiSupportTreeGridEntry child) {
		children.add(child);
	}

	public OpenApiSupportTreeGridEntry getChildFolder(String name) {
		for (var child : children) {
			if (child.getName().equals(name) && child.isFolder()) {
				return child;
			}
		}
		return null;
	}

	/**
	 * @return children
	 */
	public List<OpenApiSupportTreeGridEntry> getChildren() {
		return new ArrayList<>(children);
	}
}
