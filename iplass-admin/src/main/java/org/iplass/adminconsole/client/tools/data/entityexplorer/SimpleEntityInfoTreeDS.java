/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.entityexplorer;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.tools.dto.entityexplorer.SimpleEntityTreeNode;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.entityexplorer.EntityExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.tree.TreeNode;

public class SimpleEntityInfoTreeDS extends AbstractAdminDataSource {

	public enum FIELD_NAME {
		NAME,
		DISPLAY_NAME,

		DATA_COUNT,

		LISTNER_COUNT,
		VERSIONING,

		REPOSITORY,

		IS_ERROR,
		ERROR_MESSAGE,

		PATH
	}

	/** 件数取得制御フラグ */
	private boolean isGetDataCount = false;

	public static SimpleEntityInfoTreeDS getInstance(boolean isGetDataCount) {
		return new SimpleEntityInfoTreeDS(isGetDataCount);
	}

	public SimpleEntityInfoTreeDS(boolean isGetDataCount) {
		this.isGetDataCount = isGetDataCount;
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		EntityExplorerServiceAsync service = EntityExplorerServiceFactory.get();
		service.getSimpleEntityTree(TenantInfoHolder.getId(), isGetDataCount, new AsyncCallback<SimpleEntityTreeNode>() {

			@Override
			public void onSuccess(SimpleEntityTreeNode result) {
				TreeNode root = createTreeNode(result);
				response.setData(new Record[]{root});
				processResponse(requestId, response);
			}

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}
		});
	}

	private TreeNode createTreeNode(SimpleEntityTreeNode root) {
		TreeNode rootNode = new TreeNode("root");
		rootNode.setAttribute(FIELD_NAME.NAME.name(), root.getName());
		rootNode.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), root.getName());	//フォルダの場合はNameをセット
		rootNode.setAttribute(FIELD_NAME.IS_ERROR.name(), root.isError());
		rootNode.setAttribute(FIELD_NAME.ERROR_MESSAGE.name(), root.getErrorMessage());

		List<TreeNode> children = new ArrayList<>();
		children.addAll(createChildrenTreeNodeList(root.getChildren()));
		children.addAll(createEntityTreeNodeList(root.getItems()));
		if (!children.isEmpty()) {
			rootNode.setChildren(children.toArray(new TreeNode[]{}));
		}
		rootNode.setIsFolder(true);
		return rootNode;
	}

	private List<TreeNode> createChildrenTreeNodeList(List<SimpleEntityTreeNode> children) {
		List<TreeNode> pathNodes = new ArrayList<>();
		if (children != null) {
			for (SimpleEntityTreeNode metaNode : children) {
				TreeNode node = createChildTreeNode(metaNode);
				if (node != null) {
					pathNodes.add(node);
				}
			}
		}
		return pathNodes;
	}

	private TreeNode createChildTreeNode(SimpleEntityTreeNode metaNode) {
		TreeNode node = new TreeNode(metaNode.getPath());
		node.setAttribute(FIELD_NAME.NAME.name(), metaNode.getName());
		node.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), metaNode.getName());	//フォルダの場合はNameをセット
		node.setAttribute(FIELD_NAME.PATH.name(), metaNode.getPath());
		node.setAttribute(FIELD_NAME.IS_ERROR.name(), metaNode.isError());
		node.setAttribute(FIELD_NAME.ERROR_MESSAGE.name(), metaNode.getErrorMessage());
		node.setIsFolder(true);

		List<TreeNode> children = new ArrayList<>();
		children.addAll(createChildrenTreeNodeList(metaNode.getChildren()));
		children.addAll(createEntityTreeNodeList(metaNode.getItems()));
		if (!children.isEmpty()) {
			node.setChildren(children.toArray(new TreeNode[]{}));
			return node;
		} else {
			return null;
		}
	}

	private List<TreeNode> createEntityTreeNodeList(List<SimpleEntityTreeNode> items) {
		List<TreeNode> itemNodes = new ArrayList<>();
		if (items != null) {
			for (SimpleEntityTreeNode item : items) {
				TreeNode node = createEntityTreeNode(item);
				if (node != null) {
					itemNodes.add(node);
				}
			}
		}
		return itemNodes;
	}

	private TreeNode createEntityTreeNode(SimpleEntityTreeNode item) {

		TreeNode node = new TreeNode(item.getName());
		node.setAttribute(FIELD_NAME.NAME.name(), item.getName());
		String displayName = getSimpleName(item.getPath());
		if (item.getDisplayName() != null) {
			displayName = displayName + " (" + item.getDisplayName() + ")";
		}
		node.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), displayName);
		node.setAttribute(FIELD_NAME.PATH.name(), item.getPath());
		node.setAttribute(FIELD_NAME.REPOSITORY.name(), item.getRepository());
		node.setAttribute(FIELD_NAME.IS_ERROR.name(), item.isError());
		node.setAttribute(FIELD_NAME.ERROR_MESSAGE.name(), item.getErrorMessage());

		if (isGetDataCount) {
			node.setAttribute(FIELD_NAME.DATA_COUNT.name(), item.getCount());
		} else {
			node.setAttribute(FIELD_NAME.DATA_COUNT.name(), "-");
		}
		node.setAttribute(FIELD_NAME.LISTNER_COUNT.name(), item.getListenerCount());
		node.setAttribute(FIELD_NAME.VERSIONING.name(), item.getVersionControlType());
		node.setIsFolder(false);
		return node;
	}

	private String getSimpleName(String path) {
		if (path.contains("/")) {
			if (path.lastIndexOf("/") < path.length()) {
				return path.substring(path.lastIndexOf("/") + 1);
			} else {
				return path.substring(path.lastIndexOf("/"));
			}
		} else {
			return path;
		}
	}

}
