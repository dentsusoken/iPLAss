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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;

import javax.el.BeanNameELResolver;
import javax.el.BeanNameResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.PropertyNotWritableException;
import javax.el.VariableMapper;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

public class BeanMapperELContext extends ELContext {
	static final String ROOT_NAME = "R";
	
	private ELResolver elResolver;
	private Object bean;
	private ELMapper elMapper;
	
	private IdentityHashMap<Object, PropertyRef> propertyRefs;
	
	public BeanMapperELContext(Object bean, ELMapper elMapper) {
		this.bean = bean;
		this.elMapper = elMapper;
		putContext(BeanMapperELContext.class, this);
	}
	
	PropertyRef getPropertyRef(Object propValue) {
		if (propertyRefs == null) {
			return null;
		}
		return propertyRefs.get(propValue);
	}
	
	void setPropertyRef(Object bean, PropertyInfo propertyInfo, Object propValue) {
		switch (propertyInfo.getTypeKind()) {
		case ARRAY:
		case LIST:
		case MAP:
			if (propertyRefs == null) {
				propertyRefs = new IdentityHashMap<>();
			}
			propertyRefs.put(propValue, new PropertyRef(bean, propertyInfo));
			break;
		default:
			break;
		}
	}
	
	void setPropertyRef(Object bean, ReferencePropertyHandler rph, Object propValue) {
		if (rph.getMetaData().getMultiplicity() != 1) {
			if (propertyRefs == null) {
				propertyRefs = new IdentityHashMap<>();
			}
			propertyRefs.put(propValue, new PropertyRef(bean, rph));
		}
	}
	
	void replacePropertyRef(Object oldPropValue, Object newPropValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (propertyRefs == null) {
			return;
		}
		PropertyRef propertyRef = propertyRefs.remove(oldPropValue);
		propertyRefs.put(newPropValue, propertyRef);
		if (propertyRef.getPropertyInfo() != null) {
			Method setter = propertyRef.getPropertyInfo().getWriteMethod();
			if (setter != null) {
				setter.invoke(propertyRef.getBean(), new Object[] {newPropValue});
			} else if (propertyRef.getBean() instanceof Entity) {
				((Entity) propertyRef.getBean()).setValue(propertyRef.getPropertyName(), newPropValue);
			} else {
				throw new PropertyNotWritableException("The class:" + propertyRef.getBean().getClass().getName() + " does not have a writable property:" + propertyRef.getPropertyName());
			}
		} else {
			//Entity
			((Entity) propertyRef.getBean()).setValue(propertyRef.getPropertyName(), newPropValue);
		}
	}
	
	public Object getBean() {
		return bean;
	}

	public ELMapper getElMapper() {
		return elMapper;
	}

	@Override
	public ELResolver getELResolver() {
		if (elResolver == null) {
			CompositeELResolver resolver = new CompositeELResolver();
			
			//type converter
			resolver.add(new BinaryReferenceConverter());
			resolver.add(new SelectValueConverter());
			resolver.add(new SqlDateConverter());
			resolver.add(new TimeConverter());
			resolver.add(new TimestampConverter());
			resolver.add(new ArrayTypeConverter());
			
			resolver.add(new BeanNameELResolver(new LocalBeanNameResolver()));
			resolver.add(new ExtendedMapELResolver());
			resolver.add(new ExtendedListELResolver());
			resolver.add(new ExtendedArrayELResolver());
			resolver.add(new ExtendedBeanELResolver());
			elResolver = resolver;
		}
		return elResolver;
	}

	@Override
	public FunctionMapper getFunctionMapper() {
		return null;
	}

	@Override
	public VariableMapper getVariableMapper() {
		return null;
	}
	
	private class LocalBeanNameResolver extends BeanNameResolver {
		@Override
		public boolean isNameResolved(String beanName) {
			return ROOT_NAME.equals(beanName);
		}

		@Override
		public Object getBean(String beanName) {
			if (isNameResolved(beanName)) {
				return bean;
			}
			return null;
		}

		@Override
		public void setBeanValue(String beanName, Object value) {
		}

		@Override
		public boolean isReadOnly(String beanName) {
			return false;
		}

		@Override
		public boolean canCreateBean(String beanName) {
			return false;
		}
	}

	@Override
	public Object convertToType(Object obj, Class<?> targetType) {
		
		//trim and toNull
		//targetTypeがStringの場合はここで変換しても意味ないので
		if (obj instanceof String && targetType != String.class) {
			if (elMapper.isTrim()) {
				obj = ((String) obj).trim();
			}
			if (elMapper.isEmptyToNull()) {
				if (((String) obj).isEmpty()) {
					obj = null;
				}
			}
		}
		
		return super.convertToType(obj, targetType);
	}
	

}
