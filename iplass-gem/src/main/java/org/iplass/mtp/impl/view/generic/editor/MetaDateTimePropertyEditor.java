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

package org.iplass.mtp.impl.view.generic.editor;

import java.util.List;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.view.generic.editor.DatePropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimeFormatProperty;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor;
import org.iplass.mtp.view.generic.editor.DateTimePropertyEditor.DateTimeDisplayType;
import org.iplass.mtp.view.generic.editor.PropertyEditor;
import org.iplass.mtp.view.generic.editor.TimePropertyEditor;
import org.iplass.mtp.view.generic.editor.TimestampPropertyEditor;

/**
 * 日付・時間型プロパティエディタのメタデータのスーパークラス
 * @author lis3wg
 */
@XmlSeeAlso({MetaDatePropertyEditor.class, MetaTimePropertyEditor.class, MetaTimestampPropertyEditor.class})
public abstract class MetaDateTimePropertyEditor extends MetaPrimitivePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8195076567521958652L;

	public static MetaDateTimePropertyEditor createInstance(PropertyEditor editor) {
		if (editor instanceof DatePropertyEditor) {
			return MetaDatePropertyEditor.createInstance(editor);
		} else if (editor instanceof TimePropertyEditor) {
			return MetaTimePropertyEditor.createInstance(editor);
		} else if (editor instanceof TimestampPropertyEditor) {
			return MetaTimestampPropertyEditor.createInstance(editor);
		}
		return null;
	}

	/** 表示タイプ */
	private DateTimeDisplayType displayType;

	/** 日付/時刻のフォーマット設定 */
	private List<DateTimeFormatProperty> datetimeFormatList;

	/** 検索条件の単一日指定 */
	private boolean singleDayCondition;

	/** 検索条件From非表示設定 */
	private boolean hideSearchConditionFrom;

	/** 検索条件To非表示設定 */
	private boolean hideSearchConditionTo;

	/** Label形式の場合の登録制御 */
	private boolean insertWithLabelValue = true;

	/** Label形式の場合の更新制御 */
	private boolean updateWithLabelValue = false;

	/**
	 * 表示タイプを取得します。
	 * @return 表示タイプ
	 */
	public DateTimeDisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * 表示タイプを設定します。
	 * @param displayType 表示タイプ
	 */
	public void setDisplayType(DateTimeDisplayType displayType) {
		this.displayType = displayType;
	}

	/**
	 * 日付/時刻のフォーマット設定を取得します。
	 * @return datetimeFormatList フォーマット設定
	 */
	public List<DateTimeFormatProperty> getDatetimeFormatList() {
		return datetimeFormatList;
	}

	/**
	 * 日付/時刻のフォーマット設定を設定します。
	 * @param datetimeFormatList フォーマット設定
	 */
	public void setDatetimeFormatList(List<DateTimeFormatProperty> datetimeFormatList) {
		this.datetimeFormatList = datetimeFormatList;
	}

	/**
	 * 検索条件の単一日指定を取得します。
	 * @return 検索条件の単一日指定
	 */
	public boolean isSingleDayCondition() {
		return singleDayCondition;
	}

	/**
	 * 検索条件の単一日指定を設定します
	 * @param singleDayCondition 検索条件の単一日指定
	 */
	public void setSingleDayCondition(boolean singleDayCondition) {
		this.singleDayCondition = singleDayCondition;
	}

	/**
	 * 検索条件From非表示設定を取得します。
	 * @return 検索条件From非表示設定
	 */
	public boolean isHideSearchConditionFrom() {
	    return hideSearchConditionFrom;
	}

	/**
	 * 検索条件From非表示設定を設定します。
	 * @param hideSearchConditionFrom 検索条件From非表示設定
	 */
	public void setHideSearchConditionFrom(boolean hideSearchConditionFrom) {
	    this.hideSearchConditionFrom = hideSearchConditionFrom;
	}

	/**
	 * 検索条件To非表示設定を取得します。
	 * @return 検索条件To非表示設定
	 */
	public boolean isHideSearchConditionTo() {
	    return hideSearchConditionTo;
	}

	/**
	 * 検索条件To非表示設定を設定します。
	 * @param hideSearchConditionTo 検索条件To非表示設定
	 */
	public void setHideSearchConditionTo(boolean hideSearchConditionTo) {
	    this.hideSearchConditionTo = hideSearchConditionTo;
	}

	/**
	 * 表示タイプがLabel形式の場合に、登録時に登録対象にするかを返します。
	 *
	 * @return true：登録対象
	 */
	public boolean isInsertWithLabelValue() {
		return insertWithLabelValue;
	}

	/**
	 * Label形式の場合の登録制御を設定します。
	 *
	 * @param insertWithLabelValue Label形式の場合の登録制御
	 */
	public void setInsertWithLabelValue(boolean insertWithLabelValue) {
		this.insertWithLabelValue = insertWithLabelValue;
	}

	/**
	 * 表示タイプがLabel形式の場合に、更新時に更新対象にするかを返します。
	 *
	 * @return true：更新対象
	 */
	public boolean isUpdateWithLabelValue() {
		return updateWithLabelValue;
	}

	/**
	 * Label形式の場合の更新制御を設定します。
	 *
	 * @param updateWithLabelValue Label形式の場合の更新制御
	 */
	public void setUpdateWithLabelValue(boolean updateWithLabelValue) {
		this.updateWithLabelValue = updateWithLabelValue;
	}

	@Override
	protected void fillFrom(PropertyEditor editor) {
		super.fillFrom(editor);

		DateTimePropertyEditor e = (DateTimePropertyEditor) editor;
		displayType = e.getDisplayType();
		singleDayCondition = e.isSingleDayCondition();
		hideSearchConditionFrom = e.isHideSearchConditionFrom();
		hideSearchConditionTo = e.isHideSearchConditionTo();
		insertWithLabelValue = e.isInsertWithLabelValue();
		updateWithLabelValue = e.isUpdateWithLabelValue();
		datetimeFormatList = e.getDatetimeFormatList();
	}

	@Override
	protected void fillTo(PropertyEditor editor) {
		super.fillTo(editor);

		DateTimePropertyEditor pe = (DateTimePropertyEditor) editor;
		pe.setDisplayType(displayType);
		pe.setSingleDayCondition(singleDayCondition);
		pe.setHideSearchConditionFrom(hideSearchConditionFrom);
		pe.setHideSearchConditionTo(hideSearchConditionTo);
		pe.setInsertWithLabelValue(insertWithLabelValue);
		pe.setUpdateWithLabelValue(updateWithLabelValue);
		pe.setDatetimeFormatList(datetimeFormatList);
	}

}
