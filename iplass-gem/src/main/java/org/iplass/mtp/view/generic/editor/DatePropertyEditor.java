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

package org.iplass.mtp.view.generic.editor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * 日付型プロパティエディタ
 * @author lis3wg
 * @author SEKIGUCHI Naoya
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/DatePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/DatePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class DatePropertyEditor extends DateTimePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4666753985814020563L;

	/** 現在日付設定ボタン表示可否 */
	@MetaFieldInfo(
			displayName="現在日付設定ボタンを非表示",
			displayNameKey="generic_editor_DatePropertyEditor_hideButtonPanelDisplaNameKey",
			description="現在日付設定ボタンを非表示するかを設定します。",
			descriptionKey="generic_editor_DatePropertyEditor_hideButtonPanelDescriptionKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1010
			)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
			)
	private boolean hideButtonPanel;

	/** 曜日を表示 */
	@MetaFieldInfo(
			displayName="曜日を表示",
			displayNameKey="generic_editor_DatePropertyEditor_showWeekdayDisplaNameKey",
			description="曜日を表示します。",
			descriptionKey="generic_editor_DatePropertyEditor_showWeekdayDescriptionKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1020
			)
	@EntityViewField()
	private boolean showWeekday;

	/** 初期値 */
	@MetaFieldInfo(
			displayName="初期値",
			displayNameKey="generic_editor_DatePropertyEditor_defaultValueDisplaNameKey",
			description="新規作成時の初期値を設定します。yyyyMMdd形式か予約語を指定してください。",
			descriptionKey="generic_editor_DatePropertyEditor_defaultValueDescriptionKey",
			displayOrder=1030
			)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
			)
	private String defaultValue;

	/** 最小日付 */
	@MetaFieldInfo(
			displayName = "最小日付",
			displayNameKey = "generic_editor_DatePropertyEditor_minDateDisplaNameKey",
			description = "DatePicker を利用した入力を行う際の最小日付を設定します。 jQuery UI Datepicker のオプション 'minDate' の設定値を利用可能です。",
			descriptionKey = "generic_editor_DatePropertyEditor_minDateDescriptionKey",
			displayOrder = 1040)
	@EntityViewField(referenceTypes = { FieldReferenceType.DETAIL, FieldReferenceType.BULK })
	private String minDate;

	/** 最小日付の設定値をファンクション実行する */
	@MetaFieldInfo(displayName = "最小日付の設定値をファンクション実行する",
			displayNameKey = "generic_editor_DatePropertyEditor_minDateFunctionDisplaNameKey",
			description = "チェックをすると、最小日付の文字列を javascript function として実行し、実行結果を DatePicker の最小日付として設定します。",
			descriptionKey = "generic_editor_DatePropertyEditor_minDateFunctionDescriptionKey",
			inputType = InputType.CHECKBOX, displayOrder = 1050)
	@EntityViewField(referenceTypes = { FieldReferenceType.DETAIL, FieldReferenceType.BULK })
	private boolean minDateFunction;

	/** 最大日付 */
	@MetaFieldInfo(
			displayName = "最大日付",
			displayNameKey = "generic_editor_DatePropertyEditor_maxDateDisplaNameKey",
			description = "DatePicker を利用した入力を行う際の最大日付を設定します。 jQuery UI DatePicker のオプション 'maxDate' の設定値を利用可能です。",
			descriptionKey = "generic_editor_DatePropertyEditor_maxDateDescriptionKey",
			displayOrder = 1060)
	@EntityViewField(referenceTypes = { FieldReferenceType.DETAIL, FieldReferenceType.BULK })
	private String maxDate;

	/** 最大日付の設定値をファンクション実行する */
	@MetaFieldInfo(displayName = "最大日付の設定値をファンクション実行する",
			displayNameKey = "generic_editor_DatePropertyEditor_maxDateFunctionDisplaNameKey",
			description = "チェックをすると、最大日付の文字列を javascript function として実行し、実行結果を DatePicker の最大日付として設定します。",
			descriptionKey = "generic_editor_DatePropertyEditor_maxDateFunctionDescriptionKey",
			inputType = InputType.CHECKBOX, displayOrder = 1070)
	@EntityViewField(referenceTypes = { FieldReferenceType.DETAIL, FieldReferenceType.BULK })
	private boolean maxDateFunction;

	/** テキストフィールドへの直接入力を制限する */
	@MetaFieldInfo(
			displayName = "テキストフィールドへの直接入力を制限する",
			displayNameKey = "generic_editor_DatePropertyEditor_restrictDirectEditingDisplaNameKey",
			description = "チェックをすると、直接入力ができなくなり DatePicker の入力のみ受け付けるようになります。",
			descriptionKey = "generic_editor_DatePropertyEditor_restrictDirectEditingDescriptionKey",
			inputType = InputType.CHECKBOX,
			displayOrder = 1080)
	@EntityViewField(referenceTypes = { FieldReferenceType.DETAIL, FieldReferenceType.BULK })
	private boolean restrictDirectEditing;

	/**
	 * コンストラクタ
	 */
	public DatePropertyEditor() {
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * 現在日付設定ボタン表示可否を取得します。
	 * @return 現在日付設定ボタン表示可否
	 */
	public boolean isHideButtonPanel() {
		return hideButtonPanel;
	}

	/**
	 * 現在日付設定ボタン表示可否を設定します。
	 * @param hideButtonPanel 現在日付設定ボタン表示可否
	 */
	public void setHideButtonPanel(boolean hideButtonPanel) {
		this.hideButtonPanel = hideButtonPanel;
	}

	/**
	 * @return showWeekday
	 */
	public boolean isShowWeekday() {
		return showWeekday;
	}

	/**
	 * @param showWeekday セットする showWeekday
	 */
	public void setShowWeekday(boolean showWeekday) {
		this.showWeekday = showWeekday;
	}

	/**
	 * 最小日付を取得する
	 * @return 最小日付
	 */
	public String getMinDate() {
		return minDate;
	}

	/**
	 * 最小日付を設定する
	 * @param minDate 最小日付
	 */
	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	/**
	 * 最小日付の設定値を JS ファンクション実行するパラメータを取得する
	 * @return ファンクション実行するか（true: ファンクション実行する、false: 設定値を利用する）
	 */
	public boolean isMinDateFunction() {
		return minDateFunction;
	}

	/**
	 * 最小日付の設定値を JS ファンクション実行するパラメータを設定する
	 * @param minDateFunction ファンクション実行するか
	 */
	public void setMinDateFunction(boolean minDateFunction) {
		this.minDateFunction = minDateFunction;
	}

	/**
	 * 最大日付を取得する
	 * @return 最大日付
	 */
	public String getMaxDate() {
		return maxDate;
	}

	/**
	 * 最大日付を設定する
	 * @param maxDate 最大日付
	 */
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * 最大日付の設定値を JS ファンクション実行するパラメータを取得する
	 * @return ファンクション実行するか（true: ファンクション実行する、false: 設定値を利用する）
	 */
	public boolean isMaxDateFunction() {
		return maxDateFunction;
	}

	/**
	 * 最大日付の設定値を JS ファンクション実行するパラメータを設定する
	 * @param maxDateFunction ファンクション実行するか
	 */
	public void setMaxDateFunction(boolean maxDateFunction) {
		this.maxDateFunction = maxDateFunction;
	}

	/**
	 * テキストフィールドへの直接入力を制限する値を取得する
	 * @return テキストフィールドへの直接入力を制限する（true: 直接入力不可能、false: 直接入力可能）
	 */
	public boolean isRestrictDirectEditing() {
		return restrictDirectEditing;
	}

	/**
	 * テキストフィールドへの直接入力を制限する値を設定する
	 * @param restrictDirectEditing テキストフィールドへの直接入力を制限する
	 * @see {@link #isRestrictDirectEditing()}
	 */
	public void setRestrictDirectEditing(boolean restrictDirectEditing) {
		this.restrictDirectEditing = restrictDirectEditing;
	}
}
