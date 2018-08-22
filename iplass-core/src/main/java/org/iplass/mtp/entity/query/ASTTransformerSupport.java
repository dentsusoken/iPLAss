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

package org.iplass.mtp.entity.query;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.iplass.mtp.entity.SelectValue;
import org.iplass.mtp.entity.query.condition.Condition;
import org.iplass.mtp.entity.query.condition.expr.And;
import org.iplass.mtp.entity.query.condition.expr.Not;
import org.iplass.mtp.entity.query.condition.expr.Or;
import org.iplass.mtp.entity.query.condition.expr.Paren;
import org.iplass.mtp.entity.query.condition.predicate.Between;
import org.iplass.mtp.entity.query.condition.predicate.Contains;
import org.iplass.mtp.entity.query.condition.predicate.Equals;
import org.iplass.mtp.entity.query.condition.predicate.Greater;
import org.iplass.mtp.entity.query.condition.predicate.GreaterEqual;
import org.iplass.mtp.entity.query.condition.predicate.In;
import org.iplass.mtp.entity.query.condition.predicate.IsNotNull;
import org.iplass.mtp.entity.query.condition.predicate.IsNull;
import org.iplass.mtp.entity.query.condition.predicate.Lesser;
import org.iplass.mtp.entity.query.condition.predicate.LesserEqual;
import org.iplass.mtp.entity.query.condition.predicate.Like;
import org.iplass.mtp.entity.query.condition.predicate.NotEquals;
import org.iplass.mtp.entity.query.hint.BindHint;
import org.iplass.mtp.entity.query.hint.CacheHint;
import org.iplass.mtp.entity.query.hint.FetchSizeHint;
import org.iplass.mtp.entity.query.hint.Hint;
import org.iplass.mtp.entity.query.hint.HintComment;
import org.iplass.mtp.entity.query.hint.IndexHint;
import org.iplass.mtp.entity.query.hint.NativeHint;
import org.iplass.mtp.entity.query.hint.NoBindHint;
import org.iplass.mtp.entity.query.hint.NoIndexHint;
import org.iplass.mtp.entity.query.hint.TimeoutHint;
import org.iplass.mtp.entity.query.value.RowValueList;
import org.iplass.mtp.entity.query.value.ValueExpression;
import org.iplass.mtp.entity.query.value.aggregate.Aggregate;
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
import org.iplass.mtp.entity.query.value.controlflow.Else;
import org.iplass.mtp.entity.query.value.controlflow.When;
import org.iplass.mtp.entity.query.value.expr.MinusSign;
import org.iplass.mtp.entity.query.value.expr.Polynomial;
import org.iplass.mtp.entity.query.value.expr.Term;
import org.iplass.mtp.entity.query.value.primary.ArrayValue;
import org.iplass.mtp.entity.query.value.primary.Cast;
import org.iplass.mtp.entity.query.value.primary.EntityField;
import org.iplass.mtp.entity.query.value.primary.Function;
import org.iplass.mtp.entity.query.value.primary.Literal;
import org.iplass.mtp.entity.query.value.primary.ParenValue;
import org.iplass.mtp.entity.query.value.subquery.ScalarSubQuery;
import org.iplass.mtp.entity.query.value.window.CumeDist;
import org.iplass.mtp.entity.query.value.window.DenseRank;
import org.iplass.mtp.entity.query.value.window.PartitionBy;
import org.iplass.mtp.entity.query.value.window.PercentRank;
import org.iplass.mtp.entity.query.value.window.Rank;
import org.iplass.mtp.entity.query.value.window.RowNumber;
import org.iplass.mtp.entity.query.value.window.WindowAggregate;
import org.iplass.mtp.entity.query.value.window.WindowFunction;
import org.iplass.mtp.entity.query.value.window.WindowOrderBy;
import org.iplass.mtp.entity.query.value.window.WindowSortSpec;


