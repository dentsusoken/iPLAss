/*
 * Copyright (C) 2015 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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
package org.iplass.mtp.impl.entity;

import java.util.Iterator;
import java.util.List;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.GenericEntity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.bulkupdate.BulkUpdatable;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity;
import org.iplass.mtp.entity.bulkupdate.BulkUpdateEntity.UpdateMethod;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.properties.extend.AutoNumberType;
import org.iplass.mtp.impl.properties.extend.ComplexWrapperType;

class BulkUpdateAdapter implements BulkUpdatable {
	
	private static final String BEFORE_UPDATE_ENTITY = "____mtp_beforeUpdateEntity_";
	
	private BulkUpdatable actual;
	private EntityHandler eh;
	private EntityContext ec;
	
	private UpdateOption op;
	
	private List<PropertyHandler> complexWrapperTypePropList;
	private List<PropertyHandler> autoNumPropList;

	
	BulkUpdateAdapter(BulkUpdatable actual, EntityHandler eh, EntityContext ec) {
		this.actual = actual;
		this.eh = eh;
		this.ec = ec;
		complexWrapperTypePropList = eh.getPropertyListByPropertyType(ComplexWrapperType.class, ec);
		autoNumPropList = eh.getPropertyListByPropertyType(AutoNumberType.class, ec);
		if (actual.getUpdateProperties() != null && actual.getUpdateProperties().size() > 0) {
			op = new UpdateOption(false);
			op.setUpdateProperties(actual.getUpdateProperties());
		}
	}
	
	@Override
	public Iterator<BulkUpdateEntity> iterator() {
		return new It(actual.iterator(), ExecuteContext.getCurrentContext().getClientId());
	}

	@Override
	public String getDefinitionName() {
		return actual.getDefinitionName();
	}

	@Override
	public void updated(BulkUpdateEntity updatedEntity) {
		if (updatedEntity instanceof BulkUpdateEntityWrapper) {
			if (updatedEntity.getMethod() == UpdateMethod.DELETE) {
				Entity before = updatedEntity.getEntity().getValue(BEFORE_UPDATE_ENTITY);
				if (before != null) {
					eh.postporcessDeleteDirect(before, ec, complexWrapperTypePropList, null);
				}
			}
			
			BulkUpdateEntity forRet = ((BulkUpdateEntityWrapper) updatedEntity).actualBulkUpdateEntity;
			//oid,version,autoNumをコピー
			forRet.getEntity().setOid(updatedEntity.getEntity().getOid());
			forRet.getEntity().setVersion(updatedEntity.getEntity().getVersion());

			//auto numberを通知
			if (autoNumPropList != null) {
				for (PropertyHandler ph: autoNumPropList) {
					forRet.getEntity().setValue(ph.getName(), updatedEntity.getEntity().getValue(ph.getName()));
				}
			}
			
			actual.updated(forRet);
		} else {
			actual.updated(updatedEntity);
		}
	}

	@Override
	public void close() {
		actual.close();
	}
	
	@Override
	public List<String> getUpdateProperties() {
		return actual.getUpdateProperties();
	}

	private static class BulkUpdateEntityWrapper extends BulkUpdateEntity {
		BulkUpdateEntity actualBulkUpdateEntity;
		
		public BulkUpdateEntityWrapper(BulkUpdateEntity actualBulkUpdateEntity) {
			this.actualBulkUpdateEntity = actualBulkUpdateEntity;
			setMethod(actualBulkUpdateEntity.getMethod());
			setEntity(((GenericEntity) actualBulkUpdateEntity.getEntity()).copy());
		}

	}
	
	private class It implements Iterator<BulkUpdateEntity> {
		
		private Iterator<BulkUpdateEntity> actualIt;
		private String clientId;
		
		private It(Iterator<BulkUpdateEntity> actualIt, String clientId) {
			this.actualIt = actualIt;
			this.clientId = clientId;
		}

		@Override
		public boolean hasNext() {
			return actualIt.hasNext();
		}

		@Override
		public BulkUpdateEntity next() {
			BulkUpdateEntityWrapper forInternalUse = new BulkUpdateEntityWrapper(actualIt.next());
			if (forInternalUse.getEntity().getVersion() == null) {
				forInternalUse.getEntity().setVersion(Long.valueOf(0));
			}
			forInternalUse.getEntity().setCreateBy(clientId);
			forInternalUse.getEntity().setUpdateBy(clientId);
			if (forInternalUse.getEntity().getState() == null) {
				forInternalUse.getEntity().setState(new SelectValue(Entity.STATE_VALID_VALUE));
			}

			switch (forInternalUse.actualBulkUpdateEntity.getMethod()) {
			case INSERT:
				eh.preprocessInsertDirect(forInternalUse.getEntity(), ec, complexWrapperTypePropList);
				break;
			case UPDATE:
				eh.preprocessUpdateDirect(forInternalUse.getEntity(), op, ec, complexWrapperTypePropList, true);
				break;
			case DELETE:
				Entity beforedelete = eh.preporcessDeleteDirect(forInternalUse.getEntity(), ec, complexWrapperTypePropList);
				forInternalUse.getEntity().setValue(BEFORE_UPDATE_ENTITY, beforedelete);;
				break;
			case MERGE:
				if (forInternalUse.getEntity().getOid() == null) {
					//insert
					eh.preprocessInsertDirect(forInternalUse.getEntity(), ec, complexWrapperTypePropList);
					forInternalUse.setMethod(UpdateMethod.INSERT);
				} else {
					//update?
					boolean isPrevExists = eh.preprocessUpdateDirect(forInternalUse.getEntity(), op, ec, complexWrapperTypePropList, false);
					if (isPrevExists) {
						forInternalUse.setMethod(UpdateMethod.UPDATE);
					}
					//insert/update判別できず
				}
				break;
			default:
				throw new IllegalArgumentException();
			}
			return forInternalUse;
		}
		
	}
}
