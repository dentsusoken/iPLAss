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

package org.iplass.adminconsole.client.metadata.ui.entity;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.io.download.PostDownloadFrame;
import org.iplass.adminconsole.client.base.tenant.TenantInfoHolder;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.shared.metadata.dto.entity.EntityJavaMappingClassDownloadProperty;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class CreateJavaMappingClassDialog extends MtpDialog {

	public CreateJavaMappingClassDialog(final String defName, final String className) {

		setTitle("Create Java Mapping Class");
		setHeight(200);
		centerInPage();

		final DynamicForm form = new MtpForm();
		form.setAutoFocus(true);

		final TextItem classNameField = new MtpTextItem("className","Class Name");
		classNameField.setValue(className);

		form.setItems(classNameField);

		Label comment1 = new Label(AdminClientMessageUtil.getString("ui_metadata_entity_CreateJavaMappingClassDialog_comment1"));
		comment1.setHeight(32);
		comment1.setMargin(10);
		Label comment2 = new Label(AdminClientMessageUtil.getString("ui_metadata_entity_CreateJavaMappingClassDialog_comment2"));
		comment2.setMargin(10);
		comment2.setHeight(32);

		container.addMembers(form, comment1, comment2);

		IButton create = new IButton("Create");
		create.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()){
					PostDownloadFrame frame = new PostDownloadFrame();
					frame.setAction(GWT.getModuleBaseURL() + EntityJavaMappingClassDownloadProperty.ACTION_URL)
						.addParameter(EntityJavaMappingClassDownloadProperty.TENANT_ID, String.valueOf(TenantInfoHolder.getId()))
						.addParameter(EntityJavaMappingClassDownloadProperty.ENTITY_NAME, defName)
						.addParameter(EntityJavaMappingClassDownloadProperty.CLASS_NAME, SmartGWTUtil.getStringValue(classNameField))
						.execute();
				}
			}
		});

		IButton cancel = new IButton("Cancel");
		cancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(create, cancel);
	}

}
