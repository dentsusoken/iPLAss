/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * カレンダーに表示するEntityの設定
 * @author lis3wg
 */
public class EntityCalendarItem implements Serializable {

	/** カレンダーの検索方法 */
	@XmlType(namespace="http://mtp.iplass.org/xml/definition/view/calendar")
	public enum CalendarSearchType {
		/** 日付で検索 */
		@XmlEnumValue("Date")DATE,
		/** 期間で検索 */
		@XmlEnumValue("Period")PERIOD,
	}

	/** SerialVersionUID */
	private static final long serialVersionUID = 1037696440253022326L;

	/** Entityの定義名 */
	private String definitionName;

	/** Entityの表示色 */
	private String entityColor;

	/** カレンダーの検索方法 */
	private CalendarSearchType calendarSearchType;

	/** プロパティ名 */
	private String propertyName;

	/** Fromのプロパティ名 */
	private String fromPropertyName;

	/** Toのプロパティ名 */
	private String toPropertyName;

	/** 詳細アクション名 */
	private String viewAction;

	/** 追加アクション名 */
	private String addAction;

	/** ビュー名 */
	private String viewName;

	/** 時間を表示するか */
	private Boolean displayTime;

	/** 検索上限 */
	private Integer limit;

	/** フィルタ条件 */
	private String filterCondition;

	/** Entityの表示色（groovy） */
	private String colorConfig;

	/**
	 * Entityの定義名を取得
	 * @return Entityの定義名
	 */
	public String getDefinitionName() {
		return definitionName;
	}

	/**
	 * Entityの定義名を設定
	 * @param definitionName Entityの定義名
	 */
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	/**
	 * Entityの表示色を取得
	 * @return Entitycolor
	 */
	public String getEntityColor() {
		return entityColor;
	}

	/**
	 * Entityの表示色を設定
	 * @param enittyColor EnittyColor
	 */
	public void setEntityColor(String entityColor) {
		this.entityColor = entityColor;
	}

	/**
	 * カレンダーの検索方法を取得します。
	 * @return カレンダーの検索方法
	 */
	public CalendarSearchType getCalendarSearchType() {
		return calendarSearchType;
	}

	/**
	 * カレンダーの検索方法を設定します。
	 * @param calendarSearchType カレンダーの検索方法
	 */
	public void setCalendarSearchType(CalendarSearchType calendarSearchType) {
		this.calendarSearchType = calendarSearchType;
	}

	/**
	 * プロパティ名を取得
	 * @return プロパティ名
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * プロパティ名を設定
	 * @param definitionName プロパティ名
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Fromのプロパティ名を取得します。
	 * @return Fromのプロパティ名
	 */
	public String getFromPropertyName() {
		return fromPropertyName;
	}

	/**
	 * Fromのプロパティ名を設定します。
	 * @param fromPropertyName Fromのプロパティ名
	 */
	public void setFromPropertyName(String fromPropertyName) {
		this.fromPropertyName = fromPropertyName;
	}

	/**
	 * Toのプロパティ名を取得します。
	 * @return Toのプロパティ名
	 */
	public String getToPropertyName() {
		return toPropertyName;
	}

	/**
	 * Toのプロパティ名を設定します。
	 * @param toPropertyName Toのプロパティ名
	 */
	public void setToPropertyName(String toPropertyName) {
		this.toPropertyName = toPropertyName;
	}

	/**
	 * 詳細アクション名を取得
	 * @return 詳細アクション名
	 */
	public String getViewAction() {
		return viewAction;
	}

	/**
	 * 詳細アクション名を設定
	 * @param viewAction 詳細アクション名
	 */
	public void setViewAction(String viewAction) {
		this.viewAction = viewAction;
	}

	/**
	 * 追加アクション名を取得
	 * @return 追加アクション名
	 */
	public String getAddAction() {
		return addAction;
	}

	/**
	 * 追加アクション名を設定
	 * @param addAction 追加アクション名
	 */
	public void setAddAction(String addAction) {
		this.addAction = addAction;
	}

	/**
	 * ビュー名を取得
	 * @return ビュー名
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * ビュー名を設定
	 * @param viewName ビュー名
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * 時間を表示するかを取得
	 * @return 時間を表示するか
	 */
	public Boolean getDisplayTime() {
		return displayTime;
	}

	/**
	 * 時間を表示するかを設定
	 * @param displayTime 時間を表示するか
	 */
	public void setDisplayTime(Boolean displayTime) {
		this.displayTime = displayTime;
	}

	/**
	 * 検索上限を取得します。
	 * @return 検索上限
	 */
	public Integer getLimit() {
	    return limit;
	}

	/**
	 * 検索上限を設定します。
	 * @param limit 検索上限
	 */
	public void setLimit(Integer limit) {
	    this.limit = limit;
	}

	/**
	 * フィルタ条件を取得します。
	 * @return フィルタ条件
	 */
	public String getFilterCondition() {
		return filterCondition;
	}

	/**
	 * フィルタ条件を設定します。
	 * @param filterCondition フィルタ条件
	 */
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

	/**
	 * Entityの表示色（groovy）を取得します。
	 * @return Entityの表示色（groovy）
	 */
	public String getColorConfig() {
		return colorConfig;
	}

	/**
	 * Entityの表示色（groovy）を設定します。
	 * @param colorConfig Entityの表示色（groovy）
	 */
	public void setColorConfig(String colorConfig) {
		this.colorConfig = colorConfig;
	}

}
