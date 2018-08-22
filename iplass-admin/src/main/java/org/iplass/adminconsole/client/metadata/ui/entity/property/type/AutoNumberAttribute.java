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

package org.iplass.adminconsole.client.metadata.ui.entity.property.type;

import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyAttribute;
import org.iplass.adminconsole.client.metadata.ui.entity.property.PropertyListGridRecord;
import org.iplass.mtp.entity.definition.EntityDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.properties.AutoNumberProperty;
import org.iplass.mtp.entity.definition.properties.NumberingType;

public class AutoNumberAttribute implements PropertyAttribute {

	private AutoNumberProperty attribute;

	public AutoNumberAttribute() {

		attribute = new AutoNumberProperty();
		setAutoNumberStartWith(Long.valueOf(0));
		setAutoNumberFixedNumber(0);
		setAutoNumberNumberingType(NumberingType.ALLOW_SKIPPING);
	}

	@Override
	public void applyFrom(PropertyDefinition property, EntityDefinition entity) {

		AutoNumberProperty autoNumber = (AutoNumberProperty)property;

		setAutoNumberStartWith(autoNumber.getStartsWith());
		setAutoNumberFixedNumber(autoNumber.getFixedNumberOfDigits());
		setAutoNumberFormatScript(autoNumber.getFormatScript());
		setAutoNumberNumberingType(autoNumber.getNumberingType());
	}

	@Override
	public void applyTo(PropertyDefinition property, EntityDefinition entity) {

		AutoNumberProperty autoNumber = (AutoNumberProperty)property;

		autoNumber.setStartsWith(getAutoNumberStartWith());
		autoNumber.setFixedNumberOfDigits(getAutoNumberFixedNumber());
		autoNumber.setFormatScript(getAutoNumberFormatScript());
		autoNumber.setNumberingType(getAutoNumberNumberingType());
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {
		//一覧に書式を表示させる
		if (attribute.getFormatScript() != null) {
			record.setRemarks("format:" + attribute.getFormatScript());
		} else {
			record.setRemarks("format:");
		}
	}

	public Long getAutoNumberStartWith() {
		return attribute.getStartsWith();
	}

	public void setAutoNumberStartWith(Long value) {
		attribute.setStartsWith(value);
	}

	public Integer getAutoNumberFixedNumber() {
		return attribute.getFixedNumberOfDigits();
	}

	public void setAutoNumberFixedNumber(Integer value) {
		attribute.setFixedNumberOfDigits(value);
	}

	public NumberingType getAutoNumberNumberingType() {
		return attribute.getNumberingType();
	}

	public void setAutoNumberNumberingType(NumberingType value) {
		attribute.setNumberingType(value);
	}

	public String getAutoNumberFormatScript() {
		return attribute.getFormatScript();
	}

	public void setAutoNumberFormatScript(String value) {
		attribute.setFormatScript(value);
	}

}
