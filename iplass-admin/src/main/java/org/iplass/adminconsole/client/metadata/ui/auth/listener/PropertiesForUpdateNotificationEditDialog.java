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
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class PropertiesForUpdateNotificationEditDialog extends AbstractWindow {

	private TextItem txtPropertiesForUpdateNotificationField;

	/** フッター */
	private Canvas footerLine;
	private HLayout footer;

	/** 編集対象 */
	private String propertiesForUpdateNotification;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public PropertiesForUpdateNotificationEditDialog() {

		setWidth(550);
		setHeight(110);
		setTitle("Properties For Update Notification Setting");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		txtPropertiesForUpdateNotificationField = new TextItem("propertyName", "Property Name");

		final DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		form.setMargin(5);
		form.setAutoHeight();
		form.setWidth100();
		form.setItems(txtPropertiesForUpdateNotificationField);

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

		footerLine = SmartGWTUtil.separator();
		footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(save, cancel);

		addItem(form);
		addItem(footerLine);
		addItem(footer);
	}

	public void setPropertiesForUpdateNotification(String propertiesForUpdateNotification) {
		this.propertiesForUpdateNotification = propertiesForUpdateNotification;
		txtPropertiesForUpdateNotificationField.setValue(propertiesForUpdateNotification);

		addItem(footerLine);
		addItem(footer);
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
