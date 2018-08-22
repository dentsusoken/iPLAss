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

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.GridActionImgButton;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.mtp.view.menu.MenuItem;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DragStartEvent;
import com.smartgwt.client.widgets.events.DragStartHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

public class MenuItemTreeGrid extends TreeGrid {

	private MenuItemDragPane owner;

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
				TreeNode node = (TreeNode) getSelectedRecord();
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
		List<ListGridField> fields = new ArrayList<ListGridField>();

		ListGridField displayNameField = new ListGridField(DataSourceConstants.FIELD_DISPLAY_VALUE);
		fields.add(displayNameField);

		ListGridField editField = new ListGridField("editButton");
		editField.setWidth(25);
		fields.add(editField);

		ListGridField deleteField = new ListGridField("deleteButton");
		deleteField.setWidth(25);
		fields.add(deleteField);

		ListGridField copyField = new ListGridField("copyButton");
		copyField.setWidth(25);
		fields.add(copyField);

		setFields(fields.toArray(new ListGridField[]{}));
	}

	@Override
	protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
		TreeNode node = (TreeNode)record;
		String fieldName = this.getFieldName(colNum);

		if (fieldName.equals("editButton") && !getTree().isFolder(node)) {

			GridActionImgButton recordCanvas = new GridActionImgButton();
			recordCanvas.setActionButtonSrc("icon_edit.png");
			recordCanvas.setActionButtonPrompt(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_editMenuItem"));
			recordCanvas.addActionClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					MenuItem menuItem = (MenuItem)record.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
					MenuItemTreeDS.MenuItemType type = (MenuItemTreeDS.MenuItemType)record.getAttributeAsObject(MenuItemTreeDS.FieldName.TYPE.name());
					owner.showMenuItemDialog(type, menuItem);
				}
			});

			return recordCanvas;
		}
		if (fieldName.equals("deleteButton") && !getTree().isFolder(node)) {

			GridActionImgButton recordCanvas = new GridActionImgButton();
			recordCanvas.setActionButtonSrc("icon_remove_files.png");
			recordCanvas.setActionButtonPrompt(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_deleteMenuItem"));
			recordCanvas.addActionClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					String name = record.getAttributeAsString(DataSourceConstants.FIELD_NAME);

					SC.confirm(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_deleteConfirm"),
							AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_deleteMenuItemConf", name)
							, new BooleanCallback() {

						@Override
						public void execute(Boolean value) {
							if (value) {
								//メニューアイテム削除処理
								MenuItem menuItem = (MenuItem)record.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
								owner.delMenuItem(menuItem);
							}
						}
					});
				}
			});

			return recordCanvas;
		}
		if (fieldName.equals("copyButton")) {

			GridActionImgButton recordCanvas = new GridActionImgButton();
			if (node.getAttributeAsBoolean(MenuItemTreeDS.FieldName.ISITEMTOP.name())) {
				//Topの場合は追加ボタンにする
				recordCanvas.setActionButtonSrc("icon_add_files.png");
				recordCanvas.setActionButtonPrompt(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_addMenuItem"));
				recordCanvas.addActionClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						MenuItemTreeDS.MenuItemType type = (MenuItemTreeDS.MenuItemType)record.getAttributeAsObject(MenuItemTreeDS.FieldName.TYPE.name());
						owner.showMenuItemDialog(type);
					}
				});
			} else if (getTree().isFolder(node)) {
				//Folderの場合は追加ボタンにする（Path付き）
				recordCanvas.setActionButtonSrc("icon_add_files.png");
				recordCanvas.setActionButtonPrompt(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_addMenuItem"));
				recordCanvas.addActionClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						MenuItemTreeDS.MenuItemType type = (MenuItemTreeDS.MenuItemType)record.getAttributeAsObject(MenuItemTreeDS.FieldName.TYPE.name());
						String name = record.getAttributeAsString(DataSourceConstants.FIELD_NAME);
						owner.showMenuItemDialog(type, name);
					}
				});
			} else {
				recordCanvas.setActionButtonSrc("copy.png");
				recordCanvas.setActionButtonPrompt(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemTreeGrid_copyMenuItem"));
				recordCanvas.addActionClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						MenuItem menuItem = (MenuItem)record.getAttributeAsObject(MenuItemTreeDS.FieldName.VALUEOBJECT.name());
						MenuItemTreeDS.MenuItemType type = (MenuItemTreeDS.MenuItemType)record.getAttributeAsObject(MenuItemTreeDS.FieldName.TYPE.name());
						owner.showMenuItemDialog(type, menuItem, true);
					}
				});
			}
			return recordCanvas;
		}
		return null;
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
}
