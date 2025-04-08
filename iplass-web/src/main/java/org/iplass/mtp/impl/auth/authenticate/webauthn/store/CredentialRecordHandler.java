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

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;

import com.webauthn4j.credential.CredentialRecord;

/**
 * CredentialRecord Handler
 */
public class CredentialRecordHandler extends AuthTokenHandler {
	
	//SERIES->Credential ID
	//TOKEN->User.id
	//U_KEY->oid
	
	public static final String TYPE_WEBAUTHN_CREDENTIAL_DEFAULT = "WAC";
	
	private String hashAlgorithm = "SHA-512";
	private int seriesMaxLength = 256;

	public String getHashAlgorithm() {
		return hashAlgorithm;
	}

	public void setHashAlgorithm(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}

	public int getSeriesMaxLength() {
		return seriesMaxLength;
	}

	public void setSeriesMaxLength(int seriesMaxLength) {
		this.seriesMaxLength = seriesMaxLength;
	}

	@Override
	public void inited(AuthTokenService service, Config config) {
		super.inited(service, config);
		if (getType() == null) {
			setType(TYPE_WEBAUTHN_CREDENTIAL_DEFAULT);
		}
	}

	@Override
	public AuthTokenInfo toAuthTokenInfo(AuthToken authToken) {
		WebAuthnAuthenticatorInfoImpl info = new WebAuthnAuthenticatorInfoImpl();
		info.setType(authToken.getType());
		info.setKey(authToken.getSeries());
		info.setStartDate(authToken.getStartDate());
		info.setUserHandle(Base64.getUrlDecoder().decode(authToken.getToken()));
		
		CredentialRecordMement credentialRecordDetails = (CredentialRecordMement) authToken.getDetails();
		info.setCredentialRecord(credentialRecordDetails.toCredentialRecord());
		info.setAuthenticatorDisplayName(credentialRecordDetails.getAuthenticatorDisplayName());
		info.setLastLoginDate(credentialRecordDetails.getLastLoginDate());

		return info;
	}
	
	@Override
	public Credential toCredential(AuthToken newToken) {
		//TODO 
		return null;
	}

	@Override
	protected Serializable createDetails(String seriesString, String tokenString, String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		WebAuthnAuthenticatorInfoImpl ti = (WebAuthnAuthenticatorInfoImpl) tokenInfo;
		CredentialRecordMement details = new CredentialRecordMement(ti.getCredentialRecord());
		details.setAuthenticatorDisplayName(ti.getAuthenticatorDisplayName());
		return details;
	}

	@Override
	public String newTokenString(AuthTokenInfo tokenInfo) {
		WebAuthnAuthenticatorInfoImpl ti = (WebAuthnAuthenticatorInfoImpl) tokenInfo;
		return Base64.getUrlEncoder().withoutPadding().encodeToString(ti.getUserHandle());
	}

	@Override
	public String newSeriesString(String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		WebAuthnAuthenticatorInfoImpl ti = (WebAuthnAuthenticatorInfoImpl) tokenInfo;
		byte[] credentialId = ti.getCredentialRecord().getAttestedCredentialData().getCredentialId();
		return credentialIdToSeriesString(credentialId);
	}

	/**
	 * Credential IDをシリーズ文字列に変換。
	 * Server-side Credentialの場合のIDの長さが不定（Draftの仕様書のLevel3では最長1023bytesとある）。
	 * 現状、AuthToken格納するテーブルのSERIESのサイズが256byte(文字)なので、
	 * それを超えるCredentialID長の場合は、ハッシュ関数でダイジェスト化して格納する。
	 * このロジックはCredential ID（のbase64）の長さがseriesMaxLengthを超える場合にのみ使用される。
	 * 
	 * @param credentialId
	 * @return
	 */
	private String credentialIdToSeriesString(byte[] credentialId) {
		try {
			String seriesString = Base64.getUrlEncoder().withoutPadding().encodeToString(credentialId);
			if (seriesString.length() > seriesMaxLength) {
				MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
				byte[] buf = md.digest(credentialId);
				seriesString = Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
			}
			return seriesString;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public AuthToken getAuthTokenByCredentialId(byte[] credentialId) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		return authTokenStore().getBySeries(tenantId, getType(), credentialIdToSeriesString(credentialId));
	}

	public void updateCredentialRecord(AuthToken at, CredentialRecord credentialRecord, Timestamp lastLoginDate) {
		CredentialRecordMement credentialRecordDetails = new CredentialRecordMement(credentialRecord);
		credentialRecordDetails.setAuthenticatorDisplayName(((CredentialRecordMement) at.getDetails()).getAuthenticatorDisplayName());
		credentialRecordDetails.setLastLoginDate(lastLoginDate);

		AuthToken updatedAt = new AuthToken(
				at.getTenantId(), at.getType(), at.getOwnerId(), at.getSeries(), at.getToken(), at.getPolicyName(), at.getStartDate(),
				credentialRecordDetails);
		authTokenStore().update(updatedAt, at);
	}

	public int countCredentialRecordsByUser(String userUniqueId) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		List<AuthToken> authTokens = authTokenStore().getByOwner(tenantId, getType(), userUniqueId);
		if (authTokens == null) {
			return 0;
		} else {
			return authTokens.size();
		}
	}

}
