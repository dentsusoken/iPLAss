/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.template.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.data.template.ReportTemplateDS;
import org.iplass.mtp.web.template.report.definition.ReportParamMapDefinition;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;

public class ReportParamMapEditDialog extends AbstractWindow {

	private SelectItem typeField;
	private SelectItem templateField;
	private TextItem nameField;
	private TextItem fromField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public ReportParamMapEditDialog() {

		setWidth(370);
		setHeight(190);
		setTitle("JasperReports Parameter");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		typeField = new SelectItem("type", "Value Type");
		templateField = new SelectItem("template", "Template");

		LinkedHashMap<String, String> typeMap = new LinkedHashMap<String, String>();
		typeMap.put("string", "Parameter");
		typeMap.put("report", "SubReport");
		typeMap.put("list", "List(Deprecated)");	//TODO 削除予定
		typeField.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if("report".equals(typeField.getValueAsString())){
					fromField.hide();
					templateField.show();

					templateField.setWidth(250);
					templateField.setOptionDataSource(ReportTemplateDS.getInstance());
					templateField.setValueField("name");
					templateField.setDisplayField("displayName");
					templateField.setPickListWidth(480);
					SmartGWTUtil.setRequired(templateField);

					ListGridField nameField = new ListGridField("name", "Name", 230);
					ListGridField displayNameField = new ListGridField("displayName", "Display Name", 230);
					templateField.setPickListFields(displayNameField, nameField);

				} else {
					fromField.show();
					templateField.hide();
				}
			}
		});
		typeField.setValueMap(typeMap);

		nameField = new TextItem("name", "Name");
		nameField.setWidth(250);
		SmartGWTUtil.setRequired(nameField);

		fromField = new TextItem("mapFrom", "Value");
		fromField.setWidth(250);
		SmartGWTUtil.setRequired(fromField);

		final DynamicForm form = new DynamicForm();
		form.setMargin(5);
		form.setHeight100();
		form.setWidth100();
		form.setItems(typeField, nameField, fromField, templateField);

		IButton save = new IButton("OK");
		save.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()){
					saveMap();
				} else {
//					errors.setVisible(true);
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
		//footer.setAlign(Alignment.LEFT);
		footer.setAlign(VerticalAlignment.CENTER);
		footer.setMembers(save, cancel);

		addItem(form);
		addItem(SmartGWTUtil.separator());
		addItem(footer);
	}

	/**
	 * 編集対象の {@link ReportParamMapDefinition} を設定します。
	 * @param paramMap 編集対象の {@link ReportParamMapDefinition}
	 */
	public void setParamMap(ReportParamMapDefinition paramMap) {
		if("report".equals(paramMap.getParamType())){
			fromField.hide();
			templateField.show();

			templateField.setWidth(250);
			templateField.setOptionDataSource(ReportTemplateDS.getInstance());
			templateField.setValueField("name");
			templateField.setDisplayField("displayName");
			templateField.setPickListWidth(480);
			SmartGWTUtil.setRequired(templateField);

			ListGridField nameField = new ListGridField("name", "Name", 230);
			ListGridField displayNameField = new ListGridField("displayName", "Display Name", 230);
			templateField.setPickListFields(displayNameField, nameField);

			templateField.setValue(paramMap.getMapFrom());
		} else {
			fromField.show();
			templateField.hide();

			fromField.setValue(paramMap.getMapFrom());
		}

		nameField.setValue(paramMap.getName());
		typeField.setValue(paramMap.getParamType());
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
		ReportParamMapDefinition paramMap = new ReportParamMapDefinition();
		if("report".equals(typeField.getValueAsString())){
			paramMap.setMapFrom(SmartGWTUtil.getStringValue(templateField));
		} else {
			paramMap.setMapFrom(SmartGWTUtil.getStringValue(fromField));
		}
		paramMap.setName(SmartGWTUtil.getStringValue(nameField));
		paramMap.setParamType(typeField.getValueAsString());

		//データ変更を通知
		fireDataChanged(paramMap);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(ReportParamMapDefinition paramMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(paramMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
