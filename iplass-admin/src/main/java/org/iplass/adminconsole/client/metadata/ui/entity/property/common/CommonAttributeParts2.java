/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.adminconsole.client.metadata.ui.entity.property.common;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm2Column;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane.HasNotNullHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributePane.PropertyCommonAttributePaneHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributeParts.TypeChangeHandler;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class CommonAttributeParts2 extends PropertyCommonAttributeParts implements TypeChangeHandler, HasNotNullHandler {

	private DynamicForm form;

	/** 必須 */
	private CheckboxItem chkNotNull;

	/** 変更可否 */
	private CheckboxItem chkUpdatable;

	private PropertyCommonAttributePaneHandler handler;

	public CommonAttributeParts2() {

		chkNotNull = new CheckboxItem();
		chkNotNull.setTitle("Required");
		chkNotNull.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				handler.onChangeNotNull(SmartGWTUtil.getBooleanValue(chkNotNull));
			}
		});

		chkUpdatable = new CheckboxItem();
		chkUpdatable.setTitle("CanEdit");

		form = new MtpForm2Column();
		form.setItems(chkNotNull, chkUpdatable);

		addMember(form);
	}

	@Override
	public void initialize(PropertyCommonAttributePane owner, PropertyCommonAttributePaneHandler handler) {
		this.handler = handler;
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		CommonAttribute commonAttribute = (CommonAttribute)typeAttribute;

		chkNotNull.setValue(commonAttribute.isNotNull());
		chkUpdatable.setValue(commonAttribute.isUpdatable());

		if (commonAttribute.getType() != null) {
			typeControl(commonAttribute.getType());
		}
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		CommonAttribute commonAttribute = (CommonAttribute)record.getCommonAttribute();

		commonAttribute.setNotNull(SmartGWTUtil.getBooleanValue(chkNotNull));
		commonAttribute.setUpdatable(SmartGWTUtil.getBooleanValue(chkUpdatable));
	}

	@Override
	public boolean validate() {
		return form.validate();
	}

	@Override
	public int panelHeight() {
		return 40;
	}

	@Override
	public void onTypeChanged(PropertyDefinitionType type) {

		typeControl(type);

		switch (type) {
		case EXPRESSION:
		case AUTONUMBER:
			chkUpdatable.setValue(false);
			chkNotNull.setValue(false);
			handler.onChangeNotNull(SmartGWTUtil.getBooleanValue(chkNotNull));
			break;
		default:
		}
	}

	@Override
	public void onChangeNotNullFromList(boolean isNotNull) {
		chkNotNull.setValue(isNotNull);
	}

	@Override
	public boolean canEditNotNull() {
		return !chkNotNull.isDisabled();
	}

	private void typeControl(PropertyDefinitionType type) {

		chkNotNull.setDisabled(false);
		chkUpdatable.setDisabled(false);

		switch (type) {
		case EXPRESSION:
		case AUTONUMBER:
			chkUpdatable.setDisabled(true);
			chkNotNull.setDisabled(true);
			break;
		default:
		}
	}

}
