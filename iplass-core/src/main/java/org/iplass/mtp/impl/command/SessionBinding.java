/*
 * Copyright (C) 2012 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;

/**
 * GroovyTempalte、Scriptで利用可能な、ReadOnlyなSession。
 * 
 * SessionContextが取得できる個所（Template、Command）では、
 * SessionBindingでなく、SessionContextをバインドする想定。
 * 
 * @author K.Higuchi
 *
 */
public class SessionBinding {

	private SessionContext session;
	private boolean initSession;
	
	public static SessionBinding newSessionBinding() {
		return new SessionBinding();
	}
	
	private SessionContext getContext() {
		if (!initSession) {
			RequestContext req = RequestContextHolder.getCurrent();
			if (req != null) {
				session = req.getSession(false);
			}
			initSession = true;
		}
		return session;
	}
	
	public SessionBinding() {
	}
	
	public Object getAttribute(String name) {
		SessionContext sess = getContext();
		if (sess == null) {
			return null;
		}
		return sess.getAttribute(name);
	}

}
