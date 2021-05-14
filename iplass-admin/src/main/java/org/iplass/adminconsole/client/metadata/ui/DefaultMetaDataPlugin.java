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

package org.iplass.adminconsole.client.metadata.ui;

import org.iplass.adminconsole.client.base.event.AdminConsoleGlobalEventBus;
import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.event.RefreshMetaDataEvent;
import org.iplass.adminconsole.client.base.event.ViewMetaDataEvent;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.plugin.AdminPlugin;
import org.iplass.adminconsole.client.base.plugin.ContentSelectedEvent;
import org.iplass.adminconsole.client.base.plugin.DefaultAdminPlugin;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.layout.AdminMenuTreeNode;
import org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataRenameDialog;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataInfo;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tree.TreeNode;


public abstract class DefaultMetaDataPlugin extends DefaultAdminPlugin implements MetaDataPlugin {

//	private MetaDataServiceAsync service = GWT.create(MetaDataService.class);
	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	/**
	 * <p>ノード名を返します。</p>
	 *
	 * <p>ノード名にはアルファベットで指定してください。またブランクを含まないでください。</p>
	 *
	 * @return ノード名
	 */
	protected abstract String nodeName();

	/**
	 * <p>ノード表示名を返します。</p>
	 *
	 * @return ノード表示名
	 */
	protected abstract String nodeDisplayName();

	/**
	 * <p>ノードアイコン（パス）を返します。</p>
	 *
	 * <p>TreeGridの場合[SKIN]だと「TreeGrid」が含まれるため[SKINIMG]を使用してください。</p>
	 *
	 * @return ノードアイコン（パス）
	 */
	protected abstract String nodeIcon();

	/**
	 * <p>MetaDataに対応するDefinitionのクラス名を返します。</p>
	 *
	 * @return Definitionのクラス名
	 */
	protected abstract String definitionClassName();

	/**
	 * <p>ルートノードの「追加」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>追加処理がない場合は、{@link #onNodeContextClick(AdminMenuTreeNode)} をオーバライドして、
	 * コンテキストメニューをカスタマイズしてください。
	 * その際、このメソッドはブランク実装で構いません。</p>
	 */
	protected abstract void itemCreateAction(final String folderPath);

	/**
	 * <p>アイテムノードの「コピー」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>「コピー」処理がない場合は、{@link #onNodeContextClick(AdminMenuTreeNode)} をオーバライドして、
	 * コンテキストメニューをカスタマイズしてください。
	 * その際、このメソッドはブランク実装で構いません。</p>
	 */
	protected abstract void itemCopyAction(final MetaDataItemMenuTreeNode itemNode);

	/**
	 * <p>アイテムノードの「削除」コンテキストメニュー選択時の実際の削除更新処理です。</p>
	 *
	 * <p>「削除」処理がない場合は、{@link #onNodeContextClick(AdminMenuTreeNode)} をオーバライドして、
	 * コンテキストメニューをカスタマイズしてください。
	 * その際、このメソッドはブランク実装で構いません。</p>
	 */
	protected abstract void itemDelete(final MetaDataItemMenuTreeNode itemNode);

	/**
	 * <p>ワークスペースに追加するタブを返します。</p>
	 *
	 * <p>各プラグインごとのタブクラスのインスタンスを返してください。</p>
	 */
	protected abstract MetaDataMainEditPane workSpaceContents(final MetaDataItemMenuTreeNode itemNode);

	/**
	 * <p>ワークスペースに表示するコンテンツのクラス返します。</p>
	 *
	 * <p>このメソッドはタブが選択された際に、そのタブがプラグインの対象コンテンツかを
	 * 判断する際に利用します。<br/>
	 * このメソッドで返すクラスと一致した場合は、対象のタブがこのプラグインによって
	 * 表示されたコンテンツと判断します。</p>
	 *
	 * <p>各プラグインごとのタブに表示されるコンテンツのMainクラスを返してください。</p>
	 */
	protected abstract Class<?>[] workspaceContentsPaneClass();

	/** RootNode */
	protected AdminMenuTreeNode rootNode;

	/** Nodeの初期化フラグ */
	protected boolean isInitializeNode = false;

	/** Root用コンテキストメニュー */
	protected Menu rootContextMenu;

