/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.adminconsole.shared.tools.rpc.permissionexplorer;

import java.util.List;

import org.iplass.adminconsole.shared.base.rpc.entity.EntityDataTransferService;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionInfo;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionSearchResult;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.UpdateRoleInfo;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.XsrfProtectedService;

/**
 * PermissionExplorer用Service
 */
@RemoteServiceRelativePath("service/permissionexplorer")
public interface PermissionExplorerService extends XsrfProtectedService, EntityDataTransferService {

	/**
	 * <p>Roleの全リストを返します。</p>
	 * <p>参照Propertyは取得しません。</p>
	 *
	 * @param tenantId テナントID
	 * @return Roleの全リスト
	 */
	List<Entity> getRoleList(int tenantId);
	Entity loadRoleData(int tenantId, final String oid);
	void updateRoleData(int tenantId, final UpdateRoleInfo storeInfo);

	PermissionSearchResult getAllEntityPermissionData(int tenantId);
	void updateEntityPermissionData(int tenantId, List<PermissionInfo> permissionList);

	PermissionSearchResult getAllActionPermissionData(int tenantId);
	void updateActionPermissionData(int tenantId, List<PermissionInfo> permissionList);

	PermissionSearchResult getAllWebApiPermissionData(int tenantId);
	void updateWebApiPermissionData(int tenantId, List<PermissionInfo> permissionList);

	void dummyConnect(int tenantId);

}
