/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.util.ObjectUtil;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * DBに保存せず、CacheStoreにしか保存しないAuthTokenStore。
 * 利用するCacheStoreは、Index可能で、キャッシュエントリに有効期限が設けられている想定。
 * 
 * @author K.Higuchi
 *
 */
public class CacheOnlyAuthTokenStore implements AuthTokenStore, ServiceInitListener<AuthTokenService> {
	
	private static final String DEFAULT_TOKEN_CACHE_NAMESPACE = "mtp.auth.cacheOnlyAuthTokenStore/DEFAULT";
	
	private String cacheStoreName = DEFAULT_TOKEN_CACHE_NAMESPACE;
	
	private CacheStore cacheStore;//index[0]=tenantId-type-userUniqueKey
	
	@Override
	public void inited(AuthTokenService service, Config config) {
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		cacheStore = cs.getCache(cacheStoreName);
	}

	@Override
	public void destroyed() {
	}

	public String getCacheStoreName() {
		return cacheStoreName;
	}

	public void setCacheStoreName(String cacheStoreName) {
		this.cacheStoreName = cacheStoreName;
	}

	@Override
	public AuthToken getBySeries(int tenantId, String type, String series) {
		CacheEntry ce = cacheStore.get(new AuthTokenKey(tenantId, type, series));
		if (ce != null) {
			AuthToken at = (AuthToken) ce.getValue();
			if (at != null) {
				at = ObjectUtil.deepCopy(at);
			}
			return at;
		}
		return null;
	}

	@Override
	public List<AuthToken> getByOwner(int tenantId, String type, String userUniqueKey) {
		String key = indexKey0(tenantId, type, userUniqueKey);
		
		List<CacheEntry> l = cacheStore.getListByIndex(0, key);
		if (l != null) {
			List<AuthToken> ret = new ArrayList<>();
			for (CacheEntry e: l) {
				if (e.getValue() != null) {
					ret.add(ObjectUtil.deepCopy((AuthToken) e.getValue()));
				}
			}
			return ret;
		}
		return null;
	}
	
	private String indexKey0(int tenantId, String type, String userUniqueKey) {
		return new StringBuffer().append(tenantId).append('-').append(type).append('-').append(userUniqueKey).toString();
	}

	@Override
	public void create(AuthToken token) {
		CacheEntry ce = new CacheEntry(new AuthTokenKey(token.getTenantId(), token.getType(), token.getSeries()), token,
				indexKey0(token.getTenantId(), token.getType(), token.getOwnerId()));
		if (cacheStore.putIfAbsent(ce) != null) {
			throw new AuthTokenUpdateException("duplicate key entry:" + token.getSeries());
		}
	}

	@Override
	public void update(AuthToken newToken, AuthToken currentToken) {
		CacheEntry ce = cacheStore.get(new AuthTokenKey(currentToken.getTenantId(), currentToken.getType(), currentToken.getSeries()));
		if (ce == null || ce.getValue() == null) {
			throw new AuthTokenUpdateException("currentToken is invalid:" + currentToken.getSeries());
		}
		
		AuthToken cachedToken = (AuthToken) ce.getValue();
		if (!cachedToken.getToken().equals(currentToken.getToken())) {
			throw new AuthTokenUpdateException("currentToken is invalid:" + currentToken.getSeries());
		}
		
		CacheEntry newCe = new CacheEntry(new AuthTokenKey(newToken.getTenantId(), newToken.getType(), newToken.getSeries()), newToken,
				indexKey0(newToken.getTenantId(), newToken.getType(), newToken.getOwnerId()));
		if (!cacheStore.replace(ce, newCe)) {
			throw new AuthTokenUpdateException("currentToken is invalid:" + currentToken.getSeries());
		}
	}

	@Override
	public void delete(int tenantId, String type, String userUniqueKey) {
		cacheStore.removeByIndex(1, indexKey0(tenantId, type, userUniqueKey));
	}

	@Override
	public void deleteBySeries(int tenantId, String type, String series) {
		cacheStore.remove(new AuthTokenKey(tenantId, type, series));
	}

	@Override
	public void deleteByDate(int tenantId, String type, Timestamp ts) {
		//cacheStore側の有効期間ベースで削除される想定
	}


}
