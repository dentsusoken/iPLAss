/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.adminconsole.client.metadata.ui.menu.item.MenuItemTreeGrid;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuTree;
import org.iplass.mtp.view.menu.NodeMenuItem;
import org.iplass.mtp.view.menu.UrlMenuItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

/**
 * メニューツリー編集TreeGrid
 *
 * メニューツリーを表示します。
 * {@link org.iplass.adminconsole.client.metadata.ui.menu.item.MenuItemDragPane}で生成されます。
 *
 */
public class MenuTreeGrid extends MtpTreeGrid {

	private MenuEditPane.MenuAttributePane owner;

	/** 内部保持Tree */
	private Tree tree;

	/**
	 * コンストラクタ
	 */
	public MenuTreeGrid(final MenuEditPane.MenuAttributePane owner) {
		this.owner = owner;

		//setAutoFetchData(true);
		setDragDataAction(DragDataAction.MOVE);
		setSelectionType(SelectionStyle.SINGLE);	//単一行選択
		setBorder("none");					//外のSectionと線がかぶるので消す

		setLeaveScrollbarGap(false);		//←falseで縦スクロールバーの領域が自動表示
//		setShowHeader(true);  				//←trueで上に列タイトルが表示される（Default true）
		setEmptyMessage("No Menu Data");	//←空の場合のメッセージ
//		//setManyItemsImage("cubes_all.png");
		setCanReorderRecords(true);			//←Dragによるレコードの並べ替え許可指定（Default false）
		setCanAcceptDroppedRecords(true);	//←レコードのDropの許可指定（Default false）
		setCanDragRecordsOut(true);			//←レコードをDragして他にDropできるか（Default false）
//		setShowEdges(false);				//←trueで周りに枠が表示される（Default false）
		setCanSort(false);					//←ソートできるか（Default true）
		setCanFreezeFields(false);			//←列を固定できるか（Default null）
		setCanPickFields(false);			//←ヘッダで列を選択できるか（Default true）

		//メッセージのカスタマイズ
		//Drop先Nodeにすでに同じNodeが存在する場合、FolderDropEvent自体が発生しないが、
		//エラーメッセージが表示されるためメッセージをカスタマイズ
		//This item already contains a child item with that name.
		//項目は既に含まれます
		//setParentAlreadyContainsChildMessage(parentAlreadyContainsChildMessage);
		setParentAlreadyContainsChildMessage(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_menuAlreadyExists"));

		//以下にメッセージとして関連するプロパティがあるが呼ばれない（設定による）
		//You can't drag an item into one of it's children.
		//子項目の一つにドラッグできません
		//setCantDragIntoChildMessage(cantDragIntoChildMessage);
		//You can't drag an item into itself.
		//同一の項目にはドラッグできません
		//setCantDragIntoSelfMessage(cantDragIntoSelfMessage);

		//Treeの生成
		tree = new Tree();
		tree.setModelType(TreeModelType.CHILDREN);

		TreeGridField nameField = new TreeGridField(DataSourceConstants.FIELD_NAME, "MenuItem");
		TreeGridField remarksField = new TreeGridField("remarks", " ");
		TreeGridField editActionField = new TreeGridField("editAction", " ");
		editActionField.setWidth(25);
		TreeGridField delActionField = new TreeGridField("delAction", " ");
		delActionField.setWidth(25);
		setFields(nameField, remarksField, editActionField, delActionField);

		//Drag＆Drop用EventHandlerの設定
		addFolderDropHandler(new MenuItemFolderDropHandler());

		//この２つを指定することでcreateRecordComponentが有効
		setShowRecordComponents(true);
		setShowRecordComponentsByCell(true);

		//以下の方法で簡単に削除可能だが、確認メッセージを出せないため
		//createRecordComponentを利用して自力ボタン生成
		//レコードの削除を許可（右端にアイコンが表示される）
		//setCanRemoveRecords(true);
		//setRemoveIcon("icon_delete.png");

		//データのセット
		setData(tree);

		//一覧ダブルクリック処理
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				//編集ダイアログ表示
				Record record = event.getRecord();
				showMenuItemDialog(record);
			}
		});
	}

	private void showMenuItemDialog(Record record) {
		MenuItem menuItem = (MenuItem)record.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
		MenuItemTreeDS.MenuItemType type = (MenuItemTreeDS.MenuItemType)record.getAttributeAsObject(MenuItemTreeDS.FieldName.TYPE.name());
		owner.showMenuItemDialog(type, menuItem);
	}

	@Override
	protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
		String fieldName = this.getFieldName(colNum);
		if (fieldName.equals("editAction")) {
			GridActionImgButton recordCanvas = new GridActionImgButton();
			recordCanvas.setActionButtonSrc("icon_edit.png");
			recordCanvas.setActionButtonPrompt(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_editMenuItem"));
			recordCanvas.addActionClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					showMenuItemDialog(record);
				}
			});

			return recordCanvas;
		} else if (fieldName.equals("delAction")) {
			GridActionImgButton recordCanvas = new GridActionImgButton();
			recordCanvas.setActionButtonSrc("remove.png");
			recordCanvas.setActionButtonPrompt(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_deleteMenuItemFromTree"));
			recordCanvas.addActionClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					SC.confirm(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_deleteConfirm"),
							AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_deleteMenuItem", record.getAttribute(DataSourceConstants.FIELD_NAME))
							, new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (value) {
								TreeNode target = Tree.nodeForRecord(record);
								tree.remove(target);
							}

						}
					});
				}
			});
			return recordCanvas;
		}
		return null;

	}

	/**
	 * MenuTreeを展開します。
	 *
	 * @param menuTree MenuTree
	 */
	public void setMenuTree(MenuTree menuTree) {

		MenuTreeNode root = createRootMenuTreeNode(menuTree);

		List<MenuItem> items = menuTree.getMenuItems();
		if (items != null) {
			MenuTreeNode[] children = createMenuItemTreeNodes(items);
			root.setChildren(children);
		}

		tree = new Tree();	//Treeをnewしなおさないと階層がうまく表示されない
		tree.setRoot(root);
		tree.setModelType(TreeModelType.CHILDREN);

		setData(tree);
		getData().openAll();
	}

	/**
	 * 編集されたMenuItem情報を返します。
	 *
	 * @return 編集MenuItem情報
	 */
	public List<MenuItem> getEditMenuItems() {

		return getEditMenuItems(tree.getRoot());
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return true;
	}

	/**
	 * メニューアイテムの変更処理
	 *
	 * @param updateItem 更新MenuItem
	 */
	public void updateMenuItemNode(MenuTree menuTree, MenuItem updateItem) {

		MenuTreeNode root = createRootMenuTreeNode(menuTree);
		if (updateMenuItemNode(updateItem, tree.getRoot(), root)){
			//画面に反映
			tree = new Tree();	//Treeをnewしなおさないと階層がうまく表示されない
			tree.setRoot(root);
			tree.setModelType(TreeModelType.CHILDREN);

			setData(tree);
			getData().openAll();
		}
	}

	/**
	 * メニューアイテムの変更処理
	 *
	 * @param updateItem 更新MenuItem
	 * @param target 変更チェック対象TreeNode
	 * @param newNode 変更後TreeNode
	 * @return 変更結果（変更ありtrue）
	 */
	private boolean updateMenuItemNode(MenuItem updateItem, TreeNode target, TreeNode newNode) {
		if (!tree.hasChildren(target)) {
			return false;
		}
		boolean updated = false;
		List<TreeNode> children = new ArrayList<>(tree.getChildren(target).length);
		children.addAll(Arrays.asList(tree.getChildren(target)));

		for (int i = 0; i < children.size(); i++) {
			MenuTreeNode child = (MenuTreeNode)children.get(i);
			MenuTreeNode newChild = child;
			if (child.getName().equals(updateItem.getName())) {
				newChild = createMenuItemTreeNode(updateItem);
				children.set(i, newChild);
				updated = true;
			}
			if (updateMenuItemNode(updateItem, child, newChild)) {
				children.set(i, newChild);
				updated = true;
			}
		}
		newNode.setChildren(children.toArray(new MenuTreeNode[0]));

		return updated;
	}

	/**
	 * メニューアイテムの削除処理
	 *
	 * @param deleteItem 削除MenuItem
	 */
	public void deleteMenuItemNode(MenuItem deleteItem) {

		TreeNode root = tree.getRoot();
		if (deleteMenuItemNode(deleteItem, root)) {
			//画面に反映
			//tree.reloadChildren(tree.getRoot());
			tree = new Tree();	//Treeをnewしなおさないと階層がうまく表示されない
			tree.setRoot(root);
			tree.setModelType(TreeModelType.CHILDREN);

			setData(tree);
			getData().openAll();
		}
	}

	/**
	 * メニューアイテムの削除処理
	 *
	 * @param deleteItem 削除MenuItem
	 * @param target 変更チェック対象TreeNode
	 * @return 変更結果（変更ありtrue）
	 */
	private boolean deleteMenuItemNode(MenuItem deleteItem, TreeNode target) {

		if (!tree.hasChildren(target)) {
			return false;
		}
		boolean deleted = false;
		List<TreeNode> children = new ArrayList<>(tree.getChildren(target).length);
		//children = Arrays.asList(tree.getChildren(target));	//注意。これだとUnsupportedOperationException発生
		children.addAll(Arrays.asList(tree.getChildren(target)));

		for (int i = children.size() - 1; i >= 0 ; i--) {
			MenuTreeNode child = (MenuTreeNode)children.get(i);
			if (child.getName().equals(deleteItem.getName())) {
				children.remove(i);
				deleted = true;
			} else {
				//自分が削除されていない場合のみ子ノードチェック
				if (deleteMenuItemNode(deleteItem, child)){
					deleted = true;
				}
			}
		}
		if (deleted) {
			target.setChildren(children.toArray(new MenuTreeNode[0]));
		}
		return deleted;
	}

	/**
	 * ツリーを収縮します。
	 */
	public void expandRoot() {
		getTree().closeAll();
	}

	/**
	 * ツリーを展開します。
	 */
	public void expandAll() {
		getTree().openAll();
	}

	/**
	 * 対象TreeNode配下のMenuItem情報を返します。
	 *
	 * @param node 対象TreeNode
	 * @return 編集MenuItem情報
	 */
	private List<MenuItem> getEditMenuItems(TreeNode node) {

		List<MenuItem> items = new ArrayList<>();

		TreeNode[] children = tree.getChildren(node);

		for (TreeNode child : children) {
			MenuItem item = (MenuItem)child.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
			item.setChilds(getEditMenuItems(child));
			items.add(item);
		}

		return items;
	}


	private MenuTreeNode[] createMenuItemTreeNodes(List<MenuItem> children) {
		List<MenuTreeNode> childList = new ArrayList<>(children.size());
		for (MenuItem child : children) {
			MenuTreeNode childNode = createMenuItemTreeNode(child);
			childList.add(childNode);
		}
		return childList.toArray(new MenuTreeNode[0]);
	}

	private MenuTreeNode createMenuItemTreeNode(MenuItem item) {
		MenuTreeNode treeNode = null;
		if (item instanceof ActionMenuItem) {
			treeNode = createActionMenuTreeNode((ActionMenuItem)item);
		} else if (item instanceof EntityMenuItem) {
			treeNode = createEntityMenuTreeNode((EntityMenuItem)item);
		} else if (item instanceof NodeMenuItem) {
			treeNode = createNodeMenuTreeNode((NodeMenuItem)item);
		} else if (item instanceof UrlMenuItem) {
			treeNode = createUrlMenuTreeNode((UrlMenuItem)item);
		} else {
			GWT.log("un support menu item type. type=" + item.getClass().getName());
		}
		if (treeNode != null && item.hasChild()){
			treeNode.setChildren(createMenuItemTreeNodes(item.getChilds()));
		}
		return treeNode;
	}

	private MenuTreeNode createRootMenuTreeNode(MenuTree tree) {
		return new MenuTreeNode(null, tree.getName(),
				"cubes_all.png", true, true, null);
	}

	private MenuTreeNode createNodeMenuTreeNode(NodeMenuItem item) {
		return new MenuTreeNode(item, item.getName(),
				"cube_frame.png", true, true, MenuItemTreeDS.MenuItemType.NODE);
	}

	private MenuTreeNode createActionMenuTreeNode(ActionMenuItem item) {
		final MenuTreeNode menuTreeNode = new MenuTreeNode(item, item.getName(),
				"cube_green.png", true, false, MenuItemTreeDS.MenuItemType.ACTION);
		menuTreeNode.setAttribute("remarks", item.getActionName());
		return menuTreeNode;
	}

	private MenuTreeNode createEntityMenuTreeNode(EntityMenuItem item) {
		final MenuTreeNode menuTreeNode = new MenuTreeNode(item, item.getName(),
				"cube_yellow.png", true, false, MenuItemTreeDS.MenuItemType.ENTITY);
		String remarks = item.getEntityDefinitionName();
		String entityDefinitionName = item.getEntityDefinitionName();
		if (SmartGWTUtil.isNotEmpty(item.getViewName())) {
			entityDefinitionName = item.getEntityDefinitionName().concat(" (").concat(item.getViewName().concat(")"));
		}
		menuTreeNode.setAttribute("remarks", entityDefinitionName);
		return menuTreeNode;
	}

	private MenuTreeNode createUrlMenuTreeNode(UrlMenuItem item) {
		final MenuTreeNode menuTreeNode = new MenuTreeNode(item, item.getName(),
				"cube_blue.png", true, false, MenuItemTreeDS.MenuItemType.URL);
		menuTreeNode.setAttribute("remarks", SafeHtmlUtils.htmlEscape(item.getUrl()));
		return menuTreeNode;
	}

	/**
	 * カスタマイズTreeNode
	 */
	private class MenuTreeNode extends TreeNode {

		/**
		 * コンストラクタ
		 *
		 * @param name          名前
		 * @param icon          Nodeアイコン
		 * @param canAcceptDrop Dropされてもいいか
		 * @param canHasChild   子Nodeを保持できるか
		 */
		public MenuTreeNode(MenuItem item, String name, String icon, Boolean canAcceptDrop, Boolean canHasChild, MenuItemTreeDS.MenuItemType type) {
			setAttribute(MenuItemTreeDS.FieldName.VALUEOBJECT.name(), item);

			setName(name);
			setIcon(icon);
			setCanAcceptDrop(canAcceptDrop);

			setAttribute(DataSourceConstants.FIELD_NAME, name);
			setAttribute("remarks", "");
			setAttribute(MenuItemTreeDS.FieldName.TYPE.name(), type);

			if (canHasChild) {
				//子供を持てる場合は、空で作ってあげないとDropできないのでここで作成
				//もしかするとTreeGrid#canDropOnLeavesで設定可能かもしれない
				setChildren(new MenuTreeNode[]{});
			}

//			//ツリー内でのDragとListからのDropを区別するためOwnを設定
//			setDragType(MenuItemDragType.Own.name());
		}

	}

	/**
	 * カスタマイズFolderDropHandler
	 */
	private class MenuItemFolderDropHandler implements FolderDropHandler {

		/**
		 * フォルダDrop処理
		 *
		 * 【参考】
		 * event.getFolder()・・・・Drop先ターゲット
		 * event.getNodes() ・・・・Drag対象Node
		 * event.getIndex() ・・・・Drop先のIndex
		 *
		 * @param event FolderDropEvent
		 */
		@Override
		public void onFolderDrop(FolderDropEvent event) {

			//Drop先情報の取得
			TreeNode dropTargetMenuNode = null;
			if (event.getFolder() instanceof MenuTreeNode) {
				dropTargetMenuNode = event.getFolder();
				GWT.log("drop target(MenuTreeNode):" + dropTargetMenuNode.getName());
			} else if (event.getFolder() instanceof TreeNode) {
				dropTargetMenuNode = event.getFolder();
				GWT.log("drop target(TreeNode):" + dropTargetMenuNode.getName());
			} else {
				GWT.log("cancel drop action. drop target is not MenuTreeNode:" + event.getFolder());
				//デフォルトイベントのキャンセル
				event.cancel();
				return;
			}

			if (event.getSourceWidget() instanceof MenuTreeGrid) {
				//ツリー内移動の場合

				//Drag元TreeNode情報の取得
				TreeNode[] nodes = event.getNodes();
				if (nodes == null || nodes.length != 1) {
					throw new IllegalStateException(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_dragInvalid") + nodes);
				}
				TreeNode moveMenuNode = null;
				if (nodes[0] instanceof MenuTreeNode) {
					moveMenuNode = nodes[0];

					GWT.log("drag moving target(MenuTreeNode):" + moveMenuNode.getName());
				} else if (nodes[0] instanceof TreeNode) {
					moveMenuNode = nodes[0];

					GWT.log("drag moving target(TreeNode):" + moveMenuNode.getName());
				} else {
					GWT.log("cancel drop action. drag target is not MenuTreeNode:" + nodes[0]);
					//デフォルトイベントのキャンセル
					event.cancel();
					return;
				}

				//移動処理
				//同一Node上で上から下に移動の場合にevent.getIndex()で追加すると
				//removeした分１つ下にずれるため調整
				int curIndex = getCurrentIndex(moveMenuNode);
				int addIndex = event.getIndex();
				if (tree.getParentPath(moveMenuNode).equals(tree.getPath(dropTargetMenuNode))){
					if (addIndex > curIndex) {
						addIndex--;
					}
				}

				//★★★注意 AddするとMenuTreeNodeがTreeNodeに変わってしまう
				tree.remove(moveMenuNode);
				tree.add(moveMenuNode, dropTargetMenuNode, addIndex);

			} else if (event.getSourceWidget() instanceof MenuItemTreeGrid) {
				//MenuItem追加の場合

				//Drag元MenuItemの取得
				TreeNode[] nodes = event.getNodes();
				if (nodes == null || nodes.length != 1) {
					throw new IllegalStateException(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_dragInvalid") + nodes);
				}
				MenuItem menuItem = (MenuItem)nodes[0].getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());

				//MenuTreeNodeの作成
				MenuTreeNode addMenuNode = createMenuItemTreeNode(menuItem);

				GWT.log("drag adding target:" + addMenuNode.getName());

				//追加処理
				//★★★注意 AddするとMenuTreeNodeがTreeNodeに変わってしまう
				tree.add(addMenuNode, dropTargetMenuNode, event.getIndex());

			} else {
				GWT.log("cancel drop action. drag souce is not Support:" + event.getSourceWidget().getID());
				//デフォルトイベントのキャンセル
				event.cancel();
				return;
			}

			//デフォルトイベントのキャンセル
			event.cancel();
		}

		/**
		 * 親Nodeに対するIndexを返します。
		 *
		 * @param node 対象Node
		 * @return Index
		 */
		private int getCurrentIndex(TreeNode node) {

			GWT.log("search node index: node=" + node + ",name=" + node.getName());
			if (tree.getParent(node) != null) {
				TreeNode[] children = tree.getChildren(tree.getParent(node));

				int i = 0;
				for (TreeNode child : children) {
					GWT.log("search node index: child[" + i + "]=" + child + ",name=" + child.getName());
					//Dragでの移動処理を行うとなぜかObject自体が変わってしまうためnameでチェック
					if (child.getName().equals(node.getName())) {
						return i;
					}
					i++;
				}
			}
			//ありえない
			throw new IllegalStateException(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_canNotGetDrag") + node);
		}
	}
}
