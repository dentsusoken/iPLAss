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
package org.iplass.mtp.impl.entity.l10n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.iplass.mtp.entity.query.ASTNode;
import org.iplass.mtp.entity.query.ASTTransformerSupport;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryException;
import org.iplass.mtp.entity.query.Refer;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.impl.core.ExecuteContext;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PrimitivePropertyHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;

class QueryLocalizer extends ASTTransformerSupport {
	
	private QueryLocalizer parent;
	private EntityHandler eh;
	private EntityContext ec;
	private Map<String, EntityHandler> refers;
	private String userLang;
	private String userLangReplace;
	
	QueryLocalizer(EntityHandler eh, EntityContext ec) {
		this.eh = eh;
		this.ec = ec;
		userLang = ExecuteContext.getCurrentContext().getLanguage();
	}

	@Override
	public ASTNode visit(EntityField entityField) {
		int unnestCount = entityField.unnestCount();
		QueryLocalizer target = this;
		for (int i = 0; i < unnestCount; i++) {
			target = target.parent;
			if (target == null) {
				throw new QueryException(entityField + " specify outer query's field, but has no outer query.");
			}
		}
		String propName = entityField.getPropertyName();
		if (unnestCount > 0) {
			propName = propName.substring(unnestCount);
		}
		
		String l10nPropName = null;
		int lastDot = propName.lastIndexOf('.');
		if (lastDot < 0) {
			//直下のプロパティ
			PropertyHandler ph = target.eh.getProperty(propName, ec);
			if (ph != null) {
				if (ph instanceof PrimitivePropertyHandler) {
					if (target.eh.getMetaData().getDataLocalizationStrategy() != null
							&& target.eh.getMetaData().getDataLocalizationStrategy() instanceof MetaEachPropertyDataLocalizationStrategy) {
						l10nPropName = propName + "_" + userLangReplace();
						PropertyHandler l10nPh = target.eh.getProperty(l10nPropName, ec);
						if (l10nPh == null) {
							l10nPropName = null;
						}
					}
				} else {
					//Reference
					EntityHandler refEh = ((ReferencePropertyHandler) ph).getReferenceEntityHandler(ec);
					target.addRefers(propName, refEh);
				}
			}
		} else {
			//参照先のプロパティ
			ReferencePropertyHandler ref = (ReferencePropertyHandler) target.eh.getPropertyCascade(propName.substring(0, lastDot), ec);
			if (ref == null) {
				throw new QueryException(entityField + " not defined.");
			}
			EntityHandler targetEh = ref.getReferenceEntityHandler(ec);
			String propNameDirect = propName.substring(lastDot + 1, propName.length());
			PropertyHandler ph = targetEh.getProperty(propNameDirect, ec);
			
			if (ph != null) {
				if (ph instanceof PrimitivePropertyHandler) {
					if (targetEh.getMetaData().getDataLocalizationStrategy() != null
							&& targetEh.getMetaData().getDataLocalizationStrategy() instanceof MetaEachPropertyDataLocalizationStrategy) {
						l10nPropName = propName + "_" + userLangReplace();
						String l10nPropNameDirect = propNameDirect + "_" + userLangReplace();
						PropertyHandler l10nPh = targetEh.getProperty(l10nPropNameDirect, ec);
						if (l10nPh == null) {
							l10nPropName = null;
						}
					}
				} else {
					//Reference
					EntityHandler refEh = ((ReferencePropertyHandler) ph).getReferenceEntityHandler(ec);
					target.addRefers(propName, refEh);
				}
			}
			
			//参照階層のチェック
			String refPath = propName;
			for (int index = lastDot; index > 0; index = refPath.lastIndexOf('.')) {
				refPath = refPath.substring(0, index);
				ReferencePropertyHandler refPh = (ReferencePropertyHandler) target.eh.getPropertyCascade(refPath, ec);
				EntityHandler refEh = refPh.getReferenceEntityHandler(ec);
				target.addRefers(refPath, refEh);
			}
		}
		
		if (l10nPropName == null) {
			return new EntityField(entityField.getPropertyName());
		} else {
			if (unnestCount > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < unnestCount; i++) {
					sb.append('.');
				}
				sb.append(l10nPropName);
				return new EntityField(sb.toString());
			} else {
				return new EntityField(l10nPropName);
			}
		}
	}
	
	private void addRefers(String refName, EntityHandler refEh) {
		if (refEh.getMetaData().getDataLocalizationStrategy() instanceof MetaEachInstanceDataLocalizationStrategy) {
			if (refers == null) {
				refers = new HashMap<>();
			}
			if (!refers.containsKey(refName)) {
				refers.put(refName, refEh);
			}
		}
	}

	private String userLangReplace() {
		if (userLangReplace == null) {
			userLangReplace = userLang.replace('-', '_');
		}
		return userLangReplace;
	}

	@Override
	public ASTNode visit(SubQuery subQuery) {
		EntityHandler subEh = ec.getHandlerByName(subQuery.getQuery().getFrom().getEntityName());
		QueryLocalizer subQueryLocalizer = new QueryLocalizer(subEh, ec);
		subQueryLocalizer.parent = this;
		
		Query subQ = (Query) subQuery.getQuery().accept(subQueryLocalizer);
		Condition on = null;
		if (subQuery.getOn() != null) {
			on = (Condition) subQuery.getOn().accept(subQueryLocalizer);
		}
		return new SubQuery(subQ, on);
	}

	@Override
	public ASTNode visit(Query query) {
		
		Query q = (Query) super.visit(query);
		
		if (eh.getMetaData().getDataLocalizationStrategy() != null &&
				eh.getMetaData().getDataLocalizationStrategy() instanceof MetaEachInstanceDataLocalizationStrategy) {
			MetaEachInstanceDataLocalizationStrategy st = (MetaEachInstanceDataLocalizationStrategy) eh.getMetaData().getDataLocalizationStrategy();
			Condition l10nCond = l10nCond(userLang, st, null);
			if (q.getWhere() == null || q.getWhere().getCondition() == null) {
				//gemの汎用操作画面経由だと、Where内のconditionがnullになることがある
				q.where(l10nCond);
			} else {
				q.where(new And(q.getWhere().getCondition(), l10nCond));
			}
		}
		
		//参照
		if (q.getRefer() != null && q.getRefer().size() > 0) {
			for (Refer r: q.getRefer()) {
				EntityHandler refEh = (refers == null) ? null: refers.remove(r.getReferenceName().getPropertyName());
				if (refEh != null) {
					MetaEachInstanceDataLocalizationStrategy st = (MetaEachInstanceDataLocalizationStrategy) refEh.getMetaData().getDataLocalizationStrategy();
					Condition l10nCond = l10nCond(userLang, st, r.getReferenceName().getPropertyName());
					if (r.getCondition() == null) {
						r.setCondition(l10nCond);
					} else {
						r.setCondition(new And(r.getCondition(), l10nCond));
					}
				}
			}
		}
		if (refers != null && refers.size() > 0) {
			if (q.getRefer() == null) {
				q.setRefer(new ArrayList<>());
			}
			for (Map.Entry<String, EntityHandler> e: refers.entrySet()) {
				MetaEachInstanceDataLocalizationStrategy st = (MetaEachInstanceDataLocalizationStrategy) e.getValue().getMetaData().getDataLocalizationStrategy();
				Condition l10nCond = l10nCond(userLang, st, e.getKey());
				q.getRefer().add(new Refer(new EntityField(e.getKey()), l10nCond));
			}
		}
		
		return q;
	}
	
	private Condition l10nCond(String userLang, MetaEachInstanceDataLocalizationStrategy st, String refPath) {
		Condition l10nCond = null;
		if (st.getLanguages() != null &&
				st.getLanguages().contains(userLang)) {
			if (refPath == null) {
				l10nCond = new Equals(st.getLanguagePropertyName(), userLang);
			} else {
				l10nCond = new Equals(refPath + "." + st.getLanguagePropertyName(), userLang);
			}
		} else {
			if (refPath == null) {
				l10nCond = new IsNull(st.getLanguagePropertyName());
			} else {
				l10nCond = new IsNull(refPath + "." + st.getLanguagePropertyName());
			}
		}
		
		return l10nCond;
	}
	
}
