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

import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.common.Callable;
import org.iplass.adminconsole.client.metadata.ui.common.MetaDataCreateDialog;
import org.iplass.mtp.view.calendar.EntityCalendar;
import org.iplass.mtp.view.calendar.EntityCalendar.CalendarType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * 新規作成ダイアログ
 */
public class CreateCalendarDialog extends MetaDataCreateDialog {

	private DynamicForm calendarForm;

	private SelectItem typeField;

	public CreateCalendarDialog(String definitionClassName, String nodeDisplayName, String folderPath, boolean isCopyMode) {
		super(definitionClassName, nodeDisplayName, folderPath, isCopyMode);

		if (!isCopyMode) {
			setHeight(DEFAULT_HEIGHT + 70);

			typeField = new SelectItem("calType", "Default Disp Type");
			typeField.setValueMap("Week", "Month");
			typeField.setValue("Week");
			SmartGWTUtil.setRequired(typeField);

			calendarForm = createDefaultForm();
			calendarForm.setIsGroup(true);
			calendarForm.setGroupTitle("Calendar Settings");
			calendarForm.setItems(typeField);

			addCustomParts(calendarForm);
		}
	}

	@Override
	protected void saveAction(final SaveInfo saveInfo, final boolean isCopyMode) {
		if (calendarForm != null && !calendarForm.validate()) {
			return;
		}

		// 存在チェック
		checkExist(saveInfo.getName(), new Callable<Void>() {

			@Override
			public Void call() {
				createCalendar(saveInfo, isCopyMode);
				return null;
			}
		});
	}

	/**
	 * カレンダーを登録します。
	 */
	private void createCalendar(final SaveInfo saveInfo, final boolean isCopyMode) {

		if (isCopyMode) {
			service.copyDefinition(TenantInfoHolder.getId(), getDefinitionClassName(), getSourceName(),
					saveInfo.getName(), saveInfo.getDisplayName(), saveInfo.getDescription(), new SaveResultCallback());
		} else {
			EntityCalendar definition = new EntityCalendar();

			definition.setName(saveInfo.getName());
			if (saveInfo.getDisplayName() == null || saveInfo.getDisplayName().isEmpty()) {
				definition.setDisplayName(saveInfo.getName());
			} else {
				definition.setDisplayName(saveInfo.getDisplayName());
			}
			definition.setDescription(saveInfo.getDescription());

			String type = SmartGWTUtil.getStringValue(typeField);
			if ("Week".equals(type)) {
				definition.setType(CalendarType.WEEK);
			} else if ("Month".equals(type)) {
				definition.setType(CalendarType.MONTH);
			} else if ("Day".equals(type)) {
				definition.setType(CalendarType.DAY);
			}

			service.createDefinition(TenantInfoHolder.getId(), definition, new SaveResultCallback());
		}
	}

}
