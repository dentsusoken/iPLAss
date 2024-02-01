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

package org.iplass.adminconsole.shared.tools.dto.permissionexplorer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermissionInfo implements Serializable {

	private static final long serialVersionUID = -5846856630820745222L;

	private String definitionName;
	private String displayName;

//	// ActionPermission, WebApiPermissionで利用
//	private List<String> roleCodeList;

//	// EntityPermission, WorkflowPermission, CubePermissionで利用
//	private Map<String, String> detailByRole;

	// EntityPermission, WorkflowPermission, CubePermissionで利用
	private List<RolePermissionInfo> rolePermissionList;


//	/**
//	 * permissionValue
//	 * key : roleCode
//	 * value : permission setting info string
//	 */
//	public Map<String, String> getDetailByRole() {
//		return detailByRole;
//	}
//	public void setDetailByRole(Map<String, String> detailByRole) {
//		this.detailByRole = detailByRole;
//	}

	public String getDefinitionName() {
		return definitionName;
	}
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

//	public List<String> getRoleCodeList() {
//		return roleCodeList;
//	}
//	public void setRoleCodeList(List<String> roleCodeList) {
//		this.roleCodeList = roleCodeList;
//	}

	public List<RolePermissionInfo> getRolePermissionList() {
		return rolePermissionList;
	}
	public void setRolePermissionList(List<RolePermissionInfo> rolePermissionList) {
		this.rolePermissionList = rolePermissionList;
	}
	public void addRolePermission(RolePermissionInfo rolePermission) {
		if (rolePermissionList == null) {
			rolePermissionList = new ArrayList<RolePermissionInfo>();
		}
		rolePermissionList.add(rolePermission);
	}

}
