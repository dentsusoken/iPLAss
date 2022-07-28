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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.iplass.mtp.impl.auth.authenticate.oidc.OpenIdConnectService;

public class LocalJwks extends Jwks {
	
	private Map<String, Map<String, Object>> jwks;

	public LocalJwks(String jwksContents, OpenIdConnectService opService) {
		super(opService);
		jwks = toJwksMap(jwksContents);
	}

	@Override
	public Map<String, Object> get(String kid) {
		return jwks.get(kid);
	}

	@Override
	public List<String> kidList() {
		ArrayList<String> ret = new ArrayList<>();
		jwks.forEach((k, v) -> {
			ret.add((String) v.get(JWK_PARAM_KID));
		});
		return ret;
	}

}
