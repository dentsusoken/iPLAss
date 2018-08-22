/*
 * Copyright (C) 2013 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.command.async;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.iplass.mtp.command.IllegalParameterException;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.impl.util.ConvertUtil;
import org.iplass.mtp.impl.util.CoreResourceBundleUtil;

/**
 * 非同期Commandを呼び出し時に利用するRequestContext。<br>
 * AsyncRequestContextは実行する際Serializeされ、DBに格納される。
 * そのため、非同期呼び出し時に渡す属性（attribute）は、Serializable実装されている必要がある。
 * また、極力小さなオブジェクトとなるようにする。
 * 大きなオブジェクトを渡す場合は、別途Entityなどの形でDBに格納し、
 * AsyncRequestContextへは、そのキーであるoidを渡すようにする。<br>
 * getParam,getParamsメソッドは、以下のようなロジックで動作する。<br>
 * <pre>
 * Map param = getAttribute(AsyncRequestConstants.PARAM);
 * if (param == null) {
 * 	return  null;
 * }
 * return param.get(name)
 * </pre>
 *
 * また、非同期実行の際のtaskId、queueはそれぞれ、
 * AsyncRequestConstants.TASK_ID、AsyncRequestConstants.QUEUEをキーにattributeより取得できる。
 *
 *
 * @author K.Higuchi
 *
 */
public class AsyncRequestContext implements RequestContext, Serializable {
	private static final long serialVersionUID = 1878806170353316070L;

	private Map<String, Object> attributeMap;

	/**
	 * コンストラクタ。
	 */
	public AsyncRequestContext() {
		attributeMap = new HashMap<>();
	}

	/**
	 * コンストラクタ。
	 * 引数のattributeMapをattributeの属性として構築する。
	 *
	 * @param attributeMap
	 */
	public AsyncRequestContext(Map<String, Object> attributeMap) {
		if (attributeMap == null) {
			this.attributeMap = new HashMap<>();
		} else {
			this.attributeMap = new HashMap<>(attributeMap);
		}
	}

	@Override
	public String getParam(String name) {
		return getParam(name, String.class);
	}

	@Override
	public String[] getParams(String name) {
		return getParams(name, String.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getParam(String name, Class<T> type) {
		Map<String, Object> param = (Map<String, Object>) attributeMap.get(AsyncRequestConstants.PARAM);
		if (param == null) {
			return null;
		}
		try {
			return ConvertUtil.convert(type, param.get(name));
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] getParams(String name, Class<T> type) {
		Map<String, Object> param = (Map<String, Object>) attributeMap.get(AsyncRequestConstants.PARAM);
		if (param == null) {
			return null;
		}
		Object[] vals = (Object[]) param.get(name);
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
	public Boolean getParamAsBoolean(String name) {
		return getParam(name, Boolean.class);
	}

	@Override
	public Boolean[] getParamsAsBoolean(String name) {
		return getParams(name, Boolean.class);
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
	public Integer getParamAsInt(String name) {
		return getParam(name, Integer.class);
	}

	@Override
	public Integer[] getParamsAsInt(String name) {
		return getParams(name, Integer.class);
	}

	@Override
	public Double getParamAsDouble(String name) {
		return getParam(name, Double.class);
	}

	@Override
	public Double[] getParamsAsDouble(String name) {
		return getParams(name, Double.class);
	}

	@SuppressWarnings("unchecked")
	private <T extends java.util.Date> T getParamAsDate(String name, String format, Class<T> type) {
		Map<String, Object> param = (Map<String, Object>) attributeMap.get(AsyncRequestConstants.PARAM);
		if (param == null) {
			return null;
		}
		try {
			return ConvertUtil.convertToDate(type, param.get(name), format, false);
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends java.util.Date> T[] getParamsAsDate(String name, String format, Class<T> type) {
		Map<String, Object> param = (Map<String, Object>) attributeMap.get(AsyncRequestConstants.PARAM);
		if (param == null) {
			return null;
		}
		Object[] vals = (Object[]) param.get(name);
		if (vals == null) {
			return null;
		}
		if (vals.getClass().getComponentType() == type) {
			return (T[]) vals;
		}
		try {
			T[] ret = (T[]) Array.newInstance(type, vals.length);
			for (int i = 0; i < vals.length; i++) {
				ret[i] = ConvertUtil.convertToDate(type, vals[i], format, false);
			}
			return ret;
		} catch (RuntimeException e) {
			throw new IllegalParameterException(resourceString("impl.web.WebRequestContext.canNotConv", name), e);
		}
	}

	@Override
	public Date getParamAsDate(String name, String format) {
		return getParamAsDate(name, format, Date.class);
	}

	@Override
	public Date[] getParamsAsDate(String name, String format) {
		return getParamsAsDate(name, format, Date.class);
	}

	@Override
	public Timestamp getParamAsTimestamp(String name, String format) {
		return getParamAsDate(name, format, Timestamp.class);
	}

	@Override
	public Timestamp[] getParamsAsTimestamp(String name, String format) {
		return getParamsAsDate(name, format, Timestamp.class);
	}

	@Override
	public Time getParamAsTime(String name, String format) {
		return getParamAsDate(name, format, Time.class);
	}

	@Override
	public Time[] getParamsAsTime(String name, String format) {
		return getParamsAsDate(name, format, Time.class);
	}

	@Override
	public BigDecimal getParamAsBigDecimal(String name) {
		return getParam(name, BigDecimal.class);
	}

	@Override
	public BigDecimal[] getParamsAsBigDecimal(String name) {
		return getParams(name, BigDecimal.class);
	}

	/**
	 * AysncRequestContextでは利用できない。
	 * @throws UnsupportedOperationException
	 */
	@Override
	public UploadFileHandle getParamAsFile(String name) {
		//fileは取得できない
		throw new UnsupportedOperationException("getParamsAsFile not supported on AysncRequestContext");
	}

	/**
	 * AysncRequestContextでは利用できない。
	 * @throws UnsupportedOperationException
	 */
	@Override
	public UploadFileHandle[] getParamsAsFile(String name) {
		//fileは取得できない
		throw new UnsupportedOperationException("getParamsAsFile not supported on AysncRequestContext");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getParamMap() {
		return (Map<String, Object>) attributeMap.get(AsyncRequestConstants.PARAM);
	}

	@Override
	public Iterator<String> getParamNames() {
		Map<String, Object> param = getParamMap();
		if (param == null) {
			return Collections.emptyListIterator();
		} else {
			return param.keySet().iterator();
		}
	}

	@Override
	public Object getAttribute(String name) {
		return attributeMap.get(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		if (value == null) {
			attributeMap.remove(name);
		} else {
			attributeMap.put(name, value);
		}
	}

	@Override
	public void removeAttribute(String name) {
		attributeMap.remove(name);
	}

	@Override
	public Iterator<String> getAttributeNames() {
		return attributeMap.keySet().iterator();
	}

	/**
	 * AysncRequestContextでは利用できない。
	 * @throws UnsupportedOperationException
	 */
	@Override
	public SessionContext getSession() {
		//sessionの取得はできない
		throw new UnsupportedOperationException("getSession not supported on AysncRequestContext");
	}

	/**
	 * AysncRequestContextでは利用できない。
	 * @throws UnsupportedOperationException
	 */
	@Override
	public SessionContext getSession(boolean create) {
		//sessionの取得はできない
		throw new UnsupportedOperationException("getSession not supported on AysncRequestContext");
	}

	private static String resourceString(String key, Object... arguments) {
		return CoreResourceBundleUtil.resourceString(key, arguments);
	}
}
