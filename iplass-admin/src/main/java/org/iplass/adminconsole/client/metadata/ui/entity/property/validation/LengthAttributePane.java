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

package org.iplass.adminconsole.client.metadata.ui.entity.property.validation;

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord.ValidationType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class LengthAttributePane extends ValidationAttributePane {

	private DynamicForm form;

	private TextItem minItem;
	private TextItem maxItem;

	private CheckboxItem byteCheckItem;
	private CheckboxItem surrogatePairAsOneCharItem;

	public LengthAttributePane() {

		minItem = new MtpTextItem("minValue");
		minItem.setTitle("Min");
		minItem.setKeyPressFilter("[0-9]");

		maxItem = new MtpTextItem("maxValue");
		maxItem.setTitle("Max");
		maxItem.setKeyPressFilter("[0-9]");

		byteCheckItem = new CheckboxItem();
		byteCheckItem.setTitle("check byte");

		surrogatePairAsOneCharItem = new CheckboxItem();
		surrogatePairAsOneCharItem.setTitle("surrogate pair as one char");

		form = new MtpForm();
		form.setItems(minItem, maxItem, byteCheckItem, surrogatePairAsOneCharItem);

		addMember(form);
	}

	@Override
	public void setDefinition(ValidationListGridRecord record) {

		maxItem.setValue(record.getMax());
		minItem.setValue(record.getMin());

		byteCheckItem.setValue(record.isByteLengthCheck());
		surrogatePairAsOneCharItem.setValue(record.isSurrogatePairAsOneChar());
	}

	@Override
	public ValidationListGridRecord getEditDefinition(ValidationListGridRecord record) {

		record.setMin(SmartGWTUtil.getStringValue(minItem, true));
		record.setMax(SmartGWTUtil.getStringValue(maxItem, true));
		record.setByteLengthCheck(SmartGWTUtil.getBooleanValue(byteCheckItem));
		record.setSurrogatePairAsOneChar(SmartGWTUtil.getBooleanValue(surrogatePairAsOneCharItem));

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
		return ValidationType.LENGTH;
	}

	@Override
	public int panelHeight() {
		return 120;
	}

}
