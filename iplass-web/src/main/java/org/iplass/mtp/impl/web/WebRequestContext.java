/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.session.Session;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.impl.web.fileupload.MultiPartParameterValueMap;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;


public class WebRequestContext implements RequestContext {
	public static final String MARK_USE_OUTPUT_STREAM = "org.iplass.mtp.markUseOutputStream";

	//Webからのリクエストパラメータ
	private ParameterValueMap valueMap;
	private HttpServletRequest servletRequest;

	//サーバでのオブジェクト受け渡し用のMap
	protected SimpleSessionContext session;

	private ReadOnlyHttpServletRequest readOnlyServletRequest;
	private Map<String, Object> httpHeader;
	private Map<String, Object> paramMap;

	public WebRequestContext(ServletContext servletContext, HttpServletRequest servletRequest) {
		this.servletRequest = servletRequest;
		if (ServletFileUpload.isMultipartContent(servletRequest)) {
			setValueMap(new MultiPartParameterValueMap(servletContext, servletRequest));
		} else {
			setValueMap(new SimpleParameterValueMap(servletRequest));
		}
	}

	private ReadOnlyHttpServletRequest getReadOnlyHttpServletRequest() {
		if (readOnlyServletRequest == null) {
			if (servletRequest != null) {
				readOnlyServletRequest = new ReadOnlyHttpServletRequest(servletRequest);
			}
		}
		return readOnlyServletRequest;
	}

	private Map<String, Object> getHttpHeader() {
		if (httpHeader == null) {
			if (servletRequest != null) {
				Enumeration<?> headerNames = servletRequest.getHeaderNames();
				HashMap<String, Object> header = new HashMap<String, Object>();

				ArrayList<String> buf = new ArrayList<String>();
				while (headerNames.hasMoreElements()) {
				    String name = (String) headerNames.nextElement();
				    Enumeration<?> headerValues = servletRequest.getHeaders(name);
				    while (headerValues.hasMoreElements()) {
				    	buf.add((String) headerValues.nextElement());
				    }
				    if (buf.size() == 1) {
				    	header.put(name, buf.get(0));
				    } else if (buf.size() > 1) {
				    	header.put(name, buf.toArray(new String[buf.size()]));
				    }
					buf.clear();
				}
				httpHeader = Collections.unmodifiableMap(header);
			} else {
				httpHeader = Collections.emptyMap();
			}
		}
		
		return httpHeader;
	}
	
	public void setValueMap(ParameterValueMap valueMap) {
		this.valueMap = valueMap;
		paramMap = null;
	}

	public ParameterValueMap getValueMap() {
		return valueMap;
	}

	@Override
	public Iterator<String> getAttributeNames() {
		Enumeration<String> e = servletRequest.getAttributeNames();
		return new Iterator<String>() {
			@Override
			public boolean hasNext() {
				return e.hasMoreElements();
			}
			@Override
			public String next() {
				return e.nextElement();
			}
		};
	}

	public void finallyProcess() {
		if (valueMap != null) {
			valueMap.cleanTempResource();
		}
		
		//セッションの保存
		if (session != null && session.isUpdate()) {
			int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
			SessionService ss = ServiceRegistry.getRegistry().getService(SessionService.class);
			Session s = ss.getSession(true);
			session.resetUpdateFlag();
			s.setAttribute(SimpleSessionContext.KEY_FOR_HTTP_SERVLET_REQUEST + "." + tenantId, session);
		}
	}
	
	public Object getAttribute(String name) {
		switch (name) {
		case WebRequestConstants.HTTP_HEADER:
			return getHttpHeader();
		case WebRequestConstants.PARAM:
			return getParamMap();
		case WebRequestConstants.RESPONSE_HEADER:
			return new ResponseHeaderImpl();
		case WebRequestConstants.SERVLET_REQUEST:
			return getReadOnlyHttpServletRequest();
		case WebRequestConstants.ACTION:
			return Boolean.TRUE;
		case WebRequestConstants.ACTION_NAME:
		case WebRequestConstants.COMMAND_RESULT:
		case WebRequestConstants.EXECUTED_COMMAND:
		case WebRequestConstants.EXCEPTION:
			return WebRequestStack.getCurrent().getAttribute(name);
		case WebRequestConstants.SUB_PATH:
			WebRequestStack stack = WebRequestStack.getCurrent();
			String actName = (String) stack.getAttribute(WebRequestConstants.ACTION_NAME);
			if (actName == null) {
				return null;
			}
			return stack.getRequestPath().getTargetSubPath(actName, true);
		default:
			return servletRequest.getAttribute(name);
		}
	}
	
