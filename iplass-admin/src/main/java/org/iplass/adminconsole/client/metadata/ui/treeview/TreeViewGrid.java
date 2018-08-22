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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS;
import org.iplass.adminconsole.client.metadata.data.MetaDataNameDS.MetaDataNameDSOption;
import org.iplass.adminconsole.client.metadata.ui.common.EntityPropertyDragPane;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.BinaryProperty;
import org.iplass.mtp.entity.definition.properties.LongTextProperty;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;
import org.iplass.mtp.view.treeview.EntityTreeViewItem;
import org.iplass.mtp.view.treeview.ReferenceTreeViewItem;
import org.iplass.mtp.view.treeview.TreeView;
import org.iplass.mtp.view.treeview.TreeViewGridColModel;
import org.iplass.mtp.view.treeview.TreeViewGridColModelMapping;
import org.iplass.mtp.view.treeview.TreeViewItem;
import org.iplass.mtp.view.treeview.TreeViewItem.TreeSortType;
import org.iplass.mtp.web.actionmapping.definition.ActionMappingDefinition;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.SpinnerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.validator.RequiredIfFunction;
import com.smartgwt.client.widgets.form.validator.RequiredIfValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDropEvent;
import com.smartgwt.client.widgets.grid.events.RecordDropHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;

/**
 * ツリーメニュー編集TreeGrid
 *
 * ツリーメニューを表示します。
 * {@link org.iplass.adminconsole.client.ui.menu.item.MenuItemDragPane}で生成されます。
 *
 */
public class TreeViewGrid extends TreeGrid {

	/** 内部保持Tree */
	private Tree tree;

	/** treeGridの列-プロパティのマッピング用 */
	private List<TreeViewGridColModel> colModel;

	/**
	 * コンストラクタ
	 */
	public TreeViewGrid() {

		//setAutoFetchData(true);
		setDragDataAction(DragDataAction.MOVE);
		setSelectionType(SelectionStyle.SINGLE);	//単一行選択
//		setBorder("none");					//外のSectionと線がかぶるので消す

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
		setParentAlreadyContainsChildMessage(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_treeViewAlreadyExists"));

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

		//TreeGridFieldの生成(TreeNodeのattribute属性と一致させること)
		TreeGridField nameField = new TreeGridField("name", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_nodeName"));
		TreeGridField defNameField = new TreeGridField("defName", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_entityDefName"));
		//displayNameField.setFrozen(true);	//これを指定すると表示が崩れる
		TreeGridField editActionField = new TreeGridField("editAction", " ");
		editActionField.setWidth(25);
		TreeGridField delActionField = new TreeGridField("delAction", " ");
		delActionField.setWidth(25);
		setFields(nameField, defNameField, editActionField, delActionField);

		//Drag＆Drop用EventHandlerの設定
		addFolderDropHandler(new TreeViewFolderDropHandler());

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

	}

	@Override
	protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
		String fieldName = this.getFieldName(colNum);
		Canvas ret = null;
		if (fieldName.equals("editAction")) {
			ImgButton editBtn = new ImgButton();
			editBtn.setShowDown(false);
			editBtn.setShowRollOver(false);
			editBtn.setLayoutAlign(Alignment.CENTER);
			//deleteBtn.setSrc("icon_delete.png");
			editBtn.setSrc("icon_edit.png");
			editBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_editNode"));
			editBtn.setHeight(16);
			editBtn.setWidth(16);
			editBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					//TreeViewItemEditDialog dialog = new TreeViewItemEditDialog((TreeViewNode) record);
					TreeNode target = Tree.nodeForRecord(record);
					TreeViewItemEditDialog dialog = new TreeViewItemEditDialog(target);
					dialog.show();
				}
			});
			ret = editBtn;
		} else if (fieldName.equals("delAction")) {
			ImgButton deleteBtn = new ImgButton();
			deleteBtn.setShowDown(false);
			deleteBtn.setShowRollOver(false);
			deleteBtn.setLayoutAlign(Alignment.CENTER);
			//deleteBtn.setSrc("icon_delete.png");
			deleteBtn.setSrc("remove.png");
			deleteBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_removeNodeTree"));
			deleteBtn.setHeight(16);
			deleteBtn.setWidth(16);
			deleteBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					SC.confirm(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_deleteConfirm"),
							AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_deleteCategoryConf", record.getAttribute("defName"))
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
			ret = deleteBtn;
		}
		return ret;

	}

