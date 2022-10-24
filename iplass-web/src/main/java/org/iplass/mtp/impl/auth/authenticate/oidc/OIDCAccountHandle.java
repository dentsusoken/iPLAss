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
import java.util.Set;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;

public class OIDCAccountHandle implements AccountHandle {
	private static final long serialVersionUID = 7261716807599677842L;

	public static final String OIDC_DEFINITION_NAME = "openIdConnectDefinitionName";
	public static final String SUBJECT_ID_WITH_DEFINITION_NAME = "subjectIdWithOpenIdConnectDefinitionName";
	public static final String EMAIL_VERIFIED = "email_verified";
	public static final String EMAIL = "email";
	public static final String GIVEN_NAME = "given_name";
	public static final String FAMILY_NAME = "family_name";
	
	public static final String UNIQUE_KEY_SEPARATOR = "@";


	private String id;
	private String uniqueKey;
	private Map<String, Object> attributeMap;
	private int authenticationProviderIndex;

	private String subjectId;
	private String subjectName;
	private String accessToken;
	private Long accessTokenExpiresIn;
	private String refreshToken;
	private Set<String> scopes;

	static String createSubjectUniqueKey(String subjectId, String openIdConnectDefinitionName) {
		String uniqueKey;
		if (OpenIdConnectService.DEFAULT_NAME.equals(openIdConnectDefinitionName)) {
			uniqueKey = subjectId;
		} else {
			uniqueKey = subjectId + UNIQUE_KEY_SEPARATOR + openIdConnectDefinitionName.replace('/', '.');
		}
		return uniqueKey;
	}

	public OIDCAccountHandle(String subjectId, String subjectName, String openIdConnectDefinitionName, Map<String, Object> attributeMap, String accessToken, Long accessTokenExpiresIn, String refreshToken, Set<String> scopes) {
		this.subjectId = subjectId;
		this.uniqueKey = createSubjectUniqueKey(subjectId, openIdConnectDefinitionName);
		this.subjectName = subjectName;
		if (attributeMap != null) {
			this.attributeMap = attributeMap;
		} else {
			this.attributeMap = new HashMap<String, Object>();
		}
		this.attributeMap.put(OIDC_DEFINITION_NAME, openIdConnectDefinitionName);
		this.attributeMap.put(SUBJECT_ID_WITH_DEFINITION_NAME, uniqueKey);
		this.accessToken = accessToken;
		this.accessTokenExpiresIn = accessTokenExpiresIn;
		this.refreshToken = refreshToken;
		this.scopes = scopes;
	}

	public void setId(String id) {
		this.id = id;
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

	public Long getAccessTokenExpiresIn() {
		return accessTokenExpiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Set<String> getScopes() {
		return scopes;
	}

	public String getOpenIdConnectDefinitionName() {
		return (String) attributeMap.get(OIDC_DEFINITION_NAME);
	}

	@Override
	public boolean isAccountLocked() {
		return false;
	}

	@Override
	public boolean isExpired() {
		return false;
	}

	@Override
	public boolean isInitialLogin() {
		return false;
	}

	@Override
	public Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	@Override
	public Credential getCredential() {
		return new OIDCCredential(id);
	}

	public void setAuthenticationProviderIndex(int authenticationProviderIndex) {
		this.authenticationProviderIndex = authenticationProviderIndex;
	}

	@Override
	public int getAuthenticationProviderIndex() {
		return authenticationProviderIndex;
	}

	@Override
	public String getUnmodifiableUniqueKey() {
		return uniqueKey;
	}

}
