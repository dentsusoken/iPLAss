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

package org.iplass.adminconsole.client.metadata.ui.top.node;

import org.iplass.mtp.view.top.parts.ApplicationMaintenanceParts;
import org.iplass.mtp.view.top.parts.CsvDownloadSettingsParts;
import org.iplass.mtp.view.top.parts.FulltextSearchViewParts;
import org.iplass.mtp.view.top.parts.PreviewDateParts;
import org.iplass.mtp.view.top.parts.UserMaintenanceParts;

import com.google.gwt.core.shared.GWT;
import com.smartgwt.client.widgets.tree.Tree;

/**
 *
 * @author lis3wg
 */
public class ToolbarNodeManager extends ItemNodeManager {

	private ToolbarNodeManagerController controller = GWT.create(ToolbarNodeManagerController.class);


	@Override
	public String getName() {
		return "Toolbar Parts";
	}

	@Override
	public void loadChild(Tree tree, TopViewNode parent) {
		TopViewNode[] nodes = controller.getNodes();
		tree.addList(nodes, parent);
	}


	public interface ToolbarNodeManagerController {
		TopViewNode[] getNodes();
	}

	public static class ToolbarNodeManagerControllerImpl implements ToolbarNodeManagerController {
		@Override
		public TopViewNode[] getNodes() {
			TopViewNode previewDate = new TopViewNode("Preview Date", PreviewDateParts.class.getName(), "", true, false, true);
			TopViewNode userMaintenance = new TopViewNode("User Maintenance", UserMaintenanceParts.class.getName(), "", true, false, true);
			TopViewNode appMaintenance = new TopViewNode("Application Maintenance", ApplicationMaintenanceParts.class.getName(), "", true, false, true);
			TopViewNode fulltextSearchView = new TopViewNode("Fulltext Search", FulltextSearchViewParts.class.getName(), "", true, false, true);
			TopViewNode csvDownloadSettingsView = new TopViewNode("CsvDownload Settings", CsvDownloadSettingsParts.class.getName(), "", true, false, true);
			return new TopViewNode[]{previewDate, userMaintenance, appMaintenance, fulltextSearchView, csvDownloadSettingsView};
		}
	}
}
