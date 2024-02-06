/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.properties.basic;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.BooleanProperty;

public class BooleanType extends BasicType {
	private static final long serialVersionUID = 2354914642251405724L;
	private static final int hash = 18;
	
	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BooleanType) {
			return true;
		}
		return false;
	}

	@Override
	public BooleanType copy() {
		return new BooleanType();
	}

	@Override
	public BooleanProperty createPropertyDefinitionInstance() {
		return new BooleanProperty();
	}

	@Override
	public Class<?> storeType() {
		return Boolean.class;
	}
	
	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.BOOLEAN;
	}

	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		} else {
			Boolean valBool = (Boolean) value;
			if (valBool.booleanValue()) {
				return "1";
			} else {
				return "0";
			}
		}
	}

	@Override
	public Object fromString(String strValue) {
		if (strValue == null) {
			return null;
		}
		if (strValue.equals("1")) {
			return true;
		}
		return Boolean.valueOf(strValue);
	}

}
