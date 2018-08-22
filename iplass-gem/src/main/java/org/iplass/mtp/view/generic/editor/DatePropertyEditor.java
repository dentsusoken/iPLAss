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
 * 日付型プロパティエディタ
 * @author lis3wg
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Jsps({
	@Jsp(path="/jsp/gem/generic/editor/DatePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM),
	@Jsp(path="/jsp/gem/aggregation/unit/editor/DatePropertyEditor.jsp", key=ViewConst.DESIGN_TYPE_GEM_AGGREGATION)
})
public class DatePropertyEditor extends DateTimePropertyEditor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4666753985814020563L;

	/** 初期値 */
	@MetaFieldInfo(
			displayName="初期値",
			displayNameKey="generic_editor_DatePropertyEditor_defaultValueDisplaNameKey",
			description="新規作成時の初期値を設定します。yyyyMMdd形式か予約語を指定してください。",
			descriptionKey="generic_editor_DatePropertyEditor_defaultValueDescriptionKey"
	)
	@EntityViewField(
			referenceTypes={FieldReferenceType.DETAIL}
	)
	private String defaultValue;

	/** 現在日付設定ボタン表示可否 */
	@MetaFieldInfo(
			displayName="現在日付設定ボタンを非表示",
			displayNameKey="generic_editor_DatePropertyEditor_hideButtonPanelDisplaNameKey",
			description="現在日付設定ボタンを非表示するかを設定します。",
			descriptionKey="generic_editor_DatePropertyEditor_hideButtonPanelDescriptionKey",
			inputType=InputType.CHECKBOX
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
			inputType=InputType.CHECKBOX
	)
	@EntityViewField()
	private boolean showWeekday;

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
}
