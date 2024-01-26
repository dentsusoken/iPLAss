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

package org.iplass.adminconsole.client.metadata.ui.tenant;

import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.ContentSelectedEvent;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceAsync;
import org.iplass.adminconsole.shared.base.rpc.tenant.TenantServiceFactory;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.mtp.tenant.Tenant;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;


public class TenantPlugin extends DefaultMetaDataPlugin {

	/** ノード名 */
	private static final String NODE_NAME = "Tenant";

	/** ノードアイコン（TreeGridの場合[SKIN]だと「TreeGrid」が含まれるため[SKINIMG]を使用）  */
	private static final String NODE_ICON = "house.png";

	/** 対象テナントDefinition名 */
	private String defName;

	private TenantServiceAsync service = TenantServiceFactory.get();

	private TenantPluginController controller = GWT.create(TenantPluginController.class);

	public TenantPlugin() {
		isInitializeNode = true;
	}

	@Override
	protected String nodeName() {
		return NODE_NAME;
	}

	@Override
	protected String nodeDisplayName() {
		return AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPluginManager_tenant");
	}

	@Override
	protected String nodeIcon() {
		return NODE_ICON;
	}

	@Override
	protected String definitionClassName() {
		return Tenant.class.getName();
	}

	/**
	 * <p>ルートノードがダブルクリックされた際にタブを追加します。</p>
	 */
	@Override
	public void onNodeDoubleClick(AdminMenuTreeNode node) {
		if (isNodeTypeRoot(node)){
//			addTab(node);
			addTenantTab();
		}
	}

	/**
	 * <p>ルートノードに対して、「開く」コンテキストメニューを表示します。</p>
	 */
	@Override
	public void onNodeContextClick(final AdminMenuTreeNode node) {
		if (isNodeTypeRoot(node)){

			if (rootContextMenu == null) {
				rootContextMenu = new Menu();
				MenuItem execMenuItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPluginManager_openTenant"), NODE_ICON);
				execMenuItem.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(MenuItemClickEvent event) {
//						addTab(node);
						addTenantTab();
					}
				});
				rootContextMenu.addItem(execMenuItem);
			}

			owner.setContextMenu(rootContextMenu);
		}
	}

	/**
	 * <p>検索処理がないため、空実装</p>
	 */
	@Override
	protected void searchMetaNode(final AsyncCallback<MetaTreeNode> callback) {
	}

	/**
	 * <p>追加処理がないため、空実装</p>
	 */
	@Override
	protected void itemCreateAction(String folderPath) {
	}

	/**
	 * <p>コピー処理がないため、空実装</p>
	 */
	@Override
	protected void itemCopyAction(MetaDataItemMenuTreeNode itemNode) {
	}

	/**
	 * <p>削除処理がないため、空実装</p>
	 */
	@Override
	protected void itemDelete(MetaDataItemMenuTreeNode itemNode) {
	}

	@Override
	protected MetaDataMainEditPane workSpaceContents(MetaDataItemMenuTreeNode itemNode) {
		return new TenantMainPane(itemNode, this);
	}

	@Override
	protected Class<?>[] workspaceContentsPaneClass() {
		return new Class[]{TenantMainPane.class};
	}

	/**
	 * <p>（カスタマイズ）Tenant、TenantAvailableをサポートする</p>
	 */
	@Override
	public boolean isEditSupportMetaData(ViewMetaDataEvent event) {
		return controller.isEditSupportMetaData(event);
	}

	/**
	 * <p>（カスタマイズ）Tenant、TenantAvailableをサポートする</p>
	 */
	@Override
	protected void showEditPaneOnViewMetaDataEvent(ViewMetaDataEvent event) {
		//念のためSupportチェック
		if (!isEditSupportMetaData(event)) {
			return;
		}

		addTenantTab();
	}

	/**
	 * <p>（カスタマイズ）Tenantの場合はdefNameは無視して選択する。</p>
	 *
	 * @param event {@link ContentSelectedEvent}
	 */
	@Override
	public void onContentSelected(ContentSelectedEvent event){
		if (TenantMainPane.class == event.getSource().getClass()) {
			selectAndScrollNode(rootNode);
		}
	};

	private void addTenantTab() {
		if (defName != null) {
			addTabByName(defName);
		} else {
	        service.getTenant(TenantInfoHolder.getId(), new AsyncCallback<Tenant>() {

				@Override
				public void onSuccess(Tenant result) {
					defName = result.getName();
					addTabByName(defName);
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error!", caught);
					SC.say(AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPluginManager_failed"),
							AdminClientMessageUtil.getString("ui_metadata_tenant_TenantPluginManager_failedGetScreenInfoCause") + caught.getMessage());
				}
			});
		}
	}
}
