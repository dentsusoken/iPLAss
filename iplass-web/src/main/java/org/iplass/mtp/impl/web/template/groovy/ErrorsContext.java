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

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.ApplicationException;
import org.iplass.mtp.SystemException;
import org.iplass.mtp.command.beanmapper.MappingError;
import org.iplass.mtp.command.beanmapper.MappingException;
import org.iplass.mtp.command.beanmapper.MappingResult;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.impl.web.WebResourceBundleUtil;
import org.iplass.mtp.util.StringUtil;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.template.tags.ErrorsTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import groovy.lang.Binding;
import groovy.lang.MissingPropertyException;

class ErrorsContext {
	private static Logger log = LoggerFactory.getLogger(ErrorsContext.class);

	String errorsVariableName;
	String delimiter;
	String header;
	String footer;
	Boolean htmlEscape;

	BindContext bindContext;
	Object errors;
	Binding binding;

	boolean writeFirst;

	ErrorsContext(Map<String, Object> params, BindContext bindContext) {
		this.bindContext = bindContext;
		initParam(params);
		errors = params.get("errors");
		if (errors == null) {
			if (bindContext != null) {
				if (bindContext.getBinding() != null) {
					try {
						errors = bindContext.getBinding().getVariable(errorsVariableName);
					} catch (MissingPropertyException e) {
					}
				}
				if (errors == null && bindContext.setBean) {
					MappingResult me = bindContext.mappingResult;
					if (me != null && me.hasError()) {
						errors = me.getErrors();
					}
				}
			} else {
				WebRequestStack webStack = WebRequestStack.getCurrent();
				if (webStack != null) {
					errors = webStack.getRequestContext().getAttribute(WebRequestConstants.EXCEPTION);
				}
			}
		}

		if (errors != null) {
			setVariable(errorsVariableName, errors);
		} else {
			setVariable(errorsVariableName, null);
		}
	}

	private void initParam(Map<String, Object> params) {
		errorsVariableName = asString(params.get("errorsVariableName"));
		if (errorsVariableName == null) {
			if (bindContext != null) {
				errorsVariableName = bindContext.errorsVariableName;
			} else {
				errorsVariableName = ErrorsTag.DEFAULT_ERROR_VARIABLE_NAME;
			}
		}
		htmlEscape = (Boolean) params.get("htmlEscape");
		if (htmlEscape == null) {
			if (bindContext != null) {
				htmlEscape = bindContext.htmlEscape;
			}
			if (htmlEscape == null) {
				htmlEscape = Boolean.TRUE;
			}
		}

		delimiter = asString(params.get("delimiter"));
		if (delimiter == null) {
			delimiter = ErrorsTag.DEFAULT_DELIMITER;
		}
		header = asString(params.get("header"));
		if (header == null) {
			header = ErrorsTag.DEFAULT_HEADER;
		}
		footer = asString(params.get("footer"));
		if (footer == null) {
			footer = ErrorsTag.DEFAULT_FOOTER;
		}
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

	public Binding getBinding() {
		return binding;
	}

	@SuppressWarnings("rawtypes")
	private void writeError(Object errors, Writer out) throws IOException {
		if (errors instanceof String) {
			writeMessage((String) errors, out);
		} else if (errors instanceof List) {
			for (Object e: (List) errors) {
				writeError(e, out);
			}
		} else if (errors instanceof Object[]) {
			Object[] array = (Object[]) errors;
			for (int i = 0; i < array.length; i++) {
				writeError(array[i], out);
			}
		} else if (errors instanceof MappingError) {
			for (String m: ((MappingError) errors).getErrorMessages()) {
				writeError(m, out);
			}
		} else if (errors instanceof MappingResult) {
			for (MappingError me: ((MappingResult) errors).getErrors()) {
				writeError(me, out);
			}
		} else if (errors instanceof MappingException) {
			writeError(((MappingException) errors).getResult(), out);
		} else if (errors instanceof ApplicationException) {
			writeMessage(((ApplicationException) errors).getMessage(), out);
		} else if (errors instanceof Throwable) {
			if (log.isDebugEnabled()) {
				log.debug("System Exception: " + errors + " occoured. Message details are not output.");
			}
			if (writeFirst) {
				out.write(delimiter);
			} else {
				writeFirst = true;
			}
			writeMessage(resourceString("error.Error.retryMsg"), out);
		} else if (errors != null) {
			writeMessage(errors.toString(), out);
		}
	}

	private void writeMessage(String msg, Writer out) throws IOException {
		if (writeFirst) {
			out.write(delimiter);
		} else {
			writeFirst = true;
		}
		if (htmlEscape) {
			out.write(StringUtil.escapeHtml(msg));
		} else {
			out.write(msg);
		}
	}

	public void write(Writer w) {
		try {
			w.write(header);
			writeError(errors, w);
			w.write(footer);
		} catch (IOException e) {
			throw new SystemException(e);
		}
	}

	public boolean hasError() {
		return errors != null;
	}

	private static String resourceString(String key, Object... arguments) {
		return WebResourceBundleUtil.resourceString(key, arguments);
	}
}