public abstract class ASTTransformerSupport implements ASTTransformer {
	final static ASTTransformer copier = new ASTTransformerSupport() {
	};

	public ASTNode visit(ASTNode node) {
		throw new UnsupportedOperationException("unknown node type:" + node);
	}

	public ASTNode visit(Literal literal) {
		Object value = literal.getValue();
		if (value instanceof SelectValue) {
			SelectValue sv = (SelectValue) value;
			return new Literal(new SelectValue(sv.getValue(), sv.getDisplayName()), literal.isBindable());
		}
		if (value instanceof Timestamp) {
			return new Literal(new Timestamp(((Timestamp) value).getTime()), literal.isBindable());
		}
		if (value instanceof Date) {
			return new Literal(new Date(((Date) value).getTime()), literal.isBindable());
		}
		if (value instanceof Time) {
			return new Literal(new Time(((Time) value).getTime()), literal.isBindable());
		}
		
		//immutableと判断
		return new Literal(value, literal.isBindable());
	}

	public ASTNode visit(EntityField entityField) {
		return new EntityField(entityField.getPropertyName());
	}

	public ASTNode visit(ParenValue parenthesizedValue) {
		if (parenthesizedValue.getNestedValue() != null) {
			return new ParenValue((ValueExpression) parenthesizedValue.getNestedValue().accept(this));
		} else {
			return new ParenValue();
		}
	}

	public ASTNode visit(Avg avg) {
		if (avg.getValue() != null) {
			return new Avg((ValueExpression) avg.getValue().accept(this));
		} else {
			return new Avg();
		}
	}

	public ASTNode visit(Count count) {
		if (count.getValue() != null) {
			return new Count((ValueExpression) count.getValue().accept(this), count.isDistinct());
		} else {
			return new Count();
		}
	}

	public ASTNode visit(Max max) {
		if (max.getValue() != null) {
			return new Max((ValueExpression) max.getValue().accept(this));
		} else {
			return new Max();
		}
	}

	public ASTNode visit(Min min) {
		if (min.getValue() != null) {
			return new Min((ValueExpression) min.getValue().accept(this));
		} else {
			return new Min();
		}
	}

	public ASTNode visit(Sum sum) {
		if (sum.getValue() != null) {
			return new Sum((ValueExpression) sum.getValue().accept(this));
		} else {
			return new Sum();
		}
	}

	public ASTNode visit(MinusSign minusSign) {
		if (minusSign.getValue() != null) {
			return new MinusSign((ValueExpression) minusSign.getValue().accept(this));
		} else {
			return new MinusSign();
		}
	}

	public ASTNode visit(Polynomial polynomial) {
		List<ValueExpression> addValues = null;
		List<ValueExpression> subValues = null;
		if (polynomial.getAddValues() != null) {
			addValues = new ArrayList<ValueExpression>();
			for (ValueExpression v: polynomial.getAddValues()) {
				addValues.add((ValueExpression) v.accept(this));
			}
		}
		if (polynomial.getSubValues() != null) {
			subValues = new ArrayList<ValueExpression>();
			for (ValueExpression v: polynomial.getSubValues()) {
				subValues.add((ValueExpression) v.accept(this));
			}
		}

		return new Polynomial(addValues, subValues);
	}

	public ASTNode visit(Term term) {
		List<ValueExpression> mulValues = null;
		List<ValueExpression> divValues = null;
		if (term.getMulValues() != null) {
			mulValues = new ArrayList<ValueExpression>();
			for (ValueExpression v: term.getMulValues()) {
				mulValues.add((ValueExpression) v.accept(this));
			}
		}
		if (term.getDivValues() != null) {
			divValues = new ArrayList<ValueExpression>();
			for (ValueExpression v: term.getDivValues()) {
				divValues.add((ValueExpression) v.accept(this));
			}
		}

		return new Term(mulValues, divValues);
	}

