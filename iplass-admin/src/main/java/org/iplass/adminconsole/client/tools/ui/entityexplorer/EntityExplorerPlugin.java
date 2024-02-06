/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.entityexplorer;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.ViewEntityDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.DefaultAdminPlugin;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;


public class EntityExplorerPlugin extends DefaultAdminPlugin {

	/** ノード表示名 */
	private static final String NODE_NAME = "EntityExplorer";

	/** ノードタイプ  */
	private static final String NODE_TYPE = "EntityExplorer";

	/** ノードアイコン */
	public static final String NODE_ICON = "database_gear.png";

	private Menu contextMenu;

	private int tabCount = 0;

	private AdminMenuTreeNode node;

	public EntityExplorerPlugin() {

		//ViewEntityDataEventの登録
		AdminConsoleGlobalEventBus.addHandler(ViewEntityDataEvent.TYPE, new ViewEntityDataEvent.ViewEntityDataHandler() {
			@Override
			public void onViewEntityData(ViewEntityDataEvent event) {
				showDataListPane(event.getEntityName());
			}
		});
	}

	@Override
	public AdminMenuTreeNode createPluginRootNode() {
		node = new AdminMenuTreeNode(NODE_NAME, NODE_ICON, NODE_TYPE);
		return node;
	}

	@Override
	public void onNodeDoubleClick(AdminMenuTreeNode node) {
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
				MenuItem execMenuItem = new MenuItem(AdminClientMessageUtil.getString("ui_tools_entityexplorer_EntityExplorerPluginManager_startEntityExplorer"), NODE_ICON);
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

	private void showDataListPane(String defName) {
		EntityExplorerMainPane explorer = addTab(node);
		explorer.showDataListPane(defName);
	}

	/**
	 * <p>タブを追加します。</p>
	 *
	 * @param node 選択Node
	 */
	private EntityExplorerMainPane addTab(AdminMenuTreeNode node) {
		tabCount++;
		EntityExplorerMainPane explorer = new EntityExplorerMainPane(tabCount, this);
		workspace.addTab(node.getName(), node.getIcon(), String.valueOf(tabCount), explorer);

		return explorer;
	}

	/**
	 * タブ名を変更します。
	 *
	 * @param tabNum EntityExplorer内のタブ番号
	 * @param name 設定値（Entity名）
	 */
	public void setWorkspaceTabName(int tabNum, String name) {
		String id = workspace.getTabID(NODE_NAME, String.valueOf(tabNum));
		if (name == null || name.isEmpty()) {
			workspace.setTabName(id, NODE_ICON, NODE_NAME);
		} else {
			workspace.setTabName(id, NODE_ICON, NODE_NAME + ":" + name);
		}
	}

}
