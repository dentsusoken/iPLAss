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
package org.iplass.mtp.impl.auth.authorize.builtin.group;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.Group;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityEventContext;
import org.iplass.mtp.entity.EntityEventListener;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.core.ExecuteContext;

/**
 * 
 * @author K.Higuchi
 *
 */
public class GroupEntityEventListener implements EntityEventListener {

	/**
	 *
	 */
	public GroupEntityEventListener() {
	}

	@Override
	public boolean beforeDelete(Entity entity, EntityEventContext context) {
		Entity before = ManagerLocator.getInstance().getManager(EntityManager.class).load(entity.getOid(), entity.getDefinitionName());
		if (before != null && before.getValue(Group.CODE) != null) {
			GroupContext beforeGroupContext = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).getGroupContext((String) before.getValue(Group.CODE));
			context.setAttribute("groupContextForDel", beforeGroupContext);
		}
		return true;
	}

	@Override
	public void afterDelete(Entity entity, EntityEventContext context) {
		GroupContext beforeGroupContext = (GroupContext) context.getAttribute("groupContextForDel");
		if (beforeGroupContext != null) {
			TenantAuthorizeContext authCotnext = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class);
			authCotnext.notifyGroupDelete(beforeGroupContext.getGroupCode(), beforeGroupContext);
			if (beforeGroupContext.getParentGroupCode() != null) {
				authCotnext.notifyGroupUpdate(beforeGroupContext.getParentGroupCode());
			}
		}
	}

	@Override
	public void afterInsert(Entity entity, EntityEventContext context) {
		TenantAuthorizeContext authCotnext = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class);
		String groupCode = (String) entity.getValue(Group.CODE);
		authCotnext.notifyGroupCreate(groupCode);
		GroupContext created = authCotnext.getGroupContext(groupCode);
		if (created.getParentGroupCode() != null) {
			authCotnext.notifyGroupUpdate(created.getParentGroupCode());
		}
	}

	@Override
	public boolean beforeUpdate(Entity entity, EntityEventContext context) {
		Entity before = ManagerLocator.getInstance().getManager(EntityManager.class).load(entity.getOid(), entity.getDefinitionName());
		if (before != null && before.getValue(Group.CODE) != null) {
			GroupContext beforeGroupContext = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class).getGroupContext((String) before.getValue(Group.CODE));
			context.setAttribute("groupContextForUpdate", beforeGroupContext);
		}
		return true;
	}
	
	@Override
	public void afterUpdate(Entity entity, EntityEventContext context) {
		GroupContext beforeGroupContext = (GroupContext) context.getAttribute("groupContextForUpdate");
		TenantAuthorizeContext tAuth = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class);
		String newGroupCode = entity.getValue(Group.CODE);
		if (beforeGroupContext != null) {
			if (!beforeGroupContext.getGroupCode().equals(newGroupCode)) {
				tAuth.notifyGroupDelete(beforeGroupContext.getGroupCode(), beforeGroupContext);
			}
		}
		tAuth.notifyGroupUpdate(newGroupCode);
		GroupContext updated = tAuth.getGroupContext(newGroupCode);
		if (beforeGroupContext != null) {
			if (beforeGroupContext.getParentGroupCode() != null
					&& !beforeGroupContext.getParentGroupCode().equals(updated.getParentGroupCode())) {
				tAuth.notifyGroupUpdate(beforeGroupContext.getParentGroupCode());
			}
		}
		if (updated.getParentGroupCode() != null) {
			tAuth.notifyGroupUpdate(updated.getParentGroupCode());
		}
	}


	@Override
	public void afterRestore(Entity entity) {
		TenantAuthorizeContext authCotnext = ExecuteContext.getCurrentContext().getTenantContext().getResource(TenantAuthorizeContext.class);
		String groupCode = (String) entity.getValue(Group.CODE);
		authCotnext.notifyGroupCreate(groupCode);
		GroupContext created = authCotnext.getGroupContext(groupCode);
		if (created.getParentGroupCode() != null) {
			authCotnext.notifyGroupUpdate(created.getParentGroupCode());
		}
	}

}
