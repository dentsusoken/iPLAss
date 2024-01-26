/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.command.beanmapper.el;

import org.iplass.mtp.impl.command.beanmapper.el.PropertyInfo.TypeKind;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

class PropertyRef {
	private String propertyName;
	private PropertyInfo propertyInfo;
	private Object bean;
	private ReferencePropertyHandler rph;
	
	PropertyRef(Object bean, PropertyInfo propertyInfo) {
		this.bean = bean;
		this.propertyInfo = propertyInfo;
		this.propertyName = propertyInfo.getDescriptor().getName();
	}
	
	PropertyRef(Object bean, ReferencePropertyHandler rph) {
		this.bean = bean;
		this.propertyName = rph.getName();
		this.rph = rph;
	}
	
	public TypeKind getComponentTypeKind() {
		if (propertyInfo != null) {
			return propertyInfo.getComponentTypeKind();
		} else {
			return TypeKind.ENTITY;
		}
	}

	public String getPropertyName() {
		return propertyName;
	}

	public ReferencePropertyHandler getReferencePropertyHandler() {
		return rph;
	}

	public PropertyInfo getPropertyInfo() {
		return propertyInfo;
	}

	public Object getBean() {
		return bean;
	}

}
