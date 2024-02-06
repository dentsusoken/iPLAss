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

import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.EntityUpdateAllInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.impl.entity.EntityHandler;

public class EntityUpdateAllInvocationImpl extends EntityInvocationImpl<Integer> implements EntityUpdateAllInvocation {
	
	private UpdateCondition updateCondition;
	
	public EntityUpdateAllInvocationImpl(
			UpdateCondition updateCondition,
			EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.updateCondition = updateCondition;
	}
	
	public UpdateCondition getUpdateCondition() {
		return updateCondition;
	}
	public void setUpdateCondition(UpdateCondition updateCondition) {
		this.updateCondition = updateCondition;
	}
	
	@Override
	protected Integer callEntityHandler(EntityHandler eh) {
		return eh.updateAll(updateCondition);
	}

	@Override
	public InvocationType getType() {
		return InvocationType.UPDATE_ALL;
	}
	
}
