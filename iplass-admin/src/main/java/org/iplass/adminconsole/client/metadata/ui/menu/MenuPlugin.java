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

package org.iplass.adminconsole.client.metadata.ui.menu;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.menu.item.MenuItemDialog;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.menu.NodeMenuItem;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;


public class MenuPlugin extends DefaultMetaDataPlugin {

	/** カテゴリ名 */
	private static final String CATEGORY_NAME = MetaDataConstants.META_CATEGORY_VIEW_COMPONENTS;

	/** ノード名 */
	private static final String NODE_NAME = "Menu";

	/** ノードアイコン */
	private static final String NODE_ICON = "application_side_list.png";

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	@Override
	public String getCategoryName() {
		return CATEGORY_NAME;
	}

	@Override
	protected String nodeName() {
		return NODE_NAME;
	}

	@Override
	protected String nodeDisplayName() {
		return AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_menu");
	}

	@Override
	protected String nodeIcon() {
		return NODE_ICON;
	}

	@Override
	protected String definitionClassName() {
		return MenuTree.class.getName();
	}

	@Override
	protected void itemCreateAction(String folderPath) {
		// 新規MenuTree作成
		CreateMenuTreeDialog dialog = new CreateMenuTreeDialog(definitionClassName(), nodeDisplayName(), folderPath, false);
		dialog.setNamePolicy(isPathSlash(), isNameAcceptPeriod());
		dialog.addDataChangeHandler(new DataChangedHandler() {
			@Override
			public void onDataChanged(DataChangedEvent event) {
				refreshWithSelect(event.getValueName(), null);
			}
		});
		dialog.show();
	}

	@Override
	protected void itemCopyAction(MetaDataItemMenuTreeNode itemNode) {
		// 新規メニューツリー作成
		CreateMenuTreeDialog dialog = new CreateMenuTreeDialog(definitionClassName(), nodeDisplayName(), "", true);
		dialog.setNamePolicy(isPathSlash(), isNameAcceptPeriod());
		dialog.addDataChangeHandler(new DataChangedHandler() {
			@Override
			public void onDataChanged(DataChangedEvent event) {
				refreshWithSelect(event.getValueName(), null);
			}
		});
		dialog.setSourceName(itemNode.getDefName());
		dialog.show();
	}

	@Override
	protected void itemDelete(final MetaDataItemMenuTreeNode itemNode) {
		service.deleteDefinition(TenantInfoHolder.getId(), MenuTree.class.getName(), itemNode.getDefName(), new AsyncCallback<AdminDefinitionModifyResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// 失敗時
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_failedToDeleteMenu") + caught.getMessage());
			}
			@Override
			public void onSuccess(AdminDefinitionModifyResult result) {
				if (result.isSuccess()) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_deleteMenuComp"));

					refresh();
					removeTab(itemNode);
				} else {
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_failedToGetMenuItemCause") + result.getMessage());
				}
			}
		});
	}

	@Override
	protected MetaDataMainEditPane workSpaceContents(MetaDataItemMenuTreeNode itemNode) {
		return new MenuEditPane(itemNode, this);
	}

	@Override
	protected Class<?>[] workspaceContentsPaneClass() {
		return new Class[]{MenuEditPane.class};
	}

	/**
	 * <p>（カスタマイズ）MenuTree、MenuItemをサポートする</p>
	 */
	@Override
	public boolean isEditSupportMetaData(ViewMetaDataEvent event) {
		return (definitionClassName().equals(event.getDefinitionClassName())
				|| MenuItem.class.getName().equals(event.getDefinitionClassName()));
	}

	/**
	 * <p>（カスタマイズ）MenuTree、MenuItemをサポートする</p>
	 */
	@Override
	protected void showEditPaneOnViewMetaDataEvent(final ViewMetaDataEvent event) {
		//念のためSupportチェック
		if (!isEditSupportMetaData(event)) {
			return;
		}

		if (definitionClassName().equals(event.getDefinitionClassName())) {
			addTabOnViewMetaDataEvent(event);
		} else if (MenuItem.class.getName().equals(event.getDefinitionClassName())) {
			//MenuItemはダイアログで表示なので、ここで検索
			service.getDefinition(TenantInfoHolder.getId(), MenuItem.class.getName(), event.getDefinitionName(), new AsyncCallback<MenuItem>() {
				@Override
				public void onFailure(Throwable caught) {
					// 失敗時
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_failedToGetMenuItemCause") + caught.getMessage());
				}
				@Override
				public void onSuccess(MenuItem result) {
					if (result == null) {
						SC.warn(AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_failedToGetMenuItem"));
						return;
					}
					//ダイアログの表示
					MenuItemTreeDS.MenuItemType type = null;
					if (result instanceof ActionMenuItem) {
						type = MenuItemTreeDS.MenuItemType.ACTION;
					} else if (result instanceof EntityMenuItem) {
						type = MenuItemTreeDS.MenuItemType.ENTITY;
					} else if (result instanceof NodeMenuItem) {
						type = MenuItemTreeDS.MenuItemType.NODE;
					}

					MenuItemDialog dialog = new MenuItemDialog(type);
					dialog.setMenuItem(result, false);
					dialog.show();
				}
			});
		}
	}
}
