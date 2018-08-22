/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.tools.data.permissionexplorer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionInfo;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionSearchResult;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.RolePermissionInfo;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.core.client.JavaScriptObject;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public abstract class PermissionTreeGridDS extends PermissionGridDS {

	protected PermissionTreeGridDS(LinkedHashMap<String, Entity> roleMap) {
		super(roleMap);
	}

	@Override
	public String getColRoleCode(int col) {
		int roleIndex = getColRoleCodeIndex(col);
		if (roleIndex < 0) {
			return null;
		}
		return getRoleCodeList().get(roleIndex);
	}

	@Override
	public int getColRoleCodeIndex(int col) {
		//名前列を考慮
		if (col < 1 || (getRoleCodeList().size() <= (col - 1))) {
			return -1;
		}
		return col - 1;
	}

	/**
	 * <p>TreeGridFieldを生成します。</p>
	 *
	 * @return TreeGridField
	 */
	public TreeGridField[] getTreeGridField() {

		List<TreeGridField> fields = new ArrayList<TreeGridField>();

		//TreeGridではDisplayNameを利用(各階層ごとの名前を表示)
		TreeGridField displayNameField = new TreeGridField("displayName", "Name");
		//displayNameField.setHidden(true);
		displayNameField.setWidth(200);
		fields.add(displayNameField);
		TreeGridField definitionNameField = new TreeGridField("definitionName", "Name");
		definitionNameField.setHidden(true);
		//definitionNameField.setWidth(200);
		fields.add(definitionNameField);

		int i = 0;
		List<TreeGridField> summaryFieldList = new ArrayList<TreeGridField>();
		List<TreeGridField> codeFieldList = new ArrayList<TreeGridField>();
		List<TreeGridField> dataFieldList = new ArrayList<TreeGridField>();
		for (String roleCode : getRoleCodeList()) {
			//タイトルはロールコード
			TreeGridField roleSummaryField = new TreeGridField("roleSummary_" + i, roleCode);
			roleSummaryField.setWidth(110);
			summaryFieldList.add(roleSummaryField);

			TreeGridField roleCodeField = new TreeGridField("roleCode_" + i, null);
			roleCodeField.setHidden(true);
			codeFieldList.add(roleCodeField);

			TreeGridField roleDataField = new TreeGridField("roleData_" + i, null);
			roleDataField.setHidden(true);
			dataFieldList.add(roleDataField);
			i++;
		}
		fields.addAll(summaryFieldList);
		fields.addAll(codeFieldList);
		fields.addAll(dataFieldList);

		return fields.toArray(new TreeGridField[]{});
	}


	/**
	 * <p>画面に表示されているGrid情報から全PermissionInfoを生成します。</p>
	 *
	 * @param tree Tree
	 * @return 全PermissionInfo情報
	 */
	public PermissionSearchResult getEditPermissionInfo(Tree tree) {

		List<PermissionInfo> permissionList = new ArrayList<PermissionInfo>();
		List<PermissionInfo> wildCardPermissionList = new ArrayList<PermissionInfo>();

		TreeNode[] allNodes = tree.getAllNodes();
		if (!getRoleCodeList().isEmpty() && allNodes != null) {
			for (TreeNode lgrecord : allNodes) {
				PermissionTreeNode record = (PermissionTreeNode)lgrecord;

				if (tree.isFolder(record)) {
					//フォルダは設定されているものが存在する場合のみワイルドカードとして生成
					List<RolePermissionInfo> rolePermissionList = new ArrayList<RolePermissionInfo>();
					for (int i = 0; i < getRoleCodeList().size(); i++) {
						if (record.getPermission(i) != null) {
							for (Entity permission : record.getPermission(i)) {
								RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
								rolePermissionInfo.setPermission(permission);
								rolePermissionInfo.setStatus(record.getPermissionStatus(i));	//StatusはRoleレベルで同じ
								rolePermissionList.add(rolePermissionInfo);
							}
						}
					}

					if (!rolePermissionList.isEmpty()) {
						PermissionInfo permissionInfo = new PermissionInfo();
						permissionInfo.setDefinitionName(record.getDefinitionName());
						permissionInfo.setDisplayName(record.getDisplayName());
						permissionInfo.setRolePermissionList(rolePermissionList);

						wildCardPermissionList.add(permissionInfo);
					}
				} else {
					//アイテムはすべて生成
					PermissionInfo permissionInfo = new PermissionInfo();
					permissionInfo.setDefinitionName(record.getDefinitionName());
					permissionInfo.setDisplayName(record.getDisplayName());

					List<RolePermissionInfo> rolePermissionList = new ArrayList<RolePermissionInfo>();
					for (int i = 0; i < getRoleCodeList().size(); i++) {
						if (record.getPermission(i) != null) {
							for (Entity permission : record.getPermission(i)) {
								RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
								rolePermissionInfo.setPermission(permission);
								rolePermissionInfo.setStatus(record.getPermissionStatus(i));	//StatusはRoleレベルで同じ
								rolePermissionList.add(rolePermissionInfo);
							}
						}
					}

					if (!rolePermissionList.isEmpty()) {
						permissionInfo.setRolePermissionList(rolePermissionList);
					}

					permissionList.add(permissionInfo);
				}
			}
		}

		PermissionSearchResult result = new PermissionSearchResult();
		result.setPermissionList(permissionList);
		result.setWildCardPermissionList(wildCardPermissionList);
		return result;
	}

	/**
	 * <p>画面に表示されているGrid情報から更新対象PermissionInfoを生成します。</p>
	 *
	 * @param records Gridレコード
	 * @return 更新対象PermissionInfo情報
	 */
	public List<PermissionInfo> getUpdatePermissionInfoList(Tree tree) {

		List<PermissionInfo> permissionList = new ArrayList<PermissionInfo>();

		TreeNode[] allNodes = tree.getAllNodes();
		if (!getRoleCodeList().isEmpty() && allNodes != null) {
			for (TreeNode lgrecord : allNodes) {
				PermissionTreeNode record = (PermissionTreeNode)lgrecord;

				List<RolePermissionInfo> rolePermissionList = new ArrayList<RolePermissionInfo>();
				for (int i = 0; i < getRoleCodeList().size(); i++) {
					//Statusが設定されている場合のみ対象(追加、編集、削除されているデータ)
					if (record.getPermissionStatus(i) != null && !record.getPermissionStatus(i).isEmpty()) {
						if (record.getPermission(i) != null) {
							for (Entity permission : record.getPermission(i)) {
								RolePermissionInfo rolePermissionInfo = new RolePermissionInfo();
								rolePermissionInfo.setPermission(permission);
								rolePermissionInfo.setStatus(record.getPermissionStatus(i));	//StatusはRoleレベルで同じ
								rolePermissionList.add(rolePermissionInfo);
							}
						}
					}
				}

				if (!rolePermissionList.isEmpty()) {
					//編集対象のデータがある場合のみPermissionInfoを対象にする
					PermissionInfo permissionInfo = new PermissionInfo();
					permissionInfo.setDefinitionName(record.getDefinitionName());
					permissionInfo.setDisplayName(record.getDisplayName());
					permissionInfo.setRolePermissionList(rolePermissionList);
					permissionList.add(permissionInfo);
				}
			}
		}
		return permissionList;
	}

	/**
	 * <p>検索結果をResponseにセットします。</p>
	 *
	 * @param requestId リクエストID
	 * @param request リクエスト
	 * @param response レスポンス
	 * @param permissionInfoList 検索結果
	 */
	protected void setResponsePermissinData(String requestId, DSRequest request, DSResponse response, PermissionSearchResult result) {

		PermissionTreeNodeInfo rootNodeData = createTreeNodeData(result);

		PermissionTreeNode rootNode = createTreeNode(rootNodeData);
		if (rootNode != null) {
			response.setData(new Record[]{rootNode});
		} else {
			response.setData(new Record[]{});
		}
		response.setTotalRows(1);
		response.setStartRow(0);
		processResponse(requestId, response);
	}

	/**
	 * <p>Permissionを編集値に変更します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @param permissionArray 変更値
	 */
	public void updatePermission(PermissionTreeNode record, int roleIndex, Entity[] permissionArray) {

		Entity permission = permissionArray[0];

		if (SmartGWTUtil.isEmpty(permission.getOid())) {
			//新規(新規登録で複数状態はないので、1件目のOIDが空の場合のみ新規になる)
			record.setPermissionStatus(roleIndex, RolePermissionInfo.INSERT);
		} else {
			//更新
			record.setPermissionStatus(roleIndex, RolePermissionInfo.UPDATE);
		}

		record.setPermission(roleIndex, permissionArray);
		record.setPermissionSummary(roleIndex, getPermissionEditingSummaryText(permission));
	}

	/**
	 * <p>Permissionを全て削除対象にします。</p>
	 *
	 * @param record 対象レコード
	 */
	public void deleteAllPermission(PermissionTreeNode record) {
		if (getRoleCodeList() != null) {
			for (int i = 0; i < getRoleCodeList().size(); i++) {
				deletePermission(record, i);
			}
		}
	}

	/**
	 * <p>Permissionを削除対象にします。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 */
	public void deletePermission(PermissionTreeNode record, int roleIndex) {
		if (record.getPermission(roleIndex) != null) {
			String status = record.getPermissionStatus(roleIndex);
			if (status != null && status.equals(RolePermissionInfo.INSERT)) {
				//追加されているものは直接消す
				record.setPermission(roleIndex, null);
				record.setPermissionStatus(roleIndex, "");
				record.setPermissionSummary(roleIndex, "");
			} else {
				record.setPermissionStatus(roleIndex, RolePermissionInfo.DELETE);
				record.setPermissionSummary(roleIndex, getPermissionDeleteSummaryText());
			}
		}
	}

	/**
	 * <p>Permissionに対して全て削除オペレーションが可能かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @return true:可能
	 */
	public boolean canDeleteAllPermission(PermissionTreeNode record) {
		if (getRoleCodeList() == null) {
			return false;
		}
		for (int i = 0; i < getRoleCodeList().size(); i++) {
			if (canDeletePermission(record, i)) {
				//1つでも削除可能であれば可能
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>Permissionに対して削除オペレーションが可能かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @return true:可能
	 */
	public boolean canDeletePermission(PermissionTreeNode record, int roleIndex) {
		Entity[] permission = record.getPermission(roleIndex);
		if (permission != null) {
			String status = record.getPermissionStatus(roleIndex);
			//編集されてないか削除以外は削除可能
			if (status == null || !status.equals(RolePermissionInfo.DELETE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>Permissionの状態が削除かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @return true:削除対象
	 */
	public boolean isDeletingPermission(PermissionTreeNode record, int roleIndex) {
		return RolePermissionInfo.DELETE.equals(record.getPermissionStatus(roleIndex));
	}

	/**
	 * <p>Permissionの状態が変更あり(新規も含む)かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @return true:変更あり
	 */
	public boolean isEditingPermission(PermissionTreeNode record, int roleIndex) {
		return record.getPermissionStatus(roleIndex) != null;
	}

	/**
	 * <p>Permissionの状態が設定済み(新規も含む)かを返します。</p>
	 *
	 * @param record 対象レコード
	 * @param roleIndex ロールIndex
	 * @return true:設定済み
	 */
	public boolean isConfiguredPermission(PermissionTreeNode record, int roleIndex) {
		return record.getPermission(roleIndex) != null;
	}

	/**
	 * <p>検索結果をもとにTreeNodeを作成する為のPermissionTreeNodeDataを作成</p>
	 *
	 * @param result 検索結果
	 * @return RootのPermissionTreeNodeData
	 */
	private PermissionTreeNodeInfo createTreeNodeData(PermissionSearchResult result) {

		PermissionTreeNodeInfo rootData = createFolderTreeNodeData("/", "/", result.getWildCardPermissionList());

		Map<String, PermissionTreeNodeInfo> treeNodeMap = new HashMap<String, PermissionTreeNodeInfo>();
		for (PermissionInfo permission : result.getPermissionList()) {
			String[] nodePaths = permission.getDefinitionName().split("/");

			//folderの作成
			String prePath = rootData.getPath();
			PermissionTreeNodeInfo current = rootData;
			if (nodePaths.length > 1) {
				//Folder部分のNodeを作成する
				for (int i = 0; i < nodePaths.length - 1; i++) {
					String folderPath = prePath + nodePaths[i] + "/";

					if (treeNodeMap.containsKey(folderPath)) {
						//すでに作成済み
						current = treeNodeMap.get(folderPath);
					} else {
						PermissionTreeNodeInfo folderTreeNode = createFolderTreeNodeData(folderPath, nodePaths[i], result.getWildCardPermissionList());

						//１つ前のNodeに追加
						current.addChild(folderTreeNode);

						current = folderTreeNode;
						treeNodeMap.put(folderTreeNode.getPath(), folderTreeNode);
					}
					prePath = folderPath;
				}
			} else if (nodePaths.length == 1 && !nodePaths[0].equals(permission.getDefinitionName())){
				//gem/(TOP画面)対応
				String folderPath = prePath + nodePaths[0] + "/";

				if (treeNodeMap.containsKey(folderPath)) {
					//すでに作成済み
					current = treeNodeMap.get(folderPath);
				} else {
					PermissionTreeNodeInfo folderTreeNode = createFolderTreeNodeData(folderPath, nodePaths[0], result.getWildCardPermissionList());

					//１つ前のNodeに追加
					current.addChild(folderTreeNode);

					current = folderTreeNode;
					treeNodeMap.put(folderTreeNode.getPath(), folderTreeNode);
				}
			}

			//itemの作成
			PermissionTreeNodeInfo item = createItemTreeNodeData(permission);
			current.addItem(item);
		}

		return rootData;
	}

	private PermissionTreeNodeInfo createFolderTreeNodeData(String folderPath, String folderName, List<PermissionInfo> wildCardPermissionList) {

		String folderDefName = folderPath.substring(1) + "*";	//先頭の/をとって、後に*を追加

		PermissionTreeNodeInfo folder = new PermissionTreeNodeInfo();

		folder.setPath(folderPath);
		folder.setDisplayName(folderName);
		folder.setDefinitionName(folderDefName);

		for (PermissionInfo permission : wildCardPermissionList) {
			if (folderDefName.equals(permission.getDefinitionName())) {
				folder.setRolePermissionList(permission.getRolePermissionList());
			}
		}

		return folder;
	}

	private PermissionTreeNodeInfo createItemTreeNodeData(PermissionInfo permission) {

		PermissionTreeNodeInfo item = new PermissionTreeNodeInfo();

		item.setPath(permission.getDefinitionName());
		item.setDefinitionName(permission.getDefinitionName());
		//item.setDisplayName(permission.getDisplayName());
		item.setDisplayName(getSimpleName(permission.getDefinitionName()));

		item.setRolePermissionList(permission.getRolePermissionList());

		return item;
	}

	private String getSimpleName(String path) {
		if (path.contains("/")) {
			if (!path.endsWith("/")) {
				return path.substring(path.lastIndexOf("/") + 1);
			} else {
				//gem/対応
				return "(empty)";
			}
		} else {
			return path;
		}
	}

	private PermissionTreeNode createTreeNode(PermissionTreeNodeInfo rootInfo) {

		PermissionTreeNode root = new PermissionTreeNode(getRoleCodeList(), rootInfo);

		List<PermissionTreeNode> children = new ArrayList<PermissionTreeNode>();
		children.addAll(createFolderTreeNodeList(rootInfo.getChildren()));
		children.addAll(createItemTreeNodeList(rootInfo.getItems()));

		if (!children.isEmpty()) {
			root.setChildren(children.toArray(new PermissionTreeNode[]{}));
			root.setIsFolder(true);
		}

		return root;
	}

	private List<PermissionTreeNode> createFolderTreeNodeList(List<PermissionTreeNodeInfo> childInfoList) {
		List<PermissionTreeNode> pathNodes = new ArrayList<PermissionTreeNode>();
		if (childInfoList != null) {
			for (PermissionTreeNodeInfo childInfo : childInfoList) {
				PermissionTreeNode node = createFolderTreeNode(childInfo);
				if (node != null) {
					pathNodes.add(node);
				}
			}
		}
		return pathNodes;
	}

	private PermissionTreeNode createFolderTreeNode(PermissionTreeNodeInfo childInfo) {
		PermissionTreeNode node = new PermissionTreeNode(getRoleCodeList(), childInfo);
		node.setIsFolder(true);

		List<PermissionTreeNode> children = new ArrayList<PermissionTreeNode>();
		children.addAll(createFolderTreeNodeList(childInfo.getChildren()));
		children.addAll(createItemTreeNodeList(childInfo.getItems()));
		if (!children.isEmpty()) {
			node.setChildren(children.toArray(new PermissionTreeNode[]{}));
		}
		return node;
	}

	private List<PermissionTreeNode> createItemTreeNodeList(List<PermissionTreeNodeInfo> itemInfoList) {
		List<PermissionTreeNode> itemNodes = new ArrayList<PermissionTreeNode>();
		if (itemInfoList != null) {
			for (PermissionTreeNodeInfo item : itemInfoList) {
				PermissionTreeNode node = createItemTreeNode(item);
				if (node != null) {
					itemNodes.add(node);
				}
			}
		}
		return itemNodes;
	}

	private PermissionTreeNode createItemTreeNode(PermissionTreeNodeInfo itemInfo) {

		PermissionTreeNode node = new PermissionTreeNode(getRoleCodeList(), itemInfo);
		node.setIsFolder(false);
		return node;
	}

	public class PermissionTreeNode extends TreeNode {

		private List<String> roleCodeList;

		public PermissionTreeNode() {
		}

		public PermissionTreeNode(List<String> roleCodeList, PermissionTreeNodeInfo info) {
			this.roleCodeList = roleCodeList;

			setDefinitionName(info.getDefinitionName());

			if (SmartGWTUtil.isEmpty(info.getDisplayName())) {
//				setDisplayName(info.getDefinitionName());
				setDisplayName("");
			} else {
				setDisplayName(info.getDisplayName());
			}

			if (info.getRolePermissionList() != null) {
				for (RolePermissionInfo permissionInfo : info.getRolePermissionList()) {
					Entity permission = permissionInfo.getPermission();
					String roleCode = permission.getValue("role.code");
					addPermission(roleCode, permission);
					setPermissionStatus(roleCode, permissionInfo.getStatus());
					setPermissionSummary(roleCode, getPermissionConfiguredSummaryText(permission));
				}
			}
		}

		public void setDefinitionName(String value) {
			setAttribute("definitionName", value);
		}

		public String getDefinitionName() {
			return getAttribute("definitionName");
		}

		public void setDisplayName(String value) {
			setAttribute("displayName", value);
		}

		public String getDisplayName() {
			return getAttribute("displayName");
		}

		public void setPermission(String roleCode, Entity[] value) {
			setPermission(getRoleIndex(roleCode), value);
		}

		public void setPermission(int roleIndex, Entity[] value) {
			if (roleIndex >= 0) {
				setAttribute("roleData_" + roleIndex, value);
			}
		}

		public Entity[] getPermission(String roleCode) {
			return getPermission(getRoleIndex(roleCode));
		}

		public Entity[] getPermission(int roleIndex) {
			if (roleIndex < 0) {
				return null;
			}
			Object jsObject = getAttributeAsObject("roleData_" + roleIndex);
			if (jsObject != null) {
				Object[] objArray = (Object[]) JSOHelper.convertToJava((JavaScriptObject)jsObject, true);
				Entity[] entityArray = new Entity[objArray.length];
				int cnt = 0;
				for (Object obj : objArray) {
					entityArray[cnt]  = (Entity) obj;
					cnt ++;
				}
				return entityArray;
			} else {
				return null;
			}
		}

		public void addPermission(String roleCode, Entity value) {
			Entity[] current = getPermission(roleCode);
			if (current == null) {
				setPermission(roleCode, new Entity[]{value});
			} else {
				Entity[] newArray = new Entity[current.length + 1];
				for (int i = 0; i < current.length; i++) {
					newArray[i] = current[i];
				}
				newArray[current.length] = value;
				setPermission(roleCode, newArray);
			}
		}

		public void setPermissionSummary(String roleCode, String value) {
			setPermissionSummary(getRoleIndex(roleCode), value);
		}

		public void setPermissionSummary(int roleIndex, String value) {
			if (roleIndex >= 0) {
				setAttribute("roleSummary_" + roleIndex, value);
			}
		}

		public String getPermissionSummary(String roleCode) {
			return getPermissionSummary(getRoleIndex(roleCode));
		}

		public String getPermissionSummary(int roleIndex) {
			if (roleIndex < 0) {
				return null;
			}
			return getAttribute("roleSummary_" + roleIndex);
		}

		private void setPermissionStatus(String roleCode, String value) {
			setPermissionStatus(getRoleIndex(roleCode), value);
		}

		private void setPermissionStatus(int roleIndex, String value) {
			if (roleIndex >= 0) {
				setAttribute("status_" + roleIndex, value);
			}
		}

		private String getPermissionStatus(int roleIndex) {
			if (roleIndex < 0) {
				return null;
			}
			return getAttribute("status_" + roleIndex);
		}

		private int getRoleIndex(String roleCode) {
			if (roleCodeList == null) {
				return -1;
			}
			return roleCodeList.indexOf(roleCode);
		}
	}

	@SuppressWarnings("unused")
	private class PermissionTreeNodeInfo implements Serializable {

		private static final long serialVersionUID = -744365427908751184L;

		private String path;
		private String definitionName;

		private String displayName;

		private List<RolePermissionInfo> rolePermissionList;

		private List<PermissionTreeNodeInfo> children;
		private List<PermissionTreeNodeInfo> items;

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}


		public String getDefinitionName() {
			return definitionName;
		}

		public void setDefinitionName(String definitionName) {
			this.definitionName = definitionName;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}

		public List<RolePermissionInfo> getRolePermissionList() {
			return rolePermissionList;
		}
		public void setRolePermissionList(List<RolePermissionInfo> rolePermissionList) {
			this.rolePermissionList = rolePermissionList;
		}
		public void addRolePermission(RolePermissionInfo rolePermission) {
			if (rolePermissionList == null) {
				rolePermissionList = new ArrayList<RolePermissionInfo>();
			}
			rolePermissionList.add(rolePermission);
		}

		public List<PermissionTreeNodeInfo> getChildren() {
			return children;
		}

		public void setChildren(List<PermissionTreeNodeInfo> children) {
			this.children = children;
		}
		public void addChild(PermissionTreeNodeInfo child) {
			if (children == null) {
				children = new ArrayList<PermissionTreeNodeInfo>();
			}
			children.add(child);
		}

		public List<PermissionTreeNodeInfo> getItems() {
			return items;
		}
		public void setItems(List<PermissionTreeNodeInfo> items) {
			this.items = items;
		}
		public void addItem(PermissionTreeNodeInfo item) {
			if (items == null) {
				items = new ArrayList<PermissionTreeNodeInfo>();
			}
			items.add(item);
		}

	}

}
