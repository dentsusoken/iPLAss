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

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.menu.MenuItemTreeDS;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonAttributeSection;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.client.metadata.ui.menu.item.MenuItemDialog;
import org.iplass.adminconsole.client.metadata.ui.menu.item.MenuItemDragPane;
import org.iplass.adminconsole.client.metadata.ui.menu.item.event.MenuItemDataChangeHandler;
import org.iplass.adminconsole.client.metadata.ui.menu.item.event.MenuItemDataChangedEvent;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.MenuTree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * メニューツリー編集パネル
 *
 * メニューツリー編集の最上位パネルです。
 * {@link org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab}により生成されます。
 *
 */
public class MenuEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** 編集対象MenuTree */
	private MenuTree curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	/** 個別属性部分 */
	private MenuAttributePane menuAttrPane;


	/**
	 * コンストラクタ
	 *
	 * @param defName 選択Menu定義名
	 */
	public MenuEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		service = MetaDataServiceFactory.get();

		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new CancelClickHandler());

		headerPane.setHistoryClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection(targetNode, MenuTree.class);

		//個別属性
		menuAttrPane = new MenuAttributePane();

		//Section設定
		SectionStackSection menuSection = createSection("Menu Attribute", menuAttrPane);
		setMainSections(commonSection, menuSection);

		//配置
		addMember(headerPane);
		addMember(mainStack);

		//表示データの取得
		initializeData();
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {
		//エラーのクリア
		commonSection.clearErrors();
		menuAttrPane.clearErrors();

		//MenuTreeの検索
		service.getDefinitionEntry(TenantInfoHolder.getId(), MenuTree.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {
			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry menuTree) {
				//画面に反映
				setDefinition(menuTree);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(MenuTree.class.getName(), defName, this);
	}


	/**
	 * 対象のMenuTreeをレイアウト表示パネルに設定します。
	 *
	 * @param menuTree MenuTree
	 */
	private void setDefinition(DefinitionEntry menuTree) {
		this.curDefinition = (MenuTree) menuTree.getDefinition();
		this.curVersion = menuTree.getDefinitionInfo().getVersion();
		this.curDefinitionId = menuTree.getDefinitionInfo().getObjDefId();

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setDescription(curDefinition.getDescription());

		menuAttrPane.setDefinition(curDefinition);

	}

	/**
	 * ツリーの更新処理
	 *
	 * @param editMenuTree 更新対象MenuTree
	 */
	private void updateMenuTree(final MenuTree definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateMenuTree(definition, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				updateComplete(definition);
			}
		});
	}

	/**
	 * 更新完了処理
	 */
	private void updateComplete(MenuTree definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeEditPane_menuInfoSave"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();

		//ツリー再表示
		plugin.refreshWithSelect(definition.getName(), new AsyncCallback<MetaDataItemMenuTreeNode>() {
			@Override
			public void onSuccess(MetaDataItemMenuTreeNode result) {
				headerPane.setTargetNode(result);
				commonSection.setTargetNode(result);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean menuValidate = menuAttrPane.validate();
			if (!commonValidate || !menuValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeEditPane_saveMenuComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						MenuTree definition = new MenuTree();
						definition.setName(commonSection.getName());
						definition.setDisplayName(commonSection.getDisplayName());
						definition.setDescription(commonSection.getDescription());
						definition = menuAttrPane.getEditDefinition(definition);

						updateMenuTree(definition, true);
					}
				}
			});
		}
	}

	/**
	 * キャンセルボタンイベント
	 */
	private final class CancelClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeEditPane_cancelConfirmComment")
					, new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if (value) {
						initializeData();
						commonSection.refreshSharedConfig();
					}
				}
			});
		}
	}

	public class MenuAttributePane extends VLayout {

		private static final String EXPAND_ICON = "[SKIN]/actions/sort_descending.png";
		private static final String CONTRACT_ICON = "[SKIN]/actions/sort_ascending.png";

		DynamicForm form;
		/** 表示順序 */
		private IntegerItem displayOrderField;

		/** ツリー部分 */
		private MenuTreeGrid treeGrid;

		/** メニューアイテム部分 */
		private MenuItemDragPane dragPane;

		public MenuAttributePane() {
			setOverflow(Overflow.AUTO);	//Stack上の表示領域が小さい場合にスクロールができるようにAUTO設定

			setWidth100();

			displayOrderField = new IntegerItem();
			displayOrderField.setTitle("Display Order");
			SmartGWTUtil.addHoverToFormItem(displayOrderField, AdminClientMessageUtil.getString("ui_metadata_menu_MenuTreeGrid_displayOrder"));

			form = new DynamicForm();
			form.setItems(displayOrderField);

			//Tree構成編集部分
			VLayout treeGridPane = new VLayout();
			treeGridPane.setShowResizeBar(true);
			treeGridPane.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、次を収縮

			SectionStack treeHeaderStack = new SectionStack();
			SectionStackSection treeHeaderSection = new SectionStackSection("Menu Tree");
			treeHeaderSection.setExpanded(true);
			treeHeaderSection.setCanCollapse(false);	//CLOSE不可

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
					treeGrid.expandAll();
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
					treeGrid.expandRoot();
				}
			});
			treeHeaderSection.setControls(expandAllButton, contractAllButton);
			treeHeaderStack.addSection(treeHeaderSection);

			treeGrid = new MenuTreeGrid(this);
			treeHeaderSection.addItem(treeGrid);

			treeGridPane.addMember(treeHeaderStack);

			//TreeItem選択部分
			dragPane = new MenuItemDragPane();
			dragPane.addMenuItemDataChangeHandler(new MenuItemDataChangeHandler() {

				@Override
				public void onDataChanged(MenuItemDataChangedEvent event) {
					// メニューアイテム変更処理
					menuItemDataChanged(event);
				}
			});

			HLayout layout = new HLayout();
			layout.addMember(treeGridPane);
			layout.addMember(dragPane);

			addMember(form);
			addMember(layout);
		}

		public void setDefinition(MenuTree definition) {
			displayOrderField.setValue(definition.getDisplayOrder());
			treeGrid.setMenuTree(definition);
		}

		public MenuTree getEditDefinition(MenuTree definition) {
			definition.setDisplayOrder(SmartGWTUtil.getIntegerValue(displayOrderField));
			definition.setMenuItems(treeGrid.getEditMenuItems());
			return definition;
		}

		public boolean validate() {
			return form.validate();
		}

		public void clearErrors() {
		}

		/**
		 * メニューアイテムの更新反映処理
		 *
		 * @param event メニューアイテム変更イベント
		 */
		private void menuItemDataChanged(MenuItemDataChangedEvent event) {
			//追加時は問題なし
			if (MenuItemDataChangedEvent.Type.UPDATE.equals(event.getType())) {
				//変更時はNode反映
				MenuItem menuItem = event.getValueObject(MenuItem.class);
				updateMenuItemNode(menuItem);
			} else if (MenuItemDataChangedEvent.Type.DELETE.equals(event.getType())) {
				//削除時はNode削除
				MenuItem menuItem = event.getValueObject(MenuItem.class);
				deleteMenuItemNode(menuItem);
			}
		}

		/**
		 * メニューアイテムの変更処理
		 *
		 * @param updateItem 更新MenuItem
		 */
		private void updateMenuItemNode(MenuItem updateItem) {
			treeGrid.updateMenuItemNode(curDefinition, updateItem);
		}

		/**
		 * メニューアイテムの削除処理
		 *
		 * @param deleteItem 削除MenuItem
		 */
		private void deleteMenuItemNode(MenuItem deleteItem) {
			treeGrid.deleteMenuItemNode(deleteItem);
		}


		/**
		 * MenuTree上からメニューアイテム編集ダイアログを表示します。
		 *
		 * @param type メニューアイテムタイプ
		 * @param menuItem 更新メニューアイテム
		 */
		public void showMenuItemDialog(MenuItemTreeDS.MenuItemType type, MenuItem menuItem) {
			MenuItemDialog dialog = new MenuItemDialog(type);
			dialog.addMenuItemDataChangeHandler(new MenuItemDataChangeHandler() {

				@Override
				public void onDataChanged(MenuItemDataChangedEvent event) {
					menuItemDataChanged(event);

					//アイテム一覧も更新する必要がある
					dragPane.refresh();
				}
			});
			dialog.setMenuItem(menuItem, false);
			dialog.show();
		}
	}

}
