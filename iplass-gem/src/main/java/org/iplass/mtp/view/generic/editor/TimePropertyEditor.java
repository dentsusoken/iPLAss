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
 * 時間型プロパティエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/TimePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/TimePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class TimePropertyEditor extends DateTimePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8397976783852833945L;

	/** 時間の表示範囲 */
	@MetaFieldInfo(
			displayName="時間の表示範囲",
			displayNameKey="generic_editor_TimePropertyEditor_dispRangeDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=TimeDispRange.class,
			displayOrder=1010,
			description="時間の各リストをどこまで表示するか設定します。",
			descriptionKey="generic_editor_TimePropertyEditor_dispRangeDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.ALL}
	)
	private TimeDispRange dispRange;

	/** 分の間隔 */
	@MetaFieldInfo(
			displayName="分の間隔",
			displayNameKey="generic_editor_TimePropertyEditor_intervalDisplaNameKey",
			inputType=InputType.ENUM,
			enumClass=MinIntereval.class,
			displayOrder=1020,
			description="分のリストの表示間隔を設定します。",
			descriptionKey="generic_editor_TimePropertyEditor_intervalDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private MinIntereval interval;

	/** TimePickerの利用有無 */
	@MetaFieldInfo(
			displayName="TimePickerの利用有無",
			displayNameKey="generic_editor_TimePropertyEditor_useTimePickerDisplaNameKey",
			description="TimePickerを利用して日時を表示するかの設定です。",
			inputType=InputType.CHECKBOX,
			displayOrder=1030,
			descriptionKey="generic_editor_TimePropertyEditor_useTimePickerDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.SEARCHCONDITION, FieldReferenceType.DETAIL}
	)
	private boolean useTimePicker;

	/** 初期値 */
	@MetaFieldInfo(
			displayName="初期値",
			displayNameKey="generic_editor_TimePropertyEditor_defaultValueDisplaNameKey",
			description="新規作成時の初期値を設定します。HHmmss形式か予約語を指定してください。",
			descriptionKey="generic_editor_TimePropertyEditor_defaultValueDescriptionKey",
			displayOrder=1040
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String defaultValue;

	/**
	 * コンストラクタ
	 */
	public TimePropertyEditor() {
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
	 * TimePickerの利用有無を取得します。
	 * @return TimePickerの利用有無
	 */
	public boolean isUseTimePicker() {
		return useTimePicker;
	}

	/**
	 * TimePickerの利用有無を設定します。
	 * @param useTimePicker TimePickerの利用有無
	 */
	public void setUseTimePicker(boolean useTimePicker) {
		this.useTimePicker = useTimePicker;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
}
