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
package org.iplass.mtp.impl.entity.cache;

import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.query.From;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.MetaPrimitiveProperty;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyType;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.properties.extend.ExpressionType;

class DefNameCollector extends QueryVisitorSupport {
	
	private EntityContext ec;
	private EntityHandler eh;
	private List<String> defNames = new ArrayList<>();
	
	private DefNameCollector parent;
	
	DefNameCollector(EntityContext ec) {
		this.ec = ec;
	}
	
	public String[] getDefNames() {
		return defNames.toArray(new String[defNames.size()]);
	}
	

	@Override
	public boolean visit(Query query) {
		
		From from = query.getFrom();
		if (from == null || from.getEntityName() == null) {
			return false;
		}
		eh = ec.getHandlerByName(from.getEntityName());
		if (eh == null) {
			return false;
		}
		if (!defNames.contains(from.getEntityName())) {
			defNames.add(from.getEntityName());
		}
		
		return super.visit(query);
	}

	@Override
	public boolean visit(EntityField entityField) {
		
		if (entityField.getPropertyName() != null) {
			DefNameCollector target = this;
			String targetPropName = entityField.getPropertyName();
			//correlation subqueryかチェック
			int i;
			for (i = 0; targetPropName.charAt(i) == '.'; i++) {
				if (parent == null) {
					//親クエリがない場合は検査中断
					return false;
				}
				target = parent;
			}
			if (i > 0) {
				targetPropName = entityField.getPropertyName().substring(i);
			}
			
			int dotIndex = targetPropName.lastIndexOf('.');
			if (dotIndex > 0) {
				//referenced entity's property
				ReferencePropertyHandler ph = (ReferencePropertyHandler) target.eh.getPropertyCascade(targetPropName.substring(0, dotIndex), ec);
				EntityHandler refEH = ph.getReferenceEntityHandler(ec);
				if (!defNames.contains(refEH.getMetaData().getName())) {
					defNames.add(refEH.getMetaData().getName());
				}
			}
			
			PropertyHandler ph = target.eh.getPropertyCascade(targetPropName, ec);
			if (ph instanceof ReferencePropertyHandler) {
				//参照
				EntityHandler refEH = ((ReferencePropertyHandler) ph).getReferenceEntityHandler(ec);
				if (!defNames.contains(refEH.getMetaData().getName())) {
					defNames.add(refEH.getMetaData().getName());
				}
			} else if (ph instanceof PrimitivePropertyHandler) {
				//Primitive
				PropertyType pt = ((MetaPrimitiveProperty) ph.getMetaData()).getType();
				if (pt instanceof ExpressionType) {
					//FIXME Expressionの変換タイミングを、現状のEntityHandler内よりもっと前に
					
					//Expressionの場合、変換後のExpressionをチェック
					ValueExpression exp = ((ExpressionType) pt).translate(new EntityField(targetPropName));
					exp.accept(this);
				}
			}
		}
		return false;
	}

	@Override
	public boolean visit(SubQuery subQuery) {
		DefNameCollector sub = new DefNameCollector(ec);
		sub.parent = this;
		
		Query subq = subQuery.getQuery();
		subq.accept(sub);
		if (subQuery.getOn() != null) {
			subQuery.getOn().accept(sub);
		}
		
		for (String subDefName: sub.defNames) {
			if (!defNames.contains(subDefName)) {
				defNames.add(subDefName);
			}
		}
		
		return false;
	}

}
