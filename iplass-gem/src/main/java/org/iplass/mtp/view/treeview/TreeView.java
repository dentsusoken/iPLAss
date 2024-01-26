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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;
/**
 * ツリービュー定義
 * @author lis3wg
 */
@XmlRootElement
public class TreeView implements Definition {

	private static final long serialVersionUID = 8938014670990109232L;

	/** 名前 */
	private String name;

	/** 表示名 */
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;

	/** 記述 */
	private String description;

	/** ツリービューを構成するアイテム */
	private List<TreeViewItem> items;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedDisplayNameList;

	/** ColModel */
	private List<TreeViewGridColModel> colModel;

	public TreeView() {
	}

	/**
	 * 名前を取得する。
	 * @return 名前
	 */
	public String getName() {
		return name;
	}

	/**
	 * 名前を設定する。
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 表示名を取得する。
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 表示名を設定する。
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 記述を取得する。
	 * @return 記述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 記述を設定する。
	 * @param description 記述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * ツリービューを構成するアイテムを取得する。
	 * @return ツリービューを構成するアイテム
	 */
	public List<TreeViewItem> getItems() {
		if (items == null)
			items = new ArrayList<TreeViewItem>();
		return items;
	}

	/**
	 * ツリービューを構成するアイテムを設定する。
	 * @param items ツリービューを構成するアイテム
	 */
	public void setItems(List<TreeViewItem> items) {
		this.items = items;
	}

	/**
	 * ツリービューを構成するアイテムを追加する。
	 * @param item ツリービューを構成するアイテム
	 */
	public void addItem(TreeViewItem item) {
		getItems().add(item);
	}

	/**
	 * 多言語設定情報を取得します。
	 * @return リスト
	 */
	public List<LocalizedStringDefinition> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayNameList(List<LocalizedStringDefinition> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}

	/**
	 * 多言語設定情報を追加します。
	 * @param 多言語設定情報
	 */
	public void addLocalizedDisplayName(LocalizedStringDefinition localizedDisplayName) {
		if (localizedDisplayNameList == null) {
			localizedDisplayNameList = new ArrayList<LocalizedStringDefinition>();
		}

		localizedDisplayNameList.add(localizedDisplayName);
	}

	/**
	 * ColModelを取得します。
	 * @return ColModel
	 */
	public List<TreeViewGridColModel> getColModel() {
		if (colModel == null) colModel = new ArrayList<TreeViewGridColModel>();
	    return colModel;
	}

	/**
	 * ColModelを設定します。
	 * @param colModel ColModel
	 */
	public void setColModel(List<TreeViewGridColModel> colModel) {
	    this.colModel = colModel;
	}

	/**
	 * ColModelを追加します。
	 * @param colModel ColModel
	 */
	public void addColModel(TreeViewGridColModel colModel) {
	    getColModel().add(colModel);
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
			TreeViewItem item = search(key[0]);
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
	 * @param name アイテム名
	 * @return ツリーアイテムの定義
	 */
	private TreeViewItem search(String name) {
		for (TreeViewItem item : getItems()) {
			if (item instanceof EntityTreeViewItem) {
				if (((EntityTreeViewItem) item).getDefName().equals(name))
					return item;
			} else if (item instanceof ReferenceTreeViewItem) {
				if (((ReferenceTreeViewItem) item).getPropertyName().equals(
						name))
					return item;
			}
		}
		return null;
	}

}
