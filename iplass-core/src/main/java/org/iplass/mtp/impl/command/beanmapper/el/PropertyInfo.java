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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;

class PropertyInfo {
	
	enum TypeKind {
		SIMPLE,
		BEAN,
		ENTITY,
		ARRAY,
		LIST,
		MAP
	}
	
	private Method readMethod;
	private Method writeMethod;
	private PropertyDescriptor descriptor;

	private TypeKind typeKind;
	private TypeKind componentTypeKind;

	private Class<?> propertyType;
	private Class<?> componentType;
	
	PropertyInfo(Class<?> baseClass, PropertyDescriptor descriptor) {
		this.descriptor = descriptor;
		readMethod = getMethod(baseClass, descriptor.getReadMethod());
		writeMethod = getMethod(baseClass, descriptor.getWriteMethod());
		propertyType = descriptor.getPropertyType();
		typeKind = typeOf(propertyType);
		if (typeKind != null) {
			switch (typeKind) {
			case ARRAY:
				componentType = propertyType.getComponentType();
				componentTypeKind = typeOf(componentType);
				break;
			case LIST:
				Type gt = readMethod.getGenericReturnType();
				if (gt instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) gt;
					Type t = pt.getActualTypeArguments()[0];
					if (t instanceof Class && !Object.class.equals(t)) {
						componentType = (Class<?>) t;
					} else if (t instanceof ParameterizedType) {
						componentType = ((ParameterizedType) t).getRawType().getClass();
					}
				}
				componentTypeKind = typeOf(componentType);
				break;
			case MAP:
				gt = readMethod.getGenericReturnType();
				if (gt instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) gt;
					Type t = pt.getActualTypeArguments()[1];
					if (t instanceof Class && !Object.class.equals(t)) {
						componentType = (Class<?>) t;
					} else if (t instanceof ParameterizedType) {
						componentType = ((ParameterizedType) t).getRawType().getClass();
					}
				}
				componentTypeKind = typeOf(componentType);
				break;
			default:
				break;
			}
		}
	}
	
	private TypeKind typeOf(Class<?> t) {
		if (t == null) {
			return null;
		}
		if (t.isArray()) {
			return TypeKind.ARRAY;
		} else if (List.class.isAssignableFrom(t)) {
			return TypeKind.LIST;
		} else if (Map.class.isAssignableFrom(t)) {
			return TypeKind.MAP;
		} else if (Entity.class.isAssignableFrom(t)) {
			return TypeKind.ENTITY;
		} else if (isSimpleType(t)) {
			return TypeKind.SIMPLE;
		} else {
			return TypeKind.BEAN;
		}
	}
	
	public PropertyDescriptor getDescriptor() {
		return descriptor;
	}
	
	public TypeKind getTypeKind() {
		return typeKind;
	}

	public TypeKind getComponentTypeKind() {
		return componentTypeKind;
	}

	private boolean isSimpleType(Class<?> propertyType) {
		if (String.class.equals(propertyType)) {
			return true;
		}
		if (propertyType.isPrimitive()) {
			return true;
		}
		if (Number.class.isAssignableFrom(propertyType)) {
			return true;
		}
		if (SelectValue.class.equals(propertyType)) {
			return true;
		}
		if (Boolean.class.equals(propertyType)) {
			return true;
		}
		if (propertyType.isEnum()) {
			return true;
		}
		if (Timestamp.class.equals(propertyType)) {
			return true;
		}
		if (Date.class.equals(propertyType)) {
			return true;
		}
		if (Time.class.equals(propertyType)) {
			return true;
		}
		if (BinaryReference.class.equals(propertyType)) {
			return true;
		}
		if (Character.class.equals(propertyType)) {
			return true;
		}
		return false;
	}
	
	public Class<?> getComponentType() {
		return componentType;
	}

	public Class<?> getPropertyType() {
	    return descriptor.getPropertyType();
	}
	
	public boolean isReadOnly() {
	    return getWriteMethod() == null;
	}
	
	public Method getReadMethod() {
	    return readMethod;
	}
	
	public Method getWriteMethod() {
	    return writeMethod;
	}
	
	//参考:ELUtil
	//publicなメソッドを取得する
	static Method getMethod(Class<?> type, Method m) {
		if (m == null || Modifier.isPublic(type.getModifiers())) {
			return m;
		}
		
		Class<?>[] inf = type.getInterfaces();
		Method mp = null;
		for (int i = 0; i < inf.length; i++) {
			try {
				mp = inf[i].getMethod(m.getName(), m.getParameterTypes());
				mp = getMethod(mp.getDeclaringClass(), mp);
				if (mp != null) {
					return mp;
				}
			} catch (NoSuchMethodException e) {
			}
		}
		
		Class<?> sup = type.getSuperclass();
		if (sup != null) {
			try {
				mp = sup.getMethod(m.getName(), m.getParameterTypes());
				mp = getMethod(mp.getDeclaringClass(), mp);
				if (mp != null) {
					return mp;
				}
			} catch (NoSuchMethodException e) {
			}
		}
		return null;
	}
	
}
