/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.metafield;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpWidgetConstants;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextAreaItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.adminconsole.shared.metadata.dto.refrect.FieldInfo;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MetaFieldScriptItem extends MetaFieldCanvasItem {

	private DynamicForm form;

	private TextAreaItem txtScript;
	private IButton btnScript;

	public MetaFieldScriptItem(final MetaFieldSettingPane pane, final FieldInfo info) {

		VLayout container = new VLayout();
		container.setAutoHeight();
		container.setWidth100();

		form = new DynamicForm();
		form.setWidth100();
		form.setNumCols(2);
		form.setColWidths(MtpWidgetConstants.FORM_WIDTH_ITEM, "*");

		txtScript = new MtpTextAreaItem();
		txtScript.setShowTitle(false);
		txtScript.setWidth("100%");
		txtScript.setHeight(55);
		SmartGWTUtil.setReadOnlyTextArea(txtScript);
		txtScript.setColSpan(2);
		String description = pane.getDescription(info);
		if (SmartGWTUtil.isNotEmpty(description)) {
			SmartGWTUtil.addHoverToFormItem(txtScript, description);
		}
		if (info.isRequired()) {
			SmartGWTUtil.setRequired(txtScript);
		}

		form.setItems(txtScript);

		String displayName = pane.getDisplayName(info);
		final String title = info.isDeprecated() ? "<del>" + displayName + "</del>" : displayName;
		final String scriptHint = pane.getScriptHint(info);

		btnScript = new IButton();
		btnScript.setTitle(AdminClientMessageUtil.getString("ui_metadata_common_MetaFieldSettingPane_edit"));
		btnScript.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Object value = pane.getValue(info.getName());
				String strValue = value != null ? value.toString() : "";
				MetaDataUtil.showScriptEditDialog(
						ScriptEditorDialogMode.getMode(info.getMode()),
						strValue, title, null, scriptHint, new ScriptEditorDialogHandler() {

							@Override
							public void onSave(String text) {
								txtScript.setValue(text);
								pane.setValue(info.getName(), text);
							}

							@Override
							public void onCancel() {
							}
						});
			}

		});

		HLayout buttonPane = new HLayout(5);
		buttonPane.setMargin(5);
		buttonPane.setMembers(btnScript);

		if (pane.getValue(info.getName()) != null) {
			txtScript.setValue(pane.getValueAs(String.class, info.getName()));
		}

		container.addMember(form);
		container.addMember(buttonPane);

		setColSpan(2);
		setCanvas(container);
	}

	@Override
	public Boolean validate() {
		return form.validate();
	}

}
