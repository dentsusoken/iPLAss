/*
 * Copyright (C) 2020 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.webhook;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;
/**
 * 
 * @author lisf06
 */
public class WebHookAuthTokenCredential implements Credential, Serializable{

	private static final long serialVersionUID = -6802648562931555161L;
	
	private String id;
	private String token;
	private Map<String, Object> authenticationFactor;


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public Object getAuthenticationFactor(String name) {
		if (authenticationFactor == null) {
			return null;
		}
		return authenticationFactor.get(name);
	}

	@Override
	public void setAuthenticationFactor(String name, Object value) {
		if (authenticationFactor == null) {
			authenticationFactor = new HashMap<String, Object>();
		}
		authenticationFactor.put(name, value);
	}
	
	

}
