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

import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.EntityRuntimeException;
import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformerSupport;
import org.iplass.mtp.entity.query.AsOf;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.Refer;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.Where;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Paren;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;


public class VersionedQueryNormalizer extends ASTTransformerSupport {
	
	private class VersionInfo {
		
		ReferencePropertyHandler rph;
		boolean isVersionSpecify;
		AsOf asOf;
		
		VersionInfo(ReferencePropertyHandler rph) {
			this.rph =rph;
			isVersionSpecify = false;
		}
		
	}
	
	//Oracleでは結合条件でサブクエリ使えないので、抽出条件にくっつける形で。。。

	private VersionController vc;
	private EntityHandler eh;
	private EntityContext context;
	private boolean isVersionSpecify;
	private boolean isConditonNode;
	private Map<String, VersionInfo> refs;

	private VersionedQueryNormalizer parent;
	
	public VersionedQueryNormalizer(VersionController vc, EntityHandler eh, EntityContext context, VersionedQueryNormalizer parent) {
		this.vc = vc;
		this.eh = eh;
		this.context = context;
		isVersionSpecify = false;
		isConditonNode = false;
		refs = new HashMap<String, VersionInfo>();
		this.parent = parent;
	}
	
	@Override
	public ASTNode visit(Where where) {
		isConditonNode = true;
		ASTNode node = super.visit(where);
		isConditonNode = false;
		return node;
	}
	
	@Override
	public ASTNode visit(EntityField entityField) {
		
		String propName = entityField.getPropertyName();
		VersionedQueryNormalizer target = this;
		int unnestCount = entityField.unnestCount();
		if (unnestCount > 0) {
			propName = propName.substring(unnestCount);
			for (int i = 0; i < unnestCount; i++) {
				target = target.parent;
				if (target == null) {
					throw new QueryException("can't correlate outer query on :" + entityField);
				}
			}
		}
		
		if (propName.contains(".")) {
			String[] propPath = propName.split("[.]");
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < propPath.length - 1; i++) {
				if (i != 0) {
					sb.append('.');
				}
				sb.append(propPath[i]);
				String refPropName = sb.toString();
				VersionInfo vi = target.getVi(refPropName);
				if (i == propPath.length - 2) {
					if (target.isConditonNode && isVersionProperty(propPath[propPath.length - 1])) {
						vi.isVersionSpecify  = true;
					}
				}
			}
		} else {
			if (target.isConditonNode && isVersionProperty(propName)) {
				target.isVersionSpecify = true;
			}
		}
		return super.visit(entityField);
	}

	private VersionInfo getVi(String refPropName) {
		VersionInfo vi = refs.get(refPropName);
		if (vi == null) {
			PropertyHandler ph = eh.getPropertyCascade(refPropName, context);
			if (ph == null || !(ph instanceof ReferencePropertyHandler)) {
				throw new EntityRuntimeException(eh.getMetaData().getName() + "." + refPropName + " is not ReferenceProperty.");
			}
			vi = new VersionInfo((ReferencePropertyHandler) ph);
			refs.put(refPropName, vi);
		}
		return vi;
	}
	
	public static boolean isVersionProperty(String propName) {
		if (propName.equals(Entity.VERSION)
				|| propName.equals(Entity.STATE)
				|| propName.equals(Entity.START_DATE)
				|| propName.equals(Entity.END_DATE)
				) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ASTNode visit(Query query) {
		
		Query q = (Query) super.visit(query);
		
		And and = new And();
		if (q.getWhere() != null && q.getWhere().getCondition() != null) {
			and.addExpression(new Paren(q.getWhere().getCondition()));
		}
		if (!isVersionSpecify && !q.isVersiond()) {
			and.addExpression(vc.mainQueryCondition(eh, q.getFrom().getAsOf(), context));
		}
		if (refs.size() != 0) {
			for (Map.Entry<String, VersionInfo> e: refs.entrySet()) {
				if (!e.getValue().isVersionSpecify) {
					VersionController refVc = eh.getService().getVersionController(e.getValue().rph.getReferenceEntityHandler(context));
					Condition refEntityCond = refVc.refEntityQueryCondition(e.getKey(), e.getValue().rph, e.getValue().asOf, context);
					if (refEntityCond != null) {
						Refer r = q.refer(e.getKey());
						if (r.getCondition() == null) {
							r.setCondition(refEntityCond);
						} else {
							r.setCondition(new And(r.getCondition(), refEntityCond));
						}
					}
				}
			}
		}
		
		if (and.getChildExpressions() != null) {
			if (and.getChildExpressions().size() > 1) {
				q.where(and);
			} else if (and.getChildExpressions().size() > 0) {
				q.where(and.getChildExpressions().get(0));
			}
		}
		
		q.setVersiond(true);
		return q;
	}
	
	@Override
	public ASTNode visit(SubQuery subQuery) {
		
		Query query = null;
		Condition on = null;
		if (subQuery.getQuery() != null) {
			EntityHandler subEh = context.getHandlerByName(subQuery.getQuery().getFrom().getEntityName());
			VersionedQueryNormalizer subNormalizer = new VersionedQueryNormalizer(
					subEh.getService().getVersionController(subEh), subEh, context, this);
			
			//Onから先。生成されるQueryに反映されるように
			if (subQuery.getOn() != null) {
				on = (Condition) subQuery.getOn().accept(subNormalizer);
			}
			
			query = (Query) subQuery.getQuery().accept(subNormalizer);

		}
		
		return new SubQuery(query, on);
	}
	
	@Override
	public ASTNode visit(Refer refer) {
		
		if (refer.getAsOf() != null) {
			VersionInfo vi = getVi(refer.getReferenceName().getPropertyName());
			vi.asOf = refer.getAsOf();
		}
		
		return super.visit(refer);
	}

	
}
