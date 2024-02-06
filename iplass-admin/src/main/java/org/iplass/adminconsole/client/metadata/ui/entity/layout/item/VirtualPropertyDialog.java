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

package org.iplass.adminconsole.client.metadata.ui.entity.layout.item;

import org.iplass.adminconsole.client.base.i18n.AdminClientMessageUtil;
import org.iplass.adminconsole.client.base.ui.widget.MtpDialog;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class VirtualPropertyDialog extends MtpDialog {

	private DynamicForm form;
	private TextItem propName;
	private TextItem displayLabel;

	private IButton ok;
	private IButton cancel;

	public VirtualPropertyDialog() {

		setHeight(170);
		setTitle("VirtualProperty Setting");
		centerInPage();

		propName = new MtpTextItem();
		propName.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_propName"));
		propName.setRequired(true);

		displayLabel = new MtpTextItem();
		displayLabel.setTitle(AdminClientMessageUtil.getString("ui_metadata_entity_layout_DetailDropLayout_displayLabel"));
		displayLabel.setRequired(true);

		form = new MtpForm();
		form.setAutoFocus(true);
		form.setFields(propName, displayLabel);

		container.addMember(form);

		ok = new IButton("OK");

		//cancelはdestroyを実装
		cancel = new IButton("cancel");
		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				destroy();
			}
		});

		footer.setMembers(ok, cancel);
	}

	public void addOKClickHandler(ClickHandler handler) {
		ok.addClickHandler(handler);
	}

	public void addCancelClickHandler(ClickHandler handler) {
		cancel.addClickHandler(handler);
	}

	public boolean validate() {
		return form.validate();
	}

	public String getPropertyName() {
		return SmartGWTUtil.getStringValue(propName);
	}

	public String getDisplayLabel() {
		return SmartGWTUtil.getStringValue(displayLabel);
	}
}
