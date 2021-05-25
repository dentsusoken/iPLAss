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
package org.iplass.mtp.impl.auth;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.auth.token.AuthTokenInfoList;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;

public class AuthTokenInfoListImpl implements AuthTokenInfoList {
	
	private UserContext userContext;
	private AuthTokenService tokenService;
	
	public AuthTokenInfoListImpl(UserContext userContext) {
		this.userContext = userContext;
		this.tokenService = ServiceRegistry.getRegistry().getService(AuthTokenService.class);
	}

	@Override
	public List<AuthTokenInfo> getList() {
		
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String userUniqueKey = userContext.getAccount().getUnmodifiableUniqueKey();
		
		List<AuthTokenInfo> atiList = new ArrayList<>();
		for (AuthTokenHandler ah: tokenService.getHandlers()) {
			if (ah.isVisible()) {
				List<AuthToken> sList = ah.authTokenStore().getByOwner(tenantId, ah.getType(), userUniqueKey);
				if (sList != null) {
					for (AuthToken at: sList) {
						atiList.add(ah.toAuthTokenInfo(at));
					}
				}
			}
		}
		
		return atiList;
	}

	@Override
	public AuthTokenInfo get(String type, String key) {
		AuthTokenHandler handler = tokenService.getHandler(type);
		if (handler == null) {
			return null;
		}
		if (!handler.isVisible()) {
			return null;
		}
		
		AuthToken t = handler.authTokenStore().getBySeries(
				ExecuteContext.getCurrentContext().getClientTenantId(), type, key);
		return handler.toAuthTokenInfo(t);
	}
	
	@Override
	public void remove(String type) {
		AuthTokenHandler handler = tokenService.getHandler(type);
		if (handler != null && handler.isVisible()) {
			handler.authTokenStore().delete(ExecuteContext.getCurrentContext().getClientTenantId(), type, ExecuteContext.getCurrentContext().getClientId());
		}
	}

	@Override
	public void remove(String type, String key) {
		AuthTokenHandler handler = tokenService.getHandler(type);
		if (handler != null && handler.isVisible()) {
			handler.authTokenStore().deleteBySeries(ExecuteContext.getCurrentContext().getClientTenantId(), type, key);
		}
	}

	@Override
	public Credential generateNewToken(AuthTokenInfo newTokenInfo) {
		AuthTokenHandler handler = tokenService.getHandler(newTokenInfo.getType());
		if (handler == null) {
			throw new IllegalArgumentException("type:" + newTokenInfo.getType() + " undefined.");
		}
		if (!handler.isVisible()) {
			throw new IllegalArgumentException("type:" + newTokenInfo.getType() + " is invisible.");
		}
		
		AuthToken newToken = handler.newAuthToken(userContext.getAccount().getUnmodifiableUniqueKey(), userContext.getUser().getAccountPolicy(), newTokenInfo);
		handler.authTokenStore().create(newToken);
		return handler.toCredential(newToken);
	}

}
