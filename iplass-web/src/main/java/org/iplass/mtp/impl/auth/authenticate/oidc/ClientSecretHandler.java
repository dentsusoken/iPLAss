/*
 * Copyright (C) 2022 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authenticate.oidc;

import java.io.Serializable;
import java.sql.Timestamp;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;

public class ClientSecretHandler extends AuthTokenHandler {
	public static final String TYPE_OIDC_CLIENT_SECRET = "OIDCCS";
	
	private static class ClientSecretInfo implements AuthTokenInfo {
		
		private String type;
		private String metaDataId;
		private String secret;
		
		private ClientSecretInfo(String type, String metaDataId, String secret) {
			this.type = type;
			this.metaDataId = metaDataId;
			this.secret = secret;
		}

		@Override
		public String getType() {
			return type;
		}

		@Override
		public String getKey() {
			return metaDataId;
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public Timestamp getStartDate() {
			return null;
		}
		
	}
	
	String getClientSecret(String metaDataId) {
		AuthToken at = authTokenStore().getBySeries(ExecuteContext.getCurrentContext().getClientTenantId(), getType(), metaDataId);
		if (at != null) {
			return at.getToken();
		}
		return null;
	}
	
	void saveClientSecret(String metaDataId, String clientSecret) {
		authTokenStore().deleteBySeries(ExecuteContext.getCurrentContext().getClientTenantId(), getType(), metaDataId);
		AuthToken at = newAuthToken(metaDataId, null, new ClientSecretInfo(getType(), metaDataId, clientSecret));
		authTokenStore().create(at);
	}

	void deleteClientSecret(String metaDataId) {
		authTokenStore().deleteBySeries(ExecuteContext.getCurrentContext().getClientTenantId(), getType(), metaDataId);
	}

	@Override
	public void inited(AuthTokenService service, Config config) {
		super.inited(service, config);
		if (getType() == null) {
			setType(TYPE_OIDC_CLIENT_SECRET);
		}
		setVisible(false);
	}
	
	@Override
	protected Serializable createDetails(String seriesString, String tokenString, String userUniqueId,
			String policyName, AuthTokenInfo tokenInfo) {
		return null;
	}

	@Override
	public AuthTokenInfo toAuthTokenInfo(AuthToken authToken) {
		return null;
	}

	@Override
	public Credential toCredential(AuthToken newToken) {
		return new IdPasswordCredential(newToken.getSeries(), newToken.getToken());
	}

	@Override
	public String newTokenString(AuthTokenInfo tokenInfo) {
		return ((ClientSecretInfo) tokenInfo).secret;
	}

	@Override
	public String newSeriesString(String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		return ((ClientSecretInfo) tokenInfo).metaDataId;
	}

}
