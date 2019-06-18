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

import org.iplass.adminconsole.client.base.ui.widget.form.MtpForm;
import org.iplass.adminconsole.client.base.ui.widget.form.MtpTextItem;
import org.iplass.adminconsole.client.base.util.SmartGWTUtil;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord;
import org.iplass.adminconsole.client.metadata.ui.entity.property.ValidationListGridRecord.ValidationType;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class BinaryTypeAttributePane extends ValidationAttributePane {

	private DynamicForm form;

	private TextItem mimeTypePatternItem;

	public BinaryTypeAttributePane() {

		mimeTypePatternItem = new MtpTextItem();
		mimeTypePatternItem.setTitle("Pattern");

		form = new MtpForm();
		form.setItems(mimeTypePatternItem);

		addMember(form);
	}

	@Override
	public void setDefinition(ValidationListGridRecord record) {

		mimeTypePatternItem.setValue(record.getPtrn());
	}

	@Override
	public ValidationListGridRecord getEditDefinition(ValidationListGridRecord record) {

		record.setPtrn(SmartGWTUtil.getStringValue(mimeTypePatternItem, true));

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
		return ValidationType.BINARYTYPE;
	}

	@Override
	public int panelHeight() {
		return 30;
	}

}
