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
package org.iplass.mtp.impl.web.template.groovy;

import java.util.ArrayList;
import java.util.Map;

import jakarta.el.PropertyNotFoundException;

import org.iplass.mtp.command.beanmapper.MappingError;
import org.iplass.mtp.command.beanmapper.MappingException;
import org.iplass.mtp.command.beanmapper.MappingResult;
import org.iplass.mtp.impl.command.beanmapper.el.ELMapper;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.template.ValueFormatter;
import org.iplass.mtp.web.template.tags.BindTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Binding;

class BindContext {
	private static Logger log = LoggerFactory.getLogger(BindContext.class);
	
	String propertyDelimiter;
	String indexPrefix;
	String indexPostfix;
	String prefix;
	
	String beanVariableName;
	String mappingResultVariableName;
	Boolean autoDetectErrors = Boolean.TRUE;
	String propertyNameVariableName;
	String propertyValueVariableName;
	String propertyRawValueVariableName;
	String propertyErrorValueVariableName;
	String errorsVariableName;
	Boolean htmlEscape;
	ValueFormatter formatter;
	
	BindContext parent;
	
	Object bean;
	String prop;
	MappingResult mappingResult;
	
	boolean setBean;
	ELMapper elMapper;
	
	Binding binding;
	
	BindContext(Map<String, Object> params, BindContext parent) {
		setBean = params.containsKey("bean");
		if (!setBean && parent != null && parent.setBean) {
			this.parent = parent;
		}
		initParam(params);
		prop = asString(params.get("prop"));
		
		if (setBean) {
			bean = params.get("bean");
			mappingResult = (MappingResult) params.get("mappingResult");
			elMapper = new ELMapper();
			elMapper.setTargetBean(bean);
			exposeBean();
		} else {
			if (this.parent != null) {
				bean = this.parent.bean;
				elMapper = this.parent.elMapper;
				mappingResult = this.parent.mappingResult;
			}
		}
		
		if (prop != null) {
			exposeProp();
		}
	}
	
	public Binding getBinding() {
		return binding;
	}
	
	private String asString(Object val) {
		if (val == null) {
			return null;
		}
		return val.toString();
	}
	
	private void setVariable(String name, Object value) {
		if (binding == null) {
			binding = new Binding();
		}
		binding.setVariable(name, value);
	}
	
	private void exposeBean() {
		setVariable(beanVariableName, bean);
		
		if (autoDetectErrors != null && autoDetectErrors.booleanValue()) {
			if (mappingResult == null) {
				WebRequestStack webStack = WebRequestStack.getCurrent();
				if (webStack != null) {
					Exception e = (Exception) webStack.getRequestContext().getAttribute(WebRequestConstants.EXCEPTION);
					if (e != null) {
						MappingResult mr = ((MappingException) e).getResult();
						if (bean == mr.getBean()) {
							mappingResult = mr;
						}
					}
				}
			}
			if (mappingResult == null) {
				mappingResult = new MappingResult(bean);
			}
		}
		setVariable(mappingResultVariableName, mappingResult);
	}
	
	private void exposeProp() {
		if (bean != null) {
			String name = prop;
			if (propertyDelimiter != null) {
				name = name.replace(BindTag.DEFAULT_PROPERTY_DELIMITER, propertyDelimiter);
			}
			if (indexPrefix != null) {
				name = name.replace(BindTag.DEFAULT_INDEX_PREFIX, indexPrefix);
			}
			if (indexPostfix != null) {
				name = name.replace(BindTag.DEFAULT_INDEX_POSTFIX, indexPostfix);
			}
			if (prefix != null) {
				name = prefix + name;
			}
			setVariable(propertyNameVariableName, name);
			
			//init error value. MissingPropertyException発生させないために
			setVariable(propertyErrorValueVariableName, null);
			setVariable(errorsVariableName, null);
			Object value = null;
			boolean valResolve = false;
			if (mappingResult.hasError()) {
				MappingError e = mappingResult.getError(prop);
				if (e != null) {
					value = e.getErrorValue();
					valResolve = true;
				}
				
				ArrayList<String> errMsgs = new ArrayList<>();
				Object errorValue = null;
				for (MappingError me: mappingResult.getErrors()) {
					if (me.getPropertyPath().startsWith(prop)) {
						if (me.getPropertyPath().length() == prop.length()) {
							errMsgs.addAll(me.getErrorMessages());
							errorValue = me.getErrorValue();
						} else {
							char c = me.getPropertyPath().charAt(prop.length());
							if (c == '.' || c == '[') {
								errMsgs.addAll(me.getErrorMessages());
							}
						}
					}
				}
				if (errMsgs.size() > 0) {
					setVariable(errorsVariableName, errMsgs);
					if (errorValue != null && htmlEscape) {
						setVariable(propertyErrorValueVariableName, StringUtil.escapeHtml(formatter.apply(errorValue)));
					} else {
						setVariable(propertyErrorValueVariableName, formatter.apply(errorValue));
					}
				}
			}
			
			if (!valResolve) {
				try {
					value = elMapper.getValue(prop);
				} catch (PropertyNotFoundException e) {
					if (log.isDebugEnabled()) {
						log.debug(prop + " not reachable in target bean: " + bean + ", cause:" + e.toString());
					}
				}
			}
			
			if (value != null && htmlEscape) {
				setVariable(propertyValueVariableName, StringUtil.escapeHtml(formatter.apply(value)));
			} else {
				setVariable(propertyValueVariableName, formatter.apply(value));
			}
			setVariable(propertyRawValueVariableName, value);
		}
	}
	
