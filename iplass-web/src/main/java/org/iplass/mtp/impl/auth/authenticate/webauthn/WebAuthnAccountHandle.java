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

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;

public class WebAuthnAccountHandle implements AccountHandle {
	private static final long serialVersionUID = -7481169158257633359L;

	public static final String WEB_AUTHN_DEFINITION_NAME = "webAuthnDefinitionName";

	private String id;//WebAuthn's userHandle base64urlsafe
	private String uniqueKey;//oid
	private Map<String, Object> attributeMap = new HashMap<>();
	private int authenticationProviderIndex;

	private byte[] credentialId;

	public WebAuthnAccountHandle(String userHandle, String oid, String webAuthnDefinitionName, byte[] credentialId, Map<String, Object> attributeMap) {
		this.id = userHandle;
		this.uniqueKey = oid;
		if (attributeMap != null) {
			this.attributeMap = attributeMap;
		} else {
			this.attributeMap = new HashMap<String, Object>();
		}
		this.attributeMap.put(WEB_AUTHN_DEFINITION_NAME, webAuthnDefinitionName);
		this.credentialId = credentialId;
	}

	public byte[] getCredentialId() {
		return credentialId;
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
	public Credential getCredential() {
		WebAuthnCredential credential = new WebAuthnCredential();
		credential.setId(id);
		return credential;
	}

	@Override
	public String getUnmodifiableUniqueKey() {
		return uniqueKey;
	}

	@Override
	public Map<String, Object> getAttributeMap() {
		return attributeMap;
	}

	@Override
	public void setAuthenticationProviderIndex(int authenticationProviderIndex) {
		this.authenticationProviderIndex = authenticationProviderIndex;
	}

	@Override
	public int getAuthenticationProviderIndex() {
		return authenticationProviderIndex;
	}

}
