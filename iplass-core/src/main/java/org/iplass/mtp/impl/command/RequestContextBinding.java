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

package org.iplass.mtp.impl.command;

import java.util.Collections;
import java.util.Map;

import org.iplass.mtp.command.RequestContext;

/**
 * GroovyTempalte、Scriptで利用可能な、ReadOnlyなRequestContext。
 * ReadOnlyで扱いたい箇所で利用。
 * 
 * 
 * @author K.Higuchi
 *
 */
public class RequestContextBinding {
	
	private RequestContext request;
	
	public static RequestContextBinding newRequestContextBinding() {
		return new RequestContextBinding();
	}
	
	private RequestContext getContext() {
		if (request == null) {
			request = RequestContextHolder.getCurrent();
		}
		return request;
	}
	
	public RequestContextBinding() {
	}
	
	public RequestContextBinding(RequestContext request) {
		this.request = request;
	}
	
	public Map<String, Object> getParam() {
		RequestContext req = getContext();
		if (req == null) {
			return Collections.emptyMap();
		} else {
			return req.getParamMap();
		}
	}
	
	public Map<String, Object> getParamMap() {
		RequestContext req = getContext();
		if (req == null) {
			return Collections.emptyMap();
		} else {
			return req.getParamMap();
		}
	}
	
	public Object getAttribute(String name) {
		RequestContext req = getContext();
		if (req == null) {
			return null;
		}
		return req.getAttribute(name);
	}
	
	public String getParam(String name) {
		RequestContext req = getContext();
		if (req == null) {
			return null;
		} else {
			return req.getParam(name);
		}
	}

	public String[] getParams(String name) {
		RequestContext req = getContext();
		if (req == null) {
			return new String[0];
		} else {
			return req.getParams(name);
		}
	}

}
