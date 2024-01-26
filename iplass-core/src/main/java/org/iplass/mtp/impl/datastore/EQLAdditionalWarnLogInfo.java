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

package org.iplass.mtp.impl.datastore;

import org.iplass.mtp.entity.Entity;
import org.iplass.mtp.entity.definition.PropertyDefinitionType;
import org.iplass.mtp.entity.query.Query;
import org.iplass.mtp.entity.query.QueryVisitorSupport;
import org.iplass.mtp.entity.query.Refer;
import org.iplass.mtp.entity.query.SubQuery;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.predicate.Contains;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.hint.SuppressWarningsHint;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.aggregate.Avg;
import org.iplass.mtp.entity.query.value.aggregate.Count;
import org.iplass.mtp.entity.query.value.aggregate.Listagg;
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

import net.logstash.logback.argument.StructuredArguments;

public class EQLAdditionalWarnLogInfo implements AdditionalWarnLogInfo {
	private static final int NO_INDEX_FLAG = 0b001;
	private static final int WEAK_INDEX_FLAG = 0b010;
	private static final int CORRELATED_SUBQUERY_CONDITION_FLAG = 0b100;
	
	private static final String[] ALERT_MESSAGE = {
		null,
		"!WITHOUT INDEX QUERY!", //001
		"!LOW CARDINALITY INDEX QUERY!", //010
		"!WITHOUT INDEX QUERY!", //011
		"!CORRELATED SUBQUERY IN CONDITION QUERY!", //100
		"!WITHOUT INDEX QUERY! !CORRELATED SUBQUERY IN CONDITION QUERY!", //101
		"!LOW CARDINALITY INDEX QUERY! !CORRELATED SUBQUERY IN CONDITION QUERY!", //110
		"!WITHOUT INDEX QUERY! !CORRELATED SUBQUERY IN CONDITION QUERY!" //111
	};
	
	private static final String EQL_TYPE_EQL = "eql";
	private static final String EQL_TYPE_COUNT = "eql(count)";
	
	private final Query query;
	private final boolean count;
	
	private EntityHandler eh;
	private EntityContext ec;
	
	private boolean checked;
	
	private boolean doLog;
	
	private int alertFlags = 0b000;
	
	public EQLAdditionalWarnLogInfo(Query query, boolean count, EntityHandler eh, EntityContext ec) {
		this.query = query;
		this.count = count;
		this.eh = eh;
		this.ec = ec;
	}
	
	@Override
	public String toString() {
		if (doLog) {
			StringBuilder sb = new StringBuilder();
			String msg = ALERT_MESSAGE[alertFlags];
			if (msg != null) {
				sb.append(msg).append(' ');
			}
			
			if (count) {
				sb.append(EQL_TYPE_COUNT + "=");
			} else {
				sb.append(EQL_TYPE_EQL + "=");
			}
			sb.append(query);
			return sb.toString();
		} else {
			if (count) {
				return EQL_TYPE_COUNT + "=" + query;
			} else {
				return EQL_TYPE_EQL + "=" + query;
			}
		}
	}

	@Override
	public String logFormat() {
		if (doLog) {
			return "{} {}={}";
		} else {
			return "{}={}";
		}
	}

	@Override
	public int parameterSize() {
		if (doLog) {
			return 3;
		} else {
			return 2;
		}
	}

	@Override
	public void setParameter(int offset, Object[] params) {
		if (doLog) {
			params[offset] = StructuredArguments.value("alert_message", ALERT_MESSAGE[alertFlags]);
			if (count) {
				params[offset + 1] = StructuredArguments.value("eql_type", EQL_TYPE_COUNT);
			} else {
				params[offset + 1] = StructuredArguments.value("eql_type", EQL_TYPE_EQL);
			}
			params[offset + 2] = StructuredArguments.value("eql", query.toString());
		} else {
			if (count) {
				params[offset] = StructuredArguments.value("eql_type", EQL_TYPE_COUNT);
			} else {
				params[offset] = StructuredArguments.value("eql_type", EQL_TYPE_EQL);
			}
			params[offset + 1] = StructuredArguments.value("eql", query.toString());
			
		}
	}

