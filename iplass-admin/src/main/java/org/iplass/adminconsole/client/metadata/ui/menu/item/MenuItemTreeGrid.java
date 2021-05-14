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

import java.util.Arrays;
import java.util.List;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS.MenuItemType;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.view.menu.MenuItem;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
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
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItemStringFunction;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;

public class MenuItemTreeGrid extends MtpTreeGrid {

	private MenuItemDragPane owner;

	private MenuItemTreeDS ds;

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

		//HOVER時にRemarkを表示
		setCanHover(Boolean.TRUE);
		setHoverCustomizer(new HoverCustomizer() {

			@Override
			public String hoverHTML(Object value, ListGridRecord record, int rowNum, final int colNum) {
				final TreeNode node = Tree.nodeForRecord(record);
				String remarks = node.getAttribute(MenuItemTreeDS.FieldName.REMARKS.name());
				if (SmartGWTUtil.isNotEmpty(remarks)) {
					return SmartGWTUtil.getHoverString(remarks);
				}
				return "";
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

		setGridFields();
	}

	private void setGridFields() {

		TreeGridField displayNameField = new TreeGridField(DataSourceConstants.FIELD_DISPLAY_VALUE);
		setFields(displayNameField);
	}

	/**
	 * 初期表示処理
	 */
	private void initializeData() {
		ds = MenuItemTreeDS.getInstance();
		setDataSource(ds);
		setGridFields();
		fetchData(null, new DSCallback() {

			@Override
			public void execute(DSResponse dsResponse, Object data, DSRequest dsRequest) {
				//検索時（初期、リフレッシュ）はルートのみ表示
				expandRoot();
			}
		});
	}

	/**
	 * ツリーを更新します。
	 */
	public void refresh() {
		initializeData();
	}

	/**
	 * メニューアイテムを追加します。
	 *
	 * @param createItem メニューアイテム
	 */
	public void addMenuItemNode(MenuItem createItem) {

		//タイプ別のRoot取得
		TreeNode rootNode = ds.getTypeRootNode(createItem);
		TreeNode parent = rootNode;

		//追加するItemの生成
		TreeNode itemNode = ds.createMenuItemNode(createItem);
		MenuItemType type = (MenuItemType)itemNode.getAttributeAsObject(MenuItemTreeDS.FieldName.TYPE.name());

		//フォルダの追加
		String[] nodePaths = createItem.getName().split("/");
		if (nodePaths.length > 1) {
			//階層
			String path = "";
			for (int i = 0; i < nodePaths.length - 1; i++) {
				path += nodePaths[i] + "/";
				if (getTree().hasChildren(parent)) {
					//フォルダが存在する場合、既存チェック
					TreeNode addNode = null;
					TreeNode[] children = getTree().getChildren(parent);
					for (int j = 0; j < children.length; j++) {
						TreeNode child = children[j];
						if (getTree().isLeaf(child)) {
							//アイテムは除外
							//フォルダ、アイテムの順になっているのでアイテムが見つかった段階でフォルダなしと判断
							addNode = ds.createFolderNode(path, nodePaths[i], type);
							getTree().add(addNode, parent, j);
							parent = addNode;
							break;
						}
						int compare = child.getName().compareTo(nodePaths[i] + "/");
						if (compare == 0) {
							//既に存在
							addNode = child;
							parent = child;
							break;
						}
						if (compare > 0) {
							//途中に追加
							addNode = ds.createFolderNode(path, nodePaths[i], type);
							getTree().add(addNode, parent, j);
							parent = addNode;
							break;
						}
					}
					if (addNode == null) {
						//最後に追加
						addNode = ds.createFolderNode(path, nodePaths[i], type);
						getTree().add(addNode, parent);
						parent = addNode;
					}
				} else {
					//フォルダが存在しない場合
					TreeNode addNode = ds.createFolderNode(path, nodePaths[i], type);
					getTree().add(addNode, parent);
					parent = addNode;
				}
			}
		}

		//アイテムを追加
		if (getTree().hasChildren(parent)) {
			TreeNode[] children = getTree().getChildren(parent);
			for (int i = 0; i < children.length; i++) {
				TreeNode child = children[i];
				//フォルダは除外
				if (getTree().isFolder(child)) {
					continue;
				}
				if (child.getName().compareTo(itemNode.getName()) > 0) {
					//途中に追加
					getTree().add(itemNode, parent, i);
					itemNode = null;
					break;
				}
			}
			if (itemNode != null) {
				//最後に追加
				getTree().add(itemNode, parent);
				itemNode = null;
			}

		} else {
			getTree().add(itemNode, parent);
		}

		//選択
		selectAndScrollNode(itemNode);
	}

	/**
	 * メニューアイテムを更新します。
	 *
	 * @param updateItem メニューアイテム
	 */
	public void updateMenuItemNode(MenuItem updateItem) {

		List<TreeNode> children = Arrays.asList(getTree().getAllNodes());
		TreeNode itemNode = null;
		for (TreeNode node : children) {
			MenuItem menuItem = (MenuItem)node.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
			if (menuItem != null && menuItem.getName().equals(updateItem.getName())) {
				itemNode = node;
				int curIndex = getCurrentIndex(itemNode);
				TreeNode parent = getTree().getParent(itemNode);

				//変更内容をNodeに反映
				ds.updateMenuItemNode(itemNode, updateItem);

				//更新するため、削除して追加
				getTree().remove(itemNode);
				getTree().add(itemNode, parent, curIndex);
			}
		}

		//選択
		if (itemNode != null) {
			selectAndScrollNode(itemNode);
		}
	}

	/**
	 * メニューアイテムを削除します。
	 *
	 * @param deleteItem メニューアイテム
	 */
	public void deleteMenuItemNode(MenuItem deleteItem) {

		List<TreeNode> children = Arrays.asList(getTree().getAllNodes());
		for (TreeNode node : children) {
			MenuItem menuItem = (MenuItem)node.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
			if (menuItem != null && menuItem.getName().equals(deleteItem.getName())) {
				TreeNode parent = getTree().getParent(node);

				getTree().remove(node);

				//親の子供がなくなっていたら削除
				while(parent != null) {
					//ItemのTopなら終了
					if (parent.getAttributeAsBoolean(MenuItemTreeDS.FieldName.ISITEMTOP.name())) {
						break;
					}
					//子供がいれば終了
					if (getTree().hasChildren(parent)) {
						break;
					}
					TreeNode parParent = getTree().getParent(parent);
					getTree().remove(parent);
					parent = parParent;
				}
			}
		}
	}

	/**
	 * メニューアイテムを選択します。
	 *
	 * @param menuItem メニューアイテム
	 */
	public void selectMenuItemNode(MenuItem menuItem) {
		List<TreeNode> children = Arrays.asList(getTree().getAllNodes());
		for (TreeNode node : children) {
			MenuItem nodeItem = (MenuItem)node.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
			if (nodeItem != null && nodeItem.getName().equals(menuItem.getName())) {
				selectAndScrollNode(node);
				return;
			}
		}
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

	private int getCurrentIndex(TreeNode node) {

		if (getTree().getParent(node) != null) {
			TreeNode[] children = getTree().getChildren(getTree().getParent(node));

			int i = 0;
			for (TreeNode child : children) {
				if (child.getName().equals(node.getName())) {
					return i;
				}
				i++;
			}
		}
		//ありえない
		throw new IllegalStateException(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_canNotGetDrag") + node);
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
