/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.view.generic.element.section;

import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.metadata.MetaData;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.impl.view.generic.HasEntityProperty;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.ConditionSortType;
import org.iplass.mtp.view.generic.element.section.SortSetting;

public class MetaSortSetting implements MetaData, HasEntityProperty {

	private static final long serialVersionUID = 5223224828928956674L;

	/** ソート項目 */
	private String sortKey;

	/** ソート種別 */
	private ConditionSortType sortType;

	/** null項目のソート順 */
	private NullOrderType nullOrderType;

	/**
	 * ソート項目を取得します。
	 * @return ソート項目
	 */
	public String getSortKey() {
		return sortKey;
	}

	/**
	 * ソート項目を設定します。
	 * @param sortKey ソート項目
	 */
	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	/**
	 * ソート種別を取得します。
	 * @return ソート種別
	 */
	public ConditionSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定します。
	 * @param sortType ソート種別
	 */
	public void setSortType(ConditionSortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * null項目のソート順を取得します。
	 * @return null項目のソート順
	 */
	public NullOrderType getNullOrderType() {
		return nullOrderType;
	}

	/**
	 * null項目のソート順を設定します。
	 * @param nullOrderType null項目のソート順
	 */
	public void setNullOrderType(NullOrderType nullOrderType) {
		this.nullOrderType = nullOrderType;
	}

	@Override
	public MetaData copy() {
		return ObjectUtil.deepCopy(this);
	}

	public void applyConfig(SortSetting setting, EntityContext context, EntityHandler entity) {
		sortKey = convertId(setting.getSortKey(), context, entity);
		sortType = setting.getSortType();
		nullOrderType = setting.getNullOrderType();
	}

	public SortSetting currentConfig(EntityContext context, EntityHandler entity) {

		//プロパティ名が取得できない場合はnullを返す
		String name = convertName(sortKey, context, entity);
		if (name == null) {
			return null;
		}

		SortSetting setting = new SortSetting();
		setting.setSortKey(name);
		setting.setSortType(sortType);
		setting.setNullOrderType(nullOrderType);
		return setting;
	}

}
