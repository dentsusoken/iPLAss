/*
 * Copyright (C) 2021 INFORMATION SERVICES INTERNATIONAL - DENTSU, LTD. All Rights Reserved.
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

package org.iplass.mtp.impl.entity.versioning;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.UpdateCondition;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.AsOf.AsOfSpec;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.interceptor.EntityUpdateAllInvocationImpl;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

public class StatebaseVersionController extends NumberbaseVersionController {

	@Override
	public void update(Entity entity, UpdateOption option, EntityHandler eh, EntityContext entityContext) {
		
		boolean needStateUpdate = needStateUpdate(entity, option, eh, entityContext);
		
		super.update(entity, option, eh, entityContext);
		
		if (needStateUpdate) {
			//他のversionのstateをinvalidに
			UpdateCondition uc = new UpdateCondition(eh.getMetaData().getName())
					.value(Entity.STATE, new SelectValue(Entity.STATE_INVALID_VALUE))
					.where(new And()
							.and(new Equals(Entity.OID, entity.getOid()))
							.and(new NotEquals(Entity.VERSION, entity.getVersion()))
							.and(new Equals(Entity.STATE, new SelectValue(Entity.STATE_VALID_VALUE))));
			
			new EntityUpdateAllInvocationImpl(uc, eh.getService().getInterceptors(), eh).proceed();
		}
	}
	
	private boolean needStateUpdate(Entity entity, UpdateOption option, EntityHandler eh, EntityContext entityContext) {
		if (option.getTargetVersion() == TargetVersion.NEW) {
			return true;
		}
		
		return option.getUpdateProperties() != null
				&& option.getUpdateProperties().contains(Entity.STATE)
				&& entity.getState() != null
				&& Entity.STATE_VALID_VALUE.equals(entity.getState().getValue());
	}

	@Override
	public Condition refEntityQueryCondition(String refPropPath, ReferencePropertyHandler rph, AsOf asOf, EntityContext context) {
		
		asOf = judgeAsOf(refPropPath, rph, asOf);
		
		switch (asOf.getSpec()) {
		case UPDATE_TIME:
			return null;
		case NOW:
			return new Equals(refPropPath + "." + Entity.STATE, Entity.STATE_VALID_VALUE);
		case SPEC_VALUE:
			ValueExpression asOfVal = asOf.getValue();
			return new Equals(refPropPath + "." + Entity.VERSION, asOfVal);
		default:
			return null;
		}
	}

	@Override
	public Condition mainQueryCondition(EntityHandler eh, AsOf asOf, EntityContext context) {
		if (asOf == null || asOf.getSpec() != AsOfSpec.SPEC_VALUE) {
			return new Equals(Entity.STATE, Entity.STATE_VALID_VALUE);
		} else {
			ValueExpression asOfVal = asOf.getValue();
			return new Equals(Entity.VERSION, asOfVal);
		}
	}

}
