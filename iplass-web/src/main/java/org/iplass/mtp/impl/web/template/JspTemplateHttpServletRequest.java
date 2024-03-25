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
package org.iplass.mtp.impl.web.template;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpSession;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.UploadFileHandle;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.actionmapping.VariableParameterValueMap;

public class JspTemplateHttpServletRequest extends HttpServletRequestWrapper {
	
	private RequestContext requestContext;
	private boolean isMultipart;
	private boolean needCheckParamMap;

	public JspTemplateHttpServletRequest(HttpServletRequest request, RequestContext requestContext) {
		super(request);
		this.requestContext = requestContext;
		isMultipart = ServletFileUpload.isMultipartContent(request);
		if (requestContext instanceof WebRequestContext) {
			if (((WebRequestContext) requestContext).getValueMap() instanceof VariableParameterValueMap) {
				needCheckParamMap = ((VariableParameterValueMap) ((WebRequestContext) requestContext).getValueMap()).hasParamMapDefs();
			}
		}
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value == null) {
			if (isMultipart || needCheckParamMap) {
				value = requestContext.getParam(name);
			}
		}
		
		return value;
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null) {
			if (isMultipart || needCheckParamMap) {
				values = requestContext.getParams(name);
			}
		}
		return values;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, String[]> getParameterMap() {
		if (isMultipart) {
			Map<String, Object> paramMap = requestContext.getParamMap();
			Map<String, String[]> retMap = new HashMap<>();
			for (Map.Entry<String, Object> e: paramMap.entrySet()) {
				if (e.getValue() instanceof String[]) {
					retMap.put(e.getKey(), (String[]) e.getValue());
				} else if (e.getValue() instanceof UploadFileHandle[]) {
					UploadFileHandle[] ufhs = (UploadFileHandle[]) e.getValue();
					String[] strs = new String[ufhs.length];
					for (int i = 0; i < strs.length; i++) {
						if (ufhs[i] != null) {
							strs[i] = ufhs[i].toString();
						}
					}
					retMap.put(e.getKey(), strs);
				}
			}
			Map<String, String[]> superMap = super.getParameterMap();
			for (Map.Entry<String, String[]> e: superMap.entrySet()) {
				retMap.put(e.getKey(), e.getValue());
			}
			
			return retMap;
		} else {
			if (!needCheckParamMap) {
				return super.getParameterMap();
			} else {
				Map<String, String[]> retMap = new HashMap<>((Map) requestContext.getParamMap());
				for (Map.Entry<String, String[]> e: super.getParameterMap().entrySet()) {
					retMap.put(e.getKey(), e.getValue());
				}
				return retMap;
			}
		}
	}

	@Override
	public Enumeration<String> getParameterNames() {
		if (isMultipart || needCheckParamMap) {
			HashSet<String> names = new HashSet<>();
			Enumeration<String> pn1 = super.getParameterNames();
			while (pn1.hasMoreElements()) {
				names.add(pn1.nextElement());
			}
			Iterator<String> pn2 = requestContext.getParamNames();
			while (pn2.hasNext()) {
				names.add(pn2.next());
			}
			Iterator<String> it = names.iterator();
			return new Enumeration<String>() {
				@Override
				public boolean hasMoreElements() {
					return it.hasNext();
				}
				@Override
				public String nextElement() {
					return it.next();
				}
			};
		} else {
			return super.getParameterNames();
		}
	}

	@Override
	public HttpSession getSession(boolean create) {
		HttpSession session = super.getSession(create);
		if (session == null) {
			return null;
		}
		
		if (requestContext != null) {
			return new JspTemplateHttpSession(session, requestContext.getSession());
		} else {
			return session;
		}
	}

	@Override
	public HttpSession getSession() {
		return getSession(true);
	}

	@Override
	public Object getAttribute(String name) {
		Object ret = super.getAttribute(name);
		if (ret == null && requestContext != null) {
			ret = requestContext.getAttribute(name);
		}
		return ret;
	}
}
