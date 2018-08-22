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

package org.iplass.adminconsole.client.metadata.ui.action;

import java.util.ArrayList;
import java.util.List;

import org.iplass.adminconsole.client.base.event.DataChangedEvent;
import org.iplass.adminconsole.client.base.event.DataChangedHandler;
import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.AbstractWindow;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.web.actionmapping.definition.ParamMapDefinition;

import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;

public class ParamMapEditDialog extends AbstractWindow {

	private TextItem nameField;
	private TextItem fromField;
	private TextAreaItem conditionField;

	/** データ変更ハンドラ */
	private List<DataChangedHandler> handlers = new ArrayList<DataChangedHandler>();

	public ParamMapEditDialog() {

		setWidth(500);
		setHeight(400);
		setTitle("Parameter Mapping Setting");
		setShowMinimizeButton(false);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		nameField = new TextItem("name", "Parameter Name");
		nameField.setColSpan(2);
		nameField.setWidth(250);
		SmartGWTUtil.setRequired(nameField);

		fromField = new TextItem("mapFrom", "Map From");
		fromField.setColSpan(2);
		fromField.setWidth(250);
		SmartGWTUtil.setRequired(fromField);

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setPrompt(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_action_ParamMapEditDialog_dispEditDialogCondition")));
		editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(conditionField),
						ScriptEditorDialogConstants.ACTION_PARAMMAP_CONDITION,
						"ui_metadata_action_ParamMapEditDialog_conditionHint",
						null,
						new ScriptEditorDialogHandler() {

					@Override
					public void onSave(String text) {
						conditionField.setValue(text);
					}

					@Override
					public void onCancel() {
					}
				});

			}
		});

		conditionField = new TextAreaItem("condition", "Condition");
		conditionField.setColSpan(2);
		conditionField.setWidth("100%");
		conditionField.setHeight("100%");
		SmartGWTUtil.setReadOnlyTextArea(conditionField);

		final DynamicForm form = new DynamicForm();
		form.setNumCols(3);
		form.setColWidths(120, "*", 100);
		form.setMargin(5);
		form.setHeight100();
		form.setWidth100();
		form.setItems(nameField, fromField, new SpacerItem(), new SpacerItem(), editScript, conditionField);

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
		addItem(footer);
	}

	/**
	 * 編集対象の {@link ParamMapDefinition} を設定します。
	 * @param paramMap 編集対象の {@link ParamMapDefinition}
	 */
	public void setParamMap(ParamMapDefinition paramMap) {
		nameField.setValue(paramMap.getName());
		fromField.setValue(paramMap.getMapFrom());
		conditionField.setValue(paramMap.getCondition());
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
		ParamMapDefinition paramMap = new ParamMapDefinition();
		paramMap.setName(SmartGWTUtil.getStringValue(nameField));
		paramMap.setMapFrom(SmartGWTUtil.getStringValue(fromField));
		paramMap.setCondition(SmartGWTUtil.getStringValue(conditionField));

		//データ変更を通知
		fireDataChanged(paramMap);

		//ダイアログ消去
		destroy();
	}

	/**
	 * データ変更通知処理
	 */
	private void fireDataChanged(ParamMapDefinition paramMap) {
		DataChangedEvent event = new DataChangedEvent();
		event.setValueObject(paramMap);
		for (DataChangedHandler handler : handlers) {
			handler.onDataChanged(event);
		}
	}
}
