/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.treeview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * ツリービューのアイテム
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({EntityTreeViewItem.class})
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS)
public abstract class TreeViewItem implements Serializable {

	/** ソート種別 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/treeview")
	public enum TreeSortType {
		ASC, DESC;
	}

	/** SerialVersionUID */
	private static final long serialVersionUID = 4970065426513391661L;

	/** Entity定義の名前 */
	protected String defName;

	/** 詳細表示用アクション */
	protected String action;

	/** ビュー名 */
	protected String viewName;

	/** ソートアイテム */
	protected String sortItem;

	/** ソート種別 */
	protected TreeSortType sortType;

	/** 表示上限 */
	protected int limit;

	/** 表示するプロパティ名 */
	protected String displayPropertyName;

	/** Entity定義ノードを表示するか */
	protected boolean displayDefinitionNode;

	/** 参照のツリービューアイテム */
	protected List<ReferenceTreeViewItem> referenceTreeViewItems;

	/** Entityのノードに表示するアイコンのURL */
	protected String entityNodeIcon;

	/** Indexのノードに表示するアイコンのURL */
	protected String indexNodeIcon;

	/** Entity定義のノードに表示するアイコンのURL */
	protected String entityDefinitionNodeIcon;

	/** Entityのノードに設定するスタイルシートのクラス */
	protected String entityNodeCssStyle;

	/** Indexのノードに設定するスタイルシートのクラス */
	protected String indexNodeCssStyle;

	/** Entity定義のノードに設定するスタイルシートのクラス */
	protected String entityDefinitionNodeCssStyle;

	/** ColModelのマッピング */
	protected List<TreeViewGridColModelMapping> mapping;

	/**
	 * Entity定義の名前を取得する。
	 * @return Entity定義の名前
	 */
	public String getDefName() {
		return defName;
	}

	/**
	 * Entity定義の名前を設定する。
	 * @param defName Entity定義の名前
	 */
	public void setDefName(String defName) {
		this.defName = defName;
	}

	/**
	 * 詳細表示用アクションを取得する。
	 * @return 詳細表示用アクション
	 */
	public String getAction() {
		return action;
	}

	/**
	 * 詳細表示用アクションを設定する。
	 * @param action 詳細表示用アクション
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * ビュー名を取得します。
	 * @return ビュー名
	 */
	public String getViewName() {
	    return viewName;
	}

	/**
	 * ビュー名を設定します。
	 * @param viewName ビュー名
	 */
	public void setViewName(String viewName) {
	    this.viewName = viewName;
	}

	/**
	 * ソートアイテムを取得する。
	 * @return ソートアイテム
	 */
	public String getSortItem() {
		return sortItem;
	}

