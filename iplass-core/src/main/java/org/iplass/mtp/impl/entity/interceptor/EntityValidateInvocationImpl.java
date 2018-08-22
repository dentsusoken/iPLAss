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

package org.iplass.mtp.impl.entity.interceptor;

import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.ValidateResult;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.EntityValidateInvocation;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.impl.entity.EntityHandler;


public class EntityValidateInvocationImpl extends EntityInvocationImpl<ValidateResult> implements EntityValidateInvocation {
	
	private Entity entity;
	private List<String> validatePropertyList;
	private UpdateOption updateOption;
	private InsertOption insertOption;
	
	public EntityValidateInvocationImpl (
			Entity entity, List<String> validatePropertyList, UpdateOption updateOption,
			EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.entity = entity;
		this.validatePropertyList = validatePropertyList;
		this.updateOption = updateOption;
	}
	
	public EntityValidateInvocationImpl (
			Entity entity, InsertOption insertOption,
			EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.entity = entity;
		this.insertOption = insertOption;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public List<String> getValidatePropertyList() {
		return validatePropertyList;
	}
	public void setValidatePropertyList(List<String> validatePropertyList) {
		this.validatePropertyList = validatePropertyList;
	}
	
	public UpdateOption getUpdateOption() {
		return updateOption;
	}
	
	public InsertOption getInsertOption() {
		return insertOption;
	}

	@Override
	protected ValidateResult callEntityHandler(EntityHandler eh) {
		//FIXME ehでValidateResultを返す。
		return new ValidateResult(eh.validate(entity, validatePropertyList));
	}

	@Override
	public InvocationType getType() {
		return InvocationType.VALIDATE;
	}
	
}
