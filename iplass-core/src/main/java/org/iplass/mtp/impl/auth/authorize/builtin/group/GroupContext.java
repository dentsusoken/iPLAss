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

package org.iplass.mtp.impl.auth.authorize.builtin.group;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;

public class GroupContext implements Comparable<GroupContext> {
	private String oid;
	private String groupCode;
	private String parentGroupCode;
	private String[] childGroupCodes;
	private TenantAuthorizeContext authContext;
	
	public GroupContext(String oid, String groupCode, String parentGroupCode, String[] childGroupCodes, TenantAuthorizeContext authContext) {
		this.oid = oid;
		this.groupCode = groupCode;
		this.parentGroupCode = parentGroupCode;
		this.childGroupCodes = childGroupCodes;
		this.authContext = authContext;
	}
	
	public String getOid() {
		return oid;
	}
	
	public String getGroupCode() {
		return groupCode;
	}
	
	public String getParentGroupCode() {
		return parentGroupCode;
	}
	
	public boolean isRoot() {
		return parentGroupCode == null;
	}
	
	public List<GroupContext> getAllNestedChildGroup() {
		List<GroupContext> groups = new ArrayList<GroupContext>();
		addGroupCascadeChild(groups);
		return groups;
	}
	
	private void addGroupCascadeChild(List<GroupContext> groups) {
		groups.add(this);
		if (childGroupCodes != null) {
			for (String cgc: childGroupCodes) {
				GroupContext child = authContext.getGroupContext(cgc);
				if(child != null) {
					child.addGroupCascadeChild(groups);
				}
			}
		}
	}
	
	public List<GroupContext> getGroupPath() {
		LinkedList<GroupContext> path = new LinkedList<GroupContext>();
		addPath(path);
		return path;
	}
	
	private void addPath(LinkedList<GroupContext> path) {
		if (parentGroupCode != null) {
			GroupContext parent = authContext.getGroupContext(parentGroupCode);
			if (parent != null) {
				parent.addPath(path);
			}
		}
		path.add(this);
	}

	@Override
	public int compareTo(GroupContext o) {
		return oid.compareTo(o.oid);
	}
	
}
