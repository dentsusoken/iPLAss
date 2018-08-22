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

package org.iplass.mtp.impl.tools.tenant.create;

import org.iplass.mtp.auth.Group;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;

public abstract class AbstractCreateRoleProcess implements TenantCreateProcess {

	protected Entity insertGroupRole(String roleName, EntityManager em) {

		Group group = new Group();
		group.setName(roleName);
		group.setCode(roleName);
		em.insert(group);

		GenericEntity role = new GenericEntity("mtp.auth.Role");
		role.setName(roleName);
		role.setValue("code", roleName);

		em.insert(role);

		GenericEntity roleCond = new GenericEntity("mtp.auth.RoleCondition");
		roleCond.setName(roleName);
		roleCond.setValue("role", role);
		roleCond.setValue("expression", "user.memberOf(\""+ roleName + "\")");

		em.insert(roleCond);

		return role;
	}

	protected Entity searchRole(String roleName, EntityManager em) {
		Query query = new Query();
		query.select(Entity.OID, Entity.VERSION, Entity.NAME).from("mtp.auth.Role").where(new Equals("code", roleName));
		return em.searchEntity(query).getFirst();
	}

	protected GenericEntity createActionPermission(String targetAction, Entity role) {

		GenericEntity permission = new GenericEntity("mtp.auth.ActionPermission");
		permission.setName(targetAction);
		permission.setValue("role", role);
		permission.setValue("targetAction", targetAction);

		return permission;
	}

	protected GenericEntity createEntityPermission(String targetEntity, Entity role,
			boolean canReference, boolean canCreate, boolean canUpdate, boolean canDelete) {

		GenericEntity permission = new GenericEntity("mtp.auth.EntityPermission");
		permission.setName(targetEntity);
		permission.setValue("role", role);
		permission.setValue("targetEntity", targetEntity);
		permission.setValue("canReference", canReference);
		permission.setValue("canCreate", canCreate);
		permission.setValue("canUpdate", canUpdate);
		permission.setValue("canDelete", canDelete);

		return permission;
	}

	protected GenericEntity createWebApiPermission(String targetWebApi, Entity role) {

		GenericEntity permission = new GenericEntity("mtp.auth.WebApiPermission");
		permission.setName(targetWebApi);
		permission.setValue("role", role);
		permission.setValue("targetWebApi", targetWebApi);

		return permission;
	}
}
