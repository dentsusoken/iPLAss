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

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

import org.iplass.mtp.auth.login.Credential;
import org.iplass.mtp.auth.token.AuthTokenInfo;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.util.random.SecureRandomGenerator;
import org.iplass.mtp.impl.util.random.SecureRandomService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceConfigrationException;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;

public abstract class AuthTokenHandler implements ServiceInitListener<AuthTokenService> {
	private static class RandomHolder {
		static final SecureRandomGenerator random = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator("authTokenGenerator");
	}
	
	private String store;
	private String type;
	private boolean visible = true;
	private List<HashSetting> hashSettings;
	private String secureRandomGeneratorName;

	private AuthTokenService service;
	private AuthTokenStore authTokenStore;
	private SecureRandomGenerator generator;

	public List<HashSetting> getHashSettings() {
		return hashSettings;
	}

	public void setHashSettings(List<HashSetting> hashSettings) {
		this.hashSettings = hashSettings;
	}

	public String getSecureRandomGeneratorName() {
		return secureRandomGeneratorName;
	}

	public void setSecureRandomGeneratorName(String secureRandomGeneratorName) {
		this.secureRandomGeneratorName = secureRandomGeneratorName;
	}

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
		
		if (hashSettings != null) {
			//check hash alg
			for (HashSetting hs: hashSettings) {
				try {
					MessageDigest.getInstance(hs.getHashAlgorithm());
				} catch (NoSuchAlgorithmException e) {
					throw new ServiceConfigrationException("invalid HashAlgorithm", e);
				}
			}
		}
		
		
		if (hashSettings != null && hashSettings.size() > 0) {
			this.authTokenStore = new HashingAuthTokenStore(service.getStore(store));
		} else {
			this.authTokenStore = service.getStore(store);
		}
		
		if (secureRandomGeneratorName != null) {
			generator = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator(secureRandomGeneratorName);
		}
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
	
	public String newTokenString(AuthTokenInfo tokenInfo) {
		if (generator != null) {
			return generator.secureRandomToken();
		} else {
			return RandomHolder.random.secureRandomToken();
		}
	}

	public String newSeriesString(String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		return RandomHolder.random.secureRandomToken();
	}
	
	public boolean checkTokenValid(String inputToken, AuthToken storeToken) {
		if (storeToken == null || inputToken == null) {
			return false;
		}
		
		if (hashSettings == null || hashSettings.size() == 0) {
			return storeToken.getToken().equals(inputToken);
		}
		
		String[] verHash = divVerAndHash(storeToken.getToken());
		HashSetting hs = selectSetting(verHash[0]);
		if (hs == null) {
			return verHash[1].equals(inputToken);
		} else {
			return verHash[1].equals(hs.hash(inputToken));
		}
	}

	private HashSetting selectSetting(String ver) {
		if (ver == null || ver.length() == 0 || hashSettings == null) {
			return null;
		}

		for (int i = hashSettings.size() - 1; i >= 0; i--) {
			HashSetting set = hashSettings.get(i);
			if (ver.equals(set.getVersion())) {
				return set;
			}
		}

		return null;
	}

	private String[] divVerAndHash(String token) {
		if (token != null && token.length() != 0 && token.charAt(0) == '$') {
			int index = token.indexOf('$', 1);
			return new String[]{token.substring(1, index), token.substring(index + 1)};
		} else {
			return new String[]{null, token};
		}
	}

	public AuthToken newAuthToken(String userUniqueId, String policyName, AuthTokenInfo tokenInfo) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		String seriesString = newSeriesString(userUniqueId, policyName, tokenInfo);
		String tokenString = newTokenString(tokenInfo);
		Serializable details = createDetails(seriesString, tokenString, userUniqueId, policyName, tokenInfo);
		return new AuthToken(tenantId, getType(), userUniqueId, seriesString, tokenString, policyName, new Timestamp(System.currentTimeMillis()), details);
	}
	
	protected abstract Serializable createDetails(String seriesString, String tokenString, String userUniqueId, String policyName, AuthTokenInfo tokenInfo);
	
	public abstract AuthTokenInfo toAuthTokenInfo(AuthToken authToken);
	
	public abstract Credential toCredential(AuthToken newToken);
	
	private class HashingAuthTokenStore implements AuthTokenStore {
		
		private AuthTokenStore store;
		
		private HashingAuthTokenStore(AuthTokenStore store) {
			this.store = store;
		}

		@Override
		public AuthToken getBySeries(int tenantId, String type, String series) {
			return store.getBySeries(tenantId, type, series);
		}

		@Override
		public List<AuthToken> getByOwner(int tenantId, String type, String userUniqueKey) {
			return store.getByOwner(tenantId, type, userUniqueKey);
		}
		
		private AuthToken hashToken(AuthToken token) {
			HashSetting hs = hashSettings.get(hashSettings.size() - 1);
			String hashedTokenString = "$" + hs.getVersion() + "$" + hs.hash(token.getToken());
			
			return new AuthToken(token.getTenantId(), token.getType(), token.getOwnerId(), token.getSeries(),
					hashedTokenString, token.getPolicyName(), token.getStartDate(), token.getDetails());
		}

		@Override
		public void create(AuthToken token) {
			store.create(hashToken(token));
		}

		@Override
		public void update(AuthToken newToken, AuthToken currentToken) {
			store.update(hashToken(newToken), currentToken);
		}

		@Override
		public void delete(int tenantId, String type, String userUniqueKey) {
			store.delete(tenantId, type, userUniqueKey);
		}

		@Override
		public void deleteBySeries(int tenantId, String type, String series) {
			store.deleteBySeries(tenantId, type, series);
		}

		@Override
		public void deleteByDate(int tenantId, String type, Timestamp ts) {
			store.deleteByDate(tenantId, type, ts);
		}
	}

}
