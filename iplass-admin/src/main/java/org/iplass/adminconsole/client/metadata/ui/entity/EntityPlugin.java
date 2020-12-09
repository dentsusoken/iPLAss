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

package org.iplass.adminconsole.client.metadata.ui.entity;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.event.ViewEntityDataEvent;
import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.ContentSelectedEvent;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.entity.filter.FilterEditPanel;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.BulkLayoutPanel;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.DetailLayoutPanel;
import org.iplass.adminconsole.client.metadata.ui.entity.layout.SearchLayoutPanel;
import org.iplass.adminconsole.client.metadata.ui.entity.viewcontrol.ViewControlPanel;
import org.iplass.adminconsole.client.metadata.ui.entity.webapi.EntityWebApiEditPanel;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataInfo;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.view.filter.EntityFilter;
import org.iplass.mtp.view.generic.EntityView;
import org.iplass.mtp.webapi.definition.EntityWebApiDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;


public class EntityPlugin extends DefaultMetaDataPlugin {

	/** カテゴリ名 */
	private static final String CATEGORY_NAME = MetaDataConstants.META_CATEGORY_DATA_MODEL;

	/** ノード名 */
	private static final String NODE_NAME = "Entity";

	/** ノードアイコン（TreeGridの場合[SKIN]だと「TreeGrid」が含まれるため[SKINIMG]を使用）  */
	private static final String NODE_ICON = "database.png";

	private static final String TAB_SUB_NAME_WEBAPI = "EntityWebAPI";
	private static final String CONTEXT_MENU_ICON_WEBAPI = "[SKINIMG]/actions/column_preferences.png";

	private static final String TAB_SUB_NAME_DETAIL_LAYOUT = "Detail_Layout";
	private static final String CONTEXT_MENU_ICON_DETAIL_LAYOUT = "cube_green.png";

	private static final String TAB_SUB_NAME_SEARCH_LAYOUT = "Search_Layout";
	private static final String CONTEXT_MENU_ICON_SEARCH_LAYOUT = "cube_yellow.png";

	private static final String TAB_SUB_NAME_BULK_LAYOUT = "Bulk_Layout";
	private static final String CONTEXT_MENU_ICON_BULK_LAYOUT = "cube_blue.png";

	private static final String TAB_SUB_NAME_VIEW_CONTROL = "View_Control";
	private static final String CONTEXT_MENU_ICON_VIEW_CONTROL = "application_cascade.png";

	private static final String TAB_SUB_NAME_FILTER = "Filter";
	private static final String CONTEXT_MENU_ICON_FILTER = "database_table.png";

