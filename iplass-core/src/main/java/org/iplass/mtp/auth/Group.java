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

package org.iplass.mtp.auth;

import org.iplass.mtp.entity.GenericEntity;

/**
 * グループを表す。
 * Entity定義上、mtp.auth.Groupで定義される。
 * グループにはユーザーを所属させることが可能。
 * 
 * @author K.Higuchi
 *
 */
public class Group extends GenericEntity {
	private static final long serialVersionUID = -8721382756470446696L;

	public static final String DEFINITION_NAME = "mtp.auth.Group";
	public static final String CODE = "code";
	public static final String PARENT = "parent";
	public static final String CHILDREN = "children";
	
	public Group() {
		setDefinitionName(DEFINITION_NAME);
	}
	
	public Group(String oid, String code) {
		setDefinitionName(DEFINITION_NAME);
		setOid(oid);
		setCode(code);
	}
	
	public String getCode() {
		return getValue(CODE);
	}
	
	public void setCode(String code) {
		setValue(CODE, code);
	}
	
	public Group getParent() {
		return getValue(PARENT);
	}
	
	public void setParent(Group parent) {
		setValue(PARENT, parent);
	}
	
	public Group[] getChildren() {
		Object g = getValue(CHILDREN);
		if (g instanceof Group) {
			return new Group[]{(Group) g};
		} else {
			return (Group[]) g;
		}
	}
	
	public void setChildren(Group[] children) {
		setValue(CHILDREN, children);
	}
}
