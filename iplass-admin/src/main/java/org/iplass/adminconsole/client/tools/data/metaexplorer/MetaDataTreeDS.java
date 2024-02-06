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

package org.iplass.adminconsole.client.tools.data.metaexplorer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.RepositoryType;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.tree.TreeNode;

public class MetaDataTreeDS extends AbstractAdminDataSource {

	public static final String CRITERIA_REPOSITORY_TYPE = "CRITERIA_REPOSITORY_TYPE";
	public static final String CRITERIA_UPDATE_FROM = "CRITERIA_UPDATE_FROM";
	public static final String CRITERIA_UPDATE_TO = "CRITERIA_UPDATE_TO";
	public static final String CRITERIA_TAG_FROM = "CRITERIA_TAG_FROM";
	public static final String CRITERIA_TAG_TO = "CRITERIA_TAG_TO";

	public enum FIELD_NAME {
		NAME,
		DISPLAY_NAME,
		DESCRIPTION,
		PATH,
		CLASS_NAME,
		ITEM_ID,
		VERSION,
		STATE,
		IS_ERROR,
		ERROR_MESSAGE,
		REPOSITORY,
		UPDATE_DATE,

		SHARED,
		SHARED_NAME,
		SHARABLE,
		OVERWRITABLE
	}

	private RepositoryType repositoryType;
	private Date updateDateFrom;
	private Date updateDateTo;
	private Date tagCreateDateFrom;
	private Date tagCreateDateTo;

	public static MetaDataTreeDS getInstance() {
		return new MetaDataTreeDS();
	}

	public MetaDataTreeDS() {
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		//選択RepositoryKindの取得
		getCriteria(request.getCriteria());

		MetaDataExplorerServiceAsync service = MetaDataExplorerServiceFactory.get();
		service.getMetaTree(TenantInfoHolder.getId(), new AsyncCallback<MetaTreeNode>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(MetaTreeNode result) {
				TreeNode root = createMetaDataTreeNode(result);
				if (root != null) {
					response.setData(new Record[]{root});
				} else {
					response.setData(new Record[]{});
				}
				processResponse(requestId, response);
			}

		});
	}

	@SuppressWarnings("rawtypes")
	private void getCriteria(Criteria criteria) {
		repositoryType = RepositoryType.ALL;
		if (criteria != null && criteria.getValues() != null) {
			Map criteriaMap = criteria.getValues();
			Set criteriaSet = criteriaMap.entrySet();
			for (Iterator criteriaIte = criteriaSet.iterator(); criteriaIte.hasNext();) {
				Map.Entry entry = (Map.Entry)criteriaIte.next();
				if (CRITERIA_REPOSITORY_TYPE.equals(entry.getKey())) {
					repositoryType = RepositoryType.valueOfTypeName((String)entry.getValue());
				} else if (CRITERIA_UPDATE_FROM.equals(entry.getKey())) {
					updateDateFrom = (Date)entry.getValue();
				} else if (CRITERIA_UPDATE_TO.equals(entry.getKey())) {
					updateDateTo = (Date)entry.getValue();
				} else if (CRITERIA_TAG_FROM.equals(entry.getKey())) {
					tagCreateDateFrom = (Date)entry.getValue();
				} else if (CRITERIA_TAG_TO.equals(entry.getKey())) {
					tagCreateDateTo = (Date)entry.getValue();
				}
			}
		}
	}

	/**
	 * ルートとなるMetaTreeNodeからルートTreeNodeを生成します。
	 *
	 * @param metaRoot
	 * @return
	 */
	private TreeNode createMetaDataTreeNode(MetaTreeNode metaRoot) {
		TreeNode[] contextPathTreeNodes = createContextPathTreeNodeList(metaRoot.getChildren()).toArray(new TreeNode[]{});
		if (contextPathTreeNodes.length == 0) {
			return null;
		}

		TreeNode root = new TreeNode("root");
		root.setAttribute(FIELD_NAME.NAME.name(), metaRoot.getName());
		root.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), metaRoot.getName());	//フォルダの場合はNameをセット
		root.setAttribute(FIELD_NAME.DESCRIPTION.name(), metaRoot.getDescription());
		root.setAttribute(FIELD_NAME.IS_ERROR.name(), metaRoot.isError());
		root.setAttribute(FIELD_NAME.ERROR_MESSAGE.name(), metaRoot.getErrorMessage());

		root.setChildren(contextPathTreeNodes);
		root.setIsFolder(true);
		return root;
	}

	/**
	 * ContextPathとしてのMetaTreeNodeのリストからTreeNodeのリストを生成します。
	 *
	 * @param metaNodes
	 * @return
	 */
	private List<TreeNode> createContextPathTreeNodeList(List<MetaTreeNode> metaNodes) {
		List<TreeNode> pathNodes = new ArrayList<TreeNode>();
		if (metaNodes != null) {
			for (MetaTreeNode metaNode : metaNodes) {
				TreeNode node = createContextPathTreeNode(metaNode);
				if (node != null) {
					pathNodes.add(node);
				}
			}
		}
		return pathNodes;
	}

	/**
	 * ContextPathとなるMetaTreeNodeからTreeNodeを生成します。
	 *
	 * @param metaNode
	 * @return
	 */
	private TreeNode createContextPathTreeNode(MetaTreeNode metaNode) {
		TreeNode node = new TreeNode(metaNode.getPath());
		node.setAttribute(FIELD_NAME.NAME.name(), metaNode.getName());
		node.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), metaNode.getName());	//フォルダの場合はNameをセット
		node.setAttribute(FIELD_NAME.DESCRIPTION.name(), metaNode.getDescription());
		node.setAttribute(FIELD_NAME.PATH.name(), metaNode.getPath());
		node.setAttribute(FIELD_NAME.IS_ERROR.name(), metaNode.isError());
		node.setAttribute(FIELD_NAME.ERROR_MESSAGE.name(), metaNode.getErrorMessage());
		node.setIsFolder(true);

		List<TreeNode> children = new ArrayList<TreeNode>();
		children.addAll(createContextPathTreeNodeList(metaNode.getChildren()));
		children.addAll(createMetaDataEntryTreeNodeList(metaNode.getItems()));
		if (!children.isEmpty()) {
			node.setChildren(children.toArray(new TreeNode[]{}));
			return node;
		} else {
			return null;
		}
	}

	/**
	 * MetaDataEntryとしてのMetaTreeNodeのリストからTreeNodeのリストを生成します。
	 *
	 * @param items
	 * @return
	 */
	private List<TreeNode> createMetaDataEntryTreeNodeList(List<MetaTreeNode> items) {
		List<TreeNode> itemNodes = new ArrayList<TreeNode>();
		if (items != null) {
			for (MetaTreeNode item : items) {
				TreeNode node = createMetaDataEntryTreeNode(item);
				if (node != null) {
					itemNodes.add(node);
				}
			}
		}
		return itemNodes;
	}

	/**
	 * MetaDataEntryとなるMetaTreeNodeからTreeNodeを生成します。
	 *
	 * @param item
	 * @return
	 */
	private TreeNode createMetaDataEntryTreeNode(MetaTreeNode item) {

		//Repository種類によるFilter
		if (RepositoryType.SHARED.equals(repositoryType)) {
			if (!item.isShared()) {
				return null;
			}
		} else if (RepositoryType.LOCAL.equals(repositoryType)) {
			if (item.isShared()) {
				return null;
			}
		}

		//updateDateによるFilter
		if (updateDateFrom != null) {
			if (item.getUpdateDate() == null) {
				//Localリポジトリ以外なので除外
				return null;
			}
			long updateDate = item.getUpdateDate().getTime();
			//更新日比較は自身を含む(updateDataFrom <= updateDate)
			if (updateDateFrom.getTime() > updateDate) {
				return null;
			}
		}
		if (updateDateTo != null) {
			if (item.getUpdateDate() == null) {
				//Localリポジトリ以外なので除外
				return null;
			}
			long updateDate = item.getUpdateDate().getTime();
			//更新日比較は自身を含む(updateDataTo => updateDate)
			if (updateDateTo.getTime() < updateDate) {
				return null;
			}
		}

		//tagCreateDateによるFilter
		if (tagCreateDateFrom != null) {
			if (item.getUpdateDate() == null) {
				//Localリポジトリ以外なので除外
				return null;
			}
			long updateDate = item.getUpdateDate().getTime();
			//タグ作成日比較は自身を含まない(tagCreateDataFrom < updateDate)
			if (tagCreateDateFrom.getTime() >= updateDate) {
				return null;
			}
		}
		if (tagCreateDateTo != null) {
			if (item.getUpdateDate() == null) {
				//Localリポジトリ以外なので除外
				return null;
			}
			long updateDate = item.getUpdateDate().getTime();
			//タグ作成日比較は自身を含まない(tagCreateDataTo > updateDate)
			if (tagCreateDateTo.getTime() <= updateDate) {
				return null;
			}
		}


		TreeNode node = new TreeNode(item.getName());
		node.setAttribute(FIELD_NAME.NAME.name(), item.getName());
		String displayName = getSimpleName(item.getPath());
//		if (item.getDisplayName() != null) {
//			displayName = displayName + " (" + item.getDisplayName() + ")";
//		}
		node.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), displayName);
		node.setAttribute(FIELD_NAME.DESCRIPTION.name(), item.getDescription());
		node.setAttribute(FIELD_NAME.PATH.name(), item.getPath());
		node.setAttribute(FIELD_NAME.CLASS_NAME.name(), item.getDefinitionClassName());
		node.setAttribute(FIELD_NAME.ITEM_ID.name(), item.getId());
		node.setAttribute(FIELD_NAME.VERSION.name(), item.getVersion());
		node.setAttribute(FIELD_NAME.STATE.name(), item.getState());
