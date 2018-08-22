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
import java.lang.reflect.Array;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;

import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.impl.command.beanmapper.el.PropertyInfo.TypeKind;
import org.iplass.mtp.impl.entity.EntityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>カスタムのArrayELResolver</p>
 * <p>
 * 標準提供のArrayELResolver実装に対し、次の機能を拡張。
 * <ul>
 * <li>
 * autoGrow機能<br>
 * 対象の配列のサイズが指定のindexより小さい場合自動的にサイズを拡張し、インスタンスを生成する。
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
public class ExtendedArrayELResolver extends ELResolver {
	private static Logger log = LoggerFactory.getLogger(ExtendedArrayELResolver.class);

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}

		if (base != null && base.getClass().isArray()) {
			context.setPropertyResolved(base, property);
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			int index = toInt(property);
			int arrayLength = Array.getLength(base);
			if (isInvalidIndex(index, arrayLength, bmc)) {
				return null;
			}
			
			try {
				PropertyRef pr = null;
				if (bmc.getElMapper().isAutoGrow()) {
					pr = bmc.getPropertyRef(base);
					
					if (index >= arrayLength &&
							(pr.getComponentTypeKind() == TypeKind.BEAN || pr.getComponentTypeKind() == TypeKind.ENTITY)) {
						Object newArray = Array.newInstance(base.getClass().getComponentType(), index + 1);
						System.arraycopy(base, 0, newArray, 0, arrayLength);
						bmc.replacePropertyRef(base, newArray);
						base = newArray;
					}
				}
				
				Object value = Array.get(base, index);
				
				if (value == null && bmc.getElMapper().isAutoGrow()) {
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
				
					if (value != null) {
						Array.set(base, index, value);
					}
				}
				
				return value;
			} catch (ELException e) {
				throw e;
			} catch (Exception e) {
				throw new ELException(e);
			}
		}
		
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base.getClass().isArray()) {
			context.setPropertyResolved(true);
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			int index = toInt(property);
			if (isInvalidIndex(index, Array.getLength(base), bmc)) {
				throw new PropertyNotFoundException();
			}
			return base.getClass().getComponentType();
		}
		return null;
		
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
	public void setValue(ELContext context, Object base, Object property, Object value) {
		if (context == null) {
			throw new NullPointerException();
		}

		if (base != null && base.getClass().isArray()) {
			context.setPropertyResolved(base, property);
			int index = toInt(property);
			int arrayLength = Array.getLength(base);
			
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			try {
				if (bmc.getElMapper().isAutoGrow() && index < bmc.getElMapper().getIndexedPropertySizeLimit() && index >= arrayLength) {
					Object newArray = Array.newInstance(base.getClass().getComponentType(), index + 1);
					System.arraycopy(base, 0, newArray, 0, arrayLength);
					bmc.replacePropertyRef(base, newArray);
					base = newArray;
					arrayLength = index + 1;
				}
			} catch (ELException e) {
				throw e;
			} catch (Exception e) {
				throw new ELException(e);
			}
			
			if (index < 0 || index >= arrayLength) {
				throw new PropertyNotFoundException();
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
			
			Array.set(base, index, value);
		}
	}
    
    private boolean isInvalidIndex(int index, int arrayLength, BeanMapperELContext bmc) {
		return index < 0 ||
				!bmc.getElMapper().isAutoGrow() && index >= arrayLength ||
				bmc.getElMapper().isAutoGrow() && index >= bmc.getElMapper().getIndexedPropertySizeLimit();
    }

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}
		
		if (base != null && base.getClass().isArray()) {
			context.setPropertyResolved(true);
			int index = toInt(property);
			BeanMapperELContext bmc = (BeanMapperELContext) context.getContext(BeanMapperELContext.class);
			if (isInvalidIndex(index, Array.getLength(base), bmc)) {
				throw new PropertyNotFoundException();
			}
		}
		return false;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		if (base != null && base.getClass().isArray()) {
			return Integer.class;
		}
		return null;
	}

}
