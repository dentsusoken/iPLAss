/*
 * Copyright (C) 2018 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authenticate.rememberme;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.rememberme.RememberMeTokenCredential;
import org.iplass.mtp.auth.login.rememberme.RememberMeTokenInfo;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;

public class RememberMeTokenHandler extends AuthTokenHandler {
	
	public static final String TYPE_REMME_DEFAULT = "REMME";
	
	private AuthenticationPolicyService policyService;

	@Override
	public void inited(AuthTokenService service, Config config) {
		super.inited(service, config);
		if (getType() == null) {
			setType(TYPE_REMME_DEFAULT);
		}
		this.policyService = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
	}

	@Override
	public AuthTokenInfo toAuthTokenInfo(AuthToken authToken) {
		RememberMeTokenInfo info = new RememberMeTokenInfo();
		info.setType(authToken.getType());
		info.setKey(authToken.getSeries());
		
		Timestamp t = authToken.getStartDate();
		AuthenticationPolicyRuntime pol = policyService.getOrDefault(authToken.getPolicyName());
		if (pol.getMetaData().getRememberMePolicy() == null) {
			info.setExpired(true);
		} else {
			if (pol.getMetaData().getRememberMePolicy().getLifetimeMinutes() > 0) {
				Timestamp exp = new Timestamp(t.getTime() + TimeUnit.MINUTES.toMillis(pol.getMetaData().getRememberMePolicy().getLifetimeMinutes()));
				if (exp.getTime() >= System.currentTimeMillis()) {
					info.setExpiryDate(exp);
				} else {
					info.setExpired(true);
				}
			}
		}
		return info;
	}
	
	@Override
	public Credential toCredential(AuthToken newToken) {
		return new RememberMeTokenCredential(newToken.encodeToken());
	}

	@Override
	protected Serializable createDetails(String seriesString, String tokenString, String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		return null;
	}

}