	@Override
	public boolean logBefore() {
		if (!checked) {
			CheckWarning checkWarning = new CheckWarning(eh, ec);
			checkWarning.check(query);
			if (checkWarning.suppressWarnings) {
				doLog = false;
			} else {
				SubQueryCheckWarning subQueryCheckWarning = new SubQueryCheckWarning(ec);
				subQueryCheckWarning.check(query);
				if (!checkWarning.index || !subQueryCheckWarning.index) {
					alertFlags |= NO_INDEX_FLAG;
				};
				if (!checkWarning.highCardinality || !subQueryCheckWarning.highCardinality) {
					alertFlags |= WEAK_INDEX_FLAG;
				};
				if (checkWarning.correlatedSubqueryCondition || subQueryCheckWarning.correlatedSubqueryCondition) {
					alertFlags |= CORRELATED_SUBQUERY_CONDITION_FLAG;
				};
				doLog = alertFlags > 0;
			}
			
			checked = true;
		}
		
		return doLog;
	}

	private static class SubQueryCheckWarning extends QueryVisitorSupport {
		private EntityContext ec;
		
		private boolean index = true;
		private boolean highCardinality = true;
		private boolean correlatedSubqueryCondition = false;
		
		SubQueryCheckWarning(EntityContext ec) {
			this.ec = ec;
		}

		@Override
		public boolean visit(SubQuery subQuery) {
			if (subQuery.getQuery() != null) {
				CheckWarning sub = new CheckWarning(ec.getHandlerByName(subQuery.getQuery().getFrom().getEntityName()), ec);
				sub.check(subQuery.getQuery());
				if (subQuery.getOn() != null) {
					sub.checkOn(subQuery.getOn());
				}
				index &= sub.index;
				highCardinality &= sub.highCardinality;
				correlatedSubqueryCondition |= sub.correlatedSubqueryCondition;
				if (!index) {
					return false;
				}
			}
			return false;
		}
		
		void check(Query q) {
			q.accept(this);
		}
		
	}
	
	private static class CheckWarning extends QueryVisitorSupport {
		private EntityHandler eh;
		private EntityContext ec;

		private boolean index;
		private boolean highCardinality;
		private boolean suppressWarnings;
		private boolean correlatedSubqueryCondition;

		private boolean enableRef;

		CheckWarning(EntityHandler eh, EntityContext ec) {
			this.eh = eh;
			this.ec = ec;
		}

		void check(Query q) {
			index = false;
			highCardinality = false;
			suppressWarnings = false;
			enableRef = false;
			q.accept(this);
		}

		void checkOn(Condition on) {
			enableRef = true;
			on.accept(this);
		}

		@Override
		public boolean visit(SuppressWarningsHint suppressWarningsHint) {
			suppressWarnings = true;
			return super.visit(suppressWarningsHint);
		}

		@Override
		public boolean visit(Query query) {
			if (query.getSelect().getHintComment() != null) {
				query.getSelect().getHintComment().accept(this);
				if (suppressWarnings) {
					return false;
				}
			}
			
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
				boolean hc = true;
				for (Condition c: or.getChildExpressions()) {
					index = false;
					highCardinality = false;
					c.accept(this);
					if (!index) {
						return false;
					}
					hc &= highCardinality;
				}
				highCardinality = hc;
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
				highCardinality = true;
				return false;
			}
			
			PropertyHandler ph = eh.getPropertyCascade(entityField.getPropertyName(), ec);
			if (ph == null) {
				return false;
			}
			if (ph instanceof ReferencePropertyHandler) {
				if (enableRef) {
					index = true;
					highCardinality = true;
					return false;
				} else {
					return false;
				}
			}
			
			if (ph.isIndexed()) {
				index = true;
				if (ph.getEnumType() != PropertyDefinitionType.BOOLEAN
						&&  ph.getEnumType() != PropertyDefinitionType.SELECT) {
					highCardinality = true;
				}
			} else {
				if (ph.getName().equals(Entity.OID)
						|| ph.getName().equals(Entity.CREATE_BY)) {
					index = true;
					highCardinality = true;
				} else if (ph.getStoreSpecProperty() instanceof GRdbPropertyStoreRuntime) {
					GRdbPropertyStoreRuntime psr = (GRdbPropertyStoreRuntime) ph.getStoreSpecProperty();
					if (psr.isNative() && ph.getParent().getSuperDataModelHandler(ec) != null) {
						//カスタムのnativeカラムの場合は適切にINDEXされているものとする
						index = true;
						highCardinality = true;
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
			correlatedSubqueryCondition = true;
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
		public boolean visit(Listagg listagg) {
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
