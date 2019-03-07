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

package org.iplass.adminconsole.client.metadata.ui.webapi;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogHandler;
import org.iplass.adminconsole.client.base.ui.widget.ScriptEditorDialogMode;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.MetaDataUtil;
import org.iplass.mtp.webapi.definition.WebApiDefinition;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;

public class CorsAttributePane extends VLayout {

	private DynamicForm form;

	/** Access-Control-Allow-Originヘッダ */
	private TextAreaItem accessControlAllowOriginField;

	/** Access-Control-Allow-Credentials */
	private CheckboxItem accessControlAllowCredentialsField;

	public CorsAttributePane() {

		setWidth100();
		setAutoHeight();
		setMargin(5);
		setMembersMargin(10);

		form = new DynamicForm();
		form.setWidth100();
		form.setPadding(10);
		form.setNumCols(2);
		form.setColWidths(80, "*");
		form.setIsGroup(true);
		form.setGroupTitle("CORS");

		ButtonItem editButton = new ButtonItem("editScript", "Edit");
		editButton.setWidth(100);
		editButton.setStartRow(false);
		editButton.setColSpan(2);
		editButton.setAlign(Alignment.RIGHT);
		editButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				MetaDataUtil.showScriptEditDialog(ScriptEditorDialogMode.JSP,
					SmartGWTUtil.getStringValue(accessControlAllowOriginField),
					"Access-Control-Allow-Origin Script",
					null,
					AdminClientMessageUtil.getString("ui_metadata_webapi_CorsAttributePane_accessControlAllowOriginComment"),
					new ScriptEditorDialogHandler() {

						@Override
						public void onSave(String text) {
							accessControlAllowOriginField.setValue(text);
						}

						@Override
						public void onCancel() {
						}
					}
				);
			}
		});

		accessControlAllowOriginField = new TextAreaItem("accessControlAllowOriginField", "Access-Control-Allow-Origin");
		accessControlAllowOriginField.setWidth("*");
		accessControlAllowOriginField.setHeight(65);
		accessControlAllowOriginField.setStartRow(true);
		accessControlAllowOriginField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_CorsAttributePane_accessControlAllowOrigin")));
		SmartGWTUtil.setReadOnlyTextArea(accessControlAllowOriginField);

		accessControlAllowCredentialsField = new CheckboxItem("accessControlAllowCredentialsField", "Access-Control-Allow-Credentials");
		accessControlAllowCredentialsField.setTooltip(SmartGWTUtil.getHoverString(AdminClientMessageUtil.getString("ui_metadata_webapi_CorsAttributePane_accessControlAllowCredentials")));

		form.setItems(editButton, accessControlAllowOriginField, accessControlAllowCredentialsField);

		addMember(form);

	}

	public void setDefinition(WebApiDefinition definition) {
		accessControlAllowOriginField.setValue(definition.getAccessControlAllowOrigin());
		accessControlAllowCredentialsField.setValue(definition.isAccessControlAllowCredentials());
	}

	public WebApiDefinition getEditDefinition(WebApiDefinition definition) {

		definition.setAccessControlAllowOrigin(SmartGWTUtil.getStringValue(accessControlAllowOriginField, true));
		definition.setAccessControlAllowCredentials(SmartGWTUtil.getBooleanValue(accessControlAllowCredentialsField));

		return definition;
	}

	public boolean validate() {
		return form.validate();
	}

}
