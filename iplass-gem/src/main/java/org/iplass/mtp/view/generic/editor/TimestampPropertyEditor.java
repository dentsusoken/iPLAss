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

package org.iplass.mtp.view.generic.editor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.iplass.adminconsole.view.annotation.InputType;
import org.iplass.adminconsole.view.annotation.MetaFieldInfo;
import org.iplass.adminconsole.view.annotation.generic.EntityViewField;
import org.iplass.adminconsole.view.annotation.generic.FieldReferenceType;
import org.iplass.mtp.view.generic.Jsp;
import org.iplass.mtp.view.generic.Jsps;
import org.iplass.mtp.view.generic.ViewConst;

/**
 * 日時型プロパティエディタ
 * @author lis3wg
 * @author SEKIGUCHI Naoya
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/TimestampPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/TimestampPropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class TimestampPropertyEditor extends DateTimePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3391635812904591761L;

	/** 時間の表示範囲 */
	@MetaFieldInfo(
			displayName="時間の表示範囲",
			displayNameKey="generic_editor_TimestampPropertyEditor_dispRangeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=TimeDispRange.class,
			displayOrder=1010,
			description="時間の各リストをどこまで表示するか設定します。",
			descriptionKey="generic_editor_TimestampPropertyEditor_dispRangeDescriptionKey"
			)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
			)
	private TimeDispRange dispRange;

	/** 分の間隔 */
	@MetaFieldInfo(
			displayName="分の間隔",
			displayNameKey="generic_editor_TimestampPropertyEditor_intervalDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=MinIntereval.class,
			displayOrder=1020,
			description="分のリストの表示間隔を設定します。",
			descriptionKey="generic_editor_TimestampPropertyEditor_intervalDescriptionKey"
			)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
			)
	private MinIntereval interval;

	/** 現在日付設定ボタン表示可否 */
	@MetaFieldInfo(
			displayName="現在日付設定ボタンを非表示",
			displayNameKey="generic_editor_TimestampPropertyEditor_hideButtonPanelDisplaNameKey",
			description="現在日付設定ボタンを非表示にするかを設定します。",
			descriptionKey="generic_editor_TimestampPropertyEditor_hideButtonPanelDescriptionKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1030
			)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
			)
	private boolean hideButtonPanel;

	/** 曜日を表示 */
	@MetaFieldInfo(
			displayName="曜日を表示",
			displayNameKey="generic_editor_TimestampPropertyEditor_showWeekdayDisplaNameKey",
			description="曜日を表示します。",
			descriptionKey="generic_editor_TimestampPropertyEditor_showWeekdayDescriptionKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1040
			)
	@EntityViewField()
	private boolean showWeekday;

	/** 時間のデフォルト値補完を行わない */
	@MetaFieldInfo(
			displayName="時間のデフォルト値を設定しない",
			displayNameKey="generic_editor_TimestampPropertyEditor_notFillTimeDisplaNameKey",
			description="日付入力時に時間のプルダウンにデフォルト値を設定しないようにするかの設定です。",
			descriptionKey="generic_editor_TimestampPropertyEditor_notFillTimeDescriptionKey",
			inputType=InputType.CHECKBOX,
			displayOrder=1050
			)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
			)
	private boolean notFillTime;

	/** DatetimePickerの利用有無 */
	@MetaFieldInfo(
			displayName="DatetimePickerの利用有無",
			displayNameKey="generic_editor_TimestampPropertyEditor_useDatetimePickerDisplaNameKey",
			description="検索時にDatetimePickerを利用して日時を選択するかの設定です。",
			inputType=InputType.CHECKBOX,
			displayOrder=1060,
			descriptionKey="generic_editor_TimestampPropertyEditor_useDatetimePickerDescriptionKey"
			)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
			)
	private boolean useDatetimePicker;

	/** 初期値 */
	@MetaFieldInfo(
			displayName="初期値",
			displayNameKey="generic_editor_TimestampPropertyEditor_defaultValueDisplaNameKey",
			displayOrder=1070,
			description="新規作成時の初期値を設定します。yyyyMMddHHmmss形式か予約語を指定してください。",
			descriptionKey="generic_editor_TimestampPropertyEditor_defaultValueDescriptionKey"
			)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
			)
	private String defaultValue;

	/** 最小日付 */
	@MetaFieldInfo(
			displayName = "最小日付",
			displayNameKey = "generic_editor_TimestampPropertyEditor_minDateDisplaNameKey",
			description = "DatePicker を利用した入力を行う際の最小日付を設定します。 jQuery UI Datepicker のオプション 'minDate' の設定値を利用可能です。",
			descriptionKey = "generic_editor_TimestampPropertyEditor_minDateDescriptionKey",
			displayOrder = 1080)
	@EntityViewField(referenceTypes = { FieldReferenceType.DETAIL, FieldReferenceType.BULK })
	private String minDate;

	/** 最大日付 */
	@MetaFieldInfo(
			displayName = "最大日付",
			displayNameKey = "generic_editor_TimestampPropertyEditor_maxDateDisplaNameKey",
			description = "DatePicker を利用した入力を行う際の最大日付を設定します。 jQuery UI DatePicker のオプション 'maxDate' の設定値を利用可能です。",
			descriptionKey = "generic_editor_TimestampPropertyEditor_maxDateDescriptionKey",
			displayOrder = 1090)
	@EntityViewField(referenceTypes = { FieldReferenceType.DETAIL, FieldReferenceType.BULK })
	private String maxDate;

	/** テキストフィールドへの直接入力を制限する */
	@MetaFieldInfo(displayName = "テキストフィールドへの直接入力を制限する",
			displayNameKey = "generic_editor_TimestampPropertyEditor_restrictDirectEditingDisplaNameKey",
			description = "チェックをすると、直接入力ができなくなり DatePicker の入力のみ受け付けるようになります。",
			descriptionKey = "generic_editor_TimestampPropertyEditor_restrictDirectEditingDescriptionKey",
			inputType = InputType.CHECKBOX,
			displayOrder = 1100)
	@EntityViewField(referenceTypes = { FieldReferenceType.DETAIL, FieldReferenceType.BULK })
	private boolean restrictDirectEditing;

	/**
	 * コンストラクタ
	 */
	public TimestampPropertyEditor() {
	}

	/**
	 * 時間の表示範囲を取得します。
	 * @return 時間の表示範囲
	 */
	public TimeDispRange getDispRange() {
		return dispRange;
	}

	/**
	 * 時間の表示範囲を設定します。
	 * @param dispRange 時間の表示範囲
	 */
	public void setDispRange(TimeDispRange dispRange) {
		this.dispRange = dispRange;
	}

	/**
	 * 分の間隔を取得します。
	 * @return 分の間隔
	 */
	public MinIntereval getInterval() {
		return interval;
	}

	/**
	 * 分の間隔を設定します。
	 * @param interval 分の間隔
	 */
	public void setInterval(MinIntereval interval) {
		this.interval = interval;
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
	 * 時間のデフォルト値補完を行わないを取得します。
	 * @return 時間のデフォルト値補完を行わない
	 */
	public boolean isNotFillTime() {
		return notFillTime;
	}

	/**
	 * 時間のデフォルト値補完を行わないを設定します。
	 * @param notFillTime 時間のデフォルト値補完を行わない
	 */
	public void setNotFillTime(boolean notFillTime) {
		this.notFillTime = notFillTime;
	}

	/**
	 * DatetimePickerの利用有無を取得します。
	 * @return DatetimePickerの利用有無
	 */
	public boolean isUseDatetimePicker() {
		return useDatetimePicker;
	}

	/**
	 * DatetimePickerの利用有無を設定します。
	 * @param displayDateTimePicker DatetimePickerの利用有無
	 */
	public void setUseDatetimePicker(boolean useDatetimePicker) {
		this.useDatetimePicker = useDatetimePicker;
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
