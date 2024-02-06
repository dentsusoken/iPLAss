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

package org.iplass.mtp.view.filter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;

/**
 * Entityのフィルタ定義
 * @author lis3wg
 */
@XmlRootElement
public class EntityFilter implements Definition {

	private static final long serialVersionUID = -6472771867927582701L;

	/** 定義名(=Entity定義名) */
	private String name;

	/** 表示名 */
	private String displayName;

	/** 説明 */
	private String description;

	/** Entity定義名 */
	private String definitionName;

	/** フィルタ設定 */
	@MultiLang(isMultiLangValue = false, itemKey = "items", itemGetter = "getItems", itemSetter = "setItems")
	private List<EntityFilterItem> items;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Entity定義名を取得する。
	 * @return Entity定義名
	 */
	public String getDefinitionName() {
		return definitionName;
	}

	/**
	 * Entity定義名を設定する。
	 * @param definitionName Entity定義名
	 */
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	/**
	 * フィルタ設定を取得する。
	 * @return フィルタ設定
	 */
	public List<EntityFilterItem> getItems() {
		if (items == null) items = new ArrayList<EntityFilterItem>();
		return items;
	}

	/**
	 * フィルタ設定を設定する。
	 * @param items フィルタ設定
	 */
	public void setItems(List<EntityFilterItem> items) {
		this.items = items;
	}

	/**
	 * フィルタ設定を追加する。
	 * @param item フィルタ設定
	 */
	public void addItem(EntityFilterItem item) {
		getItems().add(item);
	}

	/**
	 * 指定の名前を持つフィルタ設定を取得する。
	 * @param name フィルタ名
	 * @return フィルタ設定
	 */
	public EntityFilterItem getItem(String name) {
		for (EntityFilterItem item : getItems()) {
			if (name.equals(item.getName())) return item;
		}
		return null;
	}
}
