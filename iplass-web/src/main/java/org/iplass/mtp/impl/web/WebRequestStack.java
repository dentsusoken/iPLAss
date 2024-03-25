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

import java.io.IOException;
import java.util.HashMap;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.jsp.PageContext;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.command.RequestContextHolder;
import org.iplass.mtp.impl.core.ExecuteContext;

public final class WebRequestStack {

	public static final String CALL_STACK_NAME = "mtp.web.RequestStack";

	private RequestPath requestPath;

	private ServletContext servletContext;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private PageContext pageContext;

	private WebRequestStack prevStack;
	private HashMap<String,Object> stackAttribute;

	private RequestContext webRequestContext;

	private boolean isLayoutStack;
	private boolean isIncludeStack;
	private boolean isIncludeTemplateStack;
	private boolean isRenderContentStack;

	public static WebRequestStack getCurrent() {
		return (WebRequestStack) ExecuteContext.getCurrentContext().getAttribute(CALL_STACK_NAME);
	}

	public WebRequestStack(RequestPath requestPath, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
		this(requestPath, null, servletContext, request, response, null);
	}

	public WebRequestStack(RequestContext webRequestContext, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, PageContext pageContext) {
		this(null, webRequestContext, servletContext, request, response, pageContext);
	}

	public WebRequestStack() throws IOException {
		this(null, null, null, null, null, null);
	}

	public WebRequestStack(RequestPath requestPath, RequestContext webRequestContext, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, PageContext pageContext) {

		ExecuteContext ec = ExecuteContext.getCurrentContext();
		prevStack = (WebRequestStack) ec.getAttribute(CALL_STACK_NAME);

		if (prevStack != null && requestPath == null) {
			this.requestPath = prevStack.requestPath;
		} else {
			this.requestPath = requestPath;
		}

		if (webRequestContext == null) {
			if (prevStack != null) {
				this.webRequestContext = prevStack.webRequestContext;
			} else {
				this.webRequestContext = new WebRequestContext(servletContext, request);
			}
		} else {
			this.webRequestContext = webRequestContext;
		}

		if (prevStack != null && servletContext == null) {
			this.servletContext = prevStack.servletContext;
		} else {
			this.servletContext = servletContext;
		}

		if (prevStack != null && request == null) {
			this.request = prevStack.request;
		} else {
			this.request = request;
		}

		if (prevStack != null && response == null) {
			this.response = prevStack.response;
		} else {
			this.response = response;
		}

		if (prevStack != null && pageContext == null) {
			this.pageContext = prevStack.pageContext;
		} else {
			this.pageContext = pageContext;
		}

		ec.setAttribute(CALL_STACK_NAME, this, true);
		RequestContextHolder.setCurrent(this.webRequestContext);
	}

//	public WebRequestStack(String requestPath, ServletContext servletContext, HttpServletRequest request, HttpServletResponse response, boolean isWebApiCall) {
//		this.servletContext = servletContext;
//		this.request = request;
//		this.response = response;
//		this.requestPath = requestPath;
//		this.isWebApiCall = isWebApiCall;
//		ExecuteContext ec = ExecuteContext.getCurrentContext();
//		prevStack = (WebRequestStack) ec.getAttribute(CALL_STACK_NAME);
//		ec.setAttribute(CALL_STACK_NAME, this, true);
//		if (servletContext == null) {
//			servletContext = request.getServletContext();
//		}
//	}

//	public boolean isWebApiCall() {
//		return isWebApiCall;
//	}

	public boolean isIncludeTemplateStack() {
		return isIncludeTemplateStack;
	}

	public void setIncludeTemplateStack(boolean isIncludeTemplateStack) {
		this.isIncludeTemplateStack = isIncludeTemplateStack;
	}

	public boolean isIncludeStack() {
		return isIncludeStack;
	}

	public void setIncludeStack(boolean isIncludeStack) {
		this.isIncludeStack = isIncludeStack;
	}

	public boolean isRenderContentStack() {
		return isRenderContentStack;
	}

	public void setRenderContentStack(boolean isRenderContentStack) {
		this.isRenderContentStack = isRenderContentStack;
	}

	public boolean isLayoutStack() {
		return isLayoutStack;
	}

	public void setLayoutStack(boolean isLayoutStack) {
		this.isLayoutStack = isLayoutStack;
	}

	void shareStackAttributeContext(WebRequestStack owner) {
		stackAttribute = owner.stackAttribute;
	}

	public WebRequestStack getPrevStack() {
		return prevStack;
	}

	public Object getAttribute(String name) {
		if (stackAttribute == null) {
			return null;
		}
		return stackAttribute.get(name);
	}

	public void setAttribute(String name, Object value) {
		if (stackAttribute == null) {
			stackAttribute = new HashMap<String, Object>();
		}
		stackAttribute.put(name, value);
	}

	public RequestPath getRequestPath() {
		return requestPath;
	}

//	public void setRequestPath(String requestPath) {
//		this.requestPath = requestPath;
//	}
//
	public ServletContext getServletContext() {
		return servletContext;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public PageContext getPageContext() {
		return pageContext;
	}
	public boolean isClientDirectRequest() {
		return prevStack == null;
	}


//	public void setRequestContext(RequestContext requestContext) {
//		this.webRequestContext = requestContext;
//		//FIXME WebRequestContextリファクタリングでちゃんと考える
//		RequestContextHolder.setCurrent(webRequestContext);
//
//	}


	public RequestContext getRequestContext() {
		return webRequestContext;
	}

	//FIXME ExecuteCOntextからのfinally呼び出しと、stackのfinallyは分けて呼び出す
	public void finallyProcess() {

		try {
			//クライアント直呼び出しの場合は、合わせてセッションストア＆リソースをクリア
			if (prevStack == null) {
				if (webRequestContext != null && webRequestContext instanceof WebRequestContext) {
					((WebRequestContext) webRequestContext).finallyProcess();
				}
			}
		} finally {
			stackAttribute = null;

			ExecuteContext ec = ExecuteContext.getCurrentContext();
			ec.setAttribute(CALL_STACK_NAME, prevStack, true);
			if (prevStack != null) {
				RequestContextHolder.setCurrent(prevStack.webRequestContext);
			} else {
				RequestContextHolder.setCurrent(null);
			}
		}
	}

}
