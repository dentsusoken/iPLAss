/*
 * Copyright (C) 2011 DENTSU SOKEN INC. All Rights Reserved.
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

package org.iplass.mtp.entity.permission;

import org.iplass.mtp.auth.Permission;

/**
 * Entityのpropertyの権限。
 * Entity定義名×property名×Action（登録、参照、更新）単位で権限を表現。
 * 
 * @author K.Higuchi
 *
 */
public class EntityPropertyPermission extends Permission {
	
	public enum Action {
		REFERENCE, CREATE, UPDATE;
	}
	
	private final String definitionName;
	private final String propertyName;
	private final Action action;
	
	public EntityPropertyPermission(String definitionName, String propertyName, Action action) {
		this.definitionName = definitionName;
		this.propertyName = propertyName;
		this.action = action;
	}

	public final String getDefinitionName() {
		return definitionName;
	}

	public final String getPropertyName() {
		return propertyName;
	}

	public final Action getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((definitionName == null) ? 0 : definitionName.hashCode());
		result = prime * result
				+ ((propertyName == null) ? 0 : propertyName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityPropertyPermission other = (EntityPropertyPermission) obj;
		if (action != other.action)
			return false;
		if (definitionName == null) {
			if (other.definitionName != null)
				return false;
		} else if (!definitionName.equals(other.definitionName))
			return false;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EntityPropertyPermission [definitionName=" + definitionName + ", propertyName=" + propertyName
				+ ", action=" + action + "]";
	}

}
