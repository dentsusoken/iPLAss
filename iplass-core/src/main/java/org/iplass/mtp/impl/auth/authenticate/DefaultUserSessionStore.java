/*
 * Copyright (C) 2014 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate;

import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.UserContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.session.Session;
import org.iplass.mtp.impl.session.SessionService;
import org.iplass.mtp.spi.Config;

public class DefaultUserSessionStore implements UserSessionStore {
	
	public enum SessionFixationProtectionMethod {
		NEW_SESSION, CHANGE_SESSION_ID;
	}
	
	private SessionService sessionService;
	
	private boolean shareLoginSession;
	private SessionFixationProtectionMethod sessionFixationProtection = SessionFixationProtectionMethod.CHANGE_SESSION_ID;
	
	public DefaultUserSessionStore() {
	}
	
	public boolean isShareLoginSession() {
		return shareLoginSession;
	}

	public void setShareLoginSession(boolean shareLoginSession) {
		this.shareLoginSession = shareLoginSession;
	}

	public SessionFixationProtectionMethod getSessionFixationProtection() {
		return sessionFixationProtection;
	}

	public void setSessionFixationProtection(SessionFixationProtectionMethod sessionFixationProtection) {
		this.sessionFixationProtection = sessionFixationProtection;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	@Override
	public UserContext getUserContext() {
		Session session = sessionService.getSession(false);
		if (session != null) {
			String key = null;
			if (shareLoginSession) {
				key = AuthService.USER_HANDLE_NAME;
			} else {
				int tenantId = ExecuteContext.getCurrentContext().getTenantContext().getTenantId();
				key = AuthService.USER_HANDLE_NAME + "." + tenantId;
			}
			return (UserContext) session.getAttribute(key);
		} else {
			return null;
		}
	}
	
	@Override
	public void setUserContext(UserContext user, boolean withSessionInit) {
		Session session;
		if (sessionFixationProtection == SessionFixationProtectionMethod.CHANGE_SESSION_ID) {
			session = sessionService.getSession(true);
			if (withSessionInit) {
				session.changeSessionId();
			}
		} else {
			if (withSessionInit) {
				session = sessionService.getSession(false);
				if (session != null) {
					session.invalidate();
				}
			}
			session = sessionService.getSession(true);
		}
		
		if (shareLoginSession) {
			session.setAttribute(AuthService.USER_HANDLE_NAME, user);
		} else {
			int tenantId = ExecuteContext.getCurrentContext().getTenantContext().getTenantId();
			session.setAttribute(AuthService.USER_HANDLE_NAME + "." + tenantId, user);
		}
	}

	@Override
	public void invalidateUserSession() {
		Session session = sessionService.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@Override
	public void inited(AuthService service, Config config) {
		sessionService = config.getDependentService(SessionService.class);
	}

	@Override
	public void destroyed() {
	}

}
