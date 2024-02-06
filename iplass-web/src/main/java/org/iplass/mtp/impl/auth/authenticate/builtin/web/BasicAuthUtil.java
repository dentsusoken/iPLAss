/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.builtin.web;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.web.WebRequestConstants;

public class BasicAuthUtil {
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String AUTH_SCHEME_BASIC = "Basic";

	public static IdPasswordCredential decodeFromHeader(RequestContext req) {
		HttpServletRequest hr = (HttpServletRequest) req.getAttribute(WebRequestConstants.SERVLET_REQUEST);
		String authHeaderValue = hr.getHeader(HEADER_AUTHORIZATION);
		if (authHeaderValue != null && authHeaderValue.regionMatches(true, 0, AUTH_SCHEME_BASIC + " ", 0, AUTH_SCHEME_BASIC.length() + 1)) {
			
			String idpassEncoded = authHeaderValue.substring(AUTH_SCHEME_BASIC.length() + 1).trim();
			String idpass;
			try {
				idpass = new String(Base64.getDecoder().decode(idpassEncoded), "utf-8");
			} catch (UnsupportedEncodingException e) {
				throw new SystemException(e);
			}
			
			String id = null;
			String pass = null;
			int index = idpass.indexOf(':');
			if (index < 0) {
				id = idpass.trim();
			} else {
				id = idpass.substring(0, index).trim();
				pass = idpass.substring(index + 1).trim();
			}
			
			return new IdPasswordCredential(id, pass);
		}
		
		return null;
	}
	
	public static String encodeValue(IdPasswordCredential idpass) {
		String str = idpass.getId() + ":" + idpass.getPassword();
		try {
			return AUTH_SCHEME_BASIC + " " + Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}
}
