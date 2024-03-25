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

import jakarta.xml.bind.annotation.XmlSeeAlso;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.impl.entity.MetaEntity;
import org.iplass.mtp.impl.metadata.MetaData;
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


//TODO JAXBへ設定するクラス群は、メタデータの設定から取得する。
@XmlSeeAlso({DateTimeType.class, DateType.class, TimeType.class, FloatType.class, IntegerType.class,
				LongTextType.class, StringType.class, BinaryType.class, DecimalType.class,
				BooleanType.class, SelectType.class, ExpressionType.class, AutoNumberType.class
				})
public abstract class PropertyType implements MetaData {
	
	//TODO 複合型への対応
	
	private static final long serialVersionUID = 7289723395858447148L;

	public abstract PropertyDefinition createPropertyDefinitionInstance();
	
	public abstract void applyDefinition(PropertyDefinition def);

	public abstract PropertyType copy();
	
	//TODO メソッド名が紛らわしい
	//iPLAss上でのデータのクラス（SelectValue型だったらSelectValue）
	//getEnumType or getDataStoreEnumTypeを使う！
	@Deprecated
	public abstract Class<?> storeType();
	
	public abstract boolean isVirtual();
	
	public abstract Object toDataStore(Object toDataStore);
	
	public abstract Object fromDataStore(Object fromDataStore);

	public abstract Object createRuntime(MetaProperty metaProperty, MetaEntity metaEntity);
	
	public abstract boolean isCompatibleTo(PropertyType another);
	
	public abstract String toString(Object value);
	
	public abstract Object fromString(String strValue);
	
	public abstract PropertyDefinitionType getEnumType();
	
	public PropertyDefinitionType getDataStoreEnumType() {
		return getEnumType();
	};
	
	public Object formatToLog(Object val) {
		return val;
	}
	
}
