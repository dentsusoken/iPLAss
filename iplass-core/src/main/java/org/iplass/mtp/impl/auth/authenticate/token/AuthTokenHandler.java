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
package org.iplass.mtp.impl.auth.authenticate.token;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;

public abstract class AuthTokenHandler implements ServiceInitListener<AuthTokenService> {
	private String store;
	private String type;
	private boolean visible = true;

	private AuthTokenService service;
	private AuthTokenStore authTokenStore;

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void inited(AuthTokenService service, Config config) {
		this.service = service;
		this.authTokenStore = service.getStore(store);
	}

	@Override
	public void destroyed() {
	}

	public AuthTokenStore authTokenStore() {
		return authTokenStore;
	}

	public AuthTokenService getService() {
		return service;
	}

	public abstract AuthTokenInfo toAuthTokenInfo(AuthToken authToken);
	public abstract AuthToken newAuthToken(String userUniqueId, String policyName, AuthTokenInfo tokenInfo);
	public abstract Credential toCredential(AuthToken newToken);

}
