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

package org.iplass.mtp.impl.session;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.util.KeyGenerator;
import org.iplass.mtp.spi.Service;

public abstract class SessionService implements Service {
	
	private static final String SESSION_NAME = "mtp.session.store.Session";
	private static final String SESSION_STATELESS_FLAG = "mtp.session.store.StatelessFlag";

	public Session getSession(boolean create) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		Session s = (Session) ec.getAttribute(SESSION_NAME);
		if (s == null) {
			if (ec.getAttribute(SESSION_STATELESS_FLAG) != null) {
				//既にSessionが存在する場合は、そちら優先
				s = getSessionInternal(false);
				if (s == null && create) {
					s = new OnetimeSessionImpl(this);
				}
			} else {
				s = getSessionInternal(create);
			}
			
			if (s != null) {
				ec.setAttribute(SESSION_NAME, s, true);
			}
		}
		
		return s;
	}

	public void setSessionStateless() {
		ExecuteContext.getCurrentContext().setAttribute(SESSION_STATELESS_FLAG, Boolean.TRUE, true);
	}
	
	public boolean isSessionStateless() {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		return ec.getAttribute(SESSION_STATELESS_FLAG) != null;
	}
	
	public void removeSessionFromExecuteContext(Session session) {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		Session s = (Session) ec.getAttribute(SESSION_NAME);
		if (s != null && s == session) {
			ec.removeAttribute(SESSION_NAME);
		}
	}

	protected abstract Session getSessionInternal(boolean create);

	public static class OnetimeSessionImpl implements Session {
		
		private static KeyGenerator gen = new KeyGenerator();
		
		private final ConcurrentHashMap<String, Object> store;
		private final String id;
		private final SessionService service;
		private final long createDate;
		
		public OnetimeSessionImpl(SessionService service) {
			store = new ConcurrentHashMap<>();
			id = "es-" + gen.generateId();
			createDate = System.currentTimeMillis();
			this.service = service;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public Object getAttribute(String name) {
			return store.get(name);
		}

		@Override
		public void setAttribute(String name, Object value) {
			store.put(name, value);
		}

		@Override
		public void removeAttribute(String name) {
			store.remove(name);
		}

		@Override
		public Iterator<String> getAttributeNames() {
			return store.keySet().iterator();
		}

		@Override
		public void invalidate() {
			store.clear();
			service.removeSessionFromExecuteContext(this);
		}

		@Override
		public Object getSessionMutexObject() {
			return this;
		}

		@Override
		public long getCreationTime() {
			return createDate;
		}
	}
	
}
