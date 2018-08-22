/*
 * Copyright 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
 * SimpleAuthTokenは、当該トークンが紐付くユーザとしてのアクセスを許可します。
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
