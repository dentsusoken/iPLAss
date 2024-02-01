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
package org.iplass.mtp.impl.auth.oauth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;

/**
 * OAuth2のアクセストークンを表現するCredential。
 * 
 * @author K.Higuchi
 *
 */
public class AccessTokenCredential implements Credential, Serializable {
	//for internal use

	private static final long serialVersionUID = -191370831048492625L;

	private String id;
	private String token;
	
	private Map<String, Object> additionalAuthenticationFactor;
	
	public AccessTokenCredential() {
	}

	public AccessTokenCredential(String token) {
		this.token = token;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public Object getAuthenticationFactor(String name) {
		if (additionalAuthenticationFactor == null) {
			return null;
		}
		return additionalAuthenticationFactor.get(name);
	}

	@Override
	public void setAuthenticationFactor(String name, Object value) {
		if (additionalAuthenticationFactor == null) {
			additionalAuthenticationFactor = new HashMap<String, Object>();
		}
		additionalAuthenticationFactor.put(name, value);
	}

}
