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

import java.lang.reflect.Array;
import java.util.Map;
import java.util.function.Consumer;

import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.PropertyNotWritableException;
import jakarta.el.ValueExpression;

import org.iplass.mtp.command.beanmapper.MappingError;
import org.iplass.mtp.command.beanmapper.MappingResult;
import org.iplass.mtp.impl.command.beanmapper.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ELMapper implements Mapper {
	private static Logger log = LoggerFactory.getLogger(ELMapper.class);
	
	private static ExpressionFactory exprFactory = ExpressionFactory.newInstance();
	
	private boolean trim;
	private boolean emptyToNull;
	private boolean autoGrow;
	private int indexedPropertySizeLimit;
	private Consumer<MappingError> typeConversionErrorHandler;
	
	private BeanMapperELContext elContext;

	public boolean isTrim() {
		return trim;
	}

	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	public boolean isEmptyToNull() {
		return emptyToNull;
	}

	public void setEmptyToNull(boolean emptyToNull) {
		this.emptyToNull = emptyToNull;
	}

	public boolean isAutoGrow() {
		return autoGrow;
	}

	public int getIndexedPropertySizeLimit() {
		return indexedPropertySizeLimit;
	}

	@Override
	public void setIndexedPropertySizeLimit(int indexedPropertySizeLimit) {
		this.indexedPropertySizeLimit = indexedPropertySizeLimit;
	}

	@Override
	public void setTypeConversionErrorHandler(Consumer<MappingError> typeConversionErrorHandler) {
		this.typeConversionErrorHandler = typeConversionErrorHandler;
	}
	
	@Override
	public void setTargetBean(Object bean) {
		elContext = new BeanMapperELContext(bean, this);
	}
	
	private void checkPropPath(String path) {
		for (int i = 0; i < path.length(); i++) {
			char c = path.charAt(i);
			if (!((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == '.'
					|| c == '['
					|| c == ']'
					|| c == '_'
					|| c == '\''
					)) {
				throw new IllegalArgumentException(" not valid property name:" + path);
			}
		}
	}
	
	@Override
	public Object getValue(String propPath) {
		checkPropPath(propPath);
		return getValue(elContext, propPath);
	}
	
	@Override
	public void map(Map<String, Object> valueMap, MappingResult result) {
		
		for (Map.Entry<String, Object> e: valueMap.entrySet()) {
			checkPropPath(e.getKey());
			
			try {
				// Trace logging (if enabled)
				if (log.isTraceEnabled()) {
					log.trace("setValue key=" + e.getKey() + ", value=" + e.getValue());
				}
				
				setValue(elContext, e.getKey(), e.getValue());
			} catch (PropertyNotFoundException | PropertyNotWritableException ee) {
				if (log.isDebugEnabled()) {
					log.debug(e.getKey() + " not reachable in target bean: " + ee.toString());
				}
			} catch (Exception ee) {
				
				Object val = e.getValue();
				if (val != null && val.getClass().isArray()) {
					Class<?> type = getType(elContext, e.getKey());
					if (!type.isArray() && Array.getLength(val) > 0) {
						val = Array.get(val, 0);
					}
				}
				
				
				if (typeConversionErrorHandler == null) {
					//FIXME message
					result.addError(new MappingError(e.getKey(), "type unmatch", val, ee));
				} else {
					MappingError me = new MappingError(e.getKey(), val);
					me.setCause(ee);
					typeConversionErrorHandler.accept(me);
					result.addError(me);
				}
			}
		}
	}
	
	private Object getValue(ELContext elContext, String exp) {
		ValueExpression ve = exprFactory.createValueExpression(elContext, "${" + BeanMapperELContext.ROOT_NAME + "." + exp + '}', Object.class);
		return ve.getValue(elContext);
	}
	
	private void setValue(ELContext elContext, String exp, Object value) {
		ValueExpression ve = exprFactory.createValueExpression(elContext, "${" + BeanMapperELContext.ROOT_NAME + "." + exp + '}', Object.class);
		if (value instanceof String) {
			if (trim) {
				value = ((String) value).trim();
			}
			if (emptyToNull) {
				if (((String) value).isEmpty()) {
					value = null;
				}
			}
		}
		ve.setValue(elContext, value);
	}
	
	private Class<?> getType(ELContext elContext, String exp) {
		ValueExpression ve = exprFactory.createValueExpression(elContext, "${" + BeanMapperELContext.ROOT_NAME + "." + exp + '}', Object.class);
		return ve.getType(elContext);
	}
	
	@Override
	public void setAutoGrow(boolean autoGrow) {
		this.autoGrow = autoGrow;
	}

}
