/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth.idtoken;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationCode;
import org.iplass.mtp.impl.auth.oauth.jwt.CertificateKeyPair;
import org.iplass.mtp.impl.auth.oauth.jwt.JwtProcessor;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;
import org.iplass.mtp.impl.auth.oauth.util.IdTokenConstants;
import org.iplass.mtp.impl.auth.oauth.util.OAuthUtil;

public class IdToken {
	
	private Map<String, Object> userInfoClaims;//includes sub claims
	
	/**
	 * Audience(s)
	 */
	private String aud;
	
	/**
	 * Expiration time. seconds from 1970-01-01T0:0:0Z
	 */
	private long exp;
	
	/**
	 * Time at which the JWT was issued. seconds from 1970-01-01T0:0:0Z
	 */
	private long iat;
	
	/**
	 * Time when the End-User authentication occurred. seconds from 1970-01-01T0:0:0Z
	 */
	private long authTime;
	
	/**
	 * nonce
	 */
	private String nonce;
	
	//currently not supported
	//acr
	//amr
	//azp
	
	/**
	 * Access Token for at_hash
	 */
	private String at;
	
	
	/**
	 * Code for c_hash
	 */
	private String c;
	
	private OAuthAuthorizationService service;
	
	public IdToken(AuthorizationCode code, AccessToken accessToken, OAuthAuthorizationRuntime server, OAuthClientRuntime client, OAuthAuthorizationService service) {
		this.service = service;
		
		userInfoClaims = server.userInfo(accessToken, client);
		
		aud = client.getMetaData().getName();
		iat = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		exp = iat + service.getIdTokenLifetimeSeconds();
		authTime = code.getRequest().getAuthTime();
		nonce = code.getRequest().getNonce();
		at = accessToken.getTokenEncoded();
		c = code.getCodeValue();
	}
	
	public String getTokenEncoded(String issuerId) {
		
		JwtProcessor processor = service.getJwtProcessor();
		if (service.getJwtKeyStore() == null) {
			throw new NullPointerException("jwtKeyStore not defined on OAuthAuthorizationService.");
		}
		CertificateKeyPair key = service.getJwtKeyStore().getCertificateKeyPair();
		
		Map<String, Object> claims = new HashMap<>(userInfoClaims);
		claims.put(IdTokenConstants.CLAIM_ISS, issuerId);
		claims.put(IdTokenConstants.CLAIM_AUD, aud);
		claims.put(IdTokenConstants.CLAIM_EXP, exp);
		claims.put(IdTokenConstants.CLAIM_IAT, iat);
		claims.put(IdTokenConstants.CLAIM_AUTH_TIME, authTime);
		if (nonce != null) {
			claims.put(IdTokenConstants.CLAIM_NONCE, nonce);
		}
		
		String jwtSignAlg = processor.preferredAlgorithm(key);
		if (at != null) {
			claims.put(IdTokenConstants.CLAIM_AT_HASH, OAuthUtil.atHash(at, jwtSignAlg));
		}
		if (c != null) {
			claims.put(IdTokenConstants.CLAIM_C_HASH, OAuthUtil.cHash(c, jwtSignAlg));
		}
		
		return processor.encode(claims, key);
	}
	
	public Map<String, Object> getUserInfoClaims() {
		return userInfoClaims;
	}

	public String getAud() {
		return aud;
	}

	public long getExp() {
		return exp;
	}

	public long getIat() {
		return iat;
	}

	public long getAuthTime() {
		return authTime;
	}

	public String getNonce() {
		return nonce;
	}

	public String getAt() {
		return at;
	}

	public String getC() {
		return c;
	}

}
