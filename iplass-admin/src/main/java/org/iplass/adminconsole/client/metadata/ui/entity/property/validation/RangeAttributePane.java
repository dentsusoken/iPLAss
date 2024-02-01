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

public class RangeAttributePane extends ValidationAttributePane {

	private DynamicForm form;

	private TextItem minItem;
	private CheckboxItem minExcludeItem;
	private TextItem maxItem;
	private CheckboxItem maxExcludeItem;

	public RangeAttributePane() {

		minItem = new MtpTextItem("minValue");
		minItem.setTitle("Min");
		minItem.setKeyPressFilter("[0-9]");

		minExcludeItem = new CheckboxItem();
		minExcludeItem.setTitle("grater than min value");
		SmartGWTUtil.addHoverToFormItem(minExcludeItem, rs("ui_metadata_entity_PropertyListGrid_minExclude"));

		maxItem = new MtpTextItem("maxValue");
		maxItem.setTitle("Max");
		maxItem.setKeyPressFilter("[0-9]");

		maxExcludeItem = new CheckboxItem();
		maxExcludeItem.setTitle("less than max value");
		SmartGWTUtil.addHoverToFormItem(maxExcludeItem, rs("ui_metadata_entity_PropertyListGrid_maxExclude"));

		form = new MtpForm();
		form.setItems(minItem, minExcludeItem, maxItem, maxExcludeItem);

		addMember(form);
	}

	@Override
	public void setDefinition(ValidationListGridRecord record) {

		maxItem.setValue(record.getMax());
		maxExcludeItem.setValue(record.isMaxValueExcluded());
		minItem.setValue(record.getMin());
		minExcludeItem.setValue(record.isMinValueExcluded());
	}

	@Override
	public ValidationListGridRecord getEditDefinition(ValidationListGridRecord record) {

		String minValue = SmartGWTUtil.getStringValue(minItem, true);
		record.setMin(minValue);
		if (minValue == null) {
			//未指定だったら除外ON
			minExcludeItem.setValue(true);
		}
		record.setMinValueExcluded(SmartGWTUtil.getBooleanValue(minExcludeItem));

		String maxValue = SmartGWTUtil.getStringValue(maxItem, true);
		record.setMax(maxValue);
		if (maxValue == null) {
			//未指定だったら除外ON
			maxExcludeItem.setValue(true);
		}
		record.setMaxValueExcluded(SmartGWTUtil.getBooleanValue(maxExcludeItem));

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
		return ValidationType.RANGE;
	}

	@Override
	public int panelHeight() {
		return 120;
	}

}
