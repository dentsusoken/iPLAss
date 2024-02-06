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

import org.iplass.mtp.entity.DeleteCondition;
import org.iplass.mtp.entity.interceptor.EntityDeleteAllInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.impl.entity.EntityHandler;

public class EntityDeleteAllInvocationImpl extends EntityInvocationImpl<Integer> implements EntityDeleteAllInvocation {
	
	private DeleteCondition deleteCondition;
	
	public EntityDeleteAllInvocationImpl(
			DeleteCondition deleteCondition,
			EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.deleteCondition = deleteCondition;
	}

	public DeleteCondition getDeleteCondition() {
		return deleteCondition;
	}
	
	public void setDeleteCondition(DeleteCondition deleteCondition) {
		this.deleteCondition = deleteCondition;
	}

	@Override
	protected Integer callEntityHandler(EntityHandler eh) {
		return eh.deleteAll(deleteCondition);
	}

	@Override
	public InvocationType getType() {
		return InvocationType.DELETE_ALL;
	}

}
