/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.treeview;

import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.DefaultMetaDataPlugin;
import org.iplass.adminconsole.client.metadata.ui.MetaDataItemMenuTreeNode;
import org.iplass.adminconsole.client.metadata.ui.MetaDataMainEditPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonAttributeSection;
import org.iplass.adminconsole.client.metadata.ui.common.MetaCommonHeaderPane;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataHistoryDialog;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataUpdateCallback;
import org.iplass.adminconsole.client.metadata.ui.common.StatusCheckUtil;
import org.iplass.adminconsole.shared.metadata.dto.AdminDefinitionModifyResult;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.definition.DefinitionEntry;
import org.iplass.mtp.view.treeview.TreeView;
import org.iplass.mtp.view.treeview.TreeViewGridColModel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * ツリーメニュー編集パネル
 *
 * ツリーメニュー編集の最上位パネルです。
 * {@link org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab}により生成されます。
 *
 */
public class TreeViewEditPane extends MetaDataMainEditPane {

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;
	/** 共通属性部分 */
	private MetaCommonAttributeSection commonSection;

	private TreeViewGridColModelPane colModelPane;
	/** ツリー部分 */
	private TreeViewGrid treeGrid;

	/** 編集対象 */
	private TreeView curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/**
	 * コンストラクタ
	 *
	 * @param defName 選択Menu定義名
	 */
	public TreeViewEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
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
		commonSection = new MetaCommonAttributeSection(targetNode, TreeView.class, true);


		//メイン編集領域
		HLayout layout = new HLayout();
		layout.setWidth100();

		//Tree構成編集部分
		VLayout layout2 = new VLayout();
		layout2.setHeight100();
		layout2.setShowResizeBar(true);
		layout2.setResizeBarTarget("next");	//リサイズバーをダブルクリックした際、次を収縮

		colModelPane = new TreeViewGridColModelPane();
		layout2.addMember(colModelPane);

		Label label = new Label(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_treeSetting"));
		label.setHeight(20);
		layout2.addMember(label);

		treeGrid = new TreeViewGrid();
		layout2.addMember(treeGrid);

		layout.addMember(layout2);

		//TreeItem選択部分
		TreeViewDragPane dragPane = new TreeViewDragPane();
		layout.addMember(dragPane);

		//Section設定
		SectionStackSection treeViewSection = createSection("TreeView Attribute", layout);
		setMainSections(commonSection, treeViewSection);

		//配置
		addMember(headerPane);
		addMember(mainStack);

		//マッピング設定
		colModelPane.addDataChangedHandler(new DataChangedHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onDataChanged(DataChangedEvent event) {
				List<TreeViewGridColModel> colModel = (List<TreeViewGridColModel>) event.getValueMap().get("colModel");
				treeGrid.setColModel(colModel);
			}
		});

		//表示データの取得
		initializeData();
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {

		//エラーのクリア
		commonSection.clearErrors();

		//TreeViewの検索
		service.getDefinitionEntry(TenantInfoHolder.getId(), TreeView.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {
			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_failedGetScreenInfo") + caught.getMessage());

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry treeView) {

				//画面に反映
				setTreeView(treeView);
			}
		});

		//ステータスチェック
		StatusCheckUtil.statuCheck(TreeView.class.getName(), defName, this);
	}


	/**
	 * 対象のTreeViewをレイアウト表示パネルに設定します。
	 *
	 * @param treeView TreeView
	 */
	private void setTreeView(DefinitionEntry entry) {
		this.curDefinition = (TreeView) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		//共通属性
		commonSection.setName(curDefinition.getName());
		commonSection.setDisplayName(curDefinition.getDisplayName());
		commonSection.setDescription(curDefinition.getDescription());
		commonSection.setLocalizedDisplayNameList(curDefinition.getLocalizedDisplayNameList());

		colModelPane.setDefinition(curDefinition.getColModel());
		treeGrid.setTreeView(curDefinition);
	}


	/**
	 * ツリーの更新処理
	 *
	 * @param editTreeView 更新対象TreeView
	 */
	private void updateTree(final TreeView editTreeView, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), editTreeView, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updateTree(editTreeView, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				updateComplete(editTreeView);
			}
		});
	}

	/**
	 * 更新完了処理
	 */
	private void updateComplete(TreeView editTreeView) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_saveTreeViewComp"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();

		//ツリー再表示
		plugin.refreshWithSelect(editTreeView.getName(), new AsyncCallback<MetaDataItemMenuTreeNode>() {
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
			boolean treeValidate = treeGrid.validate();
			if (!commonValidate || !treeValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_saveConfirmComment")
					, new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {

						TreeView editTreeView = new TreeView();

						editTreeView.setName(commonSection.getName());
						editTreeView.setDisplayName(commonSection.getDisplayName());
						editTreeView.setDescription(commonSection.getDescription());
						editTreeView.setLocalizedDisplayNameList(commonSection.getLocalizedDisplayNameList());

						//TreeView属性のセット
						editTreeView.setColModel(colModelPane.getDefinition());
						editTreeView.setItems(treeGrid.getItems());

						updateTree(editTreeView, true);
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

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewEditPane_cancelConfirmComment")
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

}
