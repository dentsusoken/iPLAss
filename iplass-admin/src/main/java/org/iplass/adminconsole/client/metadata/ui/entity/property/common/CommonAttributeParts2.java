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

import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttributePane.HasNotNullHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributePane.PropertyCommonAttributePaneHandler;
import org.iplass.adminconsole.client.metadata.ui.entity.property.common.PropertyCommonAttributeParts.TypeChangeHandler;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class CommonAttributeParts2 extends PropertyCommonAttributeParts implements TypeChangeHandler, HasNotNullHandler {

	private DynamicForm form = new DynamicForm();

	/** 多重度 */
	private TextItem txtMultipl;
	/** 必須 */
	private CheckboxItem chkNotNull;
	/** 変更可否 */
	private CheckboxItem chkUpdatable;

	private PropertyCommonAttributePaneHandler handler;

	public CommonAttributeParts2() {

		txtMultipl = new TextItem();
		txtMultipl.setTitle("Multiple");
		txtMultipl.setKeyPressFilter(KEYFILTER_NUM);
		txtMultipl.setWidth(40);

		chkNotNull = new CheckboxItem();
		chkNotNull.setTitle("Required");
		chkNotNull.setWidth(40);
		chkNotNull.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				handler.onChangeNotNull(SmartGWTUtil.getBooleanValue(chkNotNull));
			}
		});

		chkUpdatable = new CheckboxItem();
		chkUpdatable.setTitle("CanEdit");

		form.setMargin(5);
		form.setNumCols(7);
		form.setWidth100();
		form.setHeight(30);
		form.setItems(txtMultipl, chkNotNull, chkUpdatable);

		addMember(form);

	}

	@Override
	public void initialize(PropertyCommonAttributePane owner, PropertyCommonAttributePaneHandler handler) {
		this.handler = handler;
	}

	@Override
	public void applyFrom(String defName, PropertyListGridRecord record, PropertyAttribute typeAttribute) {

		CommonAttribute commonAttribute = (CommonAttribute)typeAttribute;

		txtMultipl.setValue(commonAttribute.getMultipleStringValue());
		chkNotNull.setValue(commonAttribute.isNotNull());
		chkUpdatable.setValue(commonAttribute.isUpdatable());
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {

		CommonAttribute commonAttribute = (CommonAttribute)record.getCommonAttribute();

		if (txtMultipl.getValue() != null) {
			commonAttribute.setMultipleStringValue(SmartGWTUtil.getStringValue(txtMultipl));
		}

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

		chkNotNull.setDisabled(false);
		chkUpdatable.setDisabled(false);
		txtMultipl.setDisabled(false);
		txtMultipl.setKeyPressFilter(KEYFILTER_NUM);

		switch (type) {
		case EXPRESSION:
			chkUpdatable.setDisabled(true);
			chkUpdatable.setValue(false);
			txtMultipl.setDisabled(true);
			txtMultipl.setValue(1);
			chkNotNull.setDisabled(true);
			chkNotNull.setValue(false);
			handler.onChangeNotNull(SmartGWTUtil.getBooleanValue(chkNotNull));
			break;
		case REFERENCE:
			txtMultipl.setKeyPressFilter(KEYFILTER_NUMANDASTER);
			break;
		case AUTONUMBER:
			chkUpdatable.setDisabled(true);
			chkUpdatable.setValue(false);
			txtMultipl.setDisabled(true);
			txtMultipl.setValue(1);
			chkNotNull.setDisabled(true);
			chkNotNull.setValue(false);
			handler.onChangeNotNull(SmartGWTUtil.getBooleanValue(chkNotNull));
			break;
		case BINARY:
			txtMultipl.setKeyPressFilter(KEYFILTER_NUM);
			txtMultipl.setValue(1);
			break;
		case LONGTEXT:
			txtMultipl.setValue(1);
			break;
		default:
			txtMultipl.setValue(1);
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

}
