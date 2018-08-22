/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


public class SimpleParameterValueMap implements ParameterValueMap {
	
	private HttpServletRequest req;
	
	public SimpleParameterValueMap(HttpServletRequest req) {
		this.req = req;
	}

	@Override
	public Object getParam(String name) {
		return req.getParameter(name);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getParamMap() {
		return (Map) req.getParameterMap();
	}

	@Override
	public Iterator<String> getParamNames() {
		final Enumeration<String> names = req.getParameterNames();
		return new Iterator<String>() {
			@Override
			public boolean hasNext() {
				return names.hasMoreElements();
			}
			@Override
			public String next() {
				return names.nextElement();
			}
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public Object[] getParams(String name) {
		return req.getParameterValues(name);
	}

	@Override
	public void cleanTempResource() {
	}
}
