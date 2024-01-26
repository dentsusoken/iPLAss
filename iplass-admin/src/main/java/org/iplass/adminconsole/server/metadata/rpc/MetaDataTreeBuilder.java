/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.server.metadata.rpc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.adminconsole.shared.metadata.dto.MetaTreeNode;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.impl.definition.DefinitionPath;
import org.iplass.mtp.impl.definition.DefinitionService;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.impl.metadata.MetaDataEntryInfo;

public class MetaDataTreeBuilder {

	private DefinitionService ds;

	private String rootPath;
	private String defTypeClassName;
	private List<MetaDataEntryInfo> items;

	public MetaDataTreeBuilder() {
		ds = DefinitionService.getInstance();
	}

	public MetaDataTreeBuilder all() {
		searchItems(null);
		return this;
	}

	public MetaDataTreeBuilder type(Class<? extends Definition> defType) {
		searchItems(defType);
		return this;
	}

	public MetaDataTreeBuilder items(List<MetaDataEntryInfo> items) {
		this.items = items;
		this.rootPath = "/";
		return this;
	}

	public MetaTreeNode build() {
		return createTreeNode();
	}

	private void searchItems(Class<? extends Definition> defType) {

		if (defType != null) {
			this.rootPath = ds.getPrefixPath(defType);
			this.defTypeClassName = defType.getName();
		} else {
			this.rootPath = "/";
		}
		this.items = MetaDataContext.getContext().definitionList(rootPath);
	}

	private MetaTreeNode createTreeNode() {

		MetaTreeNode root = new MetaTreeNode();
		root.setPath(rootPath);
		root.setName(rootPath);	//RootはPathをNameとする

		if (items != null) {
			Map<String, MetaTreeNode> treeNodeMap = new HashMap<String, MetaTreeNode>();
			for (MetaDataEntryInfo entry : items) {
				//rootPathを除いた部分を/で分割
				String[] nodePaths = splitPath(rootPath, entry.getPath());

				//folderの作成
				String prePath = rootPath;
				MetaTreeNode current = root;
				if (nodePaths.length > 1) {
					//Folder部分のNodeを作成する
					for (int i = 0; i < nodePaths.length - 1; i++) {
						String folderPath = prePath + nodePaths[i] + "/";

						if (treeNodeMap.containsKey(folderPath)) {
							//すでに作成済み
							current = treeNodeMap.get(folderPath);
						} else {
							MetaTreeNode folderTreeNode = createFolderTreeNode(folderPath, nodePaths[i]);

							//１つ前のNodeに追加
							current.addChild(folderTreeNode);

							current = folderTreeNode;
							treeNodeMap.put(folderTreeNode.getPath(), folderTreeNode);
						}
						prePath = folderPath;
					}
				}

				if (entry.getPath().endsWith("/")) {
					//最後がブランクの場合(最後の名前がブランク)、splitすると消えるのでフォルダを作成(例：gemのTopAction：/action/gem/)
					String folderName = nodePaths[nodePaths.length - 1];
					String folderPath = prePath + folderName + "/";

					if (treeNodeMap.containsKey(folderPath)) {
						//すでに作成済み
						current = treeNodeMap.get(folderPath);
					} else {
						MetaTreeNode folderTreeNode = createFolderTreeNode(folderPath, folderName);

						//１つ前のNodeに追加
						current.addChild(folderTreeNode);

						current = folderTreeNode;
						treeNodeMap.put(folderTreeNode.getPath(), folderTreeNode);
					}
				}

				//itemの作成
				MetaTreeNode item = createItemTreeNode(entry, defTypeClassName);

				current.addItem(item);
			}
		}
		return root;
	}

	private MetaTreeNode createFolderTreeNode(String folderPath, String folderName) {

		MetaTreeNode newTreeNode = new MetaTreeNode();
		newTreeNode.setPath(folderPath);
		newTreeNode.setName(folderName);

		return newTreeNode;
	}


	private MetaTreeNode createItemTreeNode(MetaDataEntryInfo entry, String defTypeClassName) {
		MetaTreeNode item = new MetaTreeNode();
		item.setPath(entry.getPath());
		item.setName(ds.getDefinitionName(entry.getPath()));
		item.setDisplayName(entry.getDisplayName());
		item.setDescription(entry.getDescription());
		item.setId(entry.getId());
		if (entry.getState() != null) {
			item.setState(entry.getState().toString());
		}
		item.setVersion(entry.getVersion());
		item.setRepository(entry.getRepository());

		item.setCreateDate(entry.getCreateDate());
		item.setUpdateDate(entry.getUpdateDate());

		item.setSharable(entry.isSharable());
		item.setOverwritable(entry.isOverwritable());
		item.setDataSharable(entry.isDataSharable());
		item.setPermissionSharable(entry.isPermissionSharable());
		item.setShared(RepositoryType.SHARED == entry.getRepositryType());
		item.setSharedOverwrite(RepositoryType.SHARED_OVERWRITE == entry.getRepositryType());

		if (defTypeClassName != null) {
			item.setDefinitionClassName(defTypeClassName);
		} else {
			//パスから取得
			item.setDefinitionClassName(getDefinitionClassName(entry.getPath()));
		}

		return item;
	}

	private String getDefinitionClassName(String path) {
		DefinitionPath defPath = ds.resolvePath(path);
		if (defPath != null && defPath.getType() != null) {
			return defPath.getType().getName();
		}
		return null;
	}

	private String[] splitPath(String rootPath, String path) {
		//RootPath部分を除去
		String target = path.substring(rootPath.length());

		return target.split("/");
	}

}
