/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui;

import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;

public class MetaDataItemMenuTreeNode extends AdminMenuTreeNode {

	public static final String ATTRIBUTE_DEF_NAME = "defName";
	public static final String ATTRIBUTE_DEF_PATH = "defPath";

	public static final String ATTRIBUTE_HOVER = "hover";
	public static final String ATTRIBUTE_HOVER_LOAD = "hoverLoaded";

	public static final String ATTRIBUTE_SHARED = "shared";
	public static final String ATTRIBUTE_SHARED_OVERWRITE = "sharedOverwrite";
	public static final String ATTRIBUTE_SHARABLE = "sharable";
	public static final String ATTRIBUTE_OVERWRITABLE = "overwritable";
	public static final String ATTRIBUTE_DATA_SHARABLE = "dataSharable";
	public static final String ATTRIBUTE_PERMISSION_SHARABLE = "permissionSharable";
	public static final String ATTRIBUTE_CAN_DELETE = "canDelete";
	public static final String ATTRIBUTE_CAN_RENAME = "canRename";

	public static final String ATTRIBUTE_DEF_CLASS_NAME = "defClassName";

	public MetaDataItemMenuTreeNode(String name,
			String path, String icon, String type,
			boolean shared, boolean sharedOverrite,
			boolean sharable, boolean overwritable,
			boolean dataSharable, boolean permissionSharable) {

		//Drag可能
		super(name, icon, type, true);

		//pathが指定されている場合は、pathを利用
		String workName = (path != null && !path.isEmpty() ? path : name);
		setName(getSimpleName(workName));

		setDefName(name);
		setDefPath(path);
		setHoverLoaded(false);

		setShared(shared);
		setSharedOverwrite(sharedOverrite);
		setSharable(sharable);
		setOverwritable(overwritable);
		setDataSharable(dataSharable);
		setPermissionSharable(permissionSharable);

		refreshStatus();
	}

	/**
	 * コピー用
	 * @param original
	 */
	public MetaDataItemMenuTreeNode(MetaDataItemMenuTreeNode original, boolean copySharedSettings) {
		super(original);

		setDefName(original.getDefName());
		setDefPath(original.getDefPath());
		setHoverLoaded(false);

		if (copySharedSettings) {
			setShared(original.isShared());
			setSharedOverwrite(original.isSharedOverwrite());
			setSharable(original.isSharable());
			setOverwritable(original.isOverwritable());
			setDataSharable(original.isDataSharable());
			setPermissionSharable(original.isPermissionSharable());

			refreshStatus();
		}

	}

	public void refreshStatus() {
		//Sharedなどの設定が変更された場合にcanRenameなどのステータスを最新にする

		boolean canDelete = !isShared();
		setCanDelete(canDelete);

		boolean canRename = !isShared() && !isSharedOverwrite()
				&& !(isSharable() && isOverwritable());	//共有可能でかつOverwrite可能の場合は不可
		setCanRename(canRename);
	}

	public String getDefName() {
		return getAttribute(ATTRIBUTE_DEF_NAME);
	}

	public void setDefName(String defName) {
		setAttribute(ATTRIBUTE_DEF_NAME, defName);
	}

	public String getDefPath() {
		return getAttribute(ATTRIBUTE_DEF_PATH);
	}

	public void setDefPath(String path) {
		setAttribute(ATTRIBUTE_DEF_PATH, path);
	}

	public String getHover() {
		return getAttribute(ATTRIBUTE_HOVER);
	}

	public void setHover(String hover) {
		setAttribute(ATTRIBUTE_HOVER, hover);
	}

	public void setHoverLoaded(boolean isHoverLoaded) {
		setAttribute(ATTRIBUTE_HOVER_LOAD, isHoverLoaded);
	}

	public boolean isHoverLoaded() {
		return getAttributeAsBoolean(ATTRIBUTE_HOVER_LOAD);
	}

	public boolean isShared() {
		if (getAttribute(ATTRIBUTE_SHARED) != null) {
			return getAttributeAsBoolean(ATTRIBUTE_SHARED);
		}
		return false;
	}
	public void setShared(boolean shared) {
		setAttribute(ATTRIBUTE_SHARED, shared);
	}

	public boolean isSharedOverwrite() {
		if (getAttribute(ATTRIBUTE_SHARED_OVERWRITE) != null) {
			return getAttributeAsBoolean(ATTRIBUTE_SHARED_OVERWRITE);
		}
		return false;
	}
	public void setSharedOverwrite(boolean sharedOverrite) {
		setAttribute(ATTRIBUTE_SHARED_OVERWRITE, sharedOverrite);
	}

	public boolean isSharable() {
		if (getAttribute(ATTRIBUTE_SHARABLE) != null) {
			return getAttributeAsBoolean(ATTRIBUTE_SHARABLE);
		}
		return false;
	}
	public void setSharable(boolean sharable) {
		setAttribute(ATTRIBUTE_SHARABLE, sharable);
	}

	public boolean isOverwritable() {
		if (getAttribute(ATTRIBUTE_OVERWRITABLE) != null) {
			return getAttributeAsBoolean(ATTRIBUTE_OVERWRITABLE);
		}
		return false;
	}
	public void setOverwritable(boolean overwritable) {
		setAttribute(ATTRIBUTE_OVERWRITABLE, overwritable);
	}

	public boolean isDataSharable() {
		if (getAttribute(ATTRIBUTE_DATA_SHARABLE) != null) {
			return getAttributeAsBoolean(ATTRIBUTE_DATA_SHARABLE);
		}
		return false;
	}
	public void setDataSharable(boolean dataSharable) {
		setAttribute(ATTRIBUTE_DATA_SHARABLE, dataSharable);
	}

	public boolean isPermissionSharable() {
		if (getAttribute(ATTRIBUTE_PERMISSION_SHARABLE) != null) {
			return getAttributeAsBoolean(ATTRIBUTE_PERMISSION_SHARABLE);
		}
		return false;
	}
	public void setPermissionSharable(boolean permissionSharable) {
		setAttribute(ATTRIBUTE_PERMISSION_SHARABLE, permissionSharable);
	}

	public boolean isCanDelete() {
		if (getAttribute(ATTRIBUTE_CAN_DELETE) != null) {
			return getAttributeAsBoolean(ATTRIBUTE_CAN_DELETE);
		}
		return false;
	}
	public void setCanDelete(boolean canDelete) {
		setAttribute(ATTRIBUTE_CAN_DELETE, canDelete);
	}

	public boolean isCanRename() {
		if (getAttribute(ATTRIBUTE_CAN_RENAME) != null) {
			return getAttributeAsBoolean(ATTRIBUTE_CAN_RENAME);
		}
		return false;
	}
	public void setCanRename(boolean canRename) {
		setAttribute(ATTRIBUTE_CAN_RENAME, canRename);
	}

	public String getDefinitionClassName() {
		if (getAttribute(ATTRIBUTE_DEF_CLASS_NAME) != null) {
			return getAttribute(ATTRIBUTE_DEF_CLASS_NAME);
		}
		return null;
	}
	public void setDefinitionClassName(String definitionClassName) {
		setAttribute(ATTRIBUTE_DEF_CLASS_NAME, definitionClassName);
	}

}
