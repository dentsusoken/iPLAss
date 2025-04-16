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
package org.iplass.mtp.impl.auth.authenticate.webauthn.store;

import java.sql.Timestamp;
import java.text.DateFormat;

import org.iplass.mtp.auth.webauthn.WebAuthnAuthenticatorInfo;
import org.iplass.mtp.util.DateUtil;

import com.webauthn4j.credential.CredentialRecord;

public class WebAuthnAuthenticatorInfoImpl implements WebAuthnAuthenticatorInfo {

	private String type;
	private String key;
	private Timestamp startDate;
	private Timestamp lastLoginDate;
	private CredentialRecord credentialRecord;
	private String authenticatorDisplayName;
	private byte[] userHandle;

	public byte[] getUserHandle() {
		return userHandle;
	}

	public void setUserHandle(byte[] userHandle) {
		this.userHandle = userHandle;
	}

	public CredentialRecord getCredentialRecord() {
		return credentialRecord;
	}

	public void setCredentialRecord(CredentialRecord credentialRecord) {
		this.credentialRecord = credentialRecord;
	}

	@Override
	public String getAuthenticatorDisplayName() {
		return authenticatorDisplayName;
	}

	public void setAuthenticatorDisplayName(String authenticatorDisplayName) {
		this.authenticatorDisplayName = authenticatorDisplayName;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	@Override
	public Timestamp getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Override
	public String getDescription() {
		if (lastLoginDate != null) {
			return "WebAuthn Authenticator:" + getAuthenticatorDisplayName()
					+ " registered at " + DateUtil.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, true).format(startDate)
					+ ", last login at " + DateUtil.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, true).format(lastLoginDate);
		} else {
			return "WebAuthn Authenticator:" + getAuthenticatorDisplayName()
					+ " registered at " + DateUtil.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, true).format(startDate);
		}
	}
}
