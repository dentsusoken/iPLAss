/*
 * Copyright (C) 2022 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.oidc.jwks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

public abstract class Jwks {
	
	static final String JWK_PARAM_KID = "kid";
	static final String JWKS_PARAM_KEYS = "keys";
	
	protected OpenIdConnectService service;
	
	Jwks(OpenIdConnectService service) {
		this.service = service;
	}
	
	public abstract Map<String, Object> get(String kid);
	
	public abstract List<String> kidList();
	
	@SuppressWarnings("unchecked")
	protected Map<String, Map<String, Object>> toJwksMap(String content) {
		try {
			Map<String, Object> parsed = service.getObjectMapper().readValue(content, new TypeReference<Map<String, Object>>() {});
			
			Map<String, Map<String, Object>> ret = new HashMap<>();
			List<Map<String, Object>> jwkList = (List<Map<String, Object>>) parsed.get(JWKS_PARAM_KEYS);
			if (jwkList != null) {
				for (Map<String, Object> jwk: jwkList) {
					ret.put((String) jwk.get(JWK_PARAM_KID), jwk);
				}
			}
			return ret;
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("can't parse JWKS contents", e);
		}
	}
	
}
