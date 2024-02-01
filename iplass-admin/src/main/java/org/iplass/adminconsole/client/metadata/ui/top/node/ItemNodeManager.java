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

package org.iplass.adminconsole.client.metadata.ui.top.node;

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.tree.Tree;

/**
 *
 * @author lis3wg
 */
public abstract class ItemNodeManager {

	private MetaDataServiceAsync service = null;

	/**
	 * コンストラクタ
	 */
	public ItemNodeManager() {
		 service = MetaDataServiceFactory.get();
	}

	public TopViewNode getFolder() {
		TopViewNode node = new TopViewNode(getName());
		node.setIsFolder(true);
		return node;
	}

	public abstract String getName();

	public abstract void loadChild(Tree tree, TopViewNode parent);

	//以下getMetaDataTreeを使う場合はoverrideする

	protected String treeDefinitionClassName() {
		return null;
	}

	protected String getDefinitionClassName() {
		return null;
	}

	protected boolean isParts() {
		return false;
	}

	protected boolean isWidget() {
		return false;
	}

	protected boolean isUnique() {
		return false;
	}

	protected void getMetaDataTree(final Tree tree, final TopViewNode parent) {
		//メタデータからツリーを作成
		service.getMetaDataTree(TenantInfoHolder.getId(), treeDefinitionClassName(), new AsyncCallback<MetaTreeNode>() {

			@Override
			public void onSuccess(MetaTreeNode result) {
				convertNode(tree, parent, result);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log(caught.getMessage());
			}
		});

	}

	/**
	 * <p>{@link MetaTreeNode} を {@link TopViewNode} に変換します。</p>
	 *
	 * <p>表示させたくないItemがある場合は、{@link #isVisibleItem(MetaTreeNode)}}
	 * をオーバーライドして制御してください。
	 * </p>
	 *
	 * @param parent 親 {@link TopViewNode}
	 * @param treeNode 変換対象 {@link MetaTreeNode}
	 * @return 変換後 {@link TopViewNode} の配列
	 */
	private TopViewNode[] convertNode(Tree tree, TopViewNode parent, MetaTreeNode treeNode) {
		TopViewNode[] entries = new TopViewNode[treeNode.getAllNodeCount()];
		int i = 0;
		if (treeNode.getChildren() != null) {
			for (MetaTreeNode child : treeNode.getChildren()) {
				//フォルダ要素
				entries[i] = createChildNode(child);
				tree.add(entries[i], parent);	//node.setChildrenだとツリーのIndentがうまくいかない。treeに対して操作する
				convertNode(tree, entries[i], child);
				i++;
			}
		}
		if (treeNode.getItems() != null) {
			for (MetaTreeNode item : treeNode.getItems()) {
				//Item要素(TopViewNodeではなくMetaDataItemMenuTreeNodeをセット)
				entries[i] = createItemNode(item);
				entries[i].setIsFolder(false);
				tree.add(entries[i], parent);
				i++;
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
	private TopViewNode createChildNode(MetaTreeNode child) {
		TopViewNode folder = new TopViewNode(child.getName());
		folder.setIsFolder(true);
		return folder;
	}

	/**
	 * <p>アイテムノードを生成します。</p>
	 *
	 * @param item アイテムノード
	 * @return アイテムノード
	 */
	private TopViewNode createItemNode(MetaTreeNode item) {
		String displayName = null;
		if (item.getDisplayName() == null || item.getDisplayName().isEmpty()) {
			displayName = getSimpleName(item.getName());
		} else {
			displayName = getSimpleName(item.getName()) + "(" + item.getDisplayName() + ")";
		}
		return new TopViewNode(displayName, getDefinitionClassName(), item.getName(),
				isParts(), isWidget(), isUnique());
	}

	private String getSimpleName(String name) {
		if (name == null || name.isEmpty()) return "";

		if (name.contains("/")) {
			if (name.lastIndexOf("/") < name.length()) {
				return name.substring(name.lastIndexOf("/") + 1);
			} else {
				return name.substring(name.lastIndexOf("/"));
			}
		} else {
			return name;
		}
	}
}
