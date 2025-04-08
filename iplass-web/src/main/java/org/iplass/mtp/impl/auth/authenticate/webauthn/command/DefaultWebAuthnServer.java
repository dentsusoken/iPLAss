/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.webauthn.command;

import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.command.SessionContext;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnServer;
import org.iplass.mtp.impl.auth.authenticate.webauthn.WebAuthnState;
import org.iplass.mtp.web.WebRequestConstants;

import jakarta.servlet.http.HttpServletRequest;

public class DefaultWebAuthnServer implements WebAuthnServer {

	private RequestContext requestContext;
	private String webAuthnStateSessionKey;

	public DefaultWebAuthnServer(RequestContext requestContext, String webAuthnStateSessionKey) {
		this.requestContext = requestContext;
		this.webAuthnStateSessionKey = webAuthnStateSessionKey;
	}

	@Override
	public String rpId() {
		HttpServletRequest httpReq = (HttpServletRequest) requestContext.getAttribute(WebRequestConstants.SERVLET_REQUEST);
		return httpReq.getServerName();
	}

	@Override
	public String origin() {
		HttpServletRequest httpReq = (HttpServletRequest) requestContext.getAttribute(WebRequestConstants.SERVLET_REQUEST);
		StringBuilder sb = new StringBuilder();
		if (httpReq.isSecure()) {
			sb.append("https://");
		} else {
			sb.append("http://");
		}
		sb.append(httpReq.getServerName());

		int port = httpReq.getServerPort();
		if ((httpReq.isSecure() && port != 443)
				|| (!httpReq.isSecure() && port != 80)) {
			sb.append(':').append(port);
		}
		return sb.toString();
	}

	@Override
	public WebAuthnState getWebAuthnState(boolean withRemove) {
		WebAuthnState state = null;
		SessionContext session = requestContext.getSession(false);
		if (session != null) {
			state = (WebAuthnState) session.getAttribute(webAuthnStateSessionKey);
			if (state != null && withRemove) {
				session.removeAttribute(webAuthnStateSessionKey);
			}
		}
		return state;
	}

	@Override
	public void saveWebAuthnState(WebAuthnState state) {
		SessionContext session = requestContext.getSession(true);
		session.setAttribute(webAuthnStateSessionKey, state);
	}

}
