/*
 * Copyright (C) 2019 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.oauth.jwt;

import java.security.Key;
import java.security.PrivateKey;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolverAdapter;

/**
 * jjwtを利用したJwtProcessorの実装。
 * 
 * @author K.Higuchi
 *
 */
public class JjwtProcesor implements JwtProcessor {
	
	private boolean useRsaSsaPss;
	
	public boolean isUseRsaSsaPss() {
		return useRsaSsaPss;
	}

	/**
	 * RSAベースの署名アルゴリズムに、
	 * RSASSA-PKCS1-v1_5(RS256,RS384,RS512)ではなくRSASSA-PSS(PS256,PS384,PS512)を利用する場合にtrueを指定。
	 * 
	 * @param useRsaSsaPss
	 */
	public void setUseRsaSsaPss(boolean useRsaSsaPss) {
		this.useRsaSsaPss = useRsaSsaPss;
	}

	@Override
	public String encode(Map<String, Object> claims, CertificateKeyPair key) throws InvalidKeyException {
		try {
			return Jwts.builder()
					.addClaims(claims)
					.setHeaderParam("kid", key.getKeyId())
					.signWith(key.getPrivateKey(), forSigningKey(key.getPrivateKey()))
					.compact();
		} catch (io.jsonwebtoken.security.InvalidKeyException e) {
			throw new InvalidKeyException(e.getMessage(), e);
		}
	}
	
	private SignatureAlgorithm forSigningKey(PrivateKey key) {
		SignatureAlgorithm alg = SignatureAlgorithm.forSigningKey(key);
		if (useRsaSsaPss) {
			if (alg == SignatureAlgorithm.RS256) {
				alg = SignatureAlgorithm.PS256;
			} else if (alg == SignatureAlgorithm.RS384) {
				alg = SignatureAlgorithm.PS384;
			} else if (alg == SignatureAlgorithm.RS512) {
				alg = SignatureAlgorithm.PS512;
			}
		}
		return alg;
	}

	@Override
	public String preferredAlgorithm(CertificateKeyPair key) throws InvalidKeyException {
		try {
			SignatureAlgorithm alg = forSigningKey(key.getPrivateKey());
			return alg.getValue();
		} catch (io.jsonwebtoken.security.InvalidKeyException e) {
			throw new InvalidKeyException(e.getMessage(), e);
		}
	}

	@Override
	public void checkValidVerificationKey(String algName, CertificateKeyPair key) throws InvalidKeyException {
		try {
			SignatureAlgorithm alg = SignatureAlgorithm.forName(algName);
			alg.assertValidVerificationKey(key.getPublicKey());
		} catch (io.jsonwebtoken.security.SignatureException | io.jsonwebtoken.security.InvalidKeyException e) {
			throw new InvalidKeyException(e.getMessage(), e);
		}
	}

	@Override
	public Jwt decode(String jwt, int allowedClockSkewMinutes, Function<String, Map<String, Object>> jwkResolver) throws InvalidKeyException, InvalidJwtException {
		try {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKeyResolver(new SigningKeyResolverAdapter() {
				@Override
				public Key resolveSigningKey(@SuppressWarnings("rawtypes") JwsHeader jwsHeader, Claims claims) {
					String keyId = jwsHeader.getKeyId();
					Map<String, Object> jwk = jwkResolver.apply(keyId);
					if (jwk == null) {
						throw new InvalidJwtException("JWK is not defined for specific keyId:" + keyId);
					}
					
					String use = (String) jwk.get(CertificateKeyPair.JWK_PARAM_USE);
					if (use != null) {
						if (!use.equals(CertificateKeyPair.USE_SIG)) {
							throw new InvalidKeyException("invalid use parameter:" + use);
						}
					}
					
					String alg = (String) jwk.get(CertificateKeyPair.JWK_PARAM_ALG);
					if (alg != null) {
						//jwk側の定義が正
						String algFromHeader = jwsHeader.getAlgorithm();
						if (algFromHeader != null) {
							if (!alg.equalsIgnoreCase(algFromHeader)) {
								throw new InvalidJwtException("alg parameter unmatch:" + algFromHeader);
							}
						}
					} else {
						//jwtのalgで（後続処理でKeyとの整合性チェック）
						alg = jwsHeader.getAlgorithm();
					}
					
					if (alg == null || alg.equalsIgnoreCase(CertificateKeyPair.ALG_NONE)) {
						throw new InvalidJwtException("alg parameter unspecified or none specified:" + alg);
					}
					
					CertificateKeyPair key = new CertificateKeyPair(jwk);
					checkValidVerificationKey(alg, key);
					
					return key.getPublicKey();
				}
			})
			.setAllowedClockSkewSeconds(TimeUnit.MINUTES.toSeconds(allowedClockSkewMinutes))
			.build()
			.parseClaimsJws(jwt);
			
			return new Jwt(claims.getHeader(), claims.getBody());
		} catch (io.jsonwebtoken.security.InvalidKeyException e) {
			throw new InvalidKeyException(e.getMessage(), e);
		} catch (JwtException e) {
			throw new InvalidJwtException(e.getMessage(), e);
		}
	}

}
