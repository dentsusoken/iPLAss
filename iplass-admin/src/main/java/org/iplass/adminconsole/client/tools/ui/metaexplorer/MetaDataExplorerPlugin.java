/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.ui.metaexplorer;

import java.util.LinkedHashMap;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.ViewMetaDataStatusCheckResultEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.DefaultAdminPlugin;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;

import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;


public class MetaDataExplorerPlugin extends DefaultAdminPlugin {

	/** ノード表示名 */
	private static final String NODE_NAME = "MetaDataExplorer";

	/** ノードタイプ  */
	private static final String NODE_TYPE = "MetaDataExplorer";

	/** ノードアイコン */
	private static final String NODE_ICON = "cog.png";

	private Menu contextMenu;

	private MetaDataExplorerMainPane mainPane;

	public MetaDataExplorerPlugin() {

		//ViewMetaDataErrorEventの登録
		AdminConsoleGlobalEventBus.addHandler(ViewMetaDataStatusCheckResultEvent.TYPE, new ViewMetaDataStatusCheckResultEvent.ViewMetaDataStatusCheckResultHandler() {

			@Override
			public void onViewMetaDataStatusCheckResult(ViewMetaDataStatusCheckResultEvent event) {
				showMetaDataStatusErrorList(event.getResult());
			}
		});
	}

	@Override
	public AdminMenuTreeNode createPluginRootNode() {
		AdminMenuTreeNode node = new AdminMenuTreeNode(NODE_NAME, NODE_ICON, NODE_TYPE);
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
				MenuItem execMenuItem = new MenuItem(AdminClientMessageUtil.getString("ui_tools_metaexplorer_MetaDataExplorerPluginManager_startMetaDataExplorer"), NODE_ICON);
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
		showMetaDataViewer();
	}

	private void showMetaDataViewer() {
		if (mainPane == null || !workspace.existTab(NODE_NAME, NODE_NAME)) {
			mainPane = new MetaDataExplorerMainPane(this);
		}

		workspace.addTab(NODE_NAME, NODE_ICON, NODE_NAME, mainPane);
	}

	private void showMetaDataStatusErrorList(LinkedHashMap<String, String> result) {

		//起動されていない場合はMetaDataExplolerを表示
		showMetaDataViewer();

		//既にエラーが表示されている場合は削除
		if (mainPane.getMember(0) instanceof StatusErrorListPane) {
			mainPane.removeMember(mainPane.getMember(0));
		}
		mainPane.addMember(new StatusErrorListPane(mainPane, result), 0);

	}

}
