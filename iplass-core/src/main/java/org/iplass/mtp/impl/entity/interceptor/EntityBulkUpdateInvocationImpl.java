/*
 * Copyright (C) 2015 DENTSU SOKEN INC. All Rights Reserved.
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

import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.interceptor.EntityBulkUpdateInvocation;
import org.iplass.mtp.entity.interceptor.EntityInterceptor;
import org.iplass.mtp.entity.interceptor.InvocationType;
import org.iplass.mtp.impl.entity.EntityHandler;

public class EntityBulkUpdateInvocationImpl extends EntityInvocationImpl<Void> implements EntityBulkUpdateInvocation {
	
	private BulkUpdatable bulkUpdatable;
	
	public EntityBulkUpdateInvocationImpl(
			BulkUpdatable bulkUpdatable,
			EntityInterceptor[] entityInterceptors,
			EntityHandler entityHandler) {
		super(entityInterceptors, entityHandler);
		this.bulkUpdatable = bulkUpdatable;
	}
	
	@Override
	protected Void callEntityHandler(EntityHandler eh) {
		eh.bulkUpdate(bulkUpdatable);
		return null;
	}

	@Override
	public InvocationType getType() {
		return InvocationType.BULK_UPDATE;
	}

	@Override
	public BulkUpdatable getBulkUpdatable() {
		return bulkUpdatable;
	}

	@Override
	public void setBulkUpdatable(BulkUpdatable bulkUpdatable) {
		this.bulkUpdatable = bulkUpdatable;
	}
	
}
