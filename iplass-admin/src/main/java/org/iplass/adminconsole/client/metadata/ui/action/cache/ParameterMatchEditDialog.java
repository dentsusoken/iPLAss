/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.action.cache;

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

public class ParameterMatchEditDialog extends MtpDialog {

	/** パラメータ名 */
	private TextItem nameField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public ParameterMatchEditDialog() {

		setHeight(200);
		setTitle("Matching Parameter Name");
		centerInPage();

		nameField = new MtpTextItem("name", "Parameter Name");
		SmartGWTUtil.setRequired(nameField);

		final DynamicForm form = new MtpForm();
		form.setItems(nameField);

		container.addMember(form);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean commonValidate = form.validate();
				if (commonValidate) {
					saveDefinition();
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

	public void setParameterName(String name) {
		nameField.setValue(name);
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	private void saveDefinition() {
		getEditedDefinition();
	}

	private void getEditedDefinition() {

		//データ変更を通知
		fireDataChanged(SmartGWTUtil.getStringValue(nameField));

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(String name) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(name);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
