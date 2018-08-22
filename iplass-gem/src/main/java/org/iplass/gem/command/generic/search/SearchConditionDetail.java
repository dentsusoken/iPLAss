/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.gem.command.generic.search;

/**
 * 詳細検索条件
 * @author lis3wg
 */
public class SearchConditionDetail {

	/** プロパティ名 */
	private String propertyName;

	/** 述語 */
	private String predicate;

	/** 値 */
	private String value;

	/** 検索用の値 */
	private Object conditionValue;

	/**
	 * プロパティ名を取得します。
	 * @return プロパティ名
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * プロパティ名を設定します。
	 * @param propertyName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * 述語を取得します。
	 * @return 述語
	 */
	public String getPredicate() {
		return predicate;
	}

	/**
	 * 述語を設定します。
	 * @param predicate 述語
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	/**
	 * 値を取得します。
	 * @return 値
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 値を設定します。
	 * @param value 値
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 検索用の値を取得します。
	 * @return 検索用の値
	 */
	public Object getConditionValue() {
		return conditionValue;
	}

	/**
	 * 検索用の値を設定します。
	 * @param conditionValue 検索用の値
	 */
	public void setConditionValue(Object conditionValue) {
		this.conditionValue = conditionValue;
	}

}
