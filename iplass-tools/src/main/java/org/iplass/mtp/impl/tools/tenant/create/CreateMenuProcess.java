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

package org.iplass.mtp.impl.tools.tenant.create;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.definition.DefinitionModifyResult;
import org.iplass.mtp.impl.tools.ToolsResourceBundleUtil;
import org.iplass.mtp.impl.tools.tenant.TenantCreateParameter;
import org.iplass.mtp.impl.tools.tenant.log.LogHandler;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuItemManager;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.menu.MenuTreeManager;

public class CreateMenuProcess implements TenantCreateProcess {

	/** 初期メニュー用の定義名 */
	private static final String DEFAULT_MENU_TREE = "DEFAULT";

	/** 権限情報Node名	 */
	private static final String PERMISSION_NODE_NAME = "permission_node";

	/** 権限情報Node子アイテム名	 */
	private static final String[] PERMISSION_NODE_CHILDREN = {
		"mtp/auth/Role", "mtp/auth/ActionPermission", "mtp/auth/WebApiPermission", "mtp/auth/EntityPermission", "mtp/auth/WorkflowPermission", "mtp/auth/CubePermission"};

	/** 基本情報Node名	 */
	private static final String STANDARD_NODE_NAME = "standard_node";

	/** 基本情報Node子アイテム名	 */
	private static final String[] STANDARD_NODE_CHILDREN  = {
		"mtp/auth/Group", "mtp/auth/Rank", "mtp/auth/User"
	};

	/** 権限情報、基本情報以外のアイテム名	 */
	private static final String[] OTHER_ITEMS  = {
		"mtp/Information"
	};

	@Override
	public boolean execute(TenantCreateParameter param, LogHandler logHandler) {

		MenuItemManager mm = ManagerLocator.getInstance().getManager(MenuItemManager.class);
		MenuTreeManager mtm = ManagerLocator.getInstance().getManager(MenuTreeManager.class);

		MenuTree tree = new MenuTree();
		tree.setName(DEFAULT_MENU_TREE);

		MenuItem permissionItem = mm.get(PERMISSION_NODE_NAME);
		for (String childName : PERMISSION_NODE_CHILDREN) {
			MenuItem child = mm.get(childName);
			if (child != null) {
				permissionItem.addChild(child);
			}
		}
		tree.addMenuItem(permissionItem);

		MenuItem standardItem = mm.get(STANDARD_NODE_NAME);
		for (String childName : STANDARD_NODE_CHILDREN) {
			MenuItem child = mm.get(childName);
			if (child != null) {
				standardItem.addChild(child);
			}
		}
		tree.addMenuItem(standardItem);

		for (String itemName : OTHER_ITEMS) {
			MenuItem child = mm.get(itemName);
			if (child != null) {
				tree.addMenuItem(child);
			}
		}

		DefinitionModifyResult result = mtm.create(tree);
		if (!result.isSuccess()) {
			throw new ApplicationException(result.getMessage());
		}

		logHandler.info(ToolsResourceBundleUtil.resourceString(param.getLoggerLanguage(), "tenant.create.createdMenuMsg", tree.getName()));

		return true;
	}

}
