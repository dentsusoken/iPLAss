/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.sql.Time;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.TimeProperty;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TimeType extends BasicType {
	private static final long serialVersionUID = -476913710631610881L;
	private static final int hash = 26;
	
	private static Logger logger = LoggerFactory.getLogger(TimeType.class);
	
	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TimeType) {
			return true;
		}
		return false;
	}

	@Override
	public TimeProperty createPropertyDefinitionInstance() {
		return new TimeProperty();
	}

	@Override
	public TimeType copy() {
		return new TimeType();
	}

	@Override
	public Class<?> storeType() {
		return Time.class;
	}
	
	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.TIME;
	}

	@Override
	public Object fromString(String strValue) {
		if (strValue == null) {
			return null;
		}
		try {
			return Time.valueOf(strValue);
		} catch (IllegalArgumentException e) {
			try {
				return ConvertUtil.convertFromString(Time.class, strValue);
			} catch (RuntimeException ee) {
				//TODO null or Exception?
				if (logger.isDebugEnabled()) {
					logger.debug("Can't parse to Time:" + strValue);
				}
				return null;
			}
		}
	}

}
