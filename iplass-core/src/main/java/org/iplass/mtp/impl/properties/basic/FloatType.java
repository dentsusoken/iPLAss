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
import org.iplass.mtp.entity.definition.properties.FloatProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloatType extends BasicType {
	private static final long serialVersionUID = 3314101651301305269L;
	private static final int hash = 22;
	
	private static Logger logger = LoggerFactory.getLogger(FloatType.class);

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FloatType) {
			return true;
		}
		return false;
	}
	
	@Override
	public FloatProperty createPropertyDefinitionInstance() {
		return new FloatProperty();
	}

	@Override
	public FloatType copy() {
		return new FloatType();
	}

	@Override
	public Class<?> storeType() {
		return Double.class;
	}
	
	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.FLOAT;
	}

	@Override
	public Object fromString(String strValue) {
		if (strValue == null) {
			return null;
		}
		try {
			return Double.valueOf(strValue);
		} catch (NumberFormatException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Can't parse to Float:" + strValue);
			}
			return null;
		}
	}
	
}
