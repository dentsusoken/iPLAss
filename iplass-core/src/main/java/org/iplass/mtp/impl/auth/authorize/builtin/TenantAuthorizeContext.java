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

package org.iplass.mtp.impl.auth.authorize.builtin;

import java.util.HashMap;

import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authorize.AuthorizationProvider;
import org.iplass.mtp.impl.auth.authorize.builtin.group.GroupCacheLogic;
import org.iplass.mtp.impl.auth.authorize.builtin.group.GroupContext;
import org.iplass.mtp.impl.auth.authorize.builtin.role.RoleCacheLogic;
import org.iplass.mtp.impl.auth.authorize.builtin.role.RoleContext;
import org.iplass.mtp.impl.cache.CacheController;
import org.iplass.mtp.impl.cache.CacheService;
import org.iplass.mtp.impl.core.Executable;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;
import org.iplass.mtp.impl.core.TenantResource;
import org.iplass.mtp.spi.ServiceRegistry;

public class TenantAuthorizeContext implements TenantResource {
	private static final String ROLE_CACHE_NAMESPACE = "mtp.auth.builtin.role";
	private static final String GROUP_CACHE_NAMESPACE = "mtp.auth.builtin.group";

	private TenantContext tc;
	
	private HashMap<Class<? extends AuthorizationContextHandler>, CacheController<String, BuiltinAuthorizationContext>> contextCacheMap;
	private CacheController<String, RoleContext> roleCache;
	private CacheController<String, GroupContext> groupCache;

	private boolean grantAllPermissionsToAdmin;
	private boolean declareTransactionExplicitly;

	public TenantAuthorizeContext() {
	}
	
	public CacheController<String, BuiltinAuthorizationContext> getContextCache(Class<? extends AuthorizationContextHandler> key) {
		return contextCacheMap.get(key);
	}

	@Override
	public void init(TenantContext tenantContext) {
		this.tc = tenantContext;
		contextCacheMap = new HashMap<>();

		//TODO CacheControlを一つにした方が、メモリ効率よいが、、
		CacheService cs = ServiceRegistry.getRegistry().getService(CacheService.class);

		//TODO バージョンの定義と実装
		roleCache = new CacheController<String, RoleContext>(cs.getCache(ROLE_CACHE_NAMESPACE + "/" + tc.getTenantId()), false, 0, new RoleCacheLogic(this), true, true);
		groupCache = new CacheController<String, GroupContext>(cs.getCache(GROUP_CACHE_NAMESPACE + "/" + tc.getTenantId()), false, 0, new GroupCacheLogic(this), true, true);
		
		AuthorizationProvider authz = ServiceRegistry.getRegistry().getService(AuthService.class).getAuthorizationProvider();
		if (authz instanceof BuiltinAuthorizationProvider) {
			BuiltinAuthorizationProvider bauthz = (BuiltinAuthorizationProvider) authz;
			this.grantAllPermissionsToAdmin = bauthz.isGrantAllPermissionsToAdmin();
			if (bauthz.getAuthorizationContextHandler() != null) {
				for (AuthorizationContextHandler ach: bauthz.getAuthorizationContextHandler()) {
					contextCacheMap.put(ach.getClass(), ach.initCache(this));
				}
			}
			this.declareTransactionExplicitly = bauthz.isDeclareTransactionExplicitly();
		} else {
			this.grantAllPermissionsToAdmin = false;
		}
	}

	@Override
	public void destory() {
		
		for (CacheController<String, BuiltinAuthorizationContext> c: contextCacheMap.values()) {
			c.invalidateCacheStore();
		}
		
		roleCache.invalidateCacheStore();
		groupCache.invalidateCacheStore();
	}

	public boolean isGrantAllPermissionsToAdmin() {
		return grantAllPermissionsToAdmin;
	}

	public boolean isDeclareTransactionExplicitly() {
		return declareTransactionExplicitly;
	}

