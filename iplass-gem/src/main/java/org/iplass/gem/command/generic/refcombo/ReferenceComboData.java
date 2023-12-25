/*
 * Copyright (C) 2023 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.gem.command.generic.refcombo;

import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.view.generic.editor.ReferencePropertyEditor.RefSortType;

/**
 * 連動コンボの選択値
 */
public class ReferenceComboData {

	/** プロパティPath */
	private String propertyPath;

	/** Entity名 */
	private String entityName;

	/** 上位プロパティ名 */
	private String parentPropertyName;

	/** 検索条件 */
	private String condition;

	/** 表示ラベルとして扱うプロパティ */
	private String displayLabelItem;

	/** ソートプロパティ */
	private String sortItem;

	/** ソート種別 */
	private RefSortType sortType;

	/** 現在値 */
	private String currentValue;

	/** 選択値 */
	private List<Entity> optionValues;

	/**
	 * プロパティPathを初期化します。
	 *
	 * @param propertyPath プロパティPath
	 */
	public ReferenceComboData(String propertyPath) {
		setPropertyPath(propertyPath);
	}

	/**
	 * プロパティPathを返します。
	 *
	 * @return プロパティPath
	 */
	public String getPropertyPath() {
		return propertyPath;
	}

	/**
	 * プロパティPathを設定します。
	 *
	 * @param propertyPath プロパティPath
	 */
	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}

	/**
	 * Entity名を返します。
	 *
	 * @return Entity名
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * Entity名を設定します。
	 *
	 * @param entityName Entity名
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * 上位プロパティ名を返します。
	 *
	 * @return 上位プロパティ名
	 */
	public String getParentPropertyName() {
		return parentPropertyName;
	}

	/**
	 * 上位プロパティ名を設定します。
	 *
	 * @param parentPropertyName 上位プロパティ名
	 */
	public void setParentPropertyName(String parentPropertyName) {
		this.parentPropertyName = parentPropertyName;
	}

	/**
	 * 検索条件を返します。
	 *
	 * @return 検索条件
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * 検索条件を設定します。
	 *
	 * @param condition 検索条件
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}

	/**
	 * 表示ラベルとして扱うプロパティを返します。
	 *
	 * @return 表示ラベルとして扱うプロパティ
	 */
	public String getDisplayLabelItem() {
		return displayLabelItem;
	}

	/**
	 * 表示ラベルとして扱うプロパティを設定します。
	 *
	 * @param displayLabelItem 表示ラベルとして扱うプロパティ
	 */
	public void setDisplayLabelItem(String displayLabelItem) {
		this.displayLabelItem = displayLabelItem;
	}

	/**
	 * ソートプロパティを返します。
	 *
	 * @return ソートプロパティ
	 */
	public String getSortItem() {
		return sortItem;
	}

	/**
	 * ソートプロパティを設定します。
	 *
	 * @param sortItem ソートプロパティ
	 */
	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}

	/**
	 * ソート種別を返します。
	 *
	 * @return ソート種別
	 */
	public RefSortType getSortType() {
		return sortType;
	}

	/**
	 * ソート種別を設定します。
	 *
	 * @param sortType ソート種別
	 */
	public void setSortType(RefSortType sortType) {
		this.sortType = sortType;
	}

	/**
	 * 現在値を返します。
	 *
	 * @return 現在値
	 */
	public String getCurrentValue() {
		return currentValue;
	}

	/**
	 * 現在値を設定します。
	 *
	 * @param currentValue 現在値
	 */
	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	/**
	 * 選択値を返します。
	 *
	 * @return 選択値
	 */
	public List<Entity> getOptionValues() {
		return optionValues;
	}

	/**
	 * 選択値を設定します。
	 *
	 * @param optionValues 選択値
	 */
	public void setOptionValues(List<Entity> optionValues) {
		this.optionValues = optionValues;
	}

}
