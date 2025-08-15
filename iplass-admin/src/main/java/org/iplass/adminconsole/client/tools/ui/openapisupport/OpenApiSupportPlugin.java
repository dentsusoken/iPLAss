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
package org.iplass.adminconsole.client.tools.ui.openapisupport;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.DefaultAdminPlugin;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;

/**
 * OpenAPI(Swagger)Support ツールプラグイン
 *
 * @author SEKIGUCHI Naoya
 */
public class OpenApiSupportPlugin extends DefaultAdminPlugin {

	/** ノード表示名 */
	private static final String NODE_NAME = "OpenApiSupport";

	/** ノードタイプ  */
	private static final String NODE_TYPE = "OpenApiSupport";

	/** ノードアイコン */
	private static final String NODE_ICON = "brick.png";

	/** コンテキストメニュー */
	private Menu contextMenu;

	/** メインペーン */
	private OpenApiSupportMainPane mainPane;

	@Override
	public AdminMenuTreeNode createPluginRootNode() {
		return new AdminMenuTreeNode(NODE_NAME, NODE_ICON, NODE_TYPE);
	}

	@Override
	public void onNodeDoubleClick(AdminMenuTreeNode node) {
		if (NODE_TYPE.equals(node.getType())) {
			addTab(node);
		}
	}

	@Override
	public void onNodeContextClick(final AdminMenuTreeNode node) {
		if (NODE_TYPE.equals(node.getType())) {
			if (contextMenu == null) {
				String menuItemLabel = AdminClientMessageUtil.getString("ui_tools_openapisupport_OpenApiSupportPlugin_startMenuLabel");
				MenuItem execMenuItem = new MenuItem(menuItemLabel, NODE_ICON);
				execMenuItem.addClickHandler(event -> {
					addTab(node);
				});

				contextMenu = new Menu();
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
		addOpenApiSupportTab();
	}

	/**
	 * OpenAPI(Swagger)Support タブを開きます。
	 */
	private void addOpenApiSupportTab() {
		if (mainPane == null || !workspace.existTab(NODE_NAME, NODE_NAME)) {
			mainPane = new OpenApiSupportMainPane(this);
		}

		workspace.addTab(NODE_NAME, NODE_ICON, NODE_NAME, mainPane);
	}
}
