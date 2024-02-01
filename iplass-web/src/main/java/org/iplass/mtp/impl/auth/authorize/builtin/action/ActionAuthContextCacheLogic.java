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

package org.iplass.mtp.impl.auth.authorize.builtin.action;

import java.util.ArrayList;

import org.iplass.mtp.ManagerLocator;
import org.iplass.mtp.auth.AuthContext;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityManager;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthorizationContextCacheLogic;
import org.iplass.mtp.impl.auth.authorize.builtin.BuiltinAuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.auth.authorize.builtin.role.RoleCacheLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ActionAuthContextCacheLogic extends AuthorizationContextCacheLogic {
	
	private static final Logger logger = LoggerFactory.getLogger(ActionAuthContextCacheLogic.class);
	
	public static final String ACTION_PERMISSION_DEF_NAME = "mtp.auth.ActionPermission";
	public static final String ACTION_PATH = "targetAction";
	public static final String ACTION_CONDITION = "conditionExpression";
	public static final String ACTION_ROLE = "role";
	
	private EntityManager em = ManagerLocator.getInstance().getManager(EntityManager.class);

	ActionAuthContextCacheLogic(TenantAuthorizeContext authorizeContext) {
		super(authorizeContext);
	}

	@Override
	protected BuiltinAuthorizationContext loadImpl(final String key) {
		
		return AuthContext.doPrivileged(() -> {
			Query q = new Query()
					.select(Entity.OID, ACTION_CONDITION, ACTION_ROLE + "." + RoleCacheLogic.ROLE_CODE)
					.from(ACTION_PERMISSION_DEF_NAME)
					.where(new Equals(ACTION_PATH, key));

			ArrayList<ActionPermissionEntry> entries = new ArrayList<ActionPermissionEntry>();
			em.search(q, dataModel -> {
				if (dataModel[2] != null) {
					ActionPermissionEntry e = new ActionPermissionEntry((String) dataModel[2], (String) dataModel[0], (String) dataModel[1]);
					entries.add(e);
				} else {
					logger.warn("role code not defined. so ignore this entry:oid=" + dataModel[0]);
				}
				return true;
			});

			if (entries.size() == 0) {
				return null;
			}
			return new BuiltinActionAuthContext(key, entries.toArray(new ActionPermissionEntry[entries.size()]), authorizeContext);
		});
	}

}
