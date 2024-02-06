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
package org.iplass.mtp.impl.auth.authorize.builtin.entity;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.entity.permission.EntityPermission;
import org.iplass.mtp.entity.permission.EntityPropertyPermission;
import org.iplass.mtp.impl.auth.authorize.AuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthorizationContextCacheLogic;
import org.iplass.mtp.impl.auth.authorize.builtin.AuthorizationContextHandler;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityService;

public class EntityAuthContextHandler extends AuthorizationContextHandler {
	private static final Class<?>[] PERM_TYPE = {EntityPermission.class, EntityPropertyPermission.class};

	private boolean useCorrelatedSubqueryOnEntityLimitCondition;
	
	public EntityAuthContextHandler() {
	}

	public boolean isUseCorrelatedSubqueryOnEntityLimitCondition() {
		return useCorrelatedSubqueryOnEntityLimitCondition;
	}

	public void setUseCorrelatedSubqueryOnEntityLimitCondition(
			boolean useCorrelatedSubqueryOnEntityLimitCondition) {
		this.useCorrelatedSubqueryOnEntityLimitCondition = useCorrelatedSubqueryOnEntityLimitCondition;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Permission>[] permissionType() {
		return (Class<? extends Permission>[]) PERM_TYPE;
	}

	@Override
	protected String cacheNamespace() {
		return "mtp.auth.builtin.entity";
	}
	
	@Override
	protected String contextName(Permission permission) {
		if (permission instanceof EntityPermission) {
			return ((EntityPermission) permission).getDefinitionName();
		} else {
			return ((EntityPropertyPermission) permission).getDefinitionName();
		}
	}

	@Override
	protected String toMetaDataPath(String contextName) {
		return EntityService.ENTITY_META_PATH + contextName.replace('.', '/');
	}

	@Override
	protected AuthorizationContextCacheLogic newAuthorizeContextCacheLogic(TenantAuthorizeContext tac) {
		return new EntityAuthContextCacheLogic(tac);
	}

	@Override
	protected AuthorizationContext defaultAuthorizationContext(String contextName, TenantAuthorizeContext tac) {
		//他テナントのユーザーの場合は、ReadOnlyがデフォルト
		if (ExecuteContext.getCurrentContext().getClientTenantId() != tac.getTenantContext().getTenantId()) {
			return new DefaultEntityAuthContext(contextName, null, null, tac, true);
		} else {
			return new DefaultEntityAuthContext(contextName, null, null, tac, false);
		}
	}

}
