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
package org.iplass.mtp.impl.webapi.rest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.Request;

import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.web.WebRequestConstants;
import org.iplass.mtp.webapi.WebApiRequestConstants;
import org.iplass.mtp.webapi.definition.RequestType;
import org.iplass.mtp.webapi.definition.MethodType;;

public class RestRequestContext extends WebRequestContext {
	private RequestType requestType;
	private MethodType methodType;
	private Request rsRequest;
	private boolean supportBearerToken;
	
	public RestRequestContext(ServletContext servletContext, HttpServletRequest req, Request rsRequest, boolean supportBearerToken) {
		super(servletContext, req);
		this.rsRequest = rsRequest;
		switch (rsRequest.getMethod()) {
		case HttpMethod.DELETE:
			methodType = MethodType.DELETE;
			break;
		case HttpMethod.GET:
			methodType = MethodType.GET;
			break;
		case HttpMethod.POST:
			methodType = MethodType.POST;
			break;
		case HttpMethod.PUT:
			methodType = MethodType.PUT;
			break;
		default:
			break;
		}
		this.supportBearerToken = supportBearerToken;
	}
	
	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	
	public boolean supportBearerToken() {
		return supportBearerToken;
	}
	
	public Request rsRequest() {
		return rsRequest;
	}
	
	public MethodType methodType() {
		return methodType;
	}
	
	public RequestType requestType() {
		return requestType;
	}
	
	@Override
	public Object getAttribute(String name) {
		switch (name) {
		case WebApiRequestConstants.REQUEST_TYPE:
			return requestType;
		case WebApiRequestConstants.WEB_API:
			return Boolean.TRUE;
		case WebRequestConstants.ACTION:
			return null;
		case WebApiRequestConstants.HTTP_METHOD:
			return methodType;
		case WebApiRequestConstants.API_NAME:
			return WebRequestStack.getCurrent().getAttribute(WebApiRequestConstants.API_NAME);
		case WebApiRequestConstants.SUB_PATH:
			WebRequestStack stack = WebRequestStack.getCurrent();
			String apiName = (String) stack.getAttribute(WebApiRequestConstants.API_NAME);
			if (apiName == null) {
				return null;
			}
			return stack.getRequestPath().getTargetSubPath(apiName, true);
			
		default:
			return super.getAttribute(name);
		}
	}

	@Override
	public void setAttribute(String name, Object value) {
		switch (name) {
		case WebApiRequestConstants.REQUEST_TYPE:
		case WebApiRequestConstants.WEB_API:
		case WebRequestConstants.ACTION:
		case WebApiRequestConstants.HTTP_METHOD:
		case WebApiRequestConstants.API_NAME:
			throw new IllegalArgumentException(name + " is ReadOnly attribute.");
		default:
			super.setAttribute(name, value);
		}
	}
}
