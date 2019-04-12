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

package org.iplass.adminconsole.client.metadata.ui.prefs;

import java.util.ArrayList;
import java.util.List;

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
import org.iplass.mtp.prefs.Preference;
import org.iplass.mtp.prefs.PreferenceSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.DataArrivedEvent;
import com.smartgwt.client.widgets.tree.events.DataArrivedHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.NodeContextClickEvent;
import com.smartgwt.client.widgets.tree.events.NodeContextClickHandler;

/**
 * Preference編集パネル
 *
 * Preference編集の最上位パネルです。
 * {@link org.iplass.adminconsole.client.base.ui.layout.MainWorkspaceTab}により生成されます。
 *
 */
public class PrefsEditPane extends MetaDataMainEditPane {

	/** 内部保持Tree */
	private Tree tree;

	/** メタデータサービス */
	private final MetaDataServiceAsync service;

	/** ヘッダ部分 */
	private MetaCommonHeaderPane headerPane;

	/** 共通属性部分 */
	private MetaCommonAttributeSection<Preference> commonSection;

	/** ツリー部分 */
	private PreferenceTreeGrid grid;

	/** 右クリック時表示メニュー */
	private Menu contextMenu;

	/** 編集対象 */
	private Preference curDefinition;

	/** 編集対象バージョン */
	private int curVersion;

	/** 編集対象ID */
	private String curDefinitionId;

