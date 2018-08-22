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

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.DateProperty;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateType extends BasicType {
	private static final long serialVersionUID = -273797137918846755L;
	private static final int hash = 20;

	private static Logger logger = LoggerFactory.getLogger(DateType.class);
	
	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateType) {
			return true;
		}
		return false;
	}

	@Override
	public DateProperty createPropertyDefinitionInstance() {
		return new DateProperty();
	}

	@Override
	public DateType copy() {
		return new DateType();
	}

	@Override
	public Class<?> storeType() {
		return Date.class;
	}

	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.DATE;
	}

	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		} else {
			if (DateTimeType.OLD_FORMAT_FLAG) {
				return DateUtil.getDateInstance(DateFormat.MEDIUM, false).format(value);
			} else {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(Literal.DATE_FROMAT, false);
				return sdf.format(value);
			}
		}
	}

	@Override
	public Object fromString(String strValue) {
		if (strValue == null) {
			return null;
		}
		try {
			if (DateTimeType.OLD_FORMAT_FLAG) {
				return new Date(DateUtil.getDateInstance(DateFormat.MEDIUM, false).parse(strValue).getTime());
			} else {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(Literal.DATE_FROMAT, false);
				return new Date(sdf.parse(strValue).getTime());
			}
		} catch (ParseException e) {
			try {
				return ConvertUtil.convertFromString(Date.class, strValue);
			} catch (RuntimeException ee) {
				if (logger.isDebugEnabled()) {
					logger.debug("Can't parse to Date:" + strValue);
				}
				return null;
			}
		}
	}

}
