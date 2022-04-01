/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.tools.entity;

import java.io.Serializable;

public class UpdateAllValue  implements Serializable {

	public enum UpdateAllValueType {
		LITERAL,
		VALUE_EXPRESSION
	}

	private static final long serialVersionUID = 3525495160618587524L;

	private String propertyName;
	private String value;
	private UpdateAllValueType valueType;

	public UpdateAllValue() {}
	
	public UpdateAllValue(String propertyName, String value, UpdateAllValueType valueType) {
		this.propertyName = propertyName;
		this.value = value;
		this.valueType = valueType;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public UpdateAllValueType getValueType() {
		return valueType;
	}

	public void setValueType(UpdateAllValueType valueType) {
		this.valueType = valueType;
	}
}
