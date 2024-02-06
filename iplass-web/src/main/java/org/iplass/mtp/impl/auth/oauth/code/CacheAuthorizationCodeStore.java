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
package org.iplass.mtp.impl.auth.oauth.code;

import org.iplass.mtp.impl.auth.oauth.OAuthAuthorizationService;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.util.random.SecureRandomGenerator;
import org.iplass.mtp.impl.util.random.SecureRandomService;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * CacheStoreベースのAuthorizationCodeStore。
 * 
 * @author K.Higuchi
 *
 */
public class CacheAuthorizationCodeStore implements AuthorizationCodeStore, ServiceInitListener<OAuthAuthorizationService> {
	public static final String DEFAULT_CACHE_NAMESPACE = "mtp.oauth.codeStore";
	public static final String DEFAULT_SECURE_RANDOM_GENERATOR_NAME = "oauthAuthorizationCodeGenerator";
	
	private String secureRandomGeneratorName = DEFAULT_SECURE_RANDOM_GENERATOR_NAME;
	private String cacheStoreName = DEFAULT_CACHE_NAMESPACE;

	private long timeToLive = 3 * 60 * 1000;//default 3 min.
	
	private CacheStore cache;
	private SecureRandomGenerator generator;
	
	public String getCacheStoreName() {
		return cacheStoreName;
	}

	public void setCacheStoreName(String cacheStoreName) {
		this.cacheStoreName = cacheStoreName;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}

	public String getSecureRandomGeneratorName() {
		return secureRandomGeneratorName;
	}

	public void setSecureRandomGeneratorName(String secureRandomGeneratorName) {
		this.secureRandomGeneratorName = secureRandomGeneratorName;
	}

	@Override
	public void inited(OAuthAuthorizationService service, Config config) {
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		cache = cs.getCache(cacheStoreName);
		generator = ServiceRegistry.getRegistry().getService(SecureRandomService.class).createGenerator(secureRandomGeneratorName);
	}

	@Override
	public void destroyed() {
	}
	
	private String toKey(String code) {
		int tenantId = ExecuteContext.getCurrentContext().getClientTenantId();
		StringBuilder sb = new StringBuilder();
		sb.append(tenantId).append("-").append(code);
		return sb.toString();
	}

	@Override
	public AuthorizationCode newAuthorizationCode(AuthorizationRequest authReq) {
		while (true) {
			String codeValue = generator.secureRandomToken();
			AuthorizationCode code = new AuthorizationCode();
			code.setCodeValue(codeValue);
			code.setRequest(authReq);
			code.setExpires(System.currentTimeMillis() + timeToLive);
			CacheEntry ce = new CacheEntry(toKey(codeValue), code);
			if (cache.putIfAbsent(ce) == null) {
				return code;
			}
		}
	}

	@Override
	public AuthorizationCode getAndRemoveAuthorizationCode(String codeValue) {
		CacheEntry ce = cache.remove(toKey(codeValue));
		if (ce != null) {
			AuthorizationCode code = (AuthorizationCode) ce.getValue();
			if (code.getExpires() > System.currentTimeMillis()) {
				return code;
			}
		}
		return null;
	}

}
