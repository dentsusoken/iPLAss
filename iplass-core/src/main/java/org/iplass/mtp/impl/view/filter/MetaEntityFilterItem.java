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

package org.iplass.mtp.impl.view.filter;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import org.iplass.mtp.impl.i18n.I18nUtil;
import org.iplass.mtp.impl.i18n.MetaLocalizedString;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.view.filter.EntityFilterItem;

/**
 * Entityのフィルタ設定
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MetaEntityFilterItem implements MetaData {

	private static final long serialVersionUID = 7233757620105503807L;

	/** 名前 */
	private String name;

	/** 表示名 */
	private String displayName;

	/** フィルタ条件 */
	private String condition;

	/** ソート順 */
	private String sort;

	/** 多言語設定情報 */
	private List<MetaLocalizedString> localizedDisplayNameList = new ArrayList<MetaLocalizedString>();

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
	public List<MetaLocalizedString> getLocalizedDisplayNameList() {
		return localizedDisplayNameList;
	}

	/**
	 * 多言語設定情報を設定します。
	 * @param リスト
	 */
	public void setLocalizedDisplayNameList(List<MetaLocalizedString> localizedDisplayNameList) {
		this.localizedDisplayNameList = localizedDisplayNameList;
	}


	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	/**
	 * Entityのフィルタ設定の内容を自身に反映します。
	 * @param item Entityのフィルタ設定
	 */
	public void applyConfig(EntityFilterItem item) {
		this.name = item.getName();
		this.displayName = item.getDisplayName();
		this.condition = item.getCondition();
		this.sort = item.getSort();

		localizedDisplayNameList = I18nUtil.toMeta(item.getLocalizedDisplayNameList());
	}

	/**
	 * 自身の内容をEntityのフィルタ設定に反映します。
	 * @return Entityのフィルタ設定
	 */
	public EntityFilterItem currentConfig() {
		EntityFilterItem item = new EntityFilterItem();
		item.setName(name);
		item.setDisplayName(displayName);
		item.setCondition(condition);
		item.setSort(sort);

		item.setLocalizedDisplayNameList(I18nUtil.toDef(localizedDisplayNameList));

		return item;
	}

}