	private static final String CONTEXT_MENU_ICON_DATA_VIEW = "database_gear.png";

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
		return AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_entity");
	}

	@Override
	protected String nodeIcon() {
		return NODE_ICON;
	}

	@Override
	protected String definitionClassName() {
		return EntityDefinition.class.getName();
	}

	/**
	 * （カスタマイズ）アイテムノード選択時のコンテキストメニューをカスタマイズします。
	 */
	@Override
	public void onNodeContextClick(final AdminMenuTreeNode node) {
		if (isNodeTypeRoot(node)){

			if (rootContextMenu == null) {
				rootContextMenu = new Menu();

				MenuItem newMenuItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_createEntity"), MetaDataConstants.CONTEXT_MENU_ICON_ADD);
				newMenuItem.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						itemCreateAction("");
					}
				});

				//カスタマイズ部分START

				MenuItem webAPIMenuItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_openEntityWebApi"), CONTEXT_MENU_ICON_WEBAPI);
				webAPIMenuItem.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						webAPIOpenAction();
					}
				});

				//カスタマイズ部分END

				MenuItem refreshMenuItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_refresh"), MetaDataConstants.CONTEXT_MENU_ICON_REFRESH);
				refreshMenuItem.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						refresh();
					}
				});
				// 右クリックメニュー項目を設定
				rootContextMenu.setItems(newMenuItem, webAPIMenuItem, refreshMenuItem);
			}
			owner.setContextMenu(rootContextMenu);

		} else if (isNodeTypeFolder(node)) {
			if (folderContextMenu == null) {
				folderContextMenu = new Menu();
			}

			MenuItem newMenuItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_createEntity"), MetaDataConstants.CONTEXT_MENU_ICON_ADD);
			newMenuItem.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					//Entityは「.」区切り
					itemCreateAction(getFolderPath(node).replace("/", "."));
				}
			});
			MenuItem refreshMenuItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_refresh"), MetaDataConstants.CONTEXT_MENU_ICON_REFRESH);
			refreshMenuItem.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					refresh();
				}
			});
			// 右クリックメニュー項目を設定
			folderContextMenu.setItems(newMenuItem, refreshMenuItem);
			owner.setContextMenu(folderContextMenu);

		} else if (isNodeTypeItem(node)) {

			if (itemContextMenu == null) {
				//Item選択時コンテキスト
				itemContextMenu = new Menu();
			}

			// 右クリックメニュー項目1
			MenuItem editItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_openEntity"), nodeIcon());
			editItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
//					itemOpenAction(node);
					itemOpenAction((MetaDataItemMenuTreeNode)node);
				}
			});

			//カスタマイズ部分START

			// 右クリックメニュー項目2
			MenuItem detailItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_openDetaiLayout"), CONTEXT_MENU_ICON_DETAIL_LAYOUT);
			detailItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					getDetailEntityViewTreeNode((MetaDataItemMenuTreeNode)node);
//					detailOpenAction((MetaDataItemMenuTreeNode)node);
				}
			});

			// 右クリックメニュー項目3
			MenuItem searchItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_openSearchLayout"), CONTEXT_MENU_ICON_SEARCH_LAYOUT);
			searchItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					getSearchEntityViewTreeNode((MetaDataItemMenuTreeNode)node);
//					searchOpenAction((MetaDataItemMenuTreeNode)node);
				}
			});

			// 右クリックメニュー項目4
			MenuItem bulkItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_openBulkLayout"), CONTEXT_MENU_ICON_BULK_LAYOUT);
			bulkItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					getBulkEntityViewTreeNode((MetaDataItemMenuTreeNode)node);
				}
			});

			// 右クリックメニュー項目5
			MenuItem viewControlItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_openViewControl"), CONTEXT_MENU_ICON_VIEW_CONTROL);
			viewControlItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					getViewControlEntityViewTreeNode((MetaDataItemMenuTreeNode)node);
				}
			});

			// 右クリックメニュー項目6
			MenuItem filterItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_openFilter"), CONTEXT_MENU_ICON_FILTER);
			filterItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					getFilterEntityViewTreeNode((MetaDataItemMenuTreeNode)node);
