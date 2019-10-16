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

package org.iplass.adminconsole.client.base.ui.layout;

import com.smartgwt.client.widgets.tree.TreeNode;

public class AdminMenuTreeNode extends TreeNode {

	public static final String ATTRIBUTE_TYPE = "type";

	public AdminMenuTreeNode(String name, String icon, String type) {
		this(name, icon, type, false);
	}

	public AdminMenuTreeNode(String name, String icon, String type, Boolean canDrag) {
		this(name, icon, type, canDrag, new AdminMenuTreeNode[] {});
	}

	public AdminMenuTreeNode(String name, String icon, String type, Boolean canDrag, AdminMenuTreeNode... children) {

		setName(getSimpleName(name));
		setType(type);
		setChildren(children);
		if (icon != null) {
			setIcon(icon);
		}
		if (canDrag != null) {
			setCanDrag(canDrag);
		}
	}

	/**
	 * コピー用
	 * @param original
	 */
	public AdminMenuTreeNode(AdminMenuTreeNode original) {
		setName(original.getName());
		setType(original.getType());
		setIcon(original.getIcon());
		setCanDrag(original.getCanDrag());
	}

	public String getType() {
		return getAttribute(ATTRIBUTE_TYPE);
	}

	public void setType(String type) {
		setAttribute(ATTRIBUTE_TYPE, type);
	}

	protected String getSimpleName(String name) {
		if (name.contains("/")) {
			if (name.lastIndexOf("/") < name.length()) {
				return name.substring(name.lastIndexOf("/") + 1);
			} else {
				return name.substring(name.lastIndexOf("/"));
			}
		} else {
			return name;
		}
	}

}
