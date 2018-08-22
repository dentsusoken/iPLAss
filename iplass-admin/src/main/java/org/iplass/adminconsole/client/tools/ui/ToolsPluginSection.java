/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.base.ui.widget.HasDestroy;

import com.smartgwt.client.widgets.layout.SectionStackSection;

public class ToolsPluginSection extends SectionStackSection implements HasDestroy {

	private ToolsPluginTreeGrid toolsTreeGrid;

	public ToolsPluginSection() {

		setTitle("Tools");

		toolsTreeGrid = ToolsPluginTreeGrid.getInstance();
		toolsTreeGrid.setWidth100();
		toolsTreeGrid.setHeight(200);	//メニューが少ないので。ただしスクロールがでない・・・。toolsTreeGrid.setOverflow(Overflow.AUTO)を指定してもだめ。
		addItem(toolsTreeGrid);
	}

	@Override
	public void destroy() {
		toolsTreeGrid.destroy();
	}

}