//		node.setAttribute(FIELD_NAME.UPDATE_DATE.name(), item.getUpdateDate());
		if (item.getUpdateDate() != null) {
			node.setAttribute(FIELD_NAME.UPDATE_DATE.name(), SmartGWTUtil.formatTimestamp(item.getUpdateDate()));
		}
		node.setAttribute(FIELD_NAME.REPOSITORY.name(), item.getRepository());
		node.setAttribute(FIELD_NAME.IS_ERROR.name(), item.isError());
		node.setAttribute(FIELD_NAME.ERROR_MESSAGE.name(), item.getErrorMessage());
		node.setAttribute(FIELD_NAME.SHARED.name(), item.isShared());
		node.setAttribute(FIELD_NAME.SHARED_NAME.name(), MetaDataUtil.getMetaRepositoryTypeName(item.isShared(), item.isSharedOverwrite()));
		node.setAttribute(FIELD_NAME.SHARABLE.name(), item.isSharable());
		node.setAttribute(FIELD_NAME.OVERWRITABLE.name(), item.isOverwritable());
		node.setIcon(getItemIcon(item));
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

	/**
	 * <p>アイテムノードのアイコンを返します。</p>
	 *
	 * @param node アイテムノード
	 * @return アイコン
	 */
	private String getItemIcon(MetaTreeNode node) {
		return MetaDataUtil.getMetaTypeIcon(node.isShared(), node.isSharedOverwrite(), node.isOverwritable());
	}

//	private void debug(String message) {
//		GWT.log("MetaDataTreeDS DEBUG " + message);
//	}
}
