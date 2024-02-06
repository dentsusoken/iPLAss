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

package org.iplass.adminconsole.client.metadata.ui.calendar;

import java.util.HashMap;

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.MetaDataConstants;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendar.CalendarType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * カレンダー属性編集パネル
 *
 */
public class CalendarAttributePane extends HLayout {

	/** フォーム */
	private DynamicForm form;

	/** タイプ */
	private SelectItem typeField;

	/**
	 * コンストラクタ
	 *
	 * @param defName 定義名
	 */
	public CalendarAttributePane() {
		setHeight(20);
		setMargin(5);

		//入力部分
		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(5);
		form.setColWidths(120, "*", 120, "*", "*");

		typeField = new SelectItem("calType", "Default Disp Type");
		HashMap<String, String> valueMap = new HashMap<>();
		valueMap.put(CalendarType.DAY.name(), "Day");
		valueMap.put(CalendarType.WEEK.name(), "Week");
		valueMap.put(CalendarType.MONTH.name(), "Month");
		typeField.setValueMap(valueMap);
		typeField.setWidth(MetaDataConstants.DEFAULT_FORM_ITEM_WIDTH);

		form.setFields(typeField);

		//配置
		addMember(form);

	}

	/**
	 * EntityCalendarを展開します。
	 *
	 * @param calendar EntityCalendar
	 */
	public void setCalendar(EntityCalendar calendar) {
		if (calendar.getType() != null) {
			typeField.setValue(calendar.getType().name());
		} else {
			typeField.setValue(CalendarType.MONTH.name());
		}
	}

	/**
	 * 編集されたEntityCalendar情報を返します。
	 *
	 * @return 編集EntityCalendar情報
	 */
	public EntityCalendar getEditCalendar(EntityCalendar calendar) {
		String type = SmartGWTUtil.getStringValue(typeField);
		if (type != null && !type.isEmpty()) {
			calendar.setType(CalendarType.valueOf(type));
		} else {
			calendar.setType(CalendarType.MONTH);
		}
		return calendar;
	}

	/**
	 * 入力チェックを実行します。
	 *
	 * @return 入力チェック結果
	 */
	public boolean validate() {
		return form.validate();
	}
}
