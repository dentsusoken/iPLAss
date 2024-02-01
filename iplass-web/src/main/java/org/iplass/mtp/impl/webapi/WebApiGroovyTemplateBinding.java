/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.webapi;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.entity.BinaryReference;
import org.iplass.mtp.impl.script.template.GroovyTemplateBinding;

import groovy.lang.MissingPropertyException;

class WebApiGroovyTemplateBinding extends GroovyTemplateBinding {
	private RequestContext request;

	WebApiGroovyTemplateBinding(Writer out, RequestContext request) {
		super(out);
		this.request = request;
		if (request != null) {
			setVariable("request", request);
			setVariable("session", new LazySessionContext(request));
		}
	}
	
	@Override
	public Object getVariable(String name) {
		try {
			return super.getVariable(name);
		} catch (MissingPropertyException e) {
			if (request == null) {
				throw e;
			}
			Object val = request.getAttribute(name);
			if (val == null && request.getSession(false) != null) {
				val = request.getSession(false).getAttribute(name);
			}
			if (val == null) {
				throw e;
			}
			return val;
		}
	}

	@Override
	public boolean hasVariable(String name) {
		boolean ret = super.hasVariable(name);
		if (request == null) {
			return ret;
		}
		
		if (!ret) {
			ret = request.getAttribute(name) != null;
		}
		if (!ret && request.getSession(false) != null) {
			ret = request.getSession(false).getAttribute(name) != null;
		}
		return ret;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getVariables() {
		//TODO ラップ必要？？
		return super.getVariables();
	}
	
	private static class LazySessionContext implements SessionContext {

		private final RequestContext req;
		private SessionContext sess;

		private LazySessionContext(RequestContext req) {
			this.req = req;
		}

		private void initSess() {
			if (sess == null) {
				sess = req.getSession();
			}
		}

		@Override
		public Object getAttribute(String name) {
			initSess();
			return sess.getAttribute(name);
		}

		@Override
		public void setAttribute(String name, Object value) {
			initSess();
			sess.setAttribute(name, value);
		}

		@Override
		public Iterator<String> getAttributeNames() {
			initSess();
			return sess.getAttributeNames();
		}

		@Override
		public void removeAttribute(String name) {
			initSess();
			sess.removeAttribute(name);
		}

		@Override
		public BinaryReference loadFromTemporary(long lobId) {
			initSess();
			return sess.loadFromTemporary(lobId);
		}
	}

}
