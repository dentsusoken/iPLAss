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

package org.iplass.mtp.impl.view.treeview;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.treeview.EntityTreeViewItem;
import org.iplass.mtp.view.treeview.ReferenceTreeViewItem;
import org.iplass.mtp.view.treeview.TreeViewGridColModelMapping;
import org.iplass.mtp.view.treeview.TreeViewItem;
import org.iplass.mtp.view.treeview.TreeViewItem.TreeSortType;

/**
 * ツリービューを構成するアイテムのメタデータ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ MetaEntityTreeViewItem.class })
public abstract class MetaTreeViewItem implements MetaData {

	public static MetaTreeViewItem getInstance(TreeViewItem item) {
		if (item instanceof EntityTreeViewItem) {
			return MetaEntityTreeViewItem.getInstance(item);
		}
		return null;
	}

	/** SerialVersionUID */
	private static final long serialVersionUID = -2173439410019057509L;

	/** Entity定義のID */
	protected String definitionId;

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

	/** 表示するプロパティID */
	protected String displayPropertyId;

	/** Entity定義ノードを表示するか */
	protected boolean displayDefinitionNode;

	/** 参照のツリービューアイテム */
	protected List<MetaReferenceTreeViewItem> referenceTreeViewItems;

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
	private List<MetaTreeViewGridColModelMapping> mapping;

	/**
	 * Entity定義のIDを取得します。
	 * @return Entity定義のID
	 */
	public String getDefinitionId() {
		return definitionId;
	}

	/**
	 * Entity定義のIDを設定します。
	 * @param definitionId Entity定義のID
	 */
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	/**
	 * 詳細表示用アクションを取得します。
	 * @return 詳細表示用アクション
	 */
	public String getAction() {
		return action;
	}

	/**
	 * 詳細表示用アクションを設定します。
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
	 * ソートアイテムを取得します。
	 * @return ソートアイテム
	 */
	public String getSortItem() {
		return sortItem;
	}

	/**
	 * ソートアイテムを設定します。
	 * @param sortItem ソートアイテム
	 */
	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	/**
	 * ソート種別を取得します。
	 * @return ソート種別
	 */
	public TreeSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定します。
	 * @param sortType ソート種別
	 */
	public void setSortType(TreeSortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * 表示上限を取得します。
	 * @return 表示上限
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 表示上限を設定します。
	 * @param limit 表示上限
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * 表示するプロパティIDを取得します。
	 * @return 表示するプロパティID
	 */
	public String getDisplayPropertyId() {
		return displayPropertyId;
	}

	/**
	 * 表示するプロパティIDを設定します。
	 * @param displayPropertyName 表示するプロパティID
	 */
	public void setDisplayPropertyId(String displayPropertyName) {
		this.displayPropertyId = displayPropertyName;
	}

	/**
	 * Entity定義ノードを表示するかを取得します。
	 * @return Entity定義ノードを表示するか
	 */
	public boolean isDisplayDefinitionNode() {
		return displayDefinitionNode;
	}

	/**
	 * Entity定義ノードを表示するかを設定します。
	 * @param displayDefinitionNode Entity定義ノードを表示するか
	 */
	public void setDisplayDefinitionNode(boolean displayDefinitionNode) {
		this.displayDefinitionNode = displayDefinitionNode;
	}

	/**
	 * 参照のツリービューアイテムを取得します。
	 * @return 参照のツリービューアイテム
	 */
	public List<MetaReferenceTreeViewItem> getReferenceTreeViewItems() {
		if (referenceTreeViewItems == null) {
			referenceTreeViewItems = new ArrayList<MetaReferenceTreeViewItem>();
		}
		return referenceTreeViewItems;
	}

	/**
	 * 参照のツリービューアイテムを設定します。
	 * @param referenceTreeViewItems 参照のツリービューアイテム
	 */
	public void setReferenceTreeViewItems(List<MetaReferenceTreeViewItem> referenceTreeViewItems) {
		this.referenceTreeViewItems = referenceTreeViewItems;
	}

	/**
	 * 参照のツリービューアイテムを追加します。
	 * @param referenceTreeViewItem 参照のツリービューアイテム
	 */
	public void addReferenceTreeViewItem(MetaReferenceTreeViewItem referenceTreeViewItem) {
		getReferenceTreeViewItems().add(referenceTreeViewItem);
	}

	/**
	 * Entityのノードに表示するアイコンのURLを取得します。
	 * @return Entityのノードに表示するアイコンのURL
	 */
	public String getEntityNodeIcon() {
		return entityNodeIcon;
	}

	/**
	 * Entityのノードに表示するアイコンのURLを設定します。
	 * @param entityNodeIcon Entityのノードに表示するアイコンのURL
	 */
	public void setEntityNodeIcon(String entityNodeIcon) {
		this.entityNodeIcon = entityNodeIcon;
	}

	/**
	 * Indexのノードに表示するアイコンのURLを取得します。
	 * @return Indexのノードに表示するアイコンのURL
	 */
	public String getIndexNodeIcon() {
		return indexNodeIcon;
	}

	/**
	 * Indexのノードに表示するアイコンのURLを設定します。
	 * @param indexNodeIcon Indexのノードに表示するアイコンのURL
	 */
	public void setIndexNodeIcon(String indexNodeIcon) {
		this.indexNodeIcon = indexNodeIcon;
	}

	/**
	 * Entity定義のノードに表示するアイコンのURLを取得します。
	 * @return Entity定義のノードに表示するアイコンのURL
	 */
	public String getEntityDefinitionNodeIcon() {
		return entityDefinitionNodeIcon;
	}

	/**
	 * Entity定義のノードに表示するアイコンのURLを設定します。
	 * @param entityDefinitionNodeIcon Entity定義のノードに表示するアイコンのURL
	 */
	public void setEntityDefinitionNodeIcon(String entityDefinitionNodeIcon) {
		this.entityDefinitionNodeIcon = entityDefinitionNodeIcon;
	}

	/**
	 * Entityのノードに設定するスタイルシートのクラスを取得します。
	 * @return Entityのノードに設定するスタイルシートのクラス
	 */
	public String getEntityNodeCssStyle() {
		return entityNodeCssStyle;
	}

	/**
	 * Entityのノードに設定するスタイルシートのクラスを設定します。
	 * @param entityNodeCssStyle Entityのノードに設定するスタイルシートのクラス
	 */
	public void setEntityNodeCssStyle(String entityNodeCssStyle) {
		this.entityNodeCssStyle = entityNodeCssStyle;
	}

	/**
	 * Indexのノードに設定するスタイルシートのクラスを取得します。
	 * @return Indexのノードに設定するスタイルシートのクラス
	 */
	public String getIndexNodeCssStyle() {
		return indexNodeCssStyle;
	}

	/**
	 * Indexのノードに設定するスタイルシートのクラスを設定します。
	 * @param indexNodeCssStyle Indexのノードに設定するスタイルシートのクラス
	 */
	public void setIndexNodeCssStyle(String indexNodeCssStyle) {
		this.indexNodeCssStyle = indexNodeCssStyle;
	}

	/**
	 * Entity定義のノードに設定するスタイルシートのクラスを取得します。
	 * @return Entity定義のノードに設定するスタイルシートのクラス
	 */
	public String getEntityDefinitionNodeCssStyle() {
		return entityDefinitionNodeCssStyle;
	}

	/**
	 * Entity定義のノードに設定するスタイルシートのクラスを設定します。
	 * @param entityDefinitionNodeCssStyle Entity定義のノードに設定するスタイルシートのクラス
	 */
	public void setEntityDefinitionNodeCssStyle(String entityDefinitionNodeCssStyle) {
		this.entityDefinitionNodeCssStyle = entityDefinitionNodeCssStyle;
	}

	/**
	 * ColModelのマッピングを取得します。
	 * @return ColModelのマッピング
	 */
	public List<MetaTreeViewGridColModelMapping> getMapping() {
		if (mapping == null) mapping = new ArrayList<>();
	    return mapping;
	}

	/**
	 * ColModelのマッピングを設定します。
	 * @param mapping ColModelのマッピング
	 */
	public void setMapping(List<MetaTreeViewGridColModelMapping> mapping) {
	    this.mapping = mapping;
	}

	/**
	 * ColModelのマッピングを追加します。
	 * @param mapping ColModelのマッピング
	 */
	public void addMapping(MetaTreeViewGridColModelMapping mapping) {
	    getMapping().add(mapping);
	}

	public abstract void applyConfig(TreeViewItem item);

	/**
	 * ツリービューのアイテム情報をメタデータに設定します。
	 * @param item ツリービューのアイテム
	 */
	protected void fillFrom(TreeViewItem item) {
		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler handler = ctx.getHandlerByName(item.getDefName());
		PropertyHandler property = handler.getProperty(item.getDisplayPropertyName(), ctx);
		PropertyHandler sortProperty = null;
		if (item.getSortItem() != null) {
			sortProperty = handler.getProperty(item.getSortItem(), ctx);
		}

		this.definitionId = handler.getMetaData().getId();
		this.action = item.getAction();
		this.viewName = item.getViewName();
		this.sortItem = sortProperty != null ? sortProperty.getId() : null;
		this.sortType = item.getSortType();
		this.limit = item.getLimit();
		this.displayPropertyId = property.getId();
		this.displayDefinitionNode = item.isDisplayDefinitionNode();
		this.entityNodeIcon = item.getEntityNodeIcon();
		this.indexNodeIcon = item.getIndexNodeIcon();
		this.entityDefinitionNodeIcon = item.getEntityDefinitionNodeIcon();
		this.entityNodeCssStyle = item.getEntityNodeCssStyle();
		this.indexNodeCssStyle = item.getIndexNodeCssStyle();
		this.entityDefinitionNodeCssStyle = item.getEntityDefinitionNodeCssStyle();
		for (ReferenceTreeViewItem rItem : item.getReferenceTreeViewItems()) {
			MetaReferenceTreeViewItem meta = new MetaReferenceTreeViewItem();
			meta.applyConfig(rItem, handler);
			addReferenceTreeViewItem(meta);
		}
		this.mapping = new ArrayList<>();
		if (item.getMapping() != null) {
			for (TreeViewGridColModelMapping modelMapping : item.getMapping()) {
				MetaTreeViewGridColModelMapping meta = new MetaTreeViewGridColModelMapping();
				meta.applyConfig(modelMapping);
				addMapping(meta);
			}
		}
	}

	public abstract TreeViewItem currentConfig();

	/**
	 * メタデータの情報をツリービューのアイテムに設定します。
	 * @param item ツリービューのアイテム
	 */
	protected void fillTo(TreeViewItem item) {
		EntityContext ctx = EntityContext.getCurrentContext();
		EntityHandler handler =ctx.getHandlerById(definitionId);
		PropertyHandler property = handler.getPropertyById(displayPropertyId, ctx);
		PropertyHandler sortProperty = null;
		if (sortItem != null) {
			sortProperty = handler.getPropertyById(sortItem, ctx);
		}

		item.setDefName(handler.getMetaData().getName());
		item.setAction(action);
		item.setViewName(viewName);
		item.setSortItem(sortProperty != null ? sortProperty.getName() : null);
		item.setSortType(sortType);
		item.setLimit(limit);
		item.setDisplayPropertyName(property.getName());
		item.setDisplayDefinitionNode(displayDefinitionNode);
		item.setEntityNodeIcon(entityNodeIcon);
		item.setIndexNodeIcon(indexNodeIcon);
		item.setEntityDefinitionNodeIcon(entityDefinitionNodeIcon);
		item.setEntityNodeCssStyle(entityNodeCssStyle);
		item.setIndexNodeCssStyle(indexNodeCssStyle);
		item.setEntityDefinitionNodeCssStyle(entityDefinitionNodeCssStyle);
		for (MetaReferenceTreeViewItem rItem : getReferenceTreeViewItems()) {
			item.addReferenceTreeItem(rItem.currentConfig(handler));
		}
		if (!getMapping().isEmpty()) {
			for (MetaTreeViewGridColModelMapping meta : getMapping()) {
				item.addMapping(meta.currentConfig());
			}
		}
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

}
