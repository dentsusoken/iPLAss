/*
 * Copyright (C) 2024 DENTSU SOKEN INC. All Rights Reserved.
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

import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.EntityNormalizeInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.impl.entity.EntityHandler;

public class EntityNormalizeInvocationImpl extends EntityInvocationImpl<Void> implements EntityNormalizeInvocation {

	private Entity entity;
	private List<String> validatePropertyList;

	public EntityNormalizeInvocationImpl(Entity entity, List<String> validatePropertyList,
			EntityInterceptor[] entityInterceptors, EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.entity = entity;
		this.validatePropertyList = validatePropertyList;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public List<String> getNormalizePropertyList() {
		return validatePropertyList;
	}

	public void setNormalizePropertyList(List<String> validatePropertyList) {
		this.validatePropertyList = validatePropertyList;
	}

	@Override
	protected Void callEntityHandler(EntityHandler eh) {
		eh.normalize(entity, validatePropertyList);
		return null;
	}

	@Override
	public InvocationType getType() {
		return InvocationType.NORMALIZE;
	}

}
