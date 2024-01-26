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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * Entityのフィルタ設定
 * @author lis3wg
 */
public class EntityFilterItem implements Serializable {

	private static final long serialVersionUID = 2343993522393309904L;

	/** 名前 */
	private String name;

	/** 表示名 */
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;

	/** フィルタ条件 */
	private String condition;

	/** ソート順 */
	private String sort;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedDisplayNameList;

	/**
	 * 名前を取得します。
	 * @return 名前
	 */
	public String getName() {
		return name;
	}

	/**
	 * 名前を設定します。
	 * @param name 名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 表示名を取得します。
	 * @return 表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 表示名を設定します。
	 * @param displayName 表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * フィルタ条件を取得します。
	 * @return フィルタ条件
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * フィルタ条件を設定します。
	 * @param condition フィルタ条件
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * ソート順を取得します。
	 * @return ソート順
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * ソート順を設定します。
	 * @param sort ソート順
	 */
	public void setSort(String sort) {
		this.sort = sort;
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

}
