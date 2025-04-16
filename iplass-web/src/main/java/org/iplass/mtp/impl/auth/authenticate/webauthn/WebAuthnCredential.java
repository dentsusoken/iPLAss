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
package org.iplass.mtp.impl.auth.authenticate.webauthn;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;


public class WebAuthnCredential implements Credential, Serializable {
	private static final long serialVersionUID = -2877054376956770659L;

	public static final String FACTOR_WA_DEFINITION_NAME = "webAuthnDefinitionName";
	public static final String FACTOR_PUBLIC_KEY_CREDENTIAL = "publicKeyCredential";
	public static final String FACTOR_SERVER = "server";
	
	private String id;// userHandle
	private String publicKeyCredential;
	private String webAuthnDefinitionName;
	private WebAuthnServer server;

	private Map<String, Object> additionalAuthenticationFactor;

	public WebAuthnCredential() {
	}

	public WebAuthnCredential(String webAuthnDefinitionName, String publicKeyCredential, WebAuthnServer server) {
		this.webAuthnDefinitionName = webAuthnDefinitionName;
		this.publicKeyCredential = publicKeyCredential;
		this.server = server;
	}

	/**
	 * userHandleの一致まで検証する場合はuserHandle（id）を指定する
	 * 
	 * @param webAuthnDefinitionName
	 * @param publicKeyCredential
	 * @param server
	 * @param userHandle
	 */
	public WebAuthnCredential(String webAuthnDefinitionName, String publicKeyCredential, WebAuthnServer server, String userHandle) {
		this.webAuthnDefinitionName = webAuthnDefinitionName;
		this.publicKeyCredential = publicKeyCredential;
		this.server = server;
		this.id = userHandle;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPublicKeyCredential() {
		return publicKeyCredential;
	}

	public void setPublicKeyCredential(String publicKeyCredential) {
		this.publicKeyCredential = publicKeyCredential;
	}

	public String getWebAuthnDefinitionName() {
		return webAuthnDefinitionName;
	}

	public void setWebAuthnDefinitionName(String webAuthnDefinitionName) {
		this.webAuthnDefinitionName = webAuthnDefinitionName;
	}

	public WebAuthnServer getServer() {
		return server;
	}

	public void setServer(WebAuthnServer server) {
		this.server = server;
	}

	@Override
	public Object getAuthenticationFactor(String name) {
		return switch (name) {
			case FACTOR_WA_DEFINITION_NAME -> webAuthnDefinitionName;
			case FACTOR_PUBLIC_KEY_CREDENTIAL -> publicKeyCredential;
		case FACTOR_SERVER -> server;
			default -> additionalAuthenticationFactor == null ? null : additionalAuthenticationFactor.get(name);
		};
	}

	@Override
	public void setAuthenticationFactor(String name, Object value) {
		switch (name) {
			case FACTOR_WA_DEFINITION_NAME -> this.webAuthnDefinitionName = (String) value;
			case FACTOR_PUBLIC_KEY_CREDENTIAL -> this.publicKeyCredential = (String) value;
			case FACTOR_SERVER -> this.server = (WebAuthnServer) value;
			default -> {
				if (additionalAuthenticationFactor == null) {
					additionalAuthenticationFactor = new HashMap<String, Object>();
				}
				additionalAuthenticationFactor.put(name, value);
			}
		}
	}

}
