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

package org.iplass.mtp.impl.auth;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.iplass.mtp.auth.Group;
import org.iplass.mtp.auth.User;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.impl.auth.authenticate.AccountHandle;

public class UserContextImpl implements UserContext {
	private static final long serialVersionUID = 8750752179647454737L;

	private AccountHandle account;
	private User userEntity;
	private String[] groupCode;
	private long creationTime;
	
	public UserContextImpl(AccountHandle account, User userEntity) {
		this.account = account;
		this.userEntity = userEntity;
		initGroupCode();
		creationTime = System.currentTimeMillis();
	}

	private void initGroupCode() {
		Set<String> list = new HashSet<>();
		if (userEntity != null) {
			Entity[] group = userEntity.getGroups();
			if (group != null) {
				for (Entity g: group) {
					list.add(g.<String>getValue(Group.CODE));
				}
			}
		}
		Map<String, Object> accountAttributes = account.getAttributeMap();
		if (accountAttributes != null) {
			Object groupCode = accountAttributes.get(AccountHandle.GROUP_CODE);
			if (groupCode != null) {
				if (groupCode instanceof String) {
					if (((String) groupCode).indexOf(',') >= 0) {
						String[] gcs = ((String) groupCode).split(",");
						for (String gc: gcs) {
							String gctrimed = gc.trim();
							if (!gctrimed.isEmpty()) {
								list.add(gctrimed);
							}
						}
					} else {
						list.add((String) groupCode);
					}
				} else if (groupCode instanceof String[]) {
					String[] gCodeList = (String[]) groupCode;
					for (String g: gCodeList) {
						list.add(g);
					}
				} else {
					throw new IllegalStateException("groupCode supports String/String[] types");
				}
			}
		}
		if (list.size() != 0) {
			groupCode = list.toArray(new String[list.size()]);
		} else {
			groupCode = null;
		}
	}

	public User getUser() {
		return userEntity;
	}

	public AccountHandle getAccount() {
		return account;
	}

	@Override
	public Object getAttribute(String name) {
		Object val = null;
		if (userEntity != null) {
			val = userEntity.getValue(name);
		}
		if (val == null) {
			Map<String, Object> accountAttributeMap = account.getAttributeMap();
			if (accountAttributeMap != null) {
				val = accountAttributeMap.get(name);
			}
		}
		return val;
	}

	@Override
	public String[] getGroupCode() {
		if (groupCode == null) {
			return null;
		}
		String[] copy = new String[groupCode.length];
		System.arraycopy(groupCode, 0, copy, 0, groupCode.length);
		return copy;
	}

	@Override
	public String getIdForLog() {
		if(userEntity == null) {
			return account.getCredential().getId();
		} else {
			return userEntity.getOid();
		}
	}

	@Override
	public void resetUserEntity(User userEntity) {
		this.userEntity = userEntity;
		initGroupCode();
	}

	@Override
	public long getCreationTime() {
		return creationTime;
	}

}
