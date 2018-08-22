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

import org.iplass.mtp.entity.Entity;

public class RolePermissionInfo implements Serializable {

	private static final long serialVersionUID = -7065987341212045646L;

	public static final String INSERT = "I";
	public static final String UPDATE = "U";
	public static final String DELETE = "D";

	private String status;	//blank・・変更なし、I・・Insert、U・・Update、D・・Delete
	private Entity permission;

	public RolePermissionInfo(){
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Entity getPermission() {
		return permission;
	}

	public void setPermission(Entity permission) {
		this.permission = permission;
	}

}
