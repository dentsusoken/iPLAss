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

import org.iplass.mtp.entity.definition.PropertyDefinition;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;

/**
 * 浮動小数点を表すプロパティ定義。
 * 
 * @author K.Higuchi
 *
 */
public class FloatProperty extends PropertyDefinition {
	private static final long serialVersionUID = 7810046438255422616L;

	public FloatProperty() {
	}
	
	public FloatProperty(String name) {
		setName(name);
	}

	@Override
	public Class<?> getJavaType() {
		return Double.class;
	}
	
	@Override
	public PropertyDefinitionType getType() {
		return PropertyDefinitionType.FLOAT;
	}

//	public FloatProperty copy() {
//		FloatProperty copy = new FloatProperty();
//		copyTo(copy);
//		return copy;
//	}

}
