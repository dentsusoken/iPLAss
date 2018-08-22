/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.iplass.mtp.command.SessionContext;

@SuppressWarnings("deprecation")
public class JspTemplateHttpSession implements HttpSession {

	private HttpSession session;
	private SessionContext sessionContext;

	JspTemplateHttpSession(HttpSession session, SessionContext sessionContext) {
		this.session = session;
		this.sessionContext = sessionContext;
	}

	@Override
	public long getCreationTime() {
		return session.getCreationTime();
	}

	@Override
	public String getId() {
		return session.getId();
	}

	@Override
	public long getLastAccessedTime() {
		return session.getLastAccessedTime();
	}

	@Override
	public ServletContext getServletContext() {
		return session.getServletContext();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		session.setMaxInactiveInterval(interval);
	}

	@Override
	public int getMaxInactiveInterval() {
		return session.getMaxInactiveInterval();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return session.getSessionContext();
	}

	@Override
	public Object getAttribute(String name) {
		Object ret = null;
		if (sessionContext != null) {
			ret = sessionContext.getAttribute(name);
		}
		return ret;
	}

	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		if (sessionContext != null) {
			Iterator<String> it = sessionContext.getAttributeNames();
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
			return Collections.emptyEnumeration();
		}
	}

	@Override
	public String[] getValueNames() {
		if (sessionContext != null) {
			Iterator<String> it = sessionContext.getAttributeNames();
			ArrayList<String> ret = new ArrayList<>();
			while (it.hasNext()) {
				ret.add(it.next());
			}
			return ret.toArray(new String[ret.size()]);
		} else {
			return new String[0];
		}
	}

	@Override
	public void setAttribute(String name, Object value) {
		if (sessionContext != null) {
			sessionContext.setAttribute(name, value);
		}
	}

	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		if (sessionContext != null) {
			sessionContext.removeAttribute(name);
		}
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}

	@Override
	public void invalidate() {
		session.invalidate();
	}

	@Override
	public boolean isNew() {
		return session.isNew();
	}

}
