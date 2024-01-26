/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.menu.MenuItemHolder;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceAsync;
import org.iplass.adminconsole.shared.metadata.rpc.MetaDataServiceFactory;
import org.iplass.mtp.view.menu.ActionMenuItem;
import org.iplass.mtp.view.menu.EntityMenuItem;
import org.iplass.mtp.view.menu.MenuItem;
import org.iplass.mtp.view.menu.NodeMenuItem;
import org.iplass.mtp.view.menu.UrlMenuItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * メニューアイテム データソース
 */
public class MenuItemTreeDS extends AbstractAdminDataSource {

	public enum MenuItemType {
		NODE, ACTION, ENTITY, URL
	}

	public enum FieldName {
		ISITEMTOP, TYPE, VALUEOBJECT, REMARKS
	}

	private static MenuItemTreeDS instance;

	private static final DataSourceField[] fields;

	static {
		DataSourceField name = new DataSourceField(
				DataSourceConstants.FIELD_NAME,
				FieldType.TEXT,
				DataSourceConstants.FIELD_NAME_TITLE);
		DataSourceField dispName = new DataSourceField(
				DataSourceConstants.FIELD_DISPLAY_NAME,
				FieldType.TEXT,
				DataSourceConstants.FIELD_DISPLAY_NAME_TITLE);
		DataSourceField remarks = new DataSourceField(
				FieldName.REMARKS.name(),
				FieldType.TEXT,
				"");

		fields = new DataSourceField[] {name, dispName, remarks};
	}

	private MetaDataServiceAsync service = MetaDataServiceFactory.get();

	private TreeNode nodeItemNode;
	private TreeNode actionItemNode;
	private TreeNode entityItemNode;
	private TreeNode urlItemNode;

	public static MenuItemTreeDS getInstance() {
		if (instance == null) {
			instance = new MenuItemTreeDS();
		}
		return instance;
	}

