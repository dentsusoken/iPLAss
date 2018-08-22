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

package org.iplass.adminconsole.client.tools.ui.pack;

import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Package Mainパネル
 */
public class PackageMainPane extends VLayout {

	@SuppressWarnings("unused")
	private PackagePlugin pluginManager;

	private PackageListPane listPane;

	/**
	 * コンストラクタ
	 */
	public PackageMainPane(PackagePlugin pluginManager) {
		this.pluginManager = pluginManager;

		//レイアウト設定
		setWidth100();

		listPane = new PackageListPane(this);

		addMember(listPane);
	}

//	public void showExplorer(String entityName) {
//		if (explorerPane == null) {
//			explorerPane = new EntityExplorerPane(tabNum, this);
//			addMember(explorerPane);
//		}
//		listPane.hide();
//		explorerPane.show();
//		explorerPane.setEntityName(entityName);
//	}
//
//	public void showEntityList() {
//		explorerPane.hide();
//		listPane.show();
//		listPane.refresh();
//		explorerPane.destroy();
//		explorerPane = null;
//	}
//
//	public void setWorkspaceTabName(int tabNum, String entityName) {
//		pluginManager.setWorkspaceTabName(tabNum, entityName);
//	}
}
