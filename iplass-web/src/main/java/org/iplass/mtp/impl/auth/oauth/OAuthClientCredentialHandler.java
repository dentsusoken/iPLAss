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
package org.iplass.mtp.impl.auth.oauth;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.login.IdPasswordCredential;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.auth.authenticate.token.AuthToken;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenHandler;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenService;
import org.iplass.mtp.impl.auth.authenticate.token.AuthTokenStore;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthClient.OAuthClientRuntime;
import org.iplass.mtp.impl.auth.oauth.MetaOAuthResourceServer.OAuthResourceServerRuntime;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OAuthClientCredentialHandler extends AuthTokenHandler {
	private static Logger logger = LoggerFactory.getLogger("mtp.auth.oauth");

	public static final String TYPE_CLIENT = "OC";
	public static final String TYPE_RESOURCE_SERVER = "ORS";
	public static final String TYPE_POST_FIX_OLD = "$OLD";
	
	private AuthTokenStore oldCredentialStore;

	private int oldCredentialValidDays;
	
	public int getOldCredentialValidDays() {
		return oldCredentialValidDays;
	}

	public void setOldCredentialValidDays(int oldCredentialValidDays) {
		this.oldCredentialValidDays = oldCredentialValidDays;
	}
	
	private String metaDataId(String clientId) {
		if (TYPE_CLIENT.equals(getType())) {
			OAuthClientRuntime c = ServiceRegistry.getRegistry().getService(OAuthClientService.class).getRuntimeByName(clientId);
			if (c == null) {
				return null;
			}
			return c.getMetaData().getId();
		} else if (TYPE_RESOURCE_SERVER.equals(getType())) {
			OAuthResourceServerRuntime r = ServiceRegistry.getRegistry().getService(OAuthResourceServerService.class).getRuntimeByName(clientId);
			if (r == null) {
				return null;
			}
			return r.getMetaData().getId();
		} else {
			return null;
		}
	}

	public Credential generateCredential(String clientId) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String clientMetaDataId = metaDataId(clientId);
		if (clientMetaDataId == null) {
			throw new IllegalArgumentException("invalid clientId or clientType:" + clientId + ", " + getType());
		}
		
		if (oldCredentialValidDays > 0) {
			AuthToken current = authTokenStore().getBySeries(tenantId, getType(), clientMetaDataId);
			if (current != null) {
				List<AuthToken> ats = oldCredentialStore.getByOwner(tenantId, getType() + TYPE_POST_FIX_OLD, clientMetaDataId);
				int nextIndex = nextIndex(ats);
				current.setSeries(current.getSeries() + "$" + nextIndex);
				current.setStartDate(new Timestamp(System.currentTimeMillis()));
				current.setType(current.getType() + TYPE_POST_FIX_OLD);
				oldCredentialStore.create(current);
			}
		}
		
		authTokenStore().deleteBySeries(tenantId, getType(), clientMetaDataId);
		AuthToken at = newAuthToken(clientMetaDataId, null, new OAuthClientCredentialHandler.OAuthClientCredentialInfo(getType()));
		authTokenStore().create(at);
		if (logger.isInfoEnabled()) {
			logger.info(clientId + ":" + getType() + ",generateClientSecret,success");
		}
		return new IdPasswordCredential(clientId, at.getToken());
	}
	
	private int nextIndex(List<AuthToken> ats) {
		int max = -1;
		if (ats != null) {
			for (AuthToken at: ats) {
				String series = at.getSeries();
				int i = Integer.parseInt(series.substring(series.lastIndexOf('$') + 1));
				if (max < i) {
					max = i;
				}
			}
		}
		return max + 1;
	}
	
	
	public boolean validateCredential(Credential cre, String clientId) {
		if (!(cre instanceof IdPasswordCredential)) {
			throw new OAuthRuntimeException("Currently, only IdPasswordCredential is supported.");
		}
		
		if (!cre.getId().equals(clientId)) {
			if (logger.isWarnEnabled()) {
				logger.warn(clientId + ",clientValidate,fail");
			}
			return false;
		}
		String clientMetaDataId = metaDataId(clientId);
		if (clientMetaDataId == null) {
			if (logger.isWarnEnabled()) {
				logger.warn(clientId + ",clientValidate,fail");
			}
			return false;
		}
		
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		AuthToken stToken = authTokenStore().getBySeries(tenantId, getType(), clientMetaDataId);
		if (stToken != null) {
			if (checkTokenValid(((IdPasswordCredential) cre).getPassword(), stToken)) {
				return true;
			}
		}
		
		if (oldCredentialValidDays > 0) {
			//fallback old credentials
			List<AuthToken> ats = oldCredentialStore.getByOwner(tenantId, getType() + TYPE_POST_FIX_OLD, clientMetaDataId);
			if (ats != null) {
				for (AuthToken at: ats) {
					if (at.getStartDate().getTime() + TimeUnit.DAYS.toMillis(oldCredentialValidDays) > System.currentTimeMillis()) {
						if (checkTokenValid(((IdPasswordCredential) cre).getPassword(), at)) {
							return true;
						}
					}
				}
			}
		}
			
		if (logger.isWarnEnabled()) {
			logger.warn(clientId + ",clientValidate,fail");
		}
		return false;
	}
	
	public void deleteOldCredential(String clientId) {
		String clientMetaDataId = metaDataId(clientId);
		if (clientMetaDataId == null) {
			throw new IllegalArgumentException("invalid clientId or clientType:" + clientId + ", " + getType());
		}
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		oldCredentialStore.delete(tenantId, getType() + TYPE_POST_FIX_OLD, clientMetaDataId);
	}
	
	@Override
	public void inited(AuthTokenService service, Config config) {
		super.inited(service, config);
		setVisible(false);
		oldCredentialStore = service.getStore(getStore());
	}

	@Override
	public AuthTokenInfo toAuthTokenInfo(AuthToken authToken) {
		return null;
	}
	
	@Override
	protected Serializable createDetails(String seriesString, String tokenString, String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		return null;
	}
	
	@Override
	public Credential toCredential(AuthToken newToken) {
		return new IdPasswordCredential(newToken.getOwnerId(), newToken.getToken());
	}

	@Override
	public String newSeriesString(String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		return userUniqueId;
	}
	
	static class OAuthClientCredentialInfo implements AuthTokenInfo {
		
		private String type;
		
		OAuthClientCredentialInfo(String type) {
			this.type = type;
		}
		@Override
		public String getType() {
			return type;
		}
		@Override
		public String getKey() {
			return null;
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
	
}
