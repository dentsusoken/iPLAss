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

package org.iplass.adminconsole.client.base.ui.layout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.plugin.AdminPlugin;
import org.iplass.adminconsole.client.base.plugin.ContentClosedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentSelectedEvent;
import org.iplass.adminconsole.client.base.plugin.ContentStateChangeHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;
import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;

/**
 * <p>AdminConsole Menu用抽象グリッドです。</p>
 *
 * <p>このクラスは個々のメニュー（Pluginとして管理）にできる限り依存しないような実装を目指しています。</p>
 *
 * <p>各メニューブロックはこのクラスを継承し、pluginsメソッドを実装してください。</p>
 *
 * <p>
 * 個々のメニューは、各メニューごとに {@link AdminPlugin} を実装したクラスを作成してください。<br/>
 * 作成した {@link AdminPlugin} 実装クラスを、{@link #plugins()} メソッドに追加してください。（依存している・・・）<br/>
 * このクラスから各 {@link AdminPlugin} に対して処理を委譲、または通知します。</p>
 *
 * @author lis70i
 *
 */
public abstract class AdminMenuTreeGrid extends MtpTreeGrid implements ContentStateChangeHandler {

//	private MetaDataServiceAsync metaService = GWT.create(MetaDataService.class);

	/** メインパネル */
	private MainWorkspaceTab mainPane;

	/** ツリーモデル */
	private Tree model;

	/** 管理対象プラグイン */
	private List<AdminPlugin> plugins;

	/**
	 * 表示対象のプラグインリストを返します。
	 *
	 * @return プラグインのリスト
	 */
	protected abstract List<AdminPlugin> plugins();

	/**
	 * コンストラクタ
	 */
	protected AdminMenuTreeGrid(MainWorkspaceTab mainPane) {
		super();

		this.mainPane = mainPane;

		setShowHeader(Boolean.FALSE);
		setLeaveScrollbarGap(Boolean.FALSE);
		setSelectionType(SelectionStyle.SINGLE);
		setAutoFetchData(Boolean.FALSE);

		addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				ListGridRecord record = getSelectedRecord();
				if (record == null) {
					return;
				}
				AdminMenuTreeNode node = (AdminMenuTreeNode)Tree.nodeForRecord(record);
				fireOnDoubleClick(node);
			}
		});

		addNodeContextClickHandler(new NodeContextClickHandler() {

			@Override
			public void onNodeContextClick(NodeContextClickEvent event) {
				AdminMenuTreeNode node = (AdminMenuTreeNode)event.getNode();
				fireOnNodeContextClick(node);
			}
		});

		addFolderOpenedHandler(new FolderOpenedHandler() {

			@Override
			public void onFolderOpened(FolderOpenedEvent event) {
				AdminMenuTreeNode node = (AdminMenuTreeNode)event.getNode();
				fireOnFolderOpened(node);
			}
		});

		mainPane.addWorkspaceContentsStateChangeHandler(this);

		initialize();
	}

	/**
	 * 登録プラグインを返します。
	 *
	 * @return 登録プラグイン
	 */
	protected List<AdminPlugin> getPlugin() {
		return plugins;
	}

	/**
	 * <p>初期化します。</p>
	 */
	private void initialize() {

		model = new Tree();
		model.setModelType(TreeModelType.CHILDREN);

		initPlugin();

		createPluginNode();
	}

	/**
	 * <p>プラグインを初期化します。</p>
	 */
	private void initPlugin() {
		//TODO 外部定義からのロード
		plugins = plugins();

		setupPlugin();
	}

	/**
	 * <p>プラグインに対する設定を行います。</p>
	 *
	 * <p>プラグインの機能が変更された場合はここを調整します。</p>
	 */
	private void setupPlugin() {
		for (AdminPlugin plugin : plugins) {
			plugin.setOwner(this);
			plugin.setTree(model);
			plugin.setTreeGrid(this);
			plugin.setWorkSpace(mainPane);
		}
	}

	/**
	 * <p>ノードを作成します。</p>
	 *
	 * <p>作成するノードは {@link AdminPlugin} に処理を委譲します。</p>
	 */
	private void createPluginNode() {

		Map<String, AdminMenuTreeNode> folderMap = new LinkedHashMap<>();

		AdminMenuTreeNode root = new AdminMenuTreeNode("Root", null, "Root");
		model.setRoot(root);
		folderMap.put("Root", root);

		//プラグインからNodeを作成
		for (AdminPlugin plugin : plugins) {
			AdminMenuTreeNode pluginRootNode = plugin.createPluginRootNode();
			if (pluginRootNode != null) {
				//カテゴリのNode生成
				String categoryName = plugin.getCategoryName();
				if (SmartGWTUtil.isEmpty(categoryName)) {
					//カテゴリが未指定の場合は、Rootに追加
					model.add(pluginRootNode, root);
				} else {
					//カテゴリNodeの作成
					AdminMenuTreeNode parentNode = null;
					String prefixCategory = "";
					String[] categories = categoryName.split("/");
					for (int i = 0; i < categories.length; i++) {
						String category = prefixCategory + "/" + categories[i];
						if (folderMap.containsKey(category)) {
							//既に存在する場合
							parentNode = folderMap.get(category);
						} else {
							//存在しないので生成
							AdminMenuTreeNode newNode = new AdminMenuTreeNode(categories[i], null, "CATEGORY");
							folderMap.put(category, newNode);
							if (parentNode == null) {
								//RootNodeに追加
								model.add(newNode, root);
							} else {
								//ParentNodeに追加
								model.add(newNode, parentNode);
							}
							parentNode = newNode;
						}
						prefixCategory = category;
					}

					//最後のNodeに追加
					model.add(pluginRootNode, parentNode);
				}
			}
		}

		setData(model);

		//model.openAll()だとOpenFolderイベントが1回目で効かないので個別に行う
		//model.openAll();
		for (AdminMenuTreeNode node : folderMap.values()) {
			model.openFolder(node);
		}

	}

	/**
	 * <p>NodeのダブルクリックイベントをPluginに通知します。</p>
	 *
	 * @param node ダブルクリック対象Node
	 */
	private void fireOnDoubleClick(AdminMenuTreeNode node) {
		for (AdminPlugin plugin : plugins) {
			plugin.onNodeDoubleClick(node);
		}
	}

	/**
	 * <p>NodeのContextクリックイベントをPluginに通知します。</p>
	 *
	 * @param node ダブルクリック対象Node
	 */
	private void fireOnNodeContextClick(AdminMenuTreeNode node) {
		for (AdminPlugin plugin : plugins) {
			plugin.onNodeContextClick(node);
		}
	}

	/**
	 * <p>NodeのFolderOpenイベントをPluginに通知します。</p>
	 *
	 * @param node ダブルクリック対象Node
	 */
	private void fireOnFolderOpened(AdminMenuTreeNode node) {
		for (AdminPlugin plugin : plugins) {
			plugin.onFolderOpened(node);
		}
	}

	/**
	 * <p>ContentsタブのCloseイベントをPluginに通知します。</p>
	 *
	 * @param event Closeイベント
	 */
	@Override
	public void onContentClosed(ContentClosedEvent event){
		for (AdminPlugin plugin : plugins) {
			plugin.onContentClosed(event);
		}
	};

	/**
	 * <p>ContentsタブのSelectイベントをPluginに通知します。</p>
	 *
	 * @param event Selectイベント
	 */
	@Override
	public void onContentSelected(ContentSelectedEvent event){
		for (AdminPlugin plugin : plugins) {
			plugin.onContentSelected(event);
		}
	};

}
