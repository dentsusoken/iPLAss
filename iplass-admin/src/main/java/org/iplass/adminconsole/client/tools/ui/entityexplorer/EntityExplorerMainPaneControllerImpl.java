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

package org.iplass.adminconsole.client.tools.ui.entityexplorer;

import org.iplass.adminconsole.client.base.screen.ScreenModuleBasedUIFactoryHolder;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.crawl.EntityCrawlMainPane;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.defrag.EntityDefragMainPane;
import org.iplass.adminconsole.client.tools.ui.entityexplorer.recycle.RecycleBinMainPane;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class EntityExplorerMainPaneControllerImpl implements EntityExplorerMainPaneController {

	@Override
	public void setupSubModuleTab(EntityExplorerMainPane owner, TabSet tabSet) {

		ScreenModuleBasedUIFactoryHolder.getFactory().createEntityExplorerViewPaneController().setupSubModuleTab(owner, tabSet);
		setupCrawlList(owner, tabSet);
		setupDefragList(owner, tabSet);
		setupRecycleBinList(owner, tabSet);
	}

	private void setupCrawlList(final EntityExplorerMainPane owner, final TabSet tabSet) {

		EntityCrawlMainPane crawlPane = new EntityCrawlMainPane(owner);

		final Tab crawlTab = new Tab("Entity Crawl");
		crawlTab.setPane(crawlPane);
		crawlTab.setDisabled(true);

		crawlTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				crawlPane.selectedPane();
			}
		});
		tabSet.addTab(crawlTab);

		//利用可否チェック
		EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();
		service.isUseFulltextSearch(TenantInfoHolder.getId(), new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say("failed","An error occurred in full-text search configuration loading." + caught.getMessage());
				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(Boolean result) {

				if (result) {
					tabSet.enableTab(crawlTab);
				} else {
//					SC.say("failed","Set of full-text search has become a not available.");
//					GWT.log("Set of full-text search has become a not available.");
					tabSet.removeTab(crawlTab);
				}
			}

		});
	}

	private void setupDefragList(final EntityExplorerMainPane owner, final TabSet tabSet) {
		final EntityDefragMainPane mainPane = new EntityDefragMainPane(owner);

		Tab dataListTab = new Tab("Entity Defrag");
		dataListTab.setPane(mainPane);

		dataListTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				mainPane.selectedPane();
			}
		});
		tabSet.addTab(dataListTab);
	}

	private void setupRecycleBinList(final EntityExplorerMainPane owner, final TabSet tabSet) {
		final RecycleBinMainPane mainPane = new RecycleBinMainPane(owner);

		Tab recyleBinTab = new Tab("Recycle Bin");
		recyleBinTab.setPane(mainPane);

		recyleBinTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				mainPane.selectedPane();
			}
		});
		tabSet.addTab(recyleBinTab);
	}
}