	public ASTNode visit(ScalarSubQuery scalarSubQuery) {
		SubQuery subQuery = null;
		if (scalarSubQuery.getSubQuery() != null) {
			subQuery = (SubQuery) scalarSubQuery.getSubQuery().accept(this);
		}
		return new ScalarSubQuery(subQuery);
	}

	@Override
	public ASTNode visit(SubQuery subQuery) {
		Query query = null;
		Condition on = null;
		if (subQuery.getQuery() != null) {
			query = (Query) subQuery.getQuery().accept(this);
		}
		if (subQuery.getOn() != null) {
			on = (Condition) subQuery.getOn().accept(this);
		}
		return new SubQuery(query, on);
	}

	public ASTNode visit(From from) {
		From copy = new From(from.getEntityName());
		if (from.getAsOf() != null) {
			copy.setAsOf((AsOf) from.getAsOf().accept(this));
		}
		return copy;
	}

	public ASTNode visit(SortSpec order) {
		ValueExpression ve = null;
		if (order.getSortKey() != null) {
			ve = (ValueExpression) order.getSortKey().accept(this);
		}
		return new SortSpec(ve, order.getType(), order.getNullOrderingSpec());
	}

	public ASTNode visit(Query query) {

		Query q = new Query();

		if (query.getSelect() != null) {
			q.setSelect((Select) query.getSelect().accept(this));
		}
		if (query.getFrom() != null) {
			q.setFrom((From) query.getFrom().accept(this));
		}
		if (query.getRefer() != null) {
			ArrayList<Refer> refer = new ArrayList<Refer>();
			for (Refer r: query.getRefer()) {
				refer.add((Refer) r.accept(this));
			}
			q.setRefer(refer);
		}
		if (query.getWhere() != null) {
			q.setWhere((Where) query.getWhere().accept(this));
		}
		if (query.getGroupBy() != null) {
			q.setGroupBy((GroupBy) query.getGroupBy().accept(this));
		}
		if (query.getHaving() != null) {
			q.setHaving((Having) query.getHaving().accept(this));
		}
		if (query.getOrderBy() != null) {
			q.setOrderBy((OrderBy) query.getOrderBy().accept(this));
		}
		if (query.getLimit() != null) {
			q.setLimit((Limit) query.getLimit().accept(this));
		}
		q.setVersiond(query.isVersiond());
		q.setLocalized(query.isLocalized());

		return q;
	}

	public ASTNode visit(Select select) {
		HintComment hc = null;
		if (select.getHintComment() != null) {
			hc = (HintComment) select.getHintComment().accept(this);
		}
		List<ValueExpression> selectValues = new ArrayList<ValueExpression>();
		if (select.getSelectValues() != null) {
			for (ValueExpression v: select.getSelectValues()) {
				selectValues.add((ValueExpression) v.accept(this));
			}
		}
		Select copy = new Select(select.isDistinct(), selectValues);
		copy.setHintComment(hc);
		return copy;
	}

	public ASTNode visit(Where where) {

		Condition condition = null;
		if (where.getCondition() != null) {
			condition = (Condition) where.getCondition().accept(this);
		}
		return new Where(condition);
	}

	public ASTNode visit(And and) {
		List<Condition> childExpressions = null;
		if (and.getChildExpressions() != null) {
			childExpressions = new ArrayList<Condition>();
			for (Condition c: and.getChildExpressions()) {
				childExpressions.add((Condition) c.accept(this));
			}
		}
		return new And(childExpressions);
	}

	public ASTNode visit(Paren paren) {
		Condition nestedExpression = null;
		if (paren.getNestedExpression() != null) {
			nestedExpression = (Condition) paren.getNestedExpression().accept(this);
		}
		return new Paren(nestedExpression);
	}

	public ASTNode visit(Not not) {
		Condition nestedExpression = null;
		if (not.getNestedExpression() != null) {
			nestedExpression = (Condition) not.getNestedExpression().accept(this);
		}
		return new Not(nestedExpression);
	}