	public MenuItemTreeDS() {
		setFields(fields);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request, final DSResponse response) {

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
				response.setData(roots.toArray(new Record[] {}));
				processResponse(requestId, response);
			}
		});

	}

	public TreeNode getTypeRootNode(MenuItem menuItem) {

		MenuItemType type = getItemType(menuItem);
		switch (type) {
			case NODE:
				return nodeItemNode;
			case ACTION:
				return actionItemNode;
			case ENTITY:
				return entityItemNode;
			case URL:
				return urlItemNode;
			default:
				return null;
		}
	}

	public TreeNode createFolderNode(String path, String name, MenuItemType type) {

		TreeNode folder = new TreeNode(path);
		folder.setAttribute(DataSourceConstants.FIELD_NAME, path);
		folder.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, name);
		folder.setAttribute(FieldName.ISITEMTOP.name(), false);
		folder.setAttribute(FieldName.TYPE.name(), type);
		return folder;
	}

	public TreeNode createMenuItemNode(MenuItem menuItem) {

		TreeNode itemNode = new TreeNode(menuItem.getName());
		itemNode.setAttribute(DataSourceConstants.FIELD_NAME, menuItem.getName());
		itemNode.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, getSimpleName(menuItem.getName()));
		itemNode.setAttribute(FieldName.VALUEOBJECT.name(), menuItem);
		itemNode.setAttribute(FieldName.ISITEMTOP.name(), false);
		itemNode.setAttribute(FieldName.TYPE.name(), getItemType(menuItem));
		itemNode.setAttribute(FieldName.REMARKS.name(), getRemarks(menuItem));
		itemNode.setIcon(getIcon(menuItem));
		itemNode.setIsFolder(false);
		return itemNode;
	}

	public void updateMenuItemNode(TreeNode itemNode, MenuItem updateItem) {
		itemNode.setAttribute(DataSourceConstants.FIELD_NAME, updateItem.getName());
		itemNode.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, getSimpleName(updateItem.getName()));
		itemNode.setAttribute(FieldName.VALUEOBJECT.name(), updateItem);
		itemNode.setAttribute(FieldName.REMARKS.name(), getRemarks(updateItem));
	}

	private List<TreeNode> createMenuItemTreeNode(MenuItemHolder holder) {
		List<TreeNode> nodes = new ArrayList<>();

		// NodeItem
		nodeItemNode = createTypeRootNode("NodeMenuItem", MenuItemType.NODE);
		if (holder.getNodeMenuItemList() != null && !holder.getNodeMenuItemList().isEmpty()) {
			nodes.add(createTypeTreeNodeList(nodeItemNode, holder.getNodeMenuItemList()));
		} else {
			nodeItemNode.setChildren(new TreeNode[] {});
			nodes.add(nodeItemNode);
		}

		actionItemNode = createTypeRootNode("ActionMenuItem", MenuItemType.ACTION);
		if (holder.getActionMenuItemList() != null && !holder.getActionMenuItemList().isEmpty()) {
			nodes.add(createTypeTreeNodeList(actionItemNode, holder.getActionMenuItemList()));
		} else {
			actionItemNode.setChildren(new TreeNode[] {});
			nodes.add(actionItemNode);
		}

		entityItemNode = createTypeRootNode("EntityMenuItem", MenuItemType.ENTITY);
		if (holder.getEntityMenuItemList() != null && !holder.getEntityMenuItemList().isEmpty()) {
			nodes.add(createTypeTreeNodeList(entityItemNode, holder.getEntityMenuItemList()));
		} else {
			entityItemNode.setChildren(new TreeNode[] {});
			nodes.add(entityItemNode);
		}

		urlItemNode = createTypeRootNode("UrlMenuItem", MenuItemType.URL);
		if (holder.getUrlMenuItemList() != null && !holder.getUrlMenuItemList().isEmpty()) {
			nodes.add(createTypeTreeNodeList(urlItemNode, holder.getUrlMenuItemList()));
		} else {
			urlItemNode.setChildren(new TreeNode[] {});
			nodes.add(urlItemNode);
		}

		return nodes;
	}

	private TreeNode createTypeRootNode(String name, MenuItemType type) {
		TreeNode rootNode = new TreeNode(name);
		rootNode.setAttribute(DataSourceConstants.FIELD_NAME, name);
		rootNode.setAttribute(DataSourceConstants.FIELD_DISPLAY_VALUE, name);
		rootNode.setAttribute(FieldName.ISITEMTOP.name(), true);
		rootNode.setAttribute(FieldName.TYPE.name(), type);
		rootNode.setIsFolder(true);
		return rootNode;
	}

	private TreeNode createTypeTreeNodeList(TreeNode rootItemNode, List<? extends MenuItem> items) {
		if (items != null) {
			// nameでソート
			Collections.sort(items, new Comparator<MenuItem>() {
				@Override
				public int compare(MenuItem o1, MenuItem o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

			Map<String, WorkMenuItemNode> treeNodeMap = new HashMap<>();
			WorkMenuItemNode rootWorkNode = new WorkMenuItemNode(rootItemNode);
			treeNodeMap.put("", rootWorkNode);

			for (MenuItem item : items) {
				MenuItemType type = getItemType(item);

				String[] nodePaths = item.getName().split("/");

				String prePath = "";
				WorkMenuItemNode current = rootWorkNode;
				if (nodePaths.length > 1) {
					// フォルダを含む
					for (int i = 0; i < nodePaths.length - 1; i++) {
						String folderPath = prePath + nodePaths[i] + "/";

						if (treeNodeMap.containsKey(folderPath)) {
							// すでに作成済み
							current = treeNodeMap.get(folderPath);
						} else {

							TreeNode folder = createFolderNode(folderPath, nodePaths[i], type);

							// １つ前のNodeに追加
							current.addFolder(folder);

							current = new WorkMenuItemNode(folder);
							treeNodeMap.put(folderPath, current);
						}
						prePath = folderPath;
					}
				}

				// itemの作成
				TreeNode itemNode = createMenuItemNode(item);
				current.addChild(itemNode);
			}

			// Workに格納されたNodeのChild成形
			for (WorkMenuItemNode entry : treeNodeMap.values()) {
				entry.createChildNode();
			}

			// RootのNodeを返す
			return rootWorkNode.getTreeNode();
		}

		return rootItemNode;
	}

	// ツリー編集用クラス（デフォルトのTreeNodeだとaddChildできないので）
	private class WorkMenuItemNode {

		private TreeNode node;
		private List<TreeNode> folder = new ArrayList<>();
		private List<TreeNode> children = new ArrayList<>();

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
			// Folderを先に表示させるため、Folderに対して子供を追加
			folder.addAll(children);
			if (folder.size() > 0) {
				node.setChildren(folder.toArray(new TreeNode[] {}));
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

	private MenuItemType getItemType(MenuItem item) {
		if (item instanceof NodeMenuItem) {
			return MenuItemType.NODE;
		} else if (item instanceof ActionMenuItem) {
			return MenuItemType.ACTION;
		} else if (item instanceof EntityMenuItem) {
			return MenuItemType.ENTITY;
		} else if (item instanceof UrlMenuItem) {
			return MenuItemType.URL;
		}
		return null;
	}

	private String getRemarks(MenuItem menuItem) {
		if (menuItem instanceof ActionMenuItem) {
			return ((ActionMenuItem)menuItem).getActionName();
		} else if (menuItem instanceof EntityMenuItem) {
			EntityMenuItem entityItem = (EntityMenuItem)menuItem;
			String entityDefinitionName = entityItem.getEntityDefinitionName();
			if (SmartGWTUtil.isNotEmpty(entityItem.getViewName())) {
				entityDefinitionName += " (" + entityItem.getViewName() + ")";
			}
			return entityDefinitionName;
		} else if (menuItem instanceof UrlMenuItem) {
			return SafeHtmlUtils.htmlEscape(((UrlMenuItem)menuItem).getUrl());
		} else {
			return "";
		}
	}
}
