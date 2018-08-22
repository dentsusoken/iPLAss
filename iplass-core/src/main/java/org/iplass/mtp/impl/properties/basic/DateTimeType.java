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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.definition.properties.DateTimeProperty;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTimeType extends BasicType {
	private static final long serialVersionUID = 3911004797303862636L;
	private static final int hash = 19;
	
	private static Logger logger = LoggerFactory.getLogger(DateTimeType.class);
	
	//FIXME 暫定対応。toStringを古いフォーマットで出力するか否か
	static boolean OLD_FORMAT_FLAG = false;
	static {
		String flagStr = System.getProperty("mtp.property.oldformat");
		if (flagStr != null && flagStr.equalsIgnoreCase("TRUE")) {
			OLD_FORMAT_FLAG = true;
		}
	}
	

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DateTimeType) {
			return true;
		}
		return false;
	}

	@Override
	public DateTimeProperty createPropertyDefinitionInstance() {
		return new DateTimeProperty();
	}

	@Override
	public DateTimeType copy() {
		return new DateTimeType();
	}

	@Override
	public Class<?> storeType() {
		return Timestamp.class;
	}

	@Override
	public PropertyDefinitionType getEnumType() {
		return PropertyDefinitionType.DATETIME;
	}

	@Override
	public String toString(Object value) {
		if (value == null) {
			return null;
		} else {
			if (OLD_FORMAT_FLAG) {
				return DateUtil.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, true).format(value);
			} else {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(Literal.DATE_TIME_FROMAT, true);
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
			if (OLD_FORMAT_FLAG) {
				return new Timestamp(DateUtil.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, true).parse(strValue).getTime());
			} else {
				SimpleDateFormat sdf = DateUtil.getSimpleDateFormat(Literal.DATE_TIME_FROMAT, true);
				return new Timestamp(sdf.parse(strValue).getTime());
			}
		} catch (ParseException e) {
			try {
				return ConvertUtil.convertFromString(Timestamp.class, strValue);
			} catch (RuntimeException ee) {
				if (logger.isDebugEnabled()) {
					logger.debug("Can't parse to DateTime:" + strValue);
				}
				return null;
			}
		}
	}


}
