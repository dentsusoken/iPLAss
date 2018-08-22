/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.el.ELException;

class BeanInfo {
	private static SoftConcurrentHashMap<Class<?>, BeanInfo> biMap = new SoftConcurrentHashMap<>(256);
	
	static BeanInfo getBeanInfo(Class<?> beanClass) {
		BeanInfo bi = biMap.get(beanClass);
		if (bi == null) {
			bi = new BeanInfo(beanClass);
			biMap.put(beanClass, bi);
		}
		return bi;
	}
	
	static PropertyInfo getPropertyInfo(Class<?> beanClass, String propName) {
		BeanInfo bi = getBeanInfo(beanClass);
		return bi.getProperty(propName);
	}
	
	private final Map<String, PropertyInfo> propertyMap = new HashMap<String, PropertyInfo>();
	
	BeanInfo(Class<?> beanClass) {
		PropertyDescriptor[] descriptors;
		try {
			descriptors = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
		} catch (IntrospectionException ie) {
			throw new ELException(ie);
		}
		for (PropertyDescriptor pd: descriptors) {
			PropertyInfo pi = new PropertyInfo(beanClass, pd);
			if (pi.getTypeKind() != null) {
				//型を判断できないプロパティは対象外にする
				propertyMap.put(pd.getName(), pi);
			}
		}
	}
	
	public Collection<PropertyInfo> getProperties() {
		return propertyMap.values();
	}
	
	public PropertyInfo getProperty(String property) {
		return propertyMap.get(property);
	}
}
