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

package org.iplass.mtp.impl.auth.authorize;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.AuthService;
import org.iplass.mtp.spi.ServiceInitListener;

/**
 * 認可プロバイダのインタフェース。
 *
 * @author K.Higuchi
 *
 */
public interface AuthorizationProvider extends ServiceInitListener<AuthService> {

	//sharedの権限の際に、shareTenant側のロールで判断したいため、tenantIdの引数が必要。。。
	public boolean userInRole(AuthContextHolder userAuthContext, int tenantId, String role);
	
	public boolean useSharedPermission(Permission permission);
	
	public AuthorizationContext getAuthorizationContext(int tenantId, Permission permission);

}
