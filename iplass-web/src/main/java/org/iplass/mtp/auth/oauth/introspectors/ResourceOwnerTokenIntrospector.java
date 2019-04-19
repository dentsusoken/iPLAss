/*
 * Copyright (C) 2019 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.auth.oauth.introspectors;

import java.util.Map;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.auth.oauth.CustomTokenIntrospector;
import org.iplass.mtp.command.RequestContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.tenant.Tenant;

/**
 * 
 * Tokenを所有するUserの情報を返却するCustomTokenIntrospectorです。
 * レスポンスに"resource_owner"をキー名にUserエンティティを返却します。
 * また、"tenant_id"でテナントID、"tenant_name"でテナント名を返却します。
 * 
 * @author K.Higuchi
 *
 */
public class ResourceOwnerTokenIntrospector implements CustomTokenIntrospector {
	
	public static final String RESOURCE_OWNER_NAME = "resource_owner";
	public static final String TENANT_ID = "tenant_id";
	public static final String TENANT_NAME = "tenant_name";

	@Override
	public boolean handle(Map<String, Object> response, RequestContext request, User resourceOwner) {
		response.put(RESOURCE_OWNER_NAME, resourceOwner);
		Tenant tenant = ExecuteContext.getCurrentContext().getCurrentTenant();
		response.put(TENANT_ID, tenant.getId());
		response.put(TENANT_NAME, tenant.getName());
		return true;
	}

}