	public ASTNode visit(Or or) {
		List<Condition> childExpressions = null;
		if (or.getChildExpressions() != null) {
			childExpressions = new ArrayList<Condition>();
			for (Condition c: or.getChildExpressions()) {
				childExpressions.add((Condition) c.accept(this));
			}
		}
		return new Or(childExpressions);
	}

	public ASTNode visit(Between between) {
		ValueExpression property = null;
		ValueExpression from = null;
		ValueExpression to = null;
		if (between.getProperty() != null) {
			property = (ValueExpression) between.getProperty().accept(this);
		}
		if (between.getFrom() != null) {
			from = (ValueExpression) between.getFrom().accept(this);
		}
		if (between.getTo() != null) {
			to = (ValueExpression) between.getTo().accept(this);
		}
		return new Between(property, from, to);
	}

	public ASTNode visit(Equals equals) {
		ValueExpression property = null;
		ValueExpression value = null;
		if (equals.getProperty() != null) {
			property = (ValueExpression) equals.getProperty().accept(this);
		}
		if (equals.getValue() != null) {
			value = (ValueExpression) equals.getValue().accept(this);
		}
		return new Equals(property, value);
	}

	public ASTNode visit(Greater greater) {
		ValueExpression property = null;
		ValueExpression value = null;
		if (greater.getProperty() != null) {
			property = (ValueExpression) greater.getProperty().accept(this);
		}
		if (greater.getValue() != null) {
			value = (ValueExpression) greater.getValue().accept(this);
		}
		return new Greater(property, value);
	}

	public ASTNode visit(GreaterEqual greaterEqual) {
		ValueExpression property = null;
		ValueExpression value = null;
		if (greaterEqual.getProperty() != null) {
			property = (ValueExpression) greaterEqual.getProperty().accept(this);
		}
		if (greaterEqual.getValue() != null) {
			value = (ValueExpression) greaterEqual.getValue().accept(this);
		}
		return new GreaterEqual(property, value);
	}

	public ASTNode visit(In in) {
		List<ValueExpression> propertyList = null;
		if (in.getPropertyList() != null) {
			propertyList = new ArrayList<ValueExpression>();
			for (ValueExpression p: in.getPropertyList()) {
				propertyList.add((ValueExpression) p.accept(this));
			}
		}
		if (in.getSubQuery() != null) {
			SubQuery subQuery = (SubQuery) in.getSubQuery().accept(this);
			return new In(propertyList, subQuery);
		} else {
			List<ValueExpression> value = null;
			if (in.getValue() != null) {
				value = new ArrayList<ValueExpression>();
				for (ValueExpression v: in.getValue()) {
					value.add((ValueExpression) v.accept(this));
				}
			}
			In copy = new In();
			copy.setPropertyList(propertyList);
			copy.setValue(value);
			return copy;
		}
	}

	public ASTNode visit(IsNotNull isNotNull) {
		ValueExpression property = null;
		if (isNotNull.getProperty() != null) {
			property = (ValueExpression) isNotNull.getProperty().accept(this);
		}
		return new IsNotNull(property);
	}

	public ASTNode visit(IsNull isNull) {
		ValueExpression property = null;
		if (isNull.getProperty() != null) {
			property = (ValueExpression) isNull.getProperty().accept(this);
		}
		return new IsNull(property);
	}

	public ASTNode visit(Lesser lesser) {
		ValueExpression property = null;
		ValueExpression value = null;
		if (lesser.getProperty() != null) {
			property = (ValueExpression) lesser.getProperty().accept(this);
		}
		if (lesser.getValue() != null) {
			value = (ValueExpression) lesser.getValue().accept(this);
		}
		return new Lesser(property, value);
	}

