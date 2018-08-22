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

package org.iplass.mtp.impl.auth.authorize.builtin.entity;

import java.util.HashSet;

class EntityPropertyPermissionEntry {
	
	private String role;
	private boolean isDefinePermit;//許可を定義しているか、否許可を定義しているか
	private HashSet<String> propertyNameSet;
	
	EntityPropertyPermissionEntry(String role, boolean isDefinePermit, HashSet<String> propertyNameSet) {
		this.role = role;
		this.isDefinePermit = isDefinePermit;
		this.propertyNameSet = propertyNameSet;
	}
	
	EntityPropertyPermissionEntry(String role, boolean isDefinePermit, String[] propertyNames) {
		this.role = role;
		this.isDefinePermit = isDefinePermit;
		if (propertyNames != null) {
			this.propertyNameSet = new HashSet<String>();
			for (String pn: propertyNames) {
				this.propertyNameSet.add(pn);
			}
		}
	}
	
	public String getRole() {
		return role;
	}
	
	public boolean isPermit(String propName) {
		boolean contains = false;
		if (propertyNameSet != null) {
			contains = propertyNameSet.contains(propName);
		}
		if (isDefinePermit) {
			return contains;
		} else {
			return !contains;
		}
	}
	
}
