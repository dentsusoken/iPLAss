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
package org.iplass.mtp.auth.login.token;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;

/**
 * シンプルな認証トークンで認証する際のCredentialです。
 * 認証時にはトークン文字列を指定します。
 * 認証トークンは事前にAuthTokenInfoListにて生成する必要があります。
 * SimpleAuthTokenは、当該トークンが紐付くユーザーとしてのアクセスを許可します。
 * また、SimpleAuthTokenは明示的に削除されるまで、永続的に有効です。
 * 
 * @author K.Higuchi
 *
 */
public class SimpleAuthTokenCredential implements Credential, Serializable {
	private static final long serialVersionUID = -5890942191793851280L;

	private String id;
	private String token;

	private Map<String, Object> additionalAuthenticationFactor;
	
	public SimpleAuthTokenCredential() {
	}

	public SimpleAuthTokenCredential(String token) {
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
