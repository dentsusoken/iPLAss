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

package org.iplass.mtp.impl.datastore;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.Refer;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.predicate.Contains;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.aggregate.Avg;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.entity.query.value.aggregate.Max;
import org.iplass.mtp.entity.query.value.aggregate.Median;
import org.iplass.mtp.entity.query.value.aggregate.Min;
import org.iplass.mtp.entity.query.value.aggregate.Mode;
import org.iplass.mtp.entity.query.value.aggregate.StdDevPop;
import org.iplass.mtp.entity.query.value.aggregate.StdDevSamp;
import org.iplass.mtp.entity.query.value.aggregate.Sum;
import org.iplass.mtp.entity.query.value.aggregate.VarPop;
import org.iplass.mtp.entity.query.value.aggregate.VarSamp;
import org.iplass.mtp.entity.query.value.controlflow.Case;
import org.iplass.mtp.entity.query.value.expr.MinusSign;
import org.iplass.mtp.entity.query.value.expr.Polynomial;
import org.iplass.mtp.entity.query.value.expr.Term;
import org.iplass.mtp.entity.query.value.primary.ArrayValue;
import org.iplass.mtp.entity.query.value.primary.Cast;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.ParenValue;
import org.iplass.mtp.entity.query.value.subquery.ScalarSubQuery;
import org.iplass.mtp.entity.query.value.window.CumeDist;
import org.iplass.mtp.entity.query.value.window.DenseRank;
import org.iplass.mtp.entity.query.value.window.PercentRank;
import org.iplass.mtp.entity.query.value.window.Rank;
import org.iplass.mtp.entity.query.value.window.RowNumber;
import org.iplass.mtp.entity.query.value.window.WindowAggregate;
import org.iplass.mtp.impl.datastore.grdb.GRdbPropertyStoreRuntime;
import org.iplass.mtp.impl.entity.EntityContext;
import org.iplass.mtp.impl.entity.EntityHandler;
import org.iplass.mtp.impl.entity.property.PropertyHandler;
import org.iplass.mtp.impl.entity.property.ReferencePropertyHandler;
import org.iplass.mtp.impl.rdb.connection.AdditionalWarnLogInfo;

public class EQLAdditionalWarnLogInfo implements AdditionalWarnLogInfo {
	private final Query query;
	private final boolean count;
	
	private EntityHandler eh;
	private EntityContext ec;
	private Boolean noIndex;
	
	public EQLAdditionalWarnLogInfo(Query query, boolean count, EntityHandler eh, EntityContext ec) {
		this.query = query;
		this.count = count;
		this.eh = eh;
		this.ec = ec;
	}
	
	@Override
	public String toString() {
		if (noIndex != null && noIndex) {
			if (count) {
				return "!WITHOUT INDEX QUERY! eql(count)=" + query;
			} else {
				return "!WITHOUT INDEX QUERY! eql=" + query;
			}
		} else {
			if (count) {
				return "eql(count)=" + query;
			} else {
				return "eql=" + query;
			}
		}
	}

	@Override
	public boolean logBefore() {
		if (noIndex == null) {
			noIndex = !new CheckIndex(eh, ec).isIndex(query) || !new SubQueryCheckIndex(ec).isSubqueryAllIndex(query);
		}
		
		return noIndex;
	}
	
	private static class SubQueryCheckIndex extends QueryVisitorSupport {
		private EntityContext ec;
		
		private boolean index = true;
		
		SubQueryCheckIndex(EntityContext ec) {
			this.ec = ec;
		}

		@Override
		public boolean visit(SubQuery subQuery) {
			if (subQuery.getQuery() != null) {
				CheckIndex sub = new CheckIndex(ec.getHandlerByName(subQuery.getQuery().getFrom().getEntityName()), ec);
				boolean subIndex = sub.isIndex(subQuery.getQuery());
				if (!subIndex) {
					if (subQuery.getOn() != null) {
						subIndex = sub.isIndex(subQuery.getOn());
					}
				}
				index &= subIndex;
				if (!index) {
					return false;
				}
			}
			return false;
		}
		
		boolean isSubqueryAllIndex(Query q) {
			q.accept(this);
			return index;
		}
		
	}
	
	private static class CheckIndex extends QueryVisitorSupport {
		private EntityHandler eh;
		private EntityContext ec;
		
		private boolean index;
		private boolean enableRef;
		
		CheckIndex(EntityHandler eh, EntityContext ec) {
			this.eh = eh;
			this.ec = ec;
		}
		
		boolean isIndex(Query q) {
			index = false;
			enableRef = false;
			q.accept(this);
			return index;
		}
		
		boolean isIndex(Condition on) {
			index = false;
			enableRef = true;
			on.accept(this);
			return index;
		}
		
