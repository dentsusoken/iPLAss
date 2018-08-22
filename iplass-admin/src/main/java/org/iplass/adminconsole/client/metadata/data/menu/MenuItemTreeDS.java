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

package org.iplass.adminconsole.client.metadata.data.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.data.DataSourceConstants;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.shared.metadata.dto.menu.MenuItemHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.NodeMenuItem;
import org.iplass.mtp.view.menu.UrlMenuItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.tree.TreeNode;


/**
 * メニューアイテム データソース
 */
public class MenuItemTreeDS extends AbstractAdminDataSource {

	public enum MenuItemType {
		NODE,
		ACTION,
		ENTITY,
		URL
	}

	public enum FieldName {
		ISITEMTOP,
		TYPE,
		VALUEOBJECT
	}

	private static MenuItemTreeDS instance;

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	public static MenuItemTreeDS getInstance() {
		if (instance == null) {
			instance = new MenuItemTreeDS("MenuItemDS");
		}
		return instance;
	}

	public MenuItemTreeDS(String id) {
		setID(id);
	}


	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		service.getMenuItemList(TenantInfoHolder.getId(), new AsyncCallback<MenuItemHolder>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);

				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(MenuItemHolder holder) {

				List<TreeNode> roots = createMenuItemTreeNode(holder);
				response.setData(roots.toArray(new Record[]{}));
				processResponse(requestId, response);
			}
		});

	}

	private List<TreeNode> createMenuItemTreeNode(MenuItemHolder holder) {
		List<TreeNode> nodes = new ArrayList<TreeNode>();

		//NodeItem
		TreeNode nodeItemNode = new TreeNode("NodeMenuItem");
		nodeItemNode.setAttribute(DataSourceConstants.FIELD_NAME, "NodeMenuItem");
		nodeItemNode.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, "NodeMenuItem");
		nodeItemNode.setAttribute(FieldName.ISITEMTOP.name(), true);
		nodeItemNode.setAttribute(FieldName.TYPE.name(), MenuItemType.NODE);
		nodeItemNode.setIsFolder(true);
		if (holder.getNodeMenuItemList() != null && !holder.getNodeMenuItemList().isEmpty()) {
			nodes.add(createTypeTreeNodeList(nodeItemNode, holder.getNodeMenuItemList(), MenuItemType.NODE));
		} else {
			nodeItemNode.setChildren(new TreeNode[] {});
			nodes.add(nodeItemNode);
		}

		TreeNode actionItemNode = new TreeNode("ActionMenuItem");
		actionItemNode.setAttribute(DataSourceConstants.FIELD_NAME, "ActionMenuItem");
		actionItemNode.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, "ActionMenuItem");
		actionItemNode.setAttribute(FieldName.ISITEMTOP.name(), true);
		actionItemNode.setAttribute(FieldName.TYPE.name(), MenuItemType.ACTION);
		actionItemNode.setIsFolder(true);
		if (holder.getActionMenuItemList() != null && !holder.getActionMenuItemList().isEmpty()) {
			nodes.add(createTypeTreeNodeList(actionItemNode, holder.getActionMenuItemList(), MenuItemType.ACTION));
		} else {
			actionItemNode.setChildren(new TreeNode[] {});
			nodes.add(actionItemNode);
		}

		TreeNode entityItemNode = new TreeNode("EntityMenuItem");
		entityItemNode.setAttribute(DataSourceConstants.FIELD_NAME, "EntityMenuItem");
		entityItemNode.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, "EntityMenuItem");
		entityItemNode.setAttribute(FieldName.ISITEMTOP.name(), true);
		entityItemNode.setAttribute(FieldName.TYPE.name(), MenuItemType.ENTITY);
		entityItemNode.setIsFolder(true);
		if (holder.getEntityMenuItemList() != null && !holder.getEntityMenuItemList().isEmpty()) {
			nodes.add(createTypeTreeNodeList(entityItemNode, holder.getEntityMenuItemList(), MenuItemType.ENTITY));
		} else {
			entityItemNode.setChildren(new TreeNode[] {});
			nodes.add(entityItemNode);
		}

		TreeNode urlItemNode = new TreeNode("UrlMenuItem");
		urlItemNode.setAttribute(DataSourceConstants.FIELD_NAME, "UrlMenuItem");
		urlItemNode.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, "UrlMenuItem");
		urlItemNode.setAttribute(FieldName.ISITEMTOP.name(), true);
		urlItemNode.setAttribute(FieldName.TYPE.name(), MenuItemType.URL);
		urlItemNode.setIsFolder(true);
		if (holder.getUrlMenuItemList() != null && !holder.getUrlMenuItemList().isEmpty()) {
			nodes.add(createTypeTreeNodeList(urlItemNode, holder.getUrlMenuItemList(), MenuItemType.URL));
		} else {
			urlItemNode.setChildren(new TreeNode[] {});
			nodes.add(urlItemNode);
		}

		return nodes;
	}

	private TreeNode createTypeTreeNodeList(TreeNode rootItemNode, List<? extends MenuItem> items, MenuItemType type) {
		if (items != null) {
			//nameでソート
            Collections.sort(items, new Comparator<MenuItem>() {
				@Override
				public int compare(MenuItem o1, MenuItem o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

    		Map<String, WorkMenuItemNode> treeNodeMap = new HashMap<String, WorkMenuItemNode>();
    		WorkMenuItemNode rootWorkNode = new WorkMenuItemNode(rootItemNode);
    		treeNodeMap.put("", rootWorkNode);

            for (MenuItem item : items) {
    			String[] nodePaths = item.getName().split("/");

    			String prePath = "";
    			WorkMenuItemNode current = rootWorkNode;
    			if (nodePaths.length > 1) {
    				//フォルダを含む
    				for (int i = 0; i < nodePaths.length - 1; i++) {
    					String folderPath = prePath + nodePaths[i] + "/";

    					if (treeNodeMap.containsKey(folderPath)) {
    						//すでに作成済み
    						current = treeNodeMap.get(folderPath);
    					} else {

    		    			TreeNode folder = new TreeNode(folderPath);
    		        		folder.setAttribute(DataSourceConstants.FIELD_NAME, folderPath);
    		        		folder.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, nodePaths[i]);
    		        		folder.setAttribute(FieldName.ISITEMTOP.name(), false);
    		        		folder.setAttribute(FieldName.TYPE.name(), type);

    						//１つ前のNodeに追加
    						current.addFolder(folder);

    						current = new WorkMenuItemNode(folder);
    						treeNodeMap.put(folderPath, current);
    					}
    					prePath = folderPath;
    				}
    			}

    			//itemの作成
    			TreeNode leaf = new TreeNode(item.getName());
        		leaf.setAttribute(DataSourceConstants.FIELD_NAME, item.getName());
        		leaf.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, getSimpleName(item.getName()));
        		leaf.setAttribute(FieldName.VALUEOBJECT.name(), item);
        		leaf.setAttribute(FieldName.ISITEMTOP.name(), false);
        		leaf.setAttribute(FieldName.TYPE.name(), type);
        		leaf.setIcon(getIcon(item));
        		leaf.setIsFolder(false);
    			current.addChild(leaf);
            }

            //Workに格納されたNodeのChild成形
            for (WorkMenuItemNode entry : treeNodeMap.values()) {
            	entry.createChildNode();
            }

            //RootのNodeを返す
            return rootWorkNode.getTreeNode();
		}

		return rootItemNode;
	}

	//ツリー編集用クラス（デフォルトのTreeNodeだとaddChildできないので）
	private class WorkMenuItemNode {

		private TreeNode node;
		private List<TreeNode> folder = new ArrayList<TreeNode>();
		private List<TreeNode> children = new ArrayList<TreeNode>();

		public WorkMenuItemNode(TreeNode node) {
			this.node = node;
		}

		public void addFolder(TreeNode child) {
			folder.add(child);
		}

		public void addChild(TreeNode child) {
			children.add(child);
		}

		public void createChildNode() {
			//Folderを先に表示させるため、Folderに対して子供を追加
			folder.addAll(children);
			if (folder.size() > 0) {
				node.setChildren(folder.toArray(new TreeNode[]{}));
			}
		}

		public TreeNode getTreeNode() {
			return node;
		}
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
	private String getIcon(MenuItem item) {
		if (item instanceof NodeMenuItem) {
			return "menuitem_node.png";
		} else if (item instanceof ActionMenuItem) {
			return "menuitem_action.png";
		} else if (item instanceof EntityMenuItem) {
			return "menuitem_entity.png";
		} else if (item instanceof UrlMenuItem) {
			return "menuitem_url.png";
		}
		return "";
	}

}
