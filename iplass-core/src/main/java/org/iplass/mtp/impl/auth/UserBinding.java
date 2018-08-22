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

package org.iplass.mtp.impl.auth;

import java.util.List;
import java.util.TreeSet;

import org.iplass.mtp.auth.User;
import org.iplass.mtp.impl.auth.authenticate.AnonymousUserContext;
import org.iplass.mtp.impl.auth.authorize.builtin.TenantAuthorizeContext;
import org.iplass.mtp.impl.auth.authorize.builtin.group.GroupContext;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.core.TenantContext;

public class UserBinding {
	//FIXME TenantAuthorizeContext, GroupContextをbuiltinから外だしする
	
	//TODO group取得ロジックをキャッシュ
	
	private static final String GROUP_CODE = "groupCode";
	
	private UserContext userContext;
	private TenantAuthorizeContext authContext;
	
	private boolean otherTenant;
	private int tenantId;
	
	UserBinding(UserContext userContext, TenantAuthorizeContext authContext) {
		this.userContext = userContext;
		this.authContext = authContext;
		TenantContext callerTenant = ExecuteContext.getCurrentContext().getTenantContext();
		this.otherTenant = callerTenant.getTenantId() != authContext.getTenantContext().getTenantId();
		this.tenantId = callerTenant.getTenantId();
	}
	
	public Object getAttribute(String name) {
		if (name.equals(GROUP_CODE)) {
			return userContext.getGroupCode();
		} else if (name.equals(User.ADMIN_FLG) && otherTenant) {
			return false;
		} else if (name.equals(User.ACCOUNT_ID) && userContext instanceof AnonymousUserContext) {
			//既存でaccountId == nullで未ログインを判断しているコードが存在するので、、それに対応するため、ここでnullを返す。
			return null;
		} else {
			return userContext.getAttribute(name);
		}
	}
	
	public boolean isAnonymous() {
		return userContext.getUser().isAnonymous();
	}
	
	public boolean isAdmin() {
		if (otherTenant) {
			return false;
		}
		return userContext.getUser().isAdmin();
	}
	
	public boolean isLocalAdmin() {
		if (otherTenant
				&& userContext.getUser().isAdmin()) {
			return true;
		}
		return false;
	}
	
	public boolean isGrantAllPermissions() {
		if (otherTenant) {
			return false;
		}
		return userContext.getUser().isAdmin() && authContext.isGrantAllPermissionsToAdmin();
	}
	
	public boolean isOtherTenant() {
		return otherTenant;
	}
	
	public int getTenantId() {
		return tenantId;
	}
	
	public String[] getGroupCodeWithChildren() {
		String[] groupCodeList = (String[]) userContext.getGroupCode();
		if (groupCodeList == null) {
			return null;
		}
		TreeSet<String> list = new TreeSet<String>();
		for (String gc: groupCodeList) {
			GroupContext groupContext = authContext.getGroupContext(gc);
			if (groupContext != null) {
				List<GroupContext> gcl = groupContext.getAllNestedChildGroup();
				for (GroupContext gclgc: gcl) {
					list.add(gclgc.getGroupCode());
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public String[] getGroupCodeWithParents() {
		String[] groupCodeList = (String[]) userContext.getGroupCode();
		if (groupCodeList == null) {
			return null;
		}
		TreeSet<String> list = new TreeSet<String>();
		for (String gc: groupCodeList) {
			GroupContext groupContext = authContext.getGroupContext(gc);
			if (groupContext != null) {
				List<GroupContext> gcl = groupContext.getGroupPath();
				for (GroupContext gclgc: gcl) {
					list.add(gclgc.getGroupCode());
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public String[] getGroupOidWithChildren() {
		String[] groupCodeList = (String[]) userContext.getGroupCode();
		if (groupCodeList == null) {
			return null;
		}
		TreeSet<String> list = new TreeSet<String>();
		for (String gc: groupCodeList) {
			GroupContext groupContext = authContext.getGroupContext(gc);
			if (groupContext != null) {
				List<GroupContext> gcl = groupContext.getAllNestedChildGroup();
				for (GroupContext gclgc: gcl) {
					list.add(gclgc.getOid());
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public String[] getGroupOidWithParents() {
		String[] groupCodeList = (String[]) userContext.getGroupCode();
		if (groupCodeList == null) {
			return null;
		}
		TreeSet<String> list = new TreeSet<String>();
		for (String gc: groupCodeList) {
			GroupContext groupContext = authContext.getGroupContext(gc);
			if (groupContext != null) {
				List<GroupContext> gcl = groupContext.getGroupPath();
				for (GroupContext gclgc: gcl) {
					list.add(gclgc.getOid());
				}
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public String[] getGroupOid() {
		String[] groupCodeList = (String[]) userContext.getGroupCode();
		if (groupCodeList == null) {
			return null;
		}
		TreeSet<String> list = new TreeSet<String>();
		for (String gc: groupCodeList) {
			GroupContext groupContext = authContext.getGroupContext(gc);
			if (groupContext != null) {
				list.add(groupContext.getOid());
			}
		}
		return list.toArray(new String[list.size()]);
	}
	
	public boolean memberOf(String groupCode) {
		String[] groupCodeList = (String[]) userContext.getGroupCode();
		if (groupCodeList == null) {
			return false;
		}
		for (String code: groupCodeList) {//広さ優先（DB検索の可能性が少しでもへるので）
			if (code.equals(groupCode)) {
				return true;
			}
		}
		for (String code: groupCodeList) {//グループ階層をたどりチェック
			GroupContext group = authContext.getGroupContext(code);
			if (group != null) {
				List<GroupContext> path = group.getGroupPath();
				for (GroupContext gc: path) {
					if (gc.getGroupCode().equals(groupCode)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
}