	/**
	 * ソートアイテムを設定する。
	 * @param sortItem ソートアイテム
	 */
	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	/**
	 * ソート種別を取得する。
	 * @return ソート種別
	 */
	public TreeSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定する。
	 * @param sortType ソート種別
	 */
	public void setSortType(TreeSortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * 表示上限を取得する。
	 * @return 表示上限
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 表示上限を設定する。
	 * @param limit 表示上限
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 表示するプロパティ名を取得する。
	 * @return 表示するプロパティ名
	 */
	public String getDisplayPropertyName() {
		return displayPropertyName;
	}

	/**
	 * 表示するプロパティ名を設定する。
	 * @param displayPropertyName 表示するプロパティ名
	 */
	public void setDisplayPropertyName(String displayPropertyName) {
		this.displayPropertyName = displayPropertyName;
	}

	/**
	 * Entity定義ノードを表示するかを取得する。
	 * @return Entity定義ノードを表示するか
	 */
	public boolean isDisplayDefinitionNode() {
		return displayDefinitionNode;
	}

	/**
	 * Entity定義ノードを表示するかを設定する。
	 * @param displayDefinitionNode Entity定義ノードを表示するか
	 */
	public void setDisplayDefinitionNode(boolean displayDefinitionNode) {
		this.displayDefinitionNode = displayDefinitionNode;
	}

	/**
	 * 参照のツリービューアイテムを設定する。
	 * @param referenceTreeViewItems 参照のツリービューアイテム
	 */
	public void setReferenceTreeViewItems(
			List<ReferenceTreeViewItem> referenceTreeViewItems) {
		this.referenceTreeViewItems = referenceTreeViewItems;
		for (ReferenceTreeViewItem item : this.referenceTreeViewItems) {
			item.setParent(this);
		}
	}

	/**
	 * 参照のツリービューアイテムを取得する。
	 * @return 参照のツリービューアイテム
	 */
	public List<ReferenceTreeViewItem> getReferenceTreeViewItems() {
		if (referenceTreeViewItems == null)
			referenceTreeViewItems = new ArrayList<ReferenceTreeViewItem>();
		return referenceTreeViewItems;
	}

	/**
	 * 参照のツリービューアイテムを追加する。
	 * @param referenceTreeViewItem 参照のツリービューアイテムを追加する。
	 */
	public void addReferenceTreeItem(ReferenceTreeViewItem referenceTreeViewItem) {
		referenceTreeViewItem.setParent(this);
		getReferenceTreeViewItems().add(referenceTreeViewItem);
	}

	/**
	 * Entityのノードに表示するアイコンのURLを取得する。
	 * @return Entityのノードに表示するアイコンのURL
	 */
	public String getEntityNodeIcon() {
		return entityNodeIcon;
	}

	/**
	 * Entityのノードに表示するアイコンのURLを設定する。
	 * @param entityNodeIcon Entityのノードに表示するアイコンのURL
	 */
	public void setEntityNodeIcon(String entityNodeIcon) {
		this.entityNodeIcon = entityNodeIcon;
	}

	/**
	 * Indexのノードに表示するアイコンのURLを取得する。
	 * @return Indexのノードに表示するアイコンのURL
	 */
	public String getIndexNodeIcon() {
		return indexNodeIcon;
	}

	/**
	 * Indexのノードに表示するアイコンのURLを設定する。
	 * @param indexNodeIcon Indexのノードに表示するアイコンのURL
	 */
	public void setIndexNodeIcon(String indexNodeIcon) {
		this.indexNodeIcon = indexNodeIcon;
	}

	/**
	 * Entity定義のノードに表示するアイコンのURLを取得する。
	 * @return Entity定義のノードに表示するアイコンのURL
	 */
	public String getEntityDefinitionNodeIcon() {
		return entityDefinitionNodeIcon;
	}

	/**
	 * Entity定義のノードに表示するアイコンのURLを設定する。
	 * @param entityDefinitionNodeIcon Entity定義のノードに表示するアイコンのURL
	 */
	public void setEntityDefinitionNodeIcon(String entityDefinitionNodeIcon) {
		this.entityDefinitionNodeIcon = entityDefinitionNodeIcon;
	}

	/**
	 * Entityのノードに設定するスタイルシートのクラスを取得する。
	 * @return Entityのノードに設定するスタイルシートのクラス
	 */
	public String getEntityNodeCssStyle() {
		return entityNodeCssStyle;
	}

	/**
	 * Entityのノードに設定するスタイルシートのクラスを設定する。
	 * @param entityNodeCssStyle Entityのノードに設定するスタイルシートのクラス
	 */
	public void setEntityNodeCssStyle(String entityNodeCssStyle) {
		this.entityNodeCssStyle = entityNodeCssStyle;
	}

	/**
	 * Indexのノードに設定するスタイルシートのクラスを取得する。
	 * @return Indexのノードに設定するスタイルシートのクラス
	 */
	public String getIndexNodeCssStyle() {
		return indexNodeCssStyle;
	}

	/**
	 * Indexのノードに設定するスタイルシートのクラスを設定する。
	 * @param indexNodeCssStyle Indexのノードに設定するスタイルシートのクラス
	 */
	public void setIndexNodeCssStyle(String indexNodeCssStyle) {
		this.indexNodeCssStyle = indexNodeCssStyle;
	}

	/**
	 * Entity定義のノードに設定するスタイルシートのクラスを取得する。
	 * @return Entity定義のノードに設定するスタイルシートのクラス
	 */
	public String getEntityDefinitionNodeCssStyle() {
		return entityDefinitionNodeCssStyle;
	}

	/**
	 * Entity定義のノードに設定するスタイルシートのクラスを設定する。
	 * @param entityDefinitionNodeCssStyle Entity定義のノードに設定するスタイルシートのクラス
	 */
	public void setEntityDefinitionNodeCssStyle(String entityDefinitionNodeCssStyle) {
		this.entityDefinitionNodeCssStyle = entityDefinitionNodeCssStyle;
	}

	/**
	 * ColModelのマッピングを取得します。
	 * @return ColModelのマッピング
	 */
	public List<TreeViewGridColModelMapping> getMapping() {
		if (mapping == null) mapping = new ArrayList<TreeViewGridColModelMapping>();
	    return mapping;
	}

	/**
	 * ColModelのマッピングを設定します。
	 * @param mapping ColModelのマッピング
	 */
	public void setMapping(List<TreeViewGridColModelMapping> mapping) {
	    this.mapping = mapping;
	}

	/**
	 * ColModelのマッピングを追加します。
	 * @param mapping ColModelのマッピング
	 */
	public void addMapping(TreeViewGridColModelMapping mapping) {
	    getMapping().add(mapping);
	}

	/**
	 * pathを解析して自身を含む階層構造からアイテムを取得する。
	 * @param path ツリー構造のパス
	 * @return パスの示すアイテムの定義
	 */
	public TreeViewItem getItem(String path) {
		// Entity.参照Prorperty.参照Prorperty・・・
		if (path.length() > 0) {
			String[] key = path.split("/", 2);
			ReferenceTreeViewItem item = search(key[0]);
			if (item != null) {
				if (key.length > 1) {
					return item.getItem(key[1]);
				} else {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * 指定の名前のアイテムを検索する。
	 * @param name プロパティ名
	 * @return 参照アイテムの定義
	 */
	private ReferenceTreeViewItem search(String propertyName) {
		for (ReferenceTreeViewItem item : getReferenceTreeViewItems()) {
			if (item.getPropertyName().equals(propertyName))
				return item;
		}
		return null;
	}
}
