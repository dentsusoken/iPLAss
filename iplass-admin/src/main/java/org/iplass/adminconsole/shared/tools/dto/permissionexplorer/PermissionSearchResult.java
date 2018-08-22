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

package org.iplass.adminconsole.shared.tools.dto.permissionexplorer;

import java.io.Serializable;
import java.util.List;

public class PermissionSearchResult implements Serializable {

	private static final long serialVersionUID = 5500272689863867771L;

	private List<PermissionInfo> permissionList;

	private List<PermissionInfo> wildCardPermissionList;

	public PermissionSearchResult(){
	}

	public List<PermissionInfo> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<PermissionInfo> permissionList) {
		this.permissionList = permissionList;
	}

	public List<PermissionInfo> getWildCardPermissionList() {
		return wildCardPermissionList;
	}

	public void setWildCardPermissionList(List<PermissionInfo> wildCardPermissionList) {
		this.wildCardPermissionList = wildCardPermissionList;
	}


}
