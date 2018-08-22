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

package org.iplass.mtp.impl.auth.authorize.builtin.webapi;

import java.util.LinkedList;
import java.util.List;

import org.iplass.mtp.auth.Permission;
import org.iplass.mtp.impl.auth.AuthContextHolder;
import org.iplass.mtp.impl.auth.UserBinding;
import org.iplass.mtp.impl.auth.authorize.builtin.BuiltinAuthorizationContext;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.auth.authorize.builtin.role.RoleContext;
import org.iplass.mtp.impl.webapi.auth.WebApiAuthContext;
import org.iplass.mtp.webapi.permission.WebApiPermission;

class BuiltinWebApiAuthContext extends BuiltinAuthorizationContext implements WebApiAuthContext {

	private WebApiPermissionEntry[] permissionEntry;
	private TenantAuthorizeContext tenantAuthContext;
	
	private boolean hasParameterCondition;

	//複数ロールがある場合、基本はそれぞれのロールの権限をOR（許可と否許可では許可優先）。
	//ロールには優先順位を定義可能で、優先順位の高いロールがあった場合は、それの権限を優先。

	//個別のロール単位で複数の権限定義があった場合の考え方は、否許可を優先（AND）
	//Entryがない場合、許可
	//1つでもEntryがある場合、全体に対して否許可
	//EntryがあるWebAPIに対して許可がある場合、許可
	//同一WebAPIに対して否許可があった場合、否許可

	BuiltinWebApiAuthContext(String webApiName, WebApiPermissionEntry[] permissionEntry, TenantAuthorizeContext tenantAuthContext) {
		super(webApiName);
		this.permissionEntry = permissionEntry;
		this.tenantAuthContext = tenantAuthContext;
		if (permissionEntry != null) {
			for (WebApiPermissionEntry wpe: permissionEntry) {
				if (wpe.hasParam()) {
					hasParameterCondition = true;
					break;
				}
			}
		}
	}

	private List<WebApiPermissionEntry> listTarget(AuthContextHolder userAuthContext) {
		List<WebApiPermissionEntry> target = new LinkedList<WebApiPermissionEntry>();
		long currentPriority = 0;
		for (int i = 0; i < permissionEntry.length; i++) {
			if (userAuthContext.userInRole(permissionEntry[i].getRole(), tenantAuthContext.getTenantContext().getTenantId())) {
				RoleContext role = tenantAuthContext.getRoleContext(permissionEntry[i].getRole());
				if (currentPriority < role.getPriority()) {
					//reset priority
					currentPriority = role.getPriority();
					target.clear();
				}
				if (currentPriority == role.getPriority()) {
					target.add(permissionEntry[i]);
				}
			}
		}
		return target;
	}

	@Override
	public boolean isPermit(Permission permission, AuthContextHolder user) {
		//admin（かつ、他テナントユーザでない）は全権限保持
		UserBinding userBinding = user.newUserBinding(tenantAuthContext);
		if (userBinding.isGrantAllPermissions()) {
			return true;
		}
		
		WebApiPermission wap = (WebApiPermission) permission;

		List<WebApiPermissionEntry> target = listTarget(user);
		for (WebApiPermissionEntry pe: target) {
			if (pe.isPermit(user, wap.getWebApiName(), wap.getParameter(), tenantAuthContext)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isResultCacheable(Permission permission) {
		if (hasParameterCondition) {
			return false;
		} else {
			return true;
		}
	}

}
