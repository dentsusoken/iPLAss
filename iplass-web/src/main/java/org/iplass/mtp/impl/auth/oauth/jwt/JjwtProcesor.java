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
package org.iplass.mtp.impl.auth.oauth.jwt;

import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
	public String encode(Map<String, Object> claims, CertificateKeyPair key) {
		return Jwts.builder()
				.addClaims(claims)
				.setHeaderParam("kid", key.getKeyId())
				.signWith(key.getPrivateKey())
				.compact();
	}

	@Override
	public String preferredAlgorithm(CertificateKeyPair key) {
		SignatureAlgorithm alg = SignatureAlgorithm.forSigningKey(key.getPrivateKey());
		if (useRsaSsaPss) {
			if (alg == SignatureAlgorithm.RS256) {
				alg = SignatureAlgorithm.PS256;
			} else if (alg == SignatureAlgorithm.RS384) {
				alg = SignatureAlgorithm.PS384;
			} else if (alg == SignatureAlgorithm.RS512) {
				alg = SignatureAlgorithm.PS512;
			}
		}
		return alg.getValue();
	}

}
