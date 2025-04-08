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

public class WebAuthnVerifyResult {

	private boolean verified;

	private String userHandle;
	private String userOid;
	private byte[] credentialId;
	private String policyName;

	private String error;
	private String errorDescription;
	private Exception rootCause;

	WebAuthnVerifyResult(byte[] credentialId, String userHandle, String userOid, String policyName) {
		verified = true;
		this.credentialId = credentialId;
		this.userHandle = userHandle;
		this.userOid = userOid;
		this.policyName = policyName;
	}

	WebAuthnVerifyResult(String error, String errorDescription, Exception rootCause) {
		verified = false;
		this.error = error;
		this.errorDescription = errorDescription;
		this.rootCause = rootCause;
	}

	public boolean isVerified() {
		return verified;
	}

	public String getUserHandle() {
		return userHandle;
	}

	public String getUserOid() {
		return userOid;
	}

	public String getPolicyName() {
		return policyName;
	}

	public byte[] getCredentialId() {
		return credentialId;
	}

	public String getError() {
		return error;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public Exception getRootCause() {
		return rootCause;
	}
}
