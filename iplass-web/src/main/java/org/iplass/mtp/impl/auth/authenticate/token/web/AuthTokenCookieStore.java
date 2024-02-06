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

package org.iplass.mtp.impl.auth.authenticate.token.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenClientStore;
import org.iplass.mtp.impl.web.WebRequestStack;
import org.iplass.mtp.web.template.TemplateUtil;

/**
 * RememberMeTokenをcookieで保存する場合のClientStore。
 * 
 * @author K.Higuchi
 *
 */
public class AuthTokenCookieStore implements AuthTokenClientStore {
	
	private String cookieName;
	
	public AuthTokenCookieStore() {
	}
	
	public AuthTokenCookieStore(String cookieName) {
		this.cookieName = cookieName;
	}
	
	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	@Override
	public void setToken(String token, int maxAgeSeconds) {
		if (token != null) {
			WebRequestStack reqStack = WebRequestStack.getCurrent();
			if (reqStack != null) {
				Cookie cookie = new Cookie(getCookieName(), token);
				cookie.setHttpOnly(true);
				cookie.setMaxAge(maxAgeSeconds);
				cookie.setPath(TemplateUtil.getTenantContextPath() + "/");
				cookie.setSecure(reqStack.getRequest().isSecure());
				reqStack.getResponse().addCookie(cookie);
			}
		}
	}

	@Override
	public String getToken() {
		return getToken(WebRequestStack.getCurrent());
	}
	
	private String getToken(WebRequestStack reqStack) {
		if (reqStack != null) {
			HttpServletRequest req = reqStack.getRequest();
			if (req != null) {
				Cookie[] cookies = req.getCookies();
				if (cookies != null) {
					for (Cookie c: cookies) {
						if (c.getName().equals(getCookieName())) {
							return c.getValue();
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public void clearToken() {
		WebRequestStack reqStack = WebRequestStack.getCurrent();
		if (reqStack != null) {
			if (getToken(reqStack) != null) {
				Cookie cookie = new Cookie(getCookieName(), null);
				cookie.setHttpOnly(true);
				cookie.setMaxAge(0);
				cookie.setPath(TemplateUtil.getTenantContextPath() + "/");
				reqStack.getResponse().addCookie(cookie);
			}
		}
	}
}