	private void initParam(Map<String, Object> params) {
		propertyDelimiter = asString(params.get("propertyDelimiter"));
		if (propertyDelimiter == null) {
			if (parent != null) {
				propertyDelimiter = parent.propertyDelimiter;
			}
		}
		indexPrefix = asString(params.get("indexPrefix"));
		if (indexPrefix == null) {
			if (parent != null) {
				indexPrefix = parent.indexPrefix;
			}
		}
		indexPostfix = asString(params.get("indexPostfix"));
		if (indexPostfix == null) {
			if (parent != null) {
				indexPostfix = parent.indexPostfix;
			}
		}
		prefix = asString(params.get("prefix"));
		if (prefix == null) {
			if (parent != null) {
				prefix = parent.prefix;
			}
		}
		htmlEscape = (Boolean) params.get("htmlEscape");
		if (htmlEscape == null) {
			if (parent != null) {
				htmlEscape = parent.htmlEscape;
			}
			if (htmlEscape == null) {
				htmlEscape = Boolean.TRUE;
			}
		}
		formatter = (ValueFormatter) params.get("formatter");
		if (formatter == null) {
			if (parent != null) {
				formatter = parent.formatter;
			}
			if (formatter == null) {
				formatter = ValueFormatter.DEFAULT_FORMATTER;
			}
		}
		
		Boolean adr = (Boolean) params.get("autoDetectErrors");
		if (adr != null) {
			autoDetectErrors = adr;
		}
		
		beanVariableName = asString(params.get("beanVariableName"));
		if (beanVariableName == null) {
			if (parent != null) {
				beanVariableName = parent.beanVariableName;
			}
			if (beanVariableName == null) {
				beanVariableName = BindTag.DEFAULT_BEAN_VARIABLE_NAME;
			}
		}
		mappingResultVariableName = asString(params.get("mappingResultVariableName"));
		if (mappingResultVariableName == null) {
			if (parent != null) {
				mappingResultVariableName = parent.mappingResultVariableName;
			}
			if (mappingResultVariableName == null) {
				mappingResultVariableName = BindTag.DEFAULT_MAPPING_RESULT_VARIABLE_NAME;
			}
		}
		propertyNameVariableName = asString(params.get("propertyNameVariableName"));
		if (propertyNameVariableName == null) {
			if (parent != null) {
				propertyNameVariableName = parent.propertyNameVariableName;
			}
			if (propertyNameVariableName == null) {
				propertyNameVariableName = BindTag.DEFAULT_PROPERTY_NAME_VARIABLE_NAME;
			}
		}
		propertyValueVariableName = asString(params.get("propertyValueVariableName"));
		if (propertyValueVariableName == null) {
			if (parent != null) {
				propertyValueVariableName = parent.propertyValueVariableName;
			}
			if (propertyValueVariableName == null) {
				propertyValueVariableName = BindTag.DEFAULT_PROPERTY_VALUE_VARIABLE_NAME;
			}
		}
		propertyRawValueVariableName = asString(params.get("propertyRawValueVariableName"));
		if (propertyRawValueVariableName == null) {
			if (parent != null) {
				propertyRawValueVariableName = parent.propertyRawValueVariableName;
			}
			if (propertyRawValueVariableName == null) {
				propertyRawValueVariableName = BindTag.DEFAULT_PROPERTY_RAW_VALUE_VARIABLE_NAME;
			}
		}
		propertyErrorValueVariableName = asString(params.get("propertyErrorValueVariableName"));
		if (propertyErrorValueVariableName == null) {
			if (parent != null) {
				propertyErrorValueVariableName = parent.propertyErrorValueVariableName;
			}
			if (propertyErrorValueVariableName == null) {
				propertyErrorValueVariableName = BindTag.DEFAULT_PROPERTY_ERROR_VALUE_VARIABLE_NAME;
			}
		}
		errorsVariableName = asString(params.get("errorsVariableName"));
		if (errorsVariableName == null) {
			if (parent != null) {
				errorsVariableName = parent.errorsVariableName;
			}
			if (errorsVariableName == null) {
				errorsVariableName = BindTag.DEFAULT_ERROR_VARIABLE_NAME;
			}
		}
	}
}
