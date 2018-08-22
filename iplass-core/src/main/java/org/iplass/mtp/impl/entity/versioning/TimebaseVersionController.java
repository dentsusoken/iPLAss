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

package org.iplass.mtp.impl.entity.versioning;

import java.sql.Timestamp;

import org.iplass.mtp.entity.DeleteOption;
import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.UpdateOption;
import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.AsOf.AsOfSpec;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.subquery.ScalarSubQuery;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;


public class TimebaseVersionController extends NumberbaseVersionController {

	public TimebaseVersionController() {
	}

	@Override
	public void normalizeForInsert(Entity entity, EntityContext entityContext) {
		super.normalizeForInsert(entity, entityContext);
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
	}

	@Override
	public DeleteTarget[] getDeleteTarget(Entity entity, DeleteOption option, EntityHandler eh, EntityContext entityContext) {
		return super.getDeleteTarget(entity, option, eh, entityContext);
	}

	@Override
	public Condition refEntityQueryCondition(String refPropPath, ReferencePropertyHandler rph, AsOf asOf, EntityContext context) {
		
		asOf = judgeAsOf(refPropPath, rph, asOf);
		
		//下記のイメージ
		// ([REF_NAME].oid, [REF_NAME].version) in (select oid, max(version) from [REF_ENTITY] where state='V' and s_date <= [TIMESTAMP] and e_date > [TIMESTAMP] group by oid on .this=this or [REF_NAME].oid is null)
		
		Timestamp ts = ExecuteContext.getCurrentContext().getCurrentTimestamp();
		
		switch (asOf.getSpec()) {
		case UPDATE_TIME:
			return null;
		case NOW:
			In in = new In(new String[]{refPropPath + "." + Entity.OID, refPropPath + "." + Entity.VERSION},
					new SubQuery(
							new Query().select(
									new EntityField(Entity.OID),
									new Max(Entity.VERSION))
									.from(rph.getReferenceEntityHandler(context).getMetaData().getName())
									.where(new And(
											new Equals(Entity.STATE, Entity.STATE_VALID_VALUE),
											new LesserEqual(Entity.START_DATE, ts),
											new Greater(Entity.END_DATE, ts)
											))
									.groupBy(Entity.OID))
									.on(refPropPath, SubQuery.THIS));
			//OuterJoinなので、nullは含む
			return new Or(new IsNull(refPropPath + "." + Entity.OID), in);
		case SPEC_VALUE:
			ValueExpression asOfVal = asOf.getValue();
			if (asOfVal != null) {
				asOfVal = (ValueExpression) asOfVal.accept(new PropertyUnnester());
			}
			In in2 = new In(new String[]{refPropPath + "." + Entity.OID, refPropPath + "." + Entity.VERSION},
					new SubQuery(
							new Query().select(
									new EntityField(Entity.OID),
									new Max(Entity.VERSION))
									.from(rph.getReferenceEntityHandler(context).getMetaData().getName())
									.where(new Equals(Entity.STATE, Entity.STATE_VALID_VALUE))
									.groupBy(Entity.OID))
									.on(new And(
											new Equals(new EntityField("." + refPropPath), new EntityField(SubQuery.THIS)),
											new LesserEqual(Entity.START_DATE, asOfVal),
											new Greater(Entity.END_DATE, asOfVal))));
			//OuterJoinなので、nullは含む
			return new Or(new IsNull(refPropPath + "." + Entity.OID), in2);
		default:
			return null;
		}
	}

	@Override
	public Condition mainQueryCondition(EntityHandler eh, AsOf asOf, EntityContext context) {
		//下記のイメージ
		// ([cond]) and version=(select max(version) from [ENTITY] where state='V' and s_date <= [TIMESTAMP] and e_date > [TIMESTAMP] on .this=this)
		if (asOf == null || asOf.getSpec() != AsOfSpec.SPEC_VALUE) {
			Timestamp ts = ExecuteContext.getCurrentContext().getCurrentTimestamp();
			return new Equals(Entity.VERSION,
					new ScalarSubQuery(new Query()
							.select(new Max(Entity.VERSION))
							.from(eh.getMetaData().getName())
							.where(new And(
									new Equals(Entity.STATE, Entity.STATE_VALID_VALUE),
									new LesserEqual(Entity.START_DATE, ts),
									new Greater(Entity.END_DATE, ts)
									)))
							.on(SubQuery.THIS, SubQuery.THIS));
		} else {
			ValueExpression asOfVal = asOf.getValue();
			if (asOfVal != null) {
				asOfVal = (ValueExpression) asOfVal.accept(new PropertyUnnester());
			}
			return new Equals(Entity.VERSION,
					new ScalarSubQuery(new Query()
							.select(new Max(Entity.VERSION))
							.from(eh.getMetaData().getName())
							.where(new Equals(Entity.STATE, Entity.STATE_VALID_VALUE)))
							.on(new And(
									new Equals(new EntityField("." + SubQuery.THIS), new EntityField(SubQuery.THIS)),
									new LesserEqual(Entity.START_DATE, asOfVal),
									new Greater(Entity.END_DATE, asOfVal))));
		}
	}

}
