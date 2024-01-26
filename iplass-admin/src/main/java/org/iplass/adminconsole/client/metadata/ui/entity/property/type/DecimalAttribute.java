/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
import org.iplass.mtp.entity.definition.properties.DecimalProperty;
import org.iplass.mtp.entity.definition.properties.RoundingMode;

public class DecimalAttribute implements PropertyAttribute {

	private DecimalProperty attribute;

	public DecimalAttribute() {

		attribute = new DecimalProperty();
		setScale(0);
		setRoundingMode(RoundingMode.HALF_EVEN);
	}

	@Override
	public void applyFrom(PropertyDefinition property, EntityDefinition entity) {

		DecimalProperty decimal = (DecimalProperty)property;

		setScale(decimal.getScale());
		setRoundingMode(decimal.getRoundingMode());
	}

	@Override
	public void applyTo(PropertyDefinition property, EntityDefinition entity) {

		DecimalProperty decimal = (DecimalProperty)property;

		decimal.setScale(getScale());
		decimal.setRoundingMode(getRoundingMode());
	}

	@Override
	public void applyTo(PropertyListGridRecord record) {
		record.setRemarks("scale:" + getScale() + " , Mode:" + getRoundingMode());
	}

	public int getScale() {
		return attribute.getScale();
	}

	public void setScale(int value) {
		attribute.setScale(value);
	}

	public RoundingMode getRoundingMode() {
		return attribute.getRoundingMode();
	}

	public void setRoundingMode(RoundingMode value) {
		attribute.setRoundingMode(value);
	}

}
