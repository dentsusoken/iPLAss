/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth.code;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.util.StringUtil;

public class AuthorizationRequest implements Serializable {
	private static final long serialVersionUID = -3518685489891704249L;

	private String requestId;
	
	private String authorizationServerId;
	private String clientId;
	private String redirectUri;
	private List<String> responseTypes;
	private List<String> scopes;
	private String state;
	private String responseMode;
	private String codeChallenge;
	private String codeChallengeMethod;
	
	public AuthorizationRequest(String authorizationServerId, String clientId, String redirectUri) {
		this.requestId = StringUtil.randomToken();
		this.authorizationServerId = authorizationServerId;
		this.clientId = clientId;
		this.redirectUri = redirectUri;
	}

	public String getRequestId() {
		return requestId;
	}
	
	public String getAuthorizationServerId() {
		return authorizationServerId;
	}
	public void setAuthorizationServerId(String authorizationServerId) {
		this.authorizationServerId = authorizationServerId;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCodeChallenge() {
		return codeChallenge;
	}
	public void setCodeChallenge(String codeChallenge) {
		this.codeChallenge = codeChallenge;
	}
	public String getCodeChallengeMethod() {
		return codeChallengeMethod;
	}
	public void setCodeChallengeMethod(String codeChallengeMethod) {
		this.codeChallengeMethod = codeChallengeMethod;
	}
	public List<String> getResponseTypes() {
		return responseTypes;
	}
	public void setResponseTypes(List<String> responseTypes) {
		this.responseTypes = responseTypes;
	}
	public void addResponseTypes(String... responseType) {
		if (responseType != null) {
			if (this.responseTypes == null) {
				this.responseTypes = new ArrayList<>();
			}
			for (String rt: responseType) {
				this.responseTypes.add(rt);
			}
		}
	}
	public String getResponseMode() {
		return responseMode;
	}
	public void setResponseMode(String responseMode) {
		this.responseMode = responseMode;
	}
	public List<String> getScopes() {
		return scopes;
	}
	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}
	public void addScopes(String... scope) {
		if (scope != null) {
			if (this.scopes == null) {
				this.scopes = new ArrayList<>();
			}
			for (String s: scope) {
				this.scopes.add(s);
			}
		}
	}

}
