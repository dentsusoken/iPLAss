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

package org.iplass.adminconsole.client.metadata.ui.common;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.metadata.data.entity.PropertyTreeDS;
import org.iplass.mtp.entity.definition.properties.ReferenceProperty;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 集計プロパティグリッド
 * @author lis3wg
 *
 */
public class EntityPropertyTreeGrid extends TreeGrid implements EntityPropertyGrid {

	private boolean showRoot;

	/** 内部ツリー */
	private Tree tree;

	/**
	 * コンストラクタ
	 */
	public EntityPropertyTreeGrid(boolean showRoot) {
		this.showRoot = showRoot;

		setLeaveScrollbarGap(false);
		setShowHeader(false);
		setDragDataAction(DragDataAction.NONE);
		setSelectionType(SelectionStyle.SINGLE);
		setCanDragRecordsOut(true);
		setBorder("none");

		setCanSort(false);
		setCanFreezeFields(false);
		setCanPickFields(false);
		setShowRoot(false);
		setShowOpener(false);

		tree = new Tree();
		tree.setModelType(TreeModelType.CHILDREN);
		setData(tree);

		addCellDoubleClickHandler(new CellDoubleClickHandler() {

			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				final TreeNode node = (TreeNode) event.getRecord();
				Boolean isFolder = node.getAttributeAsBoolean("isFolder");
				Boolean isLoaded = node.getAttributeAsBoolean("isLoaded");
				if ((isFolder != null && isFolder) && (isLoaded == null || !isLoaded)) {
					ReferenceProperty pd = (ReferenceProperty) node.getAttributeAsObject("propertyDefinition");

					PropertyTreeDS childDS = PropertyTreeDS.create(pd.getObjectDefinitionName(), false);
					childDS.fetchData(null, new DSCallback() {

						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							Record[] list = response.getData();
							if (list != null && list.length > 0) {
								TreeNode[] nodes = new TreeNode[list.length];
								for (int i = 0; i < list.length; i++) {
									nodes[i] = (TreeNode) list[i];
									String parentName = node.getAttribute("name");
									nodes[i].setAttribute("name", parentName + "." + nodes[i].getAttribute("name"));

									String displayName = node.getAttribute("displayName");
									String parentDisplayName = node.getAttribute("parentDisplayName");
									if (parentDisplayName != null && !parentDisplayName.isEmpty()) {
										displayName = parentDisplayName + "."  + displayName;
									}
									nodes[i].setAttribute("parentDisplayName", displayName);
								}
								tree.addList(nodes, node);
								tree.openFolder(node);
							}
							node.setAttribute("isLoaded", true);
						}
					});
				}
			}
		});

	}

	/**
	 * 表示対象フィールド設定
	 */
	private void setGridFields() {
		List<ListGridField> fields = new ArrayList<ListGridField>();

		ListGridField displayNameField = new ListGridField("outputDisplayName");
		fields.add(displayNameField);

		setFields(fields.toArray(new ListGridField[]{}));
	}

	/**
	 * 初期表示処理
	 */
	private void initializeData(String defName) {
		setGridFields();

		if (defName != null && !defName.isEmpty()) {
			PropertyTreeDS ds = PropertyTreeDS.create(defName, this.showRoot);
			ds.fetchData(null, new DSCallback() {

				@Override
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					Record[] list = response.getData();
					if (list != null && list.length > 0) {

						TreeNode root = new TreeNode();
						root.setAttribute("name", "root");
						root.setAttribute("displayName", "root");
						root.setIsFolder(true);
						TreeNode[] nodes = new TreeNode[list.length];
						for (int i = 0; i < list.length; i++) {
							nodes[i] = (TreeNode) list[i];
						}
						root.setChildren(nodes);

						tree = new Tree();	//Treeをnewしなおさないと階層がうまく表示されない
						tree.setRoot(root);
						tree.setModelType(TreeModelType.CHILDREN);

						if (nodes.length > 0 && tree.hasChildren(nodes[0])) {
							tree.openFolder(nodes[0]);
						}

						setData(tree);
					}
				}
			});
		} else {
			TreeNode root = new TreeNode();
			root.setAttribute("name", "root");
			root.setAttribute("displayName", "root");
			root.setIsFolder(true);

			tree = new Tree();	//Treeをnewしなおさないと階層がうまく表示されない
			tree.setRoot(root);
			tree.setModelType(TreeModelType.CHILDREN);

			setData(tree);

		}
	}

	public TreeNode getParent(TreeNode node) {
		return tree.getParent(node);
	}

	/**
	 * Entity定義名を元にプロパティの再読み込みを行います。
	 * @param defName Entity定義名
	 */
	@Override
	public void refresh(String defName) {
		initializeData(defName);
	}
}
