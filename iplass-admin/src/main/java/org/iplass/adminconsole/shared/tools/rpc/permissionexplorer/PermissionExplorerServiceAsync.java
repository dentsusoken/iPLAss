/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

import org.iplass.adminconsole.shared.base.dto.entity.EntityDataTransferTypeList;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionInfo;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.PermissionSearchResult;
import org.iplass.adminconsole.shared.tools.dto.permissionexplorer.UpdateRoleInfo;
import org.iplass.mtp.entity.Entity;

import com.google.gwt.user.client.rpc.AsyncCallback;



/**
 * PermissionExplorer用AsyncService
 */
public interface PermissionExplorerServiceAsync {

	/**
	 * Entityデータを送受信する場合に、値の型をGWTのホワイトリストに追加するためのメソッドです。
	 */
	void entityDataTypeWhiteList(EntityDataTransferTypeList param, AsyncCallback<EntityDataTransferTypeList> callback);

	void getRoleList(int tenantId, AsyncCallback<List<Entity>> callback);
	void loadRoleData(int tenantId, final String oid, AsyncCallback<Entity> callback);
	void updateRoleData(int tenantId, final UpdateRoleInfo storeInfo, AsyncCallback<Void> callback);

	void getAllEntityPermissionData(int tenantId, AsyncCallback<PermissionSearchResult> callback);
	void updateEntityPermissionData(int tenantId, List<PermissionInfo> permissionList, AsyncCallback<Void> callback);

	void getAllActionPermissionData(int tenantId, AsyncCallback<PermissionSearchResult> callback);
	void updateActionPermissionData(int tenantId, List<PermissionInfo> permissionList, AsyncCallback<Void> callback);

	void getAllWebApiPermissionData(int tenantId, AsyncCallback<PermissionSearchResult> callback);
	void updateWebApiPermissionData(int tenantId, List<PermissionInfo> permissionList, AsyncCallback<Void> callback);

	void dummyConnect(int tenantId, AsyncCallback<Void> callback);
}
