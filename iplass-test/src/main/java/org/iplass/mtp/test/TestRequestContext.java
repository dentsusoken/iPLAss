/*
 * Copyright (C) 2016 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.test;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.command.IllegalParameterException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.session.Session;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.web.ReadOnlyHttpServletRequest;
import org.iplass.mtp.impl.web.SimpleSessionContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.web.actionmapping.ResponseHeader;

/**
 * テスト時に利用可能なRequestContextの実装です。
 * パラメータなどを明示的にセット可能です。
 *
 * @author K.Higuchi
 *
 */
public class TestRequestContext implements RequestContext {
	private HashMap<String, Object> paramMap;
	protected Map<String, Object> attribteMap;
	protected SessionContext session;
	private Map<String, Object> httpHeader;

	private ReadOnlyHttpServletRequest servletRequest;
	private ResponseHeader responseHeader;

	public TestRequestContext() {
		attribteMap = new HashMap<String, Object>();
		paramMap = new HashMap<String, Object>();
		attribteMap.put(WebRequestConstants.PARAM, paramMap);
		attribteMap.put(WebRequestConstants.HTTP_HEADER, httpHeader);
	}

	private Map<String, Object> getHttpHeader(HttpServletRequest httpReq) {
		Enumeration<?> headerNames = httpReq.getHeaderNames();
		if (headerNames != null) {
			HashMap<String, Object> header = new HashMap<String, Object>();

			ArrayList<String> buf = new ArrayList<String>();
			while (headerNames.hasMoreElements()) {
			    String name = (String) headerNames.nextElement();
			    Enumeration<?> headerValues = httpReq.getHeaders(name);
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
			return header;
		} else {
			return null;
		}
	}

	/**
	 * 指定のHttpServletRequestのインスタンスをセットします。
	 * HttpServletRequest#getHeaderNames()が値を返す場合、
	 * HTTPリクエストヘッダーも同時にセットします。
	 *
	 * @param httpRequest
	 */
	public void setHttpServletRequest(HttpServletRequest httpRequest) {
		this.servletRequest = new ReadOnlyHttpServletRequest(httpRequest);
		this.httpHeader = getHttpHeader(httpRequest);
	}

	/**
	 * HTTPリクエストのヘッダーをセットします。
	 *
	 * @param name
	 * @param value
	 */
	public void setHttpRequestHeader(String name, Object value) {
		if (httpHeader == null) {
			httpHeader = new HashMap<>();
		}
		httpHeader.put(name, value);
	}

	/**
	 * ResponseHeaderをセットします。
	 *
	 * @param responseHeader
	 */
	public void setResponseHeader(ResponseHeader responseHeader) {
		this.responseHeader = responseHeader;
	}

	/**
	 * 指定のnameでリクエストパラメータをセットします。
	 *
	 * @param name
	 * @param value
	 */
	public void setParam(String name, Object value) {
		paramMap.put(name, value);
	}

	@Override
	public String getParam(String name) {
		Object val = paramMap.get(name);
		if (val == null) {
			return null;
		}
		if (val instanceof String[]) {
			return ((String[]) val)[0];
		}
		return (String) val;
	}

	@Override
	public String[] getParams(String name) {
		Object val = paramMap.get(name);
		if (val == null) {
			return null;
		}
		if (val instanceof String) {
			return new String[]{(String) val};
		}
		return (String[]) val;
	}

	@Override
	public <T> T getParam(String name, Class<T> type) {
		try {
			return ConvertUtil.convert(type, getParam(name));
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] getParams(String name, Class<T> type) {
		Object[] vals = getParams(name);
		if (vals == null) {
			return null;
		}
		if (vals.getClass().getComponentType() == type) {
			return (T[]) vals;
		}
		try {
			T[] ret = (T[]) Array.newInstance(type, vals.length);
			for (int i = 0; i < vals.length; i++) {
				ret[i] = ConvertUtil.convert(type, vals[i]);
			}
			return ret;
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@Override
	public Date getParamAsDate(String name, String format) {
		try {
			return ConvertUtil.convertToDate(Date.class, getParam(name), format, false);
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@Override
	public Date[] getParamsAsDate(String name, String format) {
		Object[] vals = getParams(name);
		if (vals == null) {
			return null;
		}
		if (vals.getClass().getComponentType() == Date.class) {
			return (Date[]) vals;
		}
		try {
			Date[] ret = new Date[vals.length];
			for (int i = 0; i < vals.length; i++) {
				ret[i] = ConvertUtil.convertToDate(Date.class, vals[i], format, false);
			}
			return ret;
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@Override
	public Timestamp getParamAsTimestamp(String name, String format) {
		try {
			return ConvertUtil.convertToDate(Timestamp.class, getParam(name), format, true);
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@Override
	public Timestamp[] getParamsAsTimestamp(String name, String format) {
		Object[] vals = getParams(name);
		if (vals == null) {
			return null;
		}
		if (vals.getClass().getComponentType() == Timestamp.class) {
			return (Timestamp[]) vals;
		}
		try {
			Timestamp[] ret = new Timestamp[vals.length];
			for (int i = 0; i < vals.length; i++) {
				ret[i] = ConvertUtil.convertToDate(Timestamp.class, vals[i], format, true);
			}
			return ret;
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@Override
	public Time getParamAsTime(String name, String format) {
		try {
			return ConvertUtil.convertToDate(Time.class, getParam(name), format, false);
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@Override
	public Time[] getParamsAsTime(String name, String format) {
		Object[] vals = getParams(name);
		if (vals == null) {
			return null;
		}
		if (vals.getClass().getComponentType() == Time.class) {
			return (Time[]) vals;
		}
		try {
			Time[] ret = new Time[vals.length];
			for (int i = 0; i < vals.length; i++) {
				ret[i] = ConvertUtil.convertToDate(Time.class, vals[i], format, false);
			}
			return ret;
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@Override
	public BigDecimal getParamAsBigDecimal(String name) {
		return getParam(name, BigDecimal.class);
	}

	@Override
	public BigDecimal[] getParamsAsBigDecimal(String name) {
		return getParams(name, BigDecimal.class);
	}

	@Override
	public Integer getParamAsInt(String name) {
		return getParam(name, Integer.class);
	}

	@Override
	public Integer[] getParamsAsInt(String name) {
		return getParams(name, Integer.class);
	}

	@Override
	public Boolean getParamAsBoolean(String name) {
		return getParam(name, Boolean.class);
	}

	@Override
	public Boolean[] getParamsAsBoolean(String name) {
		return getParams(name, Boolean.class);
	}

	@Override
	public Double getParamAsDouble(String name) {
		return getParam(name, Double.class);
	}

	@Override
	public Double[] getParamsAsDouble(String name) {
		return getParams(name, Double.class);
	}

	@Override
	public UploadFileHandle getParamAsFile(String name) {
		Object val = paramMap.get(name);
		if (val == null) {
			return null;
		}
		if (val instanceof UploadFileHandle[]) {
			return ((UploadFileHandle[]) val)[0];
		}
		return (UploadFileHandle) val;
	}

	@Override
	public UploadFileHandle[] getParamsAsFile(String name) {
		Object val = paramMap.get(name);
		if (val == null) {
			return null;
		}
		if (val instanceof UploadFileHandle) {
			return new UploadFileHandle[]{(UploadFileHandle) val};
		}
		return (UploadFileHandle[]) val;
	}

	@Override
	public Long getParamAsLong(String name) {
		return getParam(name, Long.class);
	}

	@Override
	public Long[] getParamsAsLong(String name) {
		return getParams(name, Long.class);
	}

	@Override
	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	@Override
	public Iterator<String> getParamNames() {
		return paramMap.keySet().iterator();
	}

	public Object getAttribute(String name) {
		switch (name) {
		case WebRequestConstants.HTTP_HEADER:
			if (httpHeader != null) {
				return httpHeader;
			} else {
				return Collections.emptyMap();
			}
		case WebRequestConstants.RESPONSE_HEADER:
			return responseHeader;
//		case WebRequestConstants.ACTION_NAME:
//		case WebRequestConstants.COMMAND_RESULT:
//		case WebRequestConstants.EXECUTED_COMMAND:
//		case WebRequestConstants.EXCEPTION:
		case WebRequestConstants.SERVLET_REQUEST:
			return servletRequest;
		default:
			return attribteMap.get(name);
		}
	}

	public void setAttribute(String name, Object value) {
		//command、action依存の情報は、stackに
		//TODO このあたり（stackの管理。変数のスコープ）、汎用化する
		switch (name) {
		case WebRequestConstants.HTTP_HEADER:
			throw new IllegalArgumentException(WebRequestConstants.HTTP_HEADER + " is ReadOnly attribute.");
		case WebRequestConstants.PARAM:
			throw new IllegalArgumentException(WebRequestConstants.PARAM + " is ReadOnly attribute.");
		case WebRequestConstants.RESPONSE_HEADER:
			throw new IllegalArgumentException(WebRequestConstants.RESPONSE_HEADER + " is ReadOnly attribute.");
//		case WebRequestConstants.ACTION_NAME:
//		case WebRequestConstants.COMMAND_RESULT:
//		case WebRequestConstants.EXECUTED_COMMAND:
//		case WebRequestConstants.EXCEPTION:
		case WebRequestConstants.SERVLET_REQUEST:
			throw new IllegalArgumentException(WebRequestConstants.SERVLET_REQUEST + " is ReadOnly attribute.");
		default:
			if (value == null) {
				attribteMap.remove(name);
			} else {
				attribteMap.put(name, value);
			}
		}
	}

	@Override
	public void removeAttribute(String name) {
		setAttribute(name, null);
	}

	@Override
	public Iterator<String> getAttributeNames() {
		return attribteMap.keySet().iterator();
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
				session = (SimpleSessionContext) s.getAttribute("com.dentsusoken.mtp.sessionContext." + tenantId);

				if (session == null) {
					synchronized (s.getSessionMutexObject()) {
						session = (SimpleSessionContext) s.getAttribute("com.dentsusoken.mtp.sessionContext." + tenantId);
						if (session == null) {
							session = new SimpleSessionContext();
							s.setAttribute("com.dentsusoken.mtp.sessionContext." + tenantId, session);
						}
					}
				}
			}
		}
		return session;
	}

	private static String resourceString(String key, Object... arguments) {
		return TestResourceBundleUtil.resourceString(key, arguments);
	}
}
