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

package org.iplass.mtp.view.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.iplass.adminconsole.annotation.MultiLang;
import org.iplass.mtp.definition.Definition;
import org.iplass.mtp.definition.LocalizedStringDefinition;

/**
 * カレンダー定義
 * @author lis3wg
 */
@XmlRootElement
public class EntityCalendar implements Definition {

	/** 期間タイプ */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/calendar")
	public enum CalendarType {
		@XmlEnumValue("Month")MONTH,
		@XmlEnumValue("Week")WEEK,
		@XmlEnumValue("Day")DAY
	}

	/** SerialVersionUID */
	private static final long serialVersionUID = 3384819427770069598L;

	/** カレンダーの定義名 */
	private String name;

	/** カレンダー定義の表示名 */
	@MultiLang(itemNameGetter = "getName", itemKey = "displayName", itemGetter = "getDisplayName", itemSetter = "setDisplayName", multiLangGetter = "getLocalizedDisplayNameList", multiLangSetter = "setLocalizedDisplayNameList")
	private String displayName;

	/** 概要 */
	private String description;

	/** 期間タイプ */
	private CalendarType type;

	/** Entityの設定 */
	private List<EntityCalendarItem> items;

	/** 多言語設定情報 */
	private List<LocalizedStringDefinition> localizedDisplayNameList;

	/**
	 * カレンダーの定義名を取得します。
	 * @return カレンダーの定義名
	 */
	public String getName() {
		return name;
	}

	/**
	 * カレンダーの定義名を設定します。
	 * @param name カレンダーの定義名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * カレンダー定義の表示名を取得します。
	 * @return カレンダー定義の表示名
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * カレンダー定義の表示名を設定します。
	 * @param name カレンダー定義の表示名
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 概要を取得します。
	 * @return 概要
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 概要を設定します。
	 * @param description 概要
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 期間タイプを取得します。
	 * @return 期間タイプ
	 */
	public CalendarType getType() {
		return type;
	}

	/**
	 * 期間タイプを設定します。
	 * @param name 期間タイプ
	 */
	public void setType(CalendarType type) {
		this.type = type;
	}

	/**
	 * Entityの設定を取得します。
	 * @return Entityの設定
	 */
	public List<EntityCalendarItem> getItems() {
		if (items == null)
			items = new ArrayList<EntityCalendarItem>();
		return items;
	}

	/**
	 * Entityの設定を設定します。
	 * @param name Entityの設定
	 */
	public void setItems(List<EntityCalendarItem> items) {
		this.items = items;
	}

	/**
	 * Entityの設定を追加します。
	 * @param item Entityの設定
	 */
	public void addItem(EntityCalendarItem item) {
		getItems().add(item);
	}

	/**
	 * 指定の定義のEntityの設定を取得します。
	 * @param definitionName Entity定義名
	 * @return Entityの設定
	 */
	public EntityCalendarItem getItem(String definitionName) {
		for (EntityCalendarItem item : getItems()) {
			if (definitionName.equals(item.getDefinitionName())) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 指定の定義のプロパティ名を取得します。
	 * @param definitionName Entity定義名
	 * @return プロパティ名
	 */
	public String getPropertyName(String definitionName) {
		EntityCalendarItem item = getItem(definitionName);
		if (item!= null) {
			return item.getPropertyName();
		}
		return null;
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
