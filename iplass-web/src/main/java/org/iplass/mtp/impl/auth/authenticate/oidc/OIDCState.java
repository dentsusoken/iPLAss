/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.oidc;

import java.io.Serializable;

public class OIDCState implements Serializable {
	private static final long serialVersionUID = 5959079983537975036L;

	private String token;
	private String nonce;
	private String codeVerifier;
	private String issuer;
	private String redirectUri;
	private long createTime = System.currentTimeMillis();
	
	private String backUrlAfterAuth;
	private String errorTemplateName;

	public String getErrorTemplateName() {
		return errorTemplateName;
	}
	public void setErrorTemplateName(String errorTemplateName) {
		this.errorTemplateName = errorTemplateName;
	}
	public long getCreateTime() {
		return createTime;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getBackUrlAfterAuth() {
		return backUrlAfterAuth;
	}
	public void setBackUrlAfterAuth(String backUrlAfterAuth) {
		this.backUrlAfterAuth = backUrlAfterAuth;
	}
	public String getCodeVerifier() {
		return codeVerifier;
	}
	public void setCodeVerifier(String codeVerifier) {
		this.codeVerifier = codeVerifier;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

}
