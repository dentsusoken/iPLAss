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

/**
 *
 */
package org.iplass.mtp.impl.auth.authorize.builtin.role;

import java.util.function.Predicate;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.core.ExecuteContext;

/**
 *
 * @author K.Higuchi
 *
 */
public class RoleConditionEntityEventListener implements EntityEventListener {

	/**
	 *
	 */
	public RoleConditionEntityEventListener() {
	}


	private String getRoleCode(Entity roleCondEntity) {
		Query q = new Query()
			.select(RoleCacheLogic.ROLE_CONDITION_ROLE + "." + RoleCacheLogic.ROLE_CODE)
			.from(RoleCacheLogic.ROLE_CONDITION_DEF_NAME)
			.where(new Equals(Entity.OID, roleCondEntity.getOid()));
		final String[] ret = new String[]{null};
		ManagerLocator.getInstance().getManager(EntityManager.class).search(q, new Predicate<Object[]>() {
			@Override
			public boolean test(Object[] dataModel) {
				ret[0] = (String) dataModel[0];
				return false;//1件のはず
			}
		});
		return ret[0];
	}


	@Override
	public boolean beforeDelete(Entity entity, EntityEventContext context) {
		String roleCode = getRoleCode(entity);
		context.setAttribute("roleCode", roleCode);
		return true;
	}

	@Override
	public void afterDelete(Entity entity, EntityEventContext context) {
		String roleCode = (String) context.getAttribute("roleCode");
		if (roleCode != null) {
			ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).notifyRoleUpdate(roleCode);
		}
	}

	@Override
	public void afterInsert(Entity entity, EntityEventContext context) {
		String roleCode = getRoleCode(entity);
		if (roleCode != null) {
			ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).notifyRoleUpdate(roleCode);
		}
	}

	@Override
	public boolean beforeUpdate(Entity entity, EntityEventContext context) {
		String roleCode = getRoleCode(entity);
		context.setAttribute("beforeRoleCode", roleCode);
		return true;
	}

	@Override
	public void afterUpdate(Entity entity, EntityEventContext context) {
		String beforeRoleCode = (String) context.getAttribute("beforeRoleCode");
		if (beforeRoleCode != null) {
			ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).notifyRoleUpdate(beforeRoleCode);
		}
		String afterRoleCode = getRoleCode(entity);
		if (afterRoleCode != null) {
			if (!afterRoleCode.equals(beforeRoleCode)) {
				ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).notifyRoleUpdate(afterRoleCode);
			}
		}
	}

	@Override
	public void afterRestore(Entity entity) {
		String roleCode = getRoleCode(entity);
		if (roleCode != null) {
			ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).notifyRoleUpdate(roleCode);
		}
	}

}
