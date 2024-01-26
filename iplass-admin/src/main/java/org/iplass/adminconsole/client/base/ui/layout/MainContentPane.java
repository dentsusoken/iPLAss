/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.base.ui.layout;

import org.iplass.adminconsole.client.metadata.ui.MetaDataPluginSection;
import org.iplass.adminconsole.client.tools.ui.ToolsPluginSection;

import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;

public class MainContentPane extends HLayout {

	private MetaDataPluginSection metaSection;
	private ToolsPluginSection toolSection;
	private MainWorkspaceTab workspaceTab;

	public MainContentPane() {

		setWidth100();
		setHeight100();

		SectionStack navi = new SectionStack();
		navi.setVisibilityMode(VisibilityMode.MULTIPLE);
		navi.setWidth(250);
		navi.setHeight100();
		navi.setShowResizeBar(true);

		metaSection = new MetaDataPluginSection();
		metaSection.setExpanded(true);
		navi.addSection(metaSection);

		toolSection = new ToolsPluginSection();
		toolSection.setExpanded(true);
		navi.addSection(toolSection);

		workspaceTab = MainWorkspaceTab.getInstance();
		workspaceTab.setWidth100();

		addMember(navi);
		addMember(workspaceTab);

	}

	@Override
	protected void onDestroy() {

		metaSection.destroy();
		toolSection.destroy();
		workspaceTab.destroy();
	}

}
