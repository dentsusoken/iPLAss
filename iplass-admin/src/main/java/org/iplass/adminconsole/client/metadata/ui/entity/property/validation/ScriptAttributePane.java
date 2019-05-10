/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.validation;

import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationAttributePane;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord.ValidationType;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

public class ScriptAttributePane extends ValidationAttributePane {

	private DynamicForm form;

	private TextAreaItem scriptItem;
	private CheckboxItem bindAsArrayItem;

	public ScriptAttributePane() {

		setHeight100();

		ButtonItem editScript = new ButtonItem("editScript", "Edit");
		editScript.setWidth(100);
		editScript.setStartRow(false);
		editScript.setColSpan(3);
		editScript.setAlign(Alignment.RIGHT);
		editScript.setPrompt(rs("ui_metadata_entity_PropertyListGrid_displayDialogEditScript"));
		editScript.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.GROOVY_SCRIPT,
						SmartGWTUtil.getStringValue(scriptItem),
						ScriptEditorDialogConstants.ENTITY_VALIDATION,
						null,
						rs("ui_metadata_entity_PropertyListGrid_scriptHint"),
						new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								scriptItem.setValue(text);
							}
							@Override
							public void onCancel() {
							}
						});
			}
		});

		scriptItem = new MtpTextAreaItem();
		scriptItem.setColSpan(2);
		scriptItem.setTitle("Script");
		scriptItem.setHeight("100%");
		SmartGWTUtil.setReadOnlyTextArea(scriptItem);

		bindAsArrayItem = new CheckboxItem();
		bindAsArrayItem.setTitle("bind variable to array types");
		SmartGWTUtil.addHoverToFormItem(bindAsArrayItem, rs("ui_metadata_entity_PropertyListGrid_scriptAsArray"));

		form = new MtpForm();
		form.setHeight100();
		form.setItems(editScript, scriptItem, bindAsArrayItem);

		addMember(form);
	}

	@Override
	public void setDefinition(ValidationListGridRecord record) {

		scriptItem.setValue(record.getScripting());
		bindAsArrayItem.setValue(record.isAsArray());
	}

	@Override
	public ValidationListGridRecord getEditDefinition(ValidationListGridRecord record) {

		record.setScripting(SmartGWTUtil.getStringValue(scriptItem, true));
		record.setAsArray(SmartGWTUtil.getBooleanValue(bindAsArrayItem));

		return record;
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public void clearErrors() {
		form.clearErrors(true);
	}

	@Override
	public ValidationType getType() {
		return ValidationType.SCRIPT;
	}

	@Override
	public int panelHeight() {
		return 200;
	}

}
