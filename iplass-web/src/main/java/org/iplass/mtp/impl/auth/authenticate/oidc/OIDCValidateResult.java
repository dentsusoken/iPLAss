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

import java.util.Map;
import java.util.Set;

public class OIDCValidateResult {
	private String subjectId;
	private String subjectName;
	private Map<String, Object> claims;
	private boolean valid;
	private String tokenType;
	private String accessToken;
	private Long expiresIn;
	private String refreshToken;
	private Set<String> scopes;
	
	private String error;
	private String errorDescription;
	private String errorUri;
	
	private Exception rootCause;
	
	OIDCValidateResult(String subjectId, String subjectName, Map<String, Object> claims, String tokenType, String accessToken, Long expiresIn, String refreshToken, Set<String> scopes) {
		valid = true;
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.claims = claims;
		this.tokenType = tokenType;
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;
		this.scopes = scopes;
	}
	
	OIDCValidateResult(String error, String errorDescription, String errorUri, Exception rootCause) {
		valid = false;
		this.error = error;
		this.errorDescription = errorDescription;
		this.errorUri = errorUri;
		this.rootCause = rootCause;
	}
	
	public String getTokenType() {
		return tokenType;
	}
	public Set<String> getScopes() {
		return scopes;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public Long getExpiresIn() {
		return expiresIn;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public Map<String, Object> getClaims() {
		return claims;
	}
	public boolean isValid() {
		return valid;
	}
	public String getError() {
		return error;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public String getErrorUri() {
		return errorUri;
	}
	public Exception getRootCause() {
		return rootCause;
	}
}
