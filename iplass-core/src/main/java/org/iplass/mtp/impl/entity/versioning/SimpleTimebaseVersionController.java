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

import static org.iplass.mtp.impl.util.CoreResourceBundleUtil.resourceString;

import java.sql.Timestamp;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityApplicationException;
import org.iplass.mtp.entity.InsertOption;
import org.iplass.mtp.entity.TargetVersion;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.AsOf.AsOfSpec;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.hint.SuppressWarningsHint;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.subquery.ScalarSubQuery;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.datastore.strategy.SearchResultIterator;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;


public class SimpleTimebaseVersionController extends NumberbaseVersionController {

	public SimpleTimebaseVersionController() {
	}

	@Override
	public void normalizeForInsert(Entity entity, InsertOption option, EntityContext entityContext) {
		super.normalizeForInsert(entity, option, entityContext);
		if (entity.getStartDate() == null) {
			entity.setStartDate(ExecuteContext.getCurrentContext().getCurrentTimestamp());
		}
		if (entity.getEndDate() == null) {
			entity.setEndDate((Timestamp) ExecuteContext.getCurrentContext().getDefaultEndDate());
		}
	}

	@Override
	public void update(Entity entity, UpdateOption option, EntityHandler eh, EntityContext entityContext) {
		if (entity.getStartDate() == null) {
			entity.setStartDate(ExecuteContext.getCurrentContext().getCurrentTimestamp());
		}
		if (entity.getEndDate() == null) {
			entity.setEndDate((Timestamp) ExecuteContext.getCurrentContext().getDefaultEndDate());
		}
		super.update(entity, option, eh, entityContext);
		
		//重複期間がないかどうかチェック
		if (option.getTargetVersion() == TargetVersion.NEW ||
				(option.getUpdateProperties() != null &&
				(option.getUpdateProperties().contains(Entity.START_DATE)
						|| option.getUpdateProperties().contains(Entity.END_DATE)
						|| option.getUpdateProperties().contains(Entity.STATE)))) {
			Query q = new Query().select(new Count())
					.hint(new SuppressWarningsHint())
					.from(eh.getMetaData().getName())
					.where(new And(
							new Equals(Entity.OID, entity.getOid()),
							new Equals(Entity.STATE, Entity.STATE_VALID_VALUE),
							new Lesser(Entity.START_DATE, new ScalarSubQuery(
									new Query().select(new EntityField(Entity.END_DATE))
									.from(eh.getMetaData().getName())
									.where(new And(new Equals(Entity.OID, entity.getOid()),
											new Equals(Entity.VERSION, entity.getVersion()),
											new Equals(Entity.STATE, Entity.STATE_VALID_VALUE)))
									.versioned())),
							new Greater(Entity.END_DATE, new ScalarSubQuery(
									new Query().select(new EntityField(Entity.START_DATE))
									.from(eh.getMetaData().getName())
									.where(new And(new Equals(Entity.OID, entity.getOid()),
											new Equals(Entity.VERSION, entity.getVersion()),
											new Equals(Entity.STATE, Entity.STATE_VALID_VALUE)))
									.versioned()))))
					.versioned();
			SearchResultIterator it = eh.getStrategy().search(entityContext, q, eh);
			Long[] res = new Long[1];
			try {
				if (it.next()) {
					res[0] = (Long) it.getValue(0);
				}
			} finally {
				it.close();
			}
			if (res[0].longValue() > 1) {
				throw new EntityApplicationException(resourceString("impl.core.versioning.SimpleTimebaseVersionController.overlappingPeriods", eh.getLocalizedDisplayName()));
			}
			
		}
	}

	@Override
	public Condition refEntityQueryCondition(String refPropPath, ReferencePropertyHandler rph, AsOf asOf, EntityContext context) {
		
		asOf = judgeAsOf(refPropPath, rph, asOf);
		
		Timestamp ts = ExecuteContext.getCurrentContext().getCurrentTimestamp();
		
		switch (asOf.getSpec()) {
		case UPDATE_TIME:
			return null;
		case NOW:
			return new And(
					new Equals(refPropPath + "." + Entity.STATE, Entity.STATE_VALID_VALUE),
					new LesserEqual(refPropPath + "." + Entity.START_DATE, ts),
					new Greater(refPropPath + "." + Entity.END_DATE, ts)
					);
		case SPEC_VALUE:
			ValueExpression asOfVal = asOf.getValue();
			return new And(
					new Equals(refPropPath + "." + Entity.STATE, Entity.STATE_VALID_VALUE),
					new LesserEqual(refPropPath + "." + Entity.START_DATE, asOfVal),
					new Greater(refPropPath + "." + Entity.END_DATE, asOfVal)
					);
		default:
			return null;
		}
	}

	@Override
	public Condition mainQueryCondition(EntityHandler eh, AsOf asOf, EntityContext context) {
		if (asOf == null || asOf.getSpec() != AsOfSpec.SPEC_VALUE) {
			Timestamp ts = ExecuteContext.getCurrentContext().getCurrentTimestamp();
			return new And(
					new Equals(Entity.STATE, Entity.STATE_VALID_VALUE),
					new LesserEqual(Entity.START_DATE, ts),
					new Greater(Entity.END_DATE, ts)
					);
		} else {
			ValueExpression asOfVal = asOf.getValue();
			return new And(
					new Equals(Entity.STATE, Entity.STATE_VALID_VALUE),
					new LesserEqual(Entity.START_DATE, asOfVal),
					new Greater(Entity.END_DATE, asOfVal)
					);
		}
	}

}