	/**
	 * TreeViewを展開します。
	 *
	 * @param treeView TreeView
	 */
	public void setTreeView(TreeView treeView) {

		this.colModel = treeView.getColModel();

		TreeViewNode root = new TreeViewNode(treeView, "cubes_all.png");

		List<TreeViewItem> items = treeView.getItems();
		if (items.size() > 0) {
			TreeViewNode[] children = createTreeViewItemNodes(items);
			root.setChildren(children);
		}

		tree = new Tree();	//Treeをnewしなおさないと階層がうまく表示されない
		tree.setRoot(root);
		tree.setModelType(TreeModelType.CHILDREN);

		setData(tree);
		getData().openAll();
	}

	public List<TreeViewItem> getItems() {
		List<TreeViewItem> items = new ArrayList<TreeViewItem>();

		TreeViewNode root = (TreeViewNode) tree.getRoot();
		TreeNode[] children = tree.getChildren(root);

		for (TreeNode child : children) {
			TreeViewItem item = (TreeViewItem)child.getAttributeAsObject("valueObject");
			item.setReferenceTreeViewItems(getReferenceTreeViewItems(child));
			items.add(item);
		}

		return items;
	}

	private List<ReferenceTreeViewItem> getReferenceTreeViewItems(TreeNode node) {

		List<ReferenceTreeViewItem> items = new ArrayList<ReferenceTreeViewItem>();

		TreeNode[] children = tree.getChildren(node);

		if (children != null) {
			for (TreeNode child : children) {
				ReferenceTreeViewItem item = (ReferenceTreeViewItem)child.getAttributeAsObject("valueObject");
				item.setReferenceTreeViewItems(getReferenceTreeViewItems(child));
				items.add(item);
			}
		}

		return items;
	}

	public void setColModel(List<TreeViewGridColModel> colModel) {
		this.colModel = colModel;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return true;
	}

	private TreeViewNode[] createTreeViewItemNodes(List<TreeViewItem> children) {
		List<TreeViewNode> childList = new ArrayList<TreeViewNode>(children.size());
		for (TreeViewItem child : children) {
			TreeViewNode childNode = createTreeViewItemNode(child);
			childList.add(childNode);
		}
		return childList.toArray(new TreeViewNode[0]);
	}

	private TreeViewNode[] createReferenceTreeViewNodes(List<ReferenceTreeViewItem> children) {
		List<TreeViewNode> childList = new ArrayList<TreeViewNode>(children.size());
		for (ReferenceTreeViewItem child : children) {
			TreeViewNode childNode = createReferenceTreeViewNode(child);
			if (child.getReferenceTreeViewItems().size() > 0){
				childNode.setChildren(createReferenceTreeViewNodes(child.getReferenceTreeViewItems()));
			}
			childList.add(childNode);
		}
		return childList.toArray(new TreeViewNode[0]);
	}

	private TreeViewNode createTreeViewItemNode(TreeViewItem item) {
		TreeViewNode treeNode = null;
		if (item instanceof ReferenceTreeViewItem) {
			treeNode = createReferenceTreeViewNode((ReferenceTreeViewItem)item);
		} else if (item instanceof EntityTreeViewItem) {
			treeNode = createEntityTreeViewNode((EntityTreeViewItem)item);
		} else {
			GWT.log("un support tree view item type. item=" + item.getClass().getName());
		}
		if (treeNode != null && item.getReferenceTreeViewItems().size() > 0){
			treeNode.setChildren(createReferenceTreeViewNodes(item.getReferenceTreeViewItems()));
		}
		return treeNode;
	}

	private TreeViewNode createEntityTreeViewNode(EntityTreeViewItem item) {
		return new TreeViewNode(item, "cube_blue.png");
	}

	private TreeViewNode createReferenceTreeViewNode(ReferenceTreeViewItem item) {
		return new TreeViewNode(item, "cube_green.png");
	}

	/**
	 * カスタマイズTreeNode
	 */
	private class TreeViewNode extends TreeNode {

		public TreeViewNode(TreeView item, String icon) {
			setName(item.getName());
			setIcon(icon);

			setAttribute("defName", item.getName());

		}

