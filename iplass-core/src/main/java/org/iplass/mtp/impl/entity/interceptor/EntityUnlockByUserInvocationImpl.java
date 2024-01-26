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

package org.iplass.mtp.impl.entity.interceptor;

import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.EntityUnlockByUserInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.impl.entity.EntityHandler;

public class EntityUnlockByUserInvocationImpl extends EntityInvocationImpl<Boolean> implements EntityUnlockByUserInvocation {
	
	private String oid;
	private String userId;
	private boolean force;
	
	public EntityUnlockByUserInvocationImpl(String oid, String userId, boolean force, EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.oid = oid;
		this.userId = userId;
		this.force = force;
	}
	
	public String getOid() {
		return oid;
	}
	
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	@Override
	protected Boolean callEntityHandler(EntityHandler eh) {
		return eh.unlockEntityByUser(oid, userId, force);
	}

	@Override
	public InvocationType getType() {
		return InvocationType.UNLOCK_BY_USER;
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean isForce() {
		return force;
	}

	@Override
	public void setForce(boolean force) {
		this.force = force;
	}
	
}
