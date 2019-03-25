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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.SystemException;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthAuthorization.OAuthAuthorizationRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.auth.oauth.code.AuthorizationCode;
import org.iplass.mtp.impl.auth.oauth.jwt.CertificateKeyPair;
import org.iplass.mtp.impl.auth.oauth.jwt.JwtProcessor;
import org.iplass.mtp.impl.auth.oauth.token.AccessToken;

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
		claims.put("iss", issuerId);
		claims.put("aud", aud);
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("auth_time", authTime);
		if (nonce != null) {
			claims.put("nonce", nonce);
		}
		
		if (at != null || c != null) {
			String jwtSignAlg = processor.preferredAlgorithm(key);
			String hashAlg = hashAlg(jwtSignAlg);
			try {
				MessageDigest md = MessageDigest.getInstance(hashAlg);
				if (at != null) {
					claims.put("at_hash", hashValue(at, md));
				}
				if (c != null) {
					claims.put("c_hash", hashValue(c, md));
				}
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				throw new SystemException(e);
			}
		}
		
		return processor.encode(claims, key);
	}
	
	private String hashValue(String target, MessageDigest md) throws UnsupportedEncodingException {
		byte[] b = md.digest(target.getBytes("UTF-8"));
		return Base64.getUrlEncoder().withoutPadding().encodeToString(Arrays.copyOf(b, (b.length / 2)));
	}
	
	private String hashAlg(String jwtSignAlg) {
		switch (jwtSignAlg) {
		case "HS256":
		case "ES256":
		case "RS256":
		case "PS256":
			return "SHA-256";
		case "HS384":
		case "ES384":
		case "RS384":
		case "PS384":
			return "SHA-384";
		case "HS512":
		case "ES512":
		case "RS512":
		case "PS512":
			return "SHA-512";
		default:
			throw new IllegalArgumentException("unknown jwtSignAlg:" + jwtSignAlg);
		}
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