	/** Folder用コンテキストメニュー */
	protected Menu folderContextMenu;

	/** Item用コンテキストメニュー */
	protected Menu itemContextMenu;

	public DefaultMetaDataPlugin() {

		//ViewMetaDataEventの登録
		AdminConsoleGlobalEventBus.addHandler(ViewMetaDataEvent.TYPE, new ViewMetaDataEvent.ViewMetaDataHandler() {

			@Override
			public void onViewMetaData(ViewMetaDataEvent event) {
				if (isEditSupportMetaData(event)) {
					showEditPaneOnViewMetaDataEvent(event);
				}
			}
		});

		//RefreshMetaDataEventの登録
		AdminConsoleGlobalEventBus.addHandler(RefreshMetaDataEvent.TYPE, new RefreshMetaDataEvent.RefreshMetaDataHandler() {

			@Override
			public void onRefresh(RefreshMetaDataEvent event) {
				refresh();
			}
		});
	}

	@Override
	public AdminMenuTreeNode createPluginRootNode() {
		rootNode = new AdminMenuTreeNode(nodeName(), nodeIcon(), nodeTypeRoot());
		return rootNode;
	}

	/**
	 * <p>ノードがダブルクリックされた際の処理を実装します。</p>
	 *
	 * <p>ノードがアイテムノードの場合、ワークスペースにタブを追加します。</br>
	 * ノードがルート、フォルダの場合、なにもしません。</p>
	 *
	 * @see AdminPlugin#onNodeDoubleClick(AdminMenuTreeNode)
	 */
	@Override
	public void onNodeDoubleClick(AdminMenuTreeNode node) {
		if (isNodeTypeItem(node)){
			addTab((MetaDataItemMenuTreeNode)node);
		}
	}

