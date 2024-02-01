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

package org.iplass.mtp.entity.definition.properties;

import java.math.BigDecimal;

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;


/**
 * 固定小数点を表すプロパティ定義。
 * 
 * @author K.Higuchi
 *
 */
public class DecimalProperty extends PropertyDefinition {
	
	private static final long serialVersionUID = 5033289491264936255L;
	
	private int scale;
	private RoundingMode roundingMode = RoundingMode.HALF_EVEN;
	
	public DecimalProperty(){
	}
	
	public DecimalProperty(String name){
		setName(name);
	}
	
	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public RoundingMode getRoundingMode() {
		return roundingMode;
	}

	public void setRoundingMode(RoundingMode roundingMode) {
		this.roundingMode = roundingMode;
	}

	@Override
	public Class<?> getJavaType() {
		return BigDecimal.class;
	}
	
	@Override
	public PropertyDefinitionType getType() {
		return PropertyDefinitionType.DECIMAL;
	}

//	public DecimalProperty copy() {
//		DecimalProperty copy = new DecimalProperty();
//		copyTo(copy);
//		copy.precision = precision;
//		copy.scale = scale;
//		copy.roundingMode = roundingMode;
//		return copy;
//	}
}
