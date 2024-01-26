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

package org.iplass.adminconsole.client.metadata.ui.menu.item;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.adminconsole.client.metadata.ui.menu.item.event.MenuItemDataChangeHandler;
import org.iplass.adminconsole.client.metadata.ui.menu.item.event.MenuItemDataChangedEvent;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.view.menu.MenuItem;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.events.DataArrivedEvent;
import com.smartgwt.client.widgets.tree.events.DataArrivedHandler;

/**
 * メニューアイテムDragパネル
 *
 * 登録されているDrag可能なMenuItemを表示します。
 */
public class MenuItemDragPane extends VLayout {

	public static final String DRAG_TYPE = "MenuItem";

	private static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
	private static final String CONTRACT_ICON = "[SKIN]/actions/sort_ascending.png";

	private MenuItemTreeGrid grid;


	/** データ変更ハンドラ */
	private List<MenuItemDataChangeHandler> handlers = new ArrayList<>();

	/**
	 * コンストラクタ
	 */
	public MenuItemDragPane() {
		setWidth("40%");

		SectionStack menuItemHeaderSection = new SectionStack();

		SectionStackSection menuItemSection = new SectionStackSection("Menu Items");
		menuItemSection.setExpanded(true);
		menuItemSection.setCanCollapse(false);	//CLOSE不可

		//Expand/Contractボタン
		ImgButton expandAllButton = new ImgButton();
		expandAllButton.setSrc(EXPAND_ICON);
		expandAllButton.setSize(16);
		expandAllButton.setShowFocused(false);
		expandAllButton.setShowRollOver(false);
		expandAllButton.setShowDown(false);
		expandAllButton.setTooltip(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_expandTree"));
		expandAllButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.expandAll();
			}
		});
		ImgButton contractAllButton = new ImgButton();
		contractAllButton.setSrc(CONTRACT_ICON);
		contractAllButton.setSize(16);
		contractAllButton.setShowFocused(false);
		contractAllButton.setShowRollOver(false);
		contractAllButton.setShowDown(false);
		contractAllButton.setTooltip(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_contractTree"));
		contractAllButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				grid.expandRoot();
			}
		});
		//Refreshボタン
		ImgButton refreshButton = new ImgButton();
		refreshButton.setSrc("refresh.png");
		refreshButton.setSize(16);
		refreshButton.setShowFocused(false);
		refreshButton.setShowRollOver(false);
		refreshButton.setShowDown(false);
		refreshButton.setTooltip(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemDragPane_refreshListOfItem"));
		refreshButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				refresh();
			}
		});
		menuItemSection.setControls(expandAllButton, contractAllButton, refreshButton);
		menuItemHeaderSection.addSection(menuItemSection);

		//表示TreeGrid
		grid = new MenuItemTreeGrid(this);
		grid.setDragType(DRAG_TYPE);	//DragされるItemのType設定
		grid.addDataArrivedHandler(new DataArrivedHandler() {

			@Override
			public void onDataArrived(DataArrivedEvent event) {
				// 展開する
				grid.getData().openAll();
			}
		});
		menuItemSection.addItem(grid);

		addMember(menuItemHeaderSection);

		//表示データの取得
		initializeData();
	}

	/**
	 * MenuItemDataChangeHandlerを追加します。
	 *
	 * @param handler {@link MenuItemDataChangeHandler}
	 */
	public void addMenuItemDataChangeHandler(MenuItemDataChangeHandler handler) {
		handlers.add(0, handler);
	}

	/**
	 * 画面を再表示します。
	 */
	public void refresh() {
		grid.refresh();
	}

	/**
	 * 初期表示処理
	 */
	private void initializeData() {
	}

	/**
	 * メニューアイテムダイアログを表示します。
	 *
	 * @param type メニューアイテムタイプ
	 */
	public void showMenuItemDialog(MenuItemTreeDS.MenuItemType type) {
		showMenuItemDialog(type, null, false, null);
	}

	/**
	 * メニューアイテムダイアログを表示します。
	 *
	 * @param type メニューアイテムタイプ
	 * @param initialName 初期設定名
	 */
	public void showMenuItemDialog(MenuItemTreeDS.MenuItemType type, String initialName) {
		showMenuItemDialog(type, null, false, initialName);
	}

	/**
	 * メニューアイテムダイアログを表示します。
	 *
	 * @param type メニューアイテムタイプ
	 * @param menuItem 更新メニューアイテム（更新時）
	 */
	public void showMenuItemDialog(MenuItemTreeDS.MenuItemType type, MenuItem menuItem) {
		showMenuItemDialog(type, menuItem, false, null);
	}

	/**
	 * メニューアイテムダイアログを表示します。
	 *
	 * @param type メニューアイテムタイプ
	 * @param menuItem 更新メニューアイテム（更新、コピー時）
	 * @param isCopy コピーモード
	 */
	public void showMenuItemDialog(MenuItemTreeDS.MenuItemType type, MenuItem menuItem, boolean isCopy) {
		showMenuItemDialog(type, menuItem, isCopy, null);
	}

	/**
	 * メニューアイテムダイアログを表示します。
	 *
	 * @param type メニューアイテムタイプ
	 * @param menuItem 更新メニューアイテム（更新、コピー時）
	 * @param isCopy コピーモード
	 * @param initialName 初期設定名
	 */
	public void showMenuItemDialog(MenuItemTreeDS.MenuItemType type, MenuItem menuItem, boolean isCopy, String initialName) {
		MenuItemDialog dialog = new MenuItemDialog(type);
		dialog.addMenuItemDataChangeHandler(new MenuItemDataChangeHandler() {

			@Override
			public void onDataChanged(MenuItemDataChangedEvent event) {
				refreshDataChanged(event);
			}
		});
		if (menuItem != null) {
			dialog.setMenuItem(menuItem, isCopy);

			//メニューアイテムを選択
			grid.selectMenuItemNode(menuItem);
		}
		if (initialName != null && !initialName.isEmpty()) {
			dialog.setInitialName(initialName);
		}
		dialog.show();
	}

	/**
	 * メニューアイテムを削除します。
	 *
	 * @param menuItem 削除対象 {@link MenuItem}
	 */
	void delMenuItem(final MenuItem menuItem) {
		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.deleteDefinition(TenantInfoHolder.getId(), MenuItem.class.getName(), menuItem.getName(), new AsyncCallback<AdminDefinitionModifyResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// 失敗時
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemDragPane_faledToDeleteMenuItem") + caught.getMessage());
			}
			@Override
			public void onSuccess(AdminDefinitionModifyResult result) {
				if (result.isSuccess()) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemDragPane_deleteMenuItemComp"));
					MenuItemDataChangedEvent event = new MenuItemDataChangedEvent(MenuItemDataChangedEvent.Type.DELETE);
					event.setValueObject(menuItem);
					refreshDataChanged(event);
				} else {
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_menu_item_MenuItemDragPane_failedToDeleteMenuItem") + result.getMessage());
				}
			}
		});
	}


	/**
	 * {@link MenuItemDataChangedEvent} を受けて、画面を再表示します。
	 *
	 * @param event {@link MenuItemDataChangedEvent}
	 */
	private void refreshDataChanged(MenuItemDataChangedEvent event) {

		MenuItem menuItem = event.getValueObject(MenuItem.class);
		if (MenuItemDataChangedEvent.Type.ADD.equals(event.getType())) {
			grid.addMenuItemNode(menuItem);
		} else if (MenuItemDataChangedEvent.Type.UPDATE.equals(event.getType())) {
			grid.updateMenuItemNode(menuItem);
		} else if (MenuItemDataChangedEvent.Type.DELETE.equals(event.getType())) {
			grid.deleteMenuItemNode(menuItem);
		}

		fireMenuItemDataChanged(event);
	}

	/**
	 * データの変更を通知します。
	 *
	 * @param item 更新{@link MenuItem}
	 */
	private void fireMenuItemDataChanged(MenuItemDataChangedEvent event) {
		for (MenuItemDataChangeHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
