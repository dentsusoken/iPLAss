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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.impl.cache.CacheController;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.LoadingAdapter;
import org.iplass.mtp.impl.cache.store.keyresolver.CacheKeyResolver;
import org.iplass.mtp.spi.Config;
import org.iplass.mtp.spi.ServiceInitListener;
import org.iplass.mtp.spi.ServiceRegistry;
import org.iplass.mtp.transaction.Transaction;
import org.iplass.mtp.transaction.TransactionStatus;

public class CachableAuthTokenStore implements AuthTokenStore, ServiceInitListener<AuthTokenService> {
	
	private static final String DEFAULT_TOKEN_CACHE_NAMESPACE = "mtp.auth.cachableAuthTokenStore/DEFAULT";
	
	private String cacheStoreName = DEFAULT_TOKEN_CACHE_NAMESPACE;
	private AuthTokenStore authTokenStore;
	
	private CacheController<AuthTokenKey, AuthToken> tokenCache;
	
	@Override
	public void inited(AuthTokenService service, Config config) {
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		tokenCache = new CacheController<AuthTokenKey, AuthToken>(cs.getCache(cacheStoreName), false, 0, new LoadingAdapter<AuthTokenKey, AuthToken>() {

			@Override
			public AuthToken load(AuthTokenKey key) {
				return authTokenStore.getBySeries(key.getTenantId(), key.getType(), key.getSeries());
			}

			@Override
			public List<AuthToken> loadByIndex(int indexType, Object indexVal) {
				return null;
			}

			@Override
			public long getVersion(AuthToken value) {
				return 0;
			}

			@Override
			public Object getIndexVal(int indexType, AuthToken value) {
				return null;
			}

			@Override
			public AuthTokenKey getKey(AuthToken val) {
				return new AuthTokenKey(val.getTenantId(), val.getType(), val.getSeries());
			}
		}, true, false);
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

	public AuthTokenStore getAuthTokenStore() {
		return authTokenStore;
	}

	public void setAuthTokenStore(AuthTokenStore authTokenStore) {
		this.authTokenStore = authTokenStore;
	}

	@Override
	public AuthToken getBySeries(int tenantId, String type, String series) {
		return tokenCache.get(new AuthTokenKey(tenantId, type, series));
	}

	@Override
	public List<AuthToken> getByOwner(int tenantId, String userUniqueKey) {
		return authTokenStore.getByOwner(tenantId, userUniqueKey);
	}

	@Override
	public void create(AuthToken token) {
		authTokenStore.create(token);
		tokenCache.notifyCreate(token);
		Transaction t = Transaction.getCurrent();
		if (t.getStatus() == TransactionStatus.ACTIVE) {
			t.afterRollback(() -> {
				tokenCache.notifyInvalid(token);
			}); 
		}
	}

	@Override
	public void update(AuthToken newToken, AuthToken currentToken) {
		authTokenStore.update(newToken, currentToken);
		tokenCache.notifyUpdate(newToken);
		Transaction t = Transaction.getCurrent();
		if (t.getStatus() == TransactionStatus.ACTIVE) {
			t.afterRollback(() -> {
				tokenCache.notifyInvalid(newToken);
			});
		}
	}

	@Override
	public void delete(int tenantId, String type, String userUniqueKey) {
		List<AuthToken> lists = getByOwner(tenantId, userUniqueKey);
		authTokenStore.delete(tenantId, type, userUniqueKey);
		if (lists != null && lists.size() > 0) {
			List<AuthTokenKey> keys = new ArrayList<>();
			for (AuthToken t: lists) {
				if (t.getType().equals(type)) {
					AuthTokenKey key = new AuthTokenKey(t.getTenantId(), t.getType(), t.getSeries());
					tokenCache.notifyDeleteByKey(key);
					keys.add(key);
				}
			}
			
			//念のため
			Transaction t = Transaction.getCurrent();
			if (t.getStatus() == TransactionStatus.ACTIVE) {
				t.afterCommit(() -> {
					for (AuthTokenKey k: keys) {
						tokenCache.notifyInvalidByKey(k);
					}
				});
			}
		}
	}

	@Override
	public void deleteBySeries(int tenantId, String type, String series) {
		authTokenStore.deleteBySeries(tenantId, type, series);
		AuthTokenKey key = new AuthTokenKey(tenantId, type, series);
		tokenCache.notifyDeleteByKey(key);
		
		//念のため
		Transaction t = Transaction.getCurrent();
		if (t.getStatus() == TransactionStatus.ACTIVE) {
			t.afterCommit(() -> {
				tokenCache.notifyInvalidByKey(key);
			});
		}
	}

	@Override
	public void deleteByDate(int tenantId, String type, Timestamp ts) {
		//キャッシュの有効期間は、ここで指定される日付より十分短い想定
		authTokenStore.deleteByDate(tenantId, type, ts);
	}

	private static class AuthTokenKey implements Serializable {
		private static final long serialVersionUID = 6033114232648174950L;

		private final int tenantId;
		private final String type;
		private final String series;
		
		public AuthTokenKey(int tenantId, String type, String series) {
			this.tenantId = tenantId;
			this.type = type;
			this.series = series;
		}

		public int getTenantId() {
			return tenantId;
		}

		public String getType() {
			return type;
		}

		public String getSeries() {
			return series;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((series == null) ? 0 : series.hashCode());
			result = prime * result + tenantId;
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AuthTokenKey other = (AuthTokenKey) obj;
			if (series == null) {
				if (other.series != null)
					return false;
			} else if (!series.equals(other.series))
				return false;
			if (tenantId != other.tenantId)
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}

	}

	public static class AuthTokenKeyResolver implements CacheKeyResolver {

		@Override
		public String toString(Object cacheKey) {
			AuthTokenKey key = (AuthTokenKey) cacheKey;
			StringBuilder sb = new StringBuilder();
			sb.append(key.getTenantId()).append(':').append(key.getType()).append(':').append(key.getSeries());
			return sb.toString();
		}

		@Override
		public Object toCacheKey(String cacheKeyString) {
			int index = cacheKeyString.indexOf(':');
			int tenantId = Integer.parseInt(cacheKeyString.substring(0, index));
			int index2 = cacheKeyString.indexOf(index + 1, ':');
			String type = cacheKeyString.substring(index, index2);
			String series = cacheKeyString.substring(index2 + 1);

			return new AuthTokenKey(tenantId, type, series);
		}

	}

}
