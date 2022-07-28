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

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;

public class OIDCCredential implements Credential {
	public static final String FACTOR_OP_DEFINITION_NAME = "openIdProviderDefinitionName";
	public static final String FACTOR_CODE = "code";
	public static final String FACTOR_STATE_TOKEN = "stateToken";
	public static final String FACTOR_REDIRECT_URI = "redirectUri";
	public static final String FACTOR_ISS = "iss";
	public static final String FACTOR_STATE = "state";
	
	private String id;
	private String openIdConnectDefinitionName;
	private String code;
	private String stateToken;
	private String redirectUri;
	private String iss;
	private OIDCState state;
	
	private Map<String, Object> additionalAuthenticationFactor;
	
	public OIDCCredential() {
	}

	public OIDCCredential(String id) {
		this.id = id;
	}
	
	public OIDCCredential(String openIdConnectDefinitionName, String code, String stateToken, String redirectUri, String iss, OIDCState state) {
		this.openIdConnectDefinitionName = openIdConnectDefinitionName;
		this.code = code;
		this.stateToken = stateToken;
		this.redirectUri = redirectUri;
		this.iss = iss;
		this.state = state;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}


	public String getOpenIdConnectDefinitionName() {
		return openIdConnectDefinitionName;
	}

	public void setOpenIdConnectDefinitionName(String openIdConnectDefinitionName) {
		this.openIdConnectDefinitionName = openIdConnectDefinitionName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStateToken() {
		return stateToken;
	}

	public void setStateToken(String stateToken) {
		this.stateToken = stateToken;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}

	public String getIss() {
		return iss;
	}

	public void setIss(String iss) {
		this.iss = iss;
	}

	public OIDCState getState() {
		return state;
	}

	public void setState(OIDCState state) {
		this.state = state;
	}

	@Override
	public Object getAuthenticationFactor(String name) {
		switch (name) {
		case FACTOR_OP_DEFINITION_NAME:
			return openIdConnectDefinitionName;
		case FACTOR_CODE:
			return code;
		case FACTOR_STATE_TOKEN:
			return stateToken;
		case FACTOR_REDIRECT_URI:
			return redirectUri;
		case FACTOR_ISS:
			return iss;
		case FACTOR_STATE:
			return state;
		default:
			if (additionalAuthenticationFactor == null) {
				return null;
			} else {
				return additionalAuthenticationFactor.get(name);
			}
		}
	}

	@Override
	public void setAuthenticationFactor(String name, Object value) {
		switch (name) {
		case FACTOR_OP_DEFINITION_NAME:
			this.openIdConnectDefinitionName = (String) value;
			break;
		case FACTOR_CODE:
			this.code = (String) value;
			break;
		case FACTOR_STATE_TOKEN:
			this.stateToken = (String) value;
			break;
		case FACTOR_REDIRECT_URI:
			this.redirectUri = (String) value;
			break;
		case FACTOR_ISS:
			this.iss = (String) value;
			break;
		case FACTOR_STATE:
			this.state = (OIDCState) value;
			break;
		default:
			if (additionalAuthenticationFactor == null) {
				additionalAuthenticationFactor = new HashMap<String, Object>();
			}
			additionalAuthenticationFactor.put(name, value);
		}
	}
}
