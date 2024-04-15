/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.view.generic.element.section;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.Refrectable;
import org.iplass.mtp.view.generic.NullOrderType;
import org.iplass.mtp.view.generic.element.section.SearchConditionSection.ConditionSortType;

/**
 * ソート条件設定
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SortSetting implements Refrectable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 372120834366334182L;

	/** ソート項目 */
	@MetaFieldInfo(
			displayName="ソート項目",
			displayNameKey="generic_element_section_SortSetting_sortKeyDisplayNameKey",
			inputType=InputType.PROPERTY,
			description="検索時にデフォルトで指定するソート項目を設定します。" +
					"未指定の場合はOIDをデフォルトで使用します。",
			descriptionKey="generic_element_section_SortSetting_sortKeyDescriptionKey",
			required=true
	)
	private String sortKey;

	/** ソート種別 */
	@MetaFieldInfo(
			displayName="ソート種別",
			displayNameKey="generic_element_section_SortSetting_sortTypeDisplayNameKey",
			inputType=InputType.ENUM,
			enumClass=ConditionSortType.class,
			description="検索時にデフォルトで指定するソート種別を設定します。" +
					"未指定の場合には降順（DESC）をデフォルトで使用します。",
			descriptionKey="generic_element_section_SortSetting_sortTypeDescriptionKey",
			required=true
	)
	private ConditionSortType sortType;

	/** null項目のソート順 */
	@MetaFieldInfo(
			displayName="null項目のソート順",
			displayNameKey="generic_element_section_SortSetting_nullOrderTypeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=NullOrderType.class,
			description="null項目のソート順を指定します。<br>" +
					"NONE:未指定、DB依存<br>" +
					"FIRST:null項目を先頭にソート<br>" +
					"LAST:null項目を後尾にソート",
			descriptionKey="generic_element_section_SortSetting_nullOrderTypeDescriptionKey"
	)
	private NullOrderType nullOrderType;

	/**
	 * デフォルトコンストラクタ
	 */
	public SortSetting() {
	}

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
}
