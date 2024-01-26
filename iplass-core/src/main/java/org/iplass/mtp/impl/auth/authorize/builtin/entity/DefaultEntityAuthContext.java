/*
 * Copyright (C) 2012 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.impl.auth.authorize.builtin.entity;

import java.util.EnumMap;

import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.permission.EntityPermission.Action;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;

class DefaultEntityAuthContext extends BuiltinEntityAuthContext {
	
	private boolean readOnly;
	
	DefaultEntityAuthContext(
			String entityDefinitionName,
			EnumMap<Action, EntityPermissionEntry[]> entityPermissionEntry,
			EnumMap<org.iplass.mtp.entity.permission.EntityPropertyPermission.Action, EntityPropertyPermissionEntry[]> entityPropertyPermissionEntry,
			TenantAuthorizeContext tenantAuthContext, boolean readOnly) {
		super(entityDefinitionName, entityPermissionEntry,
				entityPropertyPermissionEntry, tenantAuthContext);
		this.readOnly = readOnly;
	}

	@Override
	public boolean isPermit(Action action, AuthContextHolder user) {
		if (readOnly) {
			if (action == Action.REFERENCE) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public boolean isPermit(String propertyName,
			org.iplass.mtp.entity.permission.EntityPropertyPermission.Action action,
			AuthContextHolder user) {
		if (readOnly) {
			if (action == org.iplass.mtp.entity.permission.EntityPropertyPermission.Action.REFERENCE) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public Condition addLimitingCondition(Condition orignal, Action action,
			AuthContextHolder user) {
		return null;
	}

	@Override
	public boolean hasLimitCondition(EntityPermission permission, AuthContextHolder user) {
		return false;
	}

}
