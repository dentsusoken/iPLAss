/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.oauth.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import org.iplass.mtp.SystemException;

public class OAuthUtil {
	
	public static String encodeRfc3986(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
		} catch (UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
	}
	
	public static String calcCodeChallenge(String codeChallengeMethod, String codeVerifier) {
		if (codeVerifier == null) {
			return null;
		}
		if (codeChallengeMethod == null) {
			//code_verifier == code_challenge.
			return codeVerifier;
		} else switch (codeChallengeMethod) {
		case OAuthConstants.CODE_CHALLENGE_METHOD_PLAIN:
			//code_verifier == code_challenge.
			return codeVerifier;
		case OAuthConstants.CODE_CHALLENGE_METHOD_S256:
			//BASE64URL-ENCODE(SHA256(ASCII(code_verifier))) == code_challenge
			try {
				return Base64.getUrlEncoder().withoutPadding().encodeToString(
						MessageDigest.getInstance("SHA-256").digest(codeVerifier.getBytes("UTF-8")));
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				throw new SystemException(e);
			}
		default:
			return null;
		}
		
	}
	
	public static String atHash(String accessToken, String jwtSignAlg) {
		return hashValue(accessToken, jwtSignAlg);
	}
	
	public static String cHash(String code, String jwtSignAlg) {
		return hashValue(code, jwtSignAlg);
	}

	private static String hashValue(String target, String jwtSignAlg) {
		try {
			MessageDigest md = MessageDigest.getInstance(hashAlg(jwtSignAlg));
			byte[] b = md.digest(target.getBytes("UTF-8"));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(Arrays.copyOf(b, (b.length / 2)));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new SystemException(e);
		}
	}
	
	private static String hashAlg(String jwtSignAlg) {
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

}
