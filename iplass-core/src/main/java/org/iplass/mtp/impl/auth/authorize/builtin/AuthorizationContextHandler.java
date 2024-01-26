/*
 * Copyright (C) 2017 DENTSU SOKEN INC. All Rights Reserved.
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
package org.iplass.mtp.impl.auth.authorize.builtin;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.auth.authorize.AuthorizationContext;
import org.iplass.mtp.impl.cache.CacheController;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.metadata.MetaDataContext;
import org.iplass.mtp.impl.metadata.MetaDataEntry;
import org.iplass.mtp.impl.metadata.MetaDataEntry.RepositoryType;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * AuthorizationContext（Permission）毎の処理を表現するクラス。
 * 
 * @author K.Higuchi
 *
 */
public abstract class AuthorizationContextHandler {

	public abstract Class<? extends Permission>[] permissionType();
	protected abstract String cacheNamespace();
	protected abstract AuthorizationContextCacheLogic newAuthorizeContextCacheLogic(TenantAuthorizeContext tac);
	protected abstract AuthorizationContext defaultAuthorizationContext(String contextName, TenantAuthorizeContext tac);
	protected abstract String toMetaDataPath(String contextName);
	protected abstract String contextName(Permission permission);
	
	public CacheController<String, BuiltinAuthorizationContext> initCache(TenantAuthorizeContext tac) {
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);
		return new CacheController<>(cs.getCache(cacheNamespace() + "/" + tac.getTenantContext().getTenantId()), false, 0, newAuthorizeContextCacheLogic(tac), true, true);
	}
	
	public boolean useSharedPermission(Permission permission) {
		String metaDataPath = toMetaDataPath(contextName(permission));
		MetaDataEntry ent = MetaDataContext.getContext().getMetaDataEntry(metaDataPath);
		if (ent == null) {
			return false;
		}
		if (!ent.isSharable()) {
			return false;
		}
		if (ent.getRepositryType() == RepositoryType.SHARED
				&& ent.isPermissionSharable()) {
			return true;
		} else {
			return false;
		}
	}

	public BuiltinAuthorizationContext get(String contextName, TenantAuthorizeContext tac) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tac.getTenantContext().getTenantId()) {
			return ExecuteContext.executeAs(tac.getTenantContext(), () -> {
				return tac.getContextCache(this.getClass()).get(contextName);
			});
		} else {
			return tac.getContextCache(this.getClass()).get(contextName);
		}
	}
	
	public AuthorizationContext getOrDefault(String contextName, TenantAuthorizeContext tac) {
		AuthorizationContext ret = get(contextName, tac);
		if (ret == null) {
			ret = defaultAuthorizationContext(contextName, tac);
		}
		return ret;
	}

	public void notifyUpdate(final String contextName, TenantAuthorizeContext tac) {
		CacheController<String, BuiltinAuthorizationContext> cache = tac.getContextCache(this.getClass());
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tac.getTenantContext().getTenantId()) {
			ExecuteContext.executeAs(tac.getTenantContext(), () -> {
				BuiltinAuthorizationContext context = newAuthorizeContextCacheLogic(tac).load(contextName);
				if (context == null) {
					cache.notifyDeleteByKey(contextName);
				} else {
					cache.notifyUpdate(context);
				}
				return null;
			});
		} else {
			BuiltinAuthorizationContext context = newAuthorizeContextCacheLogic(tac).load(contextName);
			if (context == null) {
				cache.notifyDeleteByKey(contextName);
			} else {
				cache.notifyUpdate(context);
			}
		}
	}
}
