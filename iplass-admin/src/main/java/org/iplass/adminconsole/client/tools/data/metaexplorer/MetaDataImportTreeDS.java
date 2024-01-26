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

package org.iplass.adminconsole.client.tools.data.metaexplorer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.iplass.adminconsole.client.base.data.AbstractAdminDataSource;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportFileInfo;
import org.iplass.adminconsole.shared.tools.dto.metaexplorer.ImportMetaDataStatus;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceAsync;
import org.iplass.adminconsole.shared.tools.rpc.metaexplorer.MetaDataExplorerServiceFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class MetaDataImportTreeDS extends AbstractAdminDataSource {

	public static final String CRITERIA_FILE_OID = "CRITERIA_FILE_OID";

	public enum FIELD_NAME {
		NAME,
		PATH,
		CLASS_NAME,
		ITEM_ID,
		DISPLAY_NAME,
		ACTION_NAME,
		IS_ERROR,
		IS_WARN,
		IS_INFO,
		MESSAGE,
		MESSAGE_DETAIL
	}

	private static MetaDataImportTreeDS instance;

	private String fileOid;
	private ImportFileInfo result;

	/** Pathに対するMetaDataEntryTreeNode(ステータスの反映高速化用) */
	private LinkedHashMap<String, TreeNode> pathToEntryNodes = new LinkedHashMap<String, TreeNode>();

	public static MetaDataImportTreeDS getInstance() {
		if (instance == null) {
			instance = new MetaDataImportTreeDS();
		}
		return instance;
	}

	public MetaDataImportTreeDS() {
	}

	public ImportFileInfo getResult() {
		return result;
	}

	/**
	 * <p>TreeGridFieldを生成します。</p>
	 *
	 * @return TreeGridField
	 */
	public TreeGridField[] getTreeGridField() {

		List<TreeGridField> fields = new ArrayList<TreeGridField>();

		TreeGridField displayNameField = new TreeGridField(FIELD_NAME.DISPLAY_NAME.name());
		displayNameField.setTitle("Path");
		displayNameField.setShowHover(true);
		displayNameField.setHoverCustomizer(new HoverCustomizer() {
			@Override
			public String hoverHTML(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				return SmartGWTUtil.getHoverString(record.getAttribute(FIELD_NAME.DISPLAY_NAME.name()));
			}
		});
		fields.add(displayNameField);

		TreeGridField itemIdField = new TreeGridField(FIELD_NAME.ITEM_ID.name());
		itemIdField.setTitle("ID");
		itemIdField.setWidth(250);
		itemIdField.setShowHover(true);
		itemIdField.setHoverCustomizer(new HoverCustomizer() {
			@Override
			public String hoverHTML(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				return SmartGWTUtil.getHoverString(record.getAttribute(FIELD_NAME.ITEM_ID.name()));
			}
		});
		fields.add(itemIdField);

		TreeGridField importActionField = new TreeGridField(FIELD_NAME.ACTION_NAME.name());
		importActionField.setTitle("Action");
		importActionField.setWidth(150);
		fields.add(importActionField);

		TreeGridField messageField = new TreeGridField(FIELD_NAME.MESSAGE.name());
		messageField.setTitle("Message");
		//下に詳細を表示するようにしたのでHoverしない
//		messageField.setShowHover(true);
//		messageField.setWrap(true);
//		messageField.setHoverCustomizer(new HoverCustomizer() {
//			@Override
//			public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
//				return SmartGWTUtil.getHoverString(record.getAttribute(FIELD_NAME.MESSAGE_DETAIL.name()));
//			}
//		});
		fields.add(messageField);

		return fields.toArray(new TreeGridField[]{});
	}

	public void applyStatusResult(List<ImportMetaDataStatus> resultStatus,
			List<ImportMetaDataStatus> errorList,
			List<ImportMetaDataStatus> warnList,
			List<ImportMetaDataStatus> infoList) {

		for (ImportMetaDataStatus status : resultStatus) {
			String targetPath = status.getPath();

			//Pathに一致するTreeNodeの取得
			//下の処理だと件数が多い場合にかなりの時間がかかるためDataSource側でMapを保持して取得する
			//TreeNode target = null;
			//for (TreeNode node : allNodes) {
			//	if (targetPath.equals(node.getAttribute(FIELD_NAME.PATH.name()))) {
			//		target = node;
			//		break;
			//	}
			//}
			TreeNode targetNode = getMetaDataEntryTreeNode(targetPath);

			if (targetNode == null) {
				throw new IllegalStateException("not found node: path=" + targetPath);
			}

			targetNode.setAttribute(FIELD_NAME.ACTION_NAME.name(), status.getImportActionName());

			if (status.isError()) {
				targetNode.setAttribute(FIELD_NAME.IS_ERROR.name(), Boolean.TRUE);
				errorList.add(status);
			}

			if (status.isWarn()) {
				targetNode.setAttribute(FIELD_NAME.IS_WARN.name(), Boolean.TRUE);
				warnList.add(status);
			}

			if (status.isInfo()) {
				targetNode.setAttribute(FIELD_NAME.IS_INFO.name(), Boolean.TRUE);
				infoList.add(status);
			}

			if (!SmartGWTUtil.isEmpty(status.getMessage())) {
				if (!SmartGWTUtil.isEmpty(status.getMessageDetail())) {
					targetNode.setAttribute(FIELD_NAME.MESSAGE_DETAIL.name(), status.getMessageDetail());
				} else {
					targetNode.setAttribute(FIELD_NAME.MESSAGE_DETAIL.name(), status.getMessage());
				}
				targetNode.setAttribute(FIELD_NAME.MESSAGE.name(), status.getMessage() + "  Click...");
			}
		}
	}

	private TreeNode getMetaDataEntryTreeNode(String path) {
		return pathToEntryNodes.get(path);
	}

	@Override
	protected void executeFetch(final String requestId, final DSRequest request,
			final DSResponse response) {

		//選択FileOidの取得
		String fileOid = getFileOid(request.getCriteria());

		MetaDataExplorerServiceAsync service = MetaDataExplorerServiceFactory.get();
		service.getMetaImportFileTree(TenantInfoHolder.getId(), fileOid, new AsyncCallback<ImportFileInfo>() {

			@Override
			public void onFailure(Throwable caught) {
				GWT.log("error!!!", caught);
				response.setStatus(RPCResponse.STATUS_FAILURE);
				processResponse(requestId, response);
			}

			@Override
			public void onSuccess(ImportFileInfo result) {
				MetaDataImportTreeDS.this.result = result;

				TreeNode root = createMetaDataTreeNode(result.getRootNode());
				response.setData(new Record[]{root});
				processResponse(requestId, response);
			}

		});
	}

	@SuppressWarnings("rawtypes")
	private String getFileOid(Criteria criteria) {
		if (criteria != null && criteria.getValues() != null) {
			Map criteriaMap = criteria.getValues();
			Set criteriaSet = criteriaMap.entrySet();
			for (Iterator criteriaIte = criteriaSet.iterator(); criteriaIte.hasNext();) {
				Map.Entry entry = (Map.Entry)criteriaIte.next();
				if (CRITERIA_FILE_OID.equals(entry.getKey())) {
					fileOid = (String)entry.getValue();
					break;
				}
			}
		}
		debug("fileOid->" + fileOid);

		return fileOid;
	}

	private TreeNode createMetaDataTreeNode(MetaTreeNode metaRoot) {
		TreeNode root = new TreeNode("root");
		root.setAttribute(FIELD_NAME.PATH.name(), metaRoot.getPath());
		root.setAttribute(FIELD_NAME.ITEM_ID.name(), "");
		root.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), metaRoot.getName());	//フォルダの場合はNameをセット
		root.setAttribute(FIELD_NAME.ACTION_NAME.name(), "");
		root.setAttribute(FIELD_NAME.IS_ERROR.name(), metaRoot.isError());
		root.setAttribute(FIELD_NAME.IS_WARN.name(), Boolean.FALSE);
		root.setAttribute(FIELD_NAME.IS_INFO.name(), Boolean.FALSE);
		root.setAttribute(FIELD_NAME.MESSAGE.name(), metaRoot.getErrorMessage());
		root.setChildren(createContextPathTreeNodeList(metaRoot.getChildren()).toArray(new TreeNode[]{}));
		root.setIsFolder(true);
		return root;
	}

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

	private TreeNode createContextPathTreeNode(MetaTreeNode metaNode) {
		TreeNode node = new TreeNode(metaNode.getPath());
		node.setAttribute(FIELD_NAME.PATH.name(), metaNode.getPath());
		node.setAttribute(FIELD_NAME.ITEM_ID.name(), "");
		node.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), metaNode.getName());	//フォルダの場合はNameをセット
		node.setAttribute(FIELD_NAME.ACTION_NAME.name(), "");
		node.setAttribute(FIELD_NAME.IS_ERROR.name(), metaNode.isError());
		node.setAttribute(FIELD_NAME.IS_WARN.name(), Boolean.FALSE);
		node.setAttribute(FIELD_NAME.IS_INFO.name(), Boolean.FALSE);
		node.setAttribute(FIELD_NAME.MESSAGE.name(), metaNode.getErrorMessage());
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

	private TreeNode createMetaDataEntryTreeNode(MetaTreeNode item) {

		TreeNode node = new TreeNode(item.getName());
		node.setAttribute(FIELD_NAME.NAME.name(), item.getName());
		node.setAttribute(FIELD_NAME.PATH.name(), item.getPath());
		node.setAttribute(FIELD_NAME.CLASS_NAME.name(), item.getDefinitionClassName());
		node.setAttribute(FIELD_NAME.ITEM_ID.name(), item.getId());
		String displayName = getSimpleName(item.getPath());
		node.setAttribute(FIELD_NAME.DISPLAY_NAME.name(), displayName);
		node.setAttribute(FIELD_NAME.ACTION_NAME.name(), "");
		node.setAttribute(FIELD_NAME.IS_ERROR.name(), item.isError());
		node.setAttribute(FIELD_NAME.IS_WARN.name(), Boolean.FALSE);
		node.setAttribute(FIELD_NAME.IS_INFO.name(), Boolean.FALSE);
		node.setAttribute(FIELD_NAME.MESSAGE.name(), item.getErrorMessage());
		node.setIsFolder(false);

		//ステータス反映時にパスからNodeを取得できるようにMapにセット
		pathToEntryNodes.put(item.getPath(), node);

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

	private void debug(String message) {
		GWT.log("MetaDataImportTreeDS DEBUG " + message);
	}
}