	/**
	 * コンストラクタ
	 *
	 * @param defName 選択Menu定義名
	 */
	public PrefsEditPane(MetaDataItemMenuTreeNode targetNode, DefaultMetaDataPlugin plugin) {
		super(targetNode, plugin);

		service = MetaDataServiceFactory.get();

		//レイアウト設定
		setWidth100();

		//ヘッダ（ボタン）部分
		headerPane = new MetaCommonHeaderPane(targetNode);
		headerPane.setSaveClickHandler(new SaveClickHandler());
		headerPane.setCancelClickHandler(new CancelClickHandler());

		headerPane.setHistoryClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MetaDataHistoryDialog metaDataHistoryDialog = new MetaDataHistoryDialog(curDefinition.getClass().getName(), curDefinitionId, curVersion);
				metaDataHistoryDialog.show();
			}
		});

		//共通属性
		commonSection = new MetaCommonAttributeSection<>(targetNode, Preference.class, false);

		//メイン編集領域
		HLayout layout = new HLayout();
		layout.setWidth100();

		// Preferenceツリー表示
		grid = new PreferenceTreeGrid();
		layout.addMember(grid);

		//Section設定
		SectionStackSection preferenceSection = createSection("Preference Attribute", layout);
		setMainSections(commonSection, preferenceSection);

		//配置
		addMember(headerPane);
		addMember(mainStack);

		//表示データの取得
		initializeData();
	}

	/**
	 * 保存ボタンイベント
	 */
	private final class SaveClickHandler implements com.smartgwt.client.widgets.events.ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			boolean commonValidate = commonSection.validate();
			boolean gridValidate = grid.hasErrors();

			if (!commonValidate || gridValidate) {
				return;
			}

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_prefs_PrefsEditPane_saveConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_prefs_PrefsEditPane_savePreferenceComment"), new BooleanCallback() {

				@Override
				public void execute(Boolean value) {
					if (value) {
						Preference definition = grid.getEditPreference();
						commonSection.getEditDefinition(definition);

						updatePreference(definition, true);
					}
				}
			});
		}
	}

	/**
	 * Preferenceの更新処理
	 *
	 * @param definition 更新対象Preference
	 */
	private void updatePreference(final Preference definition, boolean checkVersion) {
		SmartGWTUtil.showSaveProgress();
		service.updateDefinition(TenantInfoHolder.getId(), definition, curVersion, checkVersion, new MetaDataUpdateCallback() {

			@Override
			protected void overwriteUpdate() {
				updatePreference(definition, false);
			}

			@Override
			protected void afterUpdate(AdminDefinitionModifyResult result) {
				updateComplete(definition);
			}
		});
	}

	/**
	 * キャンセルボタンイベント
	 */
	private final class CancelClickHandler implements com.smartgwt.client.widgets.events.ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			SC.ask(AdminClientMessageUtil.getString("ui_metadata_prefs_PrefsEditPane_cancelConfirm"),
					AdminClientMessageUtil.getString("ui_metadata_prefs_PrefsEditPane_cancelConfirmComment")
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

	/**
	 * 更新完了処理
	 */
	private void updateComplete(Preference definition) {

		SC.say(AdminClientMessageUtil.getString("ui_metadata_prefs_PrefsEditPane_completion"),
				AdminClientMessageUtil.getString("ui_metadata_prefs_PrefsEditPane_preferenceSave"));

		//再表示
		initializeData();
		commonSection.refreshSharedConfig();
	}

	/**
	 * データ初期化処理
	 */
	private void initializeData() {

		//エラーのクリア
		commonSection.clearErrors();

		service.getDefinitionEntry(TenantInfoHolder.getId(), Preference.class.getName(), defName, new AsyncCallback<DefinitionEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				SC.say(AdminClientMessageUtil.getString("ui_metadata_prefs_PrefsEditPane_failed"),
						AdminClientMessageUtil.getString("ui_metadata_prefs_PrefsEditPane_failedGetScreenInfo"));

				GWT.log(caught.toString(), caught);
			}

			@Override
			public void onSuccess(DefinitionEntry result) {
				//画面に反映
				setDefinition(result);
			}

		});
		StatusCheckUtil.statuCheck(Preference.class.getName(), defName, this);
	}

	/**
	 * Definition画面設定処理
	 *
	 * @param definition 編集対象
	 */
	private void setDefinition(DefinitionEntry entry) {
		this.curDefinition = (Preference) entry.getDefinition();
		this.curVersion = entry.getDefinitionInfo().getVersion();
		this.curDefinitionId = entry.getDefinitionInfo().getObjDefId();

		commonSection.setDefinition(curDefinition);

		refreshGrid(curDefinition.getName());
	}

	public void refreshGrid(String defName) {
		grid.refreshGrid(defName);
	}

	public ListGridRecord[] getSelectedRecords(boolean excludePartialSelections) {
		return grid.getSelectedRecords(excludePartialSelections);
	}

	public class PreferenceTreeGrid extends TreeGrid {

		public PreferenceTreeGrid() {
			setLeaveScrollbarGap(false);
			setCanSort(false);
			setCanFreezeFields(false);
			setCanGroupBy(false);
			setCanPickFields(false);
			setSelectionType(SelectionStyle.SINGLE);

			// ドラッグアンドドロップで順序の変更を可能にする設定
			setCanDragRecordsOut(true);
			setCanReorderRecords(true);
			setCanReparentNodes(true);

			// この２つを指定することでcreateRecordlComponentが有効
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			// treeのルートに配置できないようにする為の処理
			addFolderDropHandler(new FolderDropHandler() {
				@Override
				public void onFolderDrop(FolderDropEvent event) {
					TreeNode targetNode = event.getFolder();
					int targetIndex = tree.getLevel(targetNode);

					if (targetIndex == 0) {
						event.cancel();
					}
				}

			});

			// 編集可能に
			setCanEdit(true);

			contextMenu = new Menu();

			addNodeContextClickHandler(new NodeContextClickHandler() {

				@Override
				public void onNodeContextClick(NodeContextClickEvent event) {

					final TreeNode targetNode = event.getNode();

					MenuItem addPrefs = new MenuItem("Add Preference", "[SKINIMG]/MultiUploadItem/icon_add_files.png");
					addPrefs.addClickHandler(new ClickHandler() {
						public void onClick(MenuItemClickEvent event) {

							TreeNode[] allTreeNode = tree.getAllNodes();

							for (TreeNode treeNode :allTreeNode) {
								if (targetNode.equals(treeNode)) {

									TreeNode empty = new TreeNode();
									tree.add(empty, treeNode);
									tree.openFolder(treeNode);
								}
							}
						}
					});

					MenuItem delPrefs = new MenuItem("Delete Preference", "[SKINIMG]/MultiUploadItem/icon_remove_files.png");
					delPrefs.addClickHandler(new ClickHandler() {
						public void onClick(MenuItemClickEvent event) {


							TreeNode[] allTreeNode = tree.getAllNodes();
							int index = 0;
							for (TreeNode treeNode :allTreeNode) {
								if (targetNode.equals(treeNode)) {
									if (tree.getLevel(treeNode) == 1) {
										SC.say("Can't delete root preference.");
									} else {
										grid.clearRowErrors(index);
										tree.remove(treeNode);
									}
								}
								index++;
							}
						}
					});


					contextMenu.setItems(addPrefs, delPrefs);
				}

			});
			setContextMenu(contextMenu);

			addEditCompleteHandler(new EditCompleteHandler() {

				@Override
				public void onEditComplete(EditCompleteEvent event) {

					int editColNum = event.getColNum();
					int editRowNum = event.getRowNum();

					Record record = event.getOldValues();
					String oldPrefsName = record.getAttributeAsString("prefsName");

					if (editColNum == 0 && editRowNum == 0) {
						SC.say("Can't edit root preference name.");

						TreeNode[] allTreeNode = tree.getAllNodes();

						for (TreeNode treeNode :allTreeNode) {
							if (tree.getLevel(treeNode) == 1) {
								treeNode.setAttribute("prefsName", oldPrefsName);
							}
						}
					}
				}

			});

			addDataArrivedHandler(new DataArrivedHandler() {

				@Override
				public void onDataArrived(DataArrivedEvent event) {
					// Root配下を展開する
					expandRoot();
				}
			});
		}



		public void refreshGrid(String defName) {


			service.getDefinition(TenantInfoHolder.getId(), Preference.class.getName(), defName, new AsyncCallback<Preference>() {

				@Override
				public void onFailure(Throwable caught) {
					GWT.log("error!!!", caught);
				}

				@Override
				public void onSuccess(Preference preference) {

					List<TreeNode> treeNodeList = new ArrayList<TreeNode>();

					treeNodeList.add(createNodeTree(preference));

					TreeGridField name = new TreeGridField("prefsName", "Name");
					name.setWidth(200);
					name.setRequired(true);
					TreeGridField description = new TreeGridField("description", "Description");
					description.setWidth(200);
					description.setEscapeHTML(true);
					TreeGridField value = new TreeGridField("value", "Value");
					value.setWidth(200);
					value.setEscapeHTML(true);
					TreeGridField runtimeClassName = new TreeGridField("runtimeClassName", "Runtime Class Name");
					runtimeClassName.setWidth(250);
					setFields(name, description, value, runtimeClassName);
					setFixedRecordHeights(true);

					tree = new Tree();	//Treeをnewしなおさないと階層がうまく表示されない
					tree.setData(treeNodeList.toArray(new TreeNode[]{}));
					tree.setModelType(TreeModelType.CHILDREN);

					setCanEdit(true);
					setData(tree);
					getData().openAll();
				}
			});

		}

		private TreeNode createNodeTree(Preference preference) {
			TreeNode treeNode = new TreeNode(preference.getName());

			if (preference instanceof PreferenceSet) {
				List<Preference> preferenceList = ((PreferenceSet) preference).getSubSet();

				treeNode.setAttribute("prefsName", preference.getName());
				treeNode.setAttribute("description", preference.getDescription());
				treeNode.setAttribute("value", preference.getValue());
				treeNode.setAttribute("runtimeClassName", preference.getRuntimeClassName());

				List<TreeNode> childList = new ArrayList<TreeNode>();

				// PreferenceSetのSubSetがないことは基本的にはありえない（デフォルトメタで設定した場合に読み込みOKとする為）
				if (preferenceList != null) {
					for (Preference sub : preferenceList) {
						TreeNode child = createNodeTree(sub);
						childList.add(child);
					}
					treeNode.setChildren(childList.toArray(new TreeNode[]{}));
				}
			} else {
				treeNode.setAttribute("prefsName", preference.getName());
				treeNode.setAttribute("description", preference.getDescription());
				treeNode.setAttribute("value", preference.getValue());
				treeNode.setAttribute("runtimeClassName", preference.getRuntimeClassName());
			}

			return treeNode;
		}

		public void expandRoot() {
			getTree().closeAll();
			getTree().openFolders(
					getTree().getChildren(getTree().getRoot()));
		}

		public Preference getEditPreference() {
			return getEditMenuItems(tree.getChildren(tree.getRoot())[0]);
		}

		private Preference getEditMenuItems(TreeNode node) {

			if (tree.hasChildren(node)) {
				// PreferenceSet
				PreferenceSet preference = new PreferenceSet();
				preference.setName(node.getAttributeAsString("prefsName"));
				preference.setDescription(node.getAttributeAsString("description"));
				preference.setValue(node.getAttributeAsString("value"));
				preference.setRuntimeClassName(node.getAttributeAsString("runtimeClassName"));

				List<Preference> childList = new ArrayList<Preference>();
				for (TreeNode treeNode : tree.getChildren(node)) {
					Preference child = getEditMenuItems(treeNode);
					childList.add(child);
				}

				preference.setSubSet(childList);
				return preference;
			} else {
				// Preference
				Preference preference = new Preference();
				preference.setName(node.getAttributeAsString("prefsName"));
				preference.setDescription(node.getAttributeAsString("description"));
				preference.setValue(node.getAttributeAsString("value"));
				preference.setRuntimeClassName(node.getAttributeAsString("runtimeClassName"));
				return preference;
			}
		}
	}

}
