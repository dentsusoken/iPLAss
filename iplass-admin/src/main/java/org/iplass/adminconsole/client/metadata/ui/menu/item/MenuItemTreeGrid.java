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

package org.iplass.adminconsole.client.metadata.ui.menu.item;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.view.menu.MenuItem;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItemStringFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;

public class MenuItemTreeGrid extends MtpTreeGrid {

	private MenuItemDragPane owner;

	/** フォルダ用コンテキストメニュー */
	private MenuItemContextMenu folderContextMenu;

	/** アイテム用コンテキストメニュー */
	private MenuItemContextMenu itemContextMenu;

	/**
	 * コンストラクタ
	 */
	public MenuItemTreeGrid(final MenuItemDragPane owner) {
		this.owner = owner;

		setLeaveScrollbarGap(false);
		setShowHeader(false);
		setDragDataAction(DragDataAction.NONE);
		setSelectionType(SelectionStyle.SINGLE);
		setCanDragRecordsOut(true);
		setBorder("none");

		setCanSort(false);
		setCanFreezeFields(false);
		setCanPickFields(false);

		//この２つを指定することでcreateRecordComponentが有効
		setShowRecordComponents(true);
		setShowRecordComponentsByCell(true);

		//コンテキストメニュー
		addNodeContextClickHandler(new NodeContextClickHandler() {

			@Override
			public void onNodeContextClick(NodeContextClickEvent event) {
				TreeNode node = event.getNode();

				MenuItemContextMenu targetContextMenu = null;
				if (getTree().isFolder(node)) {
					if (folderContextMenu == null) {
						folderContextMenu = new MenuItemContextMenu(true);
					}
					targetContextMenu = folderContextMenu;
				} else {
					if (itemContextMenu == null) {
						itemContextMenu = new MenuItemContextMenu(false);
					}
					targetContextMenu = itemContextMenu;
				}
				targetContextMenu.setNode(node);
				setContextMenu(targetContextMenu);
			}
		});

		//一覧ダブルクリック処理
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				//編集ダイアログ表示
				Record record = event.getRecord();
				TreeNode node = (TreeNode)record;
				if (!getTree().isFolder(node)) {
					//Folderは対象外
					MenuItem menuItem = (MenuItem)record.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
					MenuItemTreeDS.MenuItemType type = (MenuItemTreeDS.MenuItemType)record.getAttributeAsObject(MenuItemTreeDS.FieldName.TYPE.name());
					owner.showMenuItemDialog(type, menuItem);
				}
			}
		});

		//Drag開始処理
		addDragStartHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {
				TreeNode node = getSelectedRecord();
				if (getTree().isFolder(node)) {
					//Folderは対象外
					event.cancel();
				}
			}
		});

		//MenuTreeの取得とかぶるので少し遅らせるため、表示後にデータ取得
		addDrawHandler(new DrawHandler() {

			@Override
			public void onDraw(DrawEvent event) {
				//表示データの取得
				initializeData();
			}
		});

//		initializeData();
	}

	private void setGridFields() {

		ListGridField displayNameField = new ListGridField(DataSourceConstants.FIELD_DISPLAY_VALUE);
		setFields(displayNameField);
	}

	/**
	 * 初期表示処理
	 */
	private void initializeData() {
		setDataSource(MenuItemTreeDS.getInstance());
		setGridFields();
		fetchData();
	}

	public void refresh() {
		initializeData();
	}

	/**
	 * ツリーを収縮します。
	 */
	public void expandRoot() {
		getTree().closeAll();
//		getTree().openFolders(
//				getTree().getChildren(getTree().getRoot()));
	}

	/**
	 * ツリーを展開します。
	 */
	public void expandAll() {
		getTree().openAll();
	}

	private class MenuItemContextMenu extends Menu {

		private TreeNode node;

		public MenuItemContextMenu(boolean isFolder) {

			if (isFolder) {
				//追加ボタン
				final com.smartgwt.client.widgets.menu.MenuItem addItem = new com.smartgwt.client.widgets.menu.MenuItem(
						AdminClientMessageUtil.getString("ui_metadata_DefaultMetaDataPluginManager_create",
								AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_menuItem")),
						MetaDataConstants.CONTEXT_MENU_ICON_ADD);
				addItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {

						MenuItemTreeDS.MenuItemType type = getItemType();
						if (node.getAttributeAsBoolean(MenuItemTreeDS.FieldName.ISITEMTOP.name())) {
							owner.showMenuItemDialog(type);
						} else {
							String name = node.getAttributeAsString(DataSourceConstants.FIELD_NAME);
							owner.showMenuItemDialog(type, name);
						}
					}
				});
				setItems(addItem);

			} else {

				// 編集
				final com.smartgwt.client.widgets.menu.MenuItem editItem = new com.smartgwt.client.widgets.menu.MenuItem(
						AdminClientMessageUtil.getString("ui_metadata_DefaultMetaDataPluginManager_open",
								AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_menuItem")));
				editItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						MenuItem menuItem = getMenuItem();
						MenuItemTreeDS.MenuItemType type = getItemType();
						owner.showMenuItemDialog(type, menuItem);
					}
				});
				editItem.setDynamicIconFunction(new MenuItemStringFunction() {
					@Override
					public String execute(Canvas target, Menu menu, com.smartgwt.client.widgets.menu.MenuItem item) {
						return node.getIcon();
					}
				});

				// 削除
				final com.smartgwt.client.widgets.menu.MenuItem deleteItem = new com.smartgwt.client.widgets.menu.MenuItem(
						AdminClientMessageUtil.getString("ui_metadata_DefaultMetaDataPluginManager_delete",
								AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_menuItem")),
						MetaDataConstants.CONTEXT_MENU_ICON_DEL);
				deleteItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						String name = node.getAttributeAsString(DataSourceConstants.FIELD_NAME);

						SC.confirm(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_deleteConfirm"),
								AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_deleteMenuItemConf", name)
								, new BooleanCallback() {

							@Override
							public void execute(Boolean value) {
								if (value) {
									//メニューアイテム削除処理
									MenuItem menuItem = getMenuItem();
									owner.delMenuItem(menuItem);
								}
							}
						});
					}
				});

				// コピー
				final com.smartgwt.client.widgets.menu.MenuItem copyItem = new com.smartgwt.client.widgets.menu.MenuItem(
						AdminClientMessageUtil.getString("ui_metadata_DefaultMetaDataPluginManager_copy",
								AdminClientMessageUtil.getString("ui_metadata_menu_MenuPluginManager_menuItem")),
						MetaDataConstants.CONTEXT_MENU_ICON_COPY);
				copyItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						MenuItem menuItem = getMenuItem();
						MenuItemTreeDS.MenuItemType type = getItemType();
						owner.showMenuItemDialog(type, menuItem, true);
					}
				});

				setItems(editItem, deleteItem, copyItem);
			}

		}

		public void setNode(TreeNode node) {
			this.node = node;
		}

		private MenuItem getMenuItem() {
			return (MenuItem)node.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
		}

		private MenuItemTreeDS.MenuItemType getItemType() {
			return (MenuItemTreeDS.MenuItemType)node.getAttributeAsObject(MenuItemTreeDS.FieldName.TYPE.name());
		}
	}

}
