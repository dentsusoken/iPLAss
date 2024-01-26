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

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;

/**
 * <p>カスタムのBeanELResolver</p>
 * <p>
 * 標準提供のBeanELResolver実装に対し、次の機能を拡張。
 * <ul>
 * <li>
 * autoGrow機能<br>
 * 対象のネストされたBean、List、配列、Mapがnullの場合、自動的にインスタンスを生成する。
 * </li>
 * <li>
 * Entityへの対応<br>
 * まずBeanとしてpropertyを取得し、もしプロパティがなかった場合、
 * それがEntityであったらEntityのgetValue/setValueを利用する。
 * </li>
 * <li>
 * 非公開とするプロパティを設定可能に<br>
 * デフォルトで、class、metaClassをEL式から非公開に。
 * </li>
 * <li>
 * 空文字をnullにセットする対応<br>
 * EL3.0の仕様上、Stringにnullをセットすることできないので、ここでカスタム。
 * </li>
 * </ul>
 * </p>
 * 
 * @author K.Higuchi
 *
 */
public class ExtendedBeanELResolver extends ELResolver {

	private static Set<String> DEFAULT_SUPPRESS_PROPERTIES;
	static {
		HashSet<String> set = new HashSet<>();
		set.add("class");
		set.add("metaClass");
		DEFAULT_SUPPRESS_PROPERTIES = Collections.unmodifiableSet(set);
	}
	
	private Set<String> suppressProperties;
	
	private EntityELResolver entityResolver = new EntityELResolver();
	
	public ExtendedBeanELResolver() {
		this(DEFAULT_SUPPRESS_PROPERTIES);
	}

	public ExtendedBeanELResolver(Set<String> suppressProperties) {
		if (suppressProperties == null) {
			this.suppressProperties = Collections.emptySet();
		} else {
			this.suppressProperties = suppressProperties;
		}
	}
	
	private void checkSuppress(Object property) {
		if (property != null && suppressProperties.contains(property.toString())) {
		    throw new PropertyNotFoundException("The property:" + property.toString() + " is defined as must suppressed.");
		}
	}
	
    @Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (base == null || property == null) {
			return null;
		}
		
		checkSuppress(property);
		
		PropertyInfo pi = BeanInfo.getPropertyInfo(base.getClass(), property.toString());
		if (pi != null) {
			Method method = pi.getReadMethod();
			if (method != null) {
				Object value;
				try {
					value = method.invoke(base, new Object[0]);
					context.setPropertyResolved(base, property);
					
					BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
					if (bmc.getElMapper().isAutoGrow()) {
						if (value == null) {
							value = newPropertyInstance(pi);
							if (value != null) {
								setValue(pi, base, value);
							}
						}
						
						bmc.setPropertyRef(base, pi, value);
					}
				} catch (ELException e) {
					throw e;
				} catch (InvocationTargetException e) {
					throw new ELException(e.getCause());
				} catch (Exception e) {
					throw new ELException(e);
				}
				
				return value;
			}
		}
		
		if (base instanceof Entity) {
			Object value = entityResolver.getValue(context, base, property);
			if (context.isPropertyResolved()) {
				return value;
			}
		}
		
	    throw new PropertyNotFoundException("The class:" + base.getClass().getName() + " does not have a readable property:" + property.toString());
	}
    
	private Object newPropertyInstance(PropertyInfo propertyInfo) throws InstantiationException, IllegalAccessException {
		switch (propertyInfo.getTypeKind()) {
		case BEAN:
			return propertyInfo.getPropertyType().newInstance();
		case ENTITY:
			if (propertyInfo.getPropertyType().isInterface()) {
				return new GenericEntity();
			} else {
				return propertyInfo.getPropertyType().newInstance();
			}
		case ARRAY:
			return Array.newInstance(propertyInfo.getComponentType(), 0);
		case LIST:
			//TODO LinkedList?
			return new ArrayList<>();
		case MAP:
			return new HashMap<>();
		case SIMPLE:
		default:
			return null;
		}
	}
	
	private void setValue(PropertyInfo pi, Object bean, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = pi.getWriteMethod();
		if (method == null) {
			throw new PropertyNotWritableException("The class:" + bean.getClass().getName() + " does not have a writable property:" + pi.getDescriptor().getName());
		}
		method.invoke(bean, new Object[] {value});
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (base == null || property == null) {
			return null;
		}
		
		checkSuppress(property);
		
		PropertyInfo pi = BeanInfo.getPropertyInfo(base.getClass(), property.toString());
		if (pi != null) {
			context.setPropertyResolved(true);
			return pi.getPropertyType();
		}
		
		if (base instanceof Entity) {
			Class<?> type = entityResolver.getType(context, base, property);
			if (context.isPropertyResolved()) {
				return type;
			}
		}
		
		throw new PropertyNotFoundException("The class:" + base.getClass().getName() + " does not have the property:" + property.toString());
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (base == null || property == null) {
			return;
		}
		
		checkSuppress(property);
		
		if (value instanceof String) {
			//EL3.0の仕様上、nullをセットしようとしても、Stringの場合は空文字をセットしようとするのをnullをセットするようにする。
			//EL3.0の仕様上、nullをセットしようとしても、Stringの場合は空文字をセットしようとするのをnullをセットするようにする。
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			if (bmc.getElMapper().isTrim()) {
				value = ((String) value).trim();
			}
			if (bmc.getElMapper().isEmptyToNull()) {
				if (((String) value).isEmpty()) {
					value = null;
				}
			}
		}
		
		PropertyInfo pi = BeanInfo.getPropertyInfo(base.getClass(), property.toString());
		if (pi != null) {
			try {
				setValue(pi, base, value);
				context.setPropertyResolved(base, property);
				return;
			} catch (ELException e) {
				throw e;
			} catch (InvocationTargetException e) {
				throw new ELException(e.getCause());
			} catch (Exception e) {
				if (null == value) {
					value = "null";
				}
				throw new ELException("Can't set property:" +  property.toString() + " on class:" + base.getClass().getName() + " to value:" + value, e);
			}
		}
		
		if (base instanceof Entity) {
			entityResolver.setValue(context, base, property, value);
			if (context.isPropertyResolved()) {
				return;
			}
		}
		
		throw new PropertyNotFoundException("The class:" + base.getClass().getName() + " does not have the property:" + property.toString());
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		if (base == null || property == null) {
			return false;
		}
		
		checkSuppress(property);
		
		context.setPropertyResolved(true);
		PropertyInfo pi = BeanInfo.getPropertyInfo(base.getClass(), property.toString());
		if (pi != null) {
			return pi.isReadOnly();
		}
		
		if (base instanceof Entity) {
			return false;
		}
		
		throw new PropertyNotFoundException("The class:" + base.getClass().getName() + " does not have the property:" + property.toString());
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		if (base == null) {
			return null;
		}
		
		BeanInfo info = BeanInfo.getBeanInfo(base.getClass());
		if (info == null) {
			return null;
		}
		ArrayList<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();
		for (PropertyInfo pi: info.getProperties()) {
			PropertyDescriptor pd = pi.getDescriptor();
			pd.setValue("type", pd.getPropertyType());
			pd.setValue("resolvableAtDesignTime", Boolean.TRUE);
			list.add(pd);
		}
		return list.iterator();
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (base == null) {
			return null;
		}
		
		return Object.class;
	}

}