	public ASTNode visit(LesserEqual lesserEqual) {
		ValueExpression property = null;
		ValueExpression value = null;
		if (lesserEqual.getProperty() != null) {
			property = (ValueExpression) lesserEqual.getProperty().accept(this);
		}
		if (lesserEqual.getValue() != null) {
			value = (ValueExpression) lesserEqual.getValue().accept(this);
		}
		return new LesserEqual(property, value);
	}

	public ASTNode visit(Like like) {
		ValueExpression property = null;
		if (like.getProperty() != null) {
			property = (ValueExpression) like.getProperty().accept(this);
		}
		Literal pattern = null;
		if (like.getPatternAsLiteral() != null) {
			pattern = (Literal) like.getPatternAsLiteral().accept(this);
		}
		return new Like(property, pattern, like.getCaseType());
	}

	public ASTNode visit(NotEquals notEquals) {
		ValueExpression property = null;
		ValueExpression value = null;
		if (notEquals.getProperty() != null) {
			property = (ValueExpression) notEquals.getProperty().accept(this);
		}
		if (notEquals.getValue() != null) {
			value = (ValueExpression) notEquals.getValue().accept(this);
		}
		return new NotEquals(property, value);
	}

	public ASTNode visit(ArrayValue arrayValue) {

		List<ValueExpression> values = null;
		if (arrayValue.getValues() != null) {
			values = new ArrayList<ValueExpression>();
			for (ValueExpression c: arrayValue.getValues()) {
				values.add((ValueExpression) c.accept(this));
			}
		}
		return new ArrayValue(values);
	}
	
	@Override
	public ASTNode visit(RowValueList rowValueList) {
		List<ValueExpression> values = null;
		if (rowValueList.getRowValues() != null) {
			values = new ArrayList<ValueExpression>();
			for (ValueExpression c: rowValueList.getRowValues()) {
				values.add((ValueExpression) c.accept(this));
			}
		}
		return new RowValueList(values);
	}

	public ASTNode visit(OrderBy orderBy) {
		OrderBy copy = new OrderBy();
		if (orderBy.getSortSpecList() != null) {
			for (SortSpec s: orderBy.getSortSpecList()) {
				copy.add((SortSpec) s.accept(this));
			}
		}
		return copy;
	}

	@Override
	public ASTNode visit(GroupBy groupBy) {
		GroupBy copy = new GroupBy();
		if (groupBy.getGroupingFieldList() != null) {
			for (ValueExpression v: groupBy.getGroupingFieldList()) {
				copy.add((ValueExpression) v.accept(this));
			}
		}
		if (groupBy.getRollType() != null) {
			copy.setRollType(groupBy.getRollType());
		}


		return copy;
	}

	@Override
	public ASTNode visit(Limit limit) {
		Limit copy = new Limit(limit.getLimit(), limit.getOffset());
		copy.setBindable(limit.isBindable());
		return copy;
	}

	@Override
	public ASTNode visit(Function function) {
		Function copy = new Function(function.getName());
		if (function.getArguments() != null) {
			ArrayList<ValueExpression> args = new ArrayList<ValueExpression>();
			for (ValueExpression ve: function.getArguments()) {
				args.add((ValueExpression) ve.accept(this));
			}
			copy.setArguments(args);
		}
		return copy;
	}
	
	@Override
	public ASTNode visit(Cast cast) {
		Cast copy = new Cast();
		if (cast.getValue() != null) {
			copy.setValue((ValueExpression) cast.getValue().accept(this));
		}
		copy.setType(cast.getType());
		if (cast.getTypeArguments() != null) {
			copy.setTypeArguments(new ArrayList<>(cast.getTypeArguments().size()));
			for (Integer i: cast.getTypeArguments()) {
				copy.getTypeArguments().add(i);
			}
		}
		return copy;
	}


