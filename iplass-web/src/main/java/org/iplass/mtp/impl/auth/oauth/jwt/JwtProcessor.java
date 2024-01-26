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

import java.util.Map;
import java.util.function.Function;

import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.spi.ServiceRegistry;

public interface JwtProcessor {
	
	public static JwtProcessor getInstance() {
		//TODO OAuthAuthorizationServiceから分離する
		return ServiceRegistry.getRegistry().getService(OAuthAuthorizationService.class).getJwtProcessor();
	}
	
	public String preferredAlgorithm(CertificateKeyPair key) throws InvalidKeyException;
	public void checkValidVerificationKey(String alg, CertificateKeyPair key) throws InvalidKeyException;
	public String encode(Map<String, Object> claims, CertificateKeyPair key) throws InvalidKeyException;
	public Jwt decode(String jwt, int allowedClockSkewMinutes, Function<String, Map<String, Object>> jwkResolver) throws InvalidKeyException, InvalidJwtException;

}
