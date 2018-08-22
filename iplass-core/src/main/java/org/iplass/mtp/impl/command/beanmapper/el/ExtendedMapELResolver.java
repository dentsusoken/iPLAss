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

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotWritableException;

import org.iplass.mtp.entity.GenericEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>カスタムのMapELResolver</p>
 * <p>
 * 標準提供のMapELResolver実装に対し、次の機能を拡張。
 * <ul>
 * <li>
 * autoGrow機能<br>
 * 対象のMapにkeyに対する値が存在しなかった場合、自動的にインスタンスを生成する。
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
public class ExtendedMapELResolver extends ELResolver {
	private static Logger log = LoggerFactory.getLogger(ExtendedMapELResolver.class);

	static private Class<?> unmodifiableMapClass = Collections.unmodifiableMap(new HashMap<Object, Object>()).getClass();
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base instanceof Map) {
			context.setPropertyResolved(base, property);
			@SuppressWarnings("rawtypes")
			Map map = (Map) base;
			
			Object value = map.get(property);
			if (value == null) {
				BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
				if (bmc.getElMapper().isAutoGrow()) {
					PropertyInfo pi = bmc.getPropertyRef(map).getPropertyInfo();
					
					try {
						switch (pi.getComponentTypeKind()) {
						case BEAN:
							value = pi.getComponentType().newInstance();
							break;
						case ENTITY:
							if (pi.getComponentType().isInterface()) {
								value = new GenericEntity();
							} else {
								value = pi.getComponentType().newInstance();
							}
							break;
						case ARRAY:
						case LIST:
						case MAP:
							if (log.isDebugEnabled()) {
								log.debug("Nested collection auto grow not supported.");
							}
						case SIMPLE:
							break;
						default:
							break;
						}
					} catch (ELException e) {
						throw e;
					} catch (Exception e) {
						throw new ELException(e);
					}
					
					if (value != null) {
						map.put(property, value);
					}
				}
			}
			
			return value;
		}
		
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base instanceof Map) {
			context.setPropertyResolved(true);
			return Object.class;
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base instanceof Map) {
			context.setPropertyResolved(base, property);
			@SuppressWarnings("rawtypes")
			Map map = (Map) base;
			if (map.getClass() == unmodifiableMapClass) {
				throw new PropertyNotWritableException();
			}
			try {
				
				if (value instanceof String) {
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
				
				map.put(property, value);
			} catch (UnsupportedOperationException e) {
				throw new PropertyNotWritableException();
			}
		}
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base instanceof Map) {
			context.setPropertyResolved(true);
			return base.getClass() == unmodifiableMapClass;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		if (base != null && base instanceof Map) {
			Map map = (Map) base;
			Iterator iter = map.keySet().iterator();
			List<FeatureDescriptor> list = new ArrayList<FeatureDescriptor>();
			while (iter.hasNext()) {
				Object key = iter.next();
				FeatureDescriptor descriptor = new FeatureDescriptor();
				String name = (key == null) ? null: key.toString();
				descriptor.setName(name);
				descriptor.setDisplayName(name);
				descriptor.setShortDescription("");
				descriptor.setExpert(false);
				descriptor.setHidden(false);
				descriptor.setPreferred(true);
				if (key != null) {
				    descriptor.setValue("type", key.getClass());
				}
				descriptor.setValue("resolvableAtDesignTime", Boolean.TRUE);
				list.add(descriptor);
			}
			return list.iterator();
		}
		
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (base != null && base instanceof Map) {
			return Object.class;
		}
		
		return null;
	}

}
