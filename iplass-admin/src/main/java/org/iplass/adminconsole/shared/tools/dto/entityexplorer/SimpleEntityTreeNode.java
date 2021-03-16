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

package org.iplass.adminconsole.shared.tools.dto.entityexplorer;

import java.util.ArrayList;
import java.util.List;

public class SimpleEntityTreeNode extends SimpleEntityInfo {

	private static final long serialVersionUID = -9081264568050691891L;

	private String path;

	private List<SimpleEntityTreeNode> children;
	private List<SimpleEntityTreeNode> items;

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
	 * @return children
	 */
	public List<SimpleEntityTreeNode> getChildren() {
		return children;
	}

	/**
	 * @param children セットする children
	 */
	public void setChildren(List<SimpleEntityTreeNode> children) {
		this.children = children;
	}

	/**
	 * @param child 追加する child
	 */
	public void addChild(SimpleEntityTreeNode child) {
		if (children == null) {
			children = new ArrayList<>();
		}
		children.add(child);
	}

	/**
	 * @return items
	 */
	public List<SimpleEntityTreeNode> getItems() {
		return items;
	}

	/**
	 * @param items セットする items
	 */
	public void setItems(List<SimpleEntityTreeNode> items) {
		this.items = items;
	}

	/**
	 * @param item 追加する item
	 */
	public void addItem(SimpleEntityTreeNode item) {
		if (items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
	}

	public int getAllNodeCount() {
		int count = children != null ? children.size() : 0;
		return count + (items != null ? items.size() : 0);
	}

}
