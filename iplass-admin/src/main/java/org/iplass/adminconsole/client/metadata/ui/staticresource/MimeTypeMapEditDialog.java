/*
 * Copyright (C) 2016 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.staticresource.definition.MimeTypeMappingDefinition;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class MimeTypeMapEditDialog extends AbstractWindow {

	private TextItem extensionField;
	private TextItem mimeTypeField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public MimeTypeMapEditDialog() {
		setWidth(370);
		setHeight(140);
		setTitle("MIME Type Mapping Setting");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		extensionField = new TextItem("extension", "Extension");
		extensionField.setWidth(250);
		SmartGWTUtil.setRequired(extensionField);

		mimeTypeField = new TextItem("mimeType", "MIME Type");
		mimeTypeField.setWidth(250);
		SmartGWTUtil.setRequired(mimeTypeField);

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
		form.setHeight100();
		form.setWidth100();
		form.setItems(extensionField, mimeTypeField);

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

		HLayout footer = new HLayout(5);
		footer.setMargin(5);
		footer.setHeight(20);
		footer.setWidth100();
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(save, cancel);

		addItem(form);
		addItem(footer);
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
