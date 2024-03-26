/*
 * Copyright (C) 2014 DENTSU SOKEN INC. All Rights Reserved.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Locale;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.web.actionmapping.RequestInfo;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.ReadListener;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

public class ReadOnlyHttpServletRequest extends HttpServletRequestWrapper implements RequestInfo {

	public ReadOnlyHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Locale getLocale() {
		return ExecuteContext.getCurrentContext().getLangLocale();
	}

	@Override
	public boolean authenticate(HttpServletResponse response)
			throws IOException, ServletException {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public Part getPart(String name) throws IllegalStateException, IOException,
			ServletException {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public Collection<Part> getParts() throws IllegalStateException,
			IOException, ServletException {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public String getPathTranslated() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public String getRequestedSessionId() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public HttpSession getSession() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public HttpSession getSession(boolean create) {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public void login(String username, String password) throws ServletException {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public void logout() throws ServletException {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public AsyncContext getAsyncContext() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public DispatcherType getDispatcherType() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (getContentType() != null) {
			return super.getInputStream();
		} else {
			//Do not handle content type not specified
			return new NullServletInputStream();
		}
	}

	@Override
	public String getLocalAddr() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public String getLocalName() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public int getLocalPort() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public BufferedReader getReader() throws IOException {
		if (getContentType() != null) {
			return super.getReader();
		} else {
			//Do not handle content type not specified
			return new BufferedReader(new InputStreamReader(new NullServletInputStream()));
		}
	}

	@Override
	public ServletRequest getRequest() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public ServletContext getServletContext() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public boolean isAsyncStarted() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public boolean isAsyncSupported() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public boolean isWrapperFor(Class<?> wrappedType) {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public boolean isWrapperFor(ServletRequest wrapped) {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public void removeAttribute(String name) {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public void setAttribute(String name, Object o) {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public void setCharacterEncoding(String enc)
			throws UnsupportedEncodingException {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public void setRequest(ServletRequest request) {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public AsyncContext startAsync() {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest,
			ServletResponse servletResponse) throws IllegalStateException {
		throw new UnsupportedOperationException("Unsupported operation on ReadOnlyHttpServletRequest");
	}
	
	private class NullServletInputStream extends ServletInputStream {

		@Override
		public boolean isFinished() {
			return true;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
			//not supported
		}

		@Override
		public int read() throws IOException {
			return -1;
		}
		
	}

}
