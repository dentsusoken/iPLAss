/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.adminconsole.client.tools.ui.entityexplorer.entityview.gem;

import org.iplass.adminconsole.client.tools.ui.entityexplorer.EntityExplorerMainPane;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.entityview.EntityExplorerViewPaneController;

import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

/**
 * EntityExplorerでGEMの画面定義タブを生成するController
 *
 * @author Y.Yasuda
 */
public class GemEntityExplorerViewPaneController implements EntityExplorerViewPaneController {

	@Override
	public void setupSubModuleTab(EntityExplorerMainPane owner, TabSet tabSet) {
		setupEntityViewList(owner, tabSet);
	}

	private void setupEntityViewList(final EntityExplorerMainPane owner, final TabSet tabSet) {
		final GemEntityViewMainPane mainPane = new GemEntityViewMainPane(owner);

		Tab entityViewListTab = new Tab("GEM Entity View");
		entityViewListTab.setPane(mainPane);

		entityViewListTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				mainPane.selectedPane();
			}
		});
		tabSet.addTab(entityViewListTab);
	}

}