	/**
	 * <p>ノードコンテキストがクリックされた際の処理を実装します。</p>
	 *
	 * <p>ルートノード、フォルダノードに対しては、「作成」「リフレッシュ」メニューを表示します。</br>
	 * アイテムノードに対しては、「開く」「削除」「コピー」メニューを表示します。</p>
	 *
	 * @see AdminPlugin#onNodeContextClick(AdminMenuTreeNode)
	 */
	@Override
	public void onNodeContextClick(final AdminMenuTreeNode node) {



		if (isNodeTypeRoot(node)){

			if (rootContextMenu == null) {
				rootContextMenu = new Menu();

				MenuItem newMenuItem = new MenuItem(rs("ui_metadata_DefaultMetaDataPluginManager_create", nodeDisplayName()), MetaDataConstants.CONTEXT_MENU_ICON_ADD);
				newMenuItem.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						itemCreateAction("");
					}
				});
				MenuItem refreshMenuItem = new MenuItem(rs("ui_metadata_DefaultMetaDataPluginManager_refreshMetadataList"), MetaDataConstants.CONTEXT_MENU_ICON_REFRESH);
				refreshMenuItem.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						refresh();
					}
				});
				// 右クリックメニュー項目を設定
				rootContextMenu.setItems(newMenuItem, refreshMenuItem);
			}
			owner.setContextMenu(rootContextMenu);

		} else if (isNodeTypeFolder(node)) {

			if (folderContextMenu == null) {
				folderContextMenu = new Menu();
			}

			//nodeを参照するため毎回作り直し

			MenuItem newMenuItem = new MenuItem(rs("ui_metadata_DefaultMetaDataPluginManager_create", nodeDisplayName()), MetaDataConstants.CONTEXT_MENU_ICON_ADD);
			newMenuItem.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					itemCreateAction(getFolderPath(node));
				}
			});
			MenuItem refreshMenuItem = new MenuItem(rs("ui_metadata_DefaultMetaDataPluginManager_refreshMetadataList"), MetaDataConstants.CONTEXT_MENU_ICON_REFRESH);
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
				// Item選択時コンテキスト
				itemContextMenu = new Menu();
			}

			//nodeを参照するため毎回作り直し

			// 右クリックメニュー項目1
			MenuItem editItem = new MenuItem(rs("ui_metadata_DefaultMetaDataPluginManager_open", nodeDisplayName()), nodeIcon());
			editItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					itemOpenAction((MetaDataItemMenuTreeNode)node);
				}
			});

			// 右クリックメニュー項目2
			MenuItem renameItem = new MenuItem(rs("ui_metadata_DefaultMetaDataPluginManager_rename", nodeDisplayName()), MetaDataConstants.CONTEXT_MENU_ICON_RENAME);
			renameItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					itemRenameAction((MetaDataItemMenuTreeNode)node);
				}
			});
			renameItem.setEnabled(((MetaDataItemMenuTreeNode)node).isCanRename());

			// 右クリックメニュー項目3
			MenuItem deleteItem = new MenuItem(rs("ui_metadata_DefaultMetaDataPluginManager_delete", nodeDisplayName()), MetaDataConstants.CONTEXT_MENU_ICON_DEL);
			deleteItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					itemDeleteAction((MetaDataItemMenuTreeNode)node);
				}
			});
			deleteItem.setEnabled(((MetaDataItemMenuTreeNode)node).isCanDelete());

			// 右クリックメニュー項目4
			MenuItem copyItem = new MenuItem(rs("ui_metadata_DefaultMetaDataPluginManager_copy", nodeDisplayName()), MetaDataConstants.CONTEXT_MENU_ICON_COPY);
			copyItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					itemCopyAction((MetaDataItemMenuTreeNode)node);
				}
			});

			// 右クリックメニュー項目を設定
			itemContextMenu.setItems(editItem, renameItem, deleteItem, copyItem);
			owner.setContextMenu(itemContextMenu);
		}
	}

	/**
	 * <p>ノードがオープンされた際の処理を実装します。</p>
	 *
	 * <p>ノードがルートノードの場合でかつ初期検索が実行されていない場合、
	 * ノードアイテムを検索しノードを生成します。</p>
	 *
	 * @see AdminPlugin#onFolderOpened(AdminMenuTreeNode)
	 */
	@Override
	public void onFolderOpened(AdminMenuTreeNode node) {
		if (isNodeTypeRoot(node) && !isInitializeNode){
			searchMetaNode();
			tree.openFolder(node);
		}
	}

	@Override
	public void refresh() {

		//未指定の場合、リフレッシュ後にMainPaneで選択されているContentsのNodeを選択
		doRefresh(new AsyncCallback<MetaTreeNode>() {

			@Override
			public void onSuccess(MetaTreeNode result) {
				MainWorkspaceTab mainPane = MainWorkspaceTab.getInstance();
				Tab selectedTab = mainPane.getSelectedTab();
				if (selectedTab != null) {
					//選択タブがある場合は、イベントを生成して通知
					ContentSelectedEvent event = new ContentSelectedEvent(
							selectedTab.getPane(), mainPane.getTabDefName(selectedTab));
					onContentSelected(event);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("refresh failed. plugin=" + getClass().getName()
						+ ". error=" + caught.getMessage());
			}
		});
	}

	/**
	 * <p>ノード配下のリフレッシュ処理</p>
	 *
	 * <p>リフレッシュ後、callbackを呼び出します。</p>
	 *
	 * @param callback リフレッシュ処理後のCallbackメソッド
	 */
	private void doRefresh(final AsyncCallback<MetaTreeNode> callback) {
		//すでに初期化されている場合のみ作り直し
		if (isInitializeNode) {
			searchMetaNode(callback);
		} else {
			if (callback != null) {
				callback.onSuccess(null);
			}
		}
	}

	/**
	 * <p>ノードリフレッシュ処理</p>
	 *
	 * <p>リフレッシュ後、defNameに一致するNodeを選択します。</p>
	 *
	 * @param defName 選択Nodeの定義名
	 */
	public void refreshWithSelect(final String defName, final AsyncCallback<MetaDataItemMenuTreeNode> callback) {
		if (defName != null && !defName.isEmpty()) {
			doRefresh(new AsyncCallback<MetaTreeNode>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("refresh failed. plugin=" + getClass().getName()
							+ ". error=" + caught.getMessage());
				}

				@Override
				public void onSuccess(MetaTreeNode result) {
					selectDefNameNode(defName, callback);
				}
			});
		} else {
			doRefresh(null);
		}
	}

	/**
	 * <p>Workspace上のコンテンツがSelectされた際の処理を実装します。</p>
	 *
	 * <p>デフォルト実装は、選択されたPaneが {@link #workspaceContentsPaneClass()}に
	 * 含まれる場合、{@link ContentSelectedEvent#getDefinitionName()}
	 * に一致するNodeを選択します。</br>
	 * defName以外のTreeNodeを選択させたい場合は、オーバーライドしてください。
	 * </p>
	 *
	 * @param event {@link ContentSelectedEvent}
	 */
	@Override
	public void onContentSelected(ContentSelectedEvent event){
		Class<?>[] contentsClasses = workspaceContentsPaneClass();
		if (contentsClasses != null) {
			for (Class<?> contentsClass : contentsClasses) {
				if (contentsClass == event.getSource().getClass()) {
					selectDefNameNode(event.getDefinitionName(), null);
				}
			}
		}
	};

	/**
	 * <p>指定されたメタデータが編集対象MetaDataかを判定します。</p>
	 * <p>MetaDataExplorerからの直接起動時用です。</p>
	 *
	 * <p>デフォルト実装は、{@link ViewMetaDataEvent#getDefinitionClassName()}が
	 * {@link #definitionClassName()} に一致するかです。<br/>
	 * EntityやMenuなど複数のパスを対象とする場合は、オーバーライドしてください。
	 * </p>
	 *
	 * @param event イベント
	 * @return 判定結果
	 */
	@Override
	public boolean isEditSupportMetaData(ViewMetaDataEvent event) {
		return event.getDefinitionClassName().equals(definitionClassName());
	}

	protected void showEditPaneOnViewMetaDataEvent(ViewMetaDataEvent event) {
		//念のためSupportチェック
		if (!isEditSupportMetaData(event)) {
			return;
		}

		addTabOnViewMetaDataEvent(event);
	}

	/**
	 * <p>メタデータを検索し、ツリーを生成します。</p>
	 */
	protected void searchMetaNode() {
		searchMetaNode(null);
	}

	/**
	 * <p>メタデータを検索し、ツリーを生成します。</p>
	 *
	 * @param callback 検索処理後のCallbackメソッド
	 */
	protected void searchMetaNode(final AsyncCallback<MetaTreeNode> callback) {
		//RootNodeの子Nodeを削除
		tree.removeList(tree.getChildren(rootNode));

		service.getMetaDataTree(TenantInfoHolder.getId(), definitionClassName(), new AsyncCallback<MetaTreeNode>() {

			@Override
			public void onSuccess(MetaTreeNode result) {
				isInitializeNode = true;
				convertNode(rootNode, result);
				if (callback != null) {
					callback.onSuccess(result);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				if (callback != null) {
					callback.onFailure(caught);
				}
			}
		});
	}

	/**
	 * <p>{@link MetaTreeNode} を {@link AdminMenuTreeNode} に変換します。</p>
	 *
	 * <p>表示させたくないItemがある場合は、{@link #isVisibleItem(MetaTreeNode)}}
	 * をオーバーライドして制御してください。
	 * </p>
	 *
	 * @param parent 親 {@link AdminMenuTreeNode}
	 * @param treeNode 変換対象 {@link MetaTreeNode}
	 * @return 変換後 {@link AdminMenuTreeNode} の配列
	 */
	protected AdminMenuTreeNode[] convertNode(AdminMenuTreeNode parent, MetaTreeNode treeNode) {
		AdminMenuTreeNode[] entries = new AdminMenuTreeNode[treeNode.getAllNodeCount()];
		int i = 0;
		if (treeNode.getChildren() != null) {
			for (MetaTreeNode child : treeNode.getChildren()) {
				//フォルダ要素
				entries[i] = createChildNode(child);
				//entries[i].setChildren(convertNode(grid, parent, child));
				tree.add(entries[i], parent);	//node.setChildrenだとツリーのIndentがうまくいかない。treeに対して操作する
				convertNode(entries[i], child);
				i++;
			}
		}
		if (treeNode.getItems() != null) {
			for (MetaTreeNode item : treeNode.getItems()) {
				if (isVisibleItem(item)) {
					//Item要素(AdminMenuTreeNodeではなくMetaDataItemMenuTreeNodeをセット)
					entries[i] = createItemNode(item);
					entries[i].setIsFolder(false);
					tree.add(entries[i], parent);
					i++;
				}
			}
		}
		return entries;
	}

	/**
	 * <p>フォルダノードを生成します。</p>
	 *
	 * @param child アイテムノード
	 * @return フォルダノード
	 */
	protected AdminMenuTreeNode createChildNode(MetaTreeNode child) {
		return new AdminMenuTreeNode(child.getName(), null, nodeTypeFolder());
	}

	/**
	 * <p>アイテムノードを生成します。</p>
	 *
	 * @param item アイテムノード
	 * @return アイテムノード
	 */
	protected MetaDataItemMenuTreeNode createItemNode(MetaDataInfo item) {
		MetaDataItemMenuTreeNode itemNode = new MetaDataItemMenuTreeNode(item.getName(), item.getPath(),
				getItemIcon(item), nodeTypeItem(),
				item.isShared() ,item.isSharedOverwrite(),
				item.isSharable(), item.isOverwritable(), item.isDataSharable(), item.isPermissionSharable());
		itemNode.setDefinitionClassName(item.getDefinitionClassName());
		return itemNode;
	}

	/**
	 * <p>アイテムノードをコピーします。</p>
	 *
	 * @param item アイテムノード
	 * @return アイテムノード
	 */
	protected MetaDataItemMenuTreeNode copyItemNode(MetaDataItemMenuTreeNode original, boolean copySharedSettings) {
		return new MetaDataItemMenuTreeNode(original, copySharedSettings);
	}

	/**
	 * <p>アイテムノードを表示していいかを判定します。</p>
	 *
	 * <p>デフォルト実装は、全て表示可です。<br/>
	 * Entityなどで表示させたくないアイテムがある場合は、オーバーライドしてください。
	 * </p>
	 *
	 * @param item アイテムノード
	 * @return true：表示
	 */
	protected boolean isVisibleItem(MetaTreeNode item) {
		return true;
	}

	/**
	 * <p>Path定義にスラッシュを利用するかを判定します。</p>
	 *
	 * <p>デフォルト実装は、スラッシュを利用です。<br/>
	 * EntityやUtilityクラスなどピリオドを利用する場合は、オーバーライドしてください。
	 * </p>
	 * @return
	 */
	protected boolean isPathSlash() {
		return true;
	}

	/**
	 * <p>名前定義にピリオドを許すかを判定します。</p>
	 *
	 * <p>デフォルト実装は、ピリオドは許しません。<br/>
	 * ActionやTemplateなどピリオドを許す場合は、オーバーライドしてください。
	 * </p>
	 * @return
	 */
	protected boolean isNameAcceptPeriod() {
		return false;
	}

	/**
	 * <p>アイテムノードのアイコンを返します。</p>
	 *
	 * @param info メタデータ情報
	 * @return アイコン
	 */
	protected String getItemIcon(MetaDataInfo info) {
		return MetaDataUtil.getMetaTypeIcon(info.isShared(), info.isSharedOverwrite(), info.isOverwritable());
	}

	/**
	 * <p>アイテムノードの「開く」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>デフォルト実装は、選択ノードに対するタブを追加します。</p>
	 *
	 * @param itemNode 選択Node
	 * @see #addTab(AdminMenuTreeNode)
	 */
	protected void itemOpenAction(final MetaDataItemMenuTreeNode itemNode) {
		addTab(itemNode);
	}

	/**
	 * <p>アイテムノードの「削除」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>デフォルト実装は、削除確認を実施後、{@link #itemDelete(AdminMenuTreeNode)} を呼び出します。</p>
	 *
	 * @param itemNode 選択Node
	 * @see #itemDelete(AdminMenuTreeNode)
	 */
	protected void itemDeleteAction(final MetaDataItemMenuTreeNode itemNode) {

		SC.ask(rs("ui_metadata_DefaultMetaDataPluginManager_deleteConfirm"),
				rs("ui_metadata_DefaultMetaDataPluginManager_deleteConfirm2", nodeDisplayName(), itemNode.getName()), new BooleanCallback() {

			@Override
			public void execute(Boolean value) {
				if(value) {
					itemDelete(itemNode);
				}
			}
		});
	}

	/**
	 * <p>アイテムノードの「名前変更」コンテキストメニュー選択時処理です。</p>
	 *
	 * <p>デフォルト実装は、名前変更ダイアログを表示します。</p>
	 *
	 * @param itemNode 選択Node
	 */
	protected void itemRenameAction(final MetaDataItemMenuTreeNode itemNode) {

		if (existTab(itemNode)) {
			//編集中のタブが存在している場合は閉じるように確認
			SC.ask(rs("ui_metadata_DefaultMetaDataPluginManager_renameCloseEditPaneConfirm"), new BooleanCallback() {
				@Override
				public void execute(Boolean value) {
					if(value) {
						removeTab(itemNode);

						openRenameDialog(itemNode);
					}
				}
			});
		} else {
			openRenameDialog(itemNode);
		}
	}

	/**
	 * 名前変更ダイアログを表示します。
	 *
	 * @param itemNode 選択Node
	 */
	protected void openRenameDialog(final MetaDataItemMenuTreeNode itemNode) {
		final MetaDataRenameDialog dialog = new MetaDataRenameDialog(definitionClassName(), nodeDisplayName(), itemNode.getDefName(), isPathSlash(), isNameAcceptPeriod());
		dialog.addDataChangeHandler(new DataChangedHandler() {

			@Override
			public void onDataChanged(DataChangedEvent event) {
				refreshWithSelect(event.getValueName(), null);
			}
		});
		dialog.show();
	}

	/**
	 * <p>選択ノードに対するタブを追加します。</p>
	 *
	 * <p>追加するタブは {@link #workSpaceContents(AdminMenuTreeNode)} から取得します。</p>
	 *
	 * @param itemNode 選択Node
	 */
	protected void addTab(MetaDataItemMenuTreeNode itemNode) {
		workspace.addTab(itemNode.getDefName(), nodeIcon(), nodeName() , workSpaceContents(itemNode));
	}

	/**
	 * <p>選択ノードに対するタブを追加します。</p>
	 * <p>MetaDataExplorerからの直接起動時用です。</p>
	 *
	 * @param event 対象イベント
	 */
	protected void addTabOnViewMetaDataEvent(final ViewMetaDataEvent event) {
		addTabByName(event.getDefinitionName());
	}

	/**
	 * <p>選択ノードに対するタブを追加します。</p>
	 *
	 * @param defName 定義名
	 */
	protected void addTabByName(final String defName) {
		//定義名に一致するTreeNodeを取得
		service.getMetaDataInfo(TenantInfoHolder.getId(), definitionClassName(), defName, new AsyncCallback<MetaDataInfo>() {

			@Override
			public void onSuccess(MetaDataInfo result) {
				if (result != null) {
					addTab(createItemNode(result));
				} else {

					SC.say(rs("ui_metadata_DefaultMetaDataPluginManager_failed"),
							rs("ui_metadata_DefaultMetaDataPluginManager_failedToGetScreenInfoCause", defName));
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!", caught);
				SC.say(rs("ui_metadata_DefaultMetaDataPluginManager_failed"),
						rs("ui_metadata_DefaultMetaDataPluginManager_failedToGetScreenInfo")
						+ caught.getMessage());
			}
		});
	}

	/**
	 * <p>選択ノードに対するタブが表示されているかを返します。</p>
	 *
	 * @param itemNode 選択Node
	 * @return true：表示あり
	 */
	protected boolean existTab(MetaDataItemMenuTreeNode itemNode) {
		return workspace.existTab(itemNode.getDefName(), nodeName());
	}

	/**
	 * <p>選択ノードに対するタブを削除します。</p>
	 *
	 * @param itemNode 選択Node
	 */
	protected void removeTab(MetaDataItemMenuTreeNode itemNode) {
		workspace.removeTab(itemNode.getDefName(), nodeName());
	}

	/**
	 * <p>ルートノードのノードタイプ文字列を返します。</p>
	 *
	 * <p>デフォルト値は 「{@link #nodeName()} + {@link AdminPlugin#NODE_TYPE_ROOT_SUFFIX}」です。</p>
	 */
	protected String nodeTypeRoot() {
		return nodeName() + NODE_TYPE_ROOT_SUFFIX;
	}
	/**
	 * <p>ルートノードかを判定します。</p>
	 *
	 * @param node ノード
	 * @return 判定結果
	 */
	protected boolean isNodeTypeRoot(final AdminMenuTreeNode node) {
		return nodeTypeRoot().equals(node.getType());
	}

	/**
	 * <p>フォルダノードのノードタイプ文字列を返します。</p>
	 *
	 * <p>デフォルト値は 「{@link #nodeName()} + {@link AdminPlugin#NODE_TYPE_FOLDER_SUFFIX}」です。</p>
	 */
	protected String nodeTypeFolder() {
		return nodeName() + NODE_TYPE_FOLDER_SUFFIX;
	}
	/**
	 * <p>フォルダノードかを判定します。</p>
	 *
	 * @param node ノード
	 * @return 判定結果
	 */
	protected boolean isNodeTypeFolder(final AdminMenuTreeNode node) {
		return nodeTypeFolder().equals(node.getType());
	}

	/**
	 * <p>アイテムノードのノードタイプ文字列を返します。</p>
	 *
	 * <p>デフォルト値は 「{@link #nodeName()} + {@link AdminPlugin#NODE_TYPE_ITEM_SUFFIX}」です。</p>
	 */
	protected String nodeTypeItem() {
		return nodeName() + NODE_TYPE_ITEM_SUFFIX;
	}
	/**
	 * <p>アイテムノードかを判定します。</p>
	 *
	 * @param node ノード
	 * @return 判定結果
	 */
	protected boolean isNodeTypeItem(final AdminMenuTreeNode node) {
		return nodeTypeItem().equals(node.getType());
	}

	/**
	 * <p>RootNode配下のパスを返します。</p>
	 *
	 * <p>フォルダで新規作成Contextを呼び出した際のパスを取得するために利用します。</p>
	 *
	 * @param node ノード
	 * @return パス
	 */
	protected String getFolderPath(final AdminMenuTreeNode node) {
		String path = tree.getName(node);
		TreeNode[] parents = tree.getParents(node);
		for (TreeNode parent : parents) {
			if (parent.equals(rootNode)) {
				break;
			}

			path = tree.getName(parent) + "/" + path;
		}
		return path + "/";
	}

	/**
	 * <p>defNameに一致するTreeNodeを選択します。</p>
	 *
	 * @param defName 定義名
	 */
	protected void selectDefNameNode(final String defName, final AsyncCallback<MetaDataItemMenuTreeNode> callback) {
		if (tree == null || treeGrid == null || rootNode == null) {
			return;
		}

		if (!isInitializeNode) {
			//初期化されていない場合は検索（MetaDataExplolerから直接表示など）
			searchMetaNode(new AsyncCallback<MetaTreeNode>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log(defName + " node is not found.");
				}

				@Override
				public void onSuccess(MetaTreeNode result) {
					doSelectDefNameNode(defName, callback);
				}
			});
		} else {
			doSelectDefNameNode(defName, callback);
		}
	}

	/**
	 * <p>defNameに一致するTreeNodeを選択します。</p>
	 *
	 * @param defName 定義名
	 */
	protected void doSelectDefNameNode(String defName, final AsyncCallback<MetaDataItemMenuTreeNode> callback) {
		TreeNode[] allNodes = tree.getAllNodes(rootNode);
		TreeNode target = null;
		for (TreeNode node : allNodes) {
			if (node instanceof MetaDataItemMenuTreeNode) {
				if (defName.equals(((MetaDataItemMenuTreeNode)node).getDefName())) {
					target = node;
					break;
				}
			}
		}

		if (target == null) {
			GWT.log(defName + " node is not found.");
		} else {
			GWT.log(tree.getPath(target) + " node is found.");
			selectAndScrollNode(target);

			if (callback != null) {
				if (target instanceof MetaDataItemMenuTreeNode) {
					callback.onSuccess((MetaDataItemMenuTreeNode)target);
				} else {
					GWT.log("doSelectDefNameNode's target is not MetaDataItemMenuTreeNode. class=" + target.getClass().getName());
				}
			}
		}
	}

	/**
	 * <p>フォルダを選択し、表示します。</p>
	 *
	 * @param node 対象TreeNode
	 */
	protected void selectAndScrollNode(TreeNode node) {
		if (tree == null || treeGrid == null) {
			return;
		}

		treeGrid.selectAndScrollNode(node);
	}

	private String rs(String key, Object... arguments) {
		return AdminClientMessageUtil.getString(key, arguments);
	}

}
