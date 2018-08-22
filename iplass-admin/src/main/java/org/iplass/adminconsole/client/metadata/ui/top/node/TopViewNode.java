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

package org.iplass.adminconsole.client.metadata.ui.top.node;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 *
 * @author lis3wg
 */
public class TopViewNode extends TreeNode {

	private static final String PARTS_ICON = "plugin.png";

	public TopViewNode(String displayName) {
		this(displayName, null, null, false, false, false);
		setIcon(null);
	}
	public TopViewNode(String displayName, String type, String defName, boolean isParts, boolean isWidget, boolean isUnique) {
//		setName(displayName);
		setIcon(PARTS_ICON);
		setType(type);
		setDefName(defName);
		setParts(isParts);
		setWidget(isWidget);
		setUnique(isUnique);
		if (isUnique) setKey(type + "_" + defName);

		String titleInfo = "";
		if (isWidget) {
			titleInfo += "W";
		}
		if (isParts) {
			titleInfo += "P";
		}
		if (isUnique) {
			titleInfo += "U";
		}
		if (titleInfo.isEmpty()) {
			setName(displayName);
		} else {
			setName(displayName + "(" + titleInfo + ")");
		}
	}

	public void setType(String type) {
		setAttribute("type", type);
	}
	public String getType() {
		return getAttributeAsString("type");
	}

	public void setDefName(String defName) {
		setAttribute("defName", defName);
	}
	public String getDefName() {
		return getAttributeAsString("defName");
	}

	public boolean isFolder() {
		return getAttributeAsBoolean("isFolder");
	}

	public void setWidget(boolean isWidget) {
		setAttribute("isWidget", isWidget);
	}
	public boolean isWidget() {
		return getAttributeAsBoolean("isWidget");
	}

	public void setParts(boolean isParts) {
		setAttribute("isParts", isParts);
	}
	public boolean isParts() {
		return getAttributeAsBoolean("isParts");
	}

	public void setUnique(boolean isUnique) {
		setAttribute("isUnique", isUnique);
	}
	public boolean isUnique() {
		return getAttributeAsBoolean("isUnique");
	}

	public void setKey(String key) {
		setAttribute("key", key);
	}
	public String getKey() {
		return getAttributeAsString("key");
	}
}
