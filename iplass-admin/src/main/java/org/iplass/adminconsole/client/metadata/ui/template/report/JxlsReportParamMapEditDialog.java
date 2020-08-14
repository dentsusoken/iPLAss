/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.mtp.web.template.report.definition.JxlsContextParamMapDefinition;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class JxlsReportParamMapEditDialog extends MtpDialog {
	private TextItem keyField;
	private TextItem mapFromField;
	private CheckboxItem toMapField;
	
	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();
	
	public JxlsReportParamMapEditDialog() {

		setHeight(200);
		setTitle("JxlsReports Parameter");
		centerInPage();

		keyField = new MtpTextItem("key", "Key");
		SmartGWTUtil.setRequired(keyField);

		mapFromField = new MtpTextItem("mapFrom", "Value");
		SmartGWTUtil.setRequired(mapFromField);
		
		toMapField = new CheckboxItem("toMap", "To Map (Only for mapping GenericEntity or lists of them)");
		SmartGWTUtil.addHoverToFormItem(toMapField, AdminClientMessageUtil.getString("ui_metadata_template_report_JxlsReportParamMapEditGrid_toMap"));

		final DynamicForm form = new MtpForm();
		form.setHeight100();
		form.setItems(keyField, mapFromField, toMapField);

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

	public void setContextParamMap(JxlsContextParamMapDefinition jcpmd) {
		keyField.setValue(jcpmd.getKey());
		mapFromField.setValue(jcpmd.getMapFrom());
		toMapField.setValue(jcpmd.isConvertEntityToMap());
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
		JxlsContextParamMapDefinition jcpmd = new JxlsContextParamMapDefinition();
		jcpmd.setKey(SmartGWTUtil.getStringValue(keyField));
		jcpmd.setMapFrom(SmartGWTUtil.getStringValue(mapFromField));
		jcpmd.setConvertEntityToMap(SmartGWTUtil.getBooleanValue(toMapField));

		//データ変更を通知
		fireDataChanged(jcpmd);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(JxlsContextParamMapDefinition jcpmd) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(jcpmd);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
