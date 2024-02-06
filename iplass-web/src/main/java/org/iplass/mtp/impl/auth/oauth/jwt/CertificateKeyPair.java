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

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class CertificateKeyPair {
	
	static final String JWK_PARAM_KID = "kid";
	static final String JWK_PARAM_ALG = "alg";
	static final String JWK_PARAM_USE = "use";
	static final String JWK_PARAM_KTY = "kty";
	static final String JWK_PARAM_X = "x";
	static final String JWK_PARAM_Y = "y";
	static final String JWK_PARAM_CRV = "crv";
	static final String JWK_PARAM_E = "e";
	static final String JWK_PARAM_N = "n";
	
	static final String USE_SIG = "sig";
	
	static final String KTY_RSA = "RSA";
	static final String KTY_EC = "EC";
	
	static final String ALG_NONE = "none";

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
	
	public CertificateKeyPair(Map<String, Object> jwkMap) throws InvalidKeyException {
		//publicKeyのみ対応
		this.keyId = (String) jwkMap.get(JWK_PARAM_KID);
		if (keyId == null) {
			throw new NullPointerException("keyId is null");
		}
		this.certificate = null;
		this.privateKey = null;
		String kty = (String) jwkMap.get(JWK_PARAM_KTY);
		if (KTY_RSA.equals(kty)) {
			try {
				BigInteger n = base64ToInt((String) jwkMap.get(JWK_PARAM_N));
				BigInteger e = base64ToInt((String) jwkMap.get(JWK_PARAM_E));
				RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
				KeyFactory keyFactory = KeyFactory.getInstance(KTY_RSA);
				this.publicKey = keyFactory.generatePublic(keySpec);
			} catch (NoSuchAlgorithmException | InvalidKeySpecException | RuntimeException e) {
				throw new InvalidKeyException("Invalid JWK parameter:" + jwkMap, e);
			}
		} else if (KTY_EC.equals(kty)) {
			try {
				BigInteger x = base64ToInt((String) jwkMap.get(JWK_PARAM_X));
				BigInteger y = base64ToInt((String) jwkMap.get(JWK_PARAM_Y));
				ECPoint point = new ECPoint(x, y);
				
				AlgorithmParameters algParams = AlgorithmParameters.getInstance(KTY_EC);
				EllipticCurveSpec crvSpec = EllipticCurveSpec.fromCurveName((String) jwkMap.get(JWK_PARAM_CRV));
				algParams.init(new ECGenParameterSpec(crvSpec.getStandardName()));
				ECParameterSpec ecParamSpec = algParams.getParameterSpec(ECParameterSpec.class);
				
				ECPublicKeySpec keySpec = new ECPublicKeySpec(point, ecParamSpec);
				KeyFactory keyFactory = KeyFactory.getInstance(KTY_EC);
				this.publicKey = keyFactory.generatePublic(keySpec);
			} catch (NoSuchAlgorithmException | InvalidParameterSpecException | InvalidKeySpecException | RuntimeException e) {
				throw new InvalidKeyException("Invalid JWK parameter:" + jwkMap, e);
			}
		} else {
			throw new InvalidKeyException("Unsupported key type:" + kty);
		}
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
		map.put(JWK_PARAM_KID, keyId);
		map.put(JWK_PARAM_KTY, privateKey.getAlgorithm());
		map.put(JWK_PARAM_USE, USE_SIG);
		map.put(JWK_PARAM_ALG, alg);
		
		if (publicKey instanceof RSAPublicKey) {
			RSAPublicKey rsaPubKey = (RSAPublicKey) publicKey;
			map.put(JWK_PARAM_N, intToBase64(rsaPubKey.getModulus(), -1));
			map.put(JWK_PARAM_E, intToBase64(rsaPubKey.getPublicExponent(), -1));
		} else if (publicKey instanceof ECPublicKey) {
			ECPublicKey ecPubKey = (ECPublicKey) publicKey;
			EllipticCurveSpec spec = EllipticCurveSpec.preferredSpec(ecPubKey.getParams().getOrder().bitLength());
			map.put(JWK_PARAM_CRV, spec.getCurveName());
			ECPoint w = ecPubKey.getW();
			map.put(JWK_PARAM_X, intToBase64(w.getAffineX(), spec.getOctetStringLength()));
			map.put(JWK_PARAM_Y, intToBase64(w.getAffineY(), spec.getOctetStringLength()));
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
	
	static BigInteger base64ToInt(String value) {
		return new BigInteger(1, Base64.getUrlDecoder().decode(value));
	}

}
