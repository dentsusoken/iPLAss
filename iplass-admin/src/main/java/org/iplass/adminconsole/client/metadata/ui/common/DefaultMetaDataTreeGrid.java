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

import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpTreeGrid;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

public abstract class DefaultMetaDataTreeGrid extends MtpTreeGrid {

	protected Tree tree;

	protected TreeNode root;

	public DefaultMetaDataTreeGrid() {

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

		initializeData();
	}

	/**
	 * 表示対象フィールド設定
	 */
	protected void setGridFields() {
		List<ListGridField> fields = new ArrayList<>();

		ListGridField nameField = new ListGridField(DataSourceConstants.FIELD_NAME);
		fields.add(nameField);

		setFields(fields.toArray(new ListGridField[]{}));
	}


	protected void initializeData() {
		setGridFields();

		root = new TreeNode("root");

		MetaDataServiceAsync service = MetaDataServiceFactory.get();
		service.getMetaDataTree(TenantInfoHolder.getId(), definitionClassName(), new AsyncCallback<MetaTreeNode>() {

			@Override
			public void onSuccess(MetaTreeNode result) {
				tree = new Tree();
				tree.setModelType(TreeModelType.CHILDREN);
				tree.setRoot(root);
				convertNode(root, result);
				setData(tree);
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});

	}

	/**
	 * ツリーノードに変換
	 * @param parent
	 * @param treeNode
	 * @return
	 */
	protected TreeNode[] convertNode(TreeNode parent, MetaTreeNode treeNode) {
		TreeNode[] entries = new TreeNode[treeNode.getAllNodeCount()];
		int i = 0;
		if (treeNode.getChildren() != null) {
			for (MetaTreeNode child : treeNode.getChildren()) {
				//フォルダ要素
				entries[i] = createChildNode(child);
				tree.add(entries[i], parent);
				convertNode(entries[i], child);
				i++;
			}
		}
		if (treeNode.getItems() != null) {
			for (MetaTreeNode item : treeNode.getItems()) {
				if (isVisibleItem(item)) {
					//Item要素(AdminMenuTreeNodeではなくMetaDataItemMenuTreeNodeをセット)
					entries[i] = createItemNode(item);
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
	protected TreeNode createChildNode(MetaTreeNode child) {
		TreeNode node = new TreeNode(child.getName());
		node.setCanAcceptDrop(false);
		node.setCanDrag(false);
		return node;
	}

	/**
	 * <p>アイテムノードを生成します。</p>
	 *
	 * @param item アイテムノード
	 * @return アイテムノード
	 */
	protected TreeNode createItemNode(MetaTreeNode item) {
		String name = null;
		if (item.getName().contains("/")) {
			name = item.getName().substring(item.getName().lastIndexOf("/") + 1);
		} else if (item.getName().contains(".")) {
			name = item.getName().substring(item.getName().lastIndexOf(".") + 1);
		} else {
			name = item.getName();
		}
		TreeNode node = new TreeNode(name);
		node.setAttribute("originalDisplayName", item.getDisplayName());
		node.setAttribute("defName", item.getName());
		node.setCanAcceptDrop(false);
		node.setCanDrag(true);
		node.setIsFolder(false);
		return node;
	}

	protected boolean isVisibleItem(MetaTreeNode item) {
		return true;
	}

	protected abstract String definitionClassName();
}
