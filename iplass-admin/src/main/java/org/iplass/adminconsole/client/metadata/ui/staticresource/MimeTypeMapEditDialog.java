/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.staticresource;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.staticresource.definition.MimeTypeMappingDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class MimeTypeMapEditDialog extends MtpDialog {

	private TextItem extensionField;
	private TextItem mimeTypeField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public MimeTypeMapEditDialog() {

		setHeight(200);
		setTitle("MIME Type Mapping Setting");
		centerInPage();

		extensionField = new MtpTextItem("extension", "Extension");
		SmartGWTUtil.setRequired(extensionField);

		mimeTypeField = new MtpTextItem("mimeType", "MIME Type");
		SmartGWTUtil.setRequired(mimeTypeField);

		final DynamicForm form = new MtpForm();
		form.setItems(extensionField, mimeTypeField);

		container.addMember(form);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()){
					saveMap();
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

	/**
	 * 編集対象の {@link MimeTypeMappingDefinition} を設定します。
	 * @param mimeTypeMap 編集対象の {@link MimeTypeMappingDefinition}
	 */
	public void setMimeTypeMap(MimeTypeMappingDefinition mimeTypeMap) {
		extensionField.setValue(mimeTypeMap.getExtension());
		mimeTypeField.setValue(mimeTypeMap.getMimeType());
	}

	/**
	 * {@link DataChangedHandler} を追加します。
	 *
	 * @param handler {@link DataChangedHandler}
	 */
	public void addDataChangeHandler(DataChangedHandler handler) {
		handlers.add(0, handler);
	}

	private void saveMap() {
		getEditedParamMap();
	}

	private void getEditedParamMap() {
		MimeTypeMappingDefinition mimeTypeMap = new MimeTypeMappingDefinition();
		mimeTypeMap.setExtension(SmartGWTUtil.getStringValue(extensionField));
		mimeTypeMap.setMimeType(SmartGWTUtil.getStringValue(mimeTypeField));

		//データ変更を通知
		fireDataChanged(mimeTypeMap);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(MimeTypeMappingDefinition mimeTypeMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(mimeTypeMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}

}