	public void setAttribute(String name, Object value) {
		switch (name) {
		case WebRequestConstants.HTTP_HEADER:
		case WebRequestConstants.PARAM:
		case WebRequestConstants.RESPONSE_HEADER:
		case WebRequestConstants.SERVLET_REQUEST:
		case WebRequestConstants.ACTION:
		case WebRequestConstants.ACTION_NAME:
		case WebRequestConstants.EXECUTED_COMMAND:
			throw new IllegalArgumentException(name + " is ReadOnly attribute.");
		case WebRequestConstants.COMMAND_RESULT:
		case WebRequestConstants.EXCEPTION:
			WebRequestStack.getCurrent().setAttribute(name, value);
		default:
			servletRequest.setAttribute(name, value);
		}
	}
	
	protected void setAttributeDirect(String name, Object value) {
		servletRequest.setAttribute(name, value);
	}
	
	@Override
	public void removeAttribute(String name) {
		servletRequest.removeAttribute(name);
	}

	public Map<String, Object> getParamMap() {
		if (paramMap == null) {
			if (valueMap != null) {
				paramMap = Collections.unmodifiableMap(valueMap.getParamMap());
			} else {
				paramMap = Collections.emptyMap();
			}
		}
		return paramMap;
	}

	@Override
	public Iterator<String> getParamNames() {
		if (valueMap != null) {
			return valueMap.getParamNames();
		} else {
			return Collections.emptyIterator();
		}
	}

	@Override
	public String getParam(String name) {
		if (valueMap != null) {
			Object val = valueMap.getParam(name);
			if (val == null) {
				return null;
			}
			return val.toString();
		} else {
			return null;
		}
	}

	@Override
	public String[] getParams(String name) {
		if (valueMap != null) {
			Object[] vals = valueMap.getParams(name);
			if (vals == null) {
				return null;
			} else if (vals instanceof String[]) {
				return (String[]) vals;
			} else {
				String[] ret = new String[vals.length];
				for (int i = 0; i < ret.length; i++) {
					if (vals[i] != null) {
						ret[i] = vals[i].toString();
					}
				}
				return ret;
			}
		} else {
			return null;
		}
	}

	@Override
	public UploadFileHandle getParamAsFile(String name) {
		if (valueMap != null) {
			Object val = valueMap.getParam(name);
			if (val instanceof UploadFileHandle) {
				return (UploadFileHandle) val;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public UploadFileHandle[] getParamsAsFile(String name) {
		if (valueMap != null) {
			Object[] vals = valueMap.getParams(name);
			if (vals instanceof UploadFileHandle[]) {
				return (UploadFileHandle[]) vals;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public SessionContext getSession() {
		return getSession(true);
	}

	@Override
	public SessionContext getSession(boolean create) {
		if (session == null) {
			SessionService ss = ServiceRegistry.getRegistry().getService(SessionService.class);
			Session s = ss.getSession(create);
			if (s != null) {
				int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
				session = (SimpleSessionContext) s.getAttribute(SimpleSessionContext.KEY_FOR_HTTP_SERVLET_REQUEST + "." + tenantId);

				if (session == null) {
					synchronized (s.getSessionMutexObject()) {
						session = (SimpleSessionContext) s.getAttribute(SimpleSessionContext.KEY_FOR_HTTP_SERVLET_REQUEST + "." + tenantId);
						if (session == null) {
							session = new SimpleSessionContext();
							s.setAttribute(SimpleSessionContext.KEY_FOR_HTTP_SERVLET_REQUEST + "." + tenantId, session);
						}
					}
				}
			}
		}
		return session;
	}

	public void clearSession() {
		session = null;
	}
}