		public TreeViewNode(TreeViewItem item, String icon) {
			setAttribute("valueObject", item);

			if (item instanceof ReferenceTreeViewItem) {
				setName(((ReferenceTreeViewItem) item).getPropertyName());
			} else if (item instanceof EntityTreeViewItem) {
				setName(item.getDefName());
			}
			setIcon(icon);

			setAttribute("defName", item.getDefName());

			setChildren(new TreeViewNode[0]);

		}
	}

	/**
	 * ドロップハンドラ
	 * @author lis3wg
	 *
	 */
	private class TreeViewFolderDropHandler implements FolderDropHandler {

		@Override
		public void onFolderDrop(FolderDropEvent event) {
			TreeViewNode dropTargetNode = null;
			if (event.getFolder() instanceof TreeViewNode) {
				dropTargetNode = (TreeViewNode) event.getFolder();
			}

			TreeViewNode moveTargetNode = null;
			TreeViewNode addTargetNode = null;
			if (event.getSourceWidget() instanceof TreeViewGrid) {
				//ツリー内移動
				//ルート直下はルート直下のみ移動可能
				//それ以外はそのノードが属する階層内のみ移動可能
				TreeNode[] nodes = event.getNodes();
				if (nodes != null && nodes.length ==1) {
					if (nodes[0] instanceof TreeViewNode) {
						moveTargetNode = (TreeViewNode) nodes[0];
					}
				}
				if (moveTargetNode == null) {
					throw new IllegalStateException(
							AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_dragOriginalInfoNotGet") + nodes);
				}
			} else if (event.getSourceWidget() instanceof ListGrid) {
				//Drag&Drop
				Canvas dragTarget = event.getSourceWidget();

				//対象レコードの取得
				ListGridRecord record = ((ListGrid)dragTarget).getSelectedRecord();
				String name = record.getAttribute("defName");
				EntityTreeViewItem item = new EntityTreeViewItem();
				item.setDefName(name);
				item.setDisplayDefinitionNode(true);
				item.setDisplayPropertyName("name");
				item.setLimit(10);
				item.setSortItem("name");
				item.setSortType(TreeSortType.ASC);
				addTargetNode = createEntityTreeViewNode(item);
			}

			//追加処理
			if (dropTargetNode != null && addTargetNode != null) {
				tree.add(addTargetNode, dropTargetNode, event.getIndex());
			}

			//Nodeの移動処理
			if (dropTargetNode != null && moveTargetNode != null) {
				if (tree.getParentPath(moveTargetNode).equals(tree.getPath(dropTargetNode))) {
					int curIndex = getCurrentIndex(moveTargetNode);
					int addIndex = event.getIndex();
					if (addIndex > curIndex) addIndex--;

					tree.remove(moveTargetNode);
					tree.add(moveTargetNode, dropTargetNode, addIndex);
				}
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

			if (tree.getParent(node) != null) {
				TreeNode[] children = tree.getChildren(tree.getParent(node));

				int i = 0;
				for (TreeNode child : children) {
					if (child == node) {
						return i;
					}
					i++;
				}
			}
			//ありえない
			throw new IllegalStateException(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_dragIndexNotGet") + node);
		}

	}

	/**
	 * TreeViewItem編集ダイアログ
	 * @author lis3wg
	 *
	 */
	private class TreeViewItemEditDialog extends AbstractWindow {

		TreeNode node = null;

		/** Entity定義名 */
		private TextItem defNameField;
		/** ツリーメニュー上に表示する項目(デフォルト:name) */
		private SelectItem displayPropertNameField;
		/** 表示上限 */
		private SpinnerItem limitField;
		/** ツリーメニュー上にEntity定義階層を表示するか */
		private CheckboxItem displayDefinitionNodeField;
		/** 表示時のソート項目 */
		private SelectItem sortItemField;
		/** 表示時のソートタイプ */
		private SelectItem sortTypeField;
		/** 詳細表示用のアクション */
		private SelectItem actionField;
		/** ビュー名 */
		private TextItem viewNameField;

		/** Entity階層に表示するアイコン */
		private TextItem entityNodeIconField;
		/** Entity階層に適用するスタイルシートの名前 */
		private TextItem entityNodeCssStyleField;
		/** Entity定義階層に表示するアイコン */
		private TextItem entityDefinitionNodeIconField;
		/** Entity定義階層に適用するスタイルシートの名前 */
		private TextItem entityDefinitionNodeCssStyleField;
		/** Index階層に表示するアイコン */
		private TextItem indexNodeIconField;
		/** Index階層に適用するスタイルシートの名前 */
		private TextItem indexNodeCssStyleField;

		/** 親階層で定義された参照型のプロパティ名(変更不可) */
		private TextItem propertyNameField;
		/** Entity定義階層の表示名 */
		private TextItem displayNameField;

		private TreeViewColModelMappingGrid colModelGrid;

		private DynamicForm refForm;

		private DynamicForm widgetForm;

		private EntityDefinition ed;

		public TreeViewItemEditDialog(TreeNode targetNode) {

			node = targetNode;

			TreeViewItem item = (TreeViewItem) node.getAttributeAsObject("valueObject");
			if (item == null) {
				//ありえないはず
				throw new IllegalArgumentException(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_editTargetInvalid"));
			}

			setTitle(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_editTreeViewItem"));
			setWidth(600);
			setHeight(340);
			setShowMinimizeButton(false);
			setIsModal(true);
			setShowModalMask(true);
			centerInPage();

			HLayout header = new HLayout(5);
			header.setHeight(20);
			header.setWidth100();
			header.setAlign(VerticalAlignment.CENTER);

			VLayout contents = new VLayout(5);
			contents.setAlign(Alignment.CENTER);

			TabSet tab = new TabSet();
			tab.setWidth100();
			tab.setHeight100();

			RequiredIfValidator requiredValidator = new RequiredIfValidator(
				new RequiredIfFunction() {

					@Override
					public boolean execute(FormItem formItem, Object value) {
						return value == null || value.toString().isEmpty();
					}
				});
			//TODO YK ロケールを設定すればデフォルトでOK
			requiredValidator.setErrorMessage(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_valueFieldRequired"));

			//設定項目の入力フィールド作成
			List<FormItem> items = new ArrayList<FormItem>();

			defNameField = new TextItem("defName", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_entityDefName"));
			defNameField.setValue(item.getDefName());
			SmartGWTUtil.setReadOnly(defNameField);
			items.add(defNameField);

			displayPropertNameField = new SelectItem("displayPropertyName", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_dispItem"));
			SmartGWTUtil.addHoverToFormItem(displayPropertNameField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_speItemEntityDispTreeView"));
			displayPropertNameField.setValidators(requiredValidator);
			displayPropertNameField.setValue(item.getDisplayPropertyName());
			items.add(displayPropertNameField);

			limitField = new SpinnerItem("limit", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_upperLimitDisp"));
			SmartGWTUtil.addHoverToFormItem(limitField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_setUpperLimit"));
			limitField.setValidators(requiredValidator);
			limitField.setDefaultValue(1);
			limitField.setMin(1);
			limitField.setMax(100);
			limitField.setStep(1);
			limitField.setValue(item.getLimit());
			items.add(limitField);

			displayDefinitionNodeField = new CheckboxItem("displayDefinitionNode", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_defHieraDispSett"));
			SmartGWTUtil.addHoverToFormItem(displayDefinitionNodeField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_setDispHieraEntityDef"));
			displayDefinitionNodeField.setValue(item.isDisplayDefinitionNode());
			items.add(displayDefinitionNodeField);

			sortItemField = new SelectItem("sortItem", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_sortItem"));
			SmartGWTUtil.addHoverToFormItem(sortItemField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_speSortItemTreeView"));
			sortItemField.setValidators(requiredValidator);
			sortItemField.setValue(item.getSortItem());
			items.add(sortItemField);

			sortTypeField = new SelectItem("sortType", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_sortType"));
			SmartGWTUtil.addHoverToFormItem(sortTypeField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_speSortTypeTreeView"));
			sortTypeField.setValidators(requiredValidator);
			sortTypeField.setValue(item.getSortType().name());
			sortTypeField.setValueMap(new String[]{"ASC", "DESC"});
			items.add(sortTypeField);

			actionField = new SelectItem("action", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_detailDispAction"));
			SmartGWTUtil.addHoverToFormItem(actionField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_actionToDisplayEntity"));
			String action = item.getAction();
			if (action == null || action.isEmpty()) action = "#default";
			actionField.setValue(action);
			MetaDataNameDS.setDataSource(actionField, ActionMappingDefinition.class, new MetaDataNameDSOption(false, true));
			items.add(actionField);

			viewNameField = new TextItem("viewName", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_viewName"));
			SmartGWTUtil.addHoverToFormItem(displayDefinitionNodeField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_viewNameComment"));
			viewNameField.setValue(item.getViewName());
			items.add(viewNameField);

			if (item instanceof ReferenceTreeViewItem) {
				ReferenceTreeViewItem rItem = (ReferenceTreeViewItem) item;
				propertyNameField = new TextItem("propertyName", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_referencePropName"));
				SmartGWTUtil.setReadOnly(propertyNameField);
				propertyNameField.setValue(rItem.getPropertyName());
				items.add(propertyNameField);

				displayNameField = new TextItem("displayName", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_dispName"));
				SmartGWTUtil.addHoverToFormItem(displayNameField,
						AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_speEntityDefHieraName"));
				displayNameField.setValue(rItem.getDisplayName());
				items.add(displayNameField);
			}

			DynamicForm baseForm = new DynamicForm();
			baseForm.setNumCols(4);
			baseForm.setMargin(10);
			baseForm.setWidth100();
			baseForm.setFields(items.toArray(new FormItem[items.size()]));

			Tab baseItem = new Tab();
			baseItem.setTitle(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_basicItem"));
			baseItem.setPane(baseForm);
			tab.addTab(baseItem);

			refForm = new DynamicForm();
			refForm.setNumCols(4);
			refForm.setMargin(10);
			refForm.setWidth100();

			Tab refItem = new Tab();
			refItem.setTitle(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_referenceConf"));
			refItem.setPane(refForm);
			tab.addTab(refItem);

			if (colModel != null && !colModel.isEmpty()) {
				//カラム設定
				HLayout colModelPane = new HLayout();
				colModelPane.setMargin(10);
				colModelPane.setMembersMargin(4);
				colModelPane.setWidth100();

				colModelGrid = new TreeViewColModelMappingGrid();
				colModelGrid.setDefinition(item);
				colModelPane.addMember(colModelGrid);

				EntityPropertyDragPane dragPane = new EntityPropertyDragPane(false);
				dragPane.refresh(item.getDefName());
				colModelPane.addMember(dragPane);

				Tab colModelMappingItem = new Tab();
				colModelMappingItem.setTitle(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_partsSetting"));
				colModelMappingItem.setPane(colModelPane);
				tab.addTab(colModelMappingItem);
			}

			widgetForm = new DynamicForm();
			widgetForm.setNumCols(4);
			widgetForm.setMargin(10);
			widgetForm.setWidth100();

			items = new ArrayList<FormItem>();

			entityNodeIconField = new TextItem("entityNodeIcon", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_icon1"));
			SmartGWTUtil.addHoverToFormItem(entityNodeIconField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_icon1Comment"));
			entityNodeIconField.setValue(item.getEntityNodeIcon());
			items.add(entityNodeIconField);

			entityNodeCssStyleField = new TextItem("entityNodeCssStyle", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_style1"));
			SmartGWTUtil.addHoverToFormItem(entityNodeCssStyleField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_style1Comment"));
			entityNodeCssStyleField.setValue(item.getEntityNodeCssStyle());
			items.add(entityNodeCssStyleField);

			entityDefinitionNodeIconField = new TextItem("entityDefinitionNodeIcon", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_icon2"));
			SmartGWTUtil.addHoverToFormItem(entityDefinitionNodeIconField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_icon2Comment"));
			entityDefinitionNodeIconField.setValue(item.getEntityDefinitionNodeIcon());
			items.add(entityDefinitionNodeIconField);

			entityDefinitionNodeCssStyleField = new TextItem("entityDefinitionNodeCssStyle", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_style2"));
			SmartGWTUtil.addHoverToFormItem(entityDefinitionNodeCssStyleField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_style2Comment"));
			entityDefinitionNodeCssStyleField.setValue(item.getEntityDefinitionNodeCssStyle());
			items.add(entityDefinitionNodeCssStyleField);

			indexNodeIconField = new TextItem("indexNodeIcon", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_icon3"));
			SmartGWTUtil.addHoverToFormItem(indexNodeIconField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_icon3Comment"));
			indexNodeIconField.setValue(item.getIndexNodeIcon());
			items.add(indexNodeIconField);

			indexNodeCssStyleField = new TextItem("indexNodeCssStyle", AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_style3"));
			SmartGWTUtil.addHoverToFormItem(indexNodeCssStyleField,
					AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_style3Comment"));
			indexNodeCssStyleField.setValue(item.getIndexNodeCssStyle());
			items.add(indexNodeCssStyleField);

			widgetForm.setItems(items.toArray(new FormItem[items.size()]));

			Tab widgetItem = new Tab();
			widgetItem.setTitle(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_widgetSetting"));
			widgetItem.setPane(widgetForm);
			tab.addTab(widgetItem);

			contents.addMember(tab);

			HLayout footer = new HLayout(5);
			footer.setMargin(5);
			footer.setHeight(20);
			footer.setWidth100();
			//footer.setAlign(Alignment.LEFT);
			footer.setAlign(VerticalAlignment.CENTER);

			IButton ok = new IButton("OK");
			ok.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					TreeViewItem item = (TreeViewItem) node.getAttributeAsObject("valueObject");
					item.setDisplayPropertyName(SmartGWTUtil.getStringValue(displayPropertNameField));
					item.setLimit((Integer) limitField.getValue());
					item.setDisplayDefinitionNode(SmartGWTUtil.getBooleanValue(displayDefinitionNodeField));
					item.setSortItem(SmartGWTUtil.getStringValue(sortItemField));
					item.setSortType(TreeSortType.valueOf(SmartGWTUtil.getStringValue(sortTypeField)));
					String action = SmartGWTUtil.getStringValue(actionField);
					if ("#default".equals(action)) action = null;
					item.setAction(action);
					item.setViewName(SmartGWTUtil.getStringValue(viewNameField));
					item.setEntityNodeIcon(SmartGWTUtil.getStringValue(entityNodeIconField));
					item.setEntityNodeCssStyle(SmartGWTUtil.getStringValue(entityNodeCssStyleField));
					item.setEntityDefinitionNodeIcon(SmartGWTUtil.getStringValue(entityDefinitionNodeIconField));
					item.setEntityDefinitionNodeCssStyle(SmartGWTUtil.getStringValue(entityDefinitionNodeCssStyleField));
					item.setIndexNodeIcon(SmartGWTUtil.getStringValue(indexNodeIconField));
					item.setIndexNodeCssStyle(SmartGWTUtil.getStringValue(indexNodeCssStyleField));
					if (item instanceof ReferenceTreeViewItem) {
						ReferenceTreeViewItem rItem = (ReferenceTreeViewItem) item;
						rItem.setDisplayName(SmartGWTUtil.getStringValue(displayNameField));
					}
					if (colModelGrid != null) {
						item.setMapping(colModelGrid.getDefinition());
					}


					refreshNode();

					destroy();
				}
			});

			IButton cancel = new IButton(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_cancel"));
			cancel.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					destroy();
				}
			});

			footer.setMembers(ok, cancel);

			MetaDataServiceAsync service  = MetaDataServiceFactory.get();
			service.getEntityDefinition(TenantInfoHolder.getId(), item.getDefName(), new AsyncCallback<EntityDefinition>() {

				@Override
				public void onSuccess(EntityDefinition result) {
					ed = result;

					//プロパティの一覧を取得して、選択型の選択肢とかを作成する
					List<PropertyDefinition> propertyList = ed.getPropertyList();
					Collections.sort(propertyList, new Comparator<PropertyDefinition>() {

						@Override
						public int compare(PropertyDefinition o1, PropertyDefinition o2) {
							return o1.getName().compareTo(o2.getName());
						}
					});

					List<String> params = new ArrayList<String>();
					List<FormItem> fields = new ArrayList<FormItem>();
					TreeNode[] children = tree.getChildren(node);
					for (PropertyDefinition pd : propertyList) {
						//選択型の項目作成
						params.add(pd.getName());

						//参照型の表示設定
						if (pd instanceof ReferenceProperty) {
							fields.add(createRefField((ReferenceProperty) pd, children));
						}
					}
					String[] array = params.toArray(new String[params.size()]);
					displayPropertNameField.setValueMap(array);
					sortItemField.setValueMap(array);

					refForm.setFields(fields.toArray(new FormItem[fields.size()]));
				}

				private FormItem createRefField(ReferenceProperty pd, TreeNode[] children) {
					CheckboxItem refField = new CheckboxItem(pd.getName(), pd.getDisplayName());

					refField.setTooltip(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_setTreeViewDisp", pd.getDisplayName()));
					if (children != null) {
						for (TreeNode child : children) {
							ReferenceTreeViewItem item = (ReferenceTreeViewItem) child.getAttributeAsObject("valueObject");
							if (item != null && item.getPropertyName().equals(pd.getName())) {
								refField.setValue(true);
							}
						}
					}
					return refField;
				}

				@Override
				public void onFailure(Throwable caught) {
					SC.warn(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_failedToGetEntityDef") + caught.getMessage());
				}
			});

			addItem(header);
			addItem(contents);
			addItem(footer);
		}

		private void refreshNode() {
			FormItem[] items = refForm.getFields();
			if (items == null || items.length == 0) return;

			List<PropertyDefinition> dispList = new ArrayList<PropertyDefinition>();
			for (FormItem item : items) {
				Boolean checked = (Boolean) item.getValue();
				if (checked != null && checked) {
					dispList.add(ed.getProperty(item.getName()));
				}
			}

			TreeNode[] children = tree.getChildren(node);

			//追加
			List<TreeNode> childList = new ArrayList<TreeNode>();
			for (PropertyDefinition pd : dispList) {
				TreeNode child = search(pd, children);
				if (child != null) {
					childList.add(child);
				} else {
					//ノード作成
					ReferenceProperty rpd = (ReferenceProperty) pd;
					ReferenceTreeViewItem item = new ReferenceTreeViewItem();
					item.setDefName(rpd.getObjectDefinitionName());
					item.setDisplayDefinitionNode(false);
					item.setDisplayPropertyName("name");
					item.setLimit(10);
					item.setSortItem("name");
					item.setSortType(TreeSortType.ASC);
					item.setPropertyName(rpd.getName());
					item.setDisplayName(rpd.getDisplayName());
					child = createReferenceTreeViewNode(item);
					childList.add(child);
				}
			}

			//全消ししてからチェックついてたのだけ再設定
			tree.removeList(children);
			if (childList.size() > 0) {
				for (int i = 0; i < childList.size(); i++) {
					tree.add(childList.get(i), node);
				}
			}
		}

		private TreeNode search(PropertyDefinition pd, TreeNode[] children) {
			if (children != null && children.length > 0) {
				for (TreeNode child : children) {
					ReferenceTreeViewItem item = (ReferenceTreeViewItem) child.getAttributeAsObject("valueObject");
					if (item != null && item.getPropertyName().equals(pd.getName())) {
						return child;
					}
				}
			}
			return null;
		}
	}

	/**
	 * 集計フィルタアイテム定義マッピング用グリッド
	 * @author lis3wg
	 *
	 */
	private class TreeViewColModelMappingGrid extends ListGrid {

		public TreeViewColModelMappingGrid() {
			setHeight100();
			setWidth("70%");
			setLeaveScrollbarGap(false);
			setCanAcceptDroppedRecords(true);
			setDropTypes("Property");

			//この２つを指定することでcreateRecordComponentが有効
			setShowRecordComponents(true);
			setShowRecordComponentsByCell(true);

			ListGridField nameField = new ListGridField("name", "name");
			nameField.setWidth(75);
			ListGridField displayLabelField = new ListGridField("displayLabel", AdminClientMessageUtil.getString("ui_metadata_aggregation_dashboard_DashboardUnitPane_propertyDispName"));
			ListGridField mappingItemDisplayNameField = new ListGridField("mappingItemDisplayLabel", AdminClientMessageUtil.getString("ui_metadata_aggregation_dashboard_DashboardUnitPane_propertyDispNameCorre"));
			ListGridField delActionField = new ListGridField("delAction", " ");
			delActionField.setWidth(25);
			setFields(nameField, displayLabelField, mappingItemDisplayNameField, delActionField);
			addRecordDropHandler(new RecordDropHandler() {

				@Override
				public void onRecordDrop(RecordDropEvent event) {
					ListGridRecord dropRecord = event.getDropRecords()[0];

					if (dropRecord instanceof TreeNode) {
						TreeNode node = (TreeNode) event.getDropRecords()[0];
						String name = node.getAttribute("name");
						String displayName = node.getAttribute("displayName");
						PropertyDefinition pd = (PropertyDefinition) node.getAttributeAsObject("propertyDefinition");

						if (pd instanceof BinaryProperty || pd instanceof LongTextProperty) {
							event.cancel();
							return;
						}

						int index = event.getIndex();
						if (index >= 0) {
							ListGridRecord targetRecord = getRecord(index);
							targetRecord.setAttribute("mappingItemName", name);
							targetRecord.setAttribute("mappingItemDisplayLabel", displayName);
							refreshFields();
						}
						event.cancel();
					}
				}
			});

			initGrid();
		}

		private void initGrid() {
			for (TreeViewGridColModel cm : colModel) {
				ListGridRecord record = new ListGridRecord();
				record.setAttribute("name", cm.getName());
				record.setAttribute("displayLabel", cm.getDisplayLabel());
				addData(record);
			}
		}

		@Override
		protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
			String fieldName = this.getFieldName(colNum);
			Canvas ret = null;
			if (fieldName.equals("delAction")) {
				ImgButton deleteBtn = new ImgButton();
				deleteBtn.setShowDown(false);
				deleteBtn.setShowRollOver(false);
				deleteBtn.setLayoutAlign(Alignment.CENTER);
				deleteBtn.setSrc("remove.png");
				deleteBtn.setPrompt(AdminClientMessageUtil.getString("ui_metadata_aggregation_dashboard_DashboardUnitPane_releaseMappingFilterItem"));
				deleteBtn.setHeight(16);
				deleteBtn.setWidth(16);
				deleteBtn.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {

						SC.confirm(AdminClientMessageUtil.getString("ui_metadata_aggregation_dashboard_DashboardUnitPane_deleteConfirm"),
								AdminClientMessageUtil.getString("ui_metadata_aggregation_dashboard_DashboardUnitPane_deleteFilterItemConf", record.getAttribute("displayLabel"))
								, new BooleanCallback() {

							@Override
							public void execute(Boolean value) {
								if (value) {
									record.setAttribute("mappingItemName", (String) null);
									record.setAttribute("mappingItemDisplayLabel", (String) null);
									refreshFields();
								}
							}
						});
					}
				});
				ret = deleteBtn;
			}
			return ret;
		}

		private ListGridRecord getRecord(String name) {
			if (name != null) {
				for (ListGridRecord record : getRecords()) {
					if (name.equals(record.getAttribute("name"))) {
						return record;
					}
				}
			}
			return null;
		}

		public void setDefinition(TreeViewItem item) {
			MetaDataServiceAsync service = MetaDataServiceFactory.get();
			List<TreeViewGridColModelMapping> mapping = item.getMapping();
			if (mapping != null && !mapping.isEmpty()) {
				for (TreeViewGridColModelMapping cm : mapping) {
					final ListGridRecord record = getRecord(cm.getName());
					if (record != null) {
						record.setAttribute("mappingItemName", cm.getMappingName());
						service.getPropertyDefinition(TenantInfoHolder.getId(), item.getDefName(), cm.getMappingName(), new AsyncCallback<PropertyDefinition>() {

							@Override
							public void onSuccess(PropertyDefinition property) {
								record.setAttribute("mappingItemDisplayLabel", property.getDisplayName());
							}

							@Override
							public void onFailure(Throwable caught) {
								SC.warn(AdminClientMessageUtil.getString("ui_metadata_treeview_TreeViewGrid_failedToGetEntityDef") + caught.getMessage());
							}
						});
					}
				}
			}
		}

		/**
		 * 集計フィルタマッピング定義を取得します。
		 * @return
		 */
		public List<TreeViewGridColModelMapping> getDefinition() {
			List<TreeViewGridColModelMapping> items = new ArrayList<TreeViewGridColModelMapping>();
			for (ListGridRecord record : getRecords()) {
				String mappingItemName = record.getAttribute("mappingItemName");
				if (mappingItemName != null) {
					TreeViewGridColModelMapping item = new TreeViewGridColModelMapping();
					item.setName(record.getAttribute("name"));
					item.setMappingName(mappingItemName);
					items.add(item);
				}
			}
			return items;
		}
	}

}
