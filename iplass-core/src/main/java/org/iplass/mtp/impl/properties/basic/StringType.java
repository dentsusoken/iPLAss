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
import org.iplass.mtp.entity.definition.properties.StringProperty;

public class StringType extends BasicType {
	//TODO カラムを2つ以上消費して、VARCHAR(8000)～の文字を定義可能とする
	
	private static final long serialVersionUID = -1150069680319011062L;
	private static final int hash = 24;
	
	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StringType) {
			return true;
		}
		return false;
	}

	@Override
	public StringProperty createPropertyDefinitionInstance() {
		return new StringProperty();
	}

	@Override
	public StringType copy() {
		return new StringType();
	}

	@Override
	public Class<?> storeType() {
		return String.class;
	}
	
	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.STRING;
	}

	@Override
	public Object fromString(String strValue) {
		return strValue;
	}
	
}
