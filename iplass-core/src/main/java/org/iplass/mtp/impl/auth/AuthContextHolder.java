/*
 * Copyright (C) 2011 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.token.AuthTokenInfoList;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.AuthenticationPolicyService;
import org.iplass.mtp.impl.auth.authenticate.builtin.policy.MetaAuthenticationPolicy.AuthenticationPolicyRuntime;
import org.iplass.mtp.impl.auth.authenticate.trust.TrustedAuthValidateResult;
import org.iplass.mtp.impl.auth.authorize.AuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.cache.store.CacheEntry;
import org.iplass.mtp.impl.cache.store.CacheStore;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 認証・認可に関する処理結果をキャッシュして保持するクラス。
 * ExecuteContextに紐づけて管理。
 *
 * @author K.Higuchi
 *
 */
public class AuthContextHolder {
	private static final Logger log = LoggerFactory.getLogger(AuthContextHolder.class);

	private static final String PERMISSION_CACHE_NAMESPACE = "mtp.auth.permissionCache";

	private final static class UserInRoleCacheKey {
		final int tenantId;
		final String role;

		UserInRoleCacheKey(int tenantId, String role) {
			this.tenantId = tenantId;
			this.role = role;
		}
		@Override
		public final int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((role == null) ? 0 : role.hashCode());
			result = prime * result + tenantId;
			return result;
		}
		@Override
		public final boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			UserInRoleCacheKey other = (UserInRoleCacheKey) obj;
			if (role == null) {
				if (other.role != null)
					return false;
			} else if (!role.equals(other.role))
				return false;
			if (tenantId != other.tenantId)
				return false;
			return true;
		}
		@Override
		public String toString() {
			return "UserInRoleCacheKey [tenantId=" + tenantId + ", role=" + role + "]";
		}
	}

	private AuthService authService;

	private boolean isSecuredAction;
	private boolean isPrivileged = false;

	private UserContext userContext;
	private Map<UserInRoleCacheKey, Boolean> userInRoleCache;
	private CacheStore permissionCache;//LRUなので、、、
	private User userForApl;//利用側へ渡すUserのインスタンス（コピー）
	private AuthTokenInfoListImpl authTokenInfoList;

	private AuthenticationPolicyRuntime policy;

	private AuthContextHolder wrapped;

	public static AuthContextHolder getAuthContext() {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		AuthContextHolder holder = (AuthContextHolder) ec.getAttribute(AuthService.HOLDER_NAME);
		if (holder == null) {
			holder = ServiceRegistry.getRegistry().getService(AuthService.class).newAuthContextHolder();
			ec.setAttribute(AuthService.HOLDER_NAME, holder, false);
		}
		return holder;
	}

	static void reflesh() {
		ExecuteContext ec = ExecuteContext.getCurrentContext();
		AuthContextHolder holder = (AuthContextHolder) ec.getAttribute(AuthService.HOLDER_NAME);
		if (holder != null) {
			AuthService as = ServiceRegistry.getRegistry().getService(AuthService.class);
			UserContext account = as.getCurrentSessionUserContext();
			if (account == null) {
				//anonymous
				account = new AnonymousUserContext();
			}
			holder.reflesh(account);
		}
	}

	AuthContextHolder(UserContext userContext, AuthService authService) {
		this.userContext = userContext;
		this.authService = authService;
	}

	public UserBinding newUserBinding() {
		return newUserBinding(ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class));
	}

	public UserBinding newUserBinding(TenantAuthorizeContext authContext) {
		return new UserBinding(userContext, authContext);
	}

	protected void reflesh(UserContext account) {
		userContext = account;
		permissionCache = null;
		userInRoleCache = null;
		userForApl = null;
		policy = null;
		authTokenInfoList = null;
		if (wrapped != null) {
			wrapped.reflesh(account);
		}
	}

	public boolean isPrivilegedExecution() {
		return isPrivileged() || !isSecuredAction();
	}

	void setSecuredAction(boolean isSecuredAction) {
		this.isSecuredAction = isSecuredAction;
	}

	public boolean isSecuredAction() {
		return isSecuredAction;
	}

	void setPrivileged(boolean isPrivileged) {
		this.isPrivileged = isPrivileged;
	}

	public boolean isPrivileged() {
		return isPrivileged;
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public AuthTokenInfoList getAuthTokenInfoList() {
		if (authTokenInfoList == null) {
			authTokenInfoList = new AuthTokenInfoListImpl(getUserContext());
		}
		return authTokenInfoList;
	}

	public User getUserCopy() {
		if (userForApl == null) {
			userForApl = (User) getUserContext().getUser().deepCopy();
		}
		return userForApl;
	}

	public AuthenticationPolicyRuntime getPolicy() {
		if (policy == null) {
			String policyName = (String) userContext.getAttribute(User.ACCOUNT_POLICY);
			AuthenticationPolicyService ps = ServiceRegistry.getRegistry().getService(AuthenticationPolicyService.class);
			policy = ps.getOrDefault(policyName);
		}
		return policy;
	}

	public void setPolicy(AuthenticationPolicyRuntime policy) {
		this.policy = policy;
	}

	private boolean userInRole(int tenantId, String role) {
		if (isSecuredAction()) {
			return authService.getAuthorizationProvider().userInRole(this, tenantId, role);
		} else {
			return true;
		}
	}

	public boolean userInRole(String role, int tenantId) {
		if (role == null) {
			throw new NullPointerException("role is null");
		}
		if (userInRoleCache == null) {
			userInRoleCache = new HashMap<UserInRoleCacheKey, Boolean>();
		}

		UserInRoleCacheKey cacheKey = new UserInRoleCacheKey(tenantId, role);
		Boolean result = userInRoleCache.get(cacheKey);
		if (result != null) {
			if(log.isTraceEnabled()) {
				log.trace("userInRole Cache Hit role={}, result={}", cacheKey, result);
			}
		}
		if (result == null) {
			boolean userInRole = userInRole(tenantId, role);
			result = Boolean.valueOf(userInRole);
			userInRoleCache.put(cacheKey, result);

			if (log.isDebugEnabled()) {
				log.debug("userInRole(" + role + ", " + tenantId + ") = " + result + " (put to userInRole cache)");
			}
		}
		return result;
	}

	public AuthorizationContext getAuthorizationContext(Permission permission) {
		return authService.getAuthorizationContext(permission);
	}

	public boolean checkPermission(Permission permission) {
		if (permission == null) {
			throw new NullPointerException("permission is null");
		}
		if (permissionCache == null) {
			permissionCache = ServiceRegistry.getRegistry().getService(CacheService.class).createLocalCache(PERMISSION_CACHE_NAMESPACE);
		}

		//check from cache
		CacheEntry entry = permissionCache.get(permission);
		Boolean result = null;
		if (entry != null) {
			result = (Boolean) entry.getValue();
			if (log.isTraceEnabled()) {
				log.trace("permission cache Hit permission={}, result={}", permission, result);
			}
		}

		if (result == null) {
			if (isPrivilegedExecution()) {
				if (log.isDebugEnabled()) {
					log.debug("check " + permission + " = true (privilegedExecution)");
				}
				return true;
			}

			AuthorizationContext ac = authService.getAuthorizationContext(permission);
			result = ac.isPermit(permission, this);

			//put to cache
			if (ac.isResultCacheable(permission)) {
				permissionCache.put(new CacheEntry(permission, result), true);
				if (log.isDebugEnabled()) {
					log.debug("check " + permission + " = " + result + " (put to permission cache)");
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("check " + permission + " = " + result);
				}
			}
		}

		return result;
	}

	public boolean[] checkPermission(Permission[] permissions) {
		boolean[] result = new boolean[permissions.length];
		for (int i = 0; i < permissions.length; i++) {
			result[i] = checkPermission(permissions[i]);
		}
		return result;
	}

	public AuthContextHolder privilegedAuthContextHolder() {
		AuthContextHolder priv = authService.newAuthContextHolder(userContext);
		priv.isPrivileged = true;
		priv.setSecuredAction(isSecuredAction);
		priv.policy = policy;
		priv.wrapped = this;
		return priv;
	}

	public TrustedAuthValidateResult checkCurrentSessionTrusted() {
		return authService.checkCurrentSessionTrusted();
	}
}
