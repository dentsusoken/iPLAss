/*
 * Copyright (C) 2018 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.simpletoken;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenCredential;
import org.iplass.mtp.auth.login.token.SimpleAuthTokenInfo;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.spi.Config;

public class SimpleAuthTokenHandler extends AuthTokenHandler {
	
	public static final String TYPE_SIMPLE_DEFAULT = "SAT";
	
	@Override
	public void inited(AuthTokenService service, Config config) {
		super.inited(service, config);
		if (getType() == null) {
			setType(TYPE_SIMPLE_DEFAULT);
		}
	}

	@Override
	public AuthTokenInfo toAuthTokenInfo(AuthToken authToken) {
		SimpleAuthTokenInfo info = new SimpleAuthTokenInfo();
		info.setType(authToken.getType());
		info.setKey(authToken.getSeries());
		info.setStartDate(authToken.getStartDate());
		
		@SuppressWarnings("unchecked")
		Map<String, Object> details = (Map<String, Object>) authToken.getDetails();
		if (details != null) {
			info.setApplication((String) details.get("application"));
		}
		
		return info;
	}
	
	@Override
	public Credential toCredential(AuthToken newToken) {
		return new SimpleAuthTokenCredential(newToken.encodeToken());
	}

	@Override
	protected Serializable createDetails(String seriesString, String tokenString, String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		SimpleAuthTokenInfo sat = (SimpleAuthTokenInfo) tokenInfo;
		HashMap<String, Object> details = null;
		if (sat.getApplication() != null) {
			details = new HashMap<>();
			details.put("application", sat.getApplication());
		}
		return details;
	}

}
