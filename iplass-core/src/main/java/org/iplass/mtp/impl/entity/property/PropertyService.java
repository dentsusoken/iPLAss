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

package org.iplass.mtp.impl.entity.property;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.properties.basic.BooleanType;
import org.iplass.mtp.impl.properties.basic.DateTimeType;
import org.iplass.mtp.impl.properties.basic.DateType;
import org.iplass.mtp.impl.properties.basic.DecimalType;
import org.iplass.mtp.impl.properties.basic.FloatType;
import org.iplass.mtp.impl.properties.basic.IntegerType;
import org.iplass.mtp.impl.properties.basic.StringType;
import org.iplass.mtp.impl.properties.basic.TimeType;
import org.iplass.mtp.impl.properties.extend.AutoNumberType;
import org.iplass.mtp.impl.properties.extend.BinaryType;
import org.iplass.mtp.impl.properties.extend.ExpressionType;
import org.iplass.mtp.impl.properties.extend.LongTextType;
import org.iplass.mtp.impl.properties.extend.SelectType;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.Service;


public class PropertyService implements Service {

	//TODO MetaDataRepositoryに登録されているプロパティのリストを取得。
	// PropertyTypeと、RdbMapping、Definitionの関係が設定されているはず。

	//TODO OracleとMySQLでは、適切な値が変わってくる
	private int longTextInlineStoreMaxLength = 1024;
	
	private boolean remainInlineText = false;


	private Map<Class<?>, PropertyType> map;
	private EnumMap<PropertyDefinitionType, PropertyType> enumMap;

	//TODO いたるところで型のマッピングが行われている、整理が必要

	public PropertyService() {
	}

	public int getLongTextInlineStoreMaxLength() {
		return longTextInlineStoreMaxLength;
	}

	public boolean isRemainInlineText() {
		return remainInlineText;
	}

	public PropertyType newPropertyType(PropertyDefinition pDef) {
		PropertyType pt = null;
		
		switch (pDef.getType()) {
		case AUTONUMBER:
			pt = new AutoNumberType();
			break;
		case BINARY:
			pt = new BinaryType();
			break;
		case BOOLEAN:
			pt = new BooleanType();
			break;
		case DATE:
			pt = new DateType();
			break;
		case DATETIME:
			pt = new DateTimeType();
			break;
		case DECIMAL:
			pt = new DecimalType();
			break;
		case EXPRESSION:
			pt = new ExpressionType();
			break;
		case FLOAT:
			pt = new FloatType();
			break;
		case INTEGER:
			pt = new IntegerType();
			break;
		case LONGTEXT:
			pt = new LongTextType();
			break;
		case REFERENCE:
			throw new IllegalArgumentException();
		case SELECT:
			pt = new SelectType();
			break;
		case STRING:
			pt = new StringType();
			break;
		case TIME:
			pt = new TimeType();
			break;
		default:
			throw new IllegalArgumentException();
		}
		
		pt.applyDefinition(pDef);
		return pt;
	}

	public PropertyType getPropertyType(Class<?> type) {
		return map.get(type);
	}

	public void destroy() {
	}

	public void init(Config config) {

		String longTextInlineStoreMaxLengthString = config.getValue("longTextInlineStoreMaxLength");
		if (longTextInlineStoreMaxLengthString != null) {
			longTextInlineStoreMaxLength = Integer.parseInt(longTextInlineStoreMaxLengthString);
		}
		
		if (config.getValue("remainInlineText") != null) {
			remainInlineText = Boolean.valueOf(config.getValue("remainInlineText"));
		}

		map = new HashMap<Class<?>, PropertyType>();
		enumMap = new EnumMap<>(PropertyDefinitionType.class);

		//TODO 設定ファイルより取得

		map.put(BinaryReference.class, new BinaryType());
		map.put(Boolean.class, new BooleanType());
		map.put(java.sql.Timestamp.class, new DateTimeType());
		map.put(java.sql.Date.class, new DateType());
		map.put(java.sql.Time.class, new TimeType());
		map.put(BigDecimal.class, new DecimalType(Integer.MIN_VALUE, null));
		map.put(Double.class, new FloatType());
		map.put(Long.class, new IntegerType());
		map.put(String.class, new StringType());
		map.put(SelectValue.class, new SelectType());
		
		enumMap.put(PropertyDefinitionType.BINARY, new BinaryType());
		enumMap.put(PropertyDefinitionType.BOOLEAN, new BooleanType());
		enumMap.put(PropertyDefinitionType.DATETIME, new DateTimeType());
		enumMap.put(PropertyDefinitionType.DATE, new DateType());
		enumMap.put(PropertyDefinitionType.TIME, new TimeType());
		enumMap.put(PropertyDefinitionType.DECIMAL, new DecimalType(Integer.MIN_VALUE, null));
		enumMap.put(PropertyDefinitionType.FLOAT, new FloatType());
		enumMap.put(PropertyDefinitionType.INTEGER, new IntegerType());
		enumMap.put(PropertyDefinitionType.STRING, new StringType());
		enumMap.put(PropertyDefinitionType.SELECT, new SelectType());
	}

	public PropertyType getPropertyType(PropertyDefinitionType et) {
		return enumMap.get(et);
	}
}
