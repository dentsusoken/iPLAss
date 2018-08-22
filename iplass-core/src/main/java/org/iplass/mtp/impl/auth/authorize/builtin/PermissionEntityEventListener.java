/*
 * Copyright (C) 2017 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.impl.auth.authorize.AuthorizationProvider;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.spi.ServiceRegistry;

/**
 * Permissionを表現するEntityのEventListenerの抽象クラス。
 * 
 * @author K.Higuchi
 *
 */
public abstract class PermissionEntityEventListener implements EntityEventListener {
	
	private static final String KEY_UPDATE = "authContextForUpdate";
	private static final String KEY_DEL = "authContextForDel";

	protected AuthorizationContextHandler handler;

	public PermissionEntityEventListener(Class<? extends Permission> typeForHandler) {
		AuthorizationProvider ap = ServiceRegistry.getRegistry().getService(AuthService.class).getAuthorizationProvider();
		if (ap instanceof BuiltinAuthorizationProvider) {
			handler = ((BuiltinAuthorizationProvider) ap).getAuthorizationContextHandler(typeForHandler);
		}
	}
	
	protected abstract String contextName(Entity entity);
	
	private TenantAuthorizeContext getTenantAuthorizeContext() {
		return ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class);
	}

	@Override
	public boolean beforeDelete(Entity entity, EntityEventContext context) {
		if (handler != null) {
			Entity before = ManagerLocator.getInstance().getManager(EntityManager.class).load(entity.getOid(), entity.getDefinitionName());
			if (before != null) {
				BuiltinAuthorizationContext beforeContext = handler.get(contextName(before), getTenantAuthorizeContext());
				context.setAttribute(KEY_DEL, beforeContext);
			}
		}
		return true;
	}

	@Override
	public void afterDelete(Entity entity, EntityEventContext context) {
		if (handler != null) {
			BuiltinAuthorizationContext beforeContext = (BuiltinAuthorizationContext) context.getAttribute(KEY_DEL);
			if (beforeContext != null) {
				handler.notifyUpdate(beforeContext.getContextName(), getTenantAuthorizeContext());
			}
		}
	}

	@Override
	public void afterInsert(Entity entity, EntityEventContext context) {
		if (handler != null) {
			handler.notifyUpdate(contextName(entity), getTenantAuthorizeContext());
		}
	}

	@Override
	public boolean beforeUpdate(Entity entity, EntityEventContext context) {
		if (handler != null) {
			Entity before = ManagerLocator.getInstance().getManager(EntityManager.class).load(entity.getOid(), entity.getDefinitionName());
			if (before != null) {
				BuiltinAuthorizationContext beforeContext = handler.get(contextName(before), getTenantAuthorizeContext());
				context.setAttribute(KEY_UPDATE, beforeContext);
			}
		}
		return true;
	}
	
	@Override
	public void afterUpdate(Entity entity, EntityEventContext context) {
		if (handler != null) {
			BuiltinAuthorizationContext beforeContext = (BuiltinAuthorizationContext) context.getAttribute(KEY_UPDATE);
			if (beforeContext != null) {
				if (!beforeContext.getContextName().equals(contextName(entity))) {
					handler.notifyUpdate(beforeContext.getContextName(), getTenantAuthorizeContext());
				}
				handler.notifyUpdate(contextName(entity), getTenantAuthorizeContext());
			}
		}
	}

	@Override
	public void afterRestore(Entity entity) {
		if (handler != null) {
			handler.notifyUpdate(contextName(entity), getTenantAuthorizeContext());
		}
	}

}
