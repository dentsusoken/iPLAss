/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.auth.builtin;

import org.iplass.adminconsole.client.base.plugin.DefaultAdminPlugin;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class BuiltinAuthExplorerPlugin extends DefaultAdminPlugin {

	/** ノード表示名 */
	private static final String NODE_NAME = "BuiltinAuthUserExplorer";

	/** ノードタイプ  */
	private static final String NODE_TYPE = "BuiltinAuthUserExplorer";

	/** ノードアイコン（TreeGridの場合[SKIN]だと「TreeGrid」が含まれるため[SKINIMG]を使用）  */
	private static final String NODE_ICON = "user.png";

	private Menu contextMenu;

	@Override
	public AdminMenuTreeNode createPluginRootNode() {
		AdminMenuTreeNode node = new AdminMenuTreeNode(NODE_NAME, NODE_ICON, NODE_TYPE);
		return node;
	}

	@Override
	public void onNodeDoubleClick(final AdminMenuTreeNode node) {
		if (NODE_TYPE.equals(node.getType())){
			addTab(node);
		}
	}

	@Override
	public void onNodeContextClick(final AdminMenuTreeNode node) {
		if (NODE_TYPE.equals(node.getType())){

			if (contextMenu == null) {
				contextMenu = new Menu();
				//contextMenu.setWidth(100);
				MenuItem execMenuItem = new MenuItem("show " + NODE_NAME, NODE_ICON);
				execMenuItem.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(MenuItemClickEvent event) {
						addTab(node);
					}
				});
				contextMenu.addItem(execMenuItem);
			}

			owner.setContextMenu(contextMenu);
		}
	}

	@Override
	public void onFolderOpened(AdminMenuTreeNode node) {
	}

	/**
	 * <p>タブを追加します。</p>
	 *
	 * @param node 選択Node
	 */
	private void addTab(AdminMenuTreeNode node) {
		workspace.addTab(node.getName(), node.getIcon(), "", new BuiltinAuthExplorerMainPane());
	}

}
