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

package org.iplass.adminconsole.client.tools.ui;

import java.util.List;

import org.iplass.adminconsole.client.base.plugin.AdminPlugin;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeGrid;
import org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab;

import com.google.gwt.core.client.GWT;

/**
 * <p>Tools用メニューグリッドです。</p>
 *
 * @author lis70i
 */
public class ToolsPluginTreeGrid extends AdminMenuTreeGrid {

	private static ToolsPluginTreeGrid instance;

	/**
	 * インスタンスを返します。
	 *
	 * @return ToolsTreeGrid
	 */
	public static ToolsPluginTreeGrid getInstance() {
		if (instance == null) {
			instance = new ToolsPluginTreeGrid(MainWorkspaceTab.getInstance());
		}
		return instance;
	}

	/**
	 * コンストラクタ
	 *
	 * @param mainPane
	 */
	private ToolsPluginTreeGrid(MainWorkspaceTab mainPane) {
		super(mainPane);
	}

	@Override
	protected List<AdminPlugin> plugins() {

		ToolsPluginController controller = GWT.create(ToolsPluginController.class);
		return controller.plugins();
	}

}
