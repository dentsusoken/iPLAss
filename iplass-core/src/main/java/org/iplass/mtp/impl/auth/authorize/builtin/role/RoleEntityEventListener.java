/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

/**
 *
 */
package org.iplass.mtp.impl.auth.authorize.builtin.role;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.LoadOption;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.core.ExecuteContext;

/**
 * 
 * @author K.Higuchi
 *
 */
public class RoleEntityEventListener implements EntityEventListener {

	/**
	 *
	 */
	public RoleEntityEventListener() {
	}

	@Override
	public boolean beforeDelete(Entity entity, EntityEventContext context) {
		Entity before = ManagerLocator.getInstance().getManager(EntityManager.class).load(entity.getOid(), entity.getDefinitionName(), new LoadOption(true, false));
		if (before != null) {
			RoleContext beforeRoleContext = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).getRoleContext((String) before.getValue(RoleCacheLogic.ROLE_CODE));
			context.setAttribute("roleContextForDel", beforeRoleContext);
		}
		return true;
	}

	@Override
	public void afterDelete(Entity entity, EntityEventContext context) {
		RoleContext beforeRoleContext = (RoleContext) context.getAttribute("roleContextForDel");
		if (beforeRoleContext != null) {
			ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).notifyRoleDelete(beforeRoleContext.getRoleCode(), beforeRoleContext);
		}
	}

	@Override
	public void afterInsert(Entity entity, EntityEventContext context) {
		ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).notifyRoleCreate((String) entity.getValue(RoleCacheLogic.ROLE_CODE));
	}

	@Override
	public boolean beforeUpdate(Entity entity, EntityEventContext context) {
		Entity before = ManagerLocator.getInstance().getManager(EntityManager.class).load(entity.getOid(), entity.getDefinitionName(), new LoadOption(true, false));
		if (before != null) {
			RoleContext beforeRoleContext = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).getRoleContext((String) before.getValue(RoleCacheLogic.ROLE_CODE));
			context.setAttribute("roleContextForUpdate", beforeRoleContext);
		}
		return true;
	}
	
	@Override
	public void afterUpdate(Entity entity, EntityEventContext context) {
		RoleContext beforeRoleContext = (RoleContext) context.getAttribute("roleContextForUpdate");
		if (beforeRoleContext != null) {
			TenantAuthorizeContext tAuth = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class);
			if (!beforeRoleContext.getRoleCode().equals(entity.getValue(RoleCacheLogic.ROLE_CODE))) {
				tAuth.notifyRoleDelete(beforeRoleContext.getRoleCode(), beforeRoleContext);
			}
			tAuth.notifyRoleUpdate((String) entity.getValue(RoleCacheLogic.ROLE_CODE));
		}
	}


	@Override
	public void afterRestore(Entity entity) {
		ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).notifyRoleCreate((String) entity.getValue(RoleCacheLogic.ROLE_CODE));
	}

}