	@Override
	public ASTNode visit(Refer refer) {
		Refer copy = new Refer();
		if (refer.getReferenceName() != null) {
			copy.setReferenceName((EntityField) refer.getReferenceName().accept(this));
		}
		if (refer.getAsOf() != null) {
			copy.setAsOf((AsOf) refer.getAsOf().accept(this));
		}
		if (refer.getCondition() != null) {
			copy.setCondition((Condition) refer.getCondition().accept(this));
		}
		return copy;
	}

	@Override
	public ASTNode visit(Having having) {
		Having copy = new Having();
		if (having.getCondition() != null) {
			copy.setCondition((Condition) having.getCondition().accept(this));
		}
		return copy;
	}

	@Override
	public ASTNode visit(Contains contains) {
		return new Contains(contains.getSearchText());
	}

	@Override
	public ASTNode visit(Case caseClause) {
		Case copy = new Case();
		if (caseClause.getWhen() != null) {
			ArrayList<When> copyWhen = new ArrayList<When>();
			for (When w: caseClause.getWhen()) {
				copyWhen.add((When) w.accept(this));
			}
			copy.setWhen(copyWhen);
		}
		if (caseClause.getElseClause() != null) {
			copy.setElseClause((Else) caseClause.getElseClause().accept(this));
		}
		return copy;
	}

	@Override
	public ASTNode visit(Else elseClause) {
		Else copy = new Else();
		if (elseClause.getResult() != null) {
			copy.setResult((ValueExpression) elseClause.getResult().accept(this));
		}
		return copy;
	}

	@Override
	public ASTNode visit(When when) {
		When copy = new When();
		if (when.getCondition() != null) {
			copy.setCondition((Condition) when.getCondition().accept(this));
		}
		if (when.getResult() != null) {
			copy.setResult((ValueExpression) when.getResult().accept(this));
		}
		return copy;
	}

	@Override
	public ASTNode visit(StdDevPop stdDevPop) {
		if (stdDevPop.getValue() != null) {
			return new StdDevPop((ValueExpression) stdDevPop.getValue().accept(this));
		} else {
			return new StdDevPop();
		}
	}

	@Override
	public ASTNode visit(StdDevSamp stdDevSamp) {
		if (stdDevSamp.getValue() != null) {
			return new StdDevSamp((ValueExpression) stdDevSamp.getValue().accept(this));
		} else {
			return new StdDevSamp();
		}
	}

	@Override
	public ASTNode visit(VarPop varPop) {
		if (varPop.getValue() != null) {
			return new VarPop((ValueExpression) varPop.getValue().accept(this));
		} else {
			return new VarPop();
		}
	}

	@Override
	public ASTNode visit(VarSamp varSamp) {
		if (varSamp.getValue() != null) {
			return new VarSamp((ValueExpression) varSamp.getValue().accept(this));
		} else {
			return new VarSamp();
		}
	}

	@Override
	public ASTNode visit(Mode mode) {
		if (mode.getValue() != null) {
			return new Mode((ValueExpression) mode.getValue().accept(this));
		} else {
			return new Mode();
		}
	}

	@Override
	public ASTNode visit(Median median) {
		if (median.getValue() != null) {
			return new Median((ValueExpression) median.getValue().accept(this));
		} else {
			return new Median();
		}
	}

	@Override
	public ASTNode visit(HintComment hintComment) {
		HintComment copy = new HintComment();
		if (hintComment.getHintList() != null) {
			for (Hint h: hintComment.getHintList()) {
				copy.add((Hint) h.accept(this));
			}
		}
		return copy;
	}

	@Override
	public ASTNode visit(IndexHint indexHint) {
		if (indexHint.getPropertyNameList() == null) {
			return new IndexHint();
		} else {
			return new IndexHint(new ArrayList<>(indexHint.getPropertyNameList()));
		}
	}

	@Override
	public ASTNode visit(NoIndexHint noIndexHint) {
		if (noIndexHint.getPropertyNameList() == null) {
			return new NoIndexHint();
		} else {
			return new NoIndexHint(new ArrayList<>(noIndexHint.getPropertyNameList()));
		}
	}