	public TenantContext getTenantContext() {
		return tc;
	}
	
	public RoleContext getRoleContext(final String role) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tc.getTenantId()) {
			return ExecuteContext.executeAs(tc, new Executable<RoleContext>() {
				@Override
				public RoleContext execute() {
					return roleCache.get(role);
				}
			});
		} else {
			return roleCache.get(role);
		}
	}

	public void notifyRoleCreate(final String role) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tc.getTenantId()) {
			ExecuteContext.executeAs(tc, new Executable<Void>() {
				@Override
				public Void execute() {
					roleCache.notifyCreate(new RoleCacheLogic(TenantAuthorizeContext.this).load(role));
					return null;
				}
			});
		} else {
			roleCache.notifyCreate(new RoleCacheLogic(this).load(role));
		}
	}

	public void notifyRoleDelete(final String role, final RoleContext roleContext) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tc.getTenantId()) {
			ExecuteContext.executeAs(tc, new Executable<Void>() {
				@Override
				public Void execute() {
					roleCache.notifyDelete(roleContext);
					return null;
				}
			});
		} else {
			roleCache.notifyDelete(roleContext);
		}
	}

	public void notifyRoleUpdate(final String role) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tc.getTenantId()) {
			ExecuteContext.executeAs(tc, new Executable<Void>() {
				@Override
				public Void execute() {
					RoleContext context = new RoleCacheLogic(TenantAuthorizeContext.this).load(role);
					if (context == null) {
						//TODO newするとは。。。
						roleCache.notifyDelete(new RoleContext(role, 0, null, TenantAuthorizeContext.this));
					} else {
						roleCache.notifyUpdate(context);
					}
					return null;
				}
			});
		} else {
			RoleContext context = new RoleCacheLogic(this).load(role);
			if (context == null) {
				//TODO newするとは。。。
				roleCache.notifyDelete(new RoleContext(role, 0, null, this));
			} else {
				roleCache.notifyUpdate(context);
			}
		}
	}

	public GroupContext getGroupContext(final String group) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tc.getTenantId()) {
			return ExecuteContext.executeAs(tc, new Executable<GroupContext>() {
				@Override
				public GroupContext execute() {
					return groupCache.get(group);
				}
			});
		} else {
			return groupCache.get(group);
		}
	}

	public void notifyGroupCreate(final String group) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tc.getTenantId()) {
			ExecuteContext.executeAs(tc, new Executable<Void>() {
				@Override
				public Void execute() {
					groupCache.notifyCreate(new GroupCacheLogic(TenantAuthorizeContext.this).load(group));
					return null;
				}
			});
		} else {
			groupCache.notifyCreate(new GroupCacheLogic(this).load(group));
		}
	}

	public void notifyGroupDelete(final String group, final GroupContext groupContext) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tc.getTenantId()) {
			ExecuteContext.executeAs(tc, new Executable<Void>() {
				@Override
				public Void execute() {
					groupCache.notifyDelete(groupContext);
					return null;
				}
			});
		} else {
			groupCache.notifyDelete(groupContext);
		}
	}

	public void notifyGroupUpdate(final String group) {
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tc.getTenantId()) {
			ExecuteContext.executeAs(tc, new Executable<Void>() {
				@Override
				public Void execute() {
					GroupContext context = new GroupCacheLogic(TenantAuthorizeContext.this).load(group);
					if (context == null) {
						//TODO newとは。。。
						groupCache.notifyDelete(new GroupContext(null, group, null, null, TenantAuthorizeContext.this));
					} else {
						groupCache.notifyUpdate(context);
					}
					return null;
				}
			});
		} else {
			GroupContext context = new GroupCacheLogic(this).load(group);
			if (context == null) {
				//TODO newとは。。。
				groupCache.notifyDelete(new GroupContext(null, group, null, null, this));
			} else {
				groupCache.notifyUpdate(context);
			}
		}
	}

}