		@Override
		public boolean visit(Query query) {
			if (query.getRefer() != null) {
				for (Refer r: query.getRefer()) {
					r.accept(this);
				}
			}
			if (query.getWhere() != null) {
				query.getWhere().accept(this);
			}
			return false;
		}

		@Override
		public boolean visit(And and) {
			if (and.getChildExpressions() != null) {
				for (Condition c: and.getChildExpressions()) {
					c.accept(this);
					if (index) {
						return false;
					}
				}
			}
			
			return false;
		}

		@Override
		public boolean visit(Like like) {
			if (like.getPattern() == null) {
				return false;
			}
			if (like.getPattern().startsWith(Like.PS)) {
				return false;
			}
			if (like.getPattern().startsWith(Like.US)) {
				return false;
			}
			
			return super.visit(like);
		}

		@Override
		public boolean visit(Or or) {
			if (or.getChildExpressions() != null) {
				for (Condition c: or.getChildExpressions()) {
					index = false;
					c.accept(this);
					if (!index) {
						return false;
					}
				}
			}
			
			return false;
		}

		@Override
		public boolean visit(IsNotNull isNotNull) {
			return false;
		}

		@Override
		public boolean visit(IsNull isNull) {
			return false;
		}

		@Override
		public boolean visit(EntityField entityField) {
			if (entityField.unnestCount() > 0) {
				return false;
			}
			if (enableRef && entityField.getPropertyName().equalsIgnoreCase(SubQuery.THIS)) {
				index = true;
				return false;
			}
			
			PropertyHandler ph = eh.getPropertyCascade(entityField.getPropertyName(), ec);
			if (ph == null) {
				return false;
			}
			if (ph instanceof ReferencePropertyHandler) {
				if (enableRef) {
					index = true;
					return false;
				} else {
					return false;
				}
			}
			
			if (ph.isIndexed()) {
				index = true;
			} else {
				if (ph.getName().equals(Entity.OID)) {
					index = true;
				} else if (ph.getStoreSpecProperty() instanceof GRdbPropertyStoreRuntime) {
					GRdbPropertyStoreRuntime psr = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
					if (psr.isNative() && ph.getParent().getSuperDataModelHandler(ec) != null) {
						//カスタムのnativeカラムの場合は適切にINDEXされているものとする
						index = true;
					}
				}
			}
			
			return false;
		}

		@Override
		public boolean visit(Count count) {
			return false;
		}

		@Override
		public boolean visit(Sum sum) {
			return false;
		}

		@Override
		public boolean visit(Polynomial polynomial) {
			return false;
		}

		@Override
		public boolean visit(Term term) {
			return false;
		}

		@Override
		public boolean visit(ParenValue parenthesizedValue) {
			return super.visit(parenthesizedValue);
		}

		@Override
		public boolean visit(MinusSign minusSign) {
			return false;
		}

		@Override
		public boolean visit(ScalarSubQuery scalarSubQuery) {
			return false;
		}

		@Override
		public boolean visit(SubQuery subQuery) {
			return false;
		}

		@Override
		public boolean visit(Avg avg) {
			return false;
		}

		@Override
		public boolean visit(Max max) {
			return false;
		}

		@Override
		public boolean visit(Min min) {
			return false;
		}

		@Override
		public boolean visit(ArrayValue arrayValue) {
			return false;
		}

		@Override
		public boolean visit(Function function) {
			return false;
		}

		@Override
		public boolean visit(Cast cast) {
			return false;
		}

		@Override
		public boolean visit(Refer refer) {
			if (refer.getCondition() != null) {
				refer.getCondition().accept(this);
			}
			return false;
		}

		@Override
		public boolean visit(Contains contains) {
			return false;
		}

		@Override
		public boolean visit(Case caseClause) {
			return false;
		}

		@Override
		public boolean visit(StdDevPop stdDevPop) {
			return false;
		}

		@Override
		public boolean visit(StdDevSamp stdDevSamp) {
			return false;
		}

		@Override
		public boolean visit(VarPop varPop) {
			return false;
		}

		@Override
		public boolean visit(VarSamp varSamp) {
			return false;
		}

		@Override
		public boolean visit(Mode mode) {
			return false;
		}

		@Override
		public boolean visit(Median median) {
			return false;
		}

		@Override
		public boolean visit(WindowAggregate windowAggregate) {
			return false;
		}

		@Override
		public boolean visit(RowNumber rowNumber) {
			return false;
		}

		@Override
		public boolean visit(Rank rank) {
			return false;
		}

		@Override
		public boolean visit(DenseRank denseRank) {
			return false;
		}

		@Override
		public boolean visit(PercentRank percentRank) {
			return false;
		}

		@Override
		public boolean visit(CumeDist cumeDist) {
			return false;
		}

		@Override
		public boolean visit(RowValueList rowValueList) {
			return false;
		}
	}

}
