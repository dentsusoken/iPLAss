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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jakarta.el.ELContext;
import jakarta.el.ELException;
import jakarta.el.ELResolver;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.PropertyNotWritableException;

import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.impl.command.beanmapper.el.PropertyInfo.TypeKind;
import org.iplass.mtp.impl.entity.EntityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>カスタムのListELResolver</p>
 * <p>
 * 標準提供のListELResolver実装に対し、次の機能を拡張。
 * <ul>
 * <li>
 * autoGrow機能<br>
 * 対象のListのサイズが指定のindexより小さい場合自動的にサイズを拡張し、インスタンスを生成する。
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
public class ExtendedListELResolver extends ELResolver {
	private static Logger log = LoggerFactory.getLogger(ExtendedListELResolver.class);

	static private Class<?> unmodifiableListClass = Collections.unmodifiableList(new ArrayList<Object>()).getClass();
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base instanceof List) {
			context.setPropertyResolved(base, property);
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			@SuppressWarnings("rawtypes")
			List list = (List) base;
			int index = toInt(property);
			if (isInvalidIndex(index, list.size(), bmc)) {
				return null;
			}
			
			PropertyRef pr = null;
			if (bmc.getElMapper().isAutoGrow()) {
				pr = bmc.getPropertyRef(list);
				if (index >= list.size() &&
						(pr.getComponentTypeKind() == TypeKind.BEAN || pr.getComponentTypeKind() == TypeKind.ENTITY)) {
					for (int i = list.size(); i <= index; i++) {
						list.add(null);
					}
				}
			}
			
			Object value = list.get(index);
			
			if (value == null && bmc.getElMapper().isAutoGrow()) {
				try {
					switch (pr.getComponentTypeKind()) {
					case BEAN:
						value = pr.getPropertyInfo().getComponentType().newInstance();
						break;
					case ENTITY:
						if (pr.getReferencePropertyHandler() != null) {
							value = pr.getReferencePropertyHandler().getReferenceEntityHandler(EntityContext.getCurrentContext()).newInstance();
						} else if (pr.getPropertyInfo().getComponentType().isInterface()) {
							value = new GenericEntity();
						} else {
							value = pr.getPropertyInfo().getComponentType().newInstance();
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
					list.set(index, value);
				}
			}
			
			return value;
		}
		
		return null;
	}

    private boolean isInvalidIndex(int index, int size, BeanMapperELContext bmc) {
		return index < 0 ||
				!bmc.getElMapper().isAutoGrow() && index >= size ||
				bmc.getElMapper().isAutoGrow() && index >= bmc.getElMapper().getIndexedPropertySizeLimit();
    }
    
	private int toInt(Object p) {
		if (p instanceof Number) {
			return ((Number) p).intValue();
		}
		if (p instanceof Character) {
			return ((Character) p).charValue();
		}
		if (p instanceof Boolean) {
			return ((Boolean) p).booleanValue()? 1: 0;
		}
		if (p instanceof String) {
			return Integer.parseInt((String) p);
		}
		throw new IllegalArgumentException();
	}
	
	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base instanceof List) {
			context.setPropertyResolved(true);
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			List<?> list = (List<?>) base;
			int index = toInt(property);
			if (isInvalidIndex(index, list.size(), bmc)) {
				throw new PropertyNotFoundException();
			} 
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
		
		if (base != null && base instanceof List) {
			context.setPropertyResolved(base, property);
			@SuppressWarnings("rawtypes")
			List list = (List) base;
			int index = toInt(property);
			try {
				
				BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
				if (bmc.getElMapper().isAutoGrow() && index < bmc.getElMapper().getIndexedPropertySizeLimit()
						&& index >= list.size()) {
					for (int i = list.size(); i <= index; i++) {
						list.add(null);
					}
				}
				
				if (value instanceof String) {
					//EL3.0の仕様上、nullをセットしようとしても、Stringの場合は空文字をセットしようとするのをnullをセットするようにする。
					if (bmc.getElMapper().isTrim()) {
						value = ((String) value).trim();
					}
					if (bmc.getElMapper().isEmptyToNull()) {
						if (((String) value).isEmpty()) {
							value = null;
						}
					}
				}
				
				list.set(index, value);
			} catch (UnsupportedOperationException e) {
				throw new PropertyNotWritableException();
			} catch (IndexOutOfBoundsException e) {
				throw new PropertyNotFoundException();
			}
		}
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base instanceof List) {
			context.setPropertyResolved(true);
			List<?> list = (List<?>) base;
			int index = toInt(property);
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			if (isInvalidIndex(index, list.size(), bmc)) {
				throw new PropertyNotFoundException();
			}
			return list.getClass() == unmodifiableListClass;
		}
		return false;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (base != null && base instanceof List) {
			return Integer.class;
		}
		return null;
	}
}
