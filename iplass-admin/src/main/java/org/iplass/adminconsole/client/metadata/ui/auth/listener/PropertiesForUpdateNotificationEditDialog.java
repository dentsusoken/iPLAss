/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.auth.listener;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class PropertiesForUpdateNotificationEditDialog extends MtpDialog {

	private TextItem txtPropertiesForUpdateNotificationField;

	/** 編集対象 */
	private String propertiesForUpdateNotification;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public PropertiesForUpdateNotificationEditDialog() {

		setHeight(200);
		setTitle("Properties For Update Notification Setting");
		centerInPage();

		txtPropertiesForUpdateNotificationField = new MtpTextItem("propertyName", "Property Name");

		final DynamicForm form = new MtpForm();
		form.setAutoHeight();
		form.setItems(txtPropertiesForUpdateNotificationField);

		container.addMember(form);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean commonValidate = form.validate();
				if (commonValidate) {
					savePropertiesForUpdateNotification();
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(save, cancel);

	}

	public void setPropertiesForUpdateNotification(String propertiesForUpdateNotification) {
		this.propertiesForUpdateNotification = propertiesForUpdateNotification;
		txtPropertiesForUpdateNotificationField.setValue(propertiesForUpdateNotification);
	}

	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	private void savePropertiesForUpdateNotification() {
		getEditedPropertiesForUpdateNotification();
	}

	private void getEditedPropertiesForUpdateNotification() {

		if (propertiesForUpdateNotification == null) {
			propertiesForUpdateNotification = new String();
		}

		propertiesForUpdateNotification = SmartGWTUtil.getStringValue(txtPropertiesForUpdateNotificationField, true);

		//データ変更を通知
		fireDataChanged(propertiesForUpdateNotification);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(String propertiesForUpdateNotification) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(propertiesForUpdateNotification);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
