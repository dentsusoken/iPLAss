/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.client.tools.ui.entityexplorer.datalist.EntityDataListMainPane;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Entity Explorer Mainパネル
 */
public class EntityExplorerMainPane extends VLayout {

	private int tabNum;
	private EntityExplorerPlugin pluginManager;

	private TabSet tabSet;

	private EntityDataListMainPane dataListPane;

	private EntityExplorerMainPaneController controller = GWT.create(EntityExplorerMainPaneController.class);

	/**
	 * コンストラクタ
	 *
	 * @param tabNum タブ番号（複数起動対応）
	 */
	public EntityExplorerMainPane(int tabNum, EntityExplorerPlugin pluginManager) {
		this.tabNum = tabNum;
		this.pluginManager = pluginManager;

		//レイアウト設定
		setWidth100();

		tabSet = new TabSet();
		tabSet.setTabBarPosition(Side.TOP);
		addMember(tabSet);

		setupEntityList();

		controller.setupSubModuleTab(this, tabSet);
//		setupAuditLogList();
//		setupCrawlList();
//		setupDefragList();
	}

	public void showDataListPane(String defName) {
		if (dataListPane != null) {
			dataListPane.showDataListPane(defName);
		}
	}

	private void setupEntityList() {
		Tab dataListTab = new Tab("Entity List");
		dataListPane = new EntityDataListMainPane(this);
		dataListTab.setPane(dataListPane);
		tabSet.addTab(dataListTab);
	}

//	private void setupAuditLogList() {
//		Tab auditLogListTab = new Tab("Audit Log");
//		EntityAuditLogMainPane auditLogPane = new EntityAuditLogMainPane(this);
//		auditLogListTab.setPane(auditLogPane);
//		tabSet.addTab(auditLogListTab);
//	}
//
//	private void setupCrawlList() {
//
//		EntityCrawlMainPane crawlPane = new EntityCrawlMainPane(EntityExplorerMainPane.this);
//
//		final Tab crawlTab = new Tab("Entity Crawl");
//		crawlTab.setPane(crawlPane);
//		crawlTab.setDisabled(true);
//
//		crawlTab.addTabSelectedHandler(new TabSelectedHandler() {
//
//			@Override
//			public void onTabSelected(TabSelectedEvent event) {
//				crawlPane.selectedPane();
//			}
//		});
//		tabSet.addTab(crawlTab);
//
//		//利用可否チェック
//		FulltextSearchAdminServiceAsync service = GWT.create(FulltextSearchAdminService.class);
//		service.isUseFulltextSearch(TenantInfoHolder.getId(), new AsyncCallback<Boolean>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				SC.say("failed","An error occurred in full-text search configuration loading." + caught.getMessage());
//				GWT.log(caught.toString(), caught);
//			}
//
//			@Override
//			public void onSuccess(Boolean result) {
//
//				if (result) {
//					tabSet.enableTab(crawlTab);
//				} else {
////					SC.say("failed","Set of full-text search has become a not available.");
////					GWT.log("Set of full-text search has become a not available.");
//					tabSet.removeTab(crawlTab);
//				}
//			}
//
//		});
//	}
//
//	private void setupDefragList() {
//		final EntityDefragMainPane mainPane = new EntityDefragMainPane(this);
//
//		Tab dataListTab = new Tab("Entity Defrag");
//		dataListTab.setPane(mainPane);
//
//		dataListTab.addTabSelectedHandler(new TabSelectedHandler() {
//
//			@Override
//			public void onTabSelected(TabSelectedEvent event) {
//				mainPane.selectedPane();
//			}
//		});
//		tabSet.addTab(dataListTab);
//	}

	public void setWorkspaceTabName(String entityName) {
		pluginManager.setWorkspaceTabName(tabNum, entityName);
	}
}
