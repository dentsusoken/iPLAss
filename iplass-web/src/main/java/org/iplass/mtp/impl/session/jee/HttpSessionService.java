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

package org.iplass.mtp.impl.session.jee;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.iplass.mtp.command.RequestContextWrapper;
import org.iplass.mtp.impl.session.Session;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.impl.web.WebRequestContext;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.spi.Config;

public class HttpSessionService extends SessionService {
	
	static final String MUTEX_OBJECT_NAME ="mtp.session.store.Mutex";

	@Override
	public void init(Config config) {
	}

	@Override
	public void destroy() {
	}

	@Override
	public Session getSessionInternal(boolean create) {
		WebRequestStack request = WebRequestStack.getCurrent();
		if (request == null) {
			if (create) {
				return new OnetimeSessionImpl(this);
			}
		} else {
			
			HttpSession httpSession = request.getRequest().getSession(create);
			if (httpSession != null) {
				return new HttpServletSession(httpSession, this);
			}
		}
		
		return null;
	}
	
	private static class HttpServletSession implements Session {
		
		private HttpSession httpSession;
		private SessionService service;
		
		private HttpServletSession(HttpSession httpSession, SessionService service) {
			this.httpSession = httpSession;
			this.service = service;
		}

		@Override
		public String getId() {
			return httpSession.getId();
		}

		@Override
		public Object getAttribute(String name) {
			return httpSession.getAttribute(name);
		}

		@Override
		public void setAttribute(String name, Object value) {
			httpSession.setAttribute(name, value);
		}

		@Override
		public void removeAttribute(String name) {
			httpSession.removeAttribute(name);
		}

		@Override
		public Iterator<String> getAttributeNames() {
			@SuppressWarnings("rawtypes")
			final Enumeration e = httpSession.getAttributeNames();
			return new Iterator<String>() {
				@Override
				public boolean hasNext() {
					return e.hasMoreElements();
				}

				@Override
				public String next() {
					return (String) e.nextElement();
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public void invalidate() {
			service.removeSessionFromExecuteContext(this);
			httpSession.invalidate();
			
			WebRequestContext webRequestContext = null;
			WebRequestStack stack = WebRequestStack.getCurrent();
			if (stack != null) {
				if (stack.getRequestContext() instanceof WebRequestContext) {
					webRequestContext = (WebRequestContext) stack.getRequestContext();
				} else if (stack.getRequestContext() instanceof RequestContextWrapper) {
					RequestContextWrapper w = (RequestContextWrapper) stack.getRequestContext();
					while (w != null) {
						if (w.getWrapped() instanceof WebRequestContext) {
							webRequestContext = (WebRequestContext) w.getWrapped();
							w = null;
						} else if (w.getWrapped() instanceof RequestContextWrapper) {
							w = (RequestContextWrapper) w.getWrapped();
						} else {
							w = null;
						}
					}
				}
			}
			if (webRequestContext != null) {
				webRequestContext.clearSession();
			}
		}

		@Override
		public Object getSessionMutexObject() {
			Object mutex = httpSession.getAttribute(MUTEX_OBJECT_NAME);
			if (mutex == null) {
				return httpSession;
			} else {
				return mutex;
			}
		}

		@Override
		public long getCreationTime() {
			return httpSession.getCreationTime();
		}

		@Override
		public void changeSessionId() {
			WebRequestStack request = WebRequestStack.getCurrent();
			if (request == null) {
				throw new IllegalStateException("HttpServletRequest cannot be identified.");
			}
			request.getRequest().changeSessionId();
		}
	}
	
	public static class MutexObject implements Serializable {
		private static final long serialVersionUID = -8257705546725540025L;
	}

}
