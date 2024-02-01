/*
 * Copyright (C) 2013 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.auth.login;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;



public class IdPasswordCredential implements Credential, Serializable {
	private static final long serialVersionUID = -2988321007462156491L;

	public static final String FACTOR_PASSWORD = "password";

	private String id;
	private String password;
	private Map<String, Object> additionalAuthenticationFactor;

	public IdPasswordCredential() {
	}

	public IdPasswordCredential(String id, String password) {
		this.id = id;
		this.password = password;
	}

	@Override
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Object getAuthenticationFactor(String name) {
		if (FACTOR_PASSWORD.equals(name)) {
			return password;
		}
		if (additionalAuthenticationFactor == null) {
			return null;
		}
		return additionalAuthenticationFactor.get(name);
	}

	@Override
	public void setAuthenticationFactor(String name, Object value) {
		if (FACTOR_PASSWORD.equals(name)) {
			this.password = (String) value;
		} else {
			if (additionalAuthenticationFactor == null) {
				additionalAuthenticationFactor = new HashMap<String, Object>();
			}
			additionalAuthenticationFactor.put(name, value);
		}
	}

}
