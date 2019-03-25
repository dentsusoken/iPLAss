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

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECPoint;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class CertificateKeyPair {
	private final String keyId;
	private final PrivateKey privateKey;
	private final PublicKey publicKey;
	private final X509Certificate certificate;
	
	public CertificateKeyPair(String keyId, X509Certificate certificate, PrivateKey privateKey) {
		this.keyId = keyId;
		this.certificate = certificate;
		this.privateKey = privateKey;
		this.publicKey = certificate.getPublicKey();
	}
	
	public CertificateKeyPair(String keyId, PublicKey publicKey, PrivateKey privateKey) {
		this.keyId = keyId;
		this.certificate = null;
		this.privateKey = privateKey;
		this.publicKey = publicKey;
	}
	
	public String getKeyId() {
		return keyId;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}
	
	public PublicKey getPublicKey() {
		return publicKey;
	}

	public X509Certificate getCertificate() {
		return certificate;
	}
	
	public Map<String, Object> toPublicJwkMap(String alg) {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("kid", keyId);
		map.put("kty", privateKey.getAlgorithm());
		map.put("use", "sig");
		map.put("alg", alg);
		
		if (publicKey instanceof RSAPublicKey) {
			RSAPublicKey rsaPubKey = (RSAPublicKey) publicKey;
			map.put("n", intToBase64(rsaPubKey.getModulus(), -1));
			map.put("e", intToBase64(rsaPubKey.getPublicExponent(), -1));
		} else if (publicKey instanceof ECPublicKey) {
			ECPublicKey ecPubKey = (ECPublicKey) publicKey;
			EllipticCurveSpec spec = EllipticCurveSpec.preferredSpec(ecPubKey.getParams().getOrder().bitLength());
			map.put("crv", spec.getCurveName());
			ECPoint w = ecPubKey.getW();
			map.put("x", intToBase64(w.getAffineX(), spec.getOctetStringLength()));
			map.put("y", intToBase64(w.getAffineY(), spec.getOctetStringLength()));
		}

		return map;
	}
	
	static String intToBase64(BigInteger num, int length) {
		byte[] b = num.toByteArray();

		//signのみによって桁上がりしてしまった場合、削除
		if (num.bitLength() % 8 == 0
				&& b[0] == 0
				&& b.length != 0) {
			byte[] bb = new byte[b.length - 1];
			System.arraycopy(b, 1, bb, 0, bb.length);
			b = bb;
		}
		
		//バイト長指定されている場合、それに足りない場合に0パディング
		if (length > 0 && length > b.length) {
			byte[] bb = new byte[length];
			System.arraycopy(b, 0, bb, length - b.length, b.length);
			b = bb;
		}
		
		return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
	}

}