//					filterOpenAction((MetaDataItemMenuTreeNode)node);
				}
			});

			// 右クリックメニュー項目7
			MenuItem renameItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_renameEntity"), MetaDataConstants.CONTEXT_MENU_ICON_RENAME);
			renameItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					itemRenameAction((MetaDataItemMenuTreeNode)node);
				}
			});
			renameItem.setEnabled(((MetaDataItemMenuTreeNode)node).isCanRename());

			// 右クリックメニュー項目8
			MenuItem deleteItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_deleteEntity"), MetaDataConstants.CONTEXT_MENU_ICON_DEL);
			deleteItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					itemDeleteAction((MetaDataItemMenuTreeNode)node);
				}
			});
			deleteItem.setEnabled(((MetaDataItemMenuTreeNode)node).isCanDelete());

			// 右クリックメニュー項目9
			MenuItem copyItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_copyEntity"), MetaDataConstants.CONTEXT_MENU_ICON_COPY);
			copyItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					itemCopyAction((MetaDataItemMenuTreeNode)node);
				}
			});

			// 右クリックメニュー項目10
			MenuItem showExplorerItem = new MenuItem(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_showEntityExplorer"), CONTEXT_MENU_ICON_DATA_VIEW);
			showExplorerItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					showExplorerAction((MetaDataItemMenuTreeNode)node);
				}
			});

			//カスタマイズ部分END

			// 右クリックメニュー項目を設定
			itemContextMenu.setItems(editItem, detailItem, searchItem, bulkItem, viewControlItem, filterItem, renameItem, deleteItem, copyItem, showExplorerItem);
			owner.setContextMenu(itemContextMenu);
		}
	}


	/**
	 * <p>（カスタマイズ）EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAMEを除去します。</p>
	 *
	 */
	@Override
	protected boolean isVisibleItem(MetaTreeNode item) {
		if (item.getName().equals(EntityDefinition.SYSTEM_DEFAULT_DEFINITION_NAME)) {
			return false;
		}
		return true;
	}

	/**
	 * <p>（カスタマイズ）Pathはピリオド。</p>
	 */
	@Override
	protected boolean isPathSlash() {
		return false;
	}

	@Override
	protected void itemCreateAction(String folderPath) {
		// 新規Entity作成
		CreateEntityDialog dialog = new CreateEntityDialog(definitionClassName(), nodeDisplayName(), folderPath, false);
		//Entity名は特殊のためダイアログ側で設定
		//dialog.setNamePolicy(isPathSlash(), isNameAcceptPeriod());
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
		CreateEntityDialog dialog = new CreateEntityDialog(definitionClassName(), nodeDisplayName(), "", true);
		//Entity名は特殊のためダイアログ側で設定
		//dialog.setNamePolicy(isPathSlash(), isNameAcceptPeriod());
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
		service.deleteEntityDefinition(TenantInfoHolder.getId(), itemNode.getDefName(), new AsyncCallback<AdminDefinitionModifyResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// 失敗時
				SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedDeleteEntity") + caught.getMessage());
			}
			@Override
			public void onSuccess(AdminDefinitionModifyResult result) {
				if (result.isSuccess()) {
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_completion"),
							AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_deleteEntityComp"));

					refresh();
					removeTab(itemNode);
				} else {
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedDeleteEntity") + result.getMessage());
				}
			}
		});
	}

	/**
	 * <p>（カスタマイズ）選択ノードに対するタブが表示されているかを返します。</p>
	 *
	 * @param itemNode 選択Node
	 * @return true：表示あり
	 */
	@Override
	protected boolean existTab(MetaDataItemMenuTreeNode itemNode) {
		return super.existTab(itemNode)
				|| workspace.existTab(itemNode.getDefName(), TAB_SUB_NAME_DETAIL_LAYOUT)
				|| workspace.existTab(itemNode.getDefName(), TAB_SUB_NAME_SEARCH_LAYOUT)
				|| workspace.existTab(itemNode.getDefName(), TAB_SUB_NAME_BULK_LAYOUT)
				|| workspace.existTab(itemNode.getDefName(), TAB_SUB_NAME_VIEW_CONTROL)
				|| workspace.existTab(itemNode.getDefName(), TAB_SUB_NAME_FILTER);
	}

	/**
	 * <p>（カスタマイズ）選択ノードに対するタブを削除します。</p>
	 *
	 * @param itemNode 選択Node
	 */
	@Override
	protected void removeTab(MetaDataItemMenuTreeNode itemNode) {
		super.removeTab(itemNode);
		workspace.removeTab(itemNode.getDefName(), TAB_SUB_NAME_DETAIL_LAYOUT);
		workspace.removeTab(itemNode.getDefName(), TAB_SUB_NAME_SEARCH_LAYOUT);
		workspace.removeTab(itemNode.getDefName(), TAB_SUB_NAME_BULK_LAYOUT);
		workspace.removeTab(itemNode.getDefName(), TAB_SUB_NAME_VIEW_CONTROL);
		workspace.removeTab(itemNode.getDefName(), TAB_SUB_NAME_FILTER);
	}

	@Override
	protected MetaDataMainEditPane workSpaceContents(MetaDataItemMenuTreeNode itemNode) {
		return new EntityEditPane(itemNode, this);
	}

	@Override
	protected Class<?>[] workspaceContentsPaneClass() {
		return new Class[]{EntityEditPane.class,
				DetailLayoutPanel.class, SearchLayoutPanel.class, BulkLayoutPanel.class,
				FilterEditPanel.class, ViewControlPanel.class,
				EntityWebApiEditPanel.class};
	}

	/**
	 * <p>（カスタマイズ）アイテムノードの「{@link #TAB_SUB_NAME_DETAIL_LAYOUT}」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>{@link DetailLayoutPanel}タブを追加します。</p>
	 *
	 * @param itemNode 選択Node
	 */
	private void detailOpenAction(MetaDataItemMenuTreeNode itemNode) {
		DetailLayoutPanel detail = GWT.create(DetailLayoutPanel.class);
		detail.setTarget(itemNode, this);
		workspace.addTab(itemNode.getDefName(), CONTEXT_MENU_ICON_DETAIL_LAYOUT, TAB_SUB_NAME_DETAIL_LAYOUT , (Canvas)detail);
	}

	/**
	 * <p>（カスタマイズ）アイテムノードの「{@link #TAB_SUB_NAME_SEARCH_LAYOUT}」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>{@link SearchLayoutPanel}タブを追加します。</p>
	 *
	 * @param itemNode 選択Node
	 */
	private void searchOpenAction(MetaDataItemMenuTreeNode itemNode) {
		SearchLayoutPanel search = GWT.create(SearchLayoutPanel.class);
		search.setTarget(itemNode, this);
		workspace.addTab(itemNode.getDefName(), CONTEXT_MENU_ICON_SEARCH_LAYOUT, TAB_SUB_NAME_SEARCH_LAYOUT , (Canvas)search);
	}

	private void bulkOpenAction(MetaDataItemMenuTreeNode itemNode) {
		BulkLayoutPanel bulk = GWT.create(BulkLayoutPanel.class);
		bulk.setTarget(itemNode, this);
		workspace.addTab(itemNode.getDefName(), CONTEXT_MENU_ICON_BULK_LAYOUT, TAB_SUB_NAME_BULK_LAYOUT , (Canvas)bulk);
	}

	/**
	 * <p>（カスタマイズ）アイテムノードの「{@link #TAB_SUB_NAME_VIEW_CONTROL}」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>{@link ViewControlPanel}タブを追加します。</p>
	 *
	 * @param itemNode 選択Node
	 */
	private void viewControlOpenAction(MetaDataItemMenuTreeNode itemNode) {
		ViewControlPanel viewControl = GWT.create(ViewControlPanel.class);
		viewControl.setTarget(itemNode, this);
		workspace.addTab(itemNode.getDefName(), CONTEXT_MENU_ICON_VIEW_CONTROL, TAB_SUB_NAME_VIEW_CONTROL , (Canvas) viewControl);
	}

	/**
	 * <p>（カスタマイズ）アイテムノードの「Show Data Explorer」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>EntityExplorerタブを追加します。</p>
	 *
	 * @param itemNode 選択Node
	 */
	private void showExplorerAction(MetaDataItemMenuTreeNode itemNode) {
		ViewEntityDataEvent.fire(itemNode.getDefName(), AdminConsoleGlobalEventBus.getEventBus());
	}

	/**
	 * <p>（カスタマイズ）ルートノードの「{@link #TAB_SUB_NAME_WEBAPI}」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>{@link EntityWebApiEditPanel}タブを追加します。</p>
	 *
	 * @param node 選択Node
	 */
	private void webAPIOpenAction() {
		EntityWebApiEditPanel webapi = GWT.create(EntityWebApiEditPanel.class);
		workspace.addTab(TAB_SUB_NAME_WEBAPI, CONTEXT_MENU_ICON_WEBAPI, TAB_SUB_NAME_WEBAPI , (Canvas)webapi);
	}

	/**
	 * <p>（カスタマイズ）アイテムノードの「{@link #TAB_SUB_NAME_FILTER}」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>{@link FilterEditPanel}タブを追加します。</p>
	 *
	 * @param node 選択Node
	 */
	private void filterOpenAction(MetaDataItemMenuTreeNode itemNode) {
		FilterEditPanel filter = GWT.create(FilterEditPanel.class);
		filter.setTarget(itemNode, this);
		workspace.addTab(itemNode.getDefName(), CONTEXT_MENU_ICON_FILTER, TAB_SUB_NAME_FILTER , (Canvas)filter);
	}

	/**
	 * <p>（カスタマイズ）Entity、View、Filterをサポートする</p>
	 */
	@Override
	public boolean isEditSupportMetaData(ViewMetaDataEvent event) {
		return (definitionClassName().equals(event.getDefinitionClassName())
				|| EntityView.class.getName().equals(event.getDefinitionClassName())
				|| EntityFilter.class.getName().equals(event.getDefinitionClassName())
				|| EntityWebApiDefinition.class.getName().equals(event.getDefinitionClassName()));
	}

	/**
	 * <p>（カスタマイズ）Entity、View、Filter、EntityWebAPIをサポートする</p>
	 */
	@Override
	protected void showEditPaneOnViewMetaDataEvent(final ViewMetaDataEvent event) {
		//念のためSupportチェック
		if (!isEditSupportMetaData(event)) {
			return;
		}

		if (definitionClassName().equals(event.getDefinitionClassName())) {
			addTabOnViewMetaDataEvent(event);
		} else if (EntityView.class.getName().equals(event.getDefinitionClassName())) {
			//定義名に一致するTreeNodeを取得
			service.getMetaDataInfo(TenantInfoHolder.getId(), EntityView.class.getName(), event.getDefinitionName(), new AsyncCallback<MetaDataInfo>() {

				@Override
				public void onSuccess(MetaDataInfo result) {
					if (result != null) {
						MetaDataItemMenuTreeNode node = createItemNode(result);
						//とりあえず両方
						detailOpenAction(node);
						searchOpenAction(node);
					} else {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
								AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
								+ event.getDefinitionName() + AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_canNotGet"));
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error!", caught);
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
							AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
							+ caught.getMessage());
				}
			});
		} else if (EntityFilter.class.getName().equals(event.getDefinitionClassName())) {
			//定義名に一致するTreeNodeを取得
			service.getMetaDataInfo(TenantInfoHolder.getId(), EntityFilter.class.getName(), event.getDefinitionName(), new AsyncCallback<MetaDataInfo>() {

				@Override
				public void onSuccess(MetaDataInfo result) {
					if (result != null) {
						filterOpenAction(createItemNode(result));
					} else {
						SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
								AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
								+ event.getDefinitionName() + AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_canNotGet"));
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error!", caught);
					SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
							AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
							+ caught.getMessage());
				}
			});
		} else if (EntityWebApiDefinition.class.getName().equals(event.getDefinitionClassName())) {
			webAPIOpenAction();
		}
	}

	/**
	 * <p>（カスタマイズ）EntityWebAPIの場合はEntityノードを選択する。</p>
	 *
	 * @param event {@link ContentSelectedEvent}
	 */
	@Override
	public void onContentSelected(ContentSelectedEvent event){
		if (event.getSource() instanceof EntityWebApiEditPanel) {
			selectAndScrollNode(rootNode);
		} else if (event.getSource() instanceof DetailLayoutPanel
				|| event.getSource() instanceof SearchLayoutPanel
				|| event.getSource() instanceof BulkLayoutPanel
				|| event.getSource() instanceof FilterEditPanel
				|| event.getSource() instanceof ViewControlPanel) {
			//defNameの区切りを/から.に変換
			selectDefNameNode(event.getDefinitionName().replace("/", "."), null);
		} else {
			super.onContentSelected(event);
		}
	};

	private void getDetailEntityViewTreeNode(final MetaDataItemMenuTreeNode entityNode) {
		service.getMetaDataInfo(TenantInfoHolder.getId(), EntityView.class.getName(), entityNode.getDefName(), new AsyncCallback<MetaDataInfo>() {

			@Override
			public void onSuccess(MetaDataInfo result) {
				if (result != null) {
					MetaDataItemMenuTreeNode node = createItemNode(result);
					detailOpenAction(node);
				} else {
					//定義なしの場合は、EntityNodeから作成
					MetaDataItemMenuTreeNode node = copyItemNode(entityNode, false);
					detailOpenAction(node);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!", caught);
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
						AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
						+ caught.getMessage());
			}
		});
	}

	private void getSearchEntityViewTreeNode(final MetaDataItemMenuTreeNode entityNode) {

		service.getMetaDataInfo(TenantInfoHolder.getId(), EntityView.class.getName(), entityNode.getDefName(), new AsyncCallback<MetaDataInfo>() {

			@Override
			public void onSuccess(MetaDataInfo result) {
				if (result != null) {
					MetaDataItemMenuTreeNode node = createItemNode(result);
					searchOpenAction(node);
				} else {
					//定義なしの場合は、EntityNodeから作成
					MetaDataItemMenuTreeNode node = copyItemNode(entityNode, false);
					searchOpenAction(node);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!", caught);
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
						AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
						+ caught.getMessage());
			}

		});
	}

	private void getBulkEntityViewTreeNode(final MetaDataItemMenuTreeNode entityNode) {

		service.getMetaDataInfo(TenantInfoHolder.getId(), EntityView.class.getName(), entityNode.getDefName(), new AsyncCallback<MetaDataInfo>() {

			@Override
			public void onSuccess(MetaDataInfo result) {
				if (result != null) {
					MetaDataItemMenuTreeNode node = createItemNode(result);
					bulkOpenAction(node);
				} else {
					//定義なしの場合は、EntityNodeから作成
					MetaDataItemMenuTreeNode node = copyItemNode(entityNode, false);
					bulkOpenAction(node);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!", caught);
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
						AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
						+ caught.getMessage());
			}

		});
	}

	private void getViewControlEntityViewTreeNode(final MetaDataItemMenuTreeNode entityNode) {

		service.getMetaDataInfo(TenantInfoHolder.getId(), EntityView.class.getName(), entityNode.getDefName(), new AsyncCallback<MetaDataInfo>() {

			@Override
			public void onSuccess(MetaDataInfo result) {
				if (result != null) {
					MetaDataItemMenuTreeNode node = createItemNode(result);
					viewControlOpenAction(node);
				} else {
					//定義なしの場合は、EntityNodeから作成
					MetaDataItemMenuTreeNode node = copyItemNode(entityNode, false);
					viewControlOpenAction(node);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!", caught);
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
						AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
						+ caught.getMessage());
			}

		});
	}

	private void getFilterEntityViewTreeNode(final MetaDataItemMenuTreeNode entityNode) {
		service.getMetaDataInfo(TenantInfoHolder.getId(), EntityFilter.class.getName(), entityNode.getDefName(), new AsyncCallback<MetaDataInfo>() {

			@Override
			public void onSuccess(MetaDataInfo result) {
				if (result != null) {
					MetaDataItemMenuTreeNode node = createItemNode(result);
					filterOpenAction(node);
				} else {
					//定義なしの場合は、EntityNodeから作成
					MetaDataItemMenuTreeNode node = copyItemNode(entityNode, false);
					filterOpenAction(node);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!", caught);
				SC.say(AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failed"),
						AdminClientMessageUtil.getString("ui_metadata_entity_EntityPluginManager_failedGetScreenInfo")
						+ caught.getMessage());
			}

		});
	}

}