	@Override
	public ASTNode visit(NativeHint nativeHint) {
		return new NativeHint(nativeHint.getTable(), nativeHint.getHintExpression());
	}

	@Override
	public ASTNode visit(BindHint bindHint) {
		return new BindHint();
	}

	@Override
	public ASTNode visit(NoBindHint noBindHint) {
		return new NoBindHint();
	}

	@Override
	public ASTNode visit(AsOf asOf) {
		AsOf copy = new AsOf(asOf.getSpec());
		if (asOf.getValue() != null) {
			copy.setValue((ValueExpression) asOf.getValue().accept(this));
		}
		return copy;
	}

	@Override
	public ASTNode visit(CacheHint cacheHint) {
		return new CacheHint(cacheHint.getScope(), cacheHint.getTTL());
	}

	@Override
	public ASTNode visit(FetchSizeHint fetchSizeHint) {
		return new FetchSizeHint(fetchSizeHint.getSize());
	}

	@Override
	public ASTNode visit(TimeoutHint timeoutHint) {
		return new TimeoutHint(timeoutHint.getSeconds());
	}

	@Override
	public ASTNode visit(WindowAggregate windowAggregateFunction) {
		WindowAggregate copy = new WindowAggregate();
		if (windowAggregateFunction.getAggregate() != null) {
			copy.setAggregate((Aggregate) windowAggregateFunction.getAggregate().accept(this));
		}
		handleOver(copy, windowAggregateFunction);
		return copy;
	}
	
	private void handleOver(WindowFunction copy, WindowFunction org) {
		if (org.getPartitionBy() != null) {
			copy.setPartitionBy((PartitionBy) org.getPartitionBy().accept(this));
		}
		if (org.getOrderBy() != null) {
			copy.setOrderBy((WindowOrderBy) org.getOrderBy().accept(this));
		}
	}

	@Override
	public ASTNode visit(RowNumber rowNumber) {
		RowNumber copy = new RowNumber();
		handleOver(copy, rowNumber);
		return copy;
	}

	@Override
	public ASTNode visit(Rank rank) {
		Rank copy = new Rank();
		handleOver(copy, rank);
		return copy;
	}

	@Override
	public ASTNode visit(DenseRank denseRank) {
		DenseRank copy = new DenseRank();
		handleOver(copy, denseRank);
		return copy;
	}

	@Override
	public ASTNode visit(PercentRank percentRank) {
		PercentRank copy = new PercentRank();
		handleOver(copy, percentRank);
		return copy;
	}

	@Override
	public ASTNode visit(CumeDist cumeDist) {
		CumeDist copy = new CumeDist();
		handleOver(copy, cumeDist);
		return copy;
	}

	@Override
	public ASTNode visit(PartitionBy partitionBy) {
		PartitionBy copy = new PartitionBy();
		if (partitionBy.getPartitionFieldList() != null) {
			ArrayList<ValueExpression> list = new ArrayList<>();
			for (ValueExpression v: partitionBy.getPartitionFieldList()) {
				list.add((ValueExpression) v.accept(this));
			}
			copy.setPartitionFieldList(list);
		}
		return copy;
	}

	@Override
	public ASTNode visit(WindowOrderBy orderBy) {
		WindowOrderBy copy = new WindowOrderBy();
		if (orderBy.getSortSpecList() != null) {
			for (WindowSortSpec s: orderBy.getSortSpecList()) {
				copy.add((WindowSortSpec) s.accept(this));
			}
		}
		return copy;
	}

	@Override
	public ASTNode visit(WindowSortSpec sortSpec) {
		ValueExpression ve = null;
		if (sortSpec.getSortKey() != null) {
			ve = (ValueExpression) sortSpec.getSortKey().accept(this);
		}
		return new WindowSortSpec(ve, sortSpec.getType(), sortSpec.getNullOrderingSpec());
	}

}
